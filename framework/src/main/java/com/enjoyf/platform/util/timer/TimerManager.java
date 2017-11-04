package com.enjoyf.platform.util.timer;

/**
 * <p/>
 * Description:Timer框架类，单例模式构造利用getTask()方法得到WebTimerTask接口
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
public interface TimerManager {

    public void addTask(TimerTasker timerTasker,long timerInterval);
}
