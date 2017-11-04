package com.enjoyf.platform.service.content;

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
 * User: zhaoxin
 * Date: 11-8-22
 * Time: 下午5:28
 * Desc: 各类应用
 */
public class AppsContentSet implements Serializable {

    private Set<AppsContent> apps = new LinkedHashSet<AppsContent>();

    public AppsContentSet() {
    }

    public AppsContentSet(Set apps) {
        this.apps = apps;
    }

    public AppsContentSet(Collection apps) {
        this.apps.addAll(apps);
    }

    public AppsContentSet(String jsonStr) {
        if (!Strings.isNullOrEmpty(jsonStr)) {
            try {
                apps = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<LinkedHashSet<AppsContent>>() {
                });
            } catch (IOException e) {
                //
                GAlerter.lab("AppsContentSet constructor error, jsonStr:" + jsonStr, e);
            }
        }
    }

    public Set<AppsContent> getApps() {
        return apps;
    }

    public void setApps(Set<AppsContent> apps) {
        this.apps = apps;
    }

    public void add(AppsContent app) {
        apps.add(app);
    }

    public void add(Set<AppsContent> apps) {
        this.apps.addAll(apps);
    }

    public String toJsonStr() {
        return JsonBinder.buildNormalBinder().toJson(apps);
    }

    public static AppsContentSet parse(String jsonStr) {
        AppsContentSet returnValue = new AppsContentSet();

        if (!Strings.isNullOrEmpty(jsonStr)) {
            try {
                Set<AppsContent> appsContents = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<Set<AppsContent>>() {
                });

                returnValue.add(appsContents);
            } catch (IOException e) {
                GAlerter.lab("AppsContentSet parse error, jsonStr:" + jsonStr, e);
            }
        }

        return returnValue;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

}
