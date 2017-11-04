package com.enjoyf.platform.service.gameres;

/**
 * 游戏筛选分类
 * @author huazhang
 *
 */
public enum GameFilterGroupType {
	PLATFORM_TYPE("PT"),//平台
	PLATFORM_DEVICE("PD"),//设备
	NET_TYPE("NT"),//网络
	LANGUAGE_TYPE("LT"),//语言
	CATEGORY_TYPE("CT"),//类型
	THEME_TYPE("TT");//主题
	
	private String type;

	private GameFilterGroupType(String type){
		this.type=type;
	}
	public String getType() {
		return type;
	}

}
