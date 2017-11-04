package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-8-14
 * Time: 上午11:50
 * To change this template use File | Settings | File Templates.
 */
public class JoymeAppTopNews implements Serializable {
	private Long top_news_id;
	private String appkey;
	private String title;
	private String url;
	private String create_userid;
	private Date createdate;
	private Date modifydate;
	private ActStatus removestatus = ActStatus.UNACT;


	public Long getTop_news_id() {
		return top_news_id;
	}

	public void setTop_news_id(Long top_news_id) {
		this.top_news_id = top_news_id;
	}

	public String getAppkey() {
		return appkey;
	}

	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public Date getModifydate() {
		return modifydate;
	}

	public void setModifydate(Date modifydate) {
		this.modifydate = modifydate;
	}

	public String getCreate_userid() {
		return create_userid;
	}

	public void setCreate_userid(String create_userid) {
		this.create_userid = create_userid;
	}

	public ActStatus getRemovestatus() {
		return removestatus;
	}

	public void setRemovestatus(ActStatus removestatus) {
		this.removestatus = removestatus;
	}

	@Override
	public String toString() {
		return ReflectUtil.fieldsToString(this);
	}
}
