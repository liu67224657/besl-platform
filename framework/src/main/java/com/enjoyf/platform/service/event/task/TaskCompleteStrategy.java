package com.enjoyf.platform.service.event.task;

import com.enjoyf.platform.service.service.ServiceException;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/1/6
 * Description: 策略类接口3根据tasktype有三种实现类
 */
public interface TaskCompleteStrategy {

    /**
     * 根据task的type返回策略类来计算任务是否完成
     *
     * @param task
     * @param profileId
     * @param doTaskDate
     * @return true 完成了 false 未完成
     * @throws ServiceException
     */
    public boolean isComplete(Task task, String profileId, Date doTaskDate) throws ServiceException;
}
