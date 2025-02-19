package com.nine.user.service.base;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nine.user.dao.SysUser;
import com.nine.user.mapper.base.SysUserMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class SysUserService extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    private final SysUserMapper sysUserMapper;


}
