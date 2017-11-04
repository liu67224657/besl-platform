package com.enjoyf.platform.util.quartz;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
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
    protected void addTriggerJob(FivewhQuartzJob job, String triggerName, String jobName, String groupName, String cronExp) throws SchedulerException {
        //build the jobdetail
        JobDetail jobDetail = JobBuilder.newJob(job.getClass())
                .withIdentity(jobName, groupName)
                .usingJobData(job.getJobDataMap())
                .build();

        // Initiate CronTrigger with its name and group name
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerName, groupName)
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExp))
                .build();

        // schedule a job with JobDetail and Trigger
        scheduler.scheduleJob(jobDetail, trigger);
    }

    //
    public final void start() throws SchedulerException {
        // start the scheduler
        scheduler.start();
    }

}
