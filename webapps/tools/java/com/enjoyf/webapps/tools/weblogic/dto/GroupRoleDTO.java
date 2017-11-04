package com.enjoyf.webapps.tools.weblogic.dto;

import com.enjoyf.platform.service.gameres.GameResource;
import com.enjoyf.platform.service.gameres.privilege.Role;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-10-17
 * Time: 下午2:52
 * To change this template use File | Settings | File Templates.
 */
public class GroupRoleDTO {

    private GameResource group;
    private Role role;

    public GameResource getGroup() {
        return group;
    }

    public void setGroup(GameResource group) {
        this.group = group;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
