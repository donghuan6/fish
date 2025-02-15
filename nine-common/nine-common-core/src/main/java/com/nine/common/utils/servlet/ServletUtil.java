package com.nine.common.utils.servlet;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import cn.hutool.http.Header;
import com.nine.common.constans.UserToken;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Map;
import java.util.Objects;

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

    public static String getAuthorization(HttpServletRequest request) {
        String auth = request.getHeader(Header.AUTHORIZATION.getValue());
        if (StrUtil.isNotBlank(auth) && auth.startsWith(UserToken.AUTHORIZATION_PREFIX)) {
            auth = auth.replaceFirst(UserToken.AUTHORIZATION_PREFIX, "");
        }
        return auth;
    }

    public static Map<String, String> getParams() {
        return JakartaServletUtil.getParamMap(getRequest());
    }
}
