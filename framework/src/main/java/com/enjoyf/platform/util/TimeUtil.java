/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.util;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * A utility class to deal with time.
 */
public class TimeUtil {
    /**
     * One hour in msecs.
     */
    public static final int ONE_HOUR = 3600 * 1000;

    /**
     * A constant representing a day in msecs.
     */
    public static final int ONE_DAY = 24 * ONE_HOUR;

    /**
     * Represents today's date.
     */
    private static Date today = null;

    static {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        today = cal.getTime();
    }

    /**
     * Returns true if two times cross midnight.
     *
     * @param t1 A time in msecs.
     * @param t2 A time in msecs.
     *           It is assumed that t2 > t1.
     * @return Result
     */
    public static boolean crossesMidnight(long t1, long t2) {
        long diff = t2 - t1;
        if (diff <= 0) {
            return false;
        }

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(new Date(t1));
        c2.setTime(new Date(t2));
        //--
        // If the days are different, then we know we've crossed midnight.
        //--
        return c1.get(Calendar.DAY_OF_MONTH)
                != c2.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Sets the passed in Calender object to the end of the day.
     *
     * @param c Calendar
     */
    public static void setToEndOfDay(Calendar c) {
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 00);
    }

    /**
     * Sets the passed in Calender object to the beginning of the day.
     *
     * @param c Calendar
     */
    public static void setToBeginningOfDay(Calendar c) {
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
    }

    /**
     * Returns a copy of today's date, set to midnight.
     *
     * @return Today's date
     */
    public static Date getToday() {
        long currTime = System.currentTimeMillis();
        long diffTime = currTime - today.getTime();
        if (diffTime >= ONE_DAY) {
            p_setToday();
        }

        return (Date) today.clone();
    }

    /**
     * Sets the cached copy of today's date.  Only the first thread to acquire the lock
     * will set the date.  Everyone else will return.
     */
    private static void p_setToday() {
        synchronized (today) {
            // only allow the date to be updated once, by the first thread in here.
            // all other threads waiting on the lock will just return when they
            // the date no longer needs to be updated
            long currTime = System.currentTimeMillis();
            long diffTime = currTime - today.getTime();
            if (diffTime >= ONE_DAY) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                cal.set(Calendar.HOUR, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                today = cal.getTime();
            }
        }

    }

    /**
     * This method converts the date in the passed time zone to GMT
     *
     * @param date Date
     * @param zone Time OnlineZone of date
     * @return Converted Date
     */
    public static Date convertDateInGMT(Date date, TimeZone zone) {
        long offset = zone.getRawOffset();
        return new Date(date.getTime() - offset);

    }

    /**
     * Returns msecs, but the msecs are truncated to 0. This is useful
     * when writing timestamps to the db and you want to compare the
     * input to the output. A typical db column may truncate the msecs.
     */
    public static long truncateMillis(long millis) {
        return millis / 1000 * 1000;
    }
}
