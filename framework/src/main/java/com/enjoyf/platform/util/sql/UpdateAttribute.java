package com.enjoyf.platform.util.sql;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

@SuppressWarnings("serial")
public class UpdateAttribute implements Serializable {
    //
    private ObjectField field;
    private UpdateType updateType = UpdateType.SET;
    private Object value;

    public UpdateAttribute(ObjectField field, UpdateType updateType, Object value) {
        this.field = field;
        this.updateType = updateType;
        this.value = value;
    }

    public ObjectField getField() {
        return field;
    }

    public UpdateType getUpdateType() {
        return updateType;
    }

    public Object getValue() {
        return value;
    }

    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
