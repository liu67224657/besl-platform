package com.enjoyf.webapps.tools.webpage.controller.joymeapp;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.joymeapp.config.AppConfig;
import com.enjoyf.platform.service.joymeapp.config.AppConfigField;
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
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
@RequestMapping(value = "/joymeapp/version")
public class AppVersionController extends ToolsBaseController {

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,      //数据库记录索引
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                             @RequestParam(value = "appkey", required = false) String appKey,
                             @RequestParam(value = "platform", required = false) Integer platform,
                             @RequestParam(value = "version", required = false) String version,
                             @RequestParam(value = "channel", required = false) String channel,
                             @RequestParam(value = "enterprise", required = false) Integer enterprise
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("appKey", appKey);
        mapMessage.put("platform", platform);
        mapMessage.put("appVersion", version);
        mapMessage.put("channel", channel);
        mapMessage.put("enterprise", enterprise);

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
            return new ModelAndView("/joymeapp/version/versionlist", mapMessage);
        }

        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);

        QueryExpress queryExpress = new QueryExpress();
        if (!StringUtil.isEmpty(appKey)) {
            queryExpress.add(QueryCriterions.eq(AppDeploymentField.APP_KEY, appKey));
        }
        if (platform != null) {
            queryExpress.add(QueryCriterions.eq(AppDeploymentField.PLATFORM, platform));
        }
        if (enterprise != null) {
            queryExpress.add(QueryCriterions.eq(AppDeploymentField.ENTERPRISER, enterprise));
        }
        queryExpress.add(QueryCriterions.eq(AppDeploymentField.DEPLOYMENT_TYPE, AppDeploymentType.VERSION.getCode()));
        if (!StringUtil.isEmpty(version)) {
            queryExpress.add(QueryCriterions.eq(AppDeploymentField.TITLE, version));
        }
        if (!StringUtil.isEmpty(channel)) {
            queryExpress.add(QueryCriterions.eq(AppDeploymentField.CHANNEL, channel));
        }
        queryExpress.add(QuerySort.add(AppDeploymentField.CREATE_DATE, QuerySortOrder.DESC));
        try {
            PageRows<AppDeployment> pageRows = JoymeAppConfigServiceSngl.get().queryAppDeploymentByPage(queryExpress, pagination);

            mapMessage.put("list", pageRows.getRows());
            mapMessage.put("page", pageRows.getPage());
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/joymeapp/version/versionlist", mapMessage);
        }

        return new ModelAndView("/joymeapp/version/versionlist", mapMessage);
    }


    @RequestMapping(value = "/createpage")
    public ModelAndView createVersion(@RequestParam(value = "version", required = false) String version,
                                      @RequestParam(value = "appkey", required = false) String appKey,
                                      @RequestParam(value = "platform", required = false) Integer platform,
                                      @RequestParam(value = "channel", required = false) String channel,
                                      @RequestParam(value = "enterprise", required = false) Integer enterprise) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("appVersion", version);
        mapMessage.put("appKey", appKey);
        mapMessage.put("platform", platform);
        mapMessage.put("channel", channel);
        mapMessage.put("enterprise", enterprise);

        mapMessage.put("versionTypes", AppVersionUpdateType.getAll());
        mapMessage.put("appEnterpriserType", AppEnterpriserType.getAll());
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

            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(AppConfigField.APPKEY, appKey));
            queryExpress.add(QueryCriterions.eq(AppConfigField.PLATFORM, platform));
            queryExpress.add(QueryCriterions.eq(AppConfigField.ENTERPRISE, enterprise));

            List<AppConfig> list = JoymeAppConfigServiceSngl.get().queryAppConfig(queryExpress);
            if (!CollectionUtil.isEmpty(list)) {
                Set<AppChannelType> channelList = new HashSet<AppChannelType>();
                Set<String> versionList = new HashSet<String>();
                for (AppConfig config : list) {
                    if (config != null) {
                        versionList.add(config.getVersion());
                        if (!StringUtil.isEmpty(version)) {
                            if (version.equals(config.getVersion())) {
                                channelList.add(AppChannelType.getByCode(config.getChannel()));
                            }
                        }
                    }
                }
                mapMessage.put("versionList", versionList);
                mapMessage.put("channelList", channelList);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/joymeapp/version/createversion", mapMessage);
        }
        return new ModelAndView("/joymeapp/version/createversion", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "version", required = false) String version,
                               @RequestParam(value = "description", required = false) String description,
                               @RequestParam(value = "path", required = false) String path,
                               @RequestParam(value = "versiontype", required = false) Integer versionType,
                               @RequestParam(value = "appkey", required = false) String appKey,
                               @RequestParam(value = "platform", required = false) Integer platform,
                               @RequestParam(value = "channel", required = false) String channel,
                               @RequestParam(value = "enterprise", required = false) Integer enterprise
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            AppDeployment appDeployment = JoymeAppConfigServiceSngl.get().getAppDeployment(new QueryExpress()
                    .add(QueryCriterions.eq(AppDeploymentField.APP_KEY, appKey))
                    .add(QueryCriterions.eq(AppDeploymentField.PLATFORM, platform))
                    .add(QueryCriterions.eq(AppDeploymentField.TITLE, version))
                    .add(QueryCriterions.eq(AppDeploymentField.CHANNEL, channel))
                    .add(QueryCriterions.eq(AppDeploymentField.ENTERPRISER, enterprise))
                    .add(QueryCriterions.eq(AppDeploymentField.DEPLOYMENT_TYPE, AppDeploymentType.VERSION.getCode())));
            if (appDeployment == null) {
                appDeployment = new AppDeployment();
                appDeployment.setAppDeploymentType(AppDeploymentType.VERSION);
                appDeployment.setAppkey(appKey);
                appDeployment.setAppPlatform(AppPlatform.getByCode(platform));
                appDeployment.setDescription(description);
                appDeployment.setTitle(version);
                appDeployment.setPath(path);
                appDeployment.setAppVersionUpdateType(AppVersionUpdateType.getByCode(versionType));
                appDeployment.setChannel(AppChannelType.getByCode(channel));
                appDeployment.setCreateDate(new Date());
                appDeployment.setCreateIp(getIp());
                appDeployment.setCreateUserId(getCurrentUser().getUserid());
                appDeployment.setAppEnterpriserType(AppEnterpriserType.getByCode(enterprise));

                JoymeAppConfigServiceSngl.get().createAppDeployment(appDeployment);
            } else {
                UpdateExpress updateExpress = new UpdateExpress();
                updateExpress.set(AppDeploymentField.DESCRIPTION, description);
                updateExpress.set(AppDeploymentField.PATH, path);
                updateExpress.set(AppDeploymentField.UPDATE_TYPE, versionType);
                updateExpress.set(AppDeploymentField.MODIFY_DATE, new Date());
                updateExpress.set(AppDeploymentField.MODIFY_USERID, getCurrentUser().getUserid());
                updateExpress.set(AppDeploymentField.MODIFY_IP, getIp());
                updateExpress.set(AppDeploymentField.REMOVE_STATUS, ActStatus.UNACT.getCode());
                JoymeAppConfigServiceSngl.get().modifyAppDeployment(new QueryExpress()
                        .add(QueryCriterions.eq(AppDeploymentField.APP_KEY, appKey))
                        .add(QueryCriterions.eq(AppDeploymentField.PLATFORM, platform))
                        .add(QueryCriterions.eq(AppDeploymentField.TITLE, version))
                        .add(QueryCriterions.eq(AppDeploymentField.CHANNEL, channel))
                        .add(QueryCriterions.eq(AppDeploymentField.ENTERPRISER, enterprise))
                        .add(QueryCriterions.eq(AppDeploymentField.DEPLOYMENT_TYPE, AppDeploymentType.VERSION.getCode())), updateExpress);
            }

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("forward:/joymeapp/version/createpage", mapMessage);
        }
        return new ModelAndView("redirect:/joymeapp/version/list?appkey=" + appKey + "&platform=" + platform + "&version=" + version + "&channel=" + channel + "&enterprise=" + enterprise);
    }

    @RequestMapping(value = "/remove")
    public ModelAndView remove(@RequestParam(value = "appkey", required = false) String appKey,
                               @RequestParam(value = "platform", required = false) Integer platform,
                               @RequestParam(value = "version", required = false) String version,
                               @RequestParam(value = "channel", required = false) String channel,
                               @RequestParam(value = "enterprise", required = false) Integer enterprise,
                               @RequestParam(value = "vid", required = true) Long versionId,
                               @RequestParam(value = "offset", required = false, defaultValue = "0") Integer pageStartIndex) {
        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(AppDeploymentField.REMOVE_STATUS, ActStatus.ACTED.getCode());
        updateExpress.set(AppDeploymentField.MODIFY_DATE, new Date());
        updateExpress.set(AppDeploymentField.MODIFY_IP, getIp());
        updateExpress.set(AppDeploymentField.MODIFY_USERID, getCurrentUser().getUserid());
        try {
            JoymeAppConfigServiceSngl.get().modifyAppDeployment(new QueryExpress().add(QueryCriterions.eq(AppDeploymentField.DEPLOYMENT_ID, versionId)), updateExpress);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e:", e);
        }
        return new ModelAndView("redirect:/joymeapp/version/list?pager.offset=" + pageStartIndex + "&appkey=" + appKey + "&platform=" + platform + "&version=" + version + "&channel=" + channel + "&enterprise=" + enterprise);
    }

    @RequestMapping(value = "/recover")
    public ModelAndView recover(@RequestParam(value = "appkey", required = false) String appKey,
                                @RequestParam(value = "platform", required = false) Integer platform,
                                @RequestParam(value = "version", required = false) String version,
                                @RequestParam(value = "channel", required = false) String channel,
                                @RequestParam(value = "enterprise", required = false) Integer enterprise,
                                @RequestParam(value = "vid", required = true) Long versionId,
                                @RequestParam(value = "offset", required = false, defaultValue = "0") Integer pageStartIndex) {
        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(AppDeploymentField.REMOVE_STATUS, ActStatus.UNACT.getCode());
        updateExpress.set(AppDeploymentField.MODIFY_DATE, new Date());
        updateExpress.set(AppDeploymentField.MODIFY_IP, getIp());
        updateExpress.set(AppDeploymentField.MODIFY_USERID, getCurrentUser().getUserid());
        try {
            JoymeAppConfigServiceSngl.get().modifyAppDeployment(new QueryExpress().add(QueryCriterions.eq(AppDeploymentField.DEPLOYMENT_ID, versionId)), updateExpress);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e:", e);
        }
        return new ModelAndView("redirect:/joymeapp/version/list?pager.offset=" + pageStartIndex + "&appkey=" + appKey + "&platform=" + platform + "&version=" + version + "&channel=" + channel + "&enterprise=" + enterprise);
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyVersion(@RequestParam(value = "version", required = false) String version,
                                      @RequestParam(value = "appkey", required = false) String appKey,
                                      @RequestParam(value = "platform", required = false) Integer platform,
                                      @RequestParam(value = "channel", required = false) String channel,
                                      @RequestParam(value = "enterprise", required = false) Integer enterprise,
                                      @RequestParam(value = "vid", required = true) Long versionId,
                                      @RequestParam(value = "offset", required = false, defaultValue = "0") Integer pageStartIndex) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        mapMessage.put("versionTypes", AppVersionUpdateType.getAll());
        mapMessage.put("appEnterpriserType", AppEnterpriserType.getAll());
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

            AppDeployment appDeployment = JoymeAppConfigServiceSngl.get().getAppDeployment(new QueryExpress().add(QueryCriterions.eq(AppDeploymentField.DEPLOYMENT_ID, versionId)));
            if (appDeployment == null) {
                return new ModelAndView("redirect:/joymeapp/version/list?pager.offset=" + pageStartIndex + "&appkey=" + appKey + "&platform=" + platform + "&version=" + version + "&channel=" + channel + "&enterprise=" + enterprise);
            }
            mapMessage.put("appVersion", appDeployment);

            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(AppConfigField.APPKEY, appDeployment.getAppkey()));
            queryExpress.add(QueryCriterions.eq(AppConfigField.PLATFORM, appDeployment.getAppPlatform().getCode()));
            queryExpress.add(QueryCriterions.eq(AppConfigField.ENTERPRISE, appDeployment.getAppEnterpriserType().getCode()));

            List<AppConfig> list = JoymeAppConfigServiceSngl.get().queryAppConfig(queryExpress);
            if (!CollectionUtil.isEmpty(list)) {
                Set<AppChannelType> channelList = new HashSet<AppChannelType>();
                Set<String> versionList = new HashSet<String>();
                for (AppConfig config : list) {
                    if (config != null) {
                        if (!StringUtil.isEmpty(version)) {
                            if (version.equals(config.getVersion())) {
                                channelList.add(AppChannelType.getByCode(config.getChannel()));
                            }
                        }
                        versionList.add(config.getVersion());
                    }
                }
                mapMessage.put("versionList", versionList);
                mapMessage.put("channelList", channelList);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e:", e);
            return new ModelAndView("redirect:/joymeapp/version/list?pager.offset=" + pageStartIndex + "&appkey=" + appKey + "&platform=" + platform + "&version=" + version + "&channel=" + channel);
        }
        return new ModelAndView("/joymeapp/version/modifyversion", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView preModifyApp(@RequestParam(value = "vid", required = true) Long versionId,
                                     @RequestParam(value = "offset", required = false, defaultValue = "0") Integer pageStartIndex,
                                     @RequestParam(value = "version", required = false) String version,
                                     @RequestParam(value = "description", required = false) String description,
                                     @RequestParam(value = "path", required = false) String path,
                                     @RequestParam(value = "versiontype", required = false) Integer versionType,
                                     @RequestParam(value = "appkey", required = false) String appKey,
                                     @RequestParam(value = "platform", required = false) Integer platform,
                                     @RequestParam(value = "channel", required = false) String channel,
                                     @RequestParam(value = "enterprise", required = false) Integer enterprise) {

        try {
            AppDeployment appDeployment = JoymeAppConfigServiceSngl.get().getAppDeployment(new QueryExpress()
                    .add(QueryCriterions.eq(AppDeploymentField.APP_KEY, appKey))
                    .add(QueryCriterions.eq(AppDeploymentField.PLATFORM, platform))
                    .add(QueryCriterions.eq(AppDeploymentField.TITLE, version))
                    .add(QueryCriterions.eq(AppDeploymentField.CHANNEL, channel))
                    .add(QueryCriterions.eq(AppDeploymentField.ENTERPRISER, enterprise))
                    .add(QueryCriterions.eq(AppDeploymentField.DEPLOYMENT_TYPE, AppDeploymentType.VERSION.getCode())));
            if (appDeployment == null) {
                UpdateExpress updateExpress = new UpdateExpress();
                updateExpress.set(AppDeploymentField.TITLE, version);
                updateExpress.set(AppDeploymentField.DESCRIPTION, description);
                updateExpress.set(AppDeploymentField.PATH, path);
                updateExpress.set(AppDeploymentField.UPDATE_TYPE, versionType);
                updateExpress.set(AppDeploymentField.APP_KEY, appKey);
                updateExpress.set(AppDeploymentField.PLATFORM, platform);
                updateExpress.set(AppDeploymentField.MODIFY_DATE, new Date());
                updateExpress.set(AppDeploymentField.MODIFY_IP, getIp());
                updateExpress.set(AppDeploymentField.MODIFY_USERID, getCurrentUser().getUserid());
                updateExpress.set(AppDeploymentField.CHANNEL, channel);
                updateExpress.set(AppDeploymentField.ENTERPRISER, enterprise);
                updateExpress.set(AppDeploymentField.REMOVE_STATUS, ActStatus.UNACT.getCode());
                JoymeAppConfigServiceSngl.get().modifyAppDeployment(new QueryExpress().add(QueryCriterions.eq(AppDeploymentField.DEPLOYMENT_ID, versionId)), updateExpress);
            } else {
                UpdateExpress updateExpress = new UpdateExpress();
                updateExpress.set(AppDeploymentField.DESCRIPTION, description);
                updateExpress.set(AppDeploymentField.PATH, path);
                updateExpress.set(AppDeploymentField.UPDATE_TYPE, versionType);
                updateExpress.set(AppDeploymentField.MODIFY_DATE, new Date());
                updateExpress.set(AppDeploymentField.MODIFY_USERID, getCurrentUser().getUserid());
                updateExpress.set(AppDeploymentField.MODIFY_IP, getIp());
                updateExpress.set(AppDeploymentField.REMOVE_STATUS, ActStatus.UNACT.getCode());
                JoymeAppConfigServiceSngl.get().modifyAppDeployment(new QueryExpress()
                        .add(QueryCriterions.eq(AppDeploymentField.APP_KEY, appKey))
                        .add(QueryCriterions.eq(AppDeploymentField.PLATFORM, platform))
                        .add(QueryCriterions.eq(AppDeploymentField.TITLE, version))
                        .add(QueryCriterions.eq(AppDeploymentField.CHANNEL, channel))
                        .add(QueryCriterions.eq(AppDeploymentField.ENTERPRISER, enterprise))
                        .add(QueryCriterions.eq(AppDeploymentField.DEPLOYMENT_TYPE, AppDeploymentType.VERSION.getCode())), updateExpress);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return new ModelAndView("redirect:/joymeapp/version/modifypage?vid=" + versionId + "&offset=" + pageStartIndex + "&appkey=" + appKey + "&platform=" + platform + "&version=" + version + "&channel=" + channel + "&enterprise=" + enterprise);
        }
        return new ModelAndView("redirect:/joymeapp/version/list?pager.offset=" + pageStartIndex + "&appkey=" + appKey + "&platform=" + platform + "&version=" + version + "&channel=" + channel + "&enterprise=" + enterprise);
    }

    @ResponseBody
    @RequestMapping(value = "/getversion")
    public String getVersion(@RequestParam(value = "appkey", required = false) String appKey,
                             @RequestParam(value = "platform", required = false) Integer platform,
                             @RequestParam(value = "enterprise", required = false) Integer enterprise,
                             @RequestParam(value = "version", required = false) String version) {
        if (StringUtil.isEmpty(appKey)) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("rs", ResultCodeConstants.SUCCESS.getCode());
            jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
            jsonObject.put("result", new JSONObject());
            return jsonObject.toString();
        }

        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(AppConfigField.APPKEY, appKey));
        queryExpress.add(QueryCriterions.eq(AppConfigField.PLATFORM, platform));
        queryExpress.add(QueryCriterions.eq(AppConfigField.ENTERPRISE, enterprise));
        if (!StringUtil.isEmpty(version)) {
            queryExpress.add(QueryCriterions.eq(AppConfigField.VERSION, version));
        }
        try {
            List<AppConfig> list = JoymeAppConfigServiceSngl.get().queryAppConfig(queryExpress);
            if (!CollectionUtil.isEmpty(list)) {
                Set<AppChannelType> channelList = new HashSet<AppChannelType>();
                Set<String> versionList = new HashSet<String>();
                for (AppConfig config : list) {
                    if (config != null) {
                        channelList.add(AppChannelType.getByCode(config.getChannel()));
                        versionList.add(config.getVersion());
                    }
                }
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("rs", ResultCodeConstants.SUCCESS.getCode());
                jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
                JSONObject result = new JSONObject();
                result.put("versionList", versionList);
                result.put("channelList", channelList);
                jsonObject.put("result", result);
                return jsonObject.toString();
            }
        } catch (ServiceException e) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("rs", ResultCodeConstants.ERROR.getCode());
            jsonObject.put("msg", ResultCodeConstants.ERROR.getMsg());
            jsonObject.put("result", new JSONObject());
            return jsonObject.toString();
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rs", ResultCodeConstants.SUCCESS.getCode());
        jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
        jsonObject.put("result", new JSONObject());
        return jsonObject.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/getapp")
    public String getApp(@RequestParam(value = "appname", required = false) String appName) {
        if (StringUtil.isEmpty(appName)) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("rs", ResultCodeConstants.SUCCESS.getCode());
            jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
            jsonObject.put("result", new JSONObject());
            return jsonObject.toString();
        }

        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.like(AuthAppField.APPNAME, "%" + appName + "%"));
        queryExpress.add(QueryCriterions.eq(AuthAppField.APPTYPE, AuthAppType.INTERNAL_CLIENT.getCode()));
        queryExpress.add(QueryCriterions.in(AuthAppField.PLATOFRM, new Integer[]{AppPlatform.ANDROID.getCode(), AppPlatform.CLIENT.getCode(), AppPlatform.IOS.getCode()}));
        queryExpress.add(QueryCriterions.eq(AuthAppField.VALIDSTATUS, ValidStatus.VALID.getCode()));
        queryExpress.add(QuerySort.add(AuthAppField.CREATEDATE, QuerySortOrder.DESC));
        try {
            List<AuthApp> list = OAuthServiceSngl.get().queryAuthApp(queryExpress);
            if (!CollectionUtil.isEmpty(list)) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("rs", ResultCodeConstants.SUCCESS.getCode());
                jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
                JSONObject result = new JSONObject();
                result.put("appList", list);
                jsonObject.put("result", result);
                return jsonObject.toString();
            }
        } catch (ServiceException e) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("rs", ResultCodeConstants.ERROR.getCode());
            jsonObject.put("msg", ResultCodeConstants.ERROR.getMsg());
            jsonObject.put("result", new JSONObject());
            return jsonObject.toString();
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rs", ResultCodeConstants.SUCCESS.getCode());
        jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
        jsonObject.put("result", new JSONObject());
        return jsonObject.toString();
    }

}
