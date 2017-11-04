package com.enjoyf.webapps.tools.webpage.controller.apiwiki;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.ask.AskServiceSngl;
import com.enjoyf.platform.service.ask.AskUtil;
import com.enjoyf.platform.service.ask.JoymewikiJt;
import com.enjoyf.platform.service.ask.wiki.Advertise;
import com.enjoyf.platform.service.ask.wiki.AdvertiseDomain;
import com.enjoyf.platform.service.ask.wiki.AdvertiseField;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.oauth.weibo4j.model.Query;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhimingli on 2017-3-27 0027.
 */

@Controller
@RequestMapping(value = "/apiwiki/slide")
public class ApiwikiSlideshowController extends ToolsBaseController {

    private static String APPKEY = "2ojbX21Pd7WqJJRWmIniM0";

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "20") int pageSize,
                             @RequestParam(value = "removestatus", required = false) String removeStatus,
                             @RequestParam(value = "platform", required = false, defaultValue = "") String platform,
                             HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("removestatus", removeStatus);
        mapMessage.put("platform", platform);
        mapMessage.put("pageStartIndex", pageStartIndex);
        mapMessage.put("now", new Date());
        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
        QueryExpress queryExpress = new QueryExpress();
        try {
            queryExpress.add(QuerySort.add(AdvertiseField.DISPLAY_ORDER, QuerySortOrder.DESC));
            queryExpress.add(QueryCriterions.eq(AdvertiseField.DOMAIN, AdvertiseDomain.CAROUSEL.getCode()));

            ValidStatus validStatus = ValidStatus.getByCode(removeStatus);
            if (validStatus != null) {
                queryExpress.add(QueryCriterions.eq(AdvertiseField.REMOVE_STATUS, validStatus.getCode()));
            }

            if (!StringUtil.isEmpty(platform)) {
                queryExpress.add(QueryCriterions.eq(AdvertiseField.PLATFORM, Integer.valueOf(platform)));
            }
            PageRows<Advertise> pageRows = AskServiceSngl.get().queryAdvertise(queryExpress, pagination);
            mapMessage.put("list", pageRows.getRows());
            mapMessage.put("page", pageRows.getPage());
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }
        return new ModelAndView("/apiwiki/slide/slidelist", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createPage(HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            mapMessage.put("joymeWikiJt", JoymewikiJt.getToolsAll());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView("/apiwiki/slide/createpage", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "title", required = false) String title,
                               @RequestParam(value = "description", required = false) String description,
                               @RequestParam(value = "type", required = false) Integer type,
                               @RequestParam(value = "target", required = false) String target,
                               @RequestParam(value = "pic", required = false) String pic,
                               @RequestParam(value = "shortlinkurl", required = false, defaultValue = "0") Integer shortlinkurl,
                               @RequestParam(value = "platform", required = false, defaultValue = "0") Integer platform,
                               @RequestParam(value = "starttime", required = false) String startTime,
                               @RequestParam(value = "endtime", required = false) String endTime,
                               HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        Advertise advertise = new Advertise();
        try {
            if (target.contains("http") && JoymewikiJt.WAP_WIKI.getCode() == type) {
                target = AskUtil.handleUrl(target);
            }
            //采用短链接
            if (shortlinkurl == 1) {
                target = ShortUrlUtils.getSinaURL(target);
            }
            Date startDate = DateUtil.formatStringToDate(startTime, "yyyy-MM-dd HH:mm:ss");
            Date endDate = DateUtil.formatStringToDate(endTime, "yyyy-MM-dd HH:mm:ss");


            advertise.setAppkey(APPKEY);
            advertise.setTitle(title);
            advertise.setDescription(description);
            advertise.setDomain(AdvertiseDomain.CAROUSEL);
            advertise.setType(type);
            advertise.setTarget(target);
            advertise.setPic(pic);
            advertise.setDisplayOrder(System.currentTimeMillis());
            advertise.setCreateDate(new Date());
            advertise.setRemoveStatus(ValidStatus.INVALID);
            advertise.setStartTime(startDate);
            advertise.setEndTime(endDate);
            //双端
            if (platform == -1) {
                advertise.setPlatform(1);
                AskServiceSngl.get().postAdvertise(advertise);

                advertise.setPlatform(0);
                advertise.setDisplayOrder(System.currentTimeMillis());
                AskServiceSngl.get().postAdvertise(advertise);
            } else {
                advertise.setPlatform(platform);
                AskServiceSngl.get().postAdvertise(advertise);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }
        return new ModelAndView("redirect:/apiwiki/slide/list");
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyPage(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "error", required = false) String error,
            HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Advertise advertise = AskServiceSngl.get().getAdvertise(id);
            mapMessage.put("advertise", advertise);
            mapMessage.put("startTime", advertise.getStartTime() == null ? "" : simpleDateFormat.format(advertise.getStartTime()));
            mapMessage.put("endTime", advertise.getEndTime() == null ? "" : simpleDateFormat.format(advertise.getEndTime()));
            mapMessage.put("joymeWikiJt", JoymewikiJt.getToolsAll());
            if (!StringUtil.isEmpty(error)) {
                mapMessage = putErrorMessage(mapMessage, error);
            }

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/apiwiki/slide/modifypage", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "id", required = false) Long id,
                               @RequestParam(value = "title", required = false) String title,
                               @RequestParam(value = "description", required = false) String description,
                               @RequestParam(value = "platform", required = false, defaultValue = "0") Integer platform,
                               @RequestParam(value = "type", required = false) Integer type,
                               @RequestParam(value = "target", required = false) String target,
                               @RequestParam(value = "shortlinkurl", required = false, defaultValue = "0") Integer shortlinkurl,
                               @RequestParam(value = "starttime", required = false) String startTime,
                               @RequestParam(value = "endtime", required = false) String endTime,
                               @RequestParam(value = "pic", required = false) String pic, HttpServletRequest request) {
        UpdateExpress updateExpress = new UpdateExpress();
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            if (target.contains("http") && JoymewikiJt.WAP_WIKI.getCode() == type) {
                target = AskUtil.handleUrl(target);
            }
            //采用短链接
            if (shortlinkurl == 1) {
                target = ShortUrlUtils.getSinaURL(target);
            }
            Date startDate = DateUtil.formatStringToDate(startTime, "yyyy-MM-dd HH:mm:ss");
            Date endDate = DateUtil.formatStringToDate(endTime, "yyyy-MM-dd HH:mm:ss");


            updateExpress.set(AdvertiseField.TITLE, title);
            updateExpress.set(AdvertiseField.DESCRIPTION, description);
            updateExpress.set(AdvertiseField.TYPE, type);
            updateExpress.set(AdvertiseField.TARGET, target);
            updateExpress.set(AdvertiseField.PLATFORM, platform);
            updateExpress.set(AdvertiseField.PIC, pic);
            updateExpress.set(AdvertiseField.START_TIME, startDate);
            updateExpress.set(AdvertiseField.END_TIME, endDate);

            boolean bool = AskServiceSngl.get().updateAdvertise(id, updateExpress);
            if (bool) {
                AskServiceSngl.get().updateAdvertiseQuartzCronTrigger(id);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        } catch (ParseException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }
        return new ModelAndView("redirect:/apiwiki/slide/list", map);
    }

    @RequestMapping(value = "/updatestatus")
    public ModelAndView updatestatus(@RequestParam(value = "id", required = false) Long id,
                                     @RequestParam(value = "removestatus", required = false, defaultValue = "") String removestatus,
                                     @RequestParam(value = "status", required = false) String status,
                                     @RequestParam(value = "pageStartIndex", required = false) String pageStartIndex,
                                     @RequestParam(value = "platform", required = false) String platform, HttpServletRequest request) {
        UpdateExpress updateExpress = new UpdateExpress();
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            Advertise advertise = AskServiceSngl.get().getAdvertise(id);
            if (ValidStatus.getByCode(status).equals(ValidStatus.VALID)) {
                if (advertise.getStartTime().getTime() > new Date().getTime()) {
                    status = "invalid";
                }
            }

            updateExpress.set(AdvertiseField.REMOVE_STATUS, ValidStatus.getByCode(status).getCode());
            AskServiceSngl.get().updateAdvertise(id, updateExpress);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }
        return new ModelAndView("redirect:/apiwiki/slide/list?removestatus=" + removestatus + "&platform=" + platform + "&pager.offset=" + pageStartIndex, map);
    }


    @ResponseBody
    @RequestMapping(value = "/sort")
    public String sort(@RequestParam(value = "sort", required = true) String sort,
                       @RequestParam(value = "id", required = true) Long id,
                       @RequestParam(value = "displayorder", required = true) Long displayorder,
                       @RequestParam(value = "otherid", required = true) String other_id,
                       @RequestParam(value = "otherdisplayorder", required = true) String other_displayorder,
                       @RequestParam(value = "removestatus", required = false, defaultValue = "") String removestatus,
                       @RequestParam(value = "platform", required = false, defaultValue = "") String
                               platform, HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {

            if (StringUtil.isEmpty(other_id)) {
                QueryExpress queryExpress = new QueryExpress();
                if (!StringUtil.isEmpty(removestatus)) {
                    queryExpress.add(QueryCriterions.eq(AdvertiseField.REMOVE_STATUS, ValidStatus.getByCode(removestatus).getCode()));
                }
                if (!StringUtil.isEmpty(platform)) {
                    queryExpress.add(QueryCriterions.eq(AdvertiseField.PLATFORM, Integer.valueOf(platform)));
                }

                //
                if (sort.equals("down")) {
                    queryExpress.add(QueryCriterions.lt(AdvertiseField.DISPLAY_ORDER, displayorder));
                    queryExpress.add(QuerySort.add(AdvertiseField.DISPLAY_ORDER, QuerySortOrder.DESC));
                } else {
                    queryExpress.add(QueryCriterions.gt(AdvertiseField.DISPLAY_ORDER, displayorder));
                    queryExpress.add(QuerySort.add(AdvertiseField.DISPLAY_ORDER, QuerySortOrder.ASC));
                }
                Pagination pagination = new Pagination(1, 1, 1);
                PageRows<Advertise> pageRows = AskServiceSngl.get().queryAdvertise(queryExpress, pagination);

                if (!CollectionUtil.isEmpty(pageRows.getRows())) {
                    other_id = String.valueOf(pageRows.getRows().get(0).getId());
                    other_displayorder = String.valueOf(pageRows.getRows().get(0).getDisplayOrder());
                }

            }

            if (!StringUtil.isEmpty(other_id)) {
                AskServiceSngl.get().updateAdvertise(id, new UpdateExpress().set(AdvertiseField.DISPLAY_ORDER, Long.valueOf(other_displayorder)));
                AskServiceSngl.get().updateAdvertise(Long.valueOf(other_id), new UpdateExpress().set(AdvertiseField.DISPLAY_ORDER, displayorder));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultCodeConstants.SUCCESS.getJsonString();
    }


}
