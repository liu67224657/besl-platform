package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-6-1
 * Time: 下午6:51
 * To change this template use File | Settings | File Templates.
 */
public class JoymeAppTopMenuTagField extends AbstractObjectField {

    //tagname, removestatus, topmeunid, createid, createip, createdate
    public static final JoymeAppTopMenuTagField TAGID = new JoymeAppTopMenuTagField("tagid", ObjectFieldDBType.LONG, true, true);
    public static final JoymeAppTopMenuTagField TAGNAME = new JoymeAppTopMenuTagField("tagname", ObjectFieldDBType.STRING, true, false);
    public static final JoymeAppTopMenuTagField REMOVESTATUS = new JoymeAppTopMenuTagField("removestatus", ObjectFieldDBType.STRING, true, true);
    public static final JoymeAppTopMenuTagField TOPMEUNID = new JoymeAppTopMenuTagField("topmenuid", ObjectFieldDBType.LONG, true, false);
    public static final JoymeAppTopMenuTagField CREATEID = new JoymeAppTopMenuTagField("createid", ObjectFieldDBType.STRING, true, true);
    public static final JoymeAppTopMenuTagField CREATEIP = new JoymeAppTopMenuTagField("createip", ObjectFieldDBType.STRING, true, false);
    public static final JoymeAppTopMenuTagField CREATEDATE = new JoymeAppTopMenuTagField("createdate", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final JoymeAppTopMenuTagField DISPLAYORDER = new JoymeAppTopMenuTagField("displayorder", ObjectFieldDBType.INT, true, false);


    //
    public JoymeAppTopMenuTagField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public JoymeAppTopMenuTagField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
