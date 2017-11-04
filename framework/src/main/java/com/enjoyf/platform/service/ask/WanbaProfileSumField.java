package com.enjoyf.platform.service.ask;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;


public class WanbaProfileSumField extends AbstractObjectField {
    public static final WanbaProfileSumField PROFILEID = new WanbaProfileSumField("profile_id", ObjectFieldDBType.STRING);
    public static final WanbaProfileSumField ANSWERSUM = new WanbaProfileSumField("answer_sum", ObjectFieldDBType.INT);
    public static final WanbaProfileSumField AWARDPOINT = new WanbaProfileSumField("award_point", ObjectFieldDBType.INT);
    public static final WanbaProfileSumField QUESIONFOLLOWSUM = new WanbaProfileSumField("question_follow_sum", ObjectFieldDBType.INT);
    public static final WanbaProfileSumField FAVORITESUM = new WanbaProfileSumField("favorite_sum", ObjectFieldDBType.INT);

    public WanbaProfileSumField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}