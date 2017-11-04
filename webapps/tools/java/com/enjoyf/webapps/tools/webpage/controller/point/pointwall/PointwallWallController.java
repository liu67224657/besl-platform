package com.enjoyf.webapps.tools.webpage.controller.point.pointwall;

import com.enjoyf.platform.service.content.GoodsActionType;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.AuthAppField;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.point.PointKeyType;
import com.enjoyf.platform.service.point.PointServiceSngl;
import com.enjoyf.platform.service.point.pointwall.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.service.tools.ToolsLog;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tonydiao on 2014/11/27.
 */

@Controller
@RequestMapping(value = "/point/pointwall/wall")
public class PointwallWallController extends ToolsBaseController {


    //显示 积分墙  分页列表
    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,      //数据库记录索引
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "20") int pageSize,
                             @RequestParam(value = "appkey", required = false) String appkey,
                             @RequestParam(value = "pointKey", required = false) String pointKey,
                             @RequestParam(value = "searchShopKey", required = false) String searchShopKey,
                             @RequestParam(value = "appKeyName", required = false) String appKeyName,
                             @RequestParam(value = "errorFK", required = false) String errorFK) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {

            if (!StringUtil.isEmpty(errorFK)) {
                mapMessage.put("errorFK", errorFK);
            }
            QueryExpress queryExpress = new QueryExpress();
            if (!StringUtil.isEmpty(appkey)) {
                mapMessage.put("appkey", appkey);
                queryExpress.add(QueryCriterions.like(PointwallWallField.APPKEY, '%' + appkey + '%'));
            }
            if (!StringUtil.isEmpty(pointKey)) {
                mapMessage.put("pointKey", pointKey);
                queryExpress.add(QueryCriterions.eq(PointwallWallField.POINT_KEY, pointKey));
            }
            if (!StringUtil.isEmpty(searchShopKey)) {
                mapMessage.put("searchShopKey", searchShopKey);
                queryExpress.add(QueryCriterions.eq(PointwallWallField.SHOP_KEY, Integer.valueOf(searchShopKey)));
            }


            int curPage = (pageStartIndex / pageSize) + 1;
            Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
            PageRows<PointwallWall> pageRows = PointServiceSngl.get().queryPointwallWallByPage(queryExpress, pagination);


            if (pageRows != null) {
                List<PointwallWall> list = pageRows.getRows();
                for (int i = 0; i < list.size(); i++) {
                    QueryExpress queryExpressTemp = new QueryExpress();
                    queryExpressTemp.add(QueryCriterions.eq(PointwallWallAppField.APPKEY, list.get(i).getAppkey()));

                    int appNum = PointServiceSngl.get().countPointwallWallAppOfOneWall(queryExpressTemp);
                    list.get(i).setAppNum(appNum);

                    AuthApp authApp = OAuthServiceSngl.get().getApp(list.get(i).getAppkey());
                    if (authApp != null) {
                        list.get(i).setAppKeyName(authApp.getAppName());
                    }

                    list.get(i).setPointKeyName(PointKeyType.getByCode(list.get(i).getPointKey()).getName());
                    list.get(i).setShopKeyName(GoodsActionType.getByCode(list.get(i).getShopKey()).getName());

                    //用于按   appKeyName 搜索的情况下
                    if (!StringUtil.isEmpty(appKeyName)) {
                        //flag==0显示 ，flag==1 隐藏

                        if (!list.get(i).getAppKeyName().contains(appKeyName)) {
                            list.get(i).setFlag(1);
                        }
                    }
                }

                mapMessage.put("list", list);
                mapMessage.put("types", PointKeyType.getAll());
                mapMessage.put("shopTypes", GoodsActionType.getAll());
                mapMessage.put("appKeyName", appKeyName);
                mapMessage.put("page", pageRows.getPage());
            }

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/point/pointwall/walllist", mapMessage);
    }


    @RequestMapping(value = "/createpage")
    public ModelAndView createPage() {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("types", PointKeyType.getAll());
        mapMessage.put("shopTypes", GoodsActionType.getAll());
        try {
            QueryExpress queryExpressWall = new QueryExpress();
            List<PointwallWall> wallList = PointServiceSngl.get().queryPointwallWall(queryExpressWall);

            //返回一个列表中包含所有配置了--我的--模块的app的appkey
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(AuthAppField.DISPLAYMY, 1));
            if (wallList != null && wallList.size() > 0) {
                String[] appkeys = new String[wallList.size()];
                for (int i = 0; i < wallList.size(); i++) {
                    appkeys[i] = wallList.get(i).getAppkey();
                }
                queryExpress.add(QueryCriterions.notIn(AuthAppField.APPID, appkeys));
            }

            queryExpress.add(QuerySort.add(AuthAppField.CREATEDATE, QuerySortOrder.DESC));
            List<AuthApp> authAppList = OAuthServiceSngl.get().queryAuthApp(queryExpress);
            mapMessage.put("list", authAppList);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }


        return new ModelAndView("/point/pointwall/walladd", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "appkey", required = false) String appkey,
                               @RequestParam(value = "pointKey", required = false) String pointKey,
                               @RequestParam(value = "shopKey", required = false) String shopKey,
                               @RequestParam(value = "wallMoneyName", required = false) String wallMoneyName,
                               @RequestParam(value = "template", required = false) String template) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("appkey", appkey);
        mapMessage.put("pointKey", pointKey);
        mapMessage.put("shopKey", shopKey);
        mapMessage.put("wallMoneyName", wallMoneyName);

        try {

            QueryExpress queryTemp = new QueryExpress();
            queryTemp.add(QueryCriterions.eq(PointwallWallField.APPKEY, appkey));
            PointwallWall tempWall = PointServiceSngl.get().getPointwallWall(queryTemp);
            if (tempWall != null) {
                mapMessage.put("types", PointKeyType.getAll());
                mapMessage.put("shopTypes", GoodsActionType.getAll());
                mapMessage.put("nameExist", "PointwallWall.appkey: " + appkey + " has.exist");
                return new ModelAndView("/point/pointwall/walladd", mapMessage);
            }


            PointwallWall pointwallWall = new PointwallWall();
            pointwallWall.setAppkey(appkey);
            pointwallWall.setPointKey(pointKey);
            pointwallWall.setShopKey(Integer.valueOf(shopKey));
            pointwallWall.setWallMoneyName(wallMoneyName);
            pointwallWall.setTemplate(template);
            PointServiceSngl.get().insertPointwallWall(pointwallWall);

            ToolsLog log = new ToolsLog();
            log.setOpUserId(getCurrentUser().getUserid());//用户的ID
            log.setOperType(LogOperType.POINT_POINTWALL_WALL_ADD);    //操作的类型
            log.setOpTime(new Date());//操作时间
            log.setOpIp(getIp());//用户IP
            log.setOpAfter("pw_wall表新增,appkey:" + appkey + ";pointKey:" + pointKey + ";shopKey:" + shopKey + ";wallMoneyName:" + wallMoneyName + ";template:" + template); //描述 推荐用中文
            addLog(log);

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/point/pointwall/wall/createpage", mapMessage);
        }
        return new ModelAndView("redirect:/point/pointwall/wall/list");
    }


    //编辑一个积分墙
    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyPage(@RequestParam(value = "appkey", required = true) String appkey) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        String appKeyName = "";
        //   Map<String, String> types = new HashMap<String, String>();
        try {
            if (appkey == null) {
                mapMessage = putErrorMessage(mapMessage, "pointwallWall.appkey.empty");
                return new ModelAndView("/point/pointwall/walllist", mapMessage);
            }

            AuthApp authApp = OAuthServiceSngl.get().getApp(appkey);
            if (authApp != null) {
                appKeyName = authApp.getAppName();
            }
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(PointwallWallField.APPKEY, appkey));
            PointwallWall pointwallWall = PointServiceSngl.get().getPointwallWall(queryExpress);
            if (pointwallWall == null) {
                mapMessage = putErrorMessage(mapMessage, "pointwallWall.appkey.empty");
                return new ModelAndView("/point/pointwall/walllist", mapMessage);
            }

            mapMessage.put("types", PointKeyType.getAll());
            mapMessage.put("shopTypes", GoodsActionType.getAll());
            mapMessage.put("appkey", pointwallWall.getAppkey());
            mapMessage.put("appKeyName", appKeyName);
            mapMessage.put("pointKey", pointwallWall.getPointKey());
            mapMessage.put("shopKey", pointwallWall.getShopKey());
            mapMessage.put("wallMoneyName", pointwallWall.getWallMoneyName());
            mapMessage.put("pointwall", pointwallWall);

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/point/pointwall/walllist", mapMessage);
        }

        return new ModelAndView("/point/pointwall/walledit", mapMessage);
    }

    //用于编辑一个积分墙的pointKey和 wall_money_name等的内容后跳到此方法 k
    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "appkey", required = false) String appkey,
                               @RequestParam(value = "pointKey", required = false) String pointKey,
                               @RequestParam(value = "shopKey", required = false) String shopKey,
                               @RequestParam(value = "wallMoneyName", required = false) String wallMoneyName,
                               @RequestParam(value = "template", required = false) String template) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(PointwallWallField.POINT_KEY, pointKey);
            updateExpress.set(PointwallWallField.WALL_MONEY_NAME, wallMoneyName);
            updateExpress.set(PointwallWallField.TEMPLATE, template);
            updateExpress.set(PointwallWallField.SHOP_KEY, Integer.valueOf(shopKey));
            QueryExpress queryExpressNew = new QueryExpress();

            queryExpressNew.add(QueryCriterions.eq(PointwallWallField.APPKEY, appkey));
            boolean bool = PointServiceSngl.get().updatePointwallWall(updateExpress, queryExpressNew);
            if (bool) {
                ToolsLog log = new ToolsLog();
                log.setOpUserId(getCurrentUser().getUserid());//用户的ID
                log.setOperType(LogOperType.POINT_POINTWALL_WALL_MODIFY);    //操作的类型
                log.setOpTime(new Date());//操作时间
                log.setOpIp(getIp());//用户IP
                log.setOpAfter("pw_wall表条目修改appkey:" + appkey + ";pointKey:" + pointKey + ";shopKey:" + shopKey + ";wallMoneyName:" + wallMoneyName + ";template:" + template); //描述 推荐用中文
                addLog(log);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            //         mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/point/pointwall/wall/list");
    }

    //删除一个积分墙
    @RequestMapping(value = "/delete")
    public ModelAndView delete(@RequestParam(value = "appkey", required = true) String appkey) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(PointwallWallAppField.APPKEY, appkey));
            int total = PointServiceSngl.get().countPointwallWallAppOfOneWall(queryExpress);
            if (total > 0) {
                Map<String, Object> mapMessageNew = new HashMap<String, Object>();
                mapMessageNew.put("errorFK", "待删除appkey仍然关联着某些应用,请先解除关联,再删除!");
                return new ModelAndView("redirect:/point/pointwall/wall/list", mapMessageNew);
            }

            QueryExpress queryExpressNew = new QueryExpress();
            queryExpressNew.add(QueryCriterions.eq(PointwallWallField.APPKEY, appkey));
            int result = PointServiceSngl.get().deletePointwallWall(queryExpressNew);

            if (result > 0) {
                ToolsLog log = new ToolsLog();
                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.POINT_PW_WALL_DELETE);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("pw_wall表删除appkey is:" + appkey);
                addLog(log);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            // mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/point/pointwall/wall/list");
    }


    //编辑属于某个积分墙的一条app
    @RequestMapping(value = "/appmodifypage")
    public ModelAndView appmodifypage(@RequestParam(value = "wallAppId", required = true) Long wallAppId, @RequestParam(value = "appkey", required = true) String appkey) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        String appKeyName = "";
        try {

            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(PointwallWallAppField.WALL_APP_ID, wallAppId));
            PointwallWallApp pointwallWallApp = PointServiceSngl.get().getPointwallWallApp(queryExpress);

            mapMessage.put("wallAppId", wallAppId);
            QueryExpress queryExpressNew = new QueryExpress();
            queryExpressNew.add(QueryCriterions.eq(PointwallAppField.APP_ID, pointwallWallApp.getAppId()));
            PointwallApp pointwallApp = PointServiceSngl.get().getPointwallApp(queryExpressNew);

            AuthApp authApp = OAuthServiceSngl.get().getApp(appkey);
            if (authApp != null) {
                appKeyName = authApp.getAppName();
            }
            mapMessage.put("packageName", pointwallApp.getPackageName());
            mapMessage.put("appName", pointwallApp.getAppName());

            mapMessage.put("appkey", appkey);
            mapMessage.put("appKeyName", appKeyName);
            mapMessage.put("platform", pointwallWallApp.getPlatform());
            mapMessage.put("displayOrder", pointwallWallApp.getDisplayOrder());
            mapMessage.put("hotStatus", pointwallWallApp.getHotStatus());
            mapMessage.put("pointAmount", pointwallWallApp.getPointAmount());
            mapMessage.put("status", pointwallWallApp.getStatus());

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/point/pointwall/walllist", mapMessage);
        }
        return new ModelAndView("/point/pointwall/wallappedit", mapMessage);
    }


    //用于编辑一个积分墙--->修改其中一个app的各种关联字段后,点击提交后跳转到这个方法
    @RequestMapping(value = "/appmodify")
    public ModelAndView appmodify(@RequestParam(value = "appkey", required = true) String appkey,
                                  @RequestParam(value = "wallAppId", required = true) Long wallAppId,
                                  @RequestParam(value = "platform", required = true) Integer platform,
                                  @RequestParam(value = "displayOrder", required = true) Integer displayOrder,
                                  @RequestParam(value = "hotStatus", required = true) Integer hotStatus,
                                  @RequestParam(value = "pointAmount", required = true) Integer pointAmount,
                                  @RequestParam(value = "status", required = true) String status) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {

            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(PointwallWallAppField.PLATFORM, platform);
            updateExpress.set(PointwallWallAppField.DISPLAY_ORDER, displayOrder);
            updateExpress.set(PointwallWallAppField.HOT_STATUS, hotStatus);
            updateExpress.set(PointwallWallAppField.POINT_AMOUNT, pointAmount);
            updateExpress.set(PointwallWallAppField.STATUS, status);

            QueryExpress queryExpressNew = new QueryExpress();
            queryExpressNew.add(QueryCriterions.eq(PointwallWallAppField.WALL_APP_ID, wallAppId));

            boolean bool = PointServiceSngl.get().updatePointwallWallApp(updateExpress, queryExpressNew);

            if (bool) {
                ToolsLog log = new ToolsLog();
                log.setOpUserId(getCurrentUser().getUserid());//用户的ID
                log.setOperType(LogOperType.POINT_POINTWALL_R_APP_MODIFY);   //操作的类型
                log.setOpTime(new Date());//操作时间
                log.setOpIp(getIp());//用户IP
                log.setOpAfter("pw_r_wall_app表修改条目appkey:" + appkey + ";wallappId:" + wallAppId + ";platform:" + platform + ";pointAmount:" + pointAmount + ";status:" + status); //描述 推荐用中文
                addLog(log);
            }

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            //   mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/point/pointwall/wall/modifypage?appkey=" + appkey);
    }


    //用于编辑一个积分墙-->增加一个app时,
    @RequestMapping(value = "/appcreatepage")
    public ModelAndView appcreatepage(@RequestParam(value = "appkey", required = true) String appkey) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        String appKeyName = "";
        try {
            AuthApp authApp = OAuthServiceSngl.get().getApp(appkey);
            if (authApp != null) {
                appKeyName = authApp.getAppName();
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        mapMessage.put("appkey", appkey);
        mapMessage.put("appKeyName", appKeyName);
        return new ModelAndView("/point/pointwall/wallappadd", mapMessage);
    }


    //用于在编辑积分墙-->新增一个app,在页面写好内容后,点击提交后,跳转到的方法
    @RequestMapping(value = "/appcreate")
    public ModelAndView appcreate(@RequestParam(value = "appkey", required = true) String appkey,
                                  @RequestParam(value = "appId", required = true) Long appId,
                                  @RequestParam(value = "platform", required = true) Integer platform,
                                  @RequestParam(value = "displayOrder", required = true) Integer displayOrder,
                                  @RequestParam(value = "hotStatus", required = true) Integer hotStatus,
                                  @RequestParam(value = "pointAmount", required = true) Integer pointAmount,
                                  @RequestParam(value = "status", required = true) String status) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            PointwallWallApp pointwallWallApp = new PointwallWallApp();
            pointwallWallApp.setAppId(appId);
            pointwallWallApp.setAppkey(appkey);
            pointwallWallApp.setPlatform(platform);
            pointwallWallApp.setDisplayOrder(displayOrder);
            pointwallWallApp.setHotStatus(hotStatus);
            pointwallWallApp.setPointAmount(pointAmount);
            pointwallWallApp.setStatus(status);
            PointServiceSngl.get().insertPointwallWallApp(pointwallWallApp);

            ToolsLog log = new ToolsLog();
            log.setOpUserId(getCurrentUser().getUserid());//用户的ID
            log.setOperType(LogOperType.POINT_POINTWALL_R_APP_ADD);    //操作的类型
            log.setOpTime(new Date());//操作时间
            log.setOpIp(getIp());//用户IP
            log.setOpAfter("pw_r_wall_app表新增条目,appkey:" + appkey + ";appId:" + appId + ";platform:" + platform + ";pointAmount:" + pointAmount + ";status:" + status); //描述 推荐用中文
            addLog(log);

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e:", e);
            //  mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("redirect:/point/pointwall/wall/jsoncreatepage?appkey=" + appkey);
        }
        return new ModelAndView("redirect:/point/pointwall/wall/modifypage?appkey=" + appkey);
    }


    //删除积分墙中的一个app
    @RequestMapping(value = "/appdelete")
    public ModelAndView appdelete(@RequestParam(value = "appkey", required = true) String appkey,
                                  @RequestParam(value = "wallAppId", required = true) Long wallAppId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            QueryExpress queryExpressNew = new QueryExpress();
            queryExpressNew.add(QueryCriterions.eq(PointwallWallAppField.WALL_APP_ID, wallAppId));

            int result = PointServiceSngl.get().deletePointwallWallApp(queryExpressNew);

            if (result > 0) {
                ToolsLog log = new ToolsLog();
                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.POINT_PW_R_WALL_APP_DELETE);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("pw_wall_app表条目删除appkey is:" + appkey + ";wallAppId is :" + wallAppId);
                addLog(log);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            //   mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/point/pointwall/wall/modifypage?appkey=" + appkey);
    }
}
