package com.nine.swagger.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * springdoc 2.5.0 配置
 *
 * @author fan
 */
@Slf4j
@Configuration
public class SwaggerConfig {

    /**
     * 配置 openAPI 信息
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("九九")
                        .description("健康与快乐")
                );
    }


}
