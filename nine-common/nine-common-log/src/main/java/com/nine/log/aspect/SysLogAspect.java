package com.nine.log.aspect;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.nine.common.constans.TableStatus;
import com.nine.common.context.ContextHolder;
import com.nine.common.utils.servlet.ServletUtil;
import com.nine.log.annotation.SysLog;
import com.nine.log.service.AsyncSysLogService;
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
public class SysLogAspect {

    public static final String[] EXCLUDE_PROPERTIES = {"password"};

    private static final ThreadLocal<Long> TIME_THREAD_LOCAL = new NamedThreadLocal<>("cost time");

    @Autowired
    private AsyncSysLogService asyncSysLogService;

    /**
     * 方法执行前
     */
    @Before(value = "@annotation(sysLog)")
    public void before(JoinPoint point, SysLog sysLog) {
        TIME_THREAD_LOCAL.set(System.currentTimeMillis());
    }

    /**
     * 处理完成请求后执行
     */
    @AfterReturning(value = "@annotation(sysLog)", returning = "jsonResult")
    public void afterReturning(JoinPoint point, SysLog sysLog, Object jsonResult) {
        handlerLog(point, sysLog, jsonResult, null);
    }


    /**
     * 异常时
     */
    @AfterThrowing(value = "@annotation(sysLog)", throwing = "e")
    public void afterThrowing(JoinPoint point, SysLog sysLog, Exception e) {
        handlerLog(point, sysLog, null, e);
    }

    /**
     * 记录日志
     */
    private void handlerLog(JoinPoint point, SysLog sysLogAnno, Object jsonResult, Exception e) {
        try {
            HttpServletRequest request = ServletUtil.getRequest();
            com.nine.log.dao.SysLog sysLog = new com.nine.log.dao.SysLog();
            sysLog.setServiceName(sysLogAnno.service());
            sysLog.setTitle(sysLogAnno.title());
            sysLog.setMethod(request.getMethod());
            sysLog.setClassMethod(point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName() + "()");
            sysLog.setUrl(request.getRequestURI());
            sysLog.setUsername(ContextHolder.getUsername());
            sysLog.setIp(ServletUtil.getIP());
            sysLog.setStatus(TableStatus.ResultStatus.SUCCESS);
            // 设置请求参数，返回结果
            sysLog.setParams(handleParams(point, request, sysLogAnno.exclude()));
            sysLog.setResult(handleResult(jsonResult));
            // 异常处理
            if (Objects.nonNull(e)) {
                sysLog.setStatus(TableStatus.ResultStatus.FAIL);
                sysLog.setErrorMsg(truncateErrorMsg(e));
            }
            sysLog.setCostTime(System.currentTimeMillis() - TIME_THREAD_LOCAL.get());
            asyncSysLogService.saveLog(sysLog);
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

    /**
     * 过滤POJO对象的字段，排除指定的敏感字段，并返回过滤后的字段映射。
     * 该函数通过反射获取POJO对象的所有getter方法，提取字段名和字段值，并排除在`allExcludeProperties`集合中指定的字段。
     * 对于非空的字段值，递归调用`filterSensitiveFields`方法进行进一步过滤。
     *
     * @param arg                  需要过滤的POJO对象
     * @param allExcludeProperties 需要排除的字段名集合
     * @return 过滤后的字段名与字段值的映射，类型为`Map<String, Object>`
     */
    private Object filterPojoFields(Object arg, Set<String> allExcludeProperties) {
        // 使用LinkedHashMap保持字段顺序
        Map<String, Object> filterMap = new LinkedHashMap<>();
        // 遍历POJO对象的所有方法
        for (Method method : arg.getClass().getMethods()) {
            String methodName = method.getName();
            // 检查方法是否为无参的getter方法
            if (methodName.startsWith("get") && method.getParameterCount() == 0) {
                // 提取字段名，去掉"get"前缀并首字母小写
                String fieldName = Introspector.decapitalize(methodName.substring(3));
                // 如果字段在排除列表中，跳过该字段
                if (allExcludeProperties.contains(fieldName)) {
                    continue;
                }
                // 通过反射调用getter方法获取字段值
                Object fieldValue;
                try {
                    fieldValue = method.invoke(arg);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    log.error("Error invoking getter method: class={}, methodName={}", arg.getClass(), methodName, e);
                    throw new RuntimeException(e);
                }
                // 如果字段值不为空，递归过滤敏感字段并存入映射
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
