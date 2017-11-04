package com.enjoyf.platform.service.event.system;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.joymeapp.AppEnterpriserType;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.joymeapp.PushListType;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 15-2-5
 * Time: 下午6:11
 * To change this template use File | Settings | File Templates.
 */
public class AppPushEvent extends SystemEvent{
    private String appKey;
    private AppPlatform platform;
    private AppEnterpriserType enterpriseType;
    private String subject;
    private String icon;
    private String context;
    private String sound;
    private int badge;
    private String profileIds;
    private String devices;
    private String channel;
    private String version;
    private String tags;
    private Date sendDate;
    private ActStatus sendStatus;
    private int jt;
    private String ji;
    private String ip;

    //pushmessage id
    private long pushMsgId;
    private PushListType pushListType;

    public AppPushEvent() {
        super(SystemEventType.APP_PUSH_EVENT);
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public AppPlatform getPlatform() {
        return platform;
    }

    public void setPlatform(AppPlatform platform) {
        this.platform = platform;
    }

    public AppEnterpriserType getEnterpriseType() {
        return enterpriseType;
    }

    public void setEnterpriseType(AppEnterpriserType enterpriseType) {
        this.enterpriseType = enterpriseType;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public int getBadge() {
        return badge;
    }

    public void setBadge(int badge) {
        this.badge = badge;
    }

    public String getProfileIds() {
        return profileIds;
    }

    public void setProfileIds(String profileIds) {
        this.profileIds = profileIds;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public ActStatus getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(ActStatus sendStatus) {
        this.sendStatus = sendStatus;
    }

    public int getJt() {
        return jt;
    }

    public void setJt(int jt) {
        this.jt = jt;
    }

    public String getJi() {
        return ji;
    }

    public void setJi(String ji) {
        this.ji = ji;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public long getPushMsgId() {
        return pushMsgId;
    }

    public void setPushMsgId(long pushMsgId) {
        this.pushMsgId = pushMsgId;
    }

    public String getDevices() {
        return devices;
    }

    public void setDevices(String devices) {
        this.devices = devices;
    }

    public PushListType getPushListType() {
        return pushListType;
    }

    public void setPushListType(PushListType pushListType) {
        this.pushListType = pushListType;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
