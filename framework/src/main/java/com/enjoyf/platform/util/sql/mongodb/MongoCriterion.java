/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.util.sql.mongodb;

import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.enjoyf.platform.util.sql.ObjectField;

import java.io.Serializable;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-23 下午2:19
 * Description:
 */
public class MongoCriterion implements Serializable {
     //
    private ObjectField field;

    //
    private MongoOperators operators = MongoOperators.EQ;

    //
    private Object value;

    public MongoCriterion(ObjectField field, MongoOperators operators, Object value) {
        this.field = field;
        this.operators = operators;
        this.value = value;
    }


    public ObjectField getField() {
        return field;
    }

    public void setField(ObjectField field) {
        this.field = field;
    }


    public MongoOperators getOperators() {
        return operators;
    }

    public void setOperators(MongoOperators operators) {
        this.operators = operators;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
