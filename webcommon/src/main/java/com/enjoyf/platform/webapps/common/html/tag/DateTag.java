package com.enjoyf.platform.webapps.common.html.tag;

import java.util.Date;

/**
 * <p/>
 * Description: 日期标签
 * </p>
 *
 * @author: zx
 */
public class DateTag {


    public static Date long2Date(Long timestamp) {
        if (timestamp == null) {
            return null;
        }

        return new Date(timestamp);
    }


}
