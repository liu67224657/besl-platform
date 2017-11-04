package com.enjoyf.platform.props.hotdeploy;

import com.enjoyf.platform.props.EnvConfig;
import com.enjoyf.platform.util.log.GAlerter;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * You can register your hotdeployable config files here, this is the single
 * entry point of our configuration files. There is only one hotdeployable directory,
 * it serves both for web and platform.
 *
 * @author Daniel
 */
public class HotdeployConfigFactory {

    private static volatile HotdeployConfigFactory instance;

    private ConcurrentMap<String, HotdeployConfig> configs = new ConcurrentHashMap<String, HotdeployConfig>();

    public static HotdeployConfigFactory get() {
        if (instance == null) {
            synchronized (HotdeployConfigFactory.class) {
                if (instance == null) {
                    instance = new HotdeployConfigFactory();
                }
            }
        }

        return instance;
    }

    private HotdeployConfigFactory() {
        new Timer().scheduleAtFixedRate(
                new TimerTask() {
                    @Override
                    public void run() {
                        reload();
                    }
                },
                EnvConfig.get().getHotdeployCheckIntervalMsec(),
                EnvConfig.get().getHotdeployCheckIntervalMsec()
        );
    }

    @SuppressWarnings("unchecked")
    public <T extends HotdeployConfig> T getConfig(Class<T> clazz) {
        if (!configs.containsKey(clazz.getName())) {
            try {
                T config = clazz.newInstance();

                configs.putIfAbsent(clazz.getName(), config);
            } catch (InstantiationException e) {
                GAlerter.lab("Cannot initialize configuration: " + clazz);
                throw new IllegalArgumentException("Class not supported: " + clazz);
            } catch (IllegalAccessException e) {
                GAlerter.lab("Cannot initialize configuration: " + clazz);
                throw new IllegalArgumentException("Class not supported: " + clazz);
            }
        }

        return (T) configs.get(clazz.getName());
    }

    public void reload() {
        //chech all the initialized hotdeploy configure.
        for (String clazzName : configs.keySet()) {
            HotdeployConfig config = configs.get(clazzName);
            //if changed.
            if (config.isModified()) {
                //reload it.
                config.reload();
            }
        }
    }

}
