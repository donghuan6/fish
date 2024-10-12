package com.nine.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * user 服务
 */
@MapperScan("com.nine.user.mapper")
@SpringBootApplication
public class UserMain {
    public static void main(String[] args) {
        SpringApplication.run(UserMain.class, args);
        System.out.println("============ User 启动成功 ============");
    }

}
