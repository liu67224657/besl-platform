package com.enjoyf.platform.service.gameres;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-8-21
 * Time: 上午9:25
 * To change this template use File | Settings | File Templates.
 */
public class NewReleaseTag implements Serializable{
    private long newReleaseTagId;
    private String tagName;
    private boolean isHot;
    private boolean isTop;
    private Date createDate;
    private String createIp;
    private String createUserId;
    private Date lastModifyDate;
    private String lastModifyIp;
    private String lastModifyUserId;
    private ValidStatus validStatus;

    public long getNewReleaseTagId() {
        return newReleaseTagId;
    }

    public void setNewReleaseTagId(long newReleaseTagId) {
        this.newReleaseTagId = newReleaseTagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public boolean getIsHot() {
        return isHot;
    }

    public void setIsHot(boolean hot) {
        this.isHot = hot;
    }

    public boolean getIsTop() {
        return isTop;
    }

    public void setIsTop(boolean top) {
        this.isTop = top;
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

    public ValidStatus getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(ValidStatus validStatus) {
        this.validStatus = validStatus;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

}
