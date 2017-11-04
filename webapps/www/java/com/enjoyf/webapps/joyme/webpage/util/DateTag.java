package com.enjoyf.webapps.joyme.webpage.util;


import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.StringUtil;

import java.util.Calendar;
import java.util.Date;


/**
 * <p/>
 * Description:日期类型的标签
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class DateTag {

    private static final long ONE_MINUTE = 60000L;

    private static final long ONE_HOUR = 3600000L;

    private static final String ONE_MINUTE_AGO = "分钟前";

    private static final String ONE_HOUR_AGO = "小时前";


    public static boolean isYesterDay(java.util.Date date) {
        return DateUtil.isYesterDay(date);
    }

    public static boolean isToday(java.util.Date date) {
        return DateUtil.isToday(date);
    }

    public static Date longToDate(Long c) {
        return new Date(c);
    }

    public static String parseDate(java.util.Date date) {
        return DateUtil.parseDate(date);
    }

    public static String parseDate(java.util.Date date, String formatString) {
        Calendar calendar = Calendar.getInstance();
        if (date == null) {
            return "";
        }
        calendar.setTime(date);

        if (isToday(date)) {//是否今天 DEFAULT_HOUR_MINITE_FORMAT
            return "今天 " + DateUtil.DateToString(date, DateUtil.HH_MM_FORMAT);
        }

        if (isYesterDay(date)) {//是否昨天
            return "昨天 " + DateUtil.DateToString(date, DateUtil.HH_MM_FORMAT);
        }

        return DateUtil.DateToString(date, StringUtil.isEmpty(formatString) ? DateUtil.DATE_FORMAT : formatString);
    }

    public static String dateToString(java.util.Date date, String formatString) {
        if (date == null) {
            return "";
        }
        return DateUtil.DateToString(date, StringUtil.isEmpty(formatString) ? DateUtil.DATE_FORMAT : formatString);
    }

    public static Integer getDayList(Date from, Date to) {
        return DateUtil.getDayList(from, to).size();
    }

    public static String parseWanbaDate(Long longdate) {

        System.out.println(longdate);

        Date date = new Date(longdate);
        Calendar calendar = Calendar.getInstance();
        if (date == null) {
            return "";
        }
        calendar.setTime(date);


        long delta = new Date().getTime() - date.getTime();


        //1小时内
        if (delta < 1L * ONE_HOUR) {
            long minutes = toMinutes(delta);
            return (minutes <= 0 ? 1 : minutes) + ONE_MINUTE_AGO;
        }
        //24小时内
        if (delta < 24L * ONE_HOUR) {
            long hours = toHours(delta);
            return (hours <= 0 ? 1 : hours) + ONE_HOUR_AGO;
        }

        //return "今天 " + DateUtil.DateToString(date, DateUtil.HH_MM_FORMAT);


        return DateUtil.DateToString(date, DateUtil.PATTERN_DATE);
    }

    private static long toHours(long date) {
        return toMinutes(date) / 60L;
    }

    private static long toMinutes(long date) {
        return toSeconds(date) / 60L;
    }

    private static long toSeconds(long date) {
        return date / 1000L;
    }


}
