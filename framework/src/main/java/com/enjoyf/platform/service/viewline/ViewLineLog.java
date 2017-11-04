/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.viewline;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: <a mailto="ericliu@enjoyf.com">Eric Liu</a>
 * Create time: 12-2-15 下午1:13
 * Description: view category
 */
public class ViewLineLog implements Serializable {

    private long logId;
    private int srcId;
    private ViewLineLogDomain logDomain;
    private String logName;
    private String logContent;
    private Date createDate;
    private String createUno;
    private String createIp;
    private String createUserId;

    public long getLogId() {
        return logId;
    }

    public void setLogId(long logId) {
        this.logId = logId;
    }

    public String getLogName() {
        return logName;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }

    public String getLogContent() {
        return logContent;
    }

    public void setLogContent(String logContent) {
        this.logContent = logContent;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateUno() {
        return createUno;
    }

    public void setCreateUno(String createUno) {
        this.createUno = createUno;
    }

    public String getCreateIp() {
        return createIp;
    }

    public void setCreateIp(String createIp) {
        this.createIp = createIp;
    }

    public ViewLineLogDomain getLogDomain() {
        return logDomain;
    }

    public void setLogDomain(ViewLineLogDomain logDomain) {
        this.logDomain = logDomain;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public int getSrcId() {
        return srcId;
    }

    public void setSrcId(int srcId) {
        this.srcId = srcId;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
