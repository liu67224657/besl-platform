package com.enjoyf.platform.serv.joymeapp.quartz;

import com.enjoyf.platform.serv.gameres.quartz.WikiStatsLogic;
import com.enjoyf.platform.serv.joymeapp.JoymeAppLogic;
import com.enjoyf.platform.serv.joymeapp.JoymeAppConfig;
import com.enjoyf.platform.util.quartz.AbstractQuartzCronTrigger;
import org.quartz.SchedulerException;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-2
 * Time: 下午5:08
 * To change this template use File | Settings | File Templates.
 */
public class AppFavoritePublishQuartzCronTrigger extends AbstractQuartzCronTrigger {

    private AppFavoritePublishQuzrtzJob appFavoritePublishQuzrtzJob;
    private JoymeAppConfig cfg;
    private JoymeAppLogic joymeAppLogic;

    public AppFavoritePublishQuartzCronTrigger(JoymeAppLogic joymeAppLogic, JoymeAppConfig cfg) throws SchedulerException {
        this.cfg = cfg;
        this.joymeAppLogic = joymeAppLogic;
    }

    @Override
    public void init() throws SchedulerException {
        appFavoritePublishQuzrtzJob = new AppFavoritePublishQuzrtzJob();
        appFavoritePublishQuzrtzJob.setJobData(JoymeAppLogic.class.getName(), joymeAppLogic);

        addTriggerJob(appFavoritePublishQuzrtzJob, "AppFavoritePublishQuartzCronTrigger", "appFavoritePublishQuzrtzJob", "appFavoritePublishQuzrtzJobGroup", cfg.getAppFavPublishTriggerExp());
    }
}
