package com.enjoyf.platform.service.profile;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.log.GAlerter;
import com.google.common.base.Strings;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-12-20
 * Time: 上午11:20
 * To change this template use File | Settings | File Templates.
 */
public class DeveloperLocation implements Serializable{

    private DeveloperCountry country;

    private DeveloperProvince province;

    private  DeveloperCity city;

    public DeveloperCountry getCountry() {
        return country;
    }

    public void setCountry(DeveloperCountry country) {
        this.country = country;
    }

    public DeveloperProvince getProvince() {
        return province;
    }

    public void setProvince(DeveloperProvince province) {
        this.province = province;
    }

    public DeveloperCity getCity() {
        return city;
    }

    public void setCity(DeveloperCity city) {
        this.city = city;
    }

    public DeveloperLocation() {
    }

    public DeveloperLocation(DeveloperCountry country, DeveloperProvince province, DeveloperCity city) {
        this.country = country;
        this.province = province;
        this.city = city;
    }

    public static String toJson(DeveloperLocation location) {
        return JsonBinder.buildNormalBinder().toJson(location);
    }

    public static DeveloperLocation fromJson(String jsonStr) {
        DeveloperLocation returnValue = new DeveloperLocation();

        if (!Strings.isNullOrEmpty(jsonStr)) {
            try {
                returnValue = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<DeveloperLocation>() {
                });
            } catch (IOException e) {
                GAlerter.lab("ProfileBlogHeadIconSet parse error, jsonStr:" + jsonStr, e);
            }
        }

        return returnValue;
    }
}
