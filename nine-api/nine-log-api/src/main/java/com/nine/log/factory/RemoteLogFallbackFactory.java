package com.nine.log.factory;

import com.nine.common.constans.Service;
import com.nine.common.domain.R;
import com.nine.log.RemoteLogApi;
import com.nine.log.domain.SysLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RemoteLogFallbackFactory implements FallbackFactory<RemoteLogApi> {
    @Override
    public RemoteLogApi create(Throwable e) {
        log.error("{} 远程日志服务调用失败 ", Service.LOG, e);
        return new RemoteLogApi() {
            @Override
            public R<Boolean> add(SysLog sysLog) {

                return null;
            }
        };
    }
}
