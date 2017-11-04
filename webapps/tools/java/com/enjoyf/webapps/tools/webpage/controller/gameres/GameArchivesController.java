package com.enjoyf.webapps.tools.webpage.controller.gameres;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.gameres.gamedb.GameDB;
import com.enjoyf.platform.service.gameres.gamedb.GameDBField;
import com.enjoyf.platform.service.gameres.gamedb.GameDbStatus;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.joymeapp.gameclient.ArchiveContentType;
import com.enjoyf.platform.service.joymeapp.gameclient.ArchiveRelationType;
import com.enjoyf.platform.service.joymeapp.gameclient.TagDedearchives;
import com.enjoyf.platform.service.joymeapp.gameclient.TagDedearchivesFiled;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.service.tools.ToolsLog;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.oauth.weibo4j.model.Query;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import com.mongodb.BasicDBObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 15-06-25
 * Time: 上午10:16
 * To change this template use File | Settings | File Templates.
 */
@Deprecated
@Controller
@RequestMapping(value = "/gamedb/archives")
public class GameArchivesController extends ToolsBaseController {

    @RequestMapping(value = "/list")
    public ModelAndView lineList(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                                 @RequestParam(value = "maxPageItems", required = false, defaultValue = "50") int pageSize,
                                 @RequestParam(value = "gameid", required = false) Long gameId,
                                 @RequestParam(value = "contenttype", required = false, defaultValue = "0") Integer contentType,
                                 @RequestParam(value = "status", required = false) String status) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("gameId", gameId);
        mapMessage.put("contentType", contentType);
        mapMessage.put("status", status);

        mapMessage.put("contentTypeSet", ArchiveContentType.getAll());

        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);

        if (gameId == null) {
            return new ModelAndView("redirect:/gamedb/list");
        }
        try {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(TagDedearchivesFiled.TAGID, gameId));
            queryExpress.add(QueryCriterions.eq(TagDedearchivesFiled.ARCHIVE_RELATION_TYPE, ArchiveRelationType.GAME_RELATION.getCode()));
            queryExpress.add(QueryCriterions.eq(TagDedearchivesFiled.ARCHIVE_CONTENT_TYPE, contentType));
            if (StringUtil.isEmpty(status)) {
                queryExpress.add(QueryCriterions.eq(TagDedearchivesFiled.REMOVE_STATUS, ValidStatus.VALID.getCode()));
            } else {
                queryExpress.add(QueryCriterions.eq(TagDedearchivesFiled.REMOVE_STATUS, status));
            }

            queryExpress.add(QuerySort.add(TagDedearchivesFiled.DEDE_ARCHIVES_PUBDATE, QuerySortOrder.DESC));
            PageRows<TagDedearchives> pageRows = JoymeAppServiceSngl.get().queryTagDedearchivesByPage(true, gameId, null, queryExpress, pagination);
            if (pageRows != null) {
                mapMessage.put("list", pageRows.getRows());
                mapMessage.put("page", pageRows.getPage());
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/gameresource/gamedb/archives/archivelist", mapMessage);
    }

    @RequestMapping(value = "/delete")
    public ModelAndView delete(@RequestParam(value = "gameid", required = false) Long gameId,
                               @RequestParam(value = "archiveid", required = false) String archiveId,
                               @RequestParam(value = "contenttype", required = false) Integer contentType,
                               @RequestParam(value = "status", required = false) String status
    ) {
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(TagDedearchivesFiled.TAGID, gameId));
        queryExpress.add(QueryCriterions.eq(TagDedearchivesFiled.DEDE_ARCHIVES_ID, archiveId));
        queryExpress.add(QueryCriterions.eq(TagDedearchivesFiled.ARCHIVE_RELATION_TYPE, ArchiveRelationType.GAME_RELATION.getCode()));

        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(TagDedearchivesFiled.REMOVE_STATUS, ValidStatus.REMOVED.getCode());
        try {
            boolean bool = JoymeAppServiceSngl.get().modifyTagDedearchives(gameId, archiveId, queryExpress, updateExpress, ArchiveRelationType.GAME_RELATION);
            if (bool) {
                GameResourceServiceSngl.get().removeGameArchivesByCache(gameId, ArchiveRelationType.GAME_RELATION, ArchiveContentType.getByCode(contentType), Integer.valueOf(archiveId));
                ToolsLog log = new ToolsLog();
                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.DELETE_TAG_DEDE_ARCHIVES);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("gameId:" + gameId + ",archiveId:" + archiveId);
                addLog(log);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + "ServiceException", e);
            return new ModelAndView("redirect:/gamedb/archives/list?gameid=" + gameId + "&contenttype=" + contentType + "&status=" + status);
        }
        return new ModelAndView("redirect:/gamedb/archives/list?gameid=" + gameId + "&contenttype=" + contentType + "&status=" + status);
    }

    @RequestMapping(value = "/recover")
    public ModelAndView recover(@RequestParam(value = "gameid", required = false) Long gameId,
                                @RequestParam(value = "archiveid", required = false) String archiveId,
                                @RequestParam(value = "contenttype", required = false) Integer contentType,
                                @RequestParam(value = "status", required = false) String status
    ) {
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(TagDedearchivesFiled.TAGID, gameId));
        queryExpress.add(QueryCriterions.eq(TagDedearchivesFiled.DEDE_ARCHIVES_ID, archiveId));
        queryExpress.add(QueryCriterions.eq(TagDedearchivesFiled.ARCHIVE_RELATION_TYPE, ArchiveRelationType.GAME_RELATION.getCode()));

        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(TagDedearchivesFiled.REMOVE_STATUS, ValidStatus.VALID.getCode());
        try {
            boolean bool = JoymeAppServiceSngl.get().modifyTagDedearchives(gameId, archiveId, queryExpress, updateExpress, ArchiveRelationType.GAME_RELATION);
            if (bool) {
                TagDedearchives tag = JoymeAppServiceSngl.get().getTagDedearchives(queryExpress);
                GameResourceServiceSngl.get().putGameArchivesByCache(gameId, ArchiveRelationType.GAME_RELATION, ArchiveContentType.getByCode(contentType), tag);
                ToolsLog log = new ToolsLog();
                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.RECOVER_TAG_DEDE_ARCHIVES);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("gameId:" + gameId + ",archiveId:" + archiveId);
                addLog(log);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + "ServiceException", e);
            return new ModelAndView("redirect:/gamedb/archives/list?gameid=" + gameId + "&contenttype=" + contentType + "&status=" + status);
        }
        return new ModelAndView("redirect:/gamedb/archives/list?gameid=" + gameId + "&contenttype=" + contentType + "&status=" + status);
    }

}
