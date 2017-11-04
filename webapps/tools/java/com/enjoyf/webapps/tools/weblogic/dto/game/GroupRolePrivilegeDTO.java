package com.enjoyf.webapps.tools.weblogic.dto.game;

import com.enjoyf.platform.service.gameres.GameResource;
import com.enjoyf.platform.service.gameres.privilege.GroupPrivilege;
import com.enjoyf.platform.service.gameres.privilege.PrivilegeRoleRelation;
import com.enjoyf.platform.service.gameres.privilege.Role;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-10-18
 * Time: 下午5:08
 * To change this template use File | Settings | File Templates.
 */
public class GroupRolePrivilegeDTO implements Serializable{

    private GameResource group;
    private Role role;
    private GroupPrivilege privilege;
    private PrivilegeRoleRelation relation;
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

    public GroupPrivilege getPrivilege() {
        return privilege;
    }

    public void setPrivilege(GroupPrivilege privilege) {
        this.privilege = privilege;
    }

    public PrivilegeRoleRelation getRelation() {
        return relation;
    }

    public void setRelation(PrivilegeRoleRelation relation) {
        this.relation = relation;
    }
}
