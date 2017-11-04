package com.enjoyf.webapps.tools.webpage.controller.wanba;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.ask.WanbaJt;
import com.enjoyf.platform.service.ask.WanbaMenuDomain;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTag;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTagAppType;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTagField;
import com.enjoyf.platform.service.joymeappconfig.JoymeAppConfigServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.shorturl.ShortUrl;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.service.tools.ToolsLog;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.webapps.tools.weblogic.joymeapp.JoymeAppWebLogic;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by zhimingli on 2016/9/13 0013.
 */
@Controller
@RequestMapping(value = "/wanba/menu")
public class AskTopMenuController extends ToolsBaseController {


    //玩霸3.0appkey
    private String APPKEY = "3iiv7VWfx84pmHgCUqRwun";

    private static List<Integer> platformList = new ArrayList<Integer>();

    static {
        platformList.add(AppPlatform.ANDROID.getCode());
        platformList.add(AppPlatform.IOS.getCode());
    }

    @Resource(name = "jomyeAppWebLogic")
    private JoymeAppWebLogic joymeAppWebLogic;

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "channelid", required = false) String channelId,
                             @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                             @RequestParam(value = "validstatus", required = false) String validstatus) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("ClientPlatformCollection", platformList);

        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QuerySort.add(ActivityTopMenuField.DISPLAY_ORDER, QuerySortOrder.ASC));
        queryExpress.add(QueryCriterions.eq(ActivityTopMenuField.APP_KEY, APPKEY));
        queryExpress.add(QueryCriterions.in(ActivityTopMenuField.PLATFORM, platformList.toArray()));
        queryExpress.add(QueryCriterions.eq(ActivityTopMenuField.CATEGORY, AppTopMenuCategory.WANBA_ASK.getCode()));


        if (StringUtil.isEmpty(validstatus)) {
            queryExpress.add(QueryCriterions.eq(ActivityTopMenuField.VALID_STATUS, ValidStatus.VALID.getCode()));
            validstatus = ValidStatus.VALID.getCode();
        } else {
            ValidStatus validStatus = ValidStatus.getByCode(validstatus);
            if (validStatus != null) {
                queryExpress.add(QueryCriterions.eq(ActivityTopMenuField.VALID_STATUS, validStatus.getCode()));
            }
        }
        mapMessage.put("validstatus", validstatus);

        if (!StringUtil.isEmpty(channelId) && !channelId.equals(String.valueOf(WanbaMenuDomain.HOT.getCode()))) {
            queryExpress.add(QueryCriterions.eq(ActivityTopMenuField.CHANNEL_ID, Long.valueOf(channelId)));
        }

//        else {
//            queryExpress.add(QueryCriterions.eq(ActivityTopMenuField.CHANNEL_ID, Long.valueOf(WanbaMenuDomain.HOT.getCode())));
//        }


        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
        try {
            PageRows<ActivityTopMenu> activityTopMenuPageRows = JoymeAppConfigServiceSngl.get().queryActivityTopMenuPage(queryExpress, pagination);

            mapMessage.put("list", activityTopMenuPageRows.getRows());
            mapMessage.put("page", activityTopMenuPageRows.getPage());

            mapMessage.put("animeTagList", getAnimetagList());
            mapMessage.put("channelid", channelId);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }


        return new ModelAndView("/wanba/menu/topmenulist", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createpage(HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        String wanbamenudomain = request.getParameter("wanbamenudomain");
        mapMessage.put("wanbamenudomain", wanbamenudomain);
        mapMessage.put("ClientPlatformCollection", platformList);
        mapMessage.put("WanbaJt", WanbaJt.getToolsAll());
        return new ModelAndView("/wanba/menu/createtopmenu", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "menuname", required = false) String menuName,
                               @RequestParam(value = "menudesc", required = false) String menudesc,
                               @RequestParam(value = "platform", required = false) Integer platform,
                               //链接
                               @RequestParam(value = "linkurl", required = false) String linkUrl,

                               //跳转类型
                               @RequestParam(value = "menutype", required = false) Integer menutype,

                               //图片链接
                               @RequestParam(value = "picurl", required = false) String picUrl,

                               //属于哪个游戏标签
                               @RequestParam(value = "channelid", required = false) Long channelId,

                               //图片形式:大图、小图
                               @RequestParam(value = "redirectType", required = false) Integer redirectType,
                               //是否采用短链
                               @RequestParam(value = "shortlinkurl", required = false, defaultValue = "0") Integer shortlinkurl) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {

            //双端
            if (platform == -1) {
                ActivityTopMenu androdidMenu = new ActivityTopMenu();
                androdidMenu.setMenuName(menuName);
                androdidMenu.setMenuDesc(menudesc);
                if (shortlinkurl == 0) {
                    androdidMenu.setLinkUrl(linkUrl);
                } else {
                    androdidMenu.setLinkUrl(ShortUrlUtils.getSinaURL(linkUrl));
                }

                androdidMenu.setRedirectType(redirectType);
                androdidMenu.setPicUrl(picUrl);
                androdidMenu.setChannelId(channelId);

                androdidMenu.setMenuType(menutype);

                androdidMenu.setAppKey(APPKEY);
                androdidMenu.setCategory(AppTopMenuCategory.WANBA_ASK);
                androdidMenu.setValidStatus(ValidStatus.INVALID);
                androdidMenu.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
                androdidMenu.setCreateUserId(getCurrentUser().getUserid());
                androdidMenu.setCreateDate(new Date());
                androdidMenu.setCreateIp(getIp());
                androdidMenu.setPlatform(AppPlatform.ANDROID.getCode());
                JoymeAppConfigServiceSngl.get().createActivityTopMenu(androdidMenu);

                ActivityTopMenu iosMenu = androdidMenu;
                iosMenu.setPlatform(AppPlatform.IOS.getCode());
                iosMenu.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000) - 1);
                androdidMenu.setCreateDate(new Date());
                JoymeAppConfigServiceSngl.get().createActivityTopMenu(iosMenu);

            } else {
                ActivityTopMenu activityTopMenu = new ActivityTopMenu();
                activityTopMenu.setMenuName(menuName);
                activityTopMenu.setMenuDesc(menudesc);
                if (shortlinkurl == 0) {
                    activityTopMenu.setLinkUrl(linkUrl);
                } else {
                    activityTopMenu.setLinkUrl(ShortUrlUtils.getSinaURL(linkUrl));
                }
                activityTopMenu.setRedirectType(redirectType);
                activityTopMenu.setPicUrl(picUrl);

                activityTopMenu.setPlatform(Integer.valueOf(platform));
                activityTopMenu.setChannelId(channelId);

                activityTopMenu.setMenuType(menutype);

                activityTopMenu.setAppKey(APPKEY);
                activityTopMenu.setCategory(AppTopMenuCategory.WANBA_ASK);
                activityTopMenu.setValidStatus(ValidStatus.INVALID);
                activityTopMenu.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
                activityTopMenu.setCreateUserId(getCurrentUser().getUserid());
                activityTopMenu.setCreateDate(new Date());
                activityTopMenu.setCreateIp(getIp());

                JoymeAppConfigServiceSngl.get().createActivityTopMenu(activityTopMenu);
            }


        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "ServiceException", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/wanba/menu/list?channelid=" + channelId, mapMessage);
    }


    @RequestMapping(value = "/modifypage")
    public ModelAndView modifypage(@RequestParam(value = "activitytopmenuid", required = true) Long activityTopMenuId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("ClientPlatformCollection", platformList);
        mapMessage.put("WanbaJt", WanbaJt.getToolsAll());
        try {
            ActivityTopMenu activityTopMenu = JoymeAppConfigServiceSngl.get().getActivityTopMenuById(activityTopMenuId);
            mapMessage.put("activityTopMenu", activityTopMenu);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "ServiceException", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/wanba/menu/modifytopmenu", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "activitytopmenuid", required = true) Long activityTopMenuId,
                               @RequestParam(value = "menuname", required = false) String menuName,
                               @RequestParam(value = "menudesc", required = false) String menudesc,
                               @RequestParam(value = "platform", required = false) Integer platform,
                               @RequestParam(value = "linkurl", required = false) String linkUrl,
                               @RequestParam(value = "menutype", required = false) Integer menutype,
                               @RequestParam(value = "channelid", required = false) Long channelId,
                               @RequestParam(value = "picurl", required = false) String picUrl,
                               @RequestParam(value = "redirectType", required = false) Integer redirectType,
                               @RequestParam(value = "validStatus", required = false) String validStatus,
                               //是否采用短链
                               @RequestParam(value = "shortlinkurl", required = false, defaultValue = "0") Integer shortlinkurl) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        Date date = new Date();
        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(ActivityTopMenuField.MENU_NAME, menuName);
        updateExpress.set(ActivityTopMenuField.MENU_DESC, menudesc);

        if (shortlinkurl == 0) {
            updateExpress.set(ActivityTopMenuField.LINK_URL, linkUrl);
        } else {
            updateExpress.set(ActivityTopMenuField.LINK_URL, ShortUrlUtils.getSinaURL(linkUrl));
        }

        updateExpress.set(ActivityTopMenuField.REDIRECT_TYPE, redirectType);
        updateExpress.set(ActivityTopMenuField.MENU_TYPE, menutype);
        updateExpress.set(ActivityTopMenuField.PIC_URL, picUrl);
        updateExpress.set(ActivityTopMenuField.PLATFORM, platform);

        updateExpress.set(ActivityTopMenuField.LAST_MODIFY_USERID, getCurrentUser().getUserid());
        updateExpress.set(ActivityTopMenuField.LAST_MODIFY_DATE, date);
        updateExpress.set(ActivityTopMenuField.LAST_MODIFY_IP, getIp());
        updateExpress.set(ActivityTopMenuField.VALID_STATUS, ValidStatus.getByCode(validStatus).getCode());

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
            return new ModelAndView("redirect:/wanba/menu/modifypage?activitytopmenuid=" + activityTopMenuId);
        }
        return new ModelAndView("redirect:/wanba/menu/list?channelid=" + channelId);
    }

    @RequestMapping(value = "/sort")
    public ModelAndView sort(@RequestParam(value = "sort") String sort,
                             @RequestParam(value = "activitytopmenuid", required = false) Long activityTopMenuId,
                             @RequestParam(value = "channelid", required = false) Long channelid) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            ActivityTopMenu activityTopMenu = JoymeAppConfigServiceSngl.get().getActivityTopMenuById(activityTopMenuId);
            if (activityTopMenu == null) {
                return new ModelAndView("redirect:/wanba/menu/list");
            }
            UpdateExpress updateExpress1 = new UpdateExpress();
            UpdateExpress updateExpress2 = new UpdateExpress();
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(ActivityTopMenuField.CATEGORY, AppTopMenuCategory.WANBA_ASK.getCode()));
            queryExpress.add(QueryCriterions.eq(ActivityTopMenuField.APP_KEY, APPKEY));
            queryExpress.add(QueryCriterions.eq(ActivityTopMenuField.CHANNEL_ID, channelid));
            //step1 get goodsId,get display order

            if (sort.equals("up")) {
                queryExpress.add(QueryCriterions.lt(ActivityTopMenuField.DISPLAY_ORDER, activityTopMenu.getDisplayOrder()));
                queryExpress.add(QuerySort.add(ActivityTopMenuField.DISPLAY_ORDER, QuerySortOrder.DESC));
            } else {
                queryExpress.add(QueryCriterions.gt(ActivityTopMenuField.DISPLAY_ORDER, activityTopMenu.getDisplayOrder()));
                queryExpress.add(QuerySort.add(ActivityTopMenuField.DISPLAY_ORDER, QuerySortOrder.ASC));
            }

            List<ActivityTopMenu> list = JoymeAppConfigServiceSngl.get().queryActivityTopMenu(queryExpress);
            if (!CollectionUtil.isEmpty(list)) {
                updateExpress1.set(ActivityTopMenuField.DISPLAY_ORDER, activityTopMenu.getDisplayOrder());
                JoymeAppConfigServiceSngl.get().modifyActivityTopMenu(new QueryExpress().add(QueryCriterions.eq(ActivityTopMenuField.ACTIVITY_MENU_ID, list.get(0).getActivityTopMenuId())), updateExpress1);

                updateExpress2.set(ActivityTopMenuField.DISPLAY_ORDER, list.get(0).getDisplayOrder());
                JoymeAppConfigServiceSngl.get().modifyActivityTopMenu(new QueryExpress().add(QueryCriterions.eq(ActivityTopMenuField.ACTIVITY_MENU_ID, activityTopMenu.getActivityTopMenuId())), updateExpress2);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + "ServiceException", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/wanba/menu/list?channelid=" + channelid, mapMessage);
    }


    @RequestMapping(value = "/remove")
    public ModelAndView remove(@RequestParam(value = "activitytopmenuid", required = false) Long activityTopMenuId,
                               @RequestParam(value = "validStatus", required = false) String validStatus,
                               @RequestParam(value = "channelid", required = false) String channelId,
                               @RequestParam(value = "validstatus", required = false) String validstatus) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(ActivityTopMenuField.ACTIVITY_MENU_ID, activityTopMenuId));
        try {
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(ActivityTopMenuField.VALID_STATUS, ValidStatus.getByCode(validStatus).getCode());

            JoymeAppConfigServiceSngl.get().modifyActivityTopMenu(queryExpress, updateExpress);

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "ServiceException", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("redirect:/wanba/menu/modifypage?activitytopmenuid=" + activityTopMenuId);
        }
        return new ModelAndView("redirect:/wanba/menu/list?channelid=" + channelId + "&validstatus=" + validstatus);
    }


    private List<AnimeTag> getAnimetagList() throws ServiceException {
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QuerySort.add(AnimeTagField.DISPLAY_ORDER, QuerySortOrder.DESC));
        queryExpress.add(QueryCriterions.eq(AnimeTagField.APP_TYPE, AnimeTagAppType.WANBA_ASK.getCode()));
        queryExpress.add(QueryCriterions.eq(AnimeTagField.REMOVE_STATUS, ValidStatus.VALID.getCode()));
        List<AnimeTag> animeTagList = JoymeAppServiceSngl.get().queryAnimeTag(queryExpress);
        return animeTagList;
    }

}
