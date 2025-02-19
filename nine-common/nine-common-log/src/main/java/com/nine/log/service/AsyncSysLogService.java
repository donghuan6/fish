package com.nine.log.service;

import com.nine.log.api.RemoteLogApi;
import com.nine.log.dao.SysLog;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AsyncSysLogService {

    private final RemoteLogApi logApi;

    @Async
    public void saveLog(SysLog dto) {
        logApi.add(dto);
    }

}
