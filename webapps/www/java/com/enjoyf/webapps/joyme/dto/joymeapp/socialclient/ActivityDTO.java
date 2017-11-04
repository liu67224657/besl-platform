package com.enjoyf.webapps.joyme.dto.joymeapp.socialclient;

import com.enjoyf.platform.service.advertise.app.AppAdvertiseRedirectType;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-5-21
 * Time: 下午8:29
 * To change this template use File | Settings | File Templates.
 */
public class ActivityDTO {
    private int type = 0;//type 0是跳转到广场列表页面 type=1是跳转到URL todo
    //广场
    private long activityid;
    private String title;
    private String description;
    private String icon;
    private String smallpic;
    private String bigpic;
    private int usesum;
    private int replysum;
    private int agreesum;
    private int giftsum;
    private long shareid;
    private SubscriptDTO subscript;

    //广告
    private long advertiseid;
    private String rurl;
    private String ios4pic;
    private String ios5pic;
    private int redirecttype = AppAdvertiseRedirectType.APPSTORE.getCode();
    private long publishId;
    private int index;

	//分享模板flag
	private long shareflag;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getActivityid() {
        return activityid;
    }

    public void setActivityid(long activityid) {
        this.activityid = activityid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSmallpic() {
        return smallpic;
    }

    public void setSmallpic(String smallpic) {
        this.smallpic = smallpic;
    }

    public String getBigpic() {
        return bigpic;
    }

    public void setBigpic(String bigpic) {
        this.bigpic = bigpic;
    }

    public int getUsesum() {
        return usesum;
    }

    public void setUsesum(int usesum) {
        this.usesum = usesum;
    }

    public int getReplysum() {
        return replysum;
    }

    public void setReplysum(int replysum) {
        this.replysum = replysum;
    }

    public int getAgreesum() {
        return agreesum;
    }

    public void setAgreesum(int agreesum) {
        this.agreesum = agreesum;
    }

    public int getGiftsum() {
        return giftsum;
    }

    public void setGiftsum(int giftsum) {
        this.giftsum = giftsum;
    }

    public long getShareid() {
        return shareid;
    }

    public void setShareid(long shareid) {
        this.shareid = shareid;
    }

    public SubscriptDTO getSubscript() {
        return subscript;
    }

    public void setSubscript(SubscriptDTO subscript) {
        this.subscript = subscript;
    }

    public long getAdvertiseid() {
        return advertiseid;
    }

    public void setAdvertiseid(long advertiseid) {
        this.advertiseid = advertiseid;
    }

    public String getRurl() {
        return rurl;
    }

    public void setRurl(String rurl) {
        this.rurl = rurl;
    }

    public String getIos4pic() {
        return ios4pic;
    }

    public void setIos4pic(String ios4pic) {
        this.ios4pic = ios4pic;
    }

    public String getIos5pic() {
        return ios5pic;
    }

    public void setIos5pic(String ios5pic) {
        this.ios5pic = ios5pic;
    }

    public int getRedirecttype() {
        return redirecttype;
    }

    public void setRedirecttype(int redirecttype) {
        this.redirecttype = redirecttype;
    }

    public long getPublishId() {
        return publishId;
    }

    public void setPublishId(long publishId) {
        this.publishId = publishId;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

	public long getShareflag() {
		return shareflag;
	}

	public void setShareflag(long shareflag) {
		this.shareflag = shareflag;
	}
}
