package com.nine.log.service;

import com.nine.log.api.RemoteLogApi;
import com.nine.log.dao.Log;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AsyncLogService {

    private final RemoteLogApi logApi;

    @Async
    public void saveLog(Log dto) {
        logApi.add(dto);
    }

}
