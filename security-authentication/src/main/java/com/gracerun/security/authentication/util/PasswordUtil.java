package com.gracerun.security.authentication.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 密码加密工具
 * @author Tom
 * @version 1.0.0
 * @date 2023/9/7
 */
@Slf4j
@Component
public class PasswordUtil {

    public static final PasswordEncoder ENCODER = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    public static String encode(String password) {
        return ENCODER.encode(password);
    }

    public static boolean matches(CharSequence rawPassword, String encodedPassword) {
        return ENCODER.matches(rawPassword, encodedPassword);
    }

    public static void main(String[] args) {
        String s = PasswordUtil.encode("1234qwer");
        log.info("加密结果:{}", s);
    }
}

