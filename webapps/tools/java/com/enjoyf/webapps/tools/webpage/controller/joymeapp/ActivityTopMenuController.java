package com.enjoyf.webapps.tools.webpage.controller.joymeapp;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.joymeappconfig.AppChannel;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.joymeappconfig.JoymeAppConfigServiceSngl;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.AuthAppField;
import com.enjoyf.platform.service.oauth.AuthAppType;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.service.tools.ToolsLog;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.platform.webapps.common.util.PicRgbUtil;
import com.enjoyf.webapps.tools.weblogic.dto.joymeApp.ActivityTopMenuDTO;
import com.enjoyf.webapps.tools.weblogic.joymeapp.JoymeAppWebLogic;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-9-17
 * Time: 上午10:02
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/joymeapp/activitytopmenu")
public class ActivityTopMenuController extends ToolsBaseController {
    private static Set<Integer> menuTypes = new HashSet<Integer>();

    static {
        menuTypes.add(0);  //native
        menuTypes.add(1);  //webview
    }

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
            //得到存在的app列表
            List<AuthApp> appList = OAuthServiceSngl.get().queryAuthApp(new QueryExpress()
                    .add(QueryCriterions.eq(AuthAppField.APPTYPE, AuthAppType.INTERNAL_CLIENT.getCode()))
                    .add(QueryCriterions.in(AuthAppField.PLATOFRM, new Integer[]{AppPlatform.ANDROID.getCode(), AppPlatform.CLIENT.getCode(), AppPlatform.IOS.getCode()}))
                    .add(QueryCriterions.eq(AuthAppField.VALIDSTATUS, ValidStatus.VALID.getCode()))
            );
            if (!CollectionUtil.isEmpty(appList)) {
                mapMessage.put("appList", appList);
            }

            List<AppChannel> channelList = JoymeAppConfigServiceSngl.get().queryAppChannel();
            if (!CollectionUtil.isEmpty(channelList)) {
                mapMessage.put("channelList", channelList);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/joymeapp/activitytopmenulist", mapMessage);
        }

        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QuerySort.add(ActivityTopMenuField.DISPLAY_ORDER, QuerySortOrder.ASC));
        if (!StringUtil.isEmpty(appKey)) {
            queryExpress.add(QueryCriterions.eq(ActivityTopMenuField.APP_KEY, appKey));
            mapMessage.put("appKey", appKey);
        }
        if (platform != null) {
            queryExpress.add(QueryCriterions.eq(ActivityTopMenuField.PLATFORM, platform));
            mapMessage.put("platform", platform);
        }
        if (channelId != null) {
            queryExpress.add(QueryCriterions.eq(ActivityTopMenuField.CHANNEL_ID, channelId));
            mapMessage.put("channelId", channelId);
        }
        queryExpress.add(QueryCriterions.eq(ActivityTopMenuField.CATEGORY, AppTopMenuCategory.DEFAULT.getCode()));

        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
        try {
            PageRows<ActivityTopMenuDTO> pageRows = joymeAppWebLogic.queryActivityTopMenuDTO(queryExpress, pagination);
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                mapMessage.put("list", pageRows.getRows());
                mapMessage.put("page", pageRows.getPage());
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/joymeapp/activitytopmenulist", mapMessage);
        }
        return new ModelAndView("/joymeapp/activitytopmenulist", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createTopMenuPage() {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("menuTypes", menuTypes);
        mapMessage.put("redirectTypes", AppRedirectType.getAll());

        try {
            //得到存在的app列表
            List<AuthApp> appList = OAuthServiceSngl.get().queryAuthApp(new QueryExpress()
                    .add(QueryCriterions.eq(AuthAppField.APPTYPE, AuthAppType.INTERNAL_CLIENT.getCode()))
                    .add(QueryCriterions.in(AuthAppField.PLATOFRM, new Integer[]{AppPlatform.ANDROID.getCode(), AppPlatform.CLIENT.getCode(), AppPlatform.IOS.getCode()}))
                    .add(QueryCriterions.eq(AuthAppField.VALIDSTATUS, ValidStatus.VALID.getCode()))
            );
            if (!CollectionUtil.isEmpty(appList)) {
                mapMessage.put("appList", appList);
            }

            List<AppChannel> channelList = JoymeAppConfigServiceSngl.get().queryAppChannel();
            if (!CollectionUtil.isEmpty(channelList)) {
                mapMessage.put("channelList", channelList);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("redirect:/joymeapp/activitytopmenu/list", mapMessage);
        }
        return new ModelAndView("/joymeapp/createactivitytopmenu", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView createTopMenu(@RequestParam(value = "menuname", required = false) String menuName,
                                      @RequestParam(value = "linkurl", required = false) String linkUrl,
                                      @RequestParam(value = "menutype", required = false) Integer menuType,
                                      @RequestParam(value = "picurl", required = false) String picUrl,
                                      @RequestParam(value = "menudesc", required = false) String menuDesc,
                                      @RequestParam(value = "appkey", required = false) String appKey,
                                      @RequestParam(value = "platform", required = false) Integer platform,
                                      @RequestParam(value = "channelid", required = false) Long channelId,
                                      @RequestParam(value = "isnew", required = false) boolean isNew,
                                      @RequestParam(value = "ishot", required = false) boolean isHot,
                                      @RequestParam(value = "redirect", required = false) Integer redirect,
                                      @RequestParam(value = "author", required = false) String author,
                                      @RequestParam(value = "publishdate", required = false) Date publishDate,
                                      @RequestParam(value = "gameid", required = false) String gameId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            if (linkUrl.indexOf("http://marticle.joyme.com/marticle/") >= 0) {
                linkUrl = linkUrl.replaceAll("http://marticle.joyme.com/marticle/", "http://marticle.joyme.com/json/");
            }

            ActivityTopMenu activityTopMenu = new ActivityTopMenu();
            activityTopMenu.setMenuName(menuName);
            activityTopMenu.setLinkUrl(linkUrl);
            activityTopMenu.setMenuType(menuType);
            activityTopMenu.setPicUrl(picUrl);
            activityTopMenu.setMenuDesc(menuDesc);
            activityTopMenu.setAppKey(appKey);
            activityTopMenu.setPlatform(platform);
            activityTopMenu.setChannelId(channelId);
            activityTopMenu.setIsNew(isNew);
            activityTopMenu.setIsHot(isHot);
            activityTopMenu.setValidStatus(ValidStatus.VALID);
            activityTopMenu.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
            activityTopMenu.setCreateUserId(getCurrentUser().getUserid());
            activityTopMenu.setCreateDate(new Date());
            activityTopMenu.setCreateIp(getIp());
            activityTopMenu.setCategory(AppTopMenuCategory.DEFAULT);
            if (redirect != null) {
                activityTopMenu.setRedirectType(redirect);
            }


            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String pubDate = publishDate == null ? "" : df.format(publishDate);
            ActivityTopMenuParam param = new ActivityTopMenuParam();
            param.setAuthor(StringUtil.isEmpty(author) ? "" : author);
            param.setPublishDate(pubDate);
            String picColor = PicRgbUtil.getImagePixel(picUrl);
            param.setPicColor(picColor);
            //优酷
            param.setGameId(gameId);
            activityTopMenu.setParam(param);


            JoymeAppConfigServiceSngl.get().createActivityTopMenu(activityTopMenu);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "ServiceException", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("redirect:/joymeapp/activitytopmenu/createpage", mapMessage);
        }
        return new ModelAndView("redirect:/joymeapp/activitytopmenu/list");
    }

    @RequestMapping(value = "/delete")
    public ModelAndView topMenuDelete(@RequestParam(value = "activitytopmenuid", required = true) Long activityTopMenuId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        if (activityTopMenuId == null) {
            return new ModelAndView("redirect:/joymeapp/activitytopmenu/list");
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
            return new ModelAndView("redirect:/joymeapp/activitytopmenu/list");
        }
        return new ModelAndView("redirect:/joymeapp/activitytopmenu/list");
    }

    @RequestMapping(value = "/recover")
    public ModelAndView recoverAppMenu(@RequestParam(value = "activitytopmenuid", required = true) Long activityTopMenuId) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();
        if (activityTopMenuId == null) {
            return new ModelAndView("redirect:/joymeapp/activitytopmenu/list");
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
            return new ModelAndView("redirect:/joymeapp/activitytopmenu/list");
        }
        return new ModelAndView("redirect:/joymeapp/activitytopmenu/list");
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyAppPage(@RequestParam(value = "activitytopmenuid", required = true) Long activityTopMenuId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("menuTypes", menuTypes);
        mapMessage.put("redirectTypes", AppRedirectType.getAll());
        try {
            ActivityTopMenu activityTopMenu = JoymeAppConfigServiceSngl.get().getActivityTopMenuById(activityTopMenuId);
            if (activityTopMenu == null) {
                return new ModelAndView("redirect:/joymeapp/activitytopmenu/list");
            }
            mapMessage.put("activityTopMenu", activityTopMenu);
            //得到存在的app列表
            List<AuthApp> appList = OAuthServiceSngl.get().queryAuthApp(new QueryExpress()
                    .add(QueryCriterions.eq(AuthAppField.APPTYPE, AuthAppType.INTERNAL_CLIENT.getCode()))
                    .add(QueryCriterions.in(AuthAppField.PLATOFRM, new Integer[]{AppPlatform.ANDROID.getCode(), AppPlatform.CLIENT.getCode(), AppPlatform.IOS.getCode()}))
                    .add(QueryCriterions.eq(AuthAppField.VALIDSTATUS, ValidStatus.VALID.getCode()))
            );
            if (!CollectionUtil.isEmpty(appList)) {
                mapMessage.put("appList", appList);
            }

            List<AppChannel> channelList = JoymeAppConfigServiceSngl.get().queryAppChannel();
            if (!CollectionUtil.isEmpty(channelList)) {
                mapMessage.put("channelList", channelList);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "ServiceException", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("redirect:/joymeapp/activitytopmenu/list");
        }
        return new ModelAndView("joymeapp/modifyactivitytopmenu", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "activitytopmenuid", required = true) Long activityTopMenuId,
                               @RequestParam(value = "menuname", required = false) String menuName,
                               @RequestParam(value = "linkurl", required = false) String linkUrl,
                               @RequestParam(value = "menutype", required = false) Integer menuType,
                               @RequestParam(value = "picurl", required = false) String picUrl,
                               @RequestParam(value = "menudesc", required = false) String menuDesc,
                               @RequestParam(value = "appkey", required = false) String appKey,
                               @RequestParam(value = "platform", required = false) Integer platform,
                               @RequestParam(value = "channelid", required = false) Long channelId,
                               @RequestParam(value = "isnew", required = false) boolean isNew,
                               @RequestParam(value = "ishot", required = false) boolean isHot,
                               @RequestParam(value = "redirect", required = false) Integer redirect,
                               @RequestParam(value = "author", required = false) String author,
                               @RequestParam(value = "publishdate", required = false) Date publishDate,
                               @RequestParam(value = "gameid", required = false) String gameId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        if (linkUrl.indexOf("http://marticle.joyme.com/marticle/") >= 0) {
            linkUrl = linkUrl.replaceAll("http://marticle.joyme.com/marticle/", "http://marticle.joyme.com/json/");
        }

        Date date = new Date();
        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(ActivityTopMenuField.MENU_NAME, menuName);
        updateExpress.set(ActivityTopMenuField.LINK_URL, linkUrl);
        updateExpress.set(ActivityTopMenuField.MENU_TYPE, menuType);
        updateExpress.set(ActivityTopMenuField.PIC_URL, picUrl);
        updateExpress.set(ActivityTopMenuField.MENU_DESC, menuDesc);
        if (!StringUtil.isEmpty(appKey)) {
            updateExpress.set(ActivityTopMenuField.APP_KEY, appKey);
        } else {
            updateExpress.set(ActivityTopMenuField.APP_KEY, "");
        }
        updateExpress.set(ActivityTopMenuField.PLATFORM, platform);
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
        updateExpress.set(ActivityTopMenuField.REDIRECT_TYPE, redirect);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String pubDate = publishDate == null ? "" : df.format(publishDate);
        ActivityTopMenuParam param = new ActivityTopMenuParam();
        param.setAuthor(StringUtil.isEmpty(author) ? "" : author);
        param.setPublishDate(pubDate);
        String picColor = PicRgbUtil.getImagePixel(picUrl);
        param.setPicColor(picColor);
        //优酷
        param.setGameId(gameId);
        updateExpress.set(ActivityTopMenuField.PARAM_TEXT, param.toJson());

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
            return new ModelAndView("redirect:/joymeapp/activitytopmenu/modifypage?activitytopmenuid=" + activityTopMenuId);
        }
        return new ModelAndView("redirect:/joymeapp/activitytopmenu/list");
    }

    @RequestMapping(value = "/sort/{sort}")
    @ResponseBody
    public String getAppMenuSort(@PathVariable(value = "sort") String sort,
                                 @RequestParam(value = "activitytopmenuid", required = true) Long activityTopMenuId,
                                 @RequestParam(value = "appkey", required = false) String appkey) {
        JsonBinder binder = JsonBinder.buildNormalBinder();
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        ResultObjectMsg resultObjectMsg = new ResultObjectMsg(ResultObjectMsg.CODE_S);
        Long returnItemId = null;
        try {
            ActivityTopMenu activityTopMenu = JoymeAppConfigServiceSngl.get().getActivityTopMenuById(activityTopMenuId);
            if (activityTopMenu == null) {
                resultObjectMsg.setRs(ResultObjectMsg.CODE_E);
                return binder.toJson(resultObjectMsg);
            }


            returnItemId = JoymeAppWebLogic.sortActivityTopMenu(sort, appkey, activityTopMenuId, AppTopMenuCategory.DEFAULT.getCode());
            if (returnItemId == null) {
                resultObjectMsg.setRs(ResultObjectMsg.CODE_E);
                return binder.toJson(resultObjectMsg);
            }
        } catch (Exception e) {
            resultObjectMsg.setRs(ResultObjectMsg.CODE_E);
            resultObjectMsg.setMsg("system.error");
            return binder.toJson(resultObjectMsg);
        }
        mapMessage.put("sort", sort);
        mapMessage.put("itemid", activityTopMenuId);
        mapMessage.put("returnitemid", returnItemId);
        resultObjectMsg.setResult(mapMessage);
        return binder.toJson(resultObjectMsg);
    }
}
