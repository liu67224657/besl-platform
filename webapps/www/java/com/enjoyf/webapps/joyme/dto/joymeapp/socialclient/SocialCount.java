package com.enjoyf.webapps.joyme.dto.joymeapp.socialclient;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-6-19
 * Time: 下午3:16
 * To change this template use File | Settings | File Templates.
 */
public class SocialCount {

    private long cid;
    private long watermarkid;
    private long bgaudioid;
    private long activityid;

    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }

    public long getWatermarkid() {
        return watermarkid;
    }

    public void setWatermarkid(long watermarkid) {
        this.watermarkid = watermarkid;
    }

    public long getBgaudioid() {
        return bgaudioid;
    }

    public void setBgaudioid(long bgaudioid) {
        this.bgaudioid = bgaudioid;
    }

    public long getActivityid() {
        return activityid;
    }

    public void setActivityid(long activityid) {
        this.activityid = activityid;
    }
}
