package com.enjoyf.webapps.tools.webpage.controller.joymeapp.anime;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.joymeapp.AnimeRedirectType;
import com.enjoyf.platform.service.joymeapp.AnimeSpecial;
import com.enjoyf.platform.service.joymeapp.AnimeSpecialField;
import com.enjoyf.platform.service.joymeapp.AnimeSpecialType;
import com.enjoyf.platform.service.service.ServiceException;
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

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 14-10-27
 * Time: 上午11:44
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/anime/special")
public class AnimeSpecialController extends ToolsBaseController {
    private static String APPKEY = "0G30ZtEkZ4vFBhAfN7Bx4v"; //海贼迷的APPKEY

    private static String CODE_APPKEY = "1zBwYvQpt3AE6JsykiA2es"; //火影的APPKEY

    @RequestMapping(value = "list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                             @RequestParam(value = "appkey", required = false, defaultValue = "0G30ZtEkZ4vFBhAfN7Bx4v") String appkey,
                             @RequestParam(value = "platform", required = false) String platform) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            int curPage = (pageStartIndex / pageSize) + 1;
            Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(AnimeIndexField.APPKEY, appkey));
            queryExpress.add(QueryCriterions.ne(AnimeSpecialField.REMOVE_STATUS, ValidStatus.REMOVED.getCode()));
            if (!StringUtil.isEmpty(platform)) {
                mapMessage.put("platform", platform);
                queryExpress.add(QueryCriterions.eq(AnimeSpecialField.PLATFORM, Integer.parseInt(platform)));
            }
            queryExpress.add(QuerySort.add(AnimeSpecialField.DISPLAY_ORDER, QuerySortOrder.ASC));

            PageRows<AnimeSpecial> pageRows = JoymeAppServiceSngl.get().queryAnimeSpecialByPage(queryExpress, pagination);
            List<AnimeSpecial> list = pageRows.getRows();
            for (int i = 0; i < list.size(); i++) {
                AnimeSpecial animeSpecial = list.get(i);
                animeSpecial.setCoverPic(URLUtils.getJoymeDnUrl(animeSpecial.getCoverPic()));
                animeSpecial.setSpecialPic(URLUtils.getJoymeDnUrl(animeSpecial.getSpecialPic()));
                list.set(i, animeSpecial);
            }
            mapMessage.put("list", list);
            mapMessage.put("page", pageRows.getPage());
            mapMessage.put("appkey", appkey);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMsg", "system.error");
        }

        return new ModelAndView("/joymeapp/anime/special/animespeciallist", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createPage(@RequestParam(value = "appkey", required = false) String apppkey) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        //专题分类
        List<Integer> list = new ArrayList<Integer>();
        list.add(AnimeRedirectType.DIVERSITY.getCode());
        list.add(AnimeRedirectType.WAP_LIST.getCode());
        list.add(AnimeRedirectType.DIRECT.getCode());
        list.add(AnimeRedirectType.WAP_TEXT_LIST.getCode());
        mapMessage.put("list", list);
        // 专题属性
        mapMessage.put("specialType", AnimeSpecialType.getAll());
        mapMessage.put("specialDisplayType", AnimeSpecialDisplayType.getAll());
        mapMessage.put("appkey", apppkey);
        return new ModelAndView("/joymeapp/anime/special/createpage", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "specialname", required = false) String specialname,
                               @RequestParam(value = "coverpic", required = false) String coverpic,
                               @RequestParam(value = "specialpic", required = false) String specialpic,
                               @RequestParam(value = "specialdesc", required = false) String specialdesc,
                               @RequestParam(value = "specialtype", required = false) String specialtype,
                               @RequestParam(value = "bgcolor", required = false) String bgcolor,
                               @RequestParam(value = "platform", required = false) String platform,
                               @RequestParam(value = "specialattr", required = false) String specialAttr,
                               @RequestParam(value = "linkurl", required = false) String linkUrl,
                               @RequestParam(value = "specialtitle", required = false) String specialTitle,
                               @RequestParam(value = "appkey", required = false) String apppkey,
                               @RequestParam(value = "read_num", required = false) String read_num,
                               @RequestParam(value = "display_type", required = false, defaultValue = "1") String display_type
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            AnimeSpecial animeSpecial = new AnimeSpecial();
            animeSpecial.setCoverPic(coverpic);
            animeSpecial.setSpecialTtile(specialTitle);
            animeSpecial.setSpecialPic(specialpic);
            animeSpecial.setPlatform(Integer.parseInt(platform));
            animeSpecial.setSpecialName(specialname);
            animeSpecial.setSpecialDesc(specialdesc);
            animeSpecial.setSpecialType(AnimeSpecialType.getByCode(Integer.parseInt(specialtype)));
            animeSpecial.setCreateDate(new Date());
            animeSpecial.setCreateUser(getCurrentUser().getUsername());
            animeSpecial.setSpecialTypeBgColor(bgcolor);
            animeSpecial.setRemoveStatus(ValidStatus.INVALID);
            animeSpecial.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
            animeSpecial.setAppkey(apppkey);
            animeSpecial.setAnimeRedirectType(AnimeRedirectType.getByCode(Integer.parseInt(specialAttr)));
            if (!StringUtil.isEmpty(read_num)) {
                animeSpecial.setRead_num(Integer.valueOf(read_num));
            }
            animeSpecial.setDisplay_type(AnimeSpecialDisplayType.getByCode(Integer.valueOf(display_type)));
            String linkUrlEncoding = "";
            if (!StringUtil.isEmpty(linkUrl)) {
                linkUrlEncoding = StringUtil.getUTF8Url(linkUrl);
            }

            animeSpecial.setLinkUrl(linkUrlEncoding);

            if (platform.equals("2")) {
                animeSpecial.setPlatform(0);
                animeSpecial = JoymeAppServiceSngl.get().createAnimeSpecial(animeSpecial);
                writeToolsLog(LogOperType.ANIME_SPECIAL_ADD, "新增:specialId=" + animeSpecial.getSpecialId());
                animeSpecial.setPlatform(1);
                animeSpecial = JoymeAppServiceSngl.get().createAnimeSpecial(animeSpecial);
                writeToolsLog(LogOperType.ANIME_SPECIAL_ADD, "新增:specialId=" + animeSpecial.getSpecialId());
            } else {
                animeSpecial = JoymeAppServiceSngl.get().createAnimeSpecial(animeSpecial);
                writeToolsLog(LogOperType.ANIME_SPECIAL_ADD, "新增:specialId=" + animeSpecial.getSpecialId());
            }


        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMsg", "system.error");
        }
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("appkey", apppkey);
        return new ModelAndView("redirect:/anime/special/list", map);
    }

    @RequestMapping(value = "modifypage")
    public ModelAndView modifyPage(@RequestParam(value = "id", required = false) Long id) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            mapMessage.put("specialType", AnimeSpecialType.getAll());
            //专题分类
            List<Integer> list = new ArrayList<Integer>();
            list.add(AnimeRedirectType.DIVERSITY.getCode());
            list.add(AnimeRedirectType.WAP_LIST.getCode());
            list.add(AnimeRedirectType.DIRECT.getCode());
            list.add(AnimeRedirectType.WAP_TEXT_LIST.getCode());
            mapMessage.put("list", list);
            AnimeSpecial animeSpecial = JoymeAppServiceSngl.get().getAnimeSpecial(new QueryExpress().add(QueryCriterions.eq(AnimeSpecialField.SPECIAL_ID, id)));

            animeSpecial.setCoverPic(URLUtils.getJoymeDnUrl(animeSpecial.getCoverPic()));
            animeSpecial.setSpecialPic(URLUtils.getJoymeDnUrl(animeSpecial.getSpecialPic()));

            mapMessage.put("animeSpecial", animeSpecial);
            mapMessage.put("specialDisplayType", AnimeSpecialDisplayType.getAll());
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMsg", "system.error");
            return new ModelAndView("/joymeapp/anime/special/animespeciallist", mapMessage);
        }
        return new ModelAndView("/joymeapp/anime/special/modifypage", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "specialname", required = false) String specialname,
                               @RequestParam(value = "coverpic", required = false) String coverpic,
                               @RequestParam(value = "specialpic", required = false) String specialpic,
                               @RequestParam(value = "specialdesc", required = false) String specialdesc,
                               @RequestParam(value = "specialtype", required = false) String specialtype,
                               @RequestParam(value = "bgcolor", required = false) String bgcolor,
                               @RequestParam(value = "platform", required = false) String platform,
                               @RequestParam(value = "specialid", required = false) String id,
                               @RequestParam(value = "specialattr", required = false) String specialAttr,
                               @RequestParam(value = "linkurl", required = false) String linkUrl,
                               @RequestParam(value = "appkey", required = false) String appkey,
                               @RequestParam(value = "specialtitle", required = false) String specialTitle,
                               @RequestParam(value = "read_num", required = false) String read_num,
                               @RequestParam(value = "display_type", required = false) String display_type) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(AnimeSpecialField.UPDATE_DATE, new Date());
            updateExpress.set(AnimeSpecialField.SPECIAL_NAME, specialname);
            updateExpress.set(AnimeSpecialField.COVER_PIC, coverpic);
            updateExpress.set(AnimeSpecialField.SPECIAL_PIC, specialpic);
            updateExpress.set(AnimeSpecialField.SPECIAL_DESC, specialdesc);
            updateExpress.set(AnimeSpecialField.SPECIAL_TYPE, Integer.parseInt(specialtype));
            updateExpress.set(AnimeSpecialField.BG_COLOR, bgcolor);
            updateExpress.set(AnimeSpecialField.PLATFORM, Integer.parseInt(platform));
            updateExpress.set(AnimeSpecialField.SPECIAL_ATTR, Integer.parseInt(specialAttr));

            updateExpress.set(AnimeSpecialField.SPECIAL_TTILE, specialTitle);
            updateExpress.set(AnimeSpecialField.READ_NUM, Integer.valueOf(read_num));
            updateExpress.set(AnimeSpecialField.DISPLAY_TYPE, Integer.valueOf(display_type));
            String linkUrlEncoding = "";
            if (!StringUtil.isEmpty(linkUrl)) {
                linkUrlEncoding = StringUtil.getUTF8Url(linkUrl);
            }
            updateExpress.set(AnimeSpecialField.LINKURL, linkUrlEncoding);
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(AnimeSpecialField.SPECIAL_ID, Long.parseLong(id)));

            boolean bval = JoymeAppServiceSngl.get().modifyAnimeSpecial(queryExpress, updateExpress);
            if (bval) {
                writeToolsLog(LogOperType.ANIME_SPECIAL_UPDATE, "更新:specialId=" + id);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMsg", "system.error");
            return new ModelAndView("/joymeapp/anime/special/animespeciallist", mapMessage);
        }
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("appkey", appkey);
        return new ModelAndView("redirect:/anime/special/list", map);
    }


    @RequestMapping(value = "/delete")
    public ModelAndView delete(@RequestParam(value = "id", required = false) String id,
                               @RequestParam(value = "appkey", required = false) String appkey) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(AnimeSpecialField.SPECIAL_ID, Long.parseLong(id)));
        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(AnimeSpecialField.REMOVE_STATUS, ValidStatus.REMOVED.getCode());
        try {
            boolean bool = JoymeAppServiceSngl.get().modifyAnimeSpecial(queryExpress, updateExpress);
            if (bool) {
                writeToolsLog(LogOperType.ANIME_SPECIAL_DETETE, "删除:specialId=" + id);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMsg", "system.error");
            return new ModelAndView("/joymeapp/anime/special/animespeciallist", mapMessage);
        }
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("appkey", appkey);
        return new ModelAndView("redirect:/anime/special/list", map);
    }

    @RequestMapping(value = "/recover")
    public ModelAndView recover(@RequestParam(value = "id", required = false) String id,
                                @RequestParam(value = "appkey", required = false) String appkey) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(AnimeSpecialField.SPECIAL_ID, Long.parseLong(id)));
        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(AnimeSpecialField.REMOVE_STATUS, ValidStatus.VALID.getCode());
        try {
            boolean bool = JoymeAppServiceSngl.get().modifyAnimeSpecial(queryExpress, updateExpress);
            if (bool) {
                writeToolsLog(LogOperType.ANIME_SPECIAL_RECOVER, "恢复:specialId=" + id);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMsg", "system.error");
            return new ModelAndView("/joymeapp/anime/special/animespeciallist", mapMessage);
        }
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("appkey", appkey);
        return new ModelAndView("redirect:/anime/special/list", map);
    }

    @RequestMapping(value = "/sort/{sort}")
    @ResponseBody
    public String sort(@PathVariable(value = "sort") String sort,
                       @RequestParam(value = "id", required = false) Long specialId,
                       @RequestParam(value = "appkey", required = false) String appkey,
                       @RequestParam(value = "platform", required = false) String platform) {
        JsonBinder binder = JsonBinder.buildNormalBinder();
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        ResultObjectMsg resultObjectMsg = new ResultObjectMsg(ResultObjectMsg.CODE_S);
        if (specialId == null) {
            resultObjectMsg.setRs(ResultObjectMsg.CODE_E);
            return binder.toJson(resultObjectMsg);
        }
        if (StringUtil.isEmpty(platform)) {
            resultObjectMsg.setRs(ResultObjectMsg.CODE_E);
            return binder.toJson(resultObjectMsg);
        }
        Long returnItemId = null;
        try {
            UpdateExpress updateExpress1 = new UpdateExpress();
            UpdateExpress updateExpress2 = new UpdateExpress();
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(AnimeSpecialField.SPECIAL_ID, specialId));
            queryExpress.add(QueryCriterions.eq(AnimeSpecialField.APPKEY, appkey));

            AnimeSpecial animeSpecial = JoymeAppServiceSngl.get().getAnimeSpecial(new QueryExpress().add(QueryCriterions.eq(AnimeSpecialField.SPECIAL_ID, specialId)));
            if (animeSpecial == null) {
                return null;
            }
            queryExpress = new QueryExpress();
            if (sort.equals("up")) {
                queryExpress.add(QueryCriterions.eq(AnimeSpecialField.PLATFORM, Integer.parseInt(platform)));
                queryExpress.add(QueryCriterions.lt(ClientLineItemField.DISPLAY_ORDER, animeSpecial.getDisplayOrder()));
                queryExpress.add(QuerySort.add(ClientLineItemField.DISPLAY_ORDER, QuerySortOrder.DESC));
                queryExpress.add(QueryCriterions.ne(AnimeSpecialField.REMOVE_STATUS, ValidStatus.REMOVED.getCode()));
            } else {
                queryExpress.add(QueryCriterions.eq(AnimeSpecialField.PLATFORM, Integer.parseInt(platform)));
                queryExpress.add(QueryCriterions.gt(ClientLineItemField.DISPLAY_ORDER, animeSpecial.getDisplayOrder()));
                queryExpress.add(QuerySort.add(ClientLineItemField.DISPLAY_ORDER, QuerySortOrder.ASC));
                queryExpress.add(QueryCriterions.ne(AnimeSpecialField.REMOVE_STATUS, ValidStatus.REMOVED.getCode()));
            }

            PageRows<AnimeSpecial> pageRows = JoymeAppServiceSngl.get().queryAnimeSpecialByPage(queryExpress, new Pagination(1, 1, 1));
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                updateExpress1.set(AnimeSpecialField.DISPLAY_ORDER, animeSpecial.getDisplayOrder());
                JoymeAppServiceSngl.get().modifyAnimeSpecial(new QueryExpress().add(QueryCriterions.eq(AnimeSpecialField.SPECIAL_ID, pageRows.getRows().get(0).getSpecialId())), updateExpress1);


                updateExpress2.set(AnimeSpecialField.DISPLAY_ORDER, pageRows.getRows().get(0).getDisplayOrder());
                JoymeAppServiceSngl.get().modifyAnimeSpecial(new QueryExpress().add(QueryCriterions.eq(AnimeSpecialField.SPECIAL_ID, animeSpecial.getSpecialId())), updateExpress2);
                returnItemId = pageRows.getRows().get(0).getSpecialId();
            }
            if (returnItemId == null) {
                resultObjectMsg.setRs(ResultObjectMsg.CODE_E);
                return binder.toJson(resultObjectMsg);
            }


            writeToolsLog(LogOperType.ANIME_SPECIAL_SORT, "排序:specialId=" + specialId + ",orderby:" + sort);
        } catch (Exception e) {
            resultObjectMsg.setRs(ResultObjectMsg.CODE_E);
            resultObjectMsg.setMsg("system.error");
            return binder.toJson(resultObjectMsg);
        }
        mapMessage.put("sort", sort);
        mapMessage.put("itemid", specialId);
        mapMessage.put("returnitemid", returnItemId);
        resultObjectMsg.setResult(mapMessage);
        return binder.toJson(resultObjectMsg);
    }
}
