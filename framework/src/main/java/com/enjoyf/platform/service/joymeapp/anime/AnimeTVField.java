package com.enjoyf.platform.service.joymeapp.anime;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created with IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-10-26
 * Time: 下午3:11
 * To change this template use File | Settings | File Templates.
 */
public class AnimeTVField extends AbstractObjectField {

	public static final AnimeTVField TV_ID = new AnimeTVField("tv_id", ObjectFieldDBType.LONG, true, false);
	public static final AnimeTVField DOMAIN = new AnimeTVField("domain", ObjectFieldDBType.INT, true, false);
	public static final AnimeTVField DOMAIN_PARAM = new AnimeTVField("domain_param", ObjectFieldDBType.STRING, true, false);
	public static final AnimeTVField URL = new AnimeTVField("url", ObjectFieldDBType.STRING, true, false);
	public static final AnimeTVField M3U8 = new AnimeTVField("m3u8", ObjectFieldDBType.STRING, true, false);
	public static final AnimeTVField TV_PIC = new AnimeTVField("tv_pic", ObjectFieldDBType.STRING, true, false);
	public static final AnimeTVField TV_NAME = new AnimeTVField("tv_name", ObjectFieldDBType.STRING, true, false);
	public static final AnimeTVField TV_NUMBER = new AnimeTVField("tv_number", ObjectFieldDBType.INT, true, false);
	public static final AnimeTVField TAGS = new AnimeTVField("tags", ObjectFieldDBType.STRING, true, false);

	public static final AnimeTVField PLAY_NUM = new AnimeTVField("play_num", ObjectFieldDBType.LONG, true, false);
	public static final AnimeTVField FAVORITE_NUM = new AnimeTVField("favorite_num", ObjectFieldDBType.LONG, true, false);

	public static final AnimeTVField SPACE = new AnimeTVField("space", ObjectFieldDBType.STRING, true, false);
	public static final AnimeTVField IS_NEW = new AnimeTVField("isnew", ObjectFieldDBType.INT, true, false);

	public static final AnimeTVField REMOVE_STATUS = new AnimeTVField("remove_status", ObjectFieldDBType.STRING, true, false);
	public static final AnimeTVField UPDATE_DATE = new AnimeTVField("update_date", ObjectFieldDBType.TIMESTAMP, true, false);
	public static final AnimeTVField CREATE_DATE = new AnimeTVField("create_date", ObjectFieldDBType.TIMESTAMP, true, false);
	public static final AnimeTVField CREATE_USER = new AnimeTVField("create_user", ObjectFieldDBType.STRING, true, false);

	public static final AnimeTagField DISPLAY_ORDER = new AnimeTagField("display_order", ObjectFieldDBType.LONG, true, false);

	public AnimeTVField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
		super(column, type, modify, uniquene);
	}

	public AnimeTVField(String column, ObjectFieldDBType type) {
		super(column, type);
	}
}
