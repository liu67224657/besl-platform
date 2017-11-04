package com.enjoyf.platform.service.gameres.gamedb;


import net.sf.json.JSONObject;

import java.io.Serializable;
import java.util.*;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-11-20 下午2:21
 * Description:
 */
public class GameDB implements Serializable {
    //基本信息
    private long gameDbId;
    private String wikiKey;
    private String gameIcon;  //游戏图标
    private String gameName;  //游戏名称
    private String anotherName; //别名
    private String gameSize;//游戏大小
    private Set<GamePlatformType> platformTypeSet;
    private Map<String, Set<GamePlatform>> platformMap;//平台
    private String iosDownload;//下载
    private String androidDownload;//下载
    private String wpDownload;//下载 Windows Phone下载


    private Date gamePublicTime;   //上市时间
    private boolean publicTips = false;//上市邮件提醒
    private String officialWebsite;//官网
    private GameNetType gameNetType;//联网方式
    private Set<GameLanguageType> languageTypeSet;//语言
    private Set<GameCategoryType> categoryTypeSet;//类型
    private Set<GameThemeType> themeTypeSet;//题材
    private String gameDeveloper;  //开发商
    private String gamePublishers;  //发行商
    private String gameProfile;   //游戏简介
    private String gameVideo;//cms视频文章
    private String gameVideoPic;//cms视频文章缩略图
    private String gamePic;//游戏截图
    private GamePicType gamePicType;//截图类型
    private String wikiUrl;//wiki
    private String cmsUrl;//专区
    private double gameRate = 8.0;//评分
    private int favorSum = 0;//喜欢数
    private int unFavorSum = 0;//不喜欢数
    private int popular = 0;//人气值

    private int newsSum = 0;//资讯数
    private int videoSum = 0;//视频数
    private int guideSum = 0;//攻略数
    private int giftSum = 0;//礼包数

    private int pvSum = 0;//访问次数

    private GameDbStatus validStatus;
    private Date createDate;
    private Date modifyDate;
    private String createUser;
    private String modifyUser;

    //商务信息
    private String teamName;   //团队名称
    private int teamNum = 0;    //团队人数
    private String city;    //所在城市
    private Date publicTime;   //预计上市时间
    private int financing = 0;    //融资方式
    private String contacts;  //联系人
    private String email;     //email
    private String phone; //联系电话
    private String qq;         //qq
    private int area = 0;   //发行范围
    private int displayIcon = 0;  //0-不显示 1-new 2-hot

    //玩霸
    private String versionProfile;  //版本简介
    private String recommendReason;//推荐理由
    private String recommendReason2; //推荐理由2
    private GameDBModifyTimeFieldJson modifyTime;
    private GameDBCover gameDBCover;//游戏封面大对象
    private GameDBCoverFieldJson gameDBCoverFieldJson;//游戏封面5个字段
    private String downloadRecommend;//下载推荐
    //着迷网首页
    private CommentAndAgree commentAndAgree;//大家对游戏的看法
    //其他
    private Set<GameDBChannelInfo> channelInfoSet; //db
    //以下属性非db字段
    private int archiveSum;
    private int piwikSum;

    private String gamePCConfigurationInfo;//pc游戏所需配置信息
    private String pcDownload;//pc下载

    private String gameDesc;//游戏一句话描述

    private long associatedGameId;//关联的其他平台类型游戏id （手机游戏、其他游戏互相关联的游戏）


    private boolean levelGame = false;//是否关卡游戏：单选，默认“否”
    private String xboxoneDownload;//Xboxone下载地址（可选）
    private String ps4Download;//PS4下载地址（可选）
    private String webpageDownload;//网页游戏地址


    //点评添加
    private String isbn; //游戏版本号
    private boolean comment = false;//是否是点评
    private String englishName;//英文名
    private String backpic;//背景图
    private String appstorePrice;//app store价格
    private String video;//宣传视频
    private boolean vpn = false;//是否需要vpn
    private String commentScore; //点评评分
    private String commentSum; //点评人数
    private String commentGamePic;//点评游戏宣传图
    private String gameTag;//游戏标签



    public String getWikiKey() {
        return wikiKey;
    }

    public void setWikiKey(String wikiKey) {
        this.wikiKey = wikiKey;
    }

    public long getGameDbId() {
        return gameDbId;
    }

    public void setGameDbId(long gameDbId) {
        this.gameDbId = gameDbId;
    }

    public String getGameIcon() {
        return gameIcon;
    }

    public void setGameIcon(String gameIcon) {
        this.gameIcon = gameIcon;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getAnotherName() {
        return anotherName;
    }

    public void setAnotherName(String anotherName) {
        this.anotherName = anotherName;
    }

    public String getGameSize() {
        return gameSize;
    }

    public void setGameSize(String gameSize) {
        this.gameSize = gameSize;
    }

    public Set<GamePlatformType> getPlatformTypeSet() {
        return platformTypeSet;
    }

    public void setPlatformTypeSet(Set<GamePlatformType> platformTypeSet) {
        this.platformTypeSet = platformTypeSet;
    }

    public Map<String, Set<GamePlatform>> getPlatformMap() {
        return platformMap;
    }

    public void setPlatformMap(Map<String, Set<GamePlatform>> platformMap) {
        this.platformMap = platformMap;
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

    public String getWpDownload() {
        return wpDownload;
    }

    public void setWpDownload(String wpDownload) {
        this.wpDownload = wpDownload;
    }

    public Date getGamePublicTime() {
        return gamePublicTime;
    }

    public void setGamePublicTime(Date gamePublicTime) {
        this.gamePublicTime = gamePublicTime;
    }

    public boolean getPublicTips() {
        return publicTips;
    }

    public void setPublicTips(boolean publicTips) {
        this.publicTips = publicTips;
    }

    public String getOfficialWebsite() {
        return officialWebsite;
    }

    public void setOfficialWebsite(String officialWebsite) {
        this.officialWebsite = officialWebsite;
    }

    public GameNetType getGameNetType() {
        return gameNetType;
    }

    public void setGameNetType(GameNetType gameNetType) {
        this.gameNetType = gameNetType;
    }

    public Set<GameLanguageType> getLanguageTypeSet() {
        return languageTypeSet;
    }

    public void setLanguageTypeSet(Set<GameLanguageType> languageTypeSet) {
        this.languageTypeSet = languageTypeSet;
    }

    public Set<GameCategoryType> getCategoryTypeSet() {
        return categoryTypeSet;
    }

    public void setCategoryTypeSet(Set<GameCategoryType> categoryTypeSet) {
        this.categoryTypeSet = categoryTypeSet;
    }

    public Set<GameThemeType> getThemeTypeSet() {
        return themeTypeSet;
    }

    public void setThemeTypeSet(Set<GameThemeType> themeTypeSet) {
        this.themeTypeSet = themeTypeSet;
    }

    public String getGameDeveloper() {
        return gameDeveloper;
    }

    public void setGameDeveloper(String gameDeveloper) {
        this.gameDeveloper = gameDeveloper;
    }

    public String getGamePublishers() {
        return gamePublishers;
    }

    public void setGamePublishers(String gamePublishers) {
        this.gamePublishers = gamePublishers;
    }

    public String getGameProfile() {
        return gameProfile;
    }

    public void setGameProfile(String gameProfile) {
        this.gameProfile = gameProfile;
    }

    public String getGameVideo() {
        return gameVideo;
    }

    public void setGameVideo(String gameVideo) {
        this.gameVideo = gameVideo;
    }

    public String getGameVideoPic() {
        return gameVideoPic;
    }

    public void setGameVideoPic(String gameVideoPic) {
        this.gameVideoPic = gameVideoPic;
    }

    public String getGamePic() {
        return gamePic;
    }

    public void setGamePic(String gamePic) {
        this.gamePic = gamePic;
    }

    public GamePicType getGamePicType() {
        return gamePicType;
    }

    public void setGamePicType(GamePicType gamePicType) {
        this.gamePicType = gamePicType;
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

    public double getGameRate() {
        return gameRate;
    }

    public void setGameRate(double gameRate) {
        this.gameRate = gameRate;
    }

    public int getFavorSum() {
        return favorSum;
    }

    public void setFavorSum(int favorSum) {
        this.favorSum = favorSum;
    }

    public int getUnFavorSum() {
        return unFavorSum;
    }

    public void setUnFavorSum(int unFavorSum) {
        this.unFavorSum = unFavorSum;
    }

    public int getPopular() {
        return popular;
    }

    public void setPopular(int popular) {
        this.popular = popular;
    }

    public GameDbStatus getValidStatus() {
        return validStatus;
    }

    public int getNewsSum() {
        return newsSum;
    }

    public void setNewsSum(int newsSum) {
        this.newsSum = newsSum;
    }

    public int getVideoSum() {
        return videoSum;
    }

    public void setVideoSum(int videoSum) {
        this.videoSum = videoSum;
    }

    public int getGuideSum() {
        return guideSum;
    }

    public void setGuideSum(int guideSum) {
        this.guideSum = guideSum;
    }

    public int getGiftSum() {
        return giftSum;
    }

    public void setGiftSum(int giftSum) {
        this.giftSum = giftSum;
    }

    public int getPvSum() {
        return pvSum;
    }

    public void setPvSum(int pvSum) {
        this.pvSum = pvSum;
    }

    public void setValidStatus(GameDbStatus validStatus) {
        this.validStatus = validStatus;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getTeamNum() {
        return teamNum;
    }

    public void setTeamNum(int teamNum) {
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

    public int getFinancing() {
        return financing;
    }

    public void setFinancing(int financing) {
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

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public String getVersionProfile() {
        return versionProfile;
    }

    public void setVersionProfile(String versionProfile) {
        this.versionProfile = versionProfile;
    }

    public String getRecommendReason() {
        return recommendReason;
    }

    public void setRecommendReason(String recommendReason) {
        this.recommendReason = recommendReason;
    }

    public String getRecommendReason2() {
        return recommendReason2;
    }

    public void setRecommendReason2(String recommendReason2) {
        this.recommendReason2 = recommendReason2;
    }

    public GameDBModifyTimeFieldJson getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(GameDBModifyTimeFieldJson modifyTime) {
        this.modifyTime = modifyTime;
    }

    public int getDisplayIcon() {
        return displayIcon;
    }

    public void setDisplayIcon(int displayIcon) {
        this.displayIcon = displayIcon;
    }

    public GameDBCover getGameDBCover() {
        return gameDBCover;
    }

    public void setGameDBCover(GameDBCover gameDBCover) {
        this.gameDBCover = gameDBCover;
    }

    public GameDBCoverFieldJson getGameDBCoverFieldJson() {
        return gameDBCoverFieldJson;
    }

    public void setGameDBCoverFieldJson(GameDBCoverFieldJson gameDBCoverFieldJson) {
        this.gameDBCoverFieldJson = gameDBCoverFieldJson;
    }

    public String getDownloadRecommend() {
        return downloadRecommend;
    }

    public void setDownloadRecommend(String downloadRecommend) {
        this.downloadRecommend = downloadRecommend;
    }

    public CommentAndAgree getCommentAndAgree() {
        return commentAndAgree;
    }

    public void setCommentAndAgree(CommentAndAgree commentAndAgree) {
        this.commentAndAgree = commentAndAgree;
    }

    public Set<GameDBChannelInfo> getChannelInfoSet() {
        return channelInfoSet;
    }

    public void setChannelInfoSet(Set<GameDBChannelInfo> channelInfoSet) {
        this.channelInfoSet = channelInfoSet;
    }

    public int getArchiveSum() {
        return archiveSum;
    }

    public void setArchiveSum(int archiveSum) {
        this.archiveSum = archiveSum;
    }

    public int getPiwikSum() {
        return piwikSum;
    }

    public void setPiwikSum(int piwikSum) {
        this.piwikSum = piwikSum;
    }

    public String getGamePCConfigurationInfo() {
        return gamePCConfigurationInfo;
    }

    public void setGamePCConfigurationInfoSet(String gamePCConfigurationInfo) {
        this.gamePCConfigurationInfo = gamePCConfigurationInfo;
    }

    public void setGamePCConfigurationInfo(String gamePCConfigurationInfo) {
        this.gamePCConfigurationInfo = gamePCConfigurationInfo;
    }

    public String getGameDesc() {
        return gameDesc;
    }

    public void setGameDesc(String gameDesc) {
        this.gameDesc = gameDesc;
    }

    public long getAssociatedGameId() {
        return associatedGameId;
    }

    public void setAssociatedGameId(long associatedGameId) {
        this.associatedGameId = associatedGameId;
    }

    public String getPcDownload() {
        return pcDownload;
    }

    public void setPcDownload(String pcDownload) {
        this.pcDownload = pcDownload;
    }

    public boolean isLevelGame() {
        return levelGame;
    }

    public void setLevelGame(boolean levelGame) {
        this.levelGame = levelGame;
    }

    public String getXboxoneDownload() {
        return xboxoneDownload;
    }

    public void setXboxoneDownload(String xboxoneDownload) {
        this.xboxoneDownload = xboxoneDownload;
    }

    public String getPs4Download() {
        return ps4Download;
    }

    public void setPs4Download(String ps4Download) {
        this.ps4Download = ps4Download;
    }

    public String getWebpageDownload() {
        return webpageDownload;
    }

    public void setWebpageDownload(String webpageDownload) {
        this.webpageDownload = webpageDownload;
    }


//    public boolean isPublicTips() {
//        return publicTips;
//    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public boolean isComment() {
        return comment;
    }

    public void setComment(boolean comment) {
        this.comment = comment;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getBackpic() {
        return backpic;
    }

    public void setBackpic(String backpic) {
        this.backpic = backpic;
    }


    public String getAppstorePrice() {
        return appstorePrice;
    }

    public void setAppstorePrice(String appstorePrice) {
        this.appstorePrice = appstorePrice;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public boolean isVpn() {
        return vpn;
    }

    public void setVpn(boolean vpn) {
        this.vpn = vpn;
    }


    public String getCommentScore() {
        return commentScore;
    }

    public void setCommentScore(String commentScore) {
        this.commentScore = commentScore;
    }

    public String getCommentSum() {
        return commentSum;
    }

    public void setCommentSum(String commentSum) {
        this.commentSum = commentSum;
    }

    public String getCommentGamePic() {
        return commentGamePic;
    }

    public void setCommentGamePic(String commentGamePic) {
        this.commentGamePic = commentGamePic;
    }


    public String getGameTag() {
        return gameTag;
    }

    public void setGameTag(String gameTag) {
        this.gameTag = gameTag;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = JSONObject.fromObject(this);
        return jsonObject.toString();
    }
}
