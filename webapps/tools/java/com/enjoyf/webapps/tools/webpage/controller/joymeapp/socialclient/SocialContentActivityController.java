package com.enjoyf.webapps.tools.webpage.controller.joymeapp.socialclient;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.content.ContentServiceSngl;
import com.enjoyf.platform.service.content.social.SocialActivity;
import com.enjoyf.platform.service.content.social.SocialContent;
import com.enjoyf.platform.service.content.social.SocialContentActivity;
import com.enjoyf.platform.service.content.social.SocialContentActivityField;
import com.enjoyf.platform.service.profile.ProfileServiceSngl;
import com.enjoyf.platform.service.profile.socialclient.SocialProfile;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.webapps.tools.weblogic.dto.joymeApp.social.SocialContentActivityDTO;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-4-19
 * Time: 下午8:33
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/joymeapp/social/activity/content")
public class SocialContentActivityController extends ToolsBaseController {

    private static Set<Integer> orderType = new HashSet<Integer>();

    static {
        orderType.add(1);  //按使用数排序
        orderType.add(2);  //按回复数派寻
        orderType.add(3);  //按点赞数排序
        orderType.add(4);  //按礼物数排序
    }

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "aid", required = false) Long activityId,
                             @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "20") int pageSize,
                             @RequestParam(value = "screenname", required = false) String screenName,
                             @RequestParam(value = "cid", required = false) Long contentId
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("activityId", activityId);
        mapMessage.put("screenName", screenName);
        mapMessage.put("contentId", contentId);

        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);

        try {
            SocialActivity socialActivity = ContentServiceSngl.get().getSocialActivity(activityId);
            if (socialActivity == null) {
                return new ModelAndView("redirect:/joymeapp/social/activity/list");
            }
            mapMessage.put("activity", socialActivity);

            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(SocialContentActivityField.ACTIVITY_ID, activityId));
            if (contentId != null) {
                queryExpress.add(QueryCriterions.eq(SocialContentActivityField.CONTENT_ID, contentId));
            }
            if (!StringUtil.isEmpty(screenName)) {
                SocialProfile profile = ProfileServiceSngl.get().getSocialProfileByScreenName(screenName);
                queryExpress.add(QueryCriterions.eq(SocialContentActivityField.CONTENT_UNO, profile.getUno()));
            }
            queryExpress.add(QueryCriterions.eq(SocialContentActivityField.REMOVE_STATUS, ValidStatus.VALID.getCode()));
            queryExpress.add(QuerySort.add(SocialContentActivityField.DISPLAY_ORDER, QuerySortOrder.ASC));
            PageRows<SocialContentActivity> pageRows = ContentServiceSngl.get().querySocialContentActivity(queryExpress, pagination);
            if (pageRows == null || CollectionUtil.isEmpty(pageRows.getRows())) {
                return new ModelAndView("/joymeapp/socialclient/activity/content/list", mapMessage);
            }
            Set<Long> contentIdSet = new HashSet<Long>();
            Set<String> unoSet = new HashSet<String>();
            for (SocialContentActivity sca : pageRows.getRows()) {
                contentIdSet.add(sca.getContentId());
                unoSet.add(sca.getContentUno());
            }
            Map<Long, SocialContent> contentMap = ContentServiceSngl.get().querySocialContentMapByIdSet(contentIdSet);
            List<SocialProfile> profileList = ProfileServiceSngl.get().querySocialProfilesByUnos(unoSet);
            if (!CollectionUtil.isEmpty(profileList)) {
                mapMessage.put("profileList", profileList);
            }
            List<SocialContentActivityDTO> returnList = new LinkedList<SocialContentActivityDTO>();
            for (SocialContentActivity sca : pageRows.getRows()) {
                SocialContent content = contentMap.get(sca.getContentId());
                if (content == null) {
                    continue;
                }
                SocialContentActivityDTO dto = new SocialContentActivityDTO();
                dto.setScaId(sca.getContentActivityId());
                dto.setActivityId(sca.getActivityId());
                dto.setContentId(content.getContentId());
                dto.setUno(content.getUno());
                dto.setBody(content.getBody());
                dto.setPic(content.getPic().getPic_s());
                dto.setDisplayOrder(sca.getDisplayOrder());
                dto.setCreateDate(sca.getCreateDate());
                dto.setModifyDate(sca.getLastModifyDate());
                dto.setModifyIp(sca.getLastModifyIp());
                dto.setModifyUserId(sca.getLastModifyUserId());
                returnList.add(dto);
            }
            if (CollectionUtil.isEmpty(returnList)) {
                return new ModelAndView("/joymeapp/socialclient/activity/content/list", mapMessage);
            }
            mapMessage.put("list", returnList);
            mapMessage.put("page", pageRows.getPage());
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("forward:/joymeapp/social/activity/list", mapMessage);
        }
        return new ModelAndView("/joymeapp/socialclient/activity/content/list", mapMessage);
    }

    @RequestMapping(value = "/top")
    public ModelAndView top(@RequestParam(value = "aid", required = false) Long activityId,
                            @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                            @RequestParam(value = "maxPageItems", required = false, defaultValue = "20") int pageSize,
                            @RequestParam(value = "cid", required = false) Long contentId,
                            @RequestParam(value = "screenname", required = false) String screenName
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination page = new Pagination(pageSize * curPage, curPage, pageSize);
        mapMessage.put("page", page);
        mapMessage.put("aid", activityId);

        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(SocialContentActivityField.REMOVE_STATUS, ValidStatus.VALID.getCode()));
        queryExpress.add(QuerySort.add(SocialContentActivityField.DISPLAY_ORDER, QuerySortOrder.ASC));
        try {
            PageRows<SocialContentActivity> pageRows = ContentServiceSngl.get().querySocialContentActivity(queryExpress, new Pagination(1, 1, 1));
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                long minOrder = pageRows.getRows().get(0).getDisplayOrder();
                UpdateExpress updateExpress = new UpdateExpress();
                if (minOrder >= 0l) {
                    updateExpress.set(SocialContentActivityField.DISPLAY_ORDER, -1l);
                } else {
                    updateExpress.set(SocialContentActivityField.DISPLAY_ORDER, minOrder - 1l);
                }
                updateExpress.set(SocialContentActivityField.MODIFY_DATE, new Date());
                updateExpress.set(SocialContentActivityField.MODIFY_IP, getIp());
                updateExpress.set(SocialContentActivityField.MODIFY_USERID, getCurrentUser().getUserid());
                ContentServiceSngl.get().modifySocialContentActivity(activityId, contentId, updateExpress);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("forward:/joymeapp/social/activity/content/list", mapMessage);
        }
        return new ModelAndView("redirect:/joymeapp/social/activity/content/list?aid=" + activityId + "&pager.offset=" + page.getStartRowIdx() + "&maxPageItems=" + page.getPageSize());
    }

    @RequestMapping(value = "/untop")
    public ModelAndView unTop(@RequestParam(value = "aid", required = false) Long activityId,
                              @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                              @RequestParam(value = "maxPageItems", required = false, defaultValue = "20") int pageSize,
                              @RequestParam(value = "cid", required = false) Long contentId,
                              @RequestParam(value = "screenname", required = false) String screenName
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination page = new Pagination(pageSize * curPage, curPage, pageSize);
        mapMessage.put("page", page);
        mapMessage.put("aid", activityId);

        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(SocialContentActivityField.REMOVE_STATUS, ValidStatus.VALID.getCode()));
        queryExpress.add(QueryCriterions.gt(SocialContentActivityField.DISPLAY_ORDER, 0l));
        queryExpress.add(QuerySort.add(SocialContentActivityField.DISPLAY_ORDER, QuerySortOrder.ASC));
        try {
            PageRows<SocialContentActivity> pageRows = ContentServiceSngl.get().querySocialContentActivity(queryExpress, new Pagination(1, 1, 1));
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(SocialContentActivityField.MODIFY_DATE, new Date());
            updateExpress.set(SocialContentActivityField.MODIFY_IP, getIp());
            updateExpress.set(SocialContentActivityField.MODIFY_USERID, getCurrentUser().getUserid());
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                long minOrder = pageRows.getRows().get(0).getDisplayOrder();
                updateExpress.set(SocialContentActivityField.DISPLAY_ORDER, minOrder - 1l);
                ContentServiceSngl.get().modifySocialContentActivity(activityId, contentId, updateExpress);
            } else {
                updateExpress.set(SocialContentActivityField.DISPLAY_ORDER, (long) Integer.MAX_VALUE);
                ContentServiceSngl.get().modifySocialContentActivity(activityId, contentId, updateExpress);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("forward:/joymeapp/social/activity/content/list", mapMessage);
        }
        return new ModelAndView("redirect:/joymeapp/social/activity/content/list?aid=" + activityId + "&pager.offset=" + page.getStartRowIdx() + "&maxPageItems=" + page.getPageSize());
    }

    @RequestMapping(value = "/sort/{sort}")
    public ModelAndView contentSort(@PathVariable(value = "sort") String sort,
                                    @RequestParam(value = "aid", required = false) Long activityId,
                                    @RequestParam(value = "cid", required = false) Long contentId,
                                    @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                                    @RequestParam(value = "maxPageItems", required = false, defaultValue = "20") int pageSize
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination page = new Pagination(pageSize * curPage, curPage, pageSize);

        UpdateExpress updateExpress1 = new UpdateExpress();
        UpdateExpress updateExpress2 = new UpdateExpress();
        QueryExpress queryExpress = new QueryExpress();

        try {
            SocialContentActivity contentActivity = ContentServiceSngl.get().getSocialContentActivity(activityId, contentId);
            if (contentActivity == null) {
                return new ModelAndView("redirect:/joymeapp/social/activity/content/list?aid=" + activityId + "&pager.offset=" + page.getStartRowIdx() + "&maxPageItems=" + page.getPageSize());
            }
            queryExpress.add(QueryCriterions.eq(SocialContentActivityField.REMOVE_STATUS, ValidStatus.VALID.getCode()));
            if (sort.equals("up")) {
                queryExpress.add(QueryCriterions.lt(SocialContentActivityField.DISPLAY_ORDER, contentActivity.getDisplayOrder()));
                queryExpress.add(QuerySort.add(SocialContentActivityField.DISPLAY_ORDER, QuerySortOrder.DESC));
            } else {
                queryExpress.add(QueryCriterions.gt(SocialContentActivityField.DISPLAY_ORDER, contentActivity.getDisplayOrder()));
                queryExpress.add(QuerySort.add(SocialContentActivityField.DISPLAY_ORDER, QuerySortOrder.ASC));
            }

            PageRows<SocialContentActivity> pageRows = ContentServiceSngl.get().querySocialContentActivity(queryExpress, new Pagination(1, 1, 1));
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                updateExpress1.set(SocialContentActivityField.DISPLAY_ORDER, contentActivity.getDisplayOrder());
                ContentServiceSngl.get().modifySocialContentActivity(pageRows.getRows().get(0).getActivityId(), pageRows.getRows().get(0).getContentId(), updateExpress1);

                updateExpress2.set(SocialContentActivityField.DISPLAY_ORDER, pageRows.getRows().get(0).getDisplayOrder());
                ContentServiceSngl.get().modifySocialContentActivity(contentActivity.getActivityId(), contentActivity.getContentId(), updateExpress2);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("redirect:/joymeapp/social/activity/content/list?aid=" + activityId + "&pager.offset=" + page.getStartRowIdx() + "&maxPageItems=" + page.getPageSize());
        }
        return new ModelAndView("redirect:/joymeapp/social/activity/content/list?aid=" + activityId + "&pager.offset=" + page.getStartRowIdx() + "&maxPageItems=" + page.getPageSize());
    }

    @RequestMapping(value = "/sink")
    public ModelAndView sink(@RequestParam(value = "aid", required = false) Long activityId,
                             @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "20") int pageSize,
                             @RequestParam(value = "cid", required = false) Long contentId,
                             @RequestParam(value = "screenname", required = false) String screenName
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination page = new Pagination(pageSize * curPage, curPage, pageSize);
        mapMessage.put("page", page);
        mapMessage.put("aid", activityId);

        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.gt(SocialContentActivityField.DISPLAY_ORDER, (long) Integer.MAX_VALUE));
        queryExpress.add(QueryCriterions.eq(SocialContentActivityField.REMOVE_STATUS, ValidStatus.VALID.getCode()));
        queryExpress.add(QuerySort.add(SocialContentActivityField.DISPLAY_ORDER, QuerySortOrder.DESC));
        try {
            PageRows<SocialContentActivity> pageRows = ContentServiceSngl.get().querySocialContentActivity(queryExpress, new Pagination(1, 1, 1));
            UpdateExpress updateExpress = new UpdateExpress();
            if (pageRows == null || CollectionUtil.isEmpty(pageRows.getRows())) {
                updateExpress.set(SocialContentActivityField.DISPLAY_ORDER, (long) Integer.MAX_VALUE + 1l);
            } else if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                long sinkOrder = pageRows.getRows().get(0).getDisplayOrder();
                updateExpress.set(SocialContentActivityField.DISPLAY_ORDER, sinkOrder + 1l);
            }
            updateExpress.set(SocialContentActivityField.MODIFY_DATE, new Date());
            updateExpress.set(SocialContentActivityField.MODIFY_IP, getIp());
            updateExpress.set(SocialContentActivityField.MODIFY_USERID, getCurrentUser().getUserid());
            ContentServiceSngl.get().modifySocialContentActivity(activityId, contentId, updateExpress);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("forward:/joymeapp/social/activity/content/list", mapMessage);
        }
        return new ModelAndView("redirect:/joymeapp/social/activity/content/list?aid=" + activityId + "&pager.offset=" + page.getStartRowIdx() + "&maxPageItems=" + page.getPageSize());
    }

    @RequestMapping(value = "/float")
    public ModelAndView floating(@RequestParam(value = "aid", required = false) Long activityId,
                                 @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                                 @RequestParam(value = "maxPageItems", required = false, defaultValue = "20") int pageSize,
                                 @RequestParam(value = "cid", required = false) Long contentId,
                                 @RequestParam(value = "screenname", required = false) String screenName
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination page = new Pagination(pageSize * curPage, curPage, pageSize);
        mapMessage.put("page", page);
        mapMessage.put("aid", activityId);

        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(SocialContentActivityField.REMOVE_STATUS, ValidStatus.VALID.getCode()));
        queryExpress.add(QueryCriterions.gt(SocialContentActivityField.DISPLAY_ORDER, 0l));
        queryExpress.add(QuerySort.add(SocialContentActivityField.DISPLAY_ORDER, QuerySortOrder.ASC));
        try {
            PageRows<SocialContentActivity> pageRows = ContentServiceSngl.get().querySocialContentActivity(queryExpress, new Pagination(1, 1, 1));
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(SocialContentActivityField.MODIFY_DATE, new Date());
            updateExpress.set(SocialContentActivityField.MODIFY_IP, getIp());
            updateExpress.set(SocialContentActivityField.MODIFY_USERID, getCurrentUser().getUserid());
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                long minOrder = pageRows.getRows().get(0).getDisplayOrder();
                updateExpress.set(SocialContentActivityField.DISPLAY_ORDER, minOrder - 1l);
            } else {
                updateExpress.set(SocialContentActivityField.DISPLAY_ORDER, (long) Integer.MAX_VALUE);
            }
            ContentServiceSngl.get().modifySocialContentActivity(activityId, contentId, updateExpress);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("forward:/joymeapp/social/activity/content/list", mapMessage);
        }
        return new ModelAndView("redirect:/joymeapp/social/activity/content/list?aid=" + activityId + "&pager.offset=" + page.getStartRowIdx() + "&maxPageItems=" + page.getPageSize());
    }

}
