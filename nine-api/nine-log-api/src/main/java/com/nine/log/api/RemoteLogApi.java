package com.nine.log.api;

import com.nine.common.constans.Service;
import com.nine.common.domain.R;
import com.nine.log.dao.SysLog;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = Service.LOG, path = "/log", fallbackFactory = RemoteLogFallbackFactory.class)
public interface RemoteLogApi {


    @Operation(summary = "保存日志")
    @PostMapping
    R<Boolean> add(@RequestBody SysLog sysLog);


}
