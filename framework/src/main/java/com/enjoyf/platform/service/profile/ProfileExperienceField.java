package com.enjoyf.platform.service.profile;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 11-12-16
 * Time: 上午10:04
 * To change this template use File | Settings | File Templates.
 */
public class ProfileExperienceField extends AbstractObjectField {
    //
    public static final ProfileExperienceField EXPID = new ProfileExperienceField("EXPID", ObjectFieldDBType.LONG, true, true);
    public static final ProfileExperienceField UNO = new ProfileExperienceField("UNO", ObjectFieldDBType.STRING, true, true);
    public static final ProfileExperienceField EXPNAME = new ProfileExperienceField("EXPNAME", ObjectFieldDBType.STRING, true, true);
    public static final ProfileExperienceField EXPTYPE = new ProfileExperienceField("EXPTYPE", ObjectFieldDBType.STRING, true, true);
    public static final ProfileExperienceField EXP_STARTDATE = new ProfileExperienceField("EXP_STARTDATE", ObjectFieldDBType.TIMESTAMP, true, true);
    public static final ProfileExperienceField EXP_ENDDATE = new ProfileExperienceField("EXP_ENDDATE", ObjectFieldDBType.TIMESTAMP, true, true);
    public static final ProfileExperienceField CREATEDATE = new ProfileExperienceField("CREATEDATE", ObjectFieldDBType.TIMESTAMP, true, true);
    public static final ProfileExperienceField UPDATADATE = new ProfileExperienceField("UPDATADATE", ObjectFieldDBType.TIMESTAMP, true, true);
    public static final ProfileExperienceField AUDITSTATUS = new ProfileExperienceField("AUDITSTATUS", ObjectFieldDBType.INT, true, true);
    public static final ProfileExperienceField AUDITDATE = new ProfileExperienceField("AUDITDATE", ObjectFieldDBType.TIMESTAMP, true, true);

    //
    public ProfileExperienceField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public ProfileExperienceField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
