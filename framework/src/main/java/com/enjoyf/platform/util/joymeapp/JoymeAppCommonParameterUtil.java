package com.enjoyf.platform.util.joymeapp;

import com.enjoyf.platform.util.StringUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-10-17
 * Time: 下午8:05
 * To change this template use File | Settings | File Templates.
 */
public class JoymeAppCommonParameterUtil {
	public static JoymeAppClientConstant geAppCommonParameter(HttpServletRequest request) {
		String platform = request.getParameter("platform");
		if (StringUtil.isEmpty(platform)) {
			return null;
		}
		String version = request.getParameter("version");
		if (StringUtil.isEmpty(version)) {
			return null;
		}
		String appkey = request.getParameter("appkey");
		if (StringUtil.isEmpty(appkey)) {
			return null;
		}
		String clientid = request.getParameter("client_id") == null ? request.getParameter("clientid") : request.getParameter("client_id");
		if (StringUtil.isEmpty(clientid)) {
			return null;
		}
		String channelid = request.getParameter("channelid");
		if (StringUtil.isEmpty(channelid)) {
			return null;
		}
		JoymeAppClientConstant constant = new JoymeAppClientConstant();
		try {
			constant.setPlatform(Integer.valueOf(platform));
			constant.setVersion(version);
			constant.setAppkey(appkey);
			constant.setClientid(clientid);
			constant.setChannelid(channelid);
		} catch (Exception e) {
			return null;
		}
		return constant;
	}
}
