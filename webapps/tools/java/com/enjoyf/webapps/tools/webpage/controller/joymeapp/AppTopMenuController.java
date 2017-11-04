package com.enjoyf.webapps.tools.webpage.controller.joymeapp;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.JsonBinder;
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
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 13-7-5
 * Time: 上午10:02
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/joymeapp/topmenu")
public class AppTopMenuController extends ToolsBaseController {
    private static Set<Integer> menuTypes = new HashSet<Integer>();

    static {
        menuTypes.add(0);
        menuTypes.add(1);
    }

    @RequestMapping(value = "/list")
    public ModelAndView queryTopMenuList(@RequestParam(value = "appkey", required = false) String appKey,
                                         @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                                         @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            //得到存在的app列表
            List<AuthApp> appList = OAuthServiceSngl.get().queryAuthApp(
                    new QueryExpress()
                            .add(QueryCriterions.eq(AuthAppField.APPTYPE, AuthAppType.INTERNAL_CLIENT.getCode()))
                            .add(QueryCriterions.in(AuthAppField.PLATOFRM, new Integer[]{AppPlatform.ANDROID.getCode(), AppPlatform.CLIENT.getCode(), AppPlatform.IOS.getCode()}))
                            .add(QueryCriterions.eq(AuthAppField.VALIDSTATUS, ValidStatus.VALID.getCode()))
            );
            mapMessage.put("applist", appList);

            //不传aqpkey 不用查询 菜单直接返回
            if (StringUtil.isEmpty(appKey)) {
                return new ModelAndView("/joymeapp/joymeapptopmenulist", mapMessage);
            }

            mapMessage.put("appkey", appKey);

            int curPage = (pageStartIndex / pageSize) + 1;
            Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);

            PageRows<JoymeAppTopMenu> joymeAppTopMenuPageRows = JoymeAppConfigServiceSngl.get().queryJoymeAppTopMenuByPage(new QueryExpress()
                    .add(QueryCriterions.eq(JoymeAppTopMenuField.APPKEY, appKey))
                    .add(QuerySort.add(JoymeAppTopMenuField.DISPLAY_ORDER, QuerySortOrder.ASC)), pagination);


            mapMessage.put("list", joymeAppTopMenuPageRows.getRows());
            mapMessage.put("page", joymeAppTopMenuPageRows.getPage());
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("/joymeapp/joymeapptopmenulist", mapMessage);
    }

    @RequestMapping(value = "/createtopmenupage")
    public ModelAndView createTopMenuPage(@RequestParam(value = "appkey", required = true) String appkey) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("appkey", appkey);
        mapMessage.put("menuTypes", menuTypes);

        try {
            List<AppChannel> channelList = JoymeAppConfigServiceSngl.get().queryAppChannel();
            if (CollectionUtil.isEmpty(channelList)) {
                return new ModelAndView("redirect:/joymeapp/topmenu/list");
            }
            mapMessage.put("channelList", channelList);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/joymeapp/joymeapptopmenulist", mapMessage);
        }
        return new ModelAndView("/joymeapp/createtopmenu", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView createTopMenu(@RequestParam(value = "appkey", required = true) String appkey,
                                      @RequestParam(value = "menuname", required = true) String menuName,
                                      @RequestParam(value = "url", required = false) String url,
                                      @RequestParam(value = "picurl1", required = true) String picUrl1,
                                      @RequestParam(value = "picurl2", required = false) String picUrl2,
                                      @RequestParam(value = "menutype", required = false) Integer menuType,
                                      @RequestParam(value = "isnew", required = false) boolean isNew,
                                      @RequestParam(value = "ishot", required = false) boolean isHot,
                                      @RequestParam(value = "gameid", required = false) String gameId,
                                      @RequestParam(value = "channeltopmenu", required = false) String channelTopMenuStr) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            JoymeAppTopMenu joymeAppTopMenu = new JoymeAppTopMenu();
            joymeAppTopMenu.setAppkey(appkey);
            joymeAppTopMenu.setMenuName(menuName);
            joymeAppTopMenu.setUrl(url);
            joymeAppTopMenu.setPicUrl1(picUrl1);
            joymeAppTopMenu.setPicUrl2(StringUtil.isEmpty(picUrl2) ? "" : picUrl2);
            joymeAppTopMenu.setMenuType(menuType);
            joymeAppTopMenu.setNew(isNew);
            joymeAppTopMenu.setHot(isHot);
            joymeAppTopMenu.setCreateUserId(getCurrentUser().getUserid());
            joymeAppTopMenu.setCreateIp(getIp());
            joymeAppTopMenu.setCreateDate(new Date());
            joymeAppTopMenu.setRemoveStatus(ActStatus.UNACT);
            joymeAppTopMenu.setDisplay_order(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
            joymeAppTopMenu.setGameId(gameId);

            //渠道图片
            if (!StringUtil.isEmpty(channelTopMenuStr)) {
                ChannelTopMenuSet set = new ChannelTopMenuSet();
                String[] arr = channelTopMenuStr.split("@");
                for (String str : arr) {
                    ChannelTopMenu channelTopMenu = JsonBinder.buildNormalBinder().getMapper().readValue(str, new TypeReference<ChannelTopMenu>() {
                    });
                    set.add(channelTopMenu);
                }
                joymeAppTopMenu.setChannelTopMenuSet(set);
            }

            JoymeAppConfigServiceSngl.get().createJoymeAppTopMenu(joymeAppTopMenu);


            ToolsLog log = new ToolsLog();

            log.setOpUserId(getCurrentUser().getUserid());
            log.setOperType(LogOperType.CREATE_JOYMEAPP_TOPMENU);
            log.setOpTime(new Date());
            log.setOpIp(getIp());
            log.setOpAfter("create joymeapptopmenu message:" + joymeAppTopMenu);

            addLog(log);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "ServiceException", e);
        } catch (JsonMappingException e) {
            GAlerter.lab(this.getClass().getName() + "JsonMappingException", e);
        } catch (JsonParseException e) {
            GAlerter.lab(this.getClass().getName() + "JsonParseException", e);
        } catch (IOException e) {
            GAlerter.lab(this.getClass().getName() + "IOException", e);
        }

        return new ModelAndView("redirect:/joymeapp/topmenu/list?appkey=" + appkey);
    }

    @RequestMapping(value = "/delete")
    public ModelAndView topMenuDelete(@RequestParam(value = "appkey", required = true) String appkey,
                                      @RequestParam(value = "menuid", required = true) Long menuId) {


        try {

            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(JoymeAppTopMenuField.REMOVESTATUS, ActStatus.ACTED.getCode());
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(JoymeAppTopMenuField.TOP_MENU_ID, menuId));
            updateExpress.set(JoymeAppTopMenuField.LASTMODIFY_USERID, getCurrentUser().getUserid());
            updateExpress.set(JoymeAppTopMenuField.LASTMODIFY_IP, getIp());
            updateExpress.set(JoymeAppTopMenuField.LASTMODIFY_DATE, new Date());
            boolean isTrue = JoymeAppConfigServiceSngl.get().modifyJoymeAppTopMenu(queryExpress, updateExpress, appkey);

            if (isTrue) {
                ToolsLog log = new ToolsLog();

                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.DELETE_JOYMEAPP_TOPMENU);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("delete joymeapptopmenu:" + menuId);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "ServiceException", e);
        }


        return new ModelAndView("redirect:/joymeapp/topmenu/list?appkey=" + appkey);
    }

    @RequestMapping(value = "/recover")
    public ModelAndView recoverAppMenu(@RequestParam(value = "menuid", required = true) Long menuId,
                                       @RequestParam(value = "appkey", required = true) String appkey) {

        try {
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(JoymeAppTopMenuField.REMOVESTATUS, ActStatus.UNACT.getCode());
            updateExpress.set(JoymeAppTopMenuField.LASTMODIFY_USERID, getCurrentUser().getUserid());
            updateExpress.set(JoymeAppTopMenuField.LASTMODIFY_IP, getIp());
            updateExpress.set(JoymeAppTopMenuField.LASTMODIFY_DATE, new Date());
            QueryExpress queryExpress = new QueryExpress().add(QueryCriterions.eq(JoymeAppTopMenuField.TOP_MENU_ID, menuId));
            boolean isTrue = JoymeAppConfigServiceSngl.get().modifyJoymeAppTopMenu(queryExpress, updateExpress, appkey);
            if (isTrue) {
                ToolsLog log = new ToolsLog();

                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.RECOVER_JOYMEAPP_TOPMENU);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("recover joymeapptopmenu:" + menuId);
            }


        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "ServiceException", e);
        }

        return new ModelAndView("redirect:/joymeapp/topmenu/list?appkey=" + appkey);

    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyAppPage(@RequestParam(value = "menuid", required = true) Long menuId,
                                      @RequestParam(value = "appkey", required = true) String appkey) {
        Map<String, Object> mapMessage = null;

        try {
            mapMessage = new HashMap<String, Object>();
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(JoymeAppTopMenuField.TOP_MENU_ID, menuId));
            JoymeAppTopMenu joymeAppTopMenu = JoymeAppConfigServiceSngl.get().getJoymeAppTopMenu(queryExpress);

            mapMessage.put("joymeAppTopMenu", joymeAppTopMenu);
            mapMessage.put("appkey", appkey);

            mapMessage.put("menuTypes", menuTypes);
        } catch (ServiceException e) {

            GAlerter.lab(this.getClass().getName() + "ServoceException", e);
        }

        try {
            List<AppChannel> channelList = JoymeAppConfigServiceSngl.get().queryAppChannel();
            if (CollectionUtil.isEmpty(channelList)) {
                return new ModelAndView("redirect:/joymeapp/topmenu/list");
            }
            mapMessage.put("channelList", channelList);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/joymeapp/joymeapptopmenulist", mapMessage);
        }
        return new ModelAndView("joymeapp/modifytopmenu", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "menuid", required = true) Long menuId,
                               @RequestParam(value = "menuname", required = false) String menuName,
                               @RequestParam(value = "url", required = false) String url,
                               @RequestParam(value = "picurl1", required = false) String picUrl1,
                               @RequestParam(value = "picurl2", required = false) String picUrl2,
                               @RequestParam(value = "menutype", required = false) Integer menuType,
                               @RequestParam(value = "isnew", required = false) boolean isNew,
                               @RequestParam(value = "ishot", required = false) boolean isHot,
                               @RequestParam(value = "appkey", required = true) String appkey,
                               @RequestParam(value = "channeltopmenu", required = false) String channelTopMenuStr,
                               @RequestParam(value = "gameid", required = false) String gameId) {
        try {
            Map<String, Object> mapMessage = new HashMap<String, Object>();
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(JoymeAppTopMenuField.MENU_NAME, menuName);
            updateExpress.set(JoymeAppTopMenuField.LINK_URL, url);
            updateExpress.set(JoymeAppTopMenuField.PIC_URL, picUrl1);
            updateExpress.set(JoymeAppTopMenuField.PIC_URL2, StringUtil.isEmpty(picUrl2)?"":picUrl2);
            updateExpress.set(JoymeAppTopMenuField.MENU_TYPE, menuType);
            updateExpress.set(JoymeAppTopMenuField.MENU_ISNEW, isNew);
            updateExpress.set(JoymeAppTopMenuField.MENU_ISHOT, isHot);

            updateExpress.set(JoymeAppTopMenuField.LASTMODIFY_USERID, getCurrentUser().getUserid());
            updateExpress.set(JoymeAppTopMenuField.LASTMODIFY_IP, getIp());
            updateExpress.set(JoymeAppTopMenuField.LASTMODIFY_DATE, new Date());
            updateExpress.set(JoymeAppTopMenuField.GAMEID, gameId);

            //渠道图片
            if (!StringUtil.isEmpty(channelTopMenuStr)) {
                ChannelTopMenuSet set = new ChannelTopMenuSet();
                String[] arr = channelTopMenuStr.split("@");
                for (String str : arr) {
                    ChannelTopMenu channelTopMenu = JsonBinder.buildNormalBinder().getMapper().readValue(str, new TypeReference<ChannelTopMenu>() {
                    });
                    set.add(channelTopMenu);
                }
                updateExpress.set(JoymeAppTopMenuField.CHANNEL_TOPMENU, set.toJson());
            } else {
                updateExpress.set(JoymeAppTopMenuField.CHANNEL_TOPMENU, null);
            }

            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(JoymeAppTopMenuField.TOP_MENU_ID, menuId));
            boolean isTrue = JoymeAppConfigServiceSngl.get().modifyJoymeAppTopMenu(queryExpress, updateExpress, appkey);
            if (isTrue) {
                ToolsLog log = new ToolsLog();

                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.MODIFY_JOYMEAPP_TOPMENU);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("modify joymeapptopmenu:" + menuId);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "ServiceException", e);
        } catch (JsonMappingException e) {
            GAlerter.lab(this.getClass().getName() + "JsonMappingException", e);
        } catch (JsonParseException e) {
            GAlerter.lab(this.getClass().getName() + "JsonParseException", e);
        } catch (IOException e) {
            GAlerter.lab(this.getClass().getName() + "IOException", e);
        }
        return new ModelAndView("redirect:/joymeapp/topmenu/list?appkey=" + appkey);
    }

    @RequestMapping(value = "/sort/{sort}")
    public ModelAndView getAppMenuSort(@PathVariable(value = "sort") String sort,
                                       @RequestParam(value = "menuId", required = true) Long menuId,
                                       @RequestParam(value = "appkey", required = true) String appkey
    ) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();

        UpdateExpress updateExpress1 = new UpdateExpress();
        UpdateExpress updateExpress2 = new UpdateExpress();
        QueryExpress queryExpress = new QueryExpress();
        mapMessage.put("appkey", appkey);
        try {
            JoymeAppTopMenu topMenu = JoymeAppConfigServiceSngl.get().getJoymeAppTopMenu(new QueryExpress().add(QueryCriterions.eq(JoymeAppTopMenuField.TOP_MENU_ID, menuId)));

            if (sort.equals("up")) {
                queryExpress.add(QueryCriterions.lt(JoymeAppTopMenuField.DISPLAY_ORDER, topMenu.getDisplay_order()))
                        .add(QueryCriterions.eq(JoymeAppTopMenuField.APPKEY, appkey))
                        .add(QuerySort.add(JoymeAppTopMenuField.DISPLAY_ORDER, QuerySortOrder.DESC));
            } else {
                queryExpress.add(QueryCriterions.gt(JoymeAppTopMenuField.DISPLAY_ORDER, topMenu.getDisplay_order()))
                        .add(QueryCriterions.eq(JoymeAppTopMenuField.APPKEY, appkey))
                        .add(QuerySort.add(JoymeAppTopMenuField.DISPLAY_ORDER, QuerySortOrder.ASC));
            }

            PageRows<JoymeAppTopMenu> menuRows = JoymeAppConfigServiceSngl.get().queryJoymeAppTopMenuByPage(queryExpress, new Pagination(1, 1, 1));
            if (menuRows != null && !CollectionUtil.isEmpty(menuRows.getRows())) {
                updateExpress1.set(JoymeAppTopMenuField.DISPLAY_ORDER, topMenu.getDisplay_order());
                JoymeAppConfigServiceSngl.get().modifyJoymeAppTopMenu(new QueryExpress()
                        .add(QueryCriterions.eq(JoymeAppTopMenuField.TOP_MENU_ID, menuRows.getRows().get(0).getMenuId())), updateExpress1, appkey);

                updateExpress2.set(JoymeAppTopMenuField.DISPLAY_ORDER, menuRows.getRows().get(0).getDisplay_order());
                JoymeAppConfigServiceSngl.get().modifyJoymeAppTopMenu(new QueryExpress()
                        .add(QueryCriterions.eq(JoymeAppTopMenuField.TOP_MENU_ID, topMenu.getMenuId())), updateExpress2, appkey);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + "ServiceException", e);
        }

        return new ModelAndView("redirect:/joymeapp/topmenu/list", mapMessage);
    }

//    @RequestMapping(value = "/detail")
//    public ModelAndView detail(@RequestParam(value = "menuid", required = true) Long menuId,
//                                      @RequestParam(value = "appkey", required = true) String appKey) {
//        Map<String, Object> mapMessage = new HashMap<String, Object>();
//
//        try {
//            QueryExpress queryExpress = new QueryExpress();
//            queryExpress.add(QueryCriterions.eq(JoymeAppTopMenuField.TOP_MENU_ID, menuId));
//            JoymeAppTopMenu joymeAppTopMenu = JoymeAppConfigServiceSngl.get().getJoymeAppTopMenu(queryExpress);
//
//            mapMessage.put("joymeAppTopMenu", joymeAppTopMenu);
//            mapMessage.put("appKey", appKey);
//        } catch (ServiceException e) {
//            GAlerter.lab(this.getClass().getName() + "ServoceException", e);
//        }
//        return new ModelAndView("joymeapp/detailtopmenu", mapMessage);
//    }
}
