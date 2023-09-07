package com.gracerun.security.authentication.util;

import com.gracerun.security.authentication.bean.UserDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 用户上下文获取工具
 *
 * @author adc
 * @version 1.0.0
 * @date 2020-07-31
 */
@Slf4j
public abstract class UserHolder {

    /**
     * 获取当前登录用户信息
     *
     * @return
     */
    public static UserDetail getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("{}", authentication);
        return (UserDetail) authentication;
    }

}
