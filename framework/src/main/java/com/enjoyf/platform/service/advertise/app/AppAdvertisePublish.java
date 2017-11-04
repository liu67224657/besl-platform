package com.enjoyf.platform.service.advertise.app;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.joymeapp.AppChannelType;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 14-6-7
 * Time: 下午12:03
 * To change this template use File | Settings | File Templates.
 */
public class AppAdvertisePublish implements Serializable {

    private long publishId;
    private String publishName;
    private String publishDesc;
    private long advertiseId;
    private String appkey;

    private Date startTime;
    private Date endTime;

    private Date createTime;
    private String createUser;
    private String creatIp;

    private AppAdvertisePublishType publishType;
    private PublishParam publishParam;
    private AppChannelType channel;

    private ActStatus removeStatus= ActStatus.UNACT;

    public long getPublishId() {
        return publishId;
    }

    public void setPublishId(long publishId) {
        this.publishId = publishId;
    }

    public String getPublishName() {
        return publishName;
    }

    public void setPublishName(String publishName) {
        this.publishName = publishName;
    }

    public String getPublishDesc() {
        return publishDesc;
    }

    public void setPublishDesc(String publishDesc) {
        this.publishDesc = publishDesc;
    }

    public long getAdvertiseId() {
        return advertiseId;
    }

    public void setAdvertiseId(long advertiseId) {
        this.advertiseId = advertiseId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCreatIp() {
        return creatIp;
    }

    public void setCreatIp(String creatIp) {
        this.creatIp = creatIp;
    }

    public AppAdvertisePublishType getPublishType() {
        return publishType;
    }

    public void setPublishType(AppAdvertisePublishType publishType) {
        this.publishType = publishType;
    }

    public PublishParam getPublishParam() {
        return publishParam;
    }

    public void setPublishParam(PublishParam publishParam) {
        this.publishParam = publishParam;
    }

    public AppChannelType getChannel() {
        return channel;
    }

    public void setChannel(AppChannelType channel) {
        this.channel = channel;
    }

    public ActStatus getRemoveStatus() {
        return removeStatus;
    }

    public void setRemoveStatus(ActStatus removeStatus) {
        this.removeStatus = removeStatus;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
