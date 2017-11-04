/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.gameres;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * @Auther: <a mailto="erickliu@enjoyfound.com">ericliu</a>
 * Create time: 11-8-31 上午11:16
 * Description:
 */
public class GamePropertyField extends AbstractObjectField {
    //
    public static final GamePropertyField RESOURCEID= new GamePropertyField("RESOURCEID", ObjectFieldDBType.LONG, false, true);
    public static final GamePropertyField PROPERTYDOMAIN= new GamePropertyField("PROPERTYDOMAIN", ObjectFieldDBType.STRING,true, true);
    public static final GamePropertyField PROPERTYTYPE= new GamePropertyField("PROPERTYTYPE", ObjectFieldDBType.STRING, true, true);
    public static final GamePropertyField VALUE= new GamePropertyField("VALUE", ObjectFieldDBType.STRING, true, true);
    public static final GamePropertyField VALUE2= new GamePropertyField("EXTSTR01", ObjectFieldDBType.STRING, true, true);
    public static final GamePropertyField VALUE3= new GamePropertyField("EXTSTR02", ObjectFieldDBType.STRING, true, true);


    //
    public GamePropertyField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public GamePropertyField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
