package com.enjoyf.platform.service.social;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: <a mailto="ericLiu@stuff.enjoyfound.com">ericliu</a>
 * Create time: 12-9-3
 * Description:
 */
public class RecommendBlack implements Serializable {
    private String uno;
    private Date date;

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    public String toJson() {
        return JsonBinder.buildNormalBinder().toJson(this);
    }

    @Override
    public int hashCode() {
        return uno.hashCode();
    }
}
