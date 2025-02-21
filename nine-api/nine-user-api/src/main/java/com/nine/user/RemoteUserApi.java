package com.nine.user;

import com.nine.common.constans.Service;
import com.nine.user.factory.RemoteUserFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = Service.USER,fallbackFactory = RemoteUserFallbackFactory.class)
public interface RemoteUserApi {
}
