package com.nine.user.detailservice;

import com.nine.user.dao.SysPermit;
import com.nine.user.dao.SysRole;
import com.nine.user.dao.SysUser;
import com.nine.user.mapper.customize.SysUserJoinMapper;
import com.nine.user.service.base.ISysUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * spring security 登录服务实现
 */
@Component
@AllArgsConstructor
public class MyUserDetailServiceImpl implements UserDetailsService {

    private final ISysUserService userService;
    private final SysUserJoinMapper sysUserJoinMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = userService.getByUserName(username);
        if (Objects.isNull(sysUser)) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        // 获取用户角色
        List<SysRole> sysRoles = sysUserJoinMapper.selectRoleByUserId(sysUser.getUserId());
        // 获取用户权限
        List<SysPermit> sysPermits = sysUserJoinMapper.selectPermitByUserId(sysUser.getUserId());
        List<String> authorities = new ArrayList<>();
        for (SysRole sysRole : sysRoles) {
            // 拼接角色，Security 中角色纳入权限管理，前缀必须是：ROLE_
            authorities.add("ROLE_" + sysRole.getRoleKey());
        }
        for (SysPermit sysPermit : sysPermits) {
            authorities.add(sysPermit.getPermit());
        }
        UserDetails userDetails =
                new org.springframework.security.core.userdetails.User(
                        username,
                        sysUser.getPassword(),
                        AuthorityUtils.createAuthorityList(authorities));
        return userDetails;
    }
}
