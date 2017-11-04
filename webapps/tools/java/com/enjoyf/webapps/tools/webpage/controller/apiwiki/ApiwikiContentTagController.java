package com.enjoyf.webapps.tools.webpage.controller.apiwiki;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.ask.AskServiceSngl;
import com.enjoyf.platform.service.ask.wiki.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhimingli on 2017-3-27 0027.
 */

@Controller
@RequestMapping(value = "/apiwiki/tag")
public class ApiwikiContentTagController extends ToolsBaseController {


    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "20") int pageSize,
                             @RequestParam(value = "removestatus", required = false, defaultValue = "") String removeStatus,
                             HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("removestatus", removeStatus);
        mapMessage.put("pageStartIndex", pageStartIndex);
        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
        QueryExpress queryExpress = new QueryExpress();
        try {
            queryExpress.add(QuerySort.add(ContentTagField.DISPLAYORDER, QuerySortOrder.DESC));
            queryExpress.add(QueryCriterions.eq(ContentTagField.TAGLINE, ContentTagLine.RECOMMEND.getCode()));
            if (!StringUtil.isEmpty(removeStatus)) {
                queryExpress.add(QueryCriterions.eq(AdvertiseField.REMOVE_STATUS, ValidStatus.getByCode(removeStatus).getCode()));
            }

            PageRows<ContentTag> pageRows = AskServiceSngl.get().queryContentTag(queryExpress, pagination);
            mapMessage.put("list", pageRows.getRows());
            mapMessage.put("page", pageRows.getPage());
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }
        return new ModelAndView("/apiwiki/tag/taglist", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createPage(HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView("/apiwiki/tag/createpage", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "name", required = false) String name,
                               @RequestParam(value = "target", required = false) String target,
                               HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        ContentTag contentTag = new ContentTag();
        try {
            contentTag.setName(name);
            contentTag.setTarget(target);
            contentTag.setCreateDate(new Date());
            contentTag.setTagType(ContentTagType.WAP);
            contentTag.setDisplayOrder(System.currentTimeMillis());
            AskServiceSngl.get().postContentTag(contentTag);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }
        return new ModelAndView("redirect:/apiwiki/tag/list");
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyPage(@RequestParam(value = "id", required = false) Long id,
                                   HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            ContentTag contentTag = AskServiceSngl.get().getContentTag(id);
            mapMessage.put("contentTag", contentTag);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/apiwiki/tag/modifypage", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "name", required = false) String name,
                               @RequestParam(value = "id", required = false) Long id,
                               @RequestParam(value = "target", required = false) String target, HttpServletRequest request) {
        UpdateExpress updateExpress = new UpdateExpress();
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            updateExpress.set(ContentTagField.NAME, name);
            updateExpress.set(AdvertiseField.TARGET, target);
            AskServiceSngl.get().updateContentTag(id, updateExpress);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }
        return new ModelAndView("redirect:/apiwiki/tag/list", map);
    }

    @RequestMapping(value = "/updatestatus")
    public ModelAndView updatestatus(@RequestParam(value = "id", required = false) Long id,
                                     @RequestParam(value = "status", required = false, defaultValue = "") String status,
                                     HttpServletRequest request) {
        UpdateExpress updateExpress = new UpdateExpress();
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            updateExpress.set(ContentTagField.VALIDSTATUS, ValidStatus.getByCode(status).getCode());
            AskServiceSngl.get().updateContentTag(id, updateExpress);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }
        return new ModelAndView("redirect:/apiwiki/tag/list", map);
    }


    @ResponseBody
    @RequestMapping(value = "/sort")
    public String sort(@RequestParam(value = "sort", required = true) String sort,
                       @RequestParam(value = "id", required = true) Long id,
                       @RequestParam(value = "displayorder", required = true) Long displayorder,
                       @RequestParam(value = "otherid", required = true) String other_id,
                       @RequestParam(value = "otherdisplayorder", required = true) String other_displayorder,
                       @RequestParam(value = "status", required = false, defaultValue = "") String status,
                       HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            if (StringUtil.isEmpty(other_id)) {
                QueryExpress queryExpress = new QueryExpress();
                if (!StringUtil.isEmpty(status)) {
                    queryExpress.add(QueryCriterions.eq(ContentTagField.VALIDSTATUS, ValidStatus.getByCode(status).getCode()));
                }
                //
                if (sort.equals("down")) {
                    queryExpress.add(QueryCriterions.lt(ContentTagField.DISPLAYORDER, displayorder));
                    queryExpress.add(QuerySort.add(ContentTagField.DISPLAYORDER, QuerySortOrder.DESC));
                } else {
                    queryExpress.add(QueryCriterions.gt(ContentTagField.DISPLAYORDER, displayorder));
                    queryExpress.add(QuerySort.add(ContentTagField.DISPLAYORDER, QuerySortOrder.ASC));
                }
                Pagination pagination = new Pagination(1, 1, 1);
                PageRows<ContentTag> pageRows = AskServiceSngl.get().queryContentTag(queryExpress, pagination);

                if (!CollectionUtil.isEmpty(pageRows.getRows())) {
                    other_id = String.valueOf(pageRows.getRows().get(0).getId());
                    other_displayorder = String.valueOf(pageRows.getRows().get(0).getDisplayOrder());
                }

            }

            if (!StringUtil.isEmpty(other_id)) {
                AskServiceSngl.get().updateContentTag(id, new UpdateExpress().set(ContentTagField.DISPLAYORDER, Long.valueOf(other_displayorder)));
                AskServiceSngl.get().updateContentTag(Long.valueOf(other_id), new UpdateExpress().set(ContentTagField.DISPLAYORDER, displayorder));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultCodeConstants.SUCCESS.getJsonString();
    }


}
