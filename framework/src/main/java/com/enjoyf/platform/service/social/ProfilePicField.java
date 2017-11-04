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
public class ProfilePicField extends AbstractObjectField {

    //picurl,remove_status,createtime,createip
    public static final ProfilePicField PROFILEPIC_ID = new ProfilePicField("profilepic_id", ObjectFieldDBType.LONG, false, true);
    public static final ProfilePicField PICURL = new ProfilePicField("picurl", ObjectFieldDBType.STRING, true, true);
    public static final ProfilePicField REMOVE_STATUS = new ProfilePicField("remove_status", ObjectFieldDBType.INT, true, true);
    public static final ProfilePicField CREATETIME = new ProfilePicField("createtime", ObjectFieldDBType.TIMESTAMP, true, true);
    public static final ProfilePicField CREATEIP = new ProfilePicField("createip", ObjectFieldDBType.STRING, true, true);
    public static final ProfilePicField LIKESUM = new ProfilePicField("likesum", ObjectFieldDBType.INT, true, true);


    public ProfilePicField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public ProfilePicField(String column, ObjectFieldDBType type) {
        super(column, type);
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
