package com.enjoyf.platform.service.event.task;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 14-12-16
 * Time: 下午6:02
 * To change this template use File | Settings | File Templates.
 */
public class TaskLog implements Serializable {

    private String logId;//Md5(profileid+taskid+date)   Md5(profileid+taskid) Md5(UUID)
    private String profileId;
    private String taskId;
    private String appKey = "";
    private AppPlatform platform;
    private Date overTime;
    private String overIp = "";
    private ActStatus overStatus; //ing 进行中 y-已领取 rj-坐完任务未领取
    private int overTimes;
    private TaskType taskType;

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Date getOverTime() {
        return overTime;
    }

    public void setOverTime(Date overTime) {
        this.overTime = overTime;
    }

    public String getOverIp() {
        return overIp;
    }

    public void setOverIp(String overIp) {
        this.overIp = overIp;
    }

    public ActStatus getOverStatus() {
        return overStatus;
    }

    public void setOverStatus(ActStatus overStatus) {
        this.overStatus = overStatus;
    }

    public int getOverTimes() {
        return overTimes;
    }

    public void setOverTimes(int overTimes) {
        this.overTimes = overTimes;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public AppPlatform getPlatform() {
        return platform;
    }

    public void setPlatform(AppPlatform platform) {
        this.platform = platform;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
