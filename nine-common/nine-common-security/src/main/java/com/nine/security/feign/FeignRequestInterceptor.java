package com.nine.security.feign;


import cn.hutool.extra.servlet.JakartaServletUtil;
import com.nine.common.constans.Security;
import com.nine.common.constans.UserToken;
import com.nine.common.utils.servlet.ServletUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * feign 拦截器
 * 所有服务内部调用拦截器
 */
@Slf4j
@Component
public class FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        HttpServletRequest request = ServletUtil.getRequest();

        String clientIP = JakartaServletUtil.getClientIP(request);
        // 配置客户端ip
        template.header("X-Forwarded-For", clientIP);

        Map<String, String> headerMap = JakartaServletUtil.getHeaderMap(request);
        String userId = headerMap.get(UserToken.USER_ID);
        String username = headerMap.get(UserToken.USERNAME);
        String nickName = headerMap.get(UserToken.NICK_NAME);
        String userKey = headerMap.get(UserToken.USER_KEY);
        template.header(UserToken.USER_ID, userId);
        template.header(UserToken.USERNAME, username);
        template.header(UserToken.NICK_NAME, nickName);
        template.header(UserToken.USER_KEY, userKey);

        // 微服务内部调用标记
        template.header(Security.FROM_SOURCE, Security.INNER);
    }
}
