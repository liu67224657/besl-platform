package com.enjoyf.webapps.joyme.dto.gamedb;

import java.util.Date;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">tony diao</a>
 * Create time: 15/03/23
 * Description:
 */
public class GameDBHotPageDTO {


    private String gameid;      //gameDB的id
    private String name;   //来自游戏资料库，存储编辑后的值  游戏名称
    private String type;   //来自游戏资料库，存储编辑后的值，游戏类型，例如策略，卡牌
    private String icon;      //游戏图标
    private String showtype;  // 显示推荐人数还是评分 "likes":推荐人数 ,"rate":评分，用于今日推荐     "type" 游戏类型 ---用于游戏分类
    private String jt;
    private String ji;
    private String referral;   //  推荐语
    private Long createtime;
    private String likes;         //推荐人数
    private String rate;                //评分
    private String flag="";         //更新提示 礼，攻，促，等
    private String tag;             //右上角 ，公测，限免等


    public String getShowtype() {
        return showtype;
    }

    public void setShowtype(String showtype) {
        this.showtype = showtype;
    }

    public String getGameid() {
        return gameid;
    }

    public void setGameid(String gameid) {
        this.gameid = gameid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getJt() {
        return jt;
    }

    public void setJt(String jt) {
        this.jt = jt;
    }

    public String getJi() {
        return ji;
    }

    public void setJi(String ji) {
        this.ji = ji;
    }


    public String getReferral() {
        return referral;
    }

    public void setReferral(String referral) {
        this.referral = referral;
    }

    public Long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Long createtime) {
        this.createtime = createtime;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
