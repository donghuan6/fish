package com.nine.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * user 服务
 */
@MapperScan("com.nine.user.mapper")
@SpringBootApplication
public class User_App {
    public static void main(String[] args) {
        SpringApplication.run(User_App.class, args);
        System.out.println("◢▇▇▇▇▇▇▇▇ User ▇▇▇▇▇▇▇▇◤");
    }

}
