package com.nine.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * user 服务
 */
@EnableFeignClients(basePackages = "com.nine")
@MapperScan("com.nine.user.mapper")
@SpringBootApplication(scanBasePackages = "com.nine")
public class UserApp {
    public static void main(String[] args) {
        SpringApplication.run(UserApp.class, args);
        System.out.println("◢▇▇▇▇▇▇▇▇ User ▇▇▇▇▇▇▇▇◤");
    }

}
