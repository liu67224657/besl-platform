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
public class Ticket implements Serializable {
    private long ticketId;
    private String ticketName;
    private String ticketDesc;
    private int base_rate;
    private int curr_num;
    private int awardLevelCount;
    private ValidStatus validStatus = ValidStatus.INVALID;
    private int win_type;                     // 0 手动开奖- 1 自动开奖
    private String winCronexp;
    private Date start_time;
    private Date end_time;
    private String createUserid;
    private String createIp;
    private Date createDate;
    private String lastModifyUserid;
    private String lastModifyIp;
    private Date lastModifyDate;

    public long getTicketId() {
        return ticketId;
    }

    public void setTicketId(long ticketId) {
        this.ticketId = ticketId;
    }

    public String getTicketName() {
        return ticketName;
    }

    public void setTicketName(String ticketName) {
        this.ticketName = ticketName;
    }

    public String getTicketDesc() {
        return ticketDesc;
    }

    public void setTicketDesc(String ticketDesc) {
        this.ticketDesc = ticketDesc;
    }

    public int getBase_rate() {
        return base_rate;
    }

    public void setBase_rate(int base_rate) {
        this.base_rate = base_rate;
    }

    public int getCurr_num() {
        return curr_num;
    }

    public void setCurr_num(int curr_num) {
        this.curr_num = curr_num;
    }

    public int getAwardLevelCount() {
        return awardLevelCount;
    }

    public void setAwardLevelCount(int awardLevelCount) {
        this.awardLevelCount = awardLevelCount;
    }

    public ValidStatus getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(ValidStatus validStatus) {
        this.validStatus = validStatus;
    }

    public int getWin_type() {
        return win_type;
    }

    public void setWin_type(int win_type) {
        this.win_type = win_type;
    }

    public String getWinCronexp() {
        return winCronexp;
    }

    public void setWinCronexp(String winCronexp) {
        this.winCronexp = winCronexp;
    }

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }

    public String getCreateUserid() {
        return createUserid;
    }

    public void setCreateUserid(String createUserid) {
        this.createUserid = createUserid;
    }

    public String getCreateIp() {
        return createIp;
    }

    public void setCreateIp(String createIp) {
        this.createIp = createIp;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getLastModifyUserid() {
        return lastModifyUserid;
    }

    public void setLastModifyUserid(String lastModifyUserid) {
        this.lastModifyUserid = lastModifyUserid;
    }

    public String getLastModifyIp() {
        return lastModifyIp;
    }

    public void setLastModifyIp(String lastModifyIp) {
        this.lastModifyIp = lastModifyIp;
    }

    public Date getLastModifyDate() {
        return lastModifyDate;
    }

    public void setLastModifyDate(Date lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }
}
