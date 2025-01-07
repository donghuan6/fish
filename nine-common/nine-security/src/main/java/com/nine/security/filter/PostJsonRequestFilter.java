package com.nine.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;

/**
 * 缓存 post json 请求体,方便全局异常中输出请求日志
 * <p>
 * 使用只执行一次的过滤器，而非使用 filter
 *
 * @author fan
 */
@Component
public class PostJsonRequestFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (HttpMethod.POST.matches(request.getMethod())
                // 这里增加判断由内部 feign 调用时，req.getContentType() 可能会为 null
                && StringUtils.hasText(request.getContentType())
                && request.getContentType().contains(MediaType.APPLICATION_JSON_VALUE)
        ) {
            ContentCachingRequestWrapper cacheReq = new ContentCachingRequestWrapper(request);
            filterChain.doFilter(cacheReq, response);
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
