package com.enjoyf.webapps.tools.webpage.controller.apiwiki;

import com.alibaba.fastjson.JSON;
import com.enjoyf.platform.cloud.OkHttpUtil;
import com.enjoyf.platform.cloud.PageRowsUtil;
import com.enjoyf.platform.cloud.contentservice.GameUtil;
import com.enjoyf.platform.cloud.contentservice.game.Game;
import com.enjoyf.platform.cloud.contentservice.game.GameTag;
import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.gameres.gamedb.GameDB;
import com.enjoyf.platform.service.gameres.gamedb.GameDBField;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryCriterions;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryExpress;
import com.enjoyf.platform.util.sql.mongodb.MongoSort;
import com.enjoyf.platform.util.sql.mongodb.MongoSortOrder;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.webapps.tools.util.MicroAuthUtil;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import com.google.common.base.Splitter;
import com.mongodb.BasicDBObject;
import com.squareup.okhttp.Response;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhimingli on 2017/6/23.
 */

@Controller
@RequestMapping(value = "/apiwiki/game")
public class ApiGameController extends ToolsBaseController {


    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "20") int pageSize,
                             @RequestParam(value = "type", required = false, defaultValue = "1") String type,
                             @RequestParam(value = "searchText", required = false, defaultValue = "") String searchText,
                             HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("type", type);
        mapMessage.put("searchText", searchText);
        try {
            int curPage = (pageStartIndex / pageSize) + 1;
            String authorization = MicroAuthUtil.getToken();
            String urlget = WebappConfig.get().getContentServiceUrl() + "/api/games?page=" + (curPage - 1) + "&size=" + pageSize +
                    "&sort=createTime,desc";
            if (!StringUtil.isEmpty(searchText) && type.equals("1")) {
                urlget += "&id=" + searchText;
            }
            if (!StringUtil.isEmpty(searchText) && type.equals("2")) {
                urlget += "&name=" + searchText;
            }
            Response response = OkHttpUtil.doGet(urlget, authorization, null);
            PageRows<Game> pageRows = PageRowsUtil.getPage(response, curPage, pageSize, Game.class);
            mapMessage.put("list", pageRows.getRows());
            mapMessage.put("page", pageRows.getPage());
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }
        return new ModelAndView("/apiwiki/game/gamelist", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createPage(HttpServletRequest request,
                                   @RequestParam(value = "gameid", required = false) String gameid,
                                   @RequestParam(value = "type", required = false, defaultValue = "1") String type) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        Pagination pagination = new Pagination(10 * 1, 1, 10);
        MongoQueryExpress queryExpress = new MongoQueryExpress();
        queryExpress.add(new MongoSort[]{new MongoSort(GameDBField.ID, MongoSortOrder.DESC)});


        queryExpress.add(MongoQueryCriterions.eq(GameDBField.VALIDSTATUS, ValidStatus.VALID.getCode()));
        if (!StringUtil.isEmpty(gameid)) {
            if ("1".equals(type)) {
                queryExpress.add(MongoQueryCriterions.eq(GameDBField.ID, StringUtils.isNumeric(gameid) ? Long.valueOf(gameid) : -1L));
            } else {
                queryExpress.add(MongoQueryCriterions.like(GameDBField.GAMENAME, gameid));
            }
        } else {
            queryExpress.add(MongoQueryCriterions.eq(GameDBField.ID, -1L));
        }


        PageRows<GameDB> pageRows = null;
        try {
            pageRows = GameResourceServiceSngl.get().queryGameDbByPage(queryExpress, pagination);

            String authorization = MicroAuthUtil.getToken();
            if (!CollectionUtil.isEmpty(pageRows.getRows()) && pageRows.getRows().size() == 1) {
                String url = WebappConfig.get().getContentServiceUrl() + "/api/games/" + pageRows.getRows().get(0).getGameDbId();

                Response response = OkHttpUtil.doGet(url, authorization, null);
                if (response.isSuccessful()) {
                    Game game = JSON.parseObject(response.body().string(), Game.class);
                    if (game != null && game.getExtJson() != null) {
                        mapMessage.put("recommend", game.getExtJson().getRecommend());
                        mapMessage.put("recommendAuth", game.getExtJson().getRecommendAuth());
                    }
                    mapMessage.put("ischeck", true);
                }
            }


            if (!CollectionUtil.isEmpty(pageRows.getRows())) {
                List<String> tagidList = new ArrayList<String>();
                for (GameDB gameDB : pageRows.getRows()) {
                    String tagArr[] = gameDB.getGameTag().split(",");
                    for (String tag : tagArr) {
                        if (!tagidList.contains(tag) && !StringUtil.isEmpty(tag) && !tag.equals(",")) {
                            tagidList.add(tag);
                        }
                    }
                }
                List<GameTag> gameTagList = GameUtil.getGameTags(StringUtils.join(tagidList.toArray(), ","), authorization);

                Map<String, String> tagMap = new HashMap<String, String>();

                for (GameTag gameTag : gameTagList) {
                    tagMap.put(String.valueOf(gameTag.getId()), gameTag.getTagName());
                }

                for (GameDB gameDB : pageRows.getRows()) {
                    String tagArr[] = gameDB.getGameTag().split(",");
                    StringBuffer stringBuffer = new StringBuffer();
                    for (int t = 0; t < tagArr.length; t++) {
                        if (tagMap.containsKey(tagArr[t])) {
                            stringBuffer.append(tagMap.get(tagArr[t]));
                            if (t != tagArr.length - 1) {
                                stringBuffer.append(",");
                            }
                        }
                    }
                    gameDB.setGameTag(stringBuffer.toString());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        mapMessage.put("page", pageRows.getPage());
        mapMessage.put("list", pageRows.getRows());
        mapMessage.put("type", type);
        return new ModelAndView("/apiwiki/game/createpage", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "gameDbId", required = false) String gameDbId,
                               @RequestParam(value = "recommend", required = false) String recommend,
                               @RequestParam(value = "recommendAuth", required = false) String recommendAuth,
                               HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            updateGame(gameDbId, recommend, recommendAuth);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }
        return new ModelAndView("redirect:/apiwiki/game/list");
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyPage(@RequestParam(value = "id", required = false) Long id,
                                   HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            String url = WebappConfig.get().getContentServiceUrl() + "/api/games/" + id;
            String authorization = MicroAuthUtil.getToken();
            Response response = OkHttpUtil.doGet(url, authorization, null);
            Game game = JSON.parseObject(response.body().string(), Game.class);
            mapMessage.put("game", game);


            if (game != null && !StringUtil.isEmpty(game.getGameTag())) {
                List<String> tagidList = Splitter.on(",").trimResults().splitToList(game.getGameTag());
                List<GameTag> gameTagList = GameUtil.getGameTags(StringUtils.join(tagidList.toArray(), ","), authorization);
                Map<String, String> tagMap = new HashMap<String, String>();
                for (GameTag gameTag : gameTagList) {
                    tagMap.put(String.valueOf(gameTag.getId()), gameTag.getTagName());
                }


                StringBuffer stringBuffer = new StringBuffer();
                for (int t = 0; t < tagidList.size(); t++) {
                    if (tagMap.containsKey(tagidList.get(t))) {
                        stringBuffer.append(tagMap.get(tagidList.get(t)));
                        if (t != tagidList.size() - 1) {
                            stringBuffer.append(",");
                        }
                    }
                }
                game.setGameTag(stringBuffer.toString());
            }

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/apiwiki/game/modifypage", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "gameDbId", required = false) String gameDbId,
                               @RequestParam(value = "recommend", required = false) String recommend,
                               @RequestParam(value = "recommendAuth", required = false) String recommendAuth,
                               HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            updateGame(gameDbId, recommend, recommendAuth);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }
        return new ModelAndView("redirect:/apiwiki/game/list", mapMessage);
    }

    private void updateGame(String gameDbId, String recommend, String recommendAuth) throws ServiceException {
        GameDB gameDB = GameResourceServiceSngl.get().getGameDB(new BasicDBObject().append("_id", Long.valueOf(gameDbId)), false);
        String authorization = MicroAuthUtil.getToken();
        GameUtil.updateGame(gameDB, authorization, recommend, recommendAuth);
    }


    @RequestMapping(value = "/json")
    @ResponseBody
    public String jsonapplist(@RequestParam(value = "id", required = false, defaultValue = "") Long gameid) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
        try {
            String url = WebappConfig.get().getContentServiceUrl() + "/api/games/" + gameid;
            String authorization = MicroAuthUtil.getToken();
            Response response = OkHttpUtil.doGet(url, authorization, null);
            if (response.isSuccessful()) {
                Game game = JSON.parseObject(response.body().string(), Game.class);
                if (game != null && game.getExtJson() != null) {
                    jsonObject.put("result", game.getExtJson());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject = ResultCodeConstants.ERROR.getJsonObject();
        }
        return jsonObject.toString();
    }


}
