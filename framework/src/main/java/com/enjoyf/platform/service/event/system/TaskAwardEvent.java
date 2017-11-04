package com.enjoyf.platform.service.event.system;

import com.enjoyf.platform.service.event.task.TaskAction;
import com.enjoyf.platform.service.event.task.TaskType;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-12-17
 * Time: 下午4:29
 * To change this template use File | Settings | File Templates.
 */
public class TaskAwardEvent extends SystemEvent {

    private String profileId;
    private String taskId;
    private String taskGroupId;
    private TaskType taskType;    //一天一次，永远一次等，任务id不允许重复后，应该没有什么 用处了
    private Date doTaskDate;
    private String taskIp;
    private String directId;

    private String appkey;
    private AppPlatform appPlatform;
    private String profileKey;
    private String uno;
    private String clientId;  //added by tony 2015-05-13

    private TaskAction taskAction;      //任务动作类型，重要


    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public TaskAwardEvent() {
        super(SystemEventType.TASK_AWARD_EVENT);
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

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public Date getDoTaskDate() {
        return doTaskDate;
    }

    public void setDoTaskDate(Date doTaskDate) {
        this.doTaskDate = doTaskDate;
    }

    public String getTaskIp() {
        return taskIp;
    }

    public void setTaskIp(String taskIp) {
        this.taskIp = taskIp;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public AppPlatform getAppPlatform() {
        return appPlatform;
    }

    public void setAppPlatform(AppPlatform appPlatform) {
        this.appPlatform = appPlatform;
    }

    public String getProfileKey() {
        return profileKey;
    }

    public void setProfileKey(String profileKey) {
        this.profileKey = profileKey;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public String getTaskGroupId() {
        return taskGroupId;
    }

    public void setTaskGroupId(String taskGroupId) {
        this.taskGroupId = taskGroupId;
    }

    public TaskAction getTaskAction() {
        return taskAction;
    }

    public void setTaskAction(TaskAction taskAction) {
        this.taskAction = taskAction;
    }

    public String getDirectId() {
        return directId;
    }

    public void setDirectId(String directId) {
        this.directId = directId;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
