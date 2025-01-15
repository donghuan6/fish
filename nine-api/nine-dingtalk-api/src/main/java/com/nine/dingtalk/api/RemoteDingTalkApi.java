package com.nine.dingtalk.api;

import com.nine.common.constans.Service;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = Service.DINGTALK,fallbackFactory = RemoteDingTalkFallbackFactory.class)
public interface RemoteDingTalkApi {
}
