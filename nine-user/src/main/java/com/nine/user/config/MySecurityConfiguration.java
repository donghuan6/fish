package com.nine.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class MySecurityConfiguration {

    /**
     * 创建 Security 过滤器链
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 认证配置
        http.formLogin(config -> config
                .usernameParameter("username")
                .passwordParameter("password")
                // 认证失败后进入指定错误页面
                .failureForwardUrl("/error")
                // 设置登录页面的访问地址，必须是 GET 请求，此配置后，必须提供控制器加视图
                .loginPage("/login")
                .loginProcessingUrl("/loginPage")
                // 认证成功后转发至主页
                .successHandler((request, response, authentication) -> {
                    // 请求转发至主页面
                    request.getRequestDispatcher("/home").forward(request, response);
                })
        );
        // 授权配置
        http.authorizeRequests()
                .requestMatchers("/login", "/loginPage", "/error").permitAll() // 放行 login 请求，不作认证授权验证
                .anyRequest().authenticated();  // 访问其他地址时，必须认证成功后才可访问

        // 关闭 csrf
        http.csrf().disable();

        return http.build();
    }

    /**
     * 创建一个密码加密器
     */
    @Bean
    public PasswordEncoder myPasswordEncoder() {
        // 构造中参数：强度-使用的对数轮次，在4到31之间，数字越大越慢，安全等级越高。默认10
        return new BCryptPasswordEncoder();
    }


}
