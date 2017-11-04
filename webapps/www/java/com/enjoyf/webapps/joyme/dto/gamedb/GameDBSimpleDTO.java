package com.enjoyf.webapps.joyme.dto.gamedb;

import com.enjoyf.platform.service.gameres.gamedb.RedMessageType;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/12/17
 * Description:
 */
public class GameDBSimpleDTO implements Comparable, Comparator<GameDBSimpleDTO>, Serializable {
    private String gameid;      //ID
    private String name; //别名
    private String iconurl;      //游戏图标
    private String desc;   //游戏简介
    private String reason;  //推荐理由1    用于新游开测榜
    private String downlink;
    private long modifytime;
    private long publishtime;
    private String dottype;    //0-不显示 1-new 2-hot
    private String rate;
    private List<String> tags;
    private String jt;
    private String ji;
    private String follow = ""; //是否关注
    private String showType;    //用于新游开测榜 显示1_精确时间还是2_自定义内容
    private String customContent;     //用于新游开测榜 自定义内容  或者显示精确时间的字符串

    private int  displayOrder;  //用于新游开测--热门块排序

    private String redMessageType = RedMessageType.DEFAULT.getCode() + "";
    private String redMessageText = "";
    private long redMessageTime = 0l;


    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getShowType() {
        return showType;
    }

    public void setShowType(String showType) {
        this.showType = showType;
    }

    public String getCustomContent() {
        return customContent;
    }

    public void setCustomContent(String customContent) {
        this.customContent = customContent;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    public String getIconurl() {
        return iconurl;
    }

    public void setIconurl(String iconurl) {
        this.iconurl = iconurl;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDownlink() {
        return downlink;
    }

    public void setDownlink(String downlink) {
        this.downlink = downlink;
    }

    public long getModifytime() {
        return modifytime;
    }

    public void setModifytime(long modifytime) {
        this.modifytime = modifytime;
    }

    public long getPublishtime() {
        return publishtime;
    }

    public void setPublishtime(long publishtime) {
        this.publishtime = publishtime;
    }


    public String getDottype() {
        return dottype;
    }

    public void setDottype(String dottype) {
        this.dottype = dottype;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
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

    public String getFollow() {
        return follow;
    }

    public void setFollow(String follow) {
        this.follow = follow;
    }

    public String getRedMessageType() {
        return redMessageType;
    }

    public void setRedMessageType(String redMessageType) {
        this.redMessageType = redMessageType;
    }

    public String getRedMessageText() {
        return redMessageText;
    }

    public void setRedMessageText(String redMessageText) {
        this.redMessageText = redMessageText;
    }

    public long getRedMessageTime() {
        return redMessageTime;
    }

    public void setRedMessageTime(long redMessageTime) {
        this.redMessageTime = redMessageTime;
    }

    @Override
    public int compareTo(Object o) {
        GameDBSimpleDTO dto = (GameDBSimpleDTO) o;
        Long result = this.publishtime - dto.publishtime;
        if (result > 0) {
            return -1;
        } else if (result == 0L) {
            return 0;
        } else {

            return 1;
        }

    }


    public int compare(GameDBSimpleDTO o1, GameDBSimpleDTO o2) {

        Long result = o1.publishtime - o2.publishtime;
        if (result < 0) {
            return -1;
        } else if (result == 0L) {
            return 0;
        } else {

            return 1;
        }

    }
}
