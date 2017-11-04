/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.util.sql;

import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-31 上午11:16
 * Description:
 */
public class AbstractObjectField implements ObjectField {
    //
    private String column;
    private ObjectFieldDBType type;
    private boolean modify = true;
    private boolean uniquene = false;

    //
    public AbstractObjectField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        this.column = column;
        this.type = type;
        this.modify = modify;
        this.uniquene = uniquene;
    }

    //
    public AbstractObjectField(String column, ObjectFieldDBType type) {
        this.column = column;
        this.type = type;
    }

    @Override
    public String getColumn() {
        return column;
    }

    @Override
    public ObjectFieldDBType getType() {
        return type;
    }

    @Override
    public boolean isModify() {
        return modify;
    }

    @Override
    public boolean isUniquene() {
        return uniquene;
    }

    //
    @Override
    public int hashCode() {
        return column.hashCode();
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof AbstractObjectField)) {
            return false;
        }

        return column.equalsIgnoreCase(((AbstractObjectField) obj).getColumn());
    }
}
