package com.enjoyf.webapps.tools.webpage.controller.joymeapp.shake;

import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.gameres.gamedb.GameDB;
import com.enjoyf.platform.service.gameres.gamedb.GameDBField;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.joymeapp.config.AppConfig;
import com.enjoyf.platform.service.joymeapp.config.ShakeItem;
import com.enjoyf.platform.service.joymeapp.config.ShakeType;
import com.enjoyf.platform.service.joymeappconfig.JoymeAppConfigServiceSngl;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryCriterions;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryExpress;
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
@RequestMapping(value = "/shake/pool")
class ShakePoolController extends ToolsBaseController {
    private static Set<AppPlatform> platformSet = new HashSet<AppPlatform>();

    static {
        platformSet.add(AppPlatform.ANDROID);
        platformSet.add(AppPlatform.IOS);
    }

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "configid", required = false) String configId,
                             @RequestParam(value = "type", required = false) Integer type,
                             @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            AppConfig appConfig = JoymeAppConfigServiceSngl.get().getAppConfig(configId);
            if (appConfig == null) {
                return new ModelAndView("redirect:/shake/type/list?configid=" + configId);
            }
            mapMessage.put("config", appConfig);
            mapMessage.put("type", type);
            if (appConfig != null && !StringUtil.isEmpty(appConfig.getAppKey())) {
                AuthApp app = OAuthServiceSngl.get().getApp(appConfig.getAppKey());
                mapMessage.put("app", app);
            }

            int curPage = (pageStartIndex / pageSize) + 1;
            Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
            PageRows<ShakeItem> pageRows = JoymeAppConfigServiceSngl.get().queryShakeItems(appConfig.getConfigId(), ShakeType.getByCode(type), pagination);
            mapMessage.put("list", pageRows == null ? null : pageRows.getRows());
            mapMessage.put("page", pageRows == null ? null : pageRows.getPage());
            if(pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())){
                Set<Long> gameIdSet = new HashSet<Long>();
                for(ShakeItem item:pageRows.getRows()){
                    if(item != null){
                        Long gameId = Long.valueOf(item.getDirectId());
                        if(gameId != null){
                            gameIdSet.add(gameId);
                        }
                    }
                }
                List<GameDB> gameDBList = GameResourceServiceSngl.get().queryGameDB(new MongoQueryExpress().add(MongoQueryCriterions.in(GameDBField.ID, gameIdSet.toArray())));
                mapMessage.put("gameDBList", gameDBList);
            }
        } catch (Exception e) {
            GAlerter.lab("AppConfigController occur Exception.e", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/shake/poollist_" + type, mapMessage);
        }
        return new ModelAndView("/shake/poollist_" + type, mapMessage);
    }


    @RequestMapping(value = "/putpage")
    public ModelAndView putpage(@RequestParam(value = "configid", required = false) String configId,
                                @RequestParam(value = "type", required = false) Integer type) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            AppConfig appConfig = JoymeAppConfigServiceSngl.get().getAppConfig(configId);
            if (appConfig == null) {
                //todo
            }
            mapMessage.put("config", appConfig);
            mapMessage.put("type", type);
            if (appConfig != null && !StringUtil.isEmpty(appConfig.getAppKey())) {
                AuthApp app = OAuthServiceSngl.get().getApp(appConfig.getAppKey());
                mapMessage.put("app", app);
            }


            //todo
//            writeToolsLog(LogOperType.MODIFY_APP_CONFIG_PAGE, "查看开关项配置：" + configId);
        } catch (Exception e) {
            GAlerter.lab("AppConfigController occur Exception.e", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/shake/poolcreate_" + type, mapMessage);
        }
        return new ModelAndView("/shake/poolput_" + type, mapMessage);
    }

    @RequestMapping(value = "/put")
    public ModelAndView put(@RequestParam(value = "configid", required = false) String configId,
                            @RequestParam(value = "directid", required = false) String directId,
                            @RequestParam(value = "weight", required = false) Integer weight,
                            @RequestParam(value = "type", required = false) Integer type) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            AppConfig appConfig = JoymeAppConfigServiceSngl.get().getAppConfig(configId);
            if (appConfig == null) {
                //todo
            }
            ShakeItem shakeItem = new ShakeItem();
            shakeItem.setWeight(weight);
            shakeItem.setDirectId(directId);
            shakeItem.setShakeType(type);

            JoymeAppConfigServiceSngl.get().addShakeItem(appConfig, ShakeType.getByCode(type), shakeItem);

            //todo
//            writeToolsLog(LogOperType.MODIFY_APP_CONFIG, "开关项配置修改：" + configId);
        } catch (Exception e) {
            GAlerter.lab("AppConfigController occur Exception.e", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("redirect:/shake/pool/list?configid=" + configId + "&type=" + type);
        }
        return new ModelAndView("redirect:/shake/pool/list?configid=" + configId + "&type=" + type);
    }


    @RequestMapping(value = "/remove")
    public ModelAndView remove(@RequestParam(value = "configid", required = false) String configId,
                               @RequestParam(value = "directid", required = false) String directId,
                               @RequestParam(value = "weight", required = false) Integer weight,
                               @RequestParam(value = "type", required = false) Integer type) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            AppConfig appConfig = JoymeAppConfigServiceSngl.get().getAppConfig(configId);
            if (appConfig == null) {
                //todo
            }
            ShakeItem shakeItem = new ShakeItem();
            shakeItem.setWeight(weight);
            shakeItem.setDirectId(directId);
            shakeItem.setShakeType(type);

            JoymeAppConfigServiceSngl.get().removeShakeItem(appConfig.getConfigId(), shakeItem);

            //todo


//            writeToolsLog(LogOperType.MODIFY_APP_CONFIG, "开关项配置修改：" + configId);
        } catch (Exception e) {
            GAlerter.lab("AppConfigController occur Exception.e", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("redirect:/shake/pool/list?configid=" + configId + "&type=" + type);
        }
        return new ModelAndView("redirect:/shake/pool/list?configid=" + configId + "&type=" + type);
    }

}
