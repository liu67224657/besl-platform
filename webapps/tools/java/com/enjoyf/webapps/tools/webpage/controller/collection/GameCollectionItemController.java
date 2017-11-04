package com.enjoyf.webapps.tools.webpage.controller.collection;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.gameres.gamedb.GameDB;
import com.enjoyf.platform.service.gameres.gamedb.GameDBField;
import com.enjoyf.platform.service.gameres.gamedb.GameDbStatus;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.service.tools.ToolsLog;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
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
 * Date: 15-06-18
 * Time: 上午10:16
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/collection/game/lineitem")
public class GameCollectionItemController extends ToolsBaseController {

    private static Map<Integer, Integer> pageSizeMap = new HashMap<Integer, Integer>();
    private static final String COLLECTION_GAME_HOT_MAJOR = "collection_game_hot_major";//热门游戏

    static {
        pageSizeMap.put(ClientItemType.GAME_RECOMMEND.getCode(), 3);
        pageSizeMap.put(ClientItemType.GAME_NEWS.getCode(), 10);
        pageSizeMap.put(ClientItemType.GAME_MOBILE.getCode(), 3);
        pageSizeMap.put(ClientItemType.GAME_PC.getCode(), 3);
        pageSizeMap.put(ClientItemType.GAME_PSP.getCode(), 3);
        pageSizeMap.put(ClientItemType.GAME_TV.getCode(), 3);
        pageSizeMap.put(ClientItemType.GAME_HOT_MAJOR.getCode(), 10);
    }

    @RequestMapping(value = "/list")
    public ModelAndView lineList(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                                 @RequestParam(value = "maxPageItems", required = false, defaultValue = "50") int pageSize,
                                 @RequestParam(value = "lineid", required = false) Long lineId,
                                 @RequestParam(value = "itemtype", required = false) Integer itemType,
                                 @RequestParam(value = "validstatus", required = false) String validStatus,
                                 @RequestParam(value = "msg", required = false) String errorMsg) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("lineId", lineId);
        mapMessage.put("itemType", itemType);
        mapMessage.put("errorMsg", errorMsg);

        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);

        if (lineId == null) {
            return new ModelAndView("/collection/itemlist");
        }
        try {
            ClientLine clientLine = JoymeAppServiceSngl.get().getClientLine(new QueryExpress().add(QueryCriterions.eq(ClientLineField.LINE_ID, lineId)));
            if (clientLine == null) {
                return new ModelAndView("/collection/itemlist");
            }
            mapMessage.put("clientLine", clientLine);

            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(ClientLineItemField.LINE_ID, clientLine.getLineId()));
            if (StringUtil.isEmpty(validStatus)) {
                queryExpress.add(QueryCriterions.eq(ClientLineItemField.VALID_STATUS, ValidStatus.VALID.getCode()));
            } else {
                queryExpress.add(QueryCriterions.eq(ClientLineItemField.VALID_STATUS, validStatus));
            }
            mapMessage.put("validstatus", validStatus);
            if(clientLine.getCode().equals(COLLECTION_GAME_HOT_MAJOR)){
                queryExpress.add(QuerySort.add(ClientLineItemField.DISPLAY_ORDER, QuerySortOrder.DESC));
            }else {
                queryExpress.add(QuerySort.add(ClientLineItemField.DISPLAY_ORDER, QuerySortOrder.ASC));
            }
            PageRows<ClientLineItem> itemPageRows = JoymeAppServiceSngl.get().queryClientLineItemByPage(queryExpress, pagination);
            if (itemPageRows != null) {
                mapMessage.put("list", itemPageRows.getRows());
                mapMessage.put("page", itemPageRows.getPage());
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/collection/itemlist", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createLinePage(@RequestParam(value = "itemtype", required = false) Integer itemType,
                                       @RequestParam(value = "lineid", required = false) Long lineId,
                                       @RequestParam(value = "gameid", required = false) String gameId,
                                       @RequestParam(value = "msg", required = false) String errorMsg) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("lineId", lineId);
        mapMessage.put("itemType", itemType);
        mapMessage.put("gameId", gameId);
        mapMessage.put("errorMsg", errorMsg);
        return new ModelAndView("/collection/createitem", mapMessage);
    }


    @RequestMapping(value = "/create")
    public ModelAndView createLine(@RequestParam(value = "itemtype", required = false) Integer itemType,
                                   @RequestParam(value = "gameid", required = false) String gid,
                                   @RequestParam(value = "lineid", required = false) String lineId) {
        if (StringUtil.isEmpty(gid)) {
            return new ModelAndView("redirect:/collection/game/lineitem/createpage?lineid=" + lineId + "&itemtype=" + itemType + "&gameid=" + gid + "&msg=gameid.is.null");
        }
        long gameId = 0l;
        try {
            try {
                gameId = Long.parseLong(gid);
            } catch (NumberFormatException e) {
                return new ModelAndView("redirect:/collection/game/lineitem/createpage?lineid=" + lineId + "&itemtype=" + itemType + "&gameid=" + gameId + "&msg=gameid.is.null");
            }
            if (gameId == 0l) {
                return new ModelAndView("redirect:/collection/game/lineitem/createpage?lineid=" + lineId + "&itemtype=" + itemType + "&gameid=" + gameId + "&msg=gameid.is.null");
            }

            ClientLine clientLine = JoymeAppServiceSngl.get().getClientLine(new QueryExpress().add(QueryCriterions.eq(ClientLineField.LINE_ID, Long.valueOf(lineId))));
            if (clientLine == null) {
                return new ModelAndView("redirect:/collection/game/lineitem/createpage?lineid=" + lineId + "&itemtype=" + itemType + "&gameid=" + gameId + "&msg=game.exists");
            }

            ClientLineItem clientLineItem = JoymeAppServiceSngl.get().getClientLineItem(new QueryExpress()
                    .add(QueryCriterions.eq(ClientLineItemField.LINE_ID, Long.valueOf(lineId)))
                    .add(QueryCriterions.eq(ClientLineItemField.DIRECT_ID, String.valueOf(gameId)))
                    .add(QueryCriterions.eq(ClientLineItemField.VALID_STATUS, ValidStatus.VALID.getCode())));
            if (clientLineItem != null) {
                return new ModelAndView("redirect:/collection/game/lineitem/createpage?lineid=" + lineId + "&itemtype=" + itemType + "&gameid=" + gameId + "&msg=game.exists");
            }

            GameDB gameDB = GameResourceServiceSngl.get().getGameDB(new BasicDBObject().append("_id", gameId).append(GameDBField.VALIDSTATUS.getColumn(), GameDbStatus.VALID.getCode()), true);
            if (gameDB == null || !gameDB.getValidStatus().equals(GameDbStatus.VALID)) {
                return new ModelAndView("redirect:/collection/game/lineitem/createpage?lineid=" + lineId + "&itemtype=" + itemType + "&gameid=" + gameId + "&msg=game.is.null");
            }
            clientLineItem = new ClientLineItem();
            clientLineItem.setDirectId(String.valueOf(gameId));
            clientLineItem.setLineId(Long.valueOf(lineId));
            clientLineItem.setItemDomain(ClientItemDomain.GAME);
            clientLineItem.setItemType(ClientItemType.getByCode(itemType));
            clientLineItem.setPicUrl(gameDB.getGameIcon());

            clientLineItem.setTitle(gameDB.getGameName());
            clientLineItem.setValidStatus(ValidStatus.VALID);
            if (clientLine.getCode().equals(COLLECTION_GAME_HOT_MAJOR)) {
                clientLineItem.setDisplayOrder(gameDB.getPvSum());
            }else {
                clientLineItem.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
            }
            clientLineItem.setItemCreateDate(new Date());
            ClientLineItem returnItem = JoymeAppServiceSngl.get().createClientLineItem(clientLineItem);
            if (returnItem != null) {
                ToolsLog log = new ToolsLog();
                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.CREATE_CLIENT_LINE_ITEM);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("itemId:" + returnItem.getItemId());
                addLog(log);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e: ", e);
            return new ModelAndView("redirect:/collection/game/lineitem/createpage?lineid=" + lineId + "&itemtype=" + itemType + "&gameid=" + gameId + "&msg=system.error");
        }
        return new ModelAndView("redirect:/collection/game/lineitem/list?lineid=" + lineId + "&itemtype=" + itemType);
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyLinePage(@RequestParam(value = "lineid", required = false) Long lineId,
                                       @RequestParam(value = "itemid", required = true) Long itemId,
                                       @RequestParam(value = "itemtype", required = false) Integer itemType,
                                       @RequestParam(value = "validstatus", required = false) String validStatus,
                                       @RequestParam(value = "msg", required = false) String errorMsg) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("lineId", lineId);
        mapMessage.put("itemType", itemType);
        mapMessage.put("errorMsg", errorMsg);
        mapMessage.put("validstatus", validStatus);
        try {
            ClientLineItem clientLineItem = JoymeAppServiceSngl.get().getClientLineItem(new QueryExpress().add(QueryCriterions.eq(ClientLineItemField.ITEM_ID, itemId)));
            if (clientLineItem == null) {
                return new ModelAndView("redirect:/collection/game/lineitem/list?lineid=" + lineId + "&itemtype=" + itemType + "&validstatus=" + validStatus);
            }
            mapMessage.put("item", clientLineItem);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return new ModelAndView("redirect:/collection/game/lineitem/list?lineid=" + lineId + "&itemtype=" + itemType + "&msg=system.error&validstatus=" + validStatus);
        }
        return new ModelAndView("/collection/modifyitem", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modifyLine(@RequestParam(value = "lineid", required = false) Long lineId,
                                   @RequestParam(value = "itemid", required = false) Long itemId,
                                   @RequestParam(value = "itemtype", required = false) Integer itemType,
                                   @RequestParam(value = "gameid", required = false) Long gameId,
                                   @RequestParam(value = "validstatus", required = false) String validStatus) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            ClientLineItem clientLineItem = JoymeAppServiceSngl.get().getClientLineItem(new QueryExpress()
                    .add(QueryCriterions.eq(ClientLineItemField.LINE_ID, lineId))
                    .add(QueryCriterions.eq(ClientLineItemField.DIRECT_ID, String.valueOf(gameId)))
                    .add(QueryCriterions.eq(ClientLineItemField.VALID_STATUS, ValidStatus.VALID.getCode()))
                    .add(QueryCriterions.ne(ClientLineItemField.ITEM_ID, itemId)));
            if (clientLineItem != null) {
                return new ModelAndView("redirect:/collection/game/lineitem/modifypage?lineid=" + lineId + "&itemid=" + itemId + "&itemtype=" + itemType + "&msg=game.exist");
            }

            GameDB gameDB = GameResourceServiceSngl.get().getGameDB(new BasicDBObject().append("_id", gameId).append(GameDBField.VALIDSTATUS.getColumn(), GameDbStatus.VALID.getCode()), false);
            if (gameDB == null || !gameDB.getValidStatus().equals(GameDbStatus.VALID)) {
                return new ModelAndView("redirect:/collection/game/lineitem/modifypage?lineid=" + lineId + "&itemid=" + itemId + "&itemtype=" + itemType + "&msg=game.is.null");
            }
            mapMessage.put("itemid", itemId);

            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(ClientLineItemField.TITLE, gameDB.getGameName());
            updateExpress.set(ClientLineItemField.PIC_URL, gameDB.getGameIcon());
            updateExpress.set(ClientLineItemField.DIRECT_ID, String.valueOf(gameId));
            boolean bool = JoymeAppServiceSngl.get().modifyClientLineItem(updateExpress, itemId);
            if (bool) {
                ToolsLog log = new ToolsLog();
                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.MODIFY_CLIENT_LINE_ITEM);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("itemId:" + itemId);
                addLog(log);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return new ModelAndView("redirect:/collection/game/lineitem/modifypage?lineid=" + lineId + "&itemid=" + itemId + "&itemtype=" + itemType + "&msg=system.error&validstatus=" + validStatus);
        }
        return new ModelAndView("redirect:/collection/game/lineitem/list?lineid=" + lineId + "&itemtype=" + itemType + "&validstatus=" + validStatus);
    }

    @RequestMapping(value = "/delete")
    public ModelAndView delete(@RequestParam(value = "itemid", required = false) Long itemId,
                               @RequestParam(value = "itemtype", required = false) Integer itemType,
                               @RequestParam(value = "lineid", required = false) Long lineId,
                               @RequestParam(value = "validstatus", required = false) String validStatus
    ) {
        if (itemId == null) {
            return new ModelAndView("redirect:/collection/game/lineitem/list?lineid=" + lineId + "&itemtype=" + itemType + "&validstatus=" + validStatus);
        }
        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(ClientLineItemField.VALID_STATUS, ValidStatus.REMOVED.getCode());
        try {
            boolean bool = JoymeAppServiceSngl.get().modifyClientLineItem(updateExpress, itemId);
            if (bool) {
                ToolsLog log = new ToolsLog();
                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.DELETE_CLIENT_LINE_ITEM);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("clientLineItemId:" + itemId);
                addLog(log);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + "ServiceException", e);
            return new ModelAndView("redirect:/collection/game/lineitem/list?lineid=" + lineId + "&itemtype=" + itemType + "&msg=system.error&validstatus=" + validStatus);
        }
        return new ModelAndView("redirect:/collection/game/lineitem/list?lineid=" + lineId + "&itemtype=" + itemType + "&validstatus=" + validStatus);
    }

    @RequestMapping(value = "/recover")
    public ModelAndView modifyLine(@RequestParam(value = "itemid", required = false) Long itemId,
                                   @RequestParam(value = "itemtype", required = false) Integer itemType,
                                   @RequestParam(value = "lineid", required = false) Long lineId,
                                   @RequestParam(value = "validstatus", required = false) String validStatus
    ) {
        if (itemId == null) {
            return new ModelAndView("redirect:/collection/game/lineitem/list?lineid=" + lineId + "&itemtype=" + itemType + "&validstatus=" + validStatus);
        }
        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(ClientLineItemField.VALID_STATUS, ValidStatus.VALID.getCode());
        try {
            boolean bool = JoymeAppServiceSngl.get().modifyClientLineItem(updateExpress, itemId);
            if (bool) {
                ToolsLog log = new ToolsLog();
                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.RECOVER_CLIENT_LINE_ITEM);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("clientLineItemId:" + itemId);
                addLog(log);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + "ServiceException", e);
            return new ModelAndView("redirect:/collection/game/lineitem/list?lineid=" + lineId + "&itemtype=" + itemType + "&msg=system.error&validstatus=" + validStatus);
        }
        return new ModelAndView("redirect:/collection/game/lineitem/list?lineid=" + lineId + "&itemtype=" + itemType + "&validstatus=" + validStatus);
    }

    @RequestMapping(value = "/sort")
    public ModelAndView sort(@RequestParam(value = "lineid", required = false) Long lineId,
                             @RequestParam(value = "itemtype", required = false) Integer itemType,
                             @RequestParam(value = "itemid", required = false) Long itemId,
                             @RequestParam(value = "ordernum", required = false) Integer orderNum,
                             @RequestParam(value = "validstatus", required = false) String validStatus) {
        if (lineId == null || itemId == null) {
            return new ModelAndView("redirect:/collection/game/lineitem/list?lineid=" + lineId + "&itemtype=" + itemType + "&validstatus=" + validStatus);
        }
        try {
            ClientLine clientLine = JoymeAppServiceSngl.get().getClientLine(new QueryExpress().add(QueryCriterions.eq(ClientLineField.LINE_ID, lineId)));
            ClientLineItem item = JoymeAppServiceSngl.get().getClientLineItem(new QueryExpress().add(QueryCriterions.eq(ClientLineItemField.ITEM_ID, itemId)));
            if (clientLine != null && item != null) {
                if (clientLine.getCode().equals(COLLECTION_GAME_HOT_MAJOR)) {
                    BasicDBObject basicDBObject = new BasicDBObject();
                    basicDBObject.put(GameDBField.ID.getColumn(), Long.valueOf(item.getDirectId()));
                    GameDB gameDB = GameResourceServiceSngl.get().getGameDB(basicDBObject, false);
                    if (gameDB != null) {
                        BasicDBObject updateDBObject = new BasicDBObject();
                        updateDBObject.put(GameDBField.PVSUM.getColumn(), orderNum);
                        GameResourceServiceSngl.get().updateGameDB(basicDBObject, updateDBObject);
                        GameResourceServiceSngl.get().putGameCollectionListCache(COLLECTION_GAME_HOT_MAJOR, orderNum, gameDB);
                    }
                }
                UpdateExpress updateExpress = new UpdateExpress();
                updateExpress.set(ClientLineItemField.DISPLAY_ORDER, orderNum);
                JoymeAppServiceSngl.get().modifyClientLineItem(updateExpress, itemId);

                ToolsLog log = new ToolsLog();
                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.SORT_CLIENT_LINE_ITEM);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("clientLineItemId:" + itemId + "," + orderNum);
                addLog(log);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + "ServiceException", e);
            return new ModelAndView("redirect:/collection/game/lineitem/list?lineid=" + lineId + "&itemtype=" + itemType + "&msg=system.error&validstatus=" + validStatus);
        }
        return new ModelAndView("redirect:/collection/game/lineitem/list?lineid=" + lineId + "&itemtype=" + itemType + "&validstatus=" + validStatus);
    }

    @RequestMapping(value = "/updatecache")
    public ModelAndView updateCache(@RequestParam(value = "lineid", required = false) Long lineId,
                                    @RequestParam(value = "linecode", required = false) String lineCode,
                                    @RequestParam(value = "itemtype", required = false) Integer itemType) {
        try {
            GameResourceServiceSngl.get().removeGameCollectionListCache(lineCode);
            Pagination pagination = new Pagination(pageSizeMap.get(itemType), 1, pageSizeMap.get(itemType));

            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(ClientLineItemField.LINE_ID, lineId));
            queryExpress.add(QueryCriterions.eq(ClientLineItemField.VALID_STATUS, ValidStatus.VALID.getCode()));
            queryExpress.add(QuerySort.add(ClientLineItemField.DISPLAY_ORDER, QuerySortOrder.ASC));
            PageRows<ClientLineItem> itemPageRows = JoymeAppServiceSngl.get().queryClientLineItemByPage(queryExpress, pagination);
            if (itemPageRows != null && !CollectionUtil.isEmpty(itemPageRows.getRows())) {
                Map<Long, GameDB> gameDBMap = new HashMap<Long, GameDB>();
                for (ClientLineItem item : itemPageRows.getRows()) {
                    GameDB gameDB = GameResourceServiceSngl.get().getGameDB(new BasicDBObject("_id", Long.parseLong(item.getDirectId())), true);
                    if(gameDB != null){
                        gameDBMap.put(gameDB.getGameDbId(), gameDB);
                    }
                }

                for (ClientLineItem item : itemPageRows.getRows()) {
                    GameDB gameDB = gameDBMap.get(Long.parseLong(item.getDirectId()));
                    if (gameDB != null) {
                        if (lineCode.equals(COLLECTION_GAME_HOT_MAJOR)) {
                            GameResourceServiceSngl.get().putGameCollectionListCache(COLLECTION_GAME_HOT_MAJOR, gameDB.getPvSum(), gameDB);
                            UpdateExpress updateExpress = new UpdateExpress();
                            updateExpress.set(ClientLineItemField.DISPLAY_ORDER, gameDB.getPvSum());
                            JoymeAppServiceSngl.get().modifyClientLineItem(updateExpress, item.getItemId());
                        } else {
                            GameResourceServiceSngl.get().putGameCollectionListCache(lineCode, item.getDisplayOrder(), gameDB);
                        }
                    }
                }
                ToolsLog log = new ToolsLog();
                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.UPDATE_CACHE_CLIENT_LINE);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("lineId:" + lineId);
                addLog(log);

            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + "ServiceException", e);
            return new ModelAndView("redirect:/collection/game/lineitem/list?lineid=" + lineId + "&itemtype=" + itemType + "&msg=system.error");
        }
        return new ModelAndView("redirect:/collection/game/lineitem/list?lineid=" + lineId + "&itemtype=" + itemType);
    }

}
