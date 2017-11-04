package com.enjoyf.platform.service.comment;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-11-13
 * Time: 下午12:36
 * To change this template use File | Settings | File Templates.
 */
public class CommentForbidField extends AbstractObjectField {
    public static final CommentForbidField PROFILE_ID = new CommentForbidField("profileid", ObjectFieldDBType.STRING, false, true);
    public static final CommentForbidField START_TIME = new CommentForbidField("start_time", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final CommentForbidField LENGTH = new CommentForbidField("length", ObjectFieldDBType.LONG, true, false);

    public CommentForbidField(String column, ObjectFieldDBType type, boolean modify, boolean unique) {
        super(column, type, modify, unique);
    }

    public CommentForbidField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
