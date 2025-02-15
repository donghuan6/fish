package com.nine.security.config;

import com.nine.common.constans.UserToken;
import com.nine.common.context.ContextHolder;
import com.nine.redis.user.TokenService;
import com.nine.redis.user.UserVo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import java.util.Objects;

@Component
@AllArgsConstructor
public class MyHeaderInterceptor implements AsyncHandlerInterceptor {

    private final TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        ContextHolder.setUserId(tokenService.getUserId(request));
        ContextHolder.setUsername(tokenService.getUsername(request));
        ContextHolder.setUserKey(tokenService.getUserKey(request));
        UserVo userVo = tokenService.getCacheUserVo(request);
        if (Objects.nonNull(userVo)) {
//            tokenService.v
            ContextHolder.set(UserToken.LOGIN_USER, userVo);
        }
        return true;
    }
}
