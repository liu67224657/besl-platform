package com.enjoyf.webapps.joyme.dto.vote;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-10-22
 * Time: 上午11:23
 * To change this template use File | Settings | File Templates.
 */
public class JSONWikiVote implements Serializable{
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
