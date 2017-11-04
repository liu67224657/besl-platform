package com.enjoyf.webapps.joyme.dto.Wanba;

import com.enjoyf.platform.service.ask.AskBody;
import com.enjoyf.platform.service.ask.AskVoice;
import com.enjoyf.platform.service.ask.QuestionStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/9/14
 */
public class QuestionDetailDTO {
    public static final int DTO_QUESTION_FOLLOW_STATUS_UNFOLLOW = 0;
    public static final int DTO_QUESTION_FOLLOW_STATUS_FOLLOW = 1;

    private long questionid;
    private AskBody body;
    private String title;
    private long questiontime;
    private long timelimit;
    private int qtype;
    private long fisrtaid;
    private long accepaid;
    private long tagid;
    private String richbody;

    private String askprofileid; //提问profileId
    private String inviteprofileid; //被提问人profileID

    private int viewsum;
    private int followsum;
    private int answersum;
    private AskVoice voice;
    private int followstatus = DTO_QUESTION_FOLLOW_STATUS_UNFOLLOW; //0未关注 1已关注

    private int questionstatus = QuestionStatus.INIT.getCode(); //0未解决 1已解决 2过期

    private long reactivated;
    private int point;

    public long getQuestionid() {
        return questionid;
    }

    public void setQuestionid(long questionid) {
        this.questionid = questionid;
    }

    public AskBody getBody() {
        return body;
    }

    public void setBody(AskBody body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getQuestiontime() {
        return questiontime;
    }

    public void setQuestiontime(long questiontime) {
        this.questiontime = questiontime;
    }

    public long getTimelimit() {
        return timelimit;
    }

    public void setTimelimit(long timelimit) {
        this.timelimit = timelimit;
    }

    public int getQtype() {
        return qtype;
    }

    public void setQtype(int qtype) {
        this.qtype = qtype;
    }

    public long getFisrtaid() {
        return fisrtaid;
    }

    public void setFisrtaid(long fisrtaid) {
        this.fisrtaid = fisrtaid;
    }

    public long getAccepaid() {
        return accepaid;
    }

    public void setAccepaid(long accepaid) {
        this.accepaid = accepaid;
    }

    public int getViewsum() {
        return viewsum;
    }

    public void setViewsum(int viewsum) {
        this.viewsum = viewsum;
    }

    public int getAnswersum() {
        return answersum;
    }

    public void setAnswersum(int answersum) {
        this.answersum = answersum;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public AskVoice getVoice() {
        return voice;
    }

    public void setVoice(AskVoice voice) {
        if (voice == null) {
            this.voice = new AskVoice();
        } else {
            this.voice = voice;
        }
    }

    public int getFollowsum() {
        return followsum;
    }

    public void setFollowsum(int followsum) {
        this.followsum = followsum;
    }

    public int getFollowstatus() {
        return followstatus;
    }

    public void setFollowstatus(int followstatus) {
        this.followstatus = followstatus;
    }

    public int getQuestionstatus() {
        return questionstatus;
    }

    public void setQuestionstatus(int questionstatus) {
        this.questionstatus = questionstatus;
    }

    public String getAskprofileid() {
        return askprofileid;
    }

    public void setAskprofileid(String askprofileid) {
        this.askprofileid = askprofileid;
    }

    public String getInviteprofileid() {
        return inviteprofileid;
    }

    public void setInviteprofileid(String inviteprofileid) {
        this.inviteprofileid = inviteprofileid;
    }

    public long getTagid() {
        return tagid;
    }

    public void setTagid(long tagid) {
        this.tagid = tagid;
    }

    public String getRichbody() {
        return richbody;
    }

    public void setRichbody(String richbody) {
        this.richbody = richbody;
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
