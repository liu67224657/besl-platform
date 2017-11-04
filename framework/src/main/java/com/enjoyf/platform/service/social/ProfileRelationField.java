package com.enjoyf.platform.service.social;

import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-17
 * Time: 下午2:21
 * To change this template use File | Settings | File Templates.
 */
public class ProfileRelationField extends AbstractObjectField {

    //profile_relation_id,src_profileid,dest_profileid,src_status,dest_status,modifytime,modifyip
    public static final ProfileRelationField PROFILE_RELATION_ID = new ProfileRelationField("profile_relation_id", ObjectFieldDBType.STRING, false, true);
    public static final ProfileRelationField SRC_PROFILEID = new ProfileRelationField("src_profileid", ObjectFieldDBType.STRING, true, true);
    public static final ProfileRelationField DEST_PROFILEID = new ProfileRelationField("dest_profileid", ObjectFieldDBType.STRING, true, true);
    public static final ProfileRelationField SRC_STATUS = new ProfileRelationField("src_status", ObjectFieldDBType.INT, true, true);
    public static final ProfileRelationField DEST_STATUS = new ProfileRelationField("dest_status", ObjectFieldDBType.INT, true, true);
    public static final ProfileRelationField MODIFYTIME = new ProfileRelationField("modifytime", ObjectFieldDBType.TIMESTAMP, true, true);
    public static final ProfileRelationField MODIFYIP = new ProfileRelationField("modifyip", ObjectFieldDBType.STRING, true, true);
    public static final ProfileRelationField RELATIONTYPE = new ProfileRelationField("relationtype", ObjectFieldDBType.INT, true, true);
    public static final ProfileRelationField PROFILEKEY = new ProfileRelationField("profilekey", ObjectFieldDBType.STRING, true, true);


    public ProfileRelationField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public ProfileRelationField(String column, ObjectFieldDBType type) {
        super(column, type);
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
