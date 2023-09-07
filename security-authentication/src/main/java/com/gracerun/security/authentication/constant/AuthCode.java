package com.gracerun.security.authentication.constant;

/**
 * 响应码
 *
 * @author xiaojie.zhang
 * @version V1.0.0
 * @since 2020/8/7
 */
public interface AuthCode {

    /**
     * 成功
     */
    String SUCCESS = "00";
    String SUCCESS_CN = "成功";

    /**
     * 被迫退出
     */
    String EXPIRED_LOGIN = "03";
    String EXPIRED_LOGIN_CN = "您的账号已在其它地方登录，若不是本人操作，请注意账号安全！";

    /**
     * 未登录
     */
    String NOT_LOGIN = "04";
    String NOT_LOGIN_CN = "您还未登录,请登录后操作!";

    /**
     * 无权限
     */
    String UN_AUTHORIZED = "05";
    String UN_AUTHORIZED_CN = "无操作权限,请联系管理员!";

    /**
     * 密码已过期
     */
    String AUTH_EXPIRED = "06";
    String AUTH_EXPIRED_CN = "密码已过期,请修改密码";

    /**
     * 业务错误
     */
    String BIZ_ERR = "98";
    String BIZ_ERR_CN = "业务错误";
}
