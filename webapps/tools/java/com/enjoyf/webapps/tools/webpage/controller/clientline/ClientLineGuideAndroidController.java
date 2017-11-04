package com.enjoyf.webapps.tools.webpage.controller.clientline;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.service.ServiceException;
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
 * User: zhitaoshi
 * Date: 13-12-10
 * Time: 下午2:08
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/clientline/android/guide")
public class ClientLineGuideAndroidController extends ToolsBaseController {

    private static final int CLIENT_PLATFORM_ANDROID = ClientPlatform.ANDROID.getCode();
    
    private static Set<ClientItemType> itemTypes = new HashSet<ClientItemType>();

    static {
        itemTypes.add(ClientItemType.GAME);
        itemTypes.add(ClientItemType.ARTICLE);
    }

    @RequestMapping(value = "/list")
    public ModelAndView lineList(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                                 @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                                 @RequestParam(value = "itemtype", required = false) Integer itemType) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("itemType", itemType);
        mapMessage.put("itemTypeCollection", itemTypes);

        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);

        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(ClientLineField.PLATFORM, CLIENT_PLATFORM_ANDROID));
        queryExpress.add(QueryCriterions.eq(ClientLineField.LINE_TYPE, ClientLineType.GUIDEITEM.getCode()));
        if (itemType != null) {
            queryExpress.add(QueryCriterions.eq(ClientLineField.ITEM_TYPE, itemType));
        }
        queryExpress.add(QuerySort.add(ClientLineField.LINE_ID, QuerySortOrder.DESC));

        try {
            PageRows<ClientLine> clientLinePageRows = JoymeAppServiceSngl.get().queryClientLineByPage(queryExpress, pagination);
            if (clientLinePageRows != null) {
                mapMessage.put("list", clientLinePageRows.getRows());
                mapMessage.put("page", clientLinePageRows.getPage());
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/clientline/androidguide/linelist", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createLinePage() {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        mapMessage.put("itemTypeCollection", itemTypes);

        return new ModelAndView("/clientline/androidguide/createline", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView createLine(@RequestParam(value = "linename", required = false) String lineName,
                                   @RequestParam(value = "linecode", required = false) String lineCode,
                                   @RequestParam(value = "itemtype", required = false) Integer itemType) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            ClientLine existLine = JoymeAppServiceSngl.get().getClientLine(new QueryExpress().add(QueryCriterions.eq(ClientLineField.CODE, lineCode)));
            if (existLine != null) {
                mapMessage.put("lineName", lineName);
                mapMessage.put("lineCode", lineCode);
                mapMessage.put("itemType", itemType);
                mapMessage.put("codeExist", "line.code.exist");
                return new ModelAndView("forward:/clientline/android/guide/createpage", mapMessage);
            }

            ClientLine clientLine = new ClientLine();
            clientLine.setLineName(lineName);
            clientLine.setCode(lineCode);
            clientLine.setItemType(ClientItemType.getByCode(itemType));
            clientLine.setCreateDate(new Date());
            clientLine.setCreateUserid(getCurrentUser().getUserid());
            clientLine.setValidStatus(ValidStatus.INVALID);
            clientLine.setPlatform(CLIENT_PLATFORM_ANDROID);
            clientLine.setLineType(ClientLineType.GUIDEITEM);

            ClientLine returnLine = JoymeAppServiceSngl.get().createClientLine(clientLine);
            if (returnLine != null) {
                ToolsLog log = new ToolsLog();

                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.CREATE_CLIENT_LINE);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("clientLineId:" + returnLine.getLineId());

                addLog(log);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("redirect:/clientline/android/guide/list?itemtype=" + itemType);
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyLinePage(@RequestParam(value = "lineid", required = true) Long lineId) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();
        if (lineId == null) {
            return new ModelAndView("redirect:/clientline/android/guide/list");
        }
        mapMessage.put("itemTypeCollection", itemTypes);

        try {
            ClientLine clientLine = JoymeAppServiceSngl.get().getClientLine(new QueryExpress().add(QueryCriterions.eq(ClientLineField.LINE_ID, lineId)));
            if (clientLine == null) {
                return new ModelAndView("redirect:/clientline/android/guide/list");
            }
            mapMessage.put("clientLine", clientLine);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("/clientline/androidguide/modifyline", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modifyLine(@RequestParam(value = "lineid", required = false) Long lineId,
                                   @RequestParam(value = "linename", required = false) String lineName,
                                   @RequestParam(value = "linecode", required = false) String lineCode,
                                   @RequestParam(value = "itemtype", required = false) Integer itemType) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(ClientLineField.LINE_NAME, lineName);
        updateExpress.set(ClientLineField.CODE, lineCode);
        updateExpress.set(ClientLineField.ITEM_TYPE, itemType);
        updateExpress.set(ClientLineField.MODIFY_DATE, new Date());
        updateExpress.set(ClientLineField.MODIFY_USERID, getCurrentUser().getUserid());

        try {
            ClientLine existLine = JoymeAppServiceSngl.get().getClientLine(new QueryExpress().add(QueryCriterions.eq(ClientLineField.CODE, lineCode)).add(QueryCriterions.ne(ClientLineField.LINE_ID, lineId)));
            if (existLine != null) {
                mapMessage.put("lineid", lineId);
                mapMessage.put("codeExist", "line.code.exist");
                return new ModelAndView("forward:/clientline/android/guide/modifypage", mapMessage);
            }

            Boolean bool = JoymeAppServiceSngl.get().modifyClientLine(updateExpress, new QueryExpress().add(QueryCriterions.eq(ClientLineField.LINE_ID, lineId)));
            if (bool) {
                ToolsLog log = new ToolsLog();

                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.MODIFY_CLIENT_LINE);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("clientLineId:" + lineId);

                addLog(log);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/clientline/android/guide/list?itemtype=" + itemType);
    }

    @RequestMapping(value = "/delete")
    public ModelAndView deleteLine(@RequestParam(value = "lineid", required = false) Long lineId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(ClientLineField.VALID_STATUS, ValidStatus.REMOVED.getCode());
        updateExpress.set(ClientLineField.MODIFY_DATE, new Date());
        updateExpress.set(ClientLineField.MODIFY_USERID, getCurrentUser().getUserid());

        try {
            Boolean bool = JoymeAppServiceSngl.get().modifyClientLine(updateExpress, new QueryExpress().add(QueryCriterions.eq(ClientLineField.LINE_ID, lineId)));
            if (bool) {
                ToolsLog log = new ToolsLog();

                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.DELETE_CLIENT_LINE);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("clientLineId:" + lineId);

                addLog(log);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/clientline/android/guide/list");
    }

    @RequestMapping(value = "/recover")
    public ModelAndView modifyLine(@RequestParam(value = "lineid", required = false) Long lineId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(ClientLineField.VALID_STATUS, ValidStatus.VALID.getCode());
        updateExpress.set(ClientLineField.MODIFY_DATE, new Date());
        updateExpress.set(ClientLineField.MODIFY_USERID, getCurrentUser().getUserid());

        try {
            Boolean bool = JoymeAppServiceSngl.get().modifyClientLine(updateExpress, new QueryExpress().add(QueryCriterions.eq(ClientLineField.LINE_ID, lineId)));
            if (bool) {
                ToolsLog log = new ToolsLog();

                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.RECOVER_CLIENT_LINE);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("clientLineId:" + lineId);

                addLog(log);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/clientline/android/guide/list");
    }

    @RequestMapping(value = "/detail")
    public ModelAndView detail(@RequestParam(value = "lineid", required = false) Long lineId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            ClientLine clientLine = JoymeAppServiceSngl.get().getClientLine(new QueryExpress().add(QueryCriterions.eq(ClientLineField.LINE_ID, lineId)));
            if (clientLine == null) {
                return new ModelAndView("redirect:/clientline/android/guide/list");
            }
            mapMessage.put("clientLine", clientLine);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("/clientline/androidguide/linedetail", mapMessage);
    }

}
