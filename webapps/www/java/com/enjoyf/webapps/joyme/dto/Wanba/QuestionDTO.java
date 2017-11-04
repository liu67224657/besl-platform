package com.enjoyf.webapps.joyme.dto.Wanba;

import com.enjoyf.platform.service.ask.Question;
import com.enjoyf.platform.service.ask.QuestionSum;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/9/14
 */
public class QuestionDTO {
    private QuestionDetailDTO question;
    private WanbaProfileDTO questionprofile;
    private AnswerDetailDTO answer;
    private WanbaProfileDTO answerprofile;
    private GameTagDTO gametag;

    public QuestionDetailDTO getQuestion() {
        return question;
    }

    public void setQuestion(QuestionDetailDTO question) {
        this.question = question;
    }

    public WanbaProfileDTO getQuestionprofile() {
        return questionprofile;
    }

    public void setQuestionprofile(WanbaProfileDTO questionprofile) {
        this.questionprofile = questionprofile;
    }

    public AnswerDetailDTO getAnswer() {
        return answer;
    }

    public void setAnswer(AnswerDetailDTO answer) {
        this.answer = answer;
    }

    public WanbaProfileDTO getAnswerprofile() {
        return answerprofile;
    }

    public void setAnswerprofile(WanbaProfileDTO answerprofile) {
        this.answerprofile = answerprofile;
    }

    public GameTagDTO getGametag() {
        return gametag;
    }

    public void setGametag(GameTagDTO gametag) {
        this.gametag = gametag;
    }
}
