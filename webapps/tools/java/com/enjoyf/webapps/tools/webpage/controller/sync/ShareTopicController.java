package com.enjoyf.webapps.tools.webpage.controller.sync;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.sync.*;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.service.tools.ToolsLog;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-31
 * Time: 上午9:43
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/sync/sharetopic")
public class ShareTopicController extends ToolsBaseController {

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,      //数据库记录索引
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                             @RequestParam(value = "shareid", required = false) Long shareId) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            List<ShareBaseInfo> shareInfoList = SyncServiceSngl.get().queryShareInfo(new QueryExpress()
                    .add(QueryCriterions.eq(ShareBaseInfoField.REMOVESTATUS, ActStatus.UNACT.getCode())));

            mapMessage.put("shareBaseInfoList", shareInfoList);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        try {
            if(shareId!=null){
                ShareBaseInfo info = SyncServiceSngl.get().getShareInfo(new QueryExpress().add(QueryCriterions.eq(ShareBaseInfoField.SHAREID, shareId)));
                mapMessage.put("info", info);
            }

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }


        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QuerySort.add(ShareTopicField.CREATEDATE, QuerySortOrder.DESC));

        mapMessage.put("shareid",shareId);

        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);

        try {
            if(shareId!=null){
                queryExpress.add(QueryCriterions.eq(ShareBodyField.SHAREID, shareId));
                PageRows<ShareTopic> pageRows = SyncServiceSngl.get().queryShareTopicByPage(queryExpress , pagination);

                mapMessage.put("list", pageRows.getRows());
                mapMessage.put("page", pageRows.getPage());
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/sync/sharetopiclist", mapMessage);
    }


    //
    @RequestMapping(value = "/createpage")
    public ModelAndView createPage(@RequestParam(value = "shareid", required = false) Long shareId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();


        try {
            ShareBaseInfo info = SyncServiceSngl.get().getShareInfo(new QueryExpress()
                    .add(QueryCriterions.eq(ShareBaseInfoField.SHAREID, shareId))
                    .add(QueryCriterions.eq(ShareTopicField.REMOVESTATUS, ActStatus.UNACT.getCode())));

            mapMessage.put("info",info);
        } catch (ServiceException e) {
             GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        mapMessage.put("shareid", shareId);

        return new ModelAndView("/sync/createsharetopic", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "sharetopic", required = false) String topic,
                                      @RequestParam(value = "shareid", required = true) Long shareId
                                      ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            ShareTopic shareTopic = new ShareTopic();
            shareTopic.setShareId(shareId);
            shareTopic.setCreateDate(new Date());
            shareTopic.setCreateUserId(getCurrentUser().getUserid());
            shareTopic.setCreateUserIp(getIp());
            shareTopic.setRemoveStatus(ActStatus.UNACT);
            shareTopic.setShareTopic(topic);
            shareTopic.setLastModifyUserid(getCurrentUser().getUserid());
            shareTopic.setLastModifyDate(new Date());
            shareTopic.setLastModifyUserIp(getIp());

            SyncServiceSngl.get().createShareTopic(shareTopic);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
        }

        mapMessage.put("shareid", shareId);
        return new ModelAndView("forward:/sync/sharetopic/list", mapMessage);
    }

    //
    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyPage(@RequestParam(value = "sharetopicid", required = true) Long shareTopicId,
                                   @RequestParam(value = "shareid", required = true) Long shareId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            ShareBaseInfo info = SyncServiceSngl.get().getShareInfo(new QueryExpress()
                    .add(QueryCriterions.eq(ShareBaseInfoField.SHAREID, shareId))
                    .add(QueryCriterions.eq(ShareTopicField.REMOVESTATUS, ActStatus.UNACT.getCode())));

            mapMessage.put("info",info);
        } catch (ServiceException e) {
             GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        mapMessage.put("shareid", shareId);

        try {
            List<ShareTopic> list = SyncServiceSngl.get().queryShareTopic(
                    new QueryExpress().add(QueryCriterions.eq(ShareTopicField.SHARETOPICID, shareTopicId))
                            .add(QueryCriterions.eq(ShareTopicField.REMOVESTATUS, ActStatus.UNACT.getCode())));
            ShareTopic topic = list.get(0);
            if (topic == null) {
               mapMessage.put("shareTopicError", "topic.shareId.empty");
               return new ModelAndView("forward:/sync/sharetopic/list", mapMessage);
            }
            mapMessage.put("topic", topic);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
        }
        mapMessage.put("sharetopicid", shareTopicId);
        return new ModelAndView("/sync/modifysharetopic", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "sharetopic", required = false) String shareTopic,
                               @RequestParam(value = "sharetopicid", required = true) Long shareTopicId,
                               @RequestParam(value = "shareid", required = true) Long shareId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(ShareTopicField.SHARETOPIC, shareTopic);
        updateExpress.set(ShareTopicField.LASTMODIFYIP, getCurrentUser().getUserid());
        updateExpress.set(ShareTopicField.LASTMODIFYDATE, new Date());
        updateExpress.set(ShareTopicField.LASTMODIFYIP, getIp());
        try {
            boolean bool = SyncServiceSngl.get().modifyShareTopic(updateExpress, new QueryExpress().add(QueryCriterions.eq(ShareTopicField.SHARETOPICID, shareTopicId)),shareId);

            if(bool){
                ToolsLog log = new ToolsLog();

                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.MODIFY_SHARE_TOPIC);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("share topic id:"+shareTopicId);

                addLog(log);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
        }
        mapMessage.put("sharetopicid", shareTopicId);
        mapMessage.put("shareid", shareId);
        return new ModelAndView("forward:/sync/sharetopic/list", mapMessage);
    }

    //
    @RequestMapping(value = "/delete")
    public ModelAndView delete(@RequestParam(value = "sharetopicid", required = true) Long shareTopicId,
                               @RequestParam(value = "shareid", required = true) Long shareId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(ShareTopicField.REMOVESTATUS, ActStatus.ACTED.getCode());
        try {
            SyncServiceSngl.get().modifyShareTopic(updateExpress, new QueryExpress().add(QueryCriterions.eq(ShareTopicField.SHARETOPICID, shareTopicId)),shareId);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
        }
        mapMessage.put("sharetopicid", shareTopicId);
        return new ModelAndView("forward:/sync/sharetopic/list", mapMessage);
    }
    @RequestMapping(value = "/recover")
    public ModelAndView recover(@RequestParam(value = "sharetopicid", required = true) Long shareTopicId,
                               @RequestParam(value = "shareid", required = true) Long shareId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(ShareTopicField.REMOVESTATUS, ActStatus.UNACT.getCode());
        try {
            SyncServiceSngl.get().modifyShareTopic(updateExpress, new QueryExpress().add(QueryCriterions.eq(ShareTopicField.SHARETOPICID, shareTopicId)),shareId);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
        }
        mapMessage.put("sharetopicid", shareTopicId);
        return new ModelAndView("forward:/sync/sharetopic/list", mapMessage);
    }

}
