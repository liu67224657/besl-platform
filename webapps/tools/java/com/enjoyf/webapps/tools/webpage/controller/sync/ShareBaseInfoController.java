package com.enjoyf.webapps.tools.webpage.controller.sync;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.sync.*;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.service.tools.ToolsConstants;
import com.enjoyf.platform.service.tools.ToolsLog;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import oracle.sql.DATE;
import org.apache.poi.hpbf.model.qcbits.QCBit;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.text.DateFormat.getDateInstance;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-31
 * Time: 上午9:43
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/sync/shareinfo")
public class ShareBaseInfoController extends ToolsBaseController {

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,      //数据库记录索引
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                             @RequestParam(value = "sharetype", required = false) Integer shareType) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("shareTypeCollection", ShareType.getAll());

        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
        try {
            ShareType shareTypeObj = null;
            if (shareType != null) {
                shareTypeObj = ShareType.getByCode(shareType);
            }

            QueryExpress queryExpress = new QueryExpress();

            queryExpress.add(QuerySort.add(ShareBaseInfoField.CREATEDATE, QuerySortOrder.DESC));
            if (shareType != null) {
                queryExpress.add(QueryCriterions.eq(ShareBaseInfoField.SHARETYPE, shareTypeObj.getCode()));

                mapMessage.put("stype", shareTypeObj.getCode());

                PageRows<ShareBaseInfo> pageRows = SyncServiceSngl.get().queryShareInfoByPage(queryExpress, pagination);

                mapMessage.put("list", pageRows.getRows());
                mapMessage.put("page", pageRows.getPage());
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("/sync/shareinfolist", mapMessage);
    }


    //
    @RequestMapping(value = "/createpage")
    public ModelAndView createShareBaseInfo() {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        mapMessage.put("sharerewardtypelist", ShareRewardType.getAll());

        mapMessage.put("shareTypeCollection", ShareType.getAll());
        return new ModelAndView("/sync/createshareinfo", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView createMessage(@RequestParam(value = "shareSource", required = true) String url,
                                      @RequestParam(value = "shareKey", required = true) String shareKey,
                                      @RequestParam(value = "displayStyle", required = false) String displayStyle,
                                      @RequestParam(value = "directId", required = false) String directId,
                                      @RequestParam(value = "sharetype", required = false) Integer shareTypeCode,
                                      @RequestParam(value = "shareRewardType", required = false) Integer shareRewardType,
                                      @RequestParam(value = "shareRewardPoint", required = false) Integer shareRewardPoint,
                                      @RequestParam(value = "shareRewardId", required = false) Long shareRewardId,
                                      @RequestParam(value = "expiredate", required = false) Date expireDate) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QuerySort.add(ShareBaseInfoField.CREATEDATE, QuerySortOrder.DESC));
        try {
            List<ShareBaseInfo> infoList = SyncServiceSngl.get().queryShareInfo(queryExpress);
            for (int i = 0; i < infoList.size(); i++) {
                if (shareKey.equals(infoList.get(i).getShareKey())) {
                    mapMessage.put("keyerror", "share.key.existed");
                    return new ModelAndView("forward:/sync/shareinfo/createpage", mapMessage);
                }
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        try {
            ShareBaseInfo shareInfo = new ShareBaseInfo();
            shareInfo.setShareKey(shareKey);
            shareInfo.setShareSource(url);
            shareInfo.setDisplayStyle(displayStyle);
            shareInfo.setDirectId(directId);
            shareInfo.setExpireDate(expireDate);
            shareInfo.setShareType(ShareType.getByCode(shareTypeCode));
            shareInfo.setCreateUserId(getCurrentUser().getUserid());
            shareInfo.setCreateDate(new Date());
            shareInfo.setCreateUserIp(getIp());
            shareInfo.setRemoveStatus(ActStatus.UNACT);
            if (shareRewardId != null) {
                shareInfo.setShareRewardId(shareRewardId);
            }
            if (shareRewardPoint != null) {
                shareInfo.setShareRewardPoint(shareRewardPoint);
            }
            shareInfo.setShareRewardType(ShareRewardType.getByValue(shareRewardType));

            shareInfo.setLastModifyUserid(getCurrentUser().getUserid());
            shareInfo.setLastModifyDate(new Date());
            shareInfo.setLastModifyUserIp(getIp());

            SyncServiceSngl.get().createShareInfo(shareInfo);

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("forward:/sync/shareinfo/list", mapMessage);
    }

    //
    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyPage(@RequestParam(value = "shareid", required = true) Long shareId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        mapMessage.put("shareTypeCollection", ShareType.getAll());
        mapMessage.put("sharerewardtypelist", ShareRewardType.getAll());
        try {
            ShareBaseInfo info = SyncServiceSngl.get().getShareInfo(new QueryExpress().add(QueryCriterions.eq(ShareBaseInfoField.SHAREID, shareId)));
            if (info == null) {
                mapMessage = putErrorMessage(mapMessage, "info.shareId.empty");
                return new ModelAndView("forward:/sync/shareinfo/list", mapMessage);
            }
            mapMessage.put("info", info);
            mapMessage.put("shareId", shareId);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("forward:/sync/shareinfo/list", mapMessage);
        }
        return new ModelAndView("/sync/modifyshareinfo", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "shareid", required = true) Long shareId,
                               @RequestParam(value = "shareKey", required = true) String shareKey,
                               @RequestParam(value = "shareSource", required = true) String url,
                               @RequestParam(value = "displayStyle", required = true) String displayStyle,
                               @RequestParam(value = "directId", required = false) String directId,
                               @RequestParam(value = "sharetype", required = false) Integer shareTypeCode,
                               @RequestParam(value = "shareRewardType", required = false) Integer shareRewardType,
                               @RequestParam(value = "shareRewardPoint", required = false) Integer shareRewardPoint,
                               @RequestParam(value = "shareRewardId", required = false) Long shareRewardId) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(ShareBaseInfoField.SHARESOURCE, url);
            updateExpress.set(ShareBaseInfoField.SHARESOURCEKEY, shareKey);
            updateExpress.set(ShareBaseInfoField.SHARETYPE, shareTypeCode);
            updateExpress.set(ShareBaseInfoField.SHAREREWARDTYPE, shareRewardType);
            updateExpress.set(ShareBaseInfoField.SHAREREWARDPOINT, shareRewardPoint);
            updateExpress.set(ShareBaseInfoField.SHAREREWARDID, shareRewardId);
            updateExpress.set(ShareBaseInfoField.SHAREDISPLAY_STYLE, displayStyle);
            updateExpress.set(ShareBaseInfoField.SHAREDIRECT_ID, directId);
            //
            updateExpress.set(ShareBaseInfoField.LASTMODIFYDATE, new Date());
            updateExpress.set(ShareBaseInfoField.LASTMODIFYUSERID, getCurrentUser().getUserid());
            updateExpress.set(ShareBaseInfoField.LASTMODIFYIP, getIp());

            boolean bool = SyncServiceSngl.get().modifyShareInfo(updateExpress, new QueryExpress().add(QueryCriterions.eq(ShareBaseInfoField.SHAREID, shareId)), shareId);

            //todo
            if (bool) {
                //添加log记录
                ToolsLog log = new ToolsLog();

                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.MODIFY_SHARE_BASE_INFO);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("share info id:" + shareId);

                addLog(log);
            }

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        mapMessage.put("shareid", shareId);
        return new ModelAndView("forward:/sync/shareinfo/list", mapMessage);
    }

    //
    @RequestMapping(value = "/delete")
    public ModelAndView delete(@RequestParam(value = "shareid", required = true) Long shareId,
                               @RequestParam(value = "sharetype", required = false) Integer shareTypeCode) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(ShareBaseInfoField.REMOVESTATUS, ActStatus.ACTED.getCode());
        try {
            SyncServiceSngl.get().modifyShareInfo(updateExpress, new QueryExpress().add(QueryCriterions.eq(ShareBaseInfoField.SHAREID, shareId)), shareId);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("forward:/sync/shareinfo/list", mapMessage);
    }

    @RequestMapping(value = "/recover")
    public ModelAndView recover(@RequestParam(value = "shareid", required = true) Long shareId,
                                @RequestParam(value = "sharetype", required = false) Integer shareTypeCode) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(ShareBaseInfoField.REMOVESTATUS, ActStatus.UNACT.getCode());
        try {
            SyncServiceSngl.get().modifyShareInfo(updateExpress, new QueryExpress().add(QueryCriterions.eq(ShareBaseInfoField.SHAREID, shareId)), shareId);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        mapMessage.put("shareid", shareId);
        return new ModelAndView("forward:/sync/shareinfo/list", mapMessage);
    }

}
