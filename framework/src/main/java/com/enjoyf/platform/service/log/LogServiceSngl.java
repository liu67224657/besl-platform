package com.enjoyf.platform.service.log;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 14-9-26
 * Time: 下午2:30
 * To change this template use File | Settings | File Templates.
 */
public class LogServiceSngl {

    private static LogServiceImpl instance;


    public static LogServiceImpl get() {
        if (instance == null) {
            synchronized (LogServiceSngl.class) {
                if (instance == null) {
                    instance = new LogServiceImpl();
                }
            }
        }

        return instance;
    }

}
