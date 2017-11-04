package com.enjoyf.webapps.tools.webpage.controller.wanba;

import com.enjoyf.platform.service.IntValidStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.ask.*;
import com.enjoyf.platform.service.joymeapp.JoymeAppServiceSngl;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTag;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTagAppType;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTagField;
import com.enjoyf.platform.service.joymeapp.anime.WanbaActivity;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by zhimingli on 2016/12/9 0009.
 */
@Controller
@RequestMapping("/wanba/release")
public class AskTimedReleaseController {

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                             @RequestParam(value = "title", required = false) String title,
                             @RequestParam(value = "type", required = false) String type,
                             @RequestParam(value = "validStatus", required = false, defaultValue = "valid") String validStatus,
                             HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
        QueryExpress queryExpress = new QueryExpress();
        try {
            queryExpress.add(QuerySort.add(AskTimedReleaseField.RELEASETIME, QuerySortOrder.ASC));
            if (!StringUtil.isEmpty(title)) {
                queryExpress.add(QueryCriterions.like(AskTimedReleaseField.TITLE, "%" + title + "%"));
            }
            if (!StringUtil.isEmpty(type)) {
                queryExpress.add(QueryCriterions.eq(AskTimedReleaseField.TIMERELSEASETYPE, TimeRelseaseType.getByCode(Integer.valueOf(type)).getCode()));
            }
            ValidStatus valid = ValidStatus.getByCode(validStatus);
            if (valid != null) {
                queryExpress.add(QueryCriterions.eq(AskTimedReleaseField.VALIDSTATUS, valid.getCode()));
            }

            mapMessage.put("validStatus", validStatus);

            PageRows<AskTimedRelease> askTimedReleasePageRows = AskServiceSngl.get().queryAskTimedRelease(queryExpress, pagination);
            mapMessage.put("list", askTimedReleasePageRows.getRows());
            mapMessage.put("page", askTimedReleasePageRows.getPage());
            mapMessage.put("type", type);
            mapMessage.put("title", title);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }
        return new ModelAndView("/wanba/release/releaselist", mapMessage);
    }


    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "timeid", required = false) String timeid,
                               @RequestParam(value = "validstatus", required = false) String validStatus,
                               HttpServletRequest request) {

        UpdateExpress updateExpress = new UpdateExpress();
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            updateExpress.set(AskTimedReleaseField.VALIDSTATUS, ValidStatus.getByCode(validStatus).getCode());
            AskServiceSngl.get().modifyAskTimedRelease(updateExpress, Long.valueOf(timeid));
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }
        return new ModelAndView("redirect:/wanba/release/list", map);
    }


    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyactpage(@RequestParam(value = "timeid", required = false) String timeid,
                                      HttpServletRequest request) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            AskTimedRelease release = AskServiceSngl.get().getAskTimedRelease(Long.valueOf(timeid));
            if (release != null) {
                if (release.getTimeRelseaseType().equals(TimeRelseaseType.ACTIVITY)) {
                    map.put("release", release);
                    AnimeTag animeTag = new Gson().fromJson(release.getExtStr(), AnimeTag.class);
                    map.put("animeTag", animeTag);
                    String ch_name = animeTag.getCh_name();
                    WanbaActivity wanbaActivity = WanbaActivity.toObject(ch_name);
                    map.put("wanbaActivity", wanbaActivity);
                    map.put("remove_status", ValidStatus.getAll());
                    return new ModelAndView("/wanba/release/modifypage_activity", map);
                }
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }
        return new ModelAndView("redirect:/wanba/release/list", map);
    }


    @RequestMapping(value = "/qmodifypage")
    public ModelAndView qmodifypage(HttpServletRequest request,
                                    @RequestParam(value = "timeid", required = false) String timeid) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(WanbaProfileClassifyField.CLASSIFY_TYPE, WanbaProfileClassifyType.WANBA_ASK_VIRTUAL.getCode()));
        queryExpress.add(QuerySort.add(WanbaProfileClassifyField.CLASSIFY_ID, QuerySortOrder.DESC));
        queryExpress.add(QueryCriterions.eq(WanbaProfileClassifyField.REMOVE_STATUS, IntValidStatus.VALID.getCode()));
        try {
            List<WanbaProfileClassify> list = AskServiceSngl.get().queryWanbaProfileClassifyList(queryExpress);
            Set<String> pid = new HashSet<String>();
            if (!CollectionUtil.isEmpty(list)) {
                for (WanbaProfileClassify classify : list) {
                    pid.add(classify.getProfileid());
                }
            }
            Map<String, Profile> profileMap = new HashMap<String, Profile>();
            if (!CollectionUtil.isEmpty(pid)) {
                profileMap.putAll(UserCenterServiceSngl.get().queryProfiles(pid));

            }

            //提问人
            mapMessage.put("profileMap", profileMap);
            mapMessage.put("list", list);


            //时间与积分对应关系
            mapMessage.put("questionconfigtype", QuestionConfigType.getAll());


            //标签
            QueryExpress qu = new QueryExpress();
            qu.add(QuerySort.add(AnimeTagField.DISPLAY_ORDER, QuerySortOrder.DESC));
            qu.add(QueryCriterions.eq(AnimeTagField.APP_TYPE, AnimeTagAppType.WANBA_ASK.getCode()));
            qu.add(QueryCriterions.eq(AnimeTagField.REMOVE_STATUS, ValidStatus.VALID.getCode()));
            List<AnimeTag> animeTagList = JoymeAppServiceSngl.get().queryAnimeTag(qu);

            List<AnimeTag> returTagList = new ArrayList<AnimeTag>();
            if (!CollectionUtil.isEmpty(animeTagList)) {
                for (AnimeTag tag : animeTagList) {
                    if (tag.getPicjson() != null && tag.getPicjson().getType().equals("0") && tag.getTag_id() > 0) {
                        returTagList.add(tag);
                    }
                }
            }
            mapMessage.put("animeTagList", returTagList);

            AskTimedRelease release = AskServiceSngl.get().getAskTimedRelease(Long.valueOf(timeid));
            mapMessage.put("release", release);
            mapMessage.put("questionRelease", new Gson().fromJson(release.getExtStr(), QuestionRelease.class));
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return new ModelAndView("/wanba/release/modify_timelimit", mapMessage);
    }


    @RequestMapping(value = "/modifyactivity")
    public ModelAndView modifyactivity(@RequestParam(value = "tag_name", required = false) String tag_name,
                                       @RequestParam(value = "tag_desc", required = false) String tag_desc,
                                       @RequestParam(value = "pic", required = false) String pic,
                                       @RequestParam(value = "corner", required = false) String corner,
                                       @RequestParam(value = "askwho", required = false) String askwho,
                                       @RequestParam(value = "apptype", required = false, defaultValue = "7") int apptype,
                                       @RequestParam(value = "validstatus", required = false) String validstatus,
                                       @RequestParam(value = "releasetime", required = false) String releasetime,
                                       @RequestParam(value = "timeid", required = false) Long timeid,
                                       HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            AnimeTag animeTag = new AnimeTag();
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
            animeTag.setCreate_date(new Date());

            animeTag.setRemove_status(ValidStatus.getByCode(validstatus));

            animeTag.setRemove_status(ValidStatus.VALID);

            //发布时间
            Date reletime = DateUtil.formatStringToDate(releasetime, DateUtil.DEFAULT_DATE_FORMAT2);
            animeTag.setDisplay_order(Integer.MAX_VALUE - (int) (reletime.getTime() / 1000));


            //定时发布
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(AskTimedReleaseField.TITLE, tag_name);
            updateExpress.set(AskTimedReleaseField.RELEASETIME, reletime);
            updateExpress.set(AskTimedReleaseField.EXTSTR, new Gson().toJson(animeTag));
            AskServiceSngl.get().modifyAskTimedRelease(updateExpress, timeid);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }
        return new ModelAndView("redirect:/wanba/release/list");
    }
}


