package com.enjoyf.platform.util.json;

import net.sf.json.JSONObject;

import java.util.Date;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/9/12
 * Description:
 */
public class JoymeJsonObject {

    private JSONObject jsonObject;

    public JoymeJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public String getString(String key) {
        if (!jsonObject.containsKey(key)) {
            return "";
        }
        return jsonObject.getString(key);
    }

    public Double getDouble(String key) {
        if (!jsonObject.containsKey(key)) {
            return null;
        }
        return jsonObject.getDouble(key);
    }


    public Integer getInt(String key) {
        if (!jsonObject.containsKey(key)) {
            return null;
        }
        return jsonObject.getInt(key);
    }

    public Long getLong(String key) {
        if (!jsonObject.containsKey(key)) {
            return null;
        }
        return jsonObject.getLong(key);
    }

    public Date getDateByTimeStamp(String key) {
        if (!jsonObject.containsKey(key)) {
            return null;
        }
        return new Date(jsonObject.getLong(key));
    }
}
