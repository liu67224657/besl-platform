package com.enjoyf.webapps.joyme.dto.joymeapp;

import com.enjoyf.platform.service.advertise.app.AppAdvertiseRedirectType;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-6-11
 * Time: 下午4:21
 * To change this template use File | Settings | File Templates.
 */
public class AppAdvertiseDTO {

    private long advertiseid;
    private String name;
    private String description;
    private String rurl;
    private String ios4pic;
    private String ios5pic;
    private int redirecttype = AppAdvertiseRedirectType.APPSTORE.getCode();
    private long publishId;
    private int index;
    private long viewtime;
    private String starttime = "";//开始时间
    private String endtime = "";//结束时间

    public long getAdvertiseid() {
        return advertiseid;
    }

    public void setAdvertiseid(long advertiseid) {
        this.advertiseid = advertiseid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public long getViewtime() {
        return viewtime;
    }

    public void setViewtime(long viewtime) {
        this.viewtime = viewtime;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }
}
