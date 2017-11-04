package com.enjoyf.webapps.joyme.dto.joymeapp;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-5-28
 * Time: 下午7:50
 * To change this template use File | Settings | File Templates.
 */
public class AppShareCountSet implements Serializable {

    private Set<AppShareCount> shareCountSet = new HashSet<AppShareCount>();

    public AppShareCountSet() {
    }

    public AppShareCountSet(Set<AppShareCount> shareCounts) {
        this.shareCountSet = shareCounts;
    }

    public AppShareCountSet(Collection arr) {
        this.shareCountSet.addAll(arr);
    }

    public AppShareCountSet(String jsonStr) {
        if (!StringUtil.isEmpty(jsonStr)) {
            try {
                this.shareCountSet = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<LinkedHashSet<AppShareCount>>() {
                });
            } catch (IOException e) {
                GAlerter.lab("SocialAwardSet constructor error, jsonStr:" + jsonStr, e);
            }
        }
    }

    public String toJsonStr() {
        return JsonBinder.buildNormalBinder().toJson(this);
    }

    public static AppShareCountSet parse(String jsonStr) {
        AppShareCountSet returnValue = new AppShareCountSet();
        if (!StringUtil.isEmpty(jsonStr)) {
            try {
                Set<AppShareCount> counts = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<LinkedHashSet<AppShareCount>>() {});
                returnValue.add(counts);
            } catch (IOException e) {
                GAlerter.lab("SocialAwardSet parse error, jsonStr:" + jsonStr, e);
            }
        }
        return returnValue;
    }

    public void add(AppShareCount count) {
        this.shareCountSet.add(count);
    }

    public void add(Set<AppShareCount> counts) {
        this.shareCountSet.addAll(counts);
    }

    public Set<AppShareCount> getShareCountSet() {
        return shareCountSet;
    }

    public void setShareCountSet(Set<AppShareCount> shareCountSet) {
        this.shareCountSet = shareCountSet;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public int hashCode(){
        return shareCountSet.hashCode();
    }
}
