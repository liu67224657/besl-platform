package com.enjoyf.platform.service.joymeapp.anime;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-10-26
 * Time: 下午2:35
 * To change this template use File | Settings | File Templates.
 */
public class AnimeTVDomain implements Serializable {
	//content, profile, gameres
	private static Map<Integer, AnimeTVDomain> map = new HashMap<Integer, AnimeTVDomain>();


	public static Map<Integer, String> mapValue = new HashMap<Integer, String>();


	//the content
	public static final AnimeTVDomain OTHER = new AnimeTVDomain(0, "其他");
	public static final AnimeTVDomain SOHU = new AnimeTVDomain(1, "搜狐");
	public static final AnimeTVDomain IQYI = new AnimeTVDomain(2, "爱奇艺");
	public static final AnimeTVDomain YOUKU = new AnimeTVDomain(3, "优酷");
	public static final AnimeTVDomain TUDOU = new AnimeTVDomain(4, "土豆");
	public static final AnimeTVDomain LETV = new AnimeTVDomain(5, "乐视");
	public static final AnimeTVDomain QQTV = new AnimeTVDomain(6, "QQ视频");
	public static final AnimeTVDomain I56 = new AnimeTVDomain(7, "56视频");
	public static final AnimeTVDomain KANKAN = new AnimeTVDomain(8, "看看");

	private int code;
	private String value;

	private AnimeTVDomain(int c, String value) {
		this.code = c;
        this.value=value;
		map.put(code, this);
		mapValue.put(code, value);
	}

	public int getCode() {
		return code;
	}

	public String getValue() {
		return value;
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


		if (code != ((AnimeTVDomain) o).code) return false;

		return true;
	}

	public static AnimeTVDomain getByCode(int c) {
		return map.get(c);
	}

	public static Collection<AnimeTVDomain> getAll() {
		return map.values();
	}
}
