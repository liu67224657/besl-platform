package com.enjoyf.webapps.tools.webpage.controller.stats;

import com.enjoyf.platform.service.stats.OperationStatDomain;
import com.enjoyf.platform.service.stats.StatDateType;
import com.enjoyf.platform.service.stats.StatItem;
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
@RequestMapping(value = "/stats/retention")
public class StatsRetentionRateController extends ToolsBaseController {
    //
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //
    @Resource(name = "statsWebLogic")
    private StatsWebLogic statsWebLogic;

    //
    @RequestMapping(value = "/list")
    public ModelAndView pvLine(HttpServletRequest request, HttpServletResponse response,//
                               //
                               @RequestParam(value = "domaincode", required = false) String domainCode,
                               //
                               @RequestParam(value = "fromdate", required = false) String fromDateStr,
                               @RequestParam(value = "todate", required = false) String toDateStr,
                               @RequestParam(value = "datetypecode", required = false, defaultValue = "day") String dateTypeCode) throws Exception {
        //
        Map<String, Object> modelData = new HashMap<String, Object>();

        if (Strings.isNullOrEmpty(domainCode)) {
            domainCode = OperationStatDomain.BACK.getCode();
        }

        StatDateType dateType = StatDateType.getByCode(dateTypeCode);
        if (dateType == null) {
            dateType = StatDateType.DAY;
        }

        Date fromDate;
        Date toDate;

        if (Strings.isNullOrEmpty(fromDateStr)) {
            fromDateStr = DateUtil.formatDateToString(DateUtil.adjustDate(new Date(), dateType.getCalendarType(), -8), "yyyy-MM-dd");
        }

        if (Strings.isNullOrEmpty(toDateStr)) {
            toDateStr = DateUtil.formatDateToString(DateUtil.adjustDate(new Date(), dateType.getCalendarType(), -2), "yyyy-MM-dd");
        }

        //
        fromDate = statsWebLogic.getStatsStartDate(fromDateStr, dateType);
        toDate = statsWebLogic.getStatsStartDate(toDateStr, dateType);

        //
        Map<Date, Map<String, StatItem>> retentions = statsWebLogic.queryStatItemsByDomainPeriod(OperationStatDomain.BACK, dateType, fromDate, DateUtil.adjustDate(toDate, dateType.getCalendarType(), 1));
        if (retentions != null) {
            modelData.put("datas", retentions);
        }

        //
        modelData.put("domainCode", domainCode);

        modelData.put("dateTypes", statsWebLogic.getAllStatDateTypes());

        //
        modelData.put("dateTypeCode", dateType.getCode());
        modelData.put("fromDateStr", DateUtil.formatDateToString(fromDate, "yyyy-MM-dd"));
        modelData.put("toDateStr", DateUtil.formatDateToString(toDate, "yyyy-MM-dd"));
        modelData.put("fromDate", fromDate);
        modelData.put("toDate", toDate);
        //
        Date today = dateType.getStartDateByType(new Date());

        modelData.put("today", today);

        modelData.put("preDay1", DateUtil.adjustDate(today, dateType.getCalendarType(), -2));
        modelData.put("preDay2", DateUtil.adjustDate(today, dateType.getCalendarType(), -5));
        modelData.put("preDay3", DateUtil.adjustDate(today, dateType.getCalendarType(), -8));

        modelData.put("preDayStr1", DateUtil.formatDateToString(DateUtil.adjustDate(today, dateType.getCalendarType(), -2), "yyyy-MM-dd"));
        modelData.put("preDayStr2", DateUtil.formatDateToString(DateUtil.adjustDate(today, dateType.getCalendarType(), -5), "yyyy-MM-dd"));
        modelData.put("preDayStr3", DateUtil.formatDateToString(DateUtil.adjustDate(today, dateType.getCalendarType(), -8), "yyyy-MM-dd"));

        //
        return new ModelAndView("/stats/retention/list", modelData);
    }
}

