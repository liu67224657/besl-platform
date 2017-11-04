package com.enjoyf.webapps.tools.webpage.controller.point.pointwall;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.point.PointServiceSngl;
import com.enjoyf.platform.service.point.pointwall.PointwallApp;
import com.enjoyf.platform.service.point.pointwall.PointwallAppField;
import com.enjoyf.platform.service.point.pointwall.PointwallWallAppField;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.service.tools.ToolsLog;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by tonydiao on 2014/11/27.
 */

@Controller
@RequestMapping(value = "/point/pointwall/app")
public class PointwallAppController extends ToolsBaseController {

    @Resource(name = "i18nSource")
    private ResourceBundleMessageSource i18nSource;

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,      //数据库记录索引
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "20") int pageSize,
                             @RequestParam(value = "packageName", required = false) String packageName,
                             @RequestParam(value = "sponsorName", required = false) String sponsorName,
                             @RequestParam(value = "appSearchName", required = false) String appSearchName,
                             @RequestParam(value = "errorFK", required = false) String errorFK) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            if (!StringUtil.isEmpty(errorFK)) {
                mapMessage.put("errorFK", errorFK);
            }
            QueryExpress queryExpress = new QueryExpress();
            if (!StringUtil.isEmpty(packageName)) {
                mapMessage.put("packageName", packageName);
                queryExpress.add(QueryCriterions.like(PointwallAppField.PACKAGE_NAME, '%' + packageName + '%'));
            }
            if (!StringUtil.isEmpty(sponsorName)) {
                mapMessage.put("sponsorName", sponsorName);
                queryExpress.add(QueryCriterions.like(PointwallAppField.SPONSOR_NAME, '%' + sponsorName + '%'));
            }

            if (!StringUtil.isEmpty(appSearchName)) {
                mapMessage.put("appSearchName", appSearchName);
                queryExpress.add(QueryCriterions.like(PointwallAppField.APP_NAME, '%' + appSearchName + '%'));
            }
            queryExpress.add(new QuerySort(PointwallAppField.APP_ID, QuerySortOrder.DESC));
            int curPage = (pageStartIndex / pageSize) + 1;
            Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
            PageRows<PointwallApp> pageRows = PointServiceSngl.get().queryPointwallAppByPage(queryExpress, pagination);
            if (pageRows != null) {
                mapMessage.put("list", pageRows.getRows());
                mapMessage.put("page", pageRows.getPage());
            }

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/point/pointwall/applist", mapMessage);
    }


    @RequestMapping(value = "/createpage")
    public ModelAndView createPage() {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        return new ModelAndView("/point/pointwall/appadd", mapMessage);
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyPage(@RequestParam(value = "appId", required = true) Long appId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            if (appId == null) {
                mapMessage = putErrorMessage(mapMessage, "pointwallApp.appId.empty");
                return new ModelAndView("/point/pointwall/applist", mapMessage);
            }

            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(PointwallAppField.APP_ID, appId));
            PointwallApp pointwallApp = PointServiceSngl.get().getPointwallApp(queryExpress);
            if (pointwallApp == null) {
                mapMessage = putErrorMessage(mapMessage, "pointwallApp.wallId.empty");
                return new ModelAndView("/point/pointwall/applist", mapMessage);
            }

            mapMessage.put("appId", pointwallApp.getAppId());
            mapMessage.put("packageName", pointwallApp.getPackageName());
            mapMessage.put("appName", pointwallApp.getAppName());
            mapMessage.put("verName", pointwallApp.getVerName());
            mapMessage.put("platform", pointwallApp.getPlatform());
            mapMessage.put("appIcon", pointwallApp.getAppIcon());
            mapMessage.put("appDesc", pointwallApp.getAppDesc());
            mapMessage.put("sponsorName", pointwallApp.getSponsorName());
            mapMessage.put("downloadUrl", pointwallApp.getDownloadUrl());
            mapMessage.put("reportUrl", pointwallApp.getReportUrl());
            mapMessage.put("initScore", pointwallApp.getInitScore());
            mapMessage.put("createTime", pointwallApp.getCreateTime());

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/point/pointwall/applist", mapMessage);
        }

        return new ModelAndView("/point/pointwall/appedit", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "packageName", required = false) String packageName,
                               @RequestParam(value = "appName", required = false) String appName,
                               @RequestParam(value = "verName", required = false) String verName,
                               @RequestParam(value = "platform", required = false) Integer platform,
                               @RequestParam(value = "appIcon", required = false) String appIcon,
                               @RequestParam(value = "appDesc", required = false) String appDesc,
                               @RequestParam(value = "sponsorName", required = false) String sponsorName,
                               @RequestParam(value = "downloadUrl", required = false) String downloadUrl,
                               @RequestParam(value = "reportUrl", required = false) String reportUrl,
                               @RequestParam(value = "initScore", required = false) Integer initScore,
                               @RequestParam(value = "createTime", required = false) String createTime,
                               HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("packageName", packageName);
        mapMessage.put("appName", appName);
        mapMessage.put("verName", verName);
        mapMessage.put("platform", platform);
        mapMessage.put("appIcon", appIcon);
        mapMessage.put("appDesc", appDesc);
        mapMessage.put("sponsorName", sponsorName);
        mapMessage.put("downloadUrl", downloadUrl);
        mapMessage.put("reportUrl", reportUrl);
        mapMessage.put("initScore", initScore);
        mapMessage.put("createTime", createTime);

        try {
            QueryExpress queryTemp = new QueryExpress();
            queryTemp.add(QueryCriterions.eq(PointwallAppField.PACKAGE_NAME, packageName));
            queryTemp.add(QueryCriterions.eq(PointwallAppField.REMOVE_STATUS, ValidStatus.VALID.getCode()));
            PointwallApp tempWall = PointServiceSngl.get().getPointwallApp(queryTemp);
            if (tempWall != null) {
                mapMessage.put("nameExist", "PointwallApp.packageName: " + packageName + " has.exist");
                return new ModelAndView("/point/pointwall/appadd", mapMessage);
            }

            if (sponsorName == null) {
                sponsorName = "";
            }
            if (reportUrl == null) {
                reportUrl = "";
            }

            if (verName == null) {
                verName = "";
            }

            PointwallApp pointwallApp = new PointwallApp();
            pointwallApp.setPackageName(packageName);
            pointwallApp.setAppName(appName);
            pointwallApp.setVerName(verName);
            pointwallApp.setPlatform(platform);
            pointwallApp.setAppIcon(appIcon);
            pointwallApp.setAppDesc(appDesc);

            pointwallApp.setSponsorName(sponsorName);
            pointwallApp.setDownloadUrl(downloadUrl);
            pointwallApp.setReportUrl(reportUrl);
            pointwallApp.setInitScore(initScore);

            pointwallApp = PointServiceSngl.get().insertPointwallApp(pointwallApp);

            ToolsLog log = new ToolsLog();
            log.setOpUserId(getCurrentUser().getUserid());//用户的ID
            log.setOperType(LogOperType.POINT_POINTWALL_APP_ADD);    //操作的类型
            log.setOpTime(new Date());//操作时间
            log.setOpIp(getIp());//用户IP
            Map<String, String[]> params = request.getParameterMap();
            String queryString = "  ";
            for (String key : params.keySet()) {
                String[] values = params.get(key);
                for (int i = 0; i < values.length; i++) {
                    queryString += key + "=" + values[i] + "&";
                }
            }
            // 去掉最后一个空格
            queryString = queryString.substring(0, queryString.length() - 1);

            if (queryString.length() > 1950) {
                queryString = queryString.substring(0, 1950);
            }

            log.setOpAfter("新增条目到pw_app,appId,queryString-->" + queryString);
            addLog(log);

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/point/pointwall/app/createpage", mapMessage);
        }

        return new ModelAndView("redirect:/point/pointwall/app/list");
    }


    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "appId", required = true) Long appId,
                               @RequestParam(value = "packageName", required = false) String packageName,
                               @RequestParam(value = "appName", required = false) String appName,
                               @RequestParam(value = "verName", required = false) String verName,
                               @RequestParam(value = "platform", required = false) String platform,
                               @RequestParam(value = "appIcon", required = false) String appIcon,
                               @RequestParam(value = "appDesc", required = false) String appDesc,
                               @RequestParam(value = "sponsorName", required = false) String sponsorName,
                               @RequestParam(value = "downloadUrl", required = false) String downloadUrl,
                               @RequestParam(value = "reportUrl", required = false) String reportUrl,
                               @RequestParam(value = "initScore", required = false) Integer initScore,
                               HttpServletRequest request) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("appId", appId);
        mapMessage.put("packageName", packageName);
        mapMessage.put("appName", appName);
        mapMessage.put("verName", verName);
        mapMessage.put("platform", platform);
        mapMessage.put("appIcon", appIcon);
        mapMessage.put("appDesc", appDesc);
        mapMessage.put("sponsorName", sponsorName);
        mapMessage.put("downloadUrl", downloadUrl);
        mapMessage.put("reportUrl", reportUrl);
        mapMessage.put("initScore", initScore);


        try {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.ne(PointwallAppField.APP_ID, appId));
            queryExpress.add(QueryCriterions.eq(PointwallAppField.PACKAGE_NAME, packageName));
            queryExpress.add(QueryCriterions.eq(PointwallAppField.REMOVE_STATUS, ValidStatus.VALID.getCode()));
            PointwallApp pointwallApp = PointServiceSngl.get().getPointwallApp(queryExpress);
            if (pointwallApp != null) {
                mapMessage.put("nameExist", "PointwallApp.packageName: " + packageName + " has.exist");
                return new ModelAndView("/point/pointwall/appedit", mapMessage);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            //  mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        try {
            if (sponsorName == null) {
                sponsorName = "";
            }
            if (reportUrl == null) {
                reportUrl = "";
            }
            if (verName == null) {
                verName = "";
            }

            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(PointwallAppField.PACKAGE_NAME, packageName);
            updateExpress.set(PointwallAppField.APP_NAME, appName);

            updateExpress.set(PointwallAppField.VER_NAME, verName);
            updateExpress.set(PointwallAppField.PLATFORM, Integer.valueOf(platform));
            updateExpress.set(PointwallAppField.APP_ICON, appIcon);
            updateExpress.set(PointwallAppField.APP_DESC, appDesc);
            updateExpress.set(PointwallAppField.SPONSOR_NAME, sponsorName);
            updateExpress.set(PointwallAppField.DOWNLOAD_URL, downloadUrl);

            updateExpress.set(PointwallAppField.REPORT_URL, reportUrl);
            updateExpress.set(PointwallAppField.INIT_SCORE, initScore);
            QueryExpress queryExpressNew = new QueryExpress();

            queryExpressNew.add(QueryCriterions.eq(PointwallAppField.APP_ID, appId));
            boolean bool = PointServiceSngl.get().updatePointwallApp(updateExpress, queryExpressNew);

            //使point.pw_r_wall_app表的platform与pw_app表保持一致。
            UpdateExpress updateExpressWallApp = new UpdateExpress();
            updateExpressWallApp.set(PointwallWallAppField.PLATFORM, Integer.valueOf(platform));

            QueryExpress queryExpressWallApp = new QueryExpress();
            queryExpressWallApp.add(QueryCriterions.eq(PointwallWallAppField.APP_ID, appId));
            PointServiceSngl.get().updatePointwallWallApp(updateExpressWallApp, queryExpressWallApp);


            ToolsLog log = new ToolsLog();
            log.setOpUserId(getCurrentUser().getUserid());//用户的ID
            log.setOperType(LogOperType.POINT_POINTWALL_APP_MODIFY);    //操作的类型
            log.setOpTime(new Date());//操作时间
            log.setOpIp(getIp());//用户IP
            Map<String, String[]> params = request.getParameterMap();
            String queryString = "  ";
            for (String key : params.keySet()) {
                String[] values = params.get(key);
                for (int i = 0; i < values.length; i++) {
                    queryString += key + "=" + values[i] + "&";
                }
            }
            // 去掉最后一个空格
            queryString = queryString.substring(0, queryString.length() - 1);

            if (queryString.length() > 1950) {
                queryString = queryString.substring(0, 1950);
            }
            log.setOpAfter("pw_app表条目修改queryString" + queryString); //描述 推荐用中文
            addLog(log);


        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            //   mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/point/pointwall/app/list");
    }


    @RequestMapping(value = "/delete")
    public ModelAndView delete(@RequestParam(value = "appId", required = true) Long appId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {

            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(PointwallWallAppField.APP_ID, appId));
            int total = PointServiceSngl.get().countPointwallWallAppOfOneWall(queryExpress);
            if (total > 0) {
                Map<String, Object> mapMessageNew = new HashMap<String, Object>();
                mapMessageNew.put("errorFK", i18nSource.getMessage("point.pointwall.app.delete.failure", null, Locale.CHINA));
                return new ModelAndView("redirect:/point/pointwall/app/list", mapMessageNew);
            }

            QueryExpress queryExpressNew = new QueryExpress();
            queryExpressNew.add(QueryCriterions.eq(PointwallAppField.APP_ID, appId));

            //  UpdateExpress updateExpress = new UpdateExpress();
            //  设置状态为 removed
            //   updateExpress.set(PointwallAppField.REMOVE_STATUS, ValidStatus.REMOVED.getCode() );
            //   boolean bool = PointServiceSngl.get().updatePointwallApp(updateExpress, queryExpressNew);

            int result = PointServiceSngl.get().deletePointwallApp(queryExpressNew);
            if (result > 0) {
                ToolsLog log = new ToolsLog();
                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.POINT_PW_APP_DELETE);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("pw_app表删除appId is:" + appId);
                addLog(log);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            //   mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/point/pointwall/app/list");
    }


}
