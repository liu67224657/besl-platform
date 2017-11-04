package com.enjoyf.platform.service.usercenter;

import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-17
 * Time: 下午2:21
 * To change this template use File | Settings | File Templates.
 */
public class ProfileSumField extends AbstractObjectField {

    //profile_relation_id,src_profileid,dest_profileid,src_status,dest_status,modifytime,modifyip
    public static final ProfileSumField PROFILEID = new ProfileSumField("profileid", ObjectFieldDBType.STRING, true, true);
    public static final ProfileSumField FOLLOWSUM = new ProfileSumField("followsum", ObjectFieldDBType.INT, true, false);
    public static final ProfileSumField FANSSUM = new ProfileSumField("fanssum", ObjectFieldDBType.INT, true, false);

    public ProfileSumField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public ProfileSumField(String column, ObjectFieldDBType type) {
        super(column, type);
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
