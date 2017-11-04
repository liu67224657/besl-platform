package com.enjoyf.platform.serv.ask.quatz;

import com.enjoyf.platform.serv.ask.AskLogic;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.quartz.FivewhQuartzJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


public class TimedReleaseJob extends FivewhQuartzJob {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        AskLogic askLogic = (AskLogic) jobExecutionContext.getJobDetail().getJobDataMap().get(AskLogic.class.getName());

        try {
            askLogic.timedRelease();
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured e: ", e);
        }
    }
}
