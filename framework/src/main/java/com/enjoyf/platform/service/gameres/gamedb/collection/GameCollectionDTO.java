package com.enjoyf.platform.service.gameres.gamedb.collection;

import com.enjoyf.platform.service.gameres.gamedb.*;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.http.URLUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-11-20 下午2:21
 * Description:
 */
public class GameCollectionDTO implements Serializable {
    //基本信息
    private long gameDbId;
    private String gameIcon;  //游戏图标
    private String gameName;  //游戏名称
    private String anotherName; //别名
    private String gameSize;//游戏大小
    private Set<GamePlatformType> platformTypeSet;
    private Map<String, Set<GamePlatform>> platformMap;//平台
    private String iosDownload;//下载
    private String androidDownload;//下载
    private String wpDownload;//下载
    private Date gamePublicTime;   //上市时间
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
    private Set<String> gamePicSet;//游戏截图
    private GamePicType gamePicType;//截图类型
    private String wikiUrl;//wiki
    private String cmsUrl;//专区
    private double gameRate;//评分
    private int favorSum;//喜欢数
    private int unFavorSum;//不喜欢数
    private int giftSum;
    private String gameDesc;//一句话描述
    private long assicatedGameId;//关联不同类型游戏id

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

    public Set<String> getGamePicSet() {
        return gamePicSet;
    }

    public void setGamePicSet(Set<String> gamePicSet) {
        this.gamePicSet = gamePicSet;
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

    public int getGiftSum() {
        return giftSum;
    }

    public void setGiftSum(int giftSum) {
        this.giftSum = giftSum;
    }

    
    public String getGameDesc() {
		return gameDesc;
	}

	public void setGameDesc(String gameDesc) {
		this.gameDesc = gameDesc;
	}

	public long getAssicatedGameId() {
		return assicatedGameId;
	}

	public void setAssicatedGameId(long assicatedGameId) {
		this.assicatedGameId = assicatedGameId;
	}

	@Override
    public String toString() {
        JSONObject jsonObject = JSONObject.fromObject(this);
        return jsonObject.toString();
    }

    public String toJson() {
        JSONObject jsonObject = JSONObject.fromObject(this);
        return jsonObject.toString();
    }

    public static GameCollectionDTO parse(Object obj) {
        if(obj == null){
            return null;
        }
        JSONObject jsonObject = JSONObject.fromObject(obj);
        if(jsonObject == null){
            return null;
        }
        GameCollectionDTO gameCollectionDTO = new GameCollectionDTO();
        gameCollectionDTO.setGameDbId(jsonObject.containsKey("gameDbId") ? jsonObject.getLong("gameDbId") : 0l);
        gameCollectionDTO.setGameIcon(jsonObject.containsKey("gameIcon") ? jsonObject.getString("gameIcon") : "");
        gameCollectionDTO.setGameName(jsonObject.containsKey("gameName") ? jsonObject.getString("gameName") : "");
        gameCollectionDTO.setAnotherName(jsonObject.containsKey("anotherName") ? jsonObject.getString("anotherName") : "");
        gameCollectionDTO.setGameSize(jsonObject.containsKey("gameSize") ? jsonObject.getString("gameSize") : "");
        if(jsonObject.containsKey("platformMap")){
            JSONObject jsonPlatformMap = jsonObject.getJSONObject("platformMap");
            if(jsonPlatformMap != null){
                Map<String, Set<GamePlatform>> map = new HashMap<String, Set<GamePlatform>>();
                Set<GamePlatformType> platformTypeSet = new HashSet<GamePlatformType>();
                if(jsonPlatformMap.containsKey(String.valueOf(GamePlatformType.MOBILE.getCode()))){
                    platformTypeSet.add(GamePlatformType.MOBILE);
                    map.put(String.valueOf(GamePlatformType.MOBILE.getCode()), new HashSet<GamePlatform>());
                    JSONArray jsonPlatformArr = jsonPlatformMap.getJSONArray(String.valueOf(GamePlatformType.MOBILE.getCode()));
                    if(jsonPlatformArr != null && jsonPlatformArr.isArray() && !jsonPlatformArr.isEmpty()){
                        for(Object platformObj:jsonPlatformArr){
                            JSONObject jsonPlatform = JSONObject.fromObject(platformObj);
                            MobilePlatform mobilePlatform = jsonPlatform.containsKey("code") ? MobilePlatform.getByCode(jsonPlatform.getInt("code")) : null;
                            if(mobilePlatform != null){
                                map.get(String.valueOf(GamePlatformType.MOBILE.getCode())).add(mobilePlatform);
                            }
                        }
                    }

                }
                if(jsonPlatformMap.containsKey(String.valueOf(GamePlatformType.PC.getCode()))){
                    platformTypeSet.add(GamePlatformType.PC);
                    map.put(String.valueOf(GamePlatformType.PC.getCode()), new HashSet<GamePlatform>());
                    JSONArray jsonPlatformArr = jsonPlatformMap.getJSONArray(String.valueOf(GamePlatformType.PC.getCode()));
                    if(jsonPlatformArr != null && jsonPlatformArr.isArray() && !jsonPlatformArr.isEmpty()){
                        for(Object platformObj:jsonPlatformArr){
                            JSONObject jsonPlatform = JSONObject.fromObject(platformObj);
                            PCPlatform pcPlatform = jsonPlatform.containsKey("code") ? PCPlatform.getByCode(jsonPlatform.getInt("code")) : null;
                            if(pcPlatform != null){
                                map.get(String.valueOf(GamePlatformType.PC.getCode())).add(pcPlatform);
                            }
                        }
                    }

                }
                if(jsonPlatformMap.containsKey(String.valueOf(GamePlatformType.PSP.getCode()))){
                    platformTypeSet.add(GamePlatformType.PSP);
                    map.put(String.valueOf(GamePlatformType.PSP.getCode()), new HashSet<GamePlatform>());
                    JSONArray jsonPlatformArr = jsonPlatformMap.getJSONArray(String.valueOf(GamePlatformType.PSP.getCode()));
                    if(jsonPlatformArr != null && jsonPlatformArr.isArray() && !jsonPlatformArr.isEmpty()){
                        for(Object platformObj:jsonPlatformArr){
                            JSONObject jsonPlatform = JSONObject.fromObject(platformObj);
                            PSPPlatform pspPlatform = jsonPlatform.containsKey("code") ? PSPPlatform.getByCode(jsonPlatform.getInt("code")) : null;
                            if(pspPlatform != null){
                                map.get(String.valueOf(GamePlatformType.PSP.getCode())).add(pspPlatform);
                            }
                        }
                    }

                }
                if(jsonPlatformMap.containsKey(String.valueOf(GamePlatformType.TV.getCode()))){
                    platformTypeSet.add(GamePlatformType.TV);
                    map.put(String.valueOf(GamePlatformType.TV.getCode()), new HashSet<GamePlatform>());
                    JSONArray jsonPlatformArr = jsonPlatformMap.getJSONArray(String.valueOf(GamePlatformType.TV.getCode()));
                    if(jsonPlatformArr != null && jsonPlatformArr.isArray() && !jsonPlatformArr.isEmpty()){
                        for(Object platformObj:jsonPlatformArr){
                            JSONObject jsonPlatform = JSONObject.fromObject(platformObj);
                            TVPlatform tvPlatform = jsonPlatform.containsKey("code") ? TVPlatform.getByCode(jsonPlatform.getInt("code")) : null;
                            if(tvPlatform != null){
                                map.get(String.valueOf(GamePlatformType.TV.getCode())).add(tvPlatform);
                            }
                        }
                    }

                }
                gameCollectionDTO.setPlatformMap(map);
            }
        }
        gameCollectionDTO.setIosDownload(jsonObject.containsKey("iosDownload") ? jsonObject.getString("iosDownload") : "");
        gameCollectionDTO.setAndroidDownload(jsonObject.containsKey("androidDownload") ? jsonObject.getString("androidDownload") : "");
        gameCollectionDTO.setWpDownload(jsonObject.containsKey("wpDownload") ? jsonObject.getString("wpDownload") : "");
        gameCollectionDTO.setGamePublicTime(jsonObject.containsKey("gamePublicTime") ? (jsonObject.getJSONObject("gamePublicTime").containsKey("time") ? new Date(jsonObject.getJSONObject("gamePublicTime").getLong("time")) : null) : null);
        gameCollectionDTO.setOfficialWebsite(jsonObject.containsKey("officialWebsite") ? jsonObject.getString("officialWebsite") : "");
        if(jsonObject.containsKey("gameNetType")){
            JSONObject jsonNetType = jsonObject.getJSONObject("gameNetType");
            if(jsonNetType != null){
                GameNetType netType = jsonNetType.containsKey("code") ?  GameNetType.getByCode(jsonNetType.getInt("code")) : null;
                if(netType != null){
                    gameCollectionDTO.setGameNetType(netType);
                }
            }
        }
        if(jsonObject.containsKey("languageTypeSet")){
            Set<GameLanguageType> languageTypeSet = new HashSet<GameLanguageType>();
            JSONArray jsonLanguageArr = jsonObject.getJSONArray("languageTypeSet");
            if(jsonLanguageArr != null && jsonLanguageArr.isArray() && !jsonLanguageArr.isEmpty()){
                for(Object languageObj:jsonLanguageArr){
                    JSONObject jsonLanguage = JSONObject.fromObject(languageObj);
                    GameLanguageType languageType = jsonLanguage.containsKey("code") ? GameLanguageType.getByCode(jsonLanguage.getInt("code")) : null;
                    if(languageType != null){
                        languageTypeSet.add(languageType);
                    }
                }
            }
            gameCollectionDTO.setLanguageTypeSet(languageTypeSet);
        }
        if(jsonObject.containsKey("categoryTypeSet")){
            Set<GameCategoryType> categoryTypeSet = new HashSet<GameCategoryType>();
            JSONArray jsonCategoryArr = jsonObject.getJSONArray("categoryTypeSet");
            if(jsonCategoryArr != null && jsonCategoryArr.isArray() && !jsonCategoryArr.isEmpty()){
                for(Object categoryObj:jsonCategoryArr){
                    JSONObject jsonCategory = JSONObject.fromObject(categoryObj);
                    GameCategoryType categoryType = jsonCategory.containsKey("code") ? GameCategoryType.getByCode(jsonCategory.getInt("code")) : null;
                    if(categoryType != null){
                        categoryTypeSet.add(categoryType);
                    }
                }
            }
            gameCollectionDTO.setCategoryTypeSet(categoryTypeSet);
        }
        if(jsonObject.containsKey("themeTypeSet")){
            Set<GameThemeType> themeTypeSet = new HashSet<GameThemeType>();
            JSONArray jsonThemeArr = jsonObject.getJSONArray("themeTypeSet");
            if(jsonThemeArr != null && jsonThemeArr.isArray() && !jsonThemeArr.isEmpty()){
                for(Object themeObj:jsonThemeArr){
                    JSONObject jsonTheme = JSONObject.fromObject(themeObj);
                    GameThemeType themeType = jsonTheme.containsKey("code") ? GameThemeType.getByCode(jsonTheme.getInt("code")) : null;
                    if(themeType != null){
                        themeTypeSet.add(themeType);
                    }
                }
            }
            gameCollectionDTO.setThemeTypeSet(themeTypeSet);
        }
        gameCollectionDTO.setGameDeveloper(jsonObject.containsKey("gameDeveloper") ? jsonObject.getString("gameDeveloper") : "");
        gameCollectionDTO.setGamePublishers(jsonObject.containsKey("gamePublishers") ? jsonObject.getString("gamePublishers") : "");
        gameCollectionDTO.setGameProfile(jsonObject.containsKey("gameProfile") ? jsonObject.getString("gameProfile") : "");
        gameCollectionDTO.setGameVideo(jsonObject.containsKey("gameVideo") ? jsonObject.getString("gameVideo") : "");
        gameCollectionDTO.setGameVideoPic(jsonObject.containsKey("gameVideoPic") ? jsonObject.getString("gameVideoPic") : "");
        if(jsonObject.containsKey("gamePicSet")){
            Set<String> gamePicSet = new HashSet<String>();
            JSONArray jsonPicArr = jsonObject.getJSONArray("gamePicSet");
            if(jsonPicArr != null && jsonPicArr.isArray() && !jsonPicArr.isEmpty()){
                for(Object picObj:jsonPicArr){
                    gamePicSet.add(String.valueOf(picObj));
                }
            }
            gameCollectionDTO.setGamePicSet(gamePicSet);
        }
        if(jsonObject.containsKey("gamePicType")){
            JSONObject jsonPicType = jsonObject.getJSONObject("gamePicType");
            if(jsonPicType != null){
                GamePicType picType = jsonPicType.containsKey("code") ?  GamePicType.getByCode(jsonPicType.getInt("code")) : null;
                if(picType != null){
                    gameCollectionDTO.setGamePicType(picType);
                }
            }
        }
        gameCollectionDTO.setWikiUrl(jsonObject.containsKey("wikiUrl") ? jsonObject.getString("wikiUrl") : "");
        gameCollectionDTO.setCmsUrl(jsonObject.containsKey("cmsUrl") ? jsonObject.getString("cmsUrl") : "");
        DecimalFormat df =new DecimalFormat("#.0");
        gameCollectionDTO.setGameRate(jsonObject.containsKey("gameRate") ? Double.valueOf(df.format(jsonObject.getDouble("gameRate"))) : 0);
        gameCollectionDTO.setFavorSum(jsonObject.containsKey("favorSum") ? jsonObject.getInt("favorSum") : 0);
        gameCollectionDTO.setUnFavorSum(jsonObject.containsKey("unFavorSum") ? jsonObject.getInt("unFavorSum") : 0);
        gameCollectionDTO.setGiftSum(jsonObject.containsKey("giftSum") ? jsonObject.getInt("giftSum") : 0);
        gameCollectionDTO.setGameDesc(jsonObject.containsKey("gameDesc")?jsonObject.getString("gameDesc"):"");
        gameCollectionDTO.setAssicatedGameId(jsonObject.containsKey("assicatedGameId")?jsonObject.getLong("assicatedGameId"):0l);
        return gameCollectionDTO;
    }

    public static GameCollectionDTO buildDTOFromGameDB(GameDB gameDB) {
        if(gameDB == null){
            return null;
        }
        GameCollectionDTO dto = new GameCollectionDTO();
        dto.setGameDbId(gameDB.getGameDbId());
        dto.setGameIcon(StringUtil.isEmpty(gameDB.getGameIcon()) ? URLUtils.getDefaultGameIcon() : gameDB.getGameIcon());
        dto.setGameName(gameDB.getGameName());
        dto.setAnotherName(gameDB.getAnotherName());
        dto.setGameSize(gameDB.getGameSize());
        dto.setPlatformTypeSet(gameDB.getPlatformTypeSet());
        dto.setPlatformMap(gameDB.getPlatformMap());
        dto.setIosDownload(gameDB.getIosDownload());
        dto.setAndroidDownload(gameDB.getAndroidDownload());
        dto.setWpDownload(gameDB.getWpDownload());
        dto.setGamePublicTime(gameDB.getGamePublicTime());
        dto.setOfficialWebsite(gameDB.getOfficialWebsite());
        dto.setGameNetType(gameDB.getGameNetType());
        dto.setLanguageTypeSet(gameDB.getLanguageTypeSet());
        dto.setCategoryTypeSet(gameDB.getCategoryTypeSet());
        dto.setThemeTypeSet(gameDB.getThemeTypeSet());
        dto.setGameDeveloper(gameDB.getGameDeveloper());
        dto.setGamePublishers(gameDB.getGamePublishers());
        dto.setGameProfile(gameDB.getGameProfile());
        dto.setGameVideo(gameDB.getGameVideo());
        dto.setGameVideoPic(gameDB.getGameVideoPic());
        if(!StringUtil.isEmpty(gameDB.getGamePic())){
            Set<String> picSet = new HashSet<String>();
            if(gameDB.getGamePic().indexOf(",") > 0){
                String[] picArr = gameDB.getGamePic().split(",");
                for(String pic:picArr){
                    picSet.add(URLUtils.getJoymeDnUrl(pic));
                }
            }
            dto.setGamePicSet(picSet);
        }
        dto.setGamePicType(gameDB.getGamePicType());
        dto.setWikiUrl(gameDB.getWikiUrl());
        dto.setCmsUrl(gameDB.getCmsUrl());
        DecimalFormat df =new DecimalFormat("#.0");
        dto.setGameRate(Double.valueOf(df.format(gameDB.getGameRate())));
        dto.setFavorSum(gameDB.getFavorSum());
        dto.setUnFavorSum(gameDB.getUnFavorSum());
        dto.setGiftSum(gameDB.getGiftSum());
        dto.setGameDesc(gameDB.getGameDesc());
        dto.setAssicatedGameId(gameDB.getAssociatedGameId());
        return dto;
    }

}
