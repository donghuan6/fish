package com.nine.user.service.base;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.nine.mybatis.service.IBaseService;
import com.nine.user.domain.SysPermit;
import org.springframework.util.StringUtils;

import java.util.Objects;

public interface ISysPermitService extends IBaseService<SysPermit> {

    @Override
    default LambdaQueryWrapper<SysPermit> qw(SysPermit queryDao) {
        if (Objects.isNull(queryDao)) {
            return new LambdaQueryWrapper<>();
        }
        if (Objects.nonNull(queryDao.getPermitId())) {
            return Wrappers.lambdaQuery(SysPermit.class)
                    .eq(SysPermit::getPermitId, queryDao.getPermitId());
        } else {
            return Wrappers.lambdaQuery(SysPermit.class)
                    .like(StringUtils.hasText(queryDao.getPermitName()), SysPermit::getPermitName, queryDao.getPermitName())
                    .like(StringUtils.hasText(queryDao.getPath()), SysPermit::getPath, queryDao.getPath())
                    .eq(Objects.nonNull(queryDao.getParentId()), SysPermit::getParentId, queryDao.getParentId())
                    .eq(StringUtils.hasText(queryDao.getMenuType()), SysPermit::getMenuType, queryDao.getMenuType())
                    .eq(Objects.nonNull(queryDao.getMenuShow()), SysPermit::getMenuShow, queryDao.getMenuShow())
                    .eq(Objects.nonNull(queryDao.getMenuStatus()), SysPermit::getMenuStatus, queryDao.getMenuStatus())
                    .like(StringUtils.hasText(queryDao.getPermit()), SysPermit::getPermit, queryDao.getPermit())
                    .like(StringUtils.hasText(queryDao.getRemark()), SysPermit::getRemark, queryDao.getRemark());
        }
    }
}
