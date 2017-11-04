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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-31
 * Time: 上午9:43
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/joymeapp/updatecontent")
public class AppContentUpdateController extends ToolsBaseController {

    @Resource(name = "jomyeAppWebLogic")
    private JoymeAppWebLogic joymeAppWebLogic;

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,      //数据库记录索引
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                             @RequestParam(value = "appkey", required = false) String appKey) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            List<AuthApp> appList = OAuthServiceSngl.get().queryAuthApp(
                    new QueryExpress()
                            .add(QueryCriterions.eq(AuthAppField.APPTYPE, AuthAppType.INTERNAL_CLIENT.getCode()))
                            .add(QueryCriterions.in(AuthAppField.PLATOFRM, new Integer[]{AppPlatform.ANDROID.getCode(), AppPlatform.CLIENT.getCode(),AppPlatform.IOS.getCode()}))
                            .add(QueryCriterions.eq(AuthAppField.VALIDSTATUS, ValidStatus.VALID.getCode()))
            );
            mapMessage.put("applist", appList);

            if (StringUtil.isEmpty(appKey)) {
                return new ModelAndView("/joymeapp/updatecontentlist", mapMessage);
            }

            int curPage = (pageStartIndex / pageSize) + 1;
            Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);


            PageRows<AppContentVersionInfo> pageRows = JoymeAppConfigServiceSngl.get().queryContentVersionByAppKeyPage(appKey, pagination);
            mapMessage.put("list", pageRows.getRows());
            mapMessage.put("page", pageRows.getPage());
            mapMessage.put("appKey", appKey);

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("/joymeapp/updatecontentlist", mapMessage);
    }


    //
    @RequestMapping(value = "/createpage")
    public ModelAndView createPushPage(@RequestParam(value = "appkey", required = true) String appKey) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            AuthApp app = OAuthServiceSngl.get().getApp(appKey);
            if (app == null) {
                mapMessage.put("platformError", "app.platform.empty");
                return new ModelAndView("forward:/joymeapp/updatecontent/list", mapMessage);
            }

            mapMessage.put("packageTypes", ContentPackageType.getAll());
            mapMessage.put("app", app);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
        }
        return new ModelAndView("/joymeapp/createcontent", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView createMessage(@RequestParam(value = "version_url", required = false) String url,
                                      @RequestParam(value = "version_info", required = false) String info,
                                      @RequestParam(value = "packagetype", required = true,defaultValue = "0") Integer pageageType,
                                      @RequestParam(value = "necessaryupdate", required = true,defaultValue = "false") boolean necessaryupdate,
                                      @RequestParam(value = "appkey", required = false) String appKey) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            AuthApp app = OAuthServiceSngl.get().getApp(appKey);
            if (app == null) {
                mapMessage.put("platformError", "app.platform.empty");
                return new ModelAndView("forward:/joymeapp/updatecontent/createpage", mapMessage);
            }

            AppContentVersionInfo appContentVersionInfo = new AppContentVersionInfo();

            appContentVersionInfo.setCurrent_version(joymeAppWebLogic.generatorApp(appKey));

            appContentVersionInfo.setVersion_url(url);
            appContentVersionInfo.setVersion_info(info);
            appContentVersionInfo.setPublishDate(new Date());
            appContentVersionInfo.setCreateIp(getIp());
            appContentVersionInfo.setNecessaryUpdate(necessaryupdate);
            appContentVersionInfo.setPackageType(pageageType);
            appContentVersionInfo.setCreateUserId(getCurrentUser().getUserid());
            appContentVersionInfo.setRemoveStatus(ActStatus.UNACT);
            appContentVersionInfo.setAppKey(appKey);

            JoymeAppConfigServiceSngl.get().createAppContentVersion(appContentVersionInfo);

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
        }

        mapMessage.put("appKey", appKey);
        return new ModelAndView("forward:/joymeapp/updatecontent/list", mapMessage);
    }

    //
    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyPage(@RequestParam(value = "vid", required = true) Long vid) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            AppContentVersionInfo version = JoymeAppConfigServiceSngl.get().getAppContnetVersion(new QueryExpress().add(QueryCriterions.eq(AppContentVersionField.ID, vid)));

            AuthApp app = OAuthServiceSngl.get().getApp(version.getAppKey());
            if (app == null) {
                mapMessage.put("platformError", "app.platform.empty");
                return new ModelAndView("forward:/joymeapp/updatecontent/list", mapMessage);

            }

            mapMessage.put("app", app);
            mapMessage.put("packageTypes", ContentPackageType.getAll());
            mapMessage.put("versionInfo", version);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);

        }

        return new ModelAndView("/joymeapp/modifycontent", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "version_url", required = false) String url,
                               @RequestParam(value = "version_info", required = false) String info,
                               @RequestParam(value = "vid", required = true) Long vid,
                               @RequestParam(value = "packagetype", required = true,defaultValue = "0") Integer pageageType,
                               @RequestParam(value = "necessaryupdate", required = true,defaultValue = "false") boolean necessaryupdate,
                               @RequestParam(value = "appkey", required = true) String appKey) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(AppContentVersionField.VERSION_URL, url);
            updateExpress.set(AppContentVersionField.VERSION_INFO, info);
            updateExpress.set(AppContentVersionField.PACKAGE_TYPE, ContentPackageType.getByCode(pageageType).getCode());
            updateExpress.set(AppContentVersionField.NECESSARY_UPDATE, necessaryupdate);
            JoymeAppConfigServiceSngl.get().modifyAppContentVersion(updateExpress, new QueryExpress().add(QueryCriterions.eq(AppContentVersionField.ID, vid)), appKey);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
        }

        mapMessage.put("appKey", appKey);
        return new ModelAndView("forward:/joymeapp/updatecontent/list", mapMessage);
    }

    //
    @RequestMapping(value = "/delete")
    public ModelAndView deleteUpdateContent(@RequestParam(value = "vid", required = true) Long appVersionId,
                                            @RequestParam(value = "appkey", required = true) String appKey) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(AppContentVersionField.REMOVESTTAUS, ActStatus.ACTED.getCode());
        try {
            JoymeAppConfigServiceSngl.get().modifyAppContentVersion(updateExpress, new QueryExpress().add(QueryCriterions.eq(AppContentVersionField.ID, appVersionId)), appKey);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
        }

        mapMessage.put("appKey", appKey);
        return new ModelAndView("forward:/joymeapp/updatecontent/list", mapMessage);
    }
}
