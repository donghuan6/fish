package com.nine.user.service.base;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.nine.mybatis.service.IBaseService;
import com.nine.user.domain.SysRoleUser;

import java.util.Objects;

public interface ISysRoleUserService extends IBaseService<SysRoleUser> {

    @Override
    default LambdaQueryWrapper<SysRoleUser> qw(SysRoleUser queryDao) {
        if (Objects.isNull(queryDao)) {
            return new LambdaQueryWrapper<>();
        }
        return Wrappers.lambdaQuery(SysRoleUser.class)
                .eq(Objects.nonNull(queryDao.getRoleId()), SysRoleUser::getRoleId, queryDao.getRoleId())
                .eq(Objects.nonNull(queryDao.getUserId()), SysRoleUser::getUserId, queryDao.getUserId());
    }
}
