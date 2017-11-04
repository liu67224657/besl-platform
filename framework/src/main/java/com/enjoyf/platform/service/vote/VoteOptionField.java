package com.enjoyf.platform.service.vote;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-9-21
 * Time: 上午10:23
 * To change this template use File | Settings | File Templates.
 */
public class VoteOptionField extends AbstractObjectField {
    //OPTIONID,DESCRIPTION,OPTIONTYPE,SUBJECTID,OPTIONNUM

    public static final VoteOptionField OPTIONID = new VoteOptionField("OPTIONID", ObjectFieldDBType.LONG, false, true);
    public static final VoteOptionField DESCRIPTION = new VoteOptionField("DESCRIPTION", ObjectFieldDBType.STRING, true, false);
    public static final VoteOptionField OPTIONTYPE = new VoteOptionField("OPTIONTYPE", ObjectFieldDBType.STRING, true, false);
    public static final VoteOptionField SUBJECTID = new VoteOptionField("SUBJECTID", ObjectFieldDBType.STRING, true, false);
    public static final VoteOptionField OPTIONNUM = new VoteOptionField("OPTIONNUM", ObjectFieldDBType.INT, true, false);

    public VoteOptionField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public VoteOptionField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
