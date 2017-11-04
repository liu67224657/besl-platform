package com.enjoyf.platform.serv.ask.quatz;

import com.enjoyf.platform.serv.ask.AskLogic;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.ask.wiki.Advertise;
import com.enjoyf.platform.service.ask.wiki.AdvertiseField;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.quartz.FivewhQuartzJob;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;

/**
 * Created by ericliu on 15/12/15.
 */
public class AdJob extends FivewhQuartzJob {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        AskLogic askLogic = (AskLogic) jobExecutionContext.getJobDetail().getJobDataMap().get(AskLogic.class.getName());
        AdQuartzCronTrigger AdQuartzCronTrigger = null;
        try {
            AdQuartzCronTrigger = new AdQuartzCronTrigger(askLogic);
            Long id = (Long) jobExecutionContext.getJobDetail().getJobDataMap().get("id");
            String validStatus = (String) jobExecutionContext.getJobDetail().getJobDataMap().get("validStatus");

            Advertise advertise = askLogic.getAdvertise(id);
            if (advertise.getRemoveStatus().equals(ValidStatus.REMOVED)) {//已经删除的不用执行
                return;
            }

            UpdateExpress updateExpress = new UpdateExpress().set(AdvertiseField.REMOVE_STATUS, ValidStatus.getByCode(validStatus).getCode());
            askLogic.updateAdvertise(id, updateExpress);
        } catch (SchedulerException e) {
            GAlerter.lab("AdJob SchedulerException error", e);
        } catch (ServiceException e) {
            GAlerter.lab("AdJob updateAdvertise error", e);
        } finally {
            AdQuartzCronTrigger.removeJob(jobExecutionContext.getJobDetail().getKey().getName());
        }
    }
}
