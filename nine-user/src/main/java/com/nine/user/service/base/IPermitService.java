package com.nine.user.service.base;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.nine.dao.service.IBaseService;
import com.nine.domain.user.Permit;
import org.springframework.util.StringUtils;

import java.util.Objects;

public interface IPermitService extends IBaseService<Permit> {

    @Override
    default LambdaQueryWrapper<Permit> qw(Permit queryDao) {
        if (Objects.isNull(queryDao)) {
            return new LambdaQueryWrapper<>();
        }
        if (Objects.nonNull(queryDao.getPermitId())) {
            return Wrappers.lambdaQuery(Permit.class)
                    .eq(Permit::getPermitId, queryDao.getPermitId());
        } else {
            return Wrappers.lambdaQuery(Permit.class)
                    .like(StringUtils.hasText(queryDao.getPermitName()), Permit::getPermitName, queryDao.getPermitName())
                    .like(StringUtils.hasText(queryDao.getPath()), Permit::getPath, queryDao.getPath())
                    .eq(Objects.nonNull(queryDao.getParentId()), Permit::getParentId, queryDao.getParentId())
                    .eq(StringUtils.hasText(queryDao.getMenuType()), Permit::getMenuType, queryDao.getMenuType())
                    .eq(Objects.nonNull(queryDao.getMenuShow()), Permit::getMenuShow, queryDao.getMenuShow())
                    .eq(Objects.nonNull(queryDao.getMenuStatus()), Permit::getMenuStatus, queryDao.getMenuStatus())
                    .like(StringUtils.hasText(queryDao.getPermit()), Permit::getPermit, queryDao.getPermit())
                    .like(StringUtils.hasText(queryDao.getRemark()), Permit::getRemark, queryDao.getRemark());
        }
    }
}
