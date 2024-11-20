package com.nine.user.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface MyPermissionChecker {

    public boolean check(HttpServletRequest request, Authentication authentication);

}
