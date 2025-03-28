package com.nine.log.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nine.common.context.ContextHolder;
import com.nine.common.domain.R;
import com.nine.common.ex.ServiceException;
import com.nine.common.utils.servlet.ServletUtil;
import com.nine.log.annotation.SysLog;
import com.nine.log.filter.LogSerializer;
import com.nine.log.service.AsyncSysLogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

@Slf4j
@Aspect
@Component
public class SysLogAspect {

    public static final String[] EXCLUDE_PROPERTIES = {"password"};

    @Autowired
    private AsyncSysLogService asyncSysLogService;

    @Around("@annotation(sysLog)")
    public Object logAround(ProceedingJoinPoint point, SysLog sysLog) {
        long start = System.currentTimeMillis();
        HttpServletRequest request = ServletUtil.getRequest();
        com.nine.log.domain.SysLog logEntity = new com.nine.log.domain.SysLog();
        logEntity.setServiceName(sysLog.service());
        logEntity.setTitle(sysLog.title());
        logEntity.setMethod(request.getMethod());
        logEntity.setClassMethod(point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName() + "()");
        logEntity.setUrl(request.getRequestURI());
        logEntity.setUsername(ContextHolder.getUsername());
        logEntity.setIp(ServletUtil.getIP());
        // 需要被过滤的参数
        Set<String> excludes = getAllExcludeProperties(sysLog.exclude());
        LogSerializer logSerializer = new LogSerializer(excludes);
        try {
            // 前置处理，请求参数
            if (sysLog.saveRequest()) {
                Map<String, Object> args = requestParams(point);
                logEntity.setParams(processRequestParams(logSerializer.getMapper(), args));
            }
            // 处理业务调用
            Object result = point.proceed();
            // 后置处理，响应参数
            if (sysLog.saveResponse()) {
                logEntity.setResult(truncateStr(processResponseData(logSerializer.getMapper(), result)));
            }
            logEntity.setOkStatus();
            if (result instanceof R<?> && ((R<?>) result).isFail()) {
                String errMsg = ((R<?>) result).getMsg();
                logEntity.setFailStatus().setErrorMsg(truncateStr(errMsg));
            } else if (result instanceof ResponseEntity<?> && !((ResponseEntity<?>) result).getStatusCode().is2xxSuccessful()) {
                String res = ((ResponseEntity<?>) result).toString();
                logEntity.setFailStatus().setErrorMsg(truncateStr(res));
            }
            return result;
        } catch (Throwable e) {
            String str = truncateStr(e.getMessage() != null ? e.getMessage() : e.toString());
            logEntity.setFailStatus().setErrorMsg(str);
            log.error("切面记录日志异常 logEntity={}", logEntity, e);
            throw new ServiceException(e.getMessage(), e);
        } finally {
            logEntity.setCostTime(System.currentTimeMillis() - start);
            asyncSysLogService.saveLog(logEntity);
        }
    }

    private Map<String, Object> requestParams(ProceedingJoinPoint point) {
        Map<String, Object> map = new LinkedHashMap<>();
        MethodSignature signature = (MethodSignature) point.getSignature();
        String[] parameterNames = signature.getParameterNames();
        Object[] args = point.getArgs();
        for (int i = 0, parameterNamesLength = parameterNames.length; i < parameterNamesLength; i++) {
            String parameterName = parameterNames[i];
            map.put(parameterName, args[i]);
        }
        return map;
    }

    // 响应-->>转json
    private String processResponseData(ObjectMapper mapper, Object result) {
        if (result == null) {
            return "";
        }
        if (result instanceof InputStream || result instanceof OutputStream) {
            return "InputStream | OutputStream - data";
        }
        if (result instanceof ResponseEntity<?> && ((ResponseEntity<?>) result).getBody() instanceof InputStream) {
            return "ResponseEntity - data";
        }
        try {
            return mapper.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            log.error("响应 --> 转json 异常 result={}", result, e);
            return "响应 --> 转json 异常: " + e.getMessage();
        }
    }

    // 请求参数-->>转json
    private String processRequestParams(ObjectMapper mapper, Map<String, Object> args) {
        if (args == null) {
            return "";
        }
        Map<String, Object> argsMap = new LinkedHashMap<>();
        args.forEach((k, v) -> {
            List<Object> fileDetails = new ArrayList<>();
            if (v instanceof MultipartFile f) {
                fileDetails.add(fileMeta(f));
            } else if (v instanceof MultipartFile[] fs) {
                Arrays.stream(fs).map(this::fileMeta).forEach(fileDetails::add);
            } else if (isSimpleType(v.getClass())) {
                argsMap.put(k, v);
            } else {
                Map map = mapper.convertValue(v, Map.class);
                argsMap.put(k, map);
            }
            if (!fileDetails.isEmpty()) {
                argsMap.put("upload_files", fileDetails);
            }
        });
        try {
            return mapper.writeValueAsString(argsMap);
        } catch (Exception e) {
            log.error("请求参数 --> 转json 异常 argsMap={}", argsMap, e);
            return "请求参数 --> 转json 异常: " + e.getMessage();
        }
    }

    private boolean isSimpleType(Class<?> clazz) {
        return clazz.isPrimitive()
                || clazz == String.class
                || Number.class.isAssignableFrom(clazz)
                || Boolean.class == clazz
                || Date.class.isAssignableFrom(clazz);

    }

    private Map<String, Object> fileMeta(MultipartFile f) {
        return Map.of(
                "filename", f.getOriginalFilename(),
                "size", f.getSize(),
                "type", f.getContentType());
    }

    private String truncateStr(String input) {
        return input.length() > 2000 ? input.substring(0, 2000) : input;
    }

    private Set<String> getAllExcludeProperties(String[] exclude) {
        Set<String> excludeSet = new HashSet<>(Arrays.asList(exclude));
        excludeSet.addAll(Arrays.asList(EXCLUDE_PROPERTIES));
        return excludeSet;
    }


}
