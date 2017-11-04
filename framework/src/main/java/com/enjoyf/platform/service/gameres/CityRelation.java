package com.enjoyf.platform.service.gameres;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-8-21
 * Time: 上午9:58
 * To change this template use File | Settings | File Templates.
 */
public class CityRelation implements Serializable {
    private long cityRelationId;
    private long cityId;
    private long newGameInfoId;

    public long getCityRelationId() {
        return cityRelationId;
    }

    public void setCityRelationId(long cityRelationId) {
        this.cityRelationId = cityRelationId;
    }

    public long getCityId() {
        return cityId;
    }

    public void setCityId(long cityId) {
        this.cityId = cityId;
    }

    public long getNewGameInfoId() {
        return newGameInfoId;
    }

    public void setNewGameInfoId(long newGameInfoId) {
        this.newGameInfoId = newGameInfoId;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

}
