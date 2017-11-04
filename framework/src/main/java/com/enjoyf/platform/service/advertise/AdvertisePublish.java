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
public class AdvertisePublish implements Serializable {
    //
    private String publishId;

    //
    private String publishName;
    private String publishDesc;

    //the related project
    private String projectId;

    //the related agent
    private String agentId;

    //the redirectUrl
    private String redirectUrl;

    //
    private ValidStatus validStatus = ValidStatus.VALID;

    //
    private String createUserid;
    private Date createDate;
    private String createIp;

    //
    private String updateUserid;
    private Date updateDate;
    private Date statEndDate;//广告位截止时间
    private String updateIp;

    //   关联父类的对象
    private AdvertiseAgent advertiseAgent;
    private AdvertiseProject advertiseProject;


    public AdvertisePublish() {
    }

    public String getPublishId() {
        return publishId;
    }

    public void setPublishId(String publishId) {
        this.publishId = publishId;
    }

    public String getPublishName() {
        return publishName;
    }

    public void setPublishName(String publishName) {
        this.publishName = publishName;
    }

    public String getPublishDesc() {
        return publishDesc;
    }

    public void setPublishDesc(String publishDesc) {
        this.publishDesc = publishDesc;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
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

    public Date getStatEndDate(){
        return statEndDate;
    }

    public void setStatEndDate(Date statEndDate){
        this.statEndDate = statEndDate;
    }

    public String getUpdateIp() {
        return updateIp;
    }

    public void setUpdateIp(String updateIp) {
        this.updateIp = updateIp;
    }


    public AdvertiseAgent getAdvertiseAgent() {
        return advertiseAgent;
    }

    public void setAdvertiseAgent(AdvertiseAgent advertiseAgent) {
        this.advertiseAgent = advertiseAgent;
    }

    public AdvertiseProject getAdvertiseProject() {
        return advertiseProject;
    }

    public void setAdvertiseProject(AdvertiseProject advertiseProject) {
        this.advertiseProject = advertiseProject;
    }


    //
    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
