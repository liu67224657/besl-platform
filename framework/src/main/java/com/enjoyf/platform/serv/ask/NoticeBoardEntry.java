package com.enjoyf.platform.serv.ask;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/10/11
 */
public class NoticeBoardEntry implements Serializable{
    private long questionId;
    private Date createTime;
    private Set<String> exceptProfileIds;

    public NoticeBoardEntry(long questionId, Date createTime, Set<String> exceptProfileIds) {
        this.questionId = questionId;
        this.createTime = createTime;
        this.exceptProfileIds = exceptProfileIds;
    }

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Set<String> getExceptProfileIds() {
        return exceptProfileIds;
    }

    public void setExceptProfileIds(Set<String> exceptProfileIds) {
        this.exceptProfileIds = exceptProfileIds;
    }
}
