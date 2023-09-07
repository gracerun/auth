package com.gracerun.security.authentication.bean;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Accessors(chain = true)
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
public class UserToken extends AbstractAuthenticationToken {

    /**
     * 状态
     */
    private String status;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 业务编码
     */
    private String businessNo;

    /**
     * 账号类型 (手机号/邮箱/用户名) 或第三方应用名称 (微信 , 微博等)
     */
    private String identityType;

    /**
     * 登录类型
     */
    private String loginType;

    /**
     * 手机号/邮箱/用户名或第三方应用的唯一标识
     */
    private String identifier;

    /**
     * 密码过期状态 true:表示已过期,false:未过期
     */
    private boolean expireStatus;

    public UserToken(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.password = null;
    }

    @Override
    public Object getPrincipal() {
        return username;
    }

    @Override
    public Object getCredentials() {
        return password;
    }
}
