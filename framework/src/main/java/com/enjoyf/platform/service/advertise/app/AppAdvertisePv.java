package com.enjoyf.platform.service.advertise.app;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-6-16
 * Time: 下午2:55
 * To change this template use File | Settings | File Templates.
 */
public class AppAdvertisePv implements Serializable {
    
    private long pvId;
    private String appkey;
    private int platform;//平台
    private String deviceId;//设备ID
    private long publishid;//发布广告ID
    private long adId;//广告ID
    private String ip;
    private Date createtime;

    public long getPvId() {
        return pvId;
    }

    public void setPvId(long pvId) {
        this.pvId = pvId;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public long getAdId() {
        return adId;
    }

    public void setAdId(long adId) {
        this.adId = adId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public long getPublishid() {
        return publishid;
    }

    public void setPublishid(long publishid) {
        this.publishid = publishid;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
