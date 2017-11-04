package com.enjoyf.webapps.joyme.dto.activity;

import com.enjoyf.platform.service.content.ActivityCategoryType;
import com.enjoyf.platform.text.TextJsonItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
public class ActivityDetailDTO {
    private Long aid;//活动的id
    private String gipic;  //icon
    private String title;  //标题
    private String explainDesc;  //激活说明
    private List<TextJsonItem> textJsonItemsList;//礼包内容
    private int rn;    //已领多少
    private int cn;    //总数
    private Long gid;   //goods或exchangegoods的 id
    private String wikiUrl;//专区URL；
    private Date endTime;
    private Date startTime;
    private List<String> goodsValue = new ArrayList<String>();         //已领取的激活码
    private String qrUrl;//一跳2链接
    private String gameIcon;
    private String gameTitie;
    private ActivityCategoryType gameType;
    private String gameDeveloper;
    private String iosUrl;
    private String androidUrl;
    private long shareId;
    private String desc;
    private int weixinExclusive;//  是否是微信独家
    private int goodsType;
    private int platform;//

    private int point;//积分兑换 goods 的 价格
    private int sn;//积分兑换 goods 的 剩余数量
    private boolean expire;//是否  过期
    private int reserveType;
    private String bgPic;
    private String consumeOrder;//dingdan hao
    private long gameDbId;//
    private int exchangeTimeType; //领取状态 详情参考 ConsumeTimesType类

    private List<String> categoryList;//新版的categoryList
    private int seckillType;

    public int getSeckillType() {
        return seckillType;
    }

    public void setSeckillType(int seckillType) {
        this.seckillType = seckillType;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public List<String> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<String> categoryList) {
        this.categoryList = categoryList;
    }

    public long getGameDbId() {
        return gameDbId;
    }

    public void setGameDbId(long gameDbId) {
        this.gameDbId = gameDbId;
    }

    public String getConsumeOrder() {
        return consumeOrder;
    }

    public void setConsumeOrder(String consumeOrder) {
        this.consumeOrder = consumeOrder;
    }

    public Long getAid() {
        return aid;
    }

    public void setAid(Long aid) {
        this.aid = aid;
    }

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

    public String getWikiUrl() {
        return wikiUrl;
    }

    public void setWikiUrl(String wikiUrl) {
        this.wikiUrl = wikiUrl;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public List<String> getGoodsValue() {
        return goodsValue;
    }

    public void setGoodsValue(List<String> goodsValue) {
        this.goodsValue = goodsValue;
    }

    public String getGameIcon() {
        return gameIcon;
    }

    public void setGameIcon(String gameIcon) {
        this.gameIcon = gameIcon;
    }

    public String getGameTitie() {
        return gameTitie;
    }

    public void setGameTitie(String gameTitie) {
        this.gameTitie = gameTitie;
    }

    public ActivityCategoryType getGameType() {
        return gameType;
    }

    public void setGameType(ActivityCategoryType gameType) {
        this.gameType = gameType;
    }

    public String getGameDeveloper() {
        return gameDeveloper;
    }

    public void setGameDeveloper(String gameDeveloper) {
        this.gameDeveloper = gameDeveloper;
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

    public long getShareId() {
        return shareId;
    }

    public void setShareId(long shareId) {
        this.shareId = shareId;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getSn() {
        return sn;
    }

    public void setSn(int sn) {
        this.sn = sn;
    }

    public boolean getExpire() {
        return expire;
    }

    public void setExpire(boolean expire) {
        this.expire = expire;
    }

    public String getQrUrl() {
        return qrUrl;
    }

    public void setQrUrl(String qrUrl) {
        this.qrUrl = qrUrl;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getWeixinExclusive() {
        return weixinExclusive;
    }

    public int getReserveType() {
        return reserveType;
    }

    public void setReserveType(int reserveType) {
        this.reserveType = reserveType;
    }

    public String getBgPic() {
        return bgPic;
    }

    public void setBgPic(String bgPic) {
        this.bgPic = bgPic;
    }

    public int getExchangeTimeType() {
        return exchangeTimeType;
    }

    public void setExchangeTimeType(int exchangeTimeType) {
        this.exchangeTimeType = exchangeTimeType;
    }

    public void setWeixinExclusive(int weixinExclusive) {
        this.weixinExclusive = weixinExclusive;
    }

    public int getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(int goodsType) {
        this.goodsType = goodsType;
    }

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }
}
