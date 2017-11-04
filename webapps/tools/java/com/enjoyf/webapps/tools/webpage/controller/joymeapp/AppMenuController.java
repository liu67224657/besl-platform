package com.enjoyf.webapps.tools.webpage.controller.joymeapp;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.joymeappconfig.JoymeAppConfigServiceSngl;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.AuthAppField;
import com.enjoyf.platform.service.oauth.AuthAppType;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.service.tools.ToolsLog;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-30
 * Time: 下午1:43
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/joymeapp/menu")
public class AppMenuController extends ToolsBaseController {
    private static Set<Integer> menuTypes = new HashSet<Integer>();

    private static Set<ActStatus> removeStatusSet = new HashSet<ActStatus>();

    private static Set<JoymeAppMenuDisplayType> displayTypes = new HashSet<JoymeAppMenuDisplayType>();

    private static Set<JoymeAppMenuContentType> contentTypes = new HashSet<JoymeAppMenuContentType>();

    static {
        menuTypes.add(0);
        menuTypes.add(1);

        removeStatusSet.add(ActStatus.ACTED);
        removeStatusSet.add(ActStatus.UNACT);

        displayTypes.addAll(JoymeAppMenuDisplayType.getAll());

        contentTypes.addAll(JoymeAppMenuContentType.getAll());
    }

    @RequestMapping(value = "/list")
    public ModelAndView queryMenuList(
            @RequestParam(value = "appkey", required = false) String appKey,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
            @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        mapMessage.put("displayTypes", displayTypes);
        mapMessage.put("appkey", appKey);
        mapMessage.put("category", category);

        mapMessage.put("menuCategorys", JoymeAppMenuModuleType.getAll());
        try {
            //得到存在的app列表
            List<AuthApp> appList = OAuthServiceSngl.get().queryAuthApp(
                    new QueryExpress()
                            .add(QueryCriterions.eq(AuthAppField.APPTYPE, AuthAppType.INTERNAL_CLIENT.getCode()))
                            .add(QueryCriterions.in(AuthAppField.PLATOFRM, new Integer[]{AppPlatform.ANDROID.getCode(), AppPlatform.CLIENT.getCode(), AppPlatform.IOS.getCode()}))
                            .add(QueryCriterions.eq(AuthAppField.VALIDSTATUS, ValidStatus.VALID.getCode()))
                            .add(QuerySort.add(AuthAppField.CREATEDATE, QuerySortOrder.DESC))
            );
            mapMessage.put("applist", appList);

            //不传aqpkey 不用查询 菜单直接返回
            if (StringUtil.isEmpty(appKey)) {
                return new ModelAndView("/joymeapp/menu/menulist", mapMessage);
            }

            int curPage = (pageStartIndex / pageSize) + 1;
            Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);

            QueryExpress queryExpress = new QueryExpress();
            if (!StringUtil.isEmpty(appKey)) {
                queryExpress.add(QueryCriterions.eq(JoymeAppMenuField.APPKEY, appKey));
            }
            if (!StringUtil.isEmpty(category)) {
                queryExpress.add(QueryCriterions.eq(JoymeAppMenuField.MODULE_TYPE, Integer.parseInt(category)));
            }
            queryExpress.add(QueryCriterions.eq(JoymeAppMenuField.PID, 0l));
            queryExpress.add(QuerySort.add(JoymeAppMenuField.DISPLAY_ORDER, QuerySortOrder.ASC));

            PageRows<JoymeAppMenu> joymeAppMenuPageRows = JoymeAppConfigServiceSngl.get().queryJoymeAppMenuByPage(queryExpress, pagination);
            mapMessage.put("list", joymeAppMenuPageRows.getRows());
            mapMessage.put("page", joymeAppMenuPageRows.getPage());
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("/joymeapp/menu/menulist", mapMessage);
    }


    @RequestMapping(value = "/createonemenupage")
    public ModelAndView createOneMenuPage(@RequestParam(value = "appkey", required = false) String appKey) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        mapMessage.put("appkey", appKey);
        mapMessage.put("menuTypes", menuTypes);
        mapMessage.put("displayTypes", displayTypes);
        mapMessage.put("menuCategorys", JoymeAppMenuModuleType.getAll());

        return new ModelAndView("/joymeapp/menu/createmenu", mapMessage);
    }

    /**
     * 添加一级菜单
     */

    @RequestMapping(value = "/create")
    public ModelAndView insertAppMenu(@RequestParam(value = "appkey", required = true) String appkey,
                                      @RequestParam(value = "menuname", required = true) String menuname,
                                      @RequestParam(value = "url", required = false) String url,
                                      @RequestParam(value = "picurl", required = true) String picurl,
                                      @RequestParam(value = "menuType", required = true) Integer menuType,
                                      @RequestParam(value = "isNew", required = true) boolean isNew,
                                      @RequestParam(value = "displaytype", required = true) Integer displayType,
                                      @RequestParam(value = "isHot", required = true) boolean isHot,
                                      @RequestParam(value = "category", required = false) Integer category,
                                      @RequestParam(value = "lastmodifydate", required = false) String lastmodifydate
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            JoymeAppMenu joymeAppMenu = new JoymeAppMenu();

            joymeAppMenu.setParentId(0l);
            joymeAppMenu.setAppkey(appkey);
            joymeAppMenu.setPicUrl(picurl);
            joymeAppMenu.setMenuName(menuname);
            joymeAppMenu.setUrl(url);
            joymeAppMenu.setMenuType(menuType);
            joymeAppMenu.setNew(isNew);
            joymeAppMenu.setHot(isHot);
            joymeAppMenu.setDisplay_order(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
            joymeAppMenu.setCreateUserId(getCurrentUser().getUserid());
            joymeAppMenu.setCreateDate(new Date());
            joymeAppMenu.setCreateIp(getIp());
            joymeAppMenu.setRemoveStatus(ActStatus.UNACT);
            joymeAppMenu.setDisplayType(JoymeAppMenuDisplayType.getByCode(displayType));
            //
            joymeAppMenu.setModuleType(JoymeAppMenuModuleType.getByCode(category));


            //更新的时间
            if (!StringUtil.isEmpty(lastmodifydate)) {
                joymeAppMenu.setLastModifyDate(DateUtil.StringTodate(lastmodifydate, DateUtil.DEFAULT_DATE_FORMAT2));
            }


            JoymeAppConfigServiceSngl.get().createJoymeAppMenu(joymeAppMenu);

            ToolsLog log = new ToolsLog();

            log.setOpUserId(getCurrentUser().getUserid());
            log.setOperType(LogOperType.CREATE_JOYMEAPP_MENU);
            log.setOpTime(new Date());
            log.setOpIp(getIp());
            log.setOpAfter("create joymeappmenu:" + joymeAppMenu);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "ServiceException ", e);
        }
        return new ModelAndView("redirect:/joymeapp/menu/list?appkey=" + appkey);
    }

    /*
    * @App菜单管理删除动作
    * */
    @RequestMapping(value = "/delete")
    public ModelAndView deleteMenu(@RequestParam(value = "menuid", required = true) Long menuid,
                                   @RequestParam(value = "appkey", required = true) String appKey,
                                   @RequestParam(value = "pid", required = true) Long pid,
                                   @RequestParam(value = "category", required = false) Integer category) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("category", category);
        mapMessage.put("appkey", appKey);
        try {
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(JoymeAppMenuField.REMOVESTATUS, ActStatus.ACTED.getCode());
            updateExpress.set(JoymeAppMenuField.LASTMODIFY_USERID, getCurrentUser().getUserid());
            updateExpress.set(JoymeAppMenuField.LASTMODIFY_IP, getIp());
            updateExpress.set(JoymeAppMenuField.LASTMODIFYDATE, new Date());
            QueryExpress queryExpress = new QueryExpress().add(QueryCriterions.eq(JoymeAppMenuField.BUTTOM_MENU_ID, menuid));

            boolean isTrue = JoymeAppConfigServiceSngl.get().modifyJoymeAppMenu(menuid, appKey, pid, JoymeAppMenuModuleType.getByCode(category), updateExpress);
            if (isTrue) {
                ToolsLog log = new ToolsLog();

                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.DELETE_JOYMEAPP_MENU);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("delete joymeappmenu:" + menuid);
            }


        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("forward:/joymeapp/menu/list", mapMessage);
    }

    /*
   * @App菜单管理恢复动作
   * */
    @RequestMapping(value = "/recover")
    public ModelAndView recoverAppMenu(@RequestParam(value = "menuid", required = true) Long menuid,
                                       @RequestParam(value = "appkey", required = true) String appkey,
                                       @RequestParam(value = "pid", required = true) Long pid,
                                       @RequestParam(value = "category", required = false) Integer category) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("category", category);
        mapMessage.put("appkey", appkey);

        try {
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(JoymeAppMenuField.REMOVESTATUS, ActStatus.UNACT.getCode());
            updateExpress.set(JoymeAppMenuField.LASTMODIFY_USERID, getCurrentUser().getUserid());
            updateExpress.set(JoymeAppMenuField.LASTMODIFY_IP, getIp());
            updateExpress.set(JoymeAppMenuField.LASTMODIFYDATE, new Date());

            QueryExpress queryExpress = new QueryExpress().add(QueryCriterions.eq(JoymeAppMenuField.BUTTOM_MENU_ID, menuid));
            boolean isTrue = JoymeAppConfigServiceSngl.get().modifyJoymeAppMenu(menuid, appkey, pid, JoymeAppMenuModuleType.getByCode(category), updateExpress);
            if (isTrue) {
                ToolsLog log = new ToolsLog();

                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.RECOVER_JOYMEAPP_MENU);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("recover joymeappmenu:" + menuid);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "ServiceException", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("forward:/joymeapp/menu/list", mapMessage);

    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyAppPage(@RequestParam(value = "menuid", required = true) Long menuid,
                                      @RequestParam(value = "appkey", required = true) String appkey) {
        Map<String, Object> mapMessage = null;

        try {
            mapMessage = new HashMap<String, Object>();
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(JoymeAppMenuField.BUTTOM_MENU_ID, menuid));
            JoymeAppMenu joymeAppMenu = JoymeAppConfigServiceSngl.get().getJoymeAppMenu(queryExpress);

            mapMessage.put("joymeAppMenu", joymeAppMenu);
            mapMessage.put("appkey", appkey);
            mapMessage.put("displayTypes", displayTypes);
            mapMessage.put("menuTypes", menuTypes);
            mapMessage.put("menuCategorys", JoymeAppMenuModuleType.getAll());
        } catch (ServiceException e) {

            GAlerter.lab(this.getClass().getName() + "ServoceException", e);
        }

        return new ModelAndView("/joymeapp/menu/modifymenu", mapMessage);
    }

    //
    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "menuid", required = true) Long menuid,
                               @RequestParam(value = "menuname", required = false) String menuname,
                               @RequestParam(value = "url", required = false) String url,
                               @RequestParam(value = "picurl", required = false) String picurl,
                               @RequestParam(value = "menutype", required = false) Integer menutype,
                               @RequestParam(value = "isnew", required = false) boolean isnew,
                               @RequestParam(value = "ishot", required = false) boolean ishot,
                               @RequestParam(value = "appkey", required = true) String appkey,
                               @RequestParam(value = "displaytype", required = true) Integer displayType,
                               @RequestParam(value = "pid", required = true) Long pid,
                               @RequestParam(value = "category", required = false) Integer category,
                               @RequestParam(value = "lastmodifydate", required = false) String lastmodifydate) {
        try {
            Map<String, Object> mapMessage = new HashMap<String, Object>();
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(JoymeAppMenuField.MENU_NAME, menuname);
            updateExpress.set(JoymeAppMenuField.LINK_URL, url);
            updateExpress.set(JoymeAppMenuField.PIC_URL, picurl);
            updateExpress.set(JoymeAppMenuField.MENU_TYPE, menutype);
            updateExpress.set(JoymeAppMenuField.MENU_ISNEW, isnew);
            updateExpress.set(JoymeAppMenuField.MENU_ISHOT, ishot);
            updateExpress.set(JoymeAppMenuField.MODULE_TYPE, category);

            updateExpress.set(JoymeAppMenuField.LASTMODIFY_USERID, getCurrentUser().getUserid());
            updateExpress.set(JoymeAppMenuField.LASTMODIFY_IP, getIp());

            updateExpress.set(JoymeAppMenuField.DISPLAY_TYPE, displayType);


            //更新的时间
            if (!StringUtil.isEmpty(lastmodifydate)) {
                updateExpress.set(JoymeAppMenuField.LASTMODIFYDATE, DateUtil.StringTodate(lastmodifydate, DateUtil.DEFAULT_DATE_FORMAT2));
            } else {
                updateExpress.set(JoymeAppMenuField.LASTMODIFYDATE, new Date());
            }

            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(JoymeAppMenuField.BUTTOM_MENU_ID, menuid));
            boolean isTrue = JoymeAppConfigServiceSngl.get().modifyJoymeAppMenu(menuid, appkey, pid, JoymeAppMenuModuleType.getByCode(category), updateExpress);

            if (isTrue) {
                ToolsLog log = new ToolsLog();

                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.MODIFY_JOYMEAPP_MENU);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("modify joymeappmenu:" + menuid);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "ServiceException", e);
        }
        return new ModelAndView("redirect:/joymeapp/menu/list?appkey=" + appkey);
    }


    //
    @RequestMapping(value = "/detail")
    public ModelAndView preModifyApp(@RequestParam(value = "menuid", required = true) Long menuid,
                                     @RequestParam(value = "appkey", required = true) String appkey) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            JoymeAppMenu appMenu = JoymeAppConfigServiceSngl.get().getJoymeAppMenu(new QueryExpress().add(QueryCriterions.eq(JoymeAppMenuField.BUTTOM_MENU_ID, menuid)));

            if (appMenu == null) {
                //todo
                return new ModelAndView("redirect:/joymeapp/menu/list?appkey=" + appkey);
            }

            mapMessage.put("menu", appMenu);

            AuthApp app = OAuthServiceSngl.get().getApp(appMenu.getAppkey());

            mapMessage.put("menuApp", app);

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "ServiceException", e);
        }
        return new ModelAndView("/joymeapp/menu/menudetail", mapMessage);
    }

    @RequestMapping(value = "/sublist")
    public ModelAndView queryMenuList(
            @RequestParam(value = "appkey", required = true) String appKey,
            @RequestParam(value = "pid", required = true) Long pid,
            @RequestParam(value = "displaytype", required = false) String displayType) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            List<JoymeAppMenuTag> tagList = JoymeAppConfigServiceSngl.get().queryJoymeAppTopMenuTagByMenuId(pid);
            if (!CollectionUtil.isEmpty(tagList)) {
                Map<Long, JoymeAppMenuTag> map = new HashMap<Long, JoymeAppMenuTag>();
                for (JoymeAppMenuTag tag : tagList) {
                    map.put(tag.getTagId(), tag);
                }
                mapMessage.put("tagmap", map);
            }


            mapMessage.put("appkey", appKey);
            List<JoymeAppMenu> menuList = JoymeAppConfigServiceSngl.get().queryJoymeAppMenu(new QueryExpress()
                    .add(QueryCriterions.eq(JoymeAppMenuField.APPKEY, appKey))
                    .add(QueryCriterions.eq(JoymeAppMenuField.PID, pid))
                    .add(QuerySort.add(JoymeAppMenuField.DISPLAY_ORDER, QuerySortOrder.ASC)));
            mapMessage.put("list", menuList);
            mapMessage.put("pid", pid);
            mapMessage.put("displaytype", displayType);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("/joymeapp/menu/submenulist", mapMessage);
    }

    @RequestMapping(value = "/subecreatepage")
    public ModelAndView subCreatePage(@RequestParam(value = "pid", required = true) Long pid,
                                      @RequestParam(value = "appkey", required = true) String appkey,
                                      @RequestParam(value = "displaytype", required = true) String displaytype) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("pid", pid);
        mapMessage.put("appkey", appkey);
        mapMessage.put("displaytype", displaytype);
        mapMessage.put("domain", WebappConfig.get().getDomain());

        mapMessage.put("displayTypes", displayTypes);

        mapMessage.put("contentTypes", contentTypes);
        try {
            JoymeAppMenu menu = JoymeAppConfigServiceSngl.get().getJoymeAppMenu(new QueryExpress().add(QueryCriterions.eq(JoymeAppMenuField.BUTTOM_MENU_ID, pid)));
            if (menu == null) {
                mapMessage = putErrorMessage(mapMessage, "system.error");
                return new ModelAndView("/joymeapp/menu/createsubpage", mapMessage);
            }
            mapMessage.put("appMenu", menu);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/joymeapp/menu/createsubmenu", mapMessage);
    }

    @RequestMapping(value = "/createsub")
    public ModelAndView createSub(@RequestParam(value = "pid", required = false) Long pid,
                                  @RequestParam(value = "appkey", required = false) String appkey,
                                  @RequestParam(value = "menuname", required = false) String menuname,
                                  @RequestParam(value = "menudesc", required = false) String menudesc,
                                  @RequestParam(value = "url", required = false) String url,
                                  @RequestParam(value = "pic_url", required = false) String pic_url,
                                  @RequestParam(value = "menuType", required = false) String menuType,
                                  @RequestParam(value = "displayType", required = false) String displayType,
                                  @RequestParam(value = "isNew", required = false) boolean isNew,
                                  @RequestParam(value = "isHot", required = false) boolean isHot,
                                  @RequestParam(value = "statusdesc", required = false) String statusDesc,
                                  @RequestParam(value = "recomstar", required = false, defaultValue = "0") Integer recommendStar,
                                  @RequestParam(value = "contenttype", required = false) Integer contentType,
                                  @RequestParam(value = "lastmodifydate", required = false) String lastmodifydate,
                                  @RequestParam(value = "menuCategory", required = false) String menuCategory) {
        JoymeAppMenu displaytype = null;
        try {
            Map<String, Object> messageMap = new HashMap<String, Object>();
            messageMap.put("appkey", appkey);
            messageMap.put("pid", pid);

            JoymeAppMenu joymeAppMenu = new JoymeAppMenu();
            joymeAppMenu.setParentId(pid);
            joymeAppMenu.setAppkey(appkey);
            joymeAppMenu.setMenuName(menuname);
            joymeAppMenu.setMenuDesc(menudesc);
            joymeAppMenu.setUrl(url);
            joymeAppMenu.setPicUrl(pic_url);
            if (!StringUtil.isEmpty(menuType)) {
                joymeAppMenu.setMenuType(Integer.valueOf(menuType));
            }
            if (!StringUtil.isEmpty(displayType)) {
                joymeAppMenu.setDisplayType(JoymeAppMenuDisplayType.getByCode(Integer.valueOf(displayType)));
            }
            joymeAppMenu.setNew(isNew);
            joymeAppMenu.setHot(isHot);
            joymeAppMenu.setDisplay_order(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
            joymeAppMenu.setCreateUserId(getCurrentUser().getUserid());
            joymeAppMenu.setCreateDate(new Date());
            joymeAppMenu.setCreateIp(getIp());
            joymeAppMenu.setRemoveStatus(ActStatus.UNACT);
            joymeAppMenu.setStatusDesc(statusDesc);
            joymeAppMenu.setRecommendStar(recommendStar);
            joymeAppMenu.setContentType(JoymeAppMenuContentType.getByCode(contentType));

            if (!StringUtil.isEmpty(menuCategory)) {
                joymeAppMenu.setModuleType(JoymeAppMenuModuleType.getByCode(Integer.valueOf(menuCategory)));
            }

            //更新的时间
            if (!StringUtil.isEmpty(lastmodifydate)) {
                joymeAppMenu.setLastModifyDate(DateUtil.StringTodate(lastmodifydate, DateUtil.DEFAULT_DATE_FORMAT2));
            }
            JoymeAppConfigServiceSngl.get().createJoymeAppMenu(joymeAppMenu);


            displaytype = JoymeAppConfigServiceSngl.get().getJoymeAppMenu(new QueryExpress().add(QueryCriterions.eq(JoymeAppMenuField.BUTTOM_MENU_ID, pid)));

            ToolsLog log = new ToolsLog();

            log.setOpUserId(getCurrentUser().getUserid());
            log.setOperType(LogOperType.CREATE_JOYMEAPP_SUBMENU);
            log.setOpTime(new Date());
            log.setOpIp(getIp());
            log.setOpAfter("create joymeappsubmenu:" + joymeAppMenu);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "ServiceException", e);
        }


        return new ModelAndView("redirect:/joymeapp/menu/sublist?pid=" + pid + "&appkey=" + appkey + "&displaytype=" + displaytype.getDisplayType().getCode());
    }

    /**
     * 修改二级菜单删除按钮
     */
    @RequestMapping(value = "/subdelete")
    public ModelAndView subMenuDelete(@RequestParam(value = "menuid", required = true) Long menuid,
                                      @RequestParam(value = "pid", required = true) Long pid,
                                      @RequestParam(value = "appkey", required = true) String appkey) {
        try {
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(JoymeAppMenuField.REMOVESTATUS, ActStatus.ACTED.getCode());
            updateExpress.set(JoymeAppMenuField.LASTMODIFY_USERID, getCurrentUser().getUserid());
            updateExpress.set(JoymeAppMenuField.LASTMODIFY_IP, getIp());
            updateExpress.set(JoymeAppMenuField.LASTMODIFYDATE, new Date());
            //todo
            boolean isTrue = JoymeAppConfigServiceSngl.get().modifyJoymeAppMenu(menuid, appkey, pid, null, updateExpress);
            if (isTrue) {
                ToolsLog log = new ToolsLog();

                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.DELETE_JOYMEAPP_SUBMENU);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("delete joymeappsubmenu:" + menuid);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "ServiceExcption", e);
        }
        return new ModelAndView("redirect:/joymeapp/menu/sublist?pid=" + pid + "&appkey=" + appkey);

    }

    /**
     * 修改二级菜单恢复按钮
     */
    @RequestMapping(value = "/subrecover")
    public ModelAndView subRecover(@RequestParam(value = "menuid", required = true) Long menuid,
                                   @RequestParam(value = "pid", required = true) Long pid,
                                   @RequestParam(value = "appkey", required = true) String appkey) {
        try {
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(JoymeAppMenuField.REMOVESTATUS, ActStatus.UNACT.getCode());
            updateExpress.set(JoymeAppMenuField.LASTMODIFY_USERID, getCurrentUser().getUserid());
            updateExpress.set(JoymeAppMenuField.LASTMODIFY_IP, getIp());
            updateExpress.set(JoymeAppMenuField.LASTMODIFYDATE, new Date());
            //todo
            boolean isTrue = JoymeAppConfigServiceSngl.get().modifyJoymeAppMenu(menuid, appkey, pid, null, updateExpress);

            if (isTrue) {
                ToolsLog log = new ToolsLog();

                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.MODIFY_JOYMEAPP_SUBMENU);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("recover joymeappsubmenu:" + menuid);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "ServiceExcption", e);
        }
        return new ModelAndView("redirect:/joymeapp/menu/sublist?pid=" + pid + "&appkey=" + appkey);

    }

    /**
     * 修改二级菜单页面
     */
    @RequestMapping(value = "/submodifypage")
    public ModelAndView subModifyAppPage(@RequestParam(value = "menuid", required = true) Long menuid,
                                         @RequestParam(value = "appkey", required = true) String appkey,
                                         @RequestParam(value = "pid", required = true) Long pid) {
        Map<String, Object> mapMessage = null;

        try {
            mapMessage = new HashMap<String, Object>();

            JoymeAppMenu pMenu = JoymeAppConfigServiceSngl.get().getJoymeAppMenu(new QueryExpress().add(QueryCriterions.eq(JoymeAppMenuField.BUTTOM_MENU_ID, pid)));
            if (pMenu == null) {
                mapMessage = putErrorMessage(mapMessage, "system.error");
                return new ModelAndView("/joymeapp/menu/submodifymenu", mapMessage);
            }
            mapMessage.put("pMenu", pMenu);

            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(JoymeAppMenuField.BUTTOM_MENU_ID, menuid));
            JoymeAppMenu joymeAppMenu = JoymeAppConfigServiceSngl.get().getJoymeAppMenu(queryExpress);

            List<JoymeAppMenuTag> tagList = JoymeAppConfigServiceSngl.get().queryJoymeAppTopMenuTagByMenuId(pid);
            if (!CollectionUtil.isEmpty(tagList)) {
                Map<Long, JoymeAppMenuTag> map = new HashMap<Long, JoymeAppMenuTag>();
                for (JoymeAppMenuTag tag : tagList) {
                    map.put(tag.getTagId(), tag);
                }
                mapMessage.put("tagmap", map);
            }

            JoymeAppMenu displayjoymeAppMenu = JoymeAppConfigServiceSngl.get().getJoymeAppMenu(new QueryExpress().add(QueryCriterions.eq(JoymeAppMenuField.BUTTOM_MENU_ID, pid)));
            mapMessage.put("displaytype", displayjoymeAppMenu.getDisplayType().getCode());

            mapMessage.put("domain", WebappConfig.get().getDomain());
            mapMessage.put("joymeAppMenu", joymeAppMenu);
            mapMessage.put("appkey", appkey);
            mapMessage.put("pid", pid);
            mapMessage.put("menuTypes", menuTypes);
            mapMessage.put("displayTypes", displayTypes);
            mapMessage.put("contentTypes", contentTypes);
        } catch (ServiceException e) {

            GAlerter.lab(this.getClass().getName() + "ServoceException", e);
        }


        return new ModelAndView("/joymeapp/menu/modifysubmenu", mapMessage);
    }

    /**
     * 修改二级菜单
     */

    @RequestMapping(value = "/submodify")
    public ModelAndView subModify(@RequestParam(value = "menuid", required = false) Long menuid,
                                  @RequestParam(value = "menuname", required = false) String menuname,
                                  @RequestParam(value = "menudesc", required = false) String menudesc,
                                  @RequestParam(value = "url", required = false) String url,
                                  @RequestParam(value = "pic_url", required = false) String pic_url,
                                  @RequestParam(value = "pid", required = false) Long pid,
                                  @RequestParam(value = "menutype", required = false) String menutype,
                                  @RequestParam(value = "displayType", required = false) String displayType,
                                  @RequestParam(value = "isnew", required = false) boolean isnew,
                                  @RequestParam(value = "ishot", required = false) boolean ishot,
                                  @RequestParam(value = "appkey", required = false) String appkey,
                                  @RequestParam(value = "tagid", required = false, defaultValue = "0") String tagId,
                                  @RequestParam(value = "statusdesc", required = false) String statusDesc,
                                  @RequestParam(value = "recomstar", required = false, defaultValue = "0") Integer recommendStar,
                                  @RequestParam(value = "contenttype", required = false) Integer contentType,
                                  @RequestParam(value = "lastmodifydate", required = false) String lastmodifydate) {
        Map<String, Object> mapMessage = null;

        try {
            mapMessage = new HashMap<String, Object>();
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(JoymeAppMenuField.MENU_NAME, menuname);
            updateExpress.set(JoymeAppMenuField.MENU_DESC, menudesc);
            updateExpress.set(JoymeAppMenuField.LINK_URL, url);
            updateExpress.set(JoymeAppMenuField.PIC_URL, pic_url);
            if (StringUtil.isEmpty(menutype)) {
                updateExpress.set(JoymeAppMenuField.MENU_TYPE, Integer.valueOf(menutype));
            }
            updateExpress.set(JoymeAppMenuField.MENU_ISNEW, isnew);
            updateExpress.set(JoymeAppMenuField.MENU_ISHOT, ishot);
            if (!StringUtil.isEmpty(displayType)) {
                updateExpress.set(JoymeAppMenuField.DISPLAY_TYPE, JoymeAppMenuDisplayType.getByCode(Integer.valueOf(displayType)).getCode());
            }

            updateExpress.set(JoymeAppMenuField.LASTMODIFY_USERID, getCurrentUser().getUserid());
            updateExpress.set(JoymeAppMenuField.LASTMODIFY_IP, getIp());
            updateExpress.set(JoymeAppMenuField.TAG_ID, Long.valueOf(tagId));
            updateExpress.set(JoymeAppMenuField.STATUS_DESCRIPTION, statusDesc);
            updateExpress.set(JoymeAppMenuField.RECOMMENDE_STAR, recommendStar);
            updateExpress.set(JoymeAppMenuField.CONTENT_TYPE, contentType);

            //更新的时间
            if (!StringUtil.isEmpty(lastmodifydate)) {
                updateExpress.set(JoymeAppMenuField.LASTMODIFYDATE, DateUtil.StringTodate(lastmodifydate, DateUtil.DEFAULT_DATE_FORMAT2));
            } else {
                updateExpress.set(JoymeAppMenuField.LASTMODIFYDATE, new Date());
            }

            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(JoymeAppMenuField.BUTTOM_MENU_ID, Long.valueOf(menuid)));
            //todo
            boolean isTrue = JoymeAppConfigServiceSngl.get().modifyJoymeAppMenu(menuid, appkey, pid, null, updateExpress);
            if (isTrue) {
                ToolsLog log = new ToolsLog();

                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.MODIFY_JOYMEAPP_SUBMENU);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("modify joymeappsubmenu:" + menuid);
            }
            mapMessage.put("appkey", appkey);
            mapMessage.put("pid", pid);
        } catch (ServiceException e) {

            GAlerter.lab(this.getClass().getName() + "ServoceException", e);
        }

        return new ModelAndView("redirect:/joymeapp/menu/sublist?pid=" + pid + "&appkey=" + appkey + "&displaytype=" + displayType);
    }


    @RequestMapping(value = "/subdetail")
    public ModelAndView subdetail(@RequestParam(value = "pid", required = false) Long pid,
                                  @RequestParam(value = "menuid", required = true) Long menuid,
                                  @RequestParam(value = "appkey", required = true) String appkey) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            JoymeAppMenu appMenu = JoymeAppConfigServiceSngl.get().getJoymeAppMenu(new QueryExpress().add(QueryCriterions.eq(JoymeAppMenuField.BUTTOM_MENU_ID, menuid)));

            if (appMenu == null) {
                //todo
                return new ModelAndView("redirect:/joymeapp/menu/list?appkey=" + appkey);
            }

            mapMessage.put("menu", appMenu);

            mapMessage.put("pid", pid);

            AuthApp app = OAuthServiceSngl.get().getApp(appMenu.getAppkey());

            mapMessage.put("menuApp", app);

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "ServiceException", e);
        }
        return new ModelAndView("/joymeapp/menu/submenudetail", mapMessage);
    }

    @RequestMapping(value = "/thirdlist")
    public ModelAndView thirdList(
            @RequestParam(value = "appkey", required = false) String appKey,
            @RequestParam(value = "oid", required = false) Long oid,
            @RequestParam(value = "pid", required = false) Long pid,
            @RequestParam(value = "displaytype", required = false) String displayType) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            List<JoymeAppMenuTag> tagList = JoymeAppConfigServiceSngl.get().queryJoymeAppTopMenuTagByMenuId(pid);
            if (!CollectionUtil.isEmpty(tagList)) {
                Map<Long, JoymeAppMenuTag> map = new HashMap<Long, JoymeAppMenuTag>();
                for (JoymeAppMenuTag tag : tagList) {
                    map.put(tag.getTagId(), tag);
                }
                mapMessage.put("tagmap", map);
            }


            mapMessage.put("appkey", appKey);
            List<JoymeAppMenu> menuList = JoymeAppConfigServiceSngl.get().queryJoymeAppMenu(new QueryExpress()
                    .add(QueryCriterions.eq(JoymeAppMenuField.APPKEY, appKey))
                    .add(QueryCriterions.eq(JoymeAppMenuField.PID, pid))
                    .add(QuerySort.add(JoymeAppMenuField.DISPLAY_ORDER, QuerySortOrder.ASC)));
            mapMessage.put("list", menuList);
            mapMessage.put("pid", pid);
            mapMessage.put("oid", oid);
            mapMessage.put("displaytype", displayType);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("/joymeapp/menu/thirdmenulist", mapMessage);
    }

    @RequestMapping(value = "/thirdcreatepage")
    public ModelAndView thirdCreatePage(@RequestParam(value = "oid", required = false) Long oid,
                                        @RequestParam(value = "pid", required = true) Long pid,
                                        @RequestParam(value = "appkey", required = true) String appkey,
                                        @RequestParam(value = "displaytype", required = true) String displaytype) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("pid", pid);
        mapMessage.put("oid", oid);
        mapMessage.put("appkey", appkey);
        mapMessage.put("displaytype", displaytype);
        mapMessage.put("domain", WebappConfig.get().getDomain());

        mapMessage.put("displayTypes", displayTypes);
        try {
            JoymeAppMenu menu = JoymeAppConfigServiceSngl.get().getJoymeAppMenu(new QueryExpress().add(QueryCriterions.eq(JoymeAppMenuField.BUTTOM_MENU_ID, pid)));
            if (menu == null) {
                mapMessage = putErrorMessage(mapMessage, "system.error");
                return new ModelAndView("redirect:/joymeapp/menu/thirdlist?pid=" + pid + "&appkey=" + appkey + "&displaytype=" + displaytype);
            }
            mapMessage.put("appMenu", menu);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/joymeapp/menu/createthirdmenu", mapMessage);
    }

    @RequestMapping(value = "/createthird")
    public ModelAndView createThird(@RequestParam(value = "oid", required = false) Long oid,
                                    @RequestParam(value = "pid", required = false) Long pid,
                                    @RequestParam(value = "appkey", required = false) String appkey,
                                    @RequestParam(value = "menuname", required = false) String menuname,
                                    @RequestParam(value = "menudesc", required = false) String menudesc,
                                    @RequestParam(value = "url", required = false) String url,
                                    @RequestParam(value = "pic_url", required = false) String pic_url,
                                    @RequestParam(value = "menuType", required = false) String menuType,
                                    @RequestParam(value = "displayType", required = false) String displayType,
                                    @RequestParam(value = "isNew", required = false) boolean isNew,
                                    @RequestParam(value = "isHot", required = false) boolean isHot,
                                    @RequestParam(value = "statusdesc", required = false) String statusDesc,
                                    @RequestParam(value = "recomstar", required = false, defaultValue = "0") Integer recommendStar) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("oid", oid);
        mapMessage.put("pid", pid);
        mapMessage.put("appkey", appkey);
        mapMessage.put("displaytype", displayType);

        JoymeAppMenu displaytype = null;
        try {
            JoymeAppMenu joymeAppMenu = new JoymeAppMenu();
            joymeAppMenu.setParentId(pid);
            joymeAppMenu.setAppkey(appkey);
            joymeAppMenu.setMenuName(menuname);
            joymeAppMenu.setMenuDesc(menudesc);
            joymeAppMenu.setUrl(url);
            joymeAppMenu.setPicUrl(pic_url);
            if (!StringUtil.isEmpty(menuType)) {
                joymeAppMenu.setMenuType(Integer.valueOf(menuType));
            }
            if (!StringUtil.isEmpty(displayType)) {
                joymeAppMenu.setDisplayType(JoymeAppMenuDisplayType.getByCode(Integer.valueOf(displayType)));
            }
            joymeAppMenu.setNew(isNew);
            joymeAppMenu.setHot(isHot);
            joymeAppMenu.setDisplay_order(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
            joymeAppMenu.setCreateUserId(getCurrentUser().getUserid());
            joymeAppMenu.setCreateDate(new Date());
            joymeAppMenu.setCreateIp(getIp());
            joymeAppMenu.setRemoveStatus(ActStatus.UNACT);
            joymeAppMenu.setStatusDesc(statusDesc);
            joymeAppMenu.setRecommendStar(recommendStar);

            JoymeAppConfigServiceSngl.get().createJoymeAppMenu(joymeAppMenu);


            displaytype = JoymeAppConfigServiceSngl.get().getJoymeAppMenu(new QueryExpress().add(QueryCriterions.eq(JoymeAppMenuField.BUTTOM_MENU_ID, pid)));

            ToolsLog log = new ToolsLog();

            log.setOpUserId(getCurrentUser().getUserid());
            log.setOperType(LogOperType.CREATE_JOYMEAPP_SUBMENU);
            log.setOpTime(new Date());
            log.setOpIp(getIp());
            log.setOpAfter("create joymeappsubmenu:" + joymeAppMenu);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "ServiceException", e);
        }
        return new ModelAndView("forward:/joymeapp/menu/thirdlist", mapMessage);
    }

    /**
     * 修改二级菜单删除按钮
     */
    @RequestMapping(value = "/thirddelete")
    public ModelAndView thirdDelete(@RequestParam(value = "oid", required = false) Long oid,
                                    @RequestParam(value = "menuid", required = true) Long menuid,
                                    @RequestParam(value = "pid", required = true) Long pid,
                                    @RequestParam(value = "appkey", required = true) String appkey) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("oid", oid);
        mapMessage.put("pid", pid);
        mapMessage.put("appkey", appkey);
        try {
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(JoymeAppMenuField.REMOVESTATUS, ActStatus.ACTED.getCode());
            updateExpress.set(JoymeAppMenuField.LASTMODIFY_USERID, getCurrentUser().getUserid());
            updateExpress.set(JoymeAppMenuField.LASTMODIFY_IP, getIp());
            updateExpress.set(JoymeAppMenuField.LASTMODIFYDATE, new Date());
            //todo
            boolean isTrue = JoymeAppConfigServiceSngl.get().modifyJoymeAppMenu(menuid, appkey, pid, null, updateExpress);
            if (isTrue) {
                ToolsLog log = new ToolsLog();

                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.DELETE_JOYMEAPP_SUBMENU);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("delete joymeappsubmenu:" + menuid);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "ServiceExcption", e);
        }
        return new ModelAndView("forward:/joymeapp/menu/thirdlist", mapMessage);

    }

    /**
     * 修改二级菜单恢复按钮
     */
    @RequestMapping(value = "/thirdrecover")
    public ModelAndView thirdRecover(@RequestParam(value = "oid", required = false) Long oid,
                                     @RequestParam(value = "menuid", required = true) Long menuid,
                                     @RequestParam(value = "pid", required = true) Long pid,
                                     @RequestParam(value = "appkey", required = true) String appkey) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("oid", oid);
        mapMessage.put("pid", pid);
        mapMessage.put("appkey", appkey);

        try {
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(JoymeAppMenuField.REMOVESTATUS, ActStatus.UNACT.getCode());
            updateExpress.set(JoymeAppMenuField.LASTMODIFY_USERID, getCurrentUser().getUserid());
            updateExpress.set(JoymeAppMenuField.LASTMODIFY_IP, getIp());
            updateExpress.set(JoymeAppMenuField.LASTMODIFYDATE, new Date());
            //todo
            boolean isTrue = JoymeAppConfigServiceSngl.get().modifyJoymeAppMenu(menuid, appkey, pid, null, updateExpress);

            if (isTrue) {
                ToolsLog log = new ToolsLog();

                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.MODIFY_JOYMEAPP_SUBMENU);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("recover joymeappsubmenu:" + menuid);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "ServiceExcption", e);
        }
        return new ModelAndView("forward:/joymeapp/menu/thirdlist", mapMessage);

    }

    /**
     * 修改二级菜单页面
     */
    @RequestMapping(value = "/thirdmodifypage")
    public ModelAndView thirdModifyPage(@RequestParam(value = "oid", required = false) Long oid,
                                        @RequestParam(value = "menuid", required = false) Long menuid,
                                        @RequestParam(value = "appkey", required = false) String appkey,
                                        @RequestParam(value = "pid", required = false) Long pid) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        ;
        mapMessage.put("appkey", appkey);
        mapMessage.put("pid", pid);
        mapMessage.put("oid", oid);
        try {
            JoymeAppMenu pMenu = JoymeAppConfigServiceSngl.get().getJoymeAppMenu(new QueryExpress().add(QueryCriterions.eq(JoymeAppMenuField.BUTTOM_MENU_ID, pid)));
            if (pMenu == null) {
                mapMessage = putErrorMessage(mapMessage, "system.error");
                return new ModelAndView("forward:/joymeapp/menu/thirdlist", mapMessage);
            }
            mapMessage.put("pMenu", pMenu);

            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(JoymeAppMenuField.BUTTOM_MENU_ID, menuid));
            JoymeAppMenu joymeAppMenu = JoymeAppConfigServiceSngl.get().getJoymeAppMenu(queryExpress);

            List<JoymeAppMenuTag> tagList = JoymeAppConfigServiceSngl.get().queryJoymeAppTopMenuTagByMenuId(pid);
            if (!CollectionUtil.isEmpty(tagList)) {
                Map<Long, JoymeAppMenuTag> map = new HashMap<Long, JoymeAppMenuTag>();
                for (JoymeAppMenuTag tag : tagList) {
                    map.put(tag.getTagId(), tag);
                }
                mapMessage.put("tagmap", map);
            }

            JoymeAppMenu displayjoymeAppMenu = JoymeAppConfigServiceSngl.get().getJoymeAppMenu(new QueryExpress().add(QueryCriterions.eq(JoymeAppMenuField.BUTTOM_MENU_ID, pid)));
            mapMessage.put("displaytype", displayjoymeAppMenu.getDisplayType().getCode());

            mapMessage.put("domain", WebappConfig.get().getDomain());
            mapMessage.put("joymeAppMenu", joymeAppMenu);
            mapMessage.put("menuTypes", menuTypes);
            mapMessage.put("displayTypes", displayTypes);
        } catch (ServiceException e) {

            GAlerter.lab(this.getClass().getName() + "ServoceException", e);
        }


        return new ModelAndView("/joymeapp/menu/modifythirdmenu", mapMessage);
    }

    /**
     * 修改二级菜单
     */

    @RequestMapping(value = "/thirdmodify")
    public ModelAndView thirdModify(@RequestParam(value = "oid", required = false) Long oid,
                                    @RequestParam(value = "menuid", required = false) Long menuid,
                                    @RequestParam(value = "menuname", required = false) String menuname,
                                    @RequestParam(value = "menudesc", required = false) String menudesc,
                                    @RequestParam(value = "url", required = false) String url,
                                    @RequestParam(value = "pic_url", required = false) String pic_url,
                                    @RequestParam(value = "pid", required = false) Long pid,
                                    @RequestParam(value = "menutype", required = false) String menutype,
                                    @RequestParam(value = "displayType", required = false) String displayType,
                                    @RequestParam(value = "isnew", required = false) boolean isnew,
                                    @RequestParam(value = "ishot", required = false) boolean ishot,
                                    @RequestParam(value = "appkey", required = false) String appkey,
                                    @RequestParam(value = "tagid", required = false, defaultValue = "0") String tagId,
                                    @RequestParam(value = "statusdesc", required = false) String statusDesc,
                                    @RequestParam(value = "recomstar", required = false, defaultValue = "0") Integer recommendStar) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        ;

        try {
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(JoymeAppMenuField.MENU_NAME, menuname);
            updateExpress.set(JoymeAppMenuField.MENU_DESC, menudesc);
            updateExpress.set(JoymeAppMenuField.LINK_URL, url);
            updateExpress.set(JoymeAppMenuField.PIC_URL, pic_url);
            if (StringUtil.isEmpty(menutype)) {
                updateExpress.set(JoymeAppMenuField.MENU_TYPE, Integer.valueOf(menutype));
            }
            updateExpress.set(JoymeAppMenuField.MENU_ISNEW, isnew);
            updateExpress.set(JoymeAppMenuField.MENU_ISHOT, ishot);
            if (!StringUtil.isEmpty(displayType)) {
                updateExpress.set(JoymeAppMenuField.DISPLAY_TYPE, JoymeAppMenuDisplayType.getByCode(Integer.valueOf(displayType)).getCode());
            }

            updateExpress.set(JoymeAppMenuField.LASTMODIFY_USERID, getCurrentUser().getUserid());
            updateExpress.set(JoymeAppMenuField.LASTMODIFY_IP, getIp());
            updateExpress.set(JoymeAppMenuField.LASTMODIFYDATE, new Date());
            updateExpress.set(JoymeAppMenuField.TAG_ID, Long.valueOf(tagId));
            updateExpress.set(JoymeAppMenuField.STATUS_DESCRIPTION, statusDesc);
            updateExpress.set(JoymeAppMenuField.RECOMMENDE_STAR, recommendStar);


            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(JoymeAppMenuField.BUTTOM_MENU_ID, menuid));
            //todo
            boolean isTrue = JoymeAppConfigServiceSngl.get().modifyJoymeAppMenu(menuid, appkey, pid, null, updateExpress);
            if (isTrue) {
                ToolsLog log = new ToolsLog();

                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.MODIFY_JOYMEAPP_SUBMENU);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("modify joymeappsubmenu:" + menuid);
            }
            mapMessage.put("appkey", appkey);
            mapMessage.put("pid", pid);
            mapMessage.put("oid", oid);
            mapMessage.put("displaytype", displayType);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "ServoceException", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("forward:/joymeapp/menu/thirdlist", mapMessage);
    }


    @RequestMapping(value = "/sort/{sort}")
    public ModelAndView getAppMenuSort(@PathVariable(value = "sort") String sort,
                                       @RequestParam(value = "menuId", required = false) Long menuId,
                                       @RequestParam(value = "appkey", required = false) String appkey,
                                       @RequestParam(value = "oid", required = false) Long oid,
                                       @RequestParam(value = "pid", required = false) Long pid,
                                       @RequestParam(value = "category", required = false) Integer category) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();

        UpdateExpress updateExpress1 = new UpdateExpress();
        UpdateExpress updateExpress2 = new UpdateExpress();
        QueryExpress queryExpress = new QueryExpress();
        mapMessage.put("appkey", appkey);
        mapMessage.put("pid", pid);
        mapMessage.put("oid", oid);
        mapMessage.put("category", category);

        String redirePage = "list";
        try {
            JoymeAppMenu appMenu = JoymeAppConfigServiceSngl.get().getJoymeAppMenu(new QueryExpress().add(QueryCriterions.eq(JoymeAppMenuField.BUTTOM_MENU_ID, menuId)));
            if (sort.equals("up")) {
                queryExpress.add(QueryCriterions.lt(JoymeAppMenuField.DISPLAY_ORDER, appMenu.getDisplay_order()))
                        .add(QuerySort.add(JoymeAppMenuField.DISPLAY_ORDER, QuerySortOrder.DESC))
                        .add(QueryCriterions.eq(JoymeAppMenuField.PID, pid))
                        .add(QueryCriterions.eq(JoymeAppMenuField.APPKEY, appkey));
            } else {
                queryExpress.add(QueryCriterions.gt(JoymeAppMenuField.DISPLAY_ORDER, appMenu.getDisplay_order()))
                        .add(QuerySort.add(JoymeAppMenuField.DISPLAY_ORDER, QuerySortOrder.ASC))
                        .add(QueryCriterions.eq(JoymeAppMenuField.PID, pid))
                        .add(QueryCriterions.eq(JoymeAppMenuField.APPKEY, appkey));
            }
            PageRows<JoymeAppMenu> appRows = JoymeAppConfigServiceSngl.get().queryJoymeAppMenuByPage(queryExpress, new Pagination(1, 1, 1));

            if (appRows != null && !CollectionUtil.isEmpty(appRows.getRows())) {

                updateExpress1.set(JoymeAppMenuField.DISPLAY_ORDER, appMenu.getDisplay_order());
                JoymeAppConfigServiceSngl.get().modifyJoymeAppMenu(appRows.getRows().get(0).getMenuId(), appkey, appRows.getRows().get(0).getParentId(), appRows.getRows().get(0).getModuleType(), updateExpress1);

                updateExpress2.set(JoymeAppMenuField.DISPLAY_ORDER, appRows.getRows().get(0).getDisplay_order());
                JoymeAppConfigServiceSngl.get().modifyJoymeAppMenu(appMenu.getMenuId(), appkey, appMenu.getParentId(), appMenu.getModuleType(), updateExpress2);
            }


            if (0 == oid && pid != 0 && !pid.equals(0)) {
                redirePage = "sublist";
            } else if (oid != 0 && pid != 0) {
                redirePage = "thirdlist";
            }
        } catch (Exception e) {
            GAlerter.lab("AppMenuController occur Exception.e:", e);
        }
        return new ModelAndView("forward:/joymeapp/menu/" + redirePage, mapMessage);
    }

    ////////////////////////////////////////////////////////////////////////////////////
    @RequestMapping(value = "/tag/taglist")
    public ModelAndView tagList(@RequestParam(value = "menuid") Long menuid) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("menuid", menuid);
        try {
            List<JoymeAppMenuTag> tagList = JoymeAppConfigServiceSngl.get().queryJoymeAppTopMenuTagByMenuId(menuid);
            mapMessage.put("taglist", tagList);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("/joymeapp/tab_appmenutaglist", mapMessage);
    }

    @RequestMapping(value = "/tag/createpage")
    public ModelAndView createPage(@RequestParam(value = "menuid") Long menuid) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("menuid", menuid);
        return new ModelAndView("/joymeapp/tab_appmenutagcreatepage", mapMessage);
    }

    @RequestMapping(value = "/tag/create")
    public ModelAndView create(@RequestParam(value = "menuid") Long menuid,
                               @RequestParam(value = "tagname") String tagName) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        JoymeAppMenuTag tag = new JoymeAppMenuTag();
        tag.setCreateDate(new Date());
        tag.setCreateId(getCurrentUser().getUserid());
        tag.setCreateIp(getIp());
        tag.setTagName(tagName);
        tag.setTopMenuId(menuid);
        tag.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));

        try {
            JoymeAppConfigServiceSngl.get().createJoymeAppTopMenuTag(tag);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
        }

        return new ModelAndView("redirect:/joymeapp/menu/tag/taglist?menuid=" + menuid);
    }

    @RequestMapping(value = "/tag/modifypage")
    public ModelAndView modifyPage(@RequestParam(value = "tagid") Long tagid) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        mapMessage.put("removeStatusSet", removeStatusSet);
        try {
            JoymeAppMenuTag tag = JoymeAppConfigServiceSngl.get().getJoymeAppTopMenuTagBytagId(tagid);
            if (tag != null) {
                mapMessage.put("tag", tag);
            }

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
        }

        return new ModelAndView("/joymeapp/tab_appmenutagmodifypage", mapMessage);
    }

    @RequestMapping(value = "/tag/modify")
    public ModelAndView modify(@RequestParam(value = "tagid") Long tagid,
                               @RequestParam(value = "menuid") Long menuid,
                               @RequestParam(value = "name") String tagname,
                               @RequestParam(value = "removestatus", defaultValue = "n") String removeStatus,
                               @RequestParam(value = "displayorder", defaultValue = "0") Integer displayOrder) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(JoymeAppTopMenuTagField.TAGNAME, tagname);
        updateExpress.set(JoymeAppTopMenuTagField.REMOVESTATUS, ActStatus.getByCode(removeStatus).getCode());
        updateExpress.set(JoymeAppTopMenuTagField.DISPLAYORDER, displayOrder);


        try {
            JoymeAppConfigServiceSngl.get().modifyJoymeAppTopMenuTag(updateExpress, tagid);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
        }

        return new ModelAndView("redirect:/joymeapp/menu/tag/taglist?menuid=" + menuid);
    }
}