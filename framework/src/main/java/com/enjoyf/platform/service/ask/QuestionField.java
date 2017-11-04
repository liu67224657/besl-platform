
package com.enjoyf.platform.service.ask;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;


public class QuestionField extends AbstractObjectField {
    public static final QuestionField QUESTIONID = new QuestionField("question_id", ObjectFieldDBType.LONG);
    public static final QuestionField GAMEID = new QuestionField("game_id", ObjectFieldDBType.LONG);
    public static final QuestionField ASKPROFILEID = new QuestionField("ask_profile_id", ObjectFieldDBType.STRING);
    public static final QuestionField TYPE = new QuestionField("type", ObjectFieldDBType.INT);
    public static final QuestionField TITLE = new QuestionField("title", ObjectFieldDBType.STRING);
    public static final QuestionField BODY = new QuestionField("body", ObjectFieldDBType.STRING);
    public static final QuestionField CREATETIME = new QuestionField("create_time", ObjectFieldDBType.TIMESTAMP);
    public static final QuestionField QUESTIONPOINT = new QuestionField("question_point", ObjectFieldDBType.INT);
    public static final QuestionField TIMELIMIT = new QuestionField("time_limit", ObjectFieldDBType.LONG);
    public static final QuestionField ACCEPTANSWERID = new QuestionField("accept_answer_id", ObjectFieldDBType.LONG);
    public static final QuestionField FIRSTANSWERID = new QuestionField("first_answer_id", ObjectFieldDBType.LONG);
    public static final QuestionField INVITEPROFILEID = new QuestionField("invite_profile_id", ObjectFieldDBType.STRING);
    public static final QuestionField REMOVESTATUS = new QuestionField("remove_status", ObjectFieldDBType.INT);
    public static final QuestionField RICHTEXT = new QuestionField("richtext", ObjectFieldDBType.STRING);
    public static final QuestionField ASKVOICE = new QuestionField("askvoice", ObjectFieldDBType.STRING);
    public static final QuestionField QUESTIONSTATUS = new QuestionField("question_status", ObjectFieldDBType.INT);
    public static final QuestionField REACTIVATED = new QuestionField("reactivated", ObjectFieldDBType.LONG);


    public QuestionField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}