package com.enjoyf.webapps.tools.webpage.controller.joymeapp.shake;

import com.enjoyf.platform.service.joymeapp.AppChannelType;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.joymeapp.config.AppConfig;
import com.enjoyf.platform.service.joymeapp.config.AppConfigField;
import com.enjoyf.platform.service.joymeapp.config.AppConfigUtil;
import com.enjoyf.platform.service.joymeapp.config.ShakeTag;
import com.enjoyf.platform.service.joymeappconfig.JoymeAppConfigServiceSngl;
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
import org.hibernate.sql.Update;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zhitaoshi on 2015/4/1.
 */
@Controller
@RequestMapping(value = "/joymeapp/shake/tag")
public class ShakeTagController extends ToolsBaseController {

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
                             @RequestParam(value = "enterprise", required = false) Integer enterprise) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("appKey", appKey);
        mapMessage.put("appPlatform", platform);
        mapMessage.put("appVersion", version);
        mapMessage.put("appChannel", channel);
        mapMessage.put("appEnterprise", enterprise);

        mapMessage.put("platformSet", platformSet);

        QueryExpress queryExpress = new QueryExpress();
        if (!StringUtil.isEmpty(appKey)) {
            queryExpress.add(QueryCriterions.eq(AppConfigField.APPKEY, appKey));
        }
        if (platform != null) {
            queryExpress.add(QueryCriterions.eq(AppConfigField.PLATFORM, platform));
        }
        if (enterprise != null) {
            queryExpress.add(QueryCriterions.eq(AppConfigField.ENTERPRISE, enterprise));
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

        PageRows<AppConfig> pageRows = null;
        try {
            pageRows = JoymeAppConfigServiceSngl.get().queryAppConfigByPage(queryExpress, pagination);
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                mapMessage.put("list", pageRows.getRows());
                mapMessage.put("page", pageRows.getPage());
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
        }
        return new ModelAndView("/shake/tag/taglist", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createPage(@RequestParam(value = "appkey", required = false) String appKey,
                                   @RequestParam(value = "platform", required = false) Integer platform,
                                   @RequestParam(value = "enterprise", required = false) Integer enterprise,
                                   @RequestParam(value = "version", required = false) String version,
                                   @RequestParam(value = "channel", required = false) String channel,
                                   @RequestParam(value = "tag", required = false) String tag,
                                   @RequestParam(value = "begintime", required = false) String beginTime,
                                   @RequestParam(value = "endtime", required = false) String endTime) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("appKey", appKey);
        mapMessage.put("appPlatform", platform);
        mapMessage.put("appEnterprise", enterprise);
        mapMessage.put("appChannel", channel);
        mapMessage.put("appVersion", version);

        mapMessage.put("platformSet", platformSet);

        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(AppConfigField.APPKEY, appKey));
        queryExpress.add(QueryCriterions.eq(AppConfigField.PLATFORM, platform));
        queryExpress.add(QueryCriterions.eq(AppConfigField.ENTERPRISE, enterprise));

        try {
            Set<AppChannelType> channelSet = new HashSet<AppChannelType>();
            Set<String> versionSet = new HashSet<String>();
            List<AppConfig> list = JoymeAppConfigServiceSngl.get().queryAppConfig(queryExpress);
            if (!CollectionUtil.isEmpty(list)) {
                for (AppConfig config : list) {
                    if (config != null) {
                        if (AppChannelType.getByCode(config.getChannel()) != null) {
                            channelSet.add(AppChannelType.getByCode(config.getChannel()));
                        }
                        versionSet.add(config.getVersion());
                    }
                }
            }
            mapMessage.put("versionSet", versionSet);
            mapMessage.put("channelSet", channelSet);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return new ModelAndView("/shake/tag/createtag", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "appkey", required = false) String appKey,
                               @RequestParam(value = "platform", required = false) Integer platform,
                               @RequestParam(value = "enterprise", required = false) Integer enterprise,
                               @RequestParam(value = "versions", required = false) String versions,
                               @RequestParam(value = "channels", required = false) String channels,
                               @RequestParam(value = "tag", required = false) String tag,
                               @RequestParam(value = "begintime", required = false) String beginTime,
                               @RequestParam(value = "endtime", required = false) String endTime) {
        try {
            if (!StringUtil.isEmpty(versions) && !StringUtil.isEmpty(channels)) {
                String[] versionArr = versions.split("\\|");
                String[] channelArr = channels.split("\\|");
                if (!CollectionUtil.isEmpty(versionArr) && !CollectionUtil.isEmpty(channelArr)) {
                    for (String version : versionArr) {
                        if (!StringUtil.isEmpty(version)) {
                            for (String channel : channelArr) {
                                if (!StringUtil.isEmpty(channel)) {
                                    String configId = AppConfigUtil.getAppConfigId(appKey, String.valueOf(platform), version, channel, String.valueOf(enterprise));
                                    AppConfig appConfig = JoymeAppConfigServiceSngl.get().getAppConfig(configId);
                                    if (appConfig != null) {
                                        ShakeTag shakeTag = new ShakeTag();
                                        shakeTag.setTag(tag);
                                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        if (!StringUtil.isEmpty(beginTime)) {
                                            shakeTag.setBegintime(df.parse(beginTime).getTime());
                                        }
                                        if (!StringUtil.isEmpty(endTime)) {
                                            shakeTag.setEndtime(df.parse(endTime).getTime());
                                        }
                                        appConfig.getInfo().setShake_tag(shakeTag);
                                        UpdateExpress updateExpress = new UpdateExpress();
                                        updateExpress.set(AppConfigField.APPINFO, appConfig.getInfo().toJsonStr());
                                        JoymeAppConfigServiceSngl.get().modifyAppConfig(configId, updateExpress);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
        }
        return new ModelAndView("redirect:/joymeapp/shake/tag/list?appkey=" + appKey + "&platform=" + platform + "&enterprise=" + enterprise);
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyPage() {

        return new ModelAndView("/shake/tag/modifytag");
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify() {
        return new ModelAndView("redirect:/joymeapp/shake/tag/list");
    }

    @RequestMapping(value = "/detail")
    public ModelAndView detail() {
        return new ModelAndView("/shake/tag/tagdetail");
    }

    @RequestMapping(value = "/publish")
    public ModelAndView publish() {
        return new ModelAndView("redirect:/joymeapp/shake/tag/list");
    }

    @RequestMapping(value = "/remove")
    public ModelAndView remove() {
        return new ModelAndView("redirect:/joymeapp/shake/tag/list");
    }

    @RequestMapping(value = "/recover")
    public ModelAndView recover() {
        return new ModelAndView("redirect:/joymeapp/shake/tag/list");
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
}
