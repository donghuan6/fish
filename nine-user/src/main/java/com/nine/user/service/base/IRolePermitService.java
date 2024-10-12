package com.nine.user.service.base;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.nine.dao.service.IBaseService;
import com.nine.domain.user.RolePermit;

import java.util.Objects;

public interface IRolePermitService extends IBaseService<RolePermit> {

    @Override
    default LambdaQueryWrapper<RolePermit> qw(RolePermit queryDao) {
        if (Objects.isNull(queryDao)) {
            return new LambdaQueryWrapper<>();
        }
        return Wrappers.lambdaQuery(RolePermit.class)
                .eq(Objects.nonNull(queryDao.getRoleId()), RolePermit::getRoleId, queryDao.getRoleId())
                .eq(Objects.nonNull(queryDao.getPermitId()), RolePermit::getPermitId, queryDao.getPermitId());
    }
}
