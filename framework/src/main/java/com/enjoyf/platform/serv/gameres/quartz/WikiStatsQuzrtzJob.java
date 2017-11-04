package com.enjoyf.platform.serv.gameres.quartz;

import com.enjoyf.platform.db.gameres.GameResourceHandler;
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
public class WikiStatsQuzrtzJob extends FivewhQuartzJob {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        WikiStatsLogic statLogic = (WikiStatsLogic) jobExecutionContext.getJobDetail().getJobDataMap().get(WikiStatsLogic.class.getName());

        statLogic.statsWiki();
    }
}
