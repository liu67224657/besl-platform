package com.enjoyf.platform.service.ask;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;


public class AnswerField extends AbstractObjectField {
    public static final AnswerField ANSWERID = new AnswerField("answer_id", ObjectFieldDBType.LONG);
    public static final AnswerField ANSWERPROFILEID = new AnswerField("answer_profile_id", ObjectFieldDBType.STRING);
    public static final AnswerField QUESTIONID = new AnswerField("question_id", ObjectFieldDBType.LONG);
    public static final AnswerField BODY = new AnswerField("body", ObjectFieldDBType.STRING);
    public static final AnswerField ANSWERTIME = new AnswerField("answer_time", ObjectFieldDBType.TIMESTAMP);
    public static final AnswerField IS_ACCEPT = new AnswerField("is_accept", ObjectFieldDBType.BOOLEAN);
    public static final QuestionField RICHTEXT = new QuestionField("richtext", ObjectFieldDBType.STRING);
    public static final QuestionField ASKVOICE = new QuestionField("askvoice", ObjectFieldDBType.STRING);
    public static final QuestionField REMOVESTATUS = new QuestionField("remove_status", ObjectFieldDBType.INT);


    public AnswerField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}