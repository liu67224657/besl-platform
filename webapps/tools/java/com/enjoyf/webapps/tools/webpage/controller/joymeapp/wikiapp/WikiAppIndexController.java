package com.enjoyf.webapps.tools.webpage.controller.joymeapp.wikiapp;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.LogOperType;
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
 * Created by zhitaoshi on 2015/4/15.
 */
@Controller
@RequestMapping(value = "/joymeapp/wikiapp/index")
public class WikiAppIndexController extends ToolsBaseController {

    private static Set<AppPlatform> platformSet = new HashSet<AppPlatform>();
    private static Set<ClientItemType> itemTypeSet = new HashSet<ClientItemType>();

    static {
        platformSet.add(AppPlatform.IOS);
        platformSet.add(AppPlatform.ANDROID);

        itemTypeSet.add(ClientItemType.WIKIAPP_HEADINFO);
        itemTypeSet.add(ClientItemType.WIKIAPP_WIKI);
        itemTypeSet.add(ClientItemType.WIKIAPP_SPECIAL);
    }

    @RequestMapping(value = "/list")
    public ModelAndView lineList(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                                 @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                                 @RequestParam(value = "platform", required = false) Integer platform) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            map.put("platform", platform);
            map.put("platformSet", platformSet);

            int curPage = (pageStartIndex / pageSize) + 1;
            Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);

            Set<Integer> itemSet = new HashSet<Integer>();
            for (ClientItemType itemType : itemTypeSet) {
                itemSet.add(itemType.getCode());
            }

            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(ClientLineField.LINE_TYPE, ClientLineType.WIKIAPP_INDEX.getCode()));
            queryExpress.add(QueryCriterions.in(ClientLineField.ITEM_TYPE, itemSet.toArray()));
            if (platform != null && AppPlatform.getByCode(platform) != null) {
                queryExpress.add(QueryCriterions.eq(ClientLineField.PLATFORM, platform));
            }
            queryExpress.add(QuerySort.add(ClientLineField.DISPLAY_ORDER, QuerySortOrder.ASC));

            PageRows<ClientLine> pageRows = JoymeAppServiceSngl.get().queryClientLineByPage(queryExpress, pagination);
            map.put("list", pageRows == null ? null : pageRows.getRows());
            map.put("page", pageRows == null ? null : pageRows.getPage());
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
            map = putErrorMessage(map, "system.error");
        }
        return new ModelAndView("/joymeapp/wikiapp/index/linelist", map);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createLine(@RequestParam(value = "platform", required = false) String platformStr) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            int platform = Integer.valueOf(platformStr);
            map.put("platform", platform);

            map.put("platformSet", platformSet);
            map.put("itemTypeSet", itemTypeSet);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
        }
        return new ModelAndView("/joymeapp/wikiapp/index/createline", map);
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "linename", required = false) String lineName,
                               @RequestParam(value = "linecode", required = false) String lineCode,
                               @RequestParam(value = "bigpic", required = false) String bigPic,
                               @RequestParam(value = "platform", required = false) String platformStr,
                               @RequestParam(value = "itemtype", required = false) String itemTypeStr) {

        int platform = Integer.valueOf(platformStr);
        int itemType = Integer.valueOf(itemTypeStr);
        if (!lineCode.endsWith("_" + platform)) {
            lineCode = lineCode + "_" + platform;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("platform", platform);
        map.put("itemType", itemType);
        map.put("lineName", lineName);
        map.put("lineCode", lineCode);
        map.put("bigPic", bigPic);
        try {
            ClientLine clientLine = JoymeAppServiceSngl.get().getClientLineByCode(lineCode);
            if (clientLine != null) {
                map = putErrorMessage(map, "唯一码已存在！");
                map.put("platformSet", platformSet);
                map.put("itemTypeSet", itemTypeSet);
                return new ModelAndView("/joymeapp/wikiapp/index/createline");
            }
            clientLine = new ClientLine();
            clientLine.setCode(lineCode);
            clientLine.setLineName(lineName);
            clientLine.setBigpic(bigPic);
            clientLine.setPlatform(platform);
            clientLine.setItemType(ClientItemType.getByCode(itemType));
            clientLine.setCreateDate(new Date());
            clientLine.setCreateUserid(getCurrentUser().getUserid());
            clientLine.setDisplay_order((int) (Integer.MAX_VALUE - (System.currentTimeMillis() / 1000)));
            clientLine.setLineType(ClientLineType.WIKIAPP_INDEX);
            clientLine.setValidStatus(ValidStatus.VALID);

            JoymeAppServiceSngl.get().createClientLine(clientLine);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
            map = putErrorMessage(map, "system.error");
            map.put("platformSet", platformSet);
            map.put("itemTypeSet", itemTypeSet);
            return new ModelAndView("/joymeapp/wikiapp/index/createline", map);
        }
        return new ModelAndView("redirect:/joymeapp/wikiapp/index/list?platform=" + platformStr);
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyLine(@RequestParam(value = "linecode", required = false) String lineCode) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            ClientLine clientLine = JoymeAppServiceSngl.get().getClientLineByCode(lineCode);
            if (clientLine == null) {
                map = putErrorMessage(map, "模块不存在");
                return new ModelAndView("/joymeapp/wikiapp/index/modifyline", map);
            }
            map.put("clientLine", clientLine);
            map.put("platformSet", platformSet);
            map.put("itemTypeSet", itemTypeSet);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
        }
        return new ModelAndView("/joymeapp/wikiapp/index/modifyline", map);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "lineid", required = false) Long lineId,
                               @RequestParam(value = "linename", required = false) String lineName,
                               @RequestParam(value = "linecode", required = false) String lineCode,
                               @RequestParam(value = "bigpic", required = false) String bigPic,
                               @RequestParam(value = "platform", required = false) String platformStr,
                               @RequestParam(value = "itemtype", required = false) String itemTypeStr) {
        Map<String, Object> map = new HashMap<String, Object>();
        int platform = Integer.valueOf(platformStr);
        int itemType = Integer.valueOf(itemTypeStr);
        try {
            ClientLine clientLine = JoymeAppServiceSngl.get().getClientLineByCode(lineCode);
            if (clientLine != null && clientLine.getLineId() != lineId) {
                map = putErrorMessage(map, "唯一码已存在！");
                map.put("platform", platform);
                map.put("itemType", itemType);
                map.put("lineName", lineName);
                map.put("lineCode", lineCode);
                map.put("bigPic", bigPic);
                map.put("platformSet", platformSet);
                map.put("itemTypeSet", itemTypeSet);
                return new ModelAndView("/joymeapp/wikiapp/index/modifyline", map);
            }
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(ClientLineField.ITEM_TYPE, itemType);
            updateExpress.set(ClientLineField.PLATFORM, platform);
            updateExpress.set(ClientLineField.BIGPIC, bigPic);
            updateExpress.set(ClientLineField.LINE_NAME, lineName);
            updateExpress.set(ClientLineField.CODE, lineCode);
            updateExpress.set(ClientLineField.MODIFY_DATE, new Date());
            updateExpress.set(ClientLineField.MODIFY_USERID, getCurrentUser().getUserid());

            Map<String, Object> param = new HashMap<String, Object>();
            param.put("lineid", lineId);
            param.put("linecode", lineCode);
            boolean bool = JoymeAppServiceSngl.get().modifyClientLineByCache(updateExpress, param);
            if (bool) {
                writeToolsLog(LogOperType.WIKIAPP_INDEX_CLIENTLINE_MODIFY, "WIKI大端首页clientline修改:" + lineCode);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
        }
        return new ModelAndView("redirect:/joymeapp/wikiapp/index/list?platform=" + platformStr);
    }

    @RequestMapping(value = "/detail")
    public ModelAndView detail(@RequestParam(value = "linecode", required = false) String lineCode) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            ClientLine clientLine = JoymeAppServiceSngl.get().getClientLineByCode(lineCode);
            if (clientLine == null) {
                map = putErrorMessage(map, "模块不存在");
                return new ModelAndView("/joymeapp/wikiapp/index/modifyline", map);
            }
            map.put("clientLine", clientLine);
            map.put("platformSet", platformSet);
            map.put("itemTypeSet", itemTypeSet);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
        }
        return new ModelAndView("/joymeapp/wikiapp/index/linedetail", map);
    }

    @RequestMapping(value = "/remove")
    public ModelAndView remove(@RequestParam(value = "lineid", required = false) Long lineId,
                               @RequestParam(value = "linecode", required = false) String lineCode,
                               @RequestParam(value = "linetype", required = false) Integer lineType,
                               @RequestParam(value = "platform", required = false) Integer platform) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            ClientLine clientLine = JoymeAppServiceSngl.get().getClientLineByCode(lineCode);
            if (clientLine == null) {
                return new ModelAndView("redirect:/joymeapp/wikiapp/index/list?platform=" + platformSet);
            }
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(ClientLineField.VALID_STATUS, ValidStatus.REMOVED.getCode());
            updateExpress.set(ClientLineField.MODIFY_DATE, new Date());
            updateExpress.set(ClientLineField.MODIFY_USERID, getCurrentUser().getUserid());

            Map<String, Object> param = new HashMap<String, Object>();
            param.put("lineid", lineId);
            param.put("linecode", lineCode);
            param.put("linetype", lineType);
            param.put("platform", platform);
            param.put("status", ValidStatus.REMOVED.getCode());
            boolean bool = JoymeAppServiceSngl.get().modifyClientLineByCache(updateExpress, param);
            if (bool) {
                writeToolsLog(LogOperType.WIKIAPP_INDEX_CLIENTLINE_MODIFY, "WIKI大端首页clientline修改:" + lineCode);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
        }
        return new ModelAndView("redirect:/joymeapp/wikiapp/index/list?platform=" + platform);
    }

    @RequestMapping(value = "/recover")
    public ModelAndView recover(@RequestParam(value = "lineid", required = false) Long lineId,
                                @RequestParam(value = "linecode", required = false) String lineCode,
                                @RequestParam(value = "linetype", required = false) Integer lineType,
                                @RequestParam(value = "platform", required = false) Integer platform) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            ClientLine clientLine = JoymeAppServiceSngl.get().getClientLineByCode(lineCode);
            if (clientLine == null) {
                return new ModelAndView("redirect:/joymeapp/wikiapp/index/list?platform=" + platformSet);
            }
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(ClientLineField.VALID_STATUS, ValidStatus.REMOVED.getCode());
            updateExpress.set(ClientLineField.MODIFY_DATE, new Date());
            updateExpress.set(ClientLineField.MODIFY_USERID, getCurrentUser().getUserid());

            Map<String, Object> param = new HashMap<String, Object>();
            param.put("lineid", lineId);
            param.put("linecode", lineCode);
            param.put("linetype", lineType);
            param.put("platform", platform);
            param.put("status", ValidStatus.VALID.getCode());
            boolean bool = JoymeAppServiceSngl.get().modifyClientLineByCache(updateExpress, param);
            if (bool) {
                writeToolsLog(LogOperType.WIKIAPP_INDEX_CLIENTLINE_MODIFY, "WIKI大端首页clientline修改:" + lineCode);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
        }
        return new ModelAndView("redirect:/joymeapp/wikiapp/index/list?platform=" + platform);
    }

    @RequestMapping(value = "/sort")
    public ModelAndView sort(@RequestParam(value = "lineid", required = false) Long lineId,
                             @RequestParam(value = "linecode", required = false) String lineCode,
                             @RequestParam(value = "linetype", required = false) Integer lineType,
                             @RequestParam(value = "platform", required = false) Integer platform,
                             @RequestParam(value = "oldorder", required = false) Integer oldOrder,
                             @RequestParam(value = "neworder", required = false) Integer newOrder) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(ClientLineField.DISPLAY_ORDER, newOrder);
            updateExpress.set(ClientLineField.MODIFY_DATE, new Date());
            updateExpress.set(ClientLineField.MODIFY_USERID, getCurrentUser().getUserid());

            Map<String, Object> param = new HashMap<String, Object>();
            param.put("lineid", lineId);
            param.put("linecode", lineCode);
            param.put("linetype", lineType);
            param.put("platform", platform);
            param.put("incorder", Double.valueOf(newOrder - oldOrder));
            boolean bool = JoymeAppServiceSngl.get().modifyClientLineByCache(updateExpress, param);
            if (bool) {
                writeToolsLog(LogOperType.WIKIAPP_INDEX_CLIENTLINE_MODIFY, "WIKI大端首页clientline修改:" + lineCode);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
        }
        return new ModelAndView("redirect:/joymeapp/wikiapp/index/list?platform=" + platform);
    }

}
