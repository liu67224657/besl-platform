package com.enjoyf.platform.service.misc;

import java.io.Serializable;

/**
 * Created by zhimingli on 2017-3-27 0027.
 */
public class FiledValue implements Serializable {
    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public FiledValue(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
