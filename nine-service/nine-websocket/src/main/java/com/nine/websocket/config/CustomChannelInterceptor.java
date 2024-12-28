package com.nine.websocket.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class CustomChannelInterceptor implements ChannelInterceptor {


    /**
     * 在消息实际发送到通道之前调用。这允许在必要时修改消息。如果此方法返回null ，则不会发生实际的发送调用。
     *
     * @param  message  要发送的消息
     * @param  channel  发送消息的通道
     * @return          在拦截器中拦截后的消息
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        return ChannelInterceptor.super.preSend(message, channel);
    }
}
