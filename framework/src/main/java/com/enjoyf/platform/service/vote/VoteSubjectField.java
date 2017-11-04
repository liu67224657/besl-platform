package com.enjoyf.platform.service.vote;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-9-21
 * Time: 上午10:22
 * To change this template use File | Settings | File Templates.
 */
public class VoteSubjectField extends AbstractObjectField {
    public static final VoteSubjectField SUBJECTID = new VoteSubjectField("SUBJECTID", ObjectFieldDBType.STRING, true, true);
    public static final VoteSubjectField VOTENUM = new VoteSubjectField("VOTENUM", ObjectFieldDBType.INT, true, false);
    public static final VoteSubjectField VOTEDOMAIN = new VoteSubjectField("VOTEDOMAIN", ObjectFieldDBType.STRING, true, false);

    public VoteSubjectField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public VoteSubjectField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
