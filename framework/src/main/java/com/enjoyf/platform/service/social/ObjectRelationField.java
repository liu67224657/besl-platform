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
public class ObjectRelationField extends AbstractObjectField {

    //
    public static final ObjectRelationField PROFILERELATION_ID = new ObjectRelationField("object_relation_id", ObjectFieldDBType.STRING, false, true);
    public static final ObjectRelationField PROFILEID = new ObjectRelationField("profileid", ObjectFieldDBType.STRING, true, true);
    public static final ObjectRelationField OBJECTID = new ObjectRelationField("objectid", ObjectFieldDBType.STRING, true, true);
    public static final ObjectRelationField OBJECTTYPE = new ObjectRelationField("objecttype", ObjectFieldDBType.INT, true, true);
    public static final ObjectRelationField MODIFYTIME = new ObjectRelationField("modifytime", ObjectFieldDBType.TIMESTAMP, true, true);
    public static final ObjectRelationField MODIFYIP = new ObjectRelationField("modifyip", ObjectFieldDBType.STRING, true, true);
    public static final ObjectRelationField STATUS = new ObjectRelationField("status", ObjectFieldDBType.INT, true, true);
    public static final ObjectRelationField EXTSTRING = new ObjectRelationField("extstring", ObjectFieldDBType.STRING, true, true);
    public static final ObjectRelationField PROFILEKEY = new ObjectRelationField("profilekey", ObjectFieldDBType.STRING, true, true);

    public ObjectRelationField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public ObjectRelationField(String column, ObjectFieldDBType type) {
        super(column, type);
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
