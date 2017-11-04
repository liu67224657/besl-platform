package com.enjoyf.webapps.tools.webpage.controller.joymeapp.anime;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.joymeapp.AnimeSpecial;
import com.enjoyf.platform.service.joymeapp.AnimeSpecialField;
import com.enjoyf.platform.service.joymeapp.AnimeSpecialItem;
import com.enjoyf.platform.service.joymeapp.AnimeSpecialItemField;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTV;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTVField;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.service.rpc.RPC;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.http.URLUtils;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 14-10-27
 * Time: 上午11:44
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/anime/special/item")
public class AnimeSpecialItemController extends ToolsBaseController {
    private static String APPKEY = "0G30ZtEkZ4vFBhAfN7Bx4v"; //海贼迷的APPKEY

    @RequestMapping(value = "list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                             @RequestParam(value = "specialid", required = false) Long specialId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            AnimeSpecial animeSpecial = JoymeAppServiceSngl.get().getAnimeSpecial(new QueryExpress().add(QueryCriterions.eq(AnimeSpecialField.SPECIAL_ID, specialId)));
            int curPage = (pageStartIndex / pageSize) + 1;
            Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(AnimeSpecialItemField.SPECIAL_ID, specialId));
            queryExpress.add(QueryCriterions.ne(AnimeSpecialField.REMOVE_STATUS, ValidStatus.REMOVED.getCode()));
            queryExpress.add(QuerySort.add(AnimeSpecialItemField.DISPLAY_ORDER, QuerySortOrder.ASC));

            PageRows<AnimeSpecialItem> pageRows = JoymeAppServiceSngl.get().queryAnimeSpecialItemByPage(queryExpress, pagination);

            List<AnimeSpecialItem> list = pageRows.getRows();
            for (int i = 0; i < list.size(); i++) {
                AnimeSpecialItem animeSpecialItem = list.get(i);
                animeSpecialItem.setPic(URLUtils.getJoymeDnUrl(animeSpecialItem.getPic()));
                list.set(i, animeSpecialItem);
            }

            mapMessage.put("list", list);
            mapMessage.put("page", pageRows.getPage());
            mapMessage.put("specialId", specialId);
            mapMessage.put("animeSpecial", animeSpecial);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMsg", "system.error");
        }

        return new ModelAndView("/joymeapp/anime/special/animespecialitemlist", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createPage(@RequestParam(value = "specialid", required = false) Long specialId,
                                   @RequestParam(value = "attr", required = false) Integer attr) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("specialId", specialId);
        mapMessage.put("attr", attr);
        return new ModelAndView("/joymeapp/anime/special/createitempage", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "tvid", required = false) Long tvid,
                               @RequestParam(value = "title", required = false) String title,
                               @RequestParam(value = "linkurl", required = false) String linkurl,
                               @RequestParam(value = "desc", required = false) String desc,
                               @RequestParam(value = "pic", required = false) String pic,
                               @RequestParam(value = "specialid", required = false) Long specialid,
                               @RequestParam(value = "attr", required = false) Integer attr

    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            if (tvid != null) {
                QueryExpress queryExpress = new QueryExpress();
                queryExpress.add(QueryCriterions.eq(AnimeTVField.TV_ID, tvid));
                AnimeTV animeTV = JoymeAppServiceSngl.get().getAnimeTV(queryExpress);
                if (animeTV == null) {
                    mapMessage.put("errorMsg", "视频不存在");
                    mapMessage.put("specialId", specialid);
                    mapMessage.put("attr", attr);
                    mapMessage.put("tvid", tvid);
                    return new ModelAndView("/joymeapp/anime/special/createitempage", mapMessage);
                }
            }

            if (tvid != null) {
                QueryExpress queryExpress = new QueryExpress();
                queryExpress.add(QueryCriterions.eq(AnimeSpecialItemField.SPECIAL_ID, specialid));
                queryExpress.add(QueryCriterions.ne(AnimeSpecialItemField.REMOVE_STATUS, ValidStatus.REMOVED.getCode()));
                queryExpress.add(QueryCriterions.eq(AnimeSpecialItemField.TV_ID, tvid));
                AnimeSpecialItem animeSpecialItem = JoymeAppServiceSngl.get().getAnimeSpecialItem(queryExpress);
                if (animeSpecialItem != null) {
                    mapMessage.put("errorMsg", "视频存在.不需重复添加！");
                    mapMessage.put("specialId", specialid);
                    mapMessage.put("attr", attr);
                    mapMessage.put("tvid", tvid);
                    return new ModelAndView("/joymeapp/anime/special/createitempage", mapMessage);
                }
            }

            AnimeSpecialItem animeSpecialItem = new AnimeSpecialItem();
            animeSpecialItem.setTitle(title);
            animeSpecialItem.setDesc(desc);
            String linkUrlEncoding = "";
            if (!StringUtil.isEmpty(linkurl)) {
                linkUrlEncoding = StringUtil.getUTF8Url(linkurl);
            }
            animeSpecialItem.setLinkUrl(linkUrlEncoding);
            animeSpecialItem.setSpecialId(specialid);
            animeSpecialItem.setTvId(tvid);
            animeSpecialItem.setPic(pic);
            animeSpecialItem.setRemoveStatus(ValidStatus.INVALID);
            animeSpecialItem.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
            animeSpecialItem.setUpdateUser(getCurrentUser().getUsername());
            animeSpecialItem.setUpdateTime(new Date());
            animeSpecialItem = JoymeAppServiceSngl.get().createAnimeSpecialItem(animeSpecialItem);


            writeToolsLog(LogOperType.ANIME_SPECIAL_ITEM_ADD, "新增:specialItemId=" + animeSpecialItem.getSpecialItemId());

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMsg", "system.error");
        }
        return new ModelAndView("redirect:/anime/special/item/list?specialid=" + specialid);
    }

    @RequestMapping(value = "modifypage")
    public ModelAndView modifyPage(@RequestParam(value = "specialid", required = false) Long specialId,
                                   @RequestParam(value = "attr", required = false) Integer attr,
                                   @RequestParam(value = "itemid", required = false) Long itemid) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            mapMessage.put("specialId", specialId);
            mapMessage.put("attr", attr);
            AnimeSpecialItem animeSpecial = JoymeAppServiceSngl.get().getAnimeSpecialItem(new QueryExpress().add(QueryCriterions.eq(AnimeSpecialItemField.SPECIAL_ITEM_ID, itemid)));
            animeSpecial.setPic(URLUtils.getJoymeDnUrl(animeSpecial.getPic()));
            mapMessage.put("animeSpecialItem", animeSpecial);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMsg", "system.error");
            return new ModelAndView("/joymeapp/anime/special/item/animespeciallist", mapMessage);
        }
        return new ModelAndView("/joymeapp/anime/special/modifyitempage", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "tvid", required = false) Long tvid,
                               @RequestParam(value = "title", required = false) String title,
                               @RequestParam(value = "linkurl", required = false) String linkurl,
                               @RequestParam(value = "desc", required = false) String desc,
                               @RequestParam(value = "pic", required = false) String pic,
                               @RequestParam(value = "specialid", required = false) Long specialid,
                               @RequestParam(value = "specialitemid", required = false) Long specialItemid) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(AnimeSpecialItemField.UPDATE_TIME, new Date());
            updateExpress.set(AnimeSpecialItemField.UPDATE_USER, getCurrentUser().getUsername());
            updateExpress.set(AnimeSpecialItemField.TV_ID, tvid);
            updateExpress.set(AnimeSpecialItemField.TITLE, title);
            String linkUrlEncoding = "";
            if (!StringUtil.isEmpty(linkurl)) {
                linkUrlEncoding = StringUtil.getUTF8Url(linkurl);
            }
            updateExpress.set(AnimeSpecialItemField.LINK_URL, linkUrlEncoding);
            updateExpress.set(AnimeSpecialItemField.DESC, desc);
            updateExpress.set(AnimeSpecialItemField.PIC, pic);
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(AnimeSpecialItemField.SPECIAL_ITEM_ID, specialItemid));

            boolean bval = JoymeAppServiceSngl.get().modifyAnimeSpecialItem(queryExpress, updateExpress);
            if (bval) {
                writeToolsLog(LogOperType.ANIME_SPECIAL_ITEM_UPDATE, "更新:specialItemId=" + specialItemid);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMsg", "system.error");
            return new ModelAndView("/joymeapp/anime/special/item/animespecialitemlist", mapMessage);
        }
        return new ModelAndView("redirect:/anime/special/item/list?specialid=" + specialid);
    }


    @RequestMapping(value = "/delete")
    public ModelAndView delete(@RequestParam(value = "itemid", required = false) Long id,
                               @RequestParam(value = "specialid", required = false) Long specialId
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(AnimeSpecialItemField.SPECIAL_ITEM_ID, id));
        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(AnimeSpecialItemField.REMOVE_STATUS, ValidStatus.REMOVED.getCode());
        try {
            boolean bool = JoymeAppServiceSngl.get().modifyAnimeSpecialItem(queryExpress, updateExpress);
            if (bool) {
                writeToolsLog(LogOperType.ANIME_SPECIAL_ITEM_DETETE, "删除:specialItemId=" + id);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMsg", "system.error");
            return new ModelAndView("/joymeapp/anime/special/item/animespecialitemlist", mapMessage);
        }
        return new ModelAndView("redirect:/anime/special/item/list?specialid=" + specialId);
    }

    @RequestMapping(value = "/recover")
    public ModelAndView recover(@RequestParam(value = "id", required = false) Long id,
                                @RequestParam(value = "specialid", required = false) Long specialId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(AnimeSpecialItemField.SPECIAL_ITEM_ID, id));
        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(AnimeSpecialItemField.REMOVE_STATUS, ValidStatus.VALID.getCode());
        try {
            boolean bool = JoymeAppServiceSngl.get().modifyAnimeSpecialItem(queryExpress, updateExpress);
            if (bool) {
                writeToolsLog(LogOperType.ANIME_SPECIAL_ITEM_RECOVER, "恢复:specialItemId=" + id);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMsg", "system.error");
            return new ModelAndView("/joymeapp/anime/special/item/animespecialitemlist", mapMessage);
        }
        return new ModelAndView("redirect:/anime/special/item/list?specialid=" + specialId);
    }

    @RequestMapping(value = "/sort/{sort}")
    @ResponseBody
    public String sort(@PathVariable(value = "sort") String sort,
                       @RequestParam(value = "id", required = false) Long specialItemId,
                       @RequestParam(value = "specialid", required = false) Long specialId) {
        JsonBinder binder = JsonBinder.buildNormalBinder();
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        ResultObjectMsg resultObjectMsg = new ResultObjectMsg(ResultObjectMsg.CODE_S);
        if (specialId == null) {
            resultObjectMsg.setRs(ResultObjectMsg.CODE_E);
            return binder.toJson(resultObjectMsg);
        }
        Long returnItemId = null;
        try {
            UpdateExpress updateExpress1 = new UpdateExpress();
            UpdateExpress updateExpress2 = new UpdateExpress();
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(AnimeSpecialItemField.SPECIAL_ITEM_ID, specialItemId));
            AnimeSpecialItem animeSpecialItem = JoymeAppServiceSngl.get().getAnimeSpecialItem(queryExpress);
            if (animeSpecialItem == null) {
                return null;
            }
            queryExpress = new QueryExpress();
            if (sort.equals("up")) {
                queryExpress.add(QueryCriterions.eq(AnimeSpecialItemField.SPECIAL_ID, specialId));
                queryExpress.add(QueryCriterions.lt(AnimeSpecialItemField.DISPLAY_ORDER, animeSpecialItem.getDisplayOrder()));
                queryExpress.add(QuerySort.add(AnimeSpecialItemField.DISPLAY_ORDER, QuerySortOrder.DESC));
            } else {
                queryExpress.add(QueryCriterions.eq(AnimeSpecialItemField.SPECIAL_ID, specialId));
                queryExpress.add(QueryCriterions.gt(AnimeSpecialItemField.DISPLAY_ORDER, animeSpecialItem.getDisplayOrder()));
                queryExpress.add(QuerySort.add(AnimeSpecialItemField.DISPLAY_ORDER, QuerySortOrder.ASC));
            }

            PageRows<AnimeSpecialItem> pageRows = JoymeAppServiceSngl.get().queryAnimeSpecialItemByPage(queryExpress, new Pagination(1, 1, 1));
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                updateExpress1.set(AnimeSpecialItemField.DISPLAY_ORDER, animeSpecialItem.getDisplayOrder());
                JoymeAppServiceSngl.get().modifyAnimeSpecialItem(new QueryExpress().add(QueryCriterions.eq(AnimeSpecialItemField.SPECIAL_ITEM_ID, pageRows.getRows().get(0).getSpecialItemId())), updateExpress1);


                updateExpress2.set(AnimeSpecialItemField.DISPLAY_ORDER, pageRows.getRows().get(0).getDisplayOrder());
                JoymeAppServiceSngl.get().modifyAnimeSpecialItem(new QueryExpress().add(QueryCriterions.eq(AnimeSpecialItemField.SPECIAL_ITEM_ID, animeSpecialItem.getSpecialItemId())), updateExpress2);
                returnItemId = pageRows.getRows().get(0).getSpecialItemId();
            }
            if (returnItemId == null) {
                resultObjectMsg.setRs(ResultObjectMsg.CODE_E);
                return binder.toJson(resultObjectMsg);
            }

            writeToolsLog(LogOperType.ANIME_SPECIAL_ITEM_SORT, "排序:specialItemId=" + specialItemId + ",orderby:" + sort);
        } catch (Exception e) {
            resultObjectMsg.setRs(ResultObjectMsg.CODE_E);
            resultObjectMsg.setMsg("system.error");
            return binder.toJson(resultObjectMsg);
        }
        mapMessage.put("sort", sort);
        mapMessage.put("itemid", specialItemId);
        mapMessage.put("returnitemid", returnItemId);
        resultObjectMsg.setResult(mapMessage);
        return binder.toJson(resultObjectMsg);
    }
}
