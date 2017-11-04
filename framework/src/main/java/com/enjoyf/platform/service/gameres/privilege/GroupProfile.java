package com.enjoyf.platform.service.gameres.privilege;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-10-14
 * Time: 下午6:07
 * To change this template use File | Settings | File Templates.
 */
public class GroupProfile implements Serializable {

    private long groupProfileId;

    private long groupId;
    private String uno;
    private RoleLevel roleLevel; //小组中的身份
    private GroupProfileStatus status;

    private Date createDate; //加入时间
    private String createIp;  //审核员ip
    private String createUno;//审核员uno

    private Date lastModifyDate; //管理操作时间
    private String lastModifyIp; //管理操作ip
    private String lastModifyUno; //管理员的uno

    private Date silencedDate;
    private Date silencedEndDate;
    private Date lastLoginDate;
    private String silencedReason;

    private int extInt1;
    private int extInt2;
    private String extString1;
    private String extString2;

    public long getGroupProfileId() {
        return groupProfileId;
    }

    public void setGroupProfileId(long groupProfileId) {
        this.groupProfileId = groupProfileId;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public RoleLevel getRoleLevel() {
        return roleLevel;
    }

    public void setRoleLevel(RoleLevel roleLevel) {
        this.roleLevel = roleLevel;
    }

    public GroupProfileStatus getStatus() {
        return status;
    }

    public void setStatus(GroupProfileStatus status) {
        this.status = status;
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

    public String getCreateUno() {
        return createUno;
    }

    public void setCreateUno(String createUno) {
        this.createUno = createUno;
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

    public String getLastModifyUno() {
        return lastModifyUno;
    }

    public void setLastModifyUno(String lastModifyUno) {
        this.lastModifyUno = lastModifyUno;
    }

    public Date getSilencedDate() {
        return silencedDate;
    }

    public void setSilencedDate(Date silencedDate) {
        this.silencedDate = silencedDate;
    }

    public Date getSilencedEndDate() {
        return silencedEndDate;
    }

    public void setSilencedEndDate(Date silencedEndDate) {
        this.silencedEndDate = silencedEndDate;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public String getSilencedReason() {
        return silencedReason;
    }

    public void setSilencedReason(String silencedReason) {
        this.silencedReason = silencedReason;
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
