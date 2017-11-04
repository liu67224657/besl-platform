package com.enjoyf.webapps.tools.webpage.controller.wanba;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.ask.AskServiceSngl;
import com.enjoyf.platform.service.ask.AskTimedRelease;
import com.enjoyf.platform.service.ask.TimeRelseaseType;
import com.enjoyf.platform.service.joymeapp.JoymeAppServiceSngl;
import com.enjoyf.platform.service.joymeapp.anime.*;
import com.enjoyf.platform.service.misc.MiscServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zhimingli on 2016/9/26 0026.
 */
@Controller
@RequestMapping("/wanba/activity")
public class WanbaActivityController extends ToolsBaseController {

    private String DEFAULT_TYPE = String.valueOf(AnimeTagAppType.WANBA_ACTIVITY.getCode());


    private static String TOOLS_ACTIVITY_TOP = "_tools.activity.top_";

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                             @RequestParam(value = "apptype", required = false, defaultValue = "7") int apptype,
                             HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
        QueryExpress queryExpress = new QueryExpress();
        try {
            queryExpress.add(QuerySort.add(AnimeTagField.DISPLAY_ORDER, QuerySortOrder.ASC));
            queryExpress.add(QueryCriterions.eq(AnimeTagField.APP_TYPE, apptype));
            PageRows<AnimeTag> pageRows = JoymeAppServiceSngl.get().queryAnimeTagByPage(queryExpress, pagination);
            mapMessage.put("list", pageRows.getRows());
            mapMessage.put("page", pageRows.getPage());
            mapMessage.put("apptype", apptype);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/wanba/activity/list", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createPage(HttpServletRequest request,
                                   @RequestParam(value = "apptype", required = false, defaultValue = "7") int apptype) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("apptype", apptype);
        mapMessage.put("remove_status", ValidStatus.getAll());
        return new ModelAndView("/wanba/activity/createpage", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "tag_name", required = false) String tag_name,
                               @RequestParam(value = "tag_desc", required = false) String tag_desc,
                               @RequestParam(value = "pic", required = false) String pic,
                               @RequestParam(value = "corner", required = false) String corner,
                               @RequestParam(value = "askwho", required = false) String askwho,
                               @RequestParam(value = "apptype", required = false, defaultValue = "7") int apptype,
                               @RequestParam(value = "validstatus", required = false) String validstatus,
                               @RequestParam(value = "releasetime", required = false) String releasetime,
                               HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        AnimeTag animeTag = new AnimeTag();
        try {
            //定义的玩霸活动对象,组装对象
            WanbaActivity wanbaActivity = new WanbaActivity();
            wanbaActivity.setPic(pic);
            wanbaActivity.setCorner(corner);
            wanbaActivity.setAskwho(askwho);
            //像谁提问
            String[] guests = request.getParameterValues("guest");
            if (guests.length > 0) {
                List<String> guestList = new ArrayList<String>();
                for (int i = 0; i < guests.length; i++) {
                    guestList.add(guests[i]);
                }
                wanbaActivity.setGuestList(guestList);
            }
            animeTag.setCh_name(wanbaActivity.toJson());

            animeTag.setTag_name(tag_name);
            animeTag.setTag_desc(tag_desc);
            animeTag.setApp_type(AnimeTagAppType.getByCode(apptype));
            animeTag.setPlay_num(0L);
            animeTag.setFavorite_num(0L);
            animeTag.setCreate_user(this.getCurrentUser().getUserid());
            animeTag.setCreate_date(new Date());

            animeTag.setRemove_status(ValidStatus.getByCode(validstatus));
            animeTag.setDisplay_order(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));


            if (StringUtil.isEmpty(releasetime)) {
                animeTag = JoymeAppServiceSngl.get().createAnimeTag(animeTag);
            } else {

                //发布时间
                Date reletime = DateUtil.formatStringToDate(releasetime, DateUtil.DEFAULT_DATE_FORMAT2);

                animeTag.setDisplay_order(Integer.MAX_VALUE - (int) (reletime.getTime() / 1000));

                //定时发布
                AskTimedRelease release = new AskTimedRelease();
                release.setTimeRelseaseType(TimeRelseaseType.ACTIVITY);
                release.setTitle(tag_name);
                release.setCreateTime(new Date());
                release.setReleaseTime(reletime);
                release.setExtStr(new Gson().toJson(animeTag));

                AskServiceSngl.get().insertAskTimedRelease(release);
            }
            //写log
            writeToolsLog(LogOperType.TAG_ANIME_ADD, "玩霸活动新增标签,tagid:" + animeTag.getTag_id());
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/wanba/activity/list?apptype=" + apptype);
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyPage(@RequestParam(value = "tag_id") Long tag_id,
                                   @RequestParam(value = "apptype", required = false, defaultValue = "7") int apptype,
                                   HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(AnimeTagField.TAG_ID, tag_id));
        try {
            AnimeTag animeTag = JoymeAppServiceSngl.get().getAnimeTag(tag_id, queryExpress);
            mapMessage.put("animeTag", animeTag);
            String ch_name = animeTag.getCh_name();
            WanbaActivity wanbaActivity = WanbaActivity.toObject(ch_name);


            mapMessage.put("wanbaActivity", wanbaActivity);
            mapMessage.put("remove_status", ValidStatus.getAll());
            mapMessage.put("apptype", apptype);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/wanba/activity/modifypage", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "tag_id", required = false) Long tag_id,
                               @RequestParam(value = "tag_name", required = false) String tag_name,
                               @RequestParam(value = "tag_desc", required = false) String tag_desc,
                               @RequestParam(value = "pic", required = false) String pic,
                               @RequestParam(value = "corner", required = false) String corner,
                               @RequestParam(value = "remove_status", required = false) String remove_status,
                               @RequestParam(value = "apptype", required = false, defaultValue = "7") int apptype,
                               @RequestParam(value = "askwho", required = false) String askwho, HttpServletRequest request) {
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(AnimeTagField.TAG_ID, tag_id));
        UpdateExpress updateExpress = new UpdateExpress();
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            updateExpress.set(AnimeTagField.TAG_NAME, tag_name);
            updateExpress.set(AnimeTagField.TAG_DESC, tag_desc);
            updateExpress.set(AnimeTagField.REMOVE_STATUS, ValidStatus.getByCode(remove_status).getCode());

            updateExpress.set(AnimeTagField.UPDATE_DATE, new Date());

            //定义的玩霸活动对象,组装对象
            WanbaActivity wanbaActivity = new WanbaActivity();
            wanbaActivity.setPic(pic);
            wanbaActivity.setAskwho(askwho);
            wanbaActivity.setCorner(corner);
            //像谁提问
            String[] guests = request.getParameterValues("guest");
            if (guests.length > 0) {
                List<String> guestList = new ArrayList<String>();
                for (int i = 0; i < guests.length; i++) {
                    guestList.add(guests[i]);
                }
                wanbaActivity.setGuestList(guestList);
            }
            updateExpress.set(AnimeTagField.CH_NAME, wanbaActivity.toJson());


            JoymeAppServiceSngl.get().modifyAnimeTag(tag_id, queryExpress, updateExpress);


            // writeToolsLog(LogOperType.TAG_ANIME_UPDATE, "玩霸活动修改标签,tagid:" + tag_id);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }
        return new ModelAndView("redirect:/wanba/activity/list?apptype=" + apptype, map);
    }


    @RequestMapping(value = "/sort")
    public ModelAndView sort(@RequestParam(value = "desc", required = true) String desc,
                             @RequestParam(value = "tag_id", required = true) Long tag_id) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();

        UpdateExpress updateExpress1 = new UpdateExpress();
        UpdateExpress updateExpress2 = new UpdateExpress();
        QueryExpress queryExpress = new QueryExpress();
        try {
            //第一个
            AnimeTag animeTag = JoymeAppServiceSngl.get().getAnimeTag(tag_id, new QueryExpress().add(QueryCriterions.eq(AnimeTagField.TAG_ID, tag_id)));
            if (desc.equals("up")) {
                queryExpress.add(QueryCriterions.gt(AnimeTagField.DISPLAY_ORDER, animeTag.getDisplay_order()))
                        .add(QuerySort.add(AnimeTagField.DISPLAY_ORDER, QuerySortOrder.ASC))
                        .add(QueryCriterions.eq(AnimeTagField.APP_TYPE, AnimeTagAppType.WANBA_ACTIVITY.getCode()));
            } else {
                queryExpress.add(QueryCriterions.lt(AnimeTagField.DISPLAY_ORDER, animeTag.getDisplay_order()))
                        .add(QuerySort.add(AnimeTagField.DISPLAY_ORDER, QuerySortOrder.DESC))
                        .add(QueryCriterions.eq(AnimeTagField.APP_TYPE, AnimeTagAppType.WANBA_ACTIVITY.getCode()));
            }

            //第二个
            PageRows<AnimeTag> appRows = JoymeAppServiceSngl.get().queryAnimeTagByPage(queryExpress, new Pagination(1, 1, 1));

            if (appRows != null && !CollectionUtil.isEmpty(appRows.getRows())) {

                updateExpress1.set(AnimeTagField.DISPLAY_ORDER, animeTag.getDisplay_order());
                JoymeAppServiceSngl.get().modifyAnimeTag(appRows.getRows().get(0).getTag_id(), new QueryExpress().add(QueryCriterions.eq(AnimeTagField.TAG_ID, appRows.getRows().get(0).getTag_id())), updateExpress1);

                updateExpress2.set(AnimeTagField.DISPLAY_ORDER, appRows.getRows().get(0).getDisplay_order());
                JoymeAppServiceSngl.get().modifyAnimeTag(animeTag.getTag_id(), new QueryExpress().add(QueryCriterions.eq(AnimeTagField.TAG_ID, animeTag.getTag_id())), updateExpress2);
            }

            writeToolsLog(LogOperType.TAG_ANIME_SORT, "玩霸标签排序活动,tagid:" + tag_id + ",orderby:" + desc);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView("redirect:/wanba/activity/list", mapMessage);
    }


    //置顶
    @RequestMapping("/top")
    @ResponseBody
    public String questionDelte(@RequestParam(value = "tag_id", required = false) Long tag_id,
                                @RequestParam(value = "type", required = false) int type) {
        try {
            //type 1置顶 2取消置顶
            if (type == 1) {
                QueryExpress queryExpress = new QueryExpress();
                queryExpress.add(QueryCriterions.eq(AnimeTagField.TAG_ID, tag_id));

                String value = MiscServiceSngl.get().getRedisMiscValue(TOOLS_ACTIVITY_TOP);

                int display = StringUtil.isEmpty(value) ? 0 : Integer.valueOf(value);
                UpdateExpress updateExpress = new UpdateExpress();
                updateExpress.set(AnimeTagField.DISPLAY_ORDER, display - 1);

                JoymeAppServiceSngl.get().modifyAnimeTag(tag_id, queryExpress, updateExpress);

                MiscServiceSngl.get().saveRedisMiscValue(TOOLS_ACTIVITY_TOP, String.valueOf((display - 1)), -1);
            } else {
                QueryExpress queryExpress = new QueryExpress();
                queryExpress.add(QueryCriterions.eq(AnimeTagField.TAG_ID, tag_id));
                int display = Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000);
                UpdateExpress updateExpress = new UpdateExpress();
                updateExpress.set(AnimeTagField.DISPLAY_ORDER, display);

                JoymeAppServiceSngl.get().modifyAnimeTag(tag_id, queryExpress, updateExpress);
            }


        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e: ", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }

        return ResultCodeConstants.SUCCESS.getJsonString();
    }

}
