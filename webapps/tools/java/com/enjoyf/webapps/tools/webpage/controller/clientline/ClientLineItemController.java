package com.enjoyf.webapps.tools.webpage.controller.clientline;

import com.enjoyf.mcms.bean.DedeArchives;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.gameres.gamedb.GameDB;
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
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.platform.webapps.common.util.PicRgbUtil;
import com.enjoyf.platform.service.joymeapp.clientline.ClientLineItemDTO;
import com.enjoyf.platform.service.joymeapp.clientline.ClientLineWebDataProcessorFactory;
import com.enjoyf.webapps.tools.weblogic.clientline.ClientLineWebLogic;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import com.mongodb.BasicDBObject;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.enjoyf.platform.service.JsonBinder.buildNormalBinder;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-12-10
 * Time: 下午2:08
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/clientline/item")
public class ClientLineItemController extends ToolsBaseController {

    @Resource(name = "clientLineWebLogic")
    private ClientLineWebLogic clientLineWebLogic;

    @Resource(name = "i18nSource")
    private ResourceBundleMessageSource i18nSource;

    @RequestMapping(value = "/list")
    public ModelAndView lineList(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                                 @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                                 @RequestParam(value = "lineid", required = false) Long lineId,
                                 @RequestParam(value = "validstatus", required = false) String validStatus,
                                 @RequestParam(value = "title", required = false) String title) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);

        if (lineId == null) {
            return new ModelAndView("redirect:/clientline/item/list");
        }
        try {
            ClientLine clientLine = JoymeAppServiceSngl.get().getClientLine(new QueryExpress().add(QueryCriterions.eq(ClientLineField.LINE_ID, lineId)));
            if (clientLine == null) {
                return new ModelAndView("redirect:/clientline/item/list");
            }
            mapMessage.put("clientLine", clientLine);

            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(ClientLineItemField.LINE_ID, clientLine.getLineId()));
            if (StringUtil.isEmpty(validStatus)) {
                queryExpress.add(QueryCriterions.eq(ClientLineItemField.VALID_STATUS, ValidStatus.VALID.getCode()));
            } else {
                queryExpress.add(QueryCriterions.eq(ClientLineItemField.VALID_STATUS, validStatus));

            }
            if (!StringUtil.isEmpty(title)) {
                queryExpress.add(QueryCriterions.like(ClientLineItemField.TITLE, "%" + title + "%"));
            }
            mapMessage.put("validstatus", validStatus);
            queryExpress.add(QuerySort.add(ClientLineItemField.DISPLAY_ORDER, QuerySortOrder.ASC));
            //queryExpress.add(QueryCriterions.eq(ClientLineItemField.VALID_STATUS, ValidStatus.VALID.getCode()));
            PageRows<ClientLineItemDTO> itemPageRows = clientLineWebLogic.queryClientLineItemByPage(queryExpress, pagination);
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
        return new ModelAndView("/clientline/lineitemlist", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createLinePage(@RequestParam(value = "itemtype", required = false) Integer itemtype,
                                       @RequestParam(value = "lineid", required = false) Long lineid) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        String pagePrefix = "default";
        try {
            ClientLine clientLine = JoymeAppServiceSngl.get().getClientLine(new QueryExpress().add(QueryCriterions.eq(ClientLineField.LINE_ID, lineid)));

            List<Integer> list = new ArrayList<Integer>();
            if (clientLine.getItemType().equals(ClientItemType.GAME)) {
                List<Integer> rateList = new ArrayList<Integer>();
                for (int i = 1; i <= 10; i++) {
                    rateList.add(i);
                }
                mapMessage.put("rateList", rateList);
                list.add(ClientItemType.GAME.getCode());
                pagePrefix = String.valueOf(ClientItemType.GAME.getCode());
            } else if (clientLine.getItemType().equals(ClientItemType.ARTICLE)) {
                list.add(ClientItemDomain.DEFAULT.getCode());
                list.add(ClientItemDomain.CMSARTICLE.getCode());
                mapMessage.put("itemDomainCollection", list);
                mapMessage.put("appDisplayType", AppDisplayType.getAll());
                pagePrefix = String.valueOf(ClientItemType.ARTICLE.getCode());
            } else if (clientLine.getItemType().equals(ClientItemType.CUSTOM)) {
                list.add(ClientItemDomain.DEFAULT.getCode());
                mapMessage.put("itemDomainCollection", list);
                pagePrefix = String.valueOf(ClientItemType.CUSTOM.getCode());
            } else {
                list.add(ClientItemDomain.DEFAULT.getCode());
                mapMessage.put("itemDomainCollection", list);
            }

//            if (ClientItemType.ARTICLE.equals(ClientItemType.getByCode(itemtype))) {
//                list.add(ClientItemDomain.DEFAULT.getCode());
//                list.add(ClientItemDomain.CMSARTICLE.getCode());
//                mapMessage.put("itemDomainCollection", list);
//
//                mapMessage.put("appDisplayType", AppDisplayType.getAll());
//            } else if (ClientItemType.CUSTOM.equals(ClientItemType.getByCode(itemtype))) {
//                list.add(ClientItemDomain.DEFAULT.getCode());
//                mapMessage.put("itemDomainCollection", list);
//            } else {
//                List<Integer> rateList = new ArrayList<Integer>();
//                for (int i = 1; i <= 10; i++) {
//                    rateList.add(i);
//                }
//                mapMessage.put("rateList", rateList);
//            }


            String code = clientLine.getCode();
            if (code.indexOf("newsclientidex") > -1) {
                mapMessage.put("newclient", "1");
            } else {
                mapMessage.put("newclient", "0");
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        mapMessage.put("redirectCollection", AppRedirectType.getAll());
        mapMessage.put("lineid", lineid);
        mapMessage.put("itemtype", itemtype);

        return new ModelAndView("/clientline/createitempage-" + pagePrefix, mapMessage);
    }


    @RequestMapping(value = "/create")
    public ModelAndView createLine(@RequestParam(value = "itemname", required = false) String itemname,
                                   @RequestParam(value = "desc", required = false) String desc,
                                   @RequestParam(value = "picurl", required = false) String picurl,
                                   @RequestParam(value = "linkaddress", required = false) String linkaddress,
                                   @RequestParam(value = "itemdomain", required = false) Integer itemdomain,
                                   @RequestParam(value = "lineid", required = false) String lineid,
                                   @RequestParam(value = "itemtype", required = false) Integer itemtype,
                                   @RequestParam(value = "directid", required = false) String directid,
                                   @RequestParam(value = "redirecttype", required = false) String redirecttype,
                                   @RequestParam(value = "gamerate", required = false) String rate,
                                   @RequestParam(value = "displaytype", required = false) String displayType,
                                   @RequestParam(value = "author", required = false) String author,
                                   @RequestParam(value = "bgcolor", required = false) String color,
                                   @RequestParam(value = "category", required = false) String category,
                                   @RequestParam(value = "categorycolor", required = false) String categorycolor) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        Map<String, Object> mapMsg = new HashMap<String, Object>();
        Map<String, String> errorMsgMap = new HashMap<String, String>();
        Map<String, String> lineItemKeyValues = new HashMap<String, String>();
        if (ClientItemType.GAME.equals(ClientItemType.getByCode(itemtype))) {
            itemdomain = ClientItemDomain.GAME.getCode();
        }
        lineItemKeyValues.put("itemname", itemname);
        lineItemKeyValues.put("desc", desc);
        lineItemKeyValues.put("picurl", picurl);
        lineItemKeyValues.put("linkaddress", linkaddress);
        lineItemKeyValues.put("itemdomain", itemdomain.toString());
        lineItemKeyValues.put("lineid", lineid);
        lineItemKeyValues.put("itemtype", itemtype.toString());
        lineItemKeyValues.put("directid", directid);
        lineItemKeyValues.put("redirecttype", redirecttype);
        lineItemKeyValues.put("rate", rate);
        if (!StringUtil.isEmpty(displayType)) {
            lineItemKeyValues.put("displaytype", displayType);
        }

        lineItemKeyValues.put("author", author);
        lineItemKeyValues.put("category", category);
        lineItemKeyValues.put("categorycolor", categorycolor);
        try {
            ClientLineItem clientLineItem = ClientLineWebDataProcessorFactory.get().factory(ClientItemDomain.getByCode(itemdomain)).generateAddLineItem(lineItemKeyValues, errorMsgMap, mapMsg);

            if (!CollectionUtil.isEmpty(errorMsgMap)) {
                GAlerter.lab(this.getClass().getName() + " clientLineItem is null; ");
                mapMessage.put("errorMsgMap", errorMsgMap);
                mapMessage.put("itemtype", itemtype);
                mapMessage.put("lineid", lineid);
                return new ModelAndView("forward:/clientline/item/createpage", mapMessage);
            }


            //bgcolor新闻端模板背景色 picrgb图片颜色主色调
            if (!StringUtil.isEmpty(color) || !StringUtil.isEmpty(clientLineItem.getPicUrl())) {
                ParamTextJson paramTextJson = new ParamTextJson();
                if (!StringUtil.isEmpty(color)) {
                    paramTextJson.setBgcolor(color);
                }
                if (!StringUtil.isEmpty(clientLineItem.getPicUrl())) {
                    String pic_rgb = PicRgbUtil.getImagePixel(clientLineItem.getPicUrl());
                    paramTextJson.setPicrgb(pic_rgb);
                }
                ParamTextJson param = buildNormalBinder().getMapper().readValue(paramTextJson.toJson(), new TypeReference<ParamTextJson>() {
                });
                clientLineItem.setParam(param);
            }

            JoymeAppServiceSngl.get().createClientLineItem(clientLineItem);

            //update DedeArchives pubdate
            try {
                DedeArchives dedeArchives = new DedeArchives();
                dedeArchives.setId(Integer.valueOf(clientLineItem.getDirectId()));
                dedeArchives.setPubdate((int) (clientLineItem.getItemCreateDate().getTime() / 1000));
                JoymeAppServiceSngl.get().modifyDedeArchivePubdateById(dedeArchives);
                GAlerter.lab("modifyDedeArchivePubdateById occured .e: directid=" + directid + ",pubdate=" + dedeArchives.getPubdate());
            } catch (Exception e) {
                GAlerter.lab(this.getClass().getName() + " modifyDedeArchivePubdateById occured ServiceException.e: ", e);
                e.printStackTrace();
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        } catch (IOException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMessage", "system.error");
        }
        return new ModelAndView("redirect:/clientline/item/list?lineid=" + lineid);
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyLinePage(@RequestParam(value = "itemid", required = true) Long itemid,
                                       @RequestParam(value = "itemtype", required = false) Integer itemtype) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("itemtype", itemtype);
        String pagePrefix = "";
        try {
            ClientLineItem clientLineItem = JoymeAppServiceSngl.get().getClientLineItem(new QueryExpress().add(QueryCriterions.eq(ClientLineItemField.ITEM_ID, itemid)));
            mapMessage.put("clienLineItem", clientLineItem);
            if (clientLineItem.getItemCreateDate() != null) {
                SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String updatetime = sd.format(clientLineItem.getItemCreateDate());
                mapMessage.put("updatetime", updatetime);
            }
            List<Integer> list = new ArrayList<Integer>();
            if (ClientItemType.ARTICLE.equals(ClientItemType.getByCode(itemtype))) {

                list.add(ClientItemDomain.DEFAULT.getCode());
                list.add(ClientItemDomain.CMSARTICLE.getCode());
                mapMessage.put("appDisplayType", AppDisplayType.getAll());

            }
            if (ClientItemType.GAME.equals(ClientItemType.getByCode(itemtype))) {
                List<Integer> rateList = new ArrayList<Integer>();
                for (int i = 1; i <= 10; i++) {
                    rateList.add(i);
                }
                mapMessage.put("rateList", rateList);
                list.add(ClientItemDomain.GAME.getCode());
            }
            if (ClientItemType.CUSTOM.equals(ClientItemType.getByCode(itemtype))) {
                list.add(ClientItemDomain.DEFAULT.getCode());
                pagePrefix = "-" + String.valueOf(ClientItemType.CUSTOM.getCode());
            }

            mapMessage.put("itemDomainCollection", list);
            mapMessage.put("redirectCollection", AppRedirectType.getAll());
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/clientline/modifyitemlinepage" + pagePrefix, mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modifyLine(@RequestParam(value = "itemid", required = false) Long itemid,
                                   @RequestParam(value = "lineid", required = false) Long lineid,
                                   @RequestParam(value = "itemname", required = false) String itemname,
                                   @RequestParam(value = "desc", required = false) String desc,
                                   @RequestParam(value = "picurl", required = false) String picurl,
                                   @RequestParam(value = "gamerate", required = false) String rate,
                                   @RequestParam(value = "redirecttype", required = false) String redirecttype,
                                   @RequestParam(value = "directid", required = false) String directid,
                                   @RequestParam(value = "itemdomain", required = false) Integer itemdomain,
                                   @RequestParam(value = "linkaddress", required = false) String linkaddress,
                                   @RequestParam(value = "itemtype", required = false) Integer itemtype,
                                   @RequestParam(value = "displaytype", required = false) String displayType,
                                   @RequestParam(value = "author", required = false) String author,
                                   @RequestParam(value = "bgcolor", required = false) String color,
                                   @RequestParam(value = "updatetime", required = false) String updateTime,
                                   @RequestParam(value = "category", required = false) String category,
                                   @RequestParam(value = "categorycolor", required = false) String categorycolor) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        Map<String, String> errorMsgMap = new HashMap<String, String>();

        mapMessage.put("itemid", itemid);
        mapMessage.put("itemtype", itemtype);

        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(ClientLineItemField.TITLE, itemname);
        updateExpress.set(ClientLineItemField.DESC, desc);
        updateExpress.set(ClientLineItemField.PIC_URL, picurl);
        updateExpress.set(ClientLineItemField.RATE, rate);
        updateExpress.set(ClientLineItemField.REDIRCT_TYPE, Integer.parseInt(redirecttype));
        updateExpress.set(ClientLineItemField.ITEM_DOMAIN, itemdomain);

        if(!StringUtil.isEmpty(displayType)) {
            updateExpress.set(ClientLineItemField.APPDISPLAYTYPE, Integer.parseInt(displayType));
        }

        updateExpress.set(ClientLineItemField.AUTHOR, author);

        if(!StringUtil.isEmpty(category)){
            updateExpress.set(ClientLineItemField.CATEGORY, category);
        }
        if(!StringUtil.isEmpty(categorycolor)){
            updateExpress.set(ClientLineItemField.CATEGORY_COLOR, categorycolor);

        }


        //bgcolor新闻端模板背景色 picrgb图片颜色主色调
        if (!StringUtil.isEmpty(color) || !StringUtil.isEmpty(picurl)) {
            ParamTextJson paramTextJson = new ParamTextJson();
            if (!StringUtil.isEmpty(color)) {
                paramTextJson.setBgcolor(color);
            }
            if (!StringUtil.isEmpty(picurl)) {
                String pic_rgb = PicRgbUtil.getImagePixel(picurl);
                paramTextJson.setPicrgb(pic_rgb);
            }
            updateExpress.set(ClientLineItemField.ITEM_PARAM, paramTextJson.toJson());
        } else {
            updateExpress.set(ClientLineItemField.ITEM_PARAM, color);
        }

        if (ClientItemType.ARTICLE.equals(ClientItemType.getByCode(itemtype))) {
            AppRedirectType redirectType = AppRedirectType.getByCode(Integer.parseInt(redirecttype));
            if (AppRedirectType.CMSARTICLE.equals(redirectType)) {
                linkaddress = linkaddress.replace("marticle/", "json/");
            }
            if (ClientItemDomain.CMSARTICLE.equals(ClientItemDomain.getByCode(itemdomain))) {
                int archiveId = getArchiveId(linkaddress);
                try {
                    Archive archive = JoymeAppServiceSngl.get().getArchiveById(archiveId);
                    if (archive == null) {
                        errorMsgMap.put("urlerror", "article.null");
                        mapMessage.put("errorMsgMap", errorMsgMap);
                        return new ModelAndView("forward:/clientline/item/modifypage", mapMessage);
                    }
                    if (StringUtil.isEmpty(itemname)) {
                        updateExpress.set(ClientLineItemField.TITLE, archive.getTitle());
                    }
                    if (StringUtil.isEmpty(desc)) {
                        updateExpress.set(ClientLineItemField.DESC, archive.getDesc());
                    }
                    if (StringUtil.isEmpty(picurl)) {
                        updateExpress.set(ClientLineItemField.PIC_URL, archive.getIcon());
                    }
                    if (StringUtil.isEmpty(author)) {
                        updateExpress.set(ClientLineItemField.AUTHOR, archive.getAuthor());
                    }
                    if (!StringUtil.isEmpty(archive.getTypeName())) {
                        updateExpress.set(ClientLineItemField.CATEGORY, archive.getTypeName());
                    }
                    if (!StringUtil.isEmpty(archive.getTypeColor())) {
                        updateExpress.set(ClientLineItemField.CATEGORY_COLOR, archive.getTypeColor());
                    }
                } catch (Exception e) {
                    GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
                    mapMessage = putErrorMessage(mapMessage, "system.error");
                }
                updateExpress.set(ClientLineItemField.DIRECT_ID, String.valueOf(archiveId));
                updateExpress.set(ClientLineItemField.URL, linkaddress);
            } else if (ClientItemDomain.DEFAULT.equals(ClientItemDomain.getByCode(itemdomain))) {
                if (AppRedirectType.TAGLIST.equals(AppRedirectType.getByCode(Integer.parseInt(redirecttype)))) {
                    if (StringUtil.isEmpty(directid)) {
                        int startIndex = linkaddress.lastIndexOf("/");
                        int endIndex = linkaddress.lastIndexOf(".html");

                        String tagIdString = linkaddress.substring(startIndex + 1, endIndex);

                        if (tagIdString.indexOf("_") > 0) {
                            tagIdString = tagIdString.split("_")[0];
                        }
                        updateExpress.set(ClientLineItemField.DIRECT_ID, tagIdString);
                        updateExpress.set(ClientLineItemField.URL, linkaddress);
                    } else if (!StringUtil.isEmpty(directid)) {
                        String url = "http://marticle.joyme.com/json/tags/" + directid + "_1.html";
                        updateExpress.set(ClientLineItemField.DIRECT_ID, directid);
                        updateExpress.set(ClientLineItemField.URL, url);
                    }
                } else {
                    updateExpress.set(ClientLineItemField.URL, linkaddress);
                    updateExpress.set(ClientLineItemField.DIRECT_ID, directid);
                }
            }
        } else if (ClientItemType.GAME.equals(ClientItemType.getByCode(itemtype))) {
            try {
                GameDB gameDB = GameResourceServiceSngl.get().getGameDB(new BasicDBObject("_id", Long.parseLong(directid)), false);
                if (gameDB == null) {
                    errorMsgMap.put("gameDbError", "gamedb.is.null");
                    mapMessage.put("errorMsgMap", errorMsgMap);
                    return new ModelAndView("forward:/clientline/item/modifypage", mapMessage);
                }
                String url = linkaddress;
                if (StringUtil.isEmpty(url)) {
                    if (!StringUtil.isEmpty(gameDB.getWikiUrl())) {
                        url = gameDB.getWikiUrl();
                    } else {
                        url = gameDB.getCmsUrl();
                    }
                }
                updateExpress.set(ClientLineItemField.DIRECT_ID, directid);
                updateExpress.set(ClientLineItemField.URL, url);
            } catch (ServiceException e) {
                GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
                mapMessage = putErrorMessage(mapMessage, "system.error");
            }
        }

        try {
            if (!StringUtil.isEmpty(updateTime)) {
                SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                updateExpress.set(ClientLineItemField.ITEM_CREATE_DATE, sd.parse(updateTime));

                //update DedeArchives pubdate
                try {
                    DedeArchives dedeArchives = new DedeArchives();

                    //2015-01-13 为了新手游画报的游戏详情的tab页，最新  不报错才添加的
                    if(StringUtil.isEmpty(directid)){
                        directid="0";
                    }
                    dedeArchives.setId(Integer.valueOf(directid));

                    dedeArchives.setPubdate((int) (sd.parse(updateTime).getTime() / 1000));
                    JoymeAppServiceSngl.get().modifyDedeArchivePubdateById(dedeArchives);
                    GAlerter.lab("modifyDedeArchivePubdateById occured .e: directid=" + directid + ",pubdate=" + dedeArchives.getPubdate());
                } catch (Exception e) {
                    GAlerter.lab(this.getClass().getName() + " modifyDedeArchivePubdateById occured ServiceException.e: ", e);
                    e.printStackTrace();
                }

            }
            JoymeAppServiceSngl.get().modifyClientLineItem(updateExpress, itemid);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        } catch (ParseException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("redirect:/clientline/item/list?lineid=" + lineid);
    }

    @RequestMapping(value = "/delete")
    public ModelAndView deleteLine
            (@RequestParam(value = "itemid", required = false) Long
                     itemId,
             @RequestParam(value = "lineid", required = false) Long
                     lineId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        if (itemId == null) {
            return new ModelAndView("redirect:/clientline/item/list?lineid=" + lineId);
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

        return new ModelAndView("redirect:/clientline/item/list?lineid=" + lineId);
    }

    @RequestMapping(value = "/recover")
    public ModelAndView modifyLine
            (@RequestParam(value = "itemid", required = false) Long
                     itemId,
             @RequestParam(value = "lineid", required = false) Long
                     lineId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        if (itemId == null) {
            return new ModelAndView("redirect:/clientline/item/list?lineid=" + lineId);
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

        return new ModelAndView("redirect:/clientline/item/list?lineid=" + lineId);
    }

    @RequestMapping(value = "/sort/{sort}")
    @ResponseBody
    public String sort(@PathVariable(value = "sort") String sort,
                       @RequestParam(value = "lineid", required = false) Long lineId,
                       @RequestParam(value = "itemid", required = false) Long itemId) {
        JsonBinder binder = JsonBinder.buildNormalBinder();
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        ResultObjectMsg resultObjectMsg = new ResultObjectMsg(ResultObjectMsg.CODE_S);
        if (lineId == null || itemId == null) {
            resultObjectMsg.setRs(ResultObjectMsg.CODE_E);
            return binder.toJson(resultObjectMsg);
        }
        Long returnItemId = null;
        try {
            returnItemId = ClientLineWebLogic.sort(sort, lineId, itemId);
            if (returnItemId == null) {
                resultObjectMsg.setRs(ResultObjectMsg.CODE_E);
                return binder.toJson(resultObjectMsg);
            }
        } catch (Exception e) {
            resultObjectMsg.setRs(ResultObjectMsg.CODE_E);
            resultObjectMsg.setMsg(i18nSource.getMessage("system.error", null, null));
            return binder.toJson(resultObjectMsg);
        }
        mapMessage.put("sort", sort);
        mapMessage.put("itemid", itemId);
        mapMessage.put("returnitemid", returnItemId);
        resultObjectMsg.setResult(mapMessage);
        return binder.toJson(resultObjectMsg);
    }

    public int getArchiveId(String url) {
        String[] urls = url.split("/");
        int archiveId = 0;
        String number = "";
        for (int i = 0; i < urls.length; i++) {
            String item = urls[i];
            if (item.endsWith(".html")) {
                item = item.replaceAll(".html", "");
                int position = item.indexOf("_");
                if (position >= 0) {
                    item = item.substring(0, position);
                }
            }

            if (com.enjoyf.util.StringUtil.isNumeric(item)) {
                number += item;
            }
        }

        if (number.length() > 8 && number.startsWith("20")) {
            archiveId = Integer.parseInt(number.substring(8, number.length()));
        }

        return archiveId;
    }


}
