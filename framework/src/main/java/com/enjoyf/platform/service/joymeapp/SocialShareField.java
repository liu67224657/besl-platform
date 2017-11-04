package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.account.AccountDomain;
import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-8-21
 * Time: 下午1:51
 * To change this template use File | Settings | File Templates.
 */
public class SocialShareField extends AbstractObjectField {

	public static final SocialShareField SHARE_ID = new SocialShareField("share_id", ObjectFieldDBType.LONG, true, true);
	public static final SocialShareField ACTIVITYID = new SocialShareField("activityid", ObjectFieldDBType.LONG, true, true);
	public static final SocialShareField SHARE_TYPE = new SocialShareField("share_type", ObjectFieldDBType.INT, true, true);
	public static final SocialShareField APPKEY = new SocialShareField("appkey", ObjectFieldDBType.STRING, true, true);
	public static final SocialShareField PLATFORM = new SocialShareField("platform", ObjectFieldDBType.INT, true, true);
	public static final SocialShareField SHAREDOMAIN = new SocialShareField("sharedomain", ObjectFieldDBType.STRING, true, true);
	public static final SocialShareField TITLE = new SocialShareField("title", ObjectFieldDBType.STRING, true, true);
	public static final SocialShareField BODY = new SocialShareField("body", ObjectFieldDBType.STRING, true, true);
	public static final SocialShareField PIC_URL = new SocialShareField("pic_url", ObjectFieldDBType.STRING, true, true);
	public static final SocialShareField URL = new SocialShareField("url", ObjectFieldDBType.STRING, true, true);
	public static final SocialShareField REMOVE_STATUS = new SocialShareField("remove_status", ObjectFieldDBType.STRING, true, true);
	public static final SocialShareField CREATE_USER = new SocialShareField("create_user", ObjectFieldDBType.STRING, true, true);
	public static final SocialShareField CREATE_TIME = new SocialShareField("create_time", ObjectFieldDBType.TIMESTAMP, true, true);
	public static final SocialShareField UPDATE_TIME_FLAG = new SocialShareField("update_time_flag", ObjectFieldDBType.TIMESTAMP, true, true);


	//
	public SocialShareField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
		super(column, type, modify, uniquene);
	}

	public SocialShareField(String column, ObjectFieldDBType type) {
		super(column, type);
	}
}
