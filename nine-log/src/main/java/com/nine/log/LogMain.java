package com.nine.log;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LogMain {

    public static void main(String[] args) {
        SpringApplication.run(LogMain.class, args);
        System.out.println("============ Log 启动成功 ============");
    }

}
