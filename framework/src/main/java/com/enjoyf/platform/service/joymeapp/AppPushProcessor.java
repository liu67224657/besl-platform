package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.service.event.system.AppPushEvent;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-9-2
 * Time: 上午9:27
 * To change this template use File | Settings | File Templates.
 */
public interface AppPushProcessor {

    public void sendPushMessage(AppPushEvent appPushEvent);

}
