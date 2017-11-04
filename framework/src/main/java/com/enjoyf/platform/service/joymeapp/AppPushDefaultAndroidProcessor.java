package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.service.event.system.AppPushEvent;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-9-2
 * Time: 上午9:42
 * To change this template use File | Settings | File Templates.
 */
public class AppPushDefaultAndroidProcessor extends AbstractAppPushProcessor implements AppPushProcessor {
    @Override
    public void sendPushMessage(AppPushEvent appPushEvent) {
//        try {
//            JoymeAppServiceSngl.get().putLastPushMessage(pushMessage);
//        } catch (Exception e) {
//            GAlerter.lad(this.getClass().getName() + " occur Exception.e", e);
//        }
    }
}
