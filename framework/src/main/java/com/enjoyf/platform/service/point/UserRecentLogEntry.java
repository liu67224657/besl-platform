package com.enjoyf.platform.service.point;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-10-25
 * Time: 下午12:15
 * To change this template use File | Settings | File Templates.
 */
public class UserRecentLogEntry implements Serializable, Comparable<UserRecentLogEntry> {

    private String screenName;
    private String goodsName;
    private String type;
    private Date date;

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "UserRecentLogEntry{" +
                "screenName='" + screenName + '\'' +
                ", goodsName=" + goodsName +
                ", type=" + type +
                ", date='" + date + '\'' +
                '}';
    }

    @Override
    public int compareTo(UserRecentLogEntry o) {
        return this.date.getTime() > o.getDate().getTime() ? -1 : (this.date.getTime() == o.getDate().getTime() ? 0 : 1);
    }
}
