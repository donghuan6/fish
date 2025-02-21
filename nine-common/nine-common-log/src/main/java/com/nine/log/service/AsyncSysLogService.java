package com.nine.log.service;

import com.nine.log.RemoteLogApi;
import com.nine.log.domain.SysLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncSysLogService {

    @Autowired
    private RemoteLogApi remoteLogApi;

    @Async
    public void saveLog(SysLog dto) {
        remoteLogApi.add(dto);
    }

}
