package com.enjoyf.webapps.tools.webpage.controller.joymeapp.socialclient;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.message.MessageServiceSngl;
import com.enjoyf.platform.service.message.SocialMessageType;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.AuthAppField;
import com.enjoyf.platform.service.oauth.AuthAppType;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.service.tools.ToolsLog;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.webapps.tools.weblogic.dto.joymeApp.PushMessageDTO;
import com.enjoyf.webapps.tools.weblogic.joymeapp.JoymeAppWebLogic;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 14-6-11
 * Time: 上午9:43
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/joymeapp/social/pushmessage")
public class SocialPushMessageController extends ToolsBaseController {


	@Resource(name = "jomyeAppWebLogic")
	private JoymeAppWebLogic joymeAppWebLogic;

	@RequestMapping(value = "/list")
	public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,      //数据库记录索引
							 @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize) {

		Map<String, Object> mapMessage = new HashMap<String, Object>();

		int curPage = pageStartIndex / pageSize + 1;

		Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
		QueryExpress queryExpress = new QueryExpress()
				.add(QueryCriterions.eq(PushMessageField.PUSHLISTTYPE, PushListType.PUSH_MESSAGE.getCode()))
				.add(QueryCriterions.ne(PushMessageField.PUSHSTATUS, ActStatus.ACTED.getCode()))
				.add(QuerySort.add(PushMessageField.CREATEDATE, QuerySortOrder.DESC));
		try {

			PageRows<PushMessageDTO> pageRows = joymeAppWebLogic.queryPushMessageDTO(queryExpress, pagination);

			mapMessage.put("list", pageRows.getRows());
			mapMessage.put("page", pageRows.getPage());
		} catch (ServiceException e) {
			GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
			mapMessage = putErrorMessage(mapMessage, "system.error");
		}
		return new ModelAndView("/joymeapp/socialclient/pushmessage/pushmessagelist", mapMessage);
	}


	//
	@RequestMapping(value = "/createpage")
	public ModelAndView createPushPage() {
		Map<String, Object> mapMessage = new HashMap<String, Object>();
		mapMessage.put("typeCollection", getSocilaMessageType());
		try {
			List<AuthApp> appList = OAuthServiceSngl.get().queryAuthApp(
					new QueryExpress()
							.add(QueryCriterions.eq(AuthAppField.APPTYPE, AuthAppType.INTERNAL_CLIENT.getCode()))
							.add(QueryCriterions.in(AuthAppField.PLATOFRM, new Integer[]{AppPlatform.ANDROID.getCode(), AppPlatform.CLIENT.getCode(), AppPlatform.IOS.getCode()}))
							.add(QueryCriterions.eq(AuthAppField.VALIDSTATUS, ValidStatus.VALID.getCode()))
							.add(QueryCriterions.eq(AuthAppField.APPNAME, "社交端"))
			);

			List<AppPlatform> platformList = new ArrayList<AppPlatform>();
			platformList.add(AppPlatform.IOS);
			platformList.add(AppPlatform.ANDROID);

			mapMessage.put("platformList", platformList);
			mapMessage.put("appList", appList);
		} catch (ServiceException e) {
			GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
		}

		return new ModelAndView("/joymeapp/socialclient/pushmessage/pushmessagecreate", mapMessage);
	}

	@RequestMapping(value = "/create")
	public ModelAndView createMessage(@RequestParam(value = "icon") String icon,
									  @RequestParam(value = "subject", required = true) String subject,
									  @RequestParam(value = "shortmessage") String shortMessage,
									  @RequestParam(value = "appkey", required = true) String appkey,
									  @RequestParam(value = "url", required = true) String url,
									  @RequestParam(value = "platform", required = true) Integer platform,
									  @RequestParam(value = "type", required = false) Integer redirectType) {
		Map<String, Object> mapMessage = new HashMap<String, Object>();

		try {
			AuthApp app = OAuthServiceSngl.get().getApp(appkey);

			if (app == null || platform == null) {
				mapMessage.put("platformError", "app.platform.empty");
				return new ModelAndView("forward:/joymeapp/social/pushmessage/createpage", mapMessage);
			}
			AppPlatform appPlatform = AppPlatform.getByCode(platform);
			if (appPlatform == null) {
				mapMessage.put("platformError", "app.platform.empty");
				return new ModelAndView("forward:/joymeapp/social/pushmessage/createpage", mapMessage);
			}

			PushMessage pushMessage = new PushMessage();

			pushMessage.setAppKey(appkey);
			pushMessage.setAppPlatform(appPlatform);
			pushMessage.setCreateDate(new Date());
			pushMessage.setCreateUserid(getCurrentUser().getUserid());
			pushMessage.setMsgIcon(icon);
			pushMessage.setMsgSubject(subject);
			pushMessage.setPushStatus(ActStatus.UNACT);
			pushMessage.setShortMessage(shortMessage);
			pushMessage.setPushListType(PushListType.PUSH_MESSAGE);

			PushMessageOptions options = new PushMessageOptions();

			PushMessageOption pushMessageOption = new PushMessageOption();
			pushMessageOption.setInfo(url);
			pushMessageOption.setType(redirectType);

			options.getList().add(pushMessageOption);
			options.setTemplate(0);
			pushMessage.setOptions(options);

            MessageServiceSngl.get().ceatePushMessage(pushMessage);

			//addLog
			ToolsLog log = new ToolsLog();

			log.setOpUserId(getCurrentUser().getUserid());
			log.setOperType(LogOperType.CREATE_JOYMEAPP_PUSHMESSAGE);
			log.setOpTime(new Date());
			log.setOpIp(getIp());
			log.setOpAfter("create push message:" + pushMessage);

			addLog(log);

		} catch (ServiceException e) {
			GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
			mapMessage.put("iconError", "error.exception");
			return new ModelAndView("forward:/joymeapp/social/pushmessage/createpage", mapMessage);
		}


		return new ModelAndView("redirect:/joymeapp/social/pushmessage/list");
	}

	@RequestMapping(value = "/send")
	public ModelAndView sendMessage(@RequestParam(value = "msgid", required = true) Long msgId) {
		Map<String, Object> mapMessage = new HashMap<String, Object>();
		try {
			PushMessage pushMessage = MessageServiceSngl.get().getPushMessage(new QueryExpress().add(QueryCriterions.eq(PushMessageField.PUSHMSGID, msgId)));
			if (pushMessage == null) {
				mapMessage.put("errorMsg", "joymeapp.pushmessage.exists");
				new ModelAndView("forward:/joymeapp/social/pushmessage/list", mapMessage);
			}

			joymeAppWebLogic.sendKaDaMessage(pushMessage);

			ToolsLog log = new ToolsLog();

			log.setOpUserId(getCurrentUser().getUserid());
			log.setOperType(LogOperType.SEND_JOYMEAPP_PUSHMESSAGE);
			log.setOpTime(new Date());
			log.setOpIp(getIp());
			log.setOpAfter("create push message:" + pushMessage);

			addLog(log);
		} catch (ServiceException e) {
			GAlerter.lab(this.getClass().getName() + "occured ServiceExcpetion.e:", e);
			mapMessage.put("errorMsg", "error.exception");
			new ModelAndView("forward:/joymeapp/social/pushmessage/list", mapMessage);
		}


		return new ModelAndView("forward:/joymeapp/social/pushmessage/list", mapMessage);
	}

	//
	@RequestMapping(value = "/modifypage")
	public ModelAndView modifyPage(@RequestParam(value = "msgid", required = true) Long pushMsgId) {
		Map<String, Object> mapMessage = new HashMap<String, Object>();
		mapMessage.put("typeCollection", getSocilaMessageType());
		try {
			PushMessageDTO pushMessageDTO = joymeAppWebLogic.getPushMessageDTO(new QueryExpress().add(QueryCriterions.eq(PushMessageField.PUSHMSGID, pushMsgId)));

			mapMessage.put("pushMessage", pushMessageDTO);
		} catch (ServiceException e) {
			GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
			mapMessage.put("errorMsg", "error.exception");
			new ModelAndView("forward:/joymeapp/social/pushmessage/list", mapMessage);
		}

		return new ModelAndView("/joymeapp/socialclient/pushmessage/pushmessagemodify", mapMessage);
	}

	@RequestMapping(value = "/modify")
	public ModelAndView modify(@RequestParam(value = "icon") String icon,
							   @RequestParam(value = "subject", required = true) String subject,
							   @RequestParam(value = "shortmessage") String shortMessage,
							   @RequestParam(value = "msgid", required = true) Long msgId,
							   @RequestParam(value = "url", required = true) String url,
							   @RequestParam(value = "appkey", required = true) String appKey,
							   @RequestParam(value = "type", required = false) Integer redirectType) {
		Map<String, Object> mapMessage = new HashMap<String, Object>();

		try {
			PushMessageOptions options = new PushMessageOptions();
			PushMessageOption pushMessageOption = new PushMessageOption();
			pushMessageOption.setInfo(url);
			pushMessageOption.setType(redirectType);
			options.getList().add(pushMessageOption);
			options.setTemplate(0);

			UpdateExpress updateExpress = new UpdateExpress().set(PushMessageField.MSGICON, icon)
					.set(PushMessageField.SHORTMESSAGE, shortMessage)
					.set(PushMessageField.MSGSUBJECT, subject)
					.set(PushMessageField.OPTIONS, options.toJson());

			boolean bVal = MessageServiceSngl.get().modifyPushMessage(updateExpress, new QueryExpress().add(QueryCriterions.eq(PushMessageField.PUSHMSGID, msgId)), appKey);

			if (bVal) {
				ToolsLog log = new ToolsLog();

				log.setOpUserId(getCurrentUser().getUserid());
				log.setOperType(LogOperType.MODIFY_JOYMEAPP_PUSHMESSAGE);
				log.setOpTime(new Date());
				log.setOpIp(getIp());
				log.setOpAfter("modify push msgId:" + msgId);

				addLog(log);
			}
		} catch (ServiceException e) {
			GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
			mapMessage.put("iconError", "error.exception");
			return new ModelAndView("forward:/joymeapp/social/pushmessage/createpage", mapMessage);
		}

		return new ModelAndView("redirect:/joymeapp/social/pushmessage/list");
	}

	//
	@RequestMapping(value = "/delete")
	public ModelAndView deleteMessage(@RequestParam(value = "pushmsgid", required = true) Long pushMsgId,
									  @RequestParam(value = "appkey", required = true) String appKey) {
		UpdateExpress updateExpress = new UpdateExpress();
		updateExpress.set(PushMessageField.PUSHSTATUS, ActStatus.ACTED.getCode());

		try {
			boolean bVal = MessageServiceSngl.get().modifyPushMessage(updateExpress, new QueryExpress().add(QueryCriterions.eq(PushMessageField.PUSHMSGID, pushMsgId)), appKey);

			if (bVal) {
				ToolsLog log = new ToolsLog();

				log.setOpUserId(getCurrentUser().getUserid());
				log.setOperType(LogOperType.DELETE_JOYMEAPP_PUSHMESSAGE);
				log.setOpTime(new Date());
				log.setOpIp(getIp());
				log.setOpAfter("modify push msgId:" + pushMsgId);

				addLog(log);
			}
		} catch (ServiceException e) {
			GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
		}

		return new ModelAndView("redirect:/joymeapp/social/pushmessage/list");
	}

	private Collection<SocialMessageType> getSocilaMessageType() {
		Map<Integer, SocialMessageType> map = new HashMap<Integer, SocialMessageType>();
		map.put(4, SocialMessageType.OPEN_CONETNT);
		map.put(5, SocialMessageType.OPEN_ACTIVITY);
		map.put(6, SocialMessageType.OPEN_WAP_HTML);
	//	map.put(7, SocialMessageType.OPEN_KAD);
		return map.values();
	}
}
