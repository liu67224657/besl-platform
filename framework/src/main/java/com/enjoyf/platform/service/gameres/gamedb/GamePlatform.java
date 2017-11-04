package com.enjoyf.platform.service.gameres.gamedb;

import net.sf.json.JSONObject;

import java.io.Serializable;

/**
 * Created by zhitaoshi on 2016/3/1.
 */
public abstract class GamePlatform implements Serializable{
    private transient int code;
    private transient String desc;
    
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || this.getClass() != o.getClass())
            return false;
        if (code != ((GamePlatform) o).code)
            return false;
        return true;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = JSONObject.fromObject(this);
        return jsonObject.toString();
    }

}
