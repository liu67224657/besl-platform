package com.enjoyf.webapps.joyme.webpage.quartz;

import com.enjoyf.platform.util.log.GAlerter;
import org.quartz.*;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-10-25 下午2:16
 * Description:
 */
public class SiteMapQuartzCronTrigger extends AbstractQuartzCronTrigger {

    public SiteMapQuartzCronTrigger() throws SchedulerException {
    }

    @Override
    public void init() throws SchedulerException {
        GAlerter.lan("=============sitemap trigger init=================");
        //每天1点  0 0 1 ? * *
        //0 0/30 * * * ?
        addTriggerJob(SiteMapQuartzJob.class, "SiteMapQuartzCronTrigger", "SiteMapQuartzJob", "SiteMapQuartzJobGroup", "0 40 3 * * ?");
    }
}
