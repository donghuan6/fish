package com.nine.user.api;

import com.nine.common.constans.Service;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = Service.USER,fallbackFactory = RemoteUserFallbackFactory.class)
public interface RemoteUserApi {
}
