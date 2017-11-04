package com.enjoyf.platform.util.timer.test;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
public class Main {

    public static void main(String[] args) {
        final long start = System.currentTimeMillis();

        ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);

        pool.scheduleWithFixedDelay(new Runnable() {

                    private long lastTime = start / 1000L;

                    @Override
                    public void run() {
                        synchronized (Main.class) {
                            try {
                                Main.class.wait(1000);
                                System.out.println("wait 1000");
                            } catch (InterruptedException e) {
                                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                            }
                        }

                        long now = System.currentTimeMillis() / 1000L;
//                        System.out.println("start:" + start / 1000L);
//                        System.out.println("now:" + System.currentTimeMillis() / 1000L);

                        System.out.println("interval:" + (now - lastTime));
                        lastTime = now;
                    }
                }, 0, 10000, TimeUnit.MILLISECONDS);

    }
}
