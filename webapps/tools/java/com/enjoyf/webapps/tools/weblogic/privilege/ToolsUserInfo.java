package com.enjoyf.webapps.tools.weblogic.privilege;

import com.enjoyf.platform.service.oauth.AuthToken;
import com.enjoyf.platform.service.tools.PrivilegeUser;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by zhitaoshi on 2015/11/18.
 */
public class ToolsUserInfo implements Serializable {

    private MenuTree menuTree; //1及菜单
    private HashMap treeMap;
    private HashMap privilegeIds;
    private PrivilegeUser privilegeUser;
    private AuthToken uploadToken;

    public MenuTree getMenuTree() {
        return menuTree;
    }

    public void setMenuTree(MenuTree menuTree) {
        this.menuTree = menuTree;
    }

    public HashMap getTreeMap() {
        return treeMap;
    }

    public void setTreeMap(HashMap treeMap) {
        this.treeMap = treeMap;
    }

    public HashMap getPrivilegeIds() {
        return privilegeIds;
    }

    public void setPrivilegeIds(HashMap privilegeIds) {
        this.privilegeIds = privilegeIds;
    }

    public PrivilegeUser getPrivilegeUser() {
        return privilegeUser;
    }

    public void setPrivilegeUser(PrivilegeUser privilegeUser) {
        this.privilegeUser = privilegeUser;
    }

    public AuthToken getUploadToken() {
        return uploadToken;
    }

    public void setUploadToken(AuthToken uploadToken) {
        this.uploadToken = uploadToken;
    }
}
