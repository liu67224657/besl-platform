package com.enjoyf.webapps.tools.webpage.controller.gameclient;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.gameres.gamedb.GameDB;
import com.enjoyf.platform.service.gameres.gamedb.GameDBCoverFieldJson;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.service.tools.ToolsLog;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.webapps.tools.weblogic.dto.joymeApp.GameDBHotPageDTO;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import com.mongodb.BasicDBObject;
import net.sf.json.JSONObject;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by tonydiao on 2015/03/23.
 */

//今日推荐的controller
@Controller
@RequestMapping(value = "/gameclient/clientline/todayrecommend")
public class GameClientTodayRecommendController extends ToolsBaseController {


    @Resource(name = "i18nSource")
    private ResourceBundleMessageSource i18nSource;


    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "20") int pageSize) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();


        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
        try {
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


            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(ClientLineField.ITEM_TYPE, ClientItemType.TODAY_RECOMMEND.getCode()));
            queryExpress.add(QueryCriterions.eq(ClientLineField.LINE_TYPE, ClientLineType.GAMECLIENT.getCode()));

            PageRows<ClientLine> pageRows = JoymeAppServiceSngl.get().queryClientLineByPage(queryExpress, pagination);
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                mapMessage.put("list", pageRows.getRows());
                mapMessage.put("page", pageRows.getPage());
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/gameclient/todayrecommend/linelist", mapMessage);
        }
        return new ModelAndView("/gameclient/todayrecommend/linelist", mapMessage);
    }

    @RequestMapping(value = "/itemlist")
    public ModelAndView itemlist(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                                 @RequestParam(value = "maxPageItems", required = false, defaultValue = "50") int pageSize,
                                 @RequestParam(value = "lineId", required = false) Long lineId,
                                 @RequestParam(value = "lineName", required = false) String lineName,
                                 @RequestParam(value = "lineCode", required = false) String lineCode,
                                 @RequestParam(value = "dateFilter", required = false, defaultValue = "") String dateFilter,
                                 @RequestParam(value = "allFilter", required = false, defaultValue = "") String allFilter) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("lineId", lineId);
        mapMessage.put("lineName", lineName);
        mapMessage.put("lineCode", lineCode);
        mapMessage.put("dateFilter", dateFilter);
        mapMessage.put("allFilter", allFilter);
        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);

        if (lineId == null) {
            return new ModelAndView("/gameclient/todayrecommend/itemlist");
        }
        try {

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

            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(ClientLineItemField.LINE_ID, lineId));

            if (!StringUtil.isEmpty(dateFilter)) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date theDay = sdf.parse(dateFilter);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(theDay);

                calendar.add(Calendar.DAY_OF_MONTH, 1);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                Date tomorrow = calendar.getTime();

                //两天之间
                queryExpress.add(QueryCriterions.lt(ClientLineItemField.ITEM_CREATE_DATE, tomorrow));
                queryExpress.add(QueryCriterions.geq(ClientLineItemField.ITEM_CREATE_DATE, DateUtil.ignoreTime(theDay)));

            }

            //  queryExpress.add(QueryCriterions.eq(ClientLineItemField.VALID_STATUS, ValidStatus.VALID.getCode()));
            queryExpress.add(QuerySort.add(ClientLineItemField.ITEM_CREATE_DATE, QuerySortOrder.DESC));

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
                List<GameDBHotPageDTO> smallList = new ArrayList<GameDBHotPageDTO>();

                for (int i = 0; i < clientLineItemList.size(); i++) {
                    Long longTemp = Long.valueOf(clientLineItemList.get(i).getDirectId());
                    GameDB gameDB = gameDBMap.get(longTemp);
                    if (gameDB != null) {
                        GameDBHotPageDTO temp = getDTOFromJson(clientLineItemList.get(i).getDesc());
                        temp.setGameDbId(gameDB.getGameDbId());
                        temp.setLikeNum(gameDB.getGameDBCover().getCoverAgreeNum());
                        temp.setIcon(gameDB.getGameIcon());
                        temp.setGameRate(average(gameDB));
                        temp.setItemCreateDate(clientLineItemList.get(i).getItemCreateDate());
                        temp.setItemId(clientLineItemList.get(i).getItemId());
                        temp.setDisplayOrder(clientLineItemList.get(i).getDisplayOrder());
                        temp.setValidStatus(clientLineItemList.get(i).getValidStatus());
                        smallList.add(temp);
                    }
                }

                //按 gamePublicTime字段由大到小排序---逆自然顺序
                //如果是新游开测按日期降序排列，热门和正在玩按display_order排序
//                if (!StringUtil.isEmpty(lineCode) && lineCode.contains("gc_newgame")) {
//                    Collections.sort(smallList);
//                }

                mapMessage.put("list", smallList);
                mapMessage.put("page", itemPageRows.getPage());
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/gameclient/todayrecommend/itemlist", mapMessage);
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
        } catch (IllegalAccessException e) {
            GAlerter.lab(this.getClass().getName() + " occured IllegalAccessException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("/gameclient/todayrecommend/createitem", mapMessage);
    }


    @RequestMapping(value = "/itemcreate")
    public ModelAndView itemcreate(@RequestParam(value = "lineId", required = false) Long lineId,
                                   @RequestParam(value = "gameDbId", required = false) String gameDbId,
                                   @RequestParam(value = "itemCreateDate", required = false) String itemCreateDate,
                                   @RequestParam(value = "lineCode", required = false) String lineCode,
                                   @RequestParam(value = "lineName", required = false) String lineName,
                                   @RequestParam(value = "gameName", required = false) String gameName,
                                   @RequestParam(value = "gameTypeDesc", required = false) String gameTypeDesc,
                                   @RequestParam(value = "jumpTarget", required = false) String jumpTarget,
                                   @RequestParam(value = "jt", required = false) String jt,
                                   @RequestParam(value = "ji", required = false) String ji,
                                   @RequestParam(value = "downloadRecommend", required = false) String downloadRecommend,
                                   //  @RequestParam(value = "tagText", required = false) String tagText,
                                   @RequestParam(value = "tag", required = false) String tag,
                                   HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("lineId", lineId);
        mapMessage.put("lineCode", lineCode);
        mapMessage.put("lineName", lineName);


        try {
            //去掉判断是否是ValidStatus 是removed 的条目再恢复的选项，因为今日推荐可以一个游戏多日推荐。 所以只有新增，没有恢复
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            ClientLineItem clientLineItemTemp = new ClientLineItem();
            GameDB gameDB = GameResourceServiceSngl.get().getGameDB(new BasicDBObject().append("_id", Long.valueOf(gameDbId)), false);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("gameName", gameName);
            jsonObject.put("gameTypeDesc", gameTypeDesc);
            jsonObject.put("jumpTarget", jumpTarget);

            if (jumpTarget.equals("cover")) {
                jsonObject.put("jt", "23");
                jsonObject.put("ji", "http://api." + WebappConfig.get().getDomain() + "/joymeapp/gameclient/webview/game/cover?gameid=" + gameDB.getGameDbId());
                jsonObject.put("showType", "rate");
            } else if (jumpTarget.equals("poster")) {
                jsonObject.put("jt", "23");
                jsonObject.put("ji", "http://api." + WebappConfig.get().getDomain() + "/joymeapp/gameclient/webview/game/poster?gameid=" + gameDB.getGameDbId());
                jsonObject.put("showType", "likes");
            } else if (jumpTarget.equals("detail")) {
                jsonObject.put("jt", "24");
                jsonObject.put("ji", gameDbId);
                jsonObject.put("showType", "rate");
            } else {
                jsonObject.put("jt", jt);
                jsonObject.put("ji", ji);
                jsonObject.put("showType", "rate");
            }

            jsonObject.put("downloadRecommend", downloadRecommend);
            jsonObject.put("tag", tag);
            //   jsonObject.put("tagColor", tagColor);

            clientLineItemTemp.setValidStatus(ValidStatus.REMOVED);
            clientLineItemTemp.setDesc(jsonObject.toString());
            clientLineItemTemp.setDirectId(gameDbId);
            clientLineItemTemp.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
            clientLineItemTemp.setItemCreateDate(format.parse(itemCreateDate));
            clientLineItemTemp.setLineId(lineId);
            clientLineItemTemp.setItemType(ClientItemType.TODAY_RECOMMEND);
            clientLineItemTemp.setItemDomain(ClientItemDomain.GAME);

            JoymeAppServiceSngl.get().createClientLineItem(clientLineItemTemp);


            ToolsLog log = new ToolsLog();
            log.setOpUserId(getCurrentUser().getUserid());//用户的ID
            log.setOperType(LogOperType.GAMECLIENT_TODAY_RECOMMEND_ADD);  //操作的类型
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

            log.setOpAfter("今日推荐添加条目-->queryString:" + queryString); //描述 推荐用中文
            addLog(log);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/gameclient/clientline/todayrecommend/itemlist", mapMessage);
    }


    @RequestMapping(value = "/itemmodifypage")
    public ModelAndView itemmodifypage(@RequestParam(value = "lineId", required = false) Long lineId,
                                       @RequestParam(value = "itemId", required = true) Long itemId,
                                       @RequestParam(value = "lineName", required = false) String lineName,
                                       @RequestParam(value = "lineCode", required = false) String lineCode) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("lineName", lineName);
        mapMessage.put("lineCode", lineCode);
        mapMessage.put("lineId", lineId);
        try {

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
            ClientLineItem clientLineItem = JoymeAppServiceSngl.get().getClientLineItem(new QueryExpress().add(QueryCriterions.eq(ClientLineItemField.ITEM_ID, itemId)));
            mapMessage.put("item", clientLineItem);

            GameDB gameDB = GameResourceServiceSngl.get().getGameDB(new BasicDBObject().append("_id", Long.valueOf(clientLineItem.getDirectId())), false);
            mapMessage.put("game", gameDB);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        } catch (IllegalAccessException e) {
            GAlerter.lab(this.getClass().getName() + " occured IllegalAccessException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/gameclient/todayrecommend/modifyitem", mapMessage);
    }

    @RequestMapping(value = "/itemmodify")
    public ModelAndView itemmodify(@RequestParam(value = "itemId", required = false) Long itemId,
                                   @RequestParam(value = "lineId", required = false) Long lineId,
                                   @RequestParam(value = "gameDbId", required = false) String gameDbId,
                                   @RequestParam(value = "itemCreateDate", required = false) String itemCreateDate,
                                   @RequestParam(value = "lineCode", required = false) String lineCode,
                                   @RequestParam(value = "lineName", required = false) String lineName,
                                   @RequestParam(value = "gameName", required = false) String gameName,
                                   @RequestParam(value = "gameTypeDesc", required = false) String gameTypeDesc,
                                   @RequestParam(value = "jumpTarget", required = false) String jumpTarget,
                                   @RequestParam(value = "jt", required = false) String jt,
                                   @RequestParam(value = "ji", required = false) String ji,
                                   @RequestParam(value = "downloadRecommend", required = false) String downloadRecommend,
                                   @RequestParam(value = "tag", required = false) String tag,
                                   //  @RequestParam(value = "tagColor", required = false) String tagColor,
                                   HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("lineId", lineId);
        mapMessage.put("lineName", lineName);
        mapMessage.put("lineCode", lineCode);
        try {
            UpdateExpress updateExpress = new UpdateExpress();
            GameDB gameDB = GameResourceServiceSngl.get().getGameDB(new BasicDBObject().append("_id", Long.valueOf(gameDbId)), false);

            if (!StringUtil.isEmpty(itemCreateDate)) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date itemCreateDateDate = format.parse(itemCreateDate);
                updateExpress.set(ClientLineItemField.ITEM_CREATE_DATE, itemCreateDateDate);
            }


            JSONObject jsonObject = new JSONObject();
            jsonObject.put("gameName", gameName);
            jsonObject.put("gameTypeDesc", gameTypeDesc);
            jsonObject.put("jumpTarget", jumpTarget);
            if (jumpTarget.equals("cover")) {
                jsonObject.put("jt", "23");
                jsonObject.put("ji", "http://api." + WebappConfig.get().getDomain() + "/joymeapp/gameclient/webview/game/cover?gameid=" + gameDB.getGameDbId());
                jsonObject.put("showType", "rate");
            } else if (jumpTarget.equals("poster")) {
                jsonObject.put("jt", "23");
                jsonObject.put("ji", "http://api." + WebappConfig.get().getDomain() + "/joymeapp/gameclient/webview/game/poster?gameid=" + gameDB.getGameDbId());
                jsonObject.put("showType", "likes");
            } else if (jumpTarget.equals("detail")) {
                jsonObject.put("jt", "24");
                jsonObject.put("ji", gameDbId);
                jsonObject.put("showType", "rate");
            } else {
                jsonObject.put("jt", jt);
                jsonObject.put("ji", ji);
                jsonObject.put("showType", "rate");
            }
            jsonObject.put("downloadRecommend", downloadRecommend);
            jsonObject.put("tag", tag);
            // jsonObject.put("tagColor", tagColor);
            updateExpress.set(ClientLineItemField.DESC, jsonObject.toString());
            JoymeAppServiceSngl.get().modifyClientLineItem(updateExpress, itemId);

            ToolsLog log = new ToolsLog();
            log.setOpUserId(getCurrentUser().getUserid());//用户的ID
            log.setOperType(LogOperType.GAMECLIENT_TODAY_RECOMMEND_MODIFY);  //操作的类型
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

            log.setOpAfter("今日推荐条目修改-->queryString:" + queryString); //描述 推荐用中文
            addLog(log);

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/gameclient/clientline/todayrecommend/itemlist", mapMessage);
    }

    @RequestMapping(value = "/itemdelete")
    public ModelAndView itemdelete(@RequestParam(value = "itemId", required = false) Long itemId,
                                   @RequestParam(value = "lineId", required = false) Long lineId,
                                   @RequestParam(value = "lineName", required = false) String lineName,
                                   @RequestParam(value = "lineCode", required = false) String lineCode,
                                   @RequestParam(value = "type", required = false) String type,
                                   HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("lineId", lineId);
        mapMessage.put("lineName", lineName);
        mapMessage.put("lineCode", lineCode);
        if (itemId == null) {
            return new ModelAndView("forward:/gameclient/clientline/todayrecommend/itemlist", mapMessage);
        }
        UpdateExpress updateExpress = new UpdateExpress();
        try {
            if (!StringUtil.isEmpty(type) && type.equals("1")) {
                updateExpress.set(ClientLineField.VALID_STATUS, ValidStatus.REMOVED.getCode());
            } else {
                updateExpress.set(ClientLineField.VALID_STATUS, ValidStatus.VALID.getCode());
            }
            boolean bool = JoymeAppServiceSngl.get().modifyClientLineItem(updateExpress, itemId);
            if (bool) {


                ToolsLog log = new ToolsLog();
                log.setOpUserId(getCurrentUser().getUserid());//用户的ID
                log.setOperType(LogOperType.GAMECLIENT_TODAY_RECOMMEND_DELETE);  //操作的类型
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

                log.setOpAfter("热门页今日推荐删除或者激活-->queryString:" + queryString); //描述 推荐用中文
                addLog(log);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + "ServiceException", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("forward:/gameclient/clientline/todayrecommend/itemlist", mapMessage);
    }

    //clientlineitem 表中的desc字段存储的是json格式的数据，从json字段串中获得java对象
    private static GameDBHotPageDTO getDTOFromJson(String jsonString) {
        JSONObject jsonObject = JSONObject.fromObject(jsonString);
        Map<String, String> map = new HashMap<String, String>();
        for (Iterator it = jsonObject.keys(); it.hasNext(); ) {
            String key = (String) it.next();
            map.put(key, (String) jsonObject.get(key));
        }
        GameDBHotPageDTO temp = new GameDBHotPageDTO();
        temp.setGameName(map.get("gameName"));
        temp.setGameTypeDesc(map.get("gameTypeDesc"));
        temp.setJumpTarget(map.get("jumpTarget"));
        temp.setJt(map.get("jt"));
        temp.setJi(map.get("ji"));
        temp.setDownloadRecommend(map.get("downloadRecommend"));
        temp.setTag(map.get("tag"));
        temp.setShowType(map.get("showType"));
        return temp;
    }

    //根据游戏资料库的5个评分，求算术平均值
    public static String average(GameDB gameDB) {
        String returnObj = "";
        GameDBCoverFieldJson fieldJson = gameDB.getGameDBCoverFieldJson();
        if (fieldJson != null) {
            double sum = Double.valueOf(fieldJson.getValue1()) + Double.valueOf(fieldJson.getValue2()) + Double.valueOf(fieldJson.getValue3()) + Double.valueOf(fieldJson.getValue4()) + Double.valueOf(fieldJson.getValue5());
            DecimalFormat df = new DecimalFormat("0.0");
            returnObj = df.format(sum / 5.0);
        }
        return returnObj;
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifypage(@RequestParam(value = "lineId", required = false) Long lineId,
                                   @RequestParam(value = "lineCode", required = false) String lineCode,
                                   HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("lineId", lineId);
        mapMessage.put("lineCode", lineCode);
        try {
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
            ClientLine clientLine = JoymeAppServiceSngl.get().getClientLine(new QueryExpress().add(QueryCriterions.eq(ClientLineField.LINE_ID, lineId)));
            mapMessage.put("line", clientLine);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        } catch (IllegalAccessException e) {
            GAlerter.lab(this.getClass().getName() + " occured IllegalAccessException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/gameclient/todayrecommend/modifyline", mapMessage);

    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "lineName", required = false) String lineName,
                               @RequestParam(value = "lineId", required = false) Long lineId,
                               @RequestParam(value = "lineCode", required = false) String lineCode,
                               @RequestParam(value = "icon", required = false) String icon,
                               @RequestParam(value = "more", required = false) String more,
                               @RequestParam(value = "jt", required = false) String jt,
                               @RequestParam(value = "ji", required = false) String ji,
                               HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        UpdateExpress updateExpress = new UpdateExpress();

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("icon", icon);
            jsonObject.put("more", more);
            if (!StringUtil.isEmpty(more) && more.equals("1")) {
                jsonObject.put("jt", jt);
                jsonObject.put("ji", ji);
            } else {
                jsonObject.put("jt", "");
                jsonObject.put("ji", "");
            }

            updateExpress.set(ClientLineField.LINE_DESC, jsonObject.toString());
            updateExpress.set(ClientLineField.LINE_NAME, lineName);
            JoymeAppServiceSngl.get().modifyClientLine(updateExpress, new QueryExpress().add(QueryCriterions.eq(ClientLineField.LINE_ID, lineId)));

            //更新缓存中的clineline 便于more ,jt，ji等字段在热门页接口中及时更新
            JoymeAppServiceSngl.get().removeClientLineFromCache(lineCode);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/gameclient/clientline/todayrecommend/list", mapMessage);
    }


}
