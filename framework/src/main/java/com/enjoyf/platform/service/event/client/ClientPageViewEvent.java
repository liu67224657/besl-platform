package com.enjoyf.platform.service.event.client;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-19
 * Time: 下午7:37
 * To change this template use File | Settings | File Templates.
 */
public class ClientPageViewEvent extends ClientEvent {
    private String clientId;
    private String appKey;
    private int platform;

    //the pageview locaiton and location id.
    private List<ClientPageView> pageViewList;

    private String device;
    private String screen;

    private String os;
    private String osVersion;

    //the event date and ip;
    private String ip;

    public ClientPageViewEvent() {
        super(ClientEventType.PAGE_VEIW_EVENT);
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }



    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getScreen() {
        return screen;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public List<ClientPageView> getPageViewList() {
        return pageViewList;
    }

    public void setPageViewList(List<ClientPageView> pageViewList) {
        this.pageViewList = pageViewList;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
