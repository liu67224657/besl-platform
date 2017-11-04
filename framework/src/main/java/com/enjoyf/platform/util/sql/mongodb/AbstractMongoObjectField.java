/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.util.sql.mongodb;

import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-31 上午11:16
 * Description:
 */
public class AbstractMongoObjectField implements ObjectField {
    //
    private String column;
    private ObjectFieldDBType type;
    //
    public AbstractMongoObjectField(String column, ObjectFieldDBType type) {
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
        return false;
    }

    @Override
    public boolean isUniquene() {
        return false;
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
        if (obj == null || !(obj instanceof AbstractMongoObjectField)) {
            return false;
        }

        return column.equalsIgnoreCase(((AbstractMongoObjectField) obj).getColumn());
    }
}
