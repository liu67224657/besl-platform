package com.enjoyf.platform.service.search;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class SearchGameDbResultEntry implements Serializable{
    private String gtId;
    private String gtName;


    public String getGtId() {
        return gtId;
    }

    public void setGtId(String gtId) {
        this.gtId = gtId;
    }

    public String getGtName() {
        return gtName;
    }

    public void setGtName(String gtName) {
        this.gtName = gtName;
    }


    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
