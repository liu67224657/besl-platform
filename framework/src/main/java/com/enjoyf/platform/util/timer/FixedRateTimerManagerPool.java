package com.enjoyf.platform.util.timer;

import com.enjoyf.platform.util.log.GAlerter;

import java.util.HashMap;
import java.util.Map;

/**
 * <p/>
 * Description:Timer框架类，单例模式构造利用getTask()方法得到WebTimerTask接口
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
public class FixedRateTimerManagerPool implements TimerManager {
    private Map<Long, FixedRateTimerManager> map = new HashMap<Long, FixedRateTimerManager>();

    private static volatile FixedRateTimerManagerPool instance;


    public static FixedRateTimerManagerPool get() {
        if (instance == null) {
            synchronized (FixedRateTimerManagerPool.class) {
                if (instance == null) {
                    instance = new FixedRateTimerManagerPool();
                }
            }
        }

        return instance;
    }

    @Override
    public synchronized void addTask(TimerTasker timerTasker, long timerInterval) {
        if (!map.containsKey(timerInterval)) {
            FixedRateTimerManager timerManager = new FixedRateTimerManager(timerInterval);

            GAlerter.latd("register timer :" + timerManager.getManagerInfo());
            map.put(timerInterval, timerManager);
        }

        map.get(timerInterval).addTask(timerTasker);
    }

    public void addTask(TimerTasker timerTasker, long timerInterval, String taskName) {
        if (!map.containsKey(timerInterval)) {
            FixedRateTimerManager timerManager = new FixedRateTimerManager(taskName,timerInterval);

            GAlerter.latd("register timer :" + timerManager.getManagerInfo());

            map.put(timerInterval, timerManager);

        }

        map.get(timerInterval).addTask(timerTasker);
    }
}
