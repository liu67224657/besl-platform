/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.account.discuz;

import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-31 上午11:16
 * Description:
 */
public class DiscuzMemberField extends AbstractObjectField {

    public static final DiscuzMemberField ACCOUNTUNO = new DiscuzMemberField("ACCOUNTUNO", ObjectFieldDBType.STRING, true, true);
    public static final DiscuzMemberField UNO = new DiscuzMemberField("UNO", ObjectFieldDBType.STRING, true, true);
    //


    public DiscuzMemberField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public DiscuzMemberField(String column, ObjectFieldDBType type) {
        super(column, type);
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
