package com.enjoyf.platform.service.content.social;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-5-28
 * Time: 下午7:50
 * To change this template use File | Settings | File Templates.
 */
public class Subscript implements Serializable {

    private int type = SubscriptType.NULL.getCode();
    private String startDate;
    private String endDate;

    public Subscript() {
    }

    public Subscript(int type, String startDate, String endDate) {
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String toJsonStr() {
        return JsonBinder.buildNormalBinder().toJson(this);
    }

    public static Subscript parse(String jsonStr) {
        Subscript returnValue = new Subscript();
        if (!StringUtil.isEmpty(jsonStr)) {
            try {
                returnValue = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<Subscript>() {});
            } catch (IOException e) {
                GAlerter.lab("SocialAwardSet parse error, jsonStr:" + jsonStr, e);
            }
        }
        return returnValue;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
