package com.nine.user.api;

import com.nine.common.constans.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RemoteUserFallbackFactory implements FallbackFactory<RemoteUserApi> {
    @Override
    public RemoteUserApi create(Throwable e) {
        log.error("{} 远程日志服务调用失败 ", Service.DINGTALK, e);

        return null;
    }
}
