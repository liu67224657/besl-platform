package com.enjoyf.platform.service.ask;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhimingli on 2016/12/9 0009.
 */
public class AskTimedRelease implements Serializable {
    private Long timeid;
    private TimeRelseaseType timeRelseaseType;
    private String title;
    private ValidStatus validStatus = ValidStatus.VALID;
    private Date createTime;
    private Date releaseTime; //发布时间
    private int releaseTimes; //重复次数
    private String extStr;

    public Long getTimeid() {
        return timeid;
    }

    public void setTimeid(Long timeid) {
        this.timeid = timeid;
    }

    public TimeRelseaseType getTimeRelseaseType() {
        return timeRelseaseType;
    }

    public void setTimeRelseaseType(TimeRelseaseType timeRelseaseType) {
        this.timeRelseaseType = timeRelseaseType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ValidStatus getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(ValidStatus validStatus) {
        this.validStatus = validStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getExtStr() {
        return extStr;
    }

    public void setExtStr(String extStr) {
        this.extStr = extStr;
    }

    public Date getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
    }

    public int getReleaseTimes() {
        return releaseTimes;
    }

    public void setReleaseTimes(int releaseTimes) {
        this.releaseTimes = releaseTimes;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
