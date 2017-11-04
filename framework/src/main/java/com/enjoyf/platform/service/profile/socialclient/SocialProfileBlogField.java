package com.enjoyf.platform.service.profile.socialclient;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-7-17
 * Time: 下午6:49
 * To change this template use File | Settings | File Templates.
 */
public class SocialProfileBlogField extends AbstractObjectField {

	//
	public static final SocialProfileBlogField UNO = new SocialProfileBlogField("UNO", ObjectFieldDBType.STRING, true, true);
	public static final SocialProfileBlogField SCREENNAME = new SocialProfileBlogField("SCREENNAME", ObjectFieldDBType.STRING, true, true);
	public static final SocialProfileBlogField DOMAINNAME = new SocialProfileBlogField("DOMAINNAME", ObjectFieldDBType.STRING, true, true);

	public static final SocialProfileBlogField HEADICON = new SocialProfileBlogField("HEADICON", ObjectFieldDBType.STRING, true, false);
	public static final SocialProfileBlogField DESCRIPTION = new SocialProfileBlogField("BLOGDESCRIPTION", ObjectFieldDBType.STRING, true, false);

	public static final SocialProfileBlogField TEMPLATEID = new SocialProfileBlogField("TEMPLATEID", ObjectFieldDBType.STRING, true, false);
	public static final SocialProfileBlogField SELFSETSTATUS = new SocialProfileBlogField("SELFSETSTATUS", ObjectFieldDBType.STRING, true, false);
	public static final SocialProfileBlogField SELFSETDATA = new SocialProfileBlogField("SELFSETDATA", ObjectFieldDBType.STRING, true, false);

	public static final SocialProfileBlogField UPDATEDATE = new SocialProfileBlogField("UPDATEDATE", ObjectFieldDBType.TIMESTAMP, true, false);
	public static final SocialProfileBlogField MOREHEADICONS = new SocialProfileBlogField("MOREHEADICONS", ObjectFieldDBType.STRING, true, false);

	public static final SocialProfileBlogField AUDITSTATUS = new SocialProfileBlogField("AUDITSTATUS", ObjectFieldDBType.INT, true, false); //审核标记字段
	public static final SocialProfileBlogField AUDITDATE = new SocialProfileBlogField("AUDITDATE", ObjectFieldDBType.TIMESTAMP, true, false);//审核时间
	public static final SocialProfileBlogField AUDITUSERID = new SocialProfileBlogField("AUDITUSERID", ObjectFieldDBType.STRING, true, false);//审核人

	public static final SocialProfileBlogField ACTIVESTATUS = new SocialProfileBlogField("ACTIVESTATUS", ObjectFieldDBType.STRING, true, false);
	public static final SocialProfileBlogField INACTIVETILLDATE = new SocialProfileBlogField("INACTIVETILLDATE", ObjectFieldDBType.TIMESTAMP, true, false);

	public static final SocialProfileBlogField PROFILEDOMAIN = new SocialProfileBlogField("PROFILEDOMAIN", ObjectFieldDBType.STRING, true, false);

	public static final SocialProfileBlogField CREATEDATE = new SocialProfileBlogField("CREATEDATE", ObjectFieldDBType.TIMESTAMP, true, false);//创建时间
	public static final SocialProfileBlogField USERID = new SocialProfileBlogField("USERID", ObjectFieldDBType.LONG, true, false);//创建时间


	public static final SocialProfileBlogField PHONENUM = new SocialProfileBlogField("PHONENUM", ObjectFieldDBType.STRING, true, false);
	public static final SocialProfileBlogField PHONEVERIFYSTATUS = new SocialProfileBlogField("PHONEVERIFYSTATUS", ObjectFieldDBType.STRING, true, false);
	public static final SocialProfileBlogField PHONEBINDDATE = new SocialProfileBlogField("PHONEBINDDATE", ObjectFieldDBType.TIMESTAMP, true, false);

	public static final SocialProfileBlogField PLAYINGGAMES = new SocialProfileBlogField("PLAYINGGAMES", ObjectFieldDBType.STRING, true, false);
	public static final SocialProfileBlogField BACKGROUNDPIC = new SocialProfileBlogField("BACKGROUNDPIC", ObjectFieldDBType.TIMESTAMP, true, false);

	//
	public SocialProfileBlogField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
		super(column, type, modify, uniquene);
	}

	public SocialProfileBlogField(String column, ObjectFieldDBType type) {
		super(column, type);
	}
}
