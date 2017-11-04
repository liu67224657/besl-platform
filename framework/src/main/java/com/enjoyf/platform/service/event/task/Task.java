package com.enjoyf.platform.service.event.task;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.joymeapp.AppRedirectType;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-12-16
 * Time: 上午10:25
 * To change this template use File | Settings | File Templates.
 */
public class Task implements Serializable {

    private String taskId;//自定义

    private String appKey;
    private AppPlatform platform;
    private String taskGroupId; //任务的分组
    private String directId;

    private TaskType taskType;
    private int overTimes = 1;

    private String taskName;
    private String taskDesc;
    private String taskPic;

    private TaskAward taskAward = new TaskAward();
    private int displayOrder;

    private String createUserId;
    private Date createTime;
    private String modifyUserId;
    private Date modifyTime;
    private ActStatus removeStatus = ActStatus.UNACT;

    private boolean autoSendAward = true;//是否是手动领取 true-是 false-否
    private AppRedirectType redirectType = AppRedirectType.NOTHING;
    private String redirectUri = "";

    private TaskAction taskAction;
    private int beginHour;
    private int taskVerifyId; //做任务的人是1-clientid还是0-profileid 默认为0

    private TaskJsonInfo taskJsonInfo = new TaskJsonInfo(); //保存任务的一些非查询需求的设置信息，保存形式JSON

    private Date  beginTime;        //任务开始时间  added by tony
    private Date  endTime;           //任务结束时间  added by tony

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
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


    public String getTaskGroupId() {
        return taskGroupId;
    }

    public void setTaskGroupId(String taskGroupId) {
        this.taskGroupId = taskGroupId;
    }

    public String getDirectId() {
        return directId;
    }

    public void setDirectId(String directId) {
        this.directId = directId;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public int getOverTimes() {
        return overTimes;
    }

    public void setOverTimes(int overTimes) {
        this.overTimes = overTimes;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDesc() {
        return taskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }

    public String getTaskPic() {
        return taskPic;
    }

    public void setTaskPic(String taskPic) {
        this.taskPic = taskPic;
    }

    public TaskAward getTaskAward() {
        return taskAward;
    }

    public void setTaskAward(TaskAward taskAward) {
        this.taskAward = taskAward;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getModifyUserId() {
        return modifyUserId;
    }

    public void setModifyUserId(String modifyUserId) {
        this.modifyUserId = modifyUserId;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public ActStatus getRemoveStatus() {
        return removeStatus;
    }

    public void setRemoveStatus(ActStatus removeStatus) {
        this.removeStatus = removeStatus;
    }

    public AppRedirectType getRedirectType() {
        return redirectType;
    }

    public void setRedirectType(AppRedirectType redirectType) {
        this.redirectType = redirectType;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }


    public boolean isAutoSendAward() {
        return autoSendAward;
    }

    public void setAutoSendAward(boolean autoSendAward) {
        this.autoSendAward = autoSendAward;
    }

    public TaskAction getTaskAction() {
        return taskAction;
    }

    public void setTaskAction(TaskAction taskAction) {
        this.taskAction = taskAction;
    }

    public int getBeginHour() {
        return beginHour;
    }

    public void setBeginHour(int beginHour) {
        this.beginHour = beginHour;
    }

    public int getTaskVerifyId() {
        return taskVerifyId;
    }

    public void setTaskVerifyId(int taskVerifyId) {
        this.taskVerifyId = taskVerifyId;
    }


    public TaskJsonInfo getTaskJsonInfo() {
        return taskJsonInfo;
    }

    public void setTaskJsonInfo(TaskJsonInfo taskJsonInfo) {
        this.taskJsonInfo = taskJsonInfo;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
