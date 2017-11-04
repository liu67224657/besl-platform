package com.enjoyf.platform.service.content.social;

import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-7-12
 * Time: 下午2:51
 * To change this template use File | Settings | File Templates.
 */
public class SocialReportField extends AbstractObjectField {

    public static final SocialReportField REPORT_ID = new SocialReportField("report_id", ObjectFieldDBType.LONG, true, false);
    public static final SocialReportField CONTENT_ID = new SocialReportField("content_id", ObjectFieldDBType.LONG, true, false);
    public static final SocialReportField REPLY_ID= new SocialReportField("reply_id", ObjectFieldDBType.LONG, true, false);
    public static final SocialReportField UNO = new SocialReportField("uno", ObjectFieldDBType.STRING, true, false);
    public static final SocialReportField INFORMER_UNO = new SocialReportField("post_uno", ObjectFieldDBType.STRING, true, false);
    public static final SocialReportField CREATE_DATE = new SocialReportField("create_date", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final SocialReportField REPORT_TYPE = new SocialReportField("report_type", ObjectFieldDBType.INT, true, false);
    public static final SocialReportField VALID_STATUS = new SocialReportField("valid_status", ObjectFieldDBType.STRING, true, false);
    public static final SocialReportField REPORT_REASON = new SocialReportField("report_reason", ObjectFieldDBType.STRING, true, false);


    public SocialReportField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public SocialReportField(String column, ObjectFieldDBType type) {
        super(column, type);
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
