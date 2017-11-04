package com.enjoyf.webapps.joyme.dto.joymeapp;

import com.enjoyf.platform.text.TextJsonItem;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-6-18
 * Time: 上午10:23
 * To change this template use File | Settings | File Templates.
 */

public class JoymeAppGiftDetailDTO implements Serializable {
    private String gipic;  //icon
    private String title;  //标题
    private String explainDesc;  //激活说明
    private List<TextJsonItem> textJsonItemsList;//礼包内容
    private int rn;    //剩余数量
    private int cn;    //总数
    private Long gid;
    private String wikiUrl;//专区URL；
    private long endTime;   //结束时间
    private List<String> goodsValue = new ArrayList<String>();  //号码
    private long shareId;//分享Id
    private String gameName;//游戏名称
    private String iosUrl;
    private String androidUrl;
    private List<String> valueList = new ArrayList<String>();// 我的礼包详情页 用来存储当前用户获得的激活码数量

    public String getGipic() {
        return gipic;
    }

    public void setGipic(String gipic) {
        this.gipic = gipic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExplainDesc() {
        return explainDesc;
    }

    public void setExplainDesc(String explainDesc) {
        this.explainDesc = explainDesc;
    }

    public List<TextJsonItem> getTextJsonItemsList() {
        return textJsonItemsList;
    }

    public void setTextJsonItemsList(List<TextJsonItem> textJsonItemsList) {
        this.textJsonItemsList = textJsonItemsList;
    }

    public int getRn() {
        return rn;
    }

    public void setRn(int rn) {
        this.rn = rn;
    }

    public int getCn() {
        return cn;
    }

    public void setCn(int cn) {
        this.cn = cn;
    }

    public Long getGid() {
        return gid;
    }

    public void setGid(Long gid) {
        this.gid = gid;
    }

    public String getWikiUrl() {
        return wikiUrl;
    }

    public void setWikiUrl(String wikiUrl) {
        this.wikiUrl = wikiUrl;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public List<String> getGoodsValue() {
        return goodsValue;
    }

    public void setGoodsValue(List<String> goodsValue) {
        this.goodsValue = goodsValue;
    }

    public long getShareId() {
        return shareId;
    }

    public void setShareId(long shareId) {
        this.shareId = shareId;
    }

    public String getIosUrl() {
        return iosUrl;
    }

    public void setIosUrl(String iosUrl) {
        this.iosUrl = iosUrl;
    }

    public String getAndroidUrl() {
        return androidUrl;
    }

    public void setAndroidUrl(String androidUrl) {
        this.androidUrl = androidUrl;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
