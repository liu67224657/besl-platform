package com.enjoyf.platform.service.content.social;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-5-27
 * Time: 上午10:35
 * To change this template use File | Settings | File Templates.
 */
public class SocialBackgroundAudio implements Serializable{

    private long audioId;
    private String audioName;
    private String audioPic;
    private String audioDescription;
    private String singer;
    private String mp3Src;
    private String wavSrc;
    private ValidStatus removeStatus = ValidStatus.INVALID;
    private int displayOrder;
    private int useSum = 0;
    private SubscriptType subscriptType = SubscriptType.NULL;
    private Subscript subscript;

    private Date createDate;
    private String createIp;
    private String createUserId;
    private Date modifyDate;
    private String modifyIp;
    private String modifyUserId;

    public long getAudioId() {
        return audioId;
    }

    public void setAudioId(long audioId) {
        this.audioId = audioId;
    }

    public String getAudioName() {
        return audioName;
    }

    public void setAudioName(String audioName) {
        this.audioName = audioName;
    }

    public String getAudioDescription() {
        return audioDescription;
    }

    public void setAudioDescription(String audioDescription) {
        this.audioDescription = audioDescription;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getMp3Src() {
        return mp3Src;
    }

    public void setMp3Src(String mp3Src) {
        this.mp3Src = mp3Src;
    }

    public String getWavSrc() {
        return wavSrc;
    }

    public void setWavSrc(String wavSrc) {
        this.wavSrc = wavSrc;
    }

    public ValidStatus getRemoveStatus() {
        return removeStatus;
    }

    public void setRemoveStatus(ValidStatus removeStatus) {
        this.removeStatus = removeStatus;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public int getUseSum() {
        return useSum;
    }

    public void setUseSum(int useSum) {
        this.useSum = useSum;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateIp() {
        return createIp;
    }

    public void setCreateIp(String createIp) {
        this.createIp = createIp;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getModifyIp() {
        return modifyIp;
    }

    public void setModifyIp(String modifyIp) {
        this.modifyIp = modifyIp;
    }

    public String getModifyUserId() {
        return modifyUserId;
    }

    public void setModifyUserId(String modifyUserId) {
        this.modifyUserId = modifyUserId;
    }

    public String getAudioPic() {
        return audioPic;
    }

    public void setAudioPic(String audioPic) {
        this.audioPic = audioPic;
    }

    public SubscriptType getSubscriptType() {
        return subscriptType;
    }

    public void setSubscriptType(SubscriptType subscriptType) {
        this.subscriptType = subscriptType;
    }

    public Subscript getSubscript() {
        return subscript;
    }

    public void setSubscript(Subscript subscript) {
        this.subscript = subscript;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
