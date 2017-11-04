package com.enjoyf.webapps.tools.webpage.controller.joymeapp.config;

import com.enjoyf.platform.cloudfile.BucketInfo;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.joymeapp.AppChannelType;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.joymeapp.config.AppConfig;
import com.enjoyf.platform.service.joymeapp.config.AppConfigField;
import com.enjoyf.platform.service.joymeapp.config.AppConfigInfo;
import com.enjoyf.platform.service.joymeappconfig.JoymeAppConfigServiceSngl;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.AuthAppField;
import com.enjoyf.platform.service.oauth.AuthAppType;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.oauth.renren.api.client.utils.Md5Utils;
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
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 15-3-4
 * Time: 上午10:27
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/joymeapp/config")
public class AppConfigController extends ToolsBaseController {

    private static Set<AppPlatform> platformSet = new HashSet<AppPlatform>();

    static {
        platformSet.add(AppPlatform.ANDROID);
        platformSet.add(AppPlatform.IOS);
    }

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                             @RequestParam(value = "appkey", required = false) String appKey,
                             @RequestParam(value = "platform", required = false) Integer platform,
                             @RequestParam(value = "version", required = false) String version,
                             @RequestParam(value = "channel", required = false) String channel,
                             @RequestParam(value = "enterprise", required = false) String enterprise) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("appKey", appKey);
        mapMessage.put("platform", platform);
        mapMessage.put("appversion", version);
        mapMessage.put("channel", channel);
        mapMessage.put("enterprise", enterprise);

        mapMessage.put("platformSet", platformSet);
        mapMessage.put("channelSet", AppChannelType.getAll());

        List<AuthApp> appList = null;
        try {
            appList = OAuthServiceSngl.get().queryAuthApp(
                    new QueryExpress()
                            .add(QueryCriterions.eq(AuthAppField.APPTYPE, AuthAppType.INTERNAL_CLIENT.getCode()))
                            .add(QueryCriterions.in(AuthAppField.PLATOFRM, new Integer[]{AppPlatform.ANDROID.getCode(), AppPlatform.CLIENT.getCode(), AppPlatform.IOS.getCode()}))
                            .add(QueryCriterions.eq(AuthAppField.VALIDSTATUS, ValidStatus.VALID.getCode()))
                            .add(QuerySort.add(AuthAppField.CREATEDATE, QuerySortOrder.DESC))
            );
            mapMessage.put("appList", appList);

            QueryExpress queryExpress = new QueryExpress();
            if (!StringUtil.isEmpty(appKey)) {
                queryExpress.add(QueryCriterions.eq(AppConfigField.APPKEY, appKey));
            }
            if (platform != null) {
                queryExpress.add(QueryCriterions.eq(AppConfigField.PLATFORM, platform));
            }
            if (enterprise != null && !StringUtil.isEmpty(enterprise)) {
                queryExpress.add(QueryCriterions.eq(AppConfigField.ENTERPRISE, Integer.valueOf(enterprise)));
            }
            if (!StringUtil.isEmpty(version)) {
                queryExpress.add(QueryCriterions.eq(AppConfigField.VERSION, version));
            }
            if (!StringUtil.isEmpty(channel)) {
                queryExpress.add(QueryCriterions.eq(AppConfigField.CHANNEL, channel));
            }
            queryExpress.add(QuerySort.add(AppConfigField.CREATEDATE, QuerySortOrder.DESC));

            int curPage = (pageStartIndex / pageSize) + 1;
            Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);

            PageRows<AppConfig> pageRows = JoymeAppConfigServiceSngl.get().queryAppConfigByPage(queryExpress, pagination);
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                mapMessage.put("list", pageRows.getRows());
                mapMessage.put("page", pageRows.getPage());
            }
        } catch (Exception e) {
            GAlerter.lab("AppConfigController occur Exception.e", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/joymeapp/config/configlist", mapMessage);
        }
        return new ModelAndView("/joymeapp/config/configlist", mapMessage);
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modify(@RequestParam(value = "configid", required = false) String configId,
                               @RequestParam(value = "appkey", required = false) String appKey,
                               @RequestParam(value = "platform", required = false) String platform,
                               @RequestParam(value = "version", required = false) String version,
                               @RequestParam(value = "channel", required = false) String channel,
                               @RequestParam(value = "enterprise", required = false) Integer enterprise) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            AppConfig appConfig = JoymeAppConfigServiceSngl.get().getAppConfig(configId);
            if (appConfig == null) {
                return new ModelAndView("redirect:/joymeapp/config/list?appkey=" + appKey + "&platform=" + platform + "&version=" + version + "&channel=" + channel + "enterprise=" + enterprise);
            }
            mapMessage.put("config", appConfig);
            if (appConfig != null && !StringUtil.isEmpty(appConfig.getAppKey())) {
                AuthApp app = OAuthServiceSngl.get().getApp(appConfig.getAppKey());
                mapMessage.put("app", app);
            }
            writeToolsLog(LogOperType.MODIFY_APP_CONFIG_PAGE, "查看开关项配置：" + configId);
        } catch (Exception e) {
            GAlerter.lab("AppConfigController occur Exception.e", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/joymeapp/config/modifyconfig", mapMessage);
        }
        return new ModelAndView("/joymeapp/config/modifyconfig", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView list(@RequestParam(value = "configid", required = false) String configId,
                             @RequestParam(value = "appkey", required = false) String appKey,
                             @RequestParam(value = "platform", required = false) String platform,
                             @RequestParam(value = "version", required = false) String version,
                             @RequestParam(value = "channel", required = false) String channel,
                             @RequestParam(value = "enterprise", required = false) Integer enterprise,
                             @RequestParam(value = "shake_open", required = false, defaultValue = "false") String shake_open,
                             @RequestParam(value = "defad_url", required = false) String defad_url,
                             @RequestParam(value = "gift_open", required = false, defaultValue = "false") String gift_open,
                             @RequestParam(value = "reddot_interval", required = false, defaultValue = "30") String reddot_interval,
                             @RequestParam(value = "shake_version", required = false, defaultValue = "0") String shake_version,
                             @RequestParam(value = "appsecret", required = false) String appSecret,
                             @RequestParam(value = "bucket", required = false) String bucket) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            AppConfigInfo info = new AppConfigInfo();
            info.setShake_open(shake_open);
            info.setDefad_url(defad_url);
            info.setGift_open(gift_open);
            info.setReddot_interval(reddot_interval);
            info.setShake_version(shake_version);
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(AppConfigField.APPINFO, info.toJsonStr());
            updateExpress.set(AppConfigField.MODIFYDATE, new Date());
            updateExpress.set(AppConfigField.MODIFYUSERID, getCurrentUser().getUserid());

            if (!StringUtil.isEmpty(bucket) && BucketInfo.getByCode(bucket) != null) {
                updateExpress.set(AppConfigField.BUCKET, bucket);
            } else {
                GAlerter.lab(this.getClass().getName() + " illegel bucket:" + bucket);
            }

            if (!StringUtil.isEmpty(appSecret)) {
                updateExpress.set(AppConfigField.APPSECRET, appSecret);
            }
            boolean bool = JoymeAppConfigServiceSngl.get().modifyAppConfig(configId, updateExpress);
            if (bool) {
                writeToolsLog(LogOperType.MODIFY_APP_CONFIG, "开关项配置修改：" + configId);
            }
        } catch (Exception e) {
            GAlerter.lab("AppConfigController occur Exception.e", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("redirect:/joymeapp/config/list?appkey=" + appKey + "&platform=" + platform + "&version=" + version + "&channel=" + channel + "&enterprise=" + enterprise);
        }
        return new ModelAndView("redirect:/joymeapp/config/list?appkey=" + appKey + "&platform=" + platform + "&version=" + version + "&channel=" + channel + "&enterprise=" + enterprise);
    }

    @RequestMapping(value = "/delete")
    public ModelAndView delete(@RequestParam(value = "configid", required = false) String configId,
                               @RequestParam(value = "appkey", required = false) String appKey,
                               @RequestParam(value = "platform", required = false) String platform,
                               @RequestParam(value = "version", required = false) String version,
                               @RequestParam(value = "channel", required = false) String channel,
                               @RequestParam(value = "enterprise", required = false) String enterprise) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            if (StringUtil.isEmpty(enterprise) || enterprise.equals("null")) {
                enterprise = "";
            }
            AppConfig appConfig = JoymeAppConfigServiceSngl.get().getAppConfig(configId);
            if (appConfig == null) {
                return new ModelAndView("redirect:/joymeapp/config/list?appkey=" + appKey + "&platform=" + platform + "&version=" + version + "&channel=" + channel + "enterprise=" + enterprise);
            }
            boolean bool = JoymeAppConfigServiceSngl.get().deleteAppConfig(configId);
            if (bool) {
                writeToolsLog(LogOperType.MODIFY_APP_CONFIG_PAGE, "删除开关项配置configId:" + configId + ",appkey:" + appKey + ",platform:" + platform + ",version:" + version + ",channel:" + channel + ",ent:" + enterprise);
            }
        } catch (Exception e) {
            GAlerter.lab("AppConfigController occur Exception.e", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/joymeapp/config/modifyconfig", mapMessage);
        }
        return new ModelAndView("redirect:/joymeapp/config/list?appkey=" + appKey + "&platform=" + platform + "&version=" + version + "&channel=" + channel + "&enterprise=" + enterprise);
    }

    /**
     * 密钥权限
     *
     * @return
     */
    @RequestMapping(value = "/secret")
    public ModelAndView secret() {
        return new ModelAndView();
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
                        if (AppChannelType.getByCode(config.getChannel()) != null) {
                            channelList.add(AppChannelType.getByCode(config.getChannel()));
                        }
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
