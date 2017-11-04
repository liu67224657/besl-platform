package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-7-4
 * Time: 下午12:50
 * To change this template use File | Settings | File Templates.
 */
public class MobileGameArticleField extends AbstractObjectField {

    public static final MobileGameArticleField ID = new MobileGameArticleField("id", ObjectFieldDBType.LONG, true, true);
    public static final MobileGameArticleField TITLE = new MobileGameArticleField("article_title", ObjectFieldDBType.STRING, true, false);
    public static final MobileGameArticleField DESC = new MobileGameArticleField("article_desc", ObjectFieldDBType.STRING, true, false);

    public static final MobileGameArticleField ARTICLE_URL = new MobileGameArticleField("article_url", ObjectFieldDBType.STRING, true, false);
    public static final MobileGameArticleField  AUTHOR_NAME= new MobileGameArticleField("author_name", ObjectFieldDBType.STRING, true, false);

    public static final MobileGameArticleField CREATE_TIME = new MobileGameArticleField("create_time", ObjectFieldDBType.TIMESTAMP, true, false);

    public static final MobileGameArticleField VALID_STATUS = new MobileGameArticleField("valid_status", ObjectFieldDBType.STRING, true, false);
    public static final MobileGameArticleField DISPLAY_ORDER = new MobileGameArticleField("display_order", ObjectFieldDBType.INT, true, false);
    public static final MobileGameArticleField GAME_DB_ID = new MobileGameArticleField("game_db_id", ObjectFieldDBType.LONG, true, false);

    //
    public MobileGameArticleField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public MobileGameArticleField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
