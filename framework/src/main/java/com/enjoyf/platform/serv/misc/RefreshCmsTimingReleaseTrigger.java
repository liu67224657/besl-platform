package com.enjoyf.platform.serv.misc;

import com.enjoyf.platform.db.misc.MiscHandler;
import com.enjoyf.platform.db.tools.ToolsHandler;
import com.enjoyf.platform.serv.tools.ToolsConfig;
import com.enjoyf.platform.serv.misc.quartz.RefreshCmsTimingReleaseJob;
import com.enjoyf.platform.util.quartz.AbstractQuartzCronTrigger;
import org.quartz.SchedulerException;

/**
 * Created by zhimingli on 2015/7/28.
 */
public class RefreshCmsTimingReleaseTrigger extends AbstractQuartzCronTrigger {

    //
    private MiscConfig config;
    private MiscHandler handler;


    private RefreshCmsTimingReleaseJob refreshCmsTimingReleaseJob;

    //
    public RefreshCmsTimingReleaseTrigger(MiscConfig cfg, MiscHandler h) throws SchedulerException {
        super();

        //
        this.config = cfg;
        this.handler = h;
    }

    @Override
    public void init() throws SchedulerException {
        refreshCmsTimingReleaseJob = new RefreshCmsTimingReleaseJob();
        refreshCmsTimingReleaseJob.setJobData(ToolsHandler.class.getName(), handler);

        addTriggerJob(refreshCmsTimingReleaseJob, "RefreshCmsTimingReleaseJob", "refreshCmsTimingReleaseJob", "refreshCmsTimingReleaseJobQuartzGroup", config.getRefreshcmstimingStatCronExp());
    }
}
