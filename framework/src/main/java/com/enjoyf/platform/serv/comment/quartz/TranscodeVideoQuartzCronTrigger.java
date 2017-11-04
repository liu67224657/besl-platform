package com.enjoyf.platform.serv.comment.quartz;

import com.enjoyf.platform.serv.comment.CommentConfig;
import com.enjoyf.platform.serv.comment.CommentLogic;
import com.enjoyf.platform.util.quartz.AbstractQuartzCronTrigger;

import org.quartz.SchedulerException;


public class TranscodeVideoQuartzCronTrigger extends AbstractQuartzCronTrigger {

    private TranscodeVideoQuzrtzJob transcodeVideoQuzrtzJob;
    private CommentLogic commentLogic;

    private CommentConfig config;

    //每1分钟执行一次
    private String cron_exp = "0 */1 * * * ?";

    public TranscodeVideoQuartzCronTrigger(CommentLogic commentLogic, CommentConfig config) throws SchedulerException {
        this.commentLogic = commentLogic;
        this.config = config;
    }

    @Override
    public void init() throws SchedulerException {
    	transcodeVideoQuzrtzJob = new TranscodeVideoQuzrtzJob();
        transcodeVideoQuzrtzJob.setJobData(CommentLogic.class.getName(), commentLogic);
        addTriggerJob(transcodeVideoQuzrtzJob, "transcodeVideoQuartzCronTrigger", "transcodeVideoQuzrtzJob", "transcodeVideoQuzrtzJob", cron_exp);
    }
}
