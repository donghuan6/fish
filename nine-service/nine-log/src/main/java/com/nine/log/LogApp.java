package com.nine.log;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.nine")
@MapperScan(value = "com.nine.log.mapper")
public class LogApp {

    public static void main(String[] args) {
        SpringApplication.run(LogApp.class, args);
        System.out.println("◢▇▇▇▇▇▇▇▇ Log ▇▇▇▇▇▇▇▇◤");
    }

}
