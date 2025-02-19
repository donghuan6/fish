package com.nine.security.config;

import com.nine.common.constans.Redis;
import com.nine.common.constans.UserToken;
import com.nine.common.context.ContextHolder;
import com.nine.common.domain.user.UserVo;
import com.nine.common.utils.servlet.ServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import java.util.Objects;

/**
 * 从网关进入所有其它服务的拦截器，必须校验用户信息，如果没有用户信息，拒绝响应
 */
@Slf4j
@Component
public class MyHeaderInterceptor implements AsyncHandlerInterceptor {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        String userKey = ServletUtil.getUserKey();
        UserVo user = (UserVo) redisTemplate.opsForValue().get(Redis.USER_LOGIN_TOKEN_PREFIX + userKey);
        if (Objects.isNull(user)) {
            log.error("用户未登录：request={}, response={}, handler={}, userKey={}, user={}", request, response, handler, userKey, user);
            return false;
        }
        ContextHolder.setUserId(ServletUtil.getRequestHeaderUserId());
        ContextHolder.setUsername(ServletUtil.getRequestHeaderUsername());
        ContextHolder.setUserKey(userKey);
        ContextHolder.setNickName(ServletUtil.getRequestHeaderNickName());
        ContextHolder.set(UserToken.LOGIN_USER, user);
        return true;
    }

}
