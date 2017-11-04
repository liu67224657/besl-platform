/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.shorturl;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-31 上午11:16
 * Description:
 */
public class ShortUrlField extends AbstractObjectField {
    //
    public static final ShortUrlField URLKEY = new ShortUrlField("URLKEY", ObjectFieldDBType.STRING, true, true);
    public static final ShortUrlField URL = new ShortUrlField("URL", ObjectFieldDBType.STRING, true, true);

    public static final ShortUrlField PROTOCOLTYPE = new ShortUrlField("PROTOCOLTYPE", ObjectFieldDBType.STRING, true, false);

    public static final ShortUrlField FILETYPE = new ShortUrlField("FILETYPE", ObjectFieldDBType.STRING, true, false);

    public static final ShortUrlField URLSTATUS = new ShortUrlField("URLSTATUS", ObjectFieldDBType.STRING, true, false);
    public static final ShortUrlField INITDATE = new ShortUrlField("INITDATE", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final ShortUrlField INITUNO = new ShortUrlField("INITUNO", ObjectFieldDBType.STRING, true, false);

    public static final ShortUrlField CLICKTIMES = new ShortUrlField("CLICKTIMES", ObjectFieldDBType.LONG, true, false);

    //
    public ShortUrlField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public ShortUrlField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
