package com.enjoyf.webapps.joyme.dto.task;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tonydiao on 2015/5/26.
 */
public class TaskInfoDTO {
    List<TaskGroupDTO> list;
    int taskCount = 0;
    int completeCount = 0;
    int awardCount = 0;
    int unComplete = 0;

    public int getUnComplete() {
        return unComplete;
    }

    public void setUnComplete(int unComplete) {
        this.unComplete = unComplete;
    }

    public List<TaskGroupDTO> getList() {
        return list;
    }

    public void setList(List<TaskGroupDTO> list) {
        this.list = list;
    }

    public int getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(int taskCount) {
        this.taskCount = taskCount;
    }

    public int getCompleteCount() {
        return completeCount;
    }

    public void setCompleteCount(int completeCount) {
        this.completeCount = completeCount;
    }

    public int getAwardCount() {
        return awardCount;
    }

    public void setAwardCount(int awardCount) {
        this.awardCount = awardCount;
    }
}
