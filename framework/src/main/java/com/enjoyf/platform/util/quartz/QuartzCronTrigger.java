package com.enjoyf.platform.util.quartz;

import org.quartz.SchedulerException;

/**
 * @Auther: <a mailto:yinpengyi@platform.com>Yin Pengyi</a>
 */
public interface QuartzCronTrigger {
    //
    public void init() throws SchedulerException;

    //
    public void start() throws SchedulerException;

}
