/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db;

import com.enjoyf.platform.util.DateUtil;
import com.google.common.base.Strings;

import java.util.Date;

public class TableUtil {

    public static String getTableDateSuffix(Date d, String format) {
        return DateUtil.formatDateToString(d, format);
    }

    public static String getTableNumSuffix(long l, int total) {
        int idx = (int) (l % total);
        int size = String.valueOf(total - 1).length();
        int len = String.valueOf(idx).length();

        return Strings.repeat("0", size - len) + idx;
    }

    public static String getTableNumSuffix(int i, int total) {
        int idx = Math.abs(i) % total;
        int size = String.valueOf(total - 1).length();
        int len = String.valueOf(idx).length();

        return Strings.repeat("0", size - len) + idx;
    }
}
