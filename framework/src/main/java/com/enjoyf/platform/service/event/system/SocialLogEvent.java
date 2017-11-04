package com.enjoyf.platform.service.event.system;

import com.enjoyf.platform.service.content.social.SocialLogCategory;
import com.enjoyf.platform.service.content.social.SocialLogType;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.joymeapp.AppShareChannel;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-5-26
 * Time: 下午4:59
 * To change this template use File | Settings | File Templates.
 */
public class SocialLogEvent extends SystemEvent{

    private long foreignId;
    private String uno;

    private Long contentId;
    //水印、活动、背景音乐
    private SocialLogType socialLogType;
    //使用数、点赞数、评论数、礼物数等
    private SocialLogCategory socialLogCategory;

    private int increaseValue;

    private AppPlatform platform;

    private AppShareChannel shareChannel;

    public SocialLogEvent() {
        super(SystemEventType.SOCIAL_INCREASE_SUM);
    }

    public long getForeignId() {
        return foreignId;
    }

    public void setForeignId(long foreignId) {
        this.foreignId = foreignId;
    }

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public SocialLogType getSocialLogType() {
        return socialLogType;
    }

    public void setSocialLogType(SocialLogType socialLogType) {
        this.socialLogType = socialLogType;
    }

    public SocialLogCategory getSocialLogCategory() {
        return socialLogCategory;
    }

    public void setSocialLogCategory(SocialLogCategory socialLogCategory) {
        this.socialLogCategory = socialLogCategory;
    }

    public int getIncreaseValue() {
        return increaseValue;
    }

    public void setIncreaseValue(int increaseValue) {
        this.increaseValue = increaseValue;
    }

    public AppPlatform getPlatform() {
        return platform;
    }

    public void setPlatform(AppPlatform platform) {
        this.platform = platform;
    }

    public AppShareChannel getShareChannel() {
        return shareChannel;
    }

    public void setShareChannel(AppShareChannel shareChannel) {
        this.shareChannel = shareChannel;
    }
}
