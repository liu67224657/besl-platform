package com.enjoyf.platform.service.event.task;

import com.enjoyf.platform.service.service.ServiceException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/1/6
 * Description:策略工厂类
 */
public class TaskCompleteStrategyFactory {
    private static Map<TaskType, TaskCompleteStrategy> map = new HashMap<TaskType, TaskCompleteStrategy>();

    static {
        map.put(TaskType.NO_LIMIT, new TaskCompleteNoLimitStrategy());
        map.put(TaskType.ONCE_BY_DAY, new TaskCompleteOnceByDayStrategy());
        map.put(TaskType.ONCE, new TaskCompleteOnceStrategy());
    }

    /**
     * 根据task的type返回策略类来计算任务是否完成
     *
     * @param task
     * @param profileId
     * @param doTaskDate
     * @return true 完成了 false 未完成
     * @throws ServiceException
     */
    public static boolean isComplete(Task task, String profileId, Date doTaskDate) throws ServiceException {
        TaskCompleteStrategy stragy = map.get(task.getTaskType());
        return stragy.isComplete(task, profileId, doTaskDate);
    }
}
