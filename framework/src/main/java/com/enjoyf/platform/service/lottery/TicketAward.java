package com.enjoyf.platform.service.lottery;

import com.enjoyf.platform.service.ValidStatus;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 13-7-12
 * Time: 上午9:42
 * To change this template use File | Settings | File Templates.
 */
public class TicketAward implements Serializable {
    private long ticketAwardId;
    private long ticketId;
    private int awardLevel;
    private String awardName;
    private String awardDesc;
    private String awardPic;
    private int awardCount;
    private int currentCount;
    private ValidStatus validStatus = ValidStatus.VALID;
    private String createUserId;
    private Date createDate;
    private String createIp;
    private String lastModifyUserId;
    private Date lastModifyDate;
    private String lastModifyIp;

    public long getTicketAwardId() {
        return ticketAwardId;
    }

    public void setTicketAwardId(long ticketAwardId) {
        this.ticketAwardId = ticketAwardId;
    }

    public long getTicketId() {
        return ticketId;
    }

    public void setTicketId(long ticketId) {
        this.ticketId = ticketId;
    }

    public int getAwardLevel() {
        return awardLevel;
    }

    public void setAwardLevel(int awardLevel) {
        this.awardLevel = awardLevel;
    }

    public String getAwardName() {
        return awardName;
    }

    public void setAwardName(String awardName) {
        this.awardName = awardName;
    }

    public String getAwardDesc() {
        return awardDesc;
    }

    public void setAwardDesc(String awardDesc) {
        this.awardDesc = awardDesc;
    }

    public String getAwardPic() {
        return awardPic;
    }

    public void setAwardPic(String awardPic) {
        this.awardPic = awardPic;
    }

    public int getAwardCount() {
        return awardCount;
    }

    public void setAwardCount(int awardCount) {
        this.awardCount = awardCount;
    }

    public int getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
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

    public String getLastModifyUserId() {
        return lastModifyUserId;
    }

    public void setLastModifyUserId(String lastModifyUserId) {
        this.lastModifyUserId = lastModifyUserId;
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

    public ValidStatus getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(ValidStatus validStatus) {
        this.validStatus = validStatus;
    }
}
