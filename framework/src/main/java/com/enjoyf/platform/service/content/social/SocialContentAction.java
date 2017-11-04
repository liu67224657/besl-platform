package com.enjoyf.platform.service.content.social;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  14-4-15 上午11:46
 * Description:
 */
public class SocialContentAction implements Serializable {
    private long actionId;
    private String uno;

    private long contentId;
    private String contentUno;

    private Date createTime;
    private String createIp;
    private ActStatus removeStatus = ActStatus.UNACT;

    private SocialContentActionType type = SocialContentActionType.AGREE;//默认类型是赞

    private float lon;
    private float lat;

    private SocialContentPlatformDomain socialContentPlatformDomain;//平台

    public long getActionId() {
        return actionId;
    }

    public void setActionId(long actionId) {
        this.actionId = actionId;
    }

    public long getContentId() {
        return contentId;
    }

    public void setContentId(long contentId) {
        this.contentId = contentId;
    }

    public String getContentUno() {
        return contentUno;
    }

    public void setContentUno(String contentUno) {
        this.contentUno = contentUno;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateIp() {
        return createIp;
    }

    public void setCreateIp(String createIp) {
        this.createIp = createIp;
    }

    public ActStatus getRemoveStatus() {
        return removeStatus;
    }

    public void setRemoveStatus(ActStatus removeStatus) {
        this.removeStatus = removeStatus;
    }

    public SocialContentActionType getType() {
        return type;
    }

    public void setType(SocialContentActionType type) {
        this.type = type;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public SocialContentPlatformDomain getSocialContentPlatformDomain() {
        return socialContentPlatformDomain;
    }

    public void setSocialContentPlatformDomain(SocialContentPlatformDomain socialContentPlatformDomain) {
        this.socialContentPlatformDomain = socialContentPlatformDomain;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
