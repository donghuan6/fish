package com.nine.common.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;

/**
 * 缓存 post json 请求体,方便全局异常中输出请求日志
 *
 * @author fan
 */
@Component
public class PostJsonRequestFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest req
                && HttpMethod.POST.matches(req.getMethod())
                // 这里增加判断由内部 feign 调用时，req.getContentType() 可能会为 null
                && StringUtils.hasText(req.getContentType())
                && req.getContentType().contains(MediaType.APPLICATION_JSON_VALUE)
        ) {
            ContentCachingRequestWrapper cacheReq = new ContentCachingRequestWrapper(req);
            chain.doFilter(cacheReq, response);
        } else {
            chain.doFilter(request, response);
        }
    }
}
