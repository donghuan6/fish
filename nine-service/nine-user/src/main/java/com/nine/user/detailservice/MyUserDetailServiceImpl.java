package com.nine.user.detailservice;

import com.nine.user.dao.Permit;
import com.nine.user.dao.Role;
import com.nine.user.dao.User;
import com.nine.user.mapper.customize.UserJoinMapper;
import com.nine.user.service.base.IUserService;
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

    private final IUserService userService;
    private final UserJoinMapper userJoinMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getByUserName(username);
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        // 获取用户角色
        List<Role> roles = userJoinMapper.selectRoleByUserId(user.getUserId());
        // 获取用户权限
        List<Permit> permits = userJoinMapper.selectPermitByUserId(user.getUserId());
        List<String> authorities = new ArrayList<>();
        for (Role role : roles) {
            // 拼接角色，Security 中角色纳入权限管理，前缀必须是：ROLE_
            authorities.add("ROLE_" + role.getRoleKey());
        }
        for (Permit permit : permits) {
            authorities.add(permit.getPermit());
        }
        UserDetails userDetails =
                new org.springframework.security.core.userdetails.User(
                        username,
                        user.getPassword(),
                        AuthorityUtils.createAuthorityList(authorities));
        return userDetails;
    }
}
