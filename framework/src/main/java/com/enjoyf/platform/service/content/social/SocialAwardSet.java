package com.enjoyf.platform.service.content.social;

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
 * Date: 14-5-13
 * Time: 下午12:33
 * To change this template use File | Settings | File Templates.
 */
public class SocialAwardSet implements Serializable {

    private Set<SocialAward> awardSet = new HashSet<SocialAward>();

    public SocialAwardSet() {
    }

    public SocialAwardSet(Set<SocialAward> awardSet) {
        this.awardSet = awardSet;
    }

    public SocialAwardSet(Collection awards) {
        this.awardSet.addAll(awards);
    }

    public SocialAwardSet(String jsonStr) {
        if (!StringUtil.isEmpty(jsonStr)) {
            try {
                awardSet = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<LinkedHashSet<SocialAward>>() {
                });
            } catch (IOException e) {
                GAlerter.lab("SocialAwardSet constructor error, jsonStr:" + jsonStr, e);
            }
        }
    }

    public void add(SocialAward award) {
        awardSet.add(award);
    }

    public void add(Set<SocialAward> awards) {
        this.awardSet.addAll(awards);
    }

    public String toJsonStr() {
        return JsonBinder.buildNormalBinder().toJson(awardSet);
    }

    public static SocialAwardSet parse(String jsonStr) {
        SocialAwardSet returnValue = new SocialAwardSet();
        if (!StringUtil.isEmpty(jsonStr)) {
            try {
                Set<SocialAward> awards = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<LinkedHashSet<SocialAward>>() {
                });

                returnValue.add(awards);
            } catch (IOException e) {
                GAlerter.lab("SocialAwardSet parse error, jsonStr:" + jsonStr, e);
            }
        }
        return returnValue;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public int hashCode(){
        return awardSet.hashCode();
    }

    public Set<SocialAward> getAwardSet() {
        return awardSet;
    }

    public void setAwardSet(Set<SocialAward> awardSet) {
        this.awardSet = awardSet;
    }
}
