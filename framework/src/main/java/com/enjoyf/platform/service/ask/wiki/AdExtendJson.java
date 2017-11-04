package com.enjoyf.platform.service.ask.wiki;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by zhimingli on 2017-3-28 0028.
 */
public class AdExtendJson implements Serializable {
    private Integer index; //广告的位置


    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static AdExtendJson toObject(String jsonStr) {
        return new Gson().fromJson(jsonStr, AdExtendJson.class);
    }

    public String toJson() {
        return new Gson().toJson(this);
    }
}
