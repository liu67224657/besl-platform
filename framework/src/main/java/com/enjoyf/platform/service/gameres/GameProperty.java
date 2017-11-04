package com.enjoyf.platform.service.gameres;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-2-18
 * Time: 下午1:50
 * To change this template use File | Settings | File Templates.
 */
public class GameProperty implements Serializable, Comparable<GameProperty> {

    private long resourceId;
    private ResourceDomain resourceDomain;
    private GamePropertyDomain gamePropertyDomain;
    private String propertyType;
    private String value;
    private String value2;
    private String value3;
    private int sortNum;

    public long getResourceId() {
        return resourceId;
    }

    public void setResourceId(long resourceId) {
        this.resourceId = resourceId;
    }

    public GamePropertyDomain getGamePropertyDomain() {
        return gamePropertyDomain;
    }

    public void setGamePropertyDomain(GamePropertyDomain gamePropertyDomain) {
        this.gamePropertyDomain = gamePropertyDomain;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public String getValue3() {
        return value3;
    }

    public void setValue3(String value3) {
        this.value3 = value3;
    }

    public int getSortNum() {
        return sortNum;
    }

    public void setSortNum(int sortNum) {
        this.sortNum = sortNum;
    }

    public ResourceDomain getResourceDomain() {
        return resourceDomain;
    }

    public void setResourceDomain(ResourceDomain resourceDomain) {
        this.resourceDomain = resourceDomain;
    }

    @Override
    public int compareTo(GameProperty o) {
        return this.getSortNum() < o.getSortNum() ? -1 : (this.getSortNum() == o.getSortNum() ? 0 : 1);
    }


    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
