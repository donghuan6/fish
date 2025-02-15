package com.nine.security.config;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

/**
 * 全局服务通用配置
 */
@Configuration
public class ApplicationConfig {

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final TimeZone CHINA_TIME_ZONE = TimeZone.getTimeZone("Asia/Shanghai");

    /**
     * 自定义 jackson ,配置全局时间和日期格式
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> {
            // 设置 jackson 序列化与反反序列化时使用的时区
            builder.timeZone(CHINA_TIME_ZONE);
            // 设置全局日期时间格式
            builder.simpleDateFormat(DATE_TIME_FORMAT);
        };
    }


}