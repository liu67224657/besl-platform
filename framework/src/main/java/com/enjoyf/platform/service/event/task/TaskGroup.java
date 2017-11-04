package com.enjoyf.platform.service.event.task;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/1/7
 * Description:
 */
public class TaskGroup implements Serializable {
    private String taskGroupId;
    private String taskGroupName;
    private String appKey;
    private AppPlatform appPlatform;
    private TaskGroupType type=TaskGroupType.COMMON;    //任务组的属性，如签到任务，普通任务等  common普通任务，sign签到任务

    private int  taskCount;    //暂时未用到
    private TaskGroupShowType showtype;   //  1 永远显示 2：完成后不显示，3：完成后即使新增也不显示

    private TaskAward taskAward;

    private String createUserId;
    private Date createTime;
    private String modifyUserId;
    private Date modifyTime;
    private ActStatus removeStatus = ActStatus.UNACT;
    private int displayOrder;     //added by tony 2015-04-29 排序


    public TaskGroupType getType() {
        return type;
    }

    public void setType(TaskGroupType type) {
        this.type = type;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getTaskGroupId() {
        return taskGroupId;
    }

    public void setTaskGroupId(String taskGroupId) {
        this.taskGroupId = taskGroupId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public int getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(int taskCount) {
        this.taskCount = taskCount;
    }

    public TaskGroupShowType getShowtype() {
        return showtype;
    }

    public void setShowtype(TaskGroupShowType showtype) {
        this.showtype = showtype;
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

    public String getTaskGroupName() {
        return taskGroupName;
    }

    public void setTaskGroupName(String taskGroupName) {
        this.taskGroupName = taskGroupName;
    }

    public TaskAward getTaskAward() {
        return taskAward;
    }

    public void setTaskAward(TaskAward taskAward) {
        this.taskAward = taskAward;
    }

    public AppPlatform getAppPlatform() {
        return appPlatform;
    }

    public void setAppPlatform(AppPlatform appPlatform) {
        this.appPlatform = appPlatform;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
