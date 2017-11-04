package com.enjoyf.platform.service.gameres.privilege;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-10-14
 * Time: 下午12:26
 * To change this template use File | Settings | File Templates.
 */
public class PrivilegeRoleRelation implements Serializable {

    private long relationId;
    private long privilegeId;
    private long roleId;
    private ActStatus actStatus = ActStatus.UNACT;

    private Date createDate;
    private String createIp;
    private String createUserId;

    private Date lastModifyDate;
    private String lastModifyIp;
    private String lastModifyUserId;

    private int extInt1;
    private int extInt2;
    private String extString1;
    private String extString2;

    public long getRelationId() {
        return relationId;
    }

    public void setRelationId(long relationId) {
        this.relationId = relationId;
    }

    public long getPrivilegeId() {
        return privilegeId;
    }

    public void setPrivilegeId(long privilegeId) {
        this.privilegeId = privilegeId;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public ActStatus getActStatus() {
        return actStatus;
    }

    public void setActStatus(ActStatus actStatus) {
        this.actStatus = actStatus;
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

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public Date getLastModifyDate() {
        return lastModifyDate;
    }

    public void setLastModifyDate(Date lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }

    public String getLastModifyIp() {
        return lastModifyIp;
    }

    public void setLastModifyIp(String lastModifyIp) {
        this.lastModifyIp = lastModifyIp;
    }

    public String getLastModifyUserId() {
        return lastModifyUserId;
    }

    public void setLastModifyUserId(String lastModifyUserId) {
        this.lastModifyUserId = lastModifyUserId;
    }

    public int getExtInt1() {
        return extInt1;
    }

    public void setExtInt1(int extInt1) {
        this.extInt1 = extInt1;
    }

    public int getExtInt2() {
        return extInt2;
    }

    public void setExtInt2(int extInt2) {
        this.extInt2 = extInt2;
    }

    public String getExtString1() {
        return extString1;
    }

    public void setExtString1(String extString1) {
        this.extString1 = extString1;
    }

    public String getExtString2() {
        return extString2;
    }

    public void setExtString2(String extString2) {
        this.extString2 = extString2;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
