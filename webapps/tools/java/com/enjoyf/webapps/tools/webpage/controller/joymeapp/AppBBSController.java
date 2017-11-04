package com.enjoyf.webapps.tools.webpage.controller.joymeapp;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.joymeappconfig.JoymeAppConfigServiceSngl;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.AuthAppField;
import com.enjoyf.platform.service.oauth.AuthAppType;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.CollectionUtil;
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

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-30
 * Time: 下午1:43
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/joymeapp/bbs")
public class AppBBSController extends ToolsBaseController {

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,      //数据库记录索引
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                             @RequestParam(value = "queryappid", required = false) String queryAppId,
                             @RequestParam(value = "queryplatform", required = false) String queryPlatform) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        if (!StringUtil.isEmpty(queryAppId)) {
            mapMessage.put("queryAppId", queryAppId);
        }
        if (!StringUtil.isEmpty(queryPlatform)) {
            mapMessage.put("queryPlatform", queryPlatform);
        }

        List<AppPlatform> platformList = new ArrayList<AppPlatform>();
        platformList.add(AppPlatform.ANDROID);
        platformList.add(AppPlatform.IOS);
        mapMessage.put("platformList", platformList);

        try {
            List<AuthApp> appList = OAuthServiceSngl.get().queryAuthApp(new QueryExpress()
                    .add(QueryCriterions.eq(AuthAppField.APPTYPE, AuthAppType.INTERNAL_CLIENT.getCode()))
                    .add(QueryCriterions.in(AuthAppField.PLATOFRM, new Integer[]{AppPlatform.ANDROID.getCode(), AppPlatform.CLIENT.getCode(), AppPlatform.IOS.getCode()}))
                    .add(QueryCriterions.eq(AuthAppField.VALIDSTATUS, ValidStatus.VALID.getCode()))
            );

            if (!CollectionUtil.isEmpty(appList)) {
                mapMessage.put("appList", appList);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/joymeapp/bbs/bbslist", mapMessage);
        }

        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);

        QueryExpress queryExpress = new QueryExpress();
        if (!StringUtil.isEmpty(queryAppId)) {
            queryExpress.add(QueryCriterions.eq(AppDeploymentField.APP_KEY, queryAppId));
        }
        if (!StringUtil.isEmpty(queryPlatform)) {
            queryExpress.add(QueryCriterions.eq(AppDeploymentField.PLATFORM, Integer.valueOf(queryPlatform)));
        }
        queryExpress.add(QueryCriterions.eq(AppDeploymentField.DEPLOYMENT_TYPE, AppDeploymentType.BBS.getCode()));
        queryExpress.add(QuerySort.add(AppDeploymentField.CREATE_DATE, QuerySortOrder.DESC));
        try {
            PageRows<AppDeployment> pageRows = JoymeAppConfigServiceSngl.get().queryAppDeploymentByPage(queryExpress, pagination);

            mapMessage.put("list", pageRows.getRows());
            mapMessage.put("page", pageRows.getPage());
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/joymeapp/bbs/bbslist", mapMessage);
        }

        return new ModelAndView("/joymeapp/bbs/bbslist", mapMessage);
    }


    @RequestMapping(value = "/createpage")
    public ModelAndView createbbs(@RequestParam(value = "queryappid", required = false) String queryAppId,
                                  @RequestParam(value = "queryplatform", required = false) Integer queryPlatform,
                                  @RequestParam(value = "path", required = false) String path,
                                  @RequestParam(value = "appkey", required = false) String appId,
                                  @RequestParam(value = "platform", required = false) Integer platform) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("queryAppId", queryAppId);
        mapMessage.put("queryPlatform", queryPlatform);
        mapMessage.put("path", path);
        mapMessage.put("appId", appId);
        mapMessage.put("platform", platform);

        List<AppPlatform> platformList = new ArrayList<AppPlatform>();
        platformList.add(AppPlatform.ANDROID);
        platformList.add(AppPlatform.IOS);
        mapMessage.put("platformList", platformList);

        try {
            List<AuthApp> appList = OAuthServiceSngl.get().queryAuthApp(new QueryExpress()
                    .add(QueryCriterions.eq(AuthAppField.APPTYPE, AuthAppType.INTERNAL_CLIENT.getCode()))
                    .add(QueryCriterions.in(AuthAppField.PLATOFRM, new Integer[]{AppPlatform.ANDROID.getCode(), AppPlatform.CLIENT.getCode(), AppPlatform.IOS.getCode()}))
                    .add(QueryCriterions.eq(AuthAppField.VALIDSTATUS, ValidStatus.VALID.getCode()))
            );

            if (!CollectionUtil.isEmpty(appList)) {
                mapMessage.put("appList", appList);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/joymeapp/bbs/createbbs", mapMessage);
        }
        return new ModelAndView("/joymeapp/bbs/createbbs", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "queryappid", required = false) String queryAppId,
                               @RequestParam(value = "queryplatform", required = false) Integer queryPlatform,
                               @RequestParam(value = "path", required = false) String path,
                               @RequestParam(value = "appkey", required = false) String appId,
                               @RequestParam(value = "platform", required = false) Integer platform
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("path", path);
        mapMessage.put("appId", appId);
        mapMessage.put("platform", platform);

        try {
            AppDeployment appDeployment = new AppDeployment();
            appDeployment.setAppDeploymentType(AppDeploymentType.BBS);
            appDeployment.setAppkey(appId);
            appDeployment.setAppPlatform(AppPlatform.getByCode(platform));
            appDeployment.setPath(path);
            appDeployment.setCreateDate(new Date());
            appDeployment.setCreateIp(getIp());
            appDeployment.setCreateUserId(getCurrentUser().getUserid());

            JoymeAppConfigServiceSngl.get().createAppDeployment(appDeployment);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("forward:/joymeapp/bbs/createpage", mapMessage);
        }
        return new ModelAndView("redirect:/joymeapp/bbs/list?queryappid=" + (StringUtil.isEmpty(queryAppId) ? "" : queryAppId) + "&queryplatform=" + (queryPlatform == null ? "" : queryPlatform));
    }

    @RequestMapping(value = "/remove")
    public ModelAndView remove(@RequestParam(value = "queryappid", required = false) String queryAppId,
                               @RequestParam(value = "queryplatform", required = false) Integer queryPlatform,
                               @RequestParam(value = "bid", required = true) Long bbsId,
                               @RequestParam(value = "offset", required = false, defaultValue = "0") Integer pageStartIndex) {
        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(AppDeploymentField.REMOVE_STATUS, ActStatus.ACTED.getCode());
        updateExpress.set(AppDeploymentField.MODIFY_DATE, new Date());
        updateExpress.set(AppDeploymentField.MODIFY_IP, getIp());
        updateExpress.set(AppDeploymentField.MODIFY_USERID, getCurrentUser().getUserid());
        try {
            JoymeAppConfigServiceSngl.get().modifyAppDeployment(new QueryExpress().add(QueryCriterions.eq(AppDeploymentField.DEPLOYMENT_ID, bbsId)), updateExpress);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e:", e);
        }
        return new ModelAndView("redirect:/joymeapp/bbs/list?queryappid=" + (StringUtil.isEmpty(queryAppId) ? "" : queryAppId) + "&queryplatform=" + (queryPlatform == null ? "" : queryPlatform) + "&pager.offset=" + pageStartIndex);
    }

    @RequestMapping(value = "/recover")
    public ModelAndView recover(@RequestParam(value = "queryappid", required = false) String queryAppId,
                                @RequestParam(value = "queryplatform", required = false) Integer queryPlatform,
                                @RequestParam(value = "bid", required = true) Long bbsId,
                                @RequestParam(value = "offset", required = false, defaultValue = "0") Integer pageStartIndex) {
        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(AppDeploymentField.REMOVE_STATUS, ActStatus.UNACT.getCode());
        updateExpress.set(AppDeploymentField.MODIFY_DATE, new Date());
        updateExpress.set(AppDeploymentField.MODIFY_IP, getIp());
        updateExpress.set(AppDeploymentField.MODIFY_USERID, getCurrentUser().getUserid());
        try {
            JoymeAppConfigServiceSngl.get().modifyAppDeployment(new QueryExpress().add(QueryCriterions.eq(AppDeploymentField.DEPLOYMENT_ID, bbsId)), updateExpress);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e:", e);
        }
        return new ModelAndView("redirect:/joymeapp/bbs/list?queryappid=" + (StringUtil.isEmpty(queryAppId) ? "" : queryAppId) + "&queryplatform=" + (queryPlatform == null ? "" : queryPlatform) + "&pager.offset=" + pageStartIndex);
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyVersion(@RequestParam(value = "queryappid", required = false) String queryAppId,
                                      @RequestParam(value = "queryplatform", required = false) Integer queryPlatform,
                                      @RequestParam(value = "bid", required = true) Long bbsId,
                                      @RequestParam(value = "offset", required = false, defaultValue = "0") Integer pageStartIndex) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("pageStartIndex", pageStartIndex);
        mapMessage.put("queryAppId", queryAppId);
        mapMessage.put("queryPlatform", queryPlatform);

        List<AppPlatform> platformList = new ArrayList<AppPlatform>();
        platformList.add(AppPlatform.ANDROID);
        platformList.add(AppPlatform.IOS);
        mapMessage.put("platformList", platformList);

        try {
            List<AuthApp> appList = OAuthServiceSngl.get().queryAuthApp(new QueryExpress()
                    .add(QueryCriterions.eq(AuthAppField.APPTYPE, AuthAppType.INTERNAL_CLIENT.getCode()))
                    .add(QueryCriterions.in(AuthAppField.PLATOFRM, new Integer[]{AppPlatform.ANDROID.getCode(), AppPlatform.CLIENT.getCode(), AppPlatform.IOS.getCode()}))
                    .add(QueryCriterions.eq(AuthAppField.VALIDSTATUS, ValidStatus.VALID.getCode()))
            );

            if (!CollectionUtil.isEmpty(appList)) {
                mapMessage.put("appList", appList);
            }

            AppDeployment appDeployment = JoymeAppConfigServiceSngl.get().getAppDeployment(new QueryExpress().add(QueryCriterions.eq(AppDeploymentField.DEPLOYMENT_ID, bbsId)));
            if (appDeployment == null) {
                return new ModelAndView("redirect:/joymeapp/bbs/list?pager.offset=" + pageStartIndex);
            }
            mapMessage.put("appBBS", appDeployment);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e:", e);
            return new ModelAndView("redirect:/joymeapp/bbs/list?pager.offset=" + pageStartIndex);
        }
        return new ModelAndView("/joymeapp/bbs/modifybbs", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView preModifyApp(@RequestParam(value = "queryappid", required = false) String queryAppId,
                                     @RequestParam(value = "queryplatform", required = false) Integer queryPlatform,
                                     @RequestParam(value = "bid", required = true) Long bbsId,
                                     @RequestParam(value = "offset", required = false, defaultValue = "0") Integer pageStartIndex,
                                     @RequestParam(value = "path", required = false) String path,
                                     @RequestParam(value = "appkey", required = false) String appId,
                                     @RequestParam(value = "platform", required = false) Integer platform) {

        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(AppDeploymentField.PATH, path);
        updateExpress.set(AppDeploymentField.APP_KEY, appId);
        updateExpress.set(AppDeploymentField.PLATFORM, platform);
        updateExpress.set(AppDeploymentField.MODIFY_DATE, new Date());
        updateExpress.set(AppDeploymentField.MODIFY_IP, getIp());
        updateExpress.set(AppDeploymentField.MODIFY_USERID, getCurrentUser().getUserid());
        try {
            JoymeAppConfigServiceSngl.get().modifyAppDeployment(new QueryExpress().add(QueryCriterions.eq(AppDeploymentField.DEPLOYMENT_ID, bbsId)), updateExpress);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return new ModelAndView("redirect:/joymeapp/bbs/modifypage?cid=" + bbsId + "&offset=" + pageStartIndex);
        }
        return new ModelAndView("redirect:/joymeapp/bbs/list?queryappid=" + (StringUtil.isEmpty(queryAppId) ? "" : queryAppId) + "&queryplatform=" + (queryPlatform == null ? "" : queryPlatform) + "&pager.offset=" + pageStartIndex);
    }

}
