package com.gracerun.security.authentication.constant;

/**
 * redis常量
 */
public interface RedisKeyConstant {

    String NAMESPACE = "sys:";

    /**
     * 用户锁定状态
     */
    String USER_LOCKED = NAMESPACE + "userLocked:";


}
