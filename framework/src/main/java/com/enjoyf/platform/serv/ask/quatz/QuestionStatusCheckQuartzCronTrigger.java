package com.enjoyf.platform.serv.ask.quatz;

import com.enjoyf.platform.serv.ask.AskLogic;
import com.enjoyf.platform.serv.ask.AskRedis;
import com.enjoyf.platform.serv.billing.BillingLogic;
import com.enjoyf.platform.util.quartz.AbstractQuartzCronTrigger;
import org.quartz.SchedulerException;


public class QuestionStatusCheckQuartzCronTrigger extends AbstractQuartzCronTrigger {

    private QuestionStatusCheckJob checkJob;
    private AskLogic askLogic;

    //0 */2 * * * ? 每2分钟执行一次

    //每2个小时执行一次
    private String cron_exp = "0 */59 * * * ?";

    public QuestionStatusCheckQuartzCronTrigger(AskLogic askLogic, String cron_exp) throws SchedulerException {
        this.askLogic = askLogic;
        this.cron_exp = cron_exp;
    }

    @Override
    public void init() throws SchedulerException {
        checkJob = new QuestionStatusCheckJob();
        checkJob.setJobData(AskLogic.class.getName(), askLogic);
        addTriggerJob(checkJob, "QuestionStatusCheckQuartzCronTrigger", "QuestionStatusCheckJob", "QuestionStatusCheckJob", cron_exp);
    }
}
