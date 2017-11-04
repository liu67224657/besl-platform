package com.enjoyf.webapps.tools.webpage.controller.gameclient.tag;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.joymeapp.JoymeAppServiceSngl;
import com.enjoyf.platform.service.joymeapp.anime.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.http.URLUtils;
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
 */
@Controller
@RequestMapping(value = "/gameclient/tag")
public class GameClientAnimeTagController extends ToolsBaseController {

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                             @RequestParam(value = "removestaus", required = false,defaultValue = "valid") String removestaus,
                             @RequestParam(value = "tag_name", required = false) String tag_name,
                             HttpServletRequest request
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        mapMessage.put("removestaus", removestaus);

        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
        QueryExpress queryExpress = new QueryExpress();
        try {
            queryExpress.add(QuerySort.add(AnimeTagField.DISPLAY_ORDER, QuerySortOrder.DESC));
            queryExpress.add(QueryCriterions.eq(AnimeTagField.APP_TYPE, AnimeTagAppType.SYHB.getCode()));

            if (!StringUtil.isEmpty(removestaus)) {
                queryExpress.add(QueryCriterions.eq(AnimeTagField.REMOVE_STATUS, ValidStatus.getByCode(removestaus).getCode()));
            }
            if (!StringUtil.isEmpty(tag_name)) {
                queryExpress.add(QueryCriterions.like(AnimeTagField.TAG_NAME, "%" + tag_name + "%"));
            }

            PageRows<AnimeTag> pageRows = JoymeAppServiceSngl.get().queryAnimeTagByPage(queryExpress, pagination);
            mapMessage.put("list", pageRows.getRows());
            mapMessage.put("page", pageRows.getPage());


        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/gameclient/tag/taglist", mapMessage);
        }

        return new ModelAndView("/gameclient/tag/taglist", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createPage(HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("gameClientTagType", AnimeTagType.getAll());

        //2.3及以上版本新加
        mapMessage.put("tagType", GameClientTagType.getAll());
        return new ModelAndView("/gameclient/tag/createpage", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "tag_name", required = false) String tag_name,
                               @RequestParam(value = "ch_name", required = false) String ch_name,//ch_name玩霸2.1.0标签名称
                               @RequestParam(value = "volume", required = false) String volume,//-1表示出现在玩霸2.1.0
                               @RequestParam(value = "type", required = false) Integer type,
                               @RequestParam(value = "icon", required = false) String icon,

                               @RequestParam(value = "tagtype", required = false) String tagtype,
                               @RequestParam(value = "url", required = false) String url,
                               HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        AnimeTag animeTag = new AnimeTag();
        try {
            animeTag.setTag_name(tag_name);
            animeTag.setVolume(volume);
            animeTag.setCreate_user(this.getCurrentUser() == null ? "" : this.getCurrentUser().getUserid());
            animeTag.setCreate_date(new Date());
            animeTag.setParent_tag_id(0L);
            animeTag.setPlay_num(0L);
            animeTag.setCh_name(ch_name);
            animeTag.setFavorite_num(0L);
            animeTag.setDisplay_order(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
            animeTag.setApp_type(AnimeTagAppType.SYHB);
            AnimeTagPicParam json = new AnimeTagPicParam();
            json.setIos(icon.replaceFirst(WebappConfig.get().getQiniuHost(), ""));
            json.setAndroid("");
            json.setUrl(url);
            if(!StringUtil.isEmpty(tagtype)){
                json.setType(tagtype);
            }

            animeTag.setPicjson(json);
            animeTag.setAnimeTagType(AnimeTagType.getByCode(type));
            animeTag = JoymeAppServiceSngl.get().createAnimeTag(animeTag);

            writeToolsLog(LogOperType.TAG_ADD, "玩霸:创建标签,tagid:" + animeTag.getTag_id());
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/gameclient/tag/taglist", mapMessage);
        }
        return new ModelAndView("redirect:/gameclient/tag/list");
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyPage(@RequestParam(value = "tag_id") Long tag_id,
                                   HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(AnimeTagField.TAG_ID, tag_id));
        try {
            AnimeTag animeTag = JoymeAppServiceSngl.get().getAnimeTag(tag_id, queryExpress);
            AnimeTagPicParam picParam = animeTag.getPicjson();
            picParam.setIos(URLUtils.getJoymeDnUrl(picParam.getIos()));
            animeTag.setPicjson(picParam);
            mapMessage.put("animeTag", animeTag);
            mapMessage.put("gameClientTagType", AnimeTagType.getAll());

            //2.3及以上版本新加
            mapMessage.put("tagType", GameClientTagType.getAll());
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/gameclient/tag/taglist", mapMessage);
        }
        return new ModelAndView("/gameclient/tag/modifypage", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "tag_id", required = false) Long tag_id,
                               @RequestParam(value = "tag_name", required = false) String tag_name,
                               @RequestParam(value = "ch_name", required = false) String ch_name,//ch_name玩霸2.1.0标签名称
                               @RequestParam(value = "volume", required = false) String volume,//-1表示出现在玩霸2.1.0
                               @RequestParam(value = "type", required = false) Integer type,
                               @RequestParam(value = "icon", required = false) String icon,

                               @RequestParam(value = "tagtype", required = false) String tagtype,
                               @RequestParam(value = "url", required = false) String url,
                               HttpServletRequest request) {
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(AnimeTagField.TAG_ID, tag_id));
        UpdateExpress updateExpress = new UpdateExpress();
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            Date date = new Date();
            updateExpress.set(AnimeTagField.VOLUME, volume);
            updateExpress.set(AnimeTagField.TAG_NAME, tag_name);
            updateExpress.set(AnimeTagField.CH_NAME, ch_name);
            updateExpress.set(AnimeTagField.UPDATE_DATE, date);
            updateExpress.set(AnimeTagField.TYPE, type);
            AnimeTagPicParam json = new AnimeTagPicParam();
            json.setIos(icon.replaceFirst(WebappConfig.get().getQiniuHost(), ""));
            json.setAndroid("");
            json.setUrl(url);
            if(!StringUtil.isEmpty(tagtype)){
                json.setType(tagtype);
            }
            updateExpress.set(AnimeTagField.PICJSON, json.toJson());
            JoymeAppServiceSngl.get().modifyAnimeTag(tag_id, queryExpress, updateExpress);


            writeToolsLog(LogOperType.TAG_UPDATE, "玩霸:修改标签,tagid:" + tag_id);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return new ModelAndView("/gameclient/tag/taglist", map);
        }
        return new ModelAndView("redirect:/gameclient/tag/list", map);
    }

    @RequestMapping(value = "/delete")
    public ModelAndView delete(@RequestParam(value = "tag_id", required = false) Long tag_id,
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
            JoymeAppServiceSngl.get().modifyAnimeTag(tag_id, queryExpress, updateExpress);

            writeToolsLog(LogOperType.TAG_UPDATE, "玩霸:标签状态修改" + removestaus);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView("redirect:/gameclient/tag/list", map);
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
                        .add(QueryCriterions.eq(AnimeTagField.APP_TYPE, AnimeTagAppType.SYHB.getCode()));
            } else {
                queryExpress.add(QueryCriterions.lt(AnimeTagField.DISPLAY_ORDER, animeTag.getDisplay_order()))
                        .add(QuerySort.add(AnimeTagField.DISPLAY_ORDER, QuerySortOrder.DESC))
                        .add(QueryCriterions.eq(AnimeTagField.APP_TYPE, AnimeTagAppType.SYHB.getCode()));
            }

            //第二个
            PageRows<AnimeTag> appRows = JoymeAppServiceSngl.get().queryAnimeTagByPage(queryExpress, new Pagination(1, 1, 1));

            if (appRows != null && !CollectionUtil.isEmpty(appRows.getRows())) {

                updateExpress1.set(AnimeTagField.DISPLAY_ORDER, animeTag.getDisplay_order());
                JoymeAppServiceSngl.get().modifyAnimeTag(appRows.getRows().get(0).getTag_id(), new QueryExpress().add(QueryCriterions.eq(AnimeTagField.TAG_ID, appRows.getRows().get(0).getTag_id())), updateExpress1);

                updateExpress2.set(AnimeTagField.DISPLAY_ORDER, appRows.getRows().get(0).getDisplay_order());
                JoymeAppServiceSngl.get().modifyAnimeTag(animeTag.getTag_id(), new QueryExpress().add(QueryCriterions.eq(AnimeTagField.TAG_ID, animeTag.getTag_id())), updateExpress2);
            }

            writeToolsLog(LogOperType.TAG_SORT, "玩霸:标签排序,tagid:" + tag_id + "," + desc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView("redirect:/gameclient/tag/list", mapMessage);
    }


}
