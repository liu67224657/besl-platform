package com.enjoyf.webapps.tools.webpage.controller.joymeapp.shake;

import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.joymeapp.config.*;
import com.enjoyf.platform.service.joymeappconfig.JoymeAppConfigServiceSngl;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/3/31
 * Description:
 */
@Controller
@RequestMapping(value = "/shake/type")
class ShakeTypeController extends ToolsBaseController {
    private static Set<AppPlatform> platformSet = new HashSet<AppPlatform>();

    static {
        platformSet.add(AppPlatform.ANDROID);
        platformSet.add(AppPlatform.IOS);
    }


    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "configid", required = false) String configId,
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

            //todo
//            writeToolsLog(LogOperType.MODIFY_APP_CONFIG_PAGE, "查看开关项配置：" + configId);
        } catch (Exception e) {
            GAlerter.lab("AppConfigController occur Exception.e", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/shake/typelist", mapMessage);
        }
        return new ModelAndView("/shake/typelist", mapMessage);
    }


    @RequestMapping(value = "/createpage")
    public ModelAndView modify(@RequestParam(value = "configid", required = false) String configId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            AppConfig appConfig = JoymeAppConfigServiceSngl.get().getAppConfig(configId);
            if (appConfig == null) {
                //todo
            }
            mapMessage.put("config", appConfig);
            if (appConfig != null && !StringUtil.isEmpty(appConfig.getAppKey())) {
                AuthApp app = OAuthServiceSngl.get().getApp(appConfig.getAppKey());
                mapMessage.put("app", app);
            }

            mapMessage.put("types", ShakeType.getAll());

            //todo
//            writeToolsLog(LogOperType.MODIFY_APP_CONFIG_PAGE, "查看开关项配置：" + configId);
        } catch (Exception e) {
            GAlerter.lab("AppConfigController occur Exception.e", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/shake/typecreate", mapMessage);
        }
        return new ModelAndView("/shake/typecreate", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView list(@RequestParam(value = "configid", required = false) String configId,
                             @RequestParam(value = "appkey", required = false) String appKey,
                             @RequestParam(value = "platform", required = false) String platform,
                             @RequestParam(value = "version", required = false) String version,
                             @RequestParam(value = "channel", required = false) String channel,
                             @RequestParam(value = "enterprise", required = false) Integer enterprise,

                             @RequestParam(value = "buttontext", required = false) String buttontext,
                             @RequestParam(value = "title", required = false) String title,
                             @RequestParam(value = "shaketype", required = false) Integer shaketype,
                             @RequestParam(value = "shaketimes", required = false) Integer shaketimes,
                             @RequestParam(value = "timelimit", required = false, defaultValue = "false") Boolean timelimit,
                             @RequestParam(value = "begintime", required = false) String begintime,
                             @RequestParam(value = "endtime", required = false) String endtime,
                             @RequestParam(value = "min_range", required = false) Integer min_range,
                             @RequestParam(value = "max_range", required = false) Integer max_range,
                             @RequestParam(value = "tag", required = false) String tag,
                             @RequestParam(value = "tagbegintime", required = false) String tagbegintime,
                             @RequestParam(value = "tagendtime", required = false) String tagendtime


    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            AppConfig appConfig = JoymeAppConfigServiceSngl.get().getAppConfig(configId);
            if (appConfig == null) {
                //todo
            }
            Map<Integer, ShakeConfig> configs = appConfig.getInfo().getShakeconfigs();

            ShakeConfig shakeConfig = new ShakeConfig();
            shakeConfig.setButtontext(buttontext);
            shakeConfig.setTitle(title);
            shakeConfig.setShakeType(shaketype);
            shakeConfig.setShaketimes(shaketimes);
            shakeConfig.setTimelimit(timelimit);
            if (!StringUtil.isEmpty(begintime) && !StringUtil.isEmpty(endtime)) {
                shakeConfig.setBegintime(DateUtil.formatStringToDate(begintime, "yyyy-MM-dd HH:mm:ss").getTime());
                shakeConfig.setEndtime(DateUtil.formatStringToDate(endtime, "yyyy-MM-dd HH:mm:ss").getTime());
            }

            shakeConfig.setShakeRange(new ShakeRange(min_range, max_range));
            shakeConfig.setTag(tag);
            if (!StringUtil.isEmpty(tagbegintime) && !StringUtil.isEmpty(tagendtime)) {
                shakeConfig.setBegintime(DateUtil.formatStringToDate(tagbegintime, "yyyy-MM-dd HH:mm:ss").getTime());
                shakeConfig.setEndtime(DateUtil.formatStringToDate(tagendtime, "yyyy-MM-dd HH:mm:ss").getTime());
            }
            configs.put(shaketype, shakeConfig);

            boolean bool = JoymeAppConfigServiceSngl.get().modifyAppConfig(configId, new UpdateExpress().set(AppConfigField.APPINFO, appConfig.getInfo().toJsonStr())
                    .set(AppConfigField.MODIFYDATE, new Date())
                    .set(AppConfigField.MODIFYUSERID, getCurrentUser().getUserid()));

            //todo
//            writeToolsLog(LogOperType.MODIFY_APP_CONFIG, "开关项配置修改：" + configId);
        } catch (Exception e) {
            GAlerter.lab("AppConfigController occur Exception.e", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("redirect:/joymeapp/shake/type/list?configid=" + configId);
        }
        return new ModelAndView("redirect:/joymeapp/shake/type/list?configid=" + configId);
    }


    @RequestMapping(value = "/remove")
    public ModelAndView list(@RequestParam(value = "configid", required = false) String configId,
                             @RequestParam(value = "type", required = false) Integer type


    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            AppConfig appConfig = JoymeAppConfigServiceSngl.get().getAppConfig(configId);
            if (appConfig == null) {
                //todo
            }
            Map<Integer, ShakeConfig> configs = appConfig.getInfo().getShakeconfigs();

            if (configs.containsKey(type)) {
                configs.remove(type);
                boolean bool = JoymeAppConfigServiceSngl.get().modifyAppConfig(configId, new UpdateExpress().set(AppConfigField.APPINFO, appConfig.getInfo().toJsonStr())
                        .set(AppConfigField.MODIFYDATE, new Date())
                        .set(AppConfigField.MODIFYUSERID, getCurrentUser().getUserid()));
            }

            //todo
//            writeToolsLog(LogOperType.MODIFY_APP_CONFIG, "开关项配置修改：" + configId);
        } catch (Exception e) {
            GAlerter.lab("AppConfigController occur Exception.e", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("redirect:/shake/type/list?configid=" + configId);
        }
        return new ModelAndView("redirect:/shake/type/list?configid=" + configId);

    }


}
