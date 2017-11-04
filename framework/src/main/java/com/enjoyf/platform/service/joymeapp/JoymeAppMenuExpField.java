package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.common.base.Strings;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhitaoshi on 2015/3/23.
 */
public class JoymeAppMenuExpField implements Serializable{

    private String expDesc;
    private String star;
    private String author;
    private String publishDate;

    public String getExpDesc() {
        return expDesc;
    }

    public void setExpDesc(String expDesc) {
        this.expDesc = expDesc;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }


    public String toJsonStr() {
        return JsonBinder.buildNormalBinder().toJson(this);
    }

    public static JoymeAppMenuExpField parse(String jsonStr) {
        JoymeAppMenuExpField appMenuExpField = null;
        if (!Strings.isNullOrEmpty(jsonStr)) {
            try {
                appMenuExpField = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<JoymeAppMenuExpField>() {
                });
            } catch (IOException e) {
                GAlerter.lab("AppConfigInfo parse error, jsonStr:" + jsonStr, e);
            }
        }
        return appMenuExpField;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
