package com.enjoyf.webapps.tools.weblogic.dto.joymeApp;

import com.enjoyf.platform.service.ValidStatus;

import java.util.Date;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/12/17
 * Description:
 */
public class GameDBHotPageDTO implements Comparable {


    private Long itemId;    //joymeapp.client_line_item 表的id
    private Long gameDbId;      //gameDB的id
    private String showType;   //数据显示 ，用于游戏分类的Wap页 有两种 gameTypeDesc ：游戏类型 likeNum：点赞人数
    private String gameName;   //来自游戏资料库，存储编辑后的值  游戏名称
    private String gameTypeDesc;   //来自游戏资料库，存储编辑后的值，游戏类型，例如策略，卡牌
    private String icon;      //游戏图标
    private String jt;
    private String ji;
    private String jumpTarget;
    private String downloadRecommend;   //  推荐语
    private Date itemCreateDate;
    private String  likeNum;
    private String  gameRate;
    private String tag;  //存放 右上角的图标 如公测，限免等
    private int displayOrder;      //用于热门，大家正在玩 的后台排序

    private ValidStatus validStatus = ValidStatus.VALID;      //用于今日推荐的itemlist方法，前台加一个状态，停用，启用

    public ValidStatus getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(ValidStatus validStatus) {
        this.validStatus = validStatus;
    }

    public String getShowType() {
        return showType;
    }

    public void setShowType(String showType) {
        this.showType = showType;
    }

    public String getJumpTarget() {
        return jumpTarget;
    }

    public void setJumpTarget(String jumpTarget) {
        this.jumpTarget = jumpTarget;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getGameDbId() {
        return gameDbId;
    }

    public void setGameDbId(Long gameDbId) {
        this.gameDbId = gameDbId;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameTypeDesc() {
        return gameTypeDesc;
    }

    public void setGameTypeDesc(String gameTypeDesc) {
        this.gameTypeDesc = gameTypeDesc;
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

    public String getDownloadRecommend() {
        return downloadRecommend;
    }

    public void setDownloadRecommend(String downloadRecommend) {
        this.downloadRecommend = downloadRecommend;
    }

    public Date getItemCreateDate() {
        return itemCreateDate;
    }

    public void setItemCreateDate(Date itemCreateDate) {
        this.itemCreateDate = itemCreateDate;
    }


    public String getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(String likeNum) {
        this.likeNum = likeNum;
    }

    public String getGameRate() {
        return gameRate;
    }

    public void setGameRate(String gameRate) {
        this.gameRate = gameRate;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    @Override
    public int compareTo(Object o) {
        GameDBHotPageDTO dto = (GameDBHotPageDTO) o;
        Long result = this.itemCreateDate.getTime() - dto.itemCreateDate.getTime();
        if (result > 0) {
            return -1;
        } else if (result == 0L) {
            return 0;
        } else {

            return 1;
        }

    }

}
