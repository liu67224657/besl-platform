package com.enjoyf.webapps.tools.webpage.controller.joymeapp.socialclient;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.account.AccountDomain;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.AuthAppField;
import com.enjoyf.platform.service.oauth.AuthAppType;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-8-21
 * Time: 下午4:19
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/joymeapp/socialclient/share")
public class SocialShareController extends ToolsBaseController {

	private static List<AppShareChannel> accountDomainList = new ArrayList<AppShareChannel>();

	private static List<AppPlatform> platformList = new ArrayList<AppPlatform>();

	private static String appkey = "0VsYSLLsN8CrbBSMUOlLNx";

	static {
		//	accountDomainList.add(AppShareChannel.DEFAULT);
		accountDomainList.add(AppShareChannel.SINAWEIBO);
		accountDomainList.add(AppShareChannel.QQWEIBO);
		accountDomainList.add(AppShareChannel.QQPLUS);
		accountDomainList.add(AppShareChannel.WEIXIN);
		accountDomainList.add(AppShareChannel.WEIXINPENGYOUQUAN);

		platformList.add(AppPlatform.IOS);
		platformList.add(AppPlatform.ANDROID);
	}

	@RequestMapping(value = "/list")
	public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
							 @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
							 HttpServletRequest request
	) {
		Map<String, Object> mapMessage = new HashMap<String, Object>();
		int curPage = (pageStartIndex / pageSize) + 1;
		Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
		QueryExpress queryExpress = new QueryExpress();
		try {
			queryExpress.add(QuerySort.add(SocialShareField.SHARE_ID, QuerySortOrder.DESC));
			queryExpress.add(QueryCriterions.eq(SocialShareField.APPKEY, appkey));
			PageRows<SocialShare> pageRows = JoymeAppServiceSngl.get().querySocialShareByPage(queryExpress, pagination);
			mapMessage.put("list", pageRows.getRows());
			mapMessage.put("page", pageRows.getPage());
		} catch (Exception e) {

		}
		return new ModelAndView("/joymeapp/socialclient/share/sharelist", mapMessage);
	}

	@RequestMapping(value = "/createpage")
	public ModelAndView createPage() {
		Map<String, Object> mapMessage = new HashMap<String, Object>();
		mapMessage.put("accountDomainList", accountDomainList);
		mapMessage.put("socialShareTypes", SocialShareType.getAll());
		mapMessage.put("platformList", platformList);
		List<AuthApp> appList = getAppList();
		mapMessage.put("appList", appList);
		return new ModelAndView("/joymeapp/socialclient/share/createpage", mapMessage);
	}

	@RequestMapping(value = "/create")
	public ModelAndView create(@RequestParam(value = "activityid", required = false) String activityid,
							   @RequestParam(value = "share_type", required = false) Integer share_type,
							   @RequestParam(value = "platform", required = false) Integer platform,
							   @RequestParam(value = "sharedomain", required = false) String sharedomain,
							   @RequestParam(value = "title", required = false) String title,
							   @RequestParam(value = "body", required = false) String body,
							   @RequestParam(value = "appkey", required = false) String appkey,
							   @RequestParam(value = "pic_url", required = false) String pic_url,
							   @RequestParam(value = "url", required = false) String url) {
		Map<String, Object> mapMessage = new HashMap<String, Object>();
		SocialShare socialShare = new SocialShare();
		try {
			socialShare.setAppkey(appkey);
			socialShare.setShare_type(SocialShareType.getByCode(share_type));
			socialShare.setPlatform(platform);
			socialShare.setSharedomain(AppShareChannel.getByCode(sharedomain));
			if (!StringUtil.isEmpty(activityid)) {
				socialShare.setActivityid(Long.valueOf(activityid));
			}

			//1-文章 2-活动
			if (sharedomain.equals("def") || share_type == 1) {
				socialShare.setActivityid(Long.valueOf(-1));
			}

			if (!StringUtil.isEmpty(url)) {
				socialShare.setUrl(url);
			}
			if (!StringUtil.isEmpty(body)) {
				socialShare.setBody(body);
			}
			if (!StringUtil.isEmpty(title)) {
				socialShare.setTitle(title);
			}

			if (!StringUtil.isEmpty(pic_url)) {
				socialShare.setPic_url(pic_url);
			}
			socialShare.setRemove_status(ActStatus.UNACT);
			socialShare.setCreate_user(this.getCurrentUser().getUserid());
			Date date = new Date();
			socialShare.setCreate_time(date);
			socialShare.setUpdate_time_flag(date);

			QueryExpress queryExpress = new QueryExpress();
			queryExpress.add(QueryCriterions.eq(SocialShareField.SHARE_TYPE, socialShare.getShare_type().getCode()));
			queryExpress.add(QueryCriterions.eq(SocialShareField.ACTIVITYID, socialShare.getActivityid()));
			queryExpress.add(QueryCriterions.eq(SocialShareField.APPKEY, socialShare.getAppkey()));
			queryExpress.add(QueryCriterions.eq(SocialShareField.PLATFORM, socialShare.getPlatform()));
			queryExpress.add(QueryCriterions.eq(SocialShareField.SHAREDOMAIN, socialShare.getSharedomain().getCode()));
			SocialShare retuenSocialshare = JoymeAppServiceSngl.get().getSocialShare(queryExpress);
			if (retuenSocialshare == null) {
				SocialShare socialShare1 = JoymeAppServiceSngl.get().createSocialShare(socialShare, appkey, platform, socialShare.getShare_type(), socialShare.getActivityid());

				updateUpdatetimeflag(socialShare1.getUpdate_time_flag(), appkey, platform, share_type, socialShare1.getActivityid());
			} else {
				mapMessage.put("accountDomainList", accountDomainList);
				mapMessage.put("socialShareTypes", SocialShareType.getAll());
				mapMessage.put("platformList", platformList);
				List<AuthApp> appList = getAppList();
				mapMessage.put("appList", appList);
				mapMessage.put("socialShare", socialShare);
				mapMessage.put("errorMsg", "已经存在需要建立的模板，请修改！");
				return new ModelAndView("/joymeapp/socialclient/share/createpage", mapMessage);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("redirect:/joymeapp/socialclient/share/list");
	}

	@RequestMapping(value = "/modifypage")
	public ModelAndView modifyPage(@RequestParam(value = "shareid") Long shareid) {
		Map<String, Object> mapMessage = new HashMap<String, Object>();
		QueryExpress queryExpress = new QueryExpress();
		queryExpress.add(QueryCriterions.eq(SocialShareField.SHARE_ID, shareid));
		try {
			SocialShare socialShare = JoymeAppServiceSngl.get().getSocialShare(queryExpress);
			mapMessage.put("socialShare", socialShare);
			if (socialShare != null) {
				AuthApp authApp = OAuthServiceSngl.get().getApp(socialShare.getAppkey());
				mapMessage.put("appName", authApp.getAppName());
				//mapMessage.put("appkey",socialShare.getAppkey());
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return new ModelAndView("/joymeapp/socialclient/share/modifypage", mapMessage);
	}

	@RequestMapping(value = "/modify")
	public ModelAndView modify(@RequestParam(value = "shareid", required = false) Long share_id,
							   @RequestParam(value = "title", required = false) String title,
							   @RequestParam(value = "url", required = false) String url,
							   @RequestParam(value = "body", required = false) String body,
							   @RequestParam(value = "appkey", required = false) String appkey,
							   @RequestParam(value = "platform", required = false) Integer platform,
							   @RequestParam(value = "pic_url", required = false) String pic_url,
							   @RequestParam(value = "del", required = false) String del,
							   @RequestParam(value = "share_type", required = false) Integer share_type,
							   @RequestParam(value = "activityid", required = false) Long activityid) {
		Map<String, Object> mapMessage = new HashMap<String, Object>();
		UpdateExpress updateExpress = new UpdateExpress();
		try {
			Date date = new Date();
			if (!StringUtil.isEmpty(del)) {
				updateExpress.set(SocialShareField.REMOVE_STATUS, ActStatus.getByCode(del).getCode());
				updateExpress.set(SocialShareField.UPDATE_TIME_FLAG, date);
			} else {
				updateExpress.set(SocialShareField.TITLE, title);
				updateExpress.set(SocialShareField.URL, url);
				updateExpress.set(SocialShareField.BODY, body);
				updateExpress.set(SocialShareField.PIC_URL, pic_url);
				updateExpress.set(SocialShareField.UPDATE_TIME_FLAG, date);
			}
			QueryExpress queryExpress = new QueryExpress();
			queryExpress.add(QueryCriterions.eq(SocialShareField.SHARE_ID, share_id));
			boolean ok = JoymeAppServiceSngl.get().modifySocialShare(queryExpress, updateExpress, appkey, platform, SocialShareType.getByCode(share_type), activityid);

			if (ok) {
				updateUpdatetimeflag(date, appkey, platform, share_type, activityid);
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return new ModelAndView("redirect:/joymeapp/socialclient/share/list");
	}


	private List<AuthApp> getAppList() {
		List<AuthApp> appList = null;
		try {
			appList = OAuthServiceSngl.get().queryAuthApp(
					new QueryExpress()
							.add(QueryCriterions.eq(AuthAppField.APPTYPE, AuthAppType.INTERNAL_CLIENT.getCode()))
							.add(QueryCriterions.in(AuthAppField.PLATOFRM, new Integer[]{AppPlatform.ANDROID.getCode(), AppPlatform.CLIENT.getCode(), AppPlatform.IOS.getCode()}))
							.add(QueryCriterions.eq(AuthAppField.VALIDSTATUS, ValidStatus.VALID.getCode()))
							.add(QueryCriterions.eq(AuthAppField.APPID, appkey))
			);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return appList;
	}

	private void updateUpdatetimeflag(Date date, String appkey, int platform, int share_type, long activityid) throws ServiceException {
		UpdateExpress updateExpress1 = new UpdateExpress();
		updateExpress1.set(SocialShareField.UPDATE_TIME_FLAG, date);
		//更新文章
		if (SocialShareType.getByCode(share_type) == SocialShareType.SOCIAL_CONTENT_TYPE) {
			QueryExpress queryExpress1 = new QueryExpress().add(QueryCriterions.eq(SocialShareField.SHARE_TYPE, SocialShareType.SOCIAL_CONTENT_TYPE.getCode()));
			JoymeAppServiceSngl.get().modifySocialShare(queryExpress1, updateExpress1, appkey, platform, SocialShareType.getByCode(share_type), activityid);
		}


		//更新活动
		if (SocialShareType.getByCode(share_type) == SocialShareType.SOCIAL_ACTIVITY_TYPE) {
			QueryExpress queryExpress1 = new QueryExpress().add(QueryCriterions.eq(SocialShareField.SHARE_TYPE, SocialShareType.SOCIAL_ACTIVITY_TYPE.getCode()));
			JoymeAppServiceSngl.get().modifySocialShare(queryExpress1, updateExpress1, appkey, platform, SocialShareType.getByCode(share_type), activityid);
		}
	}
}
