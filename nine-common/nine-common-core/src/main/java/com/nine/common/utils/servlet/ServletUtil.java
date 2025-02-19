package com.nine.common.utils.servlet;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import cn.hutool.http.Header;
import com.nine.common.constans.UserToken;
import com.nine.common.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class ServletUtil {

    public static String getIP() {
        return JakartaServletUtil.getClientIP(getRequest());
    }

    public static HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        if (Objects.isNull(requestAttributes)) {
            return null;
        }
        return requestAttributes.getRequest();
    }

    /**
     * 获取请求头中的 Authorization Bearer token
     */
    public static String getAuthorizationInToken(HttpServletRequest request) {
        String authCode = request.getHeader(Header.AUTHORIZATION.getValue());
        return getToken(authCode);
    }

    /**
     * 获取 token
     */
    public static String getToken(String authCode) {
        if (StrUtil.isNotBlank(authCode) && authCode.startsWith(UserToken.AUTHORIZATION_PREFIX)) {
            return authCode.replaceFirst(UserToken.AUTHORIZATION_PREFIX, "");
        }
        return null;
    }

    /**
     * 获取 user_key
     */
    public static String getUserKey() {
        return Optional.ofNullable(ServletUtil.getRequest())
                .map(ServletUtil::getAuthorizationInToken)
                .map(JwtUtil::getUserKey)
                .orElse("");
    }

    public static Map<String, String> getParams() {
        return Optional.ofNullable(getRequest())
                .map(JakartaServletUtil::getParamMap)
                .orElse(Collections.emptyMap());
    }

    /**
     * 获取请求头中的 userId
     */
    public static Long getRequestHeaderUserId() {
        return Optional.ofNullable(getRequest())
                .map(h -> Convert.toLong(h.getHeader(UserToken.USER_ID)))
                .orElse(-1L);
    }

    /**
     * 获取请求头中的 username
     */
    public static String getRequestHeaderUsername() {
        return Optional.ofNullable(getRequest())
                .map(h -> h.getHeader(UserToken.USERNAME))
                .orElse("");
    }

    /**
     * 获取请求头中的 nickName
     */
    public static String getRequestHeaderNickName() {
        return Optional.ofNullable(getRequest())
                .map(h -> h.getHeader(UserToken.NICK_NAME))
                .orElse("");
    }

}