package com.enjoyf.webapps.tools.webpage.controller.collection;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.gameres.gamedb.GameCategoryType;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.service.tools.ToolsLog;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by zhitaoshi on 2015/6/17.
 */
@Controller
@RequestMapping(value = "/collection/game/clientline")
public class GameCollectionLineController extends ToolsBaseController {

    private static List<ClientItemType> itemTypes = new ArrayList<ClientItemType>();

    static {
        itemTypes.add(ClientItemType.GAME_RECOMMEND);
        itemTypes.add(ClientItemType.GAME_NEWS);
        itemTypes.add(ClientItemType.GAME_MOBILE);
        itemTypes.add(ClientItemType.GAME_PC);
        itemTypes.add(ClientItemType.GAME_PSP);
        itemTypes.add(ClientItemType.GAME_TV);
        itemTypes.add(ClientItemType.GAME_HOT_MAJOR);
    }

    @RequestMapping(value = "/list")
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(ClientLineField.LINE_TYPE, ClientLineType.GAME_COLLECTION.getCode()));
        queryExpress.add(QuerySort.add(ClientLineField.LINE_ID, QuerySortOrder.DESC));
        try {
            List<ClientLine> lineList = JoymeAppServiceSngl.get().queryClientLineList(queryExpress);
            mapMessage.put("list", lineList);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/collection/linelist", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createPage(@RequestParam(value = "linename", required = false) String lineName,
                                   @RequestParam(value = "linecode", required = false) String lineCode,
                                   @RequestParam(value = "itemtype", required = false) Integer itemType,
                                   @RequestParam(value = "error", required = false) String error) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("lineName", lineName);
        mapMessage.put("lineCode", lineCode);
        mapMessage.put("itemType", itemType);
        mapMessage.put("errorMsg", error);
        mapMessage.put("itemTypeCollection", itemTypes);
        Map<Integer, String> categoryMap = new HashMap<Integer, String>();
        for(GameCategoryType categoryType:GameCategoryType.getAll()){
            categoryMap.put(categoryType.getCode(), categoryType.getValue());
        }
        mapMessage.put("gameCateMap", categoryMap);
        return new ModelAndView("/collection/createline", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "linename", required = false) String lineName,
                               @RequestParam(value = "linecode", required = false) String lineCode,
                               @RequestParam(value = "itemtype", required = false) Integer itemType,
                               @RequestParam(value = "category", required = false) String category) {
        try {
            ClientLine existLine = JoymeAppServiceSngl.get().getClientLine(new QueryExpress()
                    .add(QueryCriterions.eq(ClientLineField.CODE, lineCode)));
            if (existLine != null) {
                return new ModelAndView("redirect:/collection/game/clientline/createpage?linename=" + lineName + "&linecode=" + lineCode + "&itemtype=" + itemType + "&error=line.code.exist");
            }
            ClientLine clientLine = new ClientLine();
            clientLine.setLineName(lineName);
            clientLine.setCode(lineCode);
            clientLine.setItemType(ClientItemType.getByCode(itemType));
            clientLine.setCreateDate(new Date());
            clientLine.setCreateUserid(getCurrentUser().getUserid());
            clientLine.setValidStatus(ValidStatus.VALID);
            clientLine.setLineType(ClientLineType.GAME_COLLECTION);
            clientLine.setLine_desc(category);
            clientLine.setPlatform(AppPlatform.WEB.getCode());

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
            return new ModelAndView("redirect:/collection/game/clientline/list?error=system.error");
        }
        return new ModelAndView("redirect:/collection/game/clientline/list");
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyLinePage(@RequestParam(value = "lineid", required = true) Long lineId) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();
        if (lineId == null) {
            return new ModelAndView("redirect:/collection/game/clientline/list");
        }
        mapMessage.put("itemTypeCollection", itemTypes);
        Map<Integer, String> categoryMap = new HashMap<Integer, String>();
        for(GameCategoryType categoryType:GameCategoryType.getAll()){
            categoryMap.put(categoryType.getCode(), categoryType.getValue());
        }
        mapMessage.put("gameCateMap", categoryMap);
        try {
            ClientLine clientLine = JoymeAppServiceSngl.get().getClientLine(new QueryExpress().add(QueryCriterions.eq(ClientLineField.LINE_ID, lineId)));
            if (clientLine == null) {
                return new ModelAndView("redirect:/collection/game/clientline/list");
            }
            mapMessage.put("clientLine", clientLine);
            if (!StringUtil.isEmpty(clientLine.getLine_desc())) {
                mapMessage.put("category", Integer.parseInt(clientLine.getLine_desc()));
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return new ModelAndView("redirect:/collection/game/clientline/list?error=system.error");
        }
        return new ModelAndView("/collection/modifyline", mapMessage);
    }


    @RequestMapping(value = "/modify")
    public ModelAndView modifyLine(@RequestParam(value = "lineid", required = false) Long lineId,
                                   @RequestParam(value = "linename", required = false) String lineName,
                                   @RequestParam(value = "linecode", required = false) String lineCode,
                                   @RequestParam(value = "itemtype", required = false) Integer itemType,
                                   @RequestParam(value = "category", required = false) String category) {
        try {
            ClientLine existLine = JoymeAppServiceSngl.get().getClientLine(new QueryExpress().add(QueryCriterions.eq(ClientLineField.CODE, lineCode)).add(QueryCriterions.ne(ClientLineField.LINE_ID, lineId)));
            if (existLine != null) {
                return new ModelAndView("redirect:/collection/game/clientline/modifypage?lineid=" + lineId + "&error=line.code.exist");
            }
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(ClientLineField.LINE_NAME, lineName);
            updateExpress.set(ClientLineField.CODE, lineCode);
            updateExpress.set(ClientLineField.ITEM_TYPE, itemType);
            updateExpress.set(ClientLineField.MODIFY_DATE, new Date());
            updateExpress.set(ClientLineField.MODIFY_USERID, getCurrentUser().getUserid());
            updateExpress.set(ClientLineField.LINE_DESC, category);
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
            return new ModelAndView("redirect:/collection/game/clientline/list?error=system.error");
        }
        return new ModelAndView("redirect:/collection/game/clientline/list");
    }


    @RequestMapping(value = "/delete")
    public ModelAndView deleteLine(@RequestParam(value = "lineid", required = false) Long lineId,
                                   @RequestParam(value = "linecode", required = false) String lineCode) {
        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(ClientLineField.VALID_STATUS, ValidStatus.REMOVED.getCode());
        updateExpress.set(ClientLineField.MODIFY_DATE, new Date());
        updateExpress.set(ClientLineField.MODIFY_USERID, getCurrentUser().getUserid());

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("lineid", lineId);
        param.put("linecode", lineCode);
        param.put("linetype", ClientLineType.GAME_COLLECTION.getCode());
        param.put("platform", AppPlatform.WEB.getCode());
        param.put("status", ValidStatus.REMOVED.getCode());
        try {
            Boolean bool = JoymeAppServiceSngl.get().modifyClientLineByCache(updateExpress, param);
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
            return new ModelAndView("redirect:/collection/game/clientline/list?error=system.error");
        }
        return new ModelAndView("redirect:/collection/game/clientline/list");
    }

    @RequestMapping(value = "/recover")
    public ModelAndView modifyLine(@RequestParam(value = "lineid", required = false) Long lineId,
                                   @RequestParam(value = "linecode", required = false) String lineCode) {
        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(ClientLineField.VALID_STATUS, ValidStatus.VALID.getCode());
        updateExpress.set(ClientLineField.MODIFY_DATE, new Date());
        updateExpress.set(ClientLineField.MODIFY_USERID, getCurrentUser().getUserid());

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("lineid", lineId);
        param.put("linecode", lineCode);
        param.put("linetype", ClientLineType.GAME_COLLECTION.getCode());
        param.put("platform", AppPlatform.WEB.getCode());
        param.put("status", ValidStatus.VALID.getCode());
        try {
            Boolean bool = JoymeAppServiceSngl.get().modifyClientLineByCache(updateExpress, param);
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
            return new ModelAndView("redirect:/collection/game/clientline/list?error=system.error");
        }
        return new ModelAndView("redirect:/collection/game/clientline/list");
    }

    @RequestMapping(value = "/detail")
    public ModelAndView detail(@RequestParam(value = "lineid", required = false) Long lineId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            ClientLine clientLine = JoymeAppServiceSngl.get().getClientLine(new QueryExpress().add(QueryCriterions.eq(ClientLineField.LINE_ID, lineId)));
            if (clientLine == null) {
                return new ModelAndView("redirect:/collection/game/clientline/list");
            }
            mapMessage.put("clientLine", clientLine);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return new ModelAndView("redirect:/collection/game/clientline/list?error=system.error");
        }
        return new ModelAndView("/collection/linedetail", mapMessage);
    }

}
