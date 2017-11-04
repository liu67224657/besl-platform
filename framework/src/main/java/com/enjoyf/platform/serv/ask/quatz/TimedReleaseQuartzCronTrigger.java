package com.enjoyf.platform.serv.ask.quatz;

import com.enjoyf.platform.serv.ask.AskLogic;
import com.enjoyf.platform.util.quartz.AbstractQuartzCronTrigger;
import org.quartz.SchedulerException;


public class TimedReleaseQuartzCronTrigger extends AbstractQuartzCronTrigger {

    private TimedReleaseJob timedReleaseJob;
    private AskLogic askLogic;


    //每10分时 执行一次
    private String cron_exp = "* 0/10 * * * ?";

    public TimedReleaseQuartzCronTrigger(AskLogic askLogic, String cron_exp) throws SchedulerException {
        this.askLogic = askLogic;
        this.cron_exp = cron_exp;
    }

    @Override
    public void init() throws SchedulerException {
        timedReleaseJob = new TimedReleaseJob();
        timedReleaseJob.setJobData(AskLogic.class.getName(), askLogic);
        addTriggerJob(timedReleaseJob, "TimedReleaseQuartzCronTrigger", "TimedReleaseJob", "TimedReleaseJob", cron_exp);

    }
}
