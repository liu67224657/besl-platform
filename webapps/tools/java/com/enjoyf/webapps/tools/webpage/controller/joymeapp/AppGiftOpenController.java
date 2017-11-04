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
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.service.tools.ToolsLog;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.webapps.tools.weblogic.joymeapp.JoymeAppWebLogic;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-9-1
 * Time: 上午9:17
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/joymeapp/giftopen")
public class AppGiftOpenController extends ToolsBaseController {

    private static Set<Integer> platformSet = new HashSet<Integer>();

    static {
        platformSet.add(AppPlatform.ANDROID.getCode());
        platformSet.add(AppPlatform.IOS.getCode());
    }

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,      //数据库记录索引
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                             @RequestParam(value = "appkey", required = false) String appKey) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();

        mapMessage.put("appKey", appKey);

        int curPage = pageStartIndex / pageSize + 1;

        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(AppInfoField.APP_KEY, appKey));
        queryExpress.add(QuerySort.add(AppInfoField.INFO_ID, QuerySortOrder.DESC));

        try {
            List<AuthApp> appList = OAuthServiceSngl.get().queryAuthApp(new QueryExpress()
                    .add(QueryCriterions.eq(AuthAppField.APPTYPE, AuthAppType.INTERNAL_CLIENT.getCode()))
                    .add(QueryCriterions.in(AuthAppField.PLATOFRM, new Integer[]{AppPlatform.ANDROID.getCode(), AppPlatform.CLIENT.getCode(), AppPlatform.IOS.getCode()}))
                    .add(QueryCriterions.eq(AuthAppField.VALIDSTATUS, ValidStatus.VALID.getCode()))
            );

            if (!CollectionUtil.isEmpty(appList)) {
                mapMessage.put("appList", appList);
            }

            PageRows<AppInfo> pageRows = JoymeAppConfigServiceSngl.get().queryAppInfoByPage(queryExpress, pagination);

            mapMessage.put("list", pageRows.getRows());
            mapMessage.put("page", pageRows.getPage());
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/joymeapp/giftopen/giftopenlist", mapMessage);
    }


    //
    @RequestMapping(value = "/createpage")
    public ModelAndView createPage(@RequestParam(value = "appkey", required = false) String appKey,
                                   @RequestParam(value = "appname", required = false) String appName,
                                   @RequestParam(value = "platform", required = false) Integer platform,
                                   @RequestParam(value = "version", required = false) String version,
                                   @RequestParam(value = "channeltype", required = false) String channelType,
                                   @RequestParam(value = "hasgift", required = false) Boolean hasGift) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        mapMessage.put("appKey", appKey);
        mapMessage.put("appName", appName);
        mapMessage.put("platform", platform);
        mapMessage.put("version", version);
        mapMessage.put("channelType", channelType);
        mapMessage.put("hasGift", hasGift);

        try {
            mapMessage.put("platforms", platformSet);
            mapMessage.put("channelTypes", AppChannelType.getAll());

            List<AuthApp> appList = OAuthServiceSngl.get().queryAuthApp(new QueryExpress()
                    .add(QueryCriterions.eq(AuthAppField.APPTYPE, AuthAppType.INTERNAL_CLIENT.getCode()))
                    .add(QueryCriterions.in(AuthAppField.PLATOFRM, new Integer[]{AppPlatform.ANDROID.getCode(), AppPlatform.CLIENT.getCode(), AppPlatform.IOS.getCode()}))
                    .add(QueryCriterions.eq(AuthAppField.VALIDSTATUS, ValidStatus.VALID.getCode()))
            );

            if (!CollectionUtil.isEmpty(appList)) {
                mapMessage.put("appList", appList);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/joymeapp/giftopen/creategiftopen", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView createAppInfo(@RequestParam(value = "appkey", required = true) String appKey,
                                      @RequestParam(value = "appname", required = false) String appName,
                                      @RequestParam(value = "platform", required = false) Integer platform,
                                      @RequestParam(value = "version", required = false) String version,
                                      @RequestParam(value = "channeltype", required = false) String channelType,
                                      @RequestParam(value = "hasgift", required = false) Boolean hasGift) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        mapMessage.put("appKey", appKey);
        mapMessage.put("appName", appName);
        mapMessage.put("platform", platform);
        mapMessage.put("version", version);
        mapMessage.put("channelType", channelType);
        mapMessage.put("hasGift", hasGift);

        try {
            AppInfo appInfo = new AppInfo();
            appInfo.setAppKey(appKey);
            appInfo.setAppName(appName);
            appInfo.setVersion(version);

            appInfo.setHasGift(hasGift);
            appInfo.setChannelType(AppChannelType.getByCode(channelType));
            appInfo.setAppPlatform(AppPlatform.getByCode(platform));
            appInfo.setCreateDate(new Date());
            appInfo.setCreateUserId(getCurrentUser().getUserid());
            appInfo.setCreateIp(getIp());

            appInfo.setAppInfoType(AppInfoType.GAME);
            appInfo.setIsCommplete(false);
            appInfo.setIsSearch(false);
            appInfo.setPackageName("");
            appInfo.setRecommend(0);

            appInfo = JoymeAppConfigServiceSngl.get().createAppInfo(appInfo);
            if (appInfo != null) {
                //addLog
                ToolsLog log = new ToolsLog();

                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.CREATE_JOYMEAPP_INFO);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("create app info:" + appInfo);

                addLog(log);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("redirect:/joymeapp/giftopen/list", mapMessage);
        }
        return new ModelAndView("redirect:/joymeapp/giftopen/list?appkey=" + appKey);
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyPage(@RequestParam(value = "infoid", required = false) Long infoId,
                                   @RequestParam(value = "appkey", required = false) String appKey,
                                   @RequestParam(value = "appname", required = false) String appName,
                                   @RequestParam(value = "platform", required = false) Integer platform,
                                   @RequestParam(value = "version", required = false) String version,
                                   @RequestParam(value = "channeltype", required = false) String channelType,
                                   @RequestParam(value = "hasgift", required = false) Boolean hasGift) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        mapMessage.put("appKey", appKey);
        mapMessage.put("appName", appName);
        mapMessage.put("platform", platform);
        mapMessage.put("version", version);
        mapMessage.put("channelType", channelType);
        mapMessage.put("hasGift", hasGift);

        try {
            mapMessage.put("platforms", platformSet);
            mapMessage.put("channelTypes", AppChannelType.getAll());

            List<AuthApp> appList = OAuthServiceSngl.get().queryAuthApp(new QueryExpress()
                    .add(QueryCriterions.eq(AuthAppField.APPTYPE, AuthAppType.INTERNAL_CLIENT.getCode()))
                    .add(QueryCriterions.in(AuthAppField.PLATOFRM, new Integer[]{AppPlatform.ANDROID.getCode(), AppPlatform.CLIENT.getCode(), AppPlatform.IOS.getCode()}))
                    .add(QueryCriterions.eq(AuthAppField.VALIDSTATUS, ValidStatus.VALID.getCode()))
            );

            if (!CollectionUtil.isEmpty(appList)) {
                mapMessage.put("appList", appList);
            }

            AppInfo appInfo = JoymeAppConfigServiceSngl.get().getAppInfo(new QueryExpress().add(QueryCriterions.eq(AppInfoField.INFO_ID, infoId)));
            if (appInfo != null) {
                mapMessage.put("appInfo", appInfo);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/joymeapp/giftopen/modifygiftopen", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modifyAppInfo(@RequestParam(value = "infoid", required = false) Long infoId,
                                      @RequestParam(value = "appkey", required = false) String appKey,
                                      @RequestParam(value = "appname", required = false) String appName,
                                      @RequestParam(value = "platform", required = false) Integer platform,
                                      @RequestParam(value = "version", required = false) String version,
                                      @RequestParam(value = "channeltype", required = false) String channelType,
                                      @RequestParam(value = "hasgift", required = false) Boolean hasGift) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        mapMessage.put("appKey", appKey);
        mapMessage.put("appName", appName);
        mapMessage.put("platform", platform);
        mapMessage.put("version", version);
        mapMessage.put("channelType", channelType);
        mapMessage.put("hasGift", hasGift);

        try {
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(AppInfoField.APP_KEY, appKey);
            updateExpress.set(AppInfoField.APP_NAME, appName);
            updateExpress.set(AppInfoField.PLATFORM, platform);
            updateExpress.set(AppInfoField.VERSION, version);
            updateExpress.set(AppInfoField.CHANNEL_TYPE, channelType);
            updateExpress.set(AppInfoField.HAS_GIFT, hasGift);
            updateExpress.set(AppInfoField.MODIFY_DATE, new Date());
            updateExpress.set(AppInfoField.MODIFY_IP, getIp());
            updateExpress.set(AppInfoField.MODIFY_USERID, getCurrentUser().getUserid());

            boolean bool = JoymeAppConfigServiceSngl.get().modifyAppInfo(new QueryExpress().add(QueryCriterions.eq(AppInfoField.INFO_ID, infoId)), updateExpress);
            if (bool) {
                //addLog
                ToolsLog log = new ToolsLog();

                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.MODIFY_JOYMEAPP_INFO);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("modify app info:" + infoId);

                addLog(log);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("redirect:/joymeapp/giftopen/list", mapMessage);
        }
        return new ModelAndView("redirect:/joymeapp/giftopen/list?appkey=" + appKey);
    }

    @RequestMapping(value = "/remove")
    public ModelAndView deleteMessage(@RequestParam(value = "infoid", required = false) Long infoId,
                                      @RequestParam(value = "appkey", required = false) String appKey) {
        try {

            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(AppInfoField.REMOVE_STATUS, ActStatus.ACTED.getCode());
            updateExpress.set(AppInfoField.MODIFY_DATE, new Date());
            updateExpress.set(AppInfoField.MODIFY_IP, getIp());
            updateExpress.set(AppInfoField.MODIFY_USERID, getCurrentUser().getUserid());

            boolean bool = JoymeAppConfigServiceSngl.get().modifyAppInfo(new QueryExpress().add(QueryCriterions.eq(AppInfoField.INFO_ID, infoId)), updateExpress);
            if (bool) {
                ToolsLog log = new ToolsLog();

                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.MODIFY_JOYMEAPP_INFO);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("modify app info:" + infoId);

                addLog(log);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
        }
        return new ModelAndView("redirect:/joymeapp/giftopen/list?appkey=" + appKey);
    }

    @RequestMapping(value = "/recover")
    public ModelAndView revocer(@RequestParam(value = "infoid", required = false) Long infoId,
                                @RequestParam(value = "appkey", required = false) String appKey) {
        try {

            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(AppInfoField.REMOVE_STATUS, ActStatus.UNACT.getCode());
            updateExpress.set(AppInfoField.MODIFY_DATE, new Date());
            updateExpress.set(AppInfoField.MODIFY_IP, getIp());
            updateExpress.set(AppInfoField.MODIFY_USERID, getCurrentUser().getUserid());

            boolean bool = JoymeAppConfigServiceSngl.get().modifyAppInfo(new QueryExpress().add(QueryCriterions.eq(AppInfoField.INFO_ID, infoId)), updateExpress);
            if (bool) {
                ToolsLog log = new ToolsLog();

                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.MODIFY_JOYMEAPP_INFO);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("modify app info:" + infoId);

                addLog(log);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
        }
        return new ModelAndView("redirect:/joymeapp/giftopen/list?appkey=" + appKey);
    }

}
