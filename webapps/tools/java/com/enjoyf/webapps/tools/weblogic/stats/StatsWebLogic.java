package com.enjoyf.webapps.tools.weblogic.stats;

import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.stats.*;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.webapps.tools.webpage.controller.stats.YesterdayStatsDTO;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;

@Service(value = "statsWebLogic")
public class StatsWebLogic {
    //
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static Date globalStartDate = null;

    static {
        try {
            globalStartDate = DateUtil.formatStringToDate("2012-07-01", "yyyy-MM-dd");
        } catch (ParseException e) {
            //
        }
    }

    //
    public StatItem getStatItem(StatDomain domain, StatSection section, StatDateType dateType, Date statDate) {
        StatItem returnValue = null;

        try {
            returnValue = StatServiceSngl.get().getStatItem(domain, section, dateType, statDate);
        } catch (ServiceException e) {
            //
            GAlerter.lab("Call StatServie to getStatItem error.", e);
        }

        return returnValue;
    }

    //query the statitme in time scope.
    public List<StatItem> queryStatItems(StatDomain domain, StatSection section, StatDateType dateType, Date from, Date to) {
        List<StatItem> returnValue = null;

        try {
            returnValue = StatServiceSngl.get().queryStatItems(domain, section, dateType, from, to);
        } catch (ServiceException e) {
            //
            GAlerter.lab("Call StatServie to queryStatItems error.", e);
        }

        return returnValue;
    }

    //query the selected domain's section items in a certain stat date.
    public Map<StatSection, StatItem> queryStatItemsByDomain(StatDomain domain, StatDateType dateType, Date statDate) {
        Map<StatSection, StatItem> returnValue = null;

        try {
            returnValue = StatServiceSngl.get().queryStatItemsByDomain(domain, dateType, statDate);
        } catch (ServiceException e) {
            //
            GAlerter.lab("Call StatServie to queryStatItemsByDomain error.", e);
        }

        return returnValue;
    }

    //query multi section maps.
    public Map<Date, Map<String, StatItem>> queryStatItemsByDomainPeriod(StatDomain domain, StatDateType dateType, Date fromDate, Date toDate) {
        Map<Date, Map<String, StatItem>> returnValue = new LinkedHashMap<Date, Map<String, StatItem>>();

        try {
            Map<Date, Map<StatSection, StatItem>> originalDatas = StatServiceSngl.get().queryStatItemsByDomainPeriod(domain, dateType, fromDate, toDate);

            for (Map.Entry<Date, Map<StatSection, StatItem>> entry : originalDatas.entrySet()) {
                Map<String, StatItem> items = new HashMap<String, StatItem>();

                for (StatItem item : entry.getValue().values()) {
                    items.put(item.getStatSection().getCode(), item);
                }

                returnValue.put(entry.getKey(), items);
            }
        } catch (ServiceException e) {
            //
            GAlerter.lab("Call StatServie to queryStatItemsByDomainPeriod error.", e);
        }

        return returnValue;
    }

    //
    public YesterdayStatsDTO generateYesterdayStats(Date yesterday) {
        YesterdayStatsDTO returnValue = new YesterdayStatsDTO();

        //pvs
        returnValue.setYesterdayPageView(getStatItem(OperationStatDomain.PAGE_VIEW, new StatSectionDefault(StatSection.DEFAULT_SECTION_KEY), StatDateType.DAY, yesterday));
        returnValue.setYesterdayUniqueUser(getStatItem(OperationStatDomain.UNIQUE_USER, new StatSectionDefault(StatSection.DEFAULT_SECTION_KEY), StatDateType.DAY, yesterday));
        returnValue.setYesterdayIP(getStatItem(OperationStatDomain.IP, new StatSectionDefault(StatSection.DEFAULT_SECTION_KEY), StatDateType.DAY, yesterday));
        returnValue.setYesterdayAvgTimeOfStay(getStatItem(OperationStatDomain.TOS, new StatSectionDefault(StatSection.DEFAULT_SECTION_KEY), StatDateType.DAY, yesterday));

        Map<StatSection, StatItem> uniqueUserRefs = queryStatItemsByDomain(OperationStatDomain.UV_REF, StatDateType.DAY, yesterday);
        if (uniqueUserRefs != null) {
            for (StatItem item : uniqueUserRefs.values()) {
                returnValue.getYesterdayUniqueRefs().add(item);
            }
        }

        Map<StatSection, StatItem> pageViewPages = queryStatItemsByDomain(OperationStatDomain.PV_PAGE, StatDateType.DAY, yesterday);
        if (pageViewPages != null) {
            for (StatItem item : pageViewPages.values()) {
                returnValue.getYesterdayPageViewPages().add(item);
            }
        }

        //back
        List<StatItem> twoDayBacks = queryStatItems(OperationStatDomain.BACK, new StatSectionDefault("two"), StatDateType.DAY, DateUtil.adjustDate(yesterday, Calendar.DAY_OF_YEAR, -8), DateUtil.adjustDate(yesterday, Calendar.DAY_OF_YEAR, -1));
        if (twoDayBacks != null) {
            for (StatItem item : twoDayBacks) {
                returnValue.getSecondDayBacks().add(item);
            }
        }

        List<StatItem> fourDayBacks = queryStatItems(OperationStatDomain.BACK, new StatSectionDefault("four"), StatDateType.DAY, DateUtil.adjustDate(yesterday, Calendar.DAY_OF_YEAR, -11), DateUtil.adjustDate(yesterday, Calendar.DAY_OF_YEAR, -4));
        if (twoDayBacks != null) {
            for (StatItem item : fourDayBacks) {
                returnValue.getFourDayBacks().add(item);
            }
        }

        List<StatItem> sevenDayBacks = queryStatItems(OperationStatDomain.BACK, new StatSectionDefault("seven"), StatDateType.DAY, DateUtil.adjustDate(yesterday, Calendar.DAY_OF_YEAR, -14), DateUtil.adjustDate(yesterday, Calendar.DAY_OF_YEAR, -7));
        if (twoDayBacks != null) {
            for (StatItem item : sevenDayBacks) {
                returnValue.getSevenDayBacks().add(item);
            }
        }

        //user event.
        returnValue.setYesterdayNewArticle(getStatItem(OperationStatDomain.USER_EVENT_POST, new StatSectionDefault(StatSection.DEFAULT_SECTION_KEY), StatDateType.DAY, yesterday));
        returnValue.setYesterdayNewForward(getStatItem(OperationStatDomain.USER_EVENT_FORWARD, new StatSectionDefault(StatSection.DEFAULT_SECTION_KEY), StatDateType.DAY, yesterday));
        returnValue.setYesterdayNewLike(getStatItem(OperationStatDomain.USER_EVENT_LIKE, new StatSectionDefault(StatSection.DEFAULT_SECTION_KEY), StatDateType.DAY, yesterday));
        returnValue.setYesterdayNewComment(getStatItem(OperationStatDomain.USER_EVENT_COMMENT, new StatSectionDefault(StatSection.DEFAULT_SECTION_KEY), StatDateType.DAY, yesterday));
        returnValue.setYesterdayNewRegister(getStatItem(OperationStatDomain.USER_EVENT_REGISTER, new StatSectionDefault(StatSection.DEFAULT_SECTION_KEY), StatDateType.DAY, yesterday));

        Map<StatSection, StatItem> registerRefs = queryStatItemsByDomain(OperationStatDomain.USER_EVENT_REGISTER_REF, StatDateType.DAY, yesterday);
        if (registerRefs != null) {
            for (StatItem item : registerRefs.values()) {
                returnValue.getYesterdayRegisterRefs().add(item);
            }
        }

        Map<StatSection, StatItem> postPhases = queryStatItemsByDomain(OperationStatDomain.USER_EVENT_POST_PHASE, StatDateType.DAY, yesterday);
        if (postPhases != null) {
            for (StatItem item : postPhases.values()) {
                returnValue.getYesterdayPostPhases().add(item);
            }
        }

        Map<StatSection, StatItem> commentPhases = queryStatItemsByDomain(OperationStatDomain.USER_EVENT_COMMENT_PHASE, StatDateType.DAY, yesterday);
        if (commentPhases != null) {
            for (StatItem item : commentPhases.values()) {
                returnValue.getYesterdayCommentPhases().add(item);
            }
        }

        Map<StatSection, StatItem> forwardPhases = queryStatItemsByDomain(OperationStatDomain.USER_EVENT_FORWARD_PHASE, StatDateType.DAY, yesterday);
        if (forwardPhases != null) {
            for (StatItem item : forwardPhases.values()) {
                returnValue.getYesterdayForwardPhases().add(item);
            }
        }

        Map<StatSection, StatItem> topPostUsers = queryStatItemsByDomain(OperationStatDomain.USER_EVENT_POST_TOP, StatDateType.DAY, yesterday);
        if (topPostUsers != null) {
            for (StatItem item : topPostUsers.values()) {
                returnValue.getYesterdayTopPostUsers().add(item);
            }
        }

        //content
        Date now = new Date();

        Date lastWeekDay = StatDateType.WEEK.getStartDateByType(DateUtil.adjustDate(now, Calendar.WEEK_OF_YEAR, -1));
        Map<StatSection, StatItem> contentViewTops = queryStatItemsByDomain(OperationStatDomain.CONTENT_VIEW_TOP, StatDateType.WEEK, lastWeekDay);
        if (contentViewTops != null) {
            for (StatItem item : contentViewTops.values()) {
                returnValue.getLastWeekTopViewedArticles().add(item);
            }
        }

        Map<StatSection, StatItem> contentHotTops = queryStatItemsByDomain(OperationStatDomain.CONTENT_HOT_TOP, StatDateType.WEEK, lastWeekDay);
        if (contentHotTops != null) {
            for (StatItem item : contentHotTops.values()) {
                returnValue.getLastWeekTopHotArticles().add(item);
            }
        }

        //
        return returnValue;
    }

    public Date getStatsStartDate(String dateStr, StatDateType dateType) throws ParseException {
        //
        Date returnValue = dateType.getStartDateByType(DateUtil.adjustDate(new Date(), dateType.getCalendarType(), -1));
        if (!Strings.isNullOrEmpty(dateStr)) {
            returnValue = DateUtil.formatStringToDate(dateStr, "yyyy-MM-dd");
        }

        //
        if (returnValue.before(globalStartDate)) {
            returnValue = new Date(globalStartDate.getTime());
        }

        //
        return dateType.getStartDateByType(returnValue);
    }

    public Date getStatsEndDate(String dateStr, StatDateType dateType) throws ParseException {
        //
        Date untilDate = dateType.getStartDateByType(DateUtil.adjustDate(new Date(), dateType.getCalendarType(), -1));
        Date returnValue = untilDate;
        if (!Strings.isNullOrEmpty(dateStr)) {
            returnValue = DateUtil.formatStringToDate(dateStr, "yyyy-MM-dd");
        }

        //
        if (returnValue.before(globalStartDate)) {
            returnValue = new Date(globalStartDate.getTime());
        }

        if (returnValue.after(untilDate)) {
            returnValue = new Date(untilDate.getTime());
        }

        return dateType.getEndDateByType(returnValue);
    }

    public List<StatDateType> getAllStatDateTypes() {
        List<StatDateType> returnValue = new ArrayList<StatDateType>();

        returnValue.add(StatDateType.DAY);
        returnValue.add(StatDateType.WEEK);
        returnValue.add(StatDateType.MONTH);

        return returnValue;
    }

    public long formatMinValue(long value) {
        return Math.min(value, 0);
    }

    public long formatMaxValue(long value) {
        long returnValue = value;

        if (returnValue > 0 && returnValue < Long.MAX_VALUE) {
            int l = Math.max(String.valueOf(returnValue).length() - 1, 1);

            long delta = Long.valueOf("1" + Strings.repeat("0", l - 1));

            returnValue = (returnValue / delta + 1) * delta;
        }

        return returnValue;
    }

    public long calculateStep(long min, long max) {
        return (max - min) / 10;
    }

    public List<StatItem> queryStatItemsByQuery(String publishId, StatDomainDefault domain, StatSectionDefault section, StatDateType dateType, Date from, Date to) {
        List<StatItem> returnValue = null;
        try {
            returnValue = StatServiceSngl.get().queryStatItemsByQuery(publishId, domain, section, dateType, from, to);
        } catch (ServiceException e) {
            GAlerter.lab("Call StatServie to queryStatItems error.", e);
        }
        return returnValue;
    }

//    public void reportPageViewByMongo(String advertiseId, StatDateType dateType, Date statDate) throws ServiceException {
////        StatServiceSngl.get().reportPageViewByMongo(advertiseId, dateType, statDate);
//    }
//
//    public void reportViewDepthByMongo(String advertiseId, StatDateType dateType, Date statDate) throws ServiceException {
////        StatServiceSngl.get().reportViewDepthByMongo(advertiseId, dateType, statDate);
//    }
//
//    public void reportViewTimeByMongo(String advertiseId, StatDateType dateType, Date statDate) throws ServiceException {
////        StatServiceSngl.get().reportViewTimeByMongo(advertiseId, dateType, statDate);
//    }
//
//    public void reportUserViewByMongo(String advertiseId, StatDateType dateType, Date statDate) throws ServiceException {
////        StatServiceSngl.get().reportUserViewByMongo(advertiseId, dateType, statDate);
//    }
//
//    public void reportViewBounceByMongo(String advertiseId, StatDateType dateType, Date statDate) throws ServiceException {
////        StatServiceSngl.get().reportViewBounceByMongo(advertiseId, dateType, statDate);
//    }
//
//    public void reportUserRetainedByMongo(String advertiseId, StatDateType dateType, Date statDate) throws ServiceException {
////        StatServiceSngl.get().reportUserRetainedByMongo(advertiseId, dateType, statDate);
//    }
}
