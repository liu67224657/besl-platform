package com.enjoyf.webapps.tools.weblogic.dto.joymeApp;

import com.enjoyf.platform.service.joymeapp.PushMessage;


/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 13-6-1
 * Time: 上午1:34
 * To change this template use File | Settings | File Templates.
 */
public class PushMessageDTO {
    private PushMessage pushMessage;
    private String appName;

    public PushMessage getPushMessage() {
        return pushMessage;
    }

    public void setPushMessage(PushMessage pushMessage) {
        this.pushMessage = pushMessage;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
