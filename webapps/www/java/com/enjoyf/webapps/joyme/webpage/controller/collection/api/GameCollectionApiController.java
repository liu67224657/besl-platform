package com.enjoyf.webapps.joyme.webpage.controller.collection.api;

import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.gameres.gamedb.*;
import com.enjoyf.platform.service.gameres.gamedb.collection.GameArchivesDTO;
import com.enjoyf.platform.service.gameres.gamedb.collection.GameCollectionDTO;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.joymeapp.gameclient.ArchiveContentType;
import com.enjoyf.platform.service.joymeapp.gameclient.ArchiveRelationType;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryCriterions;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryExpress;
import com.enjoyf.platform.util.sql.mongodb.MongoSort;
import com.enjoyf.platform.util.sql.mongodb.MongoSortOrder;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.webapps.joyme.webpage.controller.collection.AbstractGameCollectionController;
import com.mongodb.BasicDBObject;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by zhitaoshi on 2015/6/16.
 */
@Controller
@RequestMapping(value = "/collection/api")
public class GameCollectionApiController extends AbstractGameCollectionController {
    private static final int GENRE_PAGE_SIZE = 18;
    private static final int VIDEO_PAGE_SIZE = 3;
    private static final int CONTENT_PAGE_SIZE = 10;
    private static final int GAMES_PAGE_SIZE = 12;

    private static final String GAME_HOT_CATEGORY_LINE_CODE = "collection_game_category_";

    private static Set<AppPlatform> platformSet = new HashSet<AppPlatform>();

    static {
        platformSet.add(AppPlatform.ANDROID);
        platformSet.add(AppPlatform.IOS);
    }

    @ResponseBody
    @RequestMapping(value = "/likegame")
    public String likeGame(HttpServletRequest request, HttpServletResponse response,
                           @RequestParam(value = "gameid", required = false) String gameIdStr) {
        String callback = request.getParameter("callback");
        JSONObject jsonObject = new JSONObject();
        if (StringUtil.isEmpty(gameIdStr)) {
            if (StringUtil.isEmpty(callback)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            } else {
                return callback + "([" + ResultCodeConstants.PARAM_EMPTY.getJsonString() + "])";
            }
        }

        long gameId = 0l;
        try {
            gameId = Long.parseLong(gameIdStr);
        } catch (NumberFormatException e) {
        }

        if (gameId == 0l) {
            if (StringUtil.isEmpty(callback)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            } else {
                return callback + "([" + ResultCodeConstants.PARAM_EMPTY.getJsonString() + "])";
            }
        }

        try {
            GameDB gameDB = GameResourceServiceSngl.get().getGameDB(new BasicDBObject("_id", gameId), false);
            if (gameDB == null) {
                if (StringUtil.isEmpty(callback)) {
                    return ResultCodeConstants.GAMEDB_GAME_NOTEXISTS.getJsonString();
                } else {
                    return callback + "([" + ResultCodeConstants.GAMEDB_GAME_NOTEXISTS.getJsonString() + "])";
                }
            }
            String anotherName=gameDB.getAnotherName()==null?"":gameDB.getAnotherName();
            GameResourceServiceSngl.get().incUserLikeGame(0l, gameId, anotherName, GameDBField.FAVOR_SUM.getColumn());
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
            if (StringUtil.isEmpty(callback)) {
                return ResultCodeConstants.ERROR.getJsonString();
            } else {
                return callback + "([" + ResultCodeConstants.ERROR.getJsonString() + "])";
            }
        }
        if (StringUtil.isEmpty(callback)) {
            return ResultCodeConstants.SUCCESS.getJsonString();
        } else {
            return callback + "([" + ResultCodeConstants.SUCCESS.getJsonString() + "])";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/unlikegame")
    public String unLikeGame(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "gameid", required = false) String gameIdStr) {
        String callback = request.getParameter("callback");
        if (StringUtil.isEmpty(gameIdStr)) {
            if (StringUtil.isEmpty(callback)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            } else {
                return callback + "([" + ResultCodeConstants.PARAM_EMPTY.getJsonString() + "])";
            }
        }

        long gameId = 0l;
        try {
            gameId = Long.parseLong(gameIdStr);
        } catch (NumberFormatException e) {
        }

        if (gameId == 0l) {
            if (StringUtil.isEmpty(callback)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            } else {
                return callback + "([" + ResultCodeConstants.PARAM_EMPTY.getJsonString() + "])";
            }
        }

        try {
            GameDB gameDB = GameResourceServiceSngl.get().getGameDB(new BasicDBObject("_id", gameId), false);
            if (gameDB == null) {
                if (StringUtil.isEmpty(callback)) {
                    return ResultCodeConstants.GAMEDB_GAME_NOTEXISTS.getJsonString();
                } else {
                    return callback + "([" + ResultCodeConstants.GAMEDB_GAME_NOTEXISTS.getJsonString() + "])";
                }
            }
            String anotherName=gameDB.getAnotherName()==null?"":gameDB.getAnotherName();
            GameResourceServiceSngl.get().incUserLikeGame(0l, gameId, anotherName, GameDBField.UN_FAVOR_SUM.getColumn());
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
            if (StringUtil.isEmpty(callback)) {
                return ResultCodeConstants.ERROR.getJsonString();
            } else {
                return callback + "([" + ResultCodeConstants.ERROR.getJsonString() + "])";
            }
        }
        if (StringUtil.isEmpty(callback)) {
            return ResultCodeConstants.SUCCESS.getJsonString();
        } else {
            return callback + "([" + ResultCodeConstants.SUCCESS.getJsonString() + "])";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/gamelist")
    public String gameList(HttpServletRequest request, HttpServletResponse response,
                           @RequestParam(value = "p", required = false, defaultValue = "1") String p,
                           @RequestParam(value = "name", required = false, defaultValue = "") String name,
                           @RequestParam(value = "platformtype", required = false, defaultValue = "") String platformtype,
                           @RequestParam(value = "platform", required = false, defaultValue = "") String platform,
                           @RequestParam(value = "nettype", required = false, defaultValue = "") String netType,
                           @RequestParam(value = "languagetype", required = false, defaultValue = "") String languageType,
                           @RequestParam(value = "category", required = false, defaultValue = "") String category,
                           @RequestParam(value = "themetype", required = false, defaultValue = "") String themeType) {
        String callback = request.getParameter("callback");
        try {
            int cp = 1;
            try {
                cp = Integer.parseInt(p);
            } catch (NumberFormatException e) {
            }
            Pagination pagination = new Pagination(GAMES_PAGE_SIZE * cp, cp, GAMES_PAGE_SIZE);

            MongoQueryExpress mongoQueryExpress = new MongoQueryExpress();
            if (!StringUtil.isEmpty(name)) {
                mongoQueryExpress.add(MongoQueryCriterions.like(GameDBField.GAMENAME, name));
            }else {
                if(!StringUtil.isEmpty(platformtype)){
                    mongoQueryExpress.add(MongoQueryCriterions.eq(new GameDBField(GameDBField.PLATFORMTYPE_.getColumn() + platformtype, ObjectFieldDBType.BOOLEAN), true));
                    if(!StringUtil.isEmpty(platform)){
                        mongoQueryExpress.add(MongoQueryCriterions.eq(new GameDBField(GameDBField.GAME_PLATFORM_.getColumn() + platformtype + "_" + platform, ObjectFieldDBType.BOOLEAN), true));
                    }
                }
                if(!StringUtil.isEmpty(netType)){
                    mongoQueryExpress.add(MongoQueryCriterions.eq(GameDBField.GAMENETTYPE, Integer.valueOf(netType)));
                }
                if(!StringUtil.isEmpty(languageType)){
                    mongoQueryExpress.add(MongoQueryCriterions.eq(new GameDBField(GameDBField.GAME_LANGUAGE_.getColumn() + languageType, ObjectFieldDBType.BOOLEAN), true));
                }
                if(!StringUtil.isEmpty(category)){
                    mongoQueryExpress.add(MongoQueryCriterions.eq(new GameDBField(GameDBField.GAME_CATEGORY_.getColumn() + category, ObjectFieldDBType.BOOLEAN), true));
                }
                if(!StringUtil.isEmpty(themeType)){
                    mongoQueryExpress.add(MongoQueryCriterions.eq(new GameDBField(GameDBField.GAME_THEME_.getColumn() + themeType, ObjectFieldDBType.BOOLEAN), true));
                }
            }
            mongoQueryExpress.add(MongoQueryCriterions.eq(GameDBField.VALIDSTATUS, GameDbStatus.VALID.getCode()));
            
            mongoQueryExpress.add(new MongoSort[]{new MongoSort(GameDBField.CREATE_DATE, MongoSortOrder.DESC)});
            
            PageRows<GameDB> gameDBPageRows = GameResourceServiceSngl.get().queryGameDbByPage(mongoQueryExpress, pagination);
            if (gameDBPageRows != null && !CollectionUtil.isEmpty(gameDBPageRows.getRows())) {
                List<GameCollectionDTO> list = new ArrayList<GameCollectionDTO>();
                for (GameDB gameDB : gameDBPageRows.getRows()) {
                    list.add(GameCollectionDTO.buildDTOFromGameDB(gameDB));
                }
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("rs", String.valueOf(ResultCodeConstants.SUCCESS.getCode()));
                jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());

                JSONObject result = new JSONObject();
                result.put("rows", list);
                result.put("page", gameDBPageRows.getPage());
                jsonObject.put("result", result);

                if (StringUtil.isEmpty(callback)) {
                    return jsonObject.toString();
                } else {
                    return callback + "([" + jsonObject.toString() + "])";
                }
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
            if (StringUtil.isEmpty(callback)) {
                return ResultCodeConstants.ERROR.getJsonString();
            } else {
                return callback + "([" + ResultCodeConstants.ERROR.getJsonString() + "])";
            }
        }
        if (StringUtil.isEmpty(callback)) {
            return ResultCodeConstants.SUCCESS.getJsonString();
        } else {
            return callback + "([" + ResultCodeConstants.SUCCESS.getJsonString() + "])";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/archivelist")
    public String archiveList(HttpServletRequest request, HttpServletResponse response,
                              @RequestParam(value = "gameid", required = false) String gameIdStr,
                              @RequestParam(value = "archivetype") String archiveType,
                              @RequestParam(value = "p", required = false, defaultValue = "1") String p) {
        String callback = request.getParameter("callback");

        if (StringUtil.isEmpty(gameIdStr) || StringUtil.isEmpty(archiveType)) {
            if (StringUtil.isEmpty(callback)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            } else {
                return callback + "([" + ResultCodeConstants.PARAM_EMPTY.getJsonString() + "])";
            }
        }

        long gameId = 0l;
        try {
            gameId = Long.parseLong(gameIdStr);
        } catch (NumberFormatException e) {
        }

        if (gameId == 0l) {
            if (StringUtil.isEmpty(callback)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            } else {
                return callback + "([" + ResultCodeConstants.PARAM_EMPTY.getJsonString() + "])";
            }
        }

        int cp = Integer.valueOf(p);
        Pagination pagination = new Pagination(CONTENT_PAGE_SIZE * cp, cp, CONTENT_PAGE_SIZE);

        ArchiveContentType contentType = null;
        try {
            contentType = ArchiveContentType.getByCode(Integer.parseInt(archiveType));
        } catch (NumberFormatException e) {
        }
        if (contentType == null) {
            if (StringUtil.isEmpty(callback)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            } else {
                return callback + "([" + ResultCodeConstants.PARAM_EMPTY.getJsonString() + "])";
            }
        }
        try {
            PageRows<GameArchivesDTO> archiveList = GameResourceServiceSngl.get().queryGameArchivesByCache(gameId, ArchiveRelationType.GAME_RELATION, contentType, pagination);
            if (archiveList != null && !CollectionUtil.isEmpty(archiveList.getRows())) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("rs", String.valueOf(ResultCodeConstants.SUCCESS.getCode()));
                jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());

                JSONObject result = new JSONObject();
                result.put("rows", archiveList.getRows());
                result.put("page", archiveList.getPage());
                jsonObject.put("result", result);

                if (StringUtil.isEmpty(callback)) {
                    return jsonObject.toString();
                } else {
                    return callback + "([" + jsonObject.toString() + "])";
                }
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
            if (StringUtil.isEmpty(callback)) {
                return ResultCodeConstants.ERROR.getJsonString();
            } else {
                return callback + "([" + ResultCodeConstants.ERROR.getJsonString() + "])";
            }
        }
        if (StringUtil.isEmpty(callback)) {
            return ResultCodeConstants.SUCCESS.getJsonString();
        } else {
            return callback + "([" + ResultCodeConstants.SUCCESS.getJsonString() + "])";
        }
    }
   
}
