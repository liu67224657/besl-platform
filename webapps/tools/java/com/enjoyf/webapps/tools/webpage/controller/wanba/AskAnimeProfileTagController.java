package com.enjoyf.webapps.tools.webpage.controller.wanba;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.ask.*;
import com.enjoyf.platform.service.joymeapp.JoymeAppServiceSngl;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTag;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTagAppType;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTagField;
import com.enjoyf.platform.service.joymeapp.anime.GameClientTagType;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.service.usercenter.*;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-10-27
 * Time: 下午2:08
 * To change this template use File | Settings | File Templates.
 * 达人标签
 */
@Controller
@RequestMapping(value = "/wanba/profiletag")
public class AskAnimeProfileTagController extends ToolsBaseController {

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "20") int pageSize,
                             HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
        QueryExpress queryExpress = new QueryExpress();
        try {
            queryExpress.add(QuerySort.add(AnimeTagField.DISPLAY_ORDER, QuerySortOrder.DESC));
            queryExpress.add(QueryCriterions.eq(AnimeTagField.APP_TYPE, AnimeTagAppType.WANBA_VERIFYPROFILE.getCode()));
            PageRows<AnimeTag> pageRows = JoymeAppServiceSngl.get().queryAnimeTagByPage(queryExpress, pagination);
            mapMessage.put("list", pageRows.getRows());
            mapMessage.put("page", pageRows.getPage());
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/wanba/profiletag/taglist", mapMessage);
        }
        return new ModelAndView("/wanba/profiletag/taglist", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createPage(HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("tagType", GameClientTagType.getAll());
        return new ModelAndView("/wanba/profiletag/createpage", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "tag_name", required = false) String tag_name,
                               HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        AnimeTag animeTag = new AnimeTag();
        try {
            animeTag.setTag_name(tag_name);
            animeTag.setApp_type(AnimeTagAppType.WANBA_VERIFYPROFILE);
            animeTag.setPlay_num(0L);
            animeTag.setFavorite_num(0L);
            animeTag.setCreate_user(this.getCurrentUser().getUserid());
            animeTag.setCreate_date(new Date());
            animeTag.setDisplay_order(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
            animeTag = JoymeAppServiceSngl.get().createAnimeTag(animeTag);

            //写log
            writeToolsLog(LogOperType.TAG_ANIME_ADD, "玩霸问答达人新增标签,tagid:" + animeTag.getTag_id());
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/wanba/profiletag/taglist", mapMessage);
        }
        return new ModelAndView("redirect:/wanba/profiletag/list");
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyPage(@RequestParam(value = "tag_id") Long tag_id,
                                   HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(AnimeTagField.TAG_ID, tag_id));
        try {
            AnimeTag animeTag = JoymeAppServiceSngl.get().getAnimeTag(tag_id, queryExpress);
            mapMessage.put("animeTag", animeTag);
            mapMessage.put("remove_status", ValidStatus.getAll());
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/wanba/profiletag/taglist", mapMessage);
        }
        return new ModelAndView("/wanba/profiletag/modifypage", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "tag_id", required = false) Long tag_id,
                               @RequestParam(value = "tag_name", required = false) String tag_name,
                               @RequestParam(value = "remove_status", required = false) String remove_status,
                               @RequestParam(value = "url", required = false) String url, HttpServletRequest request) {
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(AnimeTagField.TAG_ID, tag_id));
        UpdateExpress updateExpress = new UpdateExpress();
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            updateExpress.set(AnimeTagField.TAG_NAME, tag_name);
            updateExpress.set(AnimeTagField.REMOVE_STATUS, ValidStatus.getByCode(remove_status).getCode());

            updateExpress.set(AnimeTagField.UPDATE_DATE, new Date());

            JoymeAppServiceSngl.get().modifyAnimeTag(tag_id, queryExpress, updateExpress);


            writeToolsLog(LogOperType.TAG_ANIME_UPDATE, "玩霸问答达人修改标签,tagid:" + tag_id);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return new ModelAndView("/wanba/profiletag/taglist", map);
        }
        return new ModelAndView("redirect:/wanba/profiletag/list", map);
    }


    //todo removeItem


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
                        .add(QueryCriterions.eq(AnimeTagField.APP_TYPE, AnimeTagAppType.WANBA_VERIFYPROFILE.getCode()));
            } else {
                queryExpress.add(QueryCriterions.lt(AnimeTagField.DISPLAY_ORDER, animeTag.getDisplay_order()))
                        .add(QuerySort.add(AnimeTagField.DISPLAY_ORDER, QuerySortOrder.DESC))
                        .add(QueryCriterions.eq(AnimeTagField.APP_TYPE, AnimeTagAppType.WANBA_VERIFYPROFILE.getCode()));
            }

            //第二个
            PageRows<AnimeTag> appRows = JoymeAppServiceSngl.get().queryAnimeTagByPage(queryExpress, new Pagination(1, 1, 1));

            if (appRows != null && !CollectionUtil.isEmpty(appRows.getRows())) {

                updateExpress1.set(AnimeTagField.DISPLAY_ORDER, animeTag.getDisplay_order());
                JoymeAppServiceSngl.get().modifyAnimeTag(appRows.getRows().get(0).getTag_id(), new QueryExpress().add(QueryCriterions.eq(AnimeTagField.TAG_ID, appRows.getRows().get(0).getTag_id())), updateExpress1);

                updateExpress2.set(AnimeTagField.DISPLAY_ORDER, appRows.getRows().get(0).getDisplay_order());
                JoymeAppServiceSngl.get().modifyAnimeTag(animeTag.getTag_id(), new QueryExpress().add(QueryCriterions.eq(AnimeTagField.TAG_ID, animeTag.getTag_id())), updateExpress2);
            }

            writeToolsLog(LogOperType.TAG_ANIME_SORT, "玩霸问答达人标签排序,tagid:" + tag_id + ",orderby:" + desc);


        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }
        return new ModelAndView("redirect:/wanba/profiletag/list", mapMessage);
    }

    private static final long ALL_TAG_ID = -20000;

    @RequestMapping(value = "/querybytagid")
    public ModelAndView querybyTagId(@RequestParam(value = "tagid", required = true) Long tagId,
                                     @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                                     @RequestParam(value = "maxPageItems", required = false, defaultValue = "20") int pageSize) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
        try {
            AnimeTag animeTag = JoymeAppServiceSngl.get().getAnimeTag(tagId,
                    new QueryExpress().add(QueryCriterions.eq(AnimeTagField.TAG_ID, tagId)));
            mapMessage.put("animeTag", animeTag);

            if (tagId == ALL_TAG_ID) {
                tagId = -1l;
            }
            PageRows<VerifyProfile> wanbaProfilePageRows = UserCenterServiceSngl.get().queryVerifyProfileByTag(tagId, pagination);
            Set<Long> verifySet = new HashSet<Long>();
            Set<String> profileIds = new HashSet<String>();
            if (wanbaProfilePageRows != null && !CollectionUtil.isEmpty(wanbaProfilePageRows.getRows())) {
                for (VerifyProfile wanbaProfile : wanbaProfilePageRows.getRows()) {
                    verifySet.add(wanbaProfile.getVerifyType());
                    profileIds.add(wanbaProfile.getProfileId());
                }
                if (!CollectionUtil.isEmpty(verifySet)) {
                    List<Verify> wanbaVerifyList = UserCenterServiceSngl.get().queryVerify(new QueryExpress().add(QueryCriterions.in(VerifyField.VERIFY_ID, verifySet.toArray())));
                    mapMessage.put("wanbaVerifyList", wanbaVerifyList);
                }
                if (!CollectionUtil.isEmpty(profileIds)) {
                    Map<String, Profile> profileMap = UserCenterServiceSngl.get().queryProfiles(profileIds);
                    mapMessage.put("profileMap", profileMap);
                }
            }
            mapMessage.put("list", wanbaProfilePageRows.getRows());
            mapMessage.put("page", wanbaProfilePageRows.getPage());
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }

        return new ModelAndView("/wanba/profiletag/wanbaprofilelist", mapMessage);
    }

    @RequestMapping(value = "/deleteprofile")
    public ModelAndView deleteProfile(@RequestParam(value = "tagid", required = false) Long tagId,
                                      @RequestParam(value = "pid", required = false) String profileId) {

        try {
            if (tagId == ALL_TAG_ID) {
                tagId = -1l;
            }

            UserCenterServiceSngl.get().deleteVerifyProfile(tagId, profileId);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }
        return new ModelAndView("redirect:/wanba/profiletag/querybytagid?tagid=" + tagId);
    }

    @RequestMapping(value = "/addprofilepage")
    public ModelAndView addProfilepage(@RequestParam(value = "tagid", required = false) Long tagId,
                                       @RequestParam(value = "error", required = false) String error) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("tagid", tagId);
        if (!StringUtil.isEmpty(error)) {
            mapMessage.put("errorMsg", "profileId错误");
        }
        return new ModelAndView("/wanba/profiletag/addprofilepage", mapMessage);
    }

    @RequestMapping(value = "/addprofile")
    public ModelAndView addProfile(@RequestParam(value = "tagid", required = false) Long tagId,
                                   @RequestParam(value = "profileid", required = false) String profileId) {

        try {
            if (tagId == ALL_TAG_ID) {
                tagId = -1l;
            }


            VerifyProfile wanbaProfile = UserCenterServiceSngl.get().getVerifyProfileById(profileId);
            if (wanbaProfile == null) {
                return new ModelAndView("redirect:/wanba/profiletag/addprofilepage?tagid=" + tagId + "&error=profile.not.find");
            }
            WanbaProfileSum sum=AskServiceSngl.get().getWanProfileSum(wanbaProfile.getProfileId());
            if(sum==null){
                sum=new WanbaProfileSum();
                sum.setProfileId(wanbaProfile.getProfileId());
                sum.setAnswerSum(0);
                sum.setAwardPoint(0);
                sum.setFavoriteSum(0);
                sum.setQuestionFollowSum(0);
                AskServiceSngl.get().insertWanbaProfileSum(sum);
            }
            UserCenterServiceSngl.get().verifyProfile(wanbaProfile, tagId);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }

        return new ModelAndView("redirect:/wanba/profiletag/querybytagid?tagid=" + tagId);
    }

    @RequestMapping(value = "/profilesort")
    public ModelAndView profileSort(@RequestParam(value = "sort", required = false) Integer sort,
                                    @RequestParam(value = "profileid", required = false) String profileId,
                                    @RequestParam(value = "tagid", required = false) Long tagId) {

        try {
            if (tagId == ALL_TAG_ID) {
                tagId = -1l;
            }
            UserCenterServiceSngl.get().sortVerifyProfileByTagId(tagId, sort, profileId);
        } catch (ServiceException e) {
            e.printStackTrace();
        }


        return new ModelAndView("redirect:/wanba/profiletag/querybytagid?tagid=" + tagId);
    }
}
