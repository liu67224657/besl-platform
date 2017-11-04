package com.enjoyf.webapps.tools.webpage.controller.joymeapp.anime;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.joymeapp.JoymeAppServiceSngl;
import com.enjoyf.platform.service.joymeapp.anime.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.http.HttpUtilGetURL;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.webapps.tools.weblogic.privilege.PrivilegeWebLogic;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-10-26
 * Time: 下午4:41
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/joymeapp/anime/tv")
public class AnimeTvController extends ToolsBaseController {
    @Resource(name = "privilegeWebLogic")
    private PrivilegeWebLogic privilegeWebLogic;

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                             @RequestParam(value = "removestaus", required = false) String removestaus,
                             @RequestParam(value = "search", required = false) String search,
                             @RequestParam(value = "domain", required = false) String domain,
                             @RequestParam(value = "tv_name", required = false) String tv_name,
                             @RequestParam(value = "tag_id", required = false) String tag_id,
                             @RequestParam(value = "tv_id", required = false) String tv_id,
                             @RequestParam(value = "url", required = false) String url,
                             @RequestParam(value = "errorMsg", required = false) String errorMsg,
                             HttpServletRequest request
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
        QueryExpress queryExpress = new QueryExpress();
        try {
            //
            queryExpress.add(QuerySort.add(AnimeTVField.DISPLAY_ORDER, QuerySortOrder.DESC));

            if (!StringUtil.isEmpty(removestaus)) {
                queryExpress.add(QueryCriterions.eq(AnimeTagField.REMOVE_STATUS, ValidStatus.getByCode(removestaus).getCode()));
            }


            if (!StringUtil.isEmpty(domain)) {
                queryExpress.add(QueryCriterions.eq(AnimeTVField.DOMAIN, AnimeTVDomain.getByCode(Integer.valueOf(domain)).getCode()));
            }

            if (!StringUtil.isEmpty(tv_name)) {
                queryExpress.add(QueryCriterions.like(AnimeTVField.TV_NAME, "%" + tv_name + "%"));
            }

            if (!StringUtil.isEmpty(tag_id)) {
                queryExpress.add(QueryCriterions.like(AnimeTVField.TAGS, "%," + tag_id + ",%"));
            }

            if (!StringUtil.isEmpty(tv_id)) {
                queryExpress.add(QueryCriterions.eq(AnimeTVField.TV_ID, Long.valueOf(tv_id)));
            }

            if (!StringUtil.isEmpty(url)) {
                queryExpress.add(QueryCriterions.eq(AnimeTVField.URL, url));
            }

            PageRows<AnimeTV> pageRows = JoymeAppServiceSngl.get().queryAnimeTVByPage(queryExpress, pagination);


            mapMessage.put("list", pageRows.getRows());
            mapMessage.put("page", pageRows.getPage());
            mapMessage.put("animeTVDomain", AnimeTVDomain.getAll());
            mapMessage.put("tag_id", tag_id);
            mapMessage.put("errorMsg", errorMsg);
            if (!StringUtil.isEmpty(tag_id)) {
                AnimeTag animeTag = JoymeAppServiceSngl.get().getAnimeTag(Long.valueOf(tag_id), new QueryExpress().add(QueryCriterions.eq(AnimeTagField.TAG_ID, Long.valueOf(tag_id))));
                mapMessage.put("animeTag", animeTag);
                return new ModelAndView("/joymeapp/anime/tv/tvlist_tag", mapMessage);
            }


        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/joymeapp/anime/tag/taglist", mapMessage);
        }
        return new ModelAndView("/joymeapp/anime/tv/tvlist", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createPage(HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.ne(AnimeTagField.REMOVE_STATUS, ValidStatus.REMOVED.getCode()));
        try {
            List<AnimeTag> animeTagList = JoymeAppServiceSngl.get().queryAnimeTag(queryExpress);
            mapMessage.put("animeTagList", animeTagList);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        mapMessage.put("animeTVDomain", AnimeTVDomain.getAll());
        mapMessage.put("animeTvIsNewType", AnimeTvIsNewType.getAll());
        //String menu = privilegeWebLogic.preRoleResourceJosn();
        //mapMsg.put("menu", menu);
        //System.out.println(menu);
        return new ModelAndView("/joymeapp/anime/tv/createpage", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "tv_name", required = false) String tv_name,
                               @RequestParam(value = "domain", required = false) String domain,
                               @RequestParam(value = "domain_param", required = false) String domain_param,
                               @RequestParam(value = "url", required = false) String url,
                               @RequestParam(value = "m3u8", required = false) String m3u8,
                               @RequestParam(value = "tags", required = false) String tags,
                               @RequestParam(value = "tv_pic", required = false) String tv_pic,
                               @RequestParam(value = "isnew", required = false) String isnew,
                               @RequestParam(value = "space", required = false) String space,
                               @RequestParam(value = "tv_number", required = false) String tv_number,
                               @RequestParam(value = "display_order", required = false) String display_order,
                               HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            AnimeTV animeTV = new AnimeTV();
            animeTV.setTv_name(tv_name);
            animeTV.setDomain(AnimeTVDomain.getByCode(Integer.valueOf(domain)));
            animeTV.setDomain_param(domain_param);
            animeTV.setUrl(url);
            animeTV.setM3u8(m3u8);
            animeTV.setTv_pic(tv_pic);
            animeTV.setTags(tags);
            if (!StringUtil.isEmpty(tv_number)) {
                animeTV.setTv_number(Integer.valueOf(tv_number));
            }
            animeTV.setSpace(space);
            animeTV.setAnimeTvIsNewType(AnimeTvIsNewType.getByCode(Integer.valueOf(isnew)));
            if (StringUtil.isEmpty(display_order)) {
                animeTV.setDisplay_order(System.currentTimeMillis());
            } else {
                animeTV.setDisplay_order(Long.valueOf(display_order));
            }

            animeTV.setCreate_date(new Date());
            animeTV.setCreate_user(this.getCurrentUser().getUserid());
            animeTV = JoymeAppServiceSngl.get().createAnimeTV(animeTV);

            writeToolsLog(LogOperType.ANIME_TV_ADD, "新增视频:" + animeTV.getTv_id());
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return new ModelAndView("redirect:/joymeapp/anime/tv/list");
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyPage(@RequestParam(value = "tv_id", required = false) Long tv_id,
                                   @RequestParam(value = "tag_id", required = false) String tag_id,
                                   @RequestParam(value = "pager.offset", required = false, defaultValue = "0") String pageStartIndex,
                                   HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            AnimeTV animeTV = JoymeAppServiceSngl.get().getAnimeTV(new QueryExpress().add(QueryCriterions.eq(AnimeTVField.TV_ID, tv_id)));

            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.ne(AnimeTagField.REMOVE_STATUS, ValidStatus.REMOVED.getCode()));
            queryExpress.add(QueryCriterions.eq(AnimeTagField.APP_TYPE, AnimeTagAppType.ANIME.getCode()));
            List<AnimeTag> animeTagList = JoymeAppServiceSngl.get().queryAnimeTag(queryExpress);
            mapMessage.put("animeTagList", animeTagList);
            mapMessage.put("animeTV", animeTV);
            mapMessage.put("animeTVDomain", AnimeTVDomain.getAll());
            mapMessage.put("animeTvIsNewType", AnimeTvIsNewType.getAll());
            mapMessage.put("tag_id", tag_id);
            mapMessage.put("pageStartIndex", pageStartIndex);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return new ModelAndView("/joymeapp/anime/tv/modifypage", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "tv_id", required = false) Long tv_id,
                               @RequestParam(value = "removestaus", required = false) String removestaus,
                               @RequestParam(value = "remove", required = false) String remove,

                               @RequestParam(value = "tv_name", required = false) String tv_name,
                               @RequestParam(value = "domain", required = false) String domain,
                               @RequestParam(value = "domain_param", required = false) String domain_param,
                               @RequestParam(value = "url", required = false) String url,
                               @RequestParam(value = "m3u8", required = false) String m3u8,
                               @RequestParam(value = "tags", required = false) String tags,
                               @RequestParam(value = "tv_pic", required = false) String tv_pic,

                               @RequestParam(value = "isnew", required = false) String isnew,
                               @RequestParam(value = "space", required = false) String space,
                               @RequestParam(value = "tv_number", required = false) String tv_number,
                               @RequestParam(value = "remove_status", required = false) String remove_status,
                               @RequestParam(value = "tag_id", required = false) String tag_id,
                               @RequestParam(value = "pager.offset", required = false, defaultValue = "0") String pageStartIndex,

                               //tag_tv_id tag_tag_id 新增视频到本标签 时用
                               @RequestParam(value = "tag_tv_id", required = false) String tag_tv_id,
                               @RequestParam(value = "tag_tag_id", required = false) String tag_tag_id,

                               HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {

            if (StringUtil.isEmpty(tag_tv_id)) {
                QueryExpress queryExpress = new QueryExpress();
                queryExpress.add(QueryCriterions.eq(AnimeTVField.TV_ID, tv_id));
                UpdateExpress updateExpress = new UpdateExpress();
                updateExpress.set(AnimeTVField.UPDATE_DATE, new Date());
                //remove or restore
                List<Long> tagList = new ArrayList<Long>();
                if (!StringUtil.isEmpty(remove) && remove.equals("remove")) {
                    updateExpress.set(AnimeTVField.REMOVE_STATUS, ValidStatus.getByCode(removestaus).getCode());
                } else {//modify
                    updateExpress.set(AnimeTVField.TV_NAME, tv_name);
                    updateExpress.set(AnimeTVField.DOMAIN, AnimeTVDomain.getByCode(Integer.valueOf(domain)).getCode());
                    updateExpress.set(AnimeTVField.DOMAIN_PARAM, domain_param);
                    updateExpress.set(AnimeTVField.URL, url);
                    updateExpress.set(AnimeTVField.M3U8, m3u8);
                    updateExpress.set(AnimeTVField.TV_PIC, tv_pic);
                    updateExpress.set(AnimeTVField.TAGS, tags);

                    updateExpress.set(AnimeTVField.TV_NUMBER, Integer.valueOf(tv_number));

                    updateExpress.set(AnimeTVField.SPACE, space);
                    updateExpress.set(AnimeTVField.IS_NEW, AnimeTvIsNewType.getByCode(Integer.valueOf(isnew)).getCode());
                    updateExpress.set(AnimeTVField.REMOVE_STATUS, ValidStatus.getByCode(remove_status).getCode());

                    String arr[] = tags.split(",");
                    for (String tag : arr) {
                        if (!StringUtil.isEmpty(tag)) {
                            tagList.add(Long.valueOf(tag));
                        }
                    }

                }

                JoymeAppServiceSngl.get().modifyAnimeTV(queryExpress, updateExpress, tagList);
                mapMessage.put("tag_id", tag_id);
                mapMessage.put("pager.offset", pageStartIndex);
            } else {
                mapMessage.put("tag_id", tag_tag_id);
                mapMessage.put("pager.offset", pageStartIndex);

                QueryExpress queryExpress = new QueryExpress();
                queryExpress.add(QueryCriterions.eq(AnimeTVField.TV_ID, Long.valueOf(tag_tv_id)));
                UpdateExpress updateExpress = new UpdateExpress();
                updateExpress.set(AnimeTVField.UPDATE_DATE, new Date());

                List<Long> tagList = new ArrayList<Long>();
                AnimeTV animeTV = JoymeAppServiceSngl.get().getAnimeTV(queryExpress);
                if (animeTV == null) {
                    mapMessage.put("errorMsg", "视频不存在");
                    return new ModelAndView("redirect:/joymeapp/anime/tv/list", mapMessage);
                }
                String oldtags = animeTV.getTags();
                String arr[] = oldtags.split(",");

                for (String tag : arr) {
                    if (!StringUtil.isEmpty(tag)) {
                        //重复填写视频ID，忽略
                        if (tag.equals(tag_tag_id)) {
                            mapMessage.put("errorMsg", "视频已存在，请不要重复添加！");
                            return new ModelAndView("redirect:/joymeapp/anime/tv/list", mapMessage);
                        }
                        tagList.add(Long.valueOf(tag));
                    }
                }
                tagList.add(Long.valueOf(tag_tag_id));


                if (StringUtil.isEmpty(oldtags)) {
                    updateExpress.set(AnimeTVField.TAGS, "," + tag_tag_id + ",");
                } else {
                    updateExpress.set(AnimeTVField.TAGS, oldtags + tag_tag_id + ",");
                }

                JoymeAppServiceSngl.get().modifyAnimeTV(queryExpress, updateExpress, tagList);
            }

            writeToolsLog(LogOperType.ANIME_TV_UPDATE, "视频修改:tv_id" + tv_id);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return new ModelAndView("redirect:/joymeapp/anime/tv/list", mapMessage);
    }


    @RequestMapping(value = "/sort")
    public ModelAndView sort(@RequestParam(value = "desc", required = false) String desc,
                             @RequestParam(value = "tv_id", required = false) Long tv_id,
                             @RequestParam(value = "tag_id", required = false) Long tag_id,
                             @RequestParam(value = "sortnum", required = false) String sortnum,
                             @RequestParam(value = "pager.offset", required = false, defaultValue = "0") String pageStartIndex) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();

        UpdateExpress updateExpress1 = new UpdateExpress();
        UpdateExpress updateExpress2 = new UpdateExpress();
        QueryExpress queryExpress = new QueryExpress();
        try {
            mapMessage.put("tag_id", tag_id);
            mapMessage.put("pager.offset", pageStartIndex);
            queryExpress.add(QueryCriterions.like(AnimeTVField.TAGS, "%," + tag_id + ",%"));
            if (StringUtil.isEmpty(sortnum)) {
                //第一个
                AnimeTV animeTV = JoymeAppServiceSngl.get().getAnimeTV(new QueryExpress().add(QueryCriterions.eq(AnimeTVField.TV_ID, tv_id)));
                if (desc.equals("up")) {
                    queryExpress.add(QueryCriterions.gt(AnimeTVField.DISPLAY_ORDER, animeTV.getDisplay_order()))
                            .add(QuerySort.add(AnimeTVField.DISPLAY_ORDER, QuerySortOrder.ASC));
                } else {
                    queryExpress.add(QueryCriterions.lt(AnimeTVField.DISPLAY_ORDER, animeTV.getDisplay_order()))
                            .add(QuerySort.add(AnimeTVField.DISPLAY_ORDER, QuerySortOrder.DESC));
                }

                //第二个
                PageRows<AnimeTV> appRows = JoymeAppServiceSngl.get().queryAnimeTVByPage(queryExpress, new Pagination(1, 1, 1));
                List<Long> tagList = new ArrayList<Long>();
                tagList.add(tag_id);
                if (appRows != null && !CollectionUtil.isEmpty(appRows.getRows())) {

                    updateExpress1.set(AnimeTVField.DISPLAY_ORDER, animeTV.getDisplay_order());
                    JoymeAppServiceSngl.get().modifyAnimeTV(new QueryExpress().add(QueryCriterions.eq(AnimeTVField.TV_ID, appRows.getRows().get(0).getTv_id())), updateExpress1, tagList);

                    updateExpress2.set(AnimeTVField.DISPLAY_ORDER, appRows.getRows().get(0).getDisplay_order());
                    JoymeAppServiceSngl.get().modifyAnimeTV(new QueryExpress().add(QueryCriterions.eq(AnimeTVField.TV_ID, animeTV.getTv_id())), updateExpress2, tagList);
                }

            } else {
                int sortNum = Integer.valueOf(sortnum);
                //第一个
                AnimeTV animeTV = JoymeAppServiceSngl.get().getAnimeTV(new QueryExpress().add(QueryCriterions.eq(AnimeTVField.TV_ID, tv_id)));

                System.out.println("--------" + animeTV.getDisplay_order());

                if (desc.equals("up")) {
                    queryExpress.add(QueryCriterions.gt(AnimeTVField.DISPLAY_ORDER, animeTV.getDisplay_order()))
                            .add(QuerySort.add(AnimeTVField.DISPLAY_ORDER, QuerySortOrder.ASC));
                } else {
                    queryExpress.add(QueryCriterions.lt(AnimeTVField.DISPLAY_ORDER, animeTV.getDisplay_order()))
                            .add(QuerySort.add(AnimeTVField.DISPLAY_ORDER, QuerySortOrder.DESC));
                }

                List<Long> tagList = new ArrayList<Long>();
                tagList.add(tag_id);


                PageRows<AnimeTV> appRows = JoymeAppServiceSngl.get().queryAnimeTVByPage(queryExpress, new Pagination(sortNum, 1, sortNum));

                if (appRows != null && !CollectionUtil.isEmpty(appRows.getRows())) {
                    int listsize = appRows.getRows().size();
                    updateExpress1.set(AnimeTVField.DISPLAY_ORDER, animeTV.getDisplay_order());
                    JoymeAppServiceSngl.get().modifyAnimeTV(new QueryExpress().add(QueryCriterions.eq(AnimeTVField.TV_ID, appRows.getRows().get(0).getTv_id())), updateExpress1, tagList);

                    updateExpress2.set(AnimeTVField.DISPLAY_ORDER, appRows.getRows().get(listsize - 1).getDisplay_order());
                    JoymeAppServiceSngl.get().modifyAnimeTV(new QueryExpress().add(QueryCriterions.eq(AnimeTVField.TV_ID, animeTV.getTv_id())), updateExpress2, tagList);

                    for (int i = 0; i < listsize - 1; i++) {
                        UpdateExpress updateExpress3 = new UpdateExpress();
                        updateExpress3.set(AnimeTVField.DISPLAY_ORDER, appRows.getRows().get(i).getDisplay_order());
                        JoymeAppServiceSngl.get().modifyAnimeTV(new QueryExpress().add(QueryCriterions.eq(AnimeTVField.TV_ID, appRows.getRows().get(i + 1).getTv_id())), updateExpress3, tagList);
                        //AnimeTV animeTV1 = appRows.getRows().get(i);
                        //System.out.println(animeTV1.getTv_id() + "-----" + animeTV1.getTv_name() + "-----" + animeTV1.getDisplay_order());
                    }
                }
            }

            writeToolsLog(LogOperType.ANIME_TV_SORT, "排序:tv_id:" + tv_id + ",tagid:" + tag_id + ",order:" + desc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView("redirect:/joymeapp/anime/tv/list", mapMessage);
    }


    @ResponseBody
    @RequestMapping(value = "/get")
    public String get(@RequestParam(value = "url", required = false) String url,
                      HttpServletRequest request,
                      HttpServletResponse response) {
        String json = "";
        try {
            json = HttpUtilGetURL.httpGet(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JsonBinder.buildNormalBinder().toJson(json);
    }
}
