package com.nine.user.service.base;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nine.domain.user.Role;
import com.nine.user.mapper.RoleMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class RoleService extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    private final RoleMapper roleMapper;



}
