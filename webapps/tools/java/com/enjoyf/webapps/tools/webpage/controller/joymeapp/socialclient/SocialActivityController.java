package com.enjoyf.webapps.tools.webpage.controller.joymeapp.socialclient;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.content.ContentServiceSngl;
import com.enjoyf.platform.service.content.social.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.sync.ShareBaseInfo;
import com.enjoyf.platform.service.sync.ShareBaseInfoField;
import com.enjoyf.platform.service.sync.ShareType;
import com.enjoyf.platform.service.sync.SyncServiceSngl;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-4-19
 * Time: 下午8:33
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/joymeapp/social/activity")
public class SocialActivityController extends ToolsBaseController {

    private static Set<Integer> orderType = new HashSet<Integer>();

    static {
        orderType.add(1);  //按使用数排序
        orderType.add(2);  //按回复数派寻
        orderType.add(3);  //按点赞数排序
        orderType.add(4);  //按礼物数排序
    }

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "5") int pageSize,
                             @RequestParam(value = "qtitle", required = false) String queryTitle,
                             @RequestParam(value = "qstarttime", required = false) Date queryStartTime,
                             @RequestParam(value = "qendtime", required = false) Date queryEndTime,
                             @RequestParam(value = "qcreateuserid", required = false) String queryCreateUserId,
                             @RequestParam(value = "qstatus", required = false) String queryStatus,
                             @RequestParam(value = "qordertype", required = false) Integer queryOrderType

    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("queryTitle", queryTitle);
        mapMessage.put("queryStatus", queryStatus);
        mapMessage.put("queryStartTime", queryStartTime);
        mapMessage.put("queryEndTime", queryEndTime);
        mapMessage.put("queryCreateUserId", queryCreateUserId);
        mapMessage.put("queryOrderType", queryOrderType);
        if (queryEndTime != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(queryEndTime);
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            calendar.add(Calendar.SECOND, -1);
            queryEndTime = calendar.getTime();
        }

        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
        QueryExpress queryExpress = new QueryExpress();
        if (!StringUtil.isEmpty(queryTitle)) {
            queryExpress.add(QueryCriterions.geq(SocialActivityField.TITLE, queryTitle));
        }
        if (queryStartTime != null) {
            queryExpress.add(QueryCriterions.geq(SocialActivityField.CREATE_DATE, queryStartTime));
        }
        if (queryEndTime != null) {
            queryExpress.add(QueryCriterions.leq(SocialActivityField.CREATE_DATE, queryEndTime));
        }
        if (!StringUtil.isEmpty(queryCreateUserId)) {
            queryExpress.add(QueryCriterions.eq(SocialActivityField.CREATE_USERID, queryCreateUserId));
        }
        if (!StringUtil.isEmpty(queryStatus)) {
            queryExpress.add(QueryCriterions.eq(SocialActivityField.REMOVE_STATUS, queryStatus));
        } else {
            queryExpress.add(QueryCriterions.ne(SocialActivityField.REMOVE_STATUS, ValidStatus.REMOVED.getCode()));
        }
        if (queryOrderType != null) {
            if (queryOrderType == 1) {
                queryExpress.add(QuerySort.add(SocialActivityField.USE_SUM, QuerySortOrder.DESC));
            } else if (queryOrderType == 2) {
                queryExpress.add(QuerySort.add(SocialActivityField.REPLY_SUM, QuerySortOrder.DESC));
            } else if (queryOrderType == 3) {
                queryExpress.add(QuerySort.add(SocialActivityField.AGREE_SUM, QuerySortOrder.DESC));
            } else if (queryOrderType == 4) {
                queryExpress.add(QuerySort.add(SocialActivityField.GIFT_SUM, QuerySortOrder.DESC));
            }
        } else {
            queryExpress.add(QuerySort.add(SocialActivityField.DISPLAY_ORDER, QuerySortOrder.DESC));
        }
        try {
            PageRows<SocialActivity> pageRows = ContentServiceSngl.get().querySocialActivityByPage(queryExpress, pagination);
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                mapMessage.put("list", pageRows.getRows());
                mapMessage.put("page", pageRows.getPage());
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/joymeapp/socialclient/activity/list", mapMessage);
        }
        return new ModelAndView("/joymeapp/socialclient/activity/list", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createPage() {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("SubscriptTypeColl", SubscriptType.getAll());
        try {
            List<ShareBaseInfo> shareInfoList = SyncServiceSngl.get().queryShareInfo(new QueryExpress()
                    .add(QueryCriterions.eq(ShareBaseInfoField.SHARETYPE, ShareType.SOCIAL_CLIENT.getCode()))
                    .add(QueryCriterions.eq(ShareBaseInfoField.REMOVESTATUS, ActStatus.UNACT.getCode())));
            mapMessage.put("shareInfoList", shareInfoList);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("redirect:/joymeapp/social/activity/list");
        }
        return new ModelAndView("/joymeapp/socialclient/activity/create", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "title", required = false) String title,
                               @RequestParam(value = "description", required = false) String description,
                               @RequestParam(value = "iosicon", required = false) String iosIcon,
                               @RequestParam(value = "androidicon", required = false) String androidIcon,
                               @RequestParam(value = "iossmallpic", required = false) String iosSmallPic,
                               @RequestParam(value = "androidsmallpic", required = false) String androidSmallPic,
                               @RequestParam(value = "iosbigpic", required = false) String iosBigPic,
                               @RequestParam(value = "androidbigpic", required = false) String androidBigPic,
                               @RequestParam(value = "startdate", required = false) Date startDate,
                               @RequestParam(value = "enddate", required = false) Date endDate,
                               @RequestParam(value = "subscripttype", required = false) Integer subscriptType,
                               @RequestParam(value = "jsonaward", required = false) String jsonAward,
                               @RequestParam(value = "shareid", required = false) Long shareId,
                               @RequestParam(value = "retype", required = false) Integer reType,
                               @RequestParam(value = "reurl", required = false) String reUrl
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("title", title);
        mapMessage.put("description", description);
        mapMessage.put("iosIcon", iosIcon);
        mapMessage.put("androidIcon", androidIcon);
        mapMessage.put("iosSmallPic", iosSmallPic);
        mapMessage.put("androidSmallPic", androidSmallPic);
        mapMessage.put("iosBigPic", iosBigPic);
        mapMessage.put("androidBigPic", androidBigPic);
        mapMessage.put("startDate", startDate);
        mapMessage.put("endDate", endDate);
        SocialAwardSet awardSet = SocialAwardSet.parse(jsonAward);
        mapMessage.put("awardSet", awardSet);
        mapMessage.put("shareId", shareId);
        mapMessage.put("reType", reType);
        mapMessage.put("reUrl", reUrl);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Subscript subscript = new Subscript();
        subscript.setStartDate(startDate == null ? null : df.format(startDate));
        subscript.setEndDate(endDate == null ? null : df.format(endDate));
        subscript.setType(subscriptType);

        SocialActivity socialActivity = new SocialActivity();
        socialActivity.setTitle(title);
        socialActivity.setDescription(description);
        socialActivity.setIosIcon(iosIcon);
        socialActivity.setAndroidIcon(androidIcon);
        socialActivity.setIosSmallPic(iosSmallPic);
        socialActivity.setAndroidSmallPic(androidSmallPic);
        socialActivity.setIosBigPic(iosBigPic);
        socialActivity.setAndroidBigPic(androidBigPic);
        socialActivity.setSubscriptType(SubscriptType.getByCode(subscriptType));
        socialActivity.setSubscript(subscript);
        socialActivity.setAwardSet(awardSet);
        if (shareId != null) {
            socialActivity.setShareId(shareId);
        }
        socialActivity.setCreateDate(new Date());
        socialActivity.setCreateIp(getIp());
        socialActivity.setCreateUserId(getCurrentUser().getUserid());
        socialActivity.setRemoveStatus(ValidStatus.INVALID);
        socialActivity.setRedirectType(reType);
        socialActivity.setRedirectUrl(reUrl);
        try {
            ContentServiceSngl.get().insertSocialActivity(socialActivity);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/joymeapp/socialclient/activity/create", mapMessage);
        }
        return new ModelAndView("redirect:/joymeapp/social/activity/list");
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyPage(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,      //数据库记录索引
                                   @RequestParam(value = "maxPageItems", required = false, defaultValue = "5") int pageSize,
                                   @RequestParam(value = "aid", required = false) Long activityId,
                                   @RequestParam(value = "qtitle", required = false) String queryTitle,
                                   @RequestParam(value = "qstarttime", required = false) Date queryStartTime,
                                   @RequestParam(value = "qendtime", required = false) Date queryEndTime,
                                   @RequestParam(value = "qcreateuserid", required = false) String queryCreateUserId,
                                   @RequestParam(value = "qstatus", required = false) String queryStatus,
                                   @RequestParam(value = "qordertype", required = false) Integer queryOrderType

    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("queryTitle", queryTitle);
        mapMessage.put("queryStatus", queryStatus);
        mapMessage.put("queryStartTime", queryStartTime);
        mapMessage.put("queryEndTime", queryEndTime);
        mapMessage.put("queryCreateUserId", queryCreateUserId);
        mapMessage.put("queryOrderType", queryOrderType);
        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
        mapMessage.put("page", pagination);

        mapMessage.put("SubscriptTypeColl", SubscriptType.getAll());

        try {
            List<ShareBaseInfo> shareInfoList = SyncServiceSngl.get().queryShareInfo(new QueryExpress()
                    .add(QueryCriterions.eq(ShareBaseInfoField.SHARETYPE, ShareType.SOCIAL_CLIENT.getCode()))
                    .add(QueryCriterions.eq(ShareBaseInfoField.REMOVESTATUS, ActStatus.UNACT.getCode())));
            mapMessage.put("shareInfoList", shareInfoList);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("forward:/joymeapp/socialclient/activity/list", mapMessage);
        }

        try {
            SocialActivity socialActivity = ContentServiceSngl.get().getSocialActivity(activityId);
            if (socialActivity != null) {
                mapMessage.put("activity", socialActivity);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("forward:/joymeapp/socialclient/activity/list", mapMessage);
        }

        return new ModelAndView("/joymeapp/socialclient/activity/modify", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                               @RequestParam(value = "maxPageItems", required = false, defaultValue = "5") int pageSize,
                               @RequestParam(value = "aid", required = false) Long activityId,
                               @RequestParam(value = "title", required = false) String title,
                               @RequestParam(value = "description", required = false) String description,
                               @RequestParam(value = "iosicon", required = false) String iosIcon,
                               @RequestParam(value = "androidicon", required = false) String androidIcon,
                               @RequestParam(value = "iossmallpic", required = false) String iosSmallPic,
                               @RequestParam(value = "androidsmallpic", required = false) String androidSmallPic,
                               @RequestParam(value = "iosbigpic", required = false) String iosBigPic,
                               @RequestParam(value = "androidbigpic", required = false) String androidBigPic,
                               @RequestParam(value = "startdate", required = false) Date startDate,
                               @RequestParam(value = "enddate", required = false) Date endDate,
                               @RequestParam(value = "subscripttype", required = false) Integer subscriptType,
                               @RequestParam(value = "jsonaward", required = false) String jsonAward,
                               @RequestParam(value = "shareid", required = false) Long shareId,
                               @RequestParam(value = "retype", required = false) Integer reType,
                               @RequestParam(value = "reurl", required = false) String reUrl,

                               @RequestParam(value = "qtitle", required = false)String queryTitle,
                               @RequestParam(value = "qstarttime", required = false) Date queryStartTime,
                               @RequestParam(value = "qendtime", required = false) Date queryEndTime,
                               @RequestParam(value = "qcreateuserid", required = false) String queryCreateUserId,
                               @RequestParam(value = "qstatus", required = false) String queryStatus,
                               @RequestParam(value = "qordertype", required = false) Integer queryOrderType
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("queryTitle", queryTitle);
        mapMessage.put("queryStatus", queryStatus);
        mapMessage.put("queryStartTime", queryStartTime);
        mapMessage.put("queryEndTime", queryEndTime);
        mapMessage.put("queryCreateUserId", queryCreateUserId);
        mapMessage.put("queryOrderType", queryOrderType);
        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
        mapMessage.put("page", pagination);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Subscript subscript = new Subscript();
        subscript.setStartDate(startDate == null ? null : df.format(startDate));
        subscript.setEndDate(endDate == null ? null : df.format(endDate));
        subscript.setType(subscriptType);

        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(SocialActivityField.TITLE, title);
        updateExpress.set(SocialActivityField.DESCRIPTION, description);
        updateExpress.set(SocialActivityField.IOS_ICON, iosIcon);
        updateExpress.set(SocialActivityField.ANDROID_ICON, androidIcon);
        updateExpress.set(SocialActivityField.IOS_SMALL_PIC, iosSmallPic);
        updateExpress.set(SocialActivityField.ANDROID_SMALL_PIC, androidSmallPic);
        updateExpress.set(SocialActivityField.IOS_BIG_PIC, iosBigPic);
        updateExpress.set(SocialActivityField.ANDROID_BIG_PIC, androidBigPic);
        updateExpress.set(SocialActivityField.SUBSCRIPT, subscript.toJsonStr());
        updateExpress.set(SocialActivityField.SUBSCRIPT_TYPE, subscriptType);
        updateExpress.set(SocialActivityField.JSON_AWARD, jsonAward);
        updateExpress.set(SocialActivityField.SHARE_ID, shareId);
        updateExpress.set(SocialActivityField.MODIFY_DATE, new Date());
        updateExpress.set(SocialActivityField.MODIFY_IP, getIp());
        updateExpress.set(SocialActivityField.MODIFY_USERID, getCurrentUser().getUserid());
        updateExpress.set(SocialActivityField.RETYPE, reType);
        updateExpress.set(SocialActivityField.REURL, StringUtil.isEmpty(reUrl) ? "" : reUrl);

        try {
            ContentServiceSngl.get().modifySocialActivity(activityId, updateExpress);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("forward:/joymeapp/social/activity/list", mapMessage);
        }
        return new ModelAndView("forward:/joymeapp/social/activity/list", mapMessage);
    }

    @RequestMapping(value = "/remove")
    public ModelAndView modify(@RequestParam(value = "aid", required = false) Long activityId,
                               @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                               @RequestParam(value = "maxPageItems", required = false, defaultValue = "5") int pageSize,
                               @RequestParam(value = "qtitle", required = false) String queryTitle,
                               @RequestParam(value = "qstarttime", required = false) Date queryStartTime,
                               @RequestParam(value = "qendtime", required = false) Date queryEndTime,
                               @RequestParam(value = "qcreateuserid", required = false) String queryCreateUserId,
                               @RequestParam(value = "qstatus", required = false) String queryStatus,
                               @RequestParam(value = "qordertype", required = false) Integer queryOrderType
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("queryTitle", queryTitle);
        mapMessage.put("queryStatus", queryStatus);
        mapMessage.put("queryStartTime", queryStartTime);
        mapMessage.put("queryEndTime", queryEndTime);
        mapMessage.put("queryCreateUserId", queryCreateUserId);
        mapMessage.put("queryOrderType", queryOrderType);
        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
        mapMessage.put("page", pagination);
        try {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(SocialWatermarkField.ACTIVITY_ID, activityId));
            SocialWatermark watermark = ContentServiceSngl.get().getSocialWatermark(queryExpress);
            if (watermark != null) {
                ContentServiceSngl.get().modifySocialWatermark(new QueryExpress().add(QueryCriterions.eq(SocialWatermarkField.WATERMARK_ID, watermark.getWatermarkId())),
                        new UpdateExpress().set(SocialWatermarkField.ACTIVITY_ID, 0l));
            }
            ContentServiceSngl.get().modifySocialActivity(activityId, new UpdateExpress().set(SocialActivityField.REMOVE_STATUS, ValidStatus.REMOVED.getCode())
                    .set(SocialActivityField.MODIFY_DATE, new Date())
                    .set(SocialActivityField.MODIFY_IP, getIp())
                    .set(SocialActivityField.MODIFY_USERID, getCurrentUser().getUserid())
                    .set(SocialActivityField.BIND_STATUS, ActStatus.UNACT.getCode()));

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("forward:/joymeapp/social/activity/list", mapMessage);
        }
        return new ModelAndView("forward:/joymeapp/social/activity/list", mapMessage);
    }

    @RequestMapping(value = "/publish")
    public ModelAndView publish(@RequestParam(value = "aid", required = false) Long activityId,
                                @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                                @RequestParam(value = "maxPageItems", required = false, defaultValue = "5") int pageSize,
                                @RequestParam(value = "qtitle", required = false) String queryTitle,
                                @RequestParam(value = "qstarttime", required = false) Date queryStartTime,
                                @RequestParam(value = "qendtime", required = false) Date queryEndTime,
                                @RequestParam(value = "qcreateuserid", required = false) String queryCreateUserId,
                                @RequestParam(value = "qstatus", required = false) String queryStatus,
                                @RequestParam(value = "qordertype", required = false) Integer queryOrderType
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("queryTitle", queryTitle);
        mapMessage.put("queryStatus", queryStatus);
        mapMessage.put("queryStartTime", queryStartTime);
        mapMessage.put("queryEndTime", queryEndTime);
        mapMessage.put("queryCreateUserId", queryCreateUserId);
        mapMessage.put("queryOrderType", queryOrderType);
        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
        mapMessage.put("page", pagination);
        try {
            ContentServiceSngl.get().modifySocialActivity(activityId, new UpdateExpress().set(SocialActivityField.REMOVE_STATUS, ValidStatus.VALID.getCode())
                    .set(SocialActivityField.MODIFY_DATE, new Date())
                    .set(SocialActivityField.MODIFY_IP, getIp())
                    .set(SocialActivityField.MODIFY_USERID, getCurrentUser().getUserid()));

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("forward:/joymeapp/social/activity/list", mapMessage);
        }
        return new ModelAndView("forward:/joymeapp/social/activity/list", mapMessage);
    }

    @RequestMapping(value = "/recover")
    public ModelAndView recover(@RequestParam(value = "aid", required = false) Long activityId,
                                @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                                @RequestParam(value = "maxPageItems", required = false, defaultValue = "5") int pageSize,
                                @RequestParam(value = "qtitle", required = false) String queryTitle,
                                @RequestParam(value = "qstarttime", required = false) Date queryStartTime,
                                @RequestParam(value = "qendtime", required = false) Date queryEndTime,
                                @RequestParam(value = "qcreateuserid", required = false) String queryCreateUserId,
                                @RequestParam(value = "qstatus", required = false) String queryStatus,
                                @RequestParam(value = "qordertype", required = false) Integer queryOrderType
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("queryTitle", queryTitle);
        mapMessage.put("queryStatus", queryStatus);
        mapMessage.put("queryStartTime", queryStartTime);
        mapMessage.put("queryEndTime", queryEndTime);
        mapMessage.put("queryCreateUserId", queryCreateUserId);
        mapMessage.put("queryOrderType", queryOrderType);
        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
        mapMessage.put("page", pagination);
        try {
            ContentServiceSngl.get().modifySocialActivity(activityId, new UpdateExpress().set(SocialActivityField.REMOVE_STATUS, ValidStatus.VALID.getCode())
                    .set(SocialActivityField.MODIFY_DATE, new Date())
                    .set(SocialActivityField.MODIFY_IP, getIp())
                    .set(SocialActivityField.MODIFY_USERID, getCurrentUser().getUserid()));

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("forward:/joymeapp/social/activity/list", mapMessage);
        }
        return new ModelAndView("forward:/joymeapp/social/activity/list", mapMessage);
    }


    @RequestMapping(value = "/sort/{sort}")
    public ModelAndView sort(@PathVariable(value = "sort") String sort,
                             @RequestParam(value = "aid", required = false) Long activityId,
                             @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "5") int pageSize,
                             @RequestParam(value = "qtitle", required = false) String queryTitle,
                             @RequestParam(value = "qstarttime", required = false) Date queryStartTime,
                             @RequestParam(value = "qendtime", required = false) Date queryEndTime,
                             @RequestParam(value = "qcreateuserid", required = false) String queryCreateUserId,
                             @RequestParam(value = "qstatus", required = false) String queryStatus,
                             @RequestParam(value = "qordertype", required = false) Integer queryOrderType
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("queryTitle", queryTitle);
        mapMessage.put("queryStatus", queryStatus);
        mapMessage.put("queryStartTime", queryStartTime);
        mapMessage.put("queryEndTime", queryEndTime);
        mapMessage.put("queryCreateUserId", queryCreateUserId);
        mapMessage.put("queryOrderType", queryOrderType);
        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
        mapMessage.put("page", pagination);

        UpdateExpress updateExpress1 = new UpdateExpress();
        UpdateExpress updateExpress2 = new UpdateExpress();
        QueryExpress queryExpress = new QueryExpress();

        try {
            SocialActivity socialActivity = ContentServiceSngl.get().getSocialActivity(activityId);
            if (socialActivity == null) {
                return new ModelAndView("forward:/joymeapp/social/activity/list", mapMessage);
            }
            queryExpress.add(QueryCriterions.eq(SocialActivityField.REMOVE_STATUS, ValidStatus.VALID.getCode()));
            if (sort.equals("up")) {
                queryExpress.add(QueryCriterions.gt(SocialActivityField.DISPLAY_ORDER, socialActivity.getDisplayOrder()));
                queryExpress.add(QuerySort.add(SocialActivityField.DISPLAY_ORDER, QuerySortOrder.ASC));
            } else {
                queryExpress.add(QueryCriterions.lt(SocialActivityField.DISPLAY_ORDER, socialActivity.getDisplayOrder()));
                queryExpress.add(QuerySort.add(SocialActivityField.DISPLAY_ORDER, QuerySortOrder.DESC));
            }

            PageRows<SocialActivity> pageRows = ContentServiceSngl.get().querySocialActivityByPage(queryExpress, new Pagination(1, 1, 1));
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                updateExpress1.set(SocialActivityField.DISPLAY_ORDER, socialActivity.getDisplayOrder());
                ContentServiceSngl.get().modifySocialActivity(pageRows.getRows().get(0).getActivityId(), updateExpress1);

                updateExpress2.set(SocialActivityField.DISPLAY_ORDER, pageRows.getRows().get(0).getDisplayOrder());
                ContentServiceSngl.get().modifySocialActivity(socialActivity.getActivityId(), updateExpress2);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("forward:/joymeapp/social/activity/list", mapMessage);
        }
        return new ModelAndView("forward:/joymeapp/social/activity/list", mapMessage);
    }

    @RequestMapping(value = "/detail")
    public ModelAndView detail(@RequestParam(value = "aid", required = false) Long activityId
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            SocialActivity socialActivity = ContentServiceSngl.get().getSocialActivity(activityId);
            if (socialActivity == null) {
                return new ModelAndView("redirect:/joymeapp/social/activity/list");
            }
            mapMessage.put("activity", socialActivity);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("forward:/joymeapp/social/activity/list", mapMessage);
        }
        return new ModelAndView("/joymeapp/socialclient/activity/detail", mapMessage);
    }

}
