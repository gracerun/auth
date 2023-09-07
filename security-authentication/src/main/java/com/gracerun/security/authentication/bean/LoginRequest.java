package com.gracerun.security.authentication.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gracerun.security.authentication.constant.LoginConstant;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;

import javax.validation.constraints.NotBlank;
import java.util.Collection;

/**
 * 登录参数
 *
 * @author Tom
 * @version 1.0.0
 * @date 2023/9/6
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginRequest extends AbstractAuthenticationToken {

    /**
     * 登录类型 0:使用用户名/邮箱/手机号+密码登录,1:使用手机号+短信登录
     */
    @NotBlank
    protected String loginType;

    /**
     * 用户名/邮箱/手机号
     */
    @NotBlank
    protected String username;

    /**
     * 密码
     */
    protected String password;

    /**
     * 短信验证码
     */
    protected String smsCode;

    /**
     * 图片验证码
     */
    protected String pcCode;

    /**
     * 图片ID
     */
    protected String pcId;

    public LoginRequest() {
        super(null);
    }


    public LoginRequest(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }

    @Override
    public Object getPrincipal() {
        return username;
    }

    @Override
    public Object getCredentials() {
        if (LoginConstant.PASSWORD.equals(loginType)) {
            return password;
        } else if (LoginConstant.SMS_CODE.equals(loginType)) {
            return smsCode;
        } else {
            throw new PreAuthenticatedCredentialsNotFoundException("登录参数错误");
        }
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.password = null;
    }

}
