package com.enjoyf.platform.service.content.social;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  14-4-10 下午6:07
 * Description:
 */
public class SocialContent implements Serializable {
    private long contentId;
    private String uno;
    private String title;
    private String body;
    private SocialConetntAppImages pic;//图片
    private SocialContentAppAudios audio;//音频
    private long audioLen;//时长
    private int replyNum;//回复数
    private int agreeNum;//点赞数

    private int readNum;//阅读数
    private int playNum;//播放数

    private Date createTime;

    private Date updateTime;
    private ActStatus removeStatus = ActStatus.UNACT;//删除标志位

    private Float lon;//经度

    private Float lat;//维度

    private SocialContentPlatformDomain socialContentPlatformDomain;//平台

    private long activityId;

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public long getContentId() {
        return contentId;
    }

    public void setContentId(long contentId) {
        this.contentId = contentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public SocialConetntAppImages getPic() {
        return pic;
    }

    public void setPic(SocialConetntAppImages pic) {
        this.pic = pic;
    }

    public SocialContentAppAudios getAudio() {
        return audio;
    }

    public void setAudio(SocialContentAppAudios audio) {
        this.audio = audio;
    }

    public int getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(int replyNum) {
        this.replyNum = replyNum;
    }

    public int getAgreeNum() {
        return agreeNum;
    }

    public void setAgreeNum(int agreeNum) {
        this.agreeNum = agreeNum;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    public int getReadNum() {
        return readNum;
    }

    public void setReadNum(int readNum) {
        this.readNum = readNum;
    }

    public int getPlayNum() {
        return playNum;
    }

    public void setPlayNum(int playNum) {
        this.playNum = playNum;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public ActStatus getRemoveStatus() {
        return removeStatus;
    }

    public void setRemoveStatus(ActStatus removeStatus) {
        this.removeStatus = removeStatus;
    }

    public long getAudioLen() {
        return audioLen;
    }

    public void setAudioLen(long audioLen) {
        this.audioLen = audioLen;
    }

    public Float getLon() {
        return lon;
    }

    public void setLon(Float lon) {
        this.lon = lon;
    }

    public Float getLat() {
        return lat;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }

    public SocialContentPlatformDomain getSocialContentPlatformDomain() {
        return socialContentPlatformDomain;
    }

    public void setSocialContentPlatformDomain(SocialContentPlatformDomain socialContentPlatformDomain) {
        this.socialContentPlatformDomain = socialContentPlatformDomain;
    }

    public long getActivityId() {
        return activityId;
    }

    public void setActivityId(long activityId) {
        this.activityId = activityId;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
