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
public class AnimeIndexField extends AbstractObjectField {
    public static final AnimeIndexField ID = new AnimeIndexField("_id", ObjectFieldDBType.LONG, true, false);
    public static final AnimeIndexField APPKEY = new AnimeIndexField("appkey", ObjectFieldDBType.STRING, true, false);
    public static final AnimeIndexField CODE = new AnimeIndexField("code", ObjectFieldDBType.STRING, true, false);
    public static final AnimeIndexField CRETE_DATE = new AnimeIndexField("create_date", ObjectFieldDBType.STRING, true, false);
    public static final AnimeIndexField CREATE_USER = new AnimeIndexField("create_user", ObjectFieldDBType.DATE, true, false);
    public static final AnimeIndexField VALID_STATUS= new AnimeIndexField("valid_status", ObjectFieldDBType.STRING, true, false);

    public static final AnimeIndexField LINE_NAME = new AnimeIndexField("line_name", ObjectFieldDBType.STRING, true, false);
    public static final AnimeIndexField TITLE = new AnimeIndexField("title", ObjectFieldDBType.STRING, true, false);

    public static final AnimeIndexField ANIME_REDIRECT = new AnimeIndexField("anime_redirect", ObjectFieldDBType.INT, true, false);
    public static final AnimeIndexField LINK_URL = new AnimeIndexField("link_url", ObjectFieldDBType.STRING, true, false);
    public static final AnimeIndexField PIC_URL = new AnimeIndexField("pic_url", ObjectFieldDBType.STRING, true, false);
    public static final AnimeIndexField PLATFORM = new AnimeIndexField("platform", ObjectFieldDBType.INT, true, false);
    public static final AnimeIndexField SUPER_SCRIPT = new AnimeIndexField("super_script", ObjectFieldDBType.STRING, true, false);
    public static final AnimeIndexField WIKI_PAGE_NUM = new AnimeIndexField("wiki_page_num", ObjectFieldDBType.STRING, true, false);
    public static final AnimeIndexField DESC = new AnimeIndexField("desc", ObjectFieldDBType.STRING, true, false);

    public AnimeIndexField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public AnimeIndexField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
