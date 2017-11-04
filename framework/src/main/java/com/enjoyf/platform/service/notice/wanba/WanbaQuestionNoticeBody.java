package com.enjoyf.platform.service.notice.wanba;

import com.enjoyf.platform.service.notice.AbstractUserNoticeBody;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/9/23
 */
public class WanbaQuestionNoticeBody extends AbstractUserNoticeBody {
    private long quertionId;//quesitonid
    private long answerId;//
    private String destProfileId;
    private String extStr;


    public String getDestProfileId() {
        return destProfileId;
    }

    public void setDestProfileId(String destProfileId) {
        this.destProfileId = destProfileId;
    }

    public long getQuertionId() {
        return quertionId;
    }

    public void setQuertionId(long quertionId) {
        this.quertionId = quertionId;
    }

    public long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(long answerId) {
        this.answerId = answerId;
    }

    public String getExtStr() {
        return extStr;
    }

    public void setExtStr(String extStr) {
        this.extStr = extStr;
    }
}
