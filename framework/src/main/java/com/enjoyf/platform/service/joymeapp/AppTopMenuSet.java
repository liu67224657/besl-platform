package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.common.base.Strings;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-9-11
 * Time: 上午9:58
 * To change this template use File | Settings | File Templates.
 */
public class AppTopMenuSet implements Serializable {

    private Set<AppTopMenu> topMenuSet;

    public AppTopMenuSet() {

    }

    public AppTopMenuSet(Set<AppTopMenu> topMenuSet) {
        this.topMenuSet = topMenuSet;
    }

    public AppTopMenuSet(Collection pics) {
        this.topMenuSet.addAll(pics);
    }

    public AppTopMenuSet(String jsonStr) {
        if (!Strings.isNullOrEmpty(jsonStr)) {
            try {
                topMenuSet = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<AppTopMenu>() {
                });
            } catch (IOException e) {
                GAlerter.lab("NewGamePicSet constructor error, jsonStr:" + jsonStr, e);
            }
        }
    }

    public void add(AppTopMenu img) {
        topMenuSet.add(img);
    }

    public void add(Set<AppTopMenu> imgs) {
        topMenuSet.addAll(imgs);
    }

    public String toJsonStr() {
        return JsonBinder.buildNormalBinder().toJson(topMenuSet);
    }

    public static AppTopMenuSet parse(String jsonStr) {
        AppTopMenuSet returnValue = new AppTopMenuSet();

        if (!Strings.isNullOrEmpty(jsonStr)) {
            try {
                Set<AppTopMenu> imageContents = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<LinkedHashSet<AppTopMenu>>() {
                });
                returnValue.add(imageContents);
            } catch (IOException e) {
                GAlerter.lab("NewGamePicSet parse error, jsonStr:" + jsonStr, e);
            }
        }
        return returnValue;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public int hashCode() {
        return topMenuSet.hashCode();
    }

    public Set<AppTopMenu> getTopMenuSet() {
        return topMenuSet;
    }

    public void setTopMenuSet(Set<AppTopMenu> topMenuSet) {
        this.topMenuSet = topMenuSet;
    }
}
