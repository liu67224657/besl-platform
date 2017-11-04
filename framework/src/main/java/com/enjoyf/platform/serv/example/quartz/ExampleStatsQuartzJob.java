package com.enjoyf.platform.serv.example.quartz;

import com.enjoyf.platform.db.example.ExampleHandler;
import com.enjoyf.platform.util.quartz.FivewhQuartzJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * THE JOB CLASS MUST BEEN PUBLIC.
 */
public class ExampleStatsQuartzJob extends FivewhQuartzJob {
    //
    private static final Logger logger = LoggerFactory.getLogger(ExampleStatsQuartzJob.class);

    //
    private ExampleHandler exampleHandler;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Date today = new Date();

        exampleHandler = (ExampleHandler) jobExecutionContext.getJobDetail().getJobDataMap().get(ExampleHandler.class.getName());

        logger.info("ExampleStatsQuartzJob starts executing.");
    }
}
