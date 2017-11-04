package com.enjoyf.platform.service.joymeapp;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-11-26
 * Time: 下午4:05
 * To change this template use File | Settings | File Templates.
 */
public class AppEnterpriserType implements Serializable {
	private static Map<Integer, AppEnterpriserType> map = new HashMap<Integer, AppEnterpriserType>();
	//正式版
	public static final AppEnterpriserType DEFAULT = new AppEnterpriserType(1);
	//企业版
	public static final AppEnterpriserType ENTERPRISE = new AppEnterpriserType(2);
	//
	private int code;

	private AppEnterpriserType(int c) {
		this.code = c;

		map.put(code, this);
	}

	public int getCode() {
		return code;
	}

	@Override
	public String toString() {
		return "AppEnterpriserType: code=" + code;
	}

	@Override
	public int hashCode() {
		return code;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;


		if (code != ((AppEnterpriserType) o).code) return false;

		return true;
	}

	public static AppEnterpriserType getByCode(int c) {
		return map.get(c);
	}

	public static Collection<AppEnterpriserType> getAll() {
		return map.values();
	}


	public static Integer getEnterpriser(String appkey) {
		int enterpriser = AppEnterpriserType.DEFAULT.getCode();
		if (appkey.length() == 23) {
			if (appkey.endsWith("I") || appkey.endsWith("A")) {
				enterpriser = AppEnterpriserType.DEFAULT.getCode();
			} else if (appkey.endsWith("E")) {
				enterpriser = AppEnterpriserType.ENTERPRISE.getCode();
			}
		}
		return enterpriser;
	}


}
