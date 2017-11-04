package com.enjoyf.webapps.tools.webpage.controller.gameres;

import com.alibaba.fastjson.JSONObject;
import com.enjoyf.platform.cloud.contentservice.GameUtil;
import com.enjoyf.platform.io.mail.MailMessageText;
import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.WebHotdeployConfig;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.ask.AskServiceSngl;
import com.enjoyf.platform.service.ask.AskUtil;
import com.enjoyf.platform.service.ask.WikiGameres;
import com.enjoyf.platform.service.ask.WikiGameresField;
import com.enjoyf.platform.service.ask.search.WikiappSearchEntry;
import com.enjoyf.platform.service.ask.search.WikiappSearchType;
import com.enjoyf.platform.service.event.EventDispatchServiceSngl;
import com.enjoyf.platform.service.event.system.wiki.WikiappSearchEvent;
import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.gameres.gamedb.*;
import com.enjoyf.platform.service.joymeapp.Archive;
import com.enjoyf.platform.service.joymeapp.ClientLineItem;
import com.enjoyf.platform.service.joymeapp.ClientLineItemField;
import com.enjoyf.platform.service.joymeapp.JoymeAppServiceSngl;
import com.enjoyf.platform.service.mail.EmailServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.service.tools.ToolsLog;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.archive.ArchiveUtil;
import com.enjoyf.platform.util.http.URLUtils;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.redis.RedisManager;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryCriterions;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryExpress;
import com.enjoyf.platform.util.sql.mongodb.MongoSort;
import com.enjoyf.platform.util.sql.mongodb.MongoSortOrder;
import com.enjoyf.platform.webapps.common.util.CookieUtil;
import com.enjoyf.util.HttpClientManager;
import com.enjoyf.util.HttpParameter;
import com.enjoyf.util.HttpResult;
import com.enjoyf.webapps.tools.util.MicroAuthUtil;
import com.enjoyf.webapps.tools.weblogic.gameres.GamedbSendPhpChannel;
import com.enjoyf.webapps.tools.weblogic.privilege.CacheUtil;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import com.mongodb.BasicDBObject;
import net.sf.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 13-11-22
 * Time: 上午11:40
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/gamedb")
public class GameDBController extends ToolsBaseController {

    private static RedisManager redisManager = new RedisManager(WebappConfig.get().getProps());
    private static final String WEBAPPWWW_GAME_COLLECTION_PARAM_SET = "webappwww_game_collection_param_set";

    private static List<GamePicType> picTypeList = new ArrayList<GamePicType>();

    static {
        picTypeList.add(GamePicType.VERTICAL);
        picTypeList.add(GamePicType.TRANSVERSE);
    }

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                             @RequestParam(value = "validstatus", required = false) String validstatus,
                             @RequestParam(value = "errorMsg", required = false) String errorMsg,
                             @RequestParam(value = "searchname", required = false) String searchname) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("errorMsg", errorMsg);
        try {
            int curPage = (pageStartIndex / pageSize) + 1;
            Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
            MongoQueryExpress queryExpress = new MongoQueryExpress();
            queryExpress.add(new MongoSort[]{new MongoSort(GameDBField.ID, MongoSortOrder.DESC)});
            if (!StringUtil.isEmpty(validstatus)) {
                queryExpress.add(MongoQueryCriterions.eq(GameDBField.VALIDSTATUS, validstatus));
                mapMessage.put("validstatus", validstatus);
            }
            if (!StringUtil.isEmpty(searchname)) {
                queryExpress.add(MongoQueryCriterions.like(GameDBField.GAMENAME, searchname));
                mapMessage.put("searchname", searchname);
            }
            PageRows<GameDB> pageRows = GameResourceServiceSngl.get().queryGameDbByPage(queryExpress, pagination);
            if (pageRows != null) {
                mapMessage.put("page", pageRows.getPage());
                mapMessage.put("list", pageRows.getRows());
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMsg", "system.error");
        }
        return new ModelAndView("gameresource/gamedb/gamedblist", mapMessage);
    }

    @RequestMapping(value = "/relation/page")
    public ModelAndView relationPage(@RequestParam(value = "gamedbid", required = false) Long gameId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        BasicDBObject queryDBObject = new BasicDBObject();
        try {
            queryDBObject.put(GameDBField.ID.getColumn(), gameId);
            GameDB gameDb = GameResourceServiceSngl.get().getGameDB(queryDBObject, true);
            mapMessage.put("gameDb", gameDb);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMsg", "system.error");
        }

        return new ModelAndView("/gameresource/gamedb/relation/relation_wiki", mapMessage);
    }


    @RequestMapping(value = "/createpage")
    public ModelAndView createPage(@RequestParam(value = "errorMsg", required = false) String errorMsg,
                                   @RequestParam(value = "gptype", required = false) Integer gamePlatformType) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            mapMessage.put("errorMsg", errorMsg);
            mapMessage.put("gamePlatformType", gamePlatformType);
            mapMessage.put("mPlatforms", MobilePlatform.getAll());
            mapMessage.put("pcPlatforms", PCPlatform.getAll());
            mapMessage.put("pspPlatforms", PSPPlatform.getAll());
            mapMessage.put("tvPlatforms", TVPlatform.getAll());
            mapMessage.put("categoryTypes", GameCategoryType.getAll());
            mapMessage.put("netTypes", GameNetType.getAll());
            mapMessage.put("languageTypes", GameLanguageType.getAll());
            mapMessage.put("themeTypes", GameThemeType.getAll());
            mapMessage.put("picTypes", picTypeList);

            List<GameDBChannel> list = GameResourceServiceSngl.get().queryGameDbChannel();
            mapMessage.put("channelList", list);

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMsg", "system.error");
        }
        return new ModelAndView("/gameresource/gamedb/createpage", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(HttpServletRequest request, HttpServletResponse response,
                               @RequestParam(value = "gameicon", required = false) String gameIcon,
                               @RequestParam(value = "gamename", required = false) String gameName,
                               @RequestParam(value = "anothername", required = false) String anotherName,
                               @RequestParam(value = "gamesize", required = false) String gameSize,
                               @RequestParam(value = "gameplatformtype", required = false, defaultValue = "0") Integer gamePlatformType,
                               @RequestParam(value = "mplatform", required = false) String mPlatformStr,
                               @RequestParam(value = "pcplatform", required = false) String pcPlatformStr,
                               @RequestParam(value = "pspplatform", required = false) String pspPlatformStr,
                               @RequestParam(value = "tvplatform", required = false) String tvPlatformStr,
                               @RequestParam(value = "iosdownload", required = false) String iosDownload,
                               @RequestParam(value = "androiddownload", required = false) String androidDownload,
                               @RequestParam(value = "wpdownload", required = false) String wpDownload,
                               @RequestParam(value = "publictime", required = false) String gamePublicTime,
                               @RequestParam(value = "publichtips", required = false, defaultValue = "false") Boolean publicTips,
                               @RequestParam(value = "officialwebsite", required = false) String officialWebsite,
                               @RequestParam(value = "nettype", required = false) String netType,
                               @RequestParam(value = "languagetype", required = false) String languageTypeStr,
                               @RequestParam(value = "category", required = false) String categoryStr,
                               @RequestParam(value = "themetype", required = false) String themeTypeStr,
                               @RequestParam(value = "developer", required = false) String developer,
                               @RequestParam(value = "publishers", required = false) String publishers,
                               @RequestParam(value = "gameprofile", required = false) String gameProfile,
                               @RequestParam(value = "gamevideo", required = false) String gameVideo,
                               @RequestParam(value = "gamepics", required = false) String gamePic,
                               @RequestParam(value = "pictype", required = false, defaultValue = "0") Integer picType,
                               @RequestParam(value = "wikiurl", required = false) String wikiUrl,
                               @RequestParam(value = "cmsurl", required = false) String cmsUrl,
                               @RequestParam(value = "gamerate", required = false, defaultValue = "8.0") Double gameRate,
                               @RequestParam(value = "popular", required = false, defaultValue = "0") Integer popular,
                               //商务
                               @RequestParam(value = "teamname", required = false) String teamName,
                               @RequestParam(value = "teamnum", required = false, defaultValue = "0") Integer teamNum,
                               @RequestParam(value = "city", required = false) String city,
                               @RequestParam(value = "financing", required = false, defaultValue = "0") Integer financing,
                               @RequestParam(value = "contacts", required = false) String contacts,
                               @RequestParam(value = "email", required = false) String email,
                               @RequestParam(value = "phone", required = false) String phone,
                               @RequestParam(value = "qq", required = false) String qq,
                               @RequestParam(value = "area", required = false, defaultValue = "0") Integer area,
                               @RequestParam(value = "displayicon", required = false) Integer displayIcon,
                               //玩霸
                               @RequestParam(value = "reason", required = false) String reason,
                               @RequestParam(value = "reason2", required = false) String reason2,
                               @RequestParam(value = "versionprofile", required = false) String versionProfile,
                               @RequestParam(value = "downloadRecommend", required = false) String downloadRecommend,

                               @RequestParam(value = "coverPicUrl", required = false) String coverPicUrl,
                               @RequestParam(value = "coverTitle", required = false) String coverTitle,
                               @RequestParam(value = "coverComment", required = false) String coverComment,
                               @RequestParam(value = "coverDesc", required = false) String coverDesc,
                               @RequestParam(value = "coverAgreeNum", required = false, defaultValue = "") String coverAgreeNum,
                               @RequestParam(value = "coverDownload", required = false) String coverDownload,
                               @RequestParam(value = "coverDownloadAndroid", required = false) String coverDownloadAndroid,
                               @RequestParam(value = "key1", required = false) String key1,
                               @RequestParam(value = "value1", required = false) String value1,
                               @RequestParam(value = "key2", required = false) String key2,
                               @RequestParam(value = "value2", required = false) String value2,
                               @RequestParam(value = "key3", required = false) String key3,
                               @RequestParam(value = "value3", required = false) String value3,
                               @RequestParam(value = "key4", required = false) String key4,
                               @RequestParam(value = "value4", required = false) String value4,
                               @RequestParam(value = "key5", required = false) String key5,
                               @RequestParam(value = "value5", required = false) String value5,
                               @RequestParam(value = "posterShowTypeIos", required = false) String posterShowTypeIos,
                               @RequestParam(value = "posterShowContentIos", required = false) String posterShowContentIos,
                               @RequestParam(value = "posterGamePublicTimeIos", required = false) String posterGamePublicTimeIos,
                               @RequestParam(value = "posterShowTypeAndroid", required = false) String posterShowTypeAndroid,
                               @RequestParam(value = "posterShowContentAndroid", required = false) String posterShowContentAndroid,
                               @RequestParam(value = "posterGamePublicTimeAndroid", required = false) String posterGamePublicTimeAndroid,
                               //着迷网首页 “大家对游戏的看法”
                               @RequestParam(value = "comment1", required = false) String comment1,
                               @RequestParam(value = "comment2", required = false) String comment2,
                               @RequestParam(value = "comment3", required = false) String comment3,
                               @RequestParam(value = "agree1", required = false) String agree1,
                               @RequestParam(value = "agree2", required = false) String agree2,
                               @RequestParam(value = "agree3", required = false) String agree3,
                               //渠道下载信息
                               @RequestParam(value = "channeldownloadinfo", required = false) String channelDownloadInfoStr,
                               @RequestParam(value = "gamedesc", required = false) String gameDesc,
                               @RequestParam(value = "pcconfigurationinfo1", required = false) String pcConfigurationinfo1,
                               @RequestParam(value = "pcconfigurationinfo2", required = false) String pcConfigurationinfo2,

                               @RequestParam(value = "pc_download", required = false) String pc_download,
                               @RequestParam(value = "webpageDownload", required = false) String webpageDownload,
                               @RequestParam(value = "levelGame", required = false, defaultValue = "false") Boolean levelGame,
                               @RequestParam(value = "xboxoneDownload", required = false, defaultValue = "") String xboxoneDownload,
                               @RequestParam(value = "ps4Download", required = false, defaultValue = "") String ps4Download,

                               @RequestParam(value = "isbn", required = false, defaultValue = "") String isbn,
                               @RequestParam(value = "englishName", required = false, defaultValue = "") String englishName,
                               @RequestParam(value = "backpic", required = false, defaultValue = "") String backpic,
                               @RequestParam(value = "appstorePrice", required = false, defaultValue = "") String appstorePrice,
                               @RequestParam(value = "video", required = false, defaultValue = "") String video,
                               @RequestParam(value = "vpn", required = false, defaultValue = "false") Boolean vpn,
                               @RequestParam(value = "commentGamePic", required = false, defaultValue = "") String commentGamePic,
                               @RequestParam(value = "gameTag", required = false, defaultValue = "") String gameTag


    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            GameDB gameDB = new GameDB();
            gameDB.setGameIcon(gameIcon);
            gameDB.setGameName(gameName);
            gameDB.setGameDesc(gameDesc);
            gameDB.setAnotherName(anotherName);
            gameDB.setGameSize(gameSize);

            gameDB.setIsbn(isbn);
            gameDB.setEnglishName(englishName);
            gameDB.setBackpic(backpic);
            gameDB.setAppstorePrice(appstorePrice);
            gameDB.setVideo(video);
            gameDB.setVpn(vpn);
            gameDB.setCommentGamePic(commentGamePic);
            gameDB.setGameTag(gameTag);


            gameDB.setIosDownload(iosDownload);
            gameDB.setAndroidDownload(androidDownload);
            gameDB.setWpDownload(wpDownload);
            if (!StringUtil.isEmpty(gamePublicTime)) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                gameDB.setGamePublicTime(df.parse(gamePublicTime));
            }
            gameDB.setPublicTips(publicTips);
            if (publicTips) {
                MailMessageText mail = new MailMessageText();
                mail.setBody("您添加的游戏" + gameName + "将于" + gamePublicTime + "上市，请您手动为邮件添加提醒，避免错过。");
                mail.setFrom("java_server", "java_server@staff.joyme.com");
                for (String mailto : HotdeployConfigFactory.get().getConfig(WebHotdeployConfig.class).getTipsMail()) {
                    mail.setTo("", mailto);
                }
                mail.setSubject(gameName + "上市提醒");
                try {
                    EmailServiceSngl.get().send(mail);
                } catch (Exception e) {
                }
            }

            gameDB.setOfficialWebsite(officialWebsite);
            if (!StringUtil.isEmpty(netType)) {
                gameDB.setGameNetType(GameNetType.getByCode(Integer.valueOf(netType)));
            }


            Set<GameLanguageType> languageTypeSet = new HashSet<GameLanguageType>();
            if (!StringUtil.isEmpty(languageTypeStr)) {
                if (languageTypeStr.indexOf(",") > 0) {
                    String[] languageTypeArr = languageTypeStr.split(",");
                    for (String language : languageTypeArr) {
                        GameLanguageType languageType = GameLanguageType.getByCode(Integer.parseInt(language));
                        if (languageType != null) {
                            languageTypeSet.add(languageType);
                        }
                    }
                } else {
                    GameLanguageType languageType = GameLanguageType.getByCode(Integer.parseInt(languageTypeStr));
                    if (languageType != null) {
                        languageTypeSet.add(languageType);
                    }
                }
            }
            gameDB.setLanguageTypeSet(languageTypeSet);
            Set<GameCategoryType> categoryTypeSet = new HashSet<GameCategoryType>();
            if (!StringUtil.isEmpty(categoryStr)) {
                if (categoryStr.indexOf(",") > 0) {
                    String[] categoryArr = categoryStr.split(",");
                    for (String category : categoryArr) {
                        GameCategoryType categoryType = GameCategoryType.getByCode(Integer.parseInt(category));
                        if (categoryType != null) {
                            categoryTypeSet.add(categoryType);
                        }
                    }
                } else {
                    GameCategoryType categoryType = GameCategoryType.getByCode(Integer.parseInt(categoryStr));
                    if (categoryType != null) {
                        categoryTypeSet.add(categoryType);
                    }
                }
            }
            gameDB.setCategoryTypeSet(categoryTypeSet);
            Set<GameThemeType> themeTypeSet = new HashSet<GameThemeType>();
            if (!StringUtil.isEmpty(themeTypeStr)) {
                if (themeTypeStr.indexOf(",") > 0) {
                    String[] themeArr = themeTypeStr.split(",");
                    for (String theme : themeArr) {
                        GameThemeType themeType = GameThemeType.getByCode(Integer.parseInt(theme));
                        if (themeType != null) {
                            themeTypeSet.add(themeType);
                        }
                    }
                } else {
                    GameThemeType themeType = GameThemeType.getByCode(Integer.parseInt(themeTypeStr));
                    if (themeType != null) {
                        themeTypeSet.add(themeType);
                    }
                }
            }
            gameDB.setThemeTypeSet(themeTypeSet);
            gameDB.setGameDeveloper(developer);
            gameDB.setGamePublishers(publishers);
            gameDB.setGameProfile(gameProfile);
            gameDB.setGameVideo(gameVideo);

            if (!StringUtil.isEmpty(gameVideo)) {
                String archiveId = ArchiveUtil.getArchiveId(gameVideo);
                if (!StringUtil.isEmpty(archiveId)) {
                    Archive archive = JoymeAppServiceSngl.get().getArchiveById(Integer.valueOf(archiveId));
                    if (archive != null) {
                        gameDB.setGameVideoPic(archive.getIcon());
                    }
                }
            }
            gameDB.setGamePic(gamePic);
            gameDB.setGamePicType(GamePicType.getByCode(picType));
            gameDB.setWikiUrl(wikiUrl);
            gameDB.setCmsUrl(cmsUrl);
            gameDB.setGameRate(gameRate == null ? 8.0 : gameRate);
            gameDB.setPopular(popular);
            gameDB.setModifyDate(new Date());
            gameDB.setModifyUser(CookieUtil.getCookieValue(request, CacheUtil.TOOLS_COOKIEKEY_UID));
            gameDB.setCreateDate(new Date());
            gameDB.setCreateUser(CookieUtil.getCookieValue(request, CacheUtil.TOOLS_COOKIEKEY_UID));
            gameDB.setValidStatus(GameDbStatus.INVALID);
            /**
             * 玩霸
             */
            //摇一摇
            gameDB.setRecommendReason(reason);
            gameDB.setRecommendReason2(reason2);
            gameDB.setVersionProfile(versionProfile);
            gameDB.setDownloadRecommend(downloadRecommend);
            //游戏封面
            GameDBCover cover = new GameDBCover();
            cover.setCoverPicUrl(coverPicUrl);
            cover.setCoverTitle(coverTitle);
            cover.setCoverComment(coverComment);
            cover.setCoverDesc(coverDesc);
            cover.setCoverAgreeNum(coverAgreeNum.trim());
            cover.setCoverDownload(coverDownload);//这是ios的开关
            cover.setCoverDownloadAndroid(coverDownloadAndroid);//这是android的开关
            GameDBCoverFieldJson coverJson = new GameDBCoverFieldJson();
            coverJson.setKey1(key1);
            coverJson.setValue1(value1);
            coverJson.setKey2(key2);
            coverJson.setValue2(value2);
            coverJson.setKey3(key3);
            coverJson.setValue3(value3);
            coverJson.setKey4(key4);
            coverJson.setValue4(value4);
            coverJson.setKey5(key5);
            coverJson.setValue5(value5);
            cover.setPosterShowTypeIos(posterShowTypeIos);
            cover.setPosterShowContentIos(posterShowContentIos);
            if (!StringUtil.isEmpty(posterGamePublicTimeIos)) {
                if (Pattern.compile("[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}\\s+[0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}").matcher(posterGamePublicTimeIos).matches()) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        long posterGamePublicTimeLongIos = sdf.parse(posterGamePublicTimeIos).getTime();
                        cover.setPosterGamePublicTimeIos(String.valueOf(posterGamePublicTimeLongIos)); //数据库中存放long值
                    } catch (ParseException p) {
                        p.printStackTrace();
                    }
                }
            }
            cover.setPosterShowTypeAndroid(posterShowTypeAndroid);
            cover.setPosterShowContentAndroid(posterShowContentAndroid);
            if (!StringUtil.isEmpty(posterGamePublicTimeAndroid)) {
                if (Pattern.compile("[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}\\s+[0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}").matcher(posterGamePublicTimeAndroid).matches()) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        long posterGamePublicTimeLongAndroid = sdf.parse(posterGamePublicTimeAndroid).getTime();
                        cover.setPosterGamePublicTimeAndroid(String.valueOf(posterGamePublicTimeLongAndroid)); //数据库中存放long值
                    } catch (ParseException p) {
                        p.printStackTrace();
                    }
                }
            }
            gameDB.setGameDBCoverFieldJson(coverJson);
            gameDB.setGameDBCover(cover);
            /**
             * 商务
             */
            gameDB.setTeamName(teamName);
            gameDB.setTeamNum(teamNum);
            gameDB.setCity(city);
            gameDB.setFinancing(financing);
            gameDB.setContacts(contacts);
            gameDB.setEmail(email);
            gameDB.setPhone(phone);
            gameDB.setQq(qq);
            gameDB.setArea(area);
            if (displayIcon != null) {
                gameDB.setDisplayIcon(displayIcon);
            }
            /**
             * 渠道下载
             */
            if (!StringUtil.isEmpty(channelDownloadInfoStr) && channelDownloadInfoStr.startsWith("[")) {
                JSONArray jsonArray = JSONArray.fromObject(channelDownloadInfoStr);
                if (jsonArray != null && !jsonArray.isEmpty()) {
                    Set<GameDBChannelInfo> channelInfoSet = new HashSet<GameDBChannelInfo>();
                    for (Object object : jsonArray) {
                        GameDBChannelInfo gameDBChannelInfo = GameDBChannelInfo.parse(object);
                        if (gameDBChannelInfo != null) {
                            channelInfoSet.add(gameDBChannelInfo);
                        }
                    }
                    gameDB.setChannelInfoSet(channelInfoSet);
                }
            }
            /**
             * 着迷网首页
             */
            CommentAndAgree commentAndAgree = new CommentAndAgree();
            commentAndAgree.setComment1(StringUtil.isEmpty(comment1) ? "" : comment1);
            commentAndAgree.setComment2(StringUtil.isEmpty(comment2) ? "" : comment2);
            commentAndAgree.setComment3(StringUtil.isEmpty(comment3) ? "" : comment3);
            commentAndAgree.setAgree1(StringUtil.isEmpty(agree1) ? "0" : agree1);
            commentAndAgree.setAgree2(StringUtil.isEmpty(agree2) ? "0" : agree2);
            commentAndAgree.setAgree3(StringUtil.isEmpty(agree3) ? "0" : agree3);
            gameDB.setCommentAndAgree(commentAndAgree);


            BasicDBObject query = new BasicDBObject();
            query.put(GameDBField.GAMENAME.getColumn(), gameName);
            GameDB existGameName = GameResourceServiceSngl.get().getGameDB(query, false);
            if (existGameName != null) {
                mapMessage.put("errorMsg", "game.gamename.exists");
                mapMessage.put("gameDb", gameDB);

                List<String> gamePicList = new ArrayList<String>();
                if (gamePic.indexOf(",") > 0) {
                    String[] picArr = gamePic.split(",");
                    for (String pic : picArr) {
                        if (!StringUtil.isEmpty(pic)) {
                            gamePicList.add(URLUtils.getJoymeDnUrl(pic));
                        }
                    }
                } else {
                    if (!StringUtil.isEmpty(gamePic)) {
                        gamePicList.add(URLUtils.getJoymeDnUrl(gamePic));
                    }
                }
                mapMessage.put("gamePicList", gamePicList);


                List<String> commentGamePicList = new ArrayList<String>();
                if (!StringUtil.isEmpty(commentGamePic)) {
                    commentGamePicList = Arrays.asList(commentGamePic.split(","));
                }
                mapMessage.put("commentGamePicList", commentGamePicList);

                mapMessage.put("gamePlatformType", gamePlatformType);
                mapMessage.put("mPlatforms", MobilePlatform.getAll());
                mapMessage.put("pcPlatforms", PCPlatform.getAll());
                mapMessage.put("pspPlatforms", PSPPlatform.getAll());
                mapMessage.put("tvPlatforms", TVPlatform.getAll());
                mapMessage.put("categoryTypes", GameCategoryType.getAll());
                mapMessage.put("netTypes", GameNetType.getAll());
                mapMessage.put("languageTypes", GameLanguageType.getAll());
                mapMessage.put("themeTypes", GameThemeType.getAll());
                mapMessage.put("picTypes", picTypeList);
                List<GameDBChannel> list = GameResourceServiceSngl.get().queryGameDbChannel();
                mapMessage.put("channelList", list);

                return new ModelAndView("/gameresource/gamedb/createpage", mapMessage);
            }

            Map<String, Set<GamePlatform>> platformMap = new HashMap<String, Set<GamePlatform>>();
            
            /*
             * 手机游戏入库
             */
            int isM = 0;
            if (!StringUtil.isEmpty(mPlatformStr)) {
                isM++;
                Set<GamePlatform> mobilePlatformSet = new HashSet<GamePlatform>();
                if (mPlatformStr.indexOf(",") > 0) {
                    String[] mPlatformArr = mPlatformStr.split(",");
                    for (String platform : mPlatformArr) {
                        MobilePlatform mobilePlatform = MobilePlatform.getByCode(Integer.parseInt(platform));
                        if (mobilePlatform != null) {
                            mobilePlatformSet.add(mobilePlatform);
                        }
                    }
                } else {
                    MobilePlatform mobilePlatform = MobilePlatform.getByCode(Integer.parseInt(mPlatformStr));
                    if (mobilePlatform != null) {
                        mobilePlatformSet.add(mobilePlatform);
                    }
                }
                platformMap.put(String.valueOf(GamePlatformType.MOBILE.getCode()), mobilePlatformSet);


                //创建游戏时，如果是手机游戏，会清空用户填的以下字段：最低配置、推荐配置 PC下载地址 PS4下载地址  xboxone下载地址
                gameDB.setGamePCConfigurationInfoSet("");
                gameDB.setPcDownload("");
                gameDB.setPs4Download("");
                gameDB.setXboxoneDownload("");

                gameDB.setPlatformMap(platformMap);
                gameDB = GameResourceServiceSngl.get().createGameDb(gameDB);
                createGameDBRelationAndLog(gameDB, request);

                //TODO
                GameUtil.updateGameTags(gameDB, MicroAuthUtil.getToken());
            }

            //pc游戏配置需求
            JSONObject pcConfiguration = new JSONObject();
            if (!StringUtil.isEmpty(pcConfigurationinfo1)) {
                pcConfiguration.put("1", pcConfigurationinfo1.replace("\r\n", "\n"));
            }
            if (!StringUtil.isEmpty(pcConfigurationinfo2)) {
                pcConfiguration.put("2", pcConfigurationinfo1.replace("\r\n", "\n"));
            }
            if (!pcConfiguration.isEmpty()) {
                gameDB.setGamePCConfigurationInfoSet(pcConfiguration.toJSONString());
            }


            if (!StringUtil.isEmpty(pc_download)) {
                gameDB.setPcDownload(pc_download);
            }


            if (!StringUtil.isEmpty(webpageDownload)) {
                gameDB.setWebpageDownload(webpageDownload);
            }
            gameDB.setLevelGame(levelGame);
            gameDB.setPs4Download(ps4Download);
            gameDB.setXboxoneDownload(xboxoneDownload);


            /*
             * 非手机游戏入库
             */
            int isNotM = 0;//标记是否是非手机类游戏
            if (!StringUtil.isEmpty(pcPlatformStr)) {
                isNotM++;
                Set<GamePlatform> pcPlatformSet = new HashSet<GamePlatform>();
                if (pcPlatformStr.indexOf(",") > 0) {
                    String[] pcPlatformArr = pcPlatformStr.split(",");
                    for (String platform : pcPlatformArr) {
                        PCPlatform pcPlatform = PCPlatform.getByCode(Integer.parseInt(platform));
                        if (pcPlatform != null) {
                            pcPlatformSet.add(pcPlatform);
                        }
                    }
                } else {
                    PCPlatform pcPlatform = PCPlatform.getByCode(Integer.parseInt(pcPlatformStr));
                    if (pcPlatform != null) {
                        pcPlatformSet.add(pcPlatform);
                    }
                }
                platformMap.put(String.valueOf(GamePlatformType.PC.getCode()), pcPlatformSet);
            }

            if (!StringUtil.isEmpty(pspPlatformStr)) {
                isNotM++;
                Set<GamePlatform> pspPlatformSet = new HashSet<GamePlatform>();
                if (pspPlatformStr.indexOf(",") > 0) {
                    String[] pspPlatformArr = pspPlatformStr.split(",");
                    for (String platform : pspPlatformArr) {
                        PSPPlatform pspPlatform = PSPPlatform.getByCode(Integer.parseInt(platform));
                        if (pspPlatform != null) {
                            pspPlatformSet.add(pspPlatform);
                        }
                    }
                } else {
                    PSPPlatform pspPlatform = PSPPlatform.getByCode(Integer.parseInt(pspPlatformStr));
                    if (pspPlatform != null) {
                        pspPlatformSet.add(pspPlatform);
                    }
                }
                platformMap.put(String.valueOf(GamePlatformType.PSP.getCode()), pspPlatformSet);
                gameDB.setPlatformMap(platformMap);

            }

            if (!StringUtil.isEmpty(tvPlatformStr)) {
                isNotM++;
                Set<GamePlatform> tvPlatformSet = new HashSet<GamePlatform>();
                if (tvPlatformStr.indexOf(",") > 0) {
                    String[] tvPlatformArr = tvPlatformStr.split(",");
                    for (String platform : tvPlatformArr) {
                        TVPlatform tvPlatform = TVPlatform.getByCode(Integer.parseInt(platform));
                        if (tvPlatform != null) {
                            tvPlatformSet.add(tvPlatform);
                        }
                    }
                } else {
                    TVPlatform tvPlatform = TVPlatform.getByCode(Integer.parseInt(tvPlatformStr));
                    if (tvPlatform != null) {
                        tvPlatformSet.add(tvPlatform);
                    }
                }
                platformMap.put(String.valueOf(GamePlatformType.TV.getCode()), tvPlatformSet);
            }
            System.out.println("=============m gameid:" + gameDB.getGameDbId());
            long tempGameDbId = 0L;
            if (isNotM > 0) {
                if (gameDB.getGameDbId() > 0) {
                    gameDB.setAssociatedGameId(gameDB.getGameDbId());//如果同时属于手机类游戏则关联手机游戏id
                    gameDB.setGameDbId(0L);
                }
                if (null != platformMap.get(String.valueOf(GamePlatformType.MOBILE.getCode()))) {
                    platformMap.remove(String.valueOf(GamePlatformType.MOBILE.getCode()));
                }
                gameDB.setPlatformMap(platformMap);


                //创建游戏时，如果不是手机游戏，会清空用户填的以下字段：ios下载地址、android下载地址
                gameDB.setIosDownload("");
                gameDB.setAndroidDownload("");


                gameDB = GameResourceServiceSngl.get().createGameDb(gameDB);
                tempGameDbId = gameDB.getGameDbId();

                createGameDBRelationAndLog(gameDB, request);
                System.out.println("=============pc gameid:" + gameDB.getGameDbId());
                System.out.println("=============pc assgameid:" + gameDB.getAssociatedGameId());


                //TODO
                GameUtil.updateGameTags(gameDB, MicroAuthUtil.getToken());

                if (isM > 0) {
                    if (tempGameDbId > 0) {
                        //gameDB.setAssociatedGameId(tempGameDbId);
                        BasicDBObject queryDBObject = new BasicDBObject();
                        BasicDBObject updateDBObject = new BasicDBObject();
                        queryDBObject.put(GameDBField.ID.getColumn(), gameDB.getAssociatedGameId());
                        updateDBObject.put(GameDBField.ASSOCIATED_GAME_ID.getColumn(), tempGameDbId);
                        boolean isUpdate = GameResourceServiceSngl.get().updateGameDB(queryDBObject, updateDBObject);
                        if (!isUpdate) {
                            GAlerter.lab(this.getClass().getName() + " occur update failed:");
                        }
                    }
                }
            }

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e:", e);
        }
        return new ModelAndView("redirect:/gamedb/list");
    }

    /**
     * 创建游戏详情的tab
     */
    private boolean createGameDBRelationAndLog(GameDB gameDB, HttpServletRequest request) {
        try {
            if (!CollectionUtil.isEmpty(gameDB.getPlatformMap()) && gameDB.getGameNetType() != null && !CollectionUtil.isEmpty(gameDB.getLanguageTypeSet()) && !CollectionUtil.isEmpty(gameDB.getCategoryTypeSet()) && !CollectionUtil.isEmpty(gameDB.getThemeTypeSet())) {
                String idStr = String.valueOf(gameDB.getGameDbId());
                Set<String> platformParamSet = new HashSet<String>();
                for (String key : gameDB.getPlatformMap().keySet()) {
                    for (GamePlatform gamePlatform : gameDB.getPlatformMap().get(key)) {
                        String platformParam = idStr + "_" + key + "-" + gamePlatform.getCode();
                        platformParamSet.add(platformParam);
                    }
                }
                Set<String> netParamSet = new HashSet<String>();
                for (String platformParam : platformParamSet) {
                    String netParam = platformParam + "_" + gameDB.getGameNetType().getCode();
                    netParamSet.add(netParam);
                }
                Set<String> languageParamSet = new HashSet<String>();
                for (String netParam : netParamSet) {
                    for (GameLanguageType languageType : gameDB.getLanguageTypeSet()) {
                        String languageParam = netParam + "_" + languageType.getCode();
                        languageParamSet.add(languageParam);
                    }
                }
                Set<String> categoryParamSet = new HashSet<String>();
                for (String languageParam : languageParamSet) {
                    for (GameCategoryType categoryType : gameDB.getCategoryTypeSet()) {
                        String categoryParam = languageParam + "_" + categoryType.getCode();
                        categoryParamSet.add(categoryParam);
                    }
                }
                Set<String> themeParamSet = new HashSet<String>();
                for (String categoryParam : categoryParamSet) {
                    for (GameThemeType themeType : gameDB.getThemeTypeSet()) {
                        String themeParam = categoryParam + "_" + themeType.getCode();
                        themeParamSet.add(themeParam);
                    }
                }
                for (String param : themeParamSet) {
                    redisManager.lpush(WEBAPPWWW_GAME_COLLECTION_PARAM_SET, param);
                }
            }

            //创建游戏详情的tab
            GameDBRelation relation = new GameDBRelation();
            relation.setModifyUserid(getCurrentUser().getUserid());
            relation.setModifyIp(getCurrentUser().getAccessip());
            relation.setType(GameDBRelationType.DETAIL);
            relation.setGamedbId(gameDB.getGameDbId());
            relation.setDisplayOrder((int) (Integer.MAX_VALUE - System.currentTimeMillis() / 1000));
            relation.setTitle("详情");
            relation.setUri(String.valueOf(gameDB.getGameDbId()));
            GameResourceServiceSngl.get().createGameDbRelation(relation);

            //加入日志
            ToolsLog log = new ToolsLog();
            log.setOpUserId(getCurrentUser().getUserid());
            log.setOperType(LogOperType.GAMEDB_ADD);
            log.setOpTime(new Date());
            log.setOpIp(getIp());
            Map<String, String[]> params = request.getParameterMap();
            String queryString = "  ";
            for (String key : params.keySet()) {
                String[] values = params.get(key);
                for (int i = 0; i < values.length; i++) {
                    queryString += key + "=" + values[i] + ",";
                }
            }
            // 去掉最后一个空格
            queryString = queryString.substring(0, queryString.length() - 1);

            if (queryString.length() > 1900) {
                queryString = queryString.substring(0, 1900);
            }
            log.setOpAfter("游戏资料库添加->queryString" + queryString);
            addLog(log);
            return true;
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e:", e);
            return false;
        }

    }

    @RequestMapping(value = "/updatepage")
    public ModelAndView updatePage(@RequestParam(value = "gamedbid", required = false) Long gameDbId,
                                   @RequestParam(value = "errorMsg", required = false) String errorMsg,
                                   @RequestParam(value = "validstatus", required = false) String validstatus,
                                   @RequestParam(value = "searchname", required = false) String searchname,
                                   @RequestParam(value = "pageStartIndex", required = false) Integer pageStartIndex) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("errorMsg", errorMsg);
        mapMessage.put("searchname", searchname);
        mapMessage.put("validstatus", validstatus);
        mapMessage.put("pageStartIndex", pageStartIndex);
        try {
            mapMessage.put("mPlatforms", MobilePlatform.getAll());
            mapMessage.put("pcPlatforms", PCPlatform.getAll());
            mapMessage.put("pspPlatforms", PSPPlatform.getAll());
            mapMessage.put("tvPlatforms", TVPlatform.getAll());
            mapMessage.put("categoryTypes", GameCategoryType.getAll());
            mapMessage.put("netTypes", GameNetType.getAll());
            mapMessage.put("languageTypes", GameLanguageType.getAll());
            mapMessage.put("themeTypes", GameThemeType.getAll());
            mapMessage.put("picTypes", picTypeList);

            List<GameDBChannel> list = GameResourceServiceSngl.get().queryGameDbChannel();
            mapMessage.put("channelList", list);

            GameDB gameDB = GameResourceServiceSngl.get().getGameDB(new BasicDBObject().append(GameDBField.ID.getColumn(), gameDbId), false);
            if (gameDB == null) {
                return new ModelAndView("redirect:/gamedb/list?validstatus=" + validstatus + "&searchname=" + searchname + "&pager.offset=" + pageStartIndex);
            }
            mapMessage.put("gameDb", gameDB);

            if (!StringUtil.isEmpty(gameDB.getGamePCConfigurationInfo())) {
                String info = gameDB.getGamePCConfigurationInfo();
                JSONObject jsonObject = JSONObject.parseObject(info);
                if (!CollectionUtil.isEmpty(jsonObject)) {
                    if (null != jsonObject.get("1")) {
                        mapMessage.put("pcConfigurationInfo1", jsonObject.get("1"));
                    }
                    if (null != jsonObject.get("2")) {
                        mapMessage.put("pcConfigurationInfo2", jsonObject.get("2"));
                    }
                }

            }

            GamePlatformType gamePlatformType = null;
            for (GamePlatformType platformType : gameDB.getPlatformTypeSet()) {
                if (platformType.equals(GamePlatformType.MOBILE)) {
                    gamePlatformType = GamePlatformType.MOBILE;
                    break;
                } else {
                    gamePlatformType = platformType;
                }
            }
            mapMessage.put("gamePlatformType", gamePlatformType == null ? 0 : gamePlatformType.getCode());

            List<String> gamePicList = new ArrayList<String>();
            if (!StringUtil.isEmpty(gameDB.getGamePic())) {
                if (gameDB.getGamePic().indexOf(",") > 0) {
                    String[] picArr = gameDB.getGamePic().split(",");
                    for (String pic : picArr) {
                        if (!StringUtil.isEmpty(pic)) {
                            gamePicList.add(URLUtils.getJoymeDnUrl(pic));
                        }
                    }
                } else {
                    gamePicList.add(URLUtils.getJoymeDnUrl(gameDB.getGamePic()));
                }
            }
            mapMessage.put("gamePicList", gamePicList);

            mapMessage.put("gameTagList", GameUtil.getGameTags(gameDB.getGameTag(), MicroAuthUtil.getToken()));

            List<String> commentGamePicList = new ArrayList<String>();
            if (!StringUtil.isEmpty(gameDB.getCommentGamePic())) {
                commentGamePicList = Arrays.asList(gameDB.getCommentGamePic().split(","));
            }
            mapMessage.put("commentGamePicList", commentGamePicList);

            if (!CollectionUtil.isEmpty(gamePicList) && (gamePicList.get(0).startsWith("http://r001") || gamePicList.get(0).startsWith("http://r002"))) {
                mapMessage.put("picMsg", "不是七牛的图片，需要重新上传，才能旋转");
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMsg", "system.error");
        }
        return new ModelAndView("/gameresource/gamedb/modifypage", mapMessage);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ModelAndView update(HttpServletRequest request, HttpServletResponse response,
                               @RequestParam(value = "gamedbid", required = false) Long gameId,
                               @RequestParam(value = "validstatus", required = false) String validstatus,
                               @RequestParam(value = "searchname", required = false) String searchname,
                               @RequestParam(value = "pageStartIndex", required = false) Integer pageStartIndex,

                               @RequestParam(value = "gameicon", required = false) String gameIcon,
                               @RequestParam(value = "gamename", required = false) String gameName,
                               @RequestParam(value = "anothername", required = false) String anotherName,
                               @RequestParam(value = "gamesize", required = false) String gameSize,
                               @RequestParam(value = "gameplatformtype", required = false, defaultValue = "0") Integer gamePlatformType,
                               @RequestParam(value = "mplatform", required = false) String mPlatformStr,
                               @RequestParam(value = "pcplatform", required = false) String pcPlatformStr,
                               @RequestParam(value = "pspplatform", required = false) String pspPlatformStr,
                               @RequestParam(value = "tvplatform", required = false) String tvPlatformStr,
                               @RequestParam(value = "iosdownload", required = false) String iosDownload,
                               @RequestParam(value = "androiddownload", required = false) String androidDownload,
                               @RequestParam(value = "wpdownload", required = false) String wpDownload,
                               @RequestParam(value = "publictime", required = false) String gamePublicTime,
                               @RequestParam(value = "publichtips", required = false, defaultValue = "false") Boolean publicTips,
                               @RequestParam(value = "officialwebsite", required = false) String officialWebsite,
                               @RequestParam(value = "nettype", required = false) String netType,
                               @RequestParam(value = "languagetype", required = false) String languageTypeStr,
                               @RequestParam(value = "category", required = false) String categoryStr,
                               @RequestParam(value = "themetype", required = false) String themeTypeStr,
                               @RequestParam(value = "developer", required = false) String developer,
                               @RequestParam(value = "publishers", required = false) String publishers,
                               @RequestParam(value = "gameprofile", required = false) String gameProfile,
                               @RequestParam(value = "gamevideo", required = false) String gameVideo,
                               @RequestParam(value = "gamepics", required = false) String gamePic,
                               @RequestParam(value = "pictype", required = false, defaultValue = "0") Integer picType,
                               @RequestParam(value = "wikiurl", required = false) String wikiUrl,
                               @RequestParam(value = "cmsurl", required = false) String cmsUrl,
                               @RequestParam(value = "gamerate", required = false, defaultValue = "8.0") Double gameRate,
                               @RequestParam(value = "popular", required = false, defaultValue = "0") Integer popular,
                               //商务
                               @RequestParam(value = "teamname", required = false) String teamName,
                               @RequestParam(value = "teamnum", required = false, defaultValue = "0") Integer teamNum,
                               @RequestParam(value = "city", required = false) String city,
                               @RequestParam(value = "financing", required = false, defaultValue = "0") Integer financing,
                               @RequestParam(value = "contacts", required = false) String contacts,
                               @RequestParam(value = "email", required = false) String email,
                               @RequestParam(value = "phone", required = false) String phone,
                               @RequestParam(value = "qq", required = false) String qq,
                               @RequestParam(value = "area", required = false, defaultValue = "0") Integer area,
                               @RequestParam(value = "displayicon", required = false) Integer displayIcon,
                               //玩霸
                               @RequestParam(value = "reason", required = false) String reason,
                               @RequestParam(value = "reason2", required = false) String reason2,
                               @RequestParam(value = "versionprofile", required = false) String versionProfile,
                               @RequestParam(value = "downloadRecommend", required = false) String downloadRecommend,

                               @RequestParam(value = "coverPicUrl", required = false) String coverPicUrl,
                               @RequestParam(value = "coverTitle", required = false) String coverTitle,
                               @RequestParam(value = "coverComment", required = false) String coverComment,
                               @RequestParam(value = "coverDesc", required = false) String coverDesc,
                               @RequestParam(value = "coverAgreeNum", required = false, defaultValue = "") String coverAgreeNum,
                               @RequestParam(value = "coverDownload", required = false) String coverDownload,
                               @RequestParam(value = "coverDownloadAndroid", required = false) String coverDownloadAndroid,
                               @RequestParam(value = "key1", required = false) String key1,
                               @RequestParam(value = "value1", required = false) String value1,
                               @RequestParam(value = "key2", required = false) String key2,
                               @RequestParam(value = "value2", required = false) String value2,
                               @RequestParam(value = "key3", required = false) String key3,
                               @RequestParam(value = "value3", required = false) String value3,
                               @RequestParam(value = "key4", required = false) String key4,
                               @RequestParam(value = "value4", required = false) String value4,
                               @RequestParam(value = "key5", required = false) String key5,
                               @RequestParam(value = "value5", required = false) String value5,
                               @RequestParam(value = "posterShowTypeIos", required = false) String posterShowTypeIos,
                               @RequestParam(value = "posterShowContentIos", required = false) String posterShowContentIos,
                               @RequestParam(value = "posterGamePublicTimeIos", required = false) String posterGamePublicTimeIos,
                               @RequestParam(value = "posterShowTypeAndroid", required = false) String posterShowTypeAndroid,
                               @RequestParam(value = "posterShowContentAndroid", required = false) String posterShowContentAndroid,
                               @RequestParam(value = "posterGamePublicTimeAndroid", required = false) String posterGamePublicTimeAndroid,
                               //着迷网首页 “大家对游戏的看法”
                               @RequestParam(value = "comment1", required = false) String comment1,
                               @RequestParam(value = "comment2", required = false) String comment2,
                               @RequestParam(value = "comment3", required = false) String comment3,
                               @RequestParam(value = "agree1", required = false) String agree1,
                               @RequestParam(value = "agree2", required = false) String agree2,
                               @RequestParam(value = "agree3", required = false) String agree3,
                               //渠道下载信息
                               @RequestParam(value = "channeldownloadinfo", required = false) String channelDownloadInfoStr,
                               @RequestParam(value = "gamedesc", required = false) String gameDesc,
                               @RequestParam(value = "pcconfigurationinfo1", required = false) String pcConfigurationinfo1,
                               @RequestParam(value = "pcconfigurationinfo2", required = false) String pcConfigurationinfo2,
                               @RequestParam(value = "pc_download", required = false) String pc_download,
                               @RequestParam(value = "webpageDownload", required = false) String webpageDownload,
                               @RequestParam(value = "levelGame", required = false, defaultValue = "false") Boolean levelGame,
                               @RequestParam(value = "xboxoneDownload", required = false, defaultValue = "") String xboxoneDownload,
                               @RequestParam(value = "ps4Download", required = false, defaultValue = "") String ps4Download,


                               @RequestParam(value = "isbn", required = false, defaultValue = "") String isbn,
                               @RequestParam(value = "englishName", required = false, defaultValue = "") String englishName,
                               @RequestParam(value = "backpic", required = false, defaultValue = "") String backpic,
                               @RequestParam(value = "appstorePrice", required = false, defaultValue = "") String appstorePrice,
                               @RequestParam(value = "video", required = false, defaultValue = "") String video,
                               @RequestParam(value = "vpn", required = false, defaultValue = "false") Boolean vpn,
                               @RequestParam(value = "commentGamePic", required = false, defaultValue = "") String commentGamePic,
                               @RequestParam(value = "gameTag", required = false, defaultValue = "") String gameTag
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            BasicDBObject queryDBObject = new BasicDBObject();
            queryDBObject.put(GameDBField.ID.getColumn(), gameId);
            GameDB oldGameDB = GameResourceServiceSngl.get().getGameDB(queryDBObject, false);
            if (oldGameDB == null) {
                return new ModelAndView("redirect:/gamedb/list?validstatus=" + validstatus + "&searchname=" + searchname + "&pager.offset=" + pageStartIndex);
            }

            GameDB newGameDB = new GameDB();
            newGameDB.setGameIcon(gameIcon);
            newGameDB.setGameDesc(gameDesc);
            newGameDB.setGameName(gameName);
            newGameDB.setAnotherName(anotherName);
            newGameDB.setGameSize(gameSize);


            newGameDB.setIsbn(isbn);
            newGameDB.setEnglishName(englishName);
            newGameDB.setBackpic(backpic);
            newGameDB.setAppstorePrice(appstorePrice);
            newGameDB.setVideo(video);
            newGameDB.setVpn(vpn);
            newGameDB.setCommentGamePic(commentGamePic);


            newGameDB.setGameTag(gameTag);

            Map<String, Set<GamePlatform>> platformMap = new HashMap<String, Set<GamePlatform>>();
            if (!StringUtil.isEmpty(mPlatformStr)) {
                Set<GamePlatform> mobilePlatformSet = new HashSet<GamePlatform>();
                if (mPlatformStr.indexOf(",") > 0) {
                    String[] mPlatformArr = mPlatformStr.split(",");
                    for (String platform : mPlatformArr) {
                        MobilePlatform mobilePlatform = MobilePlatform.getByCode(Integer.parseInt(platform));
                        if (mobilePlatform != null) {
                            mobilePlatformSet.add(mobilePlatform);
                        }
                    }
                } else {
                    MobilePlatform mobilePlatform = MobilePlatform.getByCode(Integer.parseInt(mPlatformStr));
                    if (mobilePlatform != null) {
                        mobilePlatformSet.add(mobilePlatform);
                    }
                }
                platformMap.put(String.valueOf(GamePlatformType.MOBILE.getCode()), mobilePlatformSet);
            }

            if (!StringUtil.isEmpty(pcPlatformStr)) {
                Set<GamePlatform> pcPlatformSet = new HashSet<GamePlatform>();
                if (pcPlatformStr.indexOf(",") > 0) {
                    String[] pcPlatformArr = pcPlatformStr.split(",");
                    for (String platform : pcPlatformArr) {
                        PCPlatform pcPlatform = PCPlatform.getByCode(Integer.parseInt(platform));
                        if (pcPlatform != null) {
                            pcPlatformSet.add(pcPlatform);
                        }
                    }
                } else {
                    PCPlatform pcPlatform = PCPlatform.getByCode(Integer.parseInt(pcPlatformStr));
                    if (pcPlatform != null) {
                        pcPlatformSet.add(pcPlatform);
                    }
                }
                platformMap.put(String.valueOf(GamePlatformType.PC.getCode()), pcPlatformSet);
            }

            if (!StringUtil.isEmpty(pspPlatformStr)) {
                Set<GamePlatform> pspPlatformSet = new HashSet<GamePlatform>();
                if (pspPlatformStr.indexOf(",") > 0) {
                    String[] pspPlatformArr = pspPlatformStr.split(",");
                    for (String platform : pspPlatformArr) {
                        PSPPlatform pspPlatform = PSPPlatform.getByCode(Integer.parseInt(platform));
                        if (pspPlatform != null) {
                            pspPlatformSet.add(pspPlatform);
                        }
                    }
                } else {
                    PSPPlatform pspPlatform = PSPPlatform.getByCode(Integer.parseInt(pspPlatformStr));
                    if (pspPlatform != null) {
                        pspPlatformSet.add(pspPlatform);
                    }
                }
                platformMap.put(String.valueOf(GamePlatformType.PSP.getCode()), pspPlatformSet);
            }

            if (!StringUtil.isEmpty(tvPlatformStr)) {
                Set<GamePlatform> tvPlatformSet = new HashSet<GamePlatform>();
                if (tvPlatformStr.indexOf(",") > 0) {
                    String[] tvPlatformArr = tvPlatformStr.split(",");
                    for (String platform : tvPlatformArr) {
                        TVPlatform tvPlatform = TVPlatform.getByCode(Integer.parseInt(platform));
                        if (tvPlatform != null) {
                            tvPlatformSet.add(tvPlatform);
                        }
                    }
                } else {
                    TVPlatform tvPlatform = TVPlatform.getByCode(Integer.parseInt(tvPlatformStr));
                    if (tvPlatform != null) {
                        tvPlatformSet.add(tvPlatform);
                    }
                }
                platformMap.put(String.valueOf(GamePlatformType.TV.getCode()), tvPlatformSet);
            }
            newGameDB.setPlatformMap(platformMap);

            newGameDB.setIosDownload(iosDownload);
            newGameDB.setAndroidDownload(androidDownload);
            newGameDB.setWpDownload(wpDownload);
            if (!StringUtil.isEmpty(gamePublicTime)) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                newGameDB.setGamePublicTime(df.parse(gamePublicTime));
            }
            newGameDB.setPublicTips(publicTips);
            newGameDB.setOfficialWebsite(officialWebsite);
            if (!StringUtil.isEmpty(netType)) {
                newGameDB.setGameNetType(GameNetType.getByCode(Integer.valueOf(netType)));
            }
            Set<GameLanguageType> languageTypeSet = new HashSet<GameLanguageType>();
            if (!StringUtil.isEmpty(languageTypeStr)) {
                if (languageTypeStr.indexOf(",") > 0) {
                    String[] languageTypeArr = languageTypeStr.split(",");
                    for (String language : languageTypeArr) {
                        GameLanguageType languageType = GameLanguageType.getByCode(Integer.parseInt(language));
                        if (languageType != null) {
                            languageTypeSet.add(languageType);
                        }
                    }
                } else {
                    GameLanguageType languageType = GameLanguageType.getByCode(Integer.parseInt(languageTypeStr));
                    if (languageType != null) {
                        languageTypeSet.add(languageType);
                    }
                }
            }
            newGameDB.setLanguageTypeSet(languageTypeSet);
            Set<GameCategoryType> categoryTypeSet = new HashSet<GameCategoryType>();
            if (!StringUtil.isEmpty(categoryStr)) {
                if (categoryStr.indexOf(",") > 0) {
                    String[] categoryArr = categoryStr.split(",");
                    for (String category : categoryArr) {
                        GameCategoryType categoryType = GameCategoryType.getByCode(Integer.parseInt(category));
                        if (categoryType != null) {
                            categoryTypeSet.add(categoryType);
                        }
                    }
                } else {
                    GameCategoryType categoryType = GameCategoryType.getByCode(Integer.parseInt(categoryStr));
                    if (categoryType != null) {
                        categoryTypeSet.add(categoryType);
                    }
                }
            }
            newGameDB.setCategoryTypeSet(categoryTypeSet);
            Set<GameThemeType> themeTypeSet = new HashSet<GameThemeType>();
            if (!StringUtil.isEmpty(themeTypeStr)) {
                if (themeTypeStr.indexOf(",") > 0) {
                    String[] themeArr = themeTypeStr.split(",");
                    for (String theme : themeArr) {
                        GameThemeType themeType = GameThemeType.getByCode(Integer.parseInt(theme));
                        if (themeType != null) {
                            themeTypeSet.add(themeType);
                        }
                    }
                } else {
                    GameThemeType themeType = GameThemeType.getByCode(Integer.parseInt(themeTypeStr));
                    if (themeType != null) {
                        themeTypeSet.add(themeType);
                    }
                }
            }
            newGameDB.setThemeTypeSet(themeTypeSet);
            newGameDB.setGameDeveloper(developer);
            newGameDB.setGamePublishers(publishers);
            newGameDB.setGameProfile(gameProfile);
            newGameDB.setGameVideo(gameVideo);

            //pc游戏配置需求
            JSONObject pcConfiguration = new JSONObject();
            pcConfiguration.put("1", pcConfigurationinfo1);
            pcConfiguration.put("2", pcConfigurationinfo2);
            newGameDB.setGamePCConfigurationInfoSet(pcConfiguration.toJSONString());

            //
            newGameDB.setPcDownload(StringUtil.isEmpty(pc_download) ? "" : pc_download);
            newGameDB.setWebpageDownload(StringUtil.isEmpty(webpageDownload) ? "" : webpageDownload);
            newGameDB.setLevelGame(levelGame);
            newGameDB.setXboxoneDownload(xboxoneDownload);
            newGameDB.setPs4Download(ps4Download);


            if (!StringUtil.isEmpty(gameVideo)) {
                String archiveId = ArchiveUtil.getArchiveId(gameVideo);
                if (!StringUtil.isEmpty(archiveId)) {
                    Archive archive = JoymeAppServiceSngl.get().getArchiveById(Integer.valueOf(archiveId));
                    if (archive != null) {
                        newGameDB.setGameVideoPic(archive.getIcon());
                    }
                }
            }
            newGameDB.setGamePic(gamePic);
            newGameDB.setGamePicType(GamePicType.getByCode(picType));
            newGameDB.setWikiUrl(wikiUrl);
            newGameDB.setCmsUrl(cmsUrl);
            newGameDB.setGameRate(gameRate == null ? 8.0 : gameRate);
            newGameDB.setPopular(popular);
            newGameDB.setModifyDate(new Date());
            newGameDB.setModifyUser(CookieUtil.getCookieValue(request, CacheUtil.TOOLS_COOKIEKEY_UID));
            newGameDB.setCreateDate(oldGameDB.getCreateDate() == null ? new Date() : oldGameDB.getCreateDate());
            newGameDB.setCreateUser(CookieUtil.getCookieValue(request, CacheUtil.TOOLS_COOKIEKEY_UID));
//            newGameDB.setValidStatus(GameDbStatus.INVALID);
            /**
             * 玩霸
             */
            //摇一摇
            newGameDB.setRecommendReason(reason);
            newGameDB.setRecommendReason2(reason2);
            newGameDB.setVersionProfile(versionProfile);
            newGameDB.setDownloadRecommend(downloadRecommend);
            //游戏封面
            GameDBCover cover = new GameDBCover();
            cover.setCoverPicUrl(coverPicUrl);
            cover.setCoverTitle(coverTitle);
            cover.setCoverComment(coverComment);
            cover.setCoverDesc(coverDesc);
            cover.setCoverAgreeNum(coverAgreeNum.trim());
            cover.setCoverDownload(coverDownload);//这是ios的开关
            cover.setCoverDownloadAndroid(coverDownloadAndroid);//这是android的开关
            GameDBCoverFieldJson coverJson = new GameDBCoverFieldJson();
            coverJson.setKey1(key1);
            coverJson.setValue1(value1);
            coverJson.setKey2(key2);
            coverJson.setValue2(value2);
            coverJson.setKey3(key3);
            coverJson.setValue3(value3);
            coverJson.setKey4(key4);
            coverJson.setValue4(value4);
            coverJson.setKey5(key5);
            coverJson.setValue5(value5);
            cover.setPosterShowTypeIos(posterShowTypeIos);
            cover.setPosterShowContentIos(posterShowContentIos);
            if (!StringUtil.isEmpty(posterGamePublicTimeIos)) {
                if (Pattern.compile("[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}\\s+[0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}").matcher(posterGamePublicTimeIos).matches()) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        long posterGamePublicTimeLongIos = sdf.parse(posterGamePublicTimeIos).getTime();
                        cover.setPosterGamePublicTimeIos(String.valueOf(posterGamePublicTimeLongIos)); //数据库中存放long值
                    } catch (ParseException p) {
                        p.printStackTrace();
                    }
                }
            }
            cover.setPosterShowTypeAndroid(posterShowTypeAndroid);
            cover.setPosterShowContentAndroid(posterShowContentAndroid);
            if (!StringUtil.isEmpty(posterGamePublicTimeAndroid)) {
                if (Pattern.compile("[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}\\s+[0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}").matcher(posterGamePublicTimeAndroid).matches()) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        long posterGamePublicTimeLongAndroid = sdf.parse(posterGamePublicTimeAndroid).getTime();
                        cover.setPosterGamePublicTimeAndroid(String.valueOf(posterGamePublicTimeLongAndroid)); //数据库中存放long值
                    } catch (ParseException p) {
                        p.printStackTrace();
                    }
                }
            }
            newGameDB.setGameDBCoverFieldJson(coverJson);
            newGameDB.setGameDBCover(cover);
            /**
             * 商务
             */
            newGameDB.setTeamName(teamName);
            newGameDB.setTeamNum(teamNum);
            newGameDB.setCity(city);
            newGameDB.setFinancing(financing);
            newGameDB.setContacts(contacts);
            newGameDB.setEmail(email);
            newGameDB.setPhone(phone);
            newGameDB.setQq(qq);
            newGameDB.setArea(area);
            if (displayIcon != null) {
                newGameDB.setDisplayIcon(displayIcon);
            }
            /**
             * 渠道下载
             */
            if (!StringUtil.isEmpty(channelDownloadInfoStr) && channelDownloadInfoStr.startsWith("[")) {
                JSONArray jsonArray = JSONArray.fromObject(channelDownloadInfoStr);
                if (jsonArray != null && !jsonArray.isEmpty()) {
                    Set<GameDBChannelInfo> channelInfoSet = new HashSet<GameDBChannelInfo>();
                    for (Object object : jsonArray) {
                        GameDBChannelInfo gameDBChannelInfo = GameDBChannelInfo.parse(object);
                        if (gameDBChannelInfo != null) {
                            channelInfoSet.add(gameDBChannelInfo);
                        }
                    }
                    newGameDB.setChannelInfoSet(channelInfoSet);
                }
            }
            /**
             * 着迷网首页
             */
            CommentAndAgree commentAndAgree = new CommentAndAgree();
            commentAndAgree.setComment1(StringUtil.isEmpty(comment1) ? "" : comment1);
            commentAndAgree.setComment2(StringUtil.isEmpty(comment2) ? "" : comment2);
            commentAndAgree.setComment3(StringUtil.isEmpty(comment3) ? "" : comment3);
            commentAndAgree.setAgree1(StringUtil.isEmpty(agree1) ? "0" : agree1);
            commentAndAgree.setAgree2(StringUtil.isEmpty(agree2) ? "0" : agree2);
            commentAndAgree.setAgree3(StringUtil.isEmpty(agree3) ? "0" : agree3);
            newGameDB.setCommentAndAgree(commentAndAgree);

            BasicDBObject query = new BasicDBObject();
            query.put(GameDBField.ID.getColumn(), new BasicDBObject("$ne", gameId));
            query.put(GameDBField.GAMENAME.getColumn(), gameName);
            GameDB existGame = GameResourceServiceSngl.get().getGameDB(query, true);
            if (existGame != null && oldGameDB.getAssociatedGameId() <= 0L) {
                mapMessage.put("searchname", searchname);
                mapMessage.put("validstatus", validstatus);
                mapMessage.put("pageStartIndex", pageStartIndex);
                mapMessage.put("errorMsg", "game.gamename.exists");

                mapMessage.put("mPlatforms", MobilePlatform.getAll());
                mapMessage.put("pcPlatforms", PCPlatform.getAll());
                mapMessage.put("pspPlatforms", PSPPlatform.getAll());
                mapMessage.put("tvPlatforms", TVPlatform.getAll());
                mapMessage.put("categoryTypes", GameCategoryType.getAll());
                mapMessage.put("netTypes", GameNetType.getAll());
                mapMessage.put("languageTypes", GameLanguageType.getAll());
                mapMessage.put("themeTypes", GameThemeType.getAll());
                mapMessage.put("picTypes", picTypeList);

                mapMessage.put("gameDb", newGameDB);
                mapMessage.put("gamePlatformType", gamePlatformType);

                List<String> gamePicList = new ArrayList<String>();
                if (gamePic.indexOf(",") > 0) {
                    String[] picArr = gamePic.split(",");
                    for (String pic : picArr) {
                        if (!StringUtil.isEmpty(pic)) {
                            gamePicList.add(URLUtils.getJoymeDnUrl(pic));
                        }
                    }
                } else {
                    if (!StringUtil.isEmpty(gamePic)) {
                        gamePicList.add(URLUtils.getJoymeDnUrl(gamePic));
                    }
                }
                mapMessage.put("gamePicList", gamePicList);

                List<String> commentGamePicList = new ArrayList<String>();
                if (!StringUtil.isEmpty(commentGamePic)) {
                    commentGamePicList = Arrays.asList(commentGamePic.split(","));
                }
                mapMessage.put("commentGamePicList", commentGamePicList);
                List<GameDBChannel> list = GameResourceServiceSngl.get().queryGameDbChannel();
                mapMessage.put("channelList", list);

                return new ModelAndView("/gameresource/gamedb/modifypage", mapMessage);
            }
            //如果在新游开测中存在相关gameid，则更改 相关开测时间及自定义时间字符串等 --start
            Long newGameLineId = JoymeAppServiceSngl.get().getClientLineByCode("gc_newgame_0").getLineId();
            QueryExpress queryExpressNewGame = new QueryExpress();
            queryExpressNewGame.add(QueryCriterions.eq(ClientLineItemField.DIRECT_ID, String.valueOf(gameId)));
            queryExpressNewGame.add(QueryCriterions.eq(ClientLineItemField.LINE_ID, newGameLineId));
            ClientLineItem clientLineItem = JoymeAppServiceSngl.get().getClientLineItem(queryExpressNewGame);
            if (clientLineItem != null) {
                UpdateExpress updateExpress = new UpdateExpress();
                if (posterShowTypeIos.equals("1")) {
                    updateExpress.set(ClientLineItemField.RATE, posterShowTypeIos);
                } else if (posterShowTypeIos.equals("2")) {
                    updateExpress.set(ClientLineItemField.RATE, posterShowTypeIos);
                    updateExpress.set(ClientLineItemField.AUTHOR, posterShowContentIos);
                }
                if (!StringUtil.isEmpty(cover.getPosterGamePublicTimeIos())) {
                    try {
                        Date itemCreateDate = new Date(Long.valueOf(cover.getPosterGamePublicTimeIos()));
                        updateExpress.set(ClientLineItemField.ITEM_CREATE_DATE, itemCreateDate);
                    } catch (Exception p) {
                        p.printStackTrace();
                    }
                }
                JoymeAppServiceSngl.get().modifyClientLineItem(updateExpress, clientLineItem.getItemId());
            }
            //如果在新游开测中存在相关gameid，则更改 相关开测时间及自定义时间字符串等--end
            if (!StringUtil.isEmpty(posterGamePublicTimeAndroid)) {
                if (Pattern.compile("[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}\\s+[0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}").matcher(posterGamePublicTimeAndroid).matches()) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        long posterGamePublicTimeLongAndroid = sdf.parse(posterGamePublicTimeAndroid).getTime();
                        cover.setPosterGamePublicTimeAndroid(String.valueOf(posterGamePublicTimeLongAndroid)); //数据库中存放long值
                    } catch (ParseException p) {
                        p.printStackTrace();
                    }
                }
            }
            //如果在新游开测中存在相关gameid，则更改 相关开测时间及自定义时间字符串等 --start
            newGameLineId = JoymeAppServiceSngl.get().getClientLineByCode("gc_newgame_1").getLineId();
            queryExpressNewGame = new QueryExpress();
            queryExpressNewGame.add(QueryCriterions.eq(ClientLineItemField.DIRECT_ID, String.valueOf(gameId)));
            queryExpressNewGame.add(QueryCriterions.eq(ClientLineItemField.LINE_ID, newGameLineId));

            clientLineItem = JoymeAppServiceSngl.get().getClientLineItem(queryExpressNewGame);

            if (clientLineItem != null) {
                UpdateExpress updateExpress = new UpdateExpress();
                if (posterShowTypeAndroid.equals("1")) {
                    updateExpress.set(ClientLineItemField.RATE, posterShowTypeAndroid);
                } else if (posterShowTypeIos.equals("2")) {
                    updateExpress.set(ClientLineItemField.RATE, posterShowTypeAndroid);
                    updateExpress.set(ClientLineItemField.AUTHOR, posterShowContentAndroid);
                }
                if (!StringUtil.isEmpty(cover.getPosterGamePublicTimeAndroid())) {
                    try {
                        Date itemCreateDate = new Date(Long.valueOf(cover.getPosterGamePublicTimeAndroid()));
                        updateExpress.set(ClientLineItemField.ITEM_CREATE_DATE, itemCreateDate);
                    } catch (Exception p) {
                        p.printStackTrace();
                    }
                }
                JoymeAppServiceSngl.get().modifyClientLineItem(updateExpress, clientLineItem.getItemId());
            }
            //如果在新游开测中存在相关gameid，则更改 相关开测时间及自定义时间字符串等--end
            BasicDBObject updateDBObject = new BasicDBObject();
            //基本
            updateDBObject.put(GameDBField.GAMEICON.getColumn(), newGameDB.getGameIcon());
            updateDBObject.put(GameDBField.GAMENAME.getColumn(), newGameDB.getGameName());
            updateDBObject.put(GameDBField.ANOTHERNAME.getColumn(), newGameDB.getAnotherName());
            updateDBObject.put(GameDBField.GAMESIZE.getColumn(), newGameDB.getGameSize());
            //已选中的去掉
            if (!CollectionUtil.isEmpty(oldGameDB.getPlatformMap())) {
                for (String key : oldGameDB.getPlatformMap().keySet()) {
                    updateDBObject.put(GameDBField.PLATFORMTYPE_.getColumn() + key, false);
                    for (GamePlatform oldPlatform : oldGameDB.getPlatformMap().get(key)) {
                        updateDBObject.put(GameDBField.GAME_PLATFORM_.getColumn() + key + "_" + oldPlatform.getCode(), false);
                    }
                }
            }
            if (!CollectionUtil.isEmpty(newGameDB.getPlatformMap())) {
                for (String key : newGameDB.getPlatformMap().keySet()) {
                    updateDBObject.put(GameDBField.PLATFORMTYPE_.getColumn() + key, true);
                    for (GamePlatform newPlatform : newGameDB.getPlatformMap().get(key)) {
                        updateDBObject.put(GameDBField.GAME_PLATFORM_.getColumn() + key + "_" + newPlatform.getCode(), true);
                    }
                }
            }
            updateDBObject.put(GameDBField.IOSDOWNLOAD.getColumn(), newGameDB.getIosDownload());
            updateDBObject.put(GameDBField.ANDROIDDOWNLOAD.getColumn(), newGameDB.getAndroidDownload());
            updateDBObject.put(GameDBField.WPDOWNLOAD.getColumn(), newGameDB.getWpDownload());
            updateDBObject.put(GameDBField.GAMEPUBLICTIME.getColumn(), newGameDB.getGamePublicTime());
            updateDBObject.put(GameDBField.PUBLICTIPS.getColumn(), newGameDB.getPublicTips());
            updateDBObject.put(GameDBField.OFFICIALWEBSITE.getColumn(), newGameDB.getOfficialWebsite());
            if (newGameDB.getGameNetType() != null) {
                updateDBObject.put(GameDBField.GAMENETTYPE.getColumn(), newGameDB.getGameNetType().getCode());
            }

            if (!CollectionUtil.isEmpty(oldGameDB.getLanguageTypeSet())) {
                for (GameLanguageType oldLanguageType : oldGameDB.getLanguageTypeSet()) {
                    updateDBObject.put(GameDBField.GAME_LANGUAGE_.getColumn() + oldLanguageType.getCode(), false);
                }
            }
            if (!CollectionUtil.isEmpty(newGameDB.getLanguageTypeSet())) {
                for (GameLanguageType languageType : newGameDB.getLanguageTypeSet()) {
                    updateDBObject.put(GameDBField.GAME_LANGUAGE_.getColumn() + languageType.getCode(), true);
                }
            }

            if (!CollectionUtil.isEmpty(oldGameDB.getCategoryTypeSet())) {
                for (GameCategoryType oldCategoryType : oldGameDB.getCategoryTypeSet()) {
                    updateDBObject.put(GameDBField.GAME_CATEGORY_.getColumn() + oldCategoryType.getCode(), false);
                }
            }
            if (!CollectionUtil.isEmpty(newGameDB.getCategoryTypeSet())) {
                for (GameCategoryType categoryType : newGameDB.getCategoryTypeSet()) {
                    updateDBObject.put(GameDBField.GAME_CATEGORY_.getColumn() + categoryType.getCode(), true);
                }
            }

            if (!CollectionUtil.isEmpty(oldGameDB.getThemeTypeSet())) {
                for (GameThemeType oldThemeType : oldGameDB.getThemeTypeSet()) {
                    updateDBObject.put(GameDBField.GAME_THEME_.getColumn() + oldThemeType.getCode(), false);
                }
            }
            if (!CollectionUtil.isEmpty(newGameDB.getThemeTypeSet())) {
                for (GameThemeType themeType : newGameDB.getThemeTypeSet()) {
                    updateDBObject.put(GameDBField.GAME_THEME_.getColumn() + themeType.getCode(), true);
                }
            }

            updateDBObject.put(GameDBField.GAME_DESC.getColumn(), newGameDB.getGameDesc());
            updateDBObject.put(GameDBField.PC_CONFIGURATION_INFO.getColumn(), newGameDB.getGamePCConfigurationInfo());


            updateDBObject.put(GameDBField.GAMEDEVELOPER.getColumn(), newGameDB.getGameDeveloper());
            updateDBObject.put(GameDBField.GAMEPUBLICSHERS.getColumn(), newGameDB.getGamePublishers());
            updateDBObject.put(GameDBField.GAMEPROFILE.getColumn(), newGameDB.getGameProfile());
            updateDBObject.put(GameDBField.GAMEVIDEO.getColumn(), newGameDB.getGameVideo());
            updateDBObject.put(GameDBField.GAMEVIDEOPIC.getColumn(), newGameDB.getGameVideoPic());
            updateDBObject.put(GameDBField.GAMEPIC.getColumn(), newGameDB.getGamePic());
            updateDBObject.put(GameDBField.GAME_PIC_TYPE.getColumn(), newGameDB.getGamePicType().getCode());
            updateDBObject.put(GameDBField.WIKIURL.getColumn(), newGameDB.getWikiUrl());
            updateDBObject.put(GameDBField.CMSURL.getColumn(), newGameDB.getCmsUrl());
            updateDBObject.put(GameDBField.GAMERATE.getColumn(), newGameDB.getGameRate());
            updateDBObject.put(GameDBField.FAVOR_SUM.getColumn(), oldGameDB.getFavorSum());
            updateDBObject.put(GameDBField.UN_FAVOR_SUM.getColumn(), oldGameDB.getUnFavorSum());
            updateDBObject.put(GameDBField.POPULAR.getColumn(), newGameDB.getPopular());
            //updateDBObject.put(GameDBField.VALIDSTATUS.getColumn(), newGameDB.getValidStatus().getCode());
            updateDBObject.put(GameDBField.CREATE_DATE.getColumn(), newGameDB.getCreateDate() == null ? new Date() : newGameDB.getCreateDate());
            updateDBObject.put(GameDBField.CREATE_USER.getColumn(), newGameDB.getCreateUser());
            updateDBObject.put(GameDBField.MODIFY_DATE.getColumn(), newGameDB.getModifyDate());
            updateDBObject.put(GameDBField.MODIFY_USER.getColumn(), newGameDB.getModifyUser());
            //商务
            updateDBObject.put(GameDBField.TEAMNAME.getColumn(), newGameDB.getTeamName());
            updateDBObject.put(GameDBField.TEAMNUM.getColumn(), newGameDB.getTeamNum());
            updateDBObject.put(GameDBField.CITY.getColumn(), newGameDB.getCity());
            updateDBObject.put(GameDBField.PUBLICTIME.getColumn(), newGameDB.getPublicTime());
            updateDBObject.put(GameDBField.FINANCING.getColumn(), newGameDB.getFinancing());
            updateDBObject.put(GameDBField.CONTACTS.getColumn(), newGameDB.getContacts());
            updateDBObject.put(GameDBField.EMAIL.getColumn(), newGameDB.getEmail());
            updateDBObject.put(GameDBField.PHONE.getColumn(), newGameDB.getPhone());
            updateDBObject.put(GameDBField.QQ.getColumn(), newGameDB.getQq());
            updateDBObject.put(GameDBField.AREA.getColumn(), newGameDB.getArea());
            //玩霸
            updateDBObject.put(GameDBField.VERSIONPROFILE.getColumn(), newGameDB.getVersionProfile());
            updateDBObject.put(GameDBField.REASON.getColumn(), newGameDB.getRecommendReason());
            updateDBObject.put(GameDBField.REASON2.getColumn(), newGameDB.getRecommendReason2());
            updateDBObject.put(GameDBField.DISPLAY_ICON.getColumn(), newGameDB.getDisplayIcon());
            updateDBObject.put(GameDBField.GAMEDB_COVER.getColumn(), newGameDB.getGameDBCover().toJson());
            updateDBObject.put(GameDBField.GAMEDB_COVER_FIELD_JSON.getColumn(), newGameDB.getGameDBCoverFieldJson().toJson());
            updateDBObject.put(GameDBField.DOWNLOADRECOMMEND.getColumn(), newGameDB.getDownloadRecommend());
            //主站首页
            updateDBObject.put(GameDBField.COMMENT_AND_AGREE.getColumn(), newGameDB.getCommentAndAgree().toJson());


            //渠道下载
            if (!CollectionUtil.isEmpty(oldGameDB.getChannelInfoSet())) {
                for (GameDBChannelInfo gameDBChannelInfo : oldGameDB.getChannelInfoSet()) {
                    updateDBObject.put(GameDBField.CHANNEL_PLATFORM_.getColumn() + gameDBChannelInfo.getChannel_id() + "_" + gameDBChannelInfo.getDevice(), false);
                    updateDBObject.put(GameDBField.CHANNEL_DOWNLOAD_INFO_.getColumn() + gameDBChannelInfo.getChannel_id() + "_" + gameDBChannelInfo.getDevice(), "");
                }
            }
            if (!CollectionUtil.isEmpty(newGameDB.getChannelInfoSet())) {
                for (GameDBChannelInfo gameDBChannelInfo : newGameDB.getChannelInfoSet()) {
                    updateDBObject.put(GameDBField.CHANNEL_PLATFORM_.getColumn() + gameDBChannelInfo.getChannel_id() + "_" + gameDBChannelInfo.getDevice(), true);
                    updateDBObject.put(GameDBField.CHANNEL_DOWNLOAD_INFO_.getColumn() + gameDBChannelInfo.getChannel_id() + "_" + gameDBChannelInfo.getDevice(), gameDBChannelInfo.toJson());
                }
            }
            updateDBObject.put(GameDBField.PC_DOWNLOAD.getColumn(), newGameDB.getPcDownload());
            updateDBObject.put(GameDBField.WEBPAGEDOWNLOAD.getColumn(), newGameDB.getWebpageDownload());
            updateDBObject.put(GameDBField.LEVELGAME.getColumn(), newGameDB.isLevelGame());
            updateDBObject.put(GameDBField.XBOXONEDOWNLOAD.getColumn(), newGameDB.getXboxoneDownload());
            updateDBObject.put(GameDBField.PS4DOWNLOAD.getColumn(), newGameDB.getPs4Download());


            updateDBObject.put(GameDBField.ISBN.getColumn(), newGameDB.getIsbn());
            updateDBObject.put(GameDBField.ENGLISHNAME.getColumn(), newGameDB.getEnglishName());
            updateDBObject.put(GameDBField.BACKPIC.getColumn(), newGameDB.getBackpic());
            updateDBObject.put(GameDBField.APPSTOREPRICE.getColumn(), newGameDB.getAppstorePrice());
            updateDBObject.put(GameDBField.VIDEO.getColumn(), newGameDB.getVideo());
            updateDBObject.put(GameDBField.VPN.getColumn(), newGameDB.isVpn());
            updateDBObject.put(GameDBField.COMMENTGAMEPIC.getColumn(), newGameDB.getCommentGamePic());
            updateDBObject.put(GameDBField.GAMETAG.getColumn(), newGameDB.getGameTag());

            GameResourceServiceSngl.get().updateGameDB(queryDBObject, updateDBObject);


            //sendphp
            GameDB gameDBPhp = GameResourceServiceSngl.get().getGameDB(new BasicDBObject().append(GameDBField.ID.getColumn(), gameId), false);
            if (gameDBPhp != null) {
                GamedbSendPhpChannel channel = new GamedbSendPhpChannel();
                channel.sendPhpGamedb(gameDBPhp);
            }


            //修改点评数据
            GameUtil.updateGame(gameDBPhp, MicroAuthUtil.getToken(), null, null);
            //TODO
            GameUtil.updateGameTags(gameDBPhp, MicroAuthUtil.getToken());

            //更新WIKI游戏列表里的信息
            WikiGameres wikiGameres = AskServiceSngl.get().getWikiGameresByQueryExpress(new QueryExpress().add(QueryCriterions.eq(WikiGameresField.GAMEID, gameId)));
            if (wikiGameres != null) {
                if (!wikiGameres.getGameName().equals(gameName)) {
                    boolean bool = AskServiceSngl.get().modifyWikiGameres(wikiGameres.getId(), new UpdateExpress().set(WikiGameresField.GAMENAME, gameName));
                    if (bool) {
                        WikiappSearchEvent wikiappSearchEvent = new WikiappSearchEvent();
                        WikiappSearchEntry wikiappSearchEntry = new WikiappSearchEntry();
                        wikiappSearchEntry.setCreatetime(new Date().getTime());
                        wikiappSearchEntry.setId(String.valueOf(wikiGameres.getGameId()));
                        wikiappSearchEntry.setTitle(wikiGameres.getGameName());
                        wikiappSearchEntry.setType(WikiappSearchType.GAME.getCode());
                        wikiappSearchEntry.setEntryid(AskUtil.getWikiappSearchEntryId(String.valueOf(wikiGameres.getGameId()), WikiappSearchType.GAME));
                        wikiappSearchEvent.setWikiappSearchEntry(wikiappSearchEntry);
                        EventDispatchServiceSngl.get().dispatch(wikiappSearchEvent);
                    }
                }
            }


            if (!CollectionUtil.isEmpty(oldGameDB.getPlatformMap()) && oldGameDB.getGameNetType() != null && !CollectionUtil.isEmpty(oldGameDB.getLanguageTypeSet()) && !CollectionUtil.isEmpty(oldGameDB.getCategoryTypeSet()) && !CollectionUtil.isEmpty(oldGameDB.getThemeTypeSet())) {
                String oldIdStr = String.valueOf(oldGameDB.getGameDbId());
                Set<String> oldPlatformParamSet = new HashSet<String>();
                for (String key : oldGameDB.getPlatformMap().keySet()) {
                    for (GamePlatform gamePlatform : oldGameDB.getPlatformMap().get(key)) {
                        String platformParam = oldIdStr + "_" + key + "-" + gamePlatform.getCode();
                        oldPlatformParamSet.add(platformParam);
                    }
                }
                Set<String> oldNetParamSet = new HashSet<String>();
                for (String platformParam : oldPlatformParamSet) {
                    String netParam = platformParam + "_" + oldGameDB.getGameNetType().getCode();
                    oldNetParamSet.add(netParam);
                }
                Set<String> oldLanguageParamSet = new HashSet<String>();
                for (String netParam : oldNetParamSet) {
                    for (GameLanguageType languageType : oldGameDB.getLanguageTypeSet()) {
                        String languageParam = netParam + "_" + languageType.getCode();
                        oldLanguageParamSet.add(languageParam);
                    }
                }
                Set<String> oldCategoryParamSet = new HashSet<String>();
                for (String languageParam : oldLanguageParamSet) {
                    for (GameCategoryType categoryType : oldGameDB.getCategoryTypeSet()) {
                        String categoryParam = languageParam + "_" + categoryType.getCode();
                        oldCategoryParamSet.add(categoryParam);
                    }
                }
                Set<String> oldThemeParamSet = new HashSet<String>();
                for (String categoryParam : oldCategoryParamSet) {
                    for (GameThemeType themeType : oldGameDB.getThemeTypeSet()) {
                        String themeParam = categoryParam + "_" + themeType.getCode();
                        oldThemeParamSet.add(themeParam);
                    }
                }
                for (String param : oldThemeParamSet) {
                    redisManager.lrem(WEBAPPWWW_GAME_COLLECTION_PARAM_SET, 0, param);
                }
            }

            if (!CollectionUtil.isEmpty(newGameDB.getPlatformMap()) && newGameDB.getGameNetType() != null && !CollectionUtil.isEmpty(newGameDB.getLanguageTypeSet()) && !CollectionUtil.isEmpty(newGameDB.getCategoryTypeSet()) && !CollectionUtil.isEmpty(newGameDB.getThemeTypeSet())) {
                String idStr = String.valueOf(newGameDB.getGameDbId());
                Set<String> platformParamSet = new HashSet<String>();
                for (String key : newGameDB.getPlatformMap().keySet()) {
                    for (GamePlatform gamePlatform : newGameDB.getPlatformMap().get(key)) {
                        String platformParam = idStr + "_" + key + "-" + gamePlatform.getCode();
                        platformParamSet.add(platformParam);
                    }
                }
                Set<String> netParamSet = new HashSet<String>();
                for (String platformParam : platformParamSet) {
                    String netParam = platformParam + "_" + newGameDB.getGameNetType().getCode();
                    netParamSet.add(netParam);
                }
                Set<String> languageParamSet = new HashSet<String>();
                for (String netParam : netParamSet) {
                    for (GameLanguageType languageType : newGameDB.getLanguageTypeSet()) {
                        String languageParam = netParam + "_" + languageType.getCode();
                        languageParamSet.add(languageParam);
                    }
                }
                Set<String> categoryParamSet = new HashSet<String>();
                for (String languageParam : languageParamSet) {
                    for (GameCategoryType categoryType : newGameDB.getCategoryTypeSet()) {
                        String categoryParam = languageParam + "_" + categoryType.getCode();
                        categoryParamSet.add(categoryParam);
                    }
                }
                Set<String> themeParamSet = new HashSet<String>();
                for (String categoryParam : categoryParamSet) {
                    for (GameThemeType themeType : newGameDB.getThemeTypeSet()) {
                        String themeParam = categoryParam + "_" + themeType.getCode();
                        themeParamSet.add(themeParam);
                    }
                }
                for (String param : themeParamSet) {
                    redisManager.lpush(WEBAPPWWW_GAME_COLLECTION_PARAM_SET, param);
                }
            }

            //加入日志
            ToolsLog log = new ToolsLog();
            log.setOpUserId(getCurrentUser().getUserid());
            log.setOperType(LogOperType.GAMEDB_MODIFY);
            log.setOpTime(new Date());
            log.setOpIp(getIp());

            Map<String, String[]> params = request.getParameterMap();
            String queryString = "  ";
            for (String key : params.keySet()) {
                String[] values = params.get(key);
                for (int i = 0; i < values.length; i++) {
                    queryString += key + "=" + values[i] + "&";
                }
            }
            // 去掉最后一个空格
            queryString = queryString.substring(0, queryString.length() - 1);

            if (queryString.length() > 1900) {
                queryString = queryString.substring(0, 1900);
            }
            log.setOpAfter("游戏资料库更新->queryString" + queryString);
            addLog(log);

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            return new ModelAndView("redirect:/gamedb/list?validstatus=" + validstatus + "&searchname=" + searchname + "&pager.offset=" + pageStartIndex + "&errorMsg=system.error");
        }
        return new ModelAndView("redirect:/gamedb/list?validstatus=" + validstatus + "&searchname=" + searchname + "&pager.offset=" + pageStartIndex);
    }


    @RequestMapping(value = "/delete")
    public ModelAndView delete(HttpServletRequest request, HttpServletResponse response,
                               @RequestParam(value = "gamedbid", required = false) Long gameDbId,
                               @RequestParam(value = "validstatus", required = false) String validstatus,
                               @RequestParam(value = "searchname", required = false) String searchname,
                               @RequestParam(value = "pageStartIndex", required = false) Integer pageStartIndex) {
        BasicDBObject queryDBObject = new BasicDBObject();
        queryDBObject.put(GameDBField.ID.getColumn(), gameDbId);
        BasicDBObject updateDBObject = new BasicDBObject();
        updateDBObject.put(GameDBField.VALIDSTATUS.getColumn(), ValidStatus.REMOVED.getCode());
        try {
            GameDB gameDB = GameResourceServiceSngl.get().getGameDB(queryDBObject, false);

            boolean bool = GameResourceServiceSngl.get().updateGameDB(queryDBObject, updateDBObject);
            if (!CollectionUtil.isEmpty(gameDB.getPlatformMap()) && gameDB.getGameNetType() != null && !CollectionUtil.isEmpty(gameDB.getLanguageTypeSet()) && !CollectionUtil.isEmpty(gameDB.getCategoryTypeSet()) && !CollectionUtil.isEmpty(gameDB.getThemeTypeSet())) {
                String idStr = String.valueOf(gameDB.getGameDbId());
                Set<String> platformParamSet = new HashSet<String>();
                for (String key : gameDB.getPlatformMap().keySet()) {
                    for (GamePlatform gamePlatform : gameDB.getPlatformMap().get(key)) {
                        String platformParam = idStr + "_" + key + "-" + gamePlatform.getCode();
                        platformParamSet.add(platformParam);
                    }
                }
                Set<String> netParamSet = new HashSet<String>();
                for (String platformParam : platformParamSet) {
                    String netParam = platformParam + "_" + gameDB.getGameNetType().getCode();
                    netParamSet.add(netParam);
                }
                Set<String> languageParamSet = new HashSet<String>();
                for (String netParam : netParamSet) {
                    for (GameLanguageType languageType : gameDB.getLanguageTypeSet()) {
                        String languageParam = netParam + "_" + languageType.getCode();
                        languageParamSet.add(languageParam);
                    }
                }
                Set<String> categoryParamSet = new HashSet<String>();
                for (String languageParam : languageParamSet) {
                    for (GameCategoryType categoryType : gameDB.getCategoryTypeSet()) {
                        String categoryParam = languageParam + "_" + categoryType.getCode();
                        categoryParamSet.add(categoryParam);
                    }
                }
                Set<String> themeParamSet = new HashSet<String>();
                for (String categoryParam : categoryParamSet) {
                    for (GameThemeType themeType : gameDB.getThemeTypeSet()) {
                        String themeParam = categoryParam + "_" + themeType.getCode();
                        themeParamSet.add(themeParam);
                    }
                }
                for (String param : themeParamSet) {
                    redisManager.lrem(WEBAPPWWW_GAME_COLLECTION_PARAM_SET, 0, param);
                }
            }


            //sendphp
            GameDB gameDBPhp = GameResourceServiceSngl.get().getGameDB(new BasicDBObject().append(GameDBField.ID.getColumn(), gameDbId), false);
            if (gameDBPhp != null) {
                GamedbSendPhpChannel channel = new GamedbSendPhpChannel();
                channel.sendPhpGamedb(gameDBPhp);
            }


            //修改点评数据
            GameUtil.updateGame(gameDBPhp, MicroAuthUtil.getToken(), null, null);

            //加入日志
            ToolsLog log = new ToolsLog();
            log.setOpUserId(getCurrentUser().getUserid());
            log.setOperType(LogOperType.GAMEDB_DELETE);
            log.setOpTime(new Date());
            log.setOpIp(getIp());

            log.setOpAfter("游戏资料库delete->gameDbId" + gameDbId);
            addLog(log);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            return new ModelAndView("redirect:/gamedb/list?validstatus=" + validstatus + "&searchname=" + searchname + "&pager.offset=" + pageStartIndex + "&errorMsg=system.error");
        }
        return new ModelAndView("redirect:/gamedb/list?validstatus=" + validstatus + "&searchname=" + searchname + "&pager.offset=" + pageStartIndex);
    }


    @RequestMapping(value = "/recover")
    public ModelAndView recover(HttpServletRequest request, HttpServletResponse response,
                                @RequestParam(value = "gamedbid", required = false) Long gameDbId,
                                @RequestParam(value = "validstatus", required = false) String validstatus,
                                @RequestParam(value = "searchname", required = false) String searchname,
                                @RequestParam(value = "pageStartIndex", required = false) Integer pageStartIndex,
                                @RequestParam(value = "recoverstatus", required = false) String recoverstatus) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        BasicDBObject queryDBObject = new BasicDBObject();
        queryDBObject.put(GameDBField.ID.getColumn(), gameDbId);
        BasicDBObject updateDBObject = new BasicDBObject();
        updateDBObject.put(GameDBField.VALIDSTATUS.getColumn(), GameDbStatus.getByCode(recoverstatus).getCode());
        try {
            GameResourceServiceSngl.get().updateGameDB(queryDBObject, updateDBObject);
            GameDB gameDB = GameResourceServiceSngl.get().getGameDB(queryDBObject, true);
            if (!CollectionUtil.isEmpty(gameDB.getPlatformMap()) && gameDB.getGameNetType() != null && !CollectionUtil.isEmpty(gameDB.getLanguageTypeSet()) && !CollectionUtil.isEmpty(gameDB.getCategoryTypeSet()) && !CollectionUtil.isEmpty(gameDB.getThemeTypeSet())) {
                String idStr = String.valueOf(gameDB.getGameDbId());
                Set<String> platformParamSet = new HashSet<String>();
                for (String key : gameDB.getPlatformMap().keySet()) {
                    for (GamePlatform gamePlatform : gameDB.getPlatformMap().get(key)) {
                        String platformParam = idStr + "_" + key + "-" + gamePlatform.getCode();
                        platformParamSet.add(platformParam);
                    }
                }
                Set<String> netParamSet = new HashSet<String>();
                for (String platformParam : platformParamSet) {
                    String netParam = platformParam + "_" + gameDB.getGameNetType().getCode();
                    netParamSet.add(netParam);
                }
                Set<String> languageParamSet = new HashSet<String>();
                for (String netParam : netParamSet) {
                    for (GameLanguageType languageType : gameDB.getLanguageTypeSet()) {
                        String languageParam = netParam + "_" + languageType.getCode();
                        languageParamSet.add(languageParam);
                    }
                }
                Set<String> categoryParamSet = new HashSet<String>();
                for (String languageParam : languageParamSet) {
                    for (GameCategoryType categoryType : gameDB.getCategoryTypeSet()) {
                        String categoryParam = languageParam + "_" + categoryType.getCode();
                        categoryParamSet.add(categoryParam);
                    }
                }
                Set<String> themeParamSet = new HashSet<String>();
                for (String categoryParam : categoryParamSet) {
                    for (GameThemeType themeType : gameDB.getThemeTypeSet()) {
                        String themeParam = categoryParam + "_" + themeType.getCode();
                        themeParamSet.add(themeParam);
                    }
                }
                for (String param : themeParamSet) {
                    if (recoverstatus.equals(GameDbStatus.VALID.getCode())) {
                        redisManager.lpush(WEBAPPWWW_GAME_COLLECTION_PARAM_SET, param);
                    } else {
                        redisManager.lrem(WEBAPPWWW_GAME_COLLECTION_PARAM_SET, 0, param);
                    }
                }
            }

            //加入日志
            ToolsLog log = new ToolsLog();
            log.setOpUserId(getCurrentUser().getUserid());
            log.setOperType(LogOperType.GAMEDB_RECOVER);
            log.setOpTime(new Date());
            log.setOpIp(getIp());

            Map<String, String[]> params = request.getParameterMap();
            String queryString = "  ";
            for (String key : params.keySet()) {
                String[] values = params.get(key);
                for (int i = 0; i < values.length; i++) {
                    queryString += key + "=" + values[i] + "&";
                }
            }
            // 去掉最后一个空格
            queryString = queryString.substring(0, queryString.length() - 1);
            if (queryString.length() > 1900) {
                queryString = queryString.substring(0, 1900);
            }

            //sendphp
            GameDB gameDBPhp = GameResourceServiceSngl.get().getGameDB(new BasicDBObject().append(GameDBField.ID.getColumn(), gameDbId), false);
            if (gameDBPhp != null) {
                GamedbSendPhpChannel channel = new GamedbSendPhpChannel();
                channel.sendPhpGamedb(gameDBPhp);
            }


            //修改点评数据
            GameUtil.updateGame(gameDBPhp, MicroAuthUtil.getToken(), null, null);

            log.setOpAfter("游戏资料库recover->queryString" + queryString);
            addLog(log);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            return new ModelAndView("redirect:/gamedb/list?validstatus=" + validstatus + "&searchname=" + searchname + "&pager.offset=" + pageStartIndex + "&errorMsg=system.error");
        }
        return new ModelAndView("redirect:/gamedb/list?validstatus=" + validstatus + "&searchname=" + searchname + "&pager.offset=" + pageStartIndex);
    }

    @RequestMapping(value = "/getinfobyid")
    public ModelAndView getInfoById(@RequestParam(value = "gamedbid", required = false) String gameDbId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            mapMessage.put("mPlatforms", MobilePlatform.getAll());
            mapMessage.put("pcPlatforms", PCPlatform.getAll());
            mapMessage.put("pspPlatforms", PSPPlatform.getAll());
            mapMessage.put("tvPlatforms", TVPlatform.getAll());
            mapMessage.put("categoryTypes", GameCategoryType.getAll());
            mapMessage.put("netTypes", GameNetType.getAll());
            mapMessage.put("languageTypes", GameLanguageType.getAll());
            mapMessage.put("themeTypes", GameThemeType.getAll());
            mapMessage.put("picTypes", picTypeList);

            GameDB gameDB = GameResourceServiceSngl.get().getGameDB(new BasicDBObject().append(GameDBField.ID.getColumn(), gameDbId), false);
            mapMessage.put("gameDb", gameDB);

            GamePlatformType gamePlatformType = null;
            for (GamePlatformType platformType : gameDB.getPlatformTypeSet()) {
                if (platformType.equals(GamePlatformType.MOBILE)) {
                    gamePlatformType = GamePlatformType.MOBILE;
                    break;
                }
            }
            mapMessage.put("gamePlatformType", gamePlatformType == null ? 0 : gamePlatformType.getCode());

            List<String> gamePicList = new ArrayList<String>();
            if (gameDB.getGamePic().indexOf(",") > 0) {
                String[] picArr = gameDB.getGamePic().split(",");
                for (String pic : picArr) {
                    if (!StringUtil.isEmpty(pic)) {
                        gamePicList.add(URLUtils.getJoymeDnUrl(pic));
                    }
                }
            } else {
                if (!StringUtil.isEmpty(gameDB.getGamePic())) {
                    gamePicList.add(URLUtils.getJoymeDnUrl(gameDB.getGamePic()));
                }
            }
            mapMessage.put("gamePicList", gamePicList);

            List<GameDBChannel> list = GameResourceServiceSngl.get().queryGameDbChannel();
            mapMessage.put("channelList", list);

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMsg", "system.error");
        }
        return new ModelAndView("/gameresource/gamedb/gameinfopage", mapMessage);
    }

    @RequestMapping(value = "/archives")
    public ModelAndView archives(HttpServletRequest request, HttpServletResponse response,
                                 @RequestParam(value = "gamedbid", required = false) Long gameDbId,
                                 @RequestParam(value = "contenttype", required = false) Integer contentType) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("contentType", contentType);
        try {
            GameDB gameDB = GameResourceServiceSngl.get().getGameDB(new BasicDBObject().append("_id", gameDbId), false);
            mapMessage.put("gameDb", gameDB);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMessage", "system.error");
        }
        return new ModelAndView("/gameresource/gamedb/gamearchives", mapMessage);
    }

    public static void main(String[] args) {
        String httpUrl = "http://article.joyme.alpha/plus/api.php?a=getArticleImg&id=17589";
        HttpClientManager httpClientManager = new HttpClientManager();
        HttpResult httpResult = httpClientManager.get(httpUrl, new HttpParameter[]{}, "UTF-8");
        if (httpResult != null) {

        }
    }
}
