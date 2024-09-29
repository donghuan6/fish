package com.nine.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * user 服务
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class UserMain {

    public static void main(String[] args) {
        SpringApplication.run(UserMain.class, args);
        System.out.println("============ User 启动成功 ============");
    }

}
