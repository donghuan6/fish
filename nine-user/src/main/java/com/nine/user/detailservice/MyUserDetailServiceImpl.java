package com.nine.user.detailservice;

import com.nine.domain.user.Permit;
import com.nine.domain.user.Role;
import com.nine.domain.user.User;
import com.nine.user.mapper.customize.UserJoinMapper;
import com.nine.user.service.base.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

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
        List<Permit> permits = userJoinMapper.selectPermitByUserId(user.getUserId());
        List<Role> roles = userJoinMapper.selectRoleByUserId(user.getUserId());
        UserDetails userDetails =
                new org.springframework.security.core.userdetails.User(
                        username,
                        user.getPassword(),
                        AuthorityUtils.NO_AUTHORITIES);
        return userDetails;
    }
}
