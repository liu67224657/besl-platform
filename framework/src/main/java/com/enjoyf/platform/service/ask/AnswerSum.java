package com.enjoyf.platform.service.ask;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/9/18
 */
public class AnswerSum implements Serializable {
    private long answerId;
    private int viewSum;
    private int replySum;
    private int agreeSum;

    public long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(long answerId) {
        this.answerId = answerId;
    }

    public int getViewSum() {
        return viewSum;
    }

    public void setViewSum(int viewSum) {
        this.viewSum = viewSum;
    }

    public int getReplySum() {
        return replySum;
    }

    public void setReplySum(int replySum) {
        this.replySum = replySum;
    }

    public int getAgreeSum() {
        return agreeSum;
    }

    public void setAgreeSum(int agreeSum) {
        this.agreeSum = agreeSum;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
