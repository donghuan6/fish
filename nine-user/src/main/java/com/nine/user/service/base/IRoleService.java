package com.nine.user.service.base;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.nine.dao.service.IBaseService;
import com.nine.domain.user.Role;
import org.springframework.util.StringUtils;

import java.util.Objects;

public interface IRoleService extends IBaseService<Role> {

    @Override
    default LambdaQueryWrapper<Role> qw(Role queryDao) {
        if (Objects.isNull(queryDao)) {
            return new LambdaQueryWrapper<>();
        }
        if (Objects.nonNull(queryDao.getRoleId())) {
            return Wrappers.lambdaQuery(Role.class)
                    .eq(Role::getRoleId, queryDao.getRoleId());
        } else {
            return Wrappers.lambdaQuery(Role.class)
                    .like(StringUtils.hasText(queryDao.getRoleName()), Role::getRoleName, queryDao.getRoleName())
                    .eq(StringUtils.hasText(queryDao.getRoleKey()), Role::getRoleKey, queryDao.getRoleKey())
                    .like(StringUtils.hasText(queryDao.getRemark()), Role::getRemark, queryDao.getRemark());
        }
    }
}
