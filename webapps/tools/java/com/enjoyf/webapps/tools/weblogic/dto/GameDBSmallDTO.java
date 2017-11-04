package com.enjoyf.webapps.tools.weblogic.dto;

import com.enjoyf.platform.service.joymeapp.AppRedirectType;

import java.util.Date;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/12/17
 * Description:
 */
public class GameDBSmallDTO implements Comparable {


    private Long itemId;    //joymeapp.client_line_item 表的id
    private Long gameid;      //ID
    private String name; //别名
    private String icon;      //游戏图标
    private String description;   //游戏简介
    private String downlink;
    private Date gamePublicTime;
    private String categoryColor;
    private String url;
    private String showType;    //用于新游开测榜 显示1_精确时间还是2_自定义内容
    private String customContent;     //用于新游开测榜 自定义内容
    private AppRedirectType redirectType;
    private int displayOrder;      //用于热门，大家正在玩 的后台排序


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

    public String getCategoryColor() {
        return categoryColor;
    }

    public void setCategoryColor(String categoryColor) {
        this.categoryColor = categoryColor;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public AppRedirectType getRedirectType() {
        return redirectType;
    }

    public void setRedirectType(AppRedirectType redirectType) {
        this.redirectType = redirectType;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Date getGamePublicTime() {
        return gamePublicTime;
    }

    public void setGamePublicTime(Date gamePublicTime) {
        this.gamePublicTime = gamePublicTime;
    }

    public Long getGameid() {
        return gameid;
    }

    public void setGameid(Long gameid) {
        this.gameid = gameid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDownlink() {
        return downlink;
    }

    public void setDownlink(String downlink) {
        this.downlink = downlink;
    }


    @Override
    public int compareTo(Object o) {
        GameDBSmallDTO dto = (GameDBSmallDTO) o;
        Long result = this.gamePublicTime.getTime() - dto.gamePublicTime.getTime();
        if (result > 0) {
            return -1;
        } else if (result == 0L) {
            return 0;
        } else {

            return 1;
        }

    }

}
