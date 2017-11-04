package com.enjoyf.webapps.tools.webpage.controller.joymeapp.wikiapp;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * Created by zhitaoshi on 2015/4/15.
 */
@Controller
@RequestMapping(value = "/joymeapp/wikiapp/index/item")
public class WikiAppIndexItemController extends ToolsBaseController {

    private static Set<ClientItemDomain> itemDomainSet = new HashSet<ClientItemDomain>();
    private static Set<AppRedirectType> redirectTypeSet = new HashSet<AppRedirectType>();

    static {
        itemDomainSet.add(ClientItemDomain.JOYMEWIKI);
        itemDomainSet.add(ClientItemDomain.DEFAULT);

        redirectTypeSet.add(AppRedirectType.NOTHING);
        redirectTypeSet.add(AppRedirectType.OPEN_APP);
        redirectTypeSet.add(AppRedirectType.DEFAULT_WEBVIEW);
        redirectTypeSet.add(AppRedirectType.DEFAULT_NOTICE);
        redirectTypeSet.add(AppRedirectType.REDIRECT_DOWNLOAD);
        redirectTypeSet.add(AppRedirectType.USER_HOME);
        redirectTypeSet.add(AppRedirectType.WIKI_RANK);
        redirectTypeSet.add(AppRedirectType.HOT_PIC);
        redirectTypeSet.add(AppRedirectType.HOT_PIC_DETAIL);
        redirectTypeSet.add(AppRedirectType.WIKI_ACTIVE_USERS);
        redirectTypeSet.add(AppRedirectType.INDEX_MORE);
        redirectTypeSet.add(AppRedirectType.INDEX);
        redirectTypeSet.add(AppRedirectType.DISCOVER);
        redirectTypeSet.add(AppRedirectType.MY_HOME);
        redirectTypeSet.add(AppRedirectType.WIKI_INDEX);
    }

    @RequestMapping(value = "/list")
    public ModelAndView lineList(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                                 @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                                 @RequestParam(value = "lineid", required = false) Long lineId,
                                 @RequestParam(value = "linecode", required = false) String lineCode,
                                 @RequestParam(value = "itemtype", required = false) Integer itemType) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            map.put("lineId", lineId);
            map.put("lineCode", lineCode);
            map.put("itemType", itemType);

            int curPage = (pageStartIndex / pageSize) + 1;
            Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);

            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(ClientLineItemField.LINE_ID, lineId));
            queryExpress.add(QuerySort.add(ClientLineItemField.DISPLAY_ORDER, QuerySortOrder.ASC));

            PageRows<ClientLineItem> pageRows = JoymeAppServiceSngl.get().queryClientLineItemByPage(queryExpress, pagination);
            map.put("list", pageRows == null ? null : pageRows.getRows());
            map.put("page", pageRows == null ? null : pageRows.getPage());
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
            map = putErrorMessage(map, "system.error");
        }
        return new ModelAndView("/joymeapp/wikiapp/index/itemlist", map);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createLine(@RequestParam(value = "lineid", required = false) Long lineId,
                                   @RequestParam(value = "linecode", required = false) String lineCode,
                                   @RequestParam(value = "itemtype", required = false) Integer itemType) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("lineId", lineId);
        map.put("lineCode", lineCode);
        map.put("itemType", itemType);
        try {
            map.put("itemDomainSet", itemDomainSet);
            map.put("redirectTypeSet", redirectTypeSet);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
        }
        return new ModelAndView("/joymeapp/wikiapp/index/createitem", map);
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "lineid", required = false) Long lineId,
                               @RequestParam(value = "linecode", required = false) String lineCode,
                               @RequestParam(value = "itemtype", required = false) Integer itemType,

                               @RequestParam(value = "itemdomain", required = false) Integer itemDomain,
                               @RequestParam(value = "title", required = false) String title,
                               @RequestParam(value = "desc", required = false) String desc,
                               @RequestParam(value = "picurl", required = false) String picUrl,
                               @RequestParam(value = "redirecttype", required = false) Integer redirectType,
                               @RequestParam(value = "url", required = false) String url,
                               @RequestParam(value = "directid", required = false) String directId) {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("lineId", lineId);
        map.put("lineCode", lineCode);
        map.put("itemType", itemType);
        map.put("itemDomain", itemDomain);
        map.put("title", title);
        map.put("desc", desc);
        map.put("picUrl", picUrl);
        map.put("redirectType", redirectType);
        map.put("url", url);
        map.put("directId", directId);

        if (lineId == null || itemType == null) {
            map.put("itemDomainSet", itemDomainSet);
            map.put("redirectTypeSet", redirectTypeSet);
            return new ModelAndView("/joymeapp/wikiapp/index/createitem", map);
        }
        try {
            ClientLineItem clientLineItem = new ClientLineItem();
            clientLineItem.setLineId(lineId);
            clientLineItem.setItemType(ClientItemType.getByCode(itemType));
            clientLineItem.setItemDomain(ClientItemDomain.getByCode(itemDomain));
            clientLineItem.setTitle(title);
            clientLineItem.setDesc(desc);
            clientLineItem.setPicUrl(picUrl);
            clientLineItem.setRedirectType(AppRedirectType.getByCode(redirectType));
            clientLineItem.setUrl(url);
            clientLineItem.setDirectId(directId);

            clientLineItem.setDisplayOrder((int) (Integer.MAX_VALUE - (System.currentTimeMillis() / 1000)));
            clientLineItem.setValidStatus(ValidStatus.VALID);

            ParamTextJson textJson = new ParamTextJson();
            textJson.setCreateUserId(getCurrentUser().getUserid());
            textJson.setCreateDate(new Date().getTime());
            clientLineItem.setParam(textJson);

            JoymeAppServiceSngl.get().createClientLineItem(clientLineItem);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
            map = putErrorMessage(map, "system.error");
            map.put("itemDomainSet", itemDomainSet);
            map.put("redirectTypeSet", redirectTypeSet);
            return new ModelAndView("/joymeapp/wikiapp/index/createitem", map);
        }
        return new ModelAndView("redirect:/joymeapp/wikiapp/index/item/list?lineid=" + lineId + "&linecode=" + lineCode + "&itemtype=" + itemType);
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyLine(@RequestParam(value = "lineid", required = false) Long lineId,
                                   @RequestParam(value = "linecode", required = false) String lineCode,
                                   @RequestParam(value = "itemtype", required = false) Integer itemType,
                                   @RequestParam(value = "itemid", required = false) Long itemId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("lineId", lineId);
        map.put("lineCode", lineCode);
        map.put("itemType", itemType);
        map.put("itemId", itemId);
        try {
            ClientLineItem item = JoymeAppServiceSngl.get().getClientLineItemByCache(lineCode, itemId);
            if (item == null) {
                return new ModelAndView("redirect:/joymeapp/wikiapp/index/item/list?lineid=" + lineId + "&linecode=" + lineCode + "&itemtype=" + itemType);
            }
            map.put("item", item);

            map.put("itemDomainSet", itemDomainSet);
            map.put("redirectTypeSet", redirectTypeSet);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
        }
        return new ModelAndView("/joymeapp/wikiapp/index/modifyitem", map);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "lineid", required = false) Long lineId,
                               @RequestParam(value = "linecode", required = false) String lineCode,
                               @RequestParam(value = "itemtype", required = false) Integer itemType,

                               @RequestParam(value = "itemid", required = false) Long itemId,
                               @RequestParam(value = "itemdomain", required = false) Integer itemDomain,
                               @RequestParam(value = "title", required = false) String title,
                               @RequestParam(value = "desc", required = false) String desc,
                               @RequestParam(value = "picurl", required = false) String picUrl,
                               @RequestParam(value = "redirecttype", required = false) Integer redirectType,
                               @RequestParam(value = "url", required = false) String url,
                               @RequestParam(value = "directid", required = false) String directId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("lineId", lineId);
        map.put("lineCode", lineCode);
        map.put("itemType", itemType);

        map.put("itemId", itemId);
        map.put("itemDomain", itemDomain);
        map.put("title", title);
        map.put("desc", desc);
        map.put("picUrl", picUrl);
        map.put("redirectType", redirectType);
        map.put("url", url);
        map.put("directId", directId);
        try {
            ClientLineItem item = JoymeAppServiceSngl.get().getClientLineItemByCache(lineCode, itemId);
            if (item == null) {
                map = putErrorMessage(map, "内容不存在！");
                return new ModelAndView("/joymeapp/wikiapp/index/modifyitem", map);
            }
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(ClientLineItemField.ITEM_DOMAIN, itemDomain);
            updateExpress.set(ClientLineItemField.TITLE, title);
            updateExpress.set(ClientLineItemField.DESC, desc);
            updateExpress.set(ClientLineItemField.PIC_URL, picUrl);
            updateExpress.set(ClientLineItemField.REDIRCT_TYPE, redirectType);
            updateExpress.set(ClientLineItemField.URL, url);
            updateExpress.set(ClientLineItemField.DIRECT_ID, directId);

            ParamTextJson param = item.getParam();
            param.setModifyDate(new Date().getTime());
            param.setModifyUserId(getCurrentUser().getUserid());
            updateExpress.set(ClientLineItemField.ITEM_PARAM, param.toJson());

            boolean bool = JoymeAppServiceSngl.get().modifyClientLineItem(updateExpress, itemId);
            if (bool) {
                writeToolsLog(LogOperType.WIKIAPP_INDEX_CLIENTLINE_MODIFY, "WIKI大端首页clientlineitem修改:lineCode:" + lineCode + ",itemId:" + itemId);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
        }
        return new ModelAndView("redirect:/joymeapp/wikiapp/index/item/list?lineid=" + lineId + "&linecode=" + lineCode + "&itemtype=" + itemType);
    }
//
//    @RequestMapping(value = "/detail")
//    public ModelAndView detail(@RequestParam(value = "linecode", required = false) String lineCode) {
//        Map<String, Object> map = new HashMap<String, Object>();
//        try {
//            ClientLine clientLine = JoymeAppServiceSngl.get().getClientLineByCode(lineCode);
//            if (clientLine == null) {
//                map = putErrorMessage(map, "模块不存在");
//                return new ModelAndView("/joymeapp/wikiapp/index/modifyline", map);
//            }
//            map.put("clientLine", clientLine);
//            map.put("platformSet", platformSet);
//            map.put("itemTypeSet", itemTypeSet);
//        } catch (Exception e) {
//            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
//        }
//        return new ModelAndView("/joymeapp/wikiapp/index/linedetail", map);
//    }
//
//    @RequestMapping(value = "/remove")
//    public ModelAndView remove(@RequestParam(value = "lineid", required = false) Long lineId,
//                               @RequestParam(value = "linecode", required = false) String lineCode,
//                               @RequestParam(value = "linetype", required = false) Integer lineType,
//                               @RequestParam(value = "platform", required = false) Integer platform) {
//        Map<String, Object> map = new HashMap<String, Object>();
//        try {
//            ClientLine clientLine = JoymeAppServiceSngl.get().getClientLineByCode(lineCode);
//            if (clientLine == null) {
//                return new ModelAndView("redirect:/joymeapp/wikiapp/index/list?platform=" + platformSet);
//            }
//            UpdateExpress updateExpress = new UpdateExpress();
//            updateExpress.set(ClientLineField.VALID_STATUS, ValidStatus.REMOVED.getCode());
//            updateExpress.set(ClientLineField.MODIFY_DATE, new Date());
//            updateExpress.set(ClientLineField.MODIFY_USERID, getCurrentUser().getUserid());
//
//            Map<String, Object> param = new HashMap<String, Object>();
//            param.put("lineid", lineId);
//            param.put("linecode", lineCode);
//            param.put("linetype", lineType);
//            param.put("platform", platform);
//            param.put("status", ValidStatus.REMOVED.getCode());
//            boolean bool = JoymeAppServiceSngl.get().modifyClientLineByCache(updateExpress, param);
//            if (bool) {
//                writeToolsLog(LogOperType.WIKIAPP_INDEX_CLIENTLINE_MODIFY, "WIKI大端首页clientline修改:" + lineCode);
//            }
//        } catch (Exception e) {
//            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
//        }
//        return new ModelAndView("redirect:/joymeapp/wikiapp/index/list?platform=" + platform);
//    }
//
//    @RequestMapping(value = "/recover")
//    public ModelAndView recover(@RequestParam(value = "lineid", required = false) Long lineId,
//                                @RequestParam(value = "linecode", required = false) String lineCode,
//                                @RequestParam(value = "linetype", required = false) Integer lineType,
//                                @RequestParam(value = "platform", required = false) Integer platform) {
//        Map<String, Object> map = new HashMap<String, Object>();
//        try {
//            ClientLine clientLine = JoymeAppServiceSngl.get().getClientLineByCode(lineCode);
//            if (clientLine == null) {
//                return new ModelAndView("redirect:/joymeapp/wikiapp/index/list?platform=" + platformSet);
//            }
//            UpdateExpress updateExpress = new UpdateExpress();
//            updateExpress.set(ClientLineField.VALID_STATUS, ValidStatus.REMOVED.getCode());
//            updateExpress.set(ClientLineField.MODIFY_DATE, new Date());
//            updateExpress.set(ClientLineField.MODIFY_USERID, getCurrentUser().getUserid());
//
//            Map<String, Object> param = new HashMap<String, Object>();
//            param.put("lineid", lineId);
//            param.put("linecode", lineCode);
//            param.put("linetype", lineType);
//            param.put("platform", platform);
//            param.put("status", ValidStatus.VALID.getCode());
//            boolean bool = JoymeAppServiceSngl.get().modifyClientLineByCache(updateExpress, param);
//            if (bool) {
//                writeToolsLog(LogOperType.WIKIAPP_INDEX_CLIENTLINE_MODIFY, "WIKI大端首页clientline修改:" + lineCode);
//            }
//        } catch (Exception e) {
//            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
//        }
//        return new ModelAndView("redirect:/joymeapp/wikiapp/index/list?platform=" + platform);
//    }
//
//    @RequestMapping(value = "/sort")
//    public ModelAndView sort(@RequestParam(value = "lineid", required = false) Long lineId,
//                             @RequestParam(value = "linecode", required = false) String lineCode,
//                             @RequestParam(value = "linetype", required = false) Integer lineType,
//                             @RequestParam(value = "platform", required = false) Integer platform,
//                             @RequestParam(value = "oldorder", required = false) Integer oldOrder,
//                             @RequestParam(value = "neworder", required = false) Integer newOrder) {
//        Map<String, Object> map = new HashMap<String, Object>();
//        try {
//            UpdateExpress updateExpress = new UpdateExpress();
//            updateExpress.set(ClientLineField.DISPLAY_ORDER, newOrder);
//            updateExpress.set(ClientLineField.MODIFY_DATE, new Date());
//            updateExpress.set(ClientLineField.MODIFY_USERID, getCurrentUser().getUserid());
//
//            Map<String, Object> param = new HashMap<String, Object>();
//            param.put("lineid", lineId);
//            param.put("linecode", lineCode);
//            param.put("linetype", lineType);
//            param.put("platform", platform);
//            param.put("incorder", Double.valueOf(newOrder - oldOrder));
//            boolean bool = JoymeAppServiceSngl.get().modifyClientLineByCache(updateExpress, param);
//            if (bool) {
//                writeToolsLog(LogOperType.WIKIAPP_INDEX_CLIENTLINE_MODIFY, "WIKI大端首页clientline修改:" + lineCode);
//            }
//        } catch (Exception e) {
//            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
//        }
//        return new ModelAndView("redirect:/joymeapp/wikiapp/index/list?platform=" + platform);
//    }

}
