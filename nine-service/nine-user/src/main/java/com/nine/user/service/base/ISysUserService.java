package com.nine.user.service.base;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.nine.mybatis.service.IBaseService;
import com.nine.user.domain.SysUser;
import org.springframework.util.StringUtils;

import java.util.Objects;

public interface ISysUserService extends IBaseService<SysUser> {

    @Override
    default LambdaQueryWrapper<SysUser> qw(SysUser queryDao) {
        if (Objects.isNull(queryDao)) {
            return new LambdaQueryWrapper<>();
        }
        if (Objects.nonNull(queryDao.getUserId())) {
            return Wrappers.lambdaQuery(SysUser.class)
                    .eq(SysUser::getUserId, queryDao.getUserId());
        } else {
            return Wrappers.lambdaQuery(SysUser.class)
                    .like(StringUtils.hasText(queryDao.getUsername()), SysUser::getUsername, queryDao.getUsername())
                    .like(StringUtils.hasText(queryDao.getPhone()), SysUser::getPhone, queryDao.getPhone())
                    .like(StringUtils.hasText(queryDao.getNickName()), SysUser::getNickName, queryDao.getNickName())
                    .like(StringUtils.hasText(queryDao.getEmail()), SysUser::getEmail, queryDao.getEmail())
                    .eq(Objects.nonNull(queryDao.getSex()), SysUser::getSex, queryDao.getSex())
                    .eq(Objects.nonNull(queryDao.getStatus()), SysUser::getStatus, queryDao.getStatus())
                    .eq(Objects.nonNull(queryDao.getDeleted()), SysUser::getDeleted, queryDao.getDeleted());
        }
    }

    default SysUser getByUserName(String username) {
        return this.lambdaQuery().eq(SysUser::getUsername, username).one();
    }
}
