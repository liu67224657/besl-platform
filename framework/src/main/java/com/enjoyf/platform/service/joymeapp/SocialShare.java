package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.account.AccountDomain;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-8-21
 * Time: 下午1:16
 * To change this template use File | Settings | File Templates.
 */
public class SocialShare implements Serializable {
	private Long share_id;
	private Long activityid;
	private SocialShareType share_type;
	private String appkey;
	private Integer platform;
	private AppShareChannel sharedomain;
	private String title;
	private String body;
	private String pic_url;
	private String url;
	private ActStatus remove_status = ActStatus.UNACT;
	private String create_user;
	private Date create_time;
	private Date update_time_flag;


	public String getAppkey() {
		return appkey;
	}

	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}

	public Long getShare_id() {
		return share_id;
	}

	public void setShare_id(Long share_id) {
		this.share_id = share_id;
	}

	public Long getActivityid() {
		return activityid;
	}

	public void setActivityid(Long activityid) {
		this.activityid = activityid;
	}

	public SocialShareType getShare_type() {
		return share_type;
	}

	public void setShare_type(SocialShareType share_type) {
		this.share_type = share_type;
	}

	public AppShareChannel getSharedomain() {
		return sharedomain;
	}

	public void setSharedomain(AppShareChannel sharedomain) {
		this.sharedomain = sharedomain;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getPic_url() {
		return pic_url;
	}

	public void setPic_url(String pic_url) {
		this.pic_url = pic_url;
	}

	public Integer getPlatform() {
		return platform;
	}

	public void setPlatform(Integer platform) {
		this.platform = platform;
	}

	public ActStatus getRemove_status() {
		return remove_status;
	}

	public void setRemove_status(ActStatus remove_status) {
		this.remove_status = remove_status;
	}

	public String getCreate_user() {
		return create_user;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public Date getUpdate_time_flag() {
		return update_time_flag;
	}

	public void setUpdate_time_flag(Date update_time_flag) {
		this.update_time_flag = update_time_flag;
	}

	@Override
	public String toString() {
		return ReflectUtil.fieldsToString(this);
	}
}
