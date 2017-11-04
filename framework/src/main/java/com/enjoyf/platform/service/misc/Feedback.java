/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.misc;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-9-13 下午8:27
 * Description:
 */
public class Feedback implements Serializable {
    private Long feedbackId;
    private String uno;

    private String feedbackSubject;
    private String feedbackBody;

    private Date feedbackDate;
    private String feedbackIp;

    public Feedback() {
    }

    public Long getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Long feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public String getFeedbackSubject() {
        return feedbackSubject;
    }

    public void setFeedbackSubject(String feedbackSubject) {
        this.feedbackSubject = feedbackSubject;
    }

    public String getFeedbackBody() {
        return feedbackBody;
    }

    public void setFeedbackBody(String feedbackBody) {
        this.feedbackBody = feedbackBody;
    }

    public Date getFeedbackDate() {
        return feedbackDate;
    }

    public void setFeedbackDate(Date feedbackDate) {
        this.feedbackDate = feedbackDate;
    }

    public String getFeedbackIp() {
        return feedbackIp;
    }

    public void setFeedbackIp(String feedbackIp) {
        this.feedbackIp = feedbackIp;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
