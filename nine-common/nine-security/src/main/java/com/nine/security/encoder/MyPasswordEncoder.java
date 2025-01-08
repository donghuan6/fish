package com.nine.security.encoder;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 如果需要，可以使用自定义密码加密编码
 */
public class MyPasswordEncoder implements PasswordEncoder {

    /**
     * 密码加密
     *
     * @param rawPassword 明文密码
     * @return 密码加密后的密文
     */
    @Override
    public String encode(CharSequence rawPassword) {
        return rawPassword.toString();
    }

    /**
     * 匹配明文密码与密文密码是否相同
     *
     * @param rawPassword     明文密码，客户端传入
     * @param encodedPassword 密文密码，一般存储在数据库中
     * @return true：相同，false：不相同
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
