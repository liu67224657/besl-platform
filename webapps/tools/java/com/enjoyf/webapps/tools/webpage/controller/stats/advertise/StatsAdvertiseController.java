package com.enjoyf.webapps.tools.webpage.controller.stats.advertise;

import com.enjoyf.platform.service.stats.StatDateType;
import com.enjoyf.platform.service.stats.StatSectionDefault;
import com.enjoyf.platform.service.tools.stats.StatDomainAdvertise;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.webapps.tools.weblogic.stats.StatsWebLogic;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-10-10 上午11:35
 * Description:
 */
@Controller
@RequestMapping(value = "/stats/advertise")
public class StatsAdvertiseController extends ToolsBaseController {
    //
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //
    @Resource(name = "statsWebLogic")
    private StatsWebLogic statsWebLogic;

    //
    @RequestMapping(value = "/pv/line")
    public ModelAndView pvLine(HttpServletRequest request, HttpServletResponse response,//
                               //
                               @RequestParam(value = "advertiseid", required = false) String advertiseId,
                               //
                               @RequestParam(value = "domaincode", required = false) String domainCode,
                               @RequestParam(value = "sectioncode", required = false, defaultValue = "all") String sectionCode,
                               //
                               @RequestParam(value = "fromdate", required = false) String fromDateStr,
                               @RequestParam(value = "todate", required = false) String toDateStr,
                               @RequestParam(value = "datetypecode", required = false, defaultValue = "day") String dateTypeCode) throws Exception {
        //
        Map<String, Object> modelData = new HashMap<String, Object>();

        if (Strings.isNullOrEmpty(domainCode)) {
            domainCode = StatDomainAdvertise.ADVERTISE_STAT_PAGE_VIEW.getCode();
        }

        if (Strings.isNullOrEmpty(sectionCode)) {
            sectionCode = StatSectionDefault.DEFAULT_SECTION_KEY;
        }

        StatDateType dateType = StatDateType.getByCode(dateTypeCode);
        if (dateType == null) {
            dateType = StatDateType.DAY;
        }

        if (Strings.isNullOrEmpty(fromDateStr)) {
            fromDateStr = DateUtil.formatDateToString(DateUtil.adjustDate(new Date(), dateType.getCalendarType(), -31), "yyyy-MM-dd");
        }

        if (Strings.isNullOrEmpty(toDateStr)) {
            toDateStr = DateUtil.formatDateToString(DateUtil.adjustDate(new Date(), dateType.getCalendarType(), -1), "yyyy-MM-dd");
        }

        Date fromDate;
        Date toDate;

        //
        fromDate = statsWebLogic.getStatsStartDate(fromDateStr, dateType);
        toDate = statsWebLogic.getStatsEndDate(toDateStr, dateType);

        //
        modelData.put("advertiseId", advertiseId);
        modelData.put("domainCode", domainCode);
        modelData.put("sectionCode", sectionCode);

        modelData.put("dateTypes", statsWebLogic.getAllStatDateTypes());

        //
        modelData.put("dateTypeCode", dateType.getCode());
        modelData.put("fromDateStr", DateUtil.formatDateToString(fromDate, "yyyy-MM-dd"));
        modelData.put("toDateStr", DateUtil.formatDateToString(toDate, "yyyy-MM-dd"));

        //
        return new ModelAndView("/stats/advertise/pvline", modelData);
    }

    @RequestMapping(value = "/pv/report")
    public ModelAndView pvReport(HttpServletRequest request, HttpServletResponse response,//
                                 //
                                 @RequestParam(value = "advertiseid", required = false) String advertiseId,
                                 //
                                 @RequestParam(value = "statdate", required = false) String statDateStr,
                                 @RequestParam(value = "datetypecode", required = false, defaultValue = "day") String dateTypeCode) throws Exception {
        //
        StatDateType dateType = StatDateType.getByCode(dateTypeCode);
        if (dateType == null) {
            dateType = StatDateType.DAY;
        }

        if (Strings.isNullOrEmpty(statDateStr)) {
            statDateStr = DateUtil.formatDateToString(DateUtil.adjustDate(new Date(), dateType.getCalendarType(), 0), "yyyy-MM-dd");
        }

        Date statDate;
        //
        statDate = statsWebLogic.getStatsStartDate(statDateStr, dateType);

        //
//        statsWebLogic.reportPageViewByMongo(advertiseId, dateType, statDate);
        //
        return new ModelAndView("redirect:/stats/advertise/pv/line?advertiseid=" + advertiseId + "&fromdate=" + statDateStr + "&todate=" + statDateStr + "&datetypecode=" + dateType.getCode());
    }

    @RequestMapping(value = "/uv/line")
    public ModelAndView uvLine(HttpServletRequest request, HttpServletResponse response,//
                               //
                               @RequestParam(value = "advertiseid", required = false) String advertiseId,
                               //
                               @RequestParam(value = "domaincode", required = false) String domainCode,
                               @RequestParam(value = "sectioncode", required = false, defaultValue = "all") String sectionCode,
                               //
                               @RequestParam(value = "fromdate", required = false) String fromDateStr,
                               @RequestParam(value = "todate", required = false) String toDateStr,
                               @RequestParam(value = "datetypecode", required = false, defaultValue = "day") String dateTypeCode) throws Exception {
        //
        Map<String, Object> modelData = new HashMap<String, Object>();

        if (Strings.isNullOrEmpty(domainCode)) {
            domainCode = StatDomainAdvertise.ADVERTISE_STAT_USER_VIEW.getCode();
        }

        if (Strings.isNullOrEmpty(sectionCode)) {
            sectionCode = StatSectionDefault.DEFAULT_SECTION_KEY;
        }

        StatDateType dateType = StatDateType.getByCode(dateTypeCode);
        if (dateType == null) {
            dateType = StatDateType.DAY;
        }

        if (Strings.isNullOrEmpty(fromDateStr)) {
            fromDateStr = DateUtil.formatDateToString(DateUtil.adjustDate(new Date(), dateType.getCalendarType(), -31), "yyyy-MM-dd");
        }

        if (Strings.isNullOrEmpty(toDateStr)) {
            toDateStr = DateUtil.formatDateToString(DateUtil.adjustDate(new Date(), dateType.getCalendarType(), -1), "yyyy-MM-dd");
        }

        Date fromDate;
        Date toDate;

        //
        fromDate = statsWebLogic.getStatsStartDate(fromDateStr, dateType);
        toDate = statsWebLogic.getStatsEndDate(toDateStr, dateType);

        //
        modelData.put("advertiseId", advertiseId);
        modelData.put("domainCode", domainCode);
        modelData.put("sectionCode", sectionCode);

        modelData.put("dateTypes", statsWebLogic.getAllStatDateTypes());

        //
        modelData.put("dateTypeCode", dateType.getCode());
        modelData.put("fromDateStr", DateUtil.formatDateToString(fromDate, "yyyy-MM-dd"));
        modelData.put("toDateStr", DateUtil.formatDateToString(toDate, "yyyy-MM-dd"));

        //
        return new ModelAndView("/stats/advertise/uvline", modelData);
    }

    @RequestMapping(value = "/uv/report")
    public ModelAndView uvReport(HttpServletRequest request, HttpServletResponse response,//
                                 //
                                 @RequestParam(value = "advertiseid", required = false) String advertiseId,
                                 //
                                 @RequestParam(value = "statdate", required = false) String statDateStr,
                                 @RequestParam(value = "datetypecode", required = false, defaultValue = "day") String dateTypeCode) throws Exception {
        //
        StatDateType dateType = StatDateType.getByCode(dateTypeCode);
        if (dateType == null) {
            dateType = StatDateType.DAY;
        }

        if (Strings.isNullOrEmpty(statDateStr)) {
            statDateStr = DateUtil.formatDateToString(DateUtil.adjustDate(new Date(), dateType.getCalendarType(), 0), "yyyy-MM-dd");
        }

        Date statDate;
        //
        statDate = statsWebLogic.getStatsStartDate(statDateStr, dateType);

        //
//        statsWebLogic.reportUserViewByMongo(advertiseId, dateType, statDate);
        //
        return new ModelAndView("redirect:/stats/advertise/uv/line?advertiseid=" + advertiseId + "&fromdate=" + statDateStr + "&todate=" + statDateStr + "&datetypecode=" + dateType.getCode());
    }

    @RequestMapping(value = "/vt/line")
    public ModelAndView vtLine(HttpServletRequest request, HttpServletResponse response,//
                               //
                               @RequestParam(value = "advertiseid", required = false) String advertiseId,
                               //
                               @RequestParam(value = "domaincode", required = false) String domainCode,
                               @RequestParam(value = "sectioncode", required = false, defaultValue = "all") String sectionCode,
                               //
                               @RequestParam(value = "fromdate", required = false) String fromDateStr,
                               @RequestParam(value = "todate", required = false) String toDateStr,
                               @RequestParam(value = "datetypecode", required = false, defaultValue = "day") String dateTypeCode) throws Exception {
        //
        Map<String, Object> modelData = new HashMap<String, Object>();

        if (Strings.isNullOrEmpty(domainCode)) {
            domainCode = StatDomainAdvertise.ADVERTISE_STAT_VIEW_TIME.getCode();
        }

        if (Strings.isNullOrEmpty(sectionCode)) {
            sectionCode = StatSectionDefault.DEFAULT_SECTION_KEY;
        }

        StatDateType dateType = StatDateType.getByCode(dateTypeCode);
        if (dateType == null) {
            dateType = StatDateType.DAY;
        }

        if (Strings.isNullOrEmpty(fromDateStr)) {
            fromDateStr = DateUtil.formatDateToString(DateUtil.adjustDate(new Date(), dateType.getCalendarType(), -31), "yyyy-MM-dd");
        }

        if (Strings.isNullOrEmpty(toDateStr)) {
            toDateStr = DateUtil.formatDateToString(DateUtil.adjustDate(new Date(), dateType.getCalendarType(), -1), "yyyy-MM-dd");
        }

        Date fromDate;
        Date toDate;

        //
        fromDate = statsWebLogic.getStatsStartDate(fromDateStr, dateType);
        toDate = statsWebLogic.getStatsEndDate(toDateStr, dateType);

        //
        modelData.put("advertiseId", advertiseId);
        modelData.put("domainCode", domainCode);
        modelData.put("sectionCode", sectionCode);

        modelData.put("dateTypes", statsWebLogic.getAllStatDateTypes());

        //
        modelData.put("dateTypeCode", dateType.getCode());
        modelData.put("fromDateStr", DateUtil.formatDateToString(fromDate, "yyyy-MM-dd"));
        modelData.put("toDateStr", DateUtil.formatDateToString(toDate, "yyyy-MM-dd"));

        //
        return new ModelAndView("/stats/advertise/vtline", modelData);
    }

    @RequestMapping(value = "/vt/report")
    public ModelAndView vtReport(HttpServletRequest request, HttpServletResponse response,//
                                 //
                                 @RequestParam(value = "advertiseid", required = false) String advertiseId,
                                 //
                                 @RequestParam(value = "statdate", required = false) String statDateStr,
                                 @RequestParam(value = "datetypecode", required = false, defaultValue = "day") String dateTypeCode) throws Exception {
        //
        StatDateType dateType = StatDateType.getByCode(dateTypeCode);
        if (dateType == null) {
            dateType = StatDateType.DAY;
        }

        if (Strings.isNullOrEmpty(statDateStr)) {
            statDateStr = DateUtil.formatDateToString(DateUtil.adjustDate(new Date(), dateType.getCalendarType(), 0), "yyyy-MM-dd");
        }

        Date statDate;
        //
        statDate = statsWebLogic.getStatsStartDate(statDateStr, dateType);

        //
//        statsWebLogic.reportViewTimeByMongo(advertiseId, dateType, statDate);
        //
        return new ModelAndView("redirect:/stats/advertise/vt/line?advertiseid=" + advertiseId + "&fromdate=" + statDateStr + "&todate=" + statDateStr + "&datetypecode=" + dateType.getCode());
    }

    @RequestMapping(value = "/vd/line")
    public ModelAndView vdLine(HttpServletRequest request, HttpServletResponse response,//
                               //
                               @RequestParam(value = "advertiseid", required = false) String advertiseId,
                               //
                               @RequestParam(value = "domaincode", required = false) String domainCode,
                               @RequestParam(value = "sectioncode", required = false, defaultValue = "all") String sectionCode,
                               //
                               @RequestParam(value = "fromdate", required = false) String fromDateStr,
                               @RequestParam(value = "todate", required = false) String toDateStr,
                               @RequestParam(value = "datetypecode", required = false, defaultValue = "day") String dateTypeCode) throws Exception {
        //
        Map<String, Object> modelData = new HashMap<String, Object>();

        if (Strings.isNullOrEmpty(domainCode)) {
            domainCode = StatDomainAdvertise.ADVERTISE_STAT_VIEW_DEPTH.getCode();
        }

        if (Strings.isNullOrEmpty(sectionCode)) {
            sectionCode = StatSectionDefault.DEFAULT_SECTION_KEY;
        }

        StatDateType dateType = StatDateType.getByCode(dateTypeCode);
        if (dateType == null) {
            dateType = StatDateType.DAY;
        }

        if (Strings.isNullOrEmpty(fromDateStr)) {
            fromDateStr = DateUtil.formatDateToString(DateUtil.adjustDate(new Date(), dateType.getCalendarType(), -31), "yyyy-MM-dd");
        }

        if (Strings.isNullOrEmpty(toDateStr)) {
            toDateStr = DateUtil.formatDateToString(DateUtil.adjustDate(new Date(), dateType.getCalendarType(), -1), "yyyy-MM-dd");
        }

        Date fromDate;
        Date toDate;

        //
        fromDate = statsWebLogic.getStatsStartDate(fromDateStr, dateType);
        toDate = statsWebLogic.getStatsEndDate(toDateStr, dateType);

        //
        modelData.put("advertiseId", advertiseId);
        modelData.put("domainCode", domainCode);
        modelData.put("sectionCode", sectionCode);

        modelData.put("dateTypes", statsWebLogic.getAllStatDateTypes());

        //
        modelData.put("dateTypeCode", dateType.getCode());
        modelData.put("fromDateStr", DateUtil.formatDateToString(fromDate, "yyyy-MM-dd"));
        modelData.put("toDateStr", DateUtil.formatDateToString(toDate, "yyyy-MM-dd"));

        //
        return new ModelAndView("/stats/advertise/vdline", modelData);
    }

    @RequestMapping(value = "/vd/report")
    public ModelAndView vdReport(HttpServletRequest request, HttpServletResponse response,//
                                 //
                                 @RequestParam(value = "advertiseid", required = false) String advertiseId,
                                 //
                                 @RequestParam(value = "statdate", required = false) String statDateStr,
                                 @RequestParam(value = "datetypecode", required = false, defaultValue = "day") String dateTypeCode) throws Exception {
        //
        StatDateType dateType = StatDateType.getByCode(dateTypeCode);
        if (dateType == null) {
            dateType = StatDateType.DAY;
        }

        if (Strings.isNullOrEmpty(statDateStr)) {
            statDateStr = DateUtil.formatDateToString(DateUtil.adjustDate(new Date(), dateType.getCalendarType(), 0), "yyyy-MM-dd");
        }

        Date statDate;
        //
        statDate = statsWebLogic.getStatsStartDate(statDateStr, dateType);

        //
//        statsWebLogic.reportViewDepthByMongo(advertiseId, dateType, statDate);
        //
        return new ModelAndView("redirect:/stats/advertise/vd/line?advertiseid=" + advertiseId + "&fromdate=" + statDateStr + "&todate=" + statDateStr + "&datetypecode=" + dateType.getCode());
    }

    @RequestMapping(value = "/vb/line")
    public ModelAndView vbLine(HttpServletRequest request, HttpServletResponse response,//
                               //
                               @RequestParam(value = "advertiseid", required = false) String advertiseId,
                               //
                               @RequestParam(value = "domaincode", required = false) String domainCode,
                               @RequestParam(value = "sectioncode", required = false, defaultValue = "all") String sectionCode,
                               //
                               @RequestParam(value = "fromdate", required = false) String fromDateStr,
                               @RequestParam(value = "todate", required = false) String toDateStr,
                               @RequestParam(value = "datetypecode", required = false, defaultValue = "day") String dateTypeCode) throws Exception {
        //
        Map<String, Object> modelData = new HashMap<String, Object>();

        if (Strings.isNullOrEmpty(domainCode)) {
            domainCode = StatDomainAdvertise.ADVERTISE_STAT_VIEW_BOUNCE.getCode();
        }

        if (Strings.isNullOrEmpty(sectionCode)) {
            sectionCode = StatSectionDefault.DEFAULT_SECTION_KEY;
        }

        StatDateType dateType = StatDateType.getByCode(dateTypeCode);
        if (dateType == null) {
            dateType = StatDateType.DAY;
        }

        if (Strings.isNullOrEmpty(fromDateStr)) {
            fromDateStr = DateUtil.formatDateToString(DateUtil.adjustDate(new Date(), dateType.getCalendarType(), -31), "yyyy-MM-dd");
        }

        if (Strings.isNullOrEmpty(toDateStr)) {
            toDateStr = DateUtil.formatDateToString(DateUtil.adjustDate(new Date(), dateType.getCalendarType(), -1), "yyyy-MM-dd");
        }

        Date fromDate;
        Date toDate;

        //
        fromDate = statsWebLogic.getStatsStartDate(fromDateStr, dateType);
        toDate = statsWebLogic.getStatsEndDate(toDateStr, dateType);

        //
        modelData.put("advertiseId", advertiseId);
        modelData.put("domainCode", domainCode);
        modelData.put("sectionCode", sectionCode);

        modelData.put("dateTypes", statsWebLogic.getAllStatDateTypes());

        //
        modelData.put("dateTypeCode", dateType.getCode());
        modelData.put("fromDateStr", DateUtil.formatDateToString(fromDate, "yyyy-MM-dd"));
        modelData.put("toDateStr", DateUtil.formatDateToString(toDate, "yyyy-MM-dd"));

        //
        return new ModelAndView("/stats/advertise/vbline", modelData);
    }

    @RequestMapping(value = "/vb/report")
    public ModelAndView vbReport(HttpServletRequest request, HttpServletResponse response,//
                                 //
                                 @RequestParam(value = "advertiseid", required = false) String advertiseId,
                                 //
                                 @RequestParam(value = "statdate", required = false) String statDateStr,
                                 @RequestParam(value = "datetypecode", required = false, defaultValue = "day") String dateTypeCode) throws Exception {
        //
        StatDateType dateType = StatDateType.getByCode(dateTypeCode);
        if (dateType == null) {
            dateType = StatDateType.DAY;
        }

        if (Strings.isNullOrEmpty(statDateStr)) {
            statDateStr = DateUtil.formatDateToString(DateUtil.adjustDate(new Date(), dateType.getCalendarType(), 0), "yyyy-MM-dd");
        }

        Date statDate;
        //
        statDate = statsWebLogic.getStatsStartDate(statDateStr, dateType);

        //
//        statsWebLogic.reportViewBounceByMongo(advertiseId, dateType, statDate);
        //
        return new ModelAndView("redirect:/stats/advertise/vb/line?advertiseid=" + advertiseId + "&fromdate=" + statDateStr + "&todate=" + statDateStr + "&datetypecode=" + dateType.getCode());
    }

    @RequestMapping(value = "/ur/line")
    public ModelAndView urLine(HttpServletRequest request, HttpServletResponse response,//
                               //
                               @RequestParam(value = "advertiseid", required = false) String advertiseId,
                               //
                               @RequestParam(value = "domaincode", required = false) String domainCode,
                               @RequestParam(value = "sectioncode", required = false, defaultValue = "all") String sectionCode,
                               //
                               @RequestParam(value = "fromdate", required = false) String fromDateStr,
                               @RequestParam(value = "todate", required = false) String toDateStr,
                               @RequestParam(value = "datetypecode", required = false, defaultValue = "day") String dateTypeCode) throws Exception {
        //
        Map<String, Object> modelData = new HashMap<String, Object>();

        if (Strings.isNullOrEmpty(domainCode)) {
            domainCode = StatDomainAdvertise.ADVERTISE_STAT_USER_RETAINED.getCode();
        }

        if (Strings.isNullOrEmpty(sectionCode)) {
            sectionCode = StatSectionDefault.DEFAULT_SECTION_KEY;
        }

        StatDateType dateType = StatDateType.getByCode(dateTypeCode);
        if (dateType == null) {
            dateType = StatDateType.DAY;
        }

        if (Strings.isNullOrEmpty(fromDateStr)) {
            fromDateStr = DateUtil.formatDateToString(DateUtil.adjustDate(new Date(), dateType.getCalendarType(), -7), "yyyy-MM-dd");
        }

        if (Strings.isNullOrEmpty(toDateStr)) {
            toDateStr = DateUtil.formatDateToString(DateUtil.adjustDate(new Date(), dateType.getCalendarType(), 0), "yyyy-MM-dd");
        }

        Date fromDate;
        Date toDate;

        //
        fromDate = statsWebLogic.getStatsStartDate(fromDateStr, dateType);
        toDate = statsWebLogic.getStatsEndDate(toDateStr, dateType);

        //
        modelData.put("advertiseId", advertiseId);
        modelData.put("domainCode", domainCode);
        modelData.put("sectionCode", sectionCode);

        modelData.put("dateTypes", statsWebLogic.getAllStatDateTypes());

        //
        modelData.put("dateTypeCode", dateType.getCode());
        modelData.put("fromDateStr", DateUtil.formatDateToString(fromDate, "yyyy-MM-dd"));
        modelData.put("toDateStr", DateUtil.formatDateToString(toDate, "yyyy-MM-dd"));

        //
        return new ModelAndView("/stats/advertise/urline", modelData);
    }

    @RequestMapping(value = "/ur/report")
    public ModelAndView urReport(HttpServletRequest request, HttpServletResponse response,//
                                 //
                                 @RequestParam(value = "advertiseid", required = false) String advertiseId,
                                 //
                                 @RequestParam(value = "statdate", required = false) String statDateStr,
                                 @RequestParam(value = "datetypecode", required = false, defaultValue = "day") String dateTypeCode) throws Exception {
        //
        StatDateType dateType = StatDateType.getByCode(dateTypeCode);
        if (dateType == null) {
            dateType = StatDateType.DAY;
        }

        if (Strings.isNullOrEmpty(statDateStr)) {
            statDateStr = DateUtil.formatDateToString(DateUtil.adjustDate(new Date(), dateType.getCalendarType(), 0), "yyyy-MM-dd");
        }

        Date statDate;
        //
        statDate = statsWebLogic.getStatsStartDate(statDateStr, dateType);

        //
//        statsWebLogic.reportUserRetainedByMongo(advertiseId, dateType, statDate);
        //
        return new ModelAndView("redirect:/stats/advertise/ur/line?advertiseid=" + advertiseId + "&fromdate=" + statDateStr + "&todate=" + statDateStr + "&datetypecode=" + dateType.getCode());
    }


//    //
//    @RequestMapping(value = "/uv")
//    public ModelAndView ipLine(HttpServletRequest request, HttpServletResponse response,//
//                               //
//                               @RequestParam(value = "domaincode", required = false) String domainCode,
//                               @RequestParam(value = "sectioncode", required = false, defaultValue = "all") String sectionCode,
//                               //
//                               @RequestParam(value = "fromdate", required = false) String fromDateStr,
//                               @RequestParam(value = "todate", required = false) String toDateStr,
//                               @RequestParam(value = "datetypecode", required = false, defaultValue = "day") String dateTypeCode) throws Exception {
//        //
//        Map<String, Object> modelData = new HashMap<String, Object>();
//
//        if (Strings.isNullOrEmpty(domainCode)) {
//            domainCode = OperationStatDomain.IP.getCode();
//        }
//
//        if (Strings.isNullOrEmpty(sectionCode)) {
//            sectionCode = StatSectionDefault.DEFAULT_SECTION_KEY;
//        }
//
//        StatDateType dateType = StatDateType.getByCode(dateTypeCode);
//        if (dateType == null) {
//            dateType = StatDateType.DAY;
//        }
//
//        if (Strings.isNullOrEmpty(fromDateStr)) {
//            fromDateStr = DateUtil.formatDateToString(DateUtil.adjustDate(new Date(), dateType.getCalendarType(), -31), "yyyy-MM-dd");
//        }
//
//        if (Strings.isNullOrEmpty(toDateStr)) {
//            toDateStr = DateUtil.formatDateToString(DateUtil.adjustDate(new Date(), dateType.getCalendarType(), -1), "yyyy-MM-dd");
//        }
//
//        Date fromDate;
//        Date toDate;
//
//        //
//        fromDate = statsWebLogic.getStatsStartDate(fromDateStr, dateType);
//        toDate = statsWebLogic.getStatsEndDate(toDateStr, dateType);
//
//        //
//        modelData.put("domainCode", domainCode);
//        modelData.put("sectionCode", sectionCode);
//
//        modelData.put("dateTypes", statsWebLogic.getAllStatDateTypes());
//
//        //
//        modelData.put("dateTypeCode", dateType.getCode());
//        modelData.put("fromDateStr", DateUtil.formatDateToString(fromDate, "yyyy-MM-dd"));
//        modelData.put("toDateStr", DateUtil.formatDateToString(toDate, "yyyy-MM-dd"));
//
//        //
//        return new ModelAndView("/stats/pageview/ipline", modelData);
//    }
//
//
//    //
//    @RequestMapping(value = "/uvline")
//    public ModelAndView uvLine(HttpServletRequest request, HttpServletResponse response,//
//                               //
//                               @RequestParam(value = "domaincode", required = false) String domainCode,
//                               @RequestParam(value = "sectioncode", required = false, defaultValue = "all") String sectionCode,
//                               //
//                               @RequestParam(value = "fromdate", required = false) String fromDateStr,
//                               @RequestParam(value = "todate", required = false) String toDateStr,
//                               @RequestParam(value = "datetypecode", required = false, defaultValue = "day") String dateTypeCode) throws Exception {
//        //
//        Map<String, Object> modelData = new HashMap<String, Object>();
//
//        if (Strings.isNullOrEmpty(domainCode)) {
//            domainCode = OperationStatDomain.UNIQUE_USER.getCode();
//        }
//
//        if (Strings.isNullOrEmpty(sectionCode)) {
//            sectionCode = StatSectionDefault.DEFAULT_SECTION_KEY;
//        }
//
//        StatDateType dateType = StatDateType.getByCode(dateTypeCode);
//        if (dateType == null) {
//            dateType = StatDateType.DAY;
//        }
//
//        if (Strings.isNullOrEmpty(fromDateStr)) {
//            fromDateStr = DateUtil.formatDateToString(DateUtil.adjustDate(new Date(), dateType.getCalendarType(), -31), "yyyy-MM-dd");
//        }
//
//        if (Strings.isNullOrEmpty(toDateStr)) {
//            toDateStr = DateUtil.formatDateToString(DateUtil.adjustDate(new Date(), dateType.getCalendarType(), -1), "yyyy-MM-dd");
//        }
//
//        Date fromDate;
//        Date toDate;
//
//        //
//        fromDate = statsWebLogic.getStatsStartDate(fromDateStr, dateType);
//        toDate = statsWebLogic.getStatsEndDate(toDateStr, dateType);
//
//        //
//        modelData.put("domainCode", domainCode);
//        modelData.put("sectionCode", sectionCode);
//
//        modelData.put("dateTypes", statsWebLogic.getAllStatDateTypes());
//
//        //
//        modelData.put("dateTypeCode", dateType.getCode());
//        modelData.put("fromDateStr", DateUtil.formatDateToString(fromDate, "yyyy-MM-dd"));
//        modelData.put("toDateStr", DateUtil.formatDateToString(toDate, "yyyy-MM-dd"));
//
//        //
//        return new ModelAndView("/stats/pageview/uvline", modelData);
//    }
//
//    //
//    @RequestMapping(value = "/tosline")
//    public ModelAndView tosLine(HttpServletRequest request, HttpServletResponse response,//
//                                //
//                                @RequestParam(value = "domaincode", required = false) String domainCode,
//                                @RequestParam(value = "sectioncode", required = false, defaultValue = "all") String sectionCode,
//                                //
//                                @RequestParam(value = "fromdate", required = false) String fromDateStr,
//                                @RequestParam(value = "todate", required = false) String toDateStr,
//                                @RequestParam(value = "datetypecode", required = false, defaultValue = "day") String dateTypeCode) throws Exception {
//        //
//        Map<String, Object> modelData = new HashMap<String, Object>();
//
//        if (Strings.isNullOrEmpty(domainCode)) {
//            domainCode = OperationStatDomain.UNIQUE_USER.getCode();
//        }
//
//        if (Strings.isNullOrEmpty(sectionCode)) {
//            sectionCode = StatSectionDefault.DEFAULT_SECTION_KEY;
//        }
//
//        StatDateType dateType = StatDateType.getByCode(dateTypeCode);
//        if (dateType == null) {
//            dateType = StatDateType.DAY;
//        }
//
//        if (Strings.isNullOrEmpty(fromDateStr)) {
//            fromDateStr = DateUtil.formatDateToString(DateUtil.adjustDate(new Date(), dateType.getCalendarType(), -31), "yyyy-MM-dd");
//        }
//
//        if (Strings.isNullOrEmpty(toDateStr)) {
//            toDateStr = DateUtil.formatDateToString(DateUtil.adjustDate(new Date(), dateType.getCalendarType(), -1), "yyyy-MM-dd");
//        }
//
//        Date fromDate;
//        Date toDate;
//
//        //
//        fromDate = statsWebLogic.getStatsStartDate(fromDateStr, dateType);
//        toDate = statsWebLogic.getStatsEndDate(toDateStr, dateType);
//
//        //
//        modelData.put("domainCode", domainCode);
//        modelData.put("sectionCode", sectionCode);
//
//        modelData.put("dateTypes", statsWebLogic.getAllStatDateTypes());
//
//        //
//        modelData.put("dateTypeCode", dateType.getCode());
//        modelData.put("fromDateStr", DateUtil.formatDateToString(fromDate, "yyyy-MM-dd"));
//        modelData.put("toDateStr", DateUtil.formatDateToString(toDate, "yyyy-MM-dd"));
//
//        //
//        return new ModelAndView("/stats/pageview/tosline", modelData);
//    }
//
//    //
//    @RequestMapping(value = "/pie")
//    public ModelAndView pie(HttpServletRequest request, HttpServletResponse response,//
//                            //
//                            @RequestParam(value = "statdate", required = false) String statDateStr,
//                            @RequestParam(value = "datetypecode", required = false, defaultValue = "day") String dateTypeCode) throws Exception {
//        //
//        Map<String, Object> modelData = new HashMap<String, Object>();
//
//        StatDateType dateType = StatDateType.getByCode(dateTypeCode);
//        if (dateType == null) {
//            dateType = StatDateType.DAY;
//        }
//
//        Date statDate;
//
//        //
//        statDate = statsWebLogic.getStatsStartDate(statDateStr, dateType);
//
//        //
//        modelData.put("dateTypes", statsWebLogic.getAllStatDateTypes());
//
//        //
//        modelData.put("dateTypeCode", dateType.getCode());
//        modelData.put("statDateStr", DateUtil.formatDateToString(statDate, "yyyy-MM-dd"));
//
//        //
//        return new ModelAndView("/stats/pageview/pie", modelData);
//    }
}

