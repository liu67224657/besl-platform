package com.enjoyf.webapps.tools.webpage.controller.joymeapp;

import com.enjoyf.platform.service.ValidStatus;
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
import com.enjoyf.webapps.tools.weblogic.clientline.ClientLineWebLogic;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-12-10
 * Time: 下午2:08
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/platinum/item")
public class AppPlatinumItemController extends ToolsBaseController {

    @Resource(name = "clientLineWebLogic")
    private ClientLineWebLogic clientLineWebLogic;

    @Resource(name = "i18nSource")
    private ResourceBundleMessageSource i18nSource;

    private static Set<AppRedirectType> redirectTypes = new HashSet<AppRedirectType>();

    static {
        redirectTypes.add(AppRedirectType.WEBVIEW);
    }

    @RequestMapping(value = "/list")
    public ModelAndView lineList(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                                 @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                                 @RequestParam(value = "lineid", required = false) Long lineId,
                                 @RequestParam(value = "itemtype", required = false) Integer itemType,
                                 @RequestParam(value = "validstatus", required = false) String validStatus) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("lineId", lineId);
        mapMessage.put("itemType", itemType);
        mapMessage.put("apps", ClientItemType.APPS.getCode());
        mapMessage.put("events", ClientItemType.EVENTS.getCode());

        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);

        if (lineId == null) {
            return new ModelAndView("/joymeapp/platinum/itemlist");
        }
        try {
            ClientLine clientLine = JoymeAppServiceSngl.get().getClientLine(new QueryExpress().add(QueryCriterions.eq(ClientLineField.LINE_ID, lineId)));
            if (clientLine == null) {
                return new ModelAndView("/joymeapp/platinum/itemlist");
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
        return new ModelAndView("/joymeapp/platinum/itemlist", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createLinePage(@RequestParam(value = "itemtype", required = false) Integer itemType,
                                       @RequestParam(value = "lineid", required = false) Long lineId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("redirectTypes", redirectTypes);
        mapMessage.put("lineId", lineId);
        mapMessage.put("itemType", itemType);
        mapMessage.put("apps", ClientItemType.APPS.getCode());
        mapMessage.put("events", ClientItemType.EVENTS.getCode());

        return new ModelAndView("/joymeapp/platinum/createitem", mapMessage);
    }


    @RequestMapping(value = "/create")
    public ModelAndView createLine(@RequestParam(value = "title", required = false) String title,
                                   @RequestParam(value = "desc", required = false) String desc,
                                   @RequestParam(value = "pic", required = false) String pic,
                                   @RequestParam(value = "url", required = false) String url,
                                   @RequestParam(value = "size", required = false) String size,
                                   @RequestParam(value = "lineid", required = false) Long lineId,
                                   @RequestParam(value = "itemtype", required = false) Integer itemType,
                                   @RequestParam(value = "startdate", required = false) Date startDate,
                                   @RequestParam(value = "enddate", required = false) Date endDate,
                                   @RequestParam(value = "hot", required = false) boolean hot
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("title", title);
        mapMessage.put("desc", desc);
        mapMessage.put("pic", pic);
        mapMessage.put("url", url);
        mapMessage.put("size", size);
        mapMessage.put("lineId", lineId);
        mapMessage.put("itemType", itemType);
        mapMessage.put("startDate", startDate);
        mapMessage.put("endDate", endDate);
        mapMessage.put("hot", hot);
        try {
            ClientLineItem item = new ClientLineItem();
            item.setLineId(lineId);
            item.setTitle(title);
            item.setDesc(desc);
            item.setPicUrl(pic);
            item.setUrl(url);
            item.setItemType(ClientItemType.getByCode(itemType));
            item.setRedirectType(AppRedirectType.WEBVIEW);
            //TODO
            //  item.setDisplayIcon(hot);
            item.setItemDomain(ClientItemDomain.DEFAULT);
            item.setItemCreateDate(new Date());
            item.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
            item.setValidStatus(ValidStatus.VALID);

            if (startDate != null && endDate != null) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String start = format.format(startDate);
                String end = format.format(endDate);
                ParamTextJson param = new ParamTextJson();
                param.setStartDate(start);
                param.setEndDate(end);
                param.setSize(size);
                item.setParam(param);
            }

            item.setStateDate(startDate);

            JoymeAppServiceSngl.get().createClientLineItem(item);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/platinum/item/list?lineid=" + lineId);
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyLinePage(@RequestParam(value = "lineid", required = false) Long lineId,
                                       @RequestParam(value = "itemid", required = true) Long itemId,
                                       @RequestParam(value = "itemtype", required = false) Integer itemType) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("redirectTypes", redirectTypes);
        mapMessage.put("lineId", lineId);
        mapMessage.put("itemType", itemType);
        mapMessage.put("apps", ClientItemType.APPS.getCode());
        mapMessage.put("events", ClientItemType.EVENTS.getCode());
        try {
            ClientLineItem clientLineItem = JoymeAppServiceSngl.get().getClientLineItem(new QueryExpress().add(QueryCriterions.eq(ClientLineItemField.ITEM_ID, itemId)));
            mapMessage.put("item", clientLineItem);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/joymeapp/platinum/modifyitem", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modifyLine(@RequestParam(value = "itemid", required = false) Long itemId,
                                   @RequestParam(value = "title", required = false) String title,
                                   @RequestParam(value = "desc", required = false) String desc,
                                   @RequestParam(value = "pic", required = false) String pic,
                                   @RequestParam(value = "url", required = false) String url,
                                   @RequestParam(value = "size", required = false) String size,
                                   @RequestParam(value = "lineid", required = false) Long lineId,
                                   @RequestParam(value = "itemtype", required = false) Integer itemType,
                                   @RequestParam(value = "startdate", required = false) Date startDate,
                                   @RequestParam(value = "enddate", required = false) Date endDate,
                                   @RequestParam(value = "hot", required = false) boolean hot
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        mapMessage.put("lineId", lineId);
        mapMessage.put("itemType", itemType);

        try {
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(ClientLineItemField.TITLE, title);
            updateExpress.set(ClientLineItemField.DESC, desc);
            updateExpress.set(ClientLineItemField.PIC_URL, pic);
            updateExpress.set(ClientLineItemField.URL, url);

            if (startDate != null && endDate != null) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String start = format.format(startDate);
                String end = format.format(endDate);
                ParamTextJson param = new ParamTextJson();
                param.setStartDate(start);
                param.setEndDate(end);
                param.setSize(size);
                updateExpress.set(ClientLineItemField.ITEM_PARAM, param.toJson());
            }

            if (startDate != null && endDate != null) {
                updateExpress.set(ClientLineItemField.STATE_DATE, startDate);
            }
            JoymeAppServiceSngl.get().modifyClientLineItem(updateExpress, itemId);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/platinum/item/list?lineid=" + lineId);
    }

    @RequestMapping(value = "/delete")
    public ModelAndView delete(@RequestParam(value = "itemid", required = false) Long itemId,
                               @RequestParam(value = "lineid", required = false) Long lineId
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        if (itemId == null) {
            return new ModelAndView("redirect:/platinum/item/list?lineid=" + lineId);
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

        return new ModelAndView("redirect:/platinum/item/list?lineid=" + lineId);
    }

    @RequestMapping(value = "/recover")
    public ModelAndView modifyLine(@RequestParam(value = "itemid", required = false) Long itemId,
                                   @RequestParam(value = "lineid", required = false) Long lineId
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        if (itemId == null) {
            return new ModelAndView("redirect:/platinum/item/list?lineid=" + lineId);
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

        return new ModelAndView("redirect:/platinum/item/list?lineid=" + lineId);
    }

    @RequestMapping(value = "/sort/{sort}")
    public ModelAndView sort(@PathVariable(value = "sort") String sort,
                             @RequestParam(value = "lineid", required = false) Long lineId,
                             @RequestParam(value = "itemid", required = false) Long itemId) {
        JsonBinder binder = JsonBinder.buildNormalBinder();
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        if (lineId == null || itemId == null) {
            return new ModelAndView("redirect:/platinum/item/list?lineid=" + lineId);
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
        return new ModelAndView("redirect:/platinum/item/list?lineid=" + lineId);
    }

}
