package com.enjoyf.platform.serv.point.quartz;

import com.enjoyf.platform.serv.point.PointLogic;
import com.enjoyf.platform.util.quartz.AbstractQuartzCronTrigger;
import org.quartz.SchedulerException;

/**
 * Created by pengxu on 2016/12/2.
 */
public class PrestigeQuartzCronTrigger extends AbstractQuartzCronTrigger {
    private PrestigeQuartzJob prestigeQuartzJob;

    private PointLogic pointLogic;
    //每月1号执行
    private String cron_exp = "0 0 0 1 * ?";


    public PrestigeQuartzCronTrigger(PointLogic pointLogic) throws SchedulerException {
        this.pointLogic = pointLogic;
    }

    @Override
    public void init() throws SchedulerException {
        prestigeQuartzJob = new PrestigeQuartzJob();
        prestigeQuartzJob.setJobData(PointLogic.class.getName(), pointLogic);
        addTriggerJob(prestigeQuartzJob, "PrestigeQuartzCronTrigger", "PrestigeQuartzJob", "PrestigeQuartzJob", cron_exp);
    }
}
