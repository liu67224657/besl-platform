package com.enjoyf.platform.db.gameres;

import com.enjoyf.platform.db.AbstractSequenceBaseMongoDbAccessor;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.gameres.gamedb.*;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.http.URLUtils;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryExpress;
import com.mongodb.*;

import java.util.*;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-11-20 下午2:49
 * Description:
 */
public class GameDBAccessorMongo extends AbstractSequenceBaseMongoDbAccessor<GameDB> {

    private String COLLECTION_NAME = "game_db_new";
    private String SEQ_NAME = "SEQ_GAME_DB_ID";

    public GameDB insert(GameDB gameDB, DB db) throws DbException {
        long curValue = getSeqNo(SEQ_NAME, db);
        gameDB.setGameDbId(curValue);
        WriteResult writeResult = db.getCollection(COLLECTION_NAME).insert(entryToDBObject(gameDB));
        if (writeResult.getN() > 0) {
            return gameDB;
        }
        return gameDB;
    }

    public int countGameDB(MongoQueryExpress queryExpress, DB db) throws DbException {
        return super.count(COLLECTION_NAME, queryExpress, db);
    }

    public List<GameDB> query(MongoQueryExpress queryExpress, DB db) throws DbException {
        return super.query(queryExpress, COLLECTION_NAME, db);
    }

    public List<GameDB> query(MongoQueryExpress queryExpress, Pagination pagination, DB db) throws DbException {
        return super.query(queryExpress, pagination, COLLECTION_NAME, db);
    }

    public List<DBObject> findAll(BasicDBObject queryDBObject, Pagination pagination, DB db) throws DbException {
        List<DBObject> returnList = new ArrayList<DBObject>();

        pagination.setTotalRows(db.getCollection(COLLECTION_NAME).find(queryDBObject).count());

        DBCursor dbCursor = db.getCollection(COLLECTION_NAME).find(queryDBObject).skip(pagination.getStartRowIdx()).limit(pagination.getPageSize());

        while (dbCursor.hasNext()) {
            DBObject dbObject = dbCursor.next();
            returnList.add(dbObject);
        }
        return returnList;
    }

    public GameDB get(BasicDBObject basicDBObject, DB db) throws DbException {
        GameDB gameDB = null;
        DBObject dbObject = db.getCollection(COLLECTION_NAME).findOne(basicDBObject);
        if (dbObject != null) {
            gameDB = dbObjToEntry(dbObject);
        }
        return gameDB;
    }

    public boolean update(BasicDBObject queryDBObject, BasicDBObject updateDBObject, DB db) {
//        DBObject dbObject = db.getCollection(COLLECTION_NAME).findOne(queryDBObject);
        //原 doc ，如果不在updateDbOjbect里就覆盖
//        BasicDBObject updateObj = new BasicDBObject();
//        for (String key : dbObject.keySet()) {
//            if (!updateDBObject.keySet().contains(key)) {
//                updateObj.append(key, dbObject.get(key));
//            }
//        }

        BasicDBObject updateAllObj = new BasicDBObject();
//        updateAllObj.append("$set", updateObj);

        //修改的doc
        Set<String> updateSet = updateDBObject.keySet();
        for (String key : updateSet) {
            if (key.startsWith("$")) {
                updateAllObj.append(key, updateDBObject.get(key));
            } else {
                if (updateAllObj.get("$set") == null) {
                    updateAllObj.append("$set", new BasicDBObject());
                }
                ((BasicDBObject) updateAllObj.get("$set")).append(key, updateDBObject.get(key));
            }
        }
        int i = db.getCollection(COLLECTION_NAME).updateMulti(queryDBObject, updateAllObj).getN();
        return i > 0;
    }

    @Override
    protected GameDB dbObjToEntry(DBObject dbObject) {
        GameDB gameDB = new GameDB();
        gameDB.setGameDbId((Long) dbObject.get(GameDBField.ID.getColumn()));
        gameDB.setGameIcon(URLUtils.getJoymeDnUrl((String) dbObject.get(GameDBField.GAMEICON.getColumn())));
        gameDB.setGameDesc((String) dbObject.get(GameDBField.GAME_DESC.getColumn()));
        gameDB.setGamePCConfigurationInfoSet((String) dbObject.get(GameDBField.PC_CONFIGURATION_INFO.getColumn()));
        gameDB.setAssociatedGameId(dbObject.get(GameDBField.ASSOCIATED_GAME_ID.getColumn()) == null ? 0 : (Long) dbObject.get(GameDBField.ASSOCIATED_GAME_ID.getColumn()));
        gameDB.setGameName((String) dbObject.get(GameDBField.GAMENAME.getColumn()));
        gameDB.setAnotherName((String) dbObject.get(GameDBField.ANOTHERNAME.getColumn()));
        gameDB.setGameSize((String) dbObject.get(GameDBField.GAMESIZE.getColumn()));
        gameDB.setPlatformTypeSet(makePlatformTypeSet(dbObject));
        gameDB.setPlatformMap(makePlatformMap(dbObject));
        gameDB.setIosDownload((String) dbObject.get(GameDBField.IOSDOWNLOAD.getColumn()));
        gameDB.setAndroidDownload((String) dbObject.get(GameDBField.ANDROIDDOWNLOAD.getColumn()));
        gameDB.setWpDownload((String) dbObject.get(GameDBField.WPDOWNLOAD.getColumn()));
        gameDB.setGamePublicTime((Date) dbObject.get(GameDBField.GAMEPUBLICTIME.getColumn()));
        gameDB.setPublicTips(dbObject.get(GameDBField.PUBLICTIPS.getColumn()) == null ? false : (Boolean) dbObject.get(GameDBField.PUBLICTIPS.getColumn()));
        gameDB.setOfficialWebsite((String) dbObject.get(GameDBField.OFFICIALWEBSITE.getColumn()));
        gameDB.setGameNetType(dbObject.get(GameDBField.GAMENETTYPE.getColumn()) == null ? null : GameNetType.getByCode((Integer) dbObject.get(GameDBField.GAMENETTYPE.getColumn())));
        gameDB.setLanguageTypeSet(makeLanguageTypeSet(dbObject));
        gameDB.setCategoryTypeSet(makeCategoryTypeSet(dbObject));
        gameDB.setThemeTypeSet(makeThemeTypeSet(dbObject));
        gameDB.setGameDeveloper((String) dbObject.get(GameDBField.GAMEDEVELOPER.getColumn()));
        gameDB.setGamePublishers((String) dbObject.get(GameDBField.GAMEPUBLICSHERS.getColumn()));
        gameDB.setGameProfile((String) dbObject.get(GameDBField.GAMEPROFILE.getColumn()));
        gameDB.setGameVideo((String) dbObject.get(GameDBField.GAMEVIDEO.getColumn()));
        gameDB.setGameVideoPic((String) dbObject.get(GameDBField.GAMEVIDEOPIC.getColumn()));
        gameDB.setGamePic((String) dbObject.get(GameDBField.GAMEPIC.getColumn()));
        gameDB.setGamePicType(dbObject.get(GameDBField.GAME_PIC_TYPE.getColumn()) == null ? null : GamePicType.getByCode((Integer) dbObject.get(GameDBField.GAME_PIC_TYPE.getColumn())));
        gameDB.setWikiUrl((String) dbObject.get(GameDBField.WIKIURL.getColumn()));
        gameDB.setCmsUrl((String) dbObject.get(GameDBField.CMSURL.getColumn()));
        gameDB.setGameRate((Double) dbObject.get(GameDBField.GAMERATE.getColumn()));
        gameDB.setFavorSum(dbObject.get(GameDBField.FAVOR_SUM.getColumn()) == null ? 0 : (Integer) dbObject.get(GameDBField.FAVOR_SUM.getColumn()));
        gameDB.setUnFavorSum(dbObject.get(GameDBField.UN_FAVOR_SUM.getColumn()) == null ? 0 : (Integer) dbObject.get(GameDBField.UN_FAVOR_SUM.getColumn()));
        gameDB.setPopular(dbObject.get(GameDBField.POPULAR.getColumn()) == null ? 0 : (Integer) dbObject.get(GameDBField.POPULAR.getColumn()));
        gameDB.setNewsSum(dbObject.get(GameDBField.NEWSSUM.getColumn()) == null ? 0 : (Integer) dbObject.get(GameDBField.NEWSSUM.getColumn()));
        gameDB.setVideoSum(dbObject.get(GameDBField.VIDEOSUM.getColumn()) == null ? 0 : (Integer) dbObject.get(GameDBField.VIDEOSUM.getColumn()));
        gameDB.setGuideSum(dbObject.get(GameDBField.GUIDESUM.getColumn()) == null ? 0 : (Integer) dbObject.get(GameDBField.GUIDESUM.getColumn()));
        gameDB.setGiftSum(dbObject.get(GameDBField.GIFTSUM.getColumn()) == null ? 0 : (Integer) dbObject.get(GameDBField.GIFTSUM.getColumn()));
        gameDB.setPvSum(dbObject.get(GameDBField.PVSUM.getColumn()) == null ? 0 : (Integer) dbObject.get(GameDBField.PVSUM.getColumn()));
        gameDB.setValidStatus(dbObject.get(GameDBField.VALIDSTATUS.getColumn()) == null ? null : GameDbStatus.getByCode((String) dbObject.get(GameDBField.VALIDSTATUS.getColumn())));
        gameDB.setCreateDate((Date) dbObject.get(GameDBField.CREATE_DATE.getColumn()));
        gameDB.setCreateUser((String) dbObject.get(GameDBField.CREATE_USER.getColumn()));
        gameDB.setModifyDate((Date) dbObject.get(GameDBField.MODIFY_DATE.getColumn()));
        gameDB.setModifyUser((String) dbObject.get(GameDBField.MODIFY_USER.getColumn()));
        //玩霸
        gameDB.setVersionProfile((String) dbObject.get(GameDBField.VERSIONPROFILE.getColumn()));
        gameDB.setRecommendReason((String) dbObject.get(GameDBField.REASON.getColumn()));
        gameDB.setRecommendReason2((String) dbObject.get(GameDBField.REASON2.getColumn()));
        String modifyTimeJson = (String) dbObject.get(GameDBField.MODIFY_TIME_JSON.getColumn());
        gameDB.setModifyTime(GameDBModifyTimeFieldJson.parse(modifyTimeJson));
        String cover = (String) dbObject.get(GameDBField.GAMEDB_COVER.getColumn());
        gameDB.setGameDBCover(GameDBCover.fromJson(cover));
        String cover_field_json = (String) dbObject.get(GameDBField.GAMEDB_COVER_FIELD_JSON.getColumn());
        gameDB.setGameDBCoverFieldJson(GameDBCoverFieldJson.parse(cover_field_json));
        gameDB.setDisplayIcon(dbObject.get(GameDBField.DISPLAY_ICON.getColumn()) == null ? 0 : (Integer) dbObject.get(GameDBField.DISPLAY_ICON.getColumn()));
        gameDB.setDownloadRecommend((String) dbObject.get(GameDBField.DOWNLOADRECOMMEND.getColumn()));
        //商务
        gameDB.setTeamName((String) dbObject.get(GameDBField.TEAMNAME.getColumn()));
        gameDB.setTeamNum(dbObject.get(GameDBField.TEAMNUM.getColumn()) == null ? 0 : (Integer) dbObject.get(GameDBField.TEAMNUM.getColumn()));
        gameDB.setCity((String) dbObject.get(GameDBField.CITY.getColumn()));
        gameDB.setPublicTime((Date) dbObject.get(GameDBField.PUBLICTIME.getColumn()));
        gameDB.setFinancing(dbObject.get(GameDBField.FINANCING.getColumn()) == null ? -1 : (Integer) dbObject.get(GameDBField.FINANCING.getColumn()));
        gameDB.setContacts((String) dbObject.get(GameDBField.CONTACTS.getColumn()));
        gameDB.setEmail((String) dbObject.get(GameDBField.EMAIL.getColumn()));
        gameDB.setPhone((String) dbObject.get(GameDBField.PHONE.getColumn()));
        gameDB.setQq((String) dbObject.get(GameDBField.QQ.getColumn()));
        gameDB.setArea((Integer) dbObject.get(GameDBField.AREA.getColumn()));
        //主站首页
        gameDB.setCommentAndAgree(CommentAndAgree.parse((String) dbObject.get(GameDBField.COMMENT_AND_AGREE.getColumn())));
        //渠道下载
        gameDB.setChannelInfoSet(makeChannelInfoSet(dbObject));
        gameDB.setWikiKey((String) dbObject.get(GameDBField.WIKIKEY.getColumn()));


        gameDB.setPcDownload((String) dbObject.get(GameDBField.PC_DOWNLOAD.getColumn()));
        gameDB.setLevelGame(dbObject.get(GameDBField.LEVELGAME.getColumn()) == null ? false : (Boolean) dbObject.get(GameDBField.LEVELGAME.getColumn()));
        gameDB.setXboxoneDownload((String) dbObject.get(GameDBField.XBOXONEDOWNLOAD.getColumn()));
        gameDB.setPs4Download((String) dbObject.get(GameDBField.PS4DOWNLOAD.getColumn()));
        gameDB.setWebpageDownload((String) dbObject.get(GameDBField.WEBPAGEDOWNLOAD.getColumn()));


        gameDB.setIsbn((String) dbObject.get(GameDBField.ISBN.getColumn()));
        gameDB.setComment(dbObject.get(GameDBField.COMMENT.getColumn()) == null ? false : (Boolean) dbObject.get(GameDBField.COMMENT.getColumn()));
        gameDB.setEnglishName((String) dbObject.get(GameDBField.ENGLISHNAME.getColumn()));
        gameDB.setBackpic((String) dbObject.get(GameDBField.BACKPIC.getColumn()));
        gameDB.setAppstorePrice((String) dbObject.get(GameDBField.APPSTOREPRICE.getColumn()));
        gameDB.setVideo((String) dbObject.get(GameDBField.VIDEO.getColumn()));
        gameDB.setVpn(dbObject.get(GameDBField.VPN.getColumn()) == null ? false : (Boolean) dbObject.get(GameDBField.VPN.getColumn()));

        gameDB.setCommentScore(dbObject.get(GameDBField.COMMENTSCORE.getColumn()) == null ? "0" : (String) dbObject.get(GameDBField.COMMENTSCORE.getColumn()));
        gameDB.setCommentSum(dbObject.get(GameDBField.COMMENTSUM.getColumn()) == null ? "0" : (String) dbObject.get(GameDBField.COMMENTSUM.getColumn()));
        gameDB.setCommentGamePic(dbObject.get(GameDBField.COMMENTGAMEPIC.getColumn()) == null ? "" : (String) dbObject.get(GameDBField.COMMENTGAMEPIC.getColumn()));
        gameDB.setGameTag(dbObject.get(GameDBField.GAMETAG.getColumn()) == null ? "" : (String) dbObject.get(GameDBField.GAMETAG.getColumn()));

        return gameDB;
    }

    @Override
    protected BasicDBObject entryToDBObject(GameDB gameDB) {
        BasicDBObject dbObject = new BasicDBObject();
        //基本
        dbObject.put(GameDBField.ID.getColumn(), gameDB.getGameDbId());
        dbObject.put(GameDBField.GAMEICON.getColumn(), gameDB.getGameIcon());
        dbObject.put(GameDBField.GAME_DESC.getColumn(), gameDB.getGameDesc());
        dbObject.put(GameDBField.PC_CONFIGURATION_INFO.getColumn(), gameDB.getGamePCConfigurationInfo());
        dbObject.put(GameDBField.ASSOCIATED_GAME_ID.getColumn(), gameDB.getAssociatedGameId());
        dbObject.put(GameDBField.GAMENAME.getColumn(), gameDB.getGameName());
        dbObject.put(GameDBField.ANOTHERNAME.getColumn(), gameDB.getAnotherName());
        dbObject.put(GameDBField.GAMESIZE.getColumn(), gameDB.getGameSize());
        if (!CollectionUtil.isEmpty(gameDB.getPlatformMap())) {
            for (String key : gameDB.getPlatformMap().keySet()) {
                dbObject.put(GameDBField.PLATFORMTYPE_.getColumn() + key, true);
                for (GamePlatform platform : gameDB.getPlatformMap().get(key)) {
                    dbObject.put(GameDBField.GAME_PLATFORM_.getColumn() + key + "_" + platform.getCode(), true);
                }
            }
        }
        dbObject.put(GameDBField.IOSDOWNLOAD.getColumn(), gameDB.getIosDownload());
        dbObject.put(GameDBField.ANDROIDDOWNLOAD.getColumn(), gameDB.getAndroidDownload());
        dbObject.put(GameDBField.WPDOWNLOAD.getColumn(), gameDB.getWpDownload());
        dbObject.put(GameDBField.GAMEPUBLICTIME.getColumn(), gameDB.getGamePublicTime());
        dbObject.put(GameDBField.PUBLICTIPS.getColumn(), gameDB.getPublicTips());
        dbObject.put(GameDBField.OFFICIALWEBSITE.getColumn(), gameDB.getOfficialWebsite());
        dbObject.put(GameDBField.GAMENETTYPE.getColumn(), gameDB.getGameNetType() == null ? 0 : gameDB.getGameNetType().getCode());
        if (!CollectionUtil.isEmpty(gameDB.getLanguageTypeSet())) {
            for (GameLanguageType languageType : gameDB.getLanguageTypeSet()) {
                dbObject.put(GameDBField.GAME_LANGUAGE_.getColumn() + languageType.getCode(), true);
            }
        }
        if (!CollectionUtil.isEmpty(gameDB.getCategoryTypeSet())) {
            for (GameCategoryType categoryType : gameDB.getCategoryTypeSet()) {
                dbObject.put(GameDBField.GAME_CATEGORY_.getColumn() + categoryType.getCode(), true);
            }
        }
        if (!CollectionUtil.isEmpty(gameDB.getThemeTypeSet())) {
            for (GameThemeType themeType : gameDB.getThemeTypeSet()) {
                dbObject.put(GameDBField.GAME_THEME_.getColumn() + themeType.getCode(), true);
            }
        }
        dbObject.put(GameDBField.GAMEDEVELOPER.getColumn(), gameDB.getGameDeveloper());
        dbObject.put(GameDBField.GAMEPUBLICSHERS.getColumn(), gameDB.getGamePublishers());
        dbObject.put(GameDBField.GAMEPROFILE.getColumn(), gameDB.getGameProfile());
        dbObject.put(GameDBField.GAMEVIDEO.getColumn(), gameDB.getGameVideo());
        dbObject.put(GameDBField.GAMEVIDEOPIC.getColumn(), gameDB.getGameVideoPic());
        dbObject.put(GameDBField.GAMEPIC.getColumn(), gameDB.getGamePic());
        dbObject.put(GameDBField.GAME_PIC_TYPE.getColumn(), gameDB.getGamePicType() == null ? 1 : gameDB.getGamePicType().getCode());
        dbObject.put(GameDBField.WIKIURL.getColumn(), gameDB.getWikiUrl());
        dbObject.put(GameDBField.CMSURL.getColumn(), gameDB.getCmsUrl());
        dbObject.put(GameDBField.GAMERATE.getColumn(), gameDB.getGameRate());
        dbObject.put(GameDBField.FAVOR_SUM.getColumn(), gameDB.getFavorSum());
        dbObject.put(GameDBField.UN_FAVOR_SUM.getColumn(), gameDB.getUnFavorSum());
        dbObject.put(GameDBField.POPULAR.getColumn(), gameDB.getPopular());
        dbObject.put(GameDBField.NEWSSUM.getColumn(), gameDB.getNewsSum());
        dbObject.put(GameDBField.VIDEOSUM.getColumn(), gameDB.getVideoSum());
        dbObject.put(GameDBField.GUIDESUM.getColumn(), gameDB.getGuideSum());
        dbObject.put(GameDBField.GIFTSUM.getColumn(), gameDB.getGiftSum());
        dbObject.put(GameDBField.PVSUM.getColumn(), gameDB.getPvSum());
        dbObject.put(GameDBField.VALIDSTATUS.getColumn(), gameDB.getValidStatus() == null ? GameDbStatus.INVALID.getCode() : gameDB.getValidStatus().getCode());
        dbObject.put(GameDBField.CREATE_DATE.getColumn(), gameDB.getCreateDate() == null ? new Date() : gameDB.getCreateDate());
        dbObject.put(GameDBField.CREATE_USER.getColumn(), gameDB.getCreateUser());
        dbObject.put(GameDBField.MODIFY_DATE.getColumn(), gameDB.getModifyDate());
        dbObject.put(GameDBField.MODIFY_USER.getColumn(), gameDB.getModifyUser());
        //商务
        dbObject.put(GameDBField.TEAMNAME.getColumn(), gameDB.getTeamName());
        dbObject.put(GameDBField.TEAMNUM.getColumn(), gameDB.getTeamNum());
        dbObject.put(GameDBField.CITY.getColumn(), gameDB.getCity());
        dbObject.put(GameDBField.PUBLICTIME.getColumn(), gameDB.getPublicTime());
        dbObject.put(GameDBField.FINANCING.getColumn(), gameDB.getFinancing());
        dbObject.put(GameDBField.CONTACTS.getColumn(), gameDB.getContacts());
        dbObject.put(GameDBField.EMAIL.getColumn(), gameDB.getEmail());
        dbObject.put(GameDBField.PHONE.getColumn(), gameDB.getPhone());
        dbObject.put(GameDBField.QQ.getColumn(), gameDB.getQq());
        dbObject.put(GameDBField.AREA.getColumn(), gameDB.getArea());
        //玩霸
        dbObject.put(GameDBField.VERSIONPROFILE.getColumn(), gameDB.getVersionProfile());
        dbObject.put(GameDBField.REASON.getColumn(), gameDB.getRecommendReason());
        dbObject.put(GameDBField.REASON2.getColumn(), gameDB.getRecommendReason2());

        dbObject.put(GameDBField.DISPLAY_ICON.getColumn(), gameDB.getDisplayIcon());
        dbObject.put(GameDBField.GAMEDB_COVER.getColumn(), gameDB.getGameDBCover() == null ? new GameDBCover().toJson() : gameDB.getGameDBCover().toJson());
        dbObject.put(GameDBField.GAMEDB_COVER_FIELD_JSON.getColumn(), gameDB.getGameDBCoverFieldJson() == null ? new GameDBCoverFieldJson().toJson() : gameDB.getGameDBCoverFieldJson().toJson());
        dbObject.put(GameDBField.DOWNLOADRECOMMEND.getColumn(), gameDB.getDownloadRecommend());

        //主站首页
        dbObject.put(GameDBField.COMMENT_AND_AGREE.getColumn(), gameDB.getCommentAndAgree() == null ? new CommentAndAgree().toJson() : gameDB.getCommentAndAgree().toJson());
        //渠道下载
        if (!CollectionUtil.isEmpty(gameDB.getChannelInfoSet())) {
            for (GameDBChannelInfo gameDBChannelInfo : gameDB.getChannelInfoSet()) {
                dbObject.put(GameDBField.CHANNEL_PLATFORM_.getColumn() + gameDBChannelInfo.getChannel_id() + "_" + gameDBChannelInfo.getDevice(), true);
                dbObject.put(GameDBField.CHANNEL_DOWNLOAD_INFO_.getColumn() + gameDBChannelInfo.getChannel_id() + "_" + gameDBChannelInfo.getDevice(), gameDBChannelInfo.toJson());
            }
        }


        dbObject.put(GameDBField.PC_DOWNLOAD.getColumn(), gameDB.getPcDownload());
        dbObject.put(GameDBField.LEVELGAME.getColumn(), gameDB.isLevelGame());
        dbObject.put(GameDBField.XBOXONEDOWNLOAD.getColumn(), gameDB.getXboxoneDownload());
        dbObject.put(GameDBField.PS4DOWNLOAD.getColumn(), gameDB.getPs4Download());
        dbObject.put(GameDBField.WEBPAGEDOWNLOAD.getColumn(), gameDB.getWebpageDownload());


        dbObject.put(GameDBField.COMMENT.getColumn(), gameDB.isComment());
        dbObject.put(GameDBField.ISBN.getColumn(), gameDB.getIsbn());
        dbObject.put(GameDBField.ENGLISHNAME.getColumn(), gameDB.getEnglishName());
        dbObject.put(GameDBField.BACKPIC.getColumn(), gameDB.getBackpic());
        dbObject.put(GameDBField.APPSTOREPRICE.getColumn(), gameDB.getAppstorePrice());
        dbObject.put(GameDBField.VIDEO.getColumn(), gameDB.getVideo());
        dbObject.put(GameDBField.VPN.getColumn(), gameDB.isVpn());
        dbObject.put(GameDBField.COMMENTSCORE.getColumn(), gameDB.getCommentScore());
        dbObject.put(GameDBField.COMMENTSUM.getColumn(), gameDB.getCommentSum());
        dbObject.put(GameDBField.COMMENTGAMEPIC.getColumn(), gameDB.getCommentGamePic());
        dbObject.put(GameDBField.GAMETAG.getColumn(), gameDB.getGameTag());

        return dbObject;
    }

    private Map<String, Set<GamePlatform>> makePlatformMap(DBObject dbObject) {
        Map<String, Set<GamePlatform>> returnMap = new HashMap<String, Set<GamePlatform>>();
        for (String key : dbObject.keySet()) {
            try {
                if (key.startsWith(GameDBField.GAME_PLATFORM_.getColumn())) {
                    if (dbObject.get(key) == null || dbObject.get(key) == "") {
                        continue;
                    }
                    if ((Boolean) dbObject.get(key)) {
                        String[] platformArr = key.substring(GameDBField.GAME_PLATFORM_.getColumn().length()).split("_");
                        int type = Integer.parseInt(platformArr[0]);
                        GamePlatformType gamePlatformType = GamePlatformType.getByCode(type);
                        if (gamePlatformType.equals(GamePlatformType.MOBILE)) {
                            MobilePlatform platform = MobilePlatform.getByCode(Integer.parseInt(platformArr[1]));
                            if (platform != null) {
                                if (returnMap.containsKey(platformArr[0])) {
                                    returnMap.get(platformArr[0]).add(platform);
                                } else {
                                    returnMap.put(platformArr[0], new HashSet<GamePlatform>());
                                    returnMap.get(platformArr[0]).add(platform);
                                }
                            }
                        } else if (gamePlatformType.equals(GamePlatformType.PC)) {
                            PCPlatform platform = PCPlatform.getByCode(Integer.parseInt(platformArr[1]));
                            if (platform != null) {
                                if (returnMap.containsKey(platformArr[0])) {
                                    returnMap.get(platformArr[0]).add(platform);
                                } else {
                                    returnMap.put(platformArr[0], new HashSet<GamePlatform>());
                                    returnMap.get(platformArr[0]).add(platform);
                                }
                            }
                        } else if (gamePlatformType.equals(GamePlatformType.PSP)) {
                            PSPPlatform platform = PSPPlatform.getByCode(Integer.parseInt(platformArr[1]));
                            if (platform != null) {
                                if (returnMap.containsKey(platformArr[0])) {
                                    returnMap.get(platformArr[0]).add(platform);
                                } else {
                                    returnMap.put(platformArr[0], new HashSet<GamePlatform>());
                                    returnMap.get(platformArr[0]).add(platform);
                                }
                            }
                        } else if (gamePlatformType.equals(GamePlatformType.TV)) {
                            TVPlatform platform = TVPlatform.getByCode(Integer.parseInt(platformArr[1]));
                            if (platform != null) {
                                if (returnMap.containsKey(platformArr[0])) {
                                    returnMap.get(platformArr[0]).add(platform);
                                } else {
                                    returnMap.put(platformArr[0], new HashSet<GamePlatform>());
                                    returnMap.get(platformArr[0]).add(platform);
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
        return returnMap;
    }

    private Set<GameLanguageType> makeLanguageTypeSet(DBObject dbObject) {
        Set<GameLanguageType> returnSet = new HashSet<GameLanguageType>();
        for (String key : dbObject.keySet()) {
            try {
                if (key.startsWith(GameDBField.GAME_LANGUAGE_.getColumn())) {
                    if (dbObject.get(key) == null || dbObject.get(key) == "") {
                        continue;
                    }
                    if ((Boolean) dbObject.get(key)) {
                        String languageStr = key.substring(GameDBField.GAME_LANGUAGE_.getColumn().length());
                        GameLanguageType languageType = GameLanguageType.getByCode(Integer.parseInt(languageStr));
                        if (languageType != null) {
                            returnSet.add(languageType);
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
        return returnSet;
    }

    private Set<GameCategoryType> makeCategoryTypeSet(DBObject dbObject) {
        Set<GameCategoryType> returnSet = new HashSet<GameCategoryType>();
        for (String key : dbObject.keySet()) {
            try {
                if (key.startsWith(GameDBField.GAME_CATEGORY_.getColumn())) {
                    if (dbObject.get(key) == null || dbObject.get(key) == "") {
                        continue;
                    }
                    if ((Boolean) dbObject.get(key)) {
                        String categoryStr = key.substring(GameDBField.GAME_CATEGORY_.getColumn().length());
                        GameCategoryType categoryType = GameCategoryType.getByCode(Integer.parseInt(categoryStr));
                        if (categoryType != null) {
                            returnSet.add(categoryType);
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
        return returnSet;
    }

    private Set<GameThemeType> makeThemeTypeSet(DBObject dbObject) {
        Set<GameThemeType> returnSet = new HashSet<GameThemeType>();
        for (String key : dbObject.keySet()) {
            try {
                if (key.startsWith(GameDBField.GAME_THEME_.getColumn())) {
                    if (dbObject.get(key) == null || dbObject.get(key) == "") {
                        continue;
                    }
                    if ((Boolean) dbObject.get(key)) {
                        String themeStr = key.substring(GameDBField.GAME_THEME_.getColumn().length());
                        GameThemeType themeType = GameThemeType.getByCode(Integer.parseInt(themeStr));
                        if (themeType != null) {
                            returnSet.add(themeType);
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
        return returnSet;
    }

    private Set<GameDBChannelInfo> makeChannelInfoSet(DBObject dbObject) {
        Set<GameDBChannelInfo> returnSet = new HashSet<GameDBChannelInfo>();
        for (String key : dbObject.keySet()) {
            if (key.startsWith(GameDBField.CHANNEL_PLATFORM_.getColumn())) {
                if (dbObject.get(key) == null || dbObject.get(key) == "") {
                    continue;
                }
                if ((Boolean) dbObject.get(key)) {
                    String keyArray[] = key.substring(GameDBField.CHANNEL_PLATFORM_.getColumn().length()).split("_");
                    GameDBChannelInfo channelInfo = GameDBChannelInfo.parse(dbObject.get(GameDBField.CHANNEL_DOWNLOAD_INFO_.getColumn() + keyArray[0] + "_" + keyArray[1]));
                    if (channelInfo != null) {
                        returnSet.add(channelInfo);
                    }
                }
            }
        }
        return returnSet;
    }

    private Set<GamePlatformType> makePlatformTypeSet(DBObject dbObject) {
        Set<GamePlatformType> set = new HashSet<GamePlatformType>();
        for (String key : dbObject.keySet()) {
            if (key.startsWith(GameDBField.PLATFORMTYPE_.getColumn())) {
                if (dbObject.get(key) == null || dbObject.get(key) == "") {
                    continue;
                }
                if ((Boolean) dbObject.get(key)) {
                    String type = key.substring(GameDBField.PLATFORMTYPE_.getColumn().length());
                    GamePlatformType gamePlatformType = GamePlatformType.getByCode(Integer.parseInt(type));
                    if (gamePlatformType != null) {
                        set.add(gamePlatformType);
                    }
                }
            }
        }
        return set;
    }

}
