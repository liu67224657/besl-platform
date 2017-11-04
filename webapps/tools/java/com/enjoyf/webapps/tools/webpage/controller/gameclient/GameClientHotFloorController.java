package com.enjoyf.webapps.tools.webpage.controller.gameclient;

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
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import net.sf.json.JSONObject;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tonydiao on 2015/03/25.
 */

//热门页自定义楼层的controller
@Controller
@RequestMapping(value = "/gameclient/clientline/hotfloor")
public class GameClientHotFloorController extends ToolsBaseController {


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
            queryExpress.add(QueryCriterions.eq(ClientLineField.ITEM_TYPE, ClientItemType.HOT_FLOOR.getCode()));
            queryExpress.add(QueryCriterions.eq(ClientLineField.LINE_TYPE, ClientLineType.GAMECLIENT.getCode()));

            PageRows<ClientLine> pageRows = JoymeAppServiceSngl.get().queryClientLineByPage(queryExpress, pagination);
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                mapMessage.put("list", pageRows.getRows());
                mapMessage.put("page", pageRows.getPage());
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/gameclient/hotfloor/linelist", mapMessage);
        }
        return new ModelAndView("/gameclient/hotfloor/linelist", mapMessage);
    }

    @RequestMapping(value = "/itemlist")
    public ModelAndView itemlist(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                                 @RequestParam(value = "maxPageItems", required = false, defaultValue = "50") int pageSize,
                                 @RequestParam(value = "lineId", required = false) Long lineId,
                                 @RequestParam(value = "lineName", required = false) String lineName,
                                 @RequestParam(value = "lineCode", required = false) String lineCode) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("lineId", lineId);
        mapMessage.put("lineName", lineName);
        mapMessage.put("lineCode", lineCode);
        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);

        if (lineId == null) {
            return new ModelAndView("/gameclient/hotfloor/itemlist");
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
            //  queryExpress.add(QueryCriterions.eq(ClientLineItemField.VALID_STATUS, ValidStatus.VALID.getCode()));
            // queryExpress.add(QuerySort.add(ClientLineItemField.ITEM_CREATE_DATE, QuerySortOrder.DESC));

            queryExpress.add(QuerySort.add(ClientLineItemField.DISPLAY_ORDER, QuerySortOrder.ASC));
            //把属于这个clientline 的所有item都分页查出来
            PageRows<ClientLineItem> itemPageRows = JoymeAppServiceSngl.get().queryClientLineItemByPage(queryExpress, pagination);
            if (itemPageRows != null && !CollectionUtil.isEmpty(itemPageRows.getRows())) {
                mapMessage.put("list", itemPageRows.getRows());
                mapMessage.put("page", itemPageRows.getPage());
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/gameclient/hotfloor/itemlist", mapMessage);
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

        return new ModelAndView("/gameclient/hotfloor/createitem", mapMessage);
    }


    @RequestMapping(value = "/itemcreate")
    public ModelAndView itemcreate(@RequestParam(value = "lineId", required = false) Long lineId,
                                   @RequestParam(value = "lineCode", required = false) String lineCode,
                                   @RequestParam(value = "lineName", required = false) String lineName,
                                   @RequestParam(value = "floorName", required = false) String floorName,
                                   @RequestParam(value = "floorIcon", required = false) String floorIcon,
                                   @RequestParam(value = "pic1st", required = false) String pic1st,
                                   @RequestParam(value = "jt1", required = false) String jt1,
                                   @RequestParam(value = "ji1", required = false) String ji1,
                                   @RequestParam(value = "pic2nd", required = false) String pic2nd,
                                   @RequestParam(value = "jt2", required = false) String jt2,
                                   @RequestParam(value = "ji2", required = false) String ji2,
                                   @RequestParam(value = "pic3rd", required = false) String pic3rd,
                                   @RequestParam(value = "jt3", required = false) String jt3,
                                   @RequestParam(value = "ji3", required = false) String ji3,
                                   @RequestParam(value = "moreLink", required = false) String moreLink,
                                   @RequestParam(value = "jt", required = false) String jt,
                                   @RequestParam(value = "ji", required = false) String ji,
                                   HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("lineId", lineId);
        mapMessage.put("lineCode", lineCode);
        mapMessage.put("lineName", lineName);


        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Long result = new Date().getTime() - sdf.parse("2010-01-01 00:00:00").getTime();
            result = result / 1000;
            int displayOrder = Integer.MAX_VALUE - result.intValue();     //0到1989158807 之间的一个数


            ClientLineItem clientLineItemTemp = new ClientLineItem();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("floorName", floorName);
            jsonObject.put("floorIcon", floorIcon);
            jsonObject.put("pic1st", pic1st);
            jsonObject.put("jt1", jt1);
            jsonObject.put("ji1", ji1);
            jsonObject.put("pic2nd", pic2nd);
            jsonObject.put("jt2", jt2);
            jsonObject.put("ji2", ji2);
            jsonObject.put("pic3rd", pic3rd);
            jsonObject.put("jt3", jt3);
            jsonObject.put("ji3", ji3);
            jsonObject.put("moreLink", moreLink);
            jsonObject.put("jt", jt);
            jsonObject.put("ji", ji);

            clientLineItemTemp.setValidStatus(ValidStatus.REMOVED);
            clientLineItemTemp.setDesc(jsonObject.toString());

            clientLineItemTemp.setDisplayOrder(displayOrder);
            clientLineItemTemp.setItemCreateDate(new Date());
            clientLineItemTemp.setLineId(lineId);
            clientLineItemTemp.setItemType(ClientItemType.HOT_FLOOR);
            clientLineItemTemp.setItemDomain(ClientItemDomain.GAME);

            JoymeAppServiceSngl.get().createClientLineItem(clientLineItemTemp);


            ToolsLog log = new ToolsLog();
            log.setOpUserId(getCurrentUser().getUserid());//用户的ID
            log.setOperType(LogOperType.GAMECLIENT_HOT_FLOOR_ADD);  //操作的类型
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

            log.setOpAfter("热门页自定义楼层添加楼层-->queryString:" + queryString); //描述 推荐用中文
            addLog(log);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/gameclient/clientline/hotfloor/itemlist", mapMessage);
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
        mapMessage.put("itemId", itemId);
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
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        } catch (IllegalAccessException e) {
            GAlerter.lab(this.getClass().getName() + " occured IllegalAccessException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/gameclient/hotfloor/modifyitem", mapMessage);
    }

    @RequestMapping(value = "/itemmodify")
    public ModelAndView itemmodify(@RequestParam(value = "itemId", required = false) Long itemId,
                                   @RequestParam(value = "lineId", required = false) Long lineId,
                                   @RequestParam(value = "lineCode", required = false) String lineCode,
                                   @RequestParam(value = "lineName", required = false) String lineName,
                                   @RequestParam(value = "floorName", required = false) String floorName,
                                   @RequestParam(value = "floorIcon", required = false) String floorIcon,
                                   @RequestParam(value = "pic1st", required = false) String pic1st,
                                   @RequestParam(value = "jt1", required = false) String jt1,
                                   @RequestParam(value = "ji1", required = false) String ji1,
                                   @RequestParam(value = "pic2nd", required = false) String pic2nd,
                                   @RequestParam(value = "jt2", required = false) String jt2,
                                   @RequestParam(value = "ji2", required = false) String ji2,
                                   @RequestParam(value = "pic3rd", required = false) String pic3rd,
                                   @RequestParam(value = "jt3", required = false) String jt3,
                                   @RequestParam(value = "ji3", required = false) String ji3,
                                   @RequestParam(value = "moreLink", required = false) String moreLink,
                                   @RequestParam(value = "jt", required = false) String jt,
                                   @RequestParam(value = "ji", required = false) String ji,
                                   HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("lineId", lineId);
        mapMessage.put("lineName", lineName);
        mapMessage.put("lineCode", lineCode);
        try {
            UpdateExpress updateExpress = new UpdateExpress();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("floorName", floorName);
            jsonObject.put("floorIcon", floorIcon);
            jsonObject.put("pic1st", pic1st);
            jsonObject.put("jt1", jt1);
            jsonObject.put("ji1", ji1);
            jsonObject.put("pic2nd", pic2nd);
            jsonObject.put("jt2", jt2);
            jsonObject.put("ji2", ji2);
            jsonObject.put("pic3rd", pic3rd);
            jsonObject.put("jt3", jt3);
            jsonObject.put("ji3", ji3);
            jsonObject.put("moreLink", moreLink);
            jsonObject.put("jt", jt);
            jsonObject.put("ji", ji);
            updateExpress.set(ClientLineItemField.DESC, jsonObject.toString());

            JoymeAppServiceSngl.get().modifyClientLineItem(updateExpress, itemId);

            ToolsLog log = new ToolsLog();
            log.setOpUserId(getCurrentUser().getUserid());//用户的ID
            log.setOperType(LogOperType.GAMECLIENT_HOT_FLOOR_MODIFY);  //操作的类型
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

            log.setOpAfter("热门页自定义楼层之楼层修改-->queryString:" + queryString); //描述 推荐用中文
            addLog(log);

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/gameclient/clientline/hotfloor/itemlist", mapMessage);
    }


    //在本分页内交换任意两行的排序
    @RequestMapping(value = "/sort")
    @ResponseBody
    public String sort(@RequestParam(value = "type", required = false) String type,
                       @RequestParam(value = "fromId", required = false) Long fromId,
                       @RequestParam(value = "fromOrder", required = false) int fromOrder,
                       @RequestParam(value = "toId", required = false) Long toId,
                       @RequestParam(value = "toOrder", required = false) int toOrder) {
        JsonBinder binder = JsonBinder.buildNormalBinder();
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        ResultObjectMsg resultObjectMsg = new ResultObjectMsg(ResultObjectMsg.CODE_E);

        try {
            UpdateExpress updateExpressFrom = new UpdateExpress();
            updateExpressFrom.set(ClientLineItemField.DISPLAY_ORDER, toOrder);
            JoymeAppServiceSngl.get().modifyClientLineItem(updateExpressFrom, fromId);

            UpdateExpress updateExpressTo = new UpdateExpress();
            updateExpressTo.set(ClientLineItemField.DISPLAY_ORDER, fromOrder);
            JoymeAppServiceSngl.get().modifyClientLineItem(updateExpressTo, toId);
            resultObjectMsg = new ResultObjectMsg(ResultObjectMsg.CODE_S);
            resultObjectMsg.setResult(mapMessage);
        } catch (Exception e) {
            resultObjectMsg.setRs(ResultObjectMsg.CODE_E);
            resultObjectMsg.setMsg(i18nSource.getMessage("system.error", null, null));
            return binder.toJson(resultObjectMsg);
        }
        return binder.toJson(resultObjectMsg);
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
            return new ModelAndView("redirect:/gameclient/clientline/hotfloor/itemlist", mapMessage);
        }
        try {
            UpdateExpress updateExpress = new UpdateExpress();
            if (!StringUtil.isEmpty(type) && type.equals("1")) {
                updateExpress.set(ClientLineField.VALID_STATUS, ValidStatus.REMOVED.getCode());
            } else {
                updateExpress.set(ClientLineField.VALID_STATUS, ValidStatus.VALID.getCode());
            }

            boolean bool = JoymeAppServiceSngl.get().modifyClientLineItem(updateExpress, itemId);
            if (bool) {
                ToolsLog log = new ToolsLog();
                log.setOpUserId(getCurrentUser().getUserid());//用户的ID
                log.setOperType(LogOperType.GAMECLIENT_HOT_FLOOR_DELETE);  //操作的类型
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

                log.setOpAfter("热门页楼层自定义之楼层删除或者激活-->queryString:" + queryString); //描述 推荐用中文
                addLog(log);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + "ServiceException", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/gameclient/clientline/hotfloor/itemlist", mapMessage);
    }


}
