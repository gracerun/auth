package com.gracerun.security.authentication.bean;

import com.gracerun.security.authentication.constant.LoginTypeConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;

import java.util.Collection;

@Accessors(chain = true)
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
public class UserDetail extends AbstractAuthenticationToken {

    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("用户ID")
    private String userId;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("业务编码")
    private String businessNo;

    @ApiModelProperty("账号类型 (手机号/邮箱/用户名) 或第三方应用名称 (微信 , 微博等)")
    private String identityType;

    @ApiModelProperty("登录类型")
    private String loginType;

    @ApiModelProperty("(手机号/邮箱/用户名或第三方应用的唯一标识)")
    private String identifier;

    @ApiModelProperty("密码过期状态 true:表示已过期,false:未过期")
    private boolean expireStatus;

    @ApiModelProperty("认证状态,SUCCESS:表示实名认证通过")
    private String verified;

    @ApiModelProperty("账户类型")
    private String accountType;

    @ApiModelProperty("服务商等级")
    private Integer level;

    @ApiModelProperty("层级信息")
    private String levelDetail;

    @ApiModelProperty("用户角色")
    private Collection<String> roles;

    public UserDetail() {
        super(null);
    }


    public UserDetail(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }

    @Override
    public Object getPrincipal() {
        return username;
    }

    @Override
    public Object getCredentials() {
        return null;
    }
}
