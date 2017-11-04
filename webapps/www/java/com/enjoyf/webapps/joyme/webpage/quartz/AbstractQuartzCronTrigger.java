package com.enjoyf.webapps.joyme.webpage.quartz;

import com.enjoyf.platform.util.log.GAlerter;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @Auther: <a mailto:yinpengyi@platform.com>Yin Pengyi</a>
 */
public abstract class AbstractQuartzCronTrigger implements QuartzCronTrigger {
    //
    private Scheduler scheduler;

    //
    public AbstractQuartzCronTrigger() throws SchedulerException {
        // Initiate a Schedule Factory
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();

        // Retrieve a scheduler from schedule factory
        scheduler = schedulerFactory.getScheduler();
    }

    public abstract void init() throws SchedulerException;

    //add a trigger job to the cron trigger.
    protected void addTriggerJob(Class jobClass, String triggerName, String jobName, String groupName, String cronExp) throws SchedulerException {
        GAlerter.lan("=============sitemap trigger add job:"+jobName+"=================");
        //build the jobdetail
        JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity(jobName, groupName)
                .build();
        // Initiate CronTrigger with its name and group name
        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerName, groupName)
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExp))
                .build();
        // schedule a job with JobDetail and Trigger
        scheduler.scheduleJob(jobDetail, trigger);
    }

    //
    public final void start() throws SchedulerException {
        GAlerter.lan("=============sitemap trigger start:"+scheduler.getSchedulerName()+"=================");
        // start the scheduler
        scheduler.start();
    }
}
