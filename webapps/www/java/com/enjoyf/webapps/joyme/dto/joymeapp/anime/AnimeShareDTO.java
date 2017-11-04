package com.enjoyf.webapps.joyme.dto.joymeapp.anime;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * Created with IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-8-21
 * Time: 上午9:24
 * To change this template use File | Settings | File Templates.
 */
public class AnimeShareDTO {
	private String isdefault;            //是否是默认
	private String sharedomain;        //第三方平台
	private String title;                    //标题
	private String desc;            //描述
	private String picurl;                    //图片地址
	private String url;                    //地址

	public String getIsdefault() {
		return isdefault;
	}

	public void setIsdefault(String isdefault) {
		this.isdefault = isdefault;
	}

	public String getSharedomain() {
		return sharedomain;
	}

	public void setSharedomain(String sharedomain) {
		this.sharedomain = sharedomain;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getPicurl() {
		return picurl;
	}

	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
