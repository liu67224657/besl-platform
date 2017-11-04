package com.enjoyf.webapps.joyme.dto.Wanba;

/**
 * Created by zhimingli on 2016/10/14 0014.
 */
public class WanbaProfileSumDTO {
    private int questionfollowsum;
    private int favoritesum;
    private int userpoint;
    private int answersum;
    private int answerpoint;

    public int getAnswerpoint() {
        return answerpoint;
    }

    public void setAnswerpoint(int answerpoint) {
        this.answerpoint = answerpoint;
    }

    public int getQuestionfollowsum() {
        return questionfollowsum;
    }

    public void setQuestionfollowsum(int questionfollowsum) {
        this.questionfollowsum = questionfollowsum;
    }

    public int getFavoritesum() {
        return favoritesum;
    }

    public void setFavoritesum(int favoritesum) {
        this.favoritesum = favoritesum;
    }

    public int getUserpoint() {
        return userpoint;
    }

    public void setUserpoint(int userpoint) {
        this.userpoint = userpoint;
    }

    public int getAnswersum() {
        return answersum;
    }

    public void setAnswersum(int answersum) {
        this.answersum = answersum;
    }
}
