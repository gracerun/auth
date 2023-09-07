package com.gracerun.security.authentication.constant;

/**
 * 登录配置常量
 * @author Tom
 * @version 1.0.0
 * @date 2023/9/7
 */
public interface LoginConstant {

    String HEADER_X_AUTH_TOKEN = "x-auth-token";

    /**
     * 使用用户名密码登录
     */
    String PASSWORD = "0";

    /**
     * 使用手机短信登录
     */
    String SMS_CODE = "1";

}
