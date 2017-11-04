package com.enjoyf.webapps.tools.webpage.controller.joymeapp.anime;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.joymeapp.JoymeAppServiceSngl;
import com.enjoyf.platform.service.joymeapp.anime.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.LogOperType;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-10-27
 * Time: 下午2:08
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/joymeapp/anime/tag")
public class AnimeTagController extends ToolsBaseController {

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,

                             @RequestParam(value = "parent_tag_name", required = false) String parent_tag_name,
                             @RequestParam(value = "parent_tag_id", required = false) String parent_tag_id,

                             @RequestParam(value = "removestaus", required = false) String removestaus,
                             @RequestParam(value = "search_tagname", required = false) String search_tagname,
                             @RequestParam(value = "search_parent_tag_id", required = false) String search_parent_tag_id,
                             @RequestParam(value = "search_type", required = false) String search_type,
                             HttpServletRequest request
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
        QueryExpress queryExpress = new QueryExpress();
        try {
            String chapter = request.getParameter("chapter");
            queryExpress.add(QuerySort.add(AnimeTagField.DISPLAY_ORDER, QuerySortOrder.DESC));
            queryExpress.add(QueryCriterions.eq(AnimeTagField.APP_TYPE, AnimeTagAppType.ANIME.getCode()));
            if (!StringUtil.isEmpty(removestaus)) {
                queryExpress.add(QueryCriterions.eq(AnimeTagField.REMOVE_STATUS, ValidStatus.getByCode(removestaus).getCode()));
            }


            if (StringUtil.isEmpty(chapter)) {
                queryExpress.add(QueryCriterions.eq(AnimeTagField.PARENT_TAG_ID, 0L));
                if (!StringUtil.isEmpty(search_tagname)) {
                    queryExpress.add(QueryCriterions.like(AnimeTagField.TAG_NAME, "%" + search_tagname + "%"));
                }

                if (!StringUtil.isEmpty(search_type)) {
                    queryExpress.add(QueryCriterions.eq(AnimeTagField.SEARCH_TYPE, Integer.valueOf(search_type)));
                }

            } else {
                if (!StringUtil.isEmpty(search_tagname)) {
                    queryExpress.add(QueryCriterions.like(AnimeTagField.TAG_NAME, "%" + search_tagname + "%"));
                }
                mapMessage.put("parent_tag_id", parent_tag_id);
                mapMessage.put("parent_tag_name", parent_tag_name);
                queryExpress.add(QueryCriterions.eq(AnimeTagField.PARENT_TAG_ID, Long.valueOf(parent_tag_id)));
            }

            PageRows<AnimeTag> pageRows = JoymeAppServiceSngl.get().queryAnimeTagByPage(queryExpress, pagination);
            mapMessage.put("list", pageRows.getRows());
            mapMessage.put("page", pageRows.getPage());


            if (!StringUtil.isEmpty(chapter) && chapter.equals("chapter")) {
                return new ModelAndView("/joymeapp/anime/tag/taglist_chapter", mapMessage);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/joymeapp/anime/tag/taglist", mapMessage);
        }

        mapMessage.put("animeTagSearchType", AnimeTagSearchType.getAll());
        return new ModelAndView("/joymeapp/anime/tag/taglist", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createPage(HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("animeTagType", AnimeTagType.getAll());
        mapMessage.put("animeTagModel", AnimeTagModel.getAll());
        mapMessage.put("animeTagSearchType", AnimeTagSearchType.getAll());
        String chapter = request.getParameter("chapter");
        if (!StringUtil.isEmpty(chapter) && chapter.equals("chapter")) {
            mapMessage.put("parent_tag_id", request.getParameter("parent_tag_id"));
            mapMessage.put("parent_tag_name", request.getParameter("parent_tag_name"));
            return new ModelAndView("/joymeapp/anime/tag/createpage_chapter", mapMessage);
        }
        return new ModelAndView("/joymeapp/anime/tag/createpage", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "tag_name", required = false) String tag_name,
                               @RequestParam(value = "tag_desc", required = false) String tag_desc,
                               @RequestParam(value = "parent_tag_id", required = false) String parent_tag_id,
                               @RequestParam(value = "parent_tag_name", required = false) String parent_tag_name,
                               @RequestParam(value = "ch_name", required = false) String ch_name,
                               @RequestParam(value = "en_name", required = false) String en_name,
                               @RequestParam(value = "reserved", required = false) String reserved,
                               @RequestParam(value = "type", required = false) String type,
                               @RequestParam(value = "model", required = false) String model,
                               @RequestParam(value = "volume", required = false) String volume,
                               @RequestParam(value = "ch_desc", required = false) String ch_desc,
                               @RequestParam(value = "en_desc", required = false) String en_desc,
                               @RequestParam(value = "spic", required = false) String spic,
                               @RequestParam(value = "bpic", required = false) String bpic,
                               @RequestParam(value = "search_type", required = false) String search_type,
                               HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        String chapter = request.getParameter("chapter");
        AnimeTag animeTag = new AnimeTag();
        try {
            if (StringUtil.isEmpty(chapter)) {
                animeTag.setTag_name(tag_name);
                animeTag.setParent_tag_id(0L);
                animeTag.setCh_name(ch_name);
                animeTag.setEn_name(en_name);
                animeTag.setCh_desc(ch_desc);
                animeTag.setEn_desc(en_desc);
                animeTag.setParent_tag_id(Long.valueOf(parent_tag_id));
                animeTag.setPlay_num(0L);
                animeTag.setFavorite_num(0L);

                animeTag.setVolume(volume);

                AnimeTagPicParam animeTagPicParam = new AnimeTagPicParam();
                if (!StringUtil.isEmpty(spic)) {
                    animeTagPicParam.setIos(spic);
                }
                if (!StringUtil.isEmpty(bpic)) {
                    animeTagPicParam.setAndroid(bpic);
                }
                animeTag.setPicjson(animeTagPicParam);
                animeTag.setAnimeTagType(AnimeTagType.getByCode(Integer.valueOf(type)));
                animeTag.setAnimeTagModel(AnimeTagModel.getByCode(Integer.valueOf(model)));
                animeTag.setAnimeTagSearchType(AnimeTagSearchType.getByCode(Integer.valueOf(search_type)));

                animeTag.setCreate_user(this.getCurrentUser().getUserid());
                animeTag.setCreate_date(new Date());
                animeTag = JoymeAppServiceSngl.get().createAnimeTag(animeTag);
            } else {
                AnimeTagPicParam animeTagPicParam = new AnimeTagPicParam();
                if (!StringUtil.isEmpty(spic)) {
                    animeTagPicParam.setIos(spic);
                }
                if (!StringUtil.isEmpty(bpic)) {
                    animeTagPicParam.setAndroid(bpic);
                }
                animeTag.setReserved(reserved);
                animeTag.setTag_name(tag_name);
                animeTag.setTag_desc(tag_desc);
                animeTag.setParent_tag_id(Long.valueOf(parent_tag_id));
                animeTag.setPicjson(animeTagPicParam);
                animeTag.setPlay_num(0L);
                animeTag.setFavorite_num(0L);
                animeTag.setCreate_user(this.getCurrentUser().getUserid());
                animeTag.setCreate_date(new Date());
                animeTag.setDisplay_order(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
                animeTag = JoymeAppServiceSngl.get().createAnimeTag(animeTag);
                HashMap map = new HashMap();
                map.put("parent_tag_id", parent_tag_id);
                map.put("parent_tag_name", parent_tag_name);
                map.put("chapter", "chapter");
                return new ModelAndView("redirect:/joymeapp/anime/tag/list", map);
            }

            writeToolsLog(LogOperType.TAG_ANIME_ADD, "大动漫新增标签,tagid:" + animeTag.getTag_id());
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/joymeapp/anime/tag/taglist", mapMessage);
        }
        return new ModelAndView("redirect:/joymeapp/anime/tag/list");
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyPage(@RequestParam(value = "tag_id") Long tag_id,
                                   @RequestParam(value = "parent_tag_name", required = false) String parent_tag_name,
                                   @RequestParam(value = "parent_tag_id", required = false) String parent_tag_id,
                                   HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(AnimeTagField.TAG_ID, tag_id));
        try {
            AnimeTag animeTag = JoymeAppServiceSngl.get().getAnimeTag(tag_id, queryExpress);
            mapMessage.put("animeTag", animeTag);
            mapMessage.put("animeTagType", AnimeTagType.getAll());
            mapMessage.put("animeTagModel", AnimeTagModel.getAll());
            mapMessage.put("animeTagSearchType", AnimeTagSearchType.getAll());
            String chapter = request.getParameter("chapter");


            QueryExpress qu = new QueryExpress();
            qu.add(QueryCriterions.ne(AnimeTagField.REMOVE_STATUS, ValidStatus.REMOVED.getCode()));
            try {
                List<AnimeTag> animeTagList = JoymeAppServiceSngl.get().queryAnimeTag(qu);
                mapMessage.put("animeTagList", animeTagList);
            } catch (ServiceException e) {
                e.printStackTrace();
            }


            if (!StringUtil.isEmpty(chapter) && chapter.equals("chapter")) {
                mapMessage.put("parent_tag_name", parent_tag_name);
                mapMessage.put("parent_tag_id", parent_tag_id);
                return new ModelAndView("/joymeapp/anime/tag/modifypage_chapter", mapMessage);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/joymeapp/anime/tag/taglist", mapMessage);
        }
        return new ModelAndView("/joymeapp/anime/tag/modifypage", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "tag_id", required = false) Long tag_id,
                               @RequestParam(value = "tag_name", required = false) String tag_name,
                               @RequestParam(value = "tag_desc", required = false) String tag_desc,
                               @RequestParam(value = "old_tag_name", required = false) String old_tag_name,
                               @RequestParam(value = "ch_name", required = false) String ch_name,
                               @RequestParam(value = "en_name", required = false) String en_name,
                               @RequestParam(value = "reserved", required = false) String reserved,
                               @RequestParam(value = "type", required = false) Integer type,
                               @RequestParam(value = "model", required = false) Integer model,
                               @RequestParam(value = "volume", required = false) String volume,
                               @RequestParam(value = "ch_desc", required = false) String ch_desc,
                               @RequestParam(value = "en_desc", required = false) String en_desc,
                               @RequestParam(value = "spic", required = false) String spic,
                               @RequestParam(value = "bpic", required = false) String bpic,
                               @RequestParam(value = "search_type", required = false) String search_type,
                               @RequestParam(value = "parent_tag_name", required = false) String parent_tag_name,
                               @RequestParam(value = "remove_status", required = false) String remove_status,
                               @RequestParam(value = "parent_tag_id", required = false) String parent_tag_id, HttpServletRequest request) {
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(AnimeTagField.TAG_ID, tag_id));
        UpdateExpress updateExpress = new UpdateExpress();
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            String chapter = request.getParameter("chapter");
            if (StringUtil.isEmpty(chapter)) {
                updateExpress.set(AnimeTagField.TAG_NAME, tag_name);
                updateExpress.set(AnimeTagField.CH_NAME, ch_name);
                updateExpress.set(AnimeTagField.EN_NAME, en_name);
                updateExpress.set(AnimeTagField.CH_DESC, ch_desc);
                updateExpress.set(AnimeTagField.EN_DESC, en_desc);
                updateExpress.set(AnimeTagField.VOLUME, volume);
                updateExpress.set(AnimeTagField.TYPE, AnimeTagType.getByCode(Integer.valueOf(type)).getCode());
                updateExpress.set(AnimeTagField.MODEL, AnimeTagModel.getByCode(Integer.valueOf(model)).getCode());
                updateExpress.set(AnimeTagField.SEARCH_TYPE, AnimeTagSearchType.getByCode(Integer.valueOf(search_type)).getCode());
                updateExpress.set(AnimeTagField.REMOVE_STATUS, ValidStatus.getByCode(remove_status).getCode());
                updateExpress.set(AnimeTagField.PARENT_TAG_ID, Long.valueOf(parent_tag_id));
                AnimeTagPicParam animeTagPicParam = new AnimeTagPicParam();
                if (!StringUtil.isEmpty(spic)) {
                    animeTagPicParam.setIos(spic);
                }
                if (!StringUtil.isEmpty(bpic)) {
                    animeTagPicParam.setAndroid(bpic);
                }
                updateExpress.set(AnimeTagField.PICJSON, animeTagPicParam.toJson());
                Date date = new Date();
                updateExpress.set(AnimeTagField.UPDATE_DATE, date);

                JoymeAppServiceSngl.get().modifyAnimeTag(tag_id, queryExpress, updateExpress);

            } else {
                map.put("chapter", chapter);
                map.put("parent_tag_name", parent_tag_name);
                map.put("parent_tag_id", parent_tag_id);
                updateExpress.set(AnimeTagField.TAG_NAME, tag_name);
                updateExpress.set(AnimeTagField.RESERVED, reserved);
                updateExpress.set(AnimeTagField.TAG_DESC, tag_desc);
                updateExpress.set(AnimeTagField.PARENT_TAG_ID, Long.valueOf(parent_tag_id));

                AnimeTagPicParam animeTagPicParam = new AnimeTagPicParam();
                if (!StringUtil.isEmpty(spic)) {
                    animeTagPicParam.setIos(spic);
                }
                if (!StringUtil.isEmpty(bpic)) {
                    animeTagPicParam.setAndroid(bpic);
                }
                updateExpress.set(AnimeTagField.PICJSON, animeTagPicParam.toJson());
                Date date = new Date();
                updateExpress.set(AnimeTagField.UPDATE_DATE, date);
                JoymeAppServiceSngl.get().modifyAnimeTag(tag_id, queryExpress, updateExpress);
            }

            writeToolsLog(LogOperType.TAG_ANIME_UPDATE, "大动漫修改标签,tagid:" + tag_id);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return new ModelAndView("/joymeapp/anime/tag/taglist", map);
        }
        return new ModelAndView("redirect:/joymeapp/anime/tag/list", map);
    }

    @RequestMapping(value = "/delete")
    public ModelAndView delete(@RequestParam(value = "tag_id", required = false) Long tag_id,
                               @RequestParam(value = "parent_tag_name", required = false) String parent_tag_name,
                               @RequestParam(value = "parent_tag_id", required = false) String parent_tag_id,
                               @RequestParam(value = "removestaus", required = false) String removestaus,
                               HttpServletRequest request
    ) {
        HashMap map = new HashMap();
        try {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(AnimeTagField.TAG_ID, tag_id));

            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(AnimeTagField.REMOVE_STATUS, ValidStatus.getByCode(removestaus).getCode());
            updateExpress.set(AnimeTagField.UPDATE_DATE, new Date());

            String chapter = request.getParameter("chapter");
            if (StringUtil.isEmpty(chapter)) {
                JoymeAppServiceSngl.get().modifyAnimeTag(tag_id, queryExpress, updateExpress);

                //删除子节目
                if (removestaus.equals("y")) {
                    JoymeAppServiceSngl.get().modifyAnimeTag(tag_id, new QueryExpress().add(QueryCriterions.eq(AnimeTagField.PARENT_TAG_ID, tag_id)), updateExpress);
                }
            } else {
                map.put("chapter", chapter);
                map.put("parent_tag_name", parent_tag_name);
                map.put("parent_tag_id", parent_tag_id);

                updateExpress.set(AnimeTagField.REMOVE_STATUS, ValidStatus.getByCode(removestaus).getCode());
                JoymeAppServiceSngl.get().modifyAnimeTag(tag_id, queryExpress, updateExpress);
            }

            writeToolsLog(LogOperType.TAG_ANIME_UPDATE_STATUS, "状态修改tagid:" + tag_id + ",removestaus=" + removestaus);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView("redirect:/joymeapp/anime/tag/list", map);
    }


    @RequestMapping(value = "/sort")
    public ModelAndView sort(@RequestParam(value = "desc", required = true) String desc,
                             @RequestParam(value = "chapter", required = true) String chapter,
                             @RequestParam(value = "tag_id", required = true) Long tag_id,
                             @RequestParam(value = "parent_tag_name", required = true) String parent_tag_name,
                             @RequestParam(value = "parent_tag_id", required = true) Long parent_tag_id) {

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
                        .add(QueryCriterions.eq(AnimeTagField.PARENT_TAG_ID, parent_tag_id));
            } else {
                queryExpress.add(QueryCriterions.lt(AnimeTagField.DISPLAY_ORDER, animeTag.getDisplay_order()))
                        .add(QuerySort.add(AnimeTagField.DISPLAY_ORDER, QuerySortOrder.DESC))
                        .add(QueryCriterions.eq(AnimeTagField.PARENT_TAG_ID, parent_tag_id));
            }

            //第二个
            PageRows<AnimeTag> appRows = JoymeAppServiceSngl.get().queryAnimeTagByPage(queryExpress, new Pagination(1, 1, 1));

            if (appRows != null && !CollectionUtil.isEmpty(appRows.getRows())) {

                updateExpress1.set(AnimeTagField.DISPLAY_ORDER, animeTag.getDisplay_order());
                JoymeAppServiceSngl.get().modifyAnimeTag(appRows.getRows().get(0).getTag_id(), new QueryExpress().add(QueryCriterions.eq(AnimeTagField.TAG_ID, appRows.getRows().get(0).getTag_id())), updateExpress1);

                updateExpress2.set(AnimeTagField.DISPLAY_ORDER, appRows.getRows().get(0).getDisplay_order());
                JoymeAppServiceSngl.get().modifyAnimeTag(animeTag.getTag_id(), new QueryExpress().add(QueryCriterions.eq(AnimeTagField.TAG_ID, animeTag.getTag_id())), updateExpress2);
            }

            writeToolsLog(LogOperType.TAG_ANIME_SORT, "大动漫标签排序,tagid:" + tag_id + ",orderby:" + desc);

            mapMessage.put("chapter", chapter);
            mapMessage.put("parent_tag_name", parent_tag_name);
            mapMessage.put("parent_tag_id", parent_tag_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView("redirect:/joymeapp/anime/tag/list", mapMessage);
    }

    @RequestMapping(value = "/listtree")
    public ModelAndView listtree() {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            //queryAnimeTag(QueryExpress queryExpress)
            QueryExpress queryExpree = new QueryExpress();
            queryExpree.add(QueryCriterions.ne(AnimeTVField.REMOVE_STATUS, ValidStatus.REMOVED.getCode()));
            queryExpree.add(QueryCriterions.eq(AnimeTagField.APP_TYPE, AnimeTagAppType.ANIME.getCode()));
            List<AnimeTag> animeTagList = JoymeAppServiceSngl.get().queryAnimeTag(queryExpree);
            mapMessage.put("animeTagList", animeTagList);
        } catch (Exception e) {

        }
        return new ModelAndView("/joymeapp/anime/tag/tagtree", mapMessage);
    }
}
