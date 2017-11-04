package com.enjoyf.webapps.joyme.dto.joymeapp.anime;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-10-27
 * Time: 下午12:20
 * To change this template use File | Settings | File Templates.
 */
public class AnimeTvDTO implements Serializable {
    private String tvid;
    private String title;
    private String tvnumber;
    private String picurl;
    private String url;
    private String m3u8;
    private String domain;
    private String latest;
    private String reserved;

    public String getTvid() {
        return tvid;
    }

    public void setTvid(String tvid) {
        this.tvid = tvid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTvnumber() {
        return tvnumber;
    }

    public void setTvnumber(String tvnumber) {
        this.tvnumber = tvnumber;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getM3u8() {
        return m3u8;
    }

    public void setM3u8(String m3u8) {
        this.m3u8 = m3u8;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getLatest() {
        return latest;
    }

    public void setLatest(String latest) {
        this.latest = latest;
    }

    public String getReserved() {
        return reserved;
    }

    public void setReserved(String reserved) {
        this.reserved = reserved;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
