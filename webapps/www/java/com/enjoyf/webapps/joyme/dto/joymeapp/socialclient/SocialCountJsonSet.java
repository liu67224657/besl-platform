package com.enjoyf.webapps.joyme.dto.joymeapp.socialclient;

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
public class SocialCountJsonSet implements Serializable {

    private Set<SocialCount> socialCountSet = new HashSet<SocialCount>();

    public SocialCountJsonSet() {
    }

    public SocialCountJsonSet(Set<SocialCount> socialCountSet) {
        this.socialCountSet = socialCountSet;
    }

    public SocialCountJsonSet(Collection arr) {
        this.socialCountSet.addAll(arr);
    }

    public SocialCountJsonSet(String jsonStr) {
        if (!StringUtil.isEmpty(jsonStr)) {
            try {
                this.socialCountSet = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<LinkedHashSet<SocialCount>>() {
                });
            } catch (IOException e) {
                GAlerter.lab("SocialAwardSet constructor error, jsonStr:" + jsonStr, e);
            }
        }
    }

    public String toJsonStr() {
        return JsonBinder.buildNormalBinder().toJson(this);
    }

    public static SocialCountJsonSet parse(String jsonStr) {
        SocialCountJsonSet returnValue = new SocialCountJsonSet();
        if (!StringUtil.isEmpty(jsonStr)) {
            try {
                Set<SocialCount> socialCounts = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<LinkedHashSet<SocialCount>>() {});
                returnValue.add(socialCounts);
            } catch (IOException e) {
                GAlerter.lab("SocialAwardSet parse error, jsonStr:" + jsonStr, e);
            }
        }
        return returnValue;
    }

    public void add(SocialCount socialCount) {
        this.socialCountSet.add(socialCount);
    }

    public void add(Set<SocialCount> socialCounts) {
        this.socialCountSet.addAll(socialCounts);
    }

    public Set<SocialCount> getSocialCountSet() {
        return socialCountSet;
    }

    public void setSocialCountSet(Set<SocialCount> socialCountSet) {
        this.socialCountSet = socialCountSet;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public int hashCode(){
        return socialCountSet.hashCode();
    }
}
