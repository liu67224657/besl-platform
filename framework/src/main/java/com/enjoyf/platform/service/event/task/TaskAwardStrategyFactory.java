package com.enjoyf.platform.service.event.task;

import com.enjoyf.platform.service.service.ServiceException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/1/6
 * Description:
 */
public class TaskAwardStrategyFactory {
    private static Map<Integer, TaskAwardStrategy> map = new HashMap<Integer, TaskAwardStrategy>();

    static {
        map.put(TaskAwardType.POINT.getCode(), new TaskAwardPointStrategy());
    }

    public static boolean sendAward(String taskId, TaskAward taskAward, String profileId, String appkey, Date date, String uno) throws ServiceException {
        TaskAwardStrategy strategy = map.get(taskAward.getType());
        return strategy.award(taskId, taskAward, profileId, appkey, date, uno);
    }

}
