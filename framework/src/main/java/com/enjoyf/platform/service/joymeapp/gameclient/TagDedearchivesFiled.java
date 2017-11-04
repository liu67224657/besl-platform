package com.enjoyf.platform.service.joymeapp.gameclient;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by zhimingli
 * Date: 2014/12/31
 * Time: 17:02
 */
public class TagDedearchivesFiled extends AbstractObjectField {

    public static final TagDedearchivesFiled ID = new TagDedearchivesFiled("id", ObjectFieldDBType.STRING, true, false);
    public static final TagDedearchivesFiled TAGID = new TagDedearchivesFiled("tagid", ObjectFieldDBType.LONG, true, false);
    public static final TagDedearchivesFiled DEDE_ARCHIVES_ID = new TagDedearchivesFiled("dede_archives_id", ObjectFieldDBType.STRING, true, false);
    public static final TagDedearchivesFiled DEDE_ARCHIVES_TITLE = new TagDedearchivesFiled("dede_archives_title", ObjectFieldDBType.STRING, true, false);
    public static final TagDedearchivesFiled DEDE_ARCHIVES_DESCRIPTION = new TagDedearchivesFiled("dede_archives_description", ObjectFieldDBType.STRING, true, false);
    public static final TagDedearchivesFiled DEDE_ARCHIVES_LITPIC = new TagDedearchivesFiled("dede_archives_litpic", ObjectFieldDBType.STRING, true, false);
    public static final TagDedearchivesFiled DEDE_ARCHIVES_PUBDATE = new TagDedearchivesFiled("dede_archives_pubdate", ObjectFieldDBType.LONG, true, false);

    public static final TagDedearchivesFiled DEDE_ARCHIVES_SHOWIOS = new TagDedearchivesFiled("dede_archives_showios", ObjectFieldDBType.INT, true, false);
    public static final TagDedearchivesFiled DEDE_ARCHIVES_SHOWANDROID = new TagDedearchivesFiled("dede_archives_showandroid", ObjectFieldDBType.INT, true, false);
    public static final TagDedearchivesFiled DEDE_ARCHIVES_HTLISTIMG = new TagDedearchivesFiled("dede_archives_htlistimg", ObjectFieldDBType.STRING, true, false);
    public static final TagDedearchivesFiled DEDE_ARCHIVES_URL = new TagDedearchivesFiled("dede_archives_url", ObjectFieldDBType.STRING, true, false);

    public static final TagDedearchivesFiled DEDE_REDIRECT_TYPE = new TagDedearchivesFiled("dede_redirect_type", ObjectFieldDBType.INT, true, false);
    public static final TagDedearchivesFiled DEDE_REDIRECT_URL = new TagDedearchivesFiled("dede_redirect_url", ObjectFieldDBType.STRING, true, false);


    public static final TagDedearchivesFiled DISPLAY_ORDER = new TagDedearchivesFiled("display_order", ObjectFieldDBType.LONG, true, false);
    public static final TagDedearchivesFiled REMOVE_STATUS = new TagDedearchivesFiled("remove_status", ObjectFieldDBType.STRING, true, false);

    public static final TagDedearchivesFiled TAG_DISPLYTYPE = new TagDedearchivesFiled("display_type", ObjectFieldDBType.INT, true, false);

    public static final TagDedearchivesFiled DISPLAY_TAG = new TagDedearchivesFiled("display_tag", ObjectFieldDBType.LONG, true, false);

    public static final TagDedearchivesFiled ARCHIVE_RELATION_TYPE = new TagDedearchivesFiled("relation_type", ObjectFieldDBType.INT, true, false);
    public static final TagDedearchivesFiled ARCHIVE_CONTENT_TYPE = new TagDedearchivesFiled("content_type", ObjectFieldDBType.INT, true, false);
    public static final TagDedearchivesFiled PROFILE_ID = new TagDedearchivesFiled("profile_id", ObjectFieldDBType.STRING, true, false);

    public TagDedearchivesFiled(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public TagDedearchivesFiled(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}

