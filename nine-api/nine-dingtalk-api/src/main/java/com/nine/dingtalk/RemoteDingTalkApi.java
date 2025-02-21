package com.nine.dingtalk;

import com.nine.common.constans.Service;
import com.nine.dingtalk.factory.RemoteDingTalkFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = Service.DINGTALK,fallbackFactory = RemoteDingTalkFallbackFactory.class)
public interface RemoteDingTalkApi {
}
