package com.enjoyf.platform.service.gameres;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-8-21
 * Time: 下午5:22
 * To change this template use File | Settings | File Templates.
 */
public class NewReleaseTagRelationField extends AbstractObjectField {
    public static final NewReleaseTagRelationField TAG_RELATION_ID = new NewReleaseTagRelationField("new_tag_relation_id", ObjectFieldDBType.LONG, true, true);
    public static final NewReleaseTagRelationField NEW_RELEASE_ID = new NewReleaseTagRelationField("new_game_info_id", ObjectFieldDBType.LONG, true, false);
    public static final NewReleaseTagRelationField NEW_RELEASE_TAG_ID = new NewReleaseTagRelationField("new_game_tag_id", ObjectFieldDBType.LONG, true, false);
    public static final NewReleaseTagRelationField STATUS = new NewReleaseTagRelationField("status", ObjectFieldDBType.STRING, true, false);

    public NewReleaseTagRelationField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public NewReleaseTagRelationField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
