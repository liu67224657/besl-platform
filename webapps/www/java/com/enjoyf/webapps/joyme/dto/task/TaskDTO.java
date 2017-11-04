package com.enjoyf.webapps.joyme.dto.task;

import com.enjoyf.platform.service.event.task.TaskAward;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/1/8
 * Description:
 */
public class TaskDTO {
    private String taskid;
    private String name;
    private TaskAward taskAward;
    private String gourpid; //任务的分组

    private int type;
    private int overtimes = 1;

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public TaskAward getTaskAward() {
        return taskAward;
    }

    public void setTaskAward(TaskAward taskAward) {
        this.taskAward = taskAward;
    }

    public String getGourpid() {
        return gourpid;
    }

    public void setGourpid(String gourpid) {
        this.gourpid = gourpid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getOvertimes() {
        return overtimes;
    }

    public void setOvertimes(int overtimes) {
        this.overtimes = overtimes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
