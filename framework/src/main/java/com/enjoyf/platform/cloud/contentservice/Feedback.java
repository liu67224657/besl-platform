
package com.enjoyf.platform.cloud.contentservice;

import java.io.Serializable;
/**
 * A Feedback.
 */
public class Feedback implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long uid;

    private String feedbackType;

    private String status;

    private String description;

    private String createTime;

    private String create_ip;

    private String appkey;

    private Long destid = 0l;

    private String destBody;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(String feedbackType) {
        this.feedbackType = feedbackType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreate_ip() {
        return create_ip;
    }

    public void setCreate_ip(String create_ip) {
        this.create_ip = create_ip;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public Long getDestid() {
        return destid;
    }

    public void setDestid(Long destid) {
        this.destid = destid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDestBody() {
        return destBody;
    }

    public void setDestBody(String destBody) {
        this.destBody = destBody;
    }

    //    @Override
//    public boolean equals(Object o) {
//        if (this == o) {
//            return true;
//        }
//        if (o == null || getClass() != o.getClass()) {
//            return false;
//        }
//        Feedback feedback = (Feedback) o;
//        if (feedback.getId() == null || getId() == null) {
//            return false;
//        }
//        return Objects.equals(getId(), feedback.getId());
//    }

//    @Override
//    public int hashCode() {
//        return Objects.hashCode(getId());
//    }

    @Override
    public String toString() {
        return "Feedback{" +
                "id=" + getId() +
                ", uid='" + getUid() + "'" +
                ", reason='" + getFeedbackType() + "'" +
                ", description='" + getDescription() + "'" +
                ", createTime='" + getCreateTime() + "'" +
                ", create_ip='" + getCreate_ip() + "'" +
                ", appkey='" + getAppkey() + "'" +
                ", destid='" + getDestid() + "'" +
                "}";
    }
}
