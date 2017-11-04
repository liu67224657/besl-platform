package com.enjoyf.webapps.tools.webpage.controller.advertise.app;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.advertise.AdvertiseServiceSngl;
import com.enjoyf.platform.service.advertise.app.*;
import com.enjoyf.platform.service.joymeapp.AppChannelType;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.AuthAppField;
import com.enjoyf.platform.service.oauth.AuthAppType;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.webapps.tools.webpage.controller.advertise.AdvertiseBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-6-10
 * Time: 下午5:55
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/advertise/app/apply")
public class AppAdvertiseApplyController extends AdvertiseBaseController {

    private static final int PAGE_SIZE = 40;

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                             @RequestParam(value = "publishName", required = false) String publishName,
                             @RequestParam(value = "removestatus", required = false) String removestatus,
                             @RequestParam(value = "appkey", required = false, defaultValue = "") String appkey) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("publishName", publishName);
        mapMessage.put("removestatus", removestatus);
        mapMessage.put("appkey", appkey);
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
            return new ModelAndView("/advertise/apply/list", mapMessage);
        }
        return new ModelAndView("/advertise/apply/list", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createpage(@RequestParam(value = "appkey", required = false, defaultValue = "") String appkey) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("channelTypes", AppChannelType.getAll());
        mapMessage.put("appkey", appkey);
        try {
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
            return new ModelAndView("redirect:/advertise/app/apply/list");
        }

        return new ModelAndView("/advertise/apply/createpage", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "publishName", required = false) String publishName,
                               @RequestParam(value = "publishDesc", required = false) String publishDesc,
                               @RequestParam(value = "advertiseId", required = false) long advertiseId,
                               @RequestParam(value = "appkey", required = false, defaultValue = "") String appkey,
                               @RequestParam(value = "publish_type", required = false) String publish_type,
                               @RequestParam(value = "startdate", required = false) String startdate,
                               @RequestParam(value = "enddate", required = false) String enddate,
                               @RequestParam(value = "publish_param", required = false, defaultValue = "0") String publish_param,
                               @RequestParam(value = "channel", required = false) String channel
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("appkey", appkey);
        AppAdvertisePublish publish = new AppAdvertisePublish();
        try {
            publish.setPublishName(publishName);
            publish.setPublishDesc(publishDesc);

            publish.setAdvertiseId(advertiseId);

            publish.setAppkey(appkey);
            PublishParam publishParam = new PublishParam();
            if (publish_type.equals("0")) {
                publish.setPublishType(AppAdvertisePublishType.LOADING);
                publishParam.setLongtime(Long.valueOf(publish_param));
            } else if (publish_type.equals("1")) {
                publish.setPublishType(AppAdvertisePublishType.POP);
                publishParam.setLongtime(Long.valueOf(publish_param));
            } else if (publish_type.equals("2")) {
                publish.setPublishType(AppAdvertisePublishType.ARTICLE_LIS);
                publishParam.setNumberParam(Integer.valueOf(publish_param));
            } else if (publish_type.equals("3")) {
                publish.setPublishType(AppAdvertisePublishType.ACTIVITY_LIS);
                publishParam.setNumberParam(Integer.valueOf(publish_param));
            } else if (publish_type.equals("4")) {
                publish.setPublishType(AppAdvertisePublishType.MINI_GENERAL_BOTTOM);
                publishParam.setNumberParam(0);
            } else if (publish_type.equals("5")) {
                publish.setPublishType(AppAdvertisePublishType.SMALL_GAME_PAUSE);
                publishParam.setNumberParam(0);
            } else if (publish_type.equals("6")) {
                publish.setPublishType(AppAdvertisePublishType.SMALL_GAME_PASS);
                publishParam.setNumberParam(0);
            } else if (publish_type.equals("7")) {
                publish.setPublishType(AppAdvertisePublishType.SMALL_GAME_OVER);
                publishParam.setNumberParam(0);
            } else if (publish_type.equals("8")) {
                publish.setPublishType(AppAdvertisePublishType.GAME_CLIENT);
                publishParam.setNumberParam(Integer.valueOf(publish_param));
            }else{
                publish.setPublishType(AppAdvertisePublishType.getByCode(Integer.valueOf(publish_type)));
                publishParam.setNumberParam(Integer.valueOf(publish_param));
            }
            publish.setPublishParam(publishParam);
            if (!StringUtil.isEmpty(channel)) {
                publish.setChannel(AppChannelType.getByCode(channel));
            }

            publish.setCreateTime(new Date());
            publish.setCreatIp(getIp());
            publish.setCreateUser(getCurrentUser().getUserid());

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            publish.setStartTime(startdate == null ? null : df.parse(startdate));
            publish.setEndTime(enddate == null ? null : df.parse(enddate));

            publish = AdvertiseServiceSngl.get().createAppAdertisePublish(publish);

            writeToolsLog(LogOperType.ADVERTISE_APPLY_ADD, "广告应用,publishId" + publish.getPublishId());
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/advertise/apply/list", mapMessage);
        }
        return new ModelAndView("redirect:/advertise/app/apply/list?appkey=" + appkey);
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyPage(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                                   @RequestParam(value = "publishId", required = false, defaultValue = "0") long publishId,
                                   @RequestParam(value = "appkey", required = false, defaultValue = "") String appkey) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            AppAdvertisePublish advertisePublish = AdvertiseServiceSngl.get().getAppAdvertisePublish(publishId);
            mapMessage.put("advertisePublish", advertisePublish);

            mapMessage.put("pageStartIndex", pageStartIndex);

            mapMessage.put("channelTypes", AppChannelType.getAll());
            mapMessage.put("appkey", appkey);
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
            return new ModelAndView("/advertise/apply/list", mapMessage);
        }
        return new ModelAndView("/advertise/apply/modifypage", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                               @RequestParam(value = "publishId", required = false) Long publishId,
                               @RequestParam(value = "publishName", required = false) String publishName,
                               @RequestParam(value = "publishDesc", required = false) String publishDesc,
                               @RequestParam(value = "advertiseId", required = false) String advertiseId,
                               @RequestParam(value = "appkey", required = false, defaultValue = "") String appkey,
                               @RequestParam(value = "publishType", required = false) String publishType,
                               @RequestParam(value = "startTime", required = false) String startTime,
                               @RequestParam(value = "endTime", required = false) String endTime,
                               @RequestParam(value = "publishParam", required = false, defaultValue = "0") String publishParam,
                               @RequestParam(value = "removestatus", required = false) String removestatus,
                               @RequestParam(value = "channel", required = false) String channel) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            UpdateExpress updateExpress = new UpdateExpress();

            //list页面修改状态
            if (!StringUtil.isEmpty(removestatus)) {
                if (!StringUtil.isEmpty(removestatus) && removestatus.equals("n")) {
                    updateExpress.set(AppAdvertiseField.REMOVE_STATUS, ActStatus.UNACT.getCode());
                }
                if (!StringUtil.isEmpty(removestatus) && removestatus.equals("y")) {
                    updateExpress.set(AppAdvertiseField.REMOVE_STATUS, ActStatus.ACTED.getCode());
                }
            }

            //modifypage
            if (StringUtil.isEmpty(removestatus)) {
                updateExpress.set(AppAdvertisePublishField.PUBLISH_NAME, publishName);
                updateExpress.set(AppAdvertisePublishField.PUBLISH_DESC, publishDesc);
                updateExpress.set(AppAdvertisePublishField.ADVERTISE_ID, Long.valueOf(advertiseId));
                updateExpress.set(AppAdvertisePublishField.APP_KEY, appkey);
                PublishParam pb = new PublishParam();
                if (publishType.equals("0")) {
                    updateExpress.set(AppAdvertisePublishField.PUBLISH_TYPE, AppAdvertisePublishType.LOADING.getCode());
                    pb.setLongtime(Long.parseLong(publishParam));
                } else if (publishType.equals("1")) {
                    updateExpress.set(AppAdvertisePublishField.PUBLISH_TYPE, AppAdvertisePublishType.POP.getCode());
                    pb.setLongtime(Long.parseLong(publishParam));
                } else if (publishType.equals("2")) {
                    updateExpress.set(AppAdvertisePublishField.PUBLISH_TYPE, AppAdvertisePublishType.ARTICLE_LIS.getCode());
                    pb.setNumberParam(Integer.valueOf(publishParam));
                } else if (publishType.equals("3")) {
                    updateExpress.set(AppAdvertisePublishField.PUBLISH_TYPE, AppAdvertisePublishType.ACTIVITY_LIS.getCode());
                    pb.setNumberParam(Integer.valueOf(publishParam));
                } else if (publishType.equals("4")) {
                    updateExpress.set(AppAdvertisePublishField.PUBLISH_TYPE, AppAdvertisePublishType.MINI_GENERAL_BOTTOM.getCode());
                    pb.setNumberParam(0);
                } else if (publishType.equals("5")) {
                    updateExpress.set(AppAdvertisePublishField.PUBLISH_TYPE, AppAdvertisePublishType.SMALL_GAME_PAUSE.getCode());
                    pb.setNumberParam(0);
                } else if (publishType.equals("6")) {
                    updateExpress.set(AppAdvertisePublishField.PUBLISH_TYPE, AppAdvertisePublishType.SMALL_GAME_PASS.getCode());
                    pb.setNumberParam(0);
                } else if (publishType.equals("7")) {
                    updateExpress.set(AppAdvertisePublishField.PUBLISH_TYPE, AppAdvertisePublishType.SMALL_GAME_OVER.getCode());
                    pb.setNumberParam(0);
                } else if (publishType.equals("8")) {
                    updateExpress.set(AppAdvertisePublishField.PUBLISH_TYPE, AppAdvertisePublishType.GAME_CLIENT.getCode());
                    pb.setNumberParam(Integer.valueOf(publishParam));
                }else{
                    updateExpress.set(AppAdvertisePublishField.PUBLISH_TYPE, Integer.valueOf(publishType));
                    pb.setNumberParam(Integer.valueOf(publishParam));
                }
                updateExpress.set(AppAdvertisePublishField.PUBLISH_PARAM, pb.toJson());
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                updateExpress.set(AppAdvertisePublishField.START_TIME, StringUtil.isEmpty(startTime) ? null : df.parse(startTime));
                updateExpress.set(AppAdvertisePublishField.END_TIME, StringUtil.isEmpty(endTime) ? null : df.parse(endTime));
                updateExpress.set(AppAdvertisePublishField.CHANNEL, channel);
            }
            boolean bval = AdvertiseServiceSngl.get().modifyAppAdvertisePublish(updateExpress, publishId);
            if (bval) {
                writeToolsLog(LogOperType.ADVERTISE_APPLY_UPDATE, "广告应用,publishId" + publishId);
            }
            mapMessage.put("pageStartIndex", pageStartIndex);
            //mapMessage.put("appkey", appkey);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/advertise/apply/list", mapMessage);
        }
        return new ModelAndView("redirect:/advertise/app/apply/list?maxPageItems=" + PAGE_SIZE + "&pager.offset" + pageStartIndex + "&appkey=" + appkey, mapMessage);
    }
}
