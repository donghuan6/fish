package com.nine.websocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebsocketMain {

    public static void main(String[] args) {
        SpringApplication.run(WebsocketMain.class, args);
        System.out.println("============ websocket 启动成功 ============");
    }

}
