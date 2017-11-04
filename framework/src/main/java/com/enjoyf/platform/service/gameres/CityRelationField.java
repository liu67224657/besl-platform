package com.enjoyf.platform.service.gameres;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-8-21
 * Time: 下午5:01
 * To change this template use File | Settings | File Templates.
 */
public class CityRelationField extends AbstractObjectField{
    public static final CityRelationField CITY_RELATION_ID = new CityRelationField("city_relation_id", ObjectFieldDBType.LONG, true, true);
    public static final CityRelationField CITY_ID = new CityRelationField("city_id", ObjectFieldDBType.LONG, true, false);
    public static final CityRelationField NEW_GAME_INFO_ID = new CityRelationField("new_game_info_id", ObjectFieldDBType.LONG, true, false);

    public CityRelationField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public CityRelationField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
