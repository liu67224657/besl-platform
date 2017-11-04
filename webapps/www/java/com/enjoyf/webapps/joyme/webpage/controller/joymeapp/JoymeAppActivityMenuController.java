package com.enjoyf.webapps.joyme.webpage.controller.joymeapp;

import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.ResultListMsg;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.webapps.joyme.dto.joymeapp.JoymeAppTopMenuDTO;
import com.enjoyf.webapps.joyme.weblogic.joymeapp.JoymeAppWebLogic;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-20
 * Time: 下午1:45
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/joymeapp/activitymenu")
public class JoymeAppActivityMenuController extends JoymeAppBaseController {

	@Resource(name = "joymeAppWebLogic")
	private JoymeAppWebLogic joymeAppWebLogic;

	@ResponseBody
	@RequestMapping(value = "/gettopmenu")
	public String getTopMenu(HttpServletRequest request,
							 @RequestParam(value = "appkey", required = false) String appKey,
							 @RequestParam(value = "channelid", required = false) String channelCode,
							 @RequestParam(value = "queryapp", required = false, defaultValue = "false") Boolean queryApp,
							 @RequestParam(value = "querychannel", required = false, defaultValue = "false") Boolean queryChannel,
							 @RequestParam(value = "platform", required = true) Integer platform,
							 @RequestParam(value = "flag", required = false) String flag) {
		ResultListMsg resultMsg = null;
		try {
			resultMsg = new ResultListMsg(ResultListMsg.CODE_S);

			if (platform == null) {
				resultMsg.setRs(ResultObjectMsg.CODE_E);
				resultMsg.setMsg("param.is.empty");
				return JsonBinder.buildNormalBinder().toJson(resultMsg);
			}
			if (!queryChannel) {
				channelCode = "";
			}

			appKey = getAppKey(appKey);

			if (!queryApp) {
				appKey = "";
			}

			List<JoymeAppTopMenuDTO> topMenuList = joymeAppWebLogic.queryActivityTopMenuList(appKey, channelCode, platform, flag);
			if (CollectionUtil.isEmpty(topMenuList)) {
				resultMsg.setResult(null);
			} else {
				resultMsg.setResult(topMenuList);
			}
		} catch (Exception e) {
			GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
			resultMsg.setMsg("system.error");
		}
		return JsonBinder.buildNormalBinder().toJson(resultMsg);
	}

	@ResponseBody
	@RequestMapping(value = "/headimage")
	public String headMenu(HttpServletRequest request, HttpServletResponse response,
						   @RequestParam(value = "appkey", required = false) String appKey,
						   @RequestParam(value = "platform", required = false) String platform,
						   @RequestParam(value = "channelid", required = false) String channelCode,
						   @RequestParam(value = "querychannel", required = false, defaultValue = "false") boolean queryChannel,
						   @RequestParam(value = "queryapp", required = false, defaultValue = "false") boolean queryApp,
						   @RequestParam(value = "flag", required = false) String flag) {
		ResultListMsg resultMsg = null;
		try {
			resultMsg = new ResultListMsg(ResultListMsg.CODE_S);

			if (platform == null) {
				resultMsg.setRs(ResultObjectMsg.CODE_E);
				resultMsg.setMsg("param.is.empty");
				return JsonBinder.buildNormalBinder().toJson(resultMsg);
			}
			if (!queryChannel) {
				channelCode = "";
			}

			appKey = getAppKey(appKey);

			if (!queryApp) {
				appKey = "";
			}

			//手游画报企业版 替换成 手游画报，防止填2份数据
			if ("1c0VNMw4J6EWcqO6vKD5Kn".equals(appKey)) {
				appKey = "17yfn24TFexGybOF0PqjdY";
			}

			List<JoymeAppTopMenuDTO> topMenuList = joymeAppWebLogic.queryActivityHeadImageList(appKey, channelCode, platform, flag);
			if (CollectionUtil.isEmpty(topMenuList)) {
				resultMsg.setResult(null);
			} else {
				resultMsg.setResult(topMenuList);
			}
		} catch (Exception e) {
			GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
			resultMsg.setMsg("system.error");
		}
		return JsonBinder.buildNormalBinder().toJson(resultMsg);
	}
}
