package com.enjoyf.platform.serv.ask.quatz;

import com.enjoyf.platform.serv.ask.AskLogic;
import com.enjoyf.platform.serv.ask.AskRedis;
import com.enjoyf.platform.serv.billing.BillingLogic;
import com.enjoyf.platform.service.IntValidStatus;
import com.enjoyf.platform.service.billing.DepositLog;
import com.enjoyf.platform.service.billing.DepositResultSender;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.thirddeposit.CpResult;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.quartz.FivewhQuartzJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ericliu on 15/12/15.
 */
public class QuestionStatusCheckJob extends FivewhQuartzJob {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        AskLogic askLogic = (AskLogic) jobExecutionContext.getJobDetail().getJobDataMap().get(AskLogic.class.getName());

        long now = System.currentTimeMillis();
        GAlerter.lan(this.getClass().getName() + "============begin." + now);
        try {
            askLogic.checkExpireQuestion();
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured e: ", e);
        }
        GAlerter.lan(this.getClass().getName() + "============finish. spent:" + ((System.currentTimeMillis() - now) / 1000));
    }
}
