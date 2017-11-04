package com.enjoyf.platform.service.message;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-8-6
 * Time: 下午3:39
 * To change this template use File | Settings | File Templates.
 */
public class SocialNotice implements Serializable {

    private Long noticeId;
    private String ownUno;
    private String description;
    private int agreeCount;
    private int replyCount;
    private int noticeCount;
    private int hotCount;
    private int focusCount;
    private ActStatus removeStatus = ActStatus.UNACT;
    private Date createDate;
    private Date readAgreeDate;
    private Date readReplyDate;
    private Date readNoticeDate;
    private Date readHotDate;
    private Date readFocusDate;

    public Long getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(Long noticeId) {
        this.noticeId = noticeId;
    }

    public String getOwnUno() {
        return ownUno;
    }

    public void setOwnUno(String ownUno) {
        this.ownUno = ownUno;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAgreeCount() {
        return agreeCount;
    }

    public void setAgreeCount(int agreeCount) {
        this.agreeCount = agreeCount;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public int getNoticeCount() {
        return noticeCount;
    }

    public void setNoticeCount(int noticeCount) {
        this.noticeCount = noticeCount;
    }

    public ActStatus getRemoveStatus() {
        return removeStatus;
    }

    public void setRemoveStatus(ActStatus removeStatus) {
        this.removeStatus = removeStatus;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getReadAgreeDate() {
        return readAgreeDate;
    }

    public void setReadAgreeDate(Date readAgreeDate) {
        this.readAgreeDate = readAgreeDate;
    }

    public Date getReadReplyDate() {
        return readReplyDate;
    }

    public void setReadReplyDate(Date readReplyDate) {
        this.readReplyDate = readReplyDate;
    }

    public Date getReadNoticeDate() {
        return readNoticeDate;
    }

    public void setReadNoticeDate(Date readNoticeDate) {
        this.readNoticeDate = readNoticeDate;
    }

    public int getHotCount() {
        return hotCount;
    }

    public void setHotCount(int hotCount) {
        this.hotCount = hotCount;
    }

    public int getFocusCount() {
        return focusCount;
    }

    public void setFocusCount(int focusCount) {
        this.focusCount = focusCount;
    }

    public Date getReadHotDate() {
        return readHotDate;
    }

    public void setReadHotDate(Date readHotDate) {
        this.readHotDate = readHotDate;
    }

    public Date getReadFocusDate() {
        return readFocusDate;
    }

    public void setReadFocusDate(Date readFocusDate) {
        this.readFocusDate = readFocusDate;
    }
}
