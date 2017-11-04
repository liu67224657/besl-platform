package com.enjoyf.platform.service.point;

import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 13-6-12
 * Time: 下午1:27
 * To change this template use File | Settings | File Templates.
 */
public class PointProps {
    private String code;
    private int value;
    private int limit;
    private int times;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
