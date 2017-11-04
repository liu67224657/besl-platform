/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.serv.joymeappconfig;

import com.enjoyf.platform.service.joymeapp.AppDeployment;
import com.enjoyf.platform.service.joymeapp.AppInfo;
import com.enjoyf.platform.service.joymeappconfig.AppChannel;
import com.enjoyf.platform.util.memcached.MemCachedConfig;
import com.enjoyf.platform.util.memcached.MemCachedManager;

import java.util.List;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-9-25 下午5:18
 * Description:
 */
class AppInfoCache {

	private static final long TIME_OUT_SEC = 60l * 12l;
	private static final String PREFIX_SERVICE = "joymeapp";
	private static final String PREFIX_CHANNEL = "_channel_";
	private static final String PREFIX_DEPLOYMENT = "_deployment_";
	private static final String PREFIX_INFO = "_appinfo_avpc_";

	private MemCachedConfig config;

	private MemCachedManager manager;

	AppInfoCache(MemCachedConfig config) {
		this.config = config;
		manager = new MemCachedManager(config);
	}

	//////////////////////////////////////////////////////////
	public void putAppChannelList(List<AppChannel> channelList) {
		manager.put(PREFIX_SERVICE + PREFIX_CHANNEL, channelList, TIME_OUT_SEC);
	}

	public List<AppChannel> getAppChannelList() {
		Object infoList = manager.get(PREFIX_SERVICE + PREFIX_CHANNEL);
		if (infoList == null) {
			return null;
		}
		return (List<AppChannel>) infoList;
	}

	public boolean deleteAppChannelList() {
		return manager.remove(PREFIX_SERVICE + PREFIX_CHANNEL);
	}


	public void putAppDeployment(String appKey, int platform, int appDeploymentType, String channel, AppDeployment appDeployment, Integer enterpriser) {
		manager.put(PREFIX_SERVICE + PREFIX_DEPLOYMENT + appKey + platform + appDeploymentType + channel + enterpriser, appDeployment, TIME_OUT_SEC);
	}

	public AppDeployment getAppDeployment(String appKey, int platform, int appDeploymentType, String channel, Integer enterpriser) {
		Object deployment = manager.get(PREFIX_SERVICE + PREFIX_DEPLOYMENT + appKey + platform + appDeploymentType + channel + enterpriser);
		if (deployment == null) {
			return null;
		}
		return (AppDeployment) deployment;
	}

	public boolean deleteAppDeployment(String appKey, int platform, int appDeploymentType, String channel, Integer enterpriser) {
		return manager.remove(PREFIX_SERVICE + PREFIX_DEPLOYMENT + appKey + platform + appDeploymentType + channel + enterpriser);
	}

	public void putAppInfo(String appKey, String version, int platform, String channelType, AppInfo appInfo) {
		manager.put(PREFIX_SERVICE + PREFIX_INFO + appKey + version + platform + channelType, appInfo, 5l * 60l);  //5分钟
	}

	public AppInfo getAppInfo(String appKey, String version, int platform, String channelType) {
		Object appInfo = manager.get(PREFIX_SERVICE + PREFIX_INFO + appKey + version + platform + channelType);
		if (appInfo == null) {
			return null;
		}
		return (AppInfo) appInfo;
	}

	public boolean deleteAppInfo(String appKey, String version, int platform, String channelType) {
		return manager.remove(PREFIX_SERVICE + PREFIX_INFO + appKey + version + platform + channelType);
	}
}
