package com.enjoyf.platform.serv.event;

import com.enjoyf.platform.db.event.EventHandler;
import com.enjoyf.platform.serv.event.quartz.ActiveUserStatsQuartzJob;
import com.enjoyf.platform.util.quartz.AbstractQuartzCronTrigger;
import org.quartz.SchedulerException;

class EventQuartzCronTrigger extends AbstractQuartzCronTrigger {
    //
    private EventConfig config;
    private EventHandler handler;

    //
    private ActiveUserStatsQuartzJob activeUserStatsQuartzJob;

    //
    public EventQuartzCronTrigger(EventConfig cfg, EventHandler h) throws SchedulerException {
        super();

        //
        this.config = cfg;
        this.handler = h;
    }

    @Override
    public void init() throws SchedulerException {
        // user active stats
        activeUserStatsQuartzJob = new ActiveUserStatsQuartzJob();
        activeUserStatsQuartzJob.setJobData(EventHandler.class.getName(), handler);

        addTriggerJob(activeUserStatsQuartzJob, "EventQuartzCronTrigger", "activeUserStatsQuartzJob", "activeUserStatsQuartzGroup", this.config.getEventStatCronExp());
    }
}
