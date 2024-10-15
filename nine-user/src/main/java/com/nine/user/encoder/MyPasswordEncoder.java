package com.nine.user.encoder;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * spring security password encoder
 * 自定义密码编码器
 */
public class MyPasswordEncoder implements PasswordEncoder {

    /**
     * 密码加密
     * @param rawPassword 明文密码
     * @return  密码加密后的密文
     */
    @Override
    public String encode(CharSequence rawPassword) {
        return rawPassword.toString();
    }

    /**
     * 匹配明文密码与密文密码是否相同
     * @param rawPassword 明文密码，客户端传入
     * @param encodedPassword 密文密码，一般存储在数据库中
     * @return
     */
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encode(rawPassword).equals(encodedPassword);
    }

    /**
     * 是否需要升级密码解析策略，强化密码解析策略
     */
    @Override
    public boolean upgradeEncoding(String encodedPassword) {
        return PasswordEncoder.super.upgradeEncoding(encodedPassword);
    }
}
