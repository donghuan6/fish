package com.nine.user.service.base;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nine.user.dao.SysRoleUser;
import com.nine.user.mapper.base.SysRoleUserMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class SysRoleUserService extends ServiceImpl<SysRoleUserMapper, SysRoleUser> implements ISysRoleUserService {

    private final SysRoleUserMapper sysRoleUserMapper;

}
