package com.enjoyf.webapps.tools.webpage.controller.stats;

import com.enjoyf.platform.service.stats.OperationStatDomain;
import com.enjoyf.platform.service.stats.StatDateType;
import com.enjoyf.platform.service.stats.StatItem;
import com.enjoyf.platform.service.stats.StatSection;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.webapps.tools.weblogic.stats.StatsWebLogic;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-10-10 上午11:35
 * Description:
 */
@Controller
@RequestMapping(value = "/stats/content")
public class StatsContentController extends ToolsBaseController {
    //
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //
    @Resource(name = "statsWebLogic")
    private StatsWebLogic statsWebLogic;

    //
    @RequestMapping(value = "/top")
    public ModelAndView topView(HttpServletRequest request, HttpServletResponse response,//
                                //
                                @RequestParam(value = "statdate", required = false) String statDateStr,
                                @RequestParam(value = "datetypecode", required = false, defaultValue = "week") String dateTypeCode) throws Exception {
        //
        Map<String, Object> modelData = new HashMap<String, Object>();

        StatDateType dateType = StatDateType.getByCode(dateTypeCode);
        if (dateType == null) {
            dateType = StatDateType.WEEK;
        }

        //
        Date statDate;
        statDate = statsWebLogic.getStatsStartDate(statDateStr, dateType);

        //
        try {
            //the top views
            Map<StatSection, StatItem> viewItems = statsWebLogic.queryStatItemsByDomain(OperationStatDomain.CONTENT_VIEW_TOP, dateType, statDate);

            if (viewItems != null) {
                modelData.put("views", viewItems.values());
            }

            //the top hots
            Map<StatSection, StatItem> hotItems = statsWebLogic.queryStatItemsByDomain(OperationStatDomain.CONTENT_HOT_TOP, dateType, statDate);

            if (hotItems != null) {
                modelData.put("hots", hotItems.values());
            }
        } catch (Exception e) {
            GAlerter.lab("Call statsWebLogic queryStatItemsByDomain error.", e);

            throw e;
        }

        //
        modelData.put("dateTypes", this.getAllStatDateTypes());

        //
        modelData.put("dateTypeCode", dateType.getCode());
        modelData.put("statDateStr", DateUtil.formatDateToString(statDate, "yyyy-MM-dd"));

        //
        return new ModelAndView("/stats/content/top", modelData);
    }

    public List<StatDateType> getAllStatDateTypes() {
        List<StatDateType> returnValue = new ArrayList<StatDateType>();

        returnValue.add(StatDateType.WEEK);
        returnValue.add(StatDateType.MONTH);

        return returnValue;
    }
}

