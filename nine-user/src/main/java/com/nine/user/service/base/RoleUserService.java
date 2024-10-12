package com.nine.user.service.base;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nine.domain.user.RoleUser;
import com.nine.user.mapper.RoleUserMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class RoleUserService extends ServiceImpl<RoleUserMapper, RoleUser> implements IRoleUserService {

    private final RoleUserMapper roleUserMapper;

}
