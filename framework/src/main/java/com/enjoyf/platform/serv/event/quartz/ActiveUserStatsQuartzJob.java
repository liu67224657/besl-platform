package com.enjoyf.platform.serv.event.quartz;

import com.enjoyf.platform.db.event.EventHandler;
import com.enjoyf.platform.util.quartz.FivewhQuartzJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

public class ActiveUserStatsQuartzJob extends FivewhQuartzJob {
    //
    private EventHandler eventHandler;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Date today = new Date();

        eventHandler = (EventHandler) jobExecutionContext.getJobDetail().getJobDataMap().get(EventHandler.class.getName());
    }
}
