package com.enjoyf.platform.service.message;

import com.enjoyf.platform.service.profile.ProfileMobileDevice;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-8-12
 * Time: 上午10:46
 * To change this template use File | Settings | File Templates.
 */
public class PushMsg implements Serializable{

    private Message message;
    private PushMessage pushMessage;
    private ProfileMobileDevice profileMobileDevice;
    private boolean isTest;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public PushMessage getPushMessage() {
        return pushMessage;
    }

    public void setPushMessage(PushMessage pushMessage) {
        this.pushMessage = pushMessage;
    }

    public ProfileMobileDevice getProfileMobileDevice() {
        return profileMobileDevice;
    }

    public void setProfileMobileDevice(ProfileMobileDevice profileMobileDevice) {
        this.profileMobileDevice = profileMobileDevice;
    }

    public boolean isTest() {
        return isTest;
    }

    public void setTest(boolean test) {
        isTest = test;
    }
}
