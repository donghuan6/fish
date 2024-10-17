package com.nine.user.service;

import com.nine.common.ex.ServiceException;
import com.nine.dao.dto.LoginDto;
import com.nine.dao.vo.UserVo;
import com.nine.domain.user.User;
import com.nine.user.service.base.IUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class LoginService {

    private final IUserService userService;

    public UserVo login(LoginDto loginDto) {
        User user = userService.getByUserName(loginDto.getUsername());
        if (Objects.isNull(user) || !user.getPassword().equals(loginDto.getPassword())) {
            throw new ServiceException("用户名或密码错误");
        }
        if (user.getStatus() == 1) {
            throw new ServiceException("用户被禁用");
        }
        if (user.getDeleted() == 2) {
            throw new ServiceException("用户不存在");
        }
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        return userVo;
    }

}
