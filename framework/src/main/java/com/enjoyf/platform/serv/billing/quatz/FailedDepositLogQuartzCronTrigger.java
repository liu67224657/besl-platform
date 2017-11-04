package com.enjoyf.platform.serv.billing.quatz;

import com.enjoyf.platform.serv.billing.BillingConfig;
import com.enjoyf.platform.serv.billing.BillingLogic;
import com.enjoyf.platform.util.quartz.AbstractQuartzCronTrigger;
import org.quartz.SchedulerException;


public class FailedDepositLogQuartzCronTrigger extends AbstractQuartzCronTrigger {

    private FailedDepositLogJob failedDepositLogJob;
    private BillingLogic billingLogic;

    private BillingConfig config;
    //0 */2 * * * ? 每2分钟执行一次

    //每2个小时执行一次
    private String cron_exp = "0 */59 * * * ?";

    public FailedDepositLogQuartzCronTrigger(BillingLogic billingLogic, BillingConfig config) throws SchedulerException {
        this.billingLogic = billingLogic;
        this.config = config;
    }

    @Override
    public void init() throws SchedulerException {
        failedDepositLogJob = new FailedDepositLogJob();
        failedDepositLogJob.setJobData(BillingLogic.class.getName(), billingLogic);
        addTriggerJob(failedDepositLogJob, "failedDepostLogQuartzCronTrigger", "failedDepositLogJob", "failedDepositLogJob", config.getFailedDepositLogCronExp());
    }
}
