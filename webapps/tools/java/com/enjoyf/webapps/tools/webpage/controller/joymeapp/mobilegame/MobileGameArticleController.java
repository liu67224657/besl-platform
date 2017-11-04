package com.enjoyf.webapps.tools.webpage.controller.joymeapp.mobilegame;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.gameres.gamedb.GameDB;
import com.enjoyf.platform.service.gameres.gamedb.GameDBField;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryCriterions;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryExpress;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import com.mongodb.BasicDBObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xupeng on 14-9-15.
 */
@Controller
@RequestMapping(value = "/mobilegame/article")
public class MobileGameArticleController extends ToolsBaseController {
    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                             @RequestParam(value = "gamename", required = false) String gameName) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            int curPage = (pageStartIndex / pageSize) + 1;
            Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
            QueryExpress queryExpress = new QueryExpress();
            if (!StringUtil.isEmpty(gameName)) {
                GameDB gameDB = GameResourceServiceSngl.get().getGameDB(new BasicDBObject().append("gamename", gameName), false);
                if (gameDB != null) {
                    queryExpress.add(QueryCriterions.eq(MobileGameArticleField.GAME_DB_ID, gameDB.getGameDbId()));
                    mapMessage.put("gameDbId", gameDB.getGameDbId());

                } else {
                    queryExpress.add(QueryCriterions.eq(MobileGameArticleField.GAME_DB_ID, 0l));
                }
                mapMessage.put("gameName", gameName);
            }
            queryExpress.add(QuerySort.add(MobileGameArticleField.DISPLAY_ORDER, QuerySortOrder.ASC));
            PageRows<MobileGameArticle> pageRows = JoymeAppServiceSngl.get().queryMobileGameArticleByPage(queryExpress, pagination);

            if (pageRows != null) {

                mapMessage.put("list", pageRows.getRows());
                mapMessage.put("page", pageRows.getPage());
                Long longlist[] = new Long[pageRows.getRows().size()];

                for (int i = 0; i < pageRows.getRows().size(); i++) {
                    longlist[i] = pageRows.getRows().get(i).getGameDbId();
                }

                MongoQueryExpress mongoQueryExpress = new MongoQueryExpress().add(MongoQueryCriterions.in(GameDBField.ID, longlist));
                List<GameDB> gameDBList = GameResourceServiceSngl.get().queryGameDB(mongoQueryExpress);

                mapMessage.put("gameDbs", gameDBList);
            }


        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");

            return new ModelAndView("/joymeapp/mobilegame/article/mobilegamearticlelist", mapMessage);
        }
        return new ModelAndView("/joymeapp/mobilegame/article/mobilegamearticlelist", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createPage() {

        return new ModelAndView("/joymeapp/mobilegame/article/createpage");
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "title", required = false) String title,
                               @RequestParam(value = "desc", required = false) String desc,
                               @RequestParam(value = "url", required = false) String url,
                               @RequestParam(value = "author", required = false) String author,
                               @RequestParam(value = "gamedbid", required = false) String gameDbId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            GameDB gameDb = GameResourceServiceSngl.get().getGameDB(new BasicDBObject().append("_id", Long.parseLong(gameDbId)), false);
            if (gameDb == null) {
                mapMessage.put("gamedbisnull", "gamedb.is.null");
                return new ModelAndView("/joymeapp/mobilegame/article/createpage", mapMessage);
            }

            MobileGameArticle mobileGameArticle = new MobileGameArticle();
            mobileGameArticle.setTitle(title);
            mobileGameArticle.setDesc(desc);
            mobileGameArticle.setArticleUrl(url);
            mobileGameArticle.setAuthorName(author);
            mobileGameArticle.setGameDbId(Long.parseLong(gameDbId));
            mobileGameArticle.setCreateTime(new Date());
            mobileGameArticle.setValidStatus(ValidStatus.VALID);
            mobileGameArticle.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));

            JoymeAppServiceSngl.get().createMobileGameArticle(mobileGameArticle);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");

            return new ModelAndView("/joymeapp/mobilegame/article/mobilegamearticlelist", mapMessage);
        }

        return new ModelAndView("redirect:/mobilegame/article/list");
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyPage(@RequestParam(value = "id", required = false) Long id) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            MobileGameArticle mobileGameArticle = JoymeAppServiceSngl.get().getMobileGameArticle(new QueryExpress().add(QueryCriterions.eq(MobileGameArticleField.ID, id)));
            mapMessage.put("mobileGameArticle", mobileGameArticle);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");

        }
        return new ModelAndView("/joymeapp/mobilegame/article/modifypage", mapMessage);
    }

    @RequestMapping(value = "modify")
    public ModelAndView modify(@RequestParam(value = "id", required = false) Long id,
                               @RequestParam(value = "title", required = false) String title,
                               @RequestParam(value = "desc", required = false) String desc,
                               @RequestParam(value = "url", required = false) String url,
                               @RequestParam(value = "author", required = false) String author,
                               @RequestParam(value = "gamedbid", required = false) String gameDbId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            GameDB gameDb = GameResourceServiceSngl.get().getGameDB(new BasicDBObject().append("_id", Long.parseLong(gameDbId)), false);
            if (gameDb == null) {
                mapMessage.put("gamedbisnull", "gamedb.is.null");
                mapMessage.put("id", id);
                return new ModelAndView("forward:/joymeapp/mobilegame/article/modifypage", mapMessage);
            }
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(MobileGameArticleField.AUTHOR_NAME, author);
            updateExpress.set(MobileGameArticleField.ARTICLE_URL, url);
            updateExpress.set(MobileGameArticleField.TITLE, title);
            updateExpress.set(MobileGameArticleField.DESC, desc);

            JoymeAppServiceSngl.get().modifyMobileGameArticle(new QueryExpress().add(QueryCriterions.eq(MobileGameArticleField.ID, id)), updateExpress);

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");

            return new ModelAndView("/joymeapp/mobilegame/article/mobilegamearticlelist", mapMessage);
        }
        return new ModelAndView("redirect:/mobilegame/article/list");
    }


    @RequestMapping(value = "/delete")
    public ModelAndView deleteLine(@RequestParam(value = "id", required = false) Long id) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(MobileGameArticleField.VALID_STATUS, ValidStatus.REMOVED.getCode());
        try {
            boolean bool = JoymeAppServiceSngl.get().modifyMobileGameArticle(new QueryExpress().add(QueryCriterions.eq(MobileGameArticleField.ID, id)), updateExpress);

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + "ServiceException", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("redirect:/mobilegame/article/list");
    }


    @RequestMapping(value = "/recover")
    public ModelAndView modifyLine(@RequestParam(value = "id", required = false) Long id) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(ClientLineItemField.VALID_STATUS, ValidStatus.VALID.getCode());
        try {
            boolean bool = JoymeAppServiceSngl.get().modifyMobileGameArticle(new QueryExpress().add(QueryCriterions.eq(MobileGameArticleField.ID, id)), updateExpress);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + "ServiceException", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("redirect:/mobilegame/article/list");
    }

    @RequestMapping(value = "/sort/{sort}")
    @ResponseBody
    public String sort(@PathVariable(value = "sort") String sort,
                       @RequestParam(value = "id", required = false) Long id,
                       @RequestParam(value = "gamedbid", required = false) Long gameDbId) {
        JsonBinder binder = JsonBinder.buildNormalBinder();
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        ResultObjectMsg resultObjectMsg = new ResultObjectMsg(ResultObjectMsg.CODE_S);
        if (id == null || gameDbId == null) {
            resultObjectMsg.setRs(ResultObjectMsg.CODE_E);
            return binder.toJson(resultObjectMsg);
        }
        Long returnItemId = null;
        try {
            UpdateExpress updateExpress1 = new UpdateExpress();
            UpdateExpress updateExpress2 = new UpdateExpress();
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(MobileGameArticleField.ID, id));
            MobileGameArticle mobileGameArticle = JoymeAppServiceSngl.get().getMobileGameArticle(new QueryExpress().add(QueryCriterions.eq(MobileGameArticleField.ID, id)));
            if (mobileGameArticle == null) {
                return null;
            }
            QueryExpress queryExpress1 = new QueryExpress();
            queryExpress1.add(QueryCriterions.eq(MobileGameArticleField.GAME_DB_ID, gameDbId));
            if (sort.equals("up")) {
                queryExpress1.add(QueryCriterions.lt(MobileGameArticleField.DISPLAY_ORDER, mobileGameArticle.getDisplayOrder()));
                queryExpress1.add(QuerySort.add(MobileGameArticleField.DISPLAY_ORDER, QuerySortOrder.DESC));
            } else {
                queryExpress1.add(QueryCriterions.gt(MobileGameArticleField.DISPLAY_ORDER, mobileGameArticle.getDisplayOrder()));
                queryExpress1.add(QuerySort.add(MobileGameArticleField.DISPLAY_ORDER, QuerySortOrder.ASC));
            }

            PageRows<MobileGameArticle> pageRows = JoymeAppServiceSngl.get().queryMobileGameArticleByPage(queryExpress1, new Pagination(1, 1, 1));
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                updateExpress1.set(MobileGameArticleField.DISPLAY_ORDER, mobileGameArticle.getDisplayOrder());
                JoymeAppServiceSngl.get().modifyMobileGameArticle(new QueryExpress().add(QueryCriterions.eq(MobileGameArticleField.ID, pageRows.getRows().get(0).getId())),updateExpress1);


                updateExpress2.set(MobileGameArticleField.DISPLAY_ORDER, pageRows.getRows().get(0).getDisplayOrder());
                JoymeAppServiceSngl.get().modifyMobileGameArticle(new QueryExpress().add(QueryCriterions.eq(MobileGameArticleField.ID, mobileGameArticle.getId())),updateExpress2);

            }
            returnItemId = pageRows.getRows().get(0).getId();

            if (returnItemId == null) {
                resultObjectMsg.setRs(ResultObjectMsg.CODE_E);
                return binder.toJson(resultObjectMsg);
            }
        } catch (Exception e) {
            resultObjectMsg.setRs(ResultObjectMsg.CODE_E);
            resultObjectMsg.setMsg("system.error");
            return binder.toJson(resultObjectMsg);
        }
        mapMessage.put("sort", sort);
        mapMessage.put("itemid", id);
        mapMessage.put("returnitemid", returnItemId);
        resultObjectMsg.setResult(mapMessage);
        return binder.toJson(resultObjectMsg);
    }
}
