package com.gracerun.security.authorization.bean;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 角色url权限数据
 * @author Tom
 * @version 1.0.0
 * @date 2023/9/6
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class RoleUrl {

    private String url;

    private String type;

    private String roleCode;

}
