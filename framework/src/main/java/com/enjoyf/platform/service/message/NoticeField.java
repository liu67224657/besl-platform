package com.enjoyf.platform.service.message;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 11-12-16
 * Time: 上午10:04
 * To change this template use File | Settings | File Templates.
 */
public class NoticeField extends AbstractObjectField {
    //                                      noticeId
    public static final NoticeField NOTICEID = new NoticeField("NOTICEID", ObjectFieldDBType.LONG, true, true);
    public static final NoticeField VALIDSTATUS = new NoticeField("VALIDSTATUS", ObjectFieldDBType.STRING, true, false);
    public static final NoticeField NOTICECOUNT = new NoticeField("NOTICECOUNT", ObjectFieldDBType.INT, true, false);
    public static final NoticeField READDATE = new NoticeField("READDATE", ObjectFieldDBType.TIMESTAMP, true, false);

    //
    public NoticeField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public NoticeField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
