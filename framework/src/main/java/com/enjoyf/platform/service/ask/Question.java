package com.enjoyf.platform.service.ask;

import com.enjoyf.platform.service.IntValidStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/9/10
 */
public class Question implements Serializable {
    private long questionId;
    private long gameId;
    private String askProfileId;
    private QuestionType type;

    private String title;
    private AskBody body;//json对象,用于缩略图
    private AskVoice askVoice;
    private String richText;
    private Date createTime;
    private int questionPoint; //提问积分

    //timelimit setting
    private long timeLimit;//限时强答时限
    private long acceptAnswerId;//采纳的答案
    private long firstAnswerId;

    //oneonone setting
    private String inviteProfileId;
    //status
    private IntValidStatus removeStatus = IntValidStatus.UNVALID;//0 1 删除未删除
    private QuestionStatus questionStatus = QuestionStatus.INIT;//问题状态 init0 accept1 timeout2
    private long reactivated;               //是否重新激活  0 未激活  1已激活

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public String getAskProfileId() {
        return askProfileId;
    }

    public void setAskProfileId(String askProfileId) {
        this.askProfileId = askProfileId;
    }

    public QuestionType getType() {
        return type;
    }

    public void setType(QuestionType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public long getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(long timeLimit) {
        this.timeLimit = timeLimit;
    }

    public String getInviteProfileId() {
        return inviteProfileId;
    }

    public void setInviteProfileId(String inviteProfileId) {
        this.inviteProfileId = inviteProfileId;
    }

    public IntValidStatus getRemoveStatus() {
        return removeStatus;
    }

    public void setRemoveStatus(IntValidStatus removeStatus) {
        this.removeStatus = removeStatus;
    }

    public void setBody(AskBody body) {
        this.body = body;
    }

    public long getAcceptAnswerId() {
        return acceptAnswerId;
    }

    public void setAcceptAnswerId(long acceptAnswerId) {
        this.acceptAnswerId = acceptAnswerId;
    }

    public AskBody getBody() {
        return body;
    }

    public int getQuestionPoint() {
        return questionPoint;
    }

    public void setQuestionPoint(int questionPoint) {
        this.questionPoint = questionPoint;
    }

    public long getFirstAnswerId() {
        return firstAnswerId;
    }

    public void setFirstAnswerId(long firstAnswerId) {
        this.firstAnswerId = firstAnswerId;
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

    public QuestionStatus getQuestionStatus() {
        return questionStatus;
    }

    public void setQuestionStatus(QuestionStatus questionStatus) {
        this.questionStatus = questionStatus;
    }

    public long getReactivated() {
        return reactivated;
    }

    public void setReactivated(long reactivated) {
        this.reactivated = reactivated;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
