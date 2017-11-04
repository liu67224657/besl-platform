package com.enjoyf.webapps.joyme.dto.joymeapp.anime;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-10-27
 * Time: 下午12:20
 * To change this template use File | Settings | File Templates.
 */
public class AnimeTagDTO implements Serializable {
	private String tagid;
	private String tagname;
	private String reserved;
	private String picurl;


	public String getTagid() {
		return tagid;
	}

	public void setTagid(String tagid) {
		this.tagid = tagid;
	}

	public String getTagname() {
		return tagname;
	}

	public void setTagname(String tagname) {
		this.tagname = tagname;
	}

	public String getReserved() {
		return reserved;
	}

	public void setReserved(String reserved) {
		this.reserved = reserved;
	}

	public String getPicurl() {
		return picurl;
	}

	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
