package com.enjoyf.platform.service.content;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-2-12
 * Time: 下午2:28
 * To change this template use File | Settings | File Templates.
 */
public class ForignContentReplyLog implements Serializable{
    private long logId;
    private long contentId;
    private long replyId;
    private String uno;
    private ForignContentReplyLogType logType;
    private Date createDate;
    private String createIp;

    public long getLogId() {
        return logId;
    }

    public void setLogId(long logId) {
        this.logId = logId;
    }

    public long getContentId() {
        return contentId;
    }

    public void setContentId(long contentId) {
        this.contentId = contentId;
    }

    public long getReplyId() {
        return replyId;
    }

    public void setReplyId(long replyId) {
        this.replyId = replyId;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public ForignContentReplyLogType getLogType() {
        return logType;
    }

    public void setLogType(ForignContentReplyLogType logType) {
        this.logType = logType;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateIp() {
        return createIp;
    }

    public void setCreateIp(String createIp) {
        this.createIp = createIp;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
