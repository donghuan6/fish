package com.nine.dingtalk.listener;

import com.dingtalk.open.app.api.OpenDingTalkStreamClientBuilder;
import com.dingtalk.open.app.api.security.AuthClientCredential;
import com.dingtalk.open.app.stream.protocol.event.EventAckStatus;
import com.nine.dingtalk.config.DingTalkConfig;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class DingTalkEventListener {

    private final DingTalkConfig config;

    @PostConstruct
    public void eventListenerStream() {
        try {
            OpenDingTalkStreamClientBuilder.custom()
                    .credential(new AuthClientCredential(config.getAppKey(), config.getAppSecret()))
                    .registerAllEventListener(event -> {
                        String processCode = event.getData().getString("processCode");
                        try {
                            // todo 处理不同的事件

                            return EventAckStatus.SUCCESS;
                        }catch (Exception e){
                            log.error("");
                            // todo 对于失败的可以先记录，后期使用定时进行处理

                            return EventAckStatus.LATER;
                        }


                    }).build().start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }




}
