package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: <a mailto="pengxu@staff.joyme.com">PengXu</a>
 * Create time:  14-10-27 下午5:51
 * Description:
 */
public class AnimeIndex implements Serializable {
    private long animeIndexId;
    private String line_name;//名称
    private String appkey;//所属哪个APP
    private String title;//标题
    private String code;//页面标识
    private ValidStatus validStatus;//状态
    private Date createDate;
    private String createUser;
    private String pic_url;
    private int platform;
    private AnimeRedirectType animeRedirectType;//链接类型
    private String linkUrl; //链接
    private String superScript; //角标
    private String wikiPageNum;//wiki页面数字

    private String desc;//描述 可用于填写其他文字

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getAnimeIndexId() {
        return animeIndexId;
    }

    public void setAnimeIndexId(long animeIndexId) {
        this.animeIndexId = animeIndexId;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ValidStatus getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(ValidStatus validStatus) {
        this.validStatus = validStatus;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getLine_name() {
        return line_name;
    }

    public void setLine_name(String line_name) {
        this.line_name = line_name;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public AnimeRedirectType getAnimeRedirectType() {
        return animeRedirectType;
    }

    public void setAnimeRedirectType(AnimeRedirectType animeRedirectType) {
        this.animeRedirectType = animeRedirectType;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getSuperScript() {
        return superScript;
    }

    public void setSuperScript(String superScript) {
        this.superScript = superScript;
    }

    public String getWikiPageNum() {
        return wikiPageNum;
    }

    public void setWikiPageNum(String wikiPageNum) {
        this.wikiPageNum = wikiPageNum;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
