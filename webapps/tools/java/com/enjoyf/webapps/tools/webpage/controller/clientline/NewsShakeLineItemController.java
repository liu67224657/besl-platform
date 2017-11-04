package com.enjoyf.webapps.tools.webpage.controller.clientline;

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
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import com.mongodb.BasicDBObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-10-15
 * Time: 上午10:16
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/shake/line/item")
public class NewsShakeLineItemController extends ToolsBaseController {

    private static Set<AppRedirectType> redirectTypes = new HashSet<AppRedirectType>();

    static {
        redirectTypes.add(AppRedirectType.WEBVIEW);
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
            return new ModelAndView("/clientline/newsshake/itemlist");
        }
        try {
            ClientLine clientLine = JoymeAppServiceSngl.get().getClientLine(new QueryExpress().add(QueryCriterions.eq(ClientLineField.LINE_ID, lineId)));
            if (clientLine == null) {
                return new ModelAndView("/clientline/newsshake/itemlist");
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
            queryExpress.add(QuerySort.add(ClientLineItemField.DISPLAY_ORDER, QuerySortOrder.ASC));
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
        return new ModelAndView("/clientline/newsshake/itemlist", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createLinePage(@RequestParam(value = "itemtype", required = false) Integer itemType,
                                       @RequestParam(value = "lineid", required = false) Long lineId,
                                       @RequestParam(value = "linecode", required = false) String lineCode,
                                       @RequestParam(value = "msg", required = false) String errorMsg) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("lineId", lineId);
        mapMessage.put("itemType", itemType);
        mapMessage.put("lineCode", lineCode);
        mapMessage.put("errorMsg", errorMsg);
        return new ModelAndView("/clientline/newsshake/itemcreatepage", mapMessage);
    }


    @RequestMapping(value = "/create")
    public ModelAndView createLine(@RequestParam(value = "directid", required = false) String directId,
                                   @RequestParam(value = "shakeweight", required = false, defaultValue = "0") Integer shakeWeight,
                                   @RequestParam(value = "lineid", required = false) String lineId,
                                   @RequestParam(value = "linecode", required = false) String lineCode,
                                   @RequestParam(value = "updatecache", required = false, defaultValue = "false") String updateCache) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            if (updateCache.equals("true")) {
                boolean bool = JoymeAppServiceSngl.get().removeGameIdByWeight(lineCode);
                if (bool) {
                    QueryExpress queryExpress = new QueryExpress();
                    queryExpress.add(QueryCriterions.eq(ClientLineItemField.LINE_ID, Long.valueOf(lineId)));
                    queryExpress.add(QueryCriterions.gt(ClientLineItemField.CONTENTID, 0l));
                    queryExpress.add(QueryCriterions.eq(ClientLineItemField.VALID_STATUS, ValidStatus.VALID.getCode()));
                    int cp = 0;
                    Pagination page = null;
                    List<String> idList = new ArrayList<String>();
                    do {
                        cp = cp + 1;
                        page = new Pagination(500 * cp, cp, 500);
                        PageRows<ClientLineItem> itemPageRows = JoymeAppServiceSngl.get().queryClientLineItemByPage(queryExpress, page);
                        if (itemPageRows != null && !CollectionUtil.isEmpty(itemPageRows.getRows())) {
                            for (ClientLineItem item : itemPageRows.getRows()) {
                                if (item != null) {
                                    int weight = (int) item.getContentid();
                                    String gameId = item.getDirectId();
                                    for (int i = 0; i < weight; i++) {
                                        idList.add(gameId);
                                    }
                                }
                            }
                        }
                    } while (!page.isLastPage());
                    if (!CollectionUtil.isEmpty(idList)) {
                        Collections.shuffle(idList);
                        for (String id : idList) {
                            JoymeAppServiceSngl.get().putGameIdByWeight(lineCode, id);
                        }
                    }
                }
            } else {
                long did = 0l;
                try {
                    did = Long.parseLong(directId);
                } catch (NumberFormatException e) {
                    return new ModelAndView("redirect:/shake/line/item/createpage?lineid=" + lineId + "&msg=directid.is.null");
                }
                if (did == 0l) {
                    return new ModelAndView("redirect:/shake/line/item/createpage?lineid=" + lineId + "&msg=directid.is.null");
                }
                if (StringUtil.isEmpty(directId)) {
                    return new ModelAndView("redirect:/shake/line/item/createpage?lineid=" + lineId + "&msg=directid.is.null");
                }
                ClientLineItem clientLineItem = JoymeAppServiceSngl.get().getClientLineItem(new QueryExpress()
                        .add(QueryCriterions.eq(ClientLineItemField.LINE_ID, Long.valueOf(lineId)))
                        .add(QueryCriterions.eq(ClientLineItemField.VALID_STATUS, ValidStatus.VALID.getCode()))
                        .add(QueryCriterions.eq(ClientLineItemField.DIRECT_ID, directId)));
                if (clientLineItem != null) {
                    return new ModelAndView("redirect:/shake/line/item/createpage?lineid=" + lineId + "&msg=directid.exist");
                }

                GameDB gameDB = GameResourceServiceSngl.get().getGameDB(new BasicDBObject().append("_id", did).append(GameDBField.VALIDSTATUS.getColumn(), GameDbStatus.VALID.getCode()), false);
                if (gameDB == null || !gameDB.getValidStatus().equals(GameDbStatus.VALID)) {
                    return new ModelAndView("redirect:/shake/line/item/createpage?lineid=" + lineId + "&msg=gamedb.is.null");
                }
                clientLineItem = new ClientLineItem();
                clientLineItem.setDirectId(directId);
                clientLineItem.setLineId(Integer.parseInt(lineId));
                clientLineItem.setItemDomain(ClientItemDomain.GAME);
                clientLineItem.setItemType(ClientItemType.GAME);
                clientLineItem.setPicUrl(gameDB.getGameIcon());

                clientLineItem.setTitle(gameDB.getGameName());
                clientLineItem.setValidStatus(ValidStatus.VALID);
                clientLineItem.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
                clientLineItem.setItemCreateDate(new Date());

                //contentid 用于 权重
                clientLineItem.setContentid(shakeWeight);
                ClientLineItem returnClientItem = JoymeAppServiceSngl.get().createClientLineItem(clientLineItem);
                for (int i = 0; i < shakeWeight; i++) {
                    JoymeAppServiceSngl.get().putGameIdByWeight(lineCode, directId);
                }
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e: ", e);
            return new ModelAndView("redirect:/shake/line/item/createpage?lineid=" + lineId + "&msg=system.error");
        }
        return new ModelAndView("redirect:/shake/line/item/list?lineid=" + lineId, mapMessage);
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyLinePage(@RequestParam(value = "lineid", required = false) Long lineId,
                                       @RequestParam(value = "itemid", required = true) Long itemId,
                                       @RequestParam(value = "itemtype", required = false) Integer itemType,
                                       @RequestParam(value = "msg", required = false) String errorMsg) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("lineId", lineId);
        mapMessage.put("errorMsg", errorMsg);
        try {
            ClientLineItem clientLineItem = JoymeAppServiceSngl.get().getClientLineItem(new QueryExpress().add(QueryCriterions.eq(ClientLineItemField.ITEM_ID, itemId)));
            mapMessage.put("item", clientLineItem);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/clientline/newsshake/itemmodifypage", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modifyLine(@RequestParam(value = "lineid", required = false) String lineId,
                                   @RequestParam(value = "itemid", required = false) String itemId,
                                   @RequestParam(value = "directid", required = false) String directId,
                                   @RequestParam(value = "shakeweight", required = false, defaultValue = "0") Long shakeWeight,
                                   @RequestParam(value = "itemids", required = false) String itemIdStr,
                                   @RequestParam(value = "picurl", required = false) String picUrl,
                                   @RequestParam(value = "itemname", required = false) String itemName) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            if (StringUtil.isEmpty(itemIdStr)) {
                ClientLineItem clientLineItem = JoymeAppServiceSngl.get().getClientLineItem(new QueryExpress()
                        .add(QueryCriterions.eq(ClientLineItemField.LINE_ID, Long.valueOf(lineId)))
                        .add(QueryCriterions.eq(ClientLineItemField.DIRECT_ID, directId))
                        .add(QueryCriterions.eq(ClientLineItemField.VALID_STATUS, ValidStatus.VALID.getCode()))
                        .add(QueryCriterions.ne(ClientLineItemField.ITEM_ID, Long.valueOf(itemId))));
                if (clientLineItem != null) {
                    return new ModelAndView("redirect:/shake/line/item/createpage?lineid=" + lineId + "&itemid=" + itemId + "&msg=directid.exist");
                }

                GameDB gameDB = GameResourceServiceSngl.get().getGameDB(new BasicDBObject().append("_id", Long.parseLong(directId)).append(GameDBField.VALIDSTATUS.getColumn(), GameDbStatus.VALID.getCode()), false);
                if (gameDB == null || !gameDB.getValidStatus().equals(GameDbStatus.VALID)) {
                    return new ModelAndView("redirect:/shake/line/item/modifypage?lineid=" + lineId + "&itemid=" + itemId + "&msg=gamedb.is.null");
                }
                mapMessage.put("itemid", itemId);

                UpdateExpress updateExpress = new UpdateExpress();
                updateExpress.set(ClientLineItemField.TITLE, StringUtil.isEmpty(itemName) ? gameDB.getGameName() : itemName);
                updateExpress.set(ClientLineItemField.PIC_URL, StringUtil.isEmpty(picUrl) ? gameDB.getGameIcon() : picUrl);
                updateExpress.set(ClientLineItemField.DIRECT_ID, directId);
                updateExpress.set(ClientLineItemField.CONTENTID, shakeWeight);
                JoymeAppServiceSngl.get().modifyClientLineItem(updateExpress, Long.parseLong(itemId));
            } else {
                String[] itemIdArr = itemIdStr.split("\\|");
                if (CollectionUtil.isEmpty(itemIdArr)) {
                    return new ModelAndView("redirect:/shake/line/item/list?lineid=" + lineId + "&itemid=" + itemId);
                }
                for (String idStr : itemIdArr) {
                    UpdateExpress updateExpress = new UpdateExpress();
                    updateExpress.set(ClientLineItemField.CONTENTID, shakeWeight);
                    JoymeAppServiceSngl.get().modifyClientLineItem(updateExpress, Long.parseLong(idStr));
                }
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return new ModelAndView("redirect:/shake/line/item/modifypage?lineid=" + lineId + "&itemid=" + itemId + "&msg=system.error");
        }
        return new ModelAndView("redirect:/shake/line/item/list?lineid=" + lineId);
    }

    @RequestMapping(value = "/delete")
    public ModelAndView delete(@RequestParam(value = "itemid", required = false) Long itemId,
                               @RequestParam(value = "lineid", required = false) Long lineId
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("lineid", lineId);
        if (itemId == null) {
            return new ModelAndView("redirect:/shake/line/item/list?lineid=" + lineId);
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
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/shake/line/item/list?lineid=" + lineId);
    }

    @RequestMapping(value = "/recover")
    public ModelAndView modifyLine(@RequestParam(value = "itemid", required = false) Long itemId,
                                   @RequestParam(value = "lineid", required = false) Long lineId
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("lineid", lineId);
        if (itemId == null) {
            return new ModelAndView("redirect:/shake/line/item/list?lineid=" + lineId);
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
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/shake/line/item/list?lineid=" + lineId);
    }

    @RequestMapping(value = "/sort/{sort}")
    public ModelAndView sort(@PathVariable(value = "sort") String sort,
                             @RequestParam(value = "lineid", required = false) Long lineId,
                             @RequestParam(value = "itemid", required = false) Long itemId) {
        JsonBinder binder = JsonBinder.buildNormalBinder();
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("lineid", lineId);
        if (lineId == null || itemId == null) {
            return new ModelAndView("redirect:/shake/line/item/list?lineid=" + lineId);
        }
        Long returnItemId = null;
        try {
            UpdateExpress updateExpress1 = new UpdateExpress();
            UpdateExpress updateExpress2 = new UpdateExpress();
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(ClientLineItemField.LINE_ID, lineId));
            ClientLineItem clientLineItem = JoymeAppServiceSngl.get().getClientLineItem(new QueryExpress().add(QueryCriterions.eq(ClientLineItemField.ITEM_ID, itemId)));
            if (clientLineItem == null) {
                return null;
            }
            queryExpress.add(QueryCriterions.eq(ClientLineItemField.VALID_STATUS, ValidStatus.VALID.getCode()));
            if (sort.equals("up")) {
                queryExpress.add(QueryCriterions.lt(ClientLineItemField.DISPLAY_ORDER, clientLineItem.getDisplayOrder()));
                queryExpress.add(QuerySort.add(ClientLineItemField.DISPLAY_ORDER, QuerySortOrder.DESC));
            } else {
                queryExpress.add(QueryCriterions.gt(ClientLineItemField.DISPLAY_ORDER, clientLineItem.getDisplayOrder()));
                queryExpress.add(QuerySort.add(ClientLineItemField.DISPLAY_ORDER, QuerySortOrder.ASC));
            }

            PageRows<ClientLineItem> pageRows = JoymeAppServiceSngl.get().queryClientLineItemByPage(queryExpress, new Pagination(1, 1, 1));
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                updateExpress1.set(ClientLineItemField.DISPLAY_ORDER, clientLineItem.getDisplayOrder());
                updateExpress1.set(ClientLineItemField.ITEM_CREATE_DATE, clientLineItem.getItemCreateDate());
                JoymeAppServiceSngl.get().modifyClientLineItem(updateExpress1, pageRows.getRows().get(0).getItemId());


                updateExpress2.set(ClientLineItemField.DISPLAY_ORDER, pageRows.getRows().get(0).getDisplayOrder());
                updateExpress2.set(ClientLineItemField.ITEM_CREATE_DATE, pageRows.getRows().get(0).getItemCreateDate());
                JoymeAppServiceSngl.get().modifyClientLineItem(updateExpress2, clientLineItem.getItemId());
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + "ServiceException", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/shake/line/item/list?lineid=" + lineId);
    }

}
