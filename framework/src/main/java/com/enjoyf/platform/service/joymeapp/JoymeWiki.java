package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.log.GAlerter;
import com.google.common.base.Strings;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by zhitaoshi on 2015/4/13.
 */
public class JoymeWiki implements Serializable {
    private int wikiId;
    private String wikiKey;
    private String wikiName;
    private String wikiDomain;
    private String wikiPath;
    private JoymeWikiContextPath contextPath;
    private int supportSubDomain;
    private JoymeWikiParam param;

    public int getWikiId() {
        return wikiId;
    }

    public void setWikiId(int wikiId) {
        this.wikiId = wikiId;
    }

    public String getWikiKey() {
        return wikiKey;
    }

    public void setWikiKey(String wikiKey) {
        this.wikiKey = wikiKey;
    }

    public String getWikiName() {
        return wikiName;
    }

    public void setWikiName(String wikiName) {
        this.wikiName = wikiName;
    }

    public String getWikiDomain() {
        return wikiDomain;
    }

    public void setWikiDomain(String wikiDomain) {
        this.wikiDomain = wikiDomain;
    }

    public String getWikiPath() {
        return wikiPath;
    }

    public void setWikiPath(String wikiPath) {
        this.wikiPath = wikiPath;
    }

    public JoymeWikiContextPath getContextPath() {
        return contextPath;
    }

    public void setContextPath(JoymeWikiContextPath contextPath) {
        this.contextPath = contextPath;
    }

    public int getSupportSubDomain() {
        return supportSubDomain;
    }

    public void setSupportSubDomain(int supportSubDomain) {
        this.supportSubDomain = supportSubDomain;
    }

    public JoymeWikiParam getParam() {
        return param;
    }

    public void setParam(JoymeWikiParam param) {
        this.param = param;
    }

    public String toJsonStr() {
        return JsonBinder.buildNormalBinder().toJson(this);
    }

    public static JoymeWiki parse(String jsonStr) {
        JoymeWiki joymeWiki = null;
        if (!Strings.isNullOrEmpty(jsonStr)) {
            try {
                joymeWiki = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<JoymeWiki>() {
                });
            } catch (IOException e) {
                GAlerter.lab("JoymeWiki parse error, jsonStr:" + jsonStr, e);
            }
        }
        return joymeWiki;
    }
}
