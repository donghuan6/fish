package com.nine.user.service.base;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.nine.dao.service.IBaseService;
import com.nine.user.dao.SysRole;
import org.springframework.util.StringUtils;

import java.util.Objects;

public interface ISysRoleService extends IBaseService<SysRole> {

    @Override
    default LambdaQueryWrapper<SysRole> qw(SysRole queryDao) {
        if (Objects.isNull(queryDao)) {
            return new LambdaQueryWrapper<>();
        }
        if (Objects.nonNull(queryDao.getRoleId())) {
            return Wrappers.lambdaQuery(SysRole.class)
                    .eq(SysRole::getRoleId, queryDao.getRoleId());
        } else {
            return Wrappers.lambdaQuery(SysRole.class)
                    .like(StringUtils.hasText(queryDao.getRoleName()), SysRole::getRoleName, queryDao.getRoleName())
                    .eq(StringUtils.hasText(queryDao.getRoleKey()), SysRole::getRoleKey, queryDao.getRoleKey())
                    .like(StringUtils.hasText(queryDao.getRemark()), SysRole::getRemark, queryDao.getRemark());
        }
    }
}
