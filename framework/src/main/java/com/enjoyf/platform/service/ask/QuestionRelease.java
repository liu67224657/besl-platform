package com.enjoyf.platform.service.ask;

import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.gson.Gson;

import java.io.Serializable;


public class QuestionRelease implements Serializable {

    private String title;
    private String text;
    private String askpid;
    private String timelimit;
    private String tagid;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAskpid() {
        return askpid;
    }

    public void setAskpid(String askpid) {
        this.askpid = askpid;
    }

    public String getTimelimit() {
        return timelimit;
    }

    public void setTimelimit(String timelimit) {
        this.timelimit = timelimit;
    }

    public String getTagid() {
        return tagid;
    }

    public void setTagid(String tagid) {
        this.tagid = tagid;
    }

    public static QuestionRelease toObject(String jsonStr) {
        return new Gson().fromJson(jsonStr, QuestionRelease.class);
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
