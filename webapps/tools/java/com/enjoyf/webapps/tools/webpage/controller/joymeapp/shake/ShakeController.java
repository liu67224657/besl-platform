package com.enjoyf.webapps.tools.webpage.controller.joymeapp.shake;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.joymeapp.AppChannelType;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.joymeapp.JoymeAppServiceSngl;
import com.enjoyf.platform.service.joymeapp.config.AppConfig;
import com.enjoyf.platform.service.joymeapp.config.AppConfigField;
import com.enjoyf.platform.service.joymeappconfig.JoymeAppConfigServiceSngl;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.AuthAppField;
import com.enjoyf.platform.service.oauth.AuthAppType;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.QuerySort;
import com.enjoyf.platform.util.sql.QuerySortOrder;
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
@RequestMapping(value = "/shake")
class ShakeController extends ToolsBaseController {
    private static Set<AppPlatform> platformSet = new HashSet<AppPlatform>();

    static {
        platformSet.add(AppPlatform.ANDROID);
        platformSet.add(AppPlatform.IOS);
    }

    @RequestMapping(value = "/configlist")
    public ModelAndView shakeModifyPage(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                                        @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                                        @RequestParam(value = "appkey", required = false) String appKey,
                                        @RequestParam(value = "platform", required = false) Integer platform,
                                        @RequestParam(value = "version", required = false) String version,
                                        @RequestParam(value = "channel", required = false) String channel) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("appKey", appKey);
        mapMessage.put("platform", platform);
        mapMessage.put("appversion", version);
        mapMessage.put("channel", channel);

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
            return new ModelAndView("/shake/configlist", mapMessage);
        }

        return new ModelAndView("/shake/configlist", mapMessage);
    }


}
