package com.enjoyf.webapps.tools.webpage.controller.joymeapp;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.service.tools.ToolsLog;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-6-25
 * Time: 上午10:55
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/platinum")
public class AppPlatinumController extends ToolsBaseController {

    private static Set<ClientItemType> itemTypes = new HashSet<ClientItemType>();

    static {
        itemTypes.add(ClientItemType.APPS);
        itemTypes.add(ClientItemType.EVENTS);
    }

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                             @RequestParam(value = "itemtype", required = false) Integer itemType

    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("itemTypes", itemTypes);
        mapMessage.put("itemType", itemType);

        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
        try {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(ClientLineField.LINE_TYPE, ClientLineType.PLATINUM.getCode()));

            PageRows<ClientLine> pageRows = JoymeAppServiceSngl.get().queryClientLineByPage(queryExpress, pagination);
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                mapMessage.put("list", pageRows.getRows());
                mapMessage.put("page", pageRows.getPage());
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/joymeapp/platinum/linelist", mapMessage);
        }
        return new ModelAndView("/joymeapp/platinum/linelist", mapMessage);
    }

    @RequestMapping(value = "createpage")
    public ModelAndView createPage() {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("itemTypes", itemTypes);

        return new ModelAndView("/joymeapp/platinum/createline", mapMessage);
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
                return new ModelAndView("forward:/platinum/createpage", mapMessage);
            }

            ClientLine clientLine = new ClientLine();
            clientLine.setLineName(lineName);
            clientLine.setCode(lineCode);
            clientLine.setItemType(ClientItemType.getByCode(itemType));
            clientLine.setCreateDate(new Date());
            clientLine.setCreateUserid(getCurrentUser().getUserid());
            clientLine.setValidStatus(ValidStatus.INVALID);
            clientLine.setPlatform(ClientPlatform.IPHONE.getCode());
            clientLine.setLineType(ClientLineType.PLATINUM);

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

        return new ModelAndView("redirect:/platinum/list");
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyLinePage(@RequestParam(value = "lineid", required = false) Long lineId) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();
        if (lineId == null) {
            return new ModelAndView("redirect:/platinum/list");
        }
        mapMessage.put("itemTypes", itemTypes);

        try {
            ClientLine clientLine = JoymeAppServiceSngl.get().getClientLine(new QueryExpress().add(QueryCriterions.eq(ClientLineField.LINE_ID, lineId)));
            if (clientLine == null) {
                return new ModelAndView("redirect:/platinum/list");
            }
            mapMessage.put("clientLine", clientLine);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("/joymeapp/platinum/modifyline", mapMessage);
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
                return new ModelAndView("forward:/platinum/modifypage", mapMessage);
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
        return new ModelAndView("redirect:/platinum/list");
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
        return new ModelAndView("redirect:/platinum/list");
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
        return new ModelAndView("redirect:/platinum/list");
    }

    @RequestMapping(value = "/detail")
    public ModelAndView detail(@RequestParam(value = "lineid", required = false) Long lineId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            ClientLine clientLine = JoymeAppServiceSngl.get().getClientLine(new QueryExpress().add(QueryCriterions.eq(ClientLineField.LINE_ID, lineId)));
            if (clientLine == null) {
                return new ModelAndView("redirect:/platinum/list");
            }
            mapMessage.put("clientLine", clientLine);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("/joymeapp/platinum/linedetail", mapMessage);
    }
}
