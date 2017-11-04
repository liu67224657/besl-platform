package com.enjoyf.platform.service.content.social;

import com.enjoyf.platform.service.ValidStatus;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 14-4-19
 * Time: 下午4:56
 * To change this template use File | Settings | File Templates.
 */
public class SocialReport implements Serializable {
    private long reportId;
    private long contentId;
    private String uno;
    private String postUno;
    private Date createDate;
    private SocialReportType reportType;  //举报类型 1 文章 ，2 评论
    private ValidStatus validStatus;
    private int reportReason;

    private long replyId;

    public long getReportId() {
        return reportId;
    }

    public void setReportId(long reportId) {
        this.reportId = reportId;
    }

    public long getContentId() {
        return contentId;
    }

    public void setContentId(long contentId) {
        this.contentId = contentId;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public String getPostUno() {
        return postUno;
    }

    public void setPostUno(String postUno) {
        this.postUno = postUno;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public SocialReportType getReportType() {
        return reportType;
    }

    public void setReportType(SocialReportType reportType) {
        this.reportType = reportType;
    }

    public ValidStatus getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(ValidStatus validStatus) {
        this.validStatus = validStatus;
    }

    public int getReportReason() {
        return reportReason;
    }

    public void setReportReason(int reportReason) {
        this.reportReason = reportReason;
    }

    public long getReplyId() {
        return replyId;
    }

    public void setReplyId(long replyId) {
        this.replyId = replyId;
    }
}
