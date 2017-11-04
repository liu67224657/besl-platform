package com.enjoyf.webapps.tools.webpage.controller.joymeapp.socialclient;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.content.ContentServiceSngl;
import com.enjoyf.platform.service.content.social.*;
import com.enjoyf.platform.service.service.ServiceException;
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
@RequestMapping(value = "/joymeapp/social/watermark")
public class SocialWatermarkController extends ToolsBaseController {

    private static Set<Integer> orderType = new HashSet<Integer>();

    static {
        orderType.add(1);  //按使用数排序
    }

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "5") int pageSize,
                             @RequestParam(value = "qtitle", required = false) String queryTitle,
                             @RequestParam(value = "qstarttime", required = false) Date queryStartTime,
                             @RequestParam(value = "qendtime", required = false) Date queryEndTime,
                             @RequestParam(value = "qbindaid", required = false) Long queryBindAid,
                             @RequestParam(value = "qcreateuserid", required = false) String queryCreateUserId,
                             @RequestParam(value = "qstatus", required = false) String queryStatus,
                             @RequestParam(value = "qordertype", required = false) Integer queryOrderType

    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("queryTitle", queryTitle);
        mapMessage.put("queryStatus", queryStatus);
        mapMessage.put("queryStartTime", queryStartTime);
        mapMessage.put("queryEndTime", queryEndTime);
        mapMessage.put("queryBindAid", queryBindAid);
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
            queryExpress.add(QueryCriterions.eq(SocialWatermarkField.TITLE, queryTitle));
        }
        if (queryStartTime != null) {
            queryExpress.add(QueryCriterions.geq(SocialWatermarkField.CREATE_DATE, queryStartTime));
        }
        if (queryEndTime != null) {
            queryExpress.add(QueryCriterions.leq(SocialWatermarkField.CREATE_DATE, queryEndTime));
        }
        if (queryBindAid != null) {
            queryExpress.add(QueryCriterions.eq(SocialWatermarkField.ACTIVITY_ID, queryBindAid));
        }
        if (!StringUtil.isEmpty(queryCreateUserId)) {
            queryExpress.add(QueryCriterions.eq(SocialWatermarkField.CREATE_USERID, queryCreateUserId));
        }
        if (!StringUtil.isEmpty(queryStatus)) {
            queryExpress.add(QueryCriterions.eq(SocialWatermarkField.REMOVE_STATUS, queryStatus));
        } else {
            queryExpress.add(QueryCriterions.ne(SocialWatermarkField.REMOVE_STATUS, ValidStatus.REMOVED.getCode()));
        }
        if (queryOrderType != null) {
            if (queryOrderType == 1) {
                queryExpress.add(QuerySort.add(SocialWatermarkField.USE_SUM, QuerySortOrder.DESC));
            }
        } else {
            queryExpress.add(QuerySort.add(SocialWatermarkField.DISPLAY_ORDER, QuerySortOrder.DESC));
        }
        try {
            PageRows<SocialWatermark> pageRows = ContentServiceSngl.get().querySocialWatermarkByPage(queryExpress, pagination);
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                mapMessage.put("list", pageRows.getRows());
                mapMessage.put("page", pageRows.getPage());
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/joymeapp/socialclient/watermark/list", mapMessage);
        }
        return new ModelAndView("/joymeapp/socialclient/watermark/list", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createPage() {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("SubscriptTypeColl", SubscriptType.getAll());
        try {
            List<SocialActivity> activityList = ContentServiceSngl.get().querySocialActivity(new QueryExpress()
                    .add(QueryCriterions.eq(SocialActivityField.REMOVE_STATUS, ValidStatus.VALID.getCode())));
            mapMessage.put("activityList", activityList);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("redirect:/joymeapp/social/watermark/list");
        }
        return new ModelAndView("/joymeapp/socialclient/watermark/create", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "title", required = false) String title,
                               @RequestParam(value = "description", required = false) String description,
                               @RequestParam(value = "iosicon", required = false) String iosIcon,
                               @RequestParam(value = "androidicon", required = false) String androidIcon,
                               @RequestParam(value = "iospic", required = false) String iosPic,
                               @RequestParam(value = "androidpic", required = false) String androidPic,
                               @RequestParam(value = "startdate", required = false) Date startDate,
                               @RequestParam(value = "enddate", required = false) Date endDate,
                               @RequestParam(value = "subscripttype", required = false) Integer subscriptType,
                               @RequestParam(value = "activityid", required = false) Long activityId
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("title", title);
        mapMessage.put("description", description);
        mapMessage.put("iosIcon", iosIcon);
        mapMessage.put("androidIcon", androidIcon);
        mapMessage.put("iosPic", iosPic);
        mapMessage.put("androidPic", androidPic);
        mapMessage.put("startDate", startDate);
        mapMessage.put("endDate", endDate);
        mapMessage.put("subscriptType", subscriptType);
        mapMessage.put("activityId", activityId);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Subscript subscript = new Subscript();
        subscript.setStartDate(startDate == null ? null : df.format(startDate));
        subscript.setEndDate(endDate == null ? null : df.format(endDate));
        subscript.setType(subscriptType);

        SocialWatermark watermark = new SocialWatermark();
        watermark.setTitle(title);
        watermark.setDescription(description);
        watermark.setIosIcon(iosIcon);
        watermark.setAndroidIcon(androidIcon);
        watermark.setIosPic(iosPic);
        watermark.setAndroidPic(androidPic);
        watermark.setSubscript(subscript);
        watermark.setSubscriptType(SubscriptType.getByCode(subscriptType));
        if (activityId != null) {
            watermark.setActivityId(activityId);
        }
        watermark.setCreateDate(new Date());
        watermark.setCreateIp(getIp());
        watermark.setCreateUserId(getCurrentUser().getUserid());
        watermark.setRemoveStatus(ValidStatus.INVALID);
        try {
            ContentServiceSngl.get().insertSocialWatermark(watermark);
            if (activityId != null) {
                ContentServiceSngl.get().modifySocialActivity(activityId, new UpdateExpress().set(SocialActivityField.BIND_STATUS, ActStatus.ACTED.getCode()));
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/joymeapp/socialclient/watermark/create", mapMessage);
        }
        return new ModelAndView("redirect:/joymeapp/social/watermark/list");
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyPage(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,      //数据库记录索引
                                   @RequestParam(value = "maxPageItems", required = false, defaultValue = "5") int pageSize,
                                   @RequestParam(value = "wid", required = false) Long watermarkId,
                                   @RequestParam(value = "qtitle", required = false) String queryTitle,
                                   @RequestParam(value = "qstarttime", required = false) Date queryStartTime,
                                   @RequestParam(value = "qendtime", required = false) Date queryEndTime,
                                   @RequestParam(value = "qbindaid", required = false) Long queryBindAid,
                                   @RequestParam(value = "qcreateuserid", required = false) String queryCreateUserId,
                                   @RequestParam(value = "qstatus", required = false) String queryStatus,
                                   @RequestParam(value = "qordertype", required = false) Integer queryOrderType

    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("queryTitle", queryTitle);
        mapMessage.put("queryStatus", queryStatus);
        mapMessage.put("queryStartTime", queryStartTime);
        mapMessage.put("queryEndTime", queryEndTime);
        mapMessage.put("queryBindAid", queryBindAid);
        mapMessage.put("queryCreateUserId", queryCreateUserId);
        mapMessage.put("queryOrderType", queryOrderType);
        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
        mapMessage.put("page", pagination);

        mapMessage.put("SubscriptTypeColl", SubscriptType.getAll());

        try {
            List<SocialActivity> activityList = ContentServiceSngl.get().querySocialActivity(new QueryExpress()
                    .add(QueryCriterions.eq(SocialActivityField.REMOVE_STATUS, ValidStatus.VALID.getCode())));
            mapMessage.put("activityList", activityList);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("forward:/joymeapp/socialclient/watermark/list", mapMessage);
        }

        try {
            SocialWatermark watermark = ContentServiceSngl.get().getSocialWatermark(new QueryExpress().add(QueryCriterions.eq(SocialWatermarkField.WATERMARK_ID, watermarkId)));
            if (watermark != null) {
                mapMessage.put("watermark", watermark);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("forward:/joymeapp/socialclient/watermark/list", mapMessage);
        }

        return new ModelAndView("/joymeapp/socialclient/watermark/modify", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                               @RequestParam(value = "maxPageItems", required = false, defaultValue = "5") int pageSize,
                               @RequestParam(value = "wid", required = false) Long watermarkId,
                               @RequestParam(value = "qtitle", required = false) String queryTitle,
                               @RequestParam(value = "qstarttime", required = false) Date queryStartTime,
                               @RequestParam(value = "qendtime", required = false) Date queryEndTime,
                               @RequestParam(value = "qbindaid", required = false) Long queryBindAid,
                               @RequestParam(value = "qcreateuserid", required = false) String queryCreateUserId,
                               @RequestParam(value = "qstatus", required = false) String queryStatus,
                               @RequestParam(value = "qordertype", required = false) Integer queryOrderType,

                               @RequestParam(value = "title", required = false) String title,
                               @RequestParam(value = "description", required = false) String description,
                               @RequestParam(value = "iosicon", required = false) String iosIcon,
                               @RequestParam(value = "androidicon", required = false) String androidIcon,
                               @RequestParam(value = "iospic", required = false) String iosPic,
                               @RequestParam(value = "androidpic", required = false) String androidPic,
                               @RequestParam(value = "startdate", required = false) Date startDate,
                               @RequestParam(value = "enddate", required = false) Date endDate,
                               @RequestParam(value = "subscripttype", required = false) Integer subscriptType,
                               @RequestParam(value = "activityid", required = false) Long activityId

    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("queryTitle", queryTitle);
        mapMessage.put("queryStatus", queryStatus);
        mapMessage.put("queryStartTime", queryStartTime);
        mapMessage.put("queryEndTime", queryEndTime);
        mapMessage.put("queryBindAid", queryBindAid);
        mapMessage.put("queryCreateUserId", queryCreateUserId);
        mapMessage.put("queryOrderType", queryOrderType);
        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
        mapMessage.put("page", pagination);

        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(SocialWatermarkField.WATERMARK_ID, watermarkId));

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Subscript subscript = new Subscript();
        subscript.setStartDate(startDate == null ? null : df.format(startDate));
        subscript.setEndDate(endDate == null ? null : df.format(endDate));
        subscript.setType(subscriptType);

        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(SocialWatermarkField.TITLE, title);
        updateExpress.set(SocialWatermarkField.DESCRIPTION, description);
        updateExpress.set(SocialWatermarkField.IOS_ICON, iosIcon);
        updateExpress.set(SocialWatermarkField.ANDROID_ICON, androidIcon);
        updateExpress.set(SocialWatermarkField.IOS_PIC, iosPic);
        updateExpress.set(SocialWatermarkField.ANDROID_PIC, androidPic);
        updateExpress.set(SocialWatermarkField.SUBSCRIPT, subscript.toJsonStr());
        updateExpress.set(SocialWatermarkField.SUBSCRIPT_TYPE, subscriptType);
        updateExpress.set(SocialWatermarkField.ACTIVITY_ID, activityId);
        updateExpress.set(SocialWatermarkField.MODIFY_DATE, new Date());
        updateExpress.set(SocialWatermarkField.MODIFY_IP, getIp());
        updateExpress.set(SocialWatermarkField.MODIFY_USERID, getCurrentUser().getUserid());

        try {
            SocialWatermark watermark = ContentServiceSngl.get().getSocialWatermark(queryExpress);
            if (watermark == null) {
                return new ModelAndView("forward:/joymeapp/social/watermark/list", mapMessage);
            }
            if (activityId != null) {
                if (watermark.getActivityId() > 0l) {
                    if (watermark.getActivityId() != activityId) {
                        ContentServiceSngl.get().modifySocialActivity(activityId, new UpdateExpress().set(SocialActivityField.BIND_STATUS, ActStatus.ACTED.getCode()));
                        ContentServiceSngl.get().modifySocialActivity(watermark.getActivityId(), new UpdateExpress().set(SocialActivityField.BIND_STATUS, ActStatus.UNACT.getCode()));
                    }
                } else {
                    ContentServiceSngl.get().modifySocialActivity(activityId, new UpdateExpress().set(SocialActivityField.BIND_STATUS, ActStatus.ACTED.getCode()));
                }
            } else {
                if (watermark.getActivityId() > 0l) {
                    ContentServiceSngl.get().modifySocialActivity(watermark.getActivityId(), new UpdateExpress().set(SocialActivityField.BIND_STATUS, ActStatus.UNACT.getCode()));
                }
            }
            ContentServiceSngl.get().modifySocialWatermark(queryExpress, updateExpress);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("forward:/joymeapp/social/watermark/list", mapMessage);
        }
        return new ModelAndView("forward:/joymeapp/social/watermark/list", mapMessage);
    }

    @RequestMapping(value = "/remove")
    public ModelAndView remove(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                               @RequestParam(value = "maxPageItems", required = false, defaultValue = "5") int pageSize,
                               @RequestParam(value = "wid", required = false) Long watermarkId,
                               @RequestParam(value = "qtitle", required = false) String queryTitle,
                               @RequestParam(value = "qstarttime", required = false) Date queryStartTime,
                               @RequestParam(value = "qendtime", required = false) Date queryEndTime,
                               @RequestParam(value = "qbindaid", required = false) Long queryBindAid,
                               @RequestParam(value = "qcreateuserid", required = false) String queryCreateUserId,
                               @RequestParam(value = "qstatus", required = false) String queryStatus,
                               @RequestParam(value = "qordertype", required = false) Integer queryOrderType
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("queryTitle", queryTitle);
        mapMessage.put("queryStatus", queryStatus);
        mapMessage.put("queryStartTime", queryStartTime);
        mapMessage.put("queryEndTime", queryEndTime);
        mapMessage.put("queryBindAid", queryBindAid);
        mapMessage.put("queryCreateUserId", queryCreateUserId);
        mapMessage.put("queryOrderType", queryOrderType);
        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
        mapMessage.put("page", pagination);
        try {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(SocialWatermarkField.WATERMARK_ID, watermarkId));
            SocialWatermark watermark = ContentServiceSngl.get().getSocialWatermark(queryExpress);
            if (watermark == null) {
                return new ModelAndView("forward:/joymeapp/social/watermark/list", mapMessage);
            }
            if (watermark.getActivityId() > 0l) {
                ContentServiceSngl.get().modifySocialActivity(watermark.getActivityId(), new UpdateExpress().set(SocialActivityField.BIND_STATUS, ActStatus.UNACT.getCode()));
            }

            ContentServiceSngl.get().modifySocialWatermark(new QueryExpress().add(QueryCriterions.eq(SocialWatermarkField.WATERMARK_ID, watermarkId)),
                    new UpdateExpress().set(SocialWatermarkField.REMOVE_STATUS, ValidStatus.REMOVED.getCode())
                            .set(SocialWatermarkField.MODIFY_DATE, new Date())
                            .set(SocialWatermarkField.MODIFY_IP, getIp())
                            .set(SocialWatermarkField.MODIFY_USERID, getCurrentUser().getUserid())
                            .set(SocialWatermarkField.ACTIVITY_ID, 0l));

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("forward:/joymeapp/social/watermark/list", mapMessage);
        }
        return new ModelAndView("forward:/joymeapp/social/watermark/list", mapMessage);
    }

    @RequestMapping(value = "/recover")
    public ModelAndView recover(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                                @RequestParam(value = "maxPageItems", required = false, defaultValue = "5") int pageSize,
                                @RequestParam(value = "wid", required = false) Long watermarkId,
                                @RequestParam(value = "qtitle", required = false) String queryTitle,
                                @RequestParam(value = "qstarttime", required = false) Date queryStartTime,
                                @RequestParam(value = "qendtime", required = false) Date queryEndTime,
                                @RequestParam(value = "qbindaid", required = false) Long queryBindAid,
                                @RequestParam(value = "qcreateuserid", required = false) String queryCreateUserId,
                                @RequestParam(value = "qstatus", required = false) String queryStatus,
                                @RequestParam(value = "qordertype", required = false) Integer queryOrderType
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("queryTitle", queryTitle);
        mapMessage.put("queryStatus", queryStatus);
        mapMessage.put("queryStartTime", queryStartTime);
        mapMessage.put("queryEndTime", queryEndTime);
        mapMessage.put("queryBindAid", queryBindAid);
        mapMessage.put("queryCreateUserId", queryCreateUserId);
        mapMessage.put("queryOrderType", queryOrderType);
        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
        mapMessage.put("page", pagination);
        try {
            ContentServiceSngl.get().modifySocialWatermark(new QueryExpress().add(QueryCriterions.eq(SocialWatermarkField.WATERMARK_ID, watermarkId)),
                    new UpdateExpress().set(SocialWatermarkField.REMOVE_STATUS, ValidStatus.VALID.getCode())
                            .set(SocialWatermarkField.MODIFY_DATE, new Date())
                            .set(SocialWatermarkField.MODIFY_IP, getIp())
                            .set(SocialWatermarkField.MODIFY_USERID, getCurrentUser().getUserid()));

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("forward:/joymeapp/social/watermark/list", mapMessage);
        }
        return new ModelAndView("forward:/joymeapp/social/watermark/list", mapMessage);
    }

    @RequestMapping(value = "/publish")
    public ModelAndView publish(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                                @RequestParam(value = "maxPageItems", required = false, defaultValue = "5") int pageSize,
                                @RequestParam(value = "wid", required = false) Long watermarkId,
                                @RequestParam(value = "qtitle", required = false) String queryTitle,
                                @RequestParam(value = "qstarttime", required = false) Date queryStartTime,
                                @RequestParam(value = "qendtime", required = false) Date queryEndTime,
                                @RequestParam(value = "qbindaid", required = false) Long queryBindAid,
                                @RequestParam(value = "qcreateuserid", required = false) String queryCreateUserId,
                                @RequestParam(value = "qstatus", required = false) String queryStatus,
                                @RequestParam(value = "qordertype", required = false) Integer queryOrderType
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("queryTitle", queryTitle);
        mapMessage.put("queryStatus", queryStatus);
        mapMessage.put("queryStartTime", queryStartTime);
        mapMessage.put("queryEndTime", queryEndTime);
        mapMessage.put("queryBindAid", queryBindAid);
        mapMessage.put("queryCreateUserId", queryCreateUserId);
        mapMessage.put("queryOrderType", queryOrderType);
        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
        mapMessage.put("page", pagination);
        try {
            ContentServiceSngl.get().modifySocialWatermark(new QueryExpress().add(QueryCriterions.eq(SocialWatermarkField.WATERMARK_ID, watermarkId)),
                    new UpdateExpress().set(SocialWatermarkField.REMOVE_STATUS, ValidStatus.VALID.getCode())
                            .set(SocialWatermarkField.MODIFY_DATE, new Date())
                            .set(SocialWatermarkField.MODIFY_IP, getIp())
                            .set(SocialWatermarkField.MODIFY_USERID, getCurrentUser().getUserid()));

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("forward:/joymeapp/social/watermark/list", mapMessage);
        }
        return new ModelAndView("forward:/joymeapp/social/watermark/list", mapMessage);
    }

    @RequestMapping(value = "/sort/{sort}")
    public ModelAndView sort(@PathVariable(value = "sort") String sort,
                             @RequestParam(value = "wid", required = false) Long watermarkId,
                             @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "5") int pageSize,
                             @RequestParam(value = "qtitle", required = false) String queryTitle,
                             @RequestParam(value = "qstarttime", required = false) Date queryStartTime,
                             @RequestParam(value = "qendtime", required = false) Date queryEndTime,
                             @RequestParam(value = "qbindaid", required = false) Long queryBindAid,
                             @RequestParam(value = "qcreateuserid", required = false) String queryCreateUserId,
                             @RequestParam(value = "qstatus", required = false) String queryStatus,
                             @RequestParam(value = "qordertype", required = false) Integer queryOrderType
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("queryTitle", queryTitle);
        mapMessage.put("queryStatus", queryStatus);
        mapMessage.put("queryStartTime", queryStartTime);
        mapMessage.put("queryEndTime", queryEndTime);
        mapMessage.put("queryBindAid", queryBindAid);
        mapMessage.put("queryCreateUserId", queryCreateUserId);
        mapMessage.put("queryOrderType", queryOrderType);
        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
        mapMessage.put("page", pagination);

        UpdateExpress updateExpress1 = new UpdateExpress();
        UpdateExpress updateExpress2 = new UpdateExpress();
        QueryExpress queryExpress = new QueryExpress();

        try {
            SocialWatermark watermark = ContentServiceSngl.get().getSocialWatermark(new QueryExpress().add(QueryCriterions.eq(SocialWatermarkField.WATERMARK_ID, watermarkId)));
            if (watermark == null) {
                return new ModelAndView("forward:/joymeapp/social/watermark/list", mapMessage);
            }
            queryExpress.add(QueryCriterions.eq(SocialWatermarkField.REMOVE_STATUS, ValidStatus.VALID.getCode()));
            if (sort.equals("up")) {
                queryExpress.add(QueryCriterions.gt(SocialWatermarkField.DISPLAY_ORDER, watermark.getDisplayOrder()));
                queryExpress.add(QuerySort.add(SocialWatermarkField.DISPLAY_ORDER, QuerySortOrder.ASC));
            } else {
                queryExpress.add(QueryCriterions.lt(SocialWatermarkField.DISPLAY_ORDER, watermark.getDisplayOrder()));
                queryExpress.add(QuerySort.add(SocialWatermarkField.DISPLAY_ORDER, QuerySortOrder.DESC));
            }

            PageRows<SocialWatermark> pageRows = ContentServiceSngl.get().querySocialWatermarkByPage(queryExpress, new Pagination(1, 1, 1));
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                updateExpress1.set(SocialWatermarkField.DISPLAY_ORDER, watermark.getDisplayOrder());
                ContentServiceSngl.get().modifySocialWatermark(new QueryExpress().add(QueryCriterions.eq(SocialWatermarkField.WATERMARK_ID, pageRows.getRows().get(0).getWatermarkId())), updateExpress1);

                updateExpress2.set(SocialWatermarkField.DISPLAY_ORDER, pageRows.getRows().get(0).getDisplayOrder());
                ContentServiceSngl.get().modifySocialWatermark(new QueryExpress().add(QueryCriterions.eq(SocialWatermarkField.WATERMARK_ID, watermark.getWatermarkId())), updateExpress2);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("forward:/joymeapp/social/watermark/list", mapMessage);
        }
        return new ModelAndView("forward:/joymeapp/social/watermark/list", mapMessage);
    }

}
