/**
 * (C) 2010 Fivewh platform platform.com
 */
package com.enjoyf.platform.util.quartz;

import org.quartz.Job;
import org.quartz.JobDataMap;

public abstract class FivewhQuartzJob implements Job {
    private JobDataMap jobDataMap = new JobDataMap();

    public void setJobData(String key, Object obj) {
        jobDataMap.put(key, obj);
    }

    public Object getJobData(String key) {
        return jobDataMap.get(key);
    }

    public JobDataMap getJobDataMap() {
        return jobDataMap;
    }
}
