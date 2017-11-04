package com.enjoyf.platform.service.stats;

import com.enjoyf.platform.util.DateUtil;
import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.*;

/**
 * @Auther: <a mailto:yinpengyi@platform.com>Yin Pengyi</a>
 */
@SuppressWarnings("serial")
public class StatDateType implements Serializable {

    private static Map<String, StatDateType> map = new HashMap<String, StatDateType>();

    public final static StatDateType TOTAL = new StatDateType("total", Calendar.ERA);
    public final static StatDateType YEAR = new StatDateType("year", Calendar.YEAR);
    public final static StatDateType MONTH = new StatDateType("month", Calendar.MONTH);
    public final static StatDateType WEEK = new StatDateType("week", Calendar.WEEK_OF_YEAR);
    public final static StatDateType DAY = new StatDateType("day", Calendar.DAY_OF_MONTH);
    public final static StatDateType HOUR = new StatDateType("hour", Calendar.HOUR_OF_DAY);
    public final static StatDateType MINUTE = new StatDateType("minute", Calendar.MINUTE);

    private String code;
    private int calendarType;

    public StatDateType(String c, int i) {
        this.code = c.toLowerCase();
        this.calendarType = i;

        map.put(code, this);
    }

    public String getCode() {
        return code;
    }

    public int getCalendarType() {
        return calendarType;
    }

    public int hashCode() {
        return code.hashCode();
    }

    public String toString() {
        return "StatDateType: code=" + code + ", calendarType=" + calendarType;
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof StatDateType)) {
            return false;
        }

        return code.equalsIgnoreCase(((StatDateType) obj).getCode());
    }

    public static StatDateType getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<StatDateType> getAll() {
        return map.values();
    }

    /**
     * 得到统计的开始日期
     */
    public Date getStartDateByType(Date statDate) {
        Calendar cal = Calendar.getInstance();

        cal.setTime(statDate);

        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);

        if (this.equals(MINUTE)) {
            return cal.getTime();
        }
        cal.set(Calendar.MINUTE, 0);

        if (this.equals(HOUR)) {
            return cal.getTime();
        }
        cal.set(Calendar.HOUR_OF_DAY, 0);

        if (this.equals(DAY)) {
            return cal.getTime();
        }

        if (this.equals(WEEK)) {
            cal.set(Calendar.DAY_OF_WEEK, 2);

            return cal.getTime();
        }

        cal.set(Calendar.DAY_OF_MONTH, 1);

        if (this.equals(MONTH)) {
            return cal.getTime();
        }
        cal.set(Calendar.MONTH, 0);

        if (this.equals(YEAR)) {
            return cal.getTime();
        }

        return cal.getTime();
    }

    /**
     * 得到统计的截止日期
     */
    public Date getEndDateByType(Date statDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getStartDateByType(statDate));

        cal.add(this.getCalendarType(), 1);

        return cal.getTime();
    }

    public Collection<Date> getStatDates(Date from, Date to) {
        Map<Date, Date> returnValues = new LinkedHashMap<Date, Date>();

        Date d = new Date(from.getTime());

        do {
            Date formatDate = this.getStartDateByType(d);

            returnValues.put(formatDate, formatDate);

            d = DateUtil.adjustDate(d, this.getCalendarType(), 1);
        } while (d.before(to));

        return returnValues.values();
    }

    public static void main(String args[]) {
        Date date = new Date();
        System.out.println(StatDateType.HOUR.getStartDateByType(date));
        System.out.println(StatDateType.HOUR.getEndDateByType(date));
        System.out.println(StatDateType.DAY.getStartDateByType(date));
        System.out.println(StatDateType.DAY.getEndDateByType(date));
        System.out.println(StatDateType.MONTH.getStartDateByType(date));
        System.out.println(StatDateType.MONTH.getEndDateByType(date));
        System.out.println(StatDateType.YEAR.getStartDateByType(date));
        System.out.println(StatDateType.YEAR.getEndDateByType(date));

        System.out.println(StatDateType.HOUR.getStatDates(StatDateType.DAY.getStartDateByType(date), StatDateType.DAY.getEndDateByType(date)));

    }
}
