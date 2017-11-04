package com.enjoyf.webapps.tools.weblogic.dto.joymeApp;

import com.enjoyf.platform.service.joymeapp.ActivityTopMenu;
import com.enjoyf.platform.service.joymeappconfig.AppChannel;
import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-9-17
 * Time: 下午3:06
 * To change this template use File | Settings | File Templates.
 */
public class ClientTopMenuDTO {
    private ActivityTopMenu activityTopMenu;
    private AppChannel appChannel;

    public ActivityTopMenu getActivityTopMenu() {
        return activityTopMenu;
    }

    public void setActivityTopMenu(ActivityTopMenu activityTopMenu) {
        this.activityTopMenu = activityTopMenu;
    }

    public AppChannel getAppChannel() {
        return appChannel;
    }

    public void setAppChannel(AppChannel appChannel) {
        this.appChannel = appChannel;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
