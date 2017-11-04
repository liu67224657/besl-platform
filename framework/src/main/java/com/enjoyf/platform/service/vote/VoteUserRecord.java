package com.enjoyf.platform.service.vote;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-9-19
 * Time: 下午2:12
 * Description: 投票参与人记录
 */
public class VoteUserRecord implements Serializable {
    private long userRecordId;
    private String subjectId;

    private String voteUno;
    private VoteDomain voteDomain = VoteDomain.DEFAULT;

    private VoteRecordSet recordSet;

    private Date voteDate;
    private String voteIp;
    
    private String dateStr;

    //getter and setter
    public long getUserRecordId() {
        return userRecordId;
    }

    public void setUserRecordId(long userRecordId) {
        this.userRecordId = userRecordId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getVoteUno() {
        return voteUno;
    }

    public void setVoteUno(String voteUno) {
        this.voteUno = voteUno;
    }

    public VoteDomain getVoteDomain() {
        return voteDomain;
    }

    public void setVoteDomain(VoteDomain voteDomain) {
        this.voteDomain = voteDomain;
    }

    public VoteRecordSet getRecordSet() {
        return recordSet;
    }

    public void setRecordSet(VoteRecordSet recordSet) {
        this.recordSet = recordSet;
    }

    public Date getVoteDate() {
        return voteDate;
    }

    public void setVoteDate(Date voteDate) {
        this.voteDate = voteDate;
    }

    public String getVoteIp() {
        return voteIp;
    }

    public void setVoteIp(String voteIp) {
        this.voteIp = voteIp;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
