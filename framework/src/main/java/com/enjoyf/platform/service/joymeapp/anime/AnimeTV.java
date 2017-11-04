package com.enjoyf.platform.service.joymeapp.anime;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-10-26
 * Time: 下午2:34
 * To change this template use File | Settings | File Templates.
 */
public class AnimeTV implements Serializable {
	private Long tv_id;
	private AnimeTVDomain domain;
	private String domain_param;
	private String url;
	private String m3u8;
	private String tv_pic;
	private Integer tv_number;
	private String tv_name;
	private String tags;
	private Long play_num;
	private Long favorite_num;

	private String space;
	private AnimeTvIsNewType animeTvIsNewType = AnimeTvIsNewType.NOT_NEW;

	private Long display_order;

	private ValidStatus remove_status = ValidStatus.INVALID;
	private Date update_date;
	private Date create_date;
	private String create_user;

	public Long getTv_id() {
		return tv_id;
	}

	public void setTv_id(Long tv_id) {
		this.tv_id = tv_id;
	}

	public AnimeTVDomain getDomain() {
		return domain;
	}

	public void setDomain(AnimeTVDomain domain) {
		this.domain = domain;
	}

	public String getDomain_param() {
		return domain_param;
	}

	public void setDomain_param(String domain_param) {
		this.domain_param = domain_param;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getM3u8() {
		return m3u8;
	}

	public void setM3u8(String m3u8) {
		this.m3u8 = m3u8;
	}

	public String getTv_pic() {
		return tv_pic;
	}

	public void setTv_pic(String tv_pic) {
		this.tv_pic = tv_pic;
	}

	public String getTv_name() {
		return tv_name;
	}

	public void setTv_name(String tv_name) {
		this.tv_name = tv_name;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public Long getPlay_num() {
		return play_num;
	}

	public void setPlay_num(Long play_num) {
		this.play_num = play_num;
	}

	public Long getFavorite_num() {
		return favorite_num;
	}

	public void setFavorite_num(Long favorite_num) {
		this.favorite_num = favorite_num;
	}

	public ValidStatus getRemove_status() {
		return remove_status;
	}

	public void setRemove_status(ValidStatus remove_status) {
		this.remove_status = remove_status;
	}

	public Date getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public String getCreate_user() {
		return create_user;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public String getSpace() {
		return space;
	}

	public void setSpace(String space) {
		this.space = space;
	}

	public AnimeTvIsNewType getAnimeTvIsNewType() {
		return animeTvIsNewType;
	}

	public void setAnimeTvIsNewType(AnimeTvIsNewType animeTvIsNewType) {
		this.animeTvIsNewType = animeTvIsNewType;
	}

	public Integer getTv_number() {
		return tv_number;
	}

	public void setTv_number(Integer tv_number) {
		this.tv_number = tv_number;
	}

	public Long getDisplay_order() {
		return display_order;
	}

	public void setDisplay_order(Long display_order) {
		this.display_order = display_order;
	}

	@Override
	public String toString() {
		return ReflectUtil.fieldsToString(this);
	}
}
