package com.nine.user.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.nine.common.constans.Redis;
import com.nine.common.constans.TableStatus;
import com.nine.common.constans.UserToken;
import com.nine.common.context.ContextHolder;
import com.nine.common.domain.user.PermitVo;
import com.nine.common.domain.user.RoleVo;
import com.nine.common.domain.user.UserVo;
import com.nine.common.ex.ServiceException;
import com.nine.common.utils.JwtUtil;
import com.nine.common.utils.servlet.ServletUtil;
import com.nine.user.dao.SysPermit;
import com.nine.user.dao.SysRole;
import com.nine.user.dao.SysUser;
import com.nine.user.dto.LoginDto;
import com.nine.user.mapper.customize.SysUserJoinMapper;
import com.nine.user.service.base.ISysUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Slf4j
@Service
@AllArgsConstructor
public class LoginService {

    private final ISysUserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate redisTemplate;
    private SysUserJoinMapper sysUserJoinMapper;


    /**
     * 登录
     */
    public Map<String, Object> login(LoginDto loginDto) {
        SysUser sysUser = userService.getByUserName(loginDto.getUsername());
        checkPassword(sysUser, loginDto.getPassword());
        checkUserStatus(sysUser);
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(sysUser, userVo);
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
        Map<String, Object> payloads = new HashMap<>();
        payloads.put(UserToken.USER_ID, userVo.getUserId());
        payloads.put(UserToken.USERNAME, userVo.getUsername());
        payloads.put(UserToken.USER_KEY, userKey);

        // 返回 jwt token 与过期时间
        Map<String, Object> claimsMap = new HashMap<>(2);
        claimsMap.put(UserToken.ACCESS_TOKEN, JwtUtil.createToken(payloads));
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
        redisTemplate.opsForValue().set(Redis.USER_LOGIN_TOKEN_PREFIX + userVo.getUserKey(), userVo, UserToken.EXPIRE_MINUTE_TIME, TimeUnit.MINUTES);
    }

    public String getRedisUserKey(String userKey) {
        return Redis.USER_LOGIN_TOKEN_PREFIX + userKey;
    }

    /**
     * 校验用户状态
     */
    public void checkUserStatus(SysUser sysUser) {
        if (TableStatus.Deleted.DELETED == sysUser.getDeleted()) {
            throw new ServiceException("您的账号已被删除");
        }
        if (TableStatus.Disabled.DISABLED == sysUser.getStatus()) {
            throw new ServiceException("您的账号已被禁用");
        }
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * 校验密码
     */
    public void checkPassword(SysUser sysUser, String password) {
        int maxCount = 5;
        int lockTime = 5;
        String username = sysUser.getUsername();
        Object countObj = redisTemplate.opsForValue().get(Redis.USER_LOGIN_FAIL_PREFIX + username);
        int failCount = countObj == null ? 0 : Integer.parseInt(countObj.toString());
        if (failCount >= maxCount) {
            throw new ServiceException("密码错误次数过多，请" + lockTime + "分钟后再试");
        }
        // 密码校验成功，清空失败次数
        if (checkPassword(password, sysUser.getPassword())) {
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
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userVo.getUserId());
        sysUser.setLoginIp(userVo.getLoginIp());
        sysUser.setLoginDate(userVo.getLoginDate());
        userService.updateById(sysUser);
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
     * 获取用户信息
     * 权限（菜单）、角色
     * userId=1 默认是超级管理员
     */
    public UserVo info() {
        UserVo user = Optional.ofNullable(ContextHolder.get(UserToken.LOGIN_USER, UserVo.class))
                .orElseThrow(() -> new ServiceException("用户信息不存在"));
        // 是否是超级管理员
        boolean superAdmin = user.isSuperAdmin();
        user.setSuperAdmin(superAdmin);
        List<SysRole> sysRoles = superAdmin ? sysUserJoinMapper.selectAllRole() : sysUserJoinMapper.selectRoleByUserId(user.getUserId());
        List<SysPermit> sysPermits = superAdmin ? sysUserJoinMapper.selectAllPermit() : sysUserJoinMapper.selectPermitByUserId(user.getUserId());
        List<RoleVo> roleVos = convertList(sysRoles, sysRole -> {
            RoleVo roleVo = new RoleVo();
            BeanUtils.copyProperties(sysRole, roleVo);
            return roleVo;
        });
        List<PermitVo> permitVos = convertList(sysPermits, sysPermit -> {
            PermitVo permitVo = new PermitVo();
            BeanUtils.copyProperties(sysPermit, permitVo);
            return permitVo;
        });
        user.setRoleList(roleVos);
        user.setPermitList(permitVos);
        refreshToken(user);
        return user;
    }

    private <T, R> List<R> convertList(List<T> data, Function<T, R> convert) {
        return data.stream().map(convert).toList();
    }

}
