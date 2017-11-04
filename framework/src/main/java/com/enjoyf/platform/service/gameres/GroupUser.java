package com.enjoyf.platform.service.gameres;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-9-25 上午11:47
 * Description:
 */
public class GroupUser implements Serializable {

    private long groupUserId;
    private long groupId;
    private String uno;
    private GroupValidStatus validStatus = GroupValidStatus.INIT;
    private Date validTime;
    private Date createTime;
    private String createIp;

    private String validUserid;
    private String validUno;

    private String lastContentId;
    private Date lastContentDate;

    private String lastReplyContentId;
    private String lastReplyId;
    private Date lastReplyTime;
    private String createReason;

    public long getGroupUserId() {
        return groupUserId;
    }

    public void setGroupUserId(long groupUserId) {
        this.groupUserId = groupUserId;
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

    public GroupValidStatus getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(GroupValidStatus validStatus) {
        this.validStatus = validStatus;
    }

    public Date getValidTime() {
        return validTime;
    }

    public void setValidTime(Date validTime) {
        this.validTime = validTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateIp() {
        return createIp;
    }

    public void setCreateIp(String createIp) {
        this.createIp = createIp;
    }

    public String getValidUserid() {
        return validUserid;
    }

    public void setValidUserid(String validUserid) {
        this.validUserid = validUserid;
    }

    public String getValidUno() {
        return validUno;
    }

    public void setValidUno(String validUno) {
        this.validUno = validUno;
    }

    public String getLastContentId() {
        return lastContentId;
    }

    public void setLastContentId(String lastContentId) {
        this.lastContentId = lastContentId;
    }

    public Date getLastContentDate() {
        return lastContentDate;
    }

    public void setLastContentDate(Date lastContentDate) {
        this.lastContentDate = lastContentDate;
    }

    public String getLastReplyContentId() {
        return lastReplyContentId;
    }

    public void setLastReplyContentId(String lastReplyContentId) {
        this.lastReplyContentId = lastReplyContentId;
    }

    public String getLastReplyId() {
        return lastReplyId;
    }

    public void setLastReplyId(String lastReplyId) {
        this.lastReplyId = lastReplyId;
    }

    public Date getLastReplyTime() {
        return lastReplyTime;
    }

    public void setLastReplyTime(Date lastReplyTime) {
        this.lastReplyTime = lastReplyTime;
    }

    public String getCreateReason() {
        return createReason;
    }

    public void setCreateReason(String createReason) {
        this.createReason = createReason;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
