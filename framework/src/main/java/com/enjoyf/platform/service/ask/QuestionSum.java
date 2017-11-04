package com.enjoyf.platform.service.ask;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/9/18
 */
public class QuestionSum implements Serializable {
    private long questionId;
    private int viewSum;
    private int ansewerSum;
    private int followSum;
    private Date questionCreateTime;

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    public int getViewSum() {
        return viewSum;
    }

    public void setViewSum(int viewSum) {
        this.viewSum = viewSum;
    }

    public int getAnsewerSum() {
        return ansewerSum;
    }

    public void setAnsewerSum(int ansewerSum) {
        this.ansewerSum = ansewerSum;
    }

    public int getFollowSum() {
        return followSum;
    }

    public void setFollowSum(int followSum) {
        this.followSum = followSum;
    }

    public Date getQuestionCreateTime() {
        return questionCreateTime;
    }

    public void setQuestionCreateTime(Date questionCreateTime) {
        this.questionCreateTime = questionCreateTime;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
