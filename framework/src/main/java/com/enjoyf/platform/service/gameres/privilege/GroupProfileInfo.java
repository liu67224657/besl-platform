package com.enjoyf.platform.service.gameres.privilege;

import com.enjoyf.platform.service.gameres.GameResource;
import com.enjoyf.platform.service.profile.Profile;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-10-21
 * Time: 下午5:34
 * To change this template use File | Settings | File Templates.
 */
public class GroupProfileInfo implements Serializable{

    private GameResource group;
    private Profile profile;
    private Role role;
    private Map<String, GroupPrivilege> privilegeMap;

    public GameResource getGroup() {
        return group;
    }

    public void setGroup(GameResource group) {
        this.group = group;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Map<String, GroupPrivilege> getPrivilegeMap() {
        return privilegeMap;
    }

    public void setPrivilegeMap(Map<String, GroupPrivilege> privilegeMap) {
        this.privilegeMap = privilegeMap;
    }
}
