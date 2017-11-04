package com.enjoyf.platform.serv.joymeapp.quartz;

import com.enjoyf.platform.serv.joymeapp.JoymeAppLogic;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.quartz.FivewhQuartzJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-2
 * Time: 下午5:05
 * To change this template use File | Settings | File Templates.
 */
public class AppFavoritePublishQuzrtzJob extends FivewhQuartzJob {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JoymeAppLogic statLogic = (JoymeAppLogic) jobExecutionContext.getJobDetail().getJobDataMap().get(JoymeAppLogic.class.getName());

        try {
//            statLogic.publishAllUserFavoriteApp();
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + "execute JobExecutionContext.occured Exception:", e);
        }

    }
}
