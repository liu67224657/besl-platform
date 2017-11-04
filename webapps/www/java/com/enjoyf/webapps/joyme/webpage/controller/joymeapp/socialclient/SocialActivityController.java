package com.enjoyf.webapps.joyme.webpage.controller.joymeapp.socialclient;

import com.enjoyf.platform.service.content.ContentServiceSngl;
import com.enjoyf.platform.service.content.social.SocialActivity;
import com.enjoyf.platform.service.content.social.SocialActivityType;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.NextPageRows;
import com.enjoyf.platform.util.NextPagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.webapps.joyme.dto.joymeapp.socialclient.ActivityDTO;
import com.enjoyf.webapps.joyme.dto.joymeapp.socialclient.SocialProfileContentDTO;
import com.enjoyf.webapps.joyme.weblogic.joymeapp.socialclient.SocialClientWebLogic;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-4-14
 * Time: 下午12:27
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/socialapp/activity")
public class SocialActivityController {
	private Logger logger = Logger.getLogger(SocialActivityController.class);
	private static final int SOCIAL_ACTIVITY_PAGE_SIZE = 7;

	@Resource(name = "socialClientWebLogic")
	private SocialClientWebLogic socialClientWebLogic;

	@ResponseBody
	@RequestMapping(value = "/index")
	public String index(HttpServletRequest request, HttpServletResponse response,
						@RequestParam(value = "appkey", required = false) String appId,
						@RequestParam(value = "platform", required = false) String platformStr,
						@RequestParam(value = "startid", required = false, defaultValue = "0") String startNumStr,
						@RequestParam(value = "isnext", required = false, defaultValue = "true") String isNext
	) {
		ResultObjectMsg resultMsg = new ResultObjectMsg();

		//appkey为空
		if (StringUtil.isEmpty(appId)) {
			resultMsg.setRs(ResultCodeConstants.SOCIAL_APPKEY_IS_NULL.getCode());
			resultMsg.setMsg(ResultCodeConstants.SOCIAL_APPKEY_IS_NULL.getMsg());
			return JsonBinder.buildNormalBinder().toJson(resultMsg);
		}

		if (StringUtil.isEmpty(platformStr)) {
			resultMsg.setRs(ResultCodeConstants.SOCIAL_PLATFORM_IS_NULL.getCode());
			resultMsg.setMsg(ResultCodeConstants.SOCIAL_PLATFORM_IS_NULL.getMsg());
			return JsonBinder.buildNormalBinder().toJson(resultMsg);
		}

		appId = getAppKey(appId);
		int platform = Integer.valueOf(platformStr);

		int startNum = 0;
		try {
			startNum = Integer.parseInt(startNumStr);
		} catch (NumberFormatException e) {
		}

		Boolean next = Boolean.valueOf(isNext);
		NextPagination nextPagination = new NextPagination(startNum, SOCIAL_ACTIVITY_PAGE_SIZE, next);
		try {
			NextPageRows<ActivityDTO> result = socialClientWebLogic.querySocialActivity(appId, platform, nextPagination, startNum);
			if (result == null) {
				resultMsg.setRs(ResultCodeConstants.SUCCESS.getCode());
				resultMsg.setMsg("result.is.empty");
				return JsonBinder.buildNormalBinder().toJson(resultMsg);
			}
			resultMsg.setResult(result);
			resultMsg.setRs(ResultCodeConstants.SUCCESS.getCode());
			resultMsg.setMsg("success");
			return JsonBinder.buildNormalBinder().toJson(resultMsg);
		} catch (Exception e) {
			GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
			resultMsg.setRs(ResultObjectMsg.CODE_E);
			resultMsg.setMsg("system.error");
			return JsonBinder.buildNormalBinder().toJson(resultMsg);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/list")
	public String list(HttpServletRequest request, HttpServletResponse response,
					   @RequestParam(value = "uno", required = false) String uno,
					   @RequestParam(value = "activityid", required = false) String activityIdStr,
					   @RequestParam(value = "platform", required = false) String platformStr,
					   @RequestParam(value = "startid", required = false, defaultValue = "0") String startNumStr,
					   @RequestParam(value = "isnext", required = false, defaultValue = "true") String isNext,
					   @RequestParam(value = "appkey", required = false) String appkey
	) {
		ResultObjectMsg resultMsg = new ResultObjectMsg();
		if (StringUtil.isEmpty(uno)) {
			resultMsg.setRs(ResultCodeConstants.SOCIAL_UNO_IS_NULL.getCode());
			resultMsg.setMsg(ResultCodeConstants.SOCIAL_UNO_IS_NULL.getMsg());
			return JsonBinder.buildNormalBinder().toJson(resultMsg);
		}
		if (StringUtil.isEmpty(activityIdStr)) {
			resultMsg.setRs(ResultCodeConstants.SOCIAL_ACTIVITY_ID_IS_NULL.getCode());
			resultMsg.setMsg(ResultCodeConstants.SOCIAL_ACTIVITY_ID_IS_NULL.getMsg());
			return JsonBinder.buildNormalBinder().toJson(resultMsg);
		}
		if (StringUtil.isEmpty(platformStr)) {
			resultMsg.setRs(ResultCodeConstants.SOCIAL_PLATFORM_IS_NULL.getCode());
			resultMsg.setMsg(ResultCodeConstants.SOCIAL_PLATFORM_IS_NULL.getMsg());
			return JsonBinder.buildNormalBinder().toJson(resultMsg);
		}
		long activityId = Long.valueOf(activityIdStr);
		int platform = Integer.valueOf(platformStr);

		long startNum = 0l;
		try {
			startNum = Long.valueOf(startNumStr);
		} catch (NumberFormatException e) {
		}

		Boolean next = Boolean.valueOf(isNext);
		NextPagination nextPagination = new NextPagination(startNum, SOCIAL_ACTIVITY_PAGE_SIZE, next);
		try {
			SocialActivity activity = ContentServiceSngl.get().getSocialActivity(activityId);
			if (activity == null) {
				resultMsg.setRs(ResultCodeConstants.SOCIAL_ACTIVITY_IS_NULL.getCode());
				resultMsg.setMsg(ResultCodeConstants.SOCIAL_ACTIVITY_IS_NULL.getMsg());
				return JsonBinder.buildNormalBinder().toJson(resultMsg);
			}
			nextPagination.setTotalRows(activity.getTotals());

			NextPageRows<SocialProfileContentDTO> result = socialClientWebLogic.querySocialContentByActivity(uno, activityId, platform, nextPagination, appkey);
			if (result == null) {
				resultMsg.setRs(ResultCodeConstants.SUCCESS.getCode());
				resultMsg.setMsg("result.is.empty");
				return JsonBinder.buildNormalBinder().toJson(resultMsg);
			}
			resultMsg.setResult(result);
			resultMsg.setRs(ResultCodeConstants.SUCCESS.getCode());
			resultMsg.setMsg("success");
			return JsonBinder.buildNormalBinder().toJson(resultMsg);
		} catch (Exception e) {
			GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
			resultMsg.setRs(ResultObjectMsg.CODE_E);
			resultMsg.setMsg("system.error");
			return JsonBinder.buildNormalBinder().toJson(resultMsg);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/get")
	public String get(HttpServletRequest request, HttpServletResponse response,
					  @RequestParam(value = "activityid", required = false) String activityIdStr,
					  @RequestParam(value = "platform", required = false) String platformStr,
					  @RequestParam(value = "appkey", required = false) String appkey
	) {
		ResultObjectMsg resultMsg = new ResultObjectMsg();

		if (StringUtil.isEmpty(platformStr)) {
			resultMsg.setRs(ResultCodeConstants.SOCIAL_PLATFORM_IS_NULL.getCode());
			resultMsg.setMsg(ResultCodeConstants.SOCIAL_PLATFORM_IS_NULL.getMsg());
			return JsonBinder.buildNormalBinder().toJson(resultMsg);
		}
		if (StringUtil.isEmpty(activityIdStr)) {
			resultMsg.setRs(ResultCodeConstants.SOCIAL_ACTIVITY_ID_IS_NULL.getCode());
			resultMsg.setMsg(ResultCodeConstants.SOCIAL_ACTIVITY_ID_IS_NULL.getMsg());
			return JsonBinder.buildNormalBinder().toJson(resultMsg);
		}

		long activityId = Long.valueOf(activityIdStr);
		int platform = Integer.valueOf(platformStr);
		try {
			ActivityDTO result = socialClientWebLogic.getSocialActivity(platform, activityId, appkey);
			if (result == null) {
				resultMsg.setRs(ResultCodeConstants.SUCCESS.getCode());
				resultMsg.setMsg("result.is.empty");
				return JsonBinder.buildNormalBinder().toJson(resultMsg);
			}
			resultMsg.setResult(result);
			resultMsg.setRs(ResultCodeConstants.SUCCESS.getCode());
			resultMsg.setMsg("success");
			return JsonBinder.buildNormalBinder().toJson(resultMsg);
		} catch (Exception e) {
			GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
			resultMsg.setRs(ResultObjectMsg.CODE_E);
			resultMsg.setMsg("system.error");
			return JsonBinder.buildNormalBinder().toJson(resultMsg);
		}
	}

	private static String getAppKey(String appKey) {
		if (com.enjoyf.platform.util.StringUtil.isEmpty(appKey)) {
			return "";
		}
		if (appKey.length() < 23) {
			return appKey;
		}
		return appKey.substring(0, appKey.length() - 1);
	}

}
