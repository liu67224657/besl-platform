package com.enjoyf.platform.service.event.task;

import com.enjoyf.platform.service.service.ServiceException;

import java.util.Date;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/1/6
 * Description:
 */
public class TaskCompleteNoLimitStrategy implements TaskCompleteStrategy {

    @Override
    public boolean isComplete(Task task, String profileId, Date doTaskDate) throws ServiceException {
        return false;
    }
}
