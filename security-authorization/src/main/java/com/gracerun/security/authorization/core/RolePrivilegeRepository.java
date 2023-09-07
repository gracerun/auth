package com.gracerun.security.authorization.core;


import com.gracerun.security.authorization.bean.RoleUrl;

import java.util.List;

/**
 * 查询角色权限
 *
 * @author Tom
 * @version 1.0.0
 * @date 2023/9/6
 */
public interface RolePrivilegeRepository {

    List<RoleUrl> selectAllPrivilegeUrl();
}
