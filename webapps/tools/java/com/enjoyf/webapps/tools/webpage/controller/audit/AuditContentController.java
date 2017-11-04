package com.enjoyf.webapps.tools.webpage.controller.audit;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.content.*;
import com.enjoyf.platform.service.profile.ProfileBlog;
import com.enjoyf.platform.service.profile.ProfileServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.ContentAuditStatus;
import com.enjoyf.platform.service.tools.ContentReplyAuditStatus;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.service.tools.ToolsLog;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.webapps.tools.weblogic.content.*;
import com.enjoyf.webapps.tools.weblogic.dto.ContentDTO;
import com.enjoyf.webapps.tools.webpage.controller.ContentAuditUtil;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.*;

/**
 * Author: zhaoxin
 * Date: 11-10-26
 * Time: 下午1:48
 * Desc:  文章文本内容审核 Controller
 */
@Controller
@RequestMapping(value = "/audit/content")
public class AuditContentController extends ToolsBaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final Integer UNAUDIT_SELECTED_VALUE = 0;
    private static final String SPLITER_OF_CONTENTIDS = "@";
    private static final int NUM_OF_INCREASE_DAY = -5;


    @Resource(name = "contentWebLogic")
    private ContentWebLogic contentWebLogic;

    private StatusStrategy statusStrategy;

    private static Map<Integer, String> contentStatus = new HashMap<Integer, String>();
    private static Map<Integer, String> replyStatus = new HashMap<Integer, String>();
    private static Map<Integer, String> imageStatus = new HashMap<Integer, String>();

    static {
        contentStatus.put(0, "def.content.page.select.option1");
        contentStatus.put(ContentAuditStatus.AUDIT_TEXT, "def.content.page.select.option2");
        contentStatus.put(ContentAuditStatus.AUDIT_TEXT + ContentAuditStatus.ILLEGAL_TEXT, "def.content.page.select.option3");

        replyStatus.put(0, "def.content.page.select.option1");
        replyStatus.put(ContentReplyAuditStatus.AUDIT_REPLY, "def.content.page.select.option2");
        replyStatus.put(ContentReplyAuditStatus.ILLEGAL_REPLY + ContentReplyAuditStatus.AUDIT_REPLY, "def.content.page.select.option3");

        imageStatus.put(0, "def.content.page.select.option1");
        imageStatus.put(ContentAuditStatus.AUDIT_IMG, "def.content.page.select.option2");
        imageStatus.put(ContentAuditStatus.AUDIT_IMG + ContentAuditStatus.ILLEGAL_IMG, "def.content.page.select.option3");
    }


    @RequestMapping(value = "/textlist")
    public ModelAndView queryContentTextList(
            @RequestParam(value = "startdate", required = false) String startDate,
            @RequestParam(value = "enddate", required = false) String endDate,
            @RequestParam(value = "screenname", required = false) String screenName,
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "auditstatus", required = false, defaultValue = "0") Integer auditStatus,
            @RequestParam(value = "items", required = false, defaultValue = "0") Integer items,
            @RequestParam(value = "pager.offset", required = false, defaultValue = "0") Integer pagerOffset,
            @RequestParam(value = "maxPageItems", required = false, defaultValue = "1") Integer maxPageItems) {

        Pagination pagination = new Pagination(items, pagerOffset / maxPageItems + 1, WebappConfig.get().getHomePageSize());

        Map<String, Object> mapMsg = new HashMap<String, Object>();
        ContentQueryParam param = new ContentQueryParam();
        QueryExpress queryExpress = new QueryExpress();


        //
        if (StringUtil.isEmpty(startDate) && StringUtil.isEmpty(endDate)) {
            param.setEndDate(DateUtil.StringTodate(DateUtil.DateToString(new Date(), DateUtil.DATE_FORMAT) + " 23:59:59",
                    DateUtil.DEFAULT_DATE_FORMAT2));
            param.setStartDate(DateUtil.adjustDate(DateUtil.ignoreTime(new Date()), Calendar.DAY_OF_MONTH, -5));
        } else {
            //处理时间参数对象
            ContentAuditUtil.handleDateParam(param, new String[]{"startDate", "endDate"}, ContentAuditUtil.sortASC(startDate, endDate, NUM_OF_INCREASE_DAY));
        }

        //
        if (!StringUtil.isEmpty(screenName)) {
            queryExpress.add(QueryCriterions.eq(ContentField.UNO, getUno(screenName)));
        }

        if (!StringUtil.isEmpty(key)) {
//            queryExpress.add(QueryCriterions.like(ContentField.CONTENTBODY, "%" + key + "%"));
            queryExpress.add(QueryCriterions.or(QueryCriterions.like(ContentField.CONTENTBODY, "%" + key + "%"),
                    QueryCriterions.like(ContentField.CONTENTSUBJECT, "%" + key + "%")));
        }

        if (auditStatus != null) {

            if (auditStatus.equals(UNAUDIT_SELECTED_VALUE)) {
                queryExpress.add(QueryCriterions.bitwiseAnd(ContentField.AUDITSTATUS, QueryCriterionRelation.EQ, ContentAuditStatus.AUDIT_TEXT, UNAUDIT_SELECTED_VALUE));
                //用户自己未删除的文章
                queryExpress.add(QueryCriterions.eq(ContentField.REMOVESTATUS, ActStatus.UNACT.getCode()));
            } else if (auditStatus == 1) {
                queryExpress.add(QueryCriterions.bitwiseAnd(ContentField.AUDITSTATUS, QueryCriterionRelation.EQ, ContentAuditStatus.ILLEGAL_TEXT, 0))
                        .add(QueryCriterions.bitwiseAnd(ContentField.AUDITSTATUS, QueryCriterionRelation.EQ, ContentAuditStatus.AUDIT_TEXT, ContentAuditStatus.AUDIT_TEXT));

                queryExpress.add(QueryCriterions.eq(ContentField.REMOVESTATUS, ActStatus.UNACT.getCode()));
            } else {
                queryExpress.add(QueryCriterions.bitwiseAnd(ContentField.AUDITSTATUS, QueryCriterionRelation.EQ, ContentAuditStatus.AUDIT_TEXT + ContentAuditStatus.ILLEGAL_TEXT, ContentAuditStatus.AUDIT_TEXT + ContentAuditStatus.ILLEGAL_TEXT));

            }

        }


        queryExpress.add(QueryCriterions.between(ContentField.PUBLISHDATE, param.getStartDate(), param.getEndDate()));
        queryExpress.add(QuerySort.add(ContentField.PUBLISHDATE, QuerySortOrder.DESC));


        //
        try {
            PageRows<ContentDTO> pageRows = contentWebLogic.queryContents(queryExpress, pagination);

            mapMsg.put("rows", pageRows.getRows());
            mapMsg.put("page", pageRows.getPage());
            mapMsg.put("params", param);
            mapMsg.put("screenname", screenName);
            mapMsg.put("key", key);
            mapMsg.put("auditstatus", auditStatus);
            mapMsg.put("contentStatus", contentStatus);

        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
            GAlerter.lab("queryContentTextList a Controller SQLException :", e);
        }

        return new ModelAndView("/content/contentlist", mapMsg);
    }


    @RequestMapping(value = "/batchupdate")
    public ModelAndView batchUpdateContentText(
            @RequestParam(value = "contentids", required = false) String[] contentIds,
            @RequestParam(value = "updateRemoveStatusCode", required = false, defaultValue = "0") Integer updateRemoveStatusCode,
            @RequestParam(value = "startdate", required = false) String startDate,
            @RequestParam(value = "enddate", required = false) String endDate,
            @RequestParam(value = "screenname", required = false) String screenName,
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "auditstatus", required = false) Integer auditStatus,
            @RequestParam(value = "items", required = false, defaultValue = "0") Integer items,
            @RequestParam(value = "pager.offset", required = false, defaultValue = "0") Integer pagerOffset,
            @RequestParam(value = "maxPageItems", required = false, defaultValue = "1") Integer maxPageItems) {

        Pagination pagination = new Pagination(items, pagerOffset / maxPageItems + 1, WebappConfig.get().getHomePageSize());
        ContentQueryParam param = new ContentQueryParam();

        if (StringUtil.isEmpty(startDate) && StringUtil.isEmpty(endDate)) {
            param.setEndDate(DateUtil.StringTodate(DateUtil.DateToString(new Date(), DateUtil.DATE_FORMAT) + " 23:59:59",
                    DateUtil.DEFAULT_DATE_FORMAT2));
            param.setStartDate(DateUtil.adjustDate(DateUtil.ignoreTime(new Date()), Calendar.DAY_OF_MONTH, -5));
        } else {
            //处理时间参数对象
            ContentAuditUtil.handleDateParam(param, new String[]{"startDate", "endDate"}, ContentAuditUtil.sortASC(startDate, endDate, NUM_OF_INCREASE_DAY));
        }

        Map<String, Object> msgMap = new HashMap<String, Object>();
        msgMap.put("page", pagination);
        msgMap.put("params", param);
        msgMap.put("screenname", screenName);
        msgMap.put("key", key);
        msgMap.put("auditstatus", auditStatus);
        msgMap.put("contentStatus", contentStatus);
        msgMap.put("updateRemoveStatusCode", updateRemoveStatusCode);

        if (contentIds != null && contentIds.length != 0) {
            try {
//                if (!StringUtil.isEmpty(screenName)) {
//                    screenName = URLDecoder.decode(screenName, "UTF-8");
//                }

                for (String contentIdUnoValue : contentIds) {
                    String[] values = contentIdUnoValue.split(SPLITER_OF_CONTENTIDS);

//                    Content content = contentWebLogic.queryContentByIdScreenName(values[0], values[1]);
                    Integer originalValue = Integer.parseInt(values[2]);
                    //注意：依赖原来的值(页面传来)的计算，原来的值最好不要是从页面传过来的
                    //这个方法原来的值是从页面传过来的，这样做是为了减少数据库访问的次数，但是同时增大了风险
                    ContentAuditStatus contentAuditStatus = ContentAuditStatus.getByValue(originalValue);
                    Integer statusValue = null;

                    if (updateRemoveStatusCode != 0 && updateRemoveStatusCode == 2) {
                        if (!contentAuditStatus.hasAuditText()) {
                            statusValue = originalValue + ContentAuditStatus.ILLEGAL_TEXT + ContentAuditStatus.AUDIT_TEXT;
                            modifyContentAuditStatus(values[0], values[1], statusValue, originalValue, ActStatus.ACTED.getCode());
                        } else if (contentAuditStatus.hasAuditText() && contentAuditStatus.isTextPass()) {
                            statusValue = originalValue + ContentAuditStatus.ILLEGAL_TEXT;
                            modifyContentAuditStatus(values[0], values[1], statusValue, originalValue, ActStatus.ACTED.getCode());
                        } else {

                        }
                    } else if (updateRemoveStatusCode != 0 && updateRemoveStatusCode == 1) {
                        if (!contentAuditStatus.hasAuditText()) {
                            statusValue = originalValue + ContentAuditStatus.AUDIT_TEXT;
                            modifyContentAuditStatus(values[0], values[1], statusValue, originalValue, ActStatus.UNACT.getCode());
                        } else if (contentAuditStatus.hasAuditText() && !contentAuditStatus.isTextPass()) {
                            statusValue = originalValue - ContentAuditStatus.ILLEGAL_TEXT;
                            modifyContentAuditStatus(values[0], values[1], statusValue, originalValue, ActStatus.UNACT.getCode());
                        } else {

                        }
                    }

                }
            } catch (Exception e) {
                GAlerter.lab("AuditController类的modifyAllContentText方法发生异常", e);
            }
        }

        return new ModelAndView("forward:/audit/content/textlist", msgMap);
    }


    @RequestMapping(value = "/rybatchupdate")
    public ModelAndView batchUpdateContentReply(
            @RequestParam(value = "url", required = false) String url,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "replyids", required = false) String[] replyIds,
            @RequestParam(value = "updateRemoveStatusCode", required = false, defaultValue = "0") Integer updateRemoveStatusCode,
            @RequestParam(value = "startdate", required = false) String startDate,
            @RequestParam(value = "enddate", required = false) String endDate,
            @RequestParam(value = "screenname", required = false) String screenName,
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "auditstatus", required = false) Integer auditStatus,
            @RequestParam(value = "items", required = false, defaultValue = "0") Integer items,
            @RequestParam(value = "pager.offset", required = false, defaultValue = "0") Integer pagerOffset,
            @RequestParam(value = "maxPageItems", required = false, defaultValue = "1") Integer maxPageItems) {

        Pagination pagination = new Pagination(items, pagerOffset / maxPageItems + 1, WebappConfig.get().getHomePageSize());
        ContentQueryParam param = new ContentQueryParam();
        Map<String, Object> msgMap = new HashMap<String, Object>();

        if (!StringUtil.isEmpty(url) && !StringUtil.isEmpty(type)) {
            msgMap.put("url", url);
            msgMap.put("type", type);
        } else {
            if (StringUtil.isEmpty(startDate) && StringUtil.isEmpty(endDate)) {
                param.setEndDate(DateUtil.StringTodate(DateUtil.DateToString(new Date(), DateUtil.DATE_FORMAT) + " 23:59:59",
                        DateUtil.DEFAULT_DATE_FORMAT2));
                param.setStartDate(DateUtil.adjustDate(DateUtil.ignoreTime(new Date()), Calendar.DAY_OF_MONTH, -5));
            } else {
                //处理时间参数对象
                ContentAuditUtil.handleDateParam(param, new String[]{"startDate", "endDate"}, ContentAuditUtil.sortASC(startDate, endDate, NUM_OF_INCREASE_DAY));
            }

            msgMap.put("page", pagination);
            msgMap.put("params", param);
            msgMap.put("screenname", screenName);
            msgMap.put("key", key);
            msgMap.put("auditstatus", auditStatus);
            msgMap.put("contentStatus", contentStatus);
            msgMap.put("updateRemoveStatusCode", updateRemoveStatusCode);
        }

        if (replyIds != null && replyIds.length != 0) {
            try {

                for (String replyIdcontentId : replyIds) {
                    String[] values = replyIdcontentId.split(SPLITER_OF_CONTENTIDS);

                    Integer originalValue = Integer.parseInt(values[2]);
                    ContentReplyAuditStatus replyAuditStatus = ContentReplyAuditStatus.getByValue(originalValue);
                    Integer statusValue = null;
                    if (updateRemoveStatusCode != 0 && updateRemoveStatusCode == 2) {
                        if (!replyAuditStatus.hasAuditReply()) {
                            statusValue = originalValue + ContentAuditStatus.ILLEGAL_TEXT + ContentAuditStatus.AUDIT_TEXT;
                            modifyReplyAuditStatus(values[0], values[1], statusValue, originalValue, ActStatus.ACTED.getCode());
                        } else if (replyAuditStatus.hasAuditReply() && replyAuditStatus.isReplyPass()) {
                            statusValue = originalValue + ContentAuditStatus.ILLEGAL_TEXT;
                            modifyReplyAuditStatus(values[0], values[1], statusValue, originalValue, ActStatus.ACTED.getCode());
                        } else {

                        }
                    } else if (updateRemoveStatusCode != 0 && updateRemoveStatusCode == 1) {
                        if (!replyAuditStatus.hasAuditReply()) {
                            statusValue = originalValue + ContentAuditStatus.AUDIT_TEXT;
                            modifyReplyAuditStatus(values[0], values[1], statusValue, originalValue, ActStatus.UNACT.getCode());
                        } else if (replyAuditStatus.hasAuditReply() && !replyAuditStatus.isReplyPass()) {
                            statusValue = originalValue - ContentAuditStatus.ILLEGAL_TEXT;
                            modifyReplyAuditStatus(values[0], values[1], statusValue, originalValue, ActStatus.UNACT.getCode());
                        } else {

                        }
                    }

                }
            } catch (Exception e) {
                GAlerter.lab("AuditController类的modifyReplyAuditStatus方法发生异常", e);
            }
        }

        if (!StringUtil.isEmpty(url) && !StringUtil.isEmpty(type)) {
            return new ModelAndView("forward:/audit/content/auditurl", msgMap);
        }
        return new ModelAndView("forward:/audit/content/textreplylist", msgMap);
    }


    /*--------------------------------------------------------------*/
    @RequestMapping(value = "/imglist")
    public ModelAndView queryContentImageList(
            @RequestParam(value = "startdate", required = false) String startDate,
            @RequestParam(value = "enddate", required = false) String endDate,
            @RequestParam(value = "screenname", required = false) String screenName,
            @RequestParam(value = "auditstatus", required = false, defaultValue = "0") Integer auditStatus,
            @RequestParam(value = "items", required = false, defaultValue = "0") Integer items,
            @RequestParam(value = "pager.offset", required = false, defaultValue = "0") Integer pagerOffset,
            @RequestParam(value = "maxPageItems", required = false, defaultValue = "1") Integer maxPageItems) {

        Pagination pagination = new Pagination(items, pagerOffset / maxPageItems + 1, WebappConfig.get().getHomePageSize());

        Map<String, Object> mapMsg = new HashMap<String, Object>();
        ContentQueryParam param = new ContentQueryParam();
        QueryExpress queryExpress = new QueryExpress();

        //
        if (StringUtil.isEmpty(startDate) && StringUtil.isEmpty(endDate)) {
            param.setEndDate(DateUtil.StringTodate(DateUtil.DateToString(new Date(), DateUtil.DATE_FORMAT) + " 23:59:59",
                    DateUtil.DEFAULT_DATE_FORMAT2));
            param.setStartDate(DateUtil.adjustDate(DateUtil.ignoreTime(new Date()), Calendar.DAY_OF_MONTH, -5));
        } else {
            //处理时间参数对象
            ContentAuditUtil.handleDateParam(param, new String[]{"startDate", "endDate"}, ContentAuditUtil.sortASC(startDate, endDate, NUM_OF_INCREASE_DAY));
        }

        //
        if (!StringUtil.isEmpty(screenName)) {
            queryExpress.add(QueryCriterions.eq(ContentField.UNO, getUno(screenName)));
        }

        if (auditStatus != null) {

            if (auditStatus.equals(UNAUDIT_SELECTED_VALUE)) {
                queryExpress.add(QueryCriterions.bitwiseAnd(ContentField.AUDITSTATUS, QueryCriterionRelation.EQ, ContentAuditStatus.AUDIT_IMG, auditStatus));
                //用户自己未删除的文章
                queryExpress.add(QueryCriterions.eq(ContentField.REMOVESTATUS, ActStatus.UNACT.getCode()));
            } else {
                queryExpress.add(QueryCriterions.bitwiseAnd(ContentField.AUDITSTATUS, QueryCriterionRelation.EQ, ContentAuditStatus.AUDIT_IMG, ContentAuditStatus.AUDIT_IMG));
            }

        }


        //是否含有图片的文章
        queryExpress.add(QueryCriterions.bitwiseAnd(ContentField.CONTENTTYPE, QueryCriterionRelation.EQ, ContentType.IMAGE, ContentType.IMAGE));
        queryExpress.add(QueryCriterions.between(ContentField.PUBLISHDATE, param.getStartDate(), param.getEndDate()));
        queryExpress.add(QuerySort.add(ContentField.PUBLISHDATE, QuerySortOrder.DESC));


        //
        try {
            PageRows<ContentDTO> pageRows = contentWebLogic.queryContentImages(queryExpress, pagination, auditStatus);

            mapMsg.put("rows", pageRows.getRows());
            mapMsg.put("page", pageRows.getPage());
            mapMsg.put("params", param);
            mapMsg.put("screenname", screenName);
            mapMsg.put("auditstatus", auditStatus);
            mapMsg.put("imageStatus", imageStatus);

        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
            GAlerter.lab("queryContentTextList a Controller SQLException :", e);
        }

        return new ModelAndView("/content/contentimglist", mapMsg);

    }


    @RequestMapping(value = "/imgbatchupdate")
    public ModelAndView batchUpdateContentImage(
            @RequestParam(value = "contentids", required = false) String[] contentids,
            @RequestParam(value = "updateRemoveStatusCode", required = false, defaultValue = "0") Integer updateRemoveStatusCode,
            @RequestParam(value = "startdate", required = false) String startDate,
            @RequestParam(value = "enddate", required = false) String endDate,
            @RequestParam(value = "screenname", required = false) String screenName,
            @RequestParam(value = "auditstatus", required = false, defaultValue = "0") Integer auditStatus,
            @RequestParam(value = "items", required = false, defaultValue = "0") Integer items,
            @RequestParam(value = "pager.offset", required = false, defaultValue = "0") Integer pagerOffset,
            @RequestParam(value = "maxPageItems", required = false, defaultValue = "1") Integer maxPageItems) {

        Pagination pagination = new Pagination(items, pagerOffset / maxPageItems + 1, WebappConfig.get().getHomePageSize());

        Map<String, Object> mapMsg = new HashMap<String, Object>();
        ContentQueryParam param = new ContentQueryParam();

        //
        if (StringUtil.isEmpty(startDate) && StringUtil.isEmpty(endDate)) {
            param.setEndDate(DateUtil.StringTodate(DateUtil.DateToString(new Date(), DateUtil.DATE_FORMAT) + " 23:59:59",
                    DateUtil.DEFAULT_DATE_FORMAT2));
            param.setStartDate(DateUtil.adjustDate(DateUtil.ignoreTime(new Date()), Calendar.DAY_OF_MONTH, -5));
        } else {
            //处理时间参数对象
            ContentAuditUtil.handleDateParam(param, new String[]{"startDate", "endDate"}, ContentAuditUtil.sortASC(startDate, endDate, NUM_OF_INCREASE_DAY));
        }

        mapMsg.put("page", pagination);
        mapMsg.put("params", param);
        mapMsg.put("screenname", screenName);
        mapMsg.put("auditstatus", auditStatus);
        mapMsg.put("imageStatus", imageStatus);
        mapMsg.put("updateRemoveStatusCode", updateRemoveStatusCode);

        if (contentids != null && contentids.length != 0) {
            for (String checkboxId : contentids) {
                String[] IdUnoValue = checkboxId.split(SPLITER_OF_CONTENTIDS);
                Map<ObjectField, Object> map = null;
                ContentAuditStatus contentAuditStatus = ContentAuditStatus.getByValue(Integer.parseInt(IdUnoValue[2]));
                if (IdUnoValue.length == 3) {
                    if (!contentAuditStatus.hasAuditIMG()) {
                        statusStrategy = new UnCheckStrategy(contentWebLogic, IdUnoValue[0], IdUnoValue[1], null, Integer.parseInt(IdUnoValue[2]));
                    } else if (contentAuditStatus.hasAuditIMG() && contentAuditStatus.isIMGPass()) {
                        statusStrategy = new SubstandardStrategy(contentWebLogic, IdUnoValue[0], IdUnoValue[1], null, Integer.parseInt(IdUnoValue[2]));
                    } else if (contentAuditStatus.hasAuditIMG() && !contentAuditStatus.isIMGPass()) {
                        statusStrategy = new StandardStrategy(contentWebLogic, IdUnoValue[0], IdUnoValue[1], null, Integer.parseInt(IdUnoValue[2]));
                    }
                    if (ContentAuditStatus.ILLEGAL_IMG == updateRemoveStatusCode) {
                        map = statusStrategy.modifyIMGContentTemplate(true);
                    } else if (ContentAuditStatus.AUDIT_IMG == updateRemoveStatusCode) {
                        map = statusStrategy.modifyIMGContentTemplate(false);
                    }
                }

                //准备log对象
                ToolsLog log = new ToolsLog();
                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.IMG_BATCHUPDATECONTENTIMAGE);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setDescription("资源ID为：CONTENTID[" + IdUnoValue[0] + "], UNO[" + IdUnoValue[1] + "]");

                log.setOpBefore("AUDITSTATUS[" + IdUnoValue[2] + "]");
                assert map != null;
                log.setOpAfter(ContentAuditUtil.formMapToString(map));
                addLog(log);
            }
        }

        return new ModelAndView("forward:/audit/content/imglist", mapMsg);

    }


    @RequestMapping(value = "/textreplylist")
    public ModelAndView queryContentReplyTextList(
            @RequestParam(value = "startdate", required = false) String startDate,
            @RequestParam(value = "enddate", required = false) String endDate,
            @RequestParam(value = "screenname", required = false) String screenName,
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "auditstatus", required = false, defaultValue = "0") Integer auditStatus,
            @RequestParam(value = "items", required = false, defaultValue = "0") Integer items,
            @RequestParam(value = "pager.offset", required = false, defaultValue = "0") Integer pagerOffset,
            @RequestParam(value = "maxPageItems", required = false, defaultValue = "1") Integer maxPageItems
    ) {

        Pagination pagination = new Pagination(items, pagerOffset / maxPageItems + 1, WebappConfig.get().getHomePageSize());

        Map<String, Object> mapMsg = new HashMap<String, Object>();
        ContentQueryParam param = new ContentQueryParam();
        QueryExpress queryExpress = new QueryExpress();

        //
        if (StringUtil.isEmpty(startDate) && StringUtil.isEmpty(endDate)) {
            param.setEndDate(DateUtil.StringTodate(DateUtil.DateToString(new Date(), DateUtil.DATE_FORMAT) + " 23:59:59",
                    DateUtil.DEFAULT_DATE_FORMAT2));
            param.setStartDate(DateUtil.adjustDate(DateUtil.ignoreTime(new Date()), Calendar.DAY_OF_MONTH, -5));
        } else {
            //处理时间参数对象
            ContentAuditUtil.handleDateParam(param, new String[]{"startDate", "endDate"}, ContentAuditUtil.sortASC(startDate, endDate, NUM_OF_INCREASE_DAY));
        }

        //
        if (!StringUtil.isEmpty(screenName)) {
            queryExpress.add(QueryCriterions.eq(ContentInteractionField.INTERACTIONUNO, getUno(screenName)));
        }

        if (!StringUtil.isEmpty(key)) {
            queryExpress.add(QueryCriterions.like(ContentInteractionField.INTERACTIONCONTENT, "%" + key + "%"));
        }

        if (auditStatus != null) {

            if (UNAUDIT_SELECTED_VALUE.equals(auditStatus)) {
                queryExpress.add(QueryCriterions.bitwiseAnd(ContentInteractionField.AUDITSTATUS, QueryCriterionRelation.EQ, ContentReplyAuditStatus.AUDIT_REPLY, auditStatus));
                //用户自己未删除的文章
                queryExpress.add(QueryCriterions.eq(ContentInteractionField.REMOVESTATUS, ActStatus.UNACT.getCode()));
            } else if (auditStatus == 1) {
                queryExpress.add(QueryCriterions.bitwiseAnd(ContentInteractionField.AUDITSTATUS, QueryCriterionRelation.EQ, ContentReplyAuditStatus.ILLEGAL_REPLY, 0))
                        .add(QueryCriterions.bitwiseAnd(ContentInteractionField.AUDITSTATUS, QueryCriterionRelation.EQ, ContentReplyAuditStatus.AUDIT_REPLY, auditStatus));

                //用户自己未删除的文章
                queryExpress.add(QueryCriterions.eq(ContentInteractionField.REMOVESTATUS, ActStatus.UNACT.getCode()));
            } else {
                queryExpress.add(QueryCriterions.bitwiseAnd(ContentInteractionField.AUDITSTATUS, QueryCriterionRelation.EQ, auditStatus, auditStatus));
            }

        }


        queryExpress.add(QueryCriterions.between(ContentInteractionField.CREATEDATE, param.getStartDate(), param.getEndDate()));
        queryExpress.add(QuerySort.add(ContentInteractionField.CREATEDATE, QuerySortOrder.DESC));


        //
        try {
            PageRows<ContentDTO> pageRows = contentWebLogic.queryContentReplys(queryExpress, pagination);

            mapMsg.put("rows", pageRows.getRows());
            mapMsg.put("page", pageRows.getPage());
            mapMsg.put("params", param);
            mapMsg.put("screenname", screenName);
            mapMsg.put("key", key);
            mapMsg.put("auditstatus", auditStatus);
            mapMsg.put("replyStatus", replyStatus);

        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
            GAlerter.lab("queryContentTextList a Controller SQLException :", e);
        }

        return new ModelAndView("/content/contentreplylist", mapMsg);

    }


    //-----------------------------------------------------

    @RequestMapping(value = "/auditurl")
    public ModelAndView auditUrl(
            @RequestParam(value = "url", required = false) String url,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "items", required = false, defaultValue = "0") Integer items,
            @RequestParam(value = "pager.offset", required = false, defaultValue = "0") Integer pagerOffset,
            @RequestParam(value = "maxPageItems", required = false, defaultValue = "1") Integer maxPageItems) {

        Pagination pagination = new Pagination(items, pagerOffset / maxPageItems + 1, WebappConfig.get().getHomePageSize());

        Map<String, Object> mapMsg = new HashMap<String, Object>();
        mapMsg.put("type", type);
        mapMsg.put("url", url);

        if (!StringUtil.isEmpty(url)) {
            url = url.trim();
            String[] brokenString = url.split("/");
            if (brokenString.length < 2) {
                mapMsg.put("error", "");
                return new ModelAndView("/content/urlaudit", mapMsg);
            } else {
                String contentId = brokenString[brokenString.length - 1];
                String domain = brokenString[brokenString.length - 2];

                if ("reply".equals(type)) {
                    try {
                        PageRows<ContentDTO> pageRows = contentWebLogic.queryContentReplyByCidDomain(contentId, domain, pagination);

                        if (pageRows != null) {
                            mapMsg.put("rows", pageRows.getRows());
                            mapMsg.put("page", pageRows.getPage());
                        }
                    } catch (ServiceException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        PageRows<ContentDTO> pageRows = contentWebLogic.queryContentByContentIdDomain(contentId, domain, pagination, type);
                        mapMsg.put("rows", pageRows.getRows());
                        mapMsg.put("page", pageRows.getPage());
                    } catch (ServiceException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        return new ModelAndView("/content/urlaudit", mapMsg);

    }

    @RequestMapping(value = "/allremove")
    public ModelAndView allRemove(@RequestParam(value = "startdate", required = false) String startDate,
                                  @RequestParam(value = "enddate", required = false) String endDate,
                                  @RequestParam(value = "screenname", required = false) String screenName,
                                  @RequestParam(value = "key", required = false) String key,
                                  @RequestParam(value = "auditstatus", required = false, defaultValue = "0") Integer auditStatus) {

        Map<String, Object> mapMsg = new HashMap<String, Object>();
        ContentQueryParam param = new ContentQueryParam();
        QueryExpress queryExpress = new QueryExpress();
        if (StringUtil.isEmpty(startDate) && StringUtil.isEmpty(endDate)) {
            param.setEndDate(DateUtil.StringTodate(DateUtil.DateToString(new Date(), DateUtil.DATE_FORMAT) + " 23:59:59",
                    DateUtil.DEFAULT_DATE_FORMAT2));
            param.setStartDate(DateUtil.adjustDate(DateUtil.ignoreTime(new Date()), Calendar.DAY_OF_MONTH, -5));
        } else {
            //处理时间参数对象
            ContentAuditUtil.handleDateParam(param, new String[]{"startDate", "endDate"}, ContentAuditUtil.sortASC(startDate, endDate, NUM_OF_INCREASE_DAY));
        }

        if (StringUtil.isEmpty(screenName)) {
            mapMsg.put("params", param);
            mapMsg.put("screenname", screenName);
            mapMsg.put("key", key);
            mapMsg.put("auditstatus", auditStatus);
            mapMsg.put("contentStatus", contentStatus);
            return new ModelAndView("forward:/audit/content/textlist", mapMsg);
        }
        queryExpress.add(QueryCriterions.eq(ContentField.UNO, getUno(screenName)));


//        if (!StringUtil.isEmpty(key)) {
////            queryExpress.add(QueryCriterions.like(ContentField.CONTENTBODY, "%" + key + "%"));
//            queryExpress.add(QueryCriterions.or(QueryCriterions.like(ContentField.CONTENTBODY, "%" + key + "%"),
//                    QueryCriterions.like(ContentField.CONTENTSUBJECT, "%" + key + "%")));
//        }
//
//        if (auditStatus != null) {
//
//            if (auditStatus.equals(UNAUDIT_SELECTED_VALUE)) {
//                queryExpress.add(QueryCriterions.bitwiseAnd(ContentField.AUDITSTATUS, QueryCriterionRelation.EQ, ContentAuditStatus.AUDIT_TEXT, UNAUDIT_SELECTED_VALUE));
//                //用户自己未删除的文章
//                queryExpress.add(QueryCriterions.eq(ContentField.REMOVESTATUS, ActStatus.UNACT.getCode()));
//            } else if (auditStatus == 1) {
//                queryExpress.add(QueryCriterions.bitwiseAnd(ContentField.AUDITSTATUS, QueryCriterionRelation.EQ, ContentAuditStatus.ILLEGAL_TEXT, 0))
//                        .add(QueryCriterions.bitwiseAnd(ContentField.AUDITSTATUS, QueryCriterionRelation.EQ, ContentAuditStatus.AUDIT_TEXT, ContentAuditStatus.AUDIT_TEXT));
//
//                queryExpress.add(QueryCriterions.eq(ContentField.REMOVESTATUS, ActStatus.UNACT.getCode()));
//            } else {
//                queryExpress.add(QueryCriterions.bitwiseAnd(ContentField.AUDITSTATUS, QueryCriterionRelation.EQ, ContentAuditStatus.AUDIT_TEXT + ContentAuditStatus.ILLEGAL_TEXT, ContentAuditStatus.AUDIT_TEXT + ContentAuditStatus.ILLEGAL_TEXT));
//
//            }
//
//        }
//        queryExpress.add(QueryCriterions.between(ContentField.PUBLISHDATE, param.getStartDate(), param.getEndDate()));

        try {
            Pagination page = new Pagination(100, 1, 100);
            do {
                PageRows<Content> pageRows = ContentServiceSngl.get().queryContentByQueryExpress(queryExpress, page);
                page = pageRows.getPage();
                if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                    for (Content content : pageRows.getRows()) {
                        Integer originalValue = content.getAuditStatus().getValue();
                        ContentAuditStatus contentAuditStatus = ContentAuditStatus.getByValue(originalValue);
                        Integer statusValue = null;
                        if (!contentAuditStatus.hasAuditText()) {
                            statusValue = originalValue + ContentAuditStatus.ILLEGAL_TEXT + ContentAuditStatus.AUDIT_TEXT;
                            modifyContentAuditStatus(content.getContentId(), content.getUno(), statusValue, originalValue, ActStatus.ACTED.getCode());
                        } else if (contentAuditStatus.hasAuditText() && contentAuditStatus.isTextPass()) {
                            statusValue = originalValue + ContentAuditStatus.ILLEGAL_TEXT;
                            modifyContentAuditStatus(content.getContentId(), content.getUno(), statusValue, originalValue, ActStatus.ACTED.getCode());
                        } else {

                        }
                    }
                }
            } while (!page.isLastPage());
        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
            GAlerter.lab(this.getClass().getName() + "query ServiceException: ", e);
        }
        mapMsg.put("params", param);
        mapMsg.put("screenname", screenName);
        mapMsg.put("key", key);
        mapMsg.put("auditstatus", auditStatus);
        mapMsg.put("contentStatus", contentStatus);
        return new ModelAndView("forward:/audit/content/textlist", mapMsg);

    }

    private String getUno(String screenName) {

        ProfileBlog profileBlog = null;
        try {
            profileBlog = ProfileServiceSngl.get().getProfileBlogByScreenName(screenName);
        } catch (ServiceException e) {
            GAlerter.lab("query profileBlog by screenName caught an exception: " + e);
        }

        return profileBlog != null ? profileBlog.getUno() : null;

    }

    private void modifyContentAuditStatus(String contentId, String uno, Integer value, Integer originalValue, String removeStatus) {
        Map<ObjectField, Object> fieldObjectMap = new HashMap<ObjectField, Object>();

        fieldObjectMap.put(ContentField.AUDITSTATUS, value);
        fieldObjectMap.put(ContentField.REMOVESTATUS, removeStatus);
        boolean success = contentWebLogic.modifyAuditStatus(contentId, uno, fieldObjectMap);
        if (success) {
            //准备log对象
            ToolsLog log = new ToolsLog();
            log.setOpUserId(getCurrentUser().getUserid());
            log.setOperType(LogOperType.TEXT_BATCHUPDATECONTENTTEXT);
            log.setOpTime(new Date());
            log.setOpIp(getIp());
            log.setDescription("资源ID为：CONTENTID[" + contentId + "], UNO[" + uno + "]");
            log.setOpBefore("AUDITSTATUS[" + originalValue + "]");
            log.setOpAfter("AUDITSTATUS[" + value + "]");
            addLog(log);
        }
    }

    private void modifyReplyAuditStatus(String replyId, String contentId, Integer value, Integer originalValue, String removeStatus) {
        Map<ObjectField, Object> fieldObjectMap = new HashMap<ObjectField, Object>();

        fieldObjectMap.put(ContentInteractionField.AUDITSTATUS, value);
        fieldObjectMap.put(ContentInteractionField.REMOVESTATUS, removeStatus);
        boolean success = contentWebLogic.modifyReplyAuditStatus(replyId, "???", contentId, fieldObjectMap);
        if (success) {
            //准备log对象
            ToolsLog log = new ToolsLog();
            log.setOpUserId(getCurrentUser().getUserid());
            log.setOperType(LogOperType.REPLY_BATCHUPDATECONTENTREPLY);
            log.setOpTime(new Date());
            log.setOpIp(getIp());
            log.setDescription("资源ID为：CONTENTID[" + contentId + "], REPLYID[" + replyId + "]");
            log.setOpBefore("AUDITSTATUS[" + originalValue + "]");
            log.setOpAfter("AUDITSTATUS[" + value + "]");
            addLog(log);
        }
    }

}
