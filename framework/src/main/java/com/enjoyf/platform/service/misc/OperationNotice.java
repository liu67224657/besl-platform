/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.misc;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-18 下午4:17
 * Description:
 */
public class OperationNotice implements Serializable {
    //
    private Integer noticeId;

    //消息类型
    private String noticeType;

    //消息主题和内容。
    private String noticeSubject;
    private String noticeContent;

    //有效期；
    private Date validFrom;
    private Date validUtil;

    //点击次数．
    private Integer clickTimes;

    //发布时间．
    private String publishUno;
    private Date publishDate;
    private String publishIp;

    //
    public OperationNotice() {
    }

    public Integer getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(Integer noticeId) {
        this.noticeId = noticeId;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    public String getNoticeSubject() {
        return noticeSubject;
    }

    public void setNoticeSubject(String noticeSubject) {
        this.noticeSubject = noticeSubject;
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    public Date getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    public Date getValidUtil() {
        return validUtil;
    }

    public void setValidUtil(Date validUtil) {
        this.validUtil = validUtil;
    }

    public Integer getClickTimes() {
        return clickTimes;
    }

    public void setClickTimes(Integer clickTimes) {
        this.clickTimes = clickTimes;
    }

    public String getPublishUno() {
        return publishUno;
    }

    public void setPublishUno(String publishUno) {
        this.publishUno = publishUno;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public String getPublishIp() {
        return publishIp;
    }

    public void setPublishIp(String publishIp) {
        this.publishIp = publishIp;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
