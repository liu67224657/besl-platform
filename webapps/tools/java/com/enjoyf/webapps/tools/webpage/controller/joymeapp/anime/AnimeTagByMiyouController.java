package com.enjoyf.webapps.tools.webpage.controller.joymeapp.anime;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.joymeapp.JoymeAppServiceSngl;
import com.enjoyf.platform.service.joymeapp.anime.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.apache.openjpa.kernel.exps.QueryExpressions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.management.QueryExp;
import javax.management.monitor.StringMonitor;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 15-9-6
 * Time: 下午4:29
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/animetag/miyou")
public class AnimeTagByMiyouController extends ToolsBaseController {
    @RequestMapping(value = "list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,      //数据库记录索引
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "20") int pageSize,
                             @RequestParam(value = "tagname", required = false) String tagName,
                             @RequestParam(value = "postorder", required = false) String postOrder,
                             @RequestParam(value = "replyorder", required = false) String replyOrder
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            int curPage = (pageStartIndex / pageSize) + 1;
            Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
            QueryExpress queryExpress = new QueryExpress()
                    .add(QueryCriterions.eq(AnimeTagField.APP_TYPE, AnimeTagAppType.SYHB_QUANZI.getCode()));
            if (!StringUtil.isEmpty(tagName)) {
                queryExpress.add(QueryCriterions.like(AnimeTagField.TAG_NAME, "%" + tagName + "%"));
                mapMessage.put("tagname", tagName);
            }
            if (!StringUtil.isEmpty(postOrder)) {
                if (QuerySortOrder.ASC.getCode().equalsIgnoreCase(postOrder)) {
                    queryExpress.add(QuerySort.add(AnimeTagField.PLAY_NUM, QuerySortOrder.ASC));
                } else {
                    queryExpress.add(QuerySort.add(AnimeTagField.PLAY_NUM, QuerySortOrder.DESC));
                }
            }
            if (!StringUtil.isEmpty(replyOrder)) {
                if (QuerySortOrder.ASC.getCode().equalsIgnoreCase(replyOrder)) {
                    queryExpress.add(QuerySort.add(AnimeTagField.FAVORITE_NUM, QuerySortOrder.ASC));
                } else {
                    queryExpress.add(QuerySort.add(AnimeTagField.FAVORITE_NUM, QuerySortOrder.DESC));
                }
            }
            if (StringUtil.isEmpty(postOrder) && StringUtil.isEmpty(replyOrder)) {
                queryExpress.add(QuerySort.add(AnimeTagField.DISPLAY_ORDER, QuerySortOrder.ASC));
            }
            PageRows<AnimeTag> pageRows = JoymeAppServiceSngl.get().queryAnimeTagByPage(queryExpress, pagination);
            List<AnimeTag> animeTags = pageRows.getRows();
            int i = 0;
            for (AnimeTag animeTag : animeTags) {
                if (ValidStatus.VALID.equals(animeTag.getRemove_status())) {
                    i++;
                }
            }
            mapMessage.put("validnum", i);
            mapMessage.put("list", animeTags);
            mapMessage.put("page", pageRows.getPage());


        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("/joymeapp/anime/miyou/list", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createPage() {

        return new ModelAndView("/joymeapp/anime/miyou/createpage");
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "tagname", required = false) String tagname,
                               @RequestParam(value = "tagdesc", required = false) String tagdesc,
                               @RequestParam(value = "pepolenum", required = false) String pepolenum,
                               @RequestParam(value = "picurl", required = false) String picurl) {
        try {
            AnimeTag animeTag = new AnimeTag();
            animeTag.setTag_desc(tagdesc);
            animeTag.setTag_name(tagname);
            animeTag.setTotal_sum(Integer.valueOf(pepolenum));
            animeTag.setPlay_num(0l);
            animeTag.setFavorite_num(0l);
            animeTag.setCreate_date(new Date());
            animeTag.setCreate_user(getCurrentUser().getUsername());
            AnimeTagPicParam animeTagPicParam = new AnimeTagPicParam();
            animeTagPicParam.setIos(picurl);
            animeTag.setPicjson(animeTagPicParam);
            animeTag.setApp_type(AnimeTagAppType.SYHB_QUANZI);
            animeTag.setDisplay_order(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));

            JoymeAppServiceSngl.get().createAnimeTag(animeTag);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
        }
        return new ModelAndView("redirect:/animetag/miyou/list");
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyPage(@RequestParam(value = "tagid", required = false) String tagid) {
        Map<String, Object> mapMessage = null;
        try {
            long longTagId = Long.parseLong(tagid);
            mapMessage = new HashMap<String, Object>();
            AnimeTag animeTag = JoymeAppServiceSngl.get().getAnimeTag(longTagId, new QueryExpress().add(QueryCriterions.eq(AnimeTagField.TAG_ID, longTagId)));
            mapMessage.put("animeTag", animeTag);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/joymeapp/anime/miyou/modifypage", mapMessage);
    }

    @RequestMapping(value = "modify")
    public ModelAndView modify(@RequestParam(value = "tagname", required = false) String tagname,
                               @RequestParam(value = "tagdesc", required = false) String tagdesc,
                               @RequestParam(value = "pepolenum", required = false) String pepolenum,
                               @RequestParam(value = "picurl", required = false) String picurl,
                               @RequestParam(value = "tagid", required = false) String tagid) {
        try {
            long longTagId = Long.parseLong(tagid);
            AnimeTagPicParam json = new AnimeTagPicParam();
            json.setIos(picurl);
            json.setAndroid("");
            JoymeAppServiceSngl.get().modifyAnimeTag(longTagId,
                    new QueryExpress().add(QueryCriterions.eq(AnimeTagField.TAG_ID, longTagId)),
                    new UpdateExpress().set(AnimeTagField.TAG_NAME, tagname)
                            .set(AnimeTagField.TAG_DESC, tagdesc)
                            .set(AnimeTagField.TOTAL_SUM, Integer.parseInt(pepolenum))
                            .set(AnimeTagField.PICJSON, json.toJson())
                            .set(AnimeTagField.UPDATE_DATE, new Date()));
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
        }
        return new ModelAndView("redirect:/animetag/miyou/list");
    }

    @RequestMapping(value = "/sort")
    public ModelAndView sort(@RequestParam(value = "tagid", required = false) String tagid,
                             @RequestParam(value = "display_order", required = false) String display_order) {
        try {
            JoymeAppServiceSngl.get().modifyAnimeTag(Long.parseLong(tagid),
                    new QueryExpress().add(QueryCriterions.eq(AnimeTagField.TAG_ID, Long.parseLong(tagid))),
                    new UpdateExpress().set(AnimeTagField.DISPLAY_ORDER, Integer.parseInt(display_order)).set(AnimeTagField.UPDATE_DATE, new Date()));
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
        }
        return new ModelAndView("redirect:/animetag/miyou/list");
    }

    @RequestMapping(value = "/delete")
    public ModelAndView delete(@RequestParam(value = "tagid", required = false) String tagid,
                               @RequestParam(value = "valid", required = false) String valid) {
        try {
            JoymeAppServiceSngl.get().modifyAnimeTag(Long.parseLong(tagid),
                    new QueryExpress().add(QueryCriterions.eq(AnimeTagField.TAG_ID, Long.parseLong(tagid))),
                    new UpdateExpress().set(AnimeTagField.REMOVE_STATUS, valid).set(AnimeTagField.UPDATE_DATE, new Date()));
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
        }
        return new ModelAndView("redirect:/animetag/miyou/list");
    }
}
