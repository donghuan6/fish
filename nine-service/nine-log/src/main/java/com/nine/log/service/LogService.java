package com.nine.log.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nine.dao.service.IBaseService;
import com.nine.log.dao.Log;
import com.nine.log.dto.LogAddDto;
import com.nine.log.mapper.LogMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class LogService extends ServiceImpl<LogMapper, Log> implements IBaseService<Log> {

    private final LogMapper logMapper;

    @Override
    public LambdaQueryWrapper<Log> qw(Log queryDao) {
        LambdaQueryWrapper<Log> qw = Wrappers.lambdaQuery(Log.class);
        if (Objects.nonNull(queryDao.getLogId())) {
            return qw.eq(Log::getLogId, queryDao.getLogId());
        }
        return qw.eq(StrUtil.isNotBlank(queryDao.getServiceName()), Log::getServiceName, queryDao.getServiceName())
                .like(StrUtil.isNotBlank(queryDao.getMethod()), Log::getMethod, queryDao.getMethod())
                .like(StrUtil.isNotBlank(queryDao.getUrl()), Log::getUrl, queryDao.getUrl())
                .like(StrUtil.isNotBlank(queryDao.getIp()), Log::getIp, queryDao.getIp())
                .eq(Objects.nonNull(queryDao.getStatus()), Log::getStatus, queryDao.getStatus())
                .eq(Objects.nonNull(queryDao.getStatus()), Log::getStatus, queryDao.getStatus())
                .eq(Objects.nonNull(queryDao.getCreateBy()), Log::getCreateBy, queryDao.getCreateBy())
                .orderByDesc(Log::getLogId);
    }

    public void add(LogAddDto dto) {
        Log log = new Log().convert(dto);
        save(log);
    }

}
