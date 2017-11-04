package com.enjoyf.webapps.tools.webpage.controller.advertise;

import com.enjoyf.platform.db.advertise.AdvertiseIdGenerator;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.advertise.AdvertiseAgentField;
import com.enjoyf.platform.service.advertise.AdvertiseAppUrl;
import com.enjoyf.platform.service.advertise.AdvertiseAppUrlField;
import com.enjoyf.platform.service.advertise.AdvertiseServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.service.tools.ToolsConstants;
import com.enjoyf.platform.service.tools.ToolsLog;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 13-8-13
 * Time: 下午4:42
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/advertise/appurl")
public class AdvertiseAppUrlController extends ToolsBaseController {
    @RequestMapping(value = "/list")
    public ModelAndView listAppUrl(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") Integer pageStartIndex,
                                   @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") Integer pageSize,
                                   @RequestParam(value = "searchname", required = false) String name) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            int curPage = (pageStartIndex / pageSize) + 1;
            Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);

            QueryExpress queryExpress = new QueryExpress();
            if (!StringUtil.isEmpty(name)) {
                queryExpress.add(QueryCriterions.like(AdvertiseAppUrlField.CODE_NAME, "%" + name + "%"));
            }
            queryExpress.add(QuerySort.add(AdvertiseAppUrlField.CREATETIME, QuerySortOrder.DESC));

            PageRows<AdvertiseAppUrl> pageRows = AdvertiseServiceSngl.get().pageAppUrl(queryExpress, pagination);
            mapMessage.put("list", pageRows.getRows());
            mapMessage.put("page", pageRows.getPage());
            mapMessage.put("searchname", name);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "occured ServiceExcpetion.e");
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("/advertise/appurllist", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createPage() {
        return new ModelAndView("/advertise/appurlcreatepage");
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "iosUrl", required = false) String iosUrl,
                               @RequestParam(value = "androidUrl", required = false) String androidUrl,
                               @RequestParam(value = "codename", required = false) String codeName) {

        try {
            AdvertiseAppUrl advertiseAppUrl = new AdvertiseAppUrl();
            advertiseAppUrl.setCodeName(codeName);
            advertiseAppUrl.setIosUrl(iosUrl);
            advertiseAppUrl.setAndroidUrl(androidUrl);
            advertiseAppUrl.setRemoveStatus(ActStatus.UNACT);
            advertiseAppUrl.setCreateId(getCurrentUser().getUserid());
            advertiseAppUrl.setCreateIp(getIp());
            advertiseAppUrl.setCreateTime(new Date());
            advertiseAppUrl = AdvertiseServiceSngl.get().insertAppUrl(advertiseAppUrl);

            //
            try {
                ToolsLog log = new ToolsLog();
                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.ADVERTISE_APPURL_CREATE);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter(advertiseAppUrl.toString());
                addLog(log);
            } catch (Exception e) {
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "occured ServiceExcpetion.e");
        }
        return new ModelAndView("redirect:/advertise/appurl/list");

    }

    @RequestMapping(value = "delete")
    public ModelAndView delete(@RequestParam(value = "clientId", required = true) Long clientId,
                               @RequestParam(value = "code", required = false) String code) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            if (clientId == null) {
                new ModelAndView("redirect:/advertise/appurl/list");
            }
            mapMessage.put("clientId", clientId);
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(AdvertiseAppUrlField.REMOVESTATUS, ActStatus.ACTED.getCode());
            updateExpress.set(AdvertiseAppUrlField.LASTMODIFYID, getCurrentUser().getUserid());
            updateExpress.set(AdvertiseAppUrlField.LASTMODIFYIP, getIp());
            updateExpress.set(AdvertiseAppUrlField.LASTMODIFYTIME, new Date());
            AdvertiseServiceSngl.get().modifyAppUrl(updateExpress, new QueryExpress().add(QueryCriterions.eq(AdvertiseAppUrlField.CLIENTURLID, clientId)), code);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "occured ServiceExcpetion.e");
        }

        return new ModelAndView("redirect:/advertise/appurl/list");
    }

    @RequestMapping(value = "recover")
    public ModelAndView recover(@RequestParam(value = "clientId", required = true) Long clientId,
                                @RequestParam(value = "code", required = false) String code) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            if (clientId == null) {
                new ModelAndView("redirect:/advertise/appurl/list");
            }
            mapMessage.put("clientId", clientId);
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(AdvertiseAppUrlField.REMOVESTATUS, ActStatus.UNACT.getCode());
            updateExpress.set(AdvertiseAppUrlField.LASTMODIFYID, getCurrentUser().getUserid());
            updateExpress.set(AdvertiseAppUrlField.LASTMODIFYIP, getIp());
            updateExpress.set(AdvertiseAppUrlField.LASTMODIFYTIME, new Date());

            AdvertiseServiceSngl.get().modifyAppUrl(updateExpress, new QueryExpress().add(QueryCriterions.eq(AdvertiseAppUrlField.CLIENTURLID, clientId)), code);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "occured ServiceExcpetion.e");
        }

        return new ModelAndView("redirect:/advertise/appurl/list");
    }

    @RequestMapping(value = "modifypage")
    public ModelAndView modifyPage(@RequestParam(value = "clientId", required = true) Long clientId,
                                   @RequestParam(value = "code", required = true) String code
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            if (clientId == null) {
                return new ModelAndView("redirect:/advertise/appurl/list");
            }
            AdvertiseAppUrl advertiseAppUrl = AdvertiseServiceSngl.get().getAppUrl(new QueryExpress().add(QueryCriterions.eq(AdvertiseAppUrlField.CLIENTURLID, clientId)).add(QueryCriterions.eq(AdvertiseAppUrlField.CODE, code)));
            mapMessage.put("advertiseAppUrl", advertiseAppUrl);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "occured ServiceExcpetion.e");
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("advertise/appurlmodify", mapMessage);
    }

    @RequestMapping(value = "modify")
    public ModelAndView modify(@RequestParam(value = "clientId", required = true) Long clientId,
                               @RequestParam(value = "iosUrl", required = false) String iosUrl,
                               @RequestParam(value = "androidUrl", required = false) String androidUrl,
                               @RequestParam(value = "code", required = false) String code,
                               @RequestParam(value = "codename", required = false) String codeName) {
        try {
            if (clientId == null) {
                return new ModelAndView("redirect:/advertise/appurl/list");
            }
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(AdvertiseAppUrlField.CODE_NAME, codeName);
            updateExpress.set(AdvertiseAppUrlField.IOSURL, iosUrl);
            updateExpress.set(AdvertiseAppUrlField.ANDROIDURL, androidUrl);
            updateExpress.set(AdvertiseAppUrlField.LASTMODIFYID, getCurrentUser().getUserid());
            updateExpress.set(AdvertiseAppUrlField.LASTMODIFYIP, getIp());
            updateExpress.set(AdvertiseAppUrlField.LASTMODIFYTIME, new Date());
            AdvertiseServiceSngl.get().modifyAppUrl(updateExpress, new QueryExpress().add(QueryCriterions.eq(AdvertiseAppUrlField.CLIENTURLID, clientId)), code);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "occured ServiceExcpetion.e");
        }
        return new ModelAndView("redirect:/advertise/appurl/list");
    }
}
