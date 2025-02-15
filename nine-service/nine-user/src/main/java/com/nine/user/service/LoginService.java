package com.nine.user.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWTUtil;
import com.nine.common.constans.Redis;
import com.nine.common.constans.TableStatus;
import com.nine.common.constans.UserToken;
import com.nine.common.context.ContextHolder;
import com.nine.common.ex.ServiceException;
import com.nine.common.utils.servlet.ServletUtil;
import com.nine.redis.user.TokenService;
import com.nine.redis.user.UserVo;
import com.nine.user.dao.User;
import com.nine.user.dto.LoginDto;
import com.nine.user.service.base.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@AllArgsConstructor
public class LoginService {

    private final IUserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate redisTemplate;
    private final TokenService tokenService;


    /**
     * 登录
     */
    public Map<String, Object> login(LoginDto loginDto) {
        User user = userService.getByUserName(loginDto.getUsername());
        checkPassword(user, loginDto.getPassword());
        checkUserStatus(user);
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        Map<String, Object> map = generateToken(userVo);
        loginSuccess(userVo);
        return map;
    }

    /**
     * 生成 token
     */
    public Map<String, Object> generateToken(UserVo userVo) {
        // redis 用户信息
        String userKey = IdUtil.fastSimpleUUID();
        userVo.setUserKey(userKey);

        // jwt 中存储的信息
        Map<String, Object> map = new HashMap<>();
        map.put(UserToken.USER_ID, userVo.getUserId());
        map.put(UserToken.USERNAME, userVo.getUserName());
        map.put(UserToken.USER_KEY, userKey);

        // 返回 jwt token 与过期时间
        Map<String, Object> claimsMap = new HashMap<>(2);
        claimsMap.put(UserToken.ACCESS_TOKEN, JWTUtil.createToken(map, UserToken.JWT_SECRET.getBytes(StandardCharsets.UTF_8)));
        claimsMap.put(UserToken.EXPIRE_MINUTE, UserToken.EXPIRE_MINUTE_TIME);

        // 刷新用户 token 过期时间
        refreshToken(userVo);
        return claimsMap;
    }

    /**
     * 刷新用户过期时间
     */
    public void refreshToken(UserVo userVo) {
        userVo.setExpireTime(Instant.now().plus(UserToken.EXPIRE_MINUTE_TIME, ChronoUnit.MINUTES).toEpochMilli());
        userVo.setLoginDate(LocalDateTime.now());
        userVo.setLoginIp(ServletUtil.getIP());
        redisTemplate.opsForValue().set(getRedisUserKey(userVo.getUserKey()), userVo, UserToken.EXPIRE_MINUTE_TIME, TimeUnit.MINUTES);
    }

    public String getRedisUserKey(String userKey) {
        return Redis.USER_LOGIN_TOKEN_PREFIX + userKey;
    }

    /**
     * 校验用户状态
     */
    public void checkUserStatus(User user) {
        if (TableStatus.Deleted.DELETED == user.getDeleted()) {
            throw new ServiceException("您的账号已被删除");
        }
        if (TableStatus.Disabled.DISABLED == user.getStatus()) {
            throw new ServiceException("您的账号已被禁用");
        }
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * 校验密码
     */
    public void checkPassword(User user, String password) {
        int maxCount = 5;
        int lockTime = 5;
        String username = user.getUsername();
        Object countObj = redisTemplate.opsForValue().get(Redis.USER_LOGIN_FAIL_PREFIX + username);
        int failCount = countObj == null ? 0 : Integer.parseInt(countObj.toString());
        if (failCount >= maxCount) {
            throw new ServiceException("密码错误次数过多，请" + lockTime + "分钟后再试");
        }
        // 密码校验成功，清空失败次数
        if (checkPassword(password, user.getPassword())) {
            redisTemplate.delete(Redis.USER_LOGIN_FAIL_PREFIX + username);
            return;
        }
        failCount++;
        redisTemplate.opsForValue().set(Redis.USER_LOGIN_FAIL_PREFIX + username, failCount, lockTime, TimeUnit.MINUTES);
        throw new ServiceException("用户名或密码错误");

    }

    /**
     * 登录成功后，更新登录 ip,登录时间
     */
    public void loginSuccess(UserVo userVo) {
        User user = new User();
        user.setUserId(userVo.getUserId());
        user.setLoginIp(userVo.getLoginIp());
        user.setLoginDate(userVo.getLoginDate());
        userService.updateById(user);
    }

    /**
     * 退出登录
     */
    public void logout() {
        String userKey = ContextHolder.getUserKey();
        if (StrUtil.isNotBlank(userKey)) {
            redisTemplate.delete(getRedisUserKey(userKey));
        }
    }

    /**
     * 获取 user_key
     */
    public String getUserKey(HttpServletRequest request) {
        String authorization = ServletUtil.getAuthorization(request);
        if (StrUtil.isNotBlank(authorization)) {
            return (String) JWTUtil.parseToken(authorization).getPayloads().get(UserToken.USER_KEY);
        }
        return null;
    }


    /**
     * 获取用户信息
     */
    public UserVo info() {
        UserVo userVo = ContextHolder.get(UserToken.LOGIN_USER, UserVo.class);


    }
}
