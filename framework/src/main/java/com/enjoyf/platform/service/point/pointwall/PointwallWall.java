package com.enjoyf.platform.service.point.pointwall;

import java.io.Serializable;

/**
 * Created by tonydiao on 2014/11/27.
 */
public class PointwallWall  implements Serializable {
    private String appkey;
    private int appNum;

    //存放 PointKeyType 静态字段的的code ,数据库中存放的也是code
    private String pointKey;
    private String wallMoneyName;
    private int shopKey;
    private String shopKeyName;

    private String template ;//积分墙模板

    //积分墙所在app的名称
    private String appKeyName="";

    //用于按appKeyName模糊查询时是否显示某些项
    private int  flag;


    //存放 PointKeyType 静态字段的名称
    private String pointKeyName;


    public String getShopKeyName() {
        return shopKeyName;
    }

    public void setShopKeyName(String shopKeyName) {
        this.shopKeyName = shopKeyName;
    }

    public int getShopKey() {
        return shopKey;
    }

    public void setShopKey(int shopKey) {
        this.shopKey = shopKey;
    }

    public String getPointKeyName() {
        return pointKeyName;
    }

    public void setPointKeyName(String pointKeyName) {
        this.pointKeyName = pointKeyName;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getAppKeyName() {
        return appKeyName;
    }

    public void setAppKeyName(String appKeyName) {
        this.appKeyName = appKeyName;
    }

    public int getAppNum() {
        return appNum;
    }

    public void setAppNum(int appNum) {
        this.appNum = appNum;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getPointKey() {
        return pointKey;
    }

    public void setPointKey(String pointKey) {
        this.pointKey = pointKey;
    }

    public String getWallMoneyName() {
        return wallMoneyName;
    }

    public void setWallMoneyName(String wallMoneyName) {
        this.wallMoneyName = wallMoneyName;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
