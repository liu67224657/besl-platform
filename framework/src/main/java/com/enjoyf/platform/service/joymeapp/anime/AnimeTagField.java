package com.enjoyf.platform.service.joymeapp.anime;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created with IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-10-26
 * Time: 下午3:23
 * To change this template use File | Settings | File Templates.
 */
public class AnimeTagField extends AbstractObjectField {

	public static final AnimeTagField TAG_ID = new AnimeTagField("tag_id", ObjectFieldDBType.LONG, true, false);
	public static final AnimeTagField TAG_NAME = new AnimeTagField("tag_name", ObjectFieldDBType.STRING, true, false);
	public static final AnimeTagField PARENT_TAG_ID = new AnimeTagField("parent_tag_id", ObjectFieldDBType.LONG, true, false);
	public static final AnimeTagField TAG_DESC = new AnimeTagField("tag_desc", ObjectFieldDBType.STRING, true, false);


	public static final AnimeTagField PICJSON = new AnimeTagField("picjson", ObjectFieldDBType.STRING, true, false);

	public static final AnimeTagField CH_NAME = new AnimeTagField("ch_name", ObjectFieldDBType.STRING, true, false);
	public static final AnimeTagField EN_NAME = new AnimeTagField("en_name", ObjectFieldDBType.STRING, true, false);
	public static final AnimeTagField RESERVED = new AnimeTagField("reserved", ObjectFieldDBType.STRING, true, false);


	public static final AnimeTagField CH_DESC = new AnimeTagField("ch_desc", ObjectFieldDBType.STRING, true, false);
	public static final AnimeTagField EN_DESC = new AnimeTagField("en_desc", ObjectFieldDBType.STRING, true, false);

	public static final AnimeTagField TYPE = new AnimeTagField("type", ObjectFieldDBType.INT, true, false);
	public static final AnimeTagField MODEL = new AnimeTagField("model", ObjectFieldDBType.INT, true, false);

	public static final AnimeTagField VOLUME = new AnimeTagField("volume", ObjectFieldDBType.STRING, true, false);

	public static final AnimeTagField PLAY_NUM = new AnimeTagField("play_num", ObjectFieldDBType.LONG, true, false);
	public static final AnimeTagField FAVORITE_NUM = new AnimeTagField("favorite_num", ObjectFieldDBType.LONG, true, false);

	public static final AnimeTagField REMOVE_STATUS = new AnimeTagField("remove_status", ObjectFieldDBType.STRING, true, false);
	public static final AnimeTagField UPDATE_DATE = new AnimeTagField("update_date", ObjectFieldDBType.TIMESTAMP, true, false);
	public static final AnimeTagField CREATE_DATE = new AnimeTagField("create_date", ObjectFieldDBType.TIMESTAMP, true, false);
	public static final AnimeTagField CREATE_USER = new AnimeTagField("create_user", ObjectFieldDBType.STRING, true, false);

	public static final AnimeTagField DISPLAY_ORDER = new AnimeTagField("display_order", ObjectFieldDBType.INT, true, false);
	public static final AnimeTagField SEARCH_TYPE = new AnimeTagField("search_type", ObjectFieldDBType.INT, true, false);

	public static final AnimeTagField APP_TYPE = new AnimeTagField("app_type", ObjectFieldDBType.INT, true, false);
	public static final AnimeTagField TOTAL_SUM = new AnimeTagField("total_sum", ObjectFieldDBType.INT, true, false);


	public AnimeTagField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
		super(column, type, modify, uniquene);
	}

	public AnimeTagField(String column, ObjectFieldDBType type) {
		super(column, type);
	}
}
