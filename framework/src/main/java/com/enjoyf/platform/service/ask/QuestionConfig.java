package com.enjoyf.platform.service.ask;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * Created by zhimingli on 2016/9/12 0012.
 */
public class QuestionConfig implements Serializable {
    private int timeLimit;//限制时间
    private int questionPoint;//时间对应的积分
    private String timeStr;


    public QuestionConfig(int timeLimit, int questionPoint) {
        this.timeLimit = timeLimit;
        this.questionPoint = questionPoint;
    }

    public QuestionConfig(int timeLimit, int questionPoint, String timeStr) {
        this.timeLimit = timeLimit;
        this.questionPoint = questionPoint;
        this.timeStr = timeStr;
    }

    public int getQuestionPoint() {
        return questionPoint;
    }

    public void setQuestionPoint(int questionPoint) {
        this.questionPoint = questionPoint;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
