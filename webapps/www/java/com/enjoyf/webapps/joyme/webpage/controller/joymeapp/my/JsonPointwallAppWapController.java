package com.enjoyf.webapps.joyme.webpage.controller.joymeapp.my;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.point.PointActionHistory;
import com.enjoyf.platform.service.point.PointActionType;
import com.enjoyf.platform.service.point.PointKeyType;
import com.enjoyf.platform.service.point.PointServiceSngl;
import com.enjoyf.platform.service.point.pointwall.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.oauth.renren.api.client.utils.Md5Utils;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.QuerySort;
import com.enjoyf.platform.util.sql.QuerySortOrder;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import net.sf.json.JSONObject;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by tonydiao on 2014/12/16.
 */

@Controller
@RequestMapping(value = "/json/my/hotapp")
public class JsonPointwallAppWapController extends BaseRestSpringController {

    @Resource(name = "i18nSource")
    private ResourceBundleMessageSource i18nSource;


    @RequestMapping(value = "/jsonmore")
    @ResponseBody
    public String list(HttpServletRequest request, HttpServletResponse response,
                       @RequestParam(value = "pageNo", required = false) Integer pageNo,
                       @RequestParam(value = "pageSize", required = false) Integer pageSize,
                       @RequestParam(value = "appkey", required = false) String appkey,
                       @RequestParam(value = "profileid", required = false) String profileid,
                       @RequestParam(value = "clientid", required = false) String clientid,
                       @RequestParam(value = "platform", required = false) String platform) {

        JsonBinder binder = JsonBinder.buildNormalBinder();
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            pageNo = pageNo == null ? 1 : pageNo;
            pageSize = pageSize == null ? 15 : pageSize;

            if (appkey == null || appkey.trim().equals("")) {
                appkey = "default";

            }


            if (StringUtil.isEmpty(profileid) || StringUtil.isEmpty(clientid) || StringUtil.isEmpty(platform) || (!platform.equals("1") && !platform.equals("0"))) {
                mapMessage.put("errorStatus", 1);
                mapMessage.put("errorMessage", i18nSource.getMessage("point.pointwall.wap.error.param", null, Locale.CHINA));
                return binder.toJson(mapMessage);
            }

            Pagination pagination = new Pagination(pageNo * pageSize, pageNo, pageSize);


            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(PointwallWallAppField.APPKEY, appkey));

            //只选取status为valid的app
            queryExpress.add(QueryCriterions.eq(PointwallWallAppField.STATUS, ValidStatus.VALID.getCode()));
            queryExpress.add(QueryCriterions.eq(PointwallWallAppField.PLATFORM, Integer.valueOf(platform)));
            //按display_order asc 排序
            queryExpress.add(new QuerySort(PointwallWallAppField.DISPLAY_ORDER, QuerySortOrder.ASC));
            PageRows<PointwallWallApp> pageRows = PointServiceSngl.get().queryPointwallWallAppByPage(queryExpress, pagination);

            //todo 这个total 不能从pageRows上获取么？
            //获取总数,然后再判断一共有多少页,用于 前台而页的点击 "加载" 更多的功能
            int total = PointServiceSngl.get().countPointwallWallAppOfOneWall(queryExpress);
            int maxPage = (int) Math.ceil((double) total / pageSize);
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                List<PointwallWallApp> list = pageRows.getRows();
                long now = new Date().getTime();
                for (int i = 0; i < list.size(); i++) {

                    //判断某个app是否已经下载
                    QueryExpress downloadQueryExpress = new QueryExpress();
                    downloadQueryExpress.add(QueryCriterions.eq(PointwallTasklogField.TASK_ID, Md5Utils.md5(clientid + list.get(i).getAppId() + AppInstallType.DOWNLOADED.getCode())));
                    PointwallTasklog downloadPointwallTasklog = PointServiceSngl.get().getPointwallTasklog(downloadQueryExpress);
                    if (downloadPointwallTasklog != null) {

                        if ((now - downloadPointwallTasklog.getCreateTime().getTime()) / 1000 > 300) {
                            list.get(i).setInstallStatus(AppInstallType.DOWNLOADED.getCode());    //已经过了5分钟可以显示  "获取" 按钮

                        } else {

                            list.get(i).setInstallStatus(AppInstallType.DOWNLOADED_IN_FIVE.getCode());     //5分钟内 显示 "下载" 按钮
                        }


                    }

                    //判断某个app是否已经获得积分,只有对于已经下载的并且大于5分钟的app才会去判断
                    if (list.get(i).getInstallStatus() == AppInstallType.DOWNLOADED.getCode() || list.get(i).getInstallStatus() == AppInstallType.DOWNLOADED_IN_FIVE.getCode()) {
                        QueryExpress gotScoreQueryExpress = new QueryExpress();
                        gotScoreQueryExpress.add(QueryCriterions.eq(PointwallTasklogField.TASK_ID, Md5Utils.md5(clientid + list.get(i).getAppId() + AppInstallType.SCORE_AWARDED.getCode())));
                        PointwallTasklog gotPointwallTasklog = PointServiceSngl.get().getPointwallTasklog(gotScoreQueryExpress);

                        if (gotPointwallTasklog != null) {
                            list.get(i).setInstallStatus(AppInstallType.SCORE_AWARDED.getCode());

                        }
                    }
                }

                mapMessage.put("list", list);
                mapMessage.put("pageNo", pageRows.getPage());
                mapMessage.put("maxPage", maxPage);
                mapMessage.put("total", total);
            }

            mapMessage.put("errorStatus", 0);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMessage", "system.error");
            mapMessage.put("errorStatus", 1);

        }

        return binder.toJson(mapMessage);
    }


    @RequestMapping(value = "/download")
    @ResponseBody
    public String toDownload(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "appkey", required = false) String appkey,
                             @RequestParam(value = "profileid", required = false) String profileid,
                             @RequestParam(value = "clientid", required = false) String clientid,
                             @RequestParam(value = "platform", required = false) String platform,
                             @RequestParam(value = "wallAppId", required = false) Long wallAppId) {

        JsonBinder binder = JsonBinder.buildNormalBinder();
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {


            if (appkey == null || appkey.trim().equals("")) {
                appkey = "default";

            }


            if (StringUtil.isEmpty(profileid) || StringUtil.isEmpty(clientid) || StringUtil.isEmpty(platform) || (!platform.equals("1") && !platform.equals("0"))) {
                mapMessage.put("errorStatus", 1);

                mapMessage.put("errorMessage", i18nSource.getMessage("point.pointwall.wap.error.param", null, Locale.CHINA));
                return binder.toJson(mapMessage);
            }


            String ip = super.getIp(request);


            //从point.pw_r_wall_app表获取相关信息
            QueryExpress getWallAppQueryExpress = new QueryExpress();
            getWallAppQueryExpress.add(QueryCriterions.eq(PointwallWallAppField.WALL_APP_ID, wallAppId));
            PointwallWallApp pointwallWallApp = PointServiceSngl.get().getPointwallWallApp(getWallAppQueryExpress);


            //从point.pw_app表获取相关信息
            QueryExpress getAppQueryExpress = new QueryExpress();
            getAppQueryExpress.add(QueryCriterions.eq(PointwallAppField.APP_ID, pointwallWallApp.getAppId()));
            PointwallApp pointwallApp = PointServiceSngl.get().getPointwallApp(getAppQueryExpress);


            //根据pw_tasklog表是否存在记录来判断该 app是否被下载过
            QueryExpress downloadQueryExpress = new QueryExpress();
            downloadQueryExpress.add(QueryCriterions.eq(PointwallTasklogField.TASK_ID, Md5Utils.md5(clientid + pointwallWallApp.getAppId() + 1)));
            PointwallTasklog downloadPointwallTasklog = PointServiceSngl.get().getPointwallTasklog(downloadQueryExpress);

            if (downloadPointwallTasklog != null) {
                mapMessage.put("downloadUrl", pointwallApp.getDownloadUrl());
                mapMessage.put("errorStatus", 0);
                return binder.toJson(mapMessage);
            }


            //插入一条记录到point.pw_tasklog

            PointwallTasklog pointwallTasklog = new PointwallTasklog();
            pointwallTasklog.setTaskId(Md5Utils.md5(clientid + pointwallWallApp.getAppId() + 1));
            pointwallTasklog.setClientId(clientid);
            pointwallTasklog.setProfileid(profileid);
            pointwallTasklog.setAppId(pointwallWallApp.getAppId());
            pointwallTasklog.setPackageName(pointwallApp.getPackageName());
            pointwallTasklog.setAppkey(appkey);
            pointwallTasklog.setStatus(AppInstallType.DOWNLOADED.getCode());
            pointwallTasklog.setCreateIp(ip);
            pointwallTasklog.setPointAmount(pointwallWallApp.getPointAmount());
            pointwallTasklog.setPlatform(Integer.valueOf(platform));


            PointServiceSngl.get().insertPointwallTasklog(pointwallTasklog);


            mapMessage.put("downloadUrl", pointwallApp.getDownloadUrl());
            mapMessage.put("errorStatus", 0);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMessage", "system.error");
            mapMessage.put("errorStatus", 1);
        }

        return binder.toJson(mapMessage);
    }


    @RequestMapping(value = "/getscore")
    @ResponseBody
    public String toGetScore(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "appkey", required = false) String appkey,
                             @RequestParam(value = "profileid", required = false) String profileid,
                             @RequestParam(value = "clientid", required = false) String clientid,
                             @RequestParam(value = "platform", required = false) String platform,
                             @RequestParam(value = "wallAppId", required = false) Long wallAppId) {

        JsonBinder binder = JsonBinder.buildNormalBinder();
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {

            if (StringUtil.isEmpty(profileid) || StringUtil.isEmpty(clientid) || StringUtil.isEmpty(platform) || (!platform.equals("1") && !platform.equals("0"))) {
                mapMessage.put("errorStatus", 1);      //1:参数错误  2:请先下载    3:请到10分钟再领取   4:重复获取,积分已经给用户  0:领取成功
                mapMessage.put("errorMessage", i18nSource.getMessage("point.pointwall.wap.error.param", null, Locale.CHINA));
                return binder.toJson(mapMessage);
            }

            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileid);

            if (profile == null) {
                mapMessage.put("errorStatus", 1);      //1:参数错误  2:请先下载    3:请到10分钟再领取   4:重复获取,积分已经给用户  0:领取成功
                mapMessage.put("errorMessage", i18nSource.getMessage("point.pointwall.wap.error.profileid", null, Locale.CHINA));
                return binder.toJson(mapMessage);
            }

            String uno = profile.getUno();
            if (uno == null) {

                mapMessage.put("errorStatus", 1);      //1:参数错误   2:请先下载    3:请到10分钟再领取   4:重复获取,积分已经给用户  0:领取成功
                mapMessage.put("errorMessage", i18nSource.getMessage("point.pointwall.wap.error.uno", null, Locale.CHINA));
                return binder.toJson(mapMessage);

            }


            //从point.pw_r_wall_app表获取相关信息
            QueryExpress getWallAppQueryExpress = new QueryExpress();
            getWallAppQueryExpress.add(QueryCriterions.eq(PointwallWallAppField.WALL_APP_ID, wallAppId));
            PointwallWallApp pointwallWallApp = PointServiceSngl.get().getPointwallWallApp(getWallAppQueryExpress);

            //如果用户还没有下载,让他先下载----理论上不会出现,除非直接调页面的js
            QueryExpress downloadQueryExpress = new QueryExpress();
            downloadQueryExpress.add(QueryCriterions.eq(PointwallTasklogField.TASK_ID, Md5Utils.md5(clientid + pointwallWallApp.getAppId() + 1)));
            PointwallTasklog downloadPointwallTasklog = PointServiceSngl.get().getPointwallTasklog(downloadQueryExpress);

            if (downloadPointwallTasklog == null) {
                mapMessage.put("errorStatus", 2);       //1:参数错误  2:请先下载    3:请到10分钟再领取   4:重复获取,积分已经给用户  0:领取成功
                mapMessage.put("errorMessage", i18nSource.getMessage("point.pointwall.wap.error.app.first", null, Locale.CHINA));
                return binder.toJson(mapMessage);
            }


            long timeGap = (System.currentTimeMillis() - downloadPointwallTasklog.getCreateTime().getTime()) / 1000;
            if (timeGap < 60 * 5) {

                //从point.pw_app表获取下载地址相关信息
                QueryExpress getAppQueryExpress = new QueryExpress();
                getAppQueryExpress.add(QueryCriterions.eq(PointwallAppField.APP_ID, pointwallWallApp.getAppId()));
                PointwallApp pointwallApp = PointServiceSngl.get().getPointwallApp(getAppQueryExpress);

                mapMessage.put("errorStatus", 3);      //1:参数错误  2:请先下载    3:请到10分钟再领取   4:重复获取,积分已经给用户  0:领取成功
                mapMessage.put("errorMessage", i18nSource.getMessage("point.pointwall.wap.error.download.ten", null, Locale.CHINA));
                mapMessage.put("timeGap", timeGap);
                mapMessage.put("downloadUrl", pointwallApp.getDownloadUrl());

                return binder.toJson(mapMessage);

            }

            QueryExpress gotScoreQueryExpress = new QueryExpress();
            gotScoreQueryExpress.add(QueryCriterions.eq(PointwallTasklogField.TASK_ID, Md5Utils.md5(clientid + pointwallWallApp.getAppId() + 2)));
            PointwallTasklog gotPointwallTasklog = PointServiceSngl.get().getPointwallTasklog(gotScoreQueryExpress);


            PointwallWall wall = PointServiceSngl.get().getPointwallWall(new QueryExpress().add(QueryCriterions.eq(PointwallWallAppField.APPKEY, appkey)));

            String wallMoneyName = "迷豆";
            if (wall != null && wall.getWallMoneyName() != null) {

                wallMoneyName = wall.getWallMoneyName();
            }


            if (gotPointwallTasklog != null) {
                mapMessage.put("errorStatus", 4);      //1:参数错误  2:请先下载    3:请到10分钟再领取   4:重复获取,积分已经给用户  0:领取成功
                mapMessage.put("errorMessage", i18nSource.getMessage("point.pointwall.wap.error.score.got", null, Locale.CHINA) + wallMoneyName);
                return binder.toJson(mapMessage);
            }


            if (appkey == null || appkey.trim().equals("")) {
                appkey = "default";

            }


            // point.pw_wall中的point_key字段存放的是 PointKeyType类的code属性
            PointKeyType pointKey = null;

            if (wall != null && !StringUtil.isEmpty(wall.getPointKey())) {

                pointKey = PointKeyType.getByCode(wall.getPointKey());
            }

            if (pointKey == null) {
                pointKey = PointKeyType.DEFAULT;
            }

            String ip = super.getIp(request);


            //从point.pw_app表获取相关信息
            QueryExpress getAppQueryExpress = new QueryExpress();
            getAppQueryExpress.add(QueryCriterions.eq(PointwallAppField.APP_ID, pointwallWallApp.getAppId()));
            PointwallApp pointwallApp = PointServiceSngl.get().getPointwallApp(getAppQueryExpress);


            PointwallTasklog pointwallTasklog = new PointwallTasklog();
            pointwallTasklog.setTaskId(Md5Utils.md5(clientid + pointwallWallApp.getAppId() + AppInstallType.SCORE_AWARDED.getCode()));
            pointwallTasklog.setClientId(clientid);
            pointwallTasklog.setProfileid(profileid);
            pointwallTasklog.setAppId(pointwallWallApp.getAppId());
            pointwallTasklog.setPackageName(pointwallApp.getPackageName());
            pointwallTasklog.setAppkey(appkey);
            pointwallTasklog.setStatus(AppInstallType.SCORE_AWARDED.getCode());
            pointwallTasklog.setCreateIp(ip);
            pointwallTasklog.setPointAmount(pointwallWallApp.getPointAmount());
            pointwallTasklog.setPlatform(Integer.valueOf(platform));


            PointServiceSngl.get().insertPointwallTasklog(pointwallTasklog);

            PointActionHistory pointActionHistory = new PointActionHistory();
            pointActionHistory.setUserNo(uno);
            pointActionHistory.setProfileId(profileid);
            pointActionHistory.setActionType(PointActionType.DOWNLOAD_APP_FROM_POINT_WALL);
            pointActionHistory.setActionDescription("client:" + clientid + "在积分墙:" + appkey + "下载了appid为:" + pointwallWallApp.getAppId() + "包名是" + pointwallApp.getPackageName() + "的app");
            pointActionHistory.setPointValue(pointwallWallApp.getPointAmount());
            pointActionHistory.setActionDate(new Date());
            pointActionHistory.setCreateDate(new Date());
            PointServiceSngl.get().increasePointActionHistory(pointActionHistory, pointKey);


            mapMessage.put("errorStatus", 0);  //1:参数错误  2:请先下载    3:请到10分钟再领取   4:重复获取,积分已经给用户  0:领取成功

            mapMessage.put("pointAmount", pointwallWallApp.getPointAmount());


            return binder.toJson(mapMessage);


        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMessage", "system.error");
            mapMessage.put("errorStatus", 1);
        }

        return binder.toJson(mapMessage);
    }


    @RequestMapping(value = "/getapp")
    @ResponseBody
    public String toDownload(HttpServletRequest request, HttpServletResponse response) {
        String callback = request.getParameter("callback");
        String appId = request.getParameter("appid");
        if (StringUtil.isEmpty(appId)) {
            if(StringUtil.isEmpty(callback)){
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            }else {
                return callback + "([" +ResultCodeConstants.PARAM_EMPTY.getJsonString() + "])";
            }
        }
        try {

            long aid = -1l;
            try {
                aid = Long.parseLong(appId);
            } catch (NumberFormatException e) {
            }

            if (aid < 0l) {
                if(StringUtil.isEmpty(callback)){
                    return ResultCodeConstants.PARAM_EMPTY.getJsonString();
                }else {
                    return callback + "([" +ResultCodeConstants.PARAM_EMPTY.getJsonString() + "])";
                }
            }
            //从point.pw_r_wall_app表获取相关信息
            QueryExpress getWallAppQueryExpress = new QueryExpress();
            getWallAppQueryExpress.add(QueryCriterions.eq(PointwallAppField.APP_ID, aid));
            PointwallApp app = PointServiceSngl.get().getPointwallApp(getWallAppQueryExpress);


            if (app == null) {
                if(StringUtil.isEmpty(callback)){
                    return ResultCodeConstants.APP_NOT_EXISTS.getJsonString();
                }else {
                    return callback + "([" +ResultCodeConstants.APP_NOT_EXISTS.getJsonString() + "])";
                }
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("rs", String.valueOf(ResultCodeConstants.SUCCESS.getCode()));
            jsonObject.put("msg", "success");

            Map map = new HashMap();
            map.put("appid", app.getAppId());
            map.put("url", app.getDownloadUrl());
            map.put("appname", app.getAppName());

            jsonObject.put("result", map);

            if(StringUtil.isEmpty(callback)){
                return jsonObject.toString();
            }else {
                return callback + "([" +jsonObject.toString() + "])";
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            if(StringUtil.isEmpty(callback)){
                return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
            }else {
                return callback + "([" +ResultCodeConstants.SYSTEM_ERROR.getJsonString() + "])";
            }
        }
    }

}
