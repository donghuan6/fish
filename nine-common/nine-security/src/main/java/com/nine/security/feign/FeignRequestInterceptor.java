package com.nine.security.feign;


import cn.hutool.extra.servlet.JakartaServletUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * feign 拦截器
 */
@Slf4j
@Component
public class FeignRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String clientIP = JakartaServletUtil.getClientIP(request);
        // 配置客户端ip
        requestTemplate.header("X-Forwarded-For", clientIP);


        // 微服务内部调用标记
        requestTemplate.header("a", "b");

    }
}
