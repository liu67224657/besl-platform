package com.enjoyf.webapps.tools.webpage.controller.video;

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
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * Created by xupeng on 16/3/17.
 */
@Controller
@RequestMapping(value = "/video/game")
public class VideoGameController extends ToolsBaseController {
    @RequestMapping(value = "/list")
    public ModelAndView queryAppList(@RequestParam(value = "appname", required = false) String appName,
                                     @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,      //数据库记录索引
                                     @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize) {

        int curPage = (pageStartIndex / pageSize) + 1;

        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);

        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(AuthAppField.APPTYPE, AuthAppType.VIDEO_SDK.getCode()));
            if (!StringUtil.isEmpty(appName)) {
                mapMessage.put("appName", appName);
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

        return new ModelAndView("/video/gamelist", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createAppPage() {
        Map<String, Object> mapMessage = new HashMap<String, Object>();


        return new ModelAndView("/video/createapp", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView preCreatePage(@RequestParam(value = "appname", required = true) String appName,
                                      @RequestParam(value = "displayred", required = false) String displayRed) {
        AuthApp authApp = new AuthApp();
        authApp.setAppId(UUIDUtil.getShortUUID().replaceAll("-", "0").replace("_", "0"));
        authApp.setAppKey(RandomUtil.getRandomUUID(32));
        authApp.setAppName(appName);
        authApp.setAppType(AuthAppType.VIDEO_SDK);
        authApp.setAuditStatus(ActStatus.ACTED);
        authApp.setCreateDate(new Date());
        authApp.setCreateIp(getIp());
        authApp.setPlatform(AppPlatform.CLIENT);
        authApp.setValidStatus(ValidStatus.VALID);
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

        return new ModelAndView("redirect:/video/game/list");
    }

    //
    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyAppPage(@RequestParam(value = "appid", required = true) String appId,
                                      @RequestParam(value = "returnFlag", required = false) String returnFlag) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        //   用于返回到积分墙管理
        try {
            AuthApp authApp = OAuthServiceSngl.get().getApp(appId);

            if (authApp == null) {
                mapMessage.put("errorMsg", "joyme.app.notexists");
                return new ModelAndView("forward:/video/game/list", mapMessage);
            }

            mapMessage.put("authapp", authApp);
            writeToolsLog(LogOperType.MODIFY_APP_MODIFYPAGE, "编辑查看appId：" + appId);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " modifyAppPage occured ServiceException.e:", e);
            mapMessage.put("errorMsg", "error.exception");
            return new ModelAndView("forward:/video/game/list", mapMessage);
        }

        return new ModelAndView("/video/modifyapp", mapMessage);
    }

    //
    @RequestMapping(value = "/modify")
    public ModelAndView preModifyApp(@RequestParam(value = "appid", required = true) String appId,
                                     @RequestParam(value = "appname", required = true) String appName,
                                     @RequestParam(value = "ios", required = false) String ios,
                                     @RequestParam(value = "android", required = false) String android) {

        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(AuthAppField.APPNAME, appName);
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


        return new ModelAndView("redirect:/video/game/list");
    }

    @RequestMapping(value = "/delete")
    public ModelAndView preDeletePage(@RequestParam(value = "appid", required = true) String appTd,
                                      @RequestParam(value = "status", required = false) String status) {
        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(AuthAppField.VALIDSTATUS, status);
        try {
            boolean bval = OAuthServiceSngl.get().modifyAuthApp(appTd, updateExpress);
            if (bval) {
                writeToolsLog(LogOperType.MODIFY_APP_DELETE, "删除appId：" + appTd + " 状态：" + status);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
        }

        return new ModelAndView("redirect:/video/game/list");
    }
}

