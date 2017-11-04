package com.enjoyf.webapps.tools.webpage.controller.joymeapp.newsclient;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.joymeappconfig.AppChannel;
import com.enjoyf.platform.service.joymeappconfig.JoymeAppConfigServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.service.tools.ToolsLog;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.webapps.tools.weblogic.dto.joymeApp.ClientTopMenuDTO;
import com.enjoyf.webapps.tools.weblogic.joymeapp.JoymeAppWebLogic;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-9-17
 * Time: 上午10:02
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/joymeapp/newsclient/topmenu/iphone")
public class NewsIphoneTopMenuController extends ToolsBaseController {

    private static final int CLIENT_PLATFORM_IPHONE = ClientPlatform.IPHONE.getCode();

//    private JoymeAppHotdeployConfig config = HotdeployConfigFactory.get().getConfig(JoymeAppHotdeployConfig.class);
//    private JoymeAppConfig joymeAppConfig = config.getJoymeAppConfig();
    private static final String APP_KEY = "17yfn24TFexGybOF0PqjdY";

    @Resource(name = "jomyeAppWebLogic")
    private JoymeAppWebLogic joymeAppWebLogic;

    @RequestMapping(value = "/list")
    public ModelAndView queryTopMenuList(@RequestParam(value = "channelid", required = false) Long channelId,
                                         @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                                         @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("ClientPlatformCollection", ClientPlatform.getAll());

        try {
            List<AppChannel> channelList = JoymeAppConfigServiceSngl.get().queryAppChannel();
            if (!CollectionUtil.isEmpty(channelList)) {
                mapMessage.put("channelList", channelList);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/joymeapp/newsclient/iphone/topmenulist", mapMessage);
        }

        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(ActivityTopMenuField.APP_KEY, APP_KEY));
        queryExpress.add(QueryCriterions.eq(ActivityTopMenuField.CATEGORY, AppTopMenuCategory.NEWS_CLIENT_LINE.getCode()));
        queryExpress.add(QueryCriterions.eq(ActivityTopMenuField.PLATFORM, CLIENT_PLATFORM_IPHONE));
        if (channelId != null) {
            queryExpress.add(QueryCriterions.eq(ActivityTopMenuField.CHANNEL_ID, channelId));
            mapMessage.put("channelId", channelId);
        }
        queryExpress.add(QuerySort.add(ActivityTopMenuField.DISPLAY_ORDER, QuerySortOrder.ASC));

        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
        try {
            PageRows<ClientTopMenuDTO> pageRows = joymeAppWebLogic.queryClientTopMenuDTO(queryExpress, pagination);
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                mapMessage.put("list", pageRows.getRows());
                mapMessage.put("page", pageRows.getPage());
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/joymeapp/newsclient/iphone/topmenulist", mapMessage);
        }
        return new ModelAndView("/joymeapp/newsclient/iphone/topmenulist", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createTopMenuPage() {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("menuTypes", AppRedirectType.getAll());

        try {
            List<AppChannel> channelList = JoymeAppConfigServiceSngl.get().queryAppChannel();
            if (!CollectionUtil.isEmpty(channelList)) {
                mapMessage.put("channelList", channelList);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("redirect:/joymeapp/newsclient/topmenu/iphone/list", mapMessage);
        }
        return new ModelAndView("/joymeapp/newsclient/iphone/createtopmenu", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView createTopMenu(@RequestParam(value = "menuname", required = false) String menuName,
                                      @RequestParam(value = "linkurl", required = false) String linkUrl,
                                      @RequestParam(value = "menutype", required = false) Integer redirectType,
                                      @RequestParam(value = "picurl", required = false) String picUrl,
                                      @RequestParam(value = "menudesc", required = false) String menuDesc,
                                      @RequestParam(value = "channelid", required = false) Long channelId,
                                      @RequestParam(value = "isnew", required = false) boolean isNew,
                                      @RequestParam(value = "ishot", required = false) boolean isHot) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            ActivityTopMenu activityTopMenu = new ActivityTopMenu();
            activityTopMenu.setMenuName(menuName);
            activityTopMenu.setLinkUrl(linkUrl);
            activityTopMenu.setRedirectType(redirectType);
            activityTopMenu.setPicUrl(picUrl);
            activityTopMenu.setMenuDesc(menuDesc);
            activityTopMenu.setAppKey(APP_KEY);
            activityTopMenu.setPlatform(CLIENT_PLATFORM_IPHONE);
            activityTopMenu.setChannelId(channelId);
            activityTopMenu.setIsNew(isNew);
            activityTopMenu.setIsHot(isHot);
            activityTopMenu.setValidStatus(ValidStatus.VALID);
            activityTopMenu.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
            activityTopMenu.setCreateUserId(getCurrentUser().getUserid());
            activityTopMenu.setCreateDate(new Date());
            activityTopMenu.setCreateIp(getIp());
            activityTopMenu.setCategory(AppTopMenuCategory.NEWS_CLIENT_LINE);

            JoymeAppConfigServiceSngl.get().createActivityTopMenu(activityTopMenu);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "ServiceException", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("redirect:/joymeapp/newsclient/topmenu/iphone/createpage", mapMessage);
        }
        return new ModelAndView("redirect:/joymeapp/newsclient/topmenu/iphone/list");
    }

    @RequestMapping(value = "/delete")
    public ModelAndView topMenuDelete(@RequestParam(value = "activitytopmenuid", required = true) Long activityTopMenuId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        if (activityTopMenuId == null) {
            return new ModelAndView("redirect:/joymeapp/newsclient/topmenu/iphone/list");
        }
        Date date = new Date();

        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(ActivityTopMenuField.ACTIVITY_MENU_ID, activityTopMenuId));

        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(ActivityTopMenuField.VALID_STATUS, ValidStatus.REMOVED.getCode());
        updateExpress.set(ActivityTopMenuField.LAST_MODIFY_USERID, getCurrentUser().getUserid());
        updateExpress.set(ActivityTopMenuField.LAST_MODIFY_IP, getIp());
        updateExpress.set(ActivityTopMenuField.LAST_MODIFY_DATE, date);
        try {
            boolean bool = JoymeAppConfigServiceSngl.get().modifyActivityTopMenu(queryExpress, updateExpress);

            if (bool) {
                ToolsLog log = new ToolsLog();
                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.DELETE_ACTIVITY_TOP_MENU);
                log.setOpTime(date);
                log.setOpIp(getIp());
                log.setOpAfter("delete activitytopmenu:" + activityTopMenuId);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "ServiceException", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("redirect:/joymeapp/newsclient/topmenu/iphone/list");
        }
        return new ModelAndView("redirect:/joymeapp/newsclient/topmenu/iphone/list");
    }

    @RequestMapping(value = "/recover")
    public ModelAndView recoverAppMenu(@RequestParam(value = "activitytopmenuid", required = true) Long activityTopMenuId) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();
        if (activityTopMenuId == null) {
            return new ModelAndView("redirect:/joymeapp/newsclient/topmenu/iphone/list");
        }
        Date date = new Date();

        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(ActivityTopMenuField.ACTIVITY_MENU_ID, activityTopMenuId));

        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(ActivityTopMenuField.VALID_STATUS, ValidStatus.VALID.getCode());
        updateExpress.set(ActivityTopMenuField.LAST_MODIFY_USERID, getCurrentUser().getUserid());
        updateExpress.set(ActivityTopMenuField.LAST_MODIFY_IP, getIp());
        updateExpress.set(ActivityTopMenuField.LAST_MODIFY_DATE, date);
        try {
            boolean bool = JoymeAppConfigServiceSngl.get().modifyActivityTopMenu(queryExpress, updateExpress);

            if (bool) {
                ToolsLog log = new ToolsLog();
                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.RECOVER_ACTIVITY_TOP_MENU);
                log.setOpTime(date);
                log.setOpIp(getIp());
                log.setOpAfter("recover activitytopmenu:" + activityTopMenuId);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "ServiceException", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("redirect:/joymeapp/newsclient/topmenu/iphone/list");
        }
        return new ModelAndView("redirect:/joymeapp/newsclient/topmenu/iphone/list");
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyAppPage(@RequestParam(value = "activitytopmenuid", required = true) Long activityTopMenuId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("menuTypes", AppRedirectType.getAll());
        mapMessage.put("ClientPlatformCollection", ClientPlatform.getAll());

        try {
            ActivityTopMenu activityTopMenu = JoymeAppConfigServiceSngl.get().getActivityTopMenuById(activityTopMenuId);
            if (activityTopMenu == null) {
                return new ModelAndView("redirect:/joymeapp/newsclient/topmenu/iphone/list");
            }
            mapMessage.put("activityTopMenu", activityTopMenu);

            List<AppChannel> channelList = JoymeAppConfigServiceSngl.get().queryAppChannel();
            if (!CollectionUtil.isEmpty(channelList)) {
                mapMessage.put("channelList", channelList);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "ServiceException", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("redirect:/joymeapp/newsclient/topmenu/iphone/list");
        }
        return new ModelAndView("joymeapp/newsclient/iphone/modifytopmenu", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "activitytopmenuid", required = true) Long activityTopMenuId,
                               @RequestParam(value = "menuname", required = false) String menuName,
                               @RequestParam(value = "linkurl", required = false) String linkUrl,
                               @RequestParam(value = "menutype", required = false) Integer redirectType,
                               @RequestParam(value = "picurl", required = false) String picUrl,
                               @RequestParam(value = "menudesc", required = false) String menuDesc,
                               @RequestParam(value = "channelid", required = false) Long channelId,
                               @RequestParam(value = "isnew", required = false) boolean isNew,
                               @RequestParam(value = "ishot", required = false) boolean isHot) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        Date date = new Date();
        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(ActivityTopMenuField.MENU_NAME, menuName);
        updateExpress.set(ActivityTopMenuField.LINK_URL, linkUrl);
        updateExpress.set(ActivityTopMenuField.REDIRECT_TYPE, redirectType);
        updateExpress.set(ActivityTopMenuField.PIC_URL, picUrl);
        updateExpress.set(ActivityTopMenuField.MENU_DESC, menuDesc);
        if (channelId != null) {
            updateExpress.set(ActivityTopMenuField.CHANNEL_ID, channelId);
        } else {
            updateExpress.set(ActivityTopMenuField.CHANNEL_ID, 0L);
        }
        updateExpress.set(ActivityTopMenuField.IS_NEW, isNew);
        updateExpress.set(ActivityTopMenuField.IS_HOT, isHot);
        updateExpress.set(ActivityTopMenuField.LAST_MODIFY_USERID, getCurrentUser().getUserid());
        updateExpress.set(ActivityTopMenuField.LAST_MODIFY_DATE, date);
        updateExpress.set(ActivityTopMenuField.LAST_MODIFY_IP, getIp());

        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(ActivityTopMenuField.ACTIVITY_MENU_ID, activityTopMenuId));
        try {
            boolean bool = JoymeAppConfigServiceSngl.get().modifyActivityTopMenu(queryExpress, updateExpress);
            if (bool) {
                ToolsLog log = new ToolsLog();

                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.MODIFY_ACTIVITY_TOP_MENU);
                log.setOpTime(date);
                log.setOpIp(getIp());
                log.setOpAfter("modify activitytopmenu:" + activityTopMenuId);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "ServiceException", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("redirect:/joymeapp/newsclient/topmenu/iphone/modifypage?activitytopmenuid=" + activityTopMenuId);
        }
        return new ModelAndView("redirect:/joymeapp/newsclient/topmenu/iphone/list");
    }

    @RequestMapping(value = "/sort/{sort}")
    public ModelAndView getAppMenuSort(@PathVariable(value = "sort") String sort,
                                       @RequestParam(value = "activitytopmenuid", required = true) Long activityTopMenuId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            ActivityTopMenu activityTopMenu = JoymeAppConfigServiceSngl.get().getActivityTopMenuById(activityTopMenuId);
            if (activityTopMenu == null) {
                return new ModelAndView("redirect:/joymeapp/newsclient/topmenu/iphone/list");
            }
            JoymeAppWebLogic.sortClientTopMenu(sort, activityTopMenuId, AppTopMenuCategory.NEWS_CLIENT_LINE.getCode(), CLIENT_PLATFORM_IPHONE);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + "ServiceException", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("redirect:/joymeapp/newsclient/topmenu/iphone/list");
        }
        return new ModelAndView("redirect:/joymeapp/newsclient/topmenu/iphone/list");
    }
}
