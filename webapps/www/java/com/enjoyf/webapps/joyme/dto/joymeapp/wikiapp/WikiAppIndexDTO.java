package com.enjoyf.webapps.joyme.dto.joymeapp.wikiapp;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhitaoshi on 2015/4/8.
 */
public class WikiAppIndexDTO implements Serializable{

    public List<WikiAppIndexItemDTO> headinfo;
    public WikiAppIndexModuleDTO subscribe;
    public List<WikiAppIndexModuleDTO> rows;

    public List<WikiAppIndexItemDTO> getHeadinfo() {
        return headinfo;
    }

    public void setHeadinfo(List<WikiAppIndexItemDTO> headinfo) {
        this.headinfo = headinfo;
    }

    public WikiAppIndexModuleDTO getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(WikiAppIndexModuleDTO subscribe) {
        this.subscribe = subscribe;
    }

    public List<WikiAppIndexModuleDTO> getRows() {
        return rows;
    }

    public void setRows(List<WikiAppIndexModuleDTO> rows) {
        this.rows = rows;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
