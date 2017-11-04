package com.enjoyf.platform.service.admin;


import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Admin implements Serializable {
    private String adminUno;

    private String loginName;
    private String trueName;

    private String loginpwd;

    private boolean superAdmin;
    private List<AdminRole> roles = new ArrayList<AdminRole>();

    private String description;

    private ValidStatus validStatus = ValidStatus.VALID;

    private String updateAdminUno;
    private String updateLoginName;
    private Date updateDate;
    private String updateIp;

    private String createAdminUno;
    private String createLoginName;
    private Date createDate;
    private String createIp;


    public String getAdminUno() {
        return adminUno;
    }

    public void setAdminUno(String adminUno) {
        this.adminUno = adminUno;
    }

    public String getUpdateAdminUno() {
        return updateAdminUno;
    }

    public void setUpdateAdminUno(String updateAdminUno) {
        this.updateAdminUno = updateAdminUno;
    }

    public String getCreateAdminUno() {
        return createAdminUno;
    }

    public void setCreateAdminUno(String createAdminUno) {
        this.createAdminUno = createAdminUno;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getLoginpwd() {
        return loginpwd;
    }

    public void setLoginpwd(String loginpwd) {
        this.loginpwd = loginpwd;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<AdminRole> getRoles() {
        return roles;
    }

    public String getRolesValue() {
        String rolesValue = "";
        if (!CollectionUtil.isEmpty(roles)) {
            for (AdminRole role : roles) {
                if (Strings.isNullOrEmpty(rolesValue)) {
                    rolesValue = rolesValue + role.getCode();
                } else {
                    rolesValue = rolesValue + "," + role.getCode();
                }
            }
        }
        return rolesValue;
    }

    public void setRoles(List<AdminRole> roles) {
        this.roles = roles;
    }

    public void setRolesValue(String rolesValue) {
        List<AdminRole> rolesList = new ArrayList<AdminRole>();
        if (!Strings.isNullOrEmpty(rolesValue)) {
            String[] rolesArray = rolesValue.split(",");
            if (rolesArray.length > 0) {
                for (String roleCode : rolesArray) {
                    AdminRole role = AdminRole.getByCode(roleCode);

                    if (role != null) {
                        rolesList.add(role);
                    }
                }
            } else {
                AdminRole role = AdminRole.getByCode(rolesValue);

                if (role != null) {
                    rolesList.add(role);
                }
            }
        }
        roles = rolesList;
    }

    public boolean isSuperAdmin() {
        return superAdmin;
    }

    public void setSuperAdmin(boolean superAdmin) {
        this.superAdmin = superAdmin;
    }

    public ValidStatus getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(ValidStatus validStatus) {
        this.validStatus = validStatus;
    }


    public String getUpdateLoginName() {
        return updateLoginName;
    }

    public void setUpdateLoginName(String updateLoginName) {
        this.updateLoginName = updateLoginName;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateIp() {
        return updateIp;
    }

    public void setUpdateIp(String updateIp) {
        this.updateIp = updateIp;
    }

    public String getCreateLoginName() {
        return createLoginName;
    }

    public void setCreateLoginName(String createLoginName) {
        this.createLoginName = createLoginName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateIp() {
        return createIp;
    }

    public void setCreateIp(String createIp) {
        this.createIp = createIp;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
