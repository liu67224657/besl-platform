package com.enjoyf.platform.service.vote;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-9-19
 * Time: 下午2:11
 * Description: 投票结果详细记录
 */
public class VoteSubjectRecord implements Serializable {
    private long subjectRecordId;
    private String subjectId;

    private String voteUno;
    private VoteDomain voteDomain = VoteDomain.DEFAULT;

    private long voteOption;
    private String voteOptionValue;

    private Date voteDate;
    private String voteIp;

    //getter and setter
    public long getSubjectRecordId() {
        return subjectRecordId;
    }

    public void setSubjectRecordId(long subjectRecordId) {
        this.subjectRecordId = subjectRecordId;
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

    public long getVoteOption() {
        return voteOption;
    }

    public void setVoteOption(long voteOption) {
        this.voteOption = voteOption;
    }

    public String getVoteOptionValue() {
        return voteOptionValue;
    }

    public void setVoteOptionValue(String voteOptionValue) {
        this.voteOptionValue = voteOptionValue;
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

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
