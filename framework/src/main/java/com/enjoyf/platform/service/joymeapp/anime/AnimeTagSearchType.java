package com.enjoyf.platform.service.joymeapp.anime;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-10-28
 * Time: 下午2:35
 * To change this template use File | Settings | File Templates.
 */
public class AnimeTagSearchType implements Serializable {
	//content, profile, gameres
	private static Map<Integer, AnimeTagSearchType> map = new HashMap<Integer, AnimeTagSearchType>();

	//the content
	public static final AnimeTagSearchType OTHER = new AnimeTagSearchType(0);//其他
	public static final AnimeTagSearchType ANIMATION = new AnimeTagSearchType(1);//动画
	public static final AnimeTagSearchType TV_ANIMATION = new AnimeTagSearchType(2);//TV动画
	public static final AnimeTagSearchType ANIMATION_MOVIE = new AnimeTagSearchType(3);//动画电影
	public static final AnimeTagSearchType OVA_ANIMATION = new AnimeTagSearchType(4);//OVA动画
	public static final AnimeTagSearchType TV = new AnimeTagSearchType(5);//电视剧
	public static final AnimeTagSearchType USA_TV = new AnimeTagSearchType(6);//美剧
	public static final AnimeTagSearchType JAPAN_TV = new AnimeTagSearchType(7);//日剧
	public static final AnimeTagSearchType MOVIE = new AnimeTagSearchType(8);//电影
	public static final AnimeTagSearchType AMUSE = new AnimeTagSearchType(9);//娱乐
	public static final AnimeTagSearchType MUSIC = new AnimeTagSearchType(10);//音乐


	private int code;

	private AnimeTagSearchType(int c) {
		this.code = c;

		map.put(code, this);
	}

	public int getCode() {
		return code;
	}

	@Override
	public String toString() {
		return "AppPlatform: code=" + code;
	}

	@Override
	public int hashCode() {
		return code;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;


		if (code != ((AnimeTagSearchType) o).code) return false;

		return true;
	}

	public static AnimeTagSearchType getByCode(int c) {
		return map.get(c);
	}

	public static Collection<AnimeTagSearchType> getAll() {
		return map.values();
	}


}