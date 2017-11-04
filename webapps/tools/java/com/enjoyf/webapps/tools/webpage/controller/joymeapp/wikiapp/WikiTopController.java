package com.enjoyf.webapps.tools.webpage.controller.joymeapp.wikiapp;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.service.tools.ToolsLog;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.webapps.tools.weblogic.joymeapp.JoymeAppWebLogic;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-9-17
 * Time: 上午10:02
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/joymeapp/wiki/top")
public class WikiTopController extends ToolsBaseController {

    @Resource(name = "jomyeAppWebLogic")
    private JoymeAppWebLogic joymeAppWebLogic;

    @RequestMapping(value = "/list")
    public ModelAndView queryTopMenuList(@RequestParam(value = "appkey", required = false) String appKey,
                                         @RequestParam(value = "platform", required = false) Integer platform,
                                         @RequestParam(value = "channelid", required = false) Long channelId,
                                         @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                                         @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {

            ClientLine clientLine = JoymeAppServiceSngl.get().getClientLineByCode("wikitop_0");
            if (clientLine == null) {
                clientLine = new ClientLine();
                clientLine.setCode("wikitop_0");
                clientLine.setPlatform(0);
                clientLine.setItemType(ClientItemType.WIKIAPP_WIKI_RANK);
                clientLine.setLineName("wiki排行榜_iphone");
                clientLine.setCreateDate(new Date());
                clientLine.setCreateUserid("sysadmin");
                clientLine.setDisplay_order(0);
                clientLine.setValidStatus(ValidStatus.INVALID);
                clientLine.setLineType(ClientLineType.WIKIAPP_WIKIRANK);
                clientLine.setHot(0);
                JoymeAppServiceSngl.get().createClientLine(clientLine);
            }

//            ClientLine clientLine2 = JoymeAppServiceSngl.get().getClientLineByCode("wikitop_1");
//            if (clientLine2 == null) {
//                clientLine2 = new ClientLine();
//                clientLine2.setCode("wikitop_1");
//                clientLine2.setPlatform(1);
//                clientLine2.setItemType(ClientItemType.WIKIAPP_WIKI_RANK);
//                clientLine2.setLineName("wiki排行榜_android");
//                clientLine2.setCreateDate(new Date());
//                clientLine2.setCreateUserid("sysadmin");
//                clientLine2.setDisplay_order(1);
//                clientLine2.setHot(0);
//                clientLine2.setValidStatus(ValidStatus.INVALID);
//                clientLine2.setLineType(ClientLineType.WIKIAPP_WIKIRANK);
//
//                JoymeAppServiceSngl.get().createClientLine(clientLine2);
//            }


            int curPage = (pageStartIndex / pageSize) + 1;
            Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
            QueryExpress queryExpress = new QueryExpress();
            if (platform != null) {
                queryExpress.add(QueryCriterions.eq(ClientLineField.PLATFORM, platform));
                mapMessage.put("platform", platform);
            }
            queryExpress.add(QueryCriterions.eq(ClientLineField.ITEM_TYPE, ClientItemType.WIKIAPP_WIKI_RANK.getCode()));
            queryExpress.add(QueryCriterions.eq(ClientLineField.LINE_TYPE, ClientLineType.WIKIAPP_WIKIRANK.getCode()));
            queryExpress.add(QuerySort.add(ClientLineField.DISPLAY_ORDER, QuerySortOrder.ASC));

            PageRows<ClientLine> pageRows = JoymeAppServiceSngl.get().queryClientLineByPage(queryExpress, pagination);
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                mapMessage.put("list", pageRows.getRows());
                mapMessage.put("page", pageRows.getPage());
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/gameclient/gamecategory/linelist", mapMessage);
        }

        return new ModelAndView("/joymeapp/wikiapp/wikitop/wikitoplinelist", mapMessage);
    }

//    @RequestMapping(value = "/modifypage")
//    public ModelAndView modifyPage(@RequestParam(value = "lineid", required = false) Long lineId) {
//        Map<String, Object> mapMessage = new HashMap<String, Object>();
//        try {
//            ClientLine clientLine = JoymeAppServiceSngl.get().getClientLine(new QueryExpress().add(QueryCriterions.eq(ClientLineField.LINE_ID, lineId)));
//            mapMessage.put("clientline", clientLine);
//        } catch (ServiceException e) {
//            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
//            mapMessage = putErrorMessage(mapMessage, "system.error");
//            return new ModelAndView("/gameclient/gamecategory/linelist", mapMessage);
//        }
//
//
//        return new ModelAndView("/joymeapp/wikiapp/wikitop/modifypage", mapMessage);
//    }


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
        return new ModelAndView("redirect:/joymeapp/wiki/top/list");
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
        return new ModelAndView("redirect:/joymeapp/wiki/top/list");
    }


}
