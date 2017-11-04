package com.enjoyf.platform.service.joymeapp.gameclient;

import com.enjoyf.platform.service.JsonBinder;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhimingli
 * Date: 2014/12/31
 * Time: 17:02
 */
public class ArchiveRelationType implements Serializable {

	private static Map<Integer, ArchiveRelationType> map = new HashMap<Integer, ArchiveRelationType>();

	public static final ArchiveRelationType TAG_RELATION = new ArchiveRelationType(0);//文章-标签  关系
	public static final ArchiveRelationType GAME_RELATION = new ArchiveRelationType(1);//文章-游戏   关系

	private int code;

	private ArchiveRelationType(int c) {
		this.code = c;

		map.put(code, this);
	}

	public int getCode() {
		return code;
	}

	@Override
	public String toString() {
		return JsonBinder.buildNormalBinder().toJson(this);
	}

	@Override
	public int hashCode() {
		return code;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;


		if (code != ((ArchiveRelationType) o).code) return false;

		return true;
	}

	public static ArchiveRelationType getByCode(int c) {
		return map.get(c);
	}

	public static Collection<ArchiveRelationType> getAll() {
		return map.values();
	}
}
