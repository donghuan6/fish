package com.nine.common.handler;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.nine.common.domain.R;
import com.nine.common.ex.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.WebUtils;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author fan
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 从 servlet 获取请求的详细信息,包括请求头，请求参数
     */
    private String getRequestDetails() {
        HttpServletRequest request = Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .map(i -> (ServletRequestAttributes) i)
                .map(ServletRequestAttributes::getRequest)
                .orElse(null);
        if (Objects.isNull(request)) {
            return "";
        }
        String headers = Collections.list(request.getHeaderNames())
                .stream()
                .map(i -> String.join(": ", i, request.getHeader(i)))
                .collect(Collectors.joining("\n\t"));
        String params = MapUtil.emptyIfNull(request.getParameterMap())
                .entrySet()
                .stream()
                .map(en -> String.join(": ",
                        en.getKey(),
                        en.getValue() != null && en.getValue().length == 1
                                ? en.getValue()[0] : Arrays.toString(en.getValue())))
                .collect(Collectors.joining("\n\t"));
        String body = Optional.ofNullable(WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class))
                .map(i -> new String(i.getContentAsByteArray(), Charset.forName(i.getCharacterEncoding())))
                .orElse("");
        return String.format(
                """
                        URL: %s
                        Method: %s
                        Headers: %s
                        Params: %s
                        Body: %s
                        """,
                request.getRequestURL(), request.getMethod(), headers, params, body);
    }

    @ExceptionHandler(ServiceException.class)
    public Object handlerServiceException(ServiceException e, HandlerMethod method) {
        log.error("业务异常：{}", getRequestDetails(), e);
        // 由于非 json 响应也需要记录日志，使用 method 进行区分
        if (ResponseEntity.class.isAssignableFrom(method.getMethod().getReturnType())) {
            String jsonStr = JSONUtil.toJsonStr(R.fail(e.getMessage()));
            return ResponseEntity.ok().body(jsonStr);
        }
        return R.fail(e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public R<?> handlerRuntimeException(RuntimeException e) {
        log.error("运行时异常：{}", getRequestDetails(), e);
        return R.fail(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public R<?> handlerException(Exception e) {
        log.error("系统异常：{}", getRequestDetails(), e);
        return R.fail(e.getMessage());
    }


}
