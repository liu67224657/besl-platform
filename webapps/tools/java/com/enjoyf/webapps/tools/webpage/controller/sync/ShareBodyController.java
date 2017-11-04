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
@RequestMapping(value = "/sync/sharebody")
public class ShareBodyController extends ToolsBaseController {

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
        queryExpress.add(QuerySort.add(ShareBodyField.CREATEDATE, QuerySortOrder.DESC));

        mapMessage.put("shareid",shareId);

        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);

        try {
            if(shareId!=null){
                queryExpress.add(QueryCriterions.eq(ShareBodyField.SHAREID, shareId));
                PageRows<ShareBody> pageRows = SyncServiceSngl.get().queryShareBodyByPage(queryExpress, pagination);
                mapMessage.put("list", pageRows.getRows());
                mapMessage.put("page", pageRows.getPage());
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/sync/sharebodylist", mapMessage);
    }


    //
    @RequestMapping(value = "/createpage")
    public ModelAndView createBodyPage(@RequestParam(value = "shareid", required = false) Long shareId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            ShareBaseInfo info = SyncServiceSngl.get().getShareInfo(new QueryExpress()
                    .add(QueryCriterions.eq(ShareBaseInfoField.SHAREID, shareId))
                    .add(QueryCriterions.eq(ShareTopicField.REMOVESTATUS, ActStatus.UNACT.getCode())));
            mapMessage.put("info", info);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
        }
        mapMessage.put("shareid", shareId);
        return new ModelAndView("/sync/createsharebody", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView createBody(@RequestParam(value = "sharesubject", required = false) String subject,
                                   @RequestParam(value = "sharebody", required = false) String body,
                                   @RequestParam(value = "picurl", required = false) String url,
                                   @RequestParam(value = "shareid", required = false) Long shareId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            ShareBody shareBody = new ShareBody();
            shareBody.setShareId(shareId);
            shareBody.setShareSubject(subject);
            shareBody.setShareBody(body);
            shareBody.setPicUrl(url);
            shareBody.setCreateDate(new Date());
            shareBody.setCreateUserId(getCurrentUser().getUserid());
            shareBody.setCreateUserIp(getIp());
            shareBody.setLastModifyUserId(getCurrentUser().getUserid());
            shareBody.setLastModifyDate(new Date());
            shareBody.setLastModifyUserIp(getIp());
            shareBody.setRemoveStatus(ActStatus.UNACT);
            SyncServiceSngl.get().createShareBody(shareBody);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
        }
        mapMessage.put("shareid", shareId);
        return new ModelAndView("redirect:/sync/sharebody/list", mapMessage);
    }

    //
    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyPage(@RequestParam(value = "sharebodyid", required = true) Long shareBodyId,
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
            List<ShareBody> list = SyncServiceSngl.get().queryShareBody(
                    new QueryExpress().add(QueryCriterions.eq(ShareBodyField.SHAREBODYID, shareBodyId)));
            ShareBody body = list.get(0);
            if (body == null) {
                mapMessage.put("shareBodyError", "body.shareBodyId.empty");
                return new ModelAndView("forward:/sync/sharebody/list", mapMessage);
            }
            mapMessage.put("body", body);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
        }
        mapMessage.put("sharebodyid", shareBodyId);
        return new ModelAndView("/sync/modifysharebody", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "sharesubject", required = false) String subject,
                               @RequestParam(value = "sharebody", required = false) String body,
                               @RequestParam(value = "picurl", required = false) String url,
                               @RequestParam(value = "sharebodyid", required = true) Long shareBodyId,
                               @RequestParam(value = "shareid", required = true) Long shareId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(ShareBodyField.SHARESUBJECT, subject);
        updateExpress.set(ShareBodyField.SHAREBODY, body);
        updateExpress.set(ShareBodyField.PIC_URL, url);
        updateExpress.set(ShareBodyField.LASTMODIFYUSERID, getCurrentUser().getUserid());
        updateExpress.set(ShareBodyField.LASTMODIFYDATE, new Date());
        updateExpress.set(ShareBodyField.LASTMODIFYIP, getIp());
        try {
            boolean  bool = SyncServiceSngl.get().modifyShareBody(updateExpress, new QueryExpress().add(QueryCriterions.eq(ShareBodyField.SHAREBODYID, shareBodyId)), shareId);

            if(bool){
                //
                ToolsLog log = new ToolsLog();

                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.MODIFY_SHARE_BODY);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("share body id:"+shareBodyId);

                addLog(log);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
        }
        mapMessage.put("sharebodyid", shareBodyId);
        mapMessage.put("shareid", shareId);
        return new ModelAndView("forward:/sync/sharebody/list", mapMessage);
    }

    //
    @RequestMapping(value = "/delete")
    public ModelAndView deleteBody(@RequestParam(value = "sharebodyid", required = true) Long shareBodyId,
                                   @RequestParam(value = "shareid", required = true) Long shareId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(ShareBodyField.REMOVESTATUS, ActStatus.ACTED.getCode());
        try {
            SyncServiceSngl.get().modifyShareBody(updateExpress, new QueryExpress().add(QueryCriterions.eq(ShareBodyField.SHAREBODYID, shareBodyId)), shareId);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
        }
        mapMessage.put("sharebodyid", shareBodyId);
        mapMessage.put("shareid", shareId);
        return new ModelAndView("forward:/sync/sharebody/list", mapMessage);
    }
    @RequestMapping(value = "/recover")
    public ModelAndView recover(@RequestParam(value = "sharebodyid", required = true) Long shareBodyId,
                                   @RequestParam(value = "shareid", required = true) Long shareId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(ShareBodyField.REMOVESTATUS, ActStatus.UNACT.getCode());
        try {
            SyncServiceSngl.get().modifyShareBody(updateExpress, new QueryExpress().add(QueryCriterions.eq(ShareBodyField.SHAREBODYID, shareBodyId)), shareId);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
        }
        mapMessage.put("sharebodyid", shareBodyId);
        mapMessage.put("shareid", shareId);
        return new ModelAndView("forward:/sync/sharebody/list", mapMessage);
    }

}
