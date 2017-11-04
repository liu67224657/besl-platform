package com.enjoyf.platform.service.joymeapp;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-8-21
 * Time: 上午9:49
 * To change this template use File | Settings | File Templates.
 */
public class SocialShareType implements Serializable {
	private static Map<Integer, SocialShareType> map = new HashMap<Integer, SocialShareType>();
	//文章
	public static final SocialShareType SOCIAL_CONTENT_TYPE = new SocialShareType(1);
	//活动
	public static final SocialShareType SOCIAL_ACTIVITY_TYPE = new SocialShareType(2);
    //咔哒推荐
    public static final SocialShareType SOCIAL_RECOMMEND_TYPE = new SocialShareType(3);

	//大动漫视频
	public static final SocialShareType ANIME_TV_TYPE = new SocialShareType(4);
	//大动漫专题
	public static final SocialShareType ANIME_SPECIAL_TYPE = new SocialShareType(5);

	private int code;

	public SocialShareType(int c) {
		this.code = c;

		map.put(code, this);
	}

	public int getCode() {
		return code;
	}

	@Override
	public int hashCode() {
		return code;
	}

	@Override
	public String toString() {
		return "SocialShareType: code=" + code;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;


		if (code != ((SocialShareType) obj).code) return false;

		return true;
	}

	public static SocialShareType getByCode(int c) {
		return map.get(c);
	}

	public static Collection<SocialShareType> getAll() {
		return map.values();
	}
}
