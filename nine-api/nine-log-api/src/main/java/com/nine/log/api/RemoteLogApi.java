package com.nine.log.api;

import com.nine.common.constans.Service;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = Service.LOG, fallbackFactory = RemoteLogFallbackFactory.class)
public interface RemoteLogApi {



}
