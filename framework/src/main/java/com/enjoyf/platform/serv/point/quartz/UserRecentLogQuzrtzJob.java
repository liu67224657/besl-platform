package com.enjoyf.platform.serv.point.quartz;

import com.enjoyf.platform.util.quartz.FivewhQuartzJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-10-25 下午2:18
 * Description:
 */
public class UserRecentLogQuzrtzJob extends FivewhQuartzJob {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
//        PointLogic pointLogic = (PointLogic) jobExecutionContext.getJobDetail().getJobDataMap().get(PointLogic.class.getName());
//        //todo
//        pointLogic.queryRecntUserLog();

    }
}
