package com.nine.user.service.base;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.nine.dao.service.IBaseService;
import com.nine.domain.user.User;
import org.springframework.util.StringUtils;

import java.util.Objects;

public interface IUserService extends IBaseService<User> {

    @Override
    default LambdaQueryWrapper<User> qw(User queryDao) {
        if (Objects.isNull(queryDao)) {
            return new LambdaQueryWrapper<>();
        }
        if (Objects.nonNull(queryDao.getUserId())) {
            return Wrappers.lambdaQuery(User.class)
                    .eq(User::getUserId, queryDao.getUserId());
        } else {
            return Wrappers.lambdaQuery(User.class)
                    .like(StringUtils.hasText(queryDao.getUsername()), User::getUsername, queryDao.getUsername())
                    .like(StringUtils.hasText(queryDao.getPhone()), User::getPhone, queryDao.getPhone())
                    .like(StringUtils.hasText(queryDao.getNickName()), User::getNickName, queryDao.getNickName())
                    .like(StringUtils.hasText(queryDao.getEmail()), User::getEmail, queryDao.getEmail())
                    .eq(Objects.nonNull(queryDao.getSex()), User::getSex, queryDao.getSex())
                    .eq(Objects.nonNull(queryDao.getStatus()), User::getStatus, queryDao.getStatus())
                    .eq(Objects.nonNull(queryDao.getDeleted()), User::getDeleted, queryDao.getDeleted());
        }
    }

    default User getByUserName(String username) {
        return this.lambdaQuery().eq(User::getUsername, username).one();
    }
}
