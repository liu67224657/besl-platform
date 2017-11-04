package com.enjoyf.webapps.tools.webpage.controller.stats.advertise;

import com.enjoyf.platform.service.stats.*;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.webapps.tools.weblogic.stats.StatsWebLogic;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import jofc2.model.Chart;
import jofc2.model.axis.Label;
import jofc2.model.axis.XAxis;
import jofc2.model.axis.YAxis;
import jofc2.model.elements.BarChart;
import jofc2.model.elements.LineChart;
import jofc2.model.elements.PieChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-10-10 上午11:35
 * Description:
 */
@Controller
@RequestMapping(value = "/stats/advertise/data")
public class StatsAdvertiseDataController extends ToolsBaseController {
    //
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //
    @Resource(name = "statsWebLogic")
    private StatsWebLogic statsWebLogic;

    //
    @RequestMapping(value = "/line/day")
    @ResponseBody
    public String lineDay(HttpServletRequest request, HttpServletResponse response,
                          //
                          @RequestParam(value = "advertiseid", required = false) String advertiseId,
                          @RequestParam(value = "domaincode", required = false) String domainCode,
                          @RequestParam(value = "sectioncode", required = false, defaultValue = "all") String sectionCode,
                          //
                          @RequestParam(value = "fromdate", required = false) String fromDateStr,
                          @RequestParam(value = "todate", required = false) String toDateStr) throws Exception {
        //
        Chart chart = new Chart();
        LineChart line = new LineChart();
        XAxis xAxis = new XAxis();
        YAxis yAxis = new YAxis();

        //
        StatDateType dateType = StatDateType.DAY;

        Date fromDate;
        Date toDate;

        try {
            //
            fromDate = statsWebLogic.getStatsStartDate(fromDateStr, dateType);
            toDate = statsWebLogic.getStatsEndDate(toDateStr, dateType);

            //
            long maxValue = Long.MIN_VALUE;
            long minValue = Long.MAX_VALUE;

            //
            List<StatItem> items = statsWebLogic.queryStatItemsByQuery(advertiseId, new StatDomainDefault(domainCode), new StatSectionDefault(sectionCode), dateType, fromDate, toDate);
            if (items != null && items.size() > 0) {
                for (StatItem item : items) {
                    //
                    maxValue = Math.max(item.getStatValue(), maxValue);
                    minValue = Math.min(item.getStatValue(), minValue);

                    line.addValues(item.getStatValue());

                    //
                    Label label = new Label(DateUtil.formatDateToString(item.getStatDate(), "MM-dd"));
                    label.setRotation(Label.Rotation.VERTICAL);

                    xAxis.addLabels(label);
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

                //
                chart.addElements(line);
                chart.setXAxis(xAxis);
                chart.setYAxis(yAxis);
            }
        } catch (Exception e) {
            //
            GAlerter.lab("StatsLineController call weblogic to queryStatItems error.", e);

            //
            throw e;
        }

        //
        return chart.toString();
    }

    //
    @RequestMapping(value = "/line/week")
    @ResponseBody
    public String lineWeek(HttpServletRequest request, HttpServletResponse response,
                           //
                           @RequestParam(value = "advertiseid", required = false) String advertiseId,
                           @RequestParam(value = "domaincode", required = false) String domainCode,
                           @RequestParam(value = "sectioncode", required = false, defaultValue = "all") String sectionCode,
                           //
                           @RequestParam(value = "fromdate", required = false) String fromDateStr,
                           @RequestParam(value = "todate", required = false) String toDateStr) throws Exception {
        //
        Chart chart = new Chart();
        LineChart line = new LineChart();
        XAxis xAxis = new XAxis();
        YAxis yAxis = new YAxis();

        //
        StatDateType dateType = StatDateType.WEEK;

        Date fromDate;
        Date toDate;

        try {
            //
            fromDate = statsWebLogic.getStatsStartDate(fromDateStr, dateType);
            toDate = statsWebLogic.getStatsEndDate(toDateStr, dateType);

            //
            long maxValue = Long.MIN_VALUE;
            long minValue = Long.MAX_VALUE;

            //
            List<StatItem> items = statsWebLogic.queryStatItemsByQuery(advertiseId, new StatDomainDefault(domainCode), new StatSectionDefault(sectionCode), dateType, fromDate, toDate);
            if (items != null && items.size() > 0) {
                for (StatItem item : items) {
                    //
                    maxValue = Math.max(item.getStatValue(), maxValue);
                    minValue = Math.min(item.getStatValue(), minValue);

                    line.addValues(item.getStatValue());

                    //
                    Label label = new Label(DateUtil.formatDateToString(item.getStatDate(), "MM-dd"));
                    label.setRotation(Label.Rotation.VERTICAL);

                    xAxis.addLabels(label);
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

                //
                chart.addElements(line);
                chart.setXAxis(xAxis);
                chart.setYAxis(yAxis);
            }
        } catch (Exception e) {
            //
            GAlerter.lab("StatsLineController call weblogic to queryStatItems error.", e);

            //
            throw e;
        }

        //
        return chart.toString();
    }

    //
    @RequestMapping(value = "/line/month")
    @ResponseBody
    public String lineMonth(HttpServletRequest request, HttpServletResponse response,
                            //
                            @RequestParam(value = "advertiseid", required = false) String advertiseId,
                            @RequestParam(value = "domaincode", required = false) String domainCode,
                            @RequestParam(value = "sectioncode", required = false, defaultValue = "all") String sectionCode,
                            //
                            @RequestParam(value = "fromdate", required = false) String fromDateStr,
                            @RequestParam(value = "todate", required = false) String toDateStr) throws Exception {
        //
        Chart chart = new Chart();
        LineChart line = new LineChart();
        XAxis xAxis = new XAxis();
        YAxis yAxis = new YAxis();

        //
        StatDateType dateType = StatDateType.MONTH;

        Date fromDate;
        Date toDate;

        try {
            //
            fromDate = statsWebLogic.getStatsStartDate(fromDateStr, dateType);
            toDate = statsWebLogic.getStatsEndDate(toDateStr, dateType);

            //
            long maxValue = Long.MIN_VALUE;
            long minValue = Long.MAX_VALUE;

            //
            List<StatItem> items = statsWebLogic.queryStatItemsByQuery(advertiseId, new StatDomainDefault(domainCode), new StatSectionDefault(sectionCode), dateType, fromDate, toDate);
            if (items != null && items.size() > 0) {
                for (StatItem item : items) {
                    //
                    maxValue = Math.max(item.getStatValue(), maxValue);
                    minValue = Math.min(item.getStatValue(), minValue);

                    line.addValues(item.getStatValue());

                    //
                    Label label = new Label(DateUtil.formatDateToString(item.getStatDate(), "yyyy-MM"));
                    label.setRotation(Label.Rotation.VERTICAL);

                    xAxis.addLabels(label);
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

                //
                chart.addElements(line);
                chart.setXAxis(xAxis);
                chart.setYAxis(yAxis);
            }
        } catch (Exception e) {
            //
            GAlerter.lab("StatsLineController call weblogic to queryStatItems error.", e);

            //
            throw e;
        }

        //
        return chart.toString();
    }

    //
    @RequestMapping(value = "/bar/day")
    @ResponseBody
    public String barDay(HttpServletRequest request, HttpServletResponse response,
                         //
                         @RequestParam(value = "domaincode", required = false) String domainCode,
                         @RequestParam(value = "sectioncode", required = false, defaultValue = "all") String sectionCode,
                         //
                         @RequestParam(value = "fromdate", required = false) String fromDateStr,
                         @RequestParam(value = "todate", required = false) String toDateStr,
                         //
                         @RequestParam(value = "percent", required = false, defaultValue = "false") boolean percent) throws Exception {
        //
        Chart chart = new Chart();
        BarChart bar = new BarChart();
        XAxis xAxis = new XAxis();
        YAxis yAxis = new YAxis();

        //
        StatDateType dateType = StatDateType.DAY;

        Date fromDate;
        Date toDate;

        try {
            //
            fromDate = statsWebLogic.getStatsStartDate(fromDateStr, dateType);
            toDate = statsWebLogic.getStatsEndDate(toDateStr, dateType);

            //
            long maxValue = Long.MIN_VALUE;
            long minValue = Long.MAX_VALUE;

            //
            List<StatItem> items = statsWebLogic.queryStatItems(new StatDomainDefault(domainCode), new StatSectionDefault(sectionCode), dateType, fromDate, toDate);
            if (items != null && items.size() > 0) {
                for (StatItem item : items) {
                    //
                    maxValue = Math.max(item.getStatValue(), maxValue);
                    minValue = Math.min(item.getStatValue(), minValue);

                    BarChart.Bar b = new BarChart.Bar(percent ? item.getStatValue() / 100.0 : item.getStatValue());
                    if (percent) {
                        b.setTooltip((percent ? item.getStatValue() / 100.0 : item.getStatValue()) + "%");
                    }

                    bar.addBars(b);

                    //
                    Label label = new Label(DateUtil.formatDateToString(item.getStatDate(), "yyyy-MM-dd"));
                    label.setRotation(Label.Rotation.VERTICAL);

                    xAxis.addLabels(label);
                }

                //
                maxValue = statsWebLogic.formatMaxValue(maxValue);
                minValue = statsWebLogic.formatMinValue(minValue);

                //
                yAxis.setMin(percent ? minValue / 100.0 : minValue * 1.0);
                yAxis.setMax(percent ? maxValue / 100.0 : maxValue * 1.0);
                yAxis.setSteps(percent ? statsWebLogic.calculateStep(minValue, maxValue) / 100.0 : statsWebLogic.calculateStep(minValue, maxValue) * 1.0);

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

                //
                chart.addElements(bar);
                chart.setXAxis(xAxis);
                chart.setYAxis(yAxis);
            }
        } catch (Exception e) {
            //
            GAlerter.lab("StatsLineController call weblogic to queryStatItems error.", e);

            //
            throw e;
        }

        //
        return chart.toString();
    }

    //
    @RequestMapping(value = "/bar/week")
    @ResponseBody
    public String barWeek(HttpServletRequest request, HttpServletResponse response,
                          //
                          @RequestParam(value = "domaincode", required = false) String domainCode,
                          @RequestParam(value = "sectioncode", required = false, defaultValue = "all") String sectionCode,
                          //
                          @RequestParam(value = "fromdate", required = false) String fromDateStr,
                          @RequestParam(value = "todate", required = false) String toDateStr) throws Exception {
        //
        Chart chart = new Chart();
        BarChart bar = new BarChart();
        XAxis xAxis = new XAxis();
        YAxis yAxis = new YAxis();

        //
        StatDateType dateType = StatDateType.WEEK;

        Date fromDate;
        Date toDate;

        try {
            //
            fromDate = statsWebLogic.getStatsStartDate(fromDateStr, dateType);
            toDate = statsWebLogic.getStatsEndDate(toDateStr, dateType);

            //
            long maxValue = Long.MIN_VALUE;
            long minValue = Long.MAX_VALUE;

            //
            List<StatItem> items = statsWebLogic.queryStatItems(new StatDomainDefault(domainCode), new StatSectionDefault(sectionCode), dateType, fromDate, toDate);
            if (items != null && items.size() > 0) {
                for (StatItem item : items) {
                    //
                    maxValue = Math.max(item.getStatValue(), maxValue);
                    minValue = Math.min(item.getStatValue(), minValue);

                    bar.addValues(item.getStatValue());

                    //
                    Label label = new Label(DateUtil.formatDateToString(item.getStatDate(), "yyyy-MM-dd"));
                    label.setRotation(Label.Rotation.VERTICAL);

                    xAxis.addLabels(label);
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

                //
                chart.addElements(bar);
                chart.setXAxis(xAxis);
                chart.setYAxis(yAxis);
            }
        } catch (Exception e) {
            //
            GAlerter.lab("StatsLineController call weblogic to queryStatItems error.", e);

            //
            throw e;
        }

        //
        return chart.toString();
    }

    //
    @RequestMapping(value = "/bar/month")
    @ResponseBody
    public String barMonth(HttpServletRequest request, HttpServletResponse response,
                           //
                           @RequestParam(value = "domaincode", required = false) String domainCode,
                           @RequestParam(value = "sectioncode", required = false, defaultValue = "all") String sectionCode,
                           //
                           @RequestParam(value = "fromdate", required = false) String fromDateStr,
                           @RequestParam(value = "todate", required = false) String toDateStr) throws Exception {
        //
        Chart chart = new Chart();
        BarChart bar = new BarChart();
        XAxis xAxis = new XAxis();
        YAxis yAxis = new YAxis();

        //
        StatDateType dateType = StatDateType.MONTH;

        Date fromDate;
        Date toDate;

        try {
            //
            fromDate = statsWebLogic.getStatsStartDate(fromDateStr, dateType);
            toDate = statsWebLogic.getStatsEndDate(toDateStr, dateType);

            //
            long maxValue = Long.MIN_VALUE;
            long minValue = Long.MAX_VALUE;

            //
            List<StatItem> items = statsWebLogic.queryStatItems(new StatDomainDefault(domainCode), new StatSectionDefault(sectionCode), dateType, fromDate, toDate);
            if (items != null && items.size() > 0) {
                for (StatItem item : items) {
                    //
                    maxValue = Math.max(item.getStatValue(), maxValue);
                    minValue = Math.min(item.getStatValue(), minValue);

                    bar.addValues(item.getStatValue());

                    //
                    Label label = new Label(DateUtil.formatDateToString(item.getStatDate(), "yyyy-MM"));
                    label.setRotation(Label.Rotation.VERTICAL);

                    xAxis.addLabels(label);
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

                //
                chart.addElements(bar);
                chart.setXAxis(xAxis);
                chart.setYAxis(yAxis);
            }
        } catch (Exception e) {
            //
            GAlerter.lab("StatsLineController call weblogic to queryStatItems error.", e);

            //
            throw e;
        }

        //
        return chart.toString();
    }

    //
    @RequestMapping(value = "/pie/day")
    @ResponseBody
    public String pieDay(HttpServletRequest request, HttpServletResponse response,
                         //
                         @RequestParam(value = "domaincode", required = true) String domainCode,
                         //
                         @RequestParam(value = "statdate", required = true) String statDateStr) throws Exception {
        //
        Chart chart = new Chart();
        PieChart pie = new PieChart();

        //
        StatDateType dateType = StatDateType.DAY;
        Date statDate;

        try {
            //
            statDate = statsWebLogic.getStatsStartDate(statDateStr, dateType);

            //
            Map<StatSection, StatItem> items = statsWebLogic.queryStatItemsByDomain(new StatDomainDefault(domainCode), dateType, statDate);
            for (StatItem item : items.values()) {
                //
                pie.addSlice(item.getStatValue(), item.getStatSection().getCode());
            }
        } catch (Exception e) {
            //
            GAlerter.lab("StatsLineController call weblogic to queryStatItemsByDomain error.", e);

            //
            throw e;
        }

        //
        chart.addElements(pie);

        //
        return chart.toString();
    }

    //
    @RequestMapping(value = "/pie/week")
    @ResponseBody
    public String pieWeek(HttpServletRequest request, HttpServletResponse response,
                          //
                          @RequestParam(value = "domaincode", required = true) String domainCode,
                          //
                          @RequestParam(value = "statdate", required = true) String statDateStr) throws Exception {
        //
        Chart chart = new Chart();
        PieChart pie = new PieChart();

        //
        StatDateType dateType = StatDateType.WEEK;
        Date statDate;

        try {
            //
            statDate = statsWebLogic.getStatsStartDate(statDateStr, dateType);

            //
            Map<StatSection, StatItem> items = statsWebLogic.queryStatItemsByDomain(new StatDomainDefault(domainCode), dateType, statDate);
            for (StatItem item : items.values()) {
                //
                pie.addSlice(item.getStatValue(), item.getStatSection().getCode());
            }
        } catch (Exception e) {
            //
            GAlerter.lab("StatsLineController call weblogic to queryStatItemsByDomain error.", e);

            //
            throw e;
        }

        //
        chart.addElements(pie);

        //
        return chart.toString();
    }

    //
    @RequestMapping(value = "/pie/month")
    @ResponseBody
    public String pieMonth(HttpServletRequest request, HttpServletResponse response,
                           //
                           @RequestParam(value = "domaincode", required = true) String domainCode,
                           //
                           @RequestParam(value = "statdate", required = true) String statDateStr) throws Exception {
        //
        Chart chart = new Chart();
        PieChart pie = new PieChart();

        //
        StatDateType dateType = StatDateType.MONTH;
        Date statDate;

        try {
            //
            statDate = statsWebLogic.getStatsStartDate(statDateStr, dateType);

            //
            Map<StatSection, StatItem> items = statsWebLogic.queryStatItemsByDomain(new StatDomainDefault(domainCode), dateType, statDate);
            for (StatItem item : items.values()) {
                //
                pie.addSlice(item.getStatValue(), item.getStatSection().getCode());
            }
        } catch (Exception e) {
            //
            GAlerter.lab("StatsLineController call weblogic to queryStatItemsByDomain error.", e);

            //
            throw e;
        }

        //
        chart.addElements(pie);

        //
        return chart.toString();
    }
}

