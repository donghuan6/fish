package com.nine.log.aspect;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.nine.common.constans.TableStatus;
import com.nine.common.context.ContextHolder;
import com.nine.common.utils.servlet.ServletUtil;
import com.nine.log.annotation.Log;
import com.nine.log.service.AsyncLogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.beans.Introspector;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Aspect
@Component
public class LogAspect {

    public static final String[] EXCLUDE_PROPERTIES = {"password"};

    private static final ThreadLocal<Long> TIME_THREAD_LOCAL = new NamedThreadLocal<>("cost time");

    @Autowired
    private AsyncLogService asyncLogService;

    /**
     * 方法执行前
     */
    @Before(value = "@annotation(log)")
    public void before(JoinPoint point, Log log) {
        TIME_THREAD_LOCAL.set(System.currentTimeMillis());
    }

    /**
     * 处理完成请求后执行
     */
    @AfterReturning(value = "@annotation(log)", returning = "jsonResult")
    public void afterReturning(JoinPoint point, Log log, Object jsonResult) {
        handlerLog(point, log, jsonResult, null);
    }


    /**
     * 异常时
     */
    @AfterThrowing(value = "@annotation(log)", throwing = "e")
    public void afterThrowing(JoinPoint point, Log log, Exception e) {
        handlerLog(point, log, null, e);
    }

    /**
     * 记录日志
     */
    private void handlerLog(JoinPoint point, Log logAnno, Object jsonResult, Exception e) {
        try {
            HttpServletRequest request = ServletUtil.getRequest();
            com.nine.log.dao.Log log = new com.nine.log.dao.Log();
            log.setServiceName(logAnno.service());
            log.setTitle(logAnno.title());
            log.setMethod(request.getMethod());
            log.setClassMethod(point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName() + "()");
            log.setUrl(request.getRequestURI());
            log.setUsername(ContextHolder.getUsername());
            log.setIp(ServletUtil.getIP());
            log.setStatus(TableStatus.ResultStatus.SUCCESS);
            // 设置请求参数，返回结果
            log.setParams(handleParams(point, request, logAnno.exclude()));
            log.setResult(handleResult(jsonResult));
            // 异常处理
            if (Objects.nonNull(e)) {
                log.setStatus(TableStatus.ResultStatus.FAIL);
                log.setErrorMsg(truncateErrorMsg(e));
            }
            log.setCostTime(System.currentTimeMillis() - TIME_THREAD_LOCAL.get());
            asyncLogService.saveLog(log);
        } finally {
            TIME_THREAD_LOCAL.remove();
        }
    }

    private String handleResult(Object jsonResult) {
        if (jsonResult == null) return "";
        return truncateStr(JSONUtil.toJsonStr(jsonResult), 2000);
    }

    private String truncateErrorMsg(Exception e) {
        String msg = e.getMessage() != null ? e.getMessage() : e.toString();
        return truncateStr(msg, 2000);
    }

    private String truncateStr(String input, int maxLen) {
        return input.length() > maxLen ? input.substring(0, maxLen) : input;
    }

    private String handleParams(JoinPoint point, HttpServletRequest request, String[] exclude) {
        String params = "";
        Map<String, String> paramMap = ServletUtil.getParams();
        if (CollUtil.isNotEmpty(paramMap)) {
            params = JSONUtil.toJsonStr(paramMap);
        }
        // 处理 body
        if (isBodyRequest(request)) {
            String bodyParams = processBodyArgs(point.getArgs(), exclude);
            params = mergeParams(params, bodyParams);
        }
        return truncateStr(params, 5000);
    }

    /**
     * 合并 get 与 body 请求参数
     */
    private String mergeParams(String params, String bodyParams) {
        if (StrUtil.isNotBlank(params) && StrUtil.isNotBlank(bodyParams)) {
            return params + "--" + bodyParams;
        }
        return StrUtil.isNotBlank(params) ? params : bodyParams;
    }

    private boolean isBodyRequest(HttpServletRequest request) {
        HttpMethod method = HttpMethod.valueOf(request.getMethod().toUpperCase());
        return Arrays.asList(HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE).contains(method);
    }

    /**
     * 将参数数组转为字符串
     */
    private String processBodyArgs(Object[] args, String[] exclude) {
        if (args == null || args.length == 0) return "";
        Set<String> allExcludeProperties = getAllExcludeProperties(exclude);
        List<Object> processedArgs = Arrays.stream(args)
                .filter(Objects::nonNull)
                .map(arg -> filterSensitiveFields(arg, allExcludeProperties))
                .collect(Collectors.toList());
        return JSONUtil.toJsonStr(processedArgs);
    }

    private Set<String> getAllExcludeProperties(String[] exclude) {
        Set<String> excludeSet = new HashSet<>(Arrays.asList(exclude));
        excludeSet.addAll(Arrays.asList(EXCLUDE_PROPERTIES));
        return excludeSet;
    }

    /**
     * 过滤敏感字段
     * 处理不能序列化的对象，例如：multipartFile, inputStream 等
     */
    private Object filterSensitiveFields(Object arg, Set<String> allExcludeProperties) {
        if (arg instanceof MultipartFile) {
            return "File: " + ((MultipartFile) arg).getOriginalFilename();
        }
        if (arg instanceof InputStream || arg instanceof OutputStream) {
            return "Stream Data (not logged)";
        }
        if (arg instanceof Map) {
            Map<Object, Object> filterMap = new LinkedHashMap<>();
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) arg).entrySet()) {
                Object key = entry.getKey();
                // 过滤敏感字段
                if (allExcludeProperties.stream().noneMatch(e -> e.equals(key))) {
                    Object filteredValue = filterSensitiveFields(entry.getValue(), allExcludeProperties);
                    filterMap.put(key, filteredValue);
                }
            }
            return filterMap;
        }
        if (arg instanceof Collection<?>) {
            return ((Collection<?>) arg).stream()
                    .map(e -> filterSensitiveFields(e, allExcludeProperties))
                    .collect(Collectors.toList());
        }
        if (arg != null && arg.getClass().isArray()) {
            return Arrays.stream((Object[]) arg)
                    .map(e -> filterSensitiveFields(e, allExcludeProperties))
                    .toArray();
        }
        if (isSimpleValueType(arg.getClass())) {
            return arg;
        }
        return filterPojoFields(arg, allExcludeProperties);
    }

    private Object filterPojoFields(Object arg, Set<String> allExcludeProperties) {
        Map<String, Object> filterMap = new LinkedHashMap<>();
        for (Method method : arg.getClass().getMethods()) {
            String methodName = method.getName();
            if (methodName.startsWith("get") && method.getParameterCount() == 0) {
                String fieldName = Introspector.decapitalize(methodName.substring(3));
                if (allExcludeProperties.contains(fieldName)) {
                    continue;
                }
                Object fieldValue;
                try {
                    fieldValue = method.invoke(arg);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    log.error("Error invoking getter method: class={}, methodName={}", arg.getClass(), methodName, e);
                    throw new RuntimeException(e);
                }
                if (fieldValue != null) {
                    Object filteredValue = filterSensitiveFields(fieldValue, allExcludeProperties);
                    filterMap.put(fieldName, filteredValue);
                }
            }
        }
        return filterMap;
    }

    // 是否为基本数据类型
    private boolean isSimpleValueType(Class<?> clazz) {
        return clazz.isPrimitive() ||
                Character.class.isAssignableFrom(clazz) ||
                Number.class.isAssignableFrom(clazz) ||
                clazz == Boolean.class ||
                clazz == Date.class ||
                clazz == String.class;
    }


}
