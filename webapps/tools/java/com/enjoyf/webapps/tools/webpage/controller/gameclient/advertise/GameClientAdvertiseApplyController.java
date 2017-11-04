package com.enjoyf.webapps.tools.webpage.controller.gameclient.advertise;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.advertise.AdvertiseServiceSngl;
import com.enjoyf.platform.service.advertise.app.AppAdvertisePublish;
import com.enjoyf.platform.service.advertise.app.AppAdvertisePublishField;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
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
import com.enjoyf.webapps.tools.webpage.controller.advertise.AdvertiseBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-6-10
 * Time: 下午5:55
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/gameclient/apply")
public class GameClientAdvertiseApplyController extends AdvertiseBaseController {

    private static final int PAGE_SIZE = 40;

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                             @RequestParam(value = "publishName", required = false) String publishName,
                             @RequestParam(value = "removestatus", required = false) String removestatus,
                             @RequestParam(value = "appkey", required = false, defaultValue = "17yfn24TFexGybOF0PqjdY") String appkey) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            int curPage = (pageStartIndex / pageSize) + 1;
            Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
            QueryExpress queryExpress = new QueryExpress();

            if (!StringUtil.isEmpty(publishName)) {
                queryExpress.add(QueryCriterions.like(AppAdvertisePublishField.PUBLISH_NAME, "%" + publishName + "%"));
            }

            //状态
            if (!StringUtil.isEmpty(removestatus) && removestatus.equals("y")) {
                queryExpress.add(QueryCriterions.eq(AppAdvertisePublishField.REMOVE_STATUS, ActStatus.ACTED.getCode()));
            } else if (!StringUtil.isEmpty(removestatus) && removestatus.equals("n")) {
                queryExpress.add(QueryCriterions.eq(AppAdvertisePublishField.REMOVE_STATUS, ActStatus.UNACT.getCode()));
            }

            if (!StringUtil.isEmpty(appkey)) {
                queryExpress.add(QueryCriterions.eq(AppAdvertisePublishField.APP_KEY, appkey));
            }

            queryExpress.add(QuerySort.add(AppAdvertisePublishField.PUBLISH_ID, QuerySortOrder.DESC));

            PageRows<AppAdvertisePublish> pageRows = AdvertiseServiceSngl.get().queryAppAdvertisePublish(queryExpress, pagination);
            mapMessage.put("list", pageRows.getRows());
            mapMessage.put("page", pageRows.getPage());
            mapMessage.put("appkey", appkey);
            //显示appname
            if (!CollectionUtil.isEmpty(pageRows.getRows())) {
                List<AppAdvertisePublish> list = new ArrayList<AppAdvertisePublish>();
                for (AppAdvertisePublish advertisePublish : pageRows.getRows()) {
                    AuthApp app = OAuthServiceSngl.get().getApp(advertisePublish.getAppkey());
                    advertisePublish.setAppkey(app.getAppName());
                    list.add(advertisePublish);
                }
            }

            //得到存在的app列表
            List<AuthApp> appList = OAuthServiceSngl.get().queryAuthApp(
                    new QueryExpress()
                            .add(QueryCriterions.eq(AuthAppField.APPTYPE, AuthAppType.INTERNAL_CLIENT.getCode()))
                            .add(QueryCriterions.in(AuthAppField.PLATOFRM, new Integer[]{AppPlatform.ANDROID.getCode(), AppPlatform.CLIENT.getCode(), AppPlatform.IOS.getCode()}))
                            .add(QueryCriterions.eq(AuthAppField.VALIDSTATUS, ValidStatus.VALID.getCode()))
            );
            mapMessage.put("applist", appList);


        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/gameclient/apply/list", mapMessage);
        }
        return new ModelAndView("/gameclient/apply/list", mapMessage);
    }
}
