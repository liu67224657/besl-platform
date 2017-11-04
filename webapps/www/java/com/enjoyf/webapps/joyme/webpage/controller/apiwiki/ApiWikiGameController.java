package com.enjoyf.webapps.joyme.webpage.controller.apiwiki;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.ask.AskServiceSngl;
import com.enjoyf.platform.service.ask.AskUtil;
import com.enjoyf.platform.service.ask.WikiGameres;
import com.enjoyf.platform.service.ask.WikiGameresField;
import com.enjoyf.platform.service.ask.search.WikiappSearchDeleteEntry;
import com.enjoyf.platform.service.ask.search.WikiappSearchEntry;
import com.enjoyf.platform.service.ask.search.WikiappSearchType;
import com.enjoyf.platform.service.event.EventDispatchServiceSngl;
import com.enjoyf.platform.service.event.system.wiki.WikiappSearchDeleteEvent;
import com.enjoyf.platform.service.event.system.wiki.WikiappSearchEvent;
import com.enjoyf.platform.service.gameres.GameResource;
import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.gameres.gamedb.GameDB;
import com.enjoyf.platform.service.gameres.gamedb.GameDBField;
import com.enjoyf.platform.service.gameres.gamedb.GameDbStatus;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.webapps.joyme.dto.joymewiki.AlphabetGameDTO;
import com.enjoyf.webapps.joyme.dto.joymewiki.GameDTO;
import com.enjoyf.webapps.joyme.weblogic.apiwiki.ApiwikiWebLogic;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import net.sf.json.JSONObject;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by pengxu on 2017/3/27.
 */

@Controller
@RequestMapping(value = "/api/wiki/game")
public class ApiWikiGameController {
    private static final Integer RECOMMEND = 1; //推荐

    @Resource(name = "apiwikiWebLogic")
    private ApiwikiWebLogic apiwikiWebLogic;

    @Resource(name = "i18nSource")
    private ResourceBundleMessageSource i18nSource;

    @ResponseBody
    @RequestMapping(value = "/recommend")
    public String recommend(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
        Map<String, Object> returnMap = new HashMap<String, Object>();
        String profileId = HTTPUtil.getParam(request, "pid");

        Map<String, Object> map = new HashMap<String, Object>();
        try {
            PageRows<WikiGameres> wikiGameresMap = null;
            Map<Long, WikiGameres> followMap = new HashMap<Long, WikiGameres>();
            if (!StringUtil.isEmpty(profileId)) {
                //查询订阅的游戏
                wikiGameresMap = AskServiceSngl.get().queryFollowGameList(profileId, new Pagination(), true);
                if (wikiGameresMap != null && !CollectionUtil.isEmpty(wikiGameresMap.getRows())) {
                    followMap = returnWikiGameresMap(wikiGameresMap.getRows());
                }
            }

            String version = HTTPUtil.getParam(request, "version");
            Integer verint = StringUtil.isEmpty(version) ? 0 : Integer.valueOf(version.replaceAll("\\.", ""));

            List<WikiGameres> allWikiGameres = AskServiceSngl.get().queryAllWikiGameres();
            List<GameDTO> list = apiwikiWebLogic.buildGameDTOByMap(allWikiGameres, followMap, verint);
            returnMap.put("rows", CollectionUtil.isEmpty(list) ? new ArrayList<GameDTO>() : list);
            jsonObject.put("result", returnMap);
            return jsonObject.toString();

//            List<GameDTO> followGames = new ArrayList<GameDTO>();
//            if (wikiGameresMap == null) {
//                map.put("followlist", followGames);
//            } else {
//                followGames = buildFollowGameDTO(wikiGameresMap.getRows());
//                map.put("followlist", CollectionUtil.isEmpty(followGames) ? new ArrayList<GameDTO>() : followGames);
//            }
//            //推荐游戏
//            Map<Long, WikiGameres> recommendMap = AskServiceSngl.get().queryRecommendGame();
//            List<GameDTO> list = apiwikiWebLogic.buildGameDTOByMap(recommendMap);
//            map.put("recommendgames", CollectionUtil.isEmpty(list) ? new ArrayList<GameDTO>() : list);
//            //查询所有游戏
//            List<WikiGameres> wikiGameres = AskServiceSngl.get().queryAllWikiGameres();
//            List<AlphabetGameDTO> alphabetList = apiwikiWebLogic.queryAlphabetGames(wikiGameres);
//            map.put("alphabetgames", CollectionUtil.isEmpty(alphabetList) ? new ArrayList<AlphabetGameDTO>() : alphabetList);


        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return ResultCodeConstants.ERROR.getJsonString();
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return ResultCodeConstants.ERROR.getJsonString();
        }
    }

    private Map<Long, WikiGameres> returnWikiGameresMap(List<WikiGameres> list) {
        Map<Long, WikiGameres> map = new HashMap<Long, WikiGameres>();
        if (CollectionUtil.isEmpty(list)) {
            return map;
        }
        for (WikiGameres wikiGameres : list) {
            map.put(wikiGameres.getGameId(), wikiGameres);
        }

        return map;
    }

    private List<GameDTO> buildFollowGameDTO(List<WikiGameres> list) throws ServiceException {
        if (CollectionUtil.isEmpty(list)) {
            return null;
        }
        Map<Long, WikiGameres> map = new LinkedHashMap<Long, WikiGameres>();
        for (WikiGameres wikiGameres : list) {
            map.put(wikiGameres.getGameId(), wikiGameres);
        }

        List<GameDTO> gameDTOs = apiwikiWebLogic.buildGameDTOByMap(map, 0);
        return gameDTOs;
    }

    @RequestMapping(value = "/followlist")
    @ResponseBody
    public String followList(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "pnum", required = false, defaultValue = "1") Integer page,
                             @RequestParam(value = "count", required = false, defaultValue = "10") Integer count) {
        JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
        try {
            String profileId = HTTPUtil.getParam(request, "pid");
            Pagination pagination = new Pagination(count * page, page, count);
            if (StringUtil.isEmpty(profileId)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            }
            PageRows<WikiGameres> wikiGameresMap = AskServiceSngl.get().queryFollowGameList(profileId, pagination, true);
            Map<String, Object> returnMap = new HashMap<String, Object>();
            Map<Long, WikiGameres> followMap = new HashMap<Long, WikiGameres>();
            if (wikiGameresMap != null && !CollectionUtil.isEmpty(wikiGameresMap.getRows())) {
                followMap = returnWikiGameresMap(wikiGameresMap.getRows());
            }
            //查询所有游戏
            String version = HTTPUtil.getParam(request, "version");
            Integer verint = StringUtil.isEmpty(version) ? 0 : Integer.valueOf(version.replaceAll("\\.", ""));
            List<WikiGameres> wikiGameres = AskServiceSngl.get().queryAllWikiGameres();
            List<GameDTO> alphabetList = apiwikiWebLogic.buildGameDTOByMap(wikiGameres, followMap, verint);
            returnMap.put("rows", CollectionUtil.isEmpty(alphabetList) ? new ArrayList<AlphabetGameDTO>() : alphabetList);
            jsonObject.put("result", returnMap);
            return jsonObject.toString();
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return ResultCodeConstants.ERROR.getJsonString();
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return ResultCodeConstants.ERROR.getJsonString();
        }
    }


    @RequestMapping(value = "/follow")
    @ResponseBody
    public String follow(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
        try {
            String profileId = HTTPUtil.getParam(request, "pid");
            String gameIds = HTTPUtil.getParam(request, "gameids");
            if (StringUtil.isEmpty(profileId) || StringUtil.isEmpty(gameIds)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            }
            String[] gameArray = new String[0];
            try {
                gameArray = gameIds.split("\\,");
            } catch (Exception e) {
                return ResultCodeConstants.FORMAT_ERROR.getJsonString();
            }
            AskServiceSngl.get().batchFollow(gameArray, profileId);
        } catch (ServiceException e) {
            return ResultCodeConstants.ERROR.getJsonString();
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return ResultCodeConstants.ERROR.getJsonString();
        }
        return jsonObject.toString();
    }

    @RequestMapping(value = "/unfollow")
    @ResponseBody
    public String unfollow(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
        try {
            String profileId = HTTPUtil.getParam(request, "pid");
            String gameIds = HTTPUtil.getParam(request, "gameids");
            if (StringUtil.isEmpty(profileId) || StringUtil.isEmpty(gameIds)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            }
            String[] gameArray = new String[0];
            try {
                gameArray = gameIds.split("\\,");
            } catch (Exception e) {
                return ResultCodeConstants.FORMAT_ERROR.getJsonString();
            }
            AskServiceSngl.get().unFollow(gameArray, profileId);
        } catch (ServiceException e) {
            return ResultCodeConstants.ERROR.getJsonString();
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return ResultCodeConstants.ERROR.getJsonString();
        }
        return jsonObject.toString();
    }


    @RequestMapping(value = "/getgamebykey")
    @ResponseBody
    public String getGameByKey(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
        String wikikey = HTTPUtil.getParam(request, "wikikey");
        if (StringUtil.isEmpty(wikikey)) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }
        try {
            GameDB gameDB = GameResourceServiceSngl.get().getGameDB(new BasicDBObject(GameDBField.WIKIKEY.getColumn(), wikikey), true);
            if (gameDB == null) {
                return ResultCodeConstants.GAMEDB_GAME_NOTEXISTS.getJsonString();
            }
            Map<String, String> map = new HashMap<String, String>();
            map.put(String.valueOf(gameDB.getGameDbId()), gameDB.getGameName());
            jsonObject.put("result", map);
            return jsonObject.toString();
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return ResultCodeConstants.ERROR.getJsonString();
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return ResultCodeConstants.ERROR.getJsonString();
        }
    }

    @RequestMapping(value = "/querygame")
    @ResponseBody
    public String queryGameDB(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
        try {
            String gameids = HTTPUtil.getParam(request, "gameids");
            if (StringUtil.isEmpty(gameids)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            }
            String[] gameDbIdArray = null;
            try {
                gameDbIdArray = gameids.split("\\,");
            } catch (Exception e) {
                return ResultCodeConstants.FORMAT_ERROR.getJsonString();
            }
            Set<Long> gameIdSet = new HashSet<Long>();
            for (String id : gameDbIdArray) {
                gameIdSet.add(Long.valueOf(id));
            }
            Map<Long, GameDB> map = GameResourceServiceSngl.get().queryGameDBSet(gameIdSet);
            if (map == null || map.isEmpty()) {
                return ResultCodeConstants.GAMEDB_GAME_NOTEXISTS.getJsonString();
            }

            Map<String, String> returnMap = new HashMap<String, String>();
            for (Long id : map.keySet()) {
                returnMap.put(id.toString(), map.get(id).getGameName());
            }
            jsonObject.put("result", returnMap);

            return jsonObject.toString();
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return ResultCodeConstants.ERROR.getJsonString();
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return ResultCodeConstants.ERROR.getJsonString();
        }
    }

    @RequestMapping(value = "/updateheadicon")
    @ResponseBody
    public String updateHeadIcon(HttpServletRequest request, HttpServletResponse response) {
        try {
            String gameid = HTTPUtil.getParam(request, "gameid");
            String pic = HTTPUtil.getParam(request, "pic");
            String wikiKey = HTTPUtil.getParam(request, "wikikey");
            if (StringUtil.isEmpty(gameid) || StringUtil.isEmpty(pic) || StringUtil.isEmpty(wikiKey)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            }

            BasicDBObject basicDBObject = new BasicDBObject();
            basicDBObject.put("_id", Long.parseLong(gameid));
            GameDB gameDB = GameResourceServiceSngl.get().getGameDB(basicDBObject, false);
            if (gameDB == null) {
                return ResultCodeConstants.GAMEDB_GAME_NOTEXISTS.getJsonString();
            }
            if (!wikiKey.equals(gameDB.getWikiKey())) {
                return ResultCodeConstants.GAME_WIKI_NOT_ADD.getJsonString();
            }

            WikiGameres wikiGameres = AskServiceSngl.get().getWikiGameresByGameId(Long.parseLong(gameid));
            if (wikiGameres != null) {
                AskServiceSngl.get().modifyWikiGameres(wikiGameres.getId(), new UpdateExpress().set(WikiGameresField.HEADPIC, pic));
            }

            return ResultCodeConstants.SUCCESS.getJsonString();
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return ResultCodeConstants.ERROR.getJsonString();
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return ResultCodeConstants.ERROR.getJsonString();
        }
    }

    private static final String VALID = "1";

    @RequestMapping(value = "/updatestatus")
    @ResponseBody
    public String updateStatus(HttpServletRequest request, HttpServletResponse response) {
        String gameid = HTTPUtil.getParam(request, "gameid");
        String wikiKey = HTTPUtil.getParam(request, "wikikey");
        String status = HTTPUtil.getParam(request, "status");// 0=禁用 1=启用
        try {
            if (StringUtil.isEmpty(gameid) || StringUtil.isEmpty(status) || StringUtil.isEmpty(wikiKey)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            }
            BasicDBObject basicDBObject = new BasicDBObject();
            basicDBObject.put("_id", Long.parseLong(gameid));
            GameDB gameDB = GameResourceServiceSngl.get().getGameDB(basicDBObject, false);
            if (gameDB == null) {
                return ResultCodeConstants.GAMEDB_GAME_NOTEXISTS.getJsonString();
            }
            if (!wikiKey.equals(gameDB.getWikiKey())) {
                return ResultCodeConstants.GAME_WIKI_NOT_ADD.getJsonString();
            }

            WikiGameres wikiGameres = AskServiceSngl.get().getWikiGameresByGameId(Long.parseLong(gameid));
            if (wikiGameres != null) {
                boolean flag = false;
                //查询激活时是否属于推荐  如果是则放入推荐缓存列表
                if (status.equals(VALID) && wikiGameres.getRecommend() == RECOMMEND) {
                    flag = true;
                }
                AskServiceSngl.get().modifyRecommend(wikiGameres.getId(), flag, new UpdateExpress().set(WikiGameresField.VALIDSTATUS, status.equals(VALID) ? ValidStatus.VALID.getCode() : ValidStatus.INVALID.getCode()));


                if (status.equals(VALID)) {
                    WikiappSearchEntry searchEntry = new WikiappSearchEntry();
                    searchEntry.setCreatetime(new Date().getTime());
                    searchEntry.setId(String.valueOf(wikiGameres.getGameId()));
                    searchEntry.setTitle(wikiGameres.getGameName());
                    searchEntry.setType(WikiappSearchType.GAME.getCode());
                    searchEntry.setEntryid(AskUtil.getWikiappSearchEntryId(String.valueOf(wikiGameres.getGameId()), WikiappSearchType.GAME));

                    WikiappSearchEvent event = new WikiappSearchEvent();
                    event.setWikiappSearchEntry(searchEntry);
                    EventDispatchServiceSngl.get().dispatch(event);
                } else {
                    WikiappSearchDeleteEntry deleteEntry = new WikiappSearchDeleteEntry();
                    deleteEntry.setId(String.valueOf(wikiGameres.getGameId()));
                    deleteEntry.setType(WikiappSearchType.GAME);

                    WikiappSearchDeleteEvent event = new WikiappSearchDeleteEvent();
                    event.setWikiappSearchDeleteEntry(deleteEntry);
                    EventDispatchServiceSngl.get().dispatch(event);
                }

            }

            return ResultCodeConstants.SUCCESS.getJsonString();
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return ResultCodeConstants.ERROR.getJsonString();
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return ResultCodeConstants.ERROR.getJsonString();
        }
    }


    /**
     * 微服务调用，更新游戏库点评人数及积分
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/updateGamedbByComment")
    @ResponseBody
    public String updateGamedbByComment(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
        try {
            String gameid = HTTPUtil.getParam(request, "gameid");
            if (!StringUtil.isEmpty(gameid)) {
                String score = HTTPUtil.getParam(request, "score");
                String scoresum = HTTPUtil.getParam(request, "scoresum");

                BasicDBObject queryDBObject = new BasicDBObject();
                BasicDBObject updateDBObject = new BasicDBObject();
                queryDBObject.put(GameDBField.ID.getColumn(), Long.valueOf(gameid));
                updateDBObject.put(GameDBField.COMMENTSCORE.getColumn(), score);
                updateDBObject.put(GameDBField.COMMENTSUM.getColumn(), scoresum);
                GameResourceServiceSngl.get().updateGameDB(queryDBObject, updateDBObject);
            }
            return jsonObject.toString();
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return ResultCodeConstants.ERROR.getJsonString();
        }
    }
}
