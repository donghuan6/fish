package com.nine.user.service.base;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.nine.dao.service.IBaseService;
import com.nine.domain.user.RolePermit;
import com.nine.domain.user.RoleUser;

import java.util.Objects;

public interface IRoleUserService extends IBaseService<RoleUser> {

    @Override
    default LambdaQueryWrapper<RoleUser> qw(RoleUser queryDao) {
        if (Objects.isNull(queryDao)) {
            return new LambdaQueryWrapper<>();
        }
        return Wrappers.lambdaQuery(RoleUser.class)
                .eq(Objects.nonNull(queryDao.getRoleId()), RoleUser::getRoleId, queryDao.getRoleId())
                .eq(Objects.nonNull(queryDao.getUserId()), RoleUser::getUserId, queryDao.getUserId());
    }
}
