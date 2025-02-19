package com.nine.security.aspect;

import cn.hutool.core.util.StrUtil;
import com.nine.common.context.ContextHolder;
import com.nine.common.domain.user.UserVo;
import com.nine.common.ex.ServiceException;
import com.nine.common.utils.servlet.ServletUtil;
import com.nine.security.annotation.InnerAuth;
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

    @Before(value = "@annotation(innerAuth)")
    public void auth(JoinPoint point, InnerAuth innerAuth) {
        HttpServletRequest request = ServletUtil.getRequest();
        if (Objects.isNull(request)) {
            throw new ServiceException("HttpServletRequest is null");
        }
        if (!innerAuth.checkHasUser()) {
            return;
        }
        String authorization = ServletUtil.getAuthorizationInToken(request);
        if (StrUtil.isBlank(authorization)) {
            throw new ServiceException("authorization is null");
        }
        Long userId = ContextHolder.getUserId();
        if (Objects.isNull(userId)) {
            throw new ServiceException("userId is null");
        }
        String username = ContextHolder.getUsername();
        if (StrUtil.isBlank(username)) {
            throw new ServiceException("username is null");
        }
        UserVo userVo = ContextHolder.getUser();
        if (Objects.isNull(userVo)) {
            throw new ServiceException("userInfo is null");
        }
    }

}
