package com.enjoyf.webapps.joyme.dto.Wanba;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/9/22
 */
public class WanbaQuestionNoticeDTO {
    private WanbaSimpleProfileDTO aprofile;
    private WanbaSimpleQuestionDTO question;
    private long anserId;

    public WanbaSimpleProfileDTO getAprofile() {
        return aprofile;
    }

    public void setAprofile(WanbaSimpleProfileDTO aprofile) {
        this.aprofile = aprofile;
    }

    public WanbaSimpleQuestionDTO getQuestion() {
        return question;
    }

    public void setQuestion(WanbaSimpleQuestionDTO question) {
        this.question = question;
    }

    public long getAnserId() {
        return anserId;
    }

    public void setAnserId(long anserId) {
        this.anserId = anserId;
    }
}
