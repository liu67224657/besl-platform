package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created with IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-8-14
 * Time: 上午11:59
 * To change this template use File | Settings | File Templates.
 */
public class JoymeAppTopNewsField extends AbstractObjectField {

	public static final JoymeAppTopNewsField TOP_NEWS_ID = new JoymeAppTopNewsField("top_news_id", ObjectFieldDBType.LONG, true, true);
	public static final JoymeAppTopNewsField APPKEY = new JoymeAppTopNewsField("appkey", ObjectFieldDBType.STRING, true, true);
	public static final JoymeAppTopNewsField TITLE = new JoymeAppTopNewsField("title", ObjectFieldDBType.STRING, true, true);
	public static final JoymeAppTopNewsField URL = new JoymeAppTopNewsField("url", ObjectFieldDBType.STRING, true, true);
	public static final JoymeAppTopNewsField CREATE_USERID = new JoymeAppTopNewsField("create_userid", ObjectFieldDBType.STRING, true, true);
	public static final JoymeAppTopNewsField CREATEDATE = new JoymeAppTopNewsField("createdate", ObjectFieldDBType.TIMESTAMP, true, true);
	public static final JoymeAppTopNewsField MODIFYDATE = new JoymeAppTopNewsField("modifydate", ObjectFieldDBType.TIMESTAMP, true, true);
	public static final JoymeAppTopMenuField REMOVESTATUS = new JoymeAppTopMenuField("removestatus", ObjectFieldDBType.STRING, true, false);

	//
	public JoymeAppTopNewsField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
		super(column, type, modify, uniquene);
	}

	public JoymeAppTopNewsField(String column, ObjectFieldDBType type) {
		super(column, type);
	}
}
