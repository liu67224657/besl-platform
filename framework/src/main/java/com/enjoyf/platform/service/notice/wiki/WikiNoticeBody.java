package com.enjoyf.platform.service.notice.wiki;

import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by pengxu on 2016/12/6.
 */
public class WikiNoticeBody implements Serializable {
    private String destProfileId="";
    private String pageurl="";
    private String contenturl="";
    private String desc="";
    private int prestige=0;

    private WikiNoticeDestType wikiNoticeDestType=WikiNoticeDestType.REPLY; //1=发表了评论 2=回复了我 3=回复了其他人
    private String otherProfileId="";//其他人profileid

    public String getDestProfileId() {
        return destProfileId;
    }

    public void setDestProfileId(String destProfileId) {
        this.destProfileId = destProfileId;
    }

    public String getPageurl() {
        return pageurl;
    }

    public void setPageurl(String pageurl) {
        this.pageurl = pageurl;
    }

    public String getContenturl() {
        return contenturl;
    }

    public void setContenturl(String contenturl) {
        this.contenturl = contenturl;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public static WikiNoticeBody fromJson(String jsonStr) {
        return new Gson().fromJson(jsonStr, WikiNoticeBody.class);
    }

    public WikiNoticeDestType getWikiNoticeDestType() {
        return wikiNoticeDestType;
    }

    public void setWikiNoticeDestType(WikiNoticeDestType wikiNoticeDestType) {
        this.wikiNoticeDestType = wikiNoticeDestType;
    }

    public int getPrestige() {
        return prestige;
    }

    public void setPrestige(int prestige) {
        this.prestige = prestige;
    }

    public String getOtherProfileId() {
        return otherProfileId;
    }

    public void setOtherProfileId(String otherProfileId) {
        this.otherProfileId = otherProfileId;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
