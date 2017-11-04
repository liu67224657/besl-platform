package com.enjoyf.platform.serv.ask.quatz;

import com.enjoyf.platform.serv.ask.AskLogic;
import com.enjoyf.platform.util.quartz.AbstractQuartzCronTrigger;
import org.quartz.SchedulerException;


public class VerifyNoticeQuartzCronTrigger extends AbstractQuartzCronTrigger {

    private VerifyNoticeJob verifyNoticeJob;
    private AskLogic askLogic;


    //每5分时 执行一次
    private String cron_exp = "0 */59 * * * ?";

    public VerifyNoticeQuartzCronTrigger(AskLogic askLogic, String cron_exp) throws SchedulerException {
        this.askLogic = askLogic;
        this.cron_exp = cron_exp;
    }

    @Override
    public void init() throws SchedulerException {
        verifyNoticeJob = new VerifyNoticeJob();
        verifyNoticeJob.setJobData(AskLogic.class.getName(), askLogic);
        addTriggerJob(verifyNoticeJob, "VerifyNoticeQuartzCronTrigger", "VerifyNoticeJob", "VerifyNoticeJob", cron_exp);

    }
}
