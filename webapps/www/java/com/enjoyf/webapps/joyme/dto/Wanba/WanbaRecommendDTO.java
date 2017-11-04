package com.enjoyf.webapps.joyme.dto.Wanba;



import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhimingli on 2016/9/25 0025.
 */
public class WanbaRecommendDTO {
    private QuestionDTO question;

    private int type;//1问答 2广告 3达人

    private ActivityTopMenuDTO adv;

    private List<WanbaProfileDTO> daren;

    public QuestionDTO getQuestion() {
        return question;
    }

    public void setQuestion(QuestionDTO question) {
        this.question = question;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ActivityTopMenuDTO getAdv() {
        return adv;
    }

    public void setAdv(ActivityTopMenuDTO adv) {
        this.adv = adv;
    }

    public List<WanbaProfileDTO> getDaren() {
        return daren;
    }

    public void setDaren(List<WanbaProfileDTO> daren) {
        this.daren = daren;
    }


}
