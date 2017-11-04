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
public class PrivilegeUser implements java.io.Serializable {

    private Integer uno;
    private String userid;
    private String username;
    private String password;
    private ActStatus ustatus;
    private ActStatus limitLocation = ActStatus.UNACT;

    private String orgname;
    private Boolean iscas = false;
    private String accessip;

    private Set<PrivilegeRole> privilegeRoleses = new HashSet<PrivilegeRole>(0);


    public PrivilegeUser() {
    }


    public PrivilegeUser(Integer uno, String userid) {
        this.uno = uno;
        this.userid = userid;
    }

    public PrivilegeUser(Integer uno, String userid, String username,
                         Set<PrivilegeRole> privilegeRoleses) {
        this.uno = uno;
        this.userid = userid;
        this.username = username;
        this.password = password;
        this.privilegeRoleses = privilegeRoleses;
    }

    public Integer getUno() {
        return this.uno;
    }

    public void setUno(Integer uno) {
        this.uno = uno;
    }

    public String getUserid() {
        return this.userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return this.username;

    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<PrivilegeRole> getPrivilegeRoleses() {
        return this.privilegeRoleses;
    }

    public void setPrivilegeRoleses(Set<PrivilegeRole> privilegeRoleses) {
        this.privilegeRoleses = privilegeRoleses;
    }

    public String getAccessip() {
        return accessip;
    }

    public void setAccessip(String accessip) {
        this.accessip = accessip;
    }

    public ActStatus getUstatus() {
        return ustatus;
    }

    public void setUstatus(ActStatus ustatus) {
        this.ustatus = ustatus;
    }

    public ActStatus getLimitLocation() {
        return limitLocation;
    }

    public void setLimitLocation(ActStatus limitLocation) {
        this.limitLocation = limitLocation;
    }

    public Boolean getIscas() {
        return iscas;
    }

    public void setIscas(Boolean iscas) {
        this.iscas = iscas;
    }

    public String getOrgname() {
        return orgname;
    }

    public void setOrgname(String orgname) {
        this.orgname = orgname;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
