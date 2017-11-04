package com.enjoyf.webapps.tools.webpage.controller.wanba;

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
import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.gameres.gamedb.GameDB;
import com.enjoyf.platform.service.gameres.gamedb.GameDBField;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.webapps.tools.weblogic.gameres.WikiResourceWebLogic;
import com.mongodb.BasicDBObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pengxu on 2017/3/22.
 */
@Controller
@RequestMapping(value = "/joyme/wiki/gameres")
public class WikiGameResController extends AbstractWanbaController {
    private static final Integer RECOMMEND = 1; //推荐
    private static final Integer UNRECOMMEND = 0;//取消推荐

    @Resource(name = "wikiResourceWebLogic")
    private WikiResourceWebLogic wikiResourceWebLogic;


    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "20") int pageSize,
                             @RequestParam(value = "gamename", required = false) String gameName,
                             @RequestParam(value = "recommend", required = false) Integer recommend,
                             @RequestParam(value = "errorMsg", required = false) String msg,
                             @RequestParam(value = "validstatus", required = false) String validStatus
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            int curPage = (pageStartIndex / pageSize) + 1;
            Pagination page = new Pagination(pageSize * curPage, curPage, pageSize);
            QueryExpress queryExpress = new QueryExpress();
            if (recommend != null) {
                queryExpress.add(QueryCriterions.eq(WikiGameresField.RECOMMEND, recommend));
                mapMessage.put("recommend", recommend);
            }
            if (!StringUtil.isEmpty(validStatus)) {
                queryExpress.add(QueryCriterions.eq(WikiGameresField.VALIDSTATUS, validStatus));
                mapMessage.put("validstatus", validStatus);
            }
            if (!StringUtil.isEmpty(gameName)) {
                queryExpress.add(QueryCriterions.like(WikiGameresField.GAMENAME, "%" + gameName + "%"));
                mapMessage.put("gameName", gameName);
            }
            if (!StringUtil.isEmpty(msg)) {
                mapMessage.put("errorMsg", msg);
            }

            queryExpress.add(QuerySort.add(WikiGameresField.RECOMMEND, QuerySortOrder.DESC));
            queryExpress.add(QuerySort.add(WikiGameresField.DISPLAYORDER, QuerySortOrder.ASC));
            PageRows<WikiGameres> pageRows = AskServiceSngl.get().queryGamesByPage(queryExpress, page);
            mapMessage.put("rows", pageRows.getRows());
            mapMessage.put("page", pageRows.getPage());
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
        }
        return new ModelAndView("/wanba/gameres/list", mapMessage);
    }

    @RequestMapping(value = "/status")
    public ModelAndView status(@RequestParam(value = "id", required = false) Long id,
                               @RequestParam(value = "validstatus", required = false) String validStatus) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            boolean flag = false;//禁用时从推荐缓存列表删除
            WikiGameres wikiGameres = AskServiceSngl.get().getWikiGameresByQueryExpress(new QueryExpress().add(QueryCriterions.eq(WikiGameresField.ID, id)));

            if (ValidStatus.VALID.getCode().equals(validStatus)) {
                //查询激活时是否属于推荐  如果是则放入推荐缓存列表
                if (wikiGameres != null && wikiGameres.getRecommend() == RECOMMEND) {
                    flag = true;
                }
                GameDB gameDB = GameResourceServiceSngl.get().getGameDB(new BasicDBObject(GameDBField.ID.getColumn(), wikiGameres.getGameId()), true);
                boolean bool = remote(gameDB.getWikiKey(), wikiGameres.getId());
                if (!bool) {
                    String msg = URLEncoder.encode(gameDB.getGameName() + "还没有添加wiki，请在WIKI管理>wiki列表中添加WIKI", "utf-8");
                    return new ModelAndView("redirect:/joyme/wiki/gameres/list?errorMsg=" + msg);
                }
                WikiappSearchEvent wikiappSearchEvent = new WikiappSearchEvent();
                WikiappSearchEntry wikiappSearchEntry = new WikiappSearchEntry();
                wikiappSearchEntry.setCreatetime(new Date().getTime());
                wikiappSearchEntry.setId(String.valueOf(wikiGameres.getGameId()));
                wikiappSearchEntry.setTitle(wikiGameres.getGameName());
                wikiappSearchEntry.setType(WikiappSearchType.GAME.getCode());
                wikiappSearchEntry.setEntryid(AskUtil.getWikiappSearchEntryId(String.valueOf(wikiGameres.getGameId()), WikiappSearchType.GAME));
                wikiappSearchEvent.setWikiappSearchEntry(wikiappSearchEntry);

                EventDispatchServiceSngl.get().dispatch(wikiappSearchEvent);
            } else {
                WikiappSearchDeleteEvent wikiappSearchDeleteEvent = new WikiappSearchDeleteEvent();
                WikiappSearchDeleteEntry wikiappSearchDeleteEntry = new WikiappSearchDeleteEntry();
                wikiappSearchDeleteEntry.setId(String.valueOf(wikiGameres.getGameId()));
                wikiappSearchDeleteEntry.setType(WikiappSearchType.GAME);
                wikiappSearchDeleteEvent.setWikiappSearchDeleteEntry(wikiappSearchDeleteEntry);

                EventDispatchServiceSngl.get().dispatch(wikiappSearchDeleteEvent);
            }

            boolean bool = AskServiceSngl.get().modifyRecommend(id, flag, new UpdateExpress().set(WikiGameresField.VALIDSTATUS, validStatus));
            if (bool) {
                modifyWikiStatus(wikiGameres.getGameId(), ValidStatus.VALID.getCode().equals(validStatus) ? "1" : "0");
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
        }
        return new ModelAndView("redirect:/joyme/wiki/gameres/list");
    }

    @RequestMapping(value = "/recommend")
    public ModelAndView recommend(@RequestParam(value = "id", required = false) Long id,
                                  @RequestParam(value = "recommend", required = false) Integer recommend) {
        try {
            UpdateExpress updateExpress = new UpdateExpress().set(WikiGameresField.RECOMMEND, recommend);
            boolean flag;
            if (recommend == RECOMMEND) {
                flag = true;
            } else {
                flag = false;
            }
            updateExpress.set(WikiGameresField.DISPLAYORDER, Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
            AskServiceSngl.get().modifyRecommend(id, flag, updateExpress);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
        }
        return new ModelAndView("redirect:/joyme/wiki/gameres/list");
    }


    @RequestMapping(value = "/sort/{sort}")
    public ModelAndView sort(@RequestParam(value = "id", required = false) Long id,
                             @PathVariable(value = "sort") String sort) {
        try {
            wikiResourceWebLogic.sort(sort, id);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
        }
        return new ModelAndView("redirect:/joyme/wiki/gameres/list");
    }
}
