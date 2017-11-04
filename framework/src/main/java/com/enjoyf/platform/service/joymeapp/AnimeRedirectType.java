package com.enjoyf.platform.service.joymeapp;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-12-11 上午10:01
 * Description:
 */
public class AnimeRedirectType implements Serializable {

	private static Map<Integer, AnimeRedirectType> map = new HashMap<Integer, AnimeRedirectType>();

	//视频分集       Diversity
	public static final AnimeRedirectType DIVERSITY = new AnimeRedirectType(1);
	//    public static final AnimeSpecialAttr OTHER = new AnimeSpecialAttr(2);
	//wap列表
	public static final AnimeRedirectType WAP_LIST = new AnimeRedirectType(2);
	//wap直达
	public static final AnimeRedirectType DIRECT = new AnimeRedirectType(3);

	public static final AnimeRedirectType WAP_TEXT_LIST = new AnimeRedirectType(4);

	//打开专题列表
	public static final AnimeRedirectType HAIWAN = new AnimeRedirectType(5);

	//打开追番
	public static final AnimeRedirectType ZHUIFAN = new AnimeRedirectType(6);
	//打开追番详情
	public static final AnimeRedirectType ZHUIFAN_DETAIL = new AnimeRedirectType(7);
	//事典
	public static final AnimeRedirectType SHIDIAN = new AnimeRedirectType(8);

	//消息中心小红点
	public static final AnimeRedirectType RED = new AnimeRedirectType(9);

	//打开应用
	public static final AnimeRedirectType OPEN = new AnimeRedirectType(0);
	//
	private int code;

	private AnimeRedirectType(int c) {
		this.code = c;

		map.put(code, this);
	}

	public int getCode() {
		return code;
	}

	@Override
	public String toString() {
		return "AppAnimeIndexLinkType: code=" + code;
	}

	@Override
	public int hashCode() {
		return code;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;


		if (code != ((AnimeRedirectType) o).code) return false;

		return true;
	}

	public static AnimeRedirectType getByCode(int c) {
		return map.get(c);
	}

	public static Collection<AnimeRedirectType> getAll() {
		return map.values();
	}
}
