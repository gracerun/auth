package com.gracerun.security.authentication.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gracerun.security.authentication.constant.LoginTypeConstant;
import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty("登录类型 0:使用用户名/邮箱/手机号+密码登录,1:使用手机号+短信登录")
    @NotBlank
    protected String loginType;

    @ApiModelProperty("用户名/邮箱/手机号")
    @NotBlank
    protected String username;

    @ApiModelProperty("密码")
    protected String password;

    @ApiModelProperty("短信验证码")
    protected String smsCode;

    @ApiModelProperty("图片验证码")
    protected String pcCode;

    @ApiModelProperty("图片ID")
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
        if (LoginTypeConstant.PASSWORD.equals(loginType)) {
            return password;
        } else if (LoginTypeConstant.SMS_CODE.equals(loginType)) {
            return smsCode;
        } else {
            throw new PreAuthenticatedCredentialsNotFoundException("登录参数错误");
        }
    }
}
