package com.enjoyf.platform.cloud.contentservice.game;

import com.enjoyf.platform.cloud.contentservice.game.enumeration.GameOperStatus;
import com.enjoyf.platform.cloud.contentservice.game.enumeration.GameType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhimingli on 2017/7/12.
 * 游戏详情描述VM
 */
public class GameDTO {

    private Long id;
    private String name;
    private String aliasName;//游戏别名
    private List<GameTagDTO> gameTag;
    private String gameLogo = "";
    private String gameDeveloper;//游戏开发商
    private String gamePublisher;//游戏发行商
    private String size;
    private String pcDownload;
    private GameType gameType;//游戏类型
    private GameOperStatus operStatus = GameOperStatus.UNKNOWN;//运营状态
    private String video = "";
    private String recommend = "";//一句话推荐
    private String recommendAuth;//一句话推荐作者
    private Long createTime;//创建时间
    private List<String> picList = new ArrayList<String>();
    private boolean hasComment = false;
    private String price;
    private List<String> language = new ArrayList<String>();
    private String iosDownload;
    private String androidDownload;
    private String gameDesc;
    private boolean online;
    private boolean android;
    private boolean ios;
    private boolean pc;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GameTagDTO> getGameTag() {
        return gameTag;
    }

    public void setGameTag(List<GameTagDTO> gameTag) {
        this.gameTag = gameTag;
    }

    public String getGameLogo() {
        return gameLogo;
    }

    public void setGameLogo(String gameLogo) {
        this.gameLogo = gameLogo;
    }

    public String getGameDeveloper() {
        return gameDeveloper;
    }

    public void setGameDeveloper(String gameDeveloper) {
        this.gameDeveloper = gameDeveloper;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public List<String> getPicList() {
        return picList;
    }

    public void setPicList(List<String> picList) {
        this.picList = picList;
    }

    public String getRecommendAuth() {
        return recommendAuth;
    }

    public void setRecommendAuth(String recommendAuth) {
        this.recommendAuth = recommendAuth;
    }

    public boolean isHasComment() {
        return hasComment;
    }

    public void setHasComment(boolean hasComment) {
        this.hasComment = hasComment;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public List<String> getLanguage() {
        return language;
    }

    public void setLanguage(List<String> language) {
        this.language = language;
    }

    public String getIosDownload() {
        return iosDownload;
    }

    public void setIosDownload(String iosDownload) {
        this.iosDownload = iosDownload;
    }

    public String getAndroidDownload() {
        return androidDownload;
    }

    public void setAndroidDownload(String androidDownload) {
        this.androidDownload = androidDownload;
    }


    public String getGameDesc() {
        return gameDesc;
    }

    public void setGameDesc(String gameDesc) {
        this.gameDesc = gameDesc;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String getGamePublisher() {
        return gamePublisher;
    }

    public void setGamePublisher(String gamePublisher) {
        this.gamePublisher = gamePublisher;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPcDownload() {
        return pcDownload;
    }

    public void setPcDownload(String pcDownload) {
        this.pcDownload = pcDownload;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public GameOperStatus getOperStatus() {
        return operStatus;
    }

    public void setOperStatus(GameOperStatus operStatus) {
        this.operStatus = operStatus;
    }

    public boolean isAndroid() {
        return android;
    }

    public void setAndroid(boolean android) {
        this.android = android;
    }

    public boolean isIos() {
        return ios;
    }

    public void setIos(boolean ios) {
        this.ios = ios;
    }

    public boolean isPc() {
        return pc;
    }

    public void setPc(boolean pc) {
        this.pc = pc;
    }

    @Override
    public String toString() {
        return "GameDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", aliasName='" + aliasName + '\'' +
                ", gameTag=" + gameTag +
                ", gameLogo='" + gameLogo + '\'' +
                ", gameDeveloper='" + gameDeveloper + '\'' +
                ", gamePublisher='" + gamePublisher + '\'' +
                ", size='" + size + '\'' +
                ", pcDownload='" + pcDownload + '\'' +
                ", gameType=" + gameType +
                ", operStatus=" + operStatus +
                ", video='" + video + '\'' +
                ", recommend='" + recommend + '\'' +
                ", recommendAuth='" + recommendAuth + '\'' +
                ", createTime=" + createTime +
                ", picList=" + picList +
                ", hasComment=" + hasComment +
                ", price='" + price + '\'' +
                ", language=" + language +
                ", iosDownload='" + iosDownload + '\'' +
                ", androidDownload='" + androidDownload + '\'' +
                ", gameDesc='" + gameDesc + '\'' +
                ", isOnline=" + online +
                '}';
    }
}
