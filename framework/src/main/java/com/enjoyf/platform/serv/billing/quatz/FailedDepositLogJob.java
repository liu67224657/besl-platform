package com.enjoyf.platform.serv.billing.quatz;

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
public class FailedDepositLogJob extends FivewhQuartzJob {
    private static final int SYNC_TIES_LIMIT = 7;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        BillingLogic logic = (BillingLogic) jobExecutionContext.getJobDetail().getJobDataMap().get(BillingLogic.class.getName());

        Map<String, String> appKey2CallbackUrl = new HashMap<String, String>();

        //查找最近 分钟 订单 120分钟之内
        List<String> dIds = new ArrayList<String>();
        String errorMsg = "";
        do {
            try {
                String depositLogId = logic.getFailedDepositLog();

                GAlerter.lan(this.getClass().getName() + " process failed depositlog:" + depositLogId);

                if (StringUtil.isEmpty(depositLogId)) {
                    break;
                }
                DepositLog depositLog = logic.getDepositLog(depositLogId);
                if (depositLog == null) {
                    GAlerter.lab(this.getClass().getName() + " process failed logid:" + depositLogId + " log is null");
                    continue;
                }
                if (depositLog.getSyncTimes() >= SYNC_TIES_LIMIT) {
                    GAlerter.lab(this.getClass().getName() + " process failed logid:" + depositLogId + " out limit");
                    continue;
                }

                if (!appKey2CallbackUrl.containsKey(depositLog.getAppKey())) {
                    AuthApp authApp = OAuthServiceSngl.get().getApp(depositLog.getAppKey());
                    if (authApp == null || StringUtil.isEmpty(authApp.getDepositCallback())) {
                        GAlerter.lab(this.getClass().getName() + " process failed logid:" + depositLogId + " app error. appkey: " + depositLog.getAppKey());
                        continue;
                    }
                    appKey2CallbackUrl.put(depositLog.getAppKey(), authApp.getDepositCallback());
                }

                String callbackUrl = appKey2CallbackUrl.get(depositLog.getAppKey());

                logic.modifyDepositLog(IntValidStatus.VALIDING, errorMsg, depositLogId);
                CpResult cpResult = null;
                try {
                    cpResult = DepositResultSender.sendToGameServer(callbackUrl, depositLog);
                    errorMsg = cpResult.getErrormsg();
                } catch (Exception e) {
                    GAlerter.lab(this.getClass().getName() + " sendToGameServer error.e: ", e);
                }

                //成功 修改状态，不成功
                if (cpResult.getSyncStatus() == IntValidStatus.VALID.getCode()) {
                    logic.modifyDepositLog(IntValidStatus.VALID, errorMsg, depositLogId);
                } else {
                    //重试次数加1 如果小于SYNC_TIES_LIMIT 放回队列中
                    depositLog.setSyncTimes(depositLog.getSyncTimes() + 1);
                    logic.increaseSyncLogTimes(1, errorMsg, depositLogId);
                    if (depositLog.getSyncTimes() < SYNC_TIES_LIMIT) {
//                        logic.pushFailedDepositLog(depositLogId);
                        dIds.add(depositLogId);
                    }
                }
            } catch (ServiceException e) {
                GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            }
        } while (logic.getFailedDepositLogLength() > 0);

        if (dIds.size() > 0) {
            logic.pushFailedDepositLog(dIds.toArray(new String[dIds.size()]));
        }
    }
}
