/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.advertise;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;


/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 12-6-25 下午5:51
 * Description:
 */
public class AdvertiseProject implements Serializable {
    //
    private String projectId;

    //
    private String projectName;
    private String projectDesc;

    //
    private Date startDate;
    private Date endDate;

    //
    private Date statStartDate;
    private Date statEndDate;

    //
    private ValidStatus validStatus = ValidStatus.VALID;

    //
    private String createUserid;
    private Date createDate;
    private String createIp;

    //
    private String updateUserid;
    private Date updateDate;
    private String updateIp;

    //
    public AdvertiseProject() {
    }

    public String getUpdateIp() {
        return updateIp;
    }

    public void setUpdateIp(String updateIp) {
        this.updateIp = updateIp;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDesc() {
        return projectDesc;
    }

    public void setProjectDesc(String projectDesc) {
        this.projectDesc = projectDesc;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getStatStartDate() {
        return statStartDate;
    }

    public void setStatStartDate(Date statStartDate) {
        this.statStartDate = statStartDate;
    }

    public Date getStatEndDate() {
        return statEndDate;
    }

    public void setStatEndDate(Date statEndDate) {
        this.statEndDate = statEndDate;
    }

    public ValidStatus getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(ValidStatus validStatus) {
        this.validStatus = validStatus;
    }

    public String getCreateUserid() {
        return createUserid;
    }

    public void setCreateUserid(String createUserid) {
        this.createUserid = createUserid;
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

    public String getUpdateUserid() {
        return updateUserid;
    }

    public void setUpdateUserid(String updateUserid) {
        this.updateUserid = updateUserid;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    //
    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
