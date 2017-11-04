package com.enjoyf.platform.service.event.system;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-5-27
 * Time: 下午4:51
 * To change this template use File | Settings | File Templates.
 */
public class UserTimelineInsertBoardEvent extends SystemEvent {

    private String profileid;
    private String type;
    private Long timelineId;
    private String msgBody;
    private Date createDate;
    private String createIp;
    private String createUserId;

    //timeline
    public UserTimelineInsertBoardEvent() {
        super(SystemEventType.USERTIMELINE_INSERT_BOARD);
    }

    public String getProfileid() {
        return profileid;
    }

    public void setProfileid(String profileid) {
        this.profileid = profileid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(String msgBody) {
        this.msgBody = msgBody;
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

    public Long getTimelineId() {
        return timelineId;
    }

    public void setTimelineId(Long timelineId) {
        this.timelineId = timelineId;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public int hashCode() {
        return profileid.hashCode();
    }
}
