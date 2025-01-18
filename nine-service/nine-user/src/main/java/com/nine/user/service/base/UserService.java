package com.nine.user.service.base;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nine.user.dao.User;
import com.nine.user.mapper.UserMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class UserService extends ServiceImpl<UserMapper, User> implements IUserService {

    private final UserMapper userMapper;


}
