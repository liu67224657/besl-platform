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
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-8-19
 * Time: 下午3:09
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/joymeapp/tips")
public class AppTipsController extends ToolsBaseController {


    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,      //数据库记录索引
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "5") int pageSize,
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
        platformList.add(AppPlatform.CLIENT);
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
            return new ModelAndView("/joymeapp/tips/tipslist", mapMessage);
        }

        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);

        QueryExpress queryExpress = new QueryExpress();
        if (!StringUtil.isEmpty(queryAppId)) {
            queryExpress.add(QueryCriterions.eq(AppTipsField.APP_KEY, queryAppId));
        }
        if (!StringUtil.isEmpty(queryPlatform)) {
            queryExpress.add(QueryCriterions.eq(AppTipsField.PLATFORM, Integer.valueOf(queryPlatform)));
        }
        queryExpress.add(QuerySort.add(AppTipsField.TIPS_ID, QuerySortOrder.DESC));
        try {
            PageRows<AppTips> pageRows = JoymeAppConfigServiceSngl.get().queryAppTipsByPage(queryExpress, pagination);

            mapMessage.put("list", pageRows.getRows());
            mapMessage.put("page", pageRows.getPage());
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/joymeapp/tips/tipslist", mapMessage);
        }
        return new ModelAndView("/joymeapp/tips/tipslist", mapMessage);
    }


    @RequestMapping(value = "/createpage")
    public ModelAndView createbbs(@RequestParam(value = "queryappid", required = false) String queryAppId,
                                  @RequestParam(value = "queryplatform", required = false) Integer queryPlatform,
                                  @RequestParam(value = "title", required = false) String title,
                                  @RequestParam(value = "description", required = false) String description,
                                  @RequestParam(value = "pic", required = false) String pic,
                                  @RequestParam(value = "url", required = false) String url,
                                  @RequestParam(value = "updatetime", required = false) Date updateTime,
                                  @RequestParam(value = "type", required = false) Integer type,
                                  @RequestParam(value = "appid", required = false) String appId,
                                  @RequestParam(value = "platform", required = false) Integer platform
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("queryAppId", queryAppId);
        mapMessage.put("queryPlatform", queryPlatform);
        mapMessage.put("title", title);
        mapMessage.put("description", description);
        mapMessage.put("pic", pic);
        mapMessage.put("url", url);
        mapMessage.put("type", type);
        mapMessage.put("appId", appId);
        mapMessage.put("platform", platform);
        mapMessage.put("updateTime", updateTime);

        List<AppPlatform> platformList = new ArrayList<AppPlatform>();
        platformList.add(AppPlatform.ANDROID);
        platformList.add(AppPlatform.IOS);
        platformList.add(AppPlatform.CLIENT);
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
            return new ModelAndView("/joymeapp/tips/createtips", mapMessage);
        }
        return new ModelAndView("/joymeapp/tips/createtips", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "queryappid", required = false) String queryAppId,
                               @RequestParam(value = "queryplatform", required = false) Integer queryPlatform,
                               @RequestParam(value = "title", required = false) String title,
                               @RequestParam(value = "description", required = false) String description,
                               @RequestParam(value = "pic", required = false) String pic,
                               @RequestParam(value = "url", required = false) String url,
                               @RequestParam(value = "updatetime", required = false) Date updateTime,
                               @RequestParam(value = "type", required = false) Integer type,
                               @RequestParam(value = "appid", required = false) String appId,
                               @RequestParam(value = "platform", required = false) Integer platform
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("queryAppId", queryAppId);
        mapMessage.put("queryPlatform", queryPlatform);
        mapMessage.put("title", title);
        mapMessage.put("description", description);
        mapMessage.put("pic", pic);
        mapMessage.put("url", url);
        mapMessage.put("type", type);
        mapMessage.put("appId", appId);
        mapMessage.put("platform", platform);
        mapMessage.put("updateTime", updateTime);

        try {
            AppTips appTips = new AppTips();
            appTips.setTipsTitle(title);
            appTips.setTipsDescription(description);
            appTips.setTipsPic(pic);
            appTips.setTipsUrl(url);
            appTips.setUpdateTime(updateTime);
            appTips.setTipsType(AppTipsType.DEFAULT);
            appTips.setPlatform(platform);
            appTips.setAppId(appId);
            appTips.setCreateDate(new Date());
            appTips.setCreateIp(getIp());
            appTips.setCreateUserId(getCurrentUser().getUserid());

            JoymeAppConfigServiceSngl.get().createAppTips(appTips);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("forward:/joymeapp/tips/createpage", mapMessage);
        }
        return new ModelAndView("redirect:/joymeapp/tips/list?queryappid=" + (StringUtil.isEmpty(queryAppId) ? "" : queryAppId) + "&queryplatform=" + (queryPlatform == null ? 3 : queryPlatform));
    }

    @RequestMapping(value = "/remove")
    public ModelAndView remove(@RequestParam(value = "queryappid", required = false) String queryAppId,
                               @RequestParam(value = "queryplatform", required = false) Integer queryPlatform,
                               @RequestParam(value = "tid", required = false) Long tipsId,
                               @RequestParam(value = "offset", required = false, defaultValue = "0") Integer pageStartIndex) {
        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(AppTipsField.REMOVE_STATUS, ActStatus.ACTED.getCode());
        updateExpress.set(AppTipsField.MODIFY_DATE, new Date());
        updateExpress.set(AppTipsField.MODIFY_IP, getIp());
        updateExpress.set(AppTipsField.MODIFY_USERID, getCurrentUser().getUserid());
        try {
            JoymeAppConfigServiceSngl.get().modifyAppTips(new QueryExpress().add(QueryCriterions.eq(AppTipsField.TIPS_ID, tipsId)), updateExpress);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e:", e);
        }
        return new ModelAndView("redirect:/joymeapp/tips/list?queryappid=" + (StringUtil.isEmpty(queryAppId) ? "" : queryAppId) + "&queryplatform=" + (queryPlatform == null ? "" : queryPlatform) + "&pager.offset=" + pageStartIndex);
    }

    @RequestMapping(value = "/recover")
    public ModelAndView recover(@RequestParam(value = "queryappid", required = false) String queryAppId,
                                @RequestParam(value = "queryplatform", required = false) Integer queryPlatform,
                                @RequestParam(value = "tid", required = true) Long tipsId,
                                @RequestParam(value = "offset", required = false, defaultValue = "0") Integer pageStartIndex) {
        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(AppTipsField.REMOVE_STATUS, ActStatus.UNACT.getCode());
        updateExpress.set(AppTipsField.MODIFY_DATE, new Date());
        updateExpress.set(AppTipsField.MODIFY_IP, getIp());
        updateExpress.set(AppTipsField.MODIFY_USERID, getCurrentUser().getUserid());
        try {
            JoymeAppConfigServiceSngl.get().modifyAppTips(new QueryExpress().add(QueryCriterions.eq(AppTipsField.TIPS_ID, tipsId)), updateExpress);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e:", e);
        }
        return new ModelAndView("redirect:/joymeapp/tips/list?queryappid=" + (StringUtil.isEmpty(queryAppId) ? "" : queryAppId) + "&queryplatform=" + (queryPlatform == null ? "" : queryPlatform) + "&pager.offset=" + pageStartIndex);
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyVersion(@RequestParam(value = "queryappid", required = false) String queryAppId,
                                      @RequestParam(value = "queryplatform", required = false) Integer queryPlatform,
                                      @RequestParam(value = "tid", required = true) Long tipsId,
                                      @RequestParam(value = "offset", required = false, defaultValue = "0") Integer pageStartIndex) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("pageStartIndex", pageStartIndex);
        mapMessage.put("queryAppId", queryAppId);
        mapMessage.put("queryPlatform", queryPlatform);

        List<AppPlatform> platformList = new ArrayList<AppPlatform>();
        platformList.add(AppPlatform.ANDROID);
        platformList.add(AppPlatform.IOS);
        platformList.add(AppPlatform.CLIENT);
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

            AppTips appTips = JoymeAppConfigServiceSngl.get().getAppTips(new QueryExpress().add(QueryCriterions.eq(AppTipsField.TIPS_ID, tipsId)));
            if (appTips == null) {
                return new ModelAndView("redirect:/joymeapp/tips/list?pager.offset=" + pageStartIndex);
            }
            mapMessage.put("tips", appTips);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e:", e);
            return new ModelAndView("redirect:/joymeapp/tips/list?pager.offset=" + pageStartIndex);
        }
        return new ModelAndView("/joymeapp/tips/modifytips", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView preModifyApp(@RequestParam(value = "queryappid", required = false) String queryAppId,
                                     @RequestParam(value = "queryplatform", required = false) Integer queryPlatform,
                                     @RequestParam(value = "tid", required = true) Long tipsId,
                                     @RequestParam(value = "offset", required = false, defaultValue = "0") Integer pageStartIndex,
                                     @RequestParam(value = "title", required = false) String title,
                                     @RequestParam(value = "description", required = false) String description,
                                     @RequestParam(value = "pic", required = false) String pic,
                                     @RequestParam(value = "url", required = false) String url,
                                     @RequestParam(value = "updatetime", required = false) Date updateTime,
                                     @RequestParam(value = "type", required = false) Integer type,
                                     @RequestParam(value = "appid", required = false) String appId,
                                     @RequestParam(value = "platform", required = false) Integer platform) {

        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(AppTipsField.TITLE, title);
        updateExpress.set(AppTipsField.DESCRIPTION, description);
        updateExpress.set(AppTipsField.PIC, pic);
        updateExpress.set(AppTipsField.URL, url);
        updateExpress.set(AppTipsField.TYPE, type);
        updateExpress.set(AppTipsField.UPDATE_TIME, updateTime);
        updateExpress.set(AppTipsField.APP_KEY, appId);
        updateExpress.set(AppTipsField.PLATFORM, platform);
        updateExpress.set(AppTipsField.MODIFY_DATE, new Date());
        updateExpress.set(AppTipsField.MODIFY_IP, getIp());
        updateExpress.set(AppTipsField.MODIFY_USERID, getCurrentUser().getUserid());
        try {
            JoymeAppConfigServiceSngl.get().modifyAppTips(new QueryExpress().add(QueryCriterions.eq(AppTipsField.TIPS_ID, tipsId)), updateExpress);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return new ModelAndView("redirect:/joymeapp/tips/modifypage?tid=" + tipsId + "&offset=" + pageStartIndex);
        }
        return new ModelAndView("redirect:/joymeapp/tips/list?queryappid=" + (StringUtil.isEmpty(queryAppId) ? "" : queryAppId) + "&queryplatform=" + (queryPlatform == null ? "" : queryPlatform) + "&pager.offset=" + pageStartIndex);
    }
}
