package com.enjoyf.platform.service.event.task;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.event.EventServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;

import java.util.Date;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/1/6
 * Description: 永久一次
 */
public class TaskCompleteOnceStrategy implements TaskCompleteStrategy {

    @Override
    public boolean isComplete(Task task, String profileId, Date doTaskDate) throws ServiceException {
        String taskLogId = TaskUtil.getTaskLogId(profileId, task.getTaskId(), task.getTaskType(),task.getTaskAction());
        TaskLog taskLog = EventServiceSngl.get().getTaskLog(taskLogId);
        if (taskLog == null) {
            return false;
        }

        if (task.getOverTimes() > 1) {
            return !(ActStatus.ACTING.equals(taskLog.getOverStatus()) && taskLog.getOverTimes() < task.getOverTimes());
        }

        return true;
    }
}
