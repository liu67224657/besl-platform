package com.enjoyf.platform.serv.ask.quatz;

import com.enjoyf.platform.serv.ask.AskLogic;
import com.enjoyf.platform.util.quartz.AbstractQuartzCronTrigger;
import org.quartz.SchedulerException;


public class ViewsumHotrankQuartzCronTrigger extends AbstractQuartzCronTrigger {

    private ViewsumHotrankJob viewsumHotrankJob;
    private AskLogic askLogic;


    //每小时50分时 执行一次
    private String cron_exp = "0 50 * * * ?";

    public ViewsumHotrankQuartzCronTrigger(AskLogic askLogic, String cron_exp) throws SchedulerException {
        this.askLogic = askLogic;
        this.cron_exp = cron_exp;
    }

    @Override
    public void init() throws SchedulerException {
        viewsumHotrankJob = new ViewsumHotrankJob();
        viewsumHotrankJob.setJobData(AskLogic.class.getName(), askLogic);
        addTriggerJob(viewsumHotrankJob, "ViewsumHotrankQuartzCronTrigger", "ViewsumHotrankJob", "ViewsumHotrankJob", cron_exp);
    }
}
