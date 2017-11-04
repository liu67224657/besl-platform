package com.enjoyf.platform.service.vote;

import com.enjoyf.platform.service.content.ImageContentSet;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-9-19
 * Time: 上午10:10
 * Description: 投票主题
 */
public class VoteSubject implements Serializable {
    private String subjectId;

    private String subject;
    private String direction;

    // image
    private ImageContentSet imageSet;

    private int choiceNum;
    private Date expiredDate;

    private VoteVisible voteVisible = VoteVisible.ALL;
    private VoteDomain voteDomain = VoteDomain.DEFAULT;

    private int voteNum;

    private String createUno;
    private Date createDate;
    private String createIp;

    // getter and setter


    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public ImageContentSet getImageSet() {
        return imageSet;
    }

    public void setImageSet(ImageContentSet imageSet) {
        this.imageSet = imageSet;
    }

    public int getChoiceNum() {
        return choiceNum;
    }

    public void setChoiceNum(int choiceNum) {
        this.choiceNum = choiceNum;
    }

    public Date getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(Date expiredDate) {
        this.expiredDate = expiredDate;
    }

    public VoteVisible getVoteVisible() {
        return voteVisible;
    }

    public void setVoteVisible(VoteVisible voteVisible) {
        this.voteVisible = voteVisible;
    }

    public VoteDomain getVoteDomain() {
        return voteDomain;
    }

    public void setVoteDomain(VoteDomain voteDomain) {
        this.voteDomain = voteDomain;
    }

    public int getVoteNum() {
        return voteNum;
    }

    public void setVoteNum(int voteNum) {
        this.voteNum = voteNum;
    }

    public String getCreateUno() {
        return createUno;
    }

    public void setCreateUno(String createUno) {
        this.createUno = createUno;
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

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
