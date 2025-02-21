package com.nine.user.service.base;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.nine.mybatis.service.IBaseService;
import com.nine.user.domain.SysRolePermit;

import java.util.Objects;

public interface ISysRolePermitService extends IBaseService<SysRolePermit> {

    @Override
    default LambdaQueryWrapper<SysRolePermit> qw(SysRolePermit queryDao) {
        if (Objects.isNull(queryDao)) {
            return new LambdaQueryWrapper<>();
        }
        return Wrappers.lambdaQuery(SysRolePermit.class)
                .eq(Objects.nonNull(queryDao.getRoleId()), SysRolePermit::getRoleId, queryDao.getRoleId())
                .eq(Objects.nonNull(queryDao.getPermitId()), SysRolePermit::getPermitId, queryDao.getPermitId());
    }
}
