package com.enjoyf.platform.service.gameres;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-11-20
 * Time: 下午2:23
 * To change this template use File | Settings | File Templates.
 */
public class GamePropertyInfo implements Serializable {

    private long primaryKeyId;

    private long keyId;
    private GamePropertyKeyNameCode keyNameCode;
    private String keyName;
    private Integer intValue;
    private Long longValue;
    private String stringValue;
    private Date dateValue;
    private Boolean booleanValue;
    private GamePropertyValueType valueType;
    private Date createDate;
    private String createId;
    private String createIp;
    private Date lastModifyDate;
    private String lastModifyId;
    private String lastModifyIp;

    public long getPrimaryKeyId() {
        return primaryKeyId;
    }

    public void setPrimaryKeyId(long primaryKeyId) {
        this.primaryKeyId = primaryKeyId;
    }

    public long getKeyId() {
        return keyId;
    }

    public void setKeyId(long keyId) {
        this.keyId = keyId;
    }

    public GamePropertyKeyNameCode getKeyNameCode() {
        return keyNameCode;
    }

    public void setKeyNameCode(GamePropertyKeyNameCode keyNameCode) {
        this.keyNameCode = keyNameCode;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public Integer getIntValue() {
        return intValue;
    }

    public void setIntValue(Integer intValue) {
        this.intValue = intValue;
    }

    public Long getLongValue() {
        return longValue;
    }

    public void setLongValue(Long longValue) {
        this.longValue = longValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public Date getDateValue() {
        return dateValue;
    }

    public void setDateValue(Date dateValue) {
        this.dateValue = dateValue;
    }

    public Boolean getBooleanValue() {
        return booleanValue;
    }

    public void setBooleanValue(Boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    public GamePropertyValueType getGamePropertyValueType() {
        return valueType;
    }

    public void setGamePropertyValueType(GamePropertyValueType gamePropertyValueType) {
        this.valueType = gamePropertyValueType;
    }

    public GamePropertyValueType getValueType() {
        return valueType;
    }

    public void setValueType(GamePropertyValueType valueType) {
        this.valueType = valueType;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public String getCreateIp() {
        return createIp;
    }

    public void setCreateIp(String createIp) {
        this.createIp = createIp;
    }

    public Date getLastModifyDate() {
        return lastModifyDate;
    }

    public void setLastModifyDate(Date lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }

    public String getLastModifyId() {
        return lastModifyId;
    }

    public void setLastModifyId(String lastModifyId) {
        this.lastModifyId = lastModifyId;
    }

    public String getLastModifyIp() {
        return lastModifyIp;
    }

    public void setLastModifyIp(String lastModifyIp) {
        this.lastModifyIp = lastModifyIp;
    }

    public Object getValue() {
        if (intValue != null) {
            return intValue;
        }
        if (stringValue != null) {
            return stringValue;
        }
        if (dateValue != null) {
            return dateValue;
        }
        if (booleanValue != null) {
            return booleanValue;
        }
        return null;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

}
