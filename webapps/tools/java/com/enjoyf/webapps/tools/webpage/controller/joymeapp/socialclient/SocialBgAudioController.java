package com.enjoyf.webapps.tools.webpage.controller.joymeapp.socialclient;

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
@RequestMapping(value = "/joymeapp/social/bgaudio")
public class SocialBgAudioController extends ToolsBaseController {

    private static Set<Integer> orderType = new HashSet<Integer>();

    static {
        orderType.add(1);  //按使用数排序
    }

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "5") int pageSize,
                             @RequestParam(value = "qname", required = false) String queryName,
                             @RequestParam(value = "qstarttime", required = false) Date queryStartTime,
                             @RequestParam(value = "qendtime", required = false) Date queryEndTime,
                             @RequestParam(value = "qcreateuserid", required = false) String queryCreateUserId,
                             @RequestParam(value = "qstatus", required = false) String queryStatus,
                             @RequestParam(value = "qordertype", required = false) Integer queryOrderType

    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("queryName", queryName);
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
        if (!StringUtil.isEmpty(queryName)) {
            queryExpress.add(QueryCriterions.eq(SocialBackgroundAudioField.AUDIO_NAME, queryName));
        }
        if (queryStartTime != null) {
            queryExpress.add(QueryCriterions.geq(SocialBackgroundAudioField.CREATE_DATE, queryStartTime));
        }
        if (queryEndTime != null) {
            queryExpress.add(QueryCriterions.leq(SocialBackgroundAudioField.CREATE_DATE, queryEndTime));
        }
        if (!StringUtil.isEmpty(queryCreateUserId)) {
            queryExpress.add(QueryCriterions.eq(SocialBackgroundAudioField.CREATE_USERID, queryCreateUserId));
        }
        if (!StringUtil.isEmpty(queryStatus)) {
            queryExpress.add(QueryCriterions.eq(SocialBackgroundAudioField.REMOVE_STATUS, queryStatus));
        } else {
            queryExpress.add(QueryCriterions.ne(SocialBackgroundAudioField.REMOVE_STATUS, ValidStatus.REMOVED.getCode()));
        }
        if (queryOrderType != null) {
            if (queryOrderType == 1) {
                queryExpress.add(QuerySort.add(SocialBackgroundAudioField.USE_SUM, QuerySortOrder.DESC));
            }
        } else {
            queryExpress.add(QuerySort.add(SocialBackgroundAudioField.DISPLAY_ORDER, QuerySortOrder.DESC));
        }
        try {
            PageRows<SocialBackgroundAudio> pageRows = ContentServiceSngl.get().querySocialBgAudio(queryExpress, pagination);
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                mapMessage.put("list", pageRows.getRows());
                mapMessage.put("page", pageRows.getPage());
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/joymeapp/socialclient/bgaudio/list", mapMessage);
        }
        return new ModelAndView("/joymeapp/socialclient/bgaudio/list", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createPage() {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("SubscriptTypeColl", SubscriptType.getAll());
        return new ModelAndView("/joymeapp/socialclient/bgaudio/create", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "audioname", required = false) String audioName,
                               @RequestParam(value = "audiopic", required = false) String audioPic,
                               @RequestParam(value = "description", required = false) String description,
                               @RequestParam(value = "singer", required = false) String singer,
                               @RequestParam(value = "mp3src", required = false) String mp3Src,
                               @RequestParam(value = "wavsrc", required = false) String wavSrc,
                               @RequestParam(value = "startdate", required = false) Date startDate,
                               @RequestParam(value = "enddate", required = false) Date endDate,
                               @RequestParam(value = "subscripttype", required = false) Integer subscriptType
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("audioName", audioName);
        mapMessage.put("audioPic", audioPic);
        mapMessage.put("description", description);
        mapMessage.put("singer", singer);
        mapMessage.put("mp3Src", mp3Src);
        mapMessage.put("wavSrc", wavSrc);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Subscript subscript = new Subscript();
        subscript.setStartDate(startDate == null ? null : df.format(startDate));
        subscript.setEndDate(endDate == null ? null : df.format(endDate));
        subscript.setType(subscriptType);

        SocialBackgroundAudio bgAudio = new SocialBackgroundAudio();
        bgAudio.setAudioName(audioName);
        bgAudio.setAudioPic(audioPic);
        bgAudio.setAudioDescription(description);
        bgAudio.setSinger(singer);
        bgAudio.setMp3Src(mp3Src);
        bgAudio.setWavSrc(wavSrc);
        bgAudio.setSubscriptType(SubscriptType.getByCode(subscriptType));
        bgAudio.setSubscript(subscript);
        bgAudio.setCreateDate(new Date());
        bgAudio.setCreateIp(getIp());
        bgAudio.setCreateUserId(getCurrentUser().getUserid());
        bgAudio.setRemoveStatus(ValidStatus.INVALID);
        try {
            ContentServiceSngl.get().insertSocialBgAudio(bgAudio);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/joymeapp/socialclient/bgaudio/create", mapMessage);
        }
        return new ModelAndView("redirect:/joymeapp/social/bgaudio/list");
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyPage(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,      //数据库记录索引
                                   @RequestParam(value = "maxPageItems", required = false, defaultValue = "5") int pageSize,
                                   @RequestParam(value = "bgaid", required = false) Long audioId,
                                   @RequestParam(value = "qname", required = false) String queryName,
                                   @RequestParam(value = "qstarttime", required = false) Date queryStartTime,
                                   @RequestParam(value = "qendtime", required = false) Date queryEndTime,
                                   @RequestParam(value = "qcreateuserid", required = false) String queryCreateUserId,
                                   @RequestParam(value = "qstatus", required = false) String queryStatus,
                                   @RequestParam(value = "qordertype", required = false) Integer queryOrderType

    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("queryName", queryName);
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
            SocialBackgroundAudio bgAudio = ContentServiceSngl.get().getSocialBgAudio(audioId);
            if (bgAudio != null) {
                mapMessage.put("bgAudio", bgAudio);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("forward:/joymeapp/socialclient/bgaudio/list", mapMessage);
        }

        return new ModelAndView("/joymeapp/socialclient/bgaudio/modify", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                               @RequestParam(value = "maxPageItems", required = false, defaultValue = "5") int pageSize,
                               @RequestParam(value = "bgaid", required = false) Long audioId,
                               @RequestParam(value = "qname", required = false) String queryName,
                               @RequestParam(value = "qstarttime", required = false) Date queryStartTime,
                               @RequestParam(value = "qendtime", required = false) Date queryEndTime,
                               @RequestParam(value = "qcreateuserid", required = false) String queryCreateUserId,
                               @RequestParam(value = "qstatus", required = false) String queryStatus,
                               @RequestParam(value = "qordertype", required = false) Integer queryOrderType,

                               @RequestParam(value = "audioname", required = false) String audioName,
                               @RequestParam(value = "audiopic", required = false) String audioPic,
                               @RequestParam(value = "description", required = false) String description,
                               @RequestParam(value = "singer", required = false) String singer,
                               @RequestParam(value = "mp3src", required = false) String mp3Src,
                               @RequestParam(value = "wavsrc", required = false) String wavSrc,
                               @RequestParam(value = "startdate", required = false) Date startDate,
                               @RequestParam(value = "enddate", required = false) Date endDate,
                               @RequestParam(value = "subscripttype", required = false) Integer subscriptType

    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("queryName", queryName);
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
        updateExpress.set(SocialBackgroundAudioField.AUDIO_NAME, audioName);
        updateExpress.set(SocialBackgroundAudioField.AUDIO_PIC, audioPic);
        updateExpress.set(SocialBackgroundAudioField.AUDIO_DESCRIPTION, description);
        updateExpress.set(SocialBackgroundAudioField.SINGER, singer);
        updateExpress.set(SocialBackgroundAudioField.MP3_SRC, mp3Src);
        updateExpress.set(SocialBackgroundAudioField.WAV_SRC, wavSrc);
        updateExpress.set(SocialBackgroundAudioField.SUBSCRIPT, subscript.toJsonStr());
        updateExpress.set(SocialBackgroundAudioField.SUBSCRIPT_TYPE, subscriptType);
        updateExpress.set(SocialBackgroundAudioField.MODIFY_DATE, new Date());
        updateExpress.set(SocialBackgroundAudioField.MODIFY_IP, getIp());
        updateExpress.set(SocialBackgroundAudioField.MODIFY_USERID, getCurrentUser().getUserid());

        try {
            ContentServiceSngl.get().modifySocialBgAudio(audioId, updateExpress);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("forward:/joymeapp/social/bgaudio/list", mapMessage);
        }
        return new ModelAndView("forward:/joymeapp/social/bgaudio/list", mapMessage);
    }

    @RequestMapping(value = "/remove")
    public ModelAndView remove(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                               @RequestParam(value = "maxPageItems", required = false, defaultValue = "5") int pageSize,
                               @RequestParam(value = "bgaid", required = false) Long audioId,
                               @RequestParam(value = "qname", required = false) String queryName,
                               @RequestParam(value = "qstarttime", required = false) Date queryStartTime,
                               @RequestParam(value = "qendtime", required = false) Date queryEndTime,
                               @RequestParam(value = "qcreateuserid", required = false) String queryCreateUserId,
                               @RequestParam(value = "qstatus", required = false) String queryStatus,
                               @RequestParam(value = "qordertype", required = false) Integer queryOrderType
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("queryName", queryName);
        mapMessage.put("queryStatus", queryStatus);
        mapMessage.put("queryStartTime", queryStartTime);
        mapMessage.put("queryEndTime", queryEndTime);
        mapMessage.put("queryCreateUserId", queryCreateUserId);
        mapMessage.put("queryOrderType", queryOrderType);
        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
        mapMessage.put("page", pagination);
        try {
            ContentServiceSngl.get().modifySocialBgAudio(audioId,
                    new UpdateExpress().set(SocialBackgroundAudioField.REMOVE_STATUS, ValidStatus.REMOVED.getCode())
                            .set(SocialBackgroundAudioField.MODIFY_DATE, new Date())
                            .set(SocialBackgroundAudioField.MODIFY_IP, getIp())
                            .set(SocialBackgroundAudioField.MODIFY_USERID, getCurrentUser().getUserid()));

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("forward:/joymeapp/social/bgaudio/list", mapMessage);
        }
        return new ModelAndView("forward:/joymeapp/social/bgaudio/list", mapMessage);
    }

    @RequestMapping(value = "/recover")
    public ModelAndView recover(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                                @RequestParam(value = "maxPageItems", required = false, defaultValue = "5") int pageSize,
                                @RequestParam(value = "bgaid", required = false) Long audioId,
                                @RequestParam(value = "qname", required = false) String queryName,
                                @RequestParam(value = "qstarttime", required = false) Date queryStartTime,
                                @RequestParam(value = "qendtime", required = false) Date queryEndTime,
                                @RequestParam(value = "qcreateuserid", required = false) String queryCreateUserId,
                                @RequestParam(value = "qstatus", required = false) String queryStatus,
                                @RequestParam(value = "qordertype", required = false) Integer queryOrderType
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("queryName", queryName);
        mapMessage.put("queryStatus", queryStatus);
        mapMessage.put("queryStartTime", queryStartTime);
        mapMessage.put("queryEndTime", queryEndTime);
        mapMessage.put("queryCreateUserId", queryCreateUserId);
        mapMessage.put("queryOrderType", queryOrderType);
        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
        mapMessage.put("page", pagination);
        try {
            ContentServiceSngl.get().modifySocialBgAudio(audioId,
                    new UpdateExpress().set(SocialBackgroundAudioField.REMOVE_STATUS, ValidStatus.VALID.getCode())
                            .set(SocialBackgroundAudioField.MODIFY_DATE, new Date())
                            .set(SocialBackgroundAudioField.MODIFY_IP, getIp())
                            .set(SocialBackgroundAudioField.MODIFY_USERID, getCurrentUser().getUserid()));

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("forward:/joymeapp/social/bgaudio/list", mapMessage);
        }
        return new ModelAndView("forward:/joymeapp/social/bgaudio/list", mapMessage);
    }

    @RequestMapping(value = "/publish")
    public ModelAndView publish(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                                @RequestParam(value = "maxPageItems", required = false, defaultValue = "5") int pageSize,
                                @RequestParam(value = "bgaid", required = false) Long audioId,
                                @RequestParam(value = "qname", required = false) String queryName,
                                @RequestParam(value = "qstarttime", required = false) Date queryStartTime,
                                @RequestParam(value = "qendtime", required = false) Date queryEndTime,
                                @RequestParam(value = "qcreateuserid", required = false) String queryCreateUserId,
                                @RequestParam(value = "qstatus", required = false) String queryStatus,
                                @RequestParam(value = "qordertype", required = false) Integer queryOrderType
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("queryName", queryName);
        mapMessage.put("queryStatus", queryStatus);
        mapMessage.put("queryStartTime", queryStartTime);
        mapMessage.put("queryEndTime", queryEndTime);
        mapMessage.put("queryCreateUserId", queryCreateUserId);
        mapMessage.put("queryOrderType", queryOrderType);
        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
        mapMessage.put("page", pagination);
        try {
            ContentServiceSngl.get().modifySocialBgAudio(audioId,
                    new UpdateExpress().set(SocialBackgroundAudioField.REMOVE_STATUS, ValidStatus.VALID.getCode())
                            .set(SocialBackgroundAudioField.MODIFY_DATE, new Date())
                            .set(SocialBackgroundAudioField.MODIFY_IP, getIp())
                            .set(SocialBackgroundAudioField.MODIFY_USERID, getCurrentUser().getUserid()));

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("forward:/joymeapp/social/bgaudio/list", mapMessage);
        }
        return new ModelAndView("forward:/joymeapp/social/bgaudio/list", mapMessage);
    }

    @RequestMapping(value = "/sort/{sort}")
    public ModelAndView sort(@PathVariable(value = "sort") String sort,
                             @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "5") int pageSize,
                             @RequestParam(value = "bgaid", required = false) Long audioId,
                             @RequestParam(value = "qname", required = false) String queryName,
                             @RequestParam(value = "qstarttime", required = false) Date queryStartTime,
                             @RequestParam(value = "qendtime", required = false) Date queryEndTime,
                             @RequestParam(value = "qcreateuserid", required = false) String queryCreateUserId,
                             @RequestParam(value = "qstatus", required = false) String queryStatus,
                             @RequestParam(value = "qordertype", required = false) Integer queryOrderType
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("queryName", queryName);
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
            SocialBackgroundAudio bgAudio = ContentServiceSngl.get().getSocialBgAudio(audioId);
            if (bgAudio == null) {
                return new ModelAndView("forward:/joymeapp/social/bgaudio/list", mapMessage);
            }
            queryExpress.add(QueryCriterions.eq(SocialBackgroundAudioField.REMOVE_STATUS, ValidStatus.VALID.getCode()));
            if (sort.equals("up")) {
                queryExpress.add(QueryCriterions.gt(SocialBackgroundAudioField.DISPLAY_ORDER, bgAudio.getDisplayOrder()));
                queryExpress.add(QuerySort.add(SocialBackgroundAudioField.DISPLAY_ORDER, QuerySortOrder.ASC));
            } else {
                queryExpress.add(QueryCriterions.lt(SocialBackgroundAudioField.DISPLAY_ORDER, bgAudio.getDisplayOrder()));
                queryExpress.add(QuerySort.add(SocialBackgroundAudioField.DISPLAY_ORDER, QuerySortOrder.DESC));
            }

            PageRows<SocialBackgroundAudio> pageRows = ContentServiceSngl.get().querySocialBgAudio(queryExpress, new Pagination(1, 1, 1));
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                updateExpress1.set(SocialBackgroundAudioField.DISPLAY_ORDER, bgAudio.getDisplayOrder());
                ContentServiceSngl.get().modifySocialBgAudio(pageRows.getRows().get(0).getAudioId(), updateExpress1);

                updateExpress2.set(SocialBackgroundAudioField.DISPLAY_ORDER, pageRows.getRows().get(0).getDisplayOrder());
                ContentServiceSngl.get().modifySocialBgAudio(bgAudio.getAudioId(), updateExpress2);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("forward:/joymeapp/social/bgaudio/list", mapMessage);
        }
        return new ModelAndView("forward:/joymeapp/social/bgaudio/list", mapMessage);
    }

}
