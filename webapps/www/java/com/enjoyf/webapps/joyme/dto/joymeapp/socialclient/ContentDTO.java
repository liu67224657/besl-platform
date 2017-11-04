package com.enjoyf.webapps.joyme.dto.joymeapp.socialclient;

import com.enjoyf.platform.service.advertise.app.AppAdvertiseRedirectType;
import com.enjoyf.platform.service.content.social.SocialConetntAppImages;
import com.enjoyf.platform.service.content.social.SocialContentAppAudios;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-4-17
 * Time: 上午11:45
 * To change this template use File | Settings | File Templates.
 */
public class ContentDTO {

    private long cid;
    private String uno;
    private String title;
    private String body;
    private SocialConetntAppImages pic;//图片
    private SocialContentAppAudios audio;//音频
    private long audiotime;//时长
    private int replynum;//回复数
    private int agreenum;//点赞数

    private int readnum;//阅读数
    private int playnum;//播放数

    private long createtime;

    private Float lon;//经度

    private Float lat;//维度

    private boolean isagree;

    private String focus;//是否关注

    private ActivityDTO activity;

	private long shareflag;//分享模板flag

    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
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

    public long getAudiotime() {
        return audiotime;
    }

    public void setAudiotime(long audiotime) {
        this.audiotime = audiotime;
    }

    public int getReplynum() {
        return replynum;
    }

    public void setReplynum(int replynum) {
        this.replynum = replynum;
    }

    public int getAgreenum() {
        return agreenum;
    }

    public void setAgreenum(int agreenum) {
        this.agreenum = agreenum;
    }

    public int getReadnum() {
        return readnum;
    }

    public void setReadnum(int readnum) {
        this.readnum = readnum;
    }

    public int getPlaynum() {
        return playnum;
    }

    public void setPlaynum(int playnum) {
        this.playnum = playnum;
    }

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
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

    public boolean isIsagree() {
        return isagree;
    }

    public void setIsagree(boolean isagree) {
        this.isagree = isagree;
    }

    public String getFocus() {
        return focus;
    }

    public void setFocus(String focus) {
        this.focus = focus;
    }

    public ActivityDTO getActivity() {
        return activity;
    }

    public void setActivity(ActivityDTO activity) {
        this.activity = activity;
    }

	public long getShareflag() {
		return shareflag;
	}

	public void setShareflag(long shareflag) {
		this.shareflag = shareflag;
	}
}
