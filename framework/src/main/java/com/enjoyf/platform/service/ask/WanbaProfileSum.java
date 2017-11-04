package com.enjoyf.platform.service.ask;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/9/13
 */
public class WanbaProfileSum implements Serializable {
    private String profileId;
    private int answerSum;
    private int awardPoint;
    private int questionFollowSum;
    private int favoriteSum;

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public int getAnswerSum() {
        return answerSum;
    }

    public void setAnswerSum(int answerSum) {
        this.answerSum = answerSum;
    }

    public int getAwardPoint() {
        return awardPoint;
    }

    public void setAwardPoint(int awardPoint) {
        this.awardPoint = awardPoint;
    }

    public int getQuestionFollowSum() {
        return questionFollowSum;
    }

    public void setQuestionFollowSum(int questionFollowSum) {
        this.questionFollowSum = questionFollowSum;
    }

    public int getFavoriteSum() {
        return favoriteSum;
    }

    public void setFavoriteSum(int favoriteSum) {
        this.favoriteSum = favoriteSum;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
