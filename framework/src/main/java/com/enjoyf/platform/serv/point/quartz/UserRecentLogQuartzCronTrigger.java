package com.enjoyf.platform.serv.point.quartz;

import com.enjoyf.platform.serv.point.PointConfig;
import com.enjoyf.platform.serv.point.PointLogic;
import com.enjoyf.platform.util.quartz.AbstractQuartzCronTrigger;
import org.quartz.SchedulerException;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-10-25 下午2:16
 * Description:
 */
public class UserRecentLogQuartzCronTrigger extends AbstractQuartzCronTrigger {

    private UserRecentLogQuzrtzJob quzrtzJob;
    private PointLogic pointLogic;
    private PointConfig cfg;


    public UserRecentLogQuartzCronTrigger(PointLogic pointLogic, PointConfig cfg) throws SchedulerException {
        super();
        this.pointLogic = pointLogic;
        this.cfg = cfg;
    }

    @Override
    public void init() throws SchedulerException {
//        quzrtzJob = new UserRecentLogQuzrtzJob();
//        quzrtzJob.setJobData(PointLogic.class.getName(), pointLogic);
//
//        addTriggerJob(quzrtzJob, "userRecentLogQuartzCronTrigger", "userRecentLogQuzrtzJob", "wikiStatsQuartzGroup", "0 0 1 * * ?");
    }
}
