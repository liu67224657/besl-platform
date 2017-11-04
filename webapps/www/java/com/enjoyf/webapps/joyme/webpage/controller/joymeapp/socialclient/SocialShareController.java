package com.enjoyf.webapps.joyme.webpage.controller.joymeapp.socialclient;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.joymeapp.JoymeAppServiceSngl;
import com.enjoyf.platform.service.joymeapp.SocialShare;
import com.enjoyf.platform.service.joymeapp.SocialShareField;
import com.enjoyf.platform.service.joymeapp.SocialShareType;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.util.StringUtil;
import com.enjoyf.webapps.joyme.dto.joymeapp.socialclient.SocialShareDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-8-21
 * Time: 上午9:29
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/socialapp/share")
public class SocialShareController {

	@ResponseBody
	@RequestMapping(value = "/getshare")
	public String getshare(@RequestParam(value = "uno", required = false) String uno,
						   @RequestParam(value = "platform", required = false) Integer platform,
						   @RequestParam(value = "appkey", required = false) String appkey,
						   @RequestParam(value = "sharetype", required = false) Integer sharetype,
						   @RequestParam(value = "activityid", required = false) String activityid) {
		ResultObjectMsg msg = new ResultObjectMsg();
		try {


			appkey = getAppKey(appkey);

			QueryExpress queryExpress = new QueryExpress();
			queryExpress.add(QueryCriterions.eq(SocialShareField.APPKEY, appkey));
			queryExpress.add(QueryCriterions.eq(SocialShareField.PLATFORM, platform));

			List<Long> activityids = new ArrayList<Long>();
			activityids.add(-1L);

			long activity = -1L;
			//活动
			if (sharetype == SocialShareType.SOCIAL_ACTIVITY_TYPE.getCode()) {
				activity = Long.valueOf(activityid);
				activityids.add(Long.valueOf(activityid));
				queryExpress.add(QueryCriterions.in(SocialShareField.ACTIVITYID, activityids.toArray()));
				queryExpress.add(QueryCriterions.eq(SocialShareField.SHARE_TYPE, SocialShareType.SOCIAL_ACTIVITY_TYPE.getCode()));
			} else {
				//文章
				queryExpress.add(QueryCriterions.eq(SocialShareField.SHARE_TYPE, sharetype));
			}
			queryExpress.add(QueryCriterions.eq(SocialShareField.REMOVE_STATUS, ActStatus.UNACT.getCode()));




			List<SocialShare> list = JoymeAppServiceSngl.get().querySocialShare(queryExpress, appkey, platform, SocialShareType.getByCode(sharetype), activity);


			List<SocialShareDTO> returnObj = buildSocialShareDTO(list);
			msg.setRs(ResultObjectMsg.CODE_S);
			msg.setResult(returnObj);


		} catch (Exception e) {
			GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
			msg.setRs(ResultObjectMsg.CODE_E);
			msg.setMsg("system.error");
		}
		return JsonBinder.buildNormalBinder().toJson(msg);
	}

	private List<SocialShareDTO> buildSocialShareDTO(List<SocialShare> list) {
		List<SocialShareDTO> returnObj = new ArrayList<SocialShareDTO>();
		if (CollectionUtil.isEmpty(list)) {
			return null;
		}
		for (SocialShare socialShare : list) {
			SocialShareDTO socialShareDTO = new SocialShareDTO();
			socialShareDTO.setSharedomain(socialShare.getSharedomain().getCode());
			socialShareDTO.setTitle(StringUtil.isEmpty(socialShare.getTitle()) ? "" : socialShare.getTitle());
			socialShareDTO.setDescription(StringUtil.isEmpty(socialShare.getBody()) ? "" : socialShare.getBody());
			socialShareDTO.setPic(StringUtil.isEmpty(socialShare.getPic_url()) ? "" : socialShare.getPic_url());
			socialShareDTO.setUrl(StringUtil.isEmpty(socialShare.getUrl()) ? "" : socialShare.getUrl());
			socialShareDTO.setShareflag(socialShare.getUpdate_time_flag().getTime());
			if (socialShare.getActivityid() == -1) {
				socialShareDTO.setIsdefault(true);
			} else {
				socialShareDTO.setIsdefault(false);
			}
			returnObj.add(socialShareDTO);
		}
		return returnObj;
	}

	public static void main(String[] args) {
		ResultObjectMsg msg = new ResultObjectMsg(ResultObjectMsg.CODE_S);
		SocialShareDTO socialShareDTO = new SocialShareDTO();
		socialShareDTO.setSharedomain("qq");
		socialShareDTO.setTitle("title");
		socialShareDTO.setDescription("描述{title}了{body}结束了。");
		socialShareDTO.setPic("");
		socialShareDTO.setShareflag(new Date().getTime());
		msg.setMsg("");
		msg.setResult(socialShareDTO);
		List<SocialShareDTO> list = new ArrayList<SocialShareDTO>();
		list.add(socialShareDTO);
		list.add(socialShareDTO);
		msg.setResult(list);
		System.out.println(JsonBinder.buildNormalBinder().toJson(msg));
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
