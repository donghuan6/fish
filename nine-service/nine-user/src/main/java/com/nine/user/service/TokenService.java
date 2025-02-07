package com.nine.user.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.jwt.JWTUtil;
import com.nine.common.constans.UserToken;
import com.nine.user.vo.UserVo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
@AllArgsConstructor
public class TokenService {

    private final RedisTemplate redisTemplate;


    public String createToken(UserVo userVo) {
        Map<String, Object> map = new HashMap<>();
        map.put(UserToken.USER_ID, userVo.getUserId());
        map.put(UserToken.USERNAME, userVo.getUserName());
        return JWTUtil.createToken(map, UserToken.JWT_SECRET.getBytes(StandardCharsets.UTF_8));
    }

    public String getToken(HttpServletRequest request) {
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
        String token = getToken(request);
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

    public UserVo getUserVo(HttpServletRequest request) {
        String token = getToken(request);
        if (StrUtil.isNotBlank(token)) {
            UserVo userVo = (UserVo) redisTemplate.opsForValue().get(token);
            if (Objects.nonNull(userVo)) {
                return userVo;
            }
        }
        return null;
    }

    public UserVo getCacheUserVo(HttpServletRequest request) {

        return null;
    }


    /**
     * 删除用户会话中的令牌（Token）。
     */
    public void delToken(HttpServletRequest request) {
        String token = getToken(request);
        if (StrUtil.isNotBlank(token)) {
            redisTemplate.delete(token);
        }
    }
}
