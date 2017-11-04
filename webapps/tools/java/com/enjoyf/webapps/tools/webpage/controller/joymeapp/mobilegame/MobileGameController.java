package com.enjoyf.webapps.tools.webpage.controller.joymeapp.mobilegame;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.content.ContentServiceSngl;
import com.enjoyf.platform.service.content.ForignContent;
import com.enjoyf.platform.service.content.ForignContentDomain;
import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.gameres.gamedb.GameDB;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.service.tools.ToolsLog;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.platform.service.joymeapp.clientline.ClientLineItemDTO;
import com.enjoyf.webapps.tools.weblogic.clientline.ClientLineWebLogic;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import com.mongodb.BasicDBObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by xupeng on 14-9-12.
 */
@Controller
@RequestMapping(value = "/mobile/game/item")
public class MobileGameController extends ToolsBaseController {

	@Resource(name = "clientLineWebLogic")
	private ClientLineWebLogic clientLineWebLogic;


	@RequestMapping(value = "/list")
	public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
							 @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
							 @RequestParam(value = "validstatus", required = false) String validStatus,
							 @RequestParam(value = "lineid", required = false) Long lineId) {
		Map<String, Object> mapMessage = new HashMap<String, Object>();

		int curPage = (pageStartIndex / pageSize) + 1;
		Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);

		if (lineId == null) {
			return new ModelAndView("redirect:/clientline/item/list");
		}
		try {
			ClientLine clientLine = JoymeAppServiceSngl.get().getClientLine(new QueryExpress().add(QueryCriterions.eq(ClientLineField.LINE_ID, lineId)));
			if (clientLine == null) {
				return new ModelAndView("redirect:/clientline/item/list");
			}
			mapMessage.put("clientLine", clientLine);

			QueryExpress queryExpress = new QueryExpress();
			queryExpress.add(QueryCriterions.eq(ClientLineItemField.LINE_ID, clientLine.getLineId()));
			if (StringUtil.isEmpty(validStatus)) {
				queryExpress.add(QueryCriterions.eq(ClientLineItemField.VALID_STATUS, ValidStatus.VALID.getCode()));
			} else {
				queryExpress.add(QueryCriterions.eq(ClientLineItemField.VALID_STATUS, validStatus));

			}
//            if (!StringUtil.isEmpty(title)) {
//                queryExpress.add(QueryCriterions.like(ClientLineItemField.TITLE, "%" + title + "%"));
//            }
			mapMessage.put("validstatus", validStatus);
			queryExpress.add(QuerySort.add(ClientLineItemField.DISPLAY_ORDER, QuerySortOrder.ASC));

			PageRows<ClientLineItemDTO> itemPageRows = clientLineWebLogic.queryClientLineItemByPage(queryExpress, pagination);
			if (itemPageRows != null) {
				mapMessage.put("list", itemPageRows.getRows());
				mapMessage.put("page", itemPageRows.getPage());
			}
		} catch (ServiceException e) {
			GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
			mapMessage = putErrorMessage(mapMessage, "system.error");
		} catch (Exception e) {
			GAlerter.lab(this.getClass().getName() + " occured Exception.e: ", e);
			mapMessage = putErrorMessage(mapMessage, "system.error");
		}
		return new ModelAndView("/joymeapp/mobilegame/mobilegamelist", mapMessage);
	}

	@RequestMapping(value = "/createpage")
	public ModelAndView createPage(@RequestParam(value = "lineid", required = false) Long lineid) {
		Map<String, Object> mapMessage = new HashMap<String, Object>();

		mapMessage.put("lineid", lineid);
		return new ModelAndView("/joymeapp/mobilegame/creategamepage", mapMessage);
	}

	@RequestMapping(value = "/create")
	public ModelAndView create(@RequestParam(value = "lineid", required = false) String lineId,
							   @RequestParam(value = "directid", required = false) String directId,
							   @RequestParam(value = "picurl", required = false) String picUrl,
							   @RequestParam(value = "itemname", required = false) String itemName) {
		Map<String, Object> mapMessage = new HashMap<String, Object>();
		Map<String, String> errorMsgMap = new HashMap<String, String>();
		ClientLineItem clientLineItem = new ClientLineItem();
		if (StringUtil.isEmpty(directId)) {
			errorMsgMap.put("paramError", "directid.is.null");
			mapMessage.put("lineid", lineId);
			return new ModelAndView("forward:/mobile/game/item/createpage", mapMessage);
		}
		try {
			GameDB gameDB = GameResourceServiceSngl.get().getGameDB(new BasicDBObject("_id", Long.parseLong(directId)), false);
			if (gameDB == null) {

				errorMsgMap.put("gameDbError", "gamedb.is.null");
				mapMessage.put("errorMsgMap", errorMsgMap);
				mapMessage.put("lineid", lineId);
				return new ModelAndView("forward:/mobile/game/item/createpage", mapMessage);
			}
			clientLineItem.setDirectId(directId);
			clientLineItem.setLineId(Integer.parseInt(lineId));
			clientLineItem.setItemDomain(ClientItemDomain.GAME);
			clientLineItem.setItemType(ClientItemType.GAMETOP);
			clientLineItem.setPicUrl(StringUtil.isEmpty(picUrl) || picUrl == "" ? gameDB.getGameIcon() : picUrl);

			clientLineItem.setTitle(StringUtil.isEmpty(itemName) || itemName == "" ? gameDB.getGameName() : itemName);
			clientLineItem.setValidStatus(ValidStatus.VALID);
			clientLineItem.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
			clientLineItem.setItemCreateDate(new Date());


			ClientLineItem returnClientItem = JoymeAppServiceSngl.get().createClientLineItem(clientLineItem);

			//创建ClientLineItem的同时，把评论对象创建
			if (returnClientItem != null) {
				ForignContent forignContent = ContentServiceSngl.get().getForignContent(returnClientItem.getDirectId() + "", "", clientLineItem.getTitle(), "", ForignContentDomain.SHORT_COMMENTS, "");
				if (forignContent != null) {
					JoymeAppServiceSngl.get().modifyClientLineItem(new UpdateExpress().set(ClientLineItemField.CONTENTID, forignContent.getContentId()), returnClientItem.getItemId());
				}
			}

		} catch (ServiceException e) {
			GAlerter.lab(this.getClass().getName() + " occured Exception.e: ", e);
			mapMessage = putErrorMessage(mapMessage, "system.error");
			mapMessage.put("lineid", lineId);
			return new ModelAndView("forward:/mobile/game/item/createpage", mapMessage);
		}

		return new ModelAndView("redirect:/mobile/game/item/list?lineid=" + lineId);
	}

	@RequestMapping(value = "/modifypage")
	public ModelAndView modifyLinePage(@RequestParam(value = "itemid", required = true) Long itemid,
									   @RequestParam(value = "itemtype", required = false) Integer itemtype) {
		Map<String, Object> mapMessage = new HashMap<String, Object>();
		mapMessage.put("itemtype", itemtype);
		try {
			ClientLineItem clientLineItem = JoymeAppServiceSngl.get().getClientLineItem(new QueryExpress().add(QueryCriterions.eq(ClientLineItemField.ITEM_ID, itemid)));
			mapMessage.put("clienLineItem", clientLineItem);

		} catch (ServiceException e) {
			GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
			mapMessage = putErrorMessage(mapMessage, "system.error");
		}
		return new ModelAndView("/joymeapp/mobilegame/modifygamepage", mapMessage);
	}

	@RequestMapping(value = "/modify")
	public ModelAndView modify(@RequestParam(value = "lineid", required = false) String lineId,
							   @RequestParam(value = "itemid", required = false) String itemId,
							   @RequestParam(value = "directid", required = false) String directId,
							   @RequestParam(value = "picurl", required = false) String picUrl,
							   @RequestParam(value = "itemname", required = false) String itemName,
							   @RequestParam(value = "order", required = false) String order) {
		Map<String, Object> mapMessage = new HashMap<String, Object>();
		Map<String, String> errorMsgMap = new HashMap<String, String>();

		try {
			GameDB gameDB = GameResourceServiceSngl.get().getGameDB(new BasicDBObject("_id", Long.parseLong(directId)), false);
			if (gameDB == null) {
				errorMsgMap.put("gameDbError", "gamedb.is.null");
				mapMessage.put("errorMsgMap", errorMsgMap);
				mapMessage.put("lineid", lineId);
				return new ModelAndView("forward:/mobile/game/item/modifypage", mapMessage);
			}
			mapMessage.put("itemid", itemId);

			UpdateExpress updateExpress = new UpdateExpress();
			updateExpress.set(ClientLineItemField.TITLE, StringUtil.isEmpty(itemName) ? gameDB.getGameName() : itemName);
			updateExpress.set(ClientLineItemField.PIC_URL, StringUtil.isEmpty(picUrl) ? gameDB.getGameIcon() : picUrl);
			updateExpress.set(ClientLineItemField.DIRECT_ID, directId);
			updateExpress.set(ClientLineItemField.DISPLAY_ORDER, Integer.parseInt(order));
			JoymeAppServiceSngl.get().modifyClientLineItem(updateExpress, Long.parseLong(itemId));

		} catch (ServiceException e) {
			GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
			mapMessage = putErrorMessage(mapMessage, "system.error");
			mapMessage.put("lineid", lineId);
			return new ModelAndView("forward:/mobile/game/item/modifypage", mapMessage);

		}

		return new ModelAndView("redirect:/mobile/game/item/list?lineid=" + lineId);
	}

	@RequestMapping(value = "/delete")
	public ModelAndView deleteLine
			(@RequestParam(value = "itemid", required = false) Long
					 itemId,
			 @RequestParam(value = "lineid", required = false) Long
					 lineId) {
		Map<String, Object> mapMessage = new HashMap<String, Object>();
		if (itemId == null) {
			return new ModelAndView("redirect:/mobile/game/item/list?lineid=" + lineId);
		}
		UpdateExpress updateExpress = new UpdateExpress();
		updateExpress.set(ClientLineItemField.VALID_STATUS, ValidStatus.REMOVED.getCode());
		try {
			boolean bool = JoymeAppServiceSngl.get().modifyClientLineItem(updateExpress, itemId);
			if (bool) {
				ToolsLog log = new ToolsLog();

				log.setOpUserId(getCurrentUser().getUserid());
				log.setOperType(LogOperType.DELETE_CLIENT_LINE_ITEM);
				log.setOpTime(new Date());
				log.setOpIp(getIp());
				log.setOpAfter("clientLineItemId:" + itemId);

				addLog(log);
			}
		} catch (Exception e) {
			GAlerter.lab(this.getClass().getName() + "ServiceException", e);
			mapMessage = putErrorMessage(mapMessage, "system.error");
		}

		return new ModelAndView("redirect:/mobile/game/item/list?lineid=" + lineId);
	}


	@RequestMapping(value = "/recover")
	public ModelAndView modifyLine
			(@RequestParam(value = "itemid", required = false) Long
					 itemId,
			 @RequestParam(value = "lineid", required = false) Long
					 lineId) {
		Map<String, Object> mapMessage = new HashMap<String, Object>();

		if (itemId == null) {
			return new ModelAndView("redirect:/mobile/game/item/list?lineid=" + lineId);
		}
		UpdateExpress updateExpress = new UpdateExpress();
		updateExpress.set(ClientLineItemField.VALID_STATUS, ValidStatus.VALID.getCode());
		try {
			boolean bool = JoymeAppServiceSngl.get().modifyClientLineItem(updateExpress, itemId);
			if (bool) {
				ToolsLog log = new ToolsLog();

				log.setOpUserId(getCurrentUser().getUserid());
				log.setOperType(LogOperType.RECOVER_CLIENT_LINE_ITEM);
				log.setOpTime(new Date());
				log.setOpIp(getIp());
				log.setOpAfter("clientLineItemId:" + itemId);

				addLog(log);
			}
		} catch (Exception e) {
			GAlerter.lab(this.getClass().getName() + "ServiceException", e);
			mapMessage = putErrorMessage(mapMessage, "system.error");
		}

		return new ModelAndView("redirect:/mobile/game/item/list?lineid=" + lineId);
	}

	@RequestMapping(value = "/sort/{sort}")
	@ResponseBody
	public String sort(@PathVariable(value = "sort") String sort,
					   @RequestParam(value = "lineid", required = false) Long lineId,
					   @RequestParam(value = "itemid", required = false) Long itemId) {
		JsonBinder binder = JsonBinder.buildNormalBinder();
		Map<String, Object> mapMessage = new HashMap<String, Object>();
		ResultObjectMsg resultObjectMsg = new ResultObjectMsg(ResultObjectMsg.CODE_S);
		if (lineId == null || itemId == null) {
			resultObjectMsg.setRs(ResultObjectMsg.CODE_E);
			return binder.toJson(resultObjectMsg);
		}
		Long returnItemId = null;
		try {
			returnItemId = ClientLineWebLogic.sort(sort, lineId, itemId);
			if (returnItemId == null) {
				resultObjectMsg.setRs(ResultObjectMsg.CODE_E);
				return binder.toJson(resultObjectMsg);
			}
		} catch (Exception e) {
			resultObjectMsg.setRs(ResultObjectMsg.CODE_E);
			resultObjectMsg.setMsg("system.error");
			return binder.toJson(resultObjectMsg);
		}
		mapMessage.put("sort", sort);
		mapMessage.put("itemid", itemId);
		mapMessage.put("returnitemid", returnItemId);
		resultObjectMsg.setResult(mapMessage);
		return binder.toJson(resultObjectMsg);
	}


}
