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
public class CommentVideoField extends AbstractObjectField {
    public static final CommentVideoField COMMENT_VIDEO_ID = new CommentVideoField("comment_video_id", ObjectFieldDBType.LONG, true, true);
    public static final CommentVideoField VIDEO_TITLE = new CommentVideoField("video_title", ObjectFieldDBType.STRING, true, false);
    public static final CommentVideoField VIDEO_DESC = new CommentVideoField("video_desc", ObjectFieldDBType.STRING, true, false);
    public static final CommentVideoField PROFILEID = new CommentVideoField("profileid", ObjectFieldDBType.STRING, true, false);
    public static final CommentVideoField APPKEY = new CommentVideoField("appkey", ObjectFieldDBType.STRING, true, false);
    public static final CommentVideoField SDK_KEY = new CommentVideoField("sdk_key", ObjectFieldDBType.STRING, true, false);
    public static final CommentVideoField JSON_URL = new CommentVideoField("json_url", ObjectFieldDBType.STRING, true, false);
    public static final CommentVideoField COMMENT_VIDEO_TYPE = new CommentVideoField("comment_video_type", ObjectFieldDBType.INT, true, false);
    public static final CommentVideoField ACT_STATUS = new CommentVideoField("act_status", ObjectFieldDBType.STRING, true, false);
    public static final CommentVideoField CREATE_TIME = new CommentVideoField("create_time", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final CommentVideoField CREATE_IP = new CommentVideoField("create_ip", ObjectFieldDBType.STRING, true, false);
    public static final CommentVideoField VIDEO_PIC = new CommentVideoField("video_pic", ObjectFieldDBType.STRING, true, false);

    public CommentVideoField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public CommentVideoField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
