package com.enjoyf.webapps.joyme.dto.task;

import com.enjoyf.platform.service.event.task.Task;
import com.enjoyf.platform.service.event.task.TaskGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tonydiao on 2015/5/7.
 */
public class TaskGroupDTO {
    private String groupId;
    private TaskGroup taskGroup;
    private int completedNum;
    private int getAwardNum;
    private String groupName;
    List<Task> taskList = new ArrayList<Task>();


    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public TaskGroup getTaskGroup() {
        return taskGroup;
    }

    public void setTaskGroup(TaskGroup taskGroup) {
        this.taskGroup = taskGroup;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getCompletedNum() {
        return completedNum;
    }

    public void setCompletedNum(int completedNum) {
        this.completedNum = completedNum;
    }

    public int getGetAwardNum() {
        return getAwardNum;
    }

    public void setGetAwardNum(int getAwardNum) {
        this.getAwardNum = getAwardNum;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }
}
