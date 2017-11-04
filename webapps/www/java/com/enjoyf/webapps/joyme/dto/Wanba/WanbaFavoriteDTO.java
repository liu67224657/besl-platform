package com.enjoyf.webapps.joyme.dto.Wanba;

/**
 * Created by zhimingli on 2016/10/10 0010.
 */
public class WanbaFavoriteDTO {
    private WanbaProfileDTO profile;
    private AnswerDetailDTO answer;
    private QuestionDetailDTO question;

    public WanbaProfileDTO getProfile() {
        return profile;
    }

    public void setProfile(WanbaProfileDTO profile) {
        this.profile = profile;
    }

    public AnswerDetailDTO getAnswer() {
        return answer;
    }

    public void setAnswer(AnswerDetailDTO answer) {
        this.answer = answer;
    }

    public QuestionDetailDTO getQuestion() {
        return question;
    }

    public void setQuestion(QuestionDetailDTO question) {
        this.question = question;
    }
}
