package com.enjoyf.platform.service.event.task;

import java.util.Date;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/1/6
 * Description:
 */
public interface TaskAwardStrategy {

    public boolean award(String taskId, TaskAward taskAward, String profileId, String appkey, Date taskDate, String uno);
}
