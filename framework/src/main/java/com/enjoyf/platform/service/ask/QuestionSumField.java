package com.enjoyf.platform.service.ask;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;


public class QuestionSumField extends AbstractObjectField {
    public static final QuestionSumField QUESTIONID = new QuestionSumField("question_id", ObjectFieldDBType.LONG);
    public static final QuestionSumField VIEWSUM = new QuestionSumField("view_sum", ObjectFieldDBType.INT);
    public static final QuestionSumField ANSEWERSUM = new QuestionSumField("ansewer_sum", ObjectFieldDBType.INT);
    public static final QuestionSumField FOLLOWSUM = new QuestionSumField("follow_sum", ObjectFieldDBType.INT);
    public static final QuestionSumField QUESTIONCREATETIME = new QuestionSumField("question_create_time", ObjectFieldDBType.TIMESTAMP);


    public QuestionSumField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}