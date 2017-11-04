package com.enjoyf.platform.util.joymeapp;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-10-17
 * Time: 下午8:02
 * To change this template use File | Settings | File Templates.
 */
public class JoymeAppClientConstant implements Serializable {
	private int platform;//客户端平台(平台 0--ios,1--android)
	private String version;//客户端当前版本
	private String appkey;//该应用appkey(服务器端该应用唯一ID)
	private String clientid;//设备ID
	private String channelid;//渠道的code值

	public int getPlatform() {
		return platform;
	}

	public void setPlatform(int platform) {
		this.platform = platform;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getAppkey() {
		return appkey;
	}

	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}

	public String getClientid() {
		return clientid;
	}

	public void setClientid(String clientid) {
		this.clientid = clientid;
	}

	public String getChannelid() {
		return channelid;
	}

	public void setChannelid(String channelid) {
		this.channelid = channelid;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
