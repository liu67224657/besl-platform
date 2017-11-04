package com.enjoyf.webapps.tools.webpage.controller.joymeapp;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.joymeapp.AppSecret;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.AuthAppField;
import com.enjoyf.platform.service.oauth.AuthAppType;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.RandomUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.oauth.renren.api.client.param.Auth;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-30
 * Time: 下午1:43
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/joymeapp/app")
public class AppController extends ToolsBaseController {

    @RequestMapping(value = "/list")
    public ModelAndView queryAppList(@RequestParam(value = "appname", required = false) String appName,
                                     @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,      //数据库记录索引
                                     @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize) {

        int curPage = (pageStartIndex / pageSize) + 1;

        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);

        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            QueryExpress queryExpress = new QueryExpress();
            if (!StringUtil.isEmpty(appName)) {
                queryExpress.add(QueryCriterions.like(AuthAppField.APPNAME, "%" + appName + "%"));
            }
            queryExpress.add(QuerySort.add(AuthAppField.CREATEDATE, QuerySortOrder.DESC));

            PageRows<AuthApp> pageRows = OAuthServiceSngl.get().queryAuthApp(queryExpress, pagination);

            mapMessage.put("list", pageRows.getRows());
            mapMessage.put("page", pageRows.getPage());
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("/joymeapp/joymeapplist", mapMessage);
    }


    //
    @RequestMapping(value = "/createpage")
    public ModelAndView createAppPage() {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        List<AppPlatform> platformList = new ArrayList<AppPlatform>();
//        platformList.add(AppPlatform.ANDROID);
//        platformList.add(AppPlatform.IOS);
//        platformList.add(AppPlatform.CLIENT);
//        mapMessage.put("platformList", platformList);


        return new ModelAndView("/joymeapp/createapp", mapMessage);
    }

    //
    @RequestMapping(value = "/create")
    public ModelAndView preCreatePage(@RequestParam(value = "appname", required = true) String appName,
                                      @RequestParam(value = "displaymy", required = false) String displayMy,
                                      @RequestParam(value = "displayred", required = false) String displayRed) {
        AuthApp authApp = new AuthApp();
        authApp.setAppId(UUIDUtil.getShortUUID().replaceAll("-", "0").replace("_", "0"));
        authApp.setAppKey(RandomUtil.getRandomUUID(32));
        authApp.setAppName(appName);
        authApp.setAppType(AuthAppType.INTERNAL_CLIENT);
        authApp.setAuditStatus(ActStatus.ACTED);
        authApp.setCreateDate(new Date());
        authApp.setCreateIp(getIp());
        authApp.setPlatform(AppPlatform.CLIENT);
        authApp.setValidStatus(ValidStatus.VALID);
        authApp.setDisplayMy(Integer.parseInt(displayMy));
        authApp.setProfileKey("www");
//        authApp.setDisplayRed(Integer.parseInt(displayRed));
        authApp.setModifyDate(new Date());

        AppSecret appSecret = new AppSecret();
        appSecret.setIos(RandomUtil.getRandomUUID(32));
        appSecret.setAndroid(RandomUtil.getRandomUUID(32));
        authApp.setAppSecret(appSecret);
        try {
            authApp = OAuthServiceSngl.get().appplyAuthApp(authApp);

            writeToolsLog(LogOperType.MODIFY_APP_ADD, "新增appId：" + authApp.getAppId());
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
        }

        return new ModelAndView("redirect:/joymeapp/app/list");
    }

    //
    @RequestMapping(value = "/delete")
    public ModelAndView preDeletePage(@RequestParam(value = "appid", required = true) String appTd) {
        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(AuthAppField.VALIDSTATUS, ValidStatus.INVALID.getCode());
        try {
            boolean bval = OAuthServiceSngl.get().modifyAuthApp(appTd, updateExpress);
            if (bval) {
                writeToolsLog(LogOperType.MODIFY_APP_DELETE, "删除appId：" + appTd);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
        }

        return new ModelAndView("redirect:/joymeapp/app/list");
    }

    //
    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyAppPage(@RequestParam(value = "appid", required = true) String appId,
                                      @RequestParam(value = "returnFlag", required = false) String returnFlag) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        //   用于返回到积分墙管理
        mapMessage.put("returnFlag", returnFlag);
        try {
            AuthApp authApp = OAuthServiceSngl.get().getApp(appId);

            if (authApp == null) {
                mapMessage.put("errorMsg", "joyme.app.notexists");
                return new ModelAndView("forward:/joymeapp/app/list", mapMessage);
            }

            mapMessage.put("authapp", authApp);
            writeToolsLog(LogOperType.MODIFY_APP_MODIFYPAGE, "编辑查看appId：" + appId);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " modifyAppPage occured ServiceException.e:", e);
            mapMessage.put("errorMsg", "error.exception");
            return new ModelAndView("forward:/joymeapp/app/list", mapMessage);
        }

        return new ModelAndView("/joymeapp/modifyapp", mapMessage);
    }

    //
    @RequestMapping(value = "/modify")
    public ModelAndView preModifyApp(@RequestParam(value = "appid", required = true) String appId,
                                     @RequestParam(value = "appname", required = true) String appName,
                                     @RequestParam(value = "displaymy", required = false) String displayMy,
                                     @RequestParam(value = "displayred", required = false) String displayRed,
                                     @RequestParam(value = "returnFlag", required = false) String returnFlag,
                                     @RequestParam(value = "ios", required = false) String ios,
                                     @RequestParam(value = "android", required = false) String android) {

        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(AuthAppField.APPNAME, appName);
        updateExpress.set(AuthAppField.DISPLAYMY, Integer.parseInt(displayMy));
//        updateExpress.set(AuthAppField.DISPLAYRED, Integer.parseInt(displayRed));
        updateExpress.set(AuthAppField.MODIFYDATE, new Date());

        AppSecret appSecret = new AppSecret();
        appSecret.setIos(ios);
        appSecret.setAndroid(android);
        updateExpress.set(AuthAppField.APPSECRET, appSecret.toJson());


        try {
            boolean bval = OAuthServiceSngl.get().modifyAuthApp(appId, updateExpress);
            if (bval) {
                writeToolsLog(LogOperType.MODIFY_APP_MODIFY, "编辑修改appId：" + appId);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
        }


        //返回到积分墙管理
        if (returnFlag != null && returnFlag.equals("toPointWall")) {
            return new ModelAndView("forward:/point/pointwall/wall/list");

        }

        return new ModelAndView("redirect:/joymeapp/app/list");
    }

}
