package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 14-2-26
 * Time: 下午1:52
 * To change this template use File | Settings | File Templates.
 */
public class ActivityTopMenuParam implements Serializable {
    private String author;
    private String publishDate;

    private String picColor;

    private String gameId;

    public ActivityTopMenuParam() {

    }

    public ActivityTopMenuParam(String author, String publishDate, String picColor, String gameId) {
        this.author = author;
        this.publishDate = publishDate;
        this.picColor = picColor;
        this.gameId = gameId;
    }

    public static String toJson(ActivityTopMenuParam paramTextJson) {
        return JsonBinder.buildNonDefaultBinder().toJson(paramTextJson);
    }

    public static ActivityTopMenuParam fromJson(String jsonString) {
        ActivityTopMenuParam param = new ActivityTopMenuParam();
        try {
            if (!StringUtil.isEmpty(jsonString)) {
                param = JsonBinder.buildNonNullBinder().getMapper().readValue(jsonString, new TypeReference<ActivityTopMenuParam>() {
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return param;
    }

    public String toJson() {
        return JsonBinder.buildNonNullBinder().toJson(this);
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

    public String getPicColor() {
        return picColor;
    }

    public void setPicColor(String picColor) {
        this.picColor = picColor;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

}
