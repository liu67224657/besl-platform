package com.enjoyf.webapps.tools.webpage.controller.apiwiki;

import com.alibaba.fastjson.JSON;
import com.enjoyf.platform.cloud.OkHttpUtil;
import com.enjoyf.platform.cloud.PageRowsUtil;
import com.enjoyf.platform.cloud.contentservice.game.GameTag;
import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.gameres.gamedb.GameDB;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.redis.ScoreRangeRows;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.webapps.tools.util.MicroAuthUtil;
import com.google.gson.JsonObject;
import com.squareup.okhttp.Response;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by zhimingli on 2017/6/25.
 */
@Controller
@RequestMapping(value = "/apiwiki/gametag")
public class ApiGameTagController {

    @RequestMapping(value = "/json")
    @ResponseBody
    public String jsonapplist(@RequestParam(value = "page", required = false, defaultValue = "1") int page,      //数据库记录索引
                              @RequestParam(value = "rows", required = false, defaultValue = "10") int rows,
                              @RequestParam(value = "tagName", required = false) String tagName) {   //repeatFlag  值是0 是不允许重复，1时允许重复，默认是0          @RequestParam(value = "repeatFlag", required = false,defaultValue = "0") String repeatFlag
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        JsonBinder binder = JsonBinder.buildNormalBinder();
        try {
            String urlget = WebappConfig.get().getContentServiceUrl() + "/api/game-tags?page=" + (page - 1) + "&size=" + rows +
                    "&sort=createTime,desc&tagName=" + tagName;
            String authorization = MicroAuthUtil.getToken();
            Response response = OkHttpUtil.doGet(urlget, authorization, null);
            PageRows<GameTag> pageRows = PageRowsUtil.getPage(response, page, rows, GameTag.class);
            mapMessage.put("rows", pageRows.getRows());
            mapMessage.put("total", pageRows.getPage().getTotalRows());
        } catch (Exception e) {
            e.printStackTrace();
            mapMessage.put("rows", 0);
        }
        return binder.toJson(mapMessage);
    }

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "20") int pageSize,
                             @RequestParam(value = "tagName", required = false, defaultValue = "") String tagName,
                             @RequestParam(value = "validStatus", required = false, defaultValue = "") String validStatus,
                             HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("validStatus", validStatus);
        mapMessage.put("tagName", tagName);
        mapMessage.put("pageStartIndex", pageStartIndex);
        try {
            int curPage = (pageStartIndex / pageSize) + 1;
            String urlget = WebappConfig.get().getContentServiceUrl() + "/api/game-tags?page=" + (curPage - 1) + "&size=" + pageSize +
                    "&sort=id,desc&validStatus=" + validStatus + "&tagName=" + tagName;
            String authorization = MicroAuthUtil.getToken();
            Response response = OkHttpUtil.doGet(urlget, authorization, null);
            PageRows<GameTag> pageRows = PageRowsUtil.getPage(response, curPage, pageSize, GameTag.class);
            mapMessage.put("list", pageRows.getRows());
            mapMessage.put("page", pageRows.getPage());
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }
        return new ModelAndView("/apiwiki/gametag/gametaglist", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createPage(HttpServletRequest request,
                                   @RequestParam(value = "gameid", required = false) String gameid) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        return new ModelAndView("/apiwiki/gametag/createpage", mapMessage);
    }

    @ResponseBody
    @RequestMapping(value = "/create")
    public String create(@RequestParam(value = "tagName", required = false) String tagName,
                         HttpServletRequest request) {
        try {
            JsonObject json = new JsonObject();
            json.addProperty("tagName", tagName);
            String url = WebappConfig.get().getContentServiceUrl() + "/api/game-tags";
            String authorization = MicroAuthUtil.getToken();
            Response response = OkHttpUtil.doPost(url, json, authorization, null);

            //标签存在
            if (!response.isSuccessful()) {
                return ResultCodeConstants.ERROR.getJsonString();

            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }
        return ResultCodeConstants.SUCCESS.getJsonString();
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyPage(@RequestParam(value = "id", required = false) Long id,
                                   HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            String url = WebappConfig.get().getContentServiceUrl() + "/api/game-tags/" + id;
            String authorization = MicroAuthUtil.getToken();
            Response response = OkHttpUtil.doGet(url, authorization, null);
            GameTag gameTag = JSON.parseObject(response.body().string(), GameTag.class);
            mapMessage.put("gameTag", gameTag);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            ///mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/apiwiki/gametag/modifypage", mapMessage);
    }

    @ResponseBody
    @RequestMapping(value = "/modify")
    public String modify(@RequestParam(value = "id", required = false) Long id,
                         @RequestParam(value = "tagName", required = false) String tagName,
                         HttpServletRequest request) {
        try {
            JsonObject json = new JsonObject();
            json.addProperty("tagName", tagName);
            json.addProperty("id", id);
            String url = WebappConfig.get().getContentServiceUrl() + "/api/game-tags";
            String authorization = MicroAuthUtil.getToken();
            Response response = OkHttpUtil.doPut(url, json, authorization, null);
            //标签存在
            if (!response.isSuccessful()) {
                return ResultCodeConstants.ERROR.getJsonString();

            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }
        return ResultCodeConstants.SUCCESS.getJsonString();
    }


    @RequestMapping(value = "/valid")
    public ModelAndView valid(@RequestParam(value = "id", required = false) Long id,
                              @RequestParam(value = "valid", required = false) String valid,
                              HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            String url = WebappConfig.get().getContentServiceUrl() + "/api/game-tags/validstatus/" + id + "/" + valid;
            String authorization = MicroAuthUtil.getToken();
            OkHttpUtil.doPut(url, new JsonObject(), authorization, null);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            ///mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/apiwiki/gametag/list");
    }

    @RequestMapping(value = "/gamelist")
    public ModelAndView gamelist(@RequestParam(value = "pager.offset", required = false, defaultValue = "-1") double pageStartIndex,
                                 @RequestParam(value = "maxPageItems", required = false, defaultValue = "20") int pageSize,
                                 @RequestParam(value = "id", required = false, defaultValue = "") Long tagId,
                                 HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("pageStartIndex", pageStartIndex);

        try {


            String urlget = WebappConfig.get().getContentServiceUrl() + "/api/game-tags/gamedb/" + tagId + "?flag=" + pageStartIndex + "&psize=" + pageSize;

            String authorization = MicroAuthUtil.getToken();
            Response response = OkHttpUtil.doGet(urlget, authorization, null);
            ScoreRangeRows<GameTag> pageRows = PageRowsUtil.geRangeRows(response, GameTag.class);


            List<GameDB> gameDBList = new ArrayList<GameDB>();
            if (!CollectionUtils.isEmpty(pageRows.getRows())) {
                Set<Long> gameIdSet = new HashSet<Long>();
                for (GameTag gameTag : pageRows.getRows()) {
                    gameIdSet.add(gameTag.getId());
                }

                Map<Long, GameDB> gameDBMap = GameResourceServiceSngl.get().queryGameDBSet(gameIdSet);

                for (Long in : gameDBMap.keySet()) {
                    gameDBList.add(gameDBMap.get(in));
                }


                Collections.sort(gameDBList, new Comparator<GameDB>() {
                    @Override
                    public int compare(GameDB o1, GameDB o2) {
                        return o1.getGameDbId() > o2.getGameDbId() ? -1 : 1;
                    }
                });
            }


            if (pageRows.getRange().isHasnext()) {
                mapMessage.put("next", "/apiwiki/gametag/gamelist?id=" + tagId + "&pager.offset=" + pageRows.getRange().getScoreflag() +
                        "&psize=" + pageSize);
            } else {
                mapMessage.put("next", "");
            }
            mapMessage.put("list", gameDBList);
            mapMessage.put("page", pageRows.getRange());
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }
        return new ModelAndView("/apiwiki/gametag/taggamelist", mapMessage);
    }


}
