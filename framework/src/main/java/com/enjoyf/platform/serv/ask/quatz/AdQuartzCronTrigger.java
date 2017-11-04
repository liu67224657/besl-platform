package com.enjoyf.platform.serv.ask.quatz;

import com.enjoyf.platform.serv.ask.AskConfig;
import com.enjoyf.platform.serv.ask.AskLogic;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.Props;
import com.enjoyf.platform.util.log.GAlerter;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by pengxu on 2017/6/7.
 */
public class AdQuartzCronTrigger {

    private SchedulerFactory schedulerFactory = new StdSchedulerFactory();
    private Scheduler scheduler;

    private AdJob adJob;
    private AskLogic askLogic;

    private Date cronExp;
    private String jobName;
    private long id;
    private String validStatus;
    private final String JOB_GROUP_NAME = "AD_JOBGROUP_NAME";

    private final String TRIGGER_GROUP_NAME = "AD_TRIGGERGROUP_NAME";

    public AdQuartzCronTrigger(AskLogic askLogic) throws SchedulerException {
        this.askLogic = askLogic;
        scheduler = schedulerFactory.getScheduler();
    }

    public void setCronExp(Date cronExp) {
        this.cronExp = cronExp;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setValidStatus(String validStatus) {
        this.validStatus = validStatus;
    }

    public void init() throws SchedulerException {
        adJob = new AdJob();
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(AskLogic.class.getName(), askLogic);
        jobDataMap.put("id", id);
        jobDataMap.put("validStatus", validStatus);
        addTriggerJob(adJob.getClass(), TRIGGER_GROUP_NAME + jobName, jobName, JOB_GROUP_NAME + jobName, DateUtil.getCron(cronExp), jobDataMap);
    }

    private void addTriggerJob(Class job, String triggerName, String jobName, String groupName, String cronExp, JobDataMap jobDataMap) throws SchedulerException {
        //build the jobdetail
        JobDetail jobDetail = JobBuilder.newJob(job)
                .withIdentity(jobName, groupName)
                .usingJobData(jobDataMap)
                .build();

        // Initiate CronTrigger with its name and group name
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerName, groupName)
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExp))
                .build();

        // schedule a job with JobDetail and Trigger
        scheduler.scheduleJob(jobDetail, trigger);
        scheduler.start();
    }

    /**
     * 修改定时任务
     *
     * @param jobName    任务名称  XXX_ID
     * @param cronExp    时间
     * @param id         Advertise ID
     * @param validStaus 状态
     */
    public void modifyJobTime(String jobName, String cronExp, Long id, String validStaus) {
        try {
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(TriggerKey.triggerKey(TRIGGER_GROUP_NAME + jobName, JOB_GROUP_NAME + jobName));
            if (trigger == null) {
                adJob = new AdJob();
                JobDataMap jobDataMap = new JobDataMap();
                jobDataMap.put(AskLogic.class.getName(), askLogic);
                jobDataMap.put("id", id);
                jobDataMap.put("validStatus", validStaus);
                addTriggerJob(adJob.getClass(), TRIGGER_GROUP_NAME + jobName, jobName, JOB_GROUP_NAME + jobName, cronExp, jobDataMap);
                return;
            }
            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(cronExp)) {
                JobDetail jobDetail = scheduler.getJobDetail(JobKey.jobKey(jobName, JOB_GROUP_NAME + jobName));
                removeJob(jobName);
                addTriggerJob(jobDetail.getJobClass(), TRIGGER_GROUP_NAME + jobName, jobName, JOB_GROUP_NAME + jobName, cronExp, jobDetail.getJobDataMap());
            }
        } catch (Exception e) {
            GAlerter.lab("this modifyJobTime error:", e);
        }
    }

    public void removeJob(String jobName) {
        try {
            scheduler.pauseTrigger(TriggerKey.triggerKey(TRIGGER_GROUP_NAME + jobName, JOB_GROUP_NAME + jobName));// 停止触发器
            scheduler.unscheduleJob(TriggerKey.triggerKey(TRIGGER_GROUP_NAME + jobName, JOB_GROUP_NAME + jobName));// 移除触发器
            scheduler.deleteJob(new JobKey(jobName, JOB_GROUP_NAME + jobName));// 删除任务
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String agres[]) {
        try {
            FiveProps props = Props.instance().getServProps();
            AskConfig askConfig = new AskConfig(props);
            AskLogic askLogic = new AskLogic(askConfig);
            AdQuartzCronTrigger adQuartzCronTrigger = new AdQuartzCronTrigger(askLogic);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            adQuartzCronTrigger.setJobName("xupengtest1");
            adQuartzCronTrigger.setCronExp(new Date());

            adQuartzCronTrigger.init();

            //adQuartzCronTrigger.modifyJobTime("xupengtest1", "0/1 * * * * ? ");
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}