package com.enjoyf.webapps.tools.webpage.controller.gameclient;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.gameres.gamedb.GameDB;
import com.enjoyf.platform.service.gameres.gamedb.GameDBCover;
import com.enjoyf.platform.service.gameres.gamedb.GameDBField;
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
import com.enjoyf.platform.service.joymeapp.clientline.ClientLineWebDataProcessorFactory;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryCriterions;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryExpress;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.webapps.tools.weblogic.clientline.ClientLineWebLogic;
import com.enjoyf.webapps.tools.weblogic.dto.GameDBSmallDTO;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mongodb.BasicDBObject;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by tonydiao on 2014/12/18.
 */

//新游开测、热门、正在玩的controller
@Controller
@RequestMapping(value = "/gameclient/clientline/game")
public class GameClientGameController extends ToolsBaseController {


    @Resource(name = "i18nSource")
    private ResourceBundleMessageSource i18nSource;


    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "20") int pageSize) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();


        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
        try {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(ClientLineField.ITEM_TYPE, ClientItemType.GAME.getCode()));
            queryExpress.add(QueryCriterions.eq(ClientLineField.LINE_TYPE, ClientLineType.GAMECLIENT.getCode()));

            PageRows<ClientLine> pageRows = JoymeAppServiceSngl.get().queryClientLineByPage(queryExpress, pagination);
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                mapMessage.put("list", pageRows.getRows());
                mapMessage.put("page", pageRows.getPage());
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/gameclient/game/linelist", mapMessage);
        }
        return new ModelAndView("/gameclient/game/linelist", mapMessage);
    }

    @RequestMapping(value = "/itemlist")
    public ModelAndView itemlist(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                                 @RequestParam(value = "maxPageItems", required = false, defaultValue = "50") int pageSize,
                                 @RequestParam(value = "lineId", required = false) Long lineId,
                                 @RequestParam(value = "lineName", required = false) String lineName,
                                 @RequestParam(value = "lineCode", required = false) String lineCode,
                                 @RequestParam(value = "gamename", required = false) String gamename) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("lineId", lineId);
        mapMessage.put("lineName", lineName);
        mapMessage.put("lineCode", lineCode);
        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);

        if (lineId == null) {
            return new ModelAndView("/gameclient/game/itemlist");
        }
        try {

            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(ClientLineItemField.LINE_ID, lineId));
            queryExpress.add(QueryCriterions.eq(ClientLineItemField.VALID_STATUS, ValidStatus.VALID.getCode()));
            //如果是新游开测按日期降序排列，热门和正在玩按display_order排序
            if (!StringUtil.isEmpty(lineCode) && !lineCode.contains("gc_newgame")) {
                queryExpress.add(QuerySort.add(ClientLineItemField.DISPLAY_ORDER, QuerySortOrder.ASC));
            } else if (!StringUtil.isEmpty(lineCode) && lineCode.contains("gc_newgame")) {
                queryExpress.add(QuerySort.add(ClientLineItemField.ITEM_CREATE_DATE, QuerySortOrder.DESC));
            }


            //搜索游戏
            if (!StringUtil.isEmpty(gamename)) {
                MongoQueryExpress mongoQueryExpress = new MongoQueryExpress();
                mongoQueryExpress.add(MongoQueryCriterions.like(GameDBField.GAMENAME, gamename));
                List<GameDB> gameDBList = GameResourceServiceSngl.get().queryGameDB(mongoQueryExpress);
                if (!CollectionUtil.isEmpty(gameDBList)) {
                    Set<String> gameidArr = new HashSet<String>();
                    for (GameDB gameDB : gameDBList) {
                        gameidArr.add(String.valueOf(gameDB.getGameDbId()));
                    }
                    queryExpress.add(QueryCriterions.in(ClientLineItemField.DIRECT_ID, gameidArr.toArray()));
                }
            }

            //把属于这个clientline 的所有item都分页查出来
            PageRows<ClientLineItem> itemPageRows = JoymeAppServiceSngl.get().queryClientLineItemByPage(queryExpress, pagination);
            if (itemPageRows != null && !CollectionUtil.isEmpty(itemPageRows.getRows())) {
                List<ClientLineItem> clientLineItemList = itemPageRows.getRows();

                Set<Long> directIdSet = new HashSet<Long>();
                Map<Long, ClientLineItem> clientLineItemMap = new HashMap<Long, ClientLineItem>();

                for (int i = 0; i < clientLineItemList.size(); i++) {
                    Long longTemp = Long.valueOf(clientLineItemList.get(i).getDirectId());
                    directIdSet.add(longTemp);
                }

                //根据set查询mongodb的game库 的game_db表
                Map<Long, GameDB> gameDBMap = GameResourceServiceSngl.get().queryGameDBSet(directIdSet);

                //添充GameDBSmallDTO 形成的List
                List<GameDBSmallDTO> smallList = new ArrayList<GameDBSmallDTO>();

                for (int i = 0; i < clientLineItemList.size(); i++) {
                    Long longTemp = Long.valueOf(clientLineItemList.get(i).getDirectId());
                    GameDB gameDB = gameDBMap.get(longTemp);
                    if (gameDB != null) {
                        GameDBSmallDTO temp = new GameDBSmallDTO();
                        temp.setGameid(gameDB.getGameDbId());
                        temp.setName(gameDB.getGameName());
                        String recommendReason = gameDB.getDownloadRecommend();
                        if (recommendReason == null) {
                            recommendReason = "";
                        }
                        temp.setDescription(recommendReason);
                        temp.setIcon(gameDB.getGameIcon());
                        temp.setGamePublicTime(clientLineItemList.get(i).getItemCreateDate());
                        temp.setItemId(clientLineItemList.get(i).getItemId());

                        if (!StringUtil.isEmpty(lineCode) && lineCode.contains("gc_newgame")) {
                            String categoryColor = clientLineItemList.get(i).getCategoryColor();
                            if (!StringUtil.isEmpty(categoryColor)) {
                                temp.setCategoryColor(categoryColor);
                                if (categoryColor.equals("gamecustom")) {
                                    temp.setUrl(clientLineItemList.get(i).getUrl());
                                    temp.setRedirectType(clientLineItemList.get(i).getRedirectType());
                                }
                            } else {
                                temp.setCategoryColor("");
                            }
                            String rate = clientLineItemList.get(i).getRate();
                            if (!StringUtil.isEmpty(rate)) {
                                temp.setShowType(rate);
                                if (rate.equals("2") && !StringUtil.isEmpty(clientLineItemList.get(i).getAuthor())) {
                                    temp.setCustomContent(clientLineItemList.get(i).getAuthor());
                                }
                            }
                        }

                        if (!StringUtil.isEmpty(lineCode)) {
                            temp.setDisplayOrder(clientLineItemList.get(i).getDisplayOrder());
                        }
                        smallList.add(temp);
                    }
                }

                //按 gamePublicTime字段由大到小排序---逆自然顺序
                //如果是新游开测按日期降序排列，热门和正在玩按display_order排序
                if (!StringUtil.isEmpty(lineCode) && lineCode.contains("gc_newgame")) {
                    Collections.sort(smallList);
                }

                mapMessage.put("list", smallList);
                mapMessage.put("page", itemPageRows.getPage());
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/gameclient/game/itemlist", mapMessage);
    }


    @RequestMapping(value = "/itemcreatepage")
    public ModelAndView itemcreatepage(@RequestParam(value = "lineId", required = false) Long lineId,
                                       @RequestParam(value = "lineCode", required = false) String lineCode,
                                       @RequestParam(value = "lineName", required = false) String lineName) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("lineId", lineId);
        mapMessage.put("lineCode", lineCode);
        mapMessage.put("lineName", lineName);

        try {
            if (!StringUtil.isEmpty(lineCode) && lineCode.contains("gc_newgame")) {
                Map<Integer, String> types = new HashMap<Integer, String>();
                Class appRedirectType = AppRedirectType.class;
                Field[] fields = appRedirectType.getFields();
                for (int i = 0; i < fields.length; i++) {
                    if (fields[i].getType().equals(appRedirectType)) {
                        int code = ((AppRedirectType) fields[i].get(null)).getCode();
                        types.put(code, fields[i].getName());
                    }
                }
                mapMessage.put("types", types);
            }
        } catch (IllegalAccessException e) {
            GAlerter.lab(this.getClass().getName() + " occured IllegalAccessException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("/gameclient/game/createitem", mapMessage);
    }


    @RequestMapping(value = "/itemcreate")
    public ModelAndView itemcreate(@RequestParam(value = "lineId", required = false) Long lineId,
                                   @RequestParam(value = "gameDbId", required = false) String gameDbId,
                                   @RequestParam(value = "gamePublicTime", required = false) String gamePublicTime,
                                   @RequestParam(value = "lineCode", required = false) String lineCode,
                                   @RequestParam(value = "lineName", required = false) String lineName,
                                   @RequestParam(value = "isHot", required = false) String isHot,
                                   @RequestParam(value = "category", required = false) String category,
                                   @RequestParam(value = "startTime", required = false) String startTime,
                                   @RequestParam(value = "endTime", required = false) String endTime,
                                   @RequestParam(value = "categoryColor", required = false, defaultValue = "") String categoryColor,
                                   @RequestParam(value = "redirectType", required = false) String redirectType,
                                   @RequestParam(value = "url", required = false) String url,
                                   @RequestParam(value = "showType", required = false) String showType,
                                   @RequestParam(value = "customShowContent", required = false) String customShowContent,
                                   HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("lineId", lineId);
        mapMessage.put("lineCode", lineCode);
        mapMessage.put("lineName", lineName);


/*
        是否出现在热门页   is_hot     --tinyint 4        1 出现，0 不出现

        是否出现热门页中的热门块  category     varchar 256     1 出现，0 不出现

        开始时间         start_date           timestamp   以天为单位  存日期yyyy-MM-dd

        持续时间         contentid      bigint 16     以天为单位  单位秒  不能超过1年 3600*24 * 365    =31536000

        showType             rate           varchar 32
        customShowContent            author              varchar 64


       #只有当出现在热门页是   值是1的时候， 才能设置是否出现在 热门块，
       #只有当出现在热门块的值是1的时候，才能设置 开始时间和持续时间
       # rate ,author

        */

        try {
            QueryExpress queryExpressIfExist = new QueryExpress();
            queryExpressIfExist.add(QueryCriterions.eq(ClientLineItemField.DIRECT_ID, gameDbId));
            queryExpressIfExist.add(QueryCriterions.eq(ClientLineItemField.LINE_ID, lineId));

            ClientLineItem clientLineItem = JoymeAppServiceSngl.get().getClientLineItem(queryExpressIfExist);
            if (clientLineItem != null && clientLineItem.getValidStatus().equals(ValidStatus.REMOVED)) {
                //update  更新
                UpdateExpress updateExpress = new UpdateExpress();

                if (!StringUtil.isEmpty(gamePublicTime)) {
                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date itemCreateDate = format.parse(gamePublicTime);
                    updateExpress.set(ClientLineItemField.ITEM_CREATE_DATE, itemCreateDate);
                }

                //针对新游开测榜的设置
                if (lineCode != null && lineCode.contains("gc_newgame")) {
                    updateExpress.set(ClientLineItemField.DISPLAYTYPE, Integer.valueOf(isHot));
                    updateExpress.set(ClientLineItemField.CATEGORY, category);

                    if (isHot != null && isHot.equals("1") && category != null && category.equals("1")) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                        updateExpress.set(ClientLineItemField.STATE_DATE, sdf.parse(startTime + " 00:00:00"));
                        updateExpress.set(ClientLineItemField.CONTENTID, (sdf.parse(endTime + " 23:59:59").getTime() - sdf.parse(startTime + " 00:00:00").getTime()) / 1000);
                    }

                    updateExpress.set(ClientLineItemField.CATEGORY_COLOR, categoryColor);
                    if (!StringUtil.isEmpty(categoryColor) && categoryColor.equals("gamecustom")) {
                        updateExpress.set(ClientLineItemField.URL, url);
                        updateExpress.set(ClientLineItemField.REDIRCT_TYPE, Integer.valueOf(redirectType));
                    }
                    DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Long result = new Date().getTime() - sdf.parse("2010-01-01 00:00:00").getTime();
                    result = result / 1000;
                    int displayOrder = Integer.MAX_VALUE - result.intValue();     //0到1989158807 之间的一个数
                    updateExpress.set(ClientLineItemField.DISPLAY_ORDER, displayOrder);


                    if (!StringUtil.isEmpty(showType)) {
                        if (showType.equals("1")) {
                            updateExpress.set(ClientLineItemField.RATE, showType);
                        } else if (showType.equals("2")) {
                            updateExpress.set(ClientLineItemField.RATE, showType);
                            updateExpress.set(ClientLineItemField.AUTHOR, customShowContent);
                        }

                        //同步更新gameDb中的相应三个字段
                        long gameDbIdLong = Long.valueOf(gameDbId);
                        GameDB gameDB = GameResourceServiceSngl.get().getGameDB(new BasicDBObject().append("_id", gameDbIdLong), false);
                        BasicDBObject queryDBObject = new BasicDBObject();
                        queryDBObject.put(GameDBField.ID.getColumn(), gameDbIdLong);
                        BasicDBObject updateDBObject = new BasicDBObject();
                        GameDBCover cover = gameDB.getGameDBCover();
                        long posterGamePublicTimeLong = sdf.parse(gamePublicTime).getTime();
                        if (lineCode.endsWith("0")) {       //ios
                            cover.setPosterShowTypeIos(showType);
                            cover.setPosterShowContentIos(customShowContent);
                            cover.setPosterGamePublicTimeIos(String.valueOf(posterGamePublicTimeLong)); //数据库中存放long值
                        } else {                                      //android
                            cover.setPosterShowTypeAndroid(showType);
                            cover.setPosterShowContentAndroid(customShowContent);
                            cover.setPosterGamePublicTimeAndroid(String.valueOf(posterGamePublicTimeLong)); //数据库中存放long值
                        }
                        updateDBObject.put(GameDBField.GAMEDB_COVER.getColumn(), cover.toJson());
                        GameResourceServiceSngl.get().updateGameDB(queryDBObject, updateDBObject);
                    }


                }

                updateExpress.set(ClientLineItemField.VALID_STATUS, ValidStatus.VALID.getCode());
                JoymeAppServiceSngl.get().modifyClientLineItem(updateExpress, clientLineItem.getItemId());

                ToolsLog log = new ToolsLog();
                log.setOpUserId(getCurrentUser().getUserid());//用户的ID
                log.setOperType(LogOperType.GAMECLIENT_GAME_MODIFY);  //操作的类型
                log.setOpTime(new Date());//操作时间
                log.setOpIp(getIp());//用户IP
                Map<String, String[]> params = request.getParameterMap();
                String queryString = "  ";
                for (String key : params.keySet()) {
                    String[] values = params.get(key);
                    for (int i = 0; i < values.length; i++) {
                        queryString += key + "=" + values[i] + "&";
                    }
                }
                // 去掉最后一个空格
                queryString = queryString.substring(0, queryString.length() - 1);

                if (queryString.length() > 1900) {
                    queryString = queryString.substring(0, 1900);
                }

                log.setOpAfter("新游开测，大家正在玩，热门修改-->queryString:" + queryString); //描述 推荐用中文
                addLog(log);


            } else {

                //针对新游开测榜的设置
                if (!StringUtil.isEmpty(lineCode) && lineCode.contains("gc_newgame")) {
                    ClientLineItem clientLineItemTemp = new ClientLineItem();
                    clientLineItemTemp.setDisplayType(Integer.valueOf(isHot));
                    clientLineItemTemp.setCategory(category);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    if (!StringUtil.isEmpty(isHot) && isHot.equals("1") && !StringUtil.isEmpty(category) && category.equals("1")) {
                        clientLineItemTemp.setStateDate(sdf.parse(startTime + " 00:00:00"));
                        clientLineItemTemp.setContentid((sdf.parse(endTime + " 23:59:59").getTime() - sdf.parse(startTime + " 00:00:00").getTime()) / 1000);
                    }

                    clientLineItemTemp.setCategoryColor(categoryColor);
                    if (!StringUtil.isEmpty(categoryColor) && categoryColor.equals("gamecustom")) {
                        clientLineItemTemp.setUrl(StringUtil.isEmpty(url) ? "" : url);
                        clientLineItemTemp.setRedirectType(AppRedirectType.getByCode(Integer.parseInt(redirectType)));
                    }


                    Long result = new Date().getTime() - sdf.parse("2010-01-01 00:00:00").getTime();
                    result = result / 1000;
                    int displayOrder = Integer.MAX_VALUE - result.intValue();     //0到1989158807 之间的一个数
                    clientLineItemTemp.setDisplayOrder(displayOrder);

                    clientLineItemTemp.setRate(showType);
                    if (!StringUtil.isEmpty(showType)) {
                        if (showType.equals("2")) {
                            clientLineItemTemp.setAuthor(customShowContent);
                        }


                        //同步更新gameDb中的相应三个字段
                        long gameDbIdLong = Long.valueOf(gameDbId);
                        GameDB gameDB = GameResourceServiceSngl.get().getGameDB(new BasicDBObject().append("_id", gameDbIdLong), false);
                        BasicDBObject queryDBObject = new BasicDBObject();
                        queryDBObject.put(GameDBField.ID.getColumn(), gameDbIdLong);
                        BasicDBObject updateDBObject = new BasicDBObject();
                        GameDBCover cover = gameDB.getGameDBCover();
                        DateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        long posterGamePublicTimeLong = sdf2.parse(gamePublicTime).getTime();
                        if (lineCode.endsWith("0")) {       //ios
                            cover.setPosterShowTypeIos(showType);
                            cover.setPosterShowContentIos(customShowContent);
                            cover.setPosterGamePublicTimeIos(String.valueOf(posterGamePublicTimeLong)); //数据库中存放long值
                        } else {                                      //android
                            cover.setPosterShowTypeAndroid(showType);
                            cover.setPosterShowContentAndroid(customShowContent);
                            cover.setPosterGamePublicTimeAndroid(String.valueOf(posterGamePublicTimeLong)); //数据库中存放long值
                        }
                        updateDBObject.put(GameDBField.GAMEDB_COVER.getColumn(), cover.toJson());
                        GameResourceServiceSngl.get().updateGameDB(queryDBObject, updateDBObject);

                    }
                    clientLineItemTemp.setItemDomain(ClientItemDomain.GAME);
                    clientLineItemTemp.setLineId(lineId);
                    clientLineItemTemp.setItemType(ClientItemType.GAME);
                    clientLineItemTemp.setDirectId(gameDbId);
                    clientLineItemTemp.setItemCreateDate(sdf.parse(gamePublicTime));
                    clientLineItemTemp.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));

                    JoymeAppServiceSngl.get().createClientLineItem(clientLineItemTemp);
                } else {     //针对，热门，正在玩的设置
                    //插入
                    Map<String, String> errorMsgMap = new HashMap<String, String>();
                    Map<String, String> lineItemKeyValues = new HashMap<String, String>();
                    Map<String, Object> mapMsg = new HashMap<String, Object>();


                    lineItemKeyValues.put("itemdomain", String.valueOf(ClientItemDomain.GAME.getCode()));
                    lineItemKeyValues.put("lineid", String.valueOf(lineId));
                    lineItemKeyValues.put("itemtype", String.valueOf(ClientItemType.GAME.getCode()));
                    lineItemKeyValues.put("directid", gameDbId);

                    if (!StringUtil.isEmpty(lineCode) && (lineCode.contains("gc_hotgame") || lineCode.contains("gc_recommendgame"))) {
                        lineItemKeyValues.put("redirecttype", String.valueOf(AppRedirectType.WEBVIEW.getCode()));
                    }
                    lineItemKeyValues.put("itemcreatedate", gamePublicTime);
                    ClientLineItem clientLineItemInsert = ClientLineWebDataProcessorFactory.get().factory(ClientItemDomain.GAME).generateAddLineItem(lineItemKeyValues, errorMsgMap, mapMsg);

                    JoymeAppServiceSngl.get().createClientLineItem(clientLineItemInsert);

                }
                ToolsLog log = new ToolsLog();
                log.setOpUserId(getCurrentUser().getUserid());//用户的ID
                log.setOperType(LogOperType.GAMECLIENT_GAME_ADD);  //操作的类型
                log.setOpTime(new Date());//操作时间
                log.setOpIp(getIp());//用户IP

                Map<String, String[]> params = request.getParameterMap();
                String queryString = "  ";
                for (String key : params.keySet()) {
                    String[] values = params.get(key);
                    for (int i = 0; i < values.length; i++) {
                        queryString += key + "=" + values[i] + "&";
                    }
                }
                // 去掉最后一个空格
                queryString = queryString.substring(0, queryString.length() - 1);

                if (queryString.length() > 1900) {
                    queryString = queryString.substring(0, 1900);
                }

                log.setOpAfter("新游开测，大家正在玩，热门添加-->queryString:" + queryString); //描述 推荐用中文
                addLog(log);

            }

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("forward:/gameclient/clientline/game/itemlist", mapMessage);
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

    //在本分页内交换任意两行的排序
    @RequestMapping(value = "/swaptwo")
    @ResponseBody
    public String swaptwo(@RequestParam(value = "type", required = false) String type,
                          @RequestParam(value = "fromItemId", required = false) Long fromItemId,
                          @RequestParam(value = "fromOrder", required = false) int fromOrder,
                          @RequestParam(value = "toItemId", required = false) Long toItemId,
                          @RequestParam(value = "toOrder", required = false) int toOrder) {
        JsonBinder binder = JsonBinder.buildNormalBinder();
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        ResultObjectMsg resultObjectMsg = new ResultObjectMsg(ResultObjectMsg.CODE_E);

        try {
            UpdateExpress updateExpressFrom = new UpdateExpress();
            int resultOrder = 0;
            Random random = new Random();


            if (!StringUtil.isEmpty(type)) {
                if (type.equals("totop")) {
                    resultOrder = toOrder - (random.nextInt(20) + 1);
                    updateExpressFrom.set(ClientLineItemField.DISPLAY_ORDER, resultOrder);
                } else if (type.equals("tobottom")) {
                    resultOrder = toOrder + (random.nextInt(20) + 1);
                    updateExpressFrom.set(ClientLineItemField.DISPLAY_ORDER, resultOrder);
                } else if (type.equals("up10")) {
                    resultOrder = toOrder - (random.nextInt(20) + 1);
                    updateExpressFrom.set(ClientLineItemField.DISPLAY_ORDER, resultOrder);
                } else if (type.equals("down10")) {
                    resultOrder = toOrder + (random.nextInt(20) + 1);
                    updateExpressFrom.set(ClientLineItemField.DISPLAY_ORDER, resultOrder);
                }
                JoymeAppServiceSngl.get().modifyClientLineItem(updateExpressFrom, fromItemId);
            }

            resultObjectMsg = new ResultObjectMsg(ResultObjectMsg.CODE_S);
            mapMessage.put("resultOrder", resultOrder);
            resultObjectMsg.setResult(mapMessage);
        } catch (Exception e) {
            resultObjectMsg.setRs(ResultObjectMsg.CODE_E);
            resultObjectMsg.setMsg(i18nSource.getMessage("system.error", null, null));
            return binder.toJson(resultObjectMsg);
        }
        return binder.toJson(resultObjectMsg);
    }


    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyLinePage(@RequestParam(value = "lineId", required = false) Long lineId) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();
        if (lineId == null) {
            return new ModelAndView("forward:/gameclient/clientline/list");
        }


        try {
            ClientLine clientLine = JoymeAppServiceSngl.get().getClientLine(new QueryExpress().add(QueryCriterions.eq(ClientLineField.LINE_ID, lineId)));

            if (clientLine == null) {
                return new ModelAndView("forward:/gameclient/clientline/list");
            }
            mapMessage.put("clientLine", clientLine);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("/gameclient/game/modifyline", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modifyLine(@RequestParam(value = "lineId", required = false) Long lineId,
                                   @RequestParam(value = "lineName", required = false) String lineName,
                                   @RequestParam(value = "lineCode", required = false) String lineCode) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();


        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(ClientLineField.LINE_NAME, lineName);
        updateExpress.set(ClientLineField.CODE, lineCode);
        updateExpress.set(ClientLineField.MODIFY_DATE, new Date());
        updateExpress.set(ClientLineField.MODIFY_USERID, getCurrentUser().getUserid());

        try {
            ClientLine existLine = JoymeAppServiceSngl.get().getClientLine(new QueryExpress().add(QueryCriterions.eq(ClientLineField.CODE, lineCode)).add(QueryCriterions.ne(ClientLineField.LINE_ID, lineId)));
            if (existLine != null) {
                mapMessage.put("lineId", lineId);
                mapMessage.put("codeExist", "line.code.exist");
                return new ModelAndView("forward:/gameclient/clientline/modifypage", mapMessage);
            }

            Boolean bool = JoymeAppServiceSngl.get().modifyClientLine(updateExpress, new QueryExpress().add(QueryCriterions.eq(ClientLineField.LINE_ID, lineId)));
            if (bool) {
                ToolsLog log = new ToolsLog();

                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.MODIFY_CLIENT_LINE);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("clientLineId:" + lineId);

                addLog(log);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("forward:/gameclient/clientline/game/list");
    }


    @RequestMapping(value = "/itemmodifypage")
    public ModelAndView itemmodifypage(@RequestParam(value = "itemId", required = true) Long itemId,
                                       @RequestParam(value = "lineName", required = false) String lineName,
                                       @RequestParam(value = "lineCode", required = false) String lineCode,
                                       @RequestParam(value = "name", required = false) String name) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("lineName", lineName);
        mapMessage.put("lineCode", lineCode);
        mapMessage.put("name", name);
        try {

            if (!StringUtil.isEmpty(lineCode) && lineCode.contains("gc_newgame")) {
                Map<Integer, String> types = new HashMap<Integer, String>();
                Class appRedirectType = AppRedirectType.class;
                Field[] fields = appRedirectType.getFields();
                for (int i = 0; i < fields.length; i++) {
                    if (fields[i].getType().equals(appRedirectType)) {
                        int code = ((AppRedirectType) fields[i].get(null)).getCode();
                        types.put(code, fields[i].getName());
                    }
                }
                mapMessage.put("types", types);
            }

            ClientLineItem clientLineItem = JoymeAppServiceSngl.get().getClientLineItem(new QueryExpress().add(QueryCriterions.eq(ClientLineItemField.ITEM_ID, itemId)));
            mapMessage.put("item", clientLineItem);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        } catch (IllegalAccessException e) {
            GAlerter.lab(this.getClass().getName() + " occured IllegalAccessException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }


        return new ModelAndView("/gameclient/game/modifyitem", mapMessage);
    }

    @RequestMapping(value = "/itemmodify")
    public ModelAndView itemmodify(@RequestParam(value = "itemId", required = false) Long itemId,
                                   @RequestParam(value = "lineId", required = false) Long lineId,
                                   @RequestParam(value = "lineName", required = false) String lineName,
                                   @RequestParam(value = "gamePublicTime", required = false) String gamePublicTime,
                                   @RequestParam(value = "lineCode", required = false) String lineCode,
                                   @RequestParam(value = "isHot", required = false) String isHot,
                                   @RequestParam(value = "category", required = false) String category,
                                   @RequestParam(value = "startTime", required = false) String startTime,
                                   @RequestParam(value = "endTime", required = false) String endTime,
                                   @RequestParam(value = "categoryColor", required = false, defaultValue = "") String categoryColor,
                                   @RequestParam(value = "redirectType", required = false) String redirectType,
                                   @RequestParam(value = "url", required = false) String url,
                                   @RequestParam(value = "showType", required = false) String showType,
                                   @RequestParam(value = "customShowContent", required = false) String customShowContent,
                                   @RequestParam(value = "displayOrder", required = false) String displayOrder,
                                   HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("lineId", lineId);
        mapMessage.put("lineName", lineName);
        mapMessage.put("lineCode", lineCode);
        try {
            UpdateExpress updateExpress = new UpdateExpress();

            if (!StringUtil.isEmpty(gamePublicTime)) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date itemCreateDate = format.parse(gamePublicTime);
                updateExpress.set(ClientLineItemField.ITEM_CREATE_DATE, itemCreateDate);
            }

            //针对新游开测榜的设置
            if (lineCode != null && lineCode.contains("gc_newgame")) {
                updateExpress.set(ClientLineItemField.DISPLAYTYPE, Integer.valueOf(isHot));
                updateExpress.set(ClientLineItemField.CATEGORY, category);

                if (isHot != null && isHot.equals("1") && category != null && category.equals("1")) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    updateExpress.set(ClientLineItemField.STATE_DATE, sdf.parse(startTime + " 00:00:00"));
                    updateExpress.set(ClientLineItemField.CONTENTID, (sdf.parse(endTime + " 23:59:59").getTime() - sdf.parse(startTime + " 00:00:00").getTime()) / 1000);
                }
                updateExpress.set(ClientLineItemField.CATEGORY_COLOR, categoryColor);
                if (!StringUtil.isEmpty(categoryColor) && categoryColor.equals("gamecustom")) {
                    updateExpress.set(ClientLineItemField.URL, url);
                    updateExpress.set(ClientLineItemField.REDIRCT_TYPE, Integer.valueOf(redirectType));
                }

                if (!StringUtil.isEmpty(showType)) {
                    if (showType.equals("1")) {
                        updateExpress.set(ClientLineItemField.RATE, showType);
                    } else if (showType.equals("2")) {
                        updateExpress.set(ClientLineItemField.RATE, showType);
                        updateExpress.set(ClientLineItemField.AUTHOR, customShowContent);
                    }

                    //同步更新gameDb中的相应三个字段
                    ClientLineItem clientLineItem = JoymeAppServiceSngl.get().getClientLineItem(new QueryExpress().add(QueryCriterions.eq(ClientLineItemField.ITEM_ID, itemId)));
                    long gameDbIdLong = Long.valueOf(clientLineItem.getDirectId());
                    GameDB gameDB = GameResourceServiceSngl.get().getGameDB(new BasicDBObject().append("_id", gameDbIdLong), false);
                    BasicDBObject queryDBObject = new BasicDBObject();
                    queryDBObject.put(GameDBField.ID.getColumn(), gameDbIdLong);
                    BasicDBObject updateDBObject = new BasicDBObject();
                    GameDBCover cover = gameDB.getGameDBCover();
                    DateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    long posterGamePublicTimeLong = sdf2.parse(gamePublicTime).getTime();
                    if (lineCode.endsWith("0")) {       //ios
                        cover.setPosterShowTypeIos(showType);
                        cover.setPosterShowContentIos(customShowContent);
                        cover.setPosterGamePublicTimeIos(String.valueOf(posterGamePublicTimeLong)); //数据库中存放long值
                    } else {                                      //android
                        cover.setPosterShowTypeAndroid(showType);
                        cover.setPosterShowContentAndroid(customShowContent);
                        cover.setPosterGamePublicTimeAndroid(String.valueOf(posterGamePublicTimeLong)); //数据库中存放long值
                    }
                    updateDBObject.put(GameDBField.GAMEDB_COVER.getColumn(), cover.toJson());
                    GameResourceServiceSngl.get().updateGameDB(queryDBObject, updateDBObject);

                }

            }

            //针对热门和正在玩的设置  ,2015-04-17 新游开测也加入排序值，用于给热门页的热门块排序
            if (!StringUtil.isEmpty(displayOrder)) {
                try {
                    updateExpress.set(ClientLineItemField.DISPLAY_ORDER, Integer.valueOf(displayOrder));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            JoymeAppServiceSngl.get().modifyClientLineItem(updateExpress, itemId);

            ToolsLog log = new ToolsLog();
            log.setOpUserId(getCurrentUser().getUserid());//用户的ID
            log.setOperType(LogOperType.GAMECLIENT_GAME_MODIFY);  //操作的类型
            log.setOpTime(new Date());//操作时间
            log.setOpIp(getIp());//用户IP
            Map<String, String[]> params = request.getParameterMap();
            String queryString = "  ";
            for (String key : params.keySet()) {
                String[] values = params.get(key);
                for (int i = 0; i < values.length; i++) {
                    queryString += key + "=" + values[i] + "&";
                }
            }
            // 去掉最后一个空格
            queryString = queryString.substring(0, queryString.length() - 1);

            if (queryString.length() > 1900) {
                queryString = queryString.substring(0, 1900);
            }

            log.setOpAfter("新游开测，大家正在玩，热门修改-->queryString:" + queryString); //描述 推荐用中文
            addLog(log);


        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("forward:/gameclient/clientline/game/itemlist", mapMessage);
    }

    @RequestMapping(value = "/itemdelete")
    public ModelAndView itemdelete(@RequestParam(value = "itemId", required = false) Long itemId,
                                   @RequestParam(value = "lineId", required = false) Long lineId,
                                   @RequestParam(value = "lineName", required = false) String lineName,
                                   @RequestParam(value = "lineCode", required = false) String lineCode) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("lineId", lineId);
        mapMessage.put("lineName", lineName);
        mapMessage.put("lineCode", lineCode);
        if (itemId == null) {
            return new ModelAndView("forward:/gameclient/clientline/itemlist", mapMessage);
        }
        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(ClientLineItemField.VALID_STATUS, ValidStatus.REMOVED.getCode());
        try {
            boolean bool = JoymeAppServiceSngl.get().modifyClientLineItem(updateExpress, itemId);
            if (bool) {
                ToolsLog log = new ToolsLog();

                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.GAMECLIENT_GAME_DELETE);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("新游开测，大家正在玩，热门删除-->itemId:" + itemId + "lineId" + lineId); //描述 推荐用中文

                addLog(log);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + "ServiceException", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("forward:/gameclient/clientline/game/itemlist", mapMessage);
    }

}
