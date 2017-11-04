package com.enjoyf.platform.serv.gameres;

import com.enjoyf.platform.db.gameres.GameResourceHandler;
import com.enjoyf.platform.serv.gameres.quartz.WikiStatsLogic;
import com.enjoyf.platform.serv.gameres.quartz.WikiStatsQuzrtzJob;
import com.enjoyf.platform.service.gameres.GameResource;
import com.enjoyf.platform.util.quartz.AbstractQuartzCronTrigger;
import org.quartz.SchedulerException;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-2
 * Time: 下午5:08
 * To change this template use File | Settings | File Templates.
 */
public class WikiStatsQuartzCronTrigger extends AbstractQuartzCronTrigger {

    private WikiStatsQuzrtzJob wikiStatQuzrtzJob;
    private WikiStatsLogic wikiStatsLogic;
    private GameResourceConfig cfg;

    public WikiStatsQuartzCronTrigger(WikiStatsLogic wikiStatsLogic, GameResourceConfig cfg) throws SchedulerException {
        this.wikiStatsLogic = wikiStatsLogic;
        this.cfg = cfg;
    }

    @Override
    public void init() throws SchedulerException {
        wikiStatQuzrtzJob = new WikiStatsQuzrtzJob();
        wikiStatQuzrtzJob.setJobData(WikiStatsLogic.class.getName(), wikiStatsLogic);

        addTriggerJob(wikiStatQuzrtzJob, "wikiStatsQuartzCronTrigger", "wikiStatsQuzrtzJob", "wikiStatsQuartzGroup", cfg.getWikiStatsCronExp());
    }
}
