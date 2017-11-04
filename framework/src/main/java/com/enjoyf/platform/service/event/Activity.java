package com.enjoyf.platform.service.event;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 14-8-1
 * Time: 上午11:21
 * 活动的实体类
 */
public class Activity implements Serializable{
    private long activityId;
    private String name;
    private String description;
    private Date startDate;
    private Date endDate;
    private ValidStatus validStatus=ValidStatus.INVALID;

    private int count;
    private int restamount;

    private Date createDate;
    private String createIp;
    private String createUserId;

    private ActivityLimit limit=ActivityLimit.AWARD_LIMIT_DAY;

    private ActivityAwardType awardType;
    private long awardId;

    public long getActivityId() {
        return activityId;
    }

    public void setActivityId(long activityId) {
        this.activityId = activityId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public ValidStatus getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(ValidStatus validStatus) {
        this.validStatus = validStatus;
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

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ActivityAwardType getAwardType() {
        return awardType;
    }

    public void setAwardType(ActivityAwardType awardType) {
        this.awardType = awardType;
    }

    public long getAwardId() {
        return awardId;
    }

    public void setAwardId(long awardId) {
        this.awardId = awardId;
    }

    public ActivityLimit getLimit() {
        return limit;
    }

    public void setLimit(ActivityLimit limit) {
        this.limit = limit;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getRestamount() {
        return restamount;
    }

    public void setRestamount(int restamount) {
        this.restamount = restamount;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
