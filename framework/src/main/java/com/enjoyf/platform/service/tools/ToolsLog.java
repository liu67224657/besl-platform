package com.enjoyf.platform.service.tools;


import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.Date;

/**
 * Author: zhaoxin
 * Date: 11-10-31
 * Time: 下午8:35
 * Desc:
 */
public class ToolsLog implements java.io.Serializable {

    private long lid;
    private LogOperType operType;
    private String opUserId;
    private Date opTime;
    private String opIp;
    private String srcId;
    private String opBefore;
    private String opAfter;
    private String description;

    private Date startTime;
    private Date endTime;
    public static String END_TIME_PRIFIX = new String(" 23:59:59");

    public ToolsLog() {
    }

    public long getLid() {
        return lid;
    }

    public void setLid(long lid) {
        this.lid = lid;
    }


    public String getOpUserId() {
        return opUserId;
    }

    public void setOpUserId(String opUserId) {
        this.opUserId = opUserId;
    }

    public Date getOpTime() {
        return opTime;
    }

    public void setOpTime(Date opTime) {
        this.opTime = opTime;
    }

    public String getOpIp() {
        return opIp;
    }

    public void setOpIp(String opIp) {
        this.opIp = opIp;
    }

    public String getSrcId() {
        return srcId;
    }

    public void setSrcId(String srcId) {
        this.srcId = srcId;
    }

    public String getOpBefore() {
        return opBefore;
    }

    public void setOpBefore(String opBefore) {
        this.opBefore = opBefore;
    }

    public String getOpAfter() {
        return opAfter;
    }

    public void setOpAfter(String opAfter) {
        this.opAfter = opAfter;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public LogOperType getOperType() {
        return operType;
    }

    public void setOperType(LogOperType operType) {
        this.operType = operType;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
