package com.enjoyf.webapps.tools.webpage.controller.joymeapp.socialclient;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.content.ContentServiceSngl;
import com.enjoyf.platform.service.content.social.SocialContent;
import com.enjoyf.platform.service.content.social.SocialHotContent;
import com.enjoyf.platform.service.content.social.SocialHotContentField;
import com.enjoyf.platform.service.joymeapp.ActivityTopMenuField;
import com.enjoyf.platform.service.profile.ProfileServiceSngl;
import com.enjoyf.platform.service.profile.socialclient.SocialProfile;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 14-4-19
 * Time: 下午8:33
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/joymeapp/socialclient/hot/content")
public class SocialHotContentController extends ToolsBaseController {
    @Resource(name = "i18nSource")
    private ResourceBundleMessageSource i18nSource;

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                             @RequestParam(value = "status", required = false) String status,
                             @RequestParam(value = "screenname", required = false) String screenName) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QuerySort.add(SocialHotContentField.DISPLAY_ORDER, QuerySortOrder.DESC));
        if (!StringUtil.isEmpty(status)) {
            mapMessage.put("status", status);
            queryExpress.add(QueryCriterions.eq(SocialHotContentField.REMOVE_STATUS, status));
        } else {
            queryExpress.add(QueryCriterions.eq(SocialHotContentField.REMOVE_STATUS, ActStatus.UNACT.getCode()));
        }
        try {
            if (!StringUtil.isEmpty(screenName)) {
                SocialProfile profile = ProfileServiceSngl.get().getSocialProfileByScreenName(screenName);
                if (profile == null) {
                    return new ModelAndView("/joymeapp/socialclient/hotcontent/hotcontentlist", mapMessage);
                }
                mapMessage.put("screenname", screenName);
                queryExpress.add(QueryCriterions.eq(SocialHotContentField.UNO, profile.getUno()));
            }

            PageRows<SocialHotContent> pageRows = ContentServiceSngl.get().querySocialHotContentPageRows(queryExpress, pagination);
            Map<String, SocialProfile> mapProfile = new HashMap<String, SocialProfile>();
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                List<SocialHotContent> list = pageRows.getRows();
                Set<String> unos = new HashSet<String>();
                Set<Long> idSet = new HashSet<Long>();
                for (SocialHotContent hotContent : pageRows.getRows()) {
                    if (!StringUtil.isEmpty(hotContent.getUno())) {
                        unos.add(hotContent.getUno());
                    }
                    idSet.add(hotContent.getContentId());
                }
                mapProfile = ProfileServiceSngl.get().querySocialProfilesByUnosMap(unos);
                Map<Long, SocialContent> contentMap = ContentServiceSngl.get().querySocialContentMapByIdSet(idSet);
                mapMessage.put("list", pageRows.getRows());
                mapMessage.put("page", pageRows.getPage());
                mapMessage.put("mapprofile", mapProfile);
                mapMessage.put("contentMap", contentMap);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/joymeapp/socialclient/hotcontent/hotcontentlist", mapMessage);
        }
        return new ModelAndView("/joymeapp/socialclient/hotcontent/hotcontentlist", mapMessage);
    }

    @RequestMapping(value = "/delete")
    public ModelAndView modify(@RequestParam(value = "cid", required = false) String cid,
                               @RequestParam(value = "curpage", required = false) String curPage) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        int pager = 0;
        try {

            pager = (Integer.parseInt(curPage) - 1) * 40;
            ContentServiceSngl.get().modifySocialHotContent(new UpdateExpress().set(SocialHotContentField.REMOVE_STATUS, ActStatus.ACTED.getCode()), Long.parseLong(cid));

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("joymeapp/socialclient/hotcontent/hotcontentlist", mapMessage);
        }


        return new ModelAndView("redirect:/joymeapp/socialclient/hot/content/list?pager.offset=" + pager);
    }


    @RequestMapping(value = "/sort/{sort}")
    public ModelAndView sort(@PathVariable(value = "sort") String sort,
                             @RequestParam(value = "offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                             @RequestParam(value = "cid", required = false) Long cid,
                             @RequestParam(value = "sortnum", required = false, defaultValue = "0") Integer sortNum,
                             HttpServletResponse response, HttpServletRequest request
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
        mapMessage.put("page", pagination);
        if (sortNum == 0) {
            JsonBinder binder = JsonBinder.buildNormalBinder();

            ResultObjectMsg resultObjectMsg = new ResultObjectMsg(ResultObjectMsg.CODE_S);

            UpdateExpress updateExpress1 = new UpdateExpress();
            UpdateExpress updateExpress2 = new UpdateExpress();
            QueryExpress queryExpress = new QueryExpress();

            try {
                SocialHotContent socialHotContent = ContentServiceSngl.get().getSocialHotContent(cid);
                if (socialHotContent == null) {
                    resultObjectMsg.setRs(ResultObjectMsg.CODE_E);
                    HTTPUtil.writeJson(response, binder.toJson(resultObjectMsg));
                    return null;
                }
                queryExpress.add(QueryCriterions.eq(SocialHotContentField.REMOVE_STATUS, ActStatus.UNACT.getCode()));
                if (sort.equals("up")) {
                    queryExpress.add(QueryCriterions.gt(SocialHotContentField.DISPLAY_ORDER, socialHotContent.getDisplayOrder()));
                    queryExpress.add(QuerySort.add(SocialHotContentField.DISPLAY_ORDER, QuerySortOrder.ASC));
                } else {
                    queryExpress.add(QueryCriterions.lt(SocialHotContentField.DISPLAY_ORDER, socialHotContent.getDisplayOrder()));
                    queryExpress.add(QuerySort.add(SocialHotContentField.DISPLAY_ORDER, QuerySortOrder.DESC));
                }

                List<SocialHotContent> list = ContentServiceSngl.get().querySocialHotContent(queryExpress);
                if (!CollectionUtil.isEmpty(list)) {
                    updateExpress1.set(ActivityTopMenuField.DISPLAY_ORDER, socialHotContent.getDisplayOrder());
                    ContentServiceSngl.get().modifySocialHotContent(updateExpress1, list.get(0).getContentId());

                    updateExpress2.set(ActivityTopMenuField.DISPLAY_ORDER, list.get(0).getDisplayOrder());
                    ContentServiceSngl.get().modifySocialHotContent(updateExpress2, socialHotContent.getContentId());
                } else {
                    resultObjectMsg.setRs(ResultObjectMsg.CODE_E);
                    HTTPUtil.writeJson(response, binder.toJson(resultObjectMsg));
                    return null;
                }
                mapMessage.put("sort", sort);
                mapMessage.put("cid", cid);
                if (!CollectionUtil.isEmpty(list)) {
                    mapMessage.put("recid", list.get(0).getContentId());
                }

                resultObjectMsg.setResult(mapMessage);
                HTTPUtil.writeJson(response, binder.toJson(resultObjectMsg));
                return null;
            } catch (Exception e) {
                resultObjectMsg.setRs(ResultObjectMsg.CODE_E);
                resultObjectMsg.setMsg(i18nSource.getMessage("system.error", null, null));
                return new ModelAndView("forward:/joymeapp/socialclient/hot/content/list", mapMessage);
            }
        } else {
            QueryExpress queryExpress = new QueryExpress();
            try {
                SocialHotContent socialHotContent = ContentServiceSngl.get().getSocialHotContent(cid);
                if (socialHotContent == null) {
                    return new ModelAndView("forward:/joymeapp/socialclient/hot/content/list", mapMessage);
                }
                queryExpress.add(QueryCriterions.eq(SocialHotContentField.REMOVE_STATUS, ActStatus.UNACT.getCode()));
                if (sort.equals("up")) {
                    queryExpress.add(QueryCriterions.gt(SocialHotContentField.DISPLAY_ORDER, socialHotContent.getDisplayOrder()));
                    queryExpress.add(QuerySort.add(SocialHotContentField.DISPLAY_ORDER, QuerySortOrder.ASC));
                } else {
                    queryExpress.add(QueryCriterions.lt(SocialHotContentField.DISPLAY_ORDER, socialHotContent.getDisplayOrder()));
                    queryExpress.add(QuerySort.add(SocialHotContentField.DISPLAY_ORDER, QuerySortOrder.DESC));
                }

                PageRows<SocialHotContent> pageRows = ContentServiceSngl.get().querySocialHotContentPageRows(queryExpress, new Pagination(sortNum, 1, sortNum));
                if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                    UpdateExpress updateExpress1 = new UpdateExpress();
                    updateExpress1.set(ActivityTopMenuField.DISPLAY_ORDER, socialHotContent.getDisplayOrder());
                    ContentServiceSngl.get().modifySocialHotContent(updateExpress1, pageRows.getRows().get(0).getContentId());

                    UpdateExpress updateExpress2 = new UpdateExpress();
                    updateExpress2.set(ActivityTopMenuField.DISPLAY_ORDER, pageRows.getRows().get(sortNum - 1).getDisplayOrder());
                    ContentServiceSngl.get().modifySocialHotContent(updateExpress2, socialHotContent.getContentId());

                    for (int i = 0; i < sortNum-1; i++) {
                        UpdateExpress updateExpress3 = new UpdateExpress();
                        updateExpress3.set(ActivityTopMenuField.DISPLAY_ORDER, pageRows.getRows().get(i).getDisplayOrder());
                        ContentServiceSngl.get().modifySocialHotContent(updateExpress3, pageRows.getRows().get(i + 1).getContentId());
                    }
                }
            } catch (Exception e) {
                GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
                mapMessage = putErrorMessage(mapMessage, "system.error");
                return new ModelAndView("forward:/joymeapp/socialclient/hot/content/list", mapMessage);
            }
            return new ModelAndView("forward:/joymeapp/socialclient/hot/content/list", mapMessage);
        }

    }

}
