package com.enjoyf.platform.service.misc;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-14
 * Time: 下午5:12
 * To change this template use File | Settings | File Templates.
 */
public class GamePropDb implements Serializable {
    private int id;
    private long key_id;//序列字段
    private String key_name;
    private int type;
    private String string_value;
    private Float num_value;
    private Date date_value;

    private GamePropValueType valueType = GamePropValueType.STRING_VALUE;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getKey_id() {
        return key_id;
    }

    public void setKey_id(long key_id) {
        this.key_id = key_id;
    }

    public String getKey_name() {
        return key_name;
    }

    public void setKey_name(String key_name) {
        this.key_name = key_name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getString_value() {
        return string_value;
    }

    public void setString_value(String string_value) {
        this.string_value = string_value;
    }

    public Float getNum_value() {
        return num_value;
    }

    public void setNum_value(Float num_value) {
        this.num_value = num_value;
    }

    public Date getDate_value() {
        return date_value;
    }

    public void setDate_value(Date date_value) {
        this.date_value = date_value;
    }

    public GamePropValueType getValueType() {
        return valueType;
    }

    public void setValueType(GamePropValueType valueType) {
        this.valueType = valueType;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
