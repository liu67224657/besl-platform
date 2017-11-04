package com.enjoyf.webapps.joyme.dto.joymeapp;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-6-19
 * Time: 下午3:16
 * To change this template use File | Settings | File Templates.
 */
public class AppShareCount {

    private long cid;
    private int platform;
    private String channel;

    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
