package com.nine.dingtalk.factory;

import com.nine.common.constans.Service;
import com.nine.dingtalk.RemoteDingTalkApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RemoteDingTalkFallbackFactory implements FallbackFactory<RemoteDingTalkApi> {
    @Override
    public RemoteDingTalkApi create(Throwable e) {
        log.error("{} 远程日志服务调用失败 ", Service.DINGTALK, e);
        return null;
    }
}
