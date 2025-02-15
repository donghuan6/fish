package com.nine.security.aspect;

import cn.hutool.core.util.StrUtil;
import com.nine.common.context.ContextHolder;
import com.nine.common.ex.ServiceException;
import com.nine.redis.user.UserVo;
import com.nine.common.utils.servlet.ServletUtil;
import com.nine.security.annotation.InnerAuth;
import com.nine.redis.user.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Aspect
@Component
@AllArgsConstructor
public class InnerAuthAspect {

    private final TokenService tokenService;

    @Before(value = "@annotation(innerAuth)")
    public void auth(JoinPoint point, InnerAuth innerAuth) {
        HttpServletRequest request = ServletUtil.getRequest();
        if (Objects.isNull(request)) {
            throw new ServiceException("HttpServletRequest is null");
        }
        if (!innerAuth.checkHasUser()) {
            return;
        }
        String token = tokenService.getAccessToken(request);
        if (StrUtil.isBlank(token)) {
            throw new ServiceException("token is null");
        }
        Long userId = tokenService.getUserId(request);
        if (Objects.isNull(userId)) {
            throw new ServiceException("userId is null");
        }
        String username = tokenService.getUsername(request);
        if (StrUtil.isBlank(username)) {
            throw new ServiceException("username is null");
        }
        UserVo userVo = ContextHolder.get(token, UserVo.class);
        if (Objects.isNull(userVo)) {
            throw new ServiceException("userVo is null");
        }
    }

}
