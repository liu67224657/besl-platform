package com.enjoyf.webapps.joyme.dto.share;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-6-24
 * Time: 下午4:08
 * To change this template use File | Settings | File Templates.
 */
public class JoymeAppShareInfoDTO {
    private long shareId;
    private String shareTopic;
    private String shareBody;
    private String shareUrl;
    private String picUrl;


    public long getShareId() {
        return shareId;
    }

    public void setShareId(long shareId) {
        this.shareId = shareId;
    }

    public String getShareTopic() {
        return shareTopic;
    }

    public void setShareTopic(String shareTopic) {
        this.shareTopic = shareTopic;
    }

    public String getShareBody() {
        return shareBody;
    }

    public void setShareBody(String shareBody) {
        this.shareBody = shareBody;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
