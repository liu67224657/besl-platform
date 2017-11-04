package com.enjoyf.platform.util.codegenerator;

import java.lang.reflect.Field;

/**
 * Created by ericliu on 16/3/7.
 */
public  class FieldEntry {
    Field field;
    String setFiledName;
    String sqlFiledName;
    String sqlFiledType;


    public FieldEntry(Field field, String setFiledName, String sqlFiledName, String sqlFiledType) {
        this.field = field;
        this.setFiledName = setFiledName;
        this.sqlFiledName = sqlFiledName;
        this.sqlFiledType = sqlFiledType;
    }

    public String getSetFiledName() {
        return setFiledName;
    }

    public void setSetFiledName(String setFiledName) {
        this.setFiledName = setFiledName;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public String getSqlFiledName() {
        return sqlFiledName;
    }

    public void setSqlFiledName(String sqlFiledName) {
        this.sqlFiledName = sqlFiledName;
    }

    public String getSqlFiledType() {
        return sqlFiledType;
    }

    public void setSqlFiledType(String sqlFiledType) {
        this.sqlFiledType = sqlFiledType;
    }
}