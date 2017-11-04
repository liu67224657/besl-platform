package com.enjoyf.platform.serv.example;

import com.enjoyf.platform.db.example.ExampleHandler;
import com.enjoyf.platform.serv.example.quartz.ExampleStatsQuartzJob;
import com.enjoyf.platform.serv.example.quartz.ExampleStatsQuartzJob2;
import com.enjoyf.platform.util.quartz.AbstractQuartzCronTrigger;
import org.quartz.SchedulerException;

class ExampleQuartzCronTrigger extends AbstractQuartzCronTrigger {
    //
    private ExampleConfig config;
    private ExampleHandler handler;

    //
    public ExampleQuartzCronTrigger(ExampleConfig config, ExampleHandler handler) throws SchedulerException {
        super();

        //
        this.config = config;
        this.handler = handler;
   }

    @Override
    public void init() throws SchedulerException {
        //add all the job into the trigger.
        // add the exampleStatsQuartzJob
        ExampleStatsQuartzJob exampleStatsQuartzJob = new ExampleStatsQuartzJob();
        exampleStatsQuartzJob.setJobData(ExampleHandler.class.getName(), this.handler);

        addTriggerJob(exampleStatsQuartzJob, ExampleStatsQuartzJob.class.getName(), ExampleStatsQuartzJob.class.getName(), "exampleStatsQuartzGroup", this.config.getStatCronExp());

        // add the exampleStatsQuartzJob2
        ExampleStatsQuartzJob2 exampleStatsQuartzJob2 = new ExampleStatsQuartzJob2();
        exampleStatsQuartzJob2.setJobData(ExampleHandler.class.getName(), this.handler);

        addTriggerJob(exampleStatsQuartzJob2, ExampleStatsQuartzJob2.class.getName(), ExampleStatsQuartzJob2.class.getName(), "exampleStatsQuartzGroup", this.config.getStatCronExp());

    }
}
