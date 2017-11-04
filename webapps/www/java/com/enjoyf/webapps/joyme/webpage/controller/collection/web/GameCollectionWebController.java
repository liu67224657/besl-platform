package com.enjoyf.webapps.joyme.webpage.controller.collection.web;

import com.enjoyf.mcms.bean.DedeArctype;
import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.comment.CommentDomain;
import com.enjoyf.platform.service.content.ActivityType;
import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.gameres.gamedb.*;
import com.enjoyf.platform.service.gameres.gamedb.collection.GameArchivesDTO;
import com.enjoyf.platform.service.gameres.gamedb.collection.GameCollectionDTO;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.joymeapp.gameclient.ArchiveContentType;
import com.enjoyf.platform.service.joymeapp.gameclient.ArchiveRelationType;
import com.enjoyf.platform.service.joymeapp.gameclient.TagDedearchives;
import com.enjoyf.platform.service.joymeapp.gameclient.TagDedearchivesFiled;
import com.enjoyf.platform.service.point.ActivityGoods;
import com.enjoyf.platform.service.point.ActivityGoodsField;
import com.enjoyf.platform.service.point.PointServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.http.HttpClientManager;
import com.enjoyf.platform.util.http.HttpParameter;
import com.enjoyf.platform.util.http.HttpResult;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.redis.RedisManager;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryCriterions;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryExpress;
import com.enjoyf.platform.util.sql.mongodb.MongoSort;
import com.enjoyf.platform.util.sql.mongodb.MongoSortOrder;
import com.enjoyf.util.StringUtil;
import com.enjoyf.webapps.joyme.dto.activity.ActivityDTO;
import com.enjoyf.webapps.joyme.weblogic.giftmarket.GiftMarketWebLogic;
import com.enjoyf.webapps.joyme.webpage.controller.collection.AbstractGameCollectionController;
import com.mongodb.BasicDBObject;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhitaoshi on 2015/6/16.
 */
@Controller
@RequestMapping(value = "/collection")
public class GameCollectionWebController extends AbstractGameCollectionController {

    private static RedisManager redisManager = new RedisManager(WebappConfig.get().getProps());
    private static final String WEBAPPWWW_GAME_COLLECTION_PARAM_SET = "webappwww_game_collection_params";
    private static final String HOT_ARCHIVE_CACHE = "webappwww_game_collection_hot_archives";

    private static final String COLLECTION_GAME_RECOMMEND = "collection_game_recommend";//着迷推荐
    private static final String COLLECTION_GAME_NEWS = "collection_game_news";//最新入库
    private static final String COLLECTION_GAME_MOBILE = "collection_game_mobile";//手机游戏
    private static final String COLLECTION_GAME_PC = "collection_game_pc";//电脑游戏
    private static final String COLLECTION_GAME_PSP = "collection_game_psp";//掌机游戏
    private static final String COLLECTION_GAME_TV = "collection_game_tv";//电视游戏
    private static final String COLLECTION_GAME_HOT_MAJOR = "collection_game_hot_major";//热门游戏

    private static final int INDEX_PAGE_SIZE = 3;
    private static final int GENRE_PAGE_SIZE = 18;
    private static final int GIFT_PAGE_SIZE = 4;
    private static final int VIDEO_PAGE_SIZE = 4;
    private static final int GUIDE_PAGE_SIZE = 5;
    private static final int NEWS_PAGE_SIZE = 5;
    private static final int CATEGORY_PAGE_SIZE = 5;
    private static final int HOT_GAME_PAGE_SIZE = 10;

    private static final int ARCHIVES_PAGE_SIZE = 18;
    
    /**
     * 游戏库搜索参数标记
     */
    private static final String GAME_GENRE_PLATFORM="p";//平台
    private static final String GAME_GENRE_NET="n";//联网
    private static final String GAME_GENRE_LANGUAGE="l";//语言
    private static final String GAME_GENRE_CATEGORY="c";//类型
    private static final String GAME_GENRE_THEME="t";//题材

    @Resource(name = "giftMarketWebLogic")
    private GiftMarketWebLogic giftMarketWebLogic;

    private static Set<GamePlatformType> platformTypeSet = new HashSet<GamePlatformType>();
    private static Map<String, Set<GamePlatform>> platformMap = new HashMap<String, Set<GamePlatform>>();

    static {
        platformTypeSet.add(GamePlatformType.MOBILE);
        platformTypeSet.add(GamePlatformType.PC);
        platformTypeSet.add(GamePlatformType.PSP);
        platformTypeSet.add(GamePlatformType.TV);

        platformMap.put(String.valueOf(GamePlatformType.MOBILE.getCode()), new HashSet<GamePlatform>());
        for (MobilePlatform mobilePlatform : MobilePlatform.getAll()) {
            platformMap.get(String.valueOf(GamePlatformType.MOBILE.getCode())).add(mobilePlatform);
        }
//        platformMap.put(String.valueOf(GamePlatformType.WIN7.getCode()), new HashSet<GamePlatform>());
//        for (PCPlatform pcPlatform : PCPlatform.getAll()) {
//            platformMap.get(String.valueOf(GamePlatformType.WIN7.getCode())).add(pcPlatform);
//        }
        platformMap.put(String.valueOf(GamePlatformType.PSP.getCode()), new HashSet<GamePlatform>());
        for (PSPPlatform pspPlatform : PSPPlatform.getAll()) {
            platformMap.get(String.valueOf(GamePlatformType.PSP.getCode())).add(pspPlatform);
        }
        platformMap.put(String.valueOf(GamePlatformType.TV.getCode()), new HashSet<GamePlatform>());
        for (TVPlatform tvPlatform : TVPlatform.getAll()) {
            platformMap.get(String.valueOf(GamePlatformType.TV.getCode())).add(tvPlatform);
        }
    }

    @RequestMapping
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) {
        //todo 推荐用host 然后contains判断是否是手机版
        String reqUrl = request.getRequestURL().toString();
        if (request.getQueryString() != null && request.getQueryString().contains("tab_device=wap_pc")) {
            //手机访问电脑版
            return pcIndex(request, response);
        } else if (reqUrl.startsWith("http://m.joyme.") || reqUrl.startsWith("https://m.joyme.")) {
            //m域名
            return wapIndex(request, response);
        } else {
            return pcIndex(request, response);
        }
    }

    private ModelAndView wapIndex(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            //平台
            mapMessage.put("platformTypeSet", platformTypeSet);
            mapMessage.put("platformMap", platformMap);
            //联网
            mapMessage.put("netTypeSet", GameNetType.getAll());
            //语言
            mapMessage.put("languageTypeSet", GameLanguageType.getAll());
            //类型
            mapMessage.put("categorySet", GameCategoryType.getAll());
            //题材
            mapMessage.put("themeTypeSet", GameThemeType.getAll());

            Set<String> lineCodeSet = new HashSet<String>();
            lineCodeSet.add(COLLECTION_GAME_RECOMMEND);
            lineCodeSet.add(COLLECTION_GAME_NEWS);
            lineCodeSet.add(COLLECTION_GAME_MOBILE);
            lineCodeSet.add(COLLECTION_GAME_PC);
            lineCodeSet.add(COLLECTION_GAME_PSP);
            lineCodeSet.add(COLLECTION_GAME_TV);

            Pagination page = new Pagination(INDEX_PAGE_SIZE * 1, 1, INDEX_PAGE_SIZE);

            Map<String, List<GameCollectionDTO>> mapList = GameResourceServiceSngl.get().getGameCollectionListCache(ClientLineType.GAME_COLLECTION, AppPlatform.WEB, lineCodeSet, page);
            if (!CollectionUtil.isEmpty(mapList)) {
                //着迷推荐
                mapMessage.put("recommendList", mapList.get(String.valueOf(ClientItemType.GAME_RECOMMEND.getCode())));
                //最新入库
                mapMessage.put("newList", mapList.get(String.valueOf(ClientItemType.GAME_NEWS.getCode())));
                //手机游戏
                mapMessage.put("mobileList", mapList.get(String.valueOf(ClientItemType.GAME_MOBILE.getCode())));
                //电脑游戏
                mapMessage.put("pcList", mapList.get(String.valueOf(ClientItemType.GAME_PC.getCode())));
                //掌机游戏
                mapMessage.put("pspList", mapList.get(String.valueOf(ClientItemType.GAME_PSP.getCode())));
                //电视游戏
                mapMessage.put("tvList", mapList.get(String.valueOf(ClientItemType.GAME_TV.getCode())));
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/common/custompage-wap", mapMessage);
        }
        return new ModelAndView("/views/jsp/collection/index-m", mapMessage);
    }

    private ModelAndView pcIndex(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            //平台
            mapMessage.put("platformTypeSet", platformTypeSet);
            mapMessage.put("platformMap", platformMap);
            //联网
            mapMessage.put("netTypeSet", GameNetType.getAll());
            //语言
            mapMessage.put("languageTypeSet", GameLanguageType.getAll());
            //类型
            mapMessage.put("categorySet", GameCategoryType.getAll());
            //题材
            mapMessage.put("themeTypeSet", GameThemeType.getAll());

            Set<String> lineCodeSet = new HashSet<String>();
            lineCodeSet.add(COLLECTION_GAME_RECOMMEND);
            lineCodeSet.add(COLLECTION_GAME_NEWS);
            lineCodeSet.add(COLLECTION_GAME_MOBILE);
            lineCodeSet.add(COLLECTION_GAME_PC);
            lineCodeSet.add(COLLECTION_GAME_PSP);
            lineCodeSet.add(COLLECTION_GAME_TV);

            Pagination page = new Pagination(INDEX_PAGE_SIZE * 1, 1, INDEX_PAGE_SIZE);

            Date date=new Date();
            Map<String, List<GameCollectionDTO>> mapList = GameResourceServiceSngl.get().getGameCollectionListCache(ClientLineType.GAME_COLLECTION, AppPlatform.WEB, lineCodeSet, page);
            if (!CollectionUtil.isEmpty(mapList)) {
            	//着迷推荐
                List<GameCollectionDTO> recommendList=mapList.get(String.valueOf(ClientItemType.GAME_RECOMMEND.getCode()));
                if (!CollectionUtil.isEmpty(recommendList)) {
					for (GameCollectionDTO gameCollectionDTO : recommendList) {
	                	int googsNum=0;
						List<ActivityGoods> activityGoodsList = PointServiceSngl.get().queryActivityGoodsByGameId(gameCollectionDTO.getGameDbId());
						for (ActivityGoods activityGoods : activityGoodsList) {
							boolean isStart=activityGoods.getStartTime().getTime()<date.getTime();
							boolean isNotEnd=activityGoods.getEndTime().getTime()>date.getTime();
							if (isStart && isNotEnd && activityGoods.getGoodsResetAmount()>0) {
								googsNum++;
							}
						}						
						gameCollectionDTO.setGiftSum(googsNum);
					}
				}
                
                mapMessage.put("recommendList",recommendList);
               
                //最新入库
                List<GameCollectionDTO> newList=mapList.get(String.valueOf(ClientItemType.GAME_NEWS.getCode()));
                if (!CollectionUtil.isEmpty(newList)) {
					for (GameCollectionDTO gameCollectionDTO : newList) {
	                	int googsNum=0;
						List<ActivityGoods> activityGoodsList = PointServiceSngl.get().queryActivityGoodsByGameId(gameCollectionDTO.getGameDbId());
						for (ActivityGoods activityGoods : activityGoodsList) {
							boolean isStart=activityGoods.getStartTime().getTime()<date.getTime();
							boolean isNotEnd=activityGoods.getEndTime().getTime()>date.getTime();
							if (isStart && isNotEnd && activityGoods.getGoodsResetAmount()>0) {
								googsNum++;
							}
						}						
						gameCollectionDTO.setGiftSum(googsNum);
					}
				} 
                mapMessage.put("newList", newList);
                //手机游戏
                List<GameCollectionDTO> mobileList=mapList.get(String.valueOf(ClientItemType.GAME_MOBILE.getCode()));
                if (!CollectionUtil.isEmpty(mobileList)) {
					for (GameCollectionDTO gameCollectionDTO : mobileList) {
	                	int googsNum=0;
						List<ActivityGoods> activityGoodsList = PointServiceSngl.get().queryActivityGoodsByGameId(gameCollectionDTO.getGameDbId());
						for (ActivityGoods activityGoods : activityGoodsList) {
							boolean isStart=activityGoods.getStartTime().getTime()<date.getTime();
							boolean isNotEnd=activityGoods.getEndTime().getTime()>date.getTime();
							if (isStart && isNotEnd && activityGoods.getGoodsResetAmount()>0) {
								googsNum++;
							}
						}						
						gameCollectionDTO.setGiftSum(googsNum);
					}
				} 
                mapMessage.put("mobileList", mobileList);
                //电脑游戏
                List<GameCollectionDTO> pcList=mapList.get(String.valueOf(ClientItemType.GAME_PC.getCode()));
                if (!CollectionUtil.isEmpty(pcList)) {
					for (GameCollectionDTO gameCollectionDTO : pcList) {
	                	int googsNum=0;
						List<ActivityGoods> activityGoodsList = PointServiceSngl.get().queryActivityGoodsByGameId(gameCollectionDTO.getGameDbId());
						for (ActivityGoods activityGoods : activityGoodsList) {
							boolean isStart=activityGoods.getStartTime().getTime()<date.getTime();
							boolean isNotEnd=activityGoods.getEndTime().getTime()>date.getTime();
							if (isStart && isNotEnd && activityGoods.getGoodsResetAmount()>0) {
								googsNum++;
							}
						}						
						gameCollectionDTO.setGiftSum(googsNum);
					}
				}
                mapMessage.put("pcList", pcList);
                //掌机游戏
                List<GameCollectionDTO> pspList=mapList.get(String.valueOf(ClientItemType.GAME_PSP.getCode()));
                if (!CollectionUtil.isEmpty(pspList)) {
					for (GameCollectionDTO gameCollectionDTO : pspList) {
	                	int googsNum=0;
						List<ActivityGoods> activityGoodsList = PointServiceSngl.get().queryActivityGoodsByGameId(gameCollectionDTO.getGameDbId());
						for (ActivityGoods activityGoods : activityGoodsList) {
							boolean isStart=activityGoods.getStartTime().getTime()<date.getTime();
							boolean isNotEnd=activityGoods.getEndTime().getTime()>date.getTime();
							if (isStart && isNotEnd && activityGoods.getGoodsResetAmount()>0) {
								googsNum++;
							}
						}						
						gameCollectionDTO.setGiftSum(googsNum);
					}
				}
                mapMessage.put("pspList", pspList);
                //电视游戏
                List<GameCollectionDTO> tvList=mapList.get(String.valueOf(ClientItemType.GAME_TV.getCode()));
                if (!CollectionUtil.isEmpty(tvList)) {
					for (GameCollectionDTO gameCollectionDTO : tvList) {
	                	int googsNum=0;
						List<ActivityGoods> activityGoodsList = PointServiceSngl.get().queryActivityGoodsByGameId(gameCollectionDTO.getGameDbId());
						for (ActivityGoods activityGoods : activityGoodsList) {
							boolean isStart=activityGoods.getStartTime().getTime()<date.getTime();
							boolean isNotEnd=activityGoods.getEndTime().getTime()>date.getTime();
							if (isStart && isNotEnd && activityGoods.getGoodsResetAmount()>0) {
								googsNum++;
							}
						}						
						gameCollectionDTO.setGiftSum(googsNum);
					}
				}
                mapMessage.put("tvList", tvList);
            }
            

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/common/custompage", mapMessage);
        }
        return new ModelAndView("/views/jsp/collection/index", mapMessage);
    }

    @RequestMapping(value = "/genre")
    public ModelAndView genre(HttpServletRequest request, HttpServletResponse response) {
        String reqUrl = request.getRequestURL().toString();
        if (request.getQueryString() != null && request.getQueryString().contains("tab_device=wap_pc")) {
            //手机访问电脑版
            return pcGenre(request, response);
        } else if (reqUrl.startsWith("http://m.joyme.") || reqUrl.startsWith("https://m.joyme.")) {
            //m域名
            return wapGenre(request, response);
        } else {
            return pcGenre(request, response);
        }
    }

    private ModelAndView wapGenre(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        Pagination pagination = new Pagination(GENRE_PAGE_SIZE * 1, 1, GENRE_PAGE_SIZE);
        try {
            //平台
            mapMessage.put("platformTypeSet", platformTypeSet);
            mapMessage.put("platformMap", platformMap);
            //联网
            mapMessage.put("netTypeSet", GameNetType.getAll());
            //语言
            mapMessage.put("languageTypeSet", GameLanguageType.getAll());
            //类型
            mapMessage.put("categorySet", GameCategoryType.getAll());
            //题材
            mapMessage.put("themeTypeSet", GameThemeType.getAll());

            MongoQueryExpress mongoQueryExpress = new MongoQueryExpress();
            mongoQueryExpress.add(MongoQueryCriterions.eq(GameDBField.VALIDSTATUS, GameDbStatus.VALID.getCode()));
            mongoQueryExpress.add(new MongoSort[]{new MongoSort(GameDBField.GAMEPUBLICTIME, MongoSortOrder.DESC)});
            PageRows<GameDB> gameDBPageRows = GameResourceServiceSngl.get().queryGameDbByPage(mongoQueryExpress, pagination);
            if (gameDBPageRows != null && !CollectionUtil.isEmpty(gameDBPageRows.getRows())) {
                List<GameCollectionDTO> list = new ArrayList<GameCollectionDTO>();
                for (GameDB gameDB : gameDBPageRows.getRows()) {
                    list.add(GameCollectionDTO.buildDTOFromGameDB(gameDB));
                }
                mapMessage.put("list", list);
                mapMessage.put("page", gameDBPageRows.getPage());
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/common/custompage-wap", mapMessage);
        }
        return new ModelAndView("/views/jsp/collection/genre-m", mapMessage);
    }

    private ModelAndView pcGenre(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        Pagination pagination = new Pagination(GENRE_PAGE_SIZE * 1, 1, GENRE_PAGE_SIZE);
        try {
            //平台
            mapMessage.put("platformTypeSet", platformTypeSet);
            mapMessage.put("platformMap", platformMap);
            //联网
            mapMessage.put("netTypeSet", GameNetType.getAll());
            //语言
            mapMessage.put("languageTypeSet", GameLanguageType.getAll());
            //类型
            mapMessage.put("categorySet", GameCategoryType.getAll());
            //题材
            mapMessage.put("themeTypeSet", GameThemeType.getAll());

            Date date=new Date();
            MongoQueryExpress mongoQueryExpress = new MongoQueryExpress();
            mongoQueryExpress.add(MongoQueryCriterions.eq(GameDBField.VALIDSTATUS, GameDbStatus.VALID.getCode()));
            mongoQueryExpress.add( new MongoSort[]{new MongoSort(GameDBField.GAMEPUBLICTIME, MongoSortOrder.DESC)});
            PageRows<GameDB> gameDBPageRows = GameResourceServiceSngl.get().queryGameDbByPage(mongoQueryExpress, pagination);
            if (gameDBPageRows != null && !CollectionUtil.isEmpty(gameDBPageRows.getRows())) {
                List<GameCollectionDTO> list = new ArrayList<GameCollectionDTO>();
                for (GameDB gameDB : gameDBPageRows.getRows()) {
                	int googsNum=0;
					List<ActivityGoods> activityGoodsList = PointServiceSngl.get().queryActivityGoodsByGameId(gameDB.getGameDbId());
					for (ActivityGoods activityGoods : activityGoodsList) {
						boolean isStart=activityGoods.getStartTime().getTime()<date.getTime();
						boolean isNotEnd=activityGoods.getEndTime().getTime()>date.getTime();
						if (isStart && isNotEnd && activityGoods.getGoodsResetAmount()>0) {
							googsNum++;
						}
					}						
					gameDB.setGiftSum(googsNum);
                    list.add(GameCollectionDTO.buildDTOFromGameDB(gameDB));
                }
                mapMessage.put("list", list);
                mapMessage.put("page", gameDBPageRows.getPage());
                String title=request.getParameter("title");
                mapMessage.put("title", title);
                mapMessage.put("sort", "1");
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/common/custompage", mapMessage);
        }
        return new ModelAndView("/views/jsp/collection/genre", mapMessage);
    }

    @RequestMapping(value = "/genre/{param}")
    public ModelAndView genreParam(HttpServletRequest request, HttpServletResponse response,
                                   @PathVariable(value = "param") String paramStr) {
        String reqUrl = request.getRequestURL().toString();
        if (request.getQueryString() != null && request.getQueryString().contains("tab_device=wap_pc")) {
            //手机访问电脑版
            return pcGenreParam(request, response, paramStr);
        } else if (reqUrl.startsWith("http://m.joyme.") || reqUrl.startsWith("https://m.joyme.")) {
            //m域名
            return wapGenreParam(request, response, paramStr);
        } else {
            return pcGenreParam(request, response, paramStr);
        }
    }

    private ModelAndView wapGenreParam(HttpServletRequest request, HttpServletResponse response, String paramStr) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            //平台
            mapMessage.put("platformTypeSet", platformTypeSet);
            mapMessage.put("platformMap", platformMap);
            //联网
            mapMessage.put("netTypeSet", GameNetType.getAll());
            //语言
            mapMessage.put("languageTypeSet", GameLanguageType.getAll());
            //类型
            mapMessage.put("categorySet", GameCategoryType.getAll());
            //题材
            mapMessage.put("themeTypeSet", GameThemeType.getAll());

            Set<String> paramSet = new HashSet<String>();
            String name = null;
            String page = "";
            boolean isName = false;
            String sort="";
            if (!StringUtil.isEmpty(paramStr)) {
                if (paramStr.indexOf("_") > 0) {
                    String[] paramArr = paramStr.split("_");
                    for (String param : paramArr) {
                        if (param.startsWith("name:")) {
                            name = param.replaceFirst("name:", "");
                            isName = true;
                        } else if (param.startsWith("page:")) {
                            page = param.replaceFirst("page:", "");
                        } else if (param.startsWith("s")){
                            sort = param.replaceFirst("s", "");
                        } else {
                            paramSet.add(param);
                        }
                    }
                } else {
                    if (paramStr.startsWith("name:")) {
                        name = paramStr.replaceFirst("name:", "");
                        isName = true;
                    } else if (paramStr.startsWith("page:")) {
                        page = paramStr.replaceFirst("page:", "");
                    } else if(paramStr.startsWith("s")){
                        sort = paramStr.replaceFirst("s", "");
                    } else {
                        paramSet.add(paramStr);
                    }
                }
            }
            mapMessage.put("isname", isName);
            mapMessage.put("name", name);
            mapMessage.put("sort",sort=StringUtil.isEmpty(sort)?"1":sort);

            int cp = 1;
            try {
                cp = Integer.parseInt(page);
            } catch (NumberFormatException e) {
            }
            Pagination pagination = new Pagination(GENRE_PAGE_SIZE * cp, cp, GENRE_PAGE_SIZE);

            MongoQueryExpress mongoQueryExpress = new MongoQueryExpress();
            if (isName) {
                if(!StringUtil.isEmpty(name)){
                    mongoQueryExpress.add(MongoQueryCriterions.like(GameDBField.GAMENAME, name));
/*                    mongoQueryExpress.add(MongoQueryCriterions.eq(GameDBField.VALIDSTATUS, GameDbStatus.VALID.getCode()));
                    PageRows<GameDB> gameDBPageRows = GameResourceServiceSngl.get().queryGameDbByPage(mongoQueryExpress, pagination);
                    if (gameDBPageRows != null && !CollectionUtil.isEmpty(gameDBPageRows.getRows())) {
                        List<GameCollectionDTO> list = new ArrayList<GameCollectionDTO>();
                        for (GameDB gameDB : gameDBPageRows.getRows()) {
                            list.add(GameCollectionDTO.buildDTOFromGameDB(gameDB));
                        }
                        mapMessage.put("list", list);
                        mapMessage.put("page", gameDBPageRows.getPage());
                    }*/
                }
            } else {
                if (!CollectionUtil.isEmpty(paramSet)) {
                    for (String param : paramSet) {
                        if (param.startsWith(GAME_GENRE_PLATFORM)) {
                            String platformParam = param.replaceFirst(GAME_GENRE_PLATFORM, "");
                            if (platformParam.indexOf("-") > 0) {
                                String type = platformParam.substring(0, platformParam.indexOf("-"));
                                if(!StringUtil.isEmpty(type) && StringUtil.isNumeric(type) && GamePlatformType.getByCode(Integer.valueOf(type)) != null){
                                    mapMessage.put("platformTypeCode", Integer.valueOf(type));
                                    mapMessage.put("checkPlatformType", GamePlatformType.getByCode(Integer.valueOf(type)));
                                    mongoQueryExpress.add(MongoQueryCriterions.eq(new GameDBField(GameDBField.PLATFORMTYPE_.getColumn() + type, ObjectFieldDBType.BOOLEAN), true));
                                }
                                String platform = platformParam.substring(platformParam.indexOf("-")+1);
                                if(!StringUtil.isEmpty(platform) && StringUtil.isNumeric(platform)){
                                    mapMessage.put("platformCode", Integer.valueOf(platform));
                                    mongoQueryExpress.add(MongoQueryCriterions.eq(new GameDBField(GameDBField.GAME_PLATFORM_.getColumn() + type + "_" + platform, ObjectFieldDBType.BOOLEAN), true));
                                }
                            }
                        }
                        if (param.startsWith(GAME_GENRE_NET)) {
                            String net = param.replaceFirst(GAME_GENRE_NET, "");
                            if (!StringUtil.isEmpty(net) && StringUtil.isNumeric(net)) {
                                mapMessage.put("netCode", Integer.valueOf(net));
                                mapMessage.put("checkNetType", GameNetType.getByCode(Integer.valueOf(net)));
                                mongoQueryExpress.add(MongoQueryCriterions.eq(GameDBField.GAMENETTYPE, Integer.valueOf(net)));
                            }
                        }
                        if (param.startsWith(GAME_GENRE_LANGUAGE)) {
                            String language = param.replaceFirst(GAME_GENRE_LANGUAGE, "");
                            if (!StringUtil.isEmpty(language) && StringUtil.isNumeric(language)) {
                                mapMessage.put("languageCode", Integer.valueOf(language));
                                mapMessage.put("checkLanguageType", GameLanguageType.getByCode(Integer.valueOf(language)));
                                mongoQueryExpress.add(MongoQueryCriterions.eq(new GameDBField(GameDBField.GAME_LANGUAGE_.getColumn() + language, ObjectFieldDBType.BOOLEAN), true));
                            }
                        }
                        if (param.startsWith(GAME_GENRE_CATEGORY)) {
                            String category = param.replaceFirst(GAME_GENRE_CATEGORY, "");
                            if (!StringUtil.isEmpty(category) && StringUtil.isNumeric(category)) {
                                mapMessage.put("categoryCode", Integer.valueOf(category));
                                mapMessage.put("checkCategoryType", GameCategoryType.getByCode(Integer.valueOf(category)));
                                mongoQueryExpress.add(MongoQueryCriterions.eq(new GameDBField(GameDBField.GAME_CATEGORY_.getColumn() + category, ObjectFieldDBType.BOOLEAN), true));
                            }
                        }
                        if (param.startsWith(GAME_GENRE_THEME)) {
                            String theme = param.replaceFirst(GAME_GENRE_THEME, "");
                            if (!StringUtil.isEmpty(theme) && StringUtil.isNumeric(theme)) {
                                mapMessage.put("themeCode", Integer.valueOf(theme));
                                mapMessage.put("checkThemeType", GameThemeType.getByCode(Integer.valueOf(theme)));
                                mongoQueryExpress.add(MongoQueryCriterions.eq(new GameDBField(GameDBField.GAME_THEME_.getColumn() + theme, ObjectFieldDBType.BOOLEAN), true));
                            }
                        }
/*                        if (param.startsWith("s")) {
                            String sort = param.replaceFirst("s", "");
                            mapMessage.put("sort", sort);
                            if (sort.equals("2")) {
                                mongoQueryExpress.add(new MongoSort[]{new MongoSort(GameDBField.GAMERATE, MongoSortOrder.DESC),new MongoSort(GameDBField.GAMEPUBLICTIME, MongoSortOrder.DESC)});
                            } else {
                                mongoQueryExpress.add(new MongoSort[]{new MongoSort(GameDBField.GAMEPUBLICTIME, MongoSortOrder.DESC),new MongoSort(GameDBField.GAMERATE, MongoSortOrder.DESC)});
                            }
                        }*/
                    }
                }

            }
            if (sort.equals("2")) {
                mongoQueryExpress.add(new MongoSort[]{new MongoSort(GameDBField.GAMERATE, MongoSortOrder.DESC),new MongoSort(GameDBField.GAMEPUBLICTIME, MongoSortOrder.DESC)});
            } else {
                mongoQueryExpress.add(new MongoSort[]{new MongoSort(GameDBField.GAMEPUBLICTIME, MongoSortOrder.DESC),new MongoSort(GameDBField.GAMERATE, MongoSortOrder.DESC)});
            }
            mongoQueryExpress.add(MongoQueryCriterions.eq(GameDBField.VALIDSTATUS, GameDbStatus.VALID.getCode()));
            PageRows<GameDB> gameDBPageRows = GameResourceServiceSngl.get().queryGameDbByPage(mongoQueryExpress, pagination);
            if (gameDBPageRows != null && !CollectionUtil.isEmpty(gameDBPageRows.getRows())) {
                List<GameCollectionDTO> list = new ArrayList<GameCollectionDTO>();
                for (GameDB gameDB : gameDBPageRows.getRows()) {
                    list.add(GameCollectionDTO.buildDTOFromGameDB(gameDB));
                }
                mapMessage.put("list", list);
                mapMessage.put("page", gameDBPageRows.getPage());
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/common/custompage-wap", mapMessage);
        }
        return new ModelAndView("/views/jsp/collection/genre-m", mapMessage);
    }

    private ModelAndView pcGenreParam(HttpServletRequest request, HttpServletResponse response, String paramStr) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            //平台
            mapMessage.put("platformTypeSet", platformTypeSet);
            mapMessage.put("platformMap", platformMap);
            //联网
            mapMessage.put("netTypeSet", GameNetType.getAll());
            //语言
            mapMessage.put("languageTypeSet", GameLanguageType.getAll());
            //类型
            mapMessage.put("categorySet", GameCategoryType.getAll());
            //题材
            mapMessage.put("themeTypeSet", GameThemeType.getAll());

            Set<String> paramSet = new HashSet<String>();
            String name = "";
            String page = "";
            String sort = "";
            if (!StringUtil.isEmpty(paramStr)) {
                if (paramStr.indexOf("_") > 0) {
                    String[] paramArr = paramStr.split("_");
                    for (String param : paramArr) {
                        if (param.startsWith("name:")) {
                            name = param.replaceFirst("name:", "");
                        } else if (param.startsWith("page:")) {
                            page = param.replaceFirst("page:", "");
                        }else if(param.startsWith("s")){
                            sort = param.replaceFirst("s", "");
                        } else {
                            paramSet.add(param);
                        }
                    }
                } else {
                    if (paramStr.startsWith("name:")) {
                        name = paramStr.replaceFirst("name:", "");
                    } else if (paramStr.startsWith("page:")) {
                        page = paramStr.replaceFirst("page:", "");
                    }else if(paramStr.startsWith("s")){
                        sort = paramStr.replaceFirst("s", "");
                    } else {
                        paramSet.add(paramStr);
                    }
                }
            }
            mapMessage.put("name", name);
            mapMessage.put("sort", sort=StringUtil.isEmpty(sort)?"1":sort);

            int cp = 1;
            try {
                cp = Integer.parseInt(page);
            } catch (NumberFormatException e) {
            }
            Pagination pagination = new Pagination(GENRE_PAGE_SIZE * cp, cp, GENRE_PAGE_SIZE);

            String platformTypeParam = "";
            String platformParam = "";
            String netParam = "";
            String languageParam = "";
            String categoryParam = "";
            String themeParam = "";

            MongoQueryExpress mongoQueryExpress = new MongoQueryExpress();
            if (!StringUtil.isEmpty(name)) {
                mongoQueryExpress.add(MongoQueryCriterions.like(GameDBField.GAMENAME, name));
            } else {
                if (!CollectionUtil.isEmpty(paramSet)) {
                    for (String param : paramSet) {
                        if (param.startsWith(GAME_GENRE_PLATFORM)) {
                            String platformParamStr = param.replaceFirst(GAME_GENRE_PLATFORM, "");
                            if (platformParamStr.indexOf("-") > 0) {
                                platformTypeParam = platformParamStr.substring(0, platformParamStr.indexOf("-"));
                                if(!StringUtil.isEmpty(platformTypeParam) && StringUtil.isNumeric(platformTypeParam) && GamePlatformType.getByCode(Integer.valueOf(platformTypeParam)) != null){
                                    mapMessage.put("platformTypeCode", Integer.valueOf(platformTypeParam));
                                    mapMessage.put("checkPlatformType", GamePlatformType.getByCode(Integer.valueOf(platformTypeParam)));
                                    mongoQueryExpress.add(MongoQueryCriterions.eq(new GameDBField(GameDBField.PLATFORMTYPE_.getColumn() + platformTypeParam, ObjectFieldDBType.BOOLEAN), true));
                                }
                                platformParam = platformParamStr.substring(platformParamStr.indexOf("-")+1);
                                if(!StringUtil.isEmpty(platformParam) && StringUtil.isNumeric(platformParam)){
                                    mapMessage.put("platformCode", Integer.valueOf(platformParam));
                                    mongoQueryExpress.add(MongoQueryCriterions.eq(new GameDBField(GameDBField.GAME_PLATFORM_.getColumn() + platformTypeParam + "_" + platformParam, ObjectFieldDBType.BOOLEAN), true));
                                }
                            }
                        }
                        if (param.startsWith(GAME_GENRE_NET)) {
                            netParam = param.replaceFirst(GAME_GENRE_NET, "");
                            if (!StringUtil.isEmpty(netParam) && StringUtil.isNumeric(netParam)) {
                                mapMessage.put("netCode", Integer.valueOf(netParam));
                                mapMessage.put("checkNetType", GameNetType.getByCode(Integer.valueOf(netParam)));
                                mongoQueryExpress.add(MongoQueryCriterions.eq(GameDBField.GAMENETTYPE, Integer.valueOf(netParam)));
                            }
                        }
                        if (param.startsWith(GAME_GENRE_LANGUAGE)) {
                            languageParam = param.replaceFirst(GAME_GENRE_LANGUAGE, "");
                            if (!StringUtil.isEmpty(languageParam) && StringUtil.isNumeric(languageParam)) {
                                mapMessage.put("languageCode", Integer.valueOf(languageParam));
                                mapMessage.put("checkLanguageType", GameLanguageType.getByCode(Integer.valueOf(languageParam)));
                                mongoQueryExpress.add(MongoQueryCriterions.eq(new GameDBField(GameDBField.GAME_LANGUAGE_.getColumn() + languageParam, ObjectFieldDBType.BOOLEAN), true));
                            }
                        }
                        if (param.startsWith(GAME_GENRE_CATEGORY)) {
                            categoryParam = param.replaceFirst(GAME_GENRE_CATEGORY, "");
                            if (!StringUtil.isEmpty(categoryParam) && StringUtil.isNumeric(categoryParam)) {
                                mapMessage.put("categoryCode", Integer.valueOf(categoryParam));
                                mapMessage.put("checkCategoryType", GameCategoryType.getByCode(Integer.valueOf(categoryParam)));
                                mongoQueryExpress.add(MongoQueryCriterions.eq(new GameDBField(GameDBField.GAME_CATEGORY_.getColumn() + categoryParam, ObjectFieldDBType.BOOLEAN), true));
                            }
                        }
                        if (param.startsWith(GAME_GENRE_THEME)) {
                            themeParam = param.replaceFirst(GAME_GENRE_THEME, "");
                            if (!StringUtil.isEmpty(themeParam) && StringUtil.isNumeric(themeParam)) {
                                mapMessage.put("themeCode", Integer.valueOf(themeParam));
                                mapMessage.put("checkThemeType", GameThemeType.getByCode(Integer.valueOf(themeParam)));
                                mongoQueryExpress.add(MongoQueryCriterions.eq(new GameDBField(GameDBField.GAME_THEME_.getColumn() + themeParam, ObjectFieldDBType.BOOLEAN), true));
                            }
                        }
                    }
                }
            }
            if (sort.equals("2")) {
                mongoQueryExpress.add(new MongoSort[]{new MongoSort(GameDBField.GAMERATE, MongoSortOrder.DESC),new MongoSort(GameDBField.GAMEPUBLICTIME, MongoSortOrder.DESC)});
            } else {
                mongoQueryExpress.add(new MongoSort[]{new MongoSort(GameDBField.GAMEPUBLICTIME, MongoSortOrder.DESC),new MongoSort(GameDBField.GAMERATE, MongoSortOrder.DESC)});
            }
            mongoQueryExpress.add(MongoQueryCriterions.eq(GameDBField.VALIDSTATUS, GameDbStatus.VALID.getCode()));
            PageRows<GameDB> gameDBPageRows = GameResourceServiceSngl.get().queryGameDbByPage(mongoQueryExpress, pagination);
            if (gameDBPageRows != null && !CollectionUtil.isEmpty(gameDBPageRows.getRows())) {
                List<GameCollectionDTO> list = new ArrayList<GameCollectionDTO>();
                Date date=new Date();
                for (GameDB gameDB : gameDBPageRows.getRows()) {
                	int googsNum=0;
					List<ActivityGoods> activityGoodsList = PointServiceSngl.get().queryActivityGoodsByGameId(gameDB.getGameDbId());
					for (ActivityGoods activityGoods : activityGoodsList) {
						boolean isStart=activityGoods.getStartTime().getTime()<date.getTime();
						boolean isNotEnd=activityGoods.getEndTime().getTime()>date.getTime();
						if (isStart && isNotEnd && activityGoods.getGoodsResetAmount()>0) {
							googsNum++;
						}
					}
					gameDB.setGiftSum(googsNum);
                    list.add(GameCollectionDTO.buildDTOFromGameDB(gameDB));
                }
                mapMessage.put("list", list);
                mapMessage.put("page", gameDBPageRows.getPage());
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/common/custompage", mapMessage);
        }
        return new ModelAndView("/views/jsp/collection/genre", mapMessage);
    }

    @RequestMapping(value = "/{gameid}")
    public ModelAndView detail(HttpServletRequest request, HttpServletResponse response,
                               @PathVariable(value = "gameid") String gameIdStr) {
        String reqUrl = request.getRequestURL().toString();
        try {
            if(!StringUtil.isNumeric(gameIdStr)){
                GameDB gameDB = GameResourceServiceSngl.get().getGameDBByAnotherName(gameIdStr);
                if(gameDB == null){
                    return new ModelAndView("redirect:/collection");
                }else {
                    return new ModelAndView("redirect:/collection/"+gameDB.getGameDbId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (request.getQueryString() != null && request.getQueryString().contains("tab_device=wap_pc")) {
            //手机访问电脑版
            return pcDetail(request, response, gameIdStr);
        } else if (reqUrl.startsWith("http://m.joyme.") || reqUrl.startsWith("https://m.joyme.")) {
            //m域名
            return wapDetail(request, response, gameIdStr);
        } else {
            return pcDetail(request, response, gameIdStr);
        }
    }

    private ModelAndView wapDetail(HttpServletRequest request, HttpServletResponse response, String gameIdStr) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            if (StringUtil.isEmpty(gameIdStr) || !StringUtil.isNumeric(gameIdStr)) {
                return new ModelAndView("/views/jsp/common/custompage", mapMessage);
            }
            long gameId = 0l;
            try {
                gameId = Long.valueOf(gameIdStr);
            } catch (NumberFormatException e) {
            }
            //游戏
            BasicDBObject queryDBObject = new BasicDBObject();
            queryDBObject.put(GameDBField.ID.getColumn(), gameId);
            GameDB gameDB = GameResourceServiceSngl.get().getGameDB(queryDBObject, false);
            if (gameDB == null) {
                return new ModelAndView("/views/jsp/common/custompage", mapMessage);
            }
            GameCollectionDTO gameCollectionDTO = GameCollectionDTO.buildDTOFromGameDB(gameDB);
            if (gameCollectionDTO == null) {
                return new ModelAndView("/views/jsp/common/custompage", mapMessage);
            }
//            //放入热门
//            ClientLineItemThread thread = new ClientLineItemThread(COLLECTION_GAME_HOT_MAJOR, gameDB);
//            
            sendOutEvent(COLLECTION_GAME_HOT_MAJOR, gameDB);

            Set<String> gamePicSet = new HashSet<String>();
            GamePicType gamePicType = gameDB.getGamePicType();
            if (!CollectionUtil.isEmpty(gameCollectionDTO.getGamePicSet())) {
                for (String pic : gameCollectionDTO.getGamePicSet()) {
                    if (GamePicType.TRANSVERSE.equals(gamePicType)) {
                        pic = rotateImage(pic);
                    }
                    gamePicSet.add(pic);
                }
            }
            //截图、简介只在最终页使用，所以不放入缓存
            gameCollectionDTO.setGamePicSet(gamePicSet);
            //
            if(!StringUtil.isEmpty(gameDB.getGameProfile())){
                Pattern pattern = Pattern.compile("(\r\n|\r|\n|\n\r)");
                //正则表达式的匹配一定要是这样，单个替换\r|\n的时候会错误
                Matcher matcher = pattern.matcher(gameDB.getGameProfile());
                gameCollectionDTO.setGameProfile(matcher.replaceAll("<br/>"));
            }
            mapMessage.put("game", gameCollectionDTO);
            //手机游戏
            GamePlatformType gamePlatformType = null;
            for(GamePlatformType platformType : gameCollectionDTO.getPlatformTypeSet()){
                if(platformType.equals(GamePlatformType.MOBILE)){
                    gamePlatformType = GamePlatformType.MOBILE;
                    break;
                }else {
                    gamePlatformType = platformType;
                }
            }
            mapMessage.put("gamePlatformType", gamePlatformType);
            if (GamePlatformType.MOBILE.equals(gamePlatformType)) {
                //游戏礼包
                List<ActivityGoods> activityGoodsList = PointServiceSngl.get().queryActivityGoodsByGameId(gameDB.getGameDbId());
                if (!CollectionUtil.isEmpty(activityGoodsList)) {
                    List<ActivityDTO> giftList = new ArrayList<ActivityDTO>();
                    Date date=new Date();
                    int loop = GIFT_PAGE_SIZE < activityGoodsList.size() ? GIFT_PAGE_SIZE : activityGoodsList.size();
                    for (int i = 0; i < loop; i++) {
						boolean isStart=activityGoodsList.get(i).getStartTime().getTime()<date.getTime();
						boolean isNotEnd=activityGoodsList.get(i).getEndTime().getTime()>date.getTime();
                    	if (isStart && isNotEnd) {//活动期内显示礼包
							giftList.add(giftMarketWebLogic.buildExchangeActivityDTO(activityGoodsList.get(i)));
                    	}
                    }
                    mapMessage.put("giftList", giftList);
                }
                //同类游戏
                MongoQueryExpress mongoQueryExpress = new MongoQueryExpress();
                mongoQueryExpress.add(MongoQueryCriterions.ne(GameDBField.ID, gameDB.getGameDbId()));
                mongoQueryExpress.add(MongoQueryCriterions.eq(new GameDBField(GameDBField.PLATFORMTYPE_.getColumn() + GamePlatformType.MOBILE.getCode(), ObjectFieldDBType.BOOLEAN), true));
                for (GameCategoryType categoryType : gameCollectionDTO.getCategoryTypeSet()) {
                    mongoQueryExpress.add(MongoQueryCriterions.eq(new GameDBField(GameDBField.GAME_CATEGORY_.getColumn() + categoryType.getCode(), ObjectFieldDBType.BOOLEAN), true));
                }
                mongoQueryExpress.add(MongoQueryCriterions.eq(GameDBField.VALIDSTATUS, GameDbStatus.VALID.getCode()));
                mongoQueryExpress.add(new MongoSort[]{new MongoSort(GameDBField.GAMEPUBLICTIME, MongoSortOrder.DESC)});

                Pagination categoryPage = new Pagination(CATEGORY_PAGE_SIZE, 1, CATEGORY_PAGE_SIZE);
                PageRows<GameDB> gameDBPageRows = GameResourceServiceSngl.get().queryGameDbByPage(mongoQueryExpress, categoryPage);
                if (gameDBPageRows != null && !CollectionUtil.isEmpty(gameDBPageRows.getRows())) {
                    List<GameCollectionDTO> list = new ArrayList<GameCollectionDTO>();
                    for (GameDB game : gameDBPageRows.getRows()) {
                        list.add(GameCollectionDTO.buildDTOFromGameDB(game));
                    }
                    mapMessage.put("categoryList", list);
                }
            }

            //视频
            Pagination videoPage = new Pagination(VIDEO_PAGE_SIZE, 1, VIDEO_PAGE_SIZE);
            PageRows<GameArchivesDTO> videoList = GameResourceServiceSngl.get().queryGameArchivesByCache(gameDB.getGameDbId(), ArchiveRelationType.GAME_RELATION, ArchiveContentType.VIDEO_ARCHIVE, videoPage);
            if (videoList != null && !CollectionUtil.isEmpty(videoList.getRows())) {
                mapMessage.put("videoList", videoList.getRows());
            }
            //资讯
            Pagination newsPage = new Pagination(NEWS_PAGE_SIZE, 1, NEWS_PAGE_SIZE);
            PageRows<GameArchivesDTO> newsList = GameResourceServiceSngl.get().queryGameArchivesByCache(gameDB.getGameDbId(), ArchiveRelationType.GAME_RELATION, ArchiveContentType.NEWS_ARCHIVE, newsPage);
            if (newsList != null && !CollectionUtil.isEmpty(newsList.getRows())) {
                mapMessage.put("newsList", newsList.getRows());
            }
            //攻略
            Pagination guidePage = new Pagination(GUIDE_PAGE_SIZE, 1, GUIDE_PAGE_SIZE);
            PageRows<GameArchivesDTO> guideList = GameResourceServiceSngl.get().queryGameArchivesByCache(gameDB.getGameDbId(), ArchiveRelationType.GAME_RELATION, ArchiveContentType.GUIDE_ARCHIVE, guidePage);
            if (guideList != null && !CollectionUtil.isEmpty(guideList.getRows())) {
                mapMessage.put("guideList", guideList.getRows());
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/common/custompage-wap", mapMessage);
        }
        return new ModelAndView("/views/jsp/collection/detail-m", mapMessage);
    }

    private ModelAndView pcDetail(HttpServletRequest request, HttpServletResponse response, String gameIdStr) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            if (StringUtil.isEmpty(gameIdStr) || !StringUtil.isNumeric(gameIdStr)) {
                return new ModelAndView("/views/jsp/common/custompage", mapMessage);
            }
            long gameId = 0l;
            try {
                gameId = Long.valueOf(gameIdStr);
            } catch (NumberFormatException e) {
            }
            //游戏
            BasicDBObject queryDBObject = new BasicDBObject();
            queryDBObject.put(GameDBField.ID.getColumn(), gameId);
            GameDB gameDB = GameResourceServiceSngl.get().getGameDB(queryDBObject, false);
            if (gameDB == null) {
                return new ModelAndView("/views/jsp/common/custompage", mapMessage);
            }
            GameCollectionDTO gameCollectionDTO = GameCollectionDTO.buildDTOFromGameDB(gameDB);
            if (gameCollectionDTO == null) {
                return new ModelAndView("/views/jsp/common/custompage", mapMessage);
            }
            //放入 热门
//            ClientLineItemThread thread = new ClientLineItemThread(COLLECTION_GAME_HOT_MAJOR, gameDB);
//            thread.start();
            sendOutEvent(COLLECTION_GAME_HOT_MAJOR, gameDB);

            Set<String> gamePicSet = new HashSet<String>();
            GamePicType gamePicType = gameDB.getGamePicType();
            if (!CollectionUtil.isEmpty(gameCollectionDTO.getGamePicSet())) {
                for (String pic : gameCollectionDTO.getGamePicSet()) {
                    if (GamePicType.TRANSVERSE.equals(gamePicType)) {
                        pic = rotateImage(pic);
                    }
                    gamePicSet.add(pic);

                }
            }
            //截图、简介只在最终页使用，所以不放入缓存
            gameCollectionDTO.setGamePicSet(gamePicSet);
            //
            if(!StringUtil.isEmpty(gameDB.getGameProfile())){
                Pattern pattern = Pattern.compile("(\r\n|\r|\n|\n\r)");
                //正则表达式的匹配一定要是这样，单个替换\r|\n的时候会错误
                Matcher matcher = pattern.matcher(gameDB.getGameProfile());
                gameCollectionDTO.setGameProfile(matcher.replaceAll("<br/>"));
            }

            mapMessage.put("game", gameCollectionDTO);
            //手机游戏
            GamePlatformType gamePlatformType = null;
            for(GamePlatformType platformType : gameCollectionDTO.getPlatformTypeSet()){
                if(platformType.equals(GamePlatformType.MOBILE)){
                    gamePlatformType = GamePlatformType.MOBILE;
                    break;
                }else {
                    gamePlatformType = platformType;
                }
            }
            mapMessage.put("gamePlatformType", gamePlatformType);
            if (GamePlatformType.MOBILE.equals(gamePlatformType)) {
                //游戏礼包
                List<ActivityGoods> activityGoodsList = PointServiceSngl.get().queryActivityGoodsByGameId(gameDB.getGameDbId());
                if (!CollectionUtil.isEmpty(activityGoodsList)) {
                    List<ActivityDTO> giftList = new ArrayList<ActivityDTO>();
                    Date date=new Date();
                    int loop = GIFT_PAGE_SIZE < activityGoodsList.size() ? GIFT_PAGE_SIZE : activityGoodsList.size();
                    for (int i = 0; i < loop; i++) {
						boolean isStart=activityGoodsList.get(i).getStartTime().getTime()<date.getTime();
						boolean isNotEnd=activityGoodsList.get(i).getEndTime().getTime()>date.getTime();
                    	if (isStart && isNotEnd) {//活动期内显示礼包
							giftList.add(giftMarketWebLogic.buildExchangeActivityDTO(activityGoodsList.get(i)));
                    	}
                    }
                    mapMessage.put("giftList", giftList);
                }
                //同类游戏
                MongoQueryExpress mongoQueryExpress = new MongoQueryExpress();
                mongoQueryExpress.add(MongoQueryCriterions.ne(GameDBField.ID, gameDB.getGameDbId()));
                mongoQueryExpress.add(MongoQueryCriterions.eq(new GameDBField(GameDBField.PLATFORMTYPE_.getColumn() + GamePlatformType.MOBILE.getCode(), ObjectFieldDBType.BOOLEAN), true));
                for (GameCategoryType categoryType : gameCollectionDTO.getCategoryTypeSet()) {
                    mongoQueryExpress.add(MongoQueryCriterions.eq(new GameDBField(GameDBField.GAME_CATEGORY_.getColumn() + categoryType.getCode(), ObjectFieldDBType.BOOLEAN), true));
                }
                mongoQueryExpress.add(MongoQueryCriterions.eq(GameDBField.VALIDSTATUS, GameDbStatus.VALID.getCode()));
                mongoQueryExpress.add(new MongoSort[]{new MongoSort(GameDBField.GAMEPUBLICTIME, MongoSortOrder.DESC)});

                Pagination categoryPage = new Pagination(CATEGORY_PAGE_SIZE, 1, CATEGORY_PAGE_SIZE);
                PageRows<GameDB> gameDBPageRows = GameResourceServiceSngl.get().queryGameDbByPage(mongoQueryExpress, categoryPage);
                if (gameDBPageRows != null && !CollectionUtil.isEmpty(gameDBPageRows.getRows())) {
                    List<GameCollectionDTO> list = new ArrayList<GameCollectionDTO>();
                    for (GameDB game : gameDBPageRows.getRows()) {
                        list.add(GameCollectionDTO.buildDTOFromGameDB(game));
                    }
                    mapMessage.put("categoryList", list);
                }
            } else {
                Set<String> lineCodeSet = new HashSet<String>();
                lineCodeSet.add(COLLECTION_GAME_NEWS);
                lineCodeSet.add(COLLECTION_GAME_HOT_MAJOR);
                Pagination page = new Pagination(HOT_GAME_PAGE_SIZE * 1, 1, HOT_GAME_PAGE_SIZE);
                Map<String, List<GameCollectionDTO>> mapList = GameResourceServiceSngl.get().getGameCollectionListCache(ClientLineType.GAME_COLLECTION, AppPlatform.WEB, lineCodeSet, page);
                if (!CollectionUtil.isEmpty(mapList)) {
                    //最新入库
                    mapMessage.put("newList", mapList.get(String.valueOf(ClientItemType.GAME_NEWS.getCode())));
                    //热门游戏
                    mapMessage.put("hotList", mapList.get(String.valueOf(ClientItemType.GAME_HOT_MAJOR.getCode())));
                }
                
                String pcConfigInfo=gameDB.getGamePCConfigurationInfo();
                if (!StringUtil.isEmpty(pcConfigInfo)) {
					JSONObject jsonObject=JSONObject.fromObject(pcConfigInfo);
					mapMessage.put("pcConfigInfo1", jsonObject.get("1"));
					mapMessage.put("pcConfigInfo2", jsonObject.get("2"));
				}
            }

            //视频
            Pagination videoPage = new Pagination(VIDEO_PAGE_SIZE, 1, VIDEO_PAGE_SIZE);
            PageRows<GameArchivesDTO> videoList = GameResourceServiceSngl.get().queryGameArchivesByCache(gameDB.getGameDbId(), ArchiveRelationType.GAME_RELATION, ArchiveContentType.VIDEO_ARCHIVE, videoPage);
            if (videoList != null && !CollectionUtil.isEmpty(videoList.getRows())) {
                mapMessage.put("videoList", videoList.getRows());
            }
            //资讯
            Pagination newsPage = new Pagination(NEWS_PAGE_SIZE, 1, NEWS_PAGE_SIZE);
            PageRows<GameArchivesDTO> newsList = GameResourceServiceSngl.get().queryGameArchivesByCache(gameDB.getGameDbId(), ArchiveRelationType.GAME_RELATION, ArchiveContentType.NEWS_ARCHIVE, newsPage);
            if (newsList != null && !CollectionUtil.isEmpty(newsList.getRows())) {
                mapMessage.put("newsList", newsList.getRows());
            }
            //攻略
            Pagination guidePage = new Pagination(GUIDE_PAGE_SIZE, 1, GUIDE_PAGE_SIZE);
            PageRows<GameArchivesDTO> guideList = GameResourceServiceSngl.get().queryGameArchivesByCache(gameDB.getGameDbId(), ArchiveRelationType.GAME_RELATION, ArchiveContentType.GUIDE_ARCHIVE, guidePage);
            if (guideList != null && !CollectionUtil.isEmpty(guideList.getRows())) {
                mapMessage.put("guideList", guideList.getRows());
            }
            mapMessage.put("domain", CommentDomain.GAME_DETIAL);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/common/custompage", mapMessage);
        }
        return new ModelAndView("/views/jsp/collection/detail", mapMessage);
    }

    @RequestMapping(value = "/{gameid}/{archivetype}")
    public ModelAndView archiveList(HttpServletRequest request, HttpServletResponse response,
                                    @PathVariable(value = "gameid") String gameIdStr,
                                    @PathVariable(value = "archivetype") String archivetype,
                                    @RequestParam(value = "p", required = false, defaultValue = "1") String p) {
        String reqUrl = request.getRequestURL().toString();
        if (request.getQueryString() != null && request.getQueryString().contains("tab_device=wap_pc")) {
            //手机访问电脑版
            return pcArchives(request, response, gameIdStr, archivetype, p);
        } else if (reqUrl.startsWith("http://m.joyme.") || reqUrl.startsWith("https://m.joyme.")) {
            //m域名
            return wapArchives(request, response, gameIdStr, archivetype, p);
        } else {
            return pcArchives(request, response, gameIdStr, archivetype, p);
        }
    }

    private ModelAndView wapArchives(HttpServletRequest request, HttpServletResponse response, String gameIdStr, String archivetype, String p) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            if (StringUtil.isEmpty(gameIdStr) || !StringUtil.isNumeric(gameIdStr)) {
                return new ModelAndView("/views/jsp/common/custompage-wap", mapMessage);
            }
            if (StringUtil.isEmpty(archivetype)) {
                return new ModelAndView("redirect:/collection/" + gameIdStr);
            }
            long gameId = 0l;
            try {
                gameId = Long.valueOf(gameIdStr);
            } catch (NumberFormatException e) {
            }

            int cp = 1;
            try {
                cp = Integer.parseInt(p);
            } catch (NumberFormatException e) {
            }

            ArchiveContentType contentType;
            Pagination page;
            if (archivetype.equals("news")) {
                contentType = ArchiveContentType.NEWS_ARCHIVE;
                page = new Pagination(HOT_GAME_PAGE_SIZE * cp, cp, HOT_GAME_PAGE_SIZE);
            } else if (archivetype.equals("videos")) {
                contentType = ArchiveContentType.VIDEO_ARCHIVE;
                page = new Pagination(ARCHIVES_PAGE_SIZE * cp, cp, ARCHIVES_PAGE_SIZE);
            } else if (archivetype.equals("guides")) {
                contentType = ArchiveContentType.GUIDE_ARCHIVE;
                page = new Pagination(HOT_GAME_PAGE_SIZE * cp, cp, HOT_GAME_PAGE_SIZE);
            } else {
                return new ModelAndView("redirect:/collection/" + gameIdStr);
            }
            mapMessage.put("contentType", contentType);

            //游戏
            BasicDBObject queryDBObject = new BasicDBObject();
            queryDBObject.put(GameDBField.ID.getColumn(), gameId);
            GameDB gameDB = GameResourceServiceSngl.get().getGameDB(queryDBObject, false);
            if (gameDB == null) {
                return new ModelAndView("/views/jsp/common/custompage-wap", mapMessage);
            }
            GameCollectionDTO gameCollectionDTO = GameCollectionDTO.buildDTOFromGameDB(gameDB);
            if (gameCollectionDTO == null) {
                return new ModelAndView("/views/jsp/common/custompage-wap", mapMessage);
            }
            mapMessage.put("game", gameCollectionDTO);

            PageRows<GameArchivesDTO> archivePageRows = GameResourceServiceSngl.get().queryGameArchivesByCache(gameId, ArchiveRelationType.GAME_RELATION, contentType, page);
            if (archivePageRows != null && !CollectionUtil.isEmpty(archivePageRows.getRows())) {
                mapMessage.put("list", archivePageRows.getRows());
                mapMessage.put("page", archivePageRows.getPage());
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/common/custompage-wap", mapMessage);
        }
        return new ModelAndView("/views/jsp/collection/archives-m", mapMessage);
    }

    private ModelAndView pcArchives(HttpServletRequest request, HttpServletResponse response, String gameIdStr, String archivetype, String p) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            if (StringUtil.isEmpty(gameIdStr) || !StringUtil.isNumeric(gameIdStr)) {
                return new ModelAndView("/views/jsp/common/custompage", mapMessage);
            }
            if (StringUtil.isEmpty(archivetype)) {
                return new ModelAndView("redirect:/collection/" + gameIdStr);
            }
            long gameId = 0l;
            try {
                gameId = Long.valueOf(gameIdStr);
            } catch (NumberFormatException e) {
            }

            int cp = 1;
            try {
                cp = Integer.parseInt(p);
            } catch (NumberFormatException e) {
            }

            ArchiveContentType contentType;
            Pagination page;
            if (archivetype.equals("news")) {
                contentType = ArchiveContentType.NEWS_ARCHIVE;
                page = new Pagination(HOT_GAME_PAGE_SIZE * cp, cp, HOT_GAME_PAGE_SIZE);
            } else if (archivetype.equals("videos")) {
                contentType = ArchiveContentType.VIDEO_ARCHIVE;
                page = new Pagination(ARCHIVES_PAGE_SIZE * cp, cp, ARCHIVES_PAGE_SIZE);
            } else if (archivetype.equals("guides")) {
                contentType = ArchiveContentType.GUIDE_ARCHIVE;
                page = new Pagination(HOT_GAME_PAGE_SIZE * cp, cp, HOT_GAME_PAGE_SIZE);
            } else {
                return new ModelAndView("redirect:/collection/" + gameIdStr);
            }
            mapMessage.put("contentType", contentType);

            //游戏
            BasicDBObject queryDBObject = new BasicDBObject();
            queryDBObject.put(GameDBField.ID.getColumn(), gameId);
            GameDB gameDB = GameResourceServiceSngl.get().getGameDB(queryDBObject, false);
            if (gameDB == null) {
                return new ModelAndView("/views/jsp/common/custompage", mapMessage);
            }
            GameCollectionDTO gameCollectionDTO = GameCollectionDTO.buildDTOFromGameDB(gameDB);
            if (gameCollectionDTO == null) {
                return new ModelAndView("/views/jsp/common/custompage", mapMessage);
            }
            mapMessage.put("game", gameCollectionDTO);

            PageRows<GameArchivesDTO> archivePageRows = GameResourceServiceSngl.get().queryGameArchivesByCache(gameId, ArchiveRelationType.GAME_RELATION, contentType, page);
            if (archivePageRows != null && !CollectionUtil.isEmpty(archivePageRows.getRows())) {
                mapMessage.put("list", archivePageRows.getRows());
                mapMessage.put("page", archivePageRows.getPage());
            }

            List<GameArchivesDTO> newsList = new ArrayList<GameArchivesDTO>();
            List<GameArchivesDTO> videoList = new ArrayList<GameArchivesDTO>();
            List<GameArchivesDTO> guideList = new ArrayList<GameArchivesDTO>();

            String cacheValue = redisManager.get(HOT_ARCHIVE_CACHE);
            if (!StringUtil.isEmpty(cacheValue)) {
                JSONObject jsonMap = JSONObject.fromObject(cacheValue);
                if (jsonMap != null) {
                    JSONArray jsonNewsArr = jsonMap.containsKey(String.valueOf(ArchiveContentType.NEWS_ARCHIVE.getCode())) ? jsonMap.getJSONArray(String.valueOf(ArchiveContentType.NEWS_ARCHIVE.getCode())) : null;
                    if (jsonNewsArr != null && jsonNewsArr.isArray() && !jsonNewsArr.isEmpty()) {
                        for (Object dtoObj : jsonNewsArr) {
                        	if(dtoObj!=null){
                                GameArchivesDTO gameArchivesDTO = GameArchivesDTO.parse(dtoObj);
                                if (gameArchivesDTO != null) {
                                    newsList.add(gameArchivesDTO);
                                }
                        	}
                        }
                    }
                    JSONArray jsonVideoArr = jsonMap.containsKey(String.valueOf(ArchiveContentType.VIDEO_ARCHIVE.getCode())) ? jsonMap.getJSONArray(String.valueOf(ArchiveContentType.VIDEO_ARCHIVE.getCode())) : null;
                    if (jsonVideoArr != null && jsonVideoArr.isArray() && !jsonVideoArr.isEmpty()) {
                        for (Object dtoObj : jsonVideoArr) {
                        	if(dtoObj!=null){
                                GameArchivesDTO gameArchivesDTO = GameArchivesDTO.parse(dtoObj);
                                if (gameArchivesDTO != null) {
                                    videoList.add(gameArchivesDTO);
                                }           		
                        	}
                        }
                    }
                    JSONArray jsonGuideArr = jsonMap.containsKey(String.valueOf(ArchiveContentType.GUIDE_ARCHIVE.getCode())) ? jsonMap.getJSONArray(String.valueOf(ArchiveContentType.GUIDE_ARCHIVE.getCode())) : null;
                    if (jsonGuideArr != null && jsonGuideArr.isArray() && !jsonGuideArr.isEmpty()) {
                        for (Object dtoObj : jsonGuideArr) {
                        	if (dtoObj!=null) {
                                GameArchivesDTO gameArchivesDTO = GameArchivesDTO.parse(dtoObj);
                                if (gameArchivesDTO != null) {
                                    guideList.add(gameArchivesDTO);
                                }				
							}
                        }
                    }
                }
            }
            if (CollectionUtil.isEmpty(newsList) && CollectionUtil.isEmpty(videoList) && CollectionUtil.isEmpty(guideList)) {
                HttpClientManager httpClientManager = new HttpClientManager();
                HttpResult httpResult = httpClientManager.get("http://webcache." + WebappConfig.get().getDomain() + "/json/pagestat/hotlist.do", new HttpParameter[]{}, "UTF-8");
                //HttpResult httpResult = httpClientManager.get("http://webcache.joyme.alpha/json/pagestat/hotlist.do", new HttpParameter[]{}, "UTF-8");
                if (httpResult != null && httpResult.getReponseCode() == HttpClientManager.OK && httpResult.getResult() != null) {
                    JSONObject jsonObject = JSONObject.fromObject(httpResult.getResult());
                    if (jsonObject != null) {
                        JSONObject result = jsonObject.getJSONObject("result");
                        if (result != null) {
                            if (result.containsKey(String.valueOf(ArchiveContentType.NEWS_ARCHIVE.getCode()))) {
                                JSONArray newsArr = result.getJSONArray(String.valueOf(ArchiveContentType.NEWS_ARCHIVE.getCode()));
                                if (newsArr != null && newsArr.isArray() && !newsArr.isEmpty()) {
                                	Set<Integer> archiveIds=new HashSet<Integer>();
                                    for (Object obj : newsArr) {
                                        String archiveId = String.valueOf(obj);
                                        archiveIds.add(Integer.parseInt(archiveId));
                                    }
//                                  List<TagDedearchives> list=JoymeAppServiceSngl.get().queryTagDedearchivesByCache(gameId, ArchiveRelationType.GAME_RELATION, archiveIds);
//                                  for (TagDedearchives tagDedearchives : list) {
//                                	if (null != tagDedearchives) {
//                                        GameArchivesDTO gameArchivesDTO = GameArchivesDTO.buildDTOFromTagDedeArchives(tagDedearchives);
//                                        if (gameArchivesDTO != null) {
//                                            newsList.add(gameArchivesDTO);
//                                        }
//									}
//								}   
                                    Map<Integer, Archive> archiveMap=JoymeAppServiceSngl.get().queryArchiveMapByIds(archiveIds);
                                    if(!CollectionUtil.isEmpty(archiveMap)){
                                    	Set<Integer> archTypeIds=new HashSet<Integer>();
                                    	for (Integer archiveId:archiveMap.keySet()) {
											archTypeIds.add(archiveMap.get(archiveId).getTypeid());
										}
                                    	Map<Integer, DedeArctype> dedeArcTypeMap=JoymeAppServiceSngl.get().queryDedeArctypeMapByTypeId(archTypeIds);
                                    	
                                    	for (Integer archiveId:archiveMap.keySet()) {
                                    		TagDedearchives tagDedearchives=new TagDedearchives();
                                    		Archive archive=archiveMap.get(archiveId);
                                    		DedeArctype arctype=dedeArcTypeMap.get(archive.getTypeid());
                                            String dedearchiv_url = "";
                                            if (arctype != null) {
                                                dedearchiv_url = getArticleUrl(arctype, archive.getCreateTime().getTime(), archive.getArchiveId(), ArchiveContentType.NEWS_ARCHIVE);
                                            }
                                            tagDedearchives.setTagid(gameId);
                                            tagDedearchives.setDede_archives_id(String.valueOf(archive.getArchiveId()));
                                            tagDedearchives.setDede_archives_title(archive.getTitle());
                                            tagDedearchives.setDede_archives_url(dedearchiv_url);
                                            tagDedearchives.setDede_archives_description(archive.getDesc());
                                            tagDedearchives.setDede_archives_litpic(archive.getIcon());
                                            tagDedearchives.setDede_archives_pubdate(archive.getCreateTime().getTime());
                                            tagDedearchives.setArchiveContentType(contentType);
                                            
                                            GameArchivesDTO gameArchivesDTO = GameArchivesDTO.buildDTOFromTagDedeArchives(tagDedearchives);
                                            if (gameArchivesDTO != null) {
                                              newsList.add(gameArchivesDTO);
                                            }
                                    	}
                                    }
                                }
                            }
                            if (result.containsKey(String.valueOf(ArchiveContentType.VIDEO_ARCHIVE.getCode()))) {
                                JSONArray videoArr = result.getJSONArray(String.valueOf(ArchiveContentType.VIDEO_ARCHIVE.getCode()));
                                if (videoArr != null && videoArr.isArray() && !videoArr.isEmpty()) {
                                	Set<Integer> archiveIds=new HashSet<Integer>();
                                    for (Object obj : videoArr) {
                                        String archiveId = String.valueOf(obj);
                                        archiveIds.add(Integer.parseInt(archiveId));
                                    }
//                                    List<TagDedearchives> list=JoymeAppServiceSngl.get().queryTagDedearchivesByCache(gameId, ArchiveRelationType.GAME_RELATION, archiveIds);
//                                    if(!CollectionUtil.isEmpty(list)){
//                                        for (TagDedearchives tagDedearchives : list) {
//                                        	if (null!=tagDedearchives) {
//                                                GameArchivesDTO gameArchivesDTO = GameArchivesDTO.buildDTOFromTagDedeArchives(tagDedearchives);
//                                                if (gameArchivesDTO != null) {
//                                                    newsList.add(gameArchivesDTO);
//                                                }		
//											}
//    									}                              	
//                                    }
                                    Map<Integer, Archive> archiveMap=JoymeAppServiceSngl.get().queryArchiveMapByIds(archiveIds);
                                    if(!CollectionUtil.isEmpty(archiveMap)){
                                    	Set<Integer> archTypeIds=new HashSet<Integer>();
                                    	for (Integer archiveId:archiveMap.keySet()) {
											archTypeIds.add(archiveMap.get(archiveId).getTypeid());
										}
                                    	Map<Integer, DedeArctype> dedeArcTypeMap=JoymeAppServiceSngl.get().queryDedeArctypeMapByTypeId(archTypeIds);
                                    	
                                    	for (Integer archiveId:archiveMap.keySet()) {
                                    		TagDedearchives tagDedearchives=new TagDedearchives();
                                    		Archive archive=archiveMap.get(archiveId);
                                    		DedeArctype arctype=dedeArcTypeMap.get(archive.getTypeid());
                                            String dedearchiv_url = "";
                                            if (arctype != null) {
                                                dedearchiv_url = getArticleUrl(arctype, archive.getCreateTime().getTime(), archive.getArchiveId(), ArchiveContentType.GUIDE_ARCHIVE);
                                            }
                                            tagDedearchives.setTagid(gameId);
                                            tagDedearchives.setDede_archives_id(String.valueOf(archive.getArchiveId()));
                                            tagDedearchives.setDede_archives_title(archive.getTitle());
                                            tagDedearchives.setDede_archives_url(dedearchiv_url);
                                            tagDedearchives.setDede_archives_description(archive.getDesc());
                                            tagDedearchives.setDede_archives_litpic(archive.getIcon());
                                            tagDedearchives.setDede_archives_pubdate(archive.getCreateTime().getTime());
                                            tagDedearchives.setArchiveContentType(contentType);
                                            
                                            GameArchivesDTO gameArchivesDTO = GameArchivesDTO.buildDTOFromTagDedeArchives(tagDedearchives);
                                            if (gameArchivesDTO != null) {
                                              videoList.add(gameArchivesDTO);
                                            }
                                    	}
                                    }
                                }
                            }
                            if (result.containsKey(String.valueOf(ArchiveContentType.GUIDE_ARCHIVE.getCode()))) {
                                JSONArray guideArr = result.getJSONArray(String.valueOf(ArchiveContentType.GUIDE_ARCHIVE.getCode()));
                                if (guideArr != null && guideArr.isArray() && !guideArr.isEmpty()) {
                                	Set<Integer> archiveIds=new HashSet<Integer>();
                                    for (Object obj : guideArr) {
                                        String archiveId = String.valueOf(obj);
                                        archiveIds.add(Integer.parseInt(archiveId));
                                    }
//                                    List<TagDedearchives> list=JoymeAppServiceSngl.get().queryTagDedearchivesByCache(gameId, ArchiveRelationType.GAME_RELATION, archiveIds);
//                                    if(!CollectionUtil.isEmpty(list)){
//                                        for (TagDedearchives tagDedearchives : list) {
//                                        	if (null!=tagDedearchives) {
//                                                GameArchivesDTO gameArchivesDTO = GameArchivesDTO.buildDTOFromTagDedeArchives(tagDedearchives);
//                                                if (gameArchivesDTO != null) {
//                                                    newsList.add(gameArchivesDTO);
//                                                }			
//											}
//    									}                              	
//                                    }
                                    Map<Integer, Archive> archiveMap=JoymeAppServiceSngl.get().queryArchiveMapByIds(archiveIds);
                                    if(!CollectionUtil.isEmpty(archiveMap)){
                                    	Set<Integer> archTypeIds=new HashSet<Integer>();
                                    	for (Integer archiveId:archiveMap.keySet()) {
											archTypeIds.add(archiveMap.get(archiveId).getTypeid());
										}
                                    	Map<Integer, DedeArctype> dedeArcTypeMap=JoymeAppServiceSngl.get().queryDedeArctypeMapByTypeId(archTypeIds);
                                    	
                                    	for (Integer archiveId:archiveMap.keySet()) {
                                    		TagDedearchives tagDedearchives=new TagDedearchives();
                                    		Archive archive=archiveMap.get(archiveId);
                                    		DedeArctype arctype=dedeArcTypeMap.get(archive.getTypeid());
                                            String dedearchiv_url = "";
                                            if (arctype != null) {
                                                dedearchiv_url = getArticleUrl(arctype, archive.getCreateTime().getTime(), archive.getArchiveId(), ArchiveContentType.VIDEO_ARCHIVE);
                                            }
                                            tagDedearchives.setTagid(gameId);
                                            tagDedearchives.setDede_archives_id(String.valueOf(archive.getArchiveId()));
                                            tagDedearchives.setDede_archives_title(archive.getTitle());
                                            tagDedearchives.setDede_archives_url(dedearchiv_url);
                                            tagDedearchives.setDede_archives_description(archive.getDesc());
                                            tagDedearchives.setDede_archives_litpic(archive.getIcon());
                                            tagDedearchives.setDede_archives_pubdate(archive.getCreateTime().getTime());
                                            tagDedearchives.setArchiveContentType(contentType);
                                            
                                            GameArchivesDTO gameArchivesDTO = GameArchivesDTO.buildDTOFromTagDedeArchives(tagDedearchives);
                                            if (gameArchivesDTO != null) {
                                              guideList.add(gameArchivesDTO);
                                            }
                                    	}
                                    }
                                }
                            }
                        }
                    }
                }
                Map<String, List<GameArchivesDTO>> map = new HashMap<String, List<GameArchivesDTO>>();
                map.put(String.valueOf(ArchiveContentType.GUIDE_ARCHIVE.getCode()), guideList);
                map.put(String.valueOf(ArchiveContentType.NEWS_ARCHIVE.getCode()), newsList);
                map.put(String.valueOf(ArchiveContentType.VIDEO_ARCHIVE.getCode()), videoList);
                JSONObject jsonObject = JSONObject.fromObject(map);
                redisManager.setSec(HOT_ARCHIVE_CACHE, jsonObject.toString(), 60 * 30);
            }
            mapMessage.put("newsList", newsList);
            mapMessage.put("videoList", videoList);
            mapMessage.put("guideList", guideList);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/common/custompage", mapMessage);
        }
        return new ModelAndView("/views/jsp/collection/archives", mapMessage);
    }

    /**
     * 依赖于视频文章的  vip/v/的处理规则，规则不能变
     *
     * @param arctype
     * @param time
     * @param archiveId
     * @param contentType
     * @return
     */
    private String getArticleUrl(DedeArctype arctype, long time, int archiveId, ArchiveContentType contentType) {
        String host = "www." + WebappConfig.get().getDomain();
        String typedir = arctype.getTypedir();//\{cmspath\}/vip/v/zzmj
        typedir = typedir.replaceAll("\\{cmspath\\}", "");
        if (typedir.startsWith("/vip/")) {
            String key = typedir.replaceAll("/vip/", "");
            if (key.indexOf("/") > 0) {
                key = key.substring(0, key.indexOf("/"));
            }
            host = key + "." + WebappConfig.get().getDomain();
            typedir = typedir.replaceAll("/vip/"+key, "");
        }
        String namerule = arctype.getNamerule();
        // {typedir}/{Y}{M}{D}/{aid}.html
        namerule = namerule.replaceAll("\\{typedir\\}", typedir);
        String date = DateUtil.convert2String(time, DateUtil.YYYYMMDD_FORMAT);
        String year = date.substring(0, 4);
        String month = date.substring(4, 6);
        String day = date.substring(6, 8);
        namerule = namerule.replaceAll("\\{Y\\}", year);
        namerule = namerule.replaceAll("\\{M\\}", month);
        namerule = namerule.replaceAll("\\{D\\}", day);
        namerule = namerule.replaceAll("\\{aid\\}", String.valueOf(archiveId));

        int position = namerule.lastIndexOf("/");
        String[] paths = new String[2];
        if (position > 0) {
            paths[0] = namerule.substring(0, position);
            paths[1] = namerule.substring(position + 1, namerule.length());
        }
        String returnURL = "http://" + host + paths[0] + "/" + paths[1];
        return returnURL;
    }

//    //todo 没有必要用个线程.发送一个事件给serv端然后在serv端去做
//    class ClientLineItemThread extends Thread{
//        private String code;
//        private GameDB gameDB;
//
//        public ClientLineItemThread(String code, GameDB gameDB) {
//            this.code = code;
//            this.gameDB = gameDB;
//        }
//
//        @Override
//        public void run() {
//            try {
//                GameResourceServiceSngl.get().incrGameCollectionListCache(code, 1, gameDB);
//                ClientLine clientLine = JoymeAppServiceSngl.get().getClientLineByCode(code);
//                if (clientLine != null) {
//                    ClientLineItem clientLineItem = JoymeAppServiceSngl.get().getClientLineItem(new QueryExpress()
//                            .add(QueryCriterions.eq(ClientLineItemField.LINE_ID, clientLine.getLineId()))
//                            .add(QueryCriterions.eq(ClientLineItemField.DIRECT_ID, String.valueOf(gameDB.getGameDbId()))));
//                    if(clientLineItem == null){
//                        clientLineItem = new ClientLineItem();
//                        clientLineItem.setDirectId(String.valueOf(gameDB.getGameDbId()));
//                        clientLineItem.setLineId(clientLine.getLineId());
//                        clientLineItem.setItemDomain(ClientItemDomain.GAME);
//                        clientLineItem.setItemType(clientLine.getItemType());
//                        clientLineItem.setPicUrl(gameDB.getGameIcon());
//                        clientLineItem.setTitle(gameDB.getGameName());
//                        clientLineItem.setValidStatus(ValidStatus.VALID);
//                        clientLineItem.setDisplayOrder(gameDB.getPvSum());
//                        clientLineItem.setItemCreateDate(new Date());
//                        JoymeAppServiceSngl.get().createClientLineItem(clientLineItem);
//                    }else if(!clientLineItem.getValidStatus().equals(ValidStatus.VALID)){
//                        UpdateExpress updateExpress = new UpdateExpress();
//                        updateExpress.set(ClientLineItemField.VALID_STATUS, ValidStatus.VALID.getCode());
//                        JoymeAppServiceSngl.get().modifyClientLineItem(updateExpress, clientLineItem.getItemId());
//                    }
//                }
//            } catch (ServiceException e) {
//            }
//        }
//    }

}
