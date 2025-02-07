package com.nine.user.service;

import com.nine.common.constans.TableStatus;
import com.nine.common.ex.ServiceException;
import com.nine.user.dao.User;
import com.nine.user.dto.LoginDto;
import com.nine.user.service.base.IUserService;
import com.nine.user.vo.UserVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class LoginService {

    private final IUserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserVo login(LoginDto loginDto) {
        User user = userService.getByUserName(loginDto.getUsername());
        if (Objects.isNull(user) || !passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new ServiceException("用户名或密码错误");
        }
        if (TableStatus.Deleted.DELETED == user.getDeleted()) {
            throw new ServiceException("您的账号：" + loginDto.getUsername() + "已被删除");
        }
        if (TableStatus.Disabled.DISABLED == user.getStatus()) {
            throw new ServiceException("您的账号：" + loginDto.getUsername() + "已被禁用");
        }
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        return userVo;
    }

}
