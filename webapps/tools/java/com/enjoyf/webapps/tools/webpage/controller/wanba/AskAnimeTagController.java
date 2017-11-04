package com.enjoyf.webapps.tools.webpage.controller.wanba;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.joymeapp.JoymeAppServiceSngl;
import com.enjoyf.platform.service.joymeapp.anime.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
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
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-10-27
 * Time: 下午2:08
 * To change this template use File | Settings | File Templates.
 * 游戏标签
 */
@Controller
@RequestMapping(value = "/wanba/tag")
public class AskAnimeTagController extends ToolsBaseController {

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
            queryExpress.add(QueryCriterions.eq(AnimeTagField.APP_TYPE, AnimeTagAppType.WANBA_ASK.getCode()));
            PageRows<AnimeTag> pageRows = JoymeAppServiceSngl.get().queryAnimeTagByPage(queryExpress, pagination);
            mapMessage.put("list", pageRows.getRows());
            mapMessage.put("page", pageRows.getPage());
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/wanba/tag/taglist", mapMessage);
        }
        return new ModelAndView("/wanba/tag/taglist", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createPage(HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("tagType", GameClientTagType.getAll());
        return new ModelAndView("/wanba/tag/createpage", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "tag_name", required = false) String tag_name,
                               @RequestParam(value = "tag_desc", required = false) String tag_desc,
                               @RequestParam(value = "spic", required = false) String spic,
                               @RequestParam(value = "tagtype", required = false) String tagtype,
                               @RequestParam(value = "url", required = false) String url,
                               HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        AnimeTag animeTag = new AnimeTag();
        try {
            animeTag.setTag_name(tag_name);
            animeTag.setTag_desc(tag_desc);

            AnimeTagPicParam animeTagPicParam = new AnimeTagPicParam();
            if (tagtype.equals(GameClientTagType.DEFAULT.getCode() + "")) {
                animeTagPicParam.setUrl("");
            } else {
                animeTagPicParam.setUrl(url);
            }
            animeTagPicParam.setIos(spic);
            animeTagPicParam.setType(tagtype);


            animeTag.setApp_type(AnimeTagAppType.WANBA_ASK);
            animeTag.setPicjson(animeTagPicParam);
            animeTag.setPlay_num(0L);
            animeTag.setFavorite_num(0L);
            animeTag.setCreate_user(this.getCurrentUser().getUserid());
            animeTag.setCreate_date(new Date());
            animeTag.setDisplay_order(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
            animeTag = JoymeAppServiceSngl.get().createAnimeTag(animeTag);


            //写log
            writeToolsLog(LogOperType.TAG_ANIME_ADD, "玩霸问答新增标签,tagid:" + animeTag.getTag_id());
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/wanba/tag/taglist", mapMessage);
        }
        return new ModelAndView("redirect:/wanba/tag/list");
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
            mapMessage.put("tagType", GameClientTagType.getAll());
            mapMessage.put("remove_status", ValidStatus.getAll());
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/wanba/tag/taglist", mapMessage);
        }
        return new ModelAndView("/wanba/tag/modifypage", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "tag_id", required = false) Long tag_id,
                               @RequestParam(value = "tag_name", required = false) String tag_name,
                               @RequestParam(value = "tag_desc", required = false) String tag_desc,
                               @RequestParam(value = "spic", required = false) String spic,
                               @RequestParam(value = "tagtype", required = false) String tagtype,
                               @RequestParam(value = "remove_status", required = false) String remove_status,
                               @RequestParam(value = "url", required = false) String url, HttpServletRequest request) {
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(AnimeTagField.TAG_ID, tag_id));
        UpdateExpress updateExpress = new UpdateExpress();
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            updateExpress.set(AnimeTagField.TAG_NAME, tag_name);
            updateExpress.set(AnimeTagField.TAG_DESC, tag_desc);
            updateExpress.set(AnimeTagField.REMOVE_STATUS, ValidStatus.getByCode(remove_status).getCode());

            AnimeTagPicParam animeTagPicParam = new AnimeTagPicParam();
            animeTagPicParam.setIos(spic);
            if (tagtype.equals(GameClientTagType.DEFAULT.getCode() + "")) {
                animeTagPicParam.setUrl("");
            } else {
                animeTagPicParam.setUrl(url);
            }
            animeTagPicParam.setType(tagtype);


            updateExpress.set(AnimeTagField.PICJSON, animeTagPicParam.toJson());
            updateExpress.set(AnimeTagField.UPDATE_DATE, new Date());

            JoymeAppServiceSngl.get().modifyAnimeTag(tag_id, queryExpress, updateExpress);


            writeToolsLog(LogOperType.TAG_ANIME_UPDATE, "玩霸问答修改标签,tagid:" + tag_id);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return new ModelAndView("/wanba/tag/taglist", map);
        }
        return new ModelAndView("redirect:/wanba/tag/list", map);
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
                        .add(QueryCriterions.eq(AnimeTagField.APP_TYPE, AnimeTagAppType.WANBA_ASK.getCode()));
            } else {
                queryExpress.add(QueryCriterions.lt(AnimeTagField.DISPLAY_ORDER, animeTag.getDisplay_order()))
                        .add(QuerySort.add(AnimeTagField.DISPLAY_ORDER, QuerySortOrder.DESC))
                        .add(QueryCriterions.eq(AnimeTagField.APP_TYPE, AnimeTagAppType.WANBA_ASK.getCode()));
            }

            //第二个
            PageRows<AnimeTag> appRows = JoymeAppServiceSngl.get().queryAnimeTagByPage(queryExpress, new Pagination(1, 1, 1));

            if (appRows != null && !CollectionUtil.isEmpty(appRows.getRows())) {

                updateExpress1.set(AnimeTagField.DISPLAY_ORDER, animeTag.getDisplay_order());
                JoymeAppServiceSngl.get().modifyAnimeTag(appRows.getRows().get(0).getTag_id(), new QueryExpress().add(QueryCriterions.eq(AnimeTagField.TAG_ID, appRows.getRows().get(0).getTag_id())), updateExpress1);

                updateExpress2.set(AnimeTagField.DISPLAY_ORDER, appRows.getRows().get(0).getDisplay_order());
                JoymeAppServiceSngl.get().modifyAnimeTag(animeTag.getTag_id(), new QueryExpress().add(QueryCriterions.eq(AnimeTagField.TAG_ID, animeTag.getTag_id())), updateExpress2);
            }

            writeToolsLog(LogOperType.TAG_ANIME_SORT, "玩霸问答标签排序,tagid:" + tag_id + ",orderby:" + desc);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView("redirect:/wanba/tag/list", mapMessage);
    }

}
