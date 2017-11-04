package com.enjoyf.platform.service.ask;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhimingli on 2016/12/7 0007.
 */
public class AskReportField extends AbstractObjectField {
    public static final AnswerSumField REPORTID = new AnswerSumField("report_id", ObjectFieldDBType.STRING);
    public static final AnswerSumField ITEMTYPE = new AnswerSumField("item_type", ObjectFieldDBType.INT);
    public static final AnswerSumField DESTID = new AnswerSumField("dest_id", ObjectFieldDBType.LONG);
    public static final AnswerSumField DESTPROFILEID = new AnswerSumField("dest_profileid", ObjectFieldDBType.STRING);
    public static final AnswerSumField ASKREPORTTYPE = new AnswerSumField("ask_report_type", ObjectFieldDBType.INT);
    public static final AnswerSumField CREATETIME = new AnswerSumField("create_time", ObjectFieldDBType.TIMESTAMP);
    public static final AnswerSumField VALIDSTATUS = new AnswerSumField("valid_status", ObjectFieldDBType.STRING);
    public static final AnswerSumField EXTSTR = new AnswerSumField("extstr", ObjectFieldDBType.STRING);

    public AskReportField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
