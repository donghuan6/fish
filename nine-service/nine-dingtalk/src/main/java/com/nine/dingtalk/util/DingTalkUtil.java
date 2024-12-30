package com.nine.dingtalk.util;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.aliyun.dingtalkoauth2_1_0.models.GetTokenRequest;
import com.aliyun.dingtalkoauth2_1_0.models.GetTokenResponse;
import com.aliyun.dingtalkoauth2_1_0.models.GetTokenResponseBody;
import com.aliyun.dingtalkstorage_1_0.models.*;
import com.aliyun.teautil.models.RuntimeOptions;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.nine.common.constans.Redis;
import com.nine.common.ex.ServiceException;
import com.nine.dingtalk.config.DingTalkConfig;
import com.taobao.api.ApiException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import shade.com.alibaba.fastjson2.JSON;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Getter
public class DingTalkUtil {

    private static final Lock LOCK = new ReentrantLock();
    // 从bean容器中获取，否则直接创建对象
    private static final DingTalkConfig DINGTALK_CONFIG;
    private static final com.aliyun.teaopenapi.models.Config CONFIG;
    private static final com.aliyun.dingtalkoauth2_1_0.Client OAUTH_CLIENT;
    private static final StringRedisTemplate STRING_REDIS_TEMPLATE;

    public static com.aliyun.dingtalkstorage_1_0.Client STORAGE_CLIENT_1;
    public static com.aliyun.dingtalkstorage_2_0.Client STORAGE_CLIENT_2;

    static {
        STRING_REDIS_TEMPLATE = Optional.ofNullable(SpringUtil.getBeanFactory())
                .map(beanFactory -> beanFactory.getBean(StringRedisTemplate.class))
                .orElse(null);
        DINGTALK_CONFIG = Optional.ofNullable(SpringUtil.getBeanFactory())
                .map(beanFactory -> beanFactory.getBean(DingTalkConfig.class))
                .orElse(new DingTalkConfig());
        CONFIG = getConfig();
        OAUTH_CLIENT = getOauthClient();

        STORAGE_CLIENT_1 = createStorageClient_1();
        STORAGE_CLIENT_2 = createStorageClient_2();
    }

    private static com.aliyun.teaopenapi.models.Config getConfig() {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config();
        config.protocol = "https";
        config.regionId = "central";
        return config;
    }

    private static com.aliyun.dingtalkoauth2_1_0.Client getOauthClient() {
        try {
            return new com.aliyun.dingtalkoauth2_1_0.Client(CONFIG);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static com.aliyun.dingtalkstorage_1_0.Client createStorageClient_1() {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config();
        config.protocol = "https";
        config.regionId = "central";
        try {
            return new com.aliyun.dingtalkstorage_1_0.Client(config);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static com.aliyun.dingtalkstorage_2_0.Client createStorageClient_2() {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config();
        config.protocol = "https";
        config.regionId = "central";
        try {
            return new com.aliyun.dingtalkstorage_2_0.Client(config);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取Access Token
     * https://open-dev.dingtalk.com/apiExplorer?spm=ding_open_doc.document.0.0.44ac7176vDlPJ9#/?devType=org&api=oauth2_1.0%23GetToken
     * <p>
     * POST /v1.0/oauth2/{corpId}/token HTTP/1.1
     * Host:api.dingtalk.com
     * x-acs-dingtalk-access-token:String
     * Content-Type:application/json
     * <p>
     * {
     * "client_id" : "ding123",
     * "client_secret" : "*******",
     * "grant_type" : "client_credentials"
     * }
     * 返回：
     * token: accessToken;
     * 过期时间,单位秒: expiresIn;
     */
    public static String getToken() {
        String cacheToken = getCacheToken();
        if (Objects.nonNull(cacheToken)) {
            return cacheToken;
        }
        LOCK.lock();
        try {
            // 再次检查缓存，防止其他线程已经获取并设置 Token
            cacheToken = getCacheToken();
            if (Objects.nonNull(cacheToken)) {
                return cacheToken;
            }
            GetTokenRequest req = new GetTokenRequest();
            req.setClientId(DINGTALK_CONFIG.getAgentId().toString());
            req.setClientSecret(DINGTALK_CONFIG.getAppSecret());
            req.setGrantType("client_credentials");
            GetTokenResponse rsp;
            try {
                rsp = OAUTH_CLIENT.getToken(DINGTALK_CONFIG.getCorpId(), req);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            if (Objects.equals(HttpStatus.OK.value(), rsp.getStatusCode())) {
                GetTokenResponseBody body = rsp.getBody();
                STRING_REDIS_TEMPLATE.opsForValue().set(Redis.DINGTALK_TOKEN_PREFIX, body.getAccessToken(), body.getExpiresIn(), TimeUnit.SECONDS);
                return body.getAccessToken();
            }
            log.error("[钉钉-获取应用token-异常] rsp:{}", JSONUtil.toJsonStr(rsp));
            throw new ServiceException("钉钉-获取应用token-异常");
        } finally {
            LOCK.unlock();
        }
    }

    private static String getCacheToken() {
        return Optional.ofNullable(STRING_REDIS_TEMPLATE)
                .map(r -> r.opsForValue().get(Redis.DINGTALK_TOKEN_PREFIX))
                .orElse(null);
    }

    /**
     * 员工在当前开发者企业账号范围内的唯一标识。
     * https://open-dev.dingtalk.com/apiExplorer?spm=ding_open_doc.document.0.0.44ac7176vDlPJ9#/?devType=org&api=dingtalk.oapi.v2.user.get
     */
    public static String getUnionid(String userId) {
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/user/get");
            OapiV2UserGetRequest req = new OapiV2UserGetRequest();
            req.setUserid(userId);
            req.setLanguage("zh_CN");
            OapiV2UserGetResponse rsp = client.execute(req, getToken());
            if (rsp.isSuccess()) {
                return rsp.getResult().getUnionid();
            }
            log.error("[钉钉-获取用户unionid-异常] rsp:{}", JSONUtil.toJsonStr(rsp));
            throw new ServiceException("钉钉-获取用户unionid-异常");
        } catch (ApiException e) {
            log.error("[钉钉-获取用户unionid-异常] ", e);
            throw new ServiceException(e);
        }
    }

    /**
     * 上传文件
     * https://open-dev.dingtalk.com/apiExplorer?spm=ding_open_doc.document.0.0.44ac7176vDlPJ9#/?devType=org&api=storage_1.0%23GetFileUploadInfo
     */
    public static Map<String, String> upFile(Path filePath) {
        // 1.获取文件上传信息
        GetFileUploadInfoResponseBody fileUploadInfo = getFileUploadInfo();
        // 2.使用OSS的header加签方式上传文件
        upFile(filePath, fileUploadInfo);
        // 3.提交文件，完成上传
        return commitFile(filePath, fileUploadInfo);
    }

    /**
     * 提交文件，完成上传
     */
    private static Map<String, String> commitFile(Path filePath,
                                                  com.aliyun.dingtalkstorage_1_0.models.GetFileUploadInfoResponseBody fileUploadInfo) {
        CommitFileResponse rsp;
        try {
            rsp = STORAGE_CLIENT_1.commitFileWithOptions(
                    getSpaceId().toString(),
                    new CommitFileRequest()
                            .setUnionId(getUnionid(DINGTALK_CONFIG.getDefaultUserId()))
                            .setName(filePath.getFileName().toString())
                            .setUploadKey(fileUploadInfo.getUploadKey())
                            .setParentId("0"),
                    new CommitFileHeaders().setXAcsDingtalkAccessToken(getToken()),
                    new RuntimeOptions());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (Objects.equals(200, rsp.statusCode)) {
            CommitFileResponseBody.CommitFileResponseBodyDentry fileData = rsp.getBody().getDentry();
            Map<String, String> res = new HashMap<>();
            res.put("spaceId", fileData.getSpaceId());
            res.put("fileName", fileData.getName());
            res.put("fileSize", fileData.getSize().toString());
            res.put("fileType", fileData.getExtension());
            res.put("fileId", fileData.getId());
            return res;
        }
        log.error("[钉盘-提交文件-异常] rsp:{}", JSONUtil.toJsonStr(rsp));
        throw new ServiceException("钉盘-提交文件-异常");
    }

    /**
     * 上传文件
     */
    private static void upFile(Path filePath,
                               com.aliyun.dingtalkstorage_1_0.models.GetFileUploadInfoResponseBody fileUploadInfo) {
        HttpURLConnection connection = getFileUploadHttpURLConnection(fileUploadInfo);
        try (OutputStream out = connection.getOutputStream();
             FileInputStream is = new FileInputStream(filePath.toFile())
        ) {
            byte[] b = new byte[1024];
            int temp;
            while ((temp = is.read(b)) != -1) {
                out.write(b, 0, temp);
            }
            int responseCode = connection.getResponseCode();
            connection.disconnect();
            if (responseCode != 200) {
                log.error("[钉盘-上传文件-异常] filePath:{}, fileUploadInfo:{}, connection:{}", filePath, JSON.toJSONString(fileUploadInfo), connection);
                throw new RuntimeException(connection.getResponseMessage());
            }
        } catch (IOException e) {
            log.error("[钉盘-上传文件-异常] filePath:{}, fileUploadInfo:{}", filePath, JSONUtil.toJsonStr(fileUploadInfo), e);
            throw new ServiceException("钉盘-上传文件-异常");
        }
    }

    private static HttpURLConnection getFileUploadHttpURLConnection(com.aliyun.dingtalkstorage_1_0.models.GetFileUploadInfoResponseBody fileUploadInfo) {
        List<String> resourceUrls = fileUploadInfo.getHeaderSignatureInfo().getResourceUrls();
        Map<String, String> headers = fileUploadInfo.getHeaderSignatureInfo().getHeaders();
        String resourceUrl = resourceUrls.get(0);
        try {
            URL url = new URL(resourceUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    connection.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            connection.setUseCaches(false);
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(10000);
            connection.connect();
            return connection;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取审批钉盘空间id
     */
    private static Long getSpaceId() {
        DefaultDingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/processinstance/cspace/info");
        OapiProcessinstanceCspaceInfoRequest request = new OapiProcessinstanceCspaceInfoRequest();
        request.setAgentId(DINGTALK_CONFIG.getAgentId());
        request.setUserId(DINGTALK_CONFIG.getDefaultUserId());
        OapiProcessinstanceCspaceInfoResponse rsp;
        try {
            rsp = client.execute(request, getToken());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (rsp.isSuccess()) {
            return rsp.getResult().getSpaceId();
        }
        log.error("[钉钉-获取审批钉盘空间id-异常] rsp:{}", JSONUtil.toJsonStr(rsp));
        throw new ServiceException("钉钉-获取审批钉盘空间id-异常");
    }

    /**
     * 获取上传文件存储信息
     */
    private static com.aliyun.dingtalkstorage_1_0.models.GetFileUploadInfoResponseBody getFileUploadInfo() {
        GetFileUploadInfoResponse rsp;
        try {
            RuntimeOptions runtimeOptions = new RuntimeOptions();
            rsp = STORAGE_CLIENT_1.getFileUploadInfoWithOptions(
                    getSpaceId().toString(),
                    new GetFileUploadInfoRequest()
                            .setUnionId(getUnionid(DINGTALK_CONFIG.getDefaultUserId()))
                            .setProtocol("HEADER_SIGNATURE")
                            .setMultipart(false),
                    new GetFileUploadInfoHeaders().setXAcsDingtalkAccessToken(getToken()),
                    runtimeOptions
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (Objects.equals(200, rsp.getStatusCode())) {
            return rsp.getBody();
        }
        log.error("[钉钉-获取上传文件存储信息-异常] rsp:{}", JSONUtil.toJsonStr(rsp));
        throw new ServiceException("钉钉-获取上传文件存储信息-异常");
    }

    /**
     * 钉盘-下载审批附件
     */
    public static String downFile(String fileId, String processInstanceId) {
        DefaultDingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/processinstance/file/url/get");
        OapiProcessinstanceFileUrlGetRequest req = new OapiProcessinstanceFileUrlGetRequest();
        OapiProcessinstanceFileUrlGetRequest.GrantCspaceRequest obj = new OapiProcessinstanceFileUrlGetRequest.GrantCspaceRequest();
        obj.setFileId(fileId);
        obj.setProcessInstanceId(processInstanceId);
        req.setRequest(obj);
        OapiProcessinstanceFileUrlGetResponse rsp;
        try {
            rsp = client.execute(req, getToken());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (rsp.isSuccess()) {
            return rsp.getResult().getDownloadUri();
        }
        log.error("[钉盘-下载审批附件-异常], fileId:{}, processInstanceId:{}, rsp:{}", fileId, processInstanceId, JSONUtil.toJsonStr(rsp));
        throw new ServiceException("钉盘-下载审批附件-异常: " + rsp.getErrmsg());
    }

    /**
     * 发起审批
     */
    public static String startProcess(OapiProcessinstanceCreateRequest request) {
        DefaultDingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/processinstance/create");
        OapiProcessinstanceCreateResponse rsp;
        try {
            rsp = client.execute(request, getToken());
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
        if (rsp.isSuccess()) {
            return rsp.getProcessInstanceId();
        }
        log.error("[钉钉-发起审批实例-异常] req:{}, rsp:{}", JSONUtil.toJsonStr(request), JSONUtil.toJsonStr(rsp));
        throw new ServiceException(rsp.getMessage());
    }

    /**
     * 获取审批模板code
     */
    public static String getProcessCode(String processName) {
        DefaultDingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/process/get_by_name");
        OapiProcessGetByNameRequest req = new OapiProcessGetByNameRequest();
        req.setName(processName);
        OapiProcessGetByNameResponse rsp;
        try {
            rsp = client.execute(req, getToken());
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
        if (rsp.isSuccess()) {
            return rsp.getProcessCode();
        }
        log.error("[钉钉-获取审批模板code-异常] processName:{}, rsp:{}", processName, JSONUtil.toJsonStr(rsp));
        throw new ServiceException("钉钉-获取审批模板code-异常: " + rsp.getErrmsg());
    }

}
