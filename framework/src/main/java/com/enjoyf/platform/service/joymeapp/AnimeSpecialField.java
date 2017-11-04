package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-6-20
 * Time: 下午2:18
 * To change this template use File | Settings | File Templates.
 */
public class AnimeSpecialField extends AbstractObjectField {
    public static final AnimeSpecialField SPECIAL_ID = new AnimeSpecialField("special_id", ObjectFieldDBType.LONG, true, false);
    public static final AnimeSpecialField SPECIAL_NAME = new AnimeSpecialField("special_name", ObjectFieldDBType.STRING, true, false);
    public static final AnimeSpecialField SPECIAL_DESC = new AnimeSpecialField("special_desc", ObjectFieldDBType.STRING, true, false);
    public static final AnimeSpecialField PLATFORM = new AnimeSpecialField("platform", ObjectFieldDBType.INT, true, false);
    public static final AnimeSpecialField SPECIAL_TYPE = new AnimeSpecialField("special_type", ObjectFieldDBType.INT, true, false);
    public static final AnimeSpecialField SPECIAL_ATTR = new AnimeSpecialField("anime_redirect", ObjectFieldDBType.INT, true, false);
    public static final AnimeSpecialField BG_COLOR = new AnimeSpecialField("special_type_bgcolor", ObjectFieldDBType.STRING, true, false);
    public static final AnimeSpecialField REMOVE_STATUS = new AnimeSpecialField("remove_status", ObjectFieldDBType.STRING, true, false);
    public static final AnimeSpecialField APPKEY = new AnimeSpecialField("appkey", ObjectFieldDBType.STRING, true, false);
    public static final AnimeSpecialField COVER_PIC = new AnimeSpecialField("cover_pic", ObjectFieldDBType.STRING, true, false);
    public static final AnimeSpecialField SPECIAL_PIC = new AnimeSpecialField("special_pic", ObjectFieldDBType.STRING, true, false);
    public static final AnimeSpecialField DISPLAY_ORDER = new AnimeSpecialField("display_order", ObjectFieldDBType.INT, true, false);
    public static final AnimeSpecialField LINKURL = new AnimeSpecialField("link_url", ObjectFieldDBType.STRING, true, false);
    public static final AnimeSpecialField SPECIAL_TTILE = new AnimeSpecialField("special_title", ObjectFieldDBType.STRING, true, false);

    public static final AnimeSpecialField CREATE_DATE = new AnimeSpecialField("create_date", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final AnimeSpecialField UPDATE_DATE = new AnimeSpecialField("update_date", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final AnimeSpecialField CREATE_USER = new AnimeSpecialField("create_user", ObjectFieldDBType.TIMESTAMP, true, false);

    public static final AnimeSpecialField READ_NUM = new AnimeSpecialField("read_num", ObjectFieldDBType.INT, true, false);
    public static final AnimeSpecialField DISPLAY_TYPE = new AnimeSpecialField("display_type", ObjectFieldDBType.INT, true, false);

    public AnimeSpecialField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public AnimeSpecialField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
