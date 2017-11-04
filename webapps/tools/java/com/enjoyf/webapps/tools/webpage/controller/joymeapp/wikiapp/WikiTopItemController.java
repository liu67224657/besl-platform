package com.enjoyf.webapps.tools.webpage.controller.joymeapp.wikiapp;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 15-4-16
 * Time: 下午4:17
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/wiki/top/item")
public class WikiTopItemController extends ToolsBaseController {

    @RequestMapping(value = "/list")
    public ModelAndView wikiTopItemController(@RequestParam(value = "linecode", required = false) String linecode,
                                              @RequestParam(value = "title", required = false) String title,
                                              @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                                              @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            int curPage = (pageStartIndex / pageSize) + 1;
            Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
            ClientLine clientLine = JoymeAppServiceSngl.get().getClientLineByCode(linecode);
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(ClientLineItemField.LINE_ID, clientLine.getLineId()));
            if (!StringUtil.isEmpty(title)) {
                mapMessage.put("title", title);
                queryExpress.add(QueryCriterions.like(ClientLineItemField.TITLE, "%" + title + "%"));
            }
            PageRows<ClientLineItem> pageRows = JoymeAppServiceSngl.get().queryClientLineItemByPage(queryExpress, pagination);
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                Collections.sort(pageRows.getRows(), new Comparator() {
                    public int compare(Object o1, Object o2) {
                        if (Double.parseDouble(((ClientLineItem) o1).getRate()) < Double.parseDouble(((ClientLineItem) o2).getRate())) {
                            return 1;
                        }
                        return 0;
                    }
                });
                mapMessage.put("list", pageRows.getRows());
                mapMessage.put("page", pageRows.getPage());
            }

            mapMessage.put("code", linecode);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("/joymeapp/wikiapp/wikitop/item/wikitopitemlist", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createPage(@RequestParam(value = "code", required = false) String code) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("code", code);

        return new ModelAndView("/joymeapp/wikiapp/wikitop/item/createpage", mapMessage);
    }


    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "code", required = false) String code,
                               @RequestParam(value = "wikiname", required = false) String wikiname,
                               @RequestParam(value = "wikikey", required = false) String wikikey,
                               @RequestParam(value = "picurl", required = false) String picurl,
                               @RequestParam(value = "url", required = false) String url,
                               @RequestParam(value = "category", required = false) String category,
                               @RequestParam(value = "rate", required = false) String rate) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("code", code);
        try {
            ClientLine clientLine = JoymeAppServiceSngl.get().getClientLineByCode(code);
            if (clientLine != null) {
                ClientLineItem clientLineItem = new ClientLineItem();
                clientLineItem.setCategory(StringUtil.isEmpty(category) ? "" : category);
                clientLineItem.setDirectId(wikikey);
                clientLineItem.setRate(rate);
                clientLineItem.setPicUrl(picurl);
                clientLineItem.setUrl(url);
                clientLineItem.setLineId(clientLine.getLineId());
                clientLineItem.setValidStatus(ValidStatus.INVALID);
                clientLineItem.setTitle(wikiname);
                clientLineItem.setItemCreateDate(new Date());
                clientLineItem.setItemType(ClientItemType.WIKIAPP_WIKI_RANK);
                clientLineItem.setItemDomain(ClientItemDomain.DEFAULT);
                JoymeAppServiceSngl.get().createClientLineItem(clientLineItem);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("redirect:/wiki/top/item/list");
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyPage(@RequestParam(value = "itemid", required = false) String itemId,
                                   @RequestParam(value = "code", required = false) String code) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            ClientLineItem clientLineItem = JoymeAppServiceSngl.get().getClientLineItem(new QueryExpress().add(QueryCriterions.eq(ClientLineItemField.ITEM_ID, Long.parseLong(itemId))));
            mapMessage.put("clientLineItem", clientLineItem);
            mapMessage.put("code",code);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("/joymeapp/wikiapp/wikitop/item/modifypage", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "code", required = false) String code,
                               @RequestParam(value = "itemid", required = false) Long itemId,
                               @RequestParam(value = "wikiname", required = false) String wikiname,
                               @RequestParam(value = "wikikey", required = false) String wikikey,
                               @RequestParam(value = "picurl", required = false) String picurl,
                               @RequestParam(value = "url", required = false) String url,
                               @RequestParam(value = "category", required = false) String category,
                               @RequestParam(value = "rate", required = false) String rate) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(ClientLineItemField.CATEGORY, category);
            updateExpress.set(ClientLineItemField.DIRECT_ID, wikikey);
            updateExpress.set(ClientLineItemField.RATE, rate);
            updateExpress.set(ClientLineItemField.PIC_URL, picurl);
            updateExpress.set(ClientLineItemField.TITLE, wikiname);
            updateExpress.set(ClientLineItemField.URL, url);

            JoymeAppServiceSngl.get().modifyClientLineItem(updateExpress, itemId);

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("redirect:/wiki/top/item/list?linecode=" + code);
    }

    @RequestMapping(value = "/delete")
    public ModelAndView deleteLine(@RequestParam(value = "itemid", required = false) Long itemId,
                                   @RequestParam(value = "code", required = false) String code) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(ClientLineItemField.VALID_STATUS, ValidStatus.REMOVED.getCode());
        try {
            JoymeAppServiceSngl.get().modifyClientLineItem(updateExpress, itemId);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/wiki/top/item/list?linecode=" + code);
    }

    @RequestMapping(value = "/recover")
    public ModelAndView modifyLine(@RequestParam(value = "itemid", required = false) Long itemId,
                                   @RequestParam(value = "code", required = false) String code) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(ClientLineItemField.VALID_STATUS, ValidStatus.VALID.getCode());
        try {
            JoymeAppServiceSngl.get().modifyClientLineItem(updateExpress, itemId);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/wiki/top/item/list?linecode=" + code);
    }
}
