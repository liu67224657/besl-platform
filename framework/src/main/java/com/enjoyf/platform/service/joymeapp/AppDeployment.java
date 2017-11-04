package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-7-29
 * Time: 下午4:44
 * To change this template use File | Settings | File Templates.
 */
public class AppDeployment implements Serializable {

	private long deploymentId;
	private String appkey;
	private AppPlatform appPlatform;
	private String path;
	private String password;
	private boolean isProduct;
	private String title;
	private String description;

	private AppDeploymentType appDeploymentType;
	private AppVersionUpdateType appVersionUpdateType;

	private AppChannelType channel;

	private AppEnterpriserType appEnterpriserType = AppEnterpriserType.DEFAULT;

	private Date createDate;
	private String createUserId;
	private String createIp;
	private Date modifyDate;
	private String modifyUserId;
	private String modifyIp;
	private ActStatus removeStatus = ActStatus.UNACT;

	public long getDeploymentId() {
		return deploymentId;
	}

	public void setDeploymentId(long deploymentId) {
		this.deploymentId = deploymentId;
	}

	public String getAppkey() {
		return appkey;
	}

	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean getIsProduct() {
		return isProduct;
	}

	public void setIsProduct(boolean product) {
		this.isProduct = product;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public AppChannelType getChannel() {
		return channel;
	}

	public void setChannel(AppChannelType channel) {
		this.channel = channel;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public String getCreateIp() {
		return createIp;
	}

	public void setCreateIp(String createIp) {
		this.createIp = createIp;
	}

	public ActStatus getRemoveStatus() {
		return removeStatus;
	}

	public void setRemoveStatus(ActStatus removeStatus) {
		this.removeStatus = removeStatus;
	}

	public AppDeploymentType getAppDeploymentType() {
		return appDeploymentType;
	}

	public void setAppDeploymentType(AppDeploymentType appDeploymentType) {
		this.appDeploymentType = appDeploymentType;
	}

	public AppPlatform getAppPlatform() {
		return appPlatform;
	}

	public void setAppPlatform(AppPlatform appPlatform) {
		this.appPlatform = appPlatform;
	}

	public AppVersionUpdateType getAppVersionUpdateType() {
		return appVersionUpdateType;
	}

	public void setAppVersionUpdateType(AppVersionUpdateType appVersionUpdateType) {
		this.appVersionUpdateType = appVersionUpdateType;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getModifyUserId() {
		return modifyUserId;
	}

	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

	public String getModifyIp() {
		return modifyIp;
	}

	public void setModifyIp(String modifyIp) {
		this.modifyIp = modifyIp;
	}

	public AppEnterpriserType getAppEnterpriserType() {
		return appEnterpriserType;
	}

	public void setAppEnterpriserType(AppEnterpriserType appEnterpriserType) {
		this.appEnterpriserType = appEnterpriserType;
	}

	@Override
	public String toString() {
		return ReflectUtil.fieldsToString(this);
	}
}
