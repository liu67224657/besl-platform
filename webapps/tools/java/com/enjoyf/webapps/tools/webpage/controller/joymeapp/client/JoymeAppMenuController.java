package com.enjoyf.webapps.tools.webpage.controller.joymeapp.client;

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

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-30
 * Time: 下午1:43
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/joymeapp/client/menu")
public class JoymeAppMenuController extends ToolsBaseController {
    private static Set<Integer> jtSet = new HashSet<Integer>();

    private static Set<JoymeAppMenuModuleType> moduleSet = new HashSet<JoymeAppMenuModuleType>();

    static {
        jtSet.add(0);
        jtSet.add(1);

        moduleSet.add(JoymeAppMenuModuleType.HEADLIST);
        moduleSet.add(JoymeAppMenuModuleType.NEWSLIST);
        moduleSet.add(JoymeAppMenuModuleType.OTHERINFO);
        moduleSet.add(JoymeAppMenuModuleType.MODULE1);
        moduleSet.add(JoymeAppMenuModuleType.MODULE2);
        moduleSet.add(JoymeAppMenuModuleType.MODULE3);
    }

    @RequestMapping(value = "/list")
    public ModelAndView queryMenuList(
            @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
            @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
            @RequestParam(value = "appkey", required = false) String appKey,
            @RequestParam(value = "module", required = false) Integer module) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("appKey", appKey);
        mapMessage.put("module", module);

        mapMessage.put("moduleSet", moduleSet);

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
                return new ModelAndView("/joymeapp/client/menu/menulist", mapMessage);
            }

            int curPage = (pageStartIndex / pageSize) + 1;
            Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);

            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(JoymeAppMenuField.APPKEY, appKey));
            if (module != null) {
                queryExpress.add(QueryCriterions.eq(JoymeAppMenuField.MODULE_TYPE, module));
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

        return new ModelAndView("/joymeapp/client/menu/menulist", mapMessage);
    }


    @RequestMapping(value = "/createpage")
    public ModelAndView createPage(@RequestParam(value = "appkey", required = false) String appKey,
                                   @RequestParam(value = "module", required = false) Integer module,
                                   @RequestParam(value = "pid", required = false, defaultValue = "0") Long pid) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("appKey", appKey);
        mapMessage.put("module", module);
        mapMessage.put("pid", pid);
        mapMessage.put("jtSet", jtSet);
        mapMessage.put("moduleSet", moduleSet);
        mapMessage.put("displaySet", JoymeAppMenuDisplayType.getAll());
        mapMessage.put("contentSet", JoymeAppMenuContentType.getAll());
        return new ModelAndView("/joymeapp/client/menu/createmenu", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView insertAppMenu(@RequestParam(value = "pid", required = false, defaultValue = "0") Long pid,
                                      @RequestParam(value = "appkey", required = false) String appKey,
                                      @RequestParam(value = "module", required = false) Integer module,
                                      @RequestParam(value = "menuname", required = false) String menuName,
                                      @RequestParam(value = "menudesc", required = false) String menuDesc,
                                      @RequestParam(value = "ji", required = false) String ji,
                                      @RequestParam(value = "jt", required = true) Integer jt,
                                      @RequestParam(value = "picios", required = false) String picIos,
                                      @RequestParam(value = "picandroid", required = false) String picAndroid,
                                      @RequestParam(value = "displaytype", required = false) Integer displayType,
                                      @RequestParam(value = "contenttype", required = false) Integer contentType,
                                      @RequestParam(value = "isnew", required = false, defaultValue = "false") Boolean isNew,
                                      @RequestParam(value = "ishot", required = false, defaultValue = "false") Boolean isHot,
                                      @RequestParam(value = "expdesc", required = false) String expDesc,
                                      @RequestParam(value = "author", required = false) String author,
                                      @RequestParam(value = "star", required = false) String star,
                                      @RequestParam(value = "publishdate", required = false) String publishDate
    ) {
        try {
            if (pid == 0l) {
                JoymeAppMenu appMenu = JoymeAppConfigServiceSngl.get().getJoymeAppMenu(new QueryExpress()
                        .add(QueryCriterions.eq(JoymeAppMenuField.PID, 0l))
                        .add(QueryCriterions.eq(JoymeAppMenuField.APPKEY, appKey))
                        .add(QueryCriterions.eq(JoymeAppMenuField.MODULE_TYPE, module)));
                if (appMenu == null) {
                    JoymeAppMenu joymeAppMenu = new JoymeAppMenu();
                    joymeAppMenu.setParentId(pid);
                    joymeAppMenu.setAppkey(appKey);
                    joymeAppMenu.setModuleType(JoymeAppMenuModuleType.getByCode(module));

                    joymeAppMenu.setMenuName(menuName);
                    joymeAppMenu.setMenuDesc(menuDesc);
                    joymeAppMenu.setUrl(ji);
                    joymeAppMenu.setMenuType(jt);

                    JoymeAppMenuPic pic = new JoymeAppMenuPic();
                    pic.setIosPic(picIos);
                    pic.setAndroidPic(picAndroid);
                    joymeAppMenu.setPicUrl(pic.toJsonStr());

                    if (displayType != null) {
                        joymeAppMenu.setDisplayType(JoymeAppMenuDisplayType.getByCode(displayType));
                    }
                    if (contentType != null) {
                        joymeAppMenu.setContentType(JoymeAppMenuContentType.getByCode(contentType));
                    }

                    joymeAppMenu.setHot(isHot);
                    joymeAppMenu.setNew(isNew);
                    JoymeAppMenuExpField expField = new JoymeAppMenuExpField();
                    expField.setAuthor(author);
                    expField.setExpDesc(expDesc);
                    expField.setStar(star);
                    expField.setPublishDate(publishDate);
                    joymeAppMenu.setExpField(expField);

                    joymeAppMenu.setDisplay_order(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
                    joymeAppMenu.setCreateUserId(getCurrentUser().getUserid());
                    joymeAppMenu.setCreateDate(new Date());
                    joymeAppMenu.setCreateIp(getIp());
                    joymeAppMenu.setRemoveStatus(ActStatus.UNACT);

                    JoymeAppConfigServiceSngl.get().createJoymeAppMenu(joymeAppMenu);

                    writeToolsLog(LogOperType.CREATE_JOYMEAPP_MENU, "create joymeapp menu:" + appKey);
                }
            } else {
                JoymeAppMenu joymeAppMenu = new JoymeAppMenu();
                joymeAppMenu.setParentId(pid);
                joymeAppMenu.setAppkey(appKey);
                joymeAppMenu.setModuleType(JoymeAppMenuModuleType.getByCode(module));

                joymeAppMenu.setMenuName(menuName);
                joymeAppMenu.setMenuDesc(menuDesc);
                joymeAppMenu.setUrl(ji);
                joymeAppMenu.setMenuType(jt);

                JoymeAppMenuPic pic = new JoymeAppMenuPic();
                pic.setIosPic(picIos);
                pic.setAndroidPic(picAndroid);
                joymeAppMenu.setPic(pic);

                if (displayType != null) {
                    joymeAppMenu.setDisplayType(JoymeAppMenuDisplayType.getByCode(displayType));
                }
                if (contentType != null) {
                    joymeAppMenu.setContentType(JoymeAppMenuContentType.getByCode(contentType));
                }
                joymeAppMenu.setHot(isHot);
                joymeAppMenu.setNew(isNew);
                JoymeAppMenuExpField expField = new JoymeAppMenuExpField();
                expField.setAuthor(author);
                expField.setExpDesc(expDesc);
                expField.setStar(star);
                expField.setPublishDate(publishDate);
                joymeAppMenu.setExpField(expField);

                joymeAppMenu.setDisplay_order(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
                joymeAppMenu.setCreateUserId(getCurrentUser().getUserid());
                joymeAppMenu.setCreateDate(new Date());
                joymeAppMenu.setCreateIp(getIp());
                joymeAppMenu.setRemoveStatus(ActStatus.UNACT);

                JoymeAppConfigServiceSngl.get().createJoymeAppMenu(joymeAppMenu);

                writeToolsLog(LogOperType.CREATE_JOYMEAPP_MENU, "create joymeapp menu:" + appKey);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "ServiceException ", e);
        }
        if (pid > 0l) {
            return new ModelAndView("redirect:/joymeapp/client/menu/sublist?appkey=" + appKey + "&module=" + module + "&pid=" + pid);
        } else {
            return new ModelAndView("redirect:/joymeapp/client/menu/list?appkey=" + appKey + "&module=" + module);
        }

    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyPage(@RequestParam(value = "menuid", required = false) Long menuid,
                                   @RequestParam(value = "pid", required = false) Long pid,
                                   @RequestParam(value = "appkey", required = false) String appKey,
                                   @RequestParam(value = "module", required = false) Integer module) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("appKey", appKey);
        mapMessage.put("module", module);
        mapMessage.put("pid", pid);
        mapMessage.put("jtSet", jtSet);
        mapMessage.put("moduleSet", moduleSet);
        mapMessage.put("displaySet", JoymeAppMenuDisplayType.getAll());
        mapMessage.put("contentSet", JoymeAppMenuContentType.getAll());

        try {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(JoymeAppMenuField.BUTTOM_MENU_ID, menuid));
            JoymeAppMenu joymeAppMenu = JoymeAppConfigServiceSngl.get().getJoymeAppMenu(queryExpress);
            if (joymeAppMenu == null) {
                return new ModelAndView("redirect:/joymeapp/client/menu/list?appkey=" + appKey + "&module=" + module);
            }
            mapMessage.put("joymeAppMenu", joymeAppMenu);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "ServiceException", e);
        }
        return new ModelAndView("/joymeapp/client/menu/modifymenu", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "menuid", required = false) Long menuid,
                               @RequestParam(value = "pid", required = false, defaultValue = "0") Long pid,
                               @RequestParam(value = "appkey", required = false) String appKey,
                               @RequestParam(value = "module", required = false) Integer module,
                               @RequestParam(value = "menuname", required = false) String menuName,
                               @RequestParam(value = "menudesc", required = false) String menuDesc,
                               @RequestParam(value = "ji", required = false) String ji,
                               @RequestParam(value = "jt", required = true) Integer jt,
                               @RequestParam(value = "picios", required = false) String picIos,
                               @RequestParam(value = "picandroid", required = false) String picAndroid,
                               @RequestParam(value = "displaytype", required = false) Integer displayType,
                               @RequestParam(value = "contenttype", required = false) Integer contentType,
                               @RequestParam(value = "isnew", required = false, defaultValue = "false") Boolean isNew,
                               @RequestParam(value = "ishot", required = false, defaultValue = "false") Boolean isHot,
                               @RequestParam(value = "expdesc", required = false) String expDesc,
                               @RequestParam(value = "author", required = false) String author,
                               @RequestParam(value = "star", required = false) String star,
                               @RequestParam(value = "publishdate", required = false) String publishDate) {
        try {
            if (pid == 0l) {
                JoymeAppMenu appMenu = JoymeAppConfigServiceSngl.get().getJoymeAppMenu(new QueryExpress()
                        .add(QueryCriterions.ne(JoymeAppMenuField.BUTTOM_MENU_ID, menuid))
                        .add(QueryCriterions.eq(JoymeAppMenuField.APPKEY, appKey))
                        .add(QueryCriterions.eq(JoymeAppMenuField.MODULE_TYPE, module)));
                if (appMenu == null) {
                    UpdateExpress updateExpress = new UpdateExpress();
                    updateExpress.set(JoymeAppMenuField.MENU_NAME, menuName);
                    updateExpress.set(JoymeAppMenuField.MENU_DESC, menuDesc);
                    updateExpress.set(JoymeAppMenuField.LINK_URL, ji);
                    updateExpress.set(JoymeAppMenuField.MENU_TYPE, jt);
                    updateExpress.set(JoymeAppMenuField.MODULE_TYPE, module);

                    JoymeAppMenuPic pic = new JoymeAppMenuPic();
                    pic.setIosPic(picIos);
                    pic.setAndroidPic(picAndroid);
                    updateExpress.set(JoymeAppMenuField.PIC, pic.toJsonStr());

                    updateExpress.set(JoymeAppMenuField.DISPLAY_TYPE, displayType);
                    updateExpress.set(JoymeAppMenuField.CONTENT_TYPE, contentType);
                    updateExpress.set(JoymeAppMenuField.MENU_ISNEW, isNew);
                    updateExpress.set(JoymeAppMenuField.MENU_ISHOT, isHot);

                    JoymeAppMenuExpField expField = new JoymeAppMenuExpField();
                    expField.setAuthor(author);
                    expField.setExpDesc(expDesc);
                    expField.setStar(star);
                    expField.setPublishDate(publishDate);
                    updateExpress.set(JoymeAppMenuField.EXPFIELD, expField.toJsonStr());

                    updateExpress.set(JoymeAppMenuField.LASTMODIFY_USERID, getCurrentUser().getUserid());
                    updateExpress.set(JoymeAppMenuField.LASTMODIFY_IP, getIp());
                    updateExpress.set(JoymeAppMenuField.LASTMODIFYDATE, new Date());

                    boolean isTrue = JoymeAppConfigServiceSngl.get().modifyJoymeAppMenu(menuid, appKey, 0l, JoymeAppMenuModuleType.getByCode(module), updateExpress);
                    if (isTrue) {
                        writeToolsLog(LogOperType.MODIFY_JOYMEAPP_MENU, "modify joymeapp menu:" + menuid);
                    }
                }
            } else {
                UpdateExpress updateExpress = new UpdateExpress();
                updateExpress.set(JoymeAppMenuField.MENU_NAME, menuName);
                updateExpress.set(JoymeAppMenuField.MENU_DESC, menuDesc);
                updateExpress.set(JoymeAppMenuField.LINK_URL, ji);
                updateExpress.set(JoymeAppMenuField.MENU_TYPE, jt);
                updateExpress.set(JoymeAppMenuField.MODULE_TYPE, module);

                JoymeAppMenuPic pic = new JoymeAppMenuPic();
                pic.setIosPic(picIos);
                pic.setAndroidPic(picAndroid);
                updateExpress.set(JoymeAppMenuField.PIC, pic.toJsonStr());

                updateExpress.set(JoymeAppMenuField.DISPLAY_TYPE, displayType);
                updateExpress.set(JoymeAppMenuField.CONTENT_TYPE, contentType);
                updateExpress.set(JoymeAppMenuField.MENU_ISNEW, isNew);
                updateExpress.set(JoymeAppMenuField.MENU_ISHOT, isHot);

                JoymeAppMenuExpField expField = new JoymeAppMenuExpField();
                expField.setAuthor(author);
                expField.setExpDesc(expDesc);
                expField.setStar(star);
                expField.setPublishDate(publishDate);
                updateExpress.set(JoymeAppMenuField.EXPFIELD, expField.toJsonStr());

                updateExpress.set(JoymeAppMenuField.LASTMODIFY_USERID, getCurrentUser().getUserid());
                updateExpress.set(JoymeAppMenuField.LASTMODIFY_IP, getIp());
                updateExpress.set(JoymeAppMenuField.LASTMODIFYDATE, new Date());

                boolean isTrue = JoymeAppConfigServiceSngl.get().modifyJoymeAppMenu(menuid, appKey, 0l, JoymeAppMenuModuleType.getByCode(module), updateExpress);
                if (isTrue) {
                    writeToolsLog(LogOperType.MODIFY_JOYMEAPP_MENU, "modify joymeapp menu:" + menuid);
                }
            }

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "ServiceException", e);
        }
        if (pid == 0l) {
            return new ModelAndView("redirect:/joymeapp/client/menu/list?appkey=" + appKey + "&module=" + module);
        } else {
            return new ModelAndView("redirect:/joymeapp/client/menu/sublist?appkey=" + appKey + "&module=" + module + "&pid=" + pid);
        }

    }

    @RequestMapping(value = "/remove")
    public ModelAndView delete(@RequestParam(value = "menuid", required = true) Long menuid,
                               @RequestParam(value = "pid", required = true) Long pid,
                               @RequestParam(value = "appkey", required = true) String appKey,
                               @RequestParam(value = "module", required = false) Integer module) {
        try {
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(JoymeAppMenuField.REMOVESTATUS, ActStatus.ACTED.getCode());
            updateExpress.set(JoymeAppMenuField.LASTMODIFY_USERID, getCurrentUser().getUserid());
            updateExpress.set(JoymeAppMenuField.LASTMODIFY_IP, getIp());
            updateExpress.set(JoymeAppMenuField.LASTMODIFYDATE, new Date());

            boolean isTrue = JoymeAppConfigServiceSngl.get().modifyJoymeAppMenu(menuid, appKey, pid, (module == null ? null : JoymeAppMenuModuleType.getByCode(module)), updateExpress);
            if (isTrue) {
                writeToolsLog(LogOperType.DELETE_JOYMEAPP_MENU, "delete joymeapp menu:" + menuid);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }
        if (pid == 0l) {
            return new ModelAndView("redirect:/joymeapp/client/menu/list?appkey=" + appKey + "&module=" + module);
        } else {
            return new ModelAndView("redirect:/joymeapp/client/menu/sublist?appkey=" + appKey + "&module=" + module + "&pid=" + pid);
        }

    }

    @RequestMapping(value = "/recover")
    public ModelAndView recover(@RequestParam(value = "menuid", required = true) Long menuid,
                                @RequestParam(value = "pid", required = true) Long pid,
                                @RequestParam(value = "appkey", required = true) String appKey,
                                @RequestParam(value = "module", required = false) Integer module) {
        try {
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(JoymeAppMenuField.REMOVESTATUS, ActStatus.UNACT.getCode());
            updateExpress.set(JoymeAppMenuField.LASTMODIFY_USERID, getCurrentUser().getUserid());
            updateExpress.set(JoymeAppMenuField.LASTMODIFY_IP, getIp());
            updateExpress.set(JoymeAppMenuField.LASTMODIFYDATE, new Date());

            boolean isTrue = JoymeAppConfigServiceSngl.get().modifyJoymeAppMenu(menuid, appKey, pid, (module == null ? null : JoymeAppMenuModuleType.getByCode(module)), updateExpress);
            if (isTrue) {
                writeToolsLog(LogOperType.DELETE_JOYMEAPP_MENU, "delete joymeapp menu:" + menuid);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }
        if (pid == 0l) {
            return new ModelAndView("redirect:/joymeapp/client/menu/list?appkey=" + appKey + "&module=" + module);
        } else {
            return new ModelAndView("redirect:/joymeapp/client/menu/sublist?appkey=" + appKey + "&module=" + module + "&pid=" + pid);
        }
    }

    @RequestMapping(value = "/detail")
    public ModelAndView preModifyApp(@RequestParam(value = "menuid", required = false) Long menuId,
                                     @RequestParam(value = "pid", required = false) Long pid,
                                     @RequestParam(value = "appkey", required = false) String appKey,
                                     @RequestParam(value = "module", required = false) Integer module) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            JoymeAppMenu joymeAppMenu = JoymeAppConfigServiceSngl.get().getJoymeAppMenu(new QueryExpress().add(QueryCriterions.eq(JoymeAppMenuField.BUTTOM_MENU_ID, menuId)));
            if (joymeAppMenu == null) {
                return new ModelAndView("redirect:/joymeapp/client/menu/list?appkey=" + appKey + "&module=" + module);
            }
            mapMessage.put("joymeAppMenu", joymeAppMenu);

            AuthApp app = OAuthServiceSngl.get().getApp(joymeAppMenu.getAppkey());
            mapMessage.put("app", app);

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "ServiceException", e);
        }
        return new ModelAndView("/joymeapp/client/menu/menudetail", mapMessage);
    }

    @RequestMapping(value = "/sublist")
    public ModelAndView subList(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                                @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                                @RequestParam(value = "appkey", required = false) String appKey,
                                @RequestParam(value = "module", required = false) Integer module,
                                @RequestParam(value = "pid", required = false, defaultValue = "0") Long pid) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("appKey", appKey);
        mapMessage.put("module", module);
        mapMessage.put("pid", pid);

        try {
            int curPage = (pageStartIndex / pageSize) + 1;
            Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);

            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(JoymeAppMenuField.APPKEY, appKey));
            if (module != null) {
                queryExpress.add(QueryCriterions.eq(JoymeAppMenuField.MODULE_TYPE, module));
            }
            queryExpress.add(QueryCriterions.eq(JoymeAppMenuField.PID, pid));
            queryExpress.add(QuerySort.add(JoymeAppMenuField.DISPLAY_ORDER, QuerySortOrder.ASC));

            PageRows<JoymeAppMenu> joymeAppMenuPageRows = JoymeAppConfigServiceSngl.get().queryJoymeAppMenuByPage(queryExpress, pagination);
            mapMessage.put("list", joymeAppMenuPageRows.getRows());
            mapMessage.put("page", joymeAppMenuPageRows.getPage());
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("/joymeapp/client/menu/sublist", mapMessage);
    }


    @RequestMapping(value = "/sort/{sort}")
    public ModelAndView getAppMenuSort(@PathVariable(value = "sort") String sort,
                                       @RequestParam(value = "menuid", required = false) Long menuId,
                                       @RequestParam(value = "appkey", required = false) String appkey,
                                       @RequestParam(value = "pid", required = false) Long pid,
                                       @RequestParam(value = "module", required = false) Integer module) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();

        UpdateExpress updateExpress1 = new UpdateExpress();
        UpdateExpress updateExpress2 = new UpdateExpress();
        QueryExpress queryExpress = new QueryExpress();
        mapMessage.put("appkey", appkey);
        mapMessage.put("pid", pid);
        mapMessage.put("module", module);

        try {
            JoymeAppMenu appMenu = JoymeAppConfigServiceSngl.get().getJoymeAppMenu(new QueryExpress().add(QueryCriterions.eq(JoymeAppMenuField.BUTTOM_MENU_ID, menuId)));
            if (sort.equals("up")) {
                queryExpress.add(QueryCriterions.eq(JoymeAppMenuField.PID, pid))
                        .add(QueryCriterions.eq(JoymeAppMenuField.MODULE_TYPE, module))
                        .add(QueryCriterions.eq(JoymeAppMenuField.APPKEY, appkey))
                        .add(QueryCriterions.lt(JoymeAppMenuField.DISPLAY_ORDER, appMenu.getDisplay_order()))
                        .add(QuerySort.add(JoymeAppMenuField.DISPLAY_ORDER, QuerySortOrder.DESC));
            } else {
                queryExpress.add(QueryCriterions.eq(JoymeAppMenuField.PID, pid))
                        .add(QueryCriterions.eq(JoymeAppMenuField.MODULE_TYPE, module))
                        .add(QueryCriterions.eq(JoymeAppMenuField.APPKEY, appkey))
                        .add(QueryCriterions.gt(JoymeAppMenuField.DISPLAY_ORDER, appMenu.getDisplay_order()))
                        .add(QuerySort.add(JoymeAppMenuField.DISPLAY_ORDER, QuerySortOrder.ASC));
            }
            List<JoymeAppMenu> rows = JoymeAppConfigServiceSngl.get().queryJoymeAppMenu(queryExpress);

            if (!CollectionUtil.isEmpty(rows)) {
                updateExpress1.set(JoymeAppMenuField.DISPLAY_ORDER, appMenu.getDisplay_order());
                JoymeAppConfigServiceSngl.get().modifyJoymeAppMenu(rows.get(0).getMenuId(), appkey, pid, JoymeAppMenuModuleType.getByCode(module), updateExpress1);

                updateExpress2.set(JoymeAppMenuField.DISPLAY_ORDER, rows.get(0).getDisplay_order());
                JoymeAppConfigServiceSngl.get().modifyJoymeAppMenu(appMenu.getMenuId(), appkey, pid, JoymeAppMenuModuleType.getByCode(module), updateExpress2);
            }
        } catch (Exception e) {
            GAlerter.lab("AppMenuController occur Exception.e:", e);
        }
        return new ModelAndView("redirect:/joymeapp/client/menu/sublist?appkey=" + appkey + "&module=" + module + "&pid=" + pid);
    }
//
//    ////////////////////////////////////////////////////////////////////////////////////
//    @RequestMapping(value = "/tag/taglist")
//    public ModelAndView tagList(@RequestParam(value = "menuid") Long menuid) {
//        Map<String, Object> mapMessage = new HashMap<String, Object>();
//        mapMessage.put("menuid", menuid);
//        try {
//            List<JoymeAppMenuTag> tagList = JoymeAppConfigServiceSngl.get().queryJoymeAppTopMenuTagByMenuId(menuid);
//            mapMessage.put("taglist", tagList);
//        } catch (ServiceException e) {
//            GAlerter.lab(this.getClass().getName() + " occured error.e: ", e);
//            mapMessage = putErrorMessage(mapMessage, "system.error");
//        }
//
//        return new ModelAndView("/joymeapp/tab_appmenutaglist", mapMessage);
//    }
//
//    @RequestMapping(value = "/tag/createpage")
//    public ModelAndView createPage(@RequestParam(value = "menuid") Long menuid) {
//        Map<String, Object> mapMessage = new HashMap<String, Object>();
//        mapMessage.put("menuid", menuid);
//        return new ModelAndView("/joymeapp/tab_appmenutagcreatepage", mapMessage);
//    }
//
//    @RequestMapping(value = "/tag/create")
//    public ModelAndView create(@RequestParam(value = "menuid") Long menuid,
//                               @RequestParam(value = "tagname") String tagName) {
//        Map<String, Object> mapMessage = new HashMap<String, Object>();
//
//        JoymeAppMenuTag tag = new JoymeAppMenuTag();
//        tag.setCreateDate(new Date());
//        tag.setCreateId(getCurrentUser().getUserid());
//        tag.setCreateIp(getIp());
//        tag.setTagName(tagName);
//        tag.setTopMenuId(menuid);
//        tag.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
//
//        try {
//            JoymeAppConfigServiceSngl.get().createJoymeAppTopMenuTag(tag);
//        } catch (ServiceException e) {
//            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
//        }
//
//        return new ModelAndView("redirect:/joymeapp/menu/tag/taglist?menuid=" + menuid);
//    }
//
//    @RequestMapping(value = "/tag/modifypage")
//    public ModelAndView modifyPage(@RequestParam(value = "tagid") Long tagid) {
//        Map<String, Object> mapMessage = new HashMap<String, Object>();
//
//        mapMessage.put("removeStatusSet", removeStatusSet);
//        try {
//            JoymeAppMenuTag tag = JoymeAppConfigServiceSngl.get().getJoymeAppTopMenuTagBytagId(tagid);
//            if (tag != null) {
//                mapMessage.put("tag", tag);
//            }
//
//        } catch (ServiceException e) {
//            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
//        }
//
//        return new ModelAndView("/joymeapp/tab_appmenutagmodifypage", mapMessage);
//    }
//
//    @RequestMapping(value = "/tag/modify")
//    public ModelAndView modify(@RequestParam(value = "tagid") Long tagid,
//                               @RequestParam(value = "menuid") Long menuid,
//                               @RequestParam(value = "name") String tagname,
//                               @RequestParam(value = "removestatus", defaultValue = "n") String removeStatus,
//                               @RequestParam(value = "displayorder", defaultValue = "0") Integer displayOrder) {
//        Map<String, Object> mapMessage = new HashMap<String, Object>();
//
//        UpdateExpress updateExpress = new UpdateExpress();
//        updateExpress.set(JoymeAppTopMenuTagField.TAGNAME, tagname);
//        updateExpress.set(JoymeAppTopMenuTagField.REMOVESTATUS, ActStatus.getByCode(removeStatus).getCode());
//        updateExpress.set(JoymeAppTopMenuTagField.DISPLAYORDER, displayOrder);
//
//
//        try {
//            JoymeAppConfigServiceSngl.get().modifyJoymeAppTopMenuTag(updateExpress, tagid);
//        } catch (ServiceException e) {
//            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
//        }
//
//        return new ModelAndView("redirect:/joymeapp/menu/tag/taglist?menuid=" + menuid);
//    }
}