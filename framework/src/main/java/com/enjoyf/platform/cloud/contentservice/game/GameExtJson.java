package com.enjoyf.platform.cloud.contentservice.game;

import java.io.Serializable;

/**
 * Created by zhimingli on 2017/6/20.
 */
public class GameExtJson implements Serializable {

    private String gameLogo; //游戏LOGO
    private String gameDeveloper;//游戏开发商
    private String gamePublisher;//游戏发行商
    private boolean online;
    private String size;
    private String price;
    private String iosDownload;
    private String androidDownload;
    private String pcDownload;
    private String video;
    private String pic; //图片宣传图，多张以逗号分隔

    private String gameDesc;

    private String createUser; //创建人
    private String recommend;//一句话推荐
    private String recommendAuth;//一句话推荐作者

    private String language;//语言,多张以逗号分隔 ： 中文 英文 日文 其他

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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public String getPcDownload() {
        return pcDownload;
    }

    public void setPcDownload(String pcDownload) {
        this.pcDownload = pcDownload;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getGameDesc() {
        return gameDesc;
    }

    public void setGameDesc(String gameDesc) {
        this.gameDesc = gameDesc;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }

    public String getRecommendAuth() {
        return recommendAuth;
    }

    public void setRecommendAuth(String recommendAuth) {
        this.recommendAuth = recommendAuth;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return "GameExtJson{" +
                "gameLogo='" + gameLogo + '\'' +
                ", gameDeveloper='" + gameDeveloper + '\'' +
                ", gamePublisher='" + gamePublisher + '\'' +
                ", isOnline=" + online +
                ", size='" + size + '\'' +
                ", price='" + price + '\'' +
                ", iosDownload='" + iosDownload + '\'' +
                ", androidDownload='" + androidDownload + '\'' +
                ", pcDownload='" + pcDownload + '\'' +
                ", video='" + video + '\'' +
                ", pic='" + pic + '\'' +
                ", gameDesc='" + gameDesc + '\'' +
                ", createUser='" + createUser + '\'' +
                ", recommend='" + recommend + '\'' +
                ", recommendAuth='" + recommendAuth + '\'' +
                ", language='" + language + '\'' +
                '}';
    }
}
