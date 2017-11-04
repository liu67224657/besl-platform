package com.enjoyf.webapps.tools.webpage.controller.gameclient;

import com.enjoyf.platform.service.IntValidStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.gameres.gamedb.*;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.joymeapp.clientline.ClientLineItemDTO;
import com.enjoyf.platform.service.joymeapp.clientline.ClientLineWebDataProcessorFactory;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.service.tools.ToolsLog;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.webapps.tools.weblogic.clientline.ClientLineWebLogic;
import com.enjoyf.webapps.tools.weblogic.gameclient.GameClientWebLogic;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import com.mongodb.BasicDBObject;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/12/28
 * Description:
 */
//新游开测、热门、正在玩的controller
@Controller
@RequestMapping(value = "/gameclient/gametab")
public class GameClientGameTabController extends ToolsBaseController {

    @Resource(name = "clientLineWebLogic")
    private ClientLineWebLogic clientLineWebLogic;

    @Resource(name = "i18nSource")
    private ResourceBundleMessageSource i18nSource;

    @Resource(name = "gameClientWebLogic")
    private GameClientWebLogic gameClientWebLogic;


    @RequestMapping("/list")
    public ModelAndView gameReltaionList(@RequestParam(value = "gamedbid") Long gamedbId) {
        Map map = new HashMap();
        try {
            GameDB gameDb = GameResourceServiceSngl.get().getGameDB(new BasicDBObject().append("_id", gamedbId), false);
            if (gameDb == null) {
                map = putErrorMap("errorMsg", "game.not.exists");
                return new ModelAndView("gameclient/gametab/list", map);
            }

            List<GameDBRelation> relationList = GameResourceServiceSngl.get().queryGameDBRelationbyGameDbId(gamedbId, false);
            map.put("game", gameDb);
            map.put("relationlist", relationList);

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "occured ServiceException.e:", e);
            map = putErrorMap("errorMsg", "system.error");
            return new ModelAndView("gameclient/gametab/list", map);
        }

        return new ModelAndView("gameclient/gametab/list", map);
    }

    @RequestMapping("/createpage")
    public ModelAndView createpage(@RequestParam(value = "gamedbid") Long gamedbId) {
        Map map = new HashMap();
        try {
            GameDB gameDb = GameResourceServiceSngl.get().getGameDB(new BasicDBObject().append("_id", gamedbId), false);
            if (gameDb == null) {
                return new ModelAndView("redirect:/gameclient/gametab/list?gamedbid=" + gamedbId + "&errorMsg=game.not.exists");
            }

            map.put("game", gameDb);
            map.put("types", GameDBRelationType.getAll());

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "occured ServiceException.e:", e);
            return new ModelAndView("redirect:/gameclient/gametab/list?gamedbid=" + gamedbId + "&errorMsg=system.error");
        }

        return new ModelAndView("gameclient/gametab/createpage", map);
    }

    @RequestMapping("/create")
    public ModelAndView create(@RequestParam(value = "gamedbid") Long gamedbId,
                               @RequestParam(value = "title", required = false) String title,
                               @RequestParam(value = "type") int type,
                               @RequestParam(value = "uri", required = false) String url) {
        Map map = new HashMap();
        map.put("gamedbid", gamedbId);
        try {
            GameDB gameDb = GameResourceServiceSngl.get().getGameDB(new BasicDBObject().append("_id", gamedbId), false);
            if (gameDb == null) {
                return new ModelAndView("redirect:/gameclient/gametab/list?gamedbid=" + gamedbId + "&errorMsg=game.not.exists");
            }
            GameDBRelationType relationType = GameDBRelationType.getByCode(type);
            if (relationType == null) {
                return new ModelAndView("redirect:/gameclient/gametab/list?gamedbid=" + gamedbId + "&errorMsg=gamedb.relation.type.errorMsg");
            }

            int displayOrder = (int) (Integer.MAX_VALUE - System.currentTimeMillis() / 1000);
            GameDBRelation relation = new GameDBRelation();
            if (getCurrentUser() != null) {
                relation.setModifyUserid(getCurrentUser().getUserid());
                relation.setModifyIp(getCurrentUser().getAccessip());
            }
            relation.setType(relationType);
            relation.setGamedbId(gamedbId);
            relation.setDisplayOrder(displayOrder);

            if (relationType.equals(GameDBRelationType.DETAIL)) {
                QueryExpress queryExpress = new QueryExpress().add(QueryCriterions.eq(GameDBRelationField.GAMEDBID, gamedbId)).add(QueryCriterions
                        .eq(GameDBRelationField.TYPE, relationType.getCode()));
                GameDBRelation gameDBRelation = GameResourceServiceSngl.get().getGameDbRelation(queryExpress);
                if (gameDBRelation == null) {
                    relation.setTitle(StringUtil.isEmpty(title) ? "详情" : title);
                    relation.setUri(String.valueOf(gamedbId));
                    GameResourceServiceSngl.get().createGameDbRelation(relation);
                } else {
                    UpdateExpress updateExpress = new UpdateExpress();
                    updateExpress.set(GameDBRelationField.VALIDSTATUS, IntValidStatus.VALID.getCode());
                    updateExpress.set(GameDBRelationField.MODIFYIP, getCurrentUser().getAccessip());
                    updateExpress.set(GameDBRelationField.MODIFYUSERID, getCurrentUser().getUserid());
                    GameResourceServiceSngl.get().updateGameDbRelation(updateExpress, gameDBRelation.getRelationId());
                }
            } else if (relationType.equals(GameDBRelationType.NEWS)) {
                QueryExpress queryExpress = new QueryExpress().add(QueryCriterions.eq(GameDBRelationField.GAMEDBID, gamedbId)).add(QueryCriterions
                        .eq(GameDBRelationField.TYPE, relationType.getCode()));
                GameDBRelation gameDBRelation = GameResourceServiceSngl.get().getGameDbRelation(queryExpress);
                if (gameDBRelation == null) {
                    ClientLine clientLine = JoymeAppServiceSngl.get().getClientLineByCode("gamedb_news_" + gamedbId);
                    if (clientLine != null) {
                        UpdateExpress up = new UpdateExpress();
                        up.set(ClientLineField.VALID_STATUS, ValidStatus.VALID.getCode());
                        QueryExpress qu = new QueryExpress();
                        qu.add(QueryCriterions.eq(ClientLineField.CODE, "gamedb_news_" + gamedbId));
                        JoymeAppServiceSngl.get().modifyClientLine(up, qu);
                    } else {
                        clientLine = new ClientLine();
                        clientLine.setCode("gamedb_news_" + gamedbId);
                        clientLine.setCreateDate(new Date());
                        clientLine.setCreateUserid(getCurrentUser().getUserid());
                        clientLine.setItemType(ClientItemType.CUSTOM);
                        clientLine.setLineType(ClientLineType.GAMENEWSTAB);
                        clientLine.setDisplay_order(displayOrder);
                        clientLine.setValidStatus(ValidStatus.VALID);
                        clientLine = JoymeAppServiceSngl.get().createClientLine(clientLine);
                    }


                    relation.setTitle(StringUtil.isEmpty(title) ? "最新" : title);
                    relation.setUri(String.valueOf(clientLine.getLineId()));
                    GameResourceServiceSngl.get().createGameDbRelation(relation);
                } else {
                    UpdateExpress updateExpress = new UpdateExpress();
                    updateExpress.set(GameDBRelationField.VALIDSTATUS, IntValidStatus.VALID.getCode());
                    updateExpress.set(GameDBRelationField.MODIFYIP, getCurrentUser().getAccessip());
                    updateExpress.set(GameDBRelationField.MODIFYUSERID, getCurrentUser().getUserid());
                    GameResourceServiceSngl.get().updateGameDbRelation(updateExpress, gameDBRelation.getRelationId());
                }
            } else if (relationType.equals(GameDBRelationType.GIFT)) {
                QueryExpress queryExpress = new QueryExpress().add(QueryCriterions.eq(GameDBRelationField.GAMEDBID, gamedbId)).add(QueryCriterions
                        .eq(GameDBRelationField.TYPE, relationType.getCode()));
                GameDBRelation gameDBRelation = GameResourceServiceSngl.get().getGameDbRelation(queryExpress);
                if (gameDBRelation == null) {
                    relation.setTitle(StringUtil.isEmpty(title) ? "礼包" : title);
                    relation.setUri(String.valueOf(gamedbId));
                    GameResourceServiceSngl.get().createGameDbRelation(relation);
                } else {
                    UpdateExpress updateExpress = new UpdateExpress();
                    updateExpress.set(GameDBRelationField.VALIDSTATUS, IntValidStatus.VALID.getCode());
                    updateExpress.set(GameDBRelationField.MODIFYIP, getCurrentUser().getAccessip());
                    updateExpress.set(GameDBRelationField.MODIFYUSERID, getCurrentUser().getUserid());
                    GameResourceServiceSngl.get().updateGameDbRelation(updateExpress, gameDBRelation.getRelationId());
                }
            } else if (relationType.equals(GameDBRelationType.LINK)) {
                relation.setTitle(title);
                relation.setUri(url);
                GameResourceServiceSngl.get().createGameDbRelation(relation);
            }

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "occured ServiceException.e:", e);
            return new ModelAndView("redirect:/gameclient/gametab/list?gamedbid=" + gamedbId + "&errorMsg=system.error");
        }

        return new ModelAndView("redirect:/gameclient/gametab/list?gamedbid=" + gamedbId);
    }


    @RequestMapping("/modifypage")
    public ModelAndView moifypage(@RequestParam(value = "relationid") long relationid, @RequestParam(value = "gamedbid") Long gamedbId) {
        Map map = new HashMap();
        try {
            GameDB gameDb = GameResourceServiceSngl.get().getGameDB(new BasicDBObject().append("_id", gamedbId), false);
            if (gameDb == null) {
                return new ModelAndView("redirect:/gameclient/gametab/list?gamedbid=" + gamedbId + "&errorMsg=game.not.exists");
            }

            QueryExpress queryExpress = new QueryExpress().add(QueryCriterions.eq(GameDBRelationField.RELATIONID, relationid));
            GameDBRelation gameDBRelation = GameResourceServiceSngl.get().getGameDbRelation(queryExpress);
            if (gameDBRelation == null) {
                return new ModelAndView("redirect:/gameclient/gametab/list?gamedbid=" + gamedbId + "&errorMsg=game.not.exists");
            }

            map.put("game", gameDb);
            map.put("relation", gameDBRelation);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "occured ServiceException.e:", e);
            return new ModelAndView("redirect:/gameclient/gametab/list?gamedbid=" + gamedbId + "&errorMsg=system.error");
        }

        return new ModelAndView("gameclient/gametab/modifypage", map);
    }

    @RequestMapping("/modify")
    public ModelAndView modify(@RequestParam(value = "relationid") long relationid,
                               @RequestParam(value = "gamedbid") Long gamedbId,
                               @RequestParam(value = "title", required = false) String title,
                               @RequestParam(value = "uri", required = false) String url) {

        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(GameDBRelationField.TITLE, title);
        updateExpress.set(GameDBRelationField.URI, url);
        try {
            GameResourceServiceSngl.get().updateGameDbRelation(updateExpress, relationid);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "occured ServiceException.e:", e);
            return new ModelAndView("redirect:/gameclient/gametab/list?gamedbid=" + gamedbId + "&errorMsg=system.error");
        }

        return new ModelAndView("redirect:/gameclient/gametab/list?gamedbid=" + gamedbId);
    }

    @RequestMapping("/delete")
    public ModelAndView remove(@RequestParam(value = "relationid") long relationid,
                               @RequestParam(value = "gamedbid") Long gamedbId) {

        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(GameDBRelationField.VALIDSTATUS, IntValidStatus.UNVALID.getCode());
        updateExpress.set(GameDBRelationField.MODIFYIP, getCurrentUser().getAccessip());
        updateExpress.set(GameDBRelationField.MODIFYUSERID, getCurrentUser().getUserid());
        try {
            GameResourceServiceSngl.get().updateGameDbRelation(updateExpress, relationid);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "occured ServiceException.e:", e);
            return new ModelAndView("redirect:/gameclient/gametab/list?gamedbid=" + gamedbId + "&errorMsg=system.error");
        }

        return new ModelAndView("redirect:/gameclient/gametab/list?gamedbid=" + gamedbId);
    }

    @RequestMapping("/recover")
    public ModelAndView recover(@RequestParam(value = "relationid") long relationid,
                                @RequestParam(value = "gamedbid") Long gamedbId) {

        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(GameDBRelationField.VALIDSTATUS, IntValidStatus.VALID.getCode());
        updateExpress.set(GameDBRelationField.MODIFYIP, getCurrentUser().getAccessip());
        updateExpress.set(GameDBRelationField.MODIFYUSERID, getCurrentUser().getUserid());
        try {
            GameResourceServiceSngl.get().updateGameDbRelation(updateExpress, relationid);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "occured ServiceException.e:", e);
            return new ModelAndView("redirect:/gameclient/gametab/list?gamedbid=" + gamedbId + "&errorMsg=system.error");
        }

        return new ModelAndView("redirect:/gameclient/gametab/list?gamedbid=" + gamedbId);
    }

    @RequestMapping(value = "/sort/{sort}")
    @ResponseBody
    public String sort(@PathVariable(value = "sort") String sort,
                       @RequestParam(value = "gamedbid", required = false) Long gameDbId,
                       @RequestParam(value = "relationid", required = false) Long relationid) {
        JsonBinder binder = JsonBinder.buildNormalBinder();
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        ResultObjectMsg resultObjectMsg = new ResultObjectMsg(ResultObjectMsg.CODE_S);

        Long returnItemId = null;
        try {
            returnItemId = sortByRelationId(sort, gameDbId, relationid);
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
        mapMessage.put("srcid", relationid);
        mapMessage.put("destid", returnItemId);
        resultObjectMsg.setResult(mapMessage);
        return binder.toJson(resultObjectMsg);
    }

    private Long sortByRelationId(String sort, Long gamedbid, Long relationid) throws ServiceException {
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(GameDBRelationField.GAMEDBID, gamedbid));
        GameDBRelation relation = GameResourceServiceSngl.get().getGameDbRelation(new QueryExpress().add(QueryCriterions.eq(GameDBRelationField.RELATIONID, relationid)));
        if (relation == null) {
            return null;
        }

        queryExpress.add(QueryCriterions.eq(GameDBRelationField.VALIDSTATUS, IntValidStatus.VALID.getCode()));
        if (sort.equals("up")) {
            queryExpress.add(QueryCriterions.lt(GameDBRelationField.DISPLAYORDER, relation.getDisplayOrder()));
            queryExpress.add(QuerySort.add(GameDBRelationField.DISPLAYORDER, QuerySortOrder.DESC));
        } else {
            queryExpress.add(QueryCriterions.gt(GameDBRelationField.DISPLAYORDER, relation.getDisplayOrder()));
            queryExpress.add(QuerySort.add(GameDBRelationField.DISPLAYORDER, QuerySortOrder.ASC));
        }

        GameDBRelation destRelation = GameResourceServiceSngl.get().getGameDbRelation(queryExpress);
        if (destRelation != null) {
            UpdateExpress updateExpress1 = new UpdateExpress();
            updateExpress1.set(GameDBRelationField.DISPLAYORDER, relation.getDisplayOrder());
            GameResourceServiceSngl.get().updateGameDbRelation(updateExpress1, destRelation.getRelationId());

            UpdateExpress updateExpress2 = new UpdateExpress();
            updateExpress2.set(GameDBRelationField.DISPLAYORDER, destRelation.getDisplayOrder());
            GameResourceServiceSngl.get().updateGameDbRelation(updateExpress2, relation.getRelationId());
        } else {
            return null;
        }
        return destRelation.getRelationId();
    }

    @RequestMapping(value = "/newslist")
    public ModelAndView newsList(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                                 @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                                 @RequestParam(value = "lineid", required = false) Long lineId,
                                 @RequestParam(value = "gamedbid", required = false) String gamedbid,
                                 @RequestParam(value = "validstatus", required = false) String validStatus) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("gamedbid", gamedbid);
        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);

        try {
            ClientLine clientLine = JoymeAppServiceSngl.get().getClientLine(new QueryExpress().add(QueryCriterions.eq(ClientLineField.LINE_ID, lineId)));
            if (clientLine == null) {
                return new ModelAndView("redirect:/clientline/item/list");
            }

            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(ClientLineItemField.LINE_ID, clientLine.getLineId()));
            if (StringUtil.isEmpty(validStatus)) {
                queryExpress.add(QueryCriterions.eq(ClientLineItemField.VALID_STATUS, ValidStatus.VALID.getCode()));
            } else {
                queryExpress.add(QueryCriterions.eq(ClientLineItemField.VALID_STATUS, validStatus));
            }

            queryExpress.add(QuerySort.add(ClientLineItemField.DISPLAY_ORDER, QuerySortOrder.ASC));
            PageRows<ClientLineItemDTO> itemPageRows = clientLineWebLogic.queryClientLineItemByPage(queryExpress, pagination);

            mapMessage.put("clientLine", clientLine);
            mapMessage.put("validstatus", validStatus);
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
        return new ModelAndView("gameclient/gametab/newslist", mapMessage);
    }

    @RequestMapping(value = "/news/createpage")
    public ModelAndView newsCreatePage(@RequestParam(value = "lineid", required = false) Long lineid,
                                       @RequestParam(value = "gamedbid", required = false) String gamedbid,
                                       @RequestParam(value = "validstatus", required = false) String validStatus) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            ClientLine clientLine = JoymeAppServiceSngl.get().getClientLine(new QueryExpress().add(QueryCriterions.eq(ClientLineField.LINE_ID, lineid)));

            mapMessage.put("line", clientLine);
            mapMessage.put("redirectCollection", AppRedirectType.getAll());
            mapMessage.put("redMessageType", RedMessageType.getAll());
            mapMessage.put("validstatus", validStatus);
            mapMessage.put("gamedbid", gamedbid);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }


        return new ModelAndView("gameclient/gametab/news-createpage", mapMessage);
    }

    @RequestMapping(value = "/news/create")
    public ModelAndView newsCreate(@RequestParam(value = "itemname", required = false) String itemname,
                                   @RequestParam(value = "desc", required = false) String desc,
                                   @RequestParam(value = "linkaddress", required = false) String linkaddress,
                                   @RequestParam(value = "lineid", required = false) String lineid,
                                   @RequestParam(value = "redirecttype", required = false) String redirecttype,
                                   @RequestParam(value = "category", required = false) String category,
                                   @RequestParam(value = "categorycolor", required = false) String categorycolor,
                                   @RequestParam(value = "createtime", required = false) String createTime,
                                   @RequestParam(value = "validstatus", required = false) String validStatus,
                                   @RequestParam(value = "gamedbid", required = false) String gamedbid,
                                   @RequestParam(value = "redmessagetype", required = false) String redmessagetype,
                                   @RequestParam(value = "redmessagetext", required = false) String redmessagetext) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        Map<String, Object> mapMsg = new HashMap<String, Object>();
        Map<String, String> errorMsgMap = new HashMap<String, String>();
        Map<String, String> lineItemKeyValues = new HashMap<String, String>();

        lineItemKeyValues.put("itemname", itemname);
        lineItemKeyValues.put("desc", desc);
        lineItemKeyValues.put("linkaddress", linkaddress);
        lineItemKeyValues.put("itemdomain", String.valueOf(ClientItemDomain.DEFAULT.getCode()));
        lineItemKeyValues.put("lineid", lineid);
        lineItemKeyValues.put("itemtype", String.valueOf(ClientItemType.CUSTOM.getCode()));
        lineItemKeyValues.put("redirecttype", redirecttype);
        lineItemKeyValues.put("category", category);
        lineItemKeyValues.put("categorycolor", categorycolor);
        lineItemKeyValues.put("status", ValidStatus.INVALID.getCode());
        if (!StringUtil.isEmpty(createTime)) {
            lineItemKeyValues.put("itemcreatedate", createTime);
        }

        if (!StringUtil.isEmpty("redmessagetype")) {
            ParamTextJson textJson = new ParamTextJson();
            textJson.setRedMessageType(redmessagetype);
            textJson.setRedMessageText(redmessagetext);
            lineItemKeyValues.put("paramtextjson", textJson.toJson());
        }

        try {
            ClientLineItem clientLineItem = ClientLineWebDataProcessorFactory.get().factory(ClientItemDomain.DEFAULT).generateAddLineItem(lineItemKeyValues, errorMsgMap, mapMsg);

            if (!CollectionUtil.isEmpty(errorMsgMap)) {
                GAlerter.lab(this.getClass().getName() + " clientLineItem is null; ");
                mapMessage.put("errorMsgMap", errorMsgMap);
                mapMessage.put("lineid", lineid);
                return new ModelAndView("forward:/clientline/item/createpage", mapMessage);
            }

            JoymeAppServiceSngl.get().createClientLineItem(clientLineItem);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/gameclient/gametab/newslist?lineid=" + lineid + "&validstatus=" + validStatus + "&gamedbid=" + gamedbid);
    }


    @RequestMapping(value = "/news/modifypage")
    public ModelAndView modifyLinePage(@RequestParam(value = "itemid", required = true) Long itemid,
                                       @RequestParam(value = "validstatus", required = false) String validStatus,
                                       @RequestParam(value = "gamedbid", required = false) String gamedbid) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            ClientLineItem clientLineItem = JoymeAppServiceSngl.get().getClientLineItem(new QueryExpress().add(QueryCriterions.eq(ClientLineItemField.ITEM_ID, itemid)));
            if (clientLineItem != null && clientLineItem.getItemCreateDate() != null) {
                SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String updatetime = sd.format(clientLineItem.getItemCreateDate());
                mapMessage.put("updatetime", updatetime);
            }

            mapMessage.put("clienLineItem", clientLineItem);
            mapMessage.put("validstatus", validStatus);
            mapMessage.put("redirectCollection", AppRedirectType.getAll());
            mapMessage.put("redMessageType", RedMessageType.getAll());
            mapMessage.put("gamedbid", gamedbid);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("gameclient/gametab/news-modifypage", mapMessage);
    }

    @RequestMapping(value = "/news/modify")
    public ModelAndView modifyLine(@RequestParam(value = "itemid", required = false) Long itemid,
                                   @RequestParam(value = "lineid", required = false) Long lineid,
                                   @RequestParam(value = "itemname", required = false) String itemname,
                                   @RequestParam(value = "desc", required = false) String desc,
                                   @RequestParam(value = "redirecttype", required = false) String redirecttype,
                                   @RequestParam(value = "linkaddress", required = false) String linkaddress,
                                   @RequestParam(value = "createtime", required = false) String updateTime,
                                   @RequestParam(value = "category", required = false) String category,
                                   @RequestParam(value = "categorycolor", required = false) String categorycolor,
                                   @RequestParam(value = "validstatus", required = false) String validStatus,
                                   @RequestParam(value = "gamedbid", required = false) String gamedbid,
                                   @RequestParam(value = "redmessagetype", required = false) String redmessagetype,
                                   @RequestParam(value = "redmessagetext", required = false) String redmessagetext) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(ClientLineItemField.TITLE, itemname);
        updateExpress.set(ClientLineItemField.DESC, desc);
        updateExpress.set(ClientLineItemField.REDIRCT_TYPE, Integer.parseInt(redirecttype));
        updateExpress.set(ClientLineItemField.URL, linkaddress);
        if (!StringUtil.isEmpty(category)) {
            updateExpress.set(ClientLineItemField.CATEGORY, category);
        }
        if (!StringUtil.isEmpty(categorycolor)) {
            updateExpress.set(ClientLineItemField.CATEGORY_COLOR, categorycolor);
        }

        if (!StringUtil.isEmpty("redmessagetype")) {
            ParamTextJson textJson = new ParamTextJson();
            textJson.setRedMessageType(redmessagetype);
            textJson.setRedMessageText(redmessagetext);
            updateExpress.set(ClientLineItemField.ITEM_PARAM, textJson.toJson());
        }

        try {
            if (!StringUtil.isEmpty(updateTime)) {
                SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                updateExpress.set(ClientLineItemField.ITEM_CREATE_DATE, sd.parse(updateTime));
            }
            JoymeAppServiceSngl.get().modifyClientLineItem(updateExpress, itemid);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        } catch (ParseException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }

        return new ModelAndView("redirect:/gameclient/gametab/newslist?lineid=" + lineid + "&validstatus=" + validStatus + "&gamedbid=" + gamedbid);
    }


    @RequestMapping(value = "/news/delete")
    public ModelAndView deleteNews(@RequestParam(value = "itemid", required = false) Long itemId,
                                   @RequestParam(value = "lineid", required = false) Long lineId,
                                   @RequestParam(value = "validstatus", required = false) String validStatus,
                                   @RequestParam(value = "gamedbid", required = false) String gamedbid) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        if (itemId == null) {
            return new ModelAndView("redirect:/gameclient/gametab/newslist?lineid=" + lineId + "&validstatus=" + validStatus + "&gamedbid=" + gamedbid);
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

        return new ModelAndView("redirect:/gameclient/gametab/newslist?lineid=" + lineId + "&validstatus=" + validStatus + "&gamedbid=" + gamedbid);
    }

    @RequestMapping(value = "/news/recover")
    public ModelAndView recoverNews(@RequestParam(value = "itemid", required = false) Long itemId,
                                    @RequestParam(value = "lineid", required = false) Long lineId,
                                    @RequestParam(value = "gamedbid", required = false) Long gamedbid,
                                    @RequestParam(value = "validstatus", required = false) String validStatus) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        if (itemId == null) {
            return new ModelAndView("redirect:/gameclient/gametab/newslist?lineid=" + lineId + "&validstatus=" + validStatus);
        }
        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(ClientLineItemField.VALID_STATUS, ValidStatus.VALID.getCode());
        try {

            boolean bool = JoymeAppServiceSngl.get().modifyClientLineItem(updateExpress, itemId);
            if (bool) {
                ClientLineItem clientLineItem = JoymeAppServiceSngl.get().getClientLineItem(new QueryExpress().add(QueryCriterions.eq(ClientLineItemField.ITEM_ID, itemId)));
                //最新修改需要发一个事件
                GameDBModifyTimeFieldJson json = new GameDBModifyTimeFieldJson();
                long curTime = new Date().getTime();
                json.setLastModifyTime(curTime);

                GameDB gameDb = GameResourceServiceSngl.get().getGameDB(new BasicDBObject().append("_id", gamedbid), false);
                if (gameDb != null) {

                    boolean isUpdate = true;

                    //判断是否是当天
                    if (gameDb.getModifyTime() != null && DateUtil.isToday(new Date(gameDb.getModifyTime().getRedMessageTime()))) {
                        if (Integer.valueOf(gameDb.getModifyTime().getRedMessageType()) > Integer.valueOf(clientLineItem.getParam().getRedMessageType())) {
                            isUpdate = false;
                        }
                    }


                    if (isUpdate) {
                        json.setRedMessageTime(curTime);
                        if (clientLineItem != null && clientLineItem.getParam() != null) {
                            json.setRedMessageType(clientLineItem.getParam().getRedMessageType());
                            if (clientLineItem.getParam().getRedMessageType().equals("7")) {
                                json.setRedMessageText("礼");
                            } else if (clientLineItem.getParam().getRedMessageType().equals("6")) {
                                json.setRedMessageText("活");
                            } else if (clientLineItem.getParam().getRedMessageType().equals("5")) {
                                json.setRedMessageText("新");
                            } else if (clientLineItem.getParam().getRedMessageType().equals("4")) {
                                json.setRedMessageText("攻");
                            } else {
                                json.setRedMessageText(clientLineItem.getParam().getRedMessageText());
                            }
                        }
                    }
                }
                gameClientWebLogic.sendGameDBModifyTimeEvent(gamedbid, json);

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

        return new ModelAndView("redirect:/gameclient/gametab/newslist?lineid=" + lineId + "&validstatus=" + validStatus + "&gamedbid=" + gamedbid);
    }

    @RequestMapping(value = "/news/sort/{sort}")
    @ResponseBody
    public String sortNews(@PathVariable(value = "sort") String sort,
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

}
