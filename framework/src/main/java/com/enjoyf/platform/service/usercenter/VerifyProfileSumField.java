package com.enjoyf.platform.service.usercenter;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;


public class VerifyProfileSumField extends AbstractObjectField {
    public static final VerifyProfileSumField PROFILEID = new VerifyProfileSumField("profile_id", ObjectFieldDBType.STRING);
    public static final VerifyProfileSumField ANSWERSUM = new VerifyProfileSumField("answer_sum", ObjectFieldDBType.INT);
    public static final VerifyProfileSumField AWARDPOINT = new VerifyProfileSumField("award_point", ObjectFieldDBType.INT);
    public static final VerifyProfileSumField QUESIONFOLLOWSUM = new VerifyProfileSumField("question_follow_sum", ObjectFieldDBType.INT);
    public static final VerifyProfileSumField FAVORITESUM = new VerifyProfileSumField("favorite_sum", ObjectFieldDBType.INT);

    public VerifyProfileSumField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}