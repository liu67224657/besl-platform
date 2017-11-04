package com.enjoyf.platform.service.account.discuz;

import java.io.Serializable;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-11-4 下午12:05
 * Description:
 */
public class DiscuzMember implements Serializable {
    private long uid;
    private String email;
    private String userName;
    private String password;
    private int status;
    private int emailstatus;
    private int avatarstatus;
    private int videophotostatus;
    private int adminid;
    private int groupid;
    private int groupexpiry;
    private String extgroupids;
    private int regdate;
    private int credits;
    private int notifysound;
    private String timeoffset;
    private int newpm;
    private int newprompt;
    private int accessmasks;
    private int allowadmincp;
    private int onlyacceptfriendpm;
    private int conisbind;
    private String accountUno;
    private String uno;


    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getEmailstatus() {
        return emailstatus;
    }

    public void setEmailstatus(int emailstatus) {
        this.emailstatus = emailstatus;
    }

    public int getAvatarstatus() {
        return avatarstatus;
    }

    public void setAvatarstatus(int avatarstatus) {
        this.avatarstatus = avatarstatus;
    }

    public int getVideophotostatus() {
        return videophotostatus;
    }

    public void setVideophotostatus(int videophotostatus) {
        this.videophotostatus = videophotostatus;
    }

    public int getAdminid() {
        return adminid;
    }

    public void setAdminid(int adminid) {
        this.adminid = adminid;
    }

    public int getGroupid() {
        return groupid;
    }

    public void setGroupid(int groupid) {
        this.groupid = groupid;
    }

    public int getGroupexpiry() {
        return groupexpiry;
    }

    public void setGroupexpiry(int groupexpiry) {
        this.groupexpiry = groupexpiry;
    }

    public String getExtgroupids() {
        return extgroupids;
    }

    public void setExtgroupids(String extgroupids) {
        this.extgroupids = extgroupids;
    }

    public int getRegdate() {
        return regdate;
    }

    public void setRegdate(int regdate) {
        this.regdate = regdate;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public int getNotifysound() {
        return notifysound;
    }

    public void setNotifysound(int notifysound) {
        this.notifysound = notifysound;
    }

    public String getTimeoffset() {
        return timeoffset;
    }

    public void setTimeoffset(String timeoffset) {
        this.timeoffset = timeoffset;
    }

    public int getNewpm() {
        return newpm;
    }

    public void setNewpm(int newpm) {
        this.newpm = newpm;
    }

    public int getNewprompt() {
        return newprompt;
    }

    public void setNewprompt(int newprompt) {
        this.newprompt = newprompt;
    }

    public int getAccessmasks() {
        return accessmasks;
    }

    public void setAccessmasks(int accessmasks) {
        this.accessmasks = accessmasks;
    }

    public int getAllowadmincp() {
        return allowadmincp;
    }

    public void setAllowadmincp(int allowadmincp) {
        this.allowadmincp = allowadmincp;
    }

    public int getOnlyacceptfriendpm() {
        return onlyacceptfriendpm;
    }

    public void setOnlyacceptfriendpm(int onlyacceptfriendpm) {
        this.onlyacceptfriendpm = onlyacceptfriendpm;
    }

    public int getConisbind() {
        return conisbind;
    }

    public void setConisbind(int conisbind) {
        this.conisbind = conisbind;
    }

    public String getAccountUno() {
        return accountUno;
    }

    public void setAccountUno(String accountUno) {
        this.accountUno = accountUno;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
