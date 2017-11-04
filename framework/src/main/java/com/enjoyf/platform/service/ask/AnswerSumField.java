package com.enjoyf.platform.service.ask;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;


public class AnswerSumField extends AbstractObjectField {
    public static final AnswerSumField ANSWERID = new AnswerSumField("answer_id", ObjectFieldDBType.LONG);
    public static final AnswerSumField VIEWSUM = new AnswerSumField("view_sum", ObjectFieldDBType.INT);
    public static final AnswerSumField REPLYSUM = new AnswerSumField("reply_sum", ObjectFieldDBType.INT);
    public static final AnswerSumField AGREESUM = new AnswerSumField("agree_sum", ObjectFieldDBType.INT);


    public AnswerSumField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}