package com.enjoyf.webapps.tools.webpage.controller.joymeapp;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.joymeappconfig.JoymeAppConfigServiceSngl;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.AuthAppField;
import com.enjoyf.platform.service.oauth.AuthAppType;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-8-14
 * Time: 下午2:02
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/joymeapp/topnews")
public class AppTopNewsController extends ToolsBaseController {

	@RequestMapping(value = "/list")
	public ModelAndView queryTopMenuList(@RequestParam(value = "appkey", required = false) String appKey,
										 @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
										 @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize) {
		Map<String, Object> mapMessage = new HashMap<String, Object>();
		try {
			//得到存在的app列表
			List<AuthApp> appList = getAppList();

			mapMessage.put("applist", appList);

			//不传aqpkey 不用查询 菜单直接返回
			if (StringUtil.isEmpty(appKey)) {
				return new ModelAndView("/joymeapp/joymeapptopnewslist", mapMessage);
			}

			mapMessage.put("appkey", appKey);

			int curPage = (pageStartIndex / pageSize) + 1;
			Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);

			PageRows<JoymeAppTopNews> joymeAppTopNewsPageRows = JoymeAppConfigServiceSngl.get().queryJoymeAppTopNewsByPage(new QueryExpress()
					.add(QueryCriterions.eq(JoymeAppTopNewsField.APPKEY, appKey))
					.add(QuerySort.add(JoymeAppTopNewsField.MODIFYDATE, QuerySortOrder.DESC)), pagination, appKey);


			mapMessage.put("list", joymeAppTopNewsPageRows.getRows());
			mapMessage.put("page", joymeAppTopNewsPageRows.getPage());
		} catch (ServiceException e) {
			GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
			mapMessage.put("errorMsg", "system.error");
		}

		return new ModelAndView("/joymeapp/joymeapptopnewslist", mapMessage);
	}


	@RequestMapping(value = "/createtopnewspage")
	public ModelAndView createTopMenuPage(@RequestParam(value = "appkey", required = false) String appkey) {
		Map<String, Object> mapMessage = new HashMap<String, Object>();
		mapMessage.put("appkey", appkey);
		try {
			//得到存在的app列表
			List<AuthApp> appList = getAppList();
			for (AuthApp authApp : appList) {
				if (appkey.equals(authApp.getAppId())) {
					mapMessage.put("appname", authApp.getAppName());
					break;
				}
			}
		} catch (ServiceException e) {
			GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
			mapMessage.put("errorMsg", "system.error");
			return new ModelAndView("/joymeapp/joymeapptopnewslist", mapMessage);
		}
		return new ModelAndView("/joymeapp/createtopnews", mapMessage);
	}

	@RequestMapping(value = "/createtopnews")
	public ModelAndView createTopMenuPage(@RequestParam(value = "appkey", required = false) String appkey,
										  @RequestParam(value = "title", required = false) String title,
										  @RequestParam(value = "url", required = false) String url) {
		Map<String, Object> mapMessage = new HashMap<String, Object>();
		mapMessage.put("appkey", appkey);
		try {
			JoymeAppTopNews joymeAppTopNews = new JoymeAppTopNews();
			joymeAppTopNews.setAppkey(appkey);
			joymeAppTopNews.setTitle(title);
			if (!StringUtil.isEmpty(url)) {
				joymeAppTopNews.setUrl(url);
			}

			joymeAppTopNews.setCreate_userid(getCurrentUser().getUserid());
			joymeAppTopNews.setCreatedate(new Date());
			joymeAppTopNews.setModifydate(new Date());
			JoymeAppConfigServiceSngl.get().createJoymeAppTopNews(joymeAppTopNews);
		} catch (Exception e) {
			GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
			mapMessage.put("errorMsg", "system.error");
			return new ModelAndView("/joymeapp/joymeapptopnewslist", mapMessage);
		}
		return new ModelAndView("redirect:/joymeapp/topnews/list?appkey=" + appkey);
	}


	@RequestMapping(value = "/modifypage")
	public ModelAndView modifypage(@RequestParam(value = "newsid", required = false) Long newsid,
								   @RequestParam(value = "appkey", required = false) String appkey) {
		Map<String, Object> mapMessage = null;
		try {
			mapMessage = new HashMap<String, Object>();
			QueryExpress queryExpress = new QueryExpress();
			queryExpress.add(QueryCriterions.eq(JoymeAppTopNewsField.TOP_NEWS_ID, newsid));
			JoymeAppTopNews joymeAppTopNews = JoymeAppConfigServiceSngl.get().getJoymeAppTopNews(queryExpress);
			mapMessage.put("joymeAppTopNews", joymeAppTopNews);
			mapMessage.put("appkey", appkey);

			//得到存在的app列表
			List<AuthApp> appList = getAppList();
			for (AuthApp authApp : appList) {
				if (appkey.equals(authApp.getAppId())) {
					mapMessage.put("appname", authApp.getAppName());
					break;
				}
			}

		} catch (ServiceException e) {
			GAlerter.lab(this.getClass().getName() + "ServoceException", e);
		}
		return new ModelAndView("joymeapp/modifytopnews", mapMessage);
	}

	@RequestMapping(value = "/modify")
	public ModelAndView modify(@RequestParam(value = "newsid", required = false) Long newsid,
							   @RequestParam(value = "appkey", required = false) String appkey,
							   @RequestParam(value = "title", required = false) String title,
							   @RequestParam(value = "url", required = false) String url) {
		Map<String, Object> mapMessage = null;
		try {
			QueryExpress queryExpress = new QueryExpress();
			queryExpress.add(QueryCriterions.eq(JoymeAppTopNewsField.TOP_NEWS_ID, newsid));


			UpdateExpress updateExpresse = new UpdateExpress();

			updateExpresse.set(JoymeAppTopNewsField.TITLE, title);
			updateExpresse.set(JoymeAppTopNewsField.URL, url);
			updateExpresse.set(JoymeAppTopNewsField.MODIFYDATE, new Date());

			JoymeAppConfigServiceSngl.get().modifyJoymeAppTopNews(queryExpress, updateExpresse, appkey);
		} catch (ServiceException e) {
			GAlerter.lab(this.getClass().getName() + "ServoceException", e);
		}
		return new ModelAndView("redirect:/joymeapp/topnews/list?appkey=" + appkey);
	}

	@RequestMapping(value = "/delete")
	public ModelAndView delete(@RequestParam(value = "newsid", required = false) Long newsid,
							   @RequestParam(value = "appkey", required = false) String appkey,
							   @RequestParam(value = "removestatus", required = false) String removestatus) {
		Map<String, Object> mapMessage = null;
		try {
			QueryExpress queryExpress = new QueryExpress();
			queryExpress.add(QueryCriterions.eq(JoymeAppTopNewsField.TOP_NEWS_ID, newsid));
			UpdateExpress updateExpresse = new UpdateExpress();
			if (removestatus.equals("n")) {
				updateExpresse.set(JoymeAppTopNewsField.REMOVESTATUS, ActStatus.UNACT.getCode());
			} else {
				updateExpresse.set(JoymeAppTopNewsField.REMOVESTATUS, ActStatus.ACTED.getCode());
			}

			JoymeAppConfigServiceSngl.get().modifyJoymeAppTopNews(queryExpress, updateExpresse, appkey);

		} catch (ServiceException e) {
			GAlerter.lab(this.getClass().getName() + "ServoceException", e);
		}
		return new ModelAndView("redirect:/joymeapp/topnews/list?appkey=" + appkey);
	}

	private List<AuthApp> getAppList() throws ServiceException {
		List<AuthApp> appList = OAuthServiceSngl.get().queryAuthApp(
				new QueryExpress()
						.add(QueryCriterions.eq(AuthAppField.APPTYPE, AuthAppType.INTERNAL_CLIENT.getCode()))
						.add(QueryCriterions.in(AuthAppField.PLATOFRM, new Integer[]{AppPlatform.ANDROID.getCode(), AppPlatform.CLIENT.getCode(), AppPlatform.IOS.getCode()}))
						.add(QueryCriterions.eq(AuthAppField.VALIDSTATUS, ValidStatus.VALID.getCode()))
		);
		return appList;
	}

}
