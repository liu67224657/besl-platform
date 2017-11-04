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
public class AnimeSpecialItemField extends AbstractObjectField {
    public static final AnimeSpecialItemField SPECIAL_ITEM_ID = new AnimeSpecialItemField("anime_special_item_id", ObjectFieldDBType.LONG, true, false);
    public static final AnimeSpecialItemField SPECIAL_ID = new AnimeSpecialItemField("anime_special_id", ObjectFieldDBType.LONG, true, false);
    public static final AnimeSpecialItemField TV_ID = new AnimeSpecialItemField("anime_tv_id", ObjectFieldDBType.LONG, true, false);
    public static final AnimeSpecialItemField LINK_URL = new AnimeSpecialItemField("link_url", ObjectFieldDBType.STRING, true, false);
    public static final AnimeSpecialItemField TITLE = new AnimeSpecialItemField("title", ObjectFieldDBType.STRING, true, false);
    public static final AnimeSpecialItemField DESC = new AnimeSpecialItemField("anime_desc", ObjectFieldDBType.STRING, true, false);
    public static final AnimeSpecialItemField PIC = new AnimeSpecialItemField("pic", ObjectFieldDBType.STRING, true, false);
    public static final AnimeSpecialItemField DISPLAY_ORDER = new AnimeSpecialItemField("display_order", ObjectFieldDBType.INT, true, false);
    public static final AnimeSpecialItemField UPDATE_TIME = new AnimeSpecialItemField("update_time", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final AnimeSpecialItemField UPDATE_USER = new AnimeSpecialItemField("update_user", ObjectFieldDBType.STRING, true, false);
    public static final AnimeSpecialItemField REMOVE_STATUS = new AnimeSpecialItemField("remove_status", ObjectFieldDBType.STRING, true, false);
    public static final AnimeSpecialItemField REPLY_NUM = new AnimeSpecialItemField("reply_num", ObjectFieldDBType.INT, true, false);

    public AnimeSpecialItemField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public AnimeSpecialItemField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
