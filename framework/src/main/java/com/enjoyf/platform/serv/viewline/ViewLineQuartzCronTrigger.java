package com.enjoyf.platform.serv.viewline;

import com.enjoyf.platform.serv.viewline.quartz.ViewLineAutoReloadQuartzJob;
import com.enjoyf.platform.util.quartz.AbstractQuartzCronTrigger;
import org.quartz.SchedulerException;
@Deprecated
class ViewLineQuartzCronTrigger extends AbstractQuartzCronTrigger {
    //
    private ViewLineConfig config;
    private ViewLineLogic logic;

    //
    @Deprecated
    private ViewLineAutoReloadQuartzJob viewLineAutoReloadQuartzJob;

    //
    public ViewLineQuartzCronTrigger(ViewLineConfig cfg, ViewLineLogic lgc) throws SchedulerException {
        super();

        //
        this.config = cfg;
        this.logic = lgc;
    }

    @Override
    public void init() throws SchedulerException {
        //content stats
        viewLineAutoReloadQuartzJob = new ViewLineAutoReloadQuartzJob();

        viewLineAutoReloadQuartzJob.setJobData(ViewLineLogic.class.getName(), logic);
        viewLineAutoReloadQuartzJob.setJobData(ViewLineConfig.class.getName(), config);

//        addTriggerJob(viewLineAutoReloadQuartzJob, "ViewLineQuartzCronTrigger", "viewLineAutoReloadQuartzJob", "viewLineAutoReloadQuartzGroup", this.config.getReloadCronExp());
    }
}
