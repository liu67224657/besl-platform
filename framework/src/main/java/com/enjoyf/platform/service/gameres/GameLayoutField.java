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
public class GameLayoutField extends AbstractObjectField {
    public static final GameLayoutField RESOURCEID= new GameLayoutField("RESOURCEID", ObjectFieldDBType.LONG, false, true);
    public static final GameLayoutField LAYOUTSETTING= new GameLayoutField("LAYOUTSETTING", ObjectFieldDBType.STRING,true, false);


    //
    public GameLayoutField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public GameLayoutField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
