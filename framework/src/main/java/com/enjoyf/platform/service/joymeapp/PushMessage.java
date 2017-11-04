package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-6-1
 * Time: 下午3:13
 * To change this template use File | Settings | File Templates.
 */
@JsonIgnoreProperties(value = {"removeStatus", "sendStatus", "removeStatus", "removeDate", "appPlatform", "createDate", "sendDate", "createUserid", "appKey"})
public class PushMessage implements Serializable {
	private long pushMsgId;

	private String appKey;
	private AppPlatform appPlatform;

	//android通知栏
	private String msgIcon;
	private String msgSubject;
	private String shortMessage;
	//
	private PushMessageOptions options;

	private Date sendDate;

	private ActStatus pushStatus = ActStatus.UNACT;

	private String createUserid;
	private Date createDate;
	private PushListType pushListType;

	private String devices;
	private String unos;
	private AppPushChannel pushChannel = AppPushChannel.DEFAULT;
	private String appVersion;
	private String appChannel;
	private String tags;
	private String sound;
	private int badge;

	private Date modifyDate;
	private String modifyUserId;

	private ActStatus sendStatus = ActStatus.UNACT;  //推送状态
	private ActStatus e_sendStatus = ActStatus.UNACT;  //企业版推送状态

    private AppEnterpriserType enterpriseType = AppEnterpriserType.DEFAULT;

	public PushMessage() {
	}

	public long getPushMsgId() {
		return pushMsgId;
	}

	public void setPushMsgId(long pushMsgId) {
		this.pushMsgId = pushMsgId;
	}

	public String getMsgIcon() {
		return msgIcon;
	}

	public void setMsgIcon(String msgIcon) {
		this.msgIcon = msgIcon;
	}

	public String getMsgSubject() {
		return msgSubject;
	}

	public void setMsgSubject(String msgSubject) {
		this.msgSubject = msgSubject;
	}

	public String getShortMessage() {
		return shortMessage;
	}

	public void setShortMessage(String shortMessage) {
		this.shortMessage = shortMessage;
	}

	public AppPlatform getAppPlatform() {
		return appPlatform;
	}

	public void setAppPlatform(AppPlatform appPlatform) {
		this.appPlatform = appPlatform;
	}

	public PushMessageOptions getOptions() {
		return options;
	}

	public void setOptions(PushMessageOptions options) {
		this.options = options;
	}

	public Date getSendDate() {
		return sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	public ActStatus getPushStatus() {
		return pushStatus;
	}

	public void setPushStatus(ActStatus pushStatus) {
		this.pushStatus = pushStatus;
	}

	public String getCreateUserid() {
		return createUserid;
	}

	public void setCreateUserid(String createUserid) {
		this.createUserid = createUserid;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public PushListType getPushListType() {
		return pushListType;
	}

	public void setPushListType(PushListType pushListType) {
		this.pushListType = pushListType;
	}

	public String getUnos() {
		return unos;
	}

	public void setUnos(String unos) {
		this.unos = unos;
	}

	public AppPushChannel getPushChannel() {
		return pushChannel;
	}

	public void setPushChannel(AppPushChannel pushChannel) {
		this.pushChannel = pushChannel;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getAppChannel() {
		return appChannel;
	}

	public void setAppChannel(String appChannel) {
		this.appChannel = appChannel;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getDevices() {
		return devices;
	}

	public void setDevices(String devices) {
		this.devices = devices;
	}

	public String getSound() {
		return sound;
	}

	public void setSound(String sound) {
		this.sound = sound;
	}

	public int getBadge() {
		return badge;
	}

	public void setBadge(int badge) {
		this.badge = badge;
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

	public ActStatus getSendStatus() {
		return sendStatus;
	}

	public void setSendStatus(ActStatus sendStatus) {
		this.sendStatus = sendStatus;
	}

	public ActStatus getE_sendStatus() {
		return e_sendStatus;
	}

	public void setE_sendStatus(ActStatus e_sendStatus) {
		this.e_sendStatus = e_sendStatus;
	}

	public AppEnterpriserType getEnterpriseType() {
		return enterpriseType;
	}

	public void setEnterpriseType(AppEnterpriserType enterpriseType) {
		this.enterpriseType = enterpriseType;
	}

	@Override
	public String toString() {
		return ReflectUtil.fieldsToString(this);
	}

	public String toJsonStr() {
		return JsonBinder.buildNormalBinder().toJson(this);
	}

	public static PushMessage parse(String jsonStr) {
		try {
			return JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, PushMessage.class);
		} catch (IOException e) {
			GAlerter.lab("PushMessage parse error, jsonStr:" + jsonStr, e);
		}
		return null;
	}
}
