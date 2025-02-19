package com.nine.log.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nine.dao.service.IBaseService;
import com.nine.log.dao.SysLog;
import com.nine.log.mapper.SysLogMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class SysLogService extends ServiceImpl<SysLogMapper, SysLog> implements IBaseService<SysLog> {

    private final SysLogMapper sysLogMapper;

    @Override
    public LambdaQueryWrapper<SysLog> qw(SysLog queryDao) {
        LambdaQueryWrapper<SysLog> qw = Wrappers.lambdaQuery(SysLog.class);
        if (Objects.nonNull(queryDao.getLogId())) {
            return qw.eq(SysLog::getLogId, queryDao.getLogId());
        }
        return qw.eq(StrUtil.isNotBlank(queryDao.getServiceName()), SysLog::getServiceName, queryDao.getServiceName())
                .like(StrUtil.isNotBlank(queryDao.getTitle()), SysLog::getTitle, queryDao.getTitle())
                .like(StrUtil.isNotBlank(queryDao.getClassMethod()), SysLog::getClassMethod, queryDao.getClassMethod())
                .like(StrUtil.isNotBlank(queryDao.getUrl()), SysLog::getUrl, queryDao.getUrl())
                .like(StrUtil.isNotBlank(queryDao.getIp()), SysLog::getIp, queryDao.getIp())
                .eq(Objects.nonNull(queryDao.getStatus()), SysLog::getStatus, queryDao.getStatus())
                .eq(Objects.nonNull(queryDao.getStatus()), SysLog::getStatus, queryDao.getStatus())
                .eq(Objects.nonNull(queryDao.getCreateBy()), SysLog::getCreateBy, queryDao.getCreateBy())
                .orderByDesc(SysLog::getLogId);
    }

    public void add(SysLog dto) {
        save(dto);
    }

}
