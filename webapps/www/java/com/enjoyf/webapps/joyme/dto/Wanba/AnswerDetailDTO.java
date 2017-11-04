package com.enjoyf.webapps.joyme.dto.Wanba;

import com.enjoyf.platform.service.ask.AskBody;
import com.enjoyf.platform.service.ask.AskVoice;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/9/14
 */
public class AnswerDetailDTO {
    public static final int DTO_ANSWER_AGREE_STATUS_NO = 0;
    public static final int DTO_ANSWER_AGREE_STATUS_YES = 1;

    private long answerid;
    private AskBody body;
    private long answertime;
    private String answertimeString;

    private int agreesum;
    private int viewsum;
    private int replysum;
    private AskVoice voice;
    private String profileid;
    private String richbody;
    private int agreestatus = DTO_ANSWER_AGREE_STATUS_NO; //0未点赞 已点赞

    public String getRichbody() {
        return richbody;
    }

    public void setRichbody(String richbody) {
        this.richbody = richbody;
    }

    public long getAnswerid() {
        return answerid;
    }

    public void setAnswerid(long answerid) {
        this.answerid = answerid;
    }

    public AskBody getBody() {
        return body;
    }

    public void setBody(AskBody body) {
        this.body = body;
    }

    public long getAnswertime() {
        return answertime;
    }

    public void setAnswertime(long answertime) {
        this.answertime = answertime;
    }

    public int getAgreesum() {
        return agreesum;
    }

    public void setAgreesum(int agreesum) {
        this.agreesum = agreesum;
    }

    public int getViewsum() {
        return viewsum;
    }

    public void setViewsum(int viewsum) {
        this.viewsum = viewsum;
    }

    public int getReplysum() {
        return replysum;
    }

    public void setReplysum(int replysum) {
        this.replysum = replysum;
    }

    public void setVoice(AskVoice voice) {
        this.voice = voice;
    }

    public AskVoice getVoice() {
        return voice;
    }

    public int getAgreestatus() {
        return agreestatus;
    }

    public void setAgreestatus(int agreestatus) {
        this.agreestatus = agreestatus;
    }

    public String getProfileid() {
        return profileid;
    }

    public void setProfileid(String profileid) {
        this.profileid = profileid;
    }

    public String getAnswertimeString() {
        return answertimeString;
    }

    public void setAnswertimeString(String answertimeString) {
        this.answertimeString = answertimeString;
    }
}
