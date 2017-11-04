package com.enjoyf.webapps.joyme.dto.gamedb;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 13-12-12
 * Time: 上午10:40
 * To change this template use File | Settings | File Templates.
 */
public class GameDbDTO {

    private Long gameDbId;      //ID
    private String anotherName; //别名
    private String gameName;     //游戏名称
    private String gameIcon;      //游戏图标
    private String gameDeveloper;    //开发商
    private String gamePublishers;  //发行商
    private Date gamePublicTime;    //发行时间（更新时间）
    private List gameDevice;         //设备的LIST
    private List gameCategroy;        //类型的LIST
    private String gamePic;//游戏图片
    private String gameProfile;   //游戏简介
    private String versionProfile;  //版本简介
    private String teamName;   //团队名称
    private String teamNum;    //团队人数
    private String city;    //所在城市
    private Date publicTime;   //预计上市时间
    private List<String> financing;    //融资方式
    private String contacts;  //联系人
    private String email;     //email
    private String phone; //联系电话
    private String qq;         //qq
    private List<String> area;   //发行范围
    private String wikiUrl;  //wikiURL
    private String cmsUrl;    //CMSURL
    private Map gameDownloadInfo;//下载信息的MAP
    private String gameRate;//评分
    private String clientWiki;
    private String clientCms;
    private String downloadRecommend;//下载推荐
    private Map<String,Integer> commentAndAgreeMap;//


    public Map<String, Integer> getCommentAndAgreeMap() {
        return commentAndAgreeMap;
    }

    public void setCommentAndAgreeMap(Map<String, Integer> commentAndAgreeMap) {
        this.commentAndAgreeMap = commentAndAgreeMap;
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

    public String getGameIcon() {
        return gameIcon;
    }

    public void setGameIcon(String gameIcon) {
        this.gameIcon = gameIcon;
    }

    public String getGameDeveloper() {
        return gameDeveloper;
    }

    public void setGameDeveloper(String gameDeveloper) {
        this.gameDeveloper = gameDeveloper;
    }

    public Date getGamePublicTime() {
        return gamePublicTime;
    }

    public void setGamePublicTime(Date gamePublicTime) {
        this.gamePublicTime = gamePublicTime;
    }

    public List getGameDevice() {
        return gameDevice;
    }

    public void setGameDevice(List gameDevice) {
        this.gameDevice = gameDevice;
    }

    public List getGameCategroy() {
        return gameCategroy;
    }

    public void setGameCategroy(List gameCategroy) {
        this.gameCategroy = gameCategroy;
    }

    public Map getGameDownloadInfo() {
        return gameDownloadInfo;
    }

    public void setGameDownloadInfo(Map gameDownloadInfo) {
        this.gameDownloadInfo = gameDownloadInfo;
    }

    public String getGamePublishers() {
        return gamePublishers;
    }

    public void setGamePublishers(String gamePublishers) {
        this.gamePublishers = gamePublishers;
    }

    public String getGamePic() {
        return gamePic;
    }

    public void setGamePic(String gamePic) {
        this.gamePic = gamePic;
    }

    public String getGameProfile() {
        return gameProfile;
    }

    public void setGameProfile(String gameProfile) {
        this.gameProfile = gameProfile;
    }

    public String getVersionProfile() {
        return versionProfile;
    }

    public void setVersionProfile(String versionProfile) {
        this.versionProfile = versionProfile;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamNum() {
        return teamNum;
    }

    public void setTeamNum(String teamNum) {
        this.teamNum = teamNum;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Date getPublicTime() {
        return publicTime;
    }

    public void setPublicTime(Date publicTime) {
        this.publicTime = publicTime;
    }

    public List<String> getFinancing() {
        return financing;
    }

    public void setFinancing(List<String> financing) {
        this.financing = financing;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public List<String> getArea() {
        return area;
    }

    public void setArea(List<String> area) {
        this.area = area;
    }

    public String getWikiUrl() {
        return wikiUrl;
    }

    public void setWikiUrl(String wikiUrl) {
        this.wikiUrl = wikiUrl;
    }

    public String getCmsUrl() {
        return cmsUrl;
    }

    public void setCmsUrl(String cmsUrl) {
        this.cmsUrl = cmsUrl;
    }

    public String getAnotherName() {
        return anotherName;
    }

    public String getGameRate() {
        return gameRate;
    }

    public void setGameRate(String gameRate) {
        this.gameRate = gameRate;
    }

    public void setAnotherName(String anotherName) {
        this.anotherName = anotherName;
    }

    public String getClientWiki() {
        return clientWiki;
    }

    public void setClientWiki(String clientWiki) {
        this.clientWiki = clientWiki;
    }

    public String getClientCms() {
        return clientCms;
    }

    public void setClientCms(String clientCms) {
        this.clientCms = clientCms;
    }

    public String getDownloadRecommend() {
        return downloadRecommend;
    }

    public void setDownloadRecommend(String downloadRecommend) {
        this.downloadRecommend = downloadRecommend;
    }
}
