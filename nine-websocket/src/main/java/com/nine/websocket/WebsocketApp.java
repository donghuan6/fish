package com.nine.websocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebsocketApp {

    public static void main(String[] args) {
        SpringApplication.run(WebsocketApp.class, args);
        System.out.println("============ websocket 启动成功 ============");
    }

}
