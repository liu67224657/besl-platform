package com.enjoyf.platform.service.joymeappconfig;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-7-24
 * Time: 下午4:56
 * To change this template use File | Settings | File Templates.
 */
public class AppChannel implements Serializable {

    private long channelId;
    private String channelCode;
    private String channelName;

    public long getChannelId() {
        return channelId;
    }

    public void setChannelId(long channelId) {
        this.channelId = channelId;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
