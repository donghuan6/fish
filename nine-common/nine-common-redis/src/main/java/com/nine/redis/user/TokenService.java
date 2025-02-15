package com.nine.redis.user;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.jwt.JWTUtil;
import com.nine.common.constans.Redis;
import com.nine.common.constans.UserToken;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@AllArgsConstructor
public class TokenService {

    private final RedisTemplate redisTemplate;

    public String getAccessToken(HttpServletRequest request) {
        String token = request.getHeader(Header.AUTHORIZATION.getValue());
        if (StrUtil.isNotBlank(token) && token.startsWith(UserToken.AUTHORIZATION_PREFIX)) {
            token = token.replaceFirst(UserToken.AUTHORIZATION_PREFIX, "");
        }
        if (StrUtil.isNotBlank(token)) {
            return token;
        }
        return null;
    }

    public Map<String, Object> parseToken(HttpServletRequest request) {
        String token = getAccessToken(request);
        if (StrUtil.isNotBlank(token)) {
            return JWTUtil.parseToken(token).getPayloads();
        }
        return Collections.emptyMap();
    }

    public Long getUserId(HttpServletRequest request) {
        return (Long) parseToken(request).get(UserToken.USER_ID);
    }

    public String getUsername(HttpServletRequest request) {
        return (String) parseToken(request).get(UserToken.USERNAME);
    }

    public String getUserKey(HttpServletRequest request) {
        return (String) parseToken(request).get(UserToken.USER_KEY);
    }

    /**
     * 获取缓存中的用户信息
     */
    public UserVo getCacheUserVo(HttpServletRequest request) {
        String userKey = getUserKey(request);
        if (StrUtil.isBlank(userKey)) {
            return null;
        }
        return getCacheUserVo(userKey);
    }

    public UserVo getCacheUserVo(String userKey) {
        if (StrUtil.isBlank(userKey)) {
            return null;
        }
        return (UserVo) redisTemplate.opsForValue().get(userKey);
    }

    /**
     * 删除用户会话中的令牌（Token）。
     */
    public void delUserKey(HttpServletRequest request) {
        String userKey = getUserKey(request);
        if (StrUtil.isNotBlank(userKey)) {
            redisTemplate.delete(userKey);
        }
    }

}
