package com.enjoyf.platform.service.tools;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * Author: zhaoxin
 * Date: 11-10-31
 * Time: 下午8:35
 * Desc:
 */
public class PrivilegeRole implements java.io.Serializable {

    private Integer rid;
    private String roleName;
    private String description;
    private ActStatus status;
    private PrivilegeRoleType type;

    private Set<PrivilegeUser> privilegeUsers = new HashSet<PrivilegeUser>(0);
    private Set<PrivilegeResource> privilegeResources = new HashSet<PrivilegeResource>(0);

    public PrivilegeRole() {
    }

    public PrivilegeRole(String roleName, ActStatus status) {
        this.roleName = roleName;
        this.status = status;
    }

    public PrivilegeRole(String roleName, String description, ActStatus status,
                         Set<PrivilegeUser> privilegeUsers,
                         Set<PrivilegeResource> privilegeResources) {
        this.roleName = roleName;
        this.description = description;
        this.status = status;
        this.privilegeUsers = privilegeUsers;
        this.privilegeResources = privilegeResources;
    }

    public Integer getRid() {
        return this.rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ActStatus getStatus() {
        return this.status;
    }

    public void setStatus(ActStatus status) {
        this.status = status;
    }

    public PrivilegeRoleType getType() {
        return type;
    }

    public void setType(PrivilegeRoleType type) {
        this.type = type;
    }

    public Set<PrivilegeUser> getPrivilegeUsers() {
        return this.privilegeUsers;
    }

    public void setPrivilegeUsers(Set<PrivilegeUser> privilegeUsers) {
        this.privilegeUsers = privilegeUsers;
    }

    public Set<PrivilegeResource> getPrivilegeResources() {
        return this.privilegeResources;
    }

    public void setPrivilegeResources(Set<PrivilegeResource> privilegeResources) {
        this.privilegeResources = privilegeResources;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
