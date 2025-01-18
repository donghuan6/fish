package com.nine.security.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class MyPermissionCheckerImpl implements MyPermissionChecker {
    @Override
    public boolean check(HttpServletRequest request, Authentication authentication) {
        String servletPath = request.getServletPath();

        // 获取权限

        // 判断当前用户是否有权限


        return false;
    }
}
