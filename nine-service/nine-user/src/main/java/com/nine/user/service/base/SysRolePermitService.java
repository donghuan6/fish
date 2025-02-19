package com.nine.user.service.base;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nine.user.dao.SysRolePermit;
import com.nine.user.mapper.base.SysRolePermitMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class SysRolePermitService extends ServiceImpl<SysRolePermitMapper, SysRolePermit> implements ISysRolePermitService {

    private final SysRolePermitMapper sysRolePermitMapper;

}
