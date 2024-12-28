package com.nine.dingtalk.util;

import cn.hutool.extra.spring.SpringUtil;
import com.aliyun.dingtalkoauth2_1_0.models.GetTokenRequest;
import com.aliyun.dingtalkoauth2_1_0.models.GetTokenResponse;
import com.nine.dingtalk.config.DingTalkConfig;
import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.Objects;
import java.util.Optional;

@Getter
public class DingTalkUtil {

    // 从bean容器中获取，否则直接创建对象
    private static final DingTalkConfig DINGTALK_CONFIG;
    private static final com.aliyun.teaopenapi.models.Config CONFIG;
    private static final com.aliyun.dingtalkoauth2_1_0.Client OAUTH_CLIENT;

    static {
        DINGTALK_CONFIG = Optional.ofNullable(SpringUtil.getBeanFactory())
                .map(beanFactory -> beanFactory.getBean(DingTalkConfig.class))
                .orElse(new DingTalkConfig());
        CONFIG = getConfig();
        OAUTH_CLIENT = getOauthClient();
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
     */
    public static void getToken() {
        GetTokenRequest req = new GetTokenRequest();
        req.setClientId(DINGTALK_CONFIG.getAgentId());
        req.setClientSecret(DINGTALK_CONFIG.getAppSecret());
        req.setGrantType("client_credentials");
        GetTokenResponse rsp = null;
        try {
            rsp = OAUTH_CLIENT.getToken(DINGTALK_CONFIG.getCorpId(), req);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (Objects.equals(HttpStatus.OK.value(), rsp.getStatusCode())) {

        }
    }


}
