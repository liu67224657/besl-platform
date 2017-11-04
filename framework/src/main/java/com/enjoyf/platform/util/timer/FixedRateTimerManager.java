package com.enjoyf.platform.util.timer;

import com.enjoyf.platform.util.log.GAlerter;

import java.util.concurrent.*;

/**
 * <p/>
 * Description:Timer框架类，单例模式构造利用getTask()方法得到WebTimerTask接口
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
class FixedRateTimerManager {

    private static final String DEFAULT_MANAGER_NAME = "default-fixedrate-timer";

    private ConcurrentMap<String, TimerTasker> taskMap = new ConcurrentHashMap<String, TimerTasker>();

    private String name = "";
    private long timerInterval;

    public FixedRateTimerManager(long timerInterval) {
        this(DEFAULT_MANAGER_NAME, timerInterval);
    }

    public FixedRateTimerManager(String name, long timerInterval) {
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);
        pool.scheduleWithFixedDelay(new Runnable() {
                    @Override
                    public void run() {
                        reload();
                    }
                }, timerInterval, timerInterval, TimeUnit.MILLISECONDS);
        this.name = name;
        this.timerInterval = timerInterval;
    }


    public void addTask(TimerTasker timerTasker) {
        taskMap.putIfAbsent(timerTasker.getClass().getName(), timerTasker);
    }

    //加载
    private void reload() {
        for (String clazzName : taskMap.keySet()) {
            TimerTasker tasker = taskMap.get(clazzName);
            //reload it.
            try {
                tasker.run();
            } catch (Exception e) {
                GAlerter.latd("register timer run error.managerinfo :" + getManagerInfo(), e);
            }
        }
    }

    public String getName() {
        return name;
    }

    public long getTimerInterval() {
        return timerInterval;
    }

    public String getManagerInfo() {
        return "name:" + name + "- timerInterval:" + timerInterval;
    }
}
