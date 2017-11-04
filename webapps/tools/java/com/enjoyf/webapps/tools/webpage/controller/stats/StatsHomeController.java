package com.enjoyf.webapps.tools.webpage.controller.stats;

import com.enjoyf.platform.service.stats.StatDateType;
import com.enjoyf.platform.service.stats.StatDomainDefault;
import com.enjoyf.platform.service.stats.StatItem;
import com.enjoyf.platform.service.stats.StatSectionDefault;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.Refresher;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.webapps.tools.weblogic.stats.StatsWebLogic;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import jofc2.model.Chart;
import jofc2.model.axis.XAxis;
import jofc2.model.axis.YAxis;
import jofc2.model.elements.BarChart;
import jofc2.model.elements.PieChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-10-10 上午11:35
 * Description:
 */
@Controller
@RequestMapping(value = "/stats")
public class StatsHomeController extends ToolsBaseController {
    //
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //
    private static YesterdayStatsDTO yesterdayStats = null;
    private static Refresher refresher = new Refresher(1000);

    //
    @Resource(name = "statsWebLogic")
    private StatsWebLogic statsWebLogic;

    //
    @RequestMapping(value = "/home")
    public ModelAndView home(HttpServletRequest request, HttpServletResponse response) {
        //
        Map<String, Object> modelData = new HashMap<String, Object>();

        //
        loadYesterdayStats();

        //
        modelData.put("yesterdayStats", yesterdayStats);

        //
        return new ModelAndView("/stats/home", modelData);
    }

    @RequestMapping(value = "/home/uvrefs")
    @ResponseBody
    public String uvRefs(HttpServletRequest request, HttpServletResponse response) {
        //
        loadYesterdayStats();

        //
        Chart chart = new Chart();
        PieChart pie = new PieChart();

        for (StatItem item : yesterdayStats.getYesterdayUniqueRefs()) {
            pie.addSlice(item.getStatValue(), item.getStatSection().getCode());
        }

        chart.addElements(pie);

        return chart.toString();
    }

    @RequestMapping(value = "/home/pvpages")
    @ResponseBody
    public String pvPages(HttpServletRequest request, HttpServletResponse response) {
        //
        loadYesterdayStats();

        //
        Chart chart = new Chart();
        PieChart pie = new PieChart();

        for (StatItem item : yesterdayStats.getYesterdayPageViewPages()) {
            pie.addSlice(item.getStatValue(), item.getStatSection().getCode());
        }

        chart.addElements(pie);

        return chart.toString();
    }

    @RequestMapping(value = "/home/regrefs")
    @ResponseBody
    public String regRefs(HttpServletRequest request, HttpServletResponse response) {
        //
        loadYesterdayStats();

        //
        Chart chart = new Chart();
        PieChart pie = new PieChart();

        for (StatItem item : yesterdayStats.getYesterdayRegisterRefs()) {
            pie.addSlice(item.getStatValue(), item.getStatSection().getCode());
        }

        chart.addElements(pie);

        return chart.toString();
    }

    @RequestMapping(value = "/home/nextdaybacks")
    @ResponseBody
    public String nextDayBacks(HttpServletRequest request, HttpServletResponse response) {
        //
        loadYesterdayStats();

        //
        Chart chart = new Chart();
        BarChart bar = new BarChart();
        XAxis xAxis = new XAxis();
        YAxis yAxis = new YAxis();

        for (StatItem item : yesterdayStats.getSecondDayBacks()) {
            bar.addValues(item.getStatValue() / 10000.00);
            bar.setTooltip(item.getStatValue() / 100.00 + "%");
            xAxis.addLabels(DateUtil.formatDateToString(item.getStatDate(), "MM-dd"));
        }

        yAxis.setMin(0);
        yAxis.setSteps(0.1);
        yAxis.setMax(1);

        chart.addElements(bar);
        chart.setXAxis(xAxis);
        chart.setYAxis(yAxis);

        return chart.toString();
    }

    @RequestMapping(value = "/home/sevendaybacks")
    @ResponseBody
    public String servenDayBacks(HttpServletRequest request, HttpServletResponse response) {
        //
        loadYesterdayStats();

        //
        Chart chart = new Chart();
        BarChart bar = new BarChart();
        XAxis xAxis = new XAxis();
        YAxis yAxis = new YAxis();

        for (StatItem item : yesterdayStats.getSevenDayBacks()) {
            bar.addValues(item.getStatValue() / 10000.00);
            bar.setTooltip(item.getStatValue() / 100.00 + "%");
            xAxis.addLabels(DateUtil.formatDateToString(item.getStatDate(), "MM-dd"));
        }

        yAxis.setMin(0);
        yAxis.setSteps(0.1);
        yAxis.setMax(1);

        chart.addElements(bar);
        chart.setXAxis(xAxis);
        chart.setYAxis(yAxis);

        return chart.toString();
    }

    @RequestMapping(value = "/home/fourdaybacks")
    @ResponseBody
    public String fourDayBacks(HttpServletRequest request, HttpServletResponse response) {
        //
        loadYesterdayStats();

        //
        Chart chart = new Chart();
        BarChart bar = new BarChart();
        XAxis xAxis = new XAxis();
        YAxis yAxis = new YAxis();

        for (StatItem item : yesterdayStats.getFourDayBacks()) {
            bar.addValues(item.getStatValue() / 10000.00);
            bar.setTooltip(item.getStatValue() / 100.00 + "%");
            xAxis.addLabels(DateUtil.formatDateToString(item.getStatDate(), "MM-dd"));
        }

        yAxis.setMin(0);
        yAxis.setSteps(0.1);
        yAxis.setMax(1);

        chart.addElements(bar);
        chart.setXAxis(xAxis);
        chart.setYAxis(yAxis);

        return chart.toString();
    }

    @RequestMapping(value = "/home/postphase")
    @ResponseBody
    public String postPhase(HttpServletRequest request, HttpServletResponse response) {
        //
        loadYesterdayStats();

        //
        Chart chart = new Chart();
        BarChart bar = new BarChart();
        XAxis xAxis = new XAxis();
        YAxis yAxis = new YAxis();

        //
        long maxValue = Long.MIN_VALUE;
        long minValue = Long.MAX_VALUE;

        for (StatItem item : yesterdayStats.getYesterdayPostPhases()) {
            bar.addValues(item.getStatValue());

            //
            maxValue = Math.max(item.getStatValue(), maxValue);
            minValue = Math.min(item.getStatValue(), minValue);

            //
            xAxis.addLabels(item.getStatSection().getCode());
        }

        //
        maxValue = statsWebLogic.formatMaxValue(maxValue);
        minValue = statsWebLogic.formatMinValue(minValue);

        //
        yAxis.setMin(minValue * 1.0);
        yAxis.setMax(maxValue * 1.0);
        yAxis.setSteps(statsWebLogic.calculateStep(minValue, maxValue) * 1.0);

        //
        xAxis.setSteps(1);
        int xNum = 0;
        if (xAxis.getLabels() != null && xAxis.getLabels().getLabels() != null) {
            xNum = xAxis.getLabels().getLabels().size();
        }
        while (xNum > 16) {
            xAxis.setSteps(xAxis.getSteps() * 2);
            xNum = xNum / 2;
        }

        chart.addElements(bar);
        chart.setXAxis(xAxis);
        chart.setYAxis(yAxis);

        return chart.toString();
    }

    @RequestMapping(value = "/home/commentphase")
    @ResponseBody
    public String commentPhase(HttpServletRequest request, HttpServletResponse response) {
        //
        loadYesterdayStats();

        //
        Chart chart = new Chart();
        BarChart bar = new BarChart();
        XAxis xAxis = new XAxis();
        YAxis yAxis = new YAxis();

        //
        long maxValue = Long.MIN_VALUE;
        long minValue = Long.MAX_VALUE;

        for (StatItem item : yesterdayStats.getYesterdayCommentPhases()) {
            bar.addValues(item.getStatValue());

            //
            maxValue = Math.max(item.getStatValue(), maxValue);
            minValue = Math.min(item.getStatValue(), minValue);

            //
            xAxis.addLabels(item.getStatSection().getCode());
        }

        //
        maxValue = statsWebLogic.formatMaxValue(maxValue);
        minValue = statsWebLogic.formatMinValue(minValue);

        //
        yAxis.setMin(minValue * 1.0);
        yAxis.setMax(maxValue * 1.0);
        yAxis.setSteps(statsWebLogic.calculateStep(minValue, maxValue) * 1.0);

        //
        xAxis.setSteps(1);
        int xNum = 0;
        if (xAxis.getLabels() != null && xAxis.getLabels().getLabels() != null) {
            xNum = xAxis.getLabels().getLabels().size();
        }
        while (xNum > 16) {
            xAxis.setSteps(xAxis.getSteps() * 2);
            xNum = xNum / 2;
        }

        chart.addElements(bar);
        chart.setXAxis(xAxis);
        chart.setYAxis(yAxis);

        return chart.toString();
    }

    @RequestMapping(value = "/home/forwardphase")
    @ResponseBody
    public String forwardPhase(HttpServletRequest request, HttpServletResponse response) {
        //
        loadYesterdayStats();

        //
        Chart chart = new Chart();
        BarChart bar = new BarChart();
        XAxis xAxis = new XAxis();
        YAxis yAxis = new YAxis();

        //
        long maxValue = Long.MIN_VALUE;
        long minValue = Long.MAX_VALUE;

        for (StatItem item : yesterdayStats.getYesterdayForwardPhases()) {
            bar.addValues(item.getStatValue());

            //
            maxValue = Math.max(item.getStatValue(), maxValue);
            minValue = Math.min(item.getStatValue(), minValue);

            xAxis.addLabels(item.getStatSection().getCode());
        }

        //
        maxValue = statsWebLogic.formatMaxValue(maxValue);
        minValue = statsWebLogic.formatMinValue(minValue);

        //
        yAxis.setMin(minValue * 1.0);
        yAxis.setMax(maxValue * 1.0);
        yAxis.setSteps(statsWebLogic.calculateStep(minValue, maxValue) * 1.0);

        //
        xAxis.setSteps(1);
        int xNum = 0;
        if (xAxis.getLabels() != null && xAxis.getLabels().getLabels() != null) {
            xNum = xAxis.getLabels().getLabels().size();
        }
        while (xNum > 16) {
            xAxis.setSteps(xAxis.getSteps() * 2);
            xNum = xNum / 2;
        }

        chart.addElements(bar);
        chart.setXAxis(xAxis);
        chart.setYAxis(yAxis);

        return chart.toString();
    }

    private void loadYesterdayStats() {
        if (yesterdayStats == null || refresher.shouldRefresh()) {
            //
            Date statDate = StatDateType.DAY.getStartDateByType(DateUtil.adjustDate(new Date(), Calendar.DAY_OF_YEAR, -1));

            //
            yesterdayStats = statsWebLogic.generateYesterdayStats(statDate);
        }
    }

    //
    @RequestMapping(value = "/line")
    public ModelAndView line(HttpServletRequest request, HttpServletResponse response,//
                             //
                             @RequestParam(value = "domaincode", required = false) String domainCode,
                             @RequestParam(value = "sectioncode", required = false, defaultValue = "all") String sectionCode,
                             //
                             @RequestParam(value = "fromdate", required = false) String fromDateStr,
                             @RequestParam(value = "todate", required = false) String toDateStr,
                             @RequestParam(value = "datetypecode", required = false) String dateTypeCode) throws Exception {
        //
        Map<String, Object> modelData = new HashMap<String, Object>();

        StatDateType dateType = StatDateType.getByCode(dateTypeCode);
        if (dateType == null) {
            dateType = StatDateType.DAY;
        }

        Date fromDate;
        Date toDate;

        //
        try {
            fromDate = statsWebLogic.getStatsStartDate(fromDateStr, dateType);
            toDate = statsWebLogic.getStatsEndDate(toDateStr, dateType);

            modelData.put("items", statsWebLogic.queryStatItems(new StatDomainDefault(domainCode), new StatSectionDefault(sectionCode), dateType, fromDate, toDate));
        } catch (Exception e) {
            //
            GAlerter.lab("StatsLineController call weblogic to queryStatItems error.", e);

            //
            throw e;
        }

        //
        modelData.put("domainCode", domainCode);
        modelData.put("sectionCode", sectionCode);

        //
        modelData.put("dateTypeCode", dateTypeCode);
        modelData.put("fromDateStr", DateUtil.formatDateToString(fromDate, "yyyy-MM-dd"));
        modelData.put("toDateStr", DateUtil.formatDateToString(toDate, "yyyy-MM-dd"));

        //
        return new ModelAndView("/stats/line", modelData);
    }
}

