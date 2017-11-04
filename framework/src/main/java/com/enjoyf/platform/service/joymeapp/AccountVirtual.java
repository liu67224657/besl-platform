package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.service.ActStatus;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-6-20
 * Time: 下午2:16
 * To change this template use File | Settings | File Templates.
 */
public class AccountVirtual implements Serializable {
	private Long virtual_id;
	private String uno;
	private String screenname;
	private String create_user;
	private Date create_time;
	private ActStatus remove_status = ActStatus.UNACT;

	private AccountVirtualType accountVirtualType;

	private AccountVirtualHeadIcon headicon;//头像 四个尺寸

	public String getScreenname() {
		return screenname;
	}

	public void setScreenname(String screenname) {
		this.screenname = screenname;
	}

	public Long getVirtual_id() {
		return virtual_id;
	}

	public void setVirtual_id(Long virtual_id) {
		this.virtual_id = virtual_id;
	}

	public String getUno() {
		return uno;
	}

	public void setUno(String uno) {
		this.uno = uno;
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

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public ActStatus getRemove_status() {
		return remove_status;
	}

	public void setRemove_status(ActStatus remove_status) {
		this.remove_status = remove_status;
	}

	public AccountVirtualType getAccountVirtualType() {
		return accountVirtualType;
	}

	public void setAccountVirtualType(AccountVirtualType accountVirtualType) {
		this.accountVirtualType = accountVirtualType;
	}

	public AccountVirtualHeadIcon getHeadicon() {
		return headicon;
	}

	public void setHeadicon(AccountVirtualHeadIcon headicon) {
		this.headicon = headicon;
	}
}
