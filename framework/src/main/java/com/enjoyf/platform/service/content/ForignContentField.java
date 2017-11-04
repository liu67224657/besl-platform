package com.enjoyf.platform.service.content;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-1-7
 * Time: 上午11:45
 * To change this template use File | Settings | File Templates.
 */
public class ForignContentField extends AbstractObjectField {

	public static final ForignContentField CONTENT_ID = new ForignContentField("content_id", ObjectFieldDBType.LONG, true, false);
	public static final ForignContentField FORIGN_ID = new ForignContentField("forign_id", ObjectFieldDBType.STRING, true, false);
	public static final ForignContentField CONTENT_URL = new ForignContentField("content_url", ObjectFieldDBType.STRING, true, false);

	public static final ForignContentField CONTENT_DOMAIN = new ForignContentField("content_domain", ObjectFieldDBType.INT, true, false);
	public static final ForignContentField REPLY_NUM = new ForignContentField("reply_num", ObjectFieldDBType.INT, true, false);

	public static final ForignContentField CONTENT_TITLE = new ForignContentField("content_title", ObjectFieldDBType.STRING, true, false);
	public static final ForignContentField CONTENT_DESC = new ForignContentField("content_desc", ObjectFieldDBType.STRING, true, false);

	public static final ForignContentField CREATE_TIME = new ForignContentField("create_time", ObjectFieldDBType.TIMESTAMP, true, false);

	public static final ForignContentField REMOVE_STATUS = new ForignContentField("remove_status", ObjectFieldDBType.STRING, true, false);

	public static final ForignContentField TOTAL_ROWS = new ForignContentField("total_rows", ObjectFieldDBType.INT, true, false);
	public static final ForignContentField KEY_WORDS = new ForignContentField("key_words", ObjectFieldDBType.STRING, true, false);

	public static final ForignContentField LONG_COMMENT_NUM = new ForignContentField("long_comment_num", ObjectFieldDBType.INT, true, false);
	public static final ForignContentField AVERAGE_SCORE = new ForignContentField("average_score", ObjectFieldDBType.DOUBLE, true, false);
	public static final ForignContentField DISPLAY_ORDER = new ForignContentField("display_order", ObjectFieldDBType.LONG, true, false);
	//
	public static final ForignContentField SCOREREPLY_NUM = new ForignContentField("scorereply_num", ObjectFieldDBType.INT, true, false);

	public ForignContentField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
		super(column, type, modify, uniquene);
	}

	public ForignContentField(String column, ObjectFieldDBType type) {
		super(column, type);
	}
}
