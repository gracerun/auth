package com.gracerun.security.authentication.util;

import com.gracerun.security.authentication.bean.UserToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 获取用户工具
 * @author Tom
 * @version 1.0.0
 * @date 2023/9/7
 */
@Slf4j
public abstract class UserHolder {

    /**
     * 获取当前登录用户信息
     *
     * @return
     */
    public static UserToken getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("{}", authentication);
        return (UserToken) authentication;
    }

}
