package com.nine.log;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class LogApp {

    public static void main(String[] args) {
        SpringApplication.run(LogApp.class, args);
        System.out.println("============ Log 启动成功 ============");
    }

}
