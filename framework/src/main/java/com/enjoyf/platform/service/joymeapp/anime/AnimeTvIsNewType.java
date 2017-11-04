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
public class AnimeTvIsNewType implements Serializable {
	//content, profile, gameres
	private static Map<Integer, AnimeTvIsNewType> map = new HashMap<Integer, AnimeTvIsNewType>();

	//the content
	public static final AnimeTvIsNewType NOT_NEW = new AnimeTvIsNewType(0);//否
	public static final AnimeTvIsNewType NEW = new AnimeTvIsNewType(1);//是


	private int code;

	private AnimeTvIsNewType(int c) {
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


		if (code != ((AnimeTvIsNewType) o).code) return false;

		return true;
	}

	public static AnimeTvIsNewType getByCode(int c) {
		return map.get(c);
	}

	public static Collection<AnimeTvIsNewType> getAll() {
		return map.values();
	}


}
