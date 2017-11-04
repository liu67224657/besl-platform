package com.enjoyf.webapps.joyme.dto.joymeapp.wikiapp;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhitaoshi on 2015/4/8.
 */
public class WikiAppIndexModuleDTO implements Serializable{
    public String title;
    public String jt = "";
    public String ji = "";
    public String type;
    public List<WikiAppIndexItemDTO> rows;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getJt() {
        return jt;
    }

    public void setJt(String jt) {
        this.jt = jt;
    }

    public String getJi() {
        return ji;
    }

    public void setJi(String ji) {
        this.ji = ji;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<WikiAppIndexItemDTO> getRows() {
        return rows;
    }

    public void setRows(List<WikiAppIndexItemDTO> rows) {
        this.rows = rows;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
