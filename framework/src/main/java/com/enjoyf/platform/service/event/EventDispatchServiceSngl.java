/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.event;

/**
 * @Auther: <a mailto:yinpengyi@enjoyf.com>Yin Pengyi</a>
 */
public class EventDispatchServiceSngl {
    private static EventDispatchService instance;

    public static synchronized void set(EventDispatchService service) {
        instance = service;
    }

    public static synchronized EventDispatchService get() {
        if (instance == null) {
            create();
        }

        return instance;
    }

    private static void create() {
        instance = new EventDispatchServiceImpl();
    }

}
