package com.nine.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;

@Configuration
@EnableWebSecurity
public class MySecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 密码编码
     * 使用 BCrypt 强散列函数的 PasswordEncoder 的实现
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 过滤器
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 暂时禁用 CSRF
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        // 放开登录请求
                        auth.requestMatchers("/login").permitAll()
                                // 放开静态资源访问
                                .requestMatchers("/js/**", "/css/**", "/images/**").permitAll()
                                // 其他请求都需要认证
                                .anyRequest().authenticated()
                )
                // 记住我配置
                .rememberMe(rememberMeConfigurer -> {
                    // 默认有2种实现，一种是基于内存的，一种是基于数据库存储的
                    rememberMeConfigurer.tokenRepository(new InMemoryTokenRepositoryImpl())
                            // 设置请求参数名称
                            .rememberMeParameter("remember-me")
                            // 设置服务器把登录数据保存在数据库，客户端通过cookie记录数据库唯一信息
                            .rememberMeCookieName("remember-me-cookie")
                            // // cookie 作用域，默认为当前服务器域名
                            .rememberMeCookieDomain("localhost")
                            // cookie 保存时长，单位为秒
                            .tokenValiditySeconds(3600)
                            .userDetailsService(userDetailsService);
                });
        return http.build();
    }

}
