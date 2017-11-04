package com.enjoyf.platform.service.joymeapp.anime;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-10-26
 * Time: 下午3:26
 * To change this template use File | Settings | File Templates.
 */
public class AnimeTagModel implements Serializable {
	//content, profile, gameres
	private static Map<Integer, AnimeTagModel> map = new HashMap<Integer, AnimeTagModel>();

	//the content
	public static final AnimeTagModel CHAPTER = new AnimeTagModel(1);//篇章
	public static final AnimeTagModel TV = new AnimeTagModel(2);//剧集


	private int code;

	private AnimeTagModel(int c) {
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


		if (code != ((AnimeTagModel) o).code) return false;

		return true;
	}

	public static AnimeTagModel getByCode(int c) {
		return map.get(c);
	}

	public static Collection<AnimeTagModel> getAll() {
		return map.values();
	}


}
