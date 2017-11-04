/**
 * (C) 2008 Operation platform platform.com
 */
package com.enjoyf.platform.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author <a href=mailto:yinpy@platform.com>Yin Pengyi</a>
 */
public class DateUtil {
    //
    public static final String PATTERN_DATE_YEAR = "yyyy";
    public static final String PATTERN_DATE_MONTH = "yyyyMM";
    public static final String PATTERN_DATE_DAY = "yyyyMMdd";

    public static final String PATTERN_DATE_SHORT = "yyyyMMdd";
    public static final String PATTERN_DATE = "yyyy-MM-dd";
    public static final String PATTERN_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_DATE_MINUTES = "yyyy-MM-dd HH:mm";
    public static final String PATTERN_TIME = "HH:mm:ss";

    //
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss SSS";
    public static final String DEFAULT_DATE_FORMAT2 = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_DATE_FORMAT3 = "yyyy-MM-dd HH";
    public static final String DEFAULT_DATE_FORMAT4 = "yyyy年MM月dd日 HH:mm";
    public static final String HH_MM_FORMAT = "HH:mm";
    public static final String DATETIME_FORMAT = "yyyyMMddHHmmssSSS";
    public static final String YYYYMMDD_FORMAT = "yyyyMMdd";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String MM_DD_FORMAT = "MM月dd日";
    public static final String MM_DD_SHORT_FORMAT = "MM-dd";

    //
    public static final String DATE_TYPE_DAY = "DAY";
    public static final String DATE_TYPE_MONTH = "MONTH";
    public static final String DATE_TYPE_YEAR = "YEAR";
    public static final String DATE_TYPE_WEEK = "WEEK";
    public static final String DATE_TYPE_HOUR = "HOUR";
    public static final String DATE_TYPE_MINUTE = "MINUTE";
    public static final String DATE_TYPE_SECOND = "SECOND";

    public static final long MSEC_OF_MIN = 60000;

    public static String formatDateToString(Date d, String formatPattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(formatPattern);

        return dateFormat.format(d);
    }

    public static Date formatStringToDate(String dateString, String formatPattern) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(formatPattern);

        return dateFormat.parse(dateString);
    }

    public static Date adjustDate(Date d, int calendarType, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);

        cal.add(calendarType, amount);

        return cal.getTime();
    }

    public static Date ignoreTime(Date d) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    public static Date dayLastTime(Date d) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);

        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);

        return cal.getTime();
    }

    public static Date getCutMinuteDate(Date d, int cutInterval) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);

        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        int minute = cal.get(Calendar.MINUTE);
        cal.set(Calendar.MINUTE, (minute / cutInterval) * cutInterval);

        return cal.getTime();
    }

    public static List<Date> getYearList(Date from, Date to) {
        Calendar fromCal = Calendar.getInstance();
        Calendar toCal = Calendar.getInstance();

        fromCal.setTime(from);
        fromCal.set(Calendar.MONTH, 0);
        fromCal.set(Calendar.DAY_OF_MONTH, 1);
        fromCal.set(Calendar.HOUR_OF_DAY, 0);
        fromCal.set(Calendar.MINUTE, 0);
        fromCal.set(Calendar.SECOND, 0);
        fromCal.set(Calendar.MILLISECOND, 0);

        toCal.setTime(to);
        toCal.set(Calendar.MONTH, 0);
        toCal.set(Calendar.DAY_OF_MONTH, 1);
        toCal.set(Calendar.HOUR_OF_DAY, 0);
        toCal.set(Calendar.MINUTE, 0);
        toCal.set(Calendar.SECOND, 0);
        toCal.set(Calendar.MILLISECOND, 0);

        List<Date> returnList = new ArrayList<Date>();
        while (!toCal.before(fromCal)) {
            returnList.add(fromCal.getTime());
            fromCal.add(Calendar.MONTH, 1);
        }

        return returnList;
    }

    public static String getQuarterName(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int month = cal.get(Calendar.MONTH);
        String quarter = "A";

        if (month < 3) {
            quarter = "A";
        } else if (month < 6) {
            quarter = "B";
        } else if (month < 9) {
            quarter = "C";
        } else {
            quarter = "D";
        }

        return quarter;
    }

    public static List<Date> getQuarterList(Date from, Date to) {
        Calendar fromCal = Calendar.getInstance();
        Calendar toCal = Calendar.getInstance();

        fromCal.setTime(from);
        fromCal.set(Calendar.DAY_OF_MONTH, 1);
        fromCal.set(Calendar.HOUR_OF_DAY, 0);
        fromCal.set(Calendar.MINUTE, 0);
        fromCal.set(Calendar.SECOND, 0);
        fromCal.set(Calendar.MILLISECOND, 0);

        toCal.setTime(to);
        toCal.set(Calendar.DAY_OF_MONTH, 1);
        toCal.set(Calendar.HOUR_OF_DAY, 0);
        toCal.set(Calendar.MINUTE, 0);
        toCal.set(Calendar.SECOND, 0);
        toCal.set(Calendar.MILLISECOND, 0);

        List<Date> returnList = new ArrayList<Date>();
        while (!toCal.before(fromCal)) {
            returnList.add(fromCal.getTime());
            fromCal.add(Calendar.MONTH, 3);
            int month = fromCal.get(Calendar.MONTH);
            int firstMonthOfFromCal;

            if (month < 3) {
                firstMonthOfFromCal = 0;
            } else if (month < 6) {
                firstMonthOfFromCal = 3;
            } else if (month < 9) {
                firstMonthOfFromCal = 6;
            } else {
                firstMonthOfFromCal = 9;
            }
            fromCal.set(Calendar.MONTH, firstMonthOfFromCal);
        }

        return returnList;
    }

    public static int getWeekOfDate(Date d) {
        Calendar returnCal = Calendar.getInstance();
        returnCal.setTime(d);

        return returnCal.get(Calendar.WEEK_OF_YEAR);
    }


    public static List<Date> getMonthList(Date from, Date to) {
        Calendar fromCal = Calendar.getInstance();
        Calendar toCal = Calendar.getInstance();

        fromCal.setTime(from);
        fromCal.set(Calendar.DAY_OF_MONTH, 1);
        fromCal.set(Calendar.HOUR_OF_DAY, 0);
        fromCal.set(Calendar.MINUTE, 0);
        fromCal.set(Calendar.SECOND, 0);
        fromCal.set(Calendar.MILLISECOND, 0);

        toCal.setTime(to);
        toCal.set(Calendar.DAY_OF_MONTH, 1);
        toCal.set(Calendar.HOUR_OF_DAY, 0);
        toCal.set(Calendar.MINUTE, 0);
        toCal.set(Calendar.SECOND, 0);
        toCal.set(Calendar.MILLISECOND, 0);

        List<Date> returnList = new ArrayList<Date>();
        while (!toCal.before(fromCal)) {
            returnList.add(fromCal.getTime());
            fromCal.add(Calendar.MONTH, 1);
        }

        return returnList;
    }

    public static List<Date> getDayList(Date from, Date to) {
        Calendar fromCal = Calendar.getInstance();
        Calendar toCal = Calendar.getInstance();

        fromCal.setTime(from);
        fromCal.set(Calendar.HOUR_OF_DAY, 0);
        fromCal.set(Calendar.MINUTE, 0);
        fromCal.set(Calendar.SECOND, 0);
        fromCal.set(Calendar.MILLISECOND, 0);

        toCal.setTime(to);
        toCal.set(Calendar.HOUR_OF_DAY, 0);
        toCal.set(Calendar.MINUTE, 0);
        toCal.set(Calendar.SECOND, 0);
        toCal.set(Calendar.MILLISECOND, 0);

        List<Date> returnList = new ArrayList<Date>();
        while (!toCal.before(fromCal)) {
            returnList.add(fromCal.getTime());
            fromCal.add(Calendar.DAY_OF_YEAR, 1);
        }

        return returnList;
    }


    public static List<Date> getHourList(Date from, Date to) {
        Calendar fromCal = Calendar.getInstance();
        Calendar toCal = Calendar.getInstance();

        fromCal.setTime(from);
        fromCal.set(Calendar.MINUTE, 0);
        fromCal.set(Calendar.SECOND, 0);
        fromCal.set(Calendar.MILLISECOND, 0);

        toCal.setTime(to);
        toCal.set(Calendar.MINUTE, 0);
        toCal.set(Calendar.SECOND, 0);
        toCal.set(Calendar.MILLISECOND, 0);

        List<Date> returnList = new ArrayList<Date>();
        while (!toCal.before(fromCal)) {
            returnList.add(fromCal.getTime());
            fromCal.add(Calendar.HOUR, 1);
        }

        return returnList;
    }

    //
    public static Date convertToSameDayTime(final Date destDate, final Date dameDate) {
        Calendar returnCal = Calendar.getInstance();

        Calendar today = Calendar.getInstance();
        today.setTime(dameDate);

        returnCal.setTime(destDate);

        returnCal.set(Calendar.YEAR, today.get(Calendar.YEAR));
        returnCal.set(Calendar.MONTH, today.get(Calendar.MONTH));
        returnCal.set(Calendar.DAY_OF_MONTH, today.get(Calendar.DAY_OF_MONTH));

        return returnCal.getTime();
    }

    public static Date getYesterday(final Date nowDate) {
        Calendar returnCal = Calendar.getInstance();
        returnCal.setTime(nowDate);
        returnCal.add(Calendar.DATE, -1);

        return returnCal.getTime();
    }

    public static Date getLastWeek(final Date nowDate) {
        Date a = DateUtils.addDays(nowDate, -1);
        Calendar cal = Calendar.getInstance();
        cal.setTime(a);
        cal.add(Calendar.WEEK_OF_YEAR, -1);// 一周
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return cal.getTime();
    }

    public static Date getLastSunday(final Date nowDate) {
        Calendar returnCal = Calendar.getInstance();
        returnCal.setTime(nowDate);
        returnCal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

        return returnCal.getTime();
    }

    public static Date getThisWeek(final Date nowDate) {
        Calendar returnCal = Calendar.getInstance();
        returnCal.setTime(nowDate);
        returnCal.add(Calendar.WEEK_OF_MONTH, 0);

        return returnCal.getTime();
    }


    public static Date getLastMonth(final Date nowDate) {
        Calendar returnCal = Calendar.getInstance();
        returnCal.setTime(nowDate);
        returnCal.set(Calendar.DATE, 1);// 设为当前月的1号
        returnCal.add(Calendar.MONTH, -1);// 减一个月，变为下月的1号

        return returnCal.getTime();
    }

    // check the day if the first day of the month.
    public static boolean isFirstDayOfMonth(Date d) {
        Calendar day = Calendar.getInstance();
        day.setTime(d);

        return day.get(Calendar.DAY_OF_MONTH) == 1;
    }

    // check the day if the first day of the year.
    public static boolean isFirstDayOfYear(Date d) {
        Calendar day = Calendar.getInstance();
        day.setTime(d);

        return day.get(Calendar.DAY_OF_MONTH) == 1 && day.get(Calendar.MONTH) == 1;
    }

    public static long ignoreSec(long t) {
        long min = t / MSEC_OF_MIN;
        long left = t % MSEC_OF_MIN;

        if ((left * 2) >= MSEC_OF_MIN) {
            min++;
        }

        return min * MSEC_OF_MIN;
    }


    /**
     * 将Date转换为字符串
     *
     * @param date          Date            要转换的日期
     * @param dateFormatStr String 要转换的日期类型
     * @return String              返加String类型的日期
     */
    public static String DateToString(Date date, String dateFormatStr) {
        if (date == null) {
            return "";
        } else {
            if (dateFormatStr == null || "".equals(dateFormatStr)) {
                dateFormatStr = DEFAULT_DATE_FORMAT;
            }
            SimpleDateFormat simpleDteFormat = new SimpleDateFormat(dateFormatStr);
            return simpleDteFormat.format(date);
        }

    }

    /**
     * 返回当前日期时间的字符串
     *
     * @param dateFormatStr String  要转换的日期类型
     * @return String               返加String类型的日期
     */
    public static String getCurrentDateTime(String dateFormatStr) {
        if (dateFormatStr == null || "".equals(dateFormatStr)) {
            dateFormatStr = DEFAULT_DATE_FORMAT2;
        }
        Date date = new Date();
        SimpleDateFormat simpleDteFormat = new SimpleDateFormat(dateFormatStr);
        return simpleDteFormat.format(date);
    }

    /**
     * 返回当前日期的字符串
     *
     * @param dateFormatStr String  日期格式
     * @return String               返回当前日期的字符串
     */
    public static String getCurrentDate(String dateFormatStr) {
        if (dateFormatStr == null || "".equals(dateFormatStr)) {
            dateFormatStr = DATE_FORMAT;
        }
        Date date = new Date();
        SimpleDateFormat simpleDteFormat = new SimpleDateFormat(dateFormatStr);
        return simpleDteFormat.format(date);
    }

    /**
     * 将字符串转换为Date
     *
     * @param strDate       String        被转换的String类型的日期
     * @param strDateFormat String  Date格式
     * @return Date                 返加Date类型的日期
     */
    public static Date StringTodate(String strDate, String strDateFormat) {
        if (StringUtil.isEmpty(strDate)) {
            return null;
        } else {
            if ("".equals(strDateFormat) || strDateFormat == null) {
                strDateFormat = DEFAULT_DATE_FORMAT2;
            }
            Date rDate;
            SimpleDateFormat format = new SimpleDateFormat(strDateFormat);
            try {
                rDate = format.parse(strDate);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return rDate;
        }
    }

    /**
     * 将string转换成指定类型的Timestamp
     *
     * @param str  String   被转换的String类型的日期
     * @param type String  日期格式
     * @return Timestamp   返加Timestamp类型的日期
     */
    public static Timestamp cString2Timestamp(String str, String type) {
        if (type == null || type.equals("")) {
            type = DEFAULT_DATE_FORMAT2;
        }
        if (str.length() <= 10) {
            str = str + " 00:00:00";
        }
        SimpleDateFormat df = new java.text.SimpleDateFormat(type);
        try {
            return new Timestamp(df.parse(str).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Timestamp getTimeStamp() {
        return cString2Timestamp(getCurrentDateTime(null), null);
    }

    /**
     * 将Timestamp转换成指定类型的string
     *
     * @param ts   Timestamp    被转换的Timestamp类型的日期
     * @param type String     日期格式
     * @return String         返加String类型的日期
     */
    public static String cTimestamp2String(Timestamp ts, String type) {
        SimpleDateFormat df = new java.text.SimpleDateFormat(type);
        return df.format(new java.util.Date(ts.getTime()));
    }

    /**
     * 将Timestamp转换成date
     *
     * @param ts Timestamp   被转换的Timestamp类型的日期
     * @return Date          返加Date类型的日期
     */
    public static Date cTimestamp2Date(Timestamp ts) {
        String strDate = cTimestamp2String(ts, DEFAULT_DATE_FORMAT2);
        return StringTodate(strDate, DEFAULT_DATE_FORMAT2);
    }

    /**
     * 日期相加函数
     *
     * @param sorDate  String   被加的日期,必须为String类型
     * @param value    int        天数,可为负数
     * @param dateType String  日期格式
     * @return String          相加后的日期,必须为String类型
     */
    public static String dateAdd(String sorDate, int value, String dateType) {
        if (dateType == null || value == 0 || sorDate == null) {
            return sorDate;
        }
        Date date = DateUtil.StringTodate(sorDate, PATTERN_DATE_DAY);
        Date getDate = DateUtil.dateAdd(date, value, dateType);
        return DateUtil.DateToString(getDate, PATTERN_DATE_DAY);
    }

    /**
     * 日期相加函数
     *
     * @param sorDate  Date     被加的日期,必须为Date类型
     * @param value    int        天数,可为负数
     * @param dateType String  日期格式
     * @return Date            相加后的日期
     */
    public static Date dateAdd(Date sorDate, int value, String dateType) {
        if (dateType == null || value == 0 || sorDate == null) {
            return sorDate;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sorDate);
        if (dateType.equalsIgnoreCase(DATE_TYPE_SECOND)) {
            calendar.add(Calendar.SECOND, value);
        } else if (dateType.equalsIgnoreCase(DATE_TYPE_MINUTE)) {
            calendar.add(Calendar.MINUTE, value);
        } else if (dateType.equalsIgnoreCase(DATE_TYPE_HOUR)) {
            calendar.add(Calendar.HOUR, value);
        } else if (dateType.equalsIgnoreCase(DATE_TYPE_WEEK)) {
            calendar.add(Calendar.WEDNESDAY, value);
        } else if (dateType.equalsIgnoreCase(DATE_TYPE_DAY)) {
            calendar.add(Calendar.DATE, value);
        } else if (dateType.equalsIgnoreCase(DATE_TYPE_MONTH)) {
            calendar.add(Calendar.MONTH, value);
        } else if (dateType.equalsIgnoreCase(DATE_TYPE_YEAR)) {
            calendar.add(Calendar.YEAR, value);
        }
        return calendar.getTime();
    }


    //年份是否相同
    public static boolean isSameYear(Date from, Date to) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(from);
        int fromy = cal.get(Calendar.YEAR);
        cal.setTime(to);
        int toy = cal.get(Calendar.YEAR);
        return fromy == toy;
    }

    /**
     * 判断日期是否是今天
     *
     * @param from
     * @return
     */
    public static boolean isSameDay(Date from, Date to) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(from);
        long fromd = cal.get(Calendar.DAY_OF_YEAR);
        cal.setTime(to);
        long tod = cal.get(Calendar.DAY_OF_YEAR);
        return isSameYear(from, to) && fromd == tod;
    }

    /**
     * 判断日期是否是今天
     *
     * @param from
     * @return
     */
    public static boolean isToday(Date from) {
        Date to = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(from);
        long fromd = cal.get(Calendar.DAY_OF_YEAR);
        cal.setTime(to);
        long tod = cal.get(Calendar.DAY_OF_YEAR);
        return isSameYear(from, to) && fromd == tod;
    }


    /**
     * 判断日期是否是昨天
     *
     * @param from
     * @return
     */
    public static boolean isYesterDay(Date from) {
        Date to = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(from);
        int fromd = cal.get(Calendar.DAY_OF_YEAR);
        cal.setTime(to);
        int tod = cal.get(Calendar.DAY_OF_YEAR);
        return isSameYear(from, to) && ((fromd + 1) == tod);
    }

    public static boolean isBeforeYesterDay(Date from) {
        Date to = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(from);
        int fromd = cal.get(Calendar.DAY_OF_YEAR);
        cal.setTime(to);
        int tod = cal.get(Calendar.DAY_OF_YEAR);
        return isSameYear(from, to) && ((fromd + 2) == tod);
    }

    /**
     * 将时间转换成 昨天 12:30 /  今天 12:40 / 10分钟前 //两个小时前
     *
     * @param date
     * @return
     */
    public static String parseDateByLive(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date == null) {
            return "";
        }
        calendar.setTime(date);

        if (isToday(date)) {//是否今天 DEFAULT_HOUR_MINITE_FORMAT
            return DateToString(date, DateUtil.HH_MM_FORMAT);
        }

        if (isYesterDay(date)) {//是否昨天
            return "昨天 " + DateToString(date, DateUtil.HH_MM_FORMAT);
        }

        return DateToString(date, DateUtil.DEFAULT_DATE_FORMAT4);
    }

    /**
     * 将时间转换成 今天： 12:30 /  昨天：昨天 12:40 / 其他：yyyy年mm月dd日 hh:mm
     *
     * @param date
     * @return
     */
    public static String parseDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date == null) {
            return "";
        }
        calendar.setTime(date);

        if (isToday(date)) {//是否今天 DEFAULT_HOUR_MINITE_FORMAT
            return DateToString(date, DateUtil.HH_MM_FORMAT);
        }

        if (isYesterDay(date)) {//是否昨天
            return "昨天 " + DateToString(date, DateUtil.HH_MM_FORMAT);
        }

        if (isBeforeYesterDay(date)) {//是否前天
            return "前天 " + DateToString(date, DateUtil.HH_MM_FORMAT);
        }

        if (isSameYear(date, new Date())) {//是否今年
            return DateToString(date, DateUtil.MM_DD_FORMAT);
        }

        return DateToString(date, DateUtil.DATE_FORMAT);
    }

    public static String parse24Date(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date == null) {
            return "";
        }
        calendar.setTime(date);

        if (isToday(date)) {//是否今天 DEFAULT_HOUR_MINITE_FORMAT
            return DateToString(date, DateUtil.HH_MM_FORMAT);
        }

        return DateToString(date, DateUtil.MM_DD_SHORT_FORMAT);
    }

    public static String cSecond2HHmmss(long seconds) {
        String timeStr = "";

        long hour = seconds / 3600;
        long minute = seconds % 3600 / 60;
        long second = seconds % 60;

        if (hour > 0) {
            if (hour < 10) {
                timeStr = "0" + hour + ":";
            } else {
                timeStr = hour + ":";
            }
        }

        if (minute < 10) {
            timeStr = timeStr + "0" + minute + ":";
        } else {
            timeStr = timeStr + minute + ":";
        }

        if (second < 10) {
            timeStr = timeStr + "0" + second;
        } else {
            timeStr = timeStr + second;
        }

        return timeStr;
    }

    /**
     * 将长整型数字转换为日期格式的字符串
     *
     * @param time
     * @param format
     * @return
     */
    public static String convert2String(long time, String format) {
        if (time > 0l) {
            if (StringUtils.isEmpty(format))
                format = PATTERN_DATE_TIME;
            SimpleDateFormat sf = new SimpleDateFormat(format);
            Date date = new Date(time);
            return sf.format(date);
        }
        return "";
    }


    public static boolean isInDate(Date date, String strDateBegin, String strDateEnd) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = null;
        Date end = null;
        try {
            start = sdf.parse(strDateBegin);
            end = sdf.parse(strDateEnd);
            if (date.getTime() >= start.getTime() && date.getTime() <= end.getTime()) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Date getWeekBegin(Date date) {
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK); // 因为按中国礼拜一作为,
        if (1 == dayOfWeek) {
            cd.add(Calendar.DAY_OF_WEEK, -1);
        }
        cd.setFirstDayOfWeek(Calendar.MONDAY);

        int day = cd.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
        cd.add(Calendar.DATE, cd.getFirstDayOfWeek() - day);
        return cd.getTime();
    }

    public static Date getMonthBegin(Date date) {
        int mondayPlus;
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        int dayOfMonth = cd.get(Calendar.DAY_OF_MONTH);
        if (dayOfMonth == 1) {
            mondayPlus = 0;
        } else {
            mondayPlus = 1 - dayOfMonth;
        }
        cd.add(Calendar.DATE, mondayPlus);
        return cd.getTime();
    }

    public static Date getMonthByFirstOne(Date date) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        //将小时至0
        c.set(Calendar.HOUR_OF_DAY, 0);
        //将分钟至0
        c.set(Calendar.MINUTE, 0);
        //将秒至0
        c.set(Calendar.SECOND, 0);
        //将毫秒至0
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }


    /**
     * @param type    类型  point积分 prestige声望  consumption消费
     * @param rankKey 7=周排行  5=月排行 -1=总排行
     * @return
     */
    public static String getRankLinKey(String type, int rankKey) {
        if (rankKey == Calendar.DAY_OF_WEEK) {
            return type + "_" + DateUtil.formatDateToString(DateUtil.getWeekBegin(new Date()), DateUtil.YYYYMMDD_FORMAT) + "_" + rankKey;
        } else if (rankKey == Calendar.DAY_OF_MONTH) {
            return type + "_" + DateUtil.formatDateToString(DateUtil.getMonthBegin(new Date()), DateUtil.YYYYMMDD_FORMAT) + "_" + rankKey;
        } else {
            return type + "_all_" + rankKey;
        }
    }

    public static String getLastMonthRankLinKey(String type, int rankKey) {
        if (rankKey == Calendar.DAY_OF_WEEK) {
            return type + "_" + DateUtil.formatDateToString(DateUtil.getLastWeek(new Date()), DateUtil.YYYYMMDD_FORMAT) + "_" + rankKey;
        } else if (rankKey == Calendar.DAY_OF_MONTH) {
            return type + "_" + DateUtil.formatDateToString(DateUtil.getLastMonth(new Date()), DateUtil.YYYYMMDD_FORMAT) + "_" + rankKey;
        } else {
            return type + "_all_" + rankKey;
        }
    }


    public static String getLastMonthLastDay(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cale = Calendar.getInstance();
        cale.setTime(date);
        cale.set(Calendar.DAY_OF_MONTH, 0);
        String lastDay = format.format(cale.getTime());
        return lastDay;
    }

    /***
     *
     * @param date
     * @param dateFormat : e.g:yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String formatDateByPattern(Date date,String dateFormat){
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        String formatTimeStr = null;
        if (date != null) {
            formatTimeStr = sdf.format(date);
        }
        return formatTimeStr;
    }
    /***
     * convert Date to cron ,eg.  "0 06 10 15 1 ? 2014"
     * @param date  : 时间点
     * @return
     */
    public static String getCron(java.util.Date  date){
        String dateFormat="ss mm HH dd MM ? yyyy";
        return formatDateByPattern(date, dateFormat);
    }


}
