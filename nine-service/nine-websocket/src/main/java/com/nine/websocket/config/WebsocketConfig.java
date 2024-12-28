package com.nine.websocket.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * websocket 启用 stomp
 *
 * @author fan
 */
@Slf4j
@Configuration
@AllArgsConstructor
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {


}
