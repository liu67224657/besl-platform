package com.enjoyf.webapps.joyme.dto.joymeapp.anime;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * Created with IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-11-3
 * Time: 下午9:00
 * To change this template use File | Settings | File Templates.
 */
public class AnimeConfigDTO {
	private String openad;//是否可以开屏广告
	private String tvversion;//视频加载的版本
	private String download;//是否可以离线下载

	public String getOpenad() {
		return openad;
	}

	public void setOpenad(String openad) {
		this.openad = openad;
	}

	public String getTvversion() {
		return tvversion;
	}

	public void setTvversion(String tvversion) {
		this.tvversion = tvversion;
	}

	public String getDownload() {
		return download;
	}

	public void setDownload(String download) {
		this.download = download;
	}


	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
