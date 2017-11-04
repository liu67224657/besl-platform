package com.enjoyf.webapps.tools.webpage.controller.point.pointwall;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.point.PointServiceSngl;
import com.enjoyf.platform.service.point.pointwall.*;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.service.tools.ToolsLog;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by tonydiao on 2014/12/15.
 */

@Controller
@RequestMapping(value = "/json/point/pointwall/wall")
public class JsonPointWallController extends ToolsBaseController {

    //用于显示一个积分墙下的所有app,在积分墙的编辑页面
    @RequestMapping(value = "/list")
    @ResponseBody
    public String jsonlist(@RequestParam(value = "page", required = false, defaultValue = "1") int page,      //数据库记录索引
                           @RequestParam(value = "rows", required = false, defaultValue = "20") int rows,
                           @RequestParam(value = "appkey", required = true) String appkey,
                           @RequestParam(value = "platform", required = true, defaultValue = "2") String platform) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        JsonBinder binder = JsonBinder.buildNormalBinder();
        try {

            int pageStartIndex = (page - 1) * rows;
            int pageSize = rows;
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(PointwallWallAppField.APPKEY, appkey));
            if (!platform.equals("2")) {
                queryExpress.add(QueryCriterions.eq(PointwallWallAppField.PLATFORM, Integer.valueOf(platform)));
            }

            // 按照valid removed invalid 排序
            queryExpress.add(new QuerySort(PointwallWallAppField.STATUS, QuerySortOrder.DESC));
            queryExpress.add(new QuerySort(PointwallWallAppField.DISPLAY_ORDER, QuerySortOrder.ASC));

            int curPage = (pageStartIndex / pageSize) + 1;
            Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);

            PageRows<PointwallWallApp> pageRows = PointServiceSngl.get().queryPointwallWallAppByPage(queryExpress, pagination);
            int total = PointServiceSngl.get().countPointwallWallAppOfOneWall(queryExpress);
            if (pageRows != null) {
                mapMessage.put("rows", pageRows.getRows());
                mapMessage.put("total", total);
            }

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return binder.toJson(mapMessage);
    }

    //交换在一个积分墙中的两行的排序值
    @RequestMapping(value = "/swap")
    @ResponseBody
    public String swap(@RequestParam(value = "wallAppIdFirst", required = true) Long wallAppIdFirst,
                       @RequestParam(value = "displayOrderFirst", required = true) Integer displayOrderFirst,
                       @RequestParam(value = "wallAppIdSecond", required = true) Long wallAppIdSecond,
                       @RequestParam(value = "displayOrderSecond", required = true) Integer displayOrderSecond) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();
        JsonBinder binder = JsonBinder.buildNormalBinder();
        try {

            UpdateExpress updateExpressA = new UpdateExpress();
            updateExpressA.set(PointwallWallAppField.DISPLAY_ORDER, displayOrderSecond);

            QueryExpress queryExpressA = new QueryExpress();
            queryExpressA.add(QueryCriterions.eq(PointwallWallAppField.WALL_APP_ID, wallAppIdFirst));

            boolean boolA = PointServiceSngl.get().updatePointwallWallApp(updateExpressA, queryExpressA);

            UpdateExpress updateExpressB = new UpdateExpress();
            updateExpressB.set(PointwallWallAppField.DISPLAY_ORDER, displayOrderFirst);
            QueryExpress queryExpressB = new QueryExpress();
            queryExpressB.add(QueryCriterions.eq(PointwallWallAppField.WALL_APP_ID, wallAppIdSecond));

            boolean boolB = PointServiceSngl.get().updatePointwallWallApp(updateExpressB, queryExpressB);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return binder.toJson(mapMessage);
    }


    //批量插入到积分墙
    @RequestMapping(value = "/batchaddtowall")
    @ResponseBody
    public String batchaddtowall(@RequestParam(value = "appkey", required = true) String appkey,
                                 @RequestParam(value = "rows", required = true) String rows,
                                 @RequestParam(value = "length", required = true) Integer length,
                                 HttpServletRequest request) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();
        JsonBinder binder = JsonBinder.buildNormalBinder();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Long result = new Date().getTime() - sdf.parse("2010-01-01 00:00:00").getTime();
            result = result / 1000;
            int autoDisplayOrder = Integer.MAX_VALUE - result.intValue();     //0到1989158807 之间的一个数

            if (length == 1) {
                String[] columns = rows.split("_");
                PointwallWallApp pointwallWallApp = new PointwallWallApp();
                pointwallWallApp.setAppId(Long.valueOf(columns[0]));
                pointwallWallApp.setAppkey(appkey);
                pointwallWallApp.setPlatform(Integer.valueOf(columns[1]));
                pointwallWallApp.setDisplayOrder(autoDisplayOrder);
                pointwallWallApp.setPointAmount(Integer.valueOf(columns[2]));
                pointwallWallApp.setHotStatus(Integer.valueOf(columns[3]));
                pointwallWallApp.setStatus(columns[4]);
                PointServiceSngl.get().insertPointwallWallApp(pointwallWallApp);

            } else if (length > 1) {
                String[] rowsArray = rows.split("@");
                for (int i = 0; i < length; i++) {
                    String[] columns = rowsArray[i].split("_");
                    PointwallWallApp pointwallWallApp = new PointwallWallApp();
                    pointwallWallApp.setAppId(Long.valueOf(columns[0]));
                    pointwallWallApp.setAppkey(appkey);
                    pointwallWallApp.setPlatform(Integer.valueOf(columns[1]));
                    pointwallWallApp.setDisplayOrder(autoDisplayOrder - i * 10);
                    pointwallWallApp.setPointAmount(Integer.valueOf(columns[2]));
                    pointwallWallApp.setHotStatus(Integer.valueOf(columns[3]));

                    pointwallWallApp.setStatus(columns[4]);
                    PointServiceSngl.get().insertPointwallWallApp(pointwallWallApp);
                }
            }

            ToolsLog log = new ToolsLog();
            log.setOpUserId(getCurrentUser().getUserid());//用户的ID
            log.setOperType(LogOperType.POINT_POINTWALL_R_APP_ADD);    //操作的类型
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

            if (queryString.length() > 1950) {
                queryString = queryString.substring(0, 1950);
            }
            log.setOpAfter("批量添加到积分墙 queryString:" + queryString); //描述 推荐用中文
            addLog(log);

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return binder.toJson(mapMessage);
    }


    //用于在积分墙编辑页面-->增加一个app-->弹窗显示要添加的App的easyui 列表   ,和积分墙批量添加app的引入数据
    @RequestMapping(value = "/toaddapp")
    @ResponseBody
    public String jsonapplist(@RequestParam(value = "page", required = false, defaultValue = "1") int page,      //数据库记录索引
                              @RequestParam(value = "rows", required = false, defaultValue = "20") int rows,
                              @RequestParam(value = "appkey", required = true) String appkey,
                              @RequestParam(value = "platform", required = true, defaultValue = "2") String platform) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        JsonBinder binder = JsonBinder.buildNormalBinder();
        try {

            //查询当前此积分墙中已经含有的app列表
            QueryExpress queryExpressForOneWall = new QueryExpress();
            queryExpressForOneWall.add(QueryCriterions.eq(PointwallWallAppField.APPKEY, appkey));
            List<PointwallWallApp> listOfOneWall = PointServiceSngl.get().queryPointwallWallAppAll(queryExpressForOneWall);

            //查询app 表 ,要剔除已经包含 在这个积分墙内的数据
            QueryExpress queryExpress = new QueryExpress();

            if (listOfOneWall != null && listOfOneWall.size() > 0) {
                Object[] appIdOfTheWall = new Long[listOfOneWall.size()];
                for (int i = 0; i < listOfOneWall.size(); i++) {
                    appIdOfTheWall[i] = listOfOneWall.get(i).getAppId();
                }
                queryExpress.add(QueryCriterions.notIn(PointwallAppField.APP_ID, appIdOfTheWall));
            }
            //剔除已经被删除的数据
            queryExpress.add(QueryCriterions.eq(PointwallAppField.REMOVE_STATUS, ValidStatus.VALID.getCode()));

            //按平台筛选
            if (!platform.equals("2")) {
                queryExpress.add(QueryCriterions.eq(PointwallAppField.PLATFORM, Integer.valueOf(platform)));
            }

            queryExpress.add(new QuerySort(PointwallAppField.APP_ID, QuerySortOrder.DESC));

            int pageStartIndex = (page - 1) * rows;
            int pageSize = rows;

            int curPage = (pageStartIndex / pageSize) + 1;
            Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
            PageRows<PointwallApp> pageRows = PointServiceSngl.get().queryPointwallAppByPage(queryExpress, pagination);
            int total = PointServiceSngl.get().countPointwallWallAppTotalOfApps(queryExpress);
            if (pageRows != null) {
                mapMessage.put("rows", pageRows.getRows());
                mapMessage.put("total", total);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return binder.toJson(mapMessage);
    }


    //用于在热门应用列表页---批量添加到积分墙----获得积分墙列表
    @RequestMapping(value = "/getwalls")
    @ResponseBody
    public String getwalls(@RequestParam(value = "page", required = false, defaultValue = "1") int page,      //数据库记录索引
                           @RequestParam(value = "rows", required = false, defaultValue = "20") int rows,
                           @RequestParam(value = "appId", required = false, defaultValue = "0") Long appId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        JsonBinder binder = JsonBinder.buildNormalBinder();
        try {
            //查询point.pw_wall的所有条目
            QueryExpress queryExpress = new QueryExpress();

            int pageStartIndex = (page - 1) * rows;
            int pageSize = rows;
            int curPage = (pageStartIndex / pageSize) + 1;
            Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
            PageRows<PointwallWall> pageRows = PointServiceSngl.get().queryPointwallWallByPage(queryExpress, pagination);
            int total = PointServiceSngl.get().countPointwallWall(queryExpress);
            if (pageRows != null) {
                List<PointwallWall> list = pageRows.getRows();
                QueryExpress queryExpressToFindFlag;
                for (int i = 0; i < list.size(); i++) {
                    QueryExpress queryExpressTemp = new QueryExpress();
                    queryExpressTemp.add(QueryCriterions.eq(PointwallWallAppField.APPKEY, list.get(i).getAppkey()));

                    int appNum = PointServiceSngl.get().countPointwallWallAppOfOneWall(queryExpressTemp);
                    list.get(i).setAppNum(appNum);

                    AuthApp authApp = OAuthServiceSngl.get().getApp(list.get(i).getAppkey());
                    if (authApp != null) {
                        list.get(i).setAppKeyName(authApp.getAppName());
                    }
                    queryExpressToFindFlag = new QueryExpress();
                    queryExpressToFindFlag.add(QueryCriterions.eq(PointwallWallAppField.APP_ID, appId));
                    queryExpressToFindFlag.add(QueryCriterions.eq(PointwallWallAppField.APPKEY, list.get(i).getAppkey()));

                    List<PointwallWallApp> lisTemp = PointServiceSngl.get().queryPointwallWallAppAll(queryExpressToFindFlag);
                    if (lisTemp != null && lisTemp.size() > 0) {
                        list.get(i).setFlag(1);
                    }
                }
                mapMessage.put("rows", list);
                mapMessage.put("total", total);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Excpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return binder.toJson(mapMessage);
    }


    //用于在热门应用列表页---批量添加，删除,或更新app到积分墙列表
    @RequestMapping(value = "/updateappstowalls")
    @ResponseBody
    public String updateappstowalls(@RequestParam(value = "selectedAppkeys", required = true) String selectedAppkeys,
                                    @RequestParam(value = "originalAppkeys", required = true) String originalAppkeys,
                                    @RequestParam(value = "sortPolicy", required = true) String sortPolicy,
                                    @RequestParam(value = "displayOrder", required = true) Integer displayOrder,
                                    @RequestParam(value = "hotStatus", required = true) Integer hotStatus,
                                    @RequestParam(value = "pointAmount", required = true) Integer pointAmount,
                                    @RequestParam(value = "status", required = true) String status,
                                    @RequestParam(value = "appId", required = true) Long appId,
                                    @RequestParam(value = "platform", required = true) Integer platform,
                                    @RequestParam(value = "updatePolicy", required = true) String updatePolicy,
                                    HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        JsonBinder binder = JsonBinder.buildNormalBinder();
        try {

            if (!StringUtil.isEmpty(selectedAppkeys)) {
                String[] selectedArrays = selectedAppkeys.split(",");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Long result = new Date().getTime() - sdf.parse("2010-01-01 00:00:00").getTime();
                result = result / 1000;
                int autoDisplayOrder = Integer.MAX_VALUE - result.intValue();     //0到1989158807 之间的一个数

                //增加或者更新，遍历选择的selectedArrays集合，如果原来表中没有，就是增加，如果原来表中有，就是更新
                for (int i = 0; i < selectedArrays.length; i++) {
                    QueryExpress queryExpress = new QueryExpress();
                    queryExpress.add(QueryCriterions.eq(PointwallWallAppField.APP_ID, appId));
                    queryExpress.add(QueryCriterions.eq(PointwallWallAppField.APPKEY, selectedArrays[i]));

                    List<PointwallWallApp> list = PointServiceSngl.get().queryPointwallWallAppAll(queryExpress);

                    if (list.size() > 0 && updatePolicy != null && updatePolicy.equals("change")) {   //更新
                        UpdateExpress updateExpress = new UpdateExpress();
                        updateExpress.set(PointwallWallAppField.PLATFORM, platform);

                        if (sortPolicy.equals("auto")) {    //自动排序
                            updateExpress.set(PointwallWallAppField.DISPLAY_ORDER, autoDisplayOrder);
                        } else {                         //手动排序
                            updateExpress.set(PointwallWallAppField.DISPLAY_ORDER, displayOrder);
                        }

                        updateExpress.set(PointwallWallAppField.HOT_STATUS, hotStatus);
                        updateExpress.set(PointwallWallAppField.POINT_AMOUNT, pointAmount);
                        updateExpress.set(PointwallWallAppField.STATUS, status);

                        PointServiceSngl.get().updatePointwallWallApp(updateExpress, queryExpress);


                    } else if (list.size() <= 0) {     //新增

                        PointwallWallApp pointwallWallApp = new PointwallWallApp();
                        pointwallWallApp.setAppId(appId);
                        pointwallWallApp.setAppkey(selectedArrays[i]);
                        pointwallWallApp.setPlatform(platform);

                        if (sortPolicy.equals("auto")) {    //自动排序
                            pointwallWallApp.setDisplayOrder(autoDisplayOrder);
                        } else {                         //手动排序
                            pointwallWallApp.setDisplayOrder(displayOrder);
                        }

                        pointwallWallApp.setHotStatus(hotStatus);
                        pointwallWallApp.setPointAmount(pointAmount);
                        pointwallWallApp.setStatus(status);
                        PointServiceSngl.get().insertPointwallWallApp(pointwallWallApp);
                    }
                }
            }

            //删除功能
            String[] originalArrays = originalAppkeys.split(",");

            List<String> originalList = new ArrayList<String>();
            for (int i = 0; i < originalArrays.length; i++) {
                originalList.add(originalArrays[i]);
            }

            if (!StringUtil.isEmpty(selectedAppkeys)) {
                String[] selectedArrays = selectedAppkeys.split(",");
                for (int i = 0; i < selectedArrays.length; i++) {
                    originalList.remove(selectedArrays[i]);
                }
            }

            //对于在  originalAppkeys而不在 selectedAppkeys中的的appkeys , 在point.pw_r_wall_app对它们进行删除

            //size()大于0时才删除
            if (originalList.size() > 0) {
                String[] appkeysToDelete = new String[originalList.size()];
                for (int i = 0; i < originalList.size(); i++) {
                    appkeysToDelete[i] = originalList.get(i);
                }

                QueryExpress queryExpressDelete = new QueryExpress();
                queryExpressDelete.add(QueryCriterions.eq(PointwallWallAppField.APP_ID, appId));

                queryExpressDelete.add(QueryCriterions.in(PointwallWallAppField.APPKEY, appkeysToDelete));

                int result = PointServiceSngl.get().deletePointwallWallApp(queryExpressDelete);

            }
            mapMessage.put("operationStatus", 1);

            ToolsLog log = new ToolsLog();
            log.setOpUserId(getCurrentUser().getUserid());//用户的ID
            log.setOperType(LogOperType.POINT_POINTWALL_R_APP_MODIFY);    //操作的类型
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
            log.setOpAfter("单app批量添加到积分墙操作queryString-->"+queryString); //描述 推荐用中文
            addLog(log);

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Excpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            mapMessage.put("operationStatus", 0);
        }
        return binder.toJson(mapMessage);
    }
}
