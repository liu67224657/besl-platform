/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.util.sql;


import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-23 下午3:00
 * Description:
 */
public class UpdateExpress implements Serializable {
    //
    private List<UpdateAttribute> updateAttributes = new ArrayList<UpdateAttribute>();

    //
    public UpdateExpress() {
        //
    }

    public UpdateExpress set(ObjectField field, Object value) {
        updateAttributes.add(new UpdateAttribute(field, UpdateType.SET, value));

        return this;
    }

    public UpdateExpress increase(ObjectField field, int delta) {
        updateAttributes.add(new UpdateAttribute(field, UpdateType.INCREASE, delta));

        return this;
    }

    public UpdateExpress increase(ObjectField field, long delta) {
        updateAttributes.add(new UpdateAttribute(field, UpdateType.INCREASE, delta));

        return this;
    }

    public UpdateExpress increase(ObjectField field, double delta) {
        updateAttributes.add(new UpdateAttribute(field, UpdateType.INCREASE, delta));

        return this;
    }

    public UpdateExpress increase(ObjectField field, float delta) {
        updateAttributes.add(new UpdateAttribute(field, UpdateType.INCREASE, delta));

        return this;
    }

    public List<UpdateAttribute> getUpdateAttributes() {
        return updateAttributes;
    }

    public Object getUpdateValueByField(ObjectField field) {
        Object returnObj = null;

        for (UpdateAttribute updateAttribute : updateAttributes) {
            if (updateAttribute.getField().getColumn().equals(field.getColumn())) {
                returnObj = updateAttribute.getValue();
                break;
            }
        }
        return returnObj;
    }

    public boolean containField(ObjectField field) {
        boolean returnObj = false;

        for (UpdateAttribute updateAttribute : updateAttributes) {
            if (updateAttribute.getField().getColumn().equals(field.getColumn())) {
                returnObj = true;
                break;
            }
        }
        return returnObj;
    }

    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
