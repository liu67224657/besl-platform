package com.enjoyf.webapps.joyme.dto;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: yujiaxiu
 * Date: 12-8-9
 * Time: 下午3:16
 * To change this template use File | Settings | File Templates.
 */
public class MediaDTO implements Serializable {
    private String thumb;

    private String url;

    private String song;

    private String sing;

    public MediaDTO(){

    }

    public MediaDTO(String thumb, String url) {
        this.thumb = thumb;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getSing() {
        return sing;
    }

    public void setSing(String sing) {
        this.sing = sing;
    }

    /** to json */
    public String toJson(){
        return JsonBinder.buildNormalBinder().toJson(this);
    }

    @Override
    public String toString() {
         return ReflectUtil.fieldsToString(this);
    }

}
