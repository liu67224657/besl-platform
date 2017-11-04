package com.enjoyf.platform.service.joymeapp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-8-1
 * Time: 下午3:33
 * To change this template use File | Settings | File Templates.
 */
public class AppPushFactory {

    private static AppPushFactory instance;

    private static Map<String, AppPushProcessor> map = new HashMap<String, AppPushProcessor>();


    public static AppPushFactory get() {
        if (instance == null) {
            synchronized (AppPushFactory.class) {
                if (instance == null) {
                    instance = new AppPushFactory();
                }
            }
        }
        return instance;
    }

    public AppPushProcessor factory(AppPushChannel pushChannel, AppPlatform platform) {
        String key = "" + pushChannel.getCode() + "_" + platform.getCode();
        AppPushProcessor returnProcessor = map.containsKey(key) ? map.get(key) : null;
        if (returnProcessor == null) {
            synchronized (map) {
                if (platform.equals(AppPlatform.IOS)) {
                    if (pushChannel.equals(AppPushChannel.DEFAULT)) {
                        returnProcessor = new AppPushDefaultIOSProcessor();
                    } else if (pushChannel.equals(AppPushChannel.UMENG_DEV) || pushChannel.equals(AppPushChannel.UMENG_PROD)) {
                        returnProcessor = new AppPushUmengIosProcessor();
                    }
                } else if (platform.equals(AppPlatform.ANDROID)) {
                    if (pushChannel.equals(AppPushChannel.DEFAULT)) {
                        returnProcessor = new AppPushDefaultAndroidProcessor();
                    } else if (pushChannel.equals(AppPushChannel.UMENG_DEV) || pushChannel.equals(AppPushChannel.UMENG_PROD)) {
                        returnProcessor = new AppPushUmengAndroidProcessor();
                    }

                }
            }

            if (returnProcessor != null) {
                map.put(key, returnProcessor);
            }
        }

        return returnProcessor;
    }
}
