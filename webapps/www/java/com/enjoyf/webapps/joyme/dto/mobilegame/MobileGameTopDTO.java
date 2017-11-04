package com.enjoyf.webapps.joyme.dto.mobilegame;

/**
 * Created by xupeng on 14-9-19.
 * 相关排行榜DTO类
 */
public class MobileGameTopDTO {
    private String title;//line的标题
    private Long lineId;//LIneId
    private String gamePic;//游戏图片
    private int lineRank;//游戏在此line种的排名
    private int gagNum;//吐槽数量
    private int lineItemNum;//item数量

    private long gamedbId;   //游戏资料库ID
    private String reason;//推荐理由
    private Double rate;//评分


    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getLineId() {
        return lineId;
    }

    public void setLineId(Long lineId) {
        this.lineId = lineId;
    }

    public String getGamePic() {
        return gamePic;
    }

    public void setGamePic(String gamePic) {
        this.gamePic = gamePic;
    }

    public int getLineRank() {
        return lineRank;
    }

    public void setLineRank(int lineRank) {
        this.lineRank = lineRank;
    }

    public int getGagNum() {
        return gagNum;
    }

    public void setGagNum(int gagNum) {
        this.gagNum = gagNum;
    }

    public int getLineItemNum() {
        return lineItemNum;
    }

    public void setLineItemNum(int lineItemNum) {
        this.lineItemNum = lineItemNum;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public long getGamedbId() {
        return gamedbId;
    }

    public void setGamedbId(long gamedbId) {
        this.gamedbId = gamedbId;
    }

    @Override
    public String toString() {
        return "MobileGameTopDTO{" +
                "title='" + title + '\'' +
                ", lineId=" + lineId +
                ", gamePic='" + gamePic + '\'' +
                ", lineRank='" + lineRank + '\'' +
                ", gagNum=" + gagNum +
                ", lineItemNum=" + lineItemNum +
                '}';
    }
}
