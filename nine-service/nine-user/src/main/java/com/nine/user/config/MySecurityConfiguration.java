package com.nine.user.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class MySecurityConfiguration {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 创建 Security 过滤器链
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 认证配置
        http.formLogin(config -> config
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .loginPage("/login") // 设置登录页面的访问地址，必须是 GET 请求，此配置后，必须提供控制器加视图
                        .loginProcessingUrl("/login") // 设置登录请求的访问地址，必须是 POST 请求
//                .defaultSuccessUrl("/home") // 设置登录成功后的跳转页面，仅当直接访问登录页面时生效，如果访问其余页面，发现被跳回登录页面，登录成功后则会自动跳转到原访问页面
                        .successHandler((request, response, authentication) -> {
                            // 认证成功后转发至主页，请求转发至主页面
                            request.getRequestDispatcher("/home").forward(request, response);
                        })
        );

        http.exceptionHandling(configurer -> {
            // 自定义配置无权限错误处理
            configurer.accessDeniedHandler(new MyAccessDeniedHandler())
                    // 自定义配置认证失败处理逻辑
                    .authenticationEntryPoint(new MyAuthenticationEntryPoint())
            ;
        });

        // 授权配置
        http.authorizeRequests()
//                .requestMatchers("/**").access("@myPermissionCheckerImpl.check(request,authentication)")
                // 放行登录页面
                .requestMatchers("/login").permitAll() // 放行 login 请求，不作认证授权验证
                // 放行静态资源
                .requestMatchers("/js/**", "/css/**", "/images/**").permitAll()
                .anyRequest().authenticated();  // 访问其他地址时，必须认证成功后才可访问
        // 记住我
        http.rememberMe(httpSecurityRememberMeConfigurer -> {
            httpSecurityRememberMeConfigurer
                    .tokenRepository(persistentTokenRepository()) // 设置保存记住我数据的具体对象
                    .rememberMeParameter("remember-me") // 设置请求参数名称
                    .rememberMeCookieName("JIU_REM") // 设置服务器把登录数据保存在数据库，客户端通过cookie记录数据库唯一信息
//                    .rememberMeCookieDomain("localhost") // cookie 作用域，默认为当前服务器域名
                    .tokenValiditySeconds(3600) // cookie 保存时长，单位为秒
                    .userDetailsService(userDetailsService);
        });
        // 退出登录
        http.logout(logoutConfigurer -> {
            logoutConfigurer
                    .logoutUrl("/logout") // 设置退出登录的访问地址
                    .logoutSuccessUrl("/login") // 设置退出登录成功后的跳转页面
//                    .logoutSuccessHandler((request, response, authentication) -> { // 设置退出登录成功后的处理器
//                        request.getSession().invalidate(); // 退出登录后销毁 session
//                        response.sendRedirect("/login"); // 重定向至登录页面
//                    })
                    .addLogoutHandler((request, response, authentication) -> {
                        // 登录退出后处理逻辑
                        // 记录日志等
                    })
            ;
        });
        // 关闭 csrf
        http.csrf().disable()
                // 关闭 session，对于前后端分享项目，session 不需要的
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        ;

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

    /**
     * 创建一个持久化令牌仓储，实现 remember-me 功能
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        jdbcTokenRepository.setCreateTableOnStartup(false);

        return jdbcTokenRepository;
    }

}
