package com.enjoyf.platform.service.vote;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-9-21
 * Time: 下午3:18
 * To change this template use File | Settings | File Templates.
 */
public class VoteUserRecordField extends AbstractObjectField {
    // USERRECORDID, SUBJECTID, VOTEUNO, VOTEDOMAIN, VOTERECORD, VOTEDATE, VOTEIP

    public static final VoteUserRecordField USERRECORDID = new VoteUserRecordField("USERRECORDID", ObjectFieldDBType.LONG, false, true);
    public static final VoteUserRecordField SUBJECTID = new VoteUserRecordField("SUBJECTID", ObjectFieldDBType.STRING, true, false);
    public static final VoteUserRecordField VOTEUNO = new VoteUserRecordField("VOTEUNO", ObjectFieldDBType.STRING, true, false);
    public static final VoteUserRecordField VOTEDOMAIN = new VoteUserRecordField("VOTEDOMAIN", ObjectFieldDBType.STRING, true, false);
    public static final VoteUserRecordField VOTEDATE = new VoteUserRecordField("VOTEDATE", ObjectFieldDBType.TIMESTAMP, true, false);

    public VoteUserRecordField(String column, ObjectFieldDBType type) {
        super(column, type);
    }

    public VoteUserRecordField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }
}
