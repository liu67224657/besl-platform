package com.enjoyf.platform.service.vote;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-9-24
 * Time: 下午2:03
 * To change this template use File | Settings | File Templates.
 */
public class VoteSubjectRecordField extends AbstractObjectField {
    //SUBJECTRECORDID, SUBJECTID, VOTEUNO, VOTEDOMAIN, VOTEOPTION, VOTEOPTIONVALUE, VOTEDATE, VOTEIP

    public static final VoteSubjectRecordField SUBJECTRECORDID = new VoteSubjectRecordField("SUBJECTRECORDID", ObjectFieldDBType.LONG, false, true);
    public static final VoteSubjectRecordField SUBJECTID = new VoteSubjectRecordField("SUBJECTID", ObjectFieldDBType.STRING, true, false);
    public static final VoteSubjectRecordField VOTEUNO = new VoteSubjectRecordField("VOTEUNO", ObjectFieldDBType.STRING, true, false);
    public static final VoteSubjectRecordField VOTEDOMAIN = new VoteSubjectRecordField("VOTEDOMAIN", ObjectFieldDBType.STRING, true, false);
    public static final VoteSubjectRecordField VOTEOPTION = new VoteSubjectRecordField("VOTEOPTION", ObjectFieldDBType.LONG, true, false);


    public VoteSubjectRecordField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public VoteSubjectRecordField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
