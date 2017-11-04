package com.enjoyf.platform.service.misc;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-16
 * Time: 下午4:37
 * To change this template use File | Settings | File Templates.
 */
public class GamePropDbQueryParam implements Serializable {
    private String key;
    private String stringValue;
    private Float numValue;
    private Date dateValue;
    private GamePropValueType gamePropValueType;

    public GamePropDbQueryParam() {
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public Float getNumValue() {
        return numValue;
    }

    public void setNumValue(Float numValue) {
        this.numValue = numValue;
    }

    public Date getDateValue() {
        return dateValue;
    }

    public void setDateValue(Date dateValue) {
        this.dateValue = dateValue;
    }

    public GamePropValueType getGamePropValueType() {
        return gamePropValueType;
    }

    public void setGamePropValueType(GamePropValueType gamePropValueType) {
        this.gamePropValueType = gamePropValueType;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
