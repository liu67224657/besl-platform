package com.enjoyf.platform.service.ask;

import com.enjoyf.platform.service.IntValidStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/9/12
 */
public class Answer implements Serializable {
    private long answerId;
    private String answerProfileId;
    private long questionId;
    private AskBody body;
    private AskVoice askVoice;
    private String richText;
    private Date answerTime;
    private boolean isAccept = false;
    private IntValidStatus removeStatus = IntValidStatus.UNVALID;

    public long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(long answerId) {
        this.answerId = answerId;
    }

    public String getAnswerProfileId() {
        return answerProfileId;
    }

    public void setAnswerProfileId(String answerProfileId) {
        this.answerProfileId = answerProfileId;
    }

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    public AskBody getBody() {
        return body;
    }

    public void setBody(AskBody body) {
        this.body = body;
    }

    public Date getAnswerTime() {
        return answerTime;
    }

    public void setAnswerTime(Date answerTime) {
        this.answerTime = answerTime;
    }

    public boolean getIsAccept() {
        return isAccept;
    }

    public void setIsAccept(boolean isAccept) {
        this.isAccept = isAccept;
    }

    public String getRichText() {
        return richText;
    }

    public void setRichText(String richText) {
        this.richText = richText;
    }

    public AskVoice getAskVoice() {
        return askVoice;
    }

    public void setAskVoice(AskVoice askVoice) {
        this.askVoice = askVoice;
    }

    public IntValidStatus getRemoveStatus() {
        return removeStatus;
    }

    public void setRemoveStatus(IntValidStatus removeStatus) {
        this.removeStatus = removeStatus;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
