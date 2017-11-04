package com.enjoyf.webapps.tools.webpage.controller.joymeapp.mobilegame;

import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-9-9
 * Time: 下午5:18
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/joymeapp/mobilegame/virtual")
public class SocialVirtualUserController extends ToolsBaseController {

	@RequestMapping(value = "/list/{accountVirtualType}")
	public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
							 @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
							 @RequestParam(value = "screenname", required = false) String screenname,
							 @RequestParam(value = "remove_status", required = false) String remove_status,
							 @PathVariable int accountVirtualType
	) {
		Map<String, Object> mapMessage = new HashMap<String, Object>();
		int curPage = (pageStartIndex / pageSize) + 1;
		Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
		QueryExpress queryExpress = new QueryExpress();
		try {

			queryExpress.add(QueryCriterions.eq(AccountVirtualField.VIRTUAL_TYPE, accountVirtualType));
			queryExpress.add(QuerySort.add(AccountVirtualField.VIRTUAL_ID, QuerySortOrder.DESC));
			if (!StringUtil.isEmpty(screenname)) {
				queryExpress.add(QueryCriterions.like(AccountVirtualField.SCREENNAME, "%" + screenname + "%"));
			}

			if(!StringUtil.isEmpty(remove_status)){
				queryExpress.add(QueryCriterions.eq(AccountVirtualField.REMOVE_STATUS,remove_status));
			}

//			PageRows<AccountVirtual> pageRows = JoymeAppServiceSngl.get().queryAccountVirtualByPage(queryExpress, pagination);


			mapMessage.put("accountVirtualType", accountVirtualType);
//			mapMessage.put("list", pageRows.getRows());
//			mapMessage.put("page", pageRows.getPage());


		} catch (Exception e) {

		}
		return new ModelAndView("/joymeapp/mobilegame/user/virtuallist", mapMessage);
	}

	@RequestMapping(value = "/createpage/{accountVirtualType}")
	public ModelAndView createPage(@PathVariable int accountVirtualType) {
		Map<String, Object> mapMessage = new HashMap<String, Object>();
		mapMessage.put("accountVirtualType", accountVirtualType);
		return new ModelAndView("/joymeapp/mobilegame/user/createpage", mapMessage);
	}

	@RequestMapping(value = "/create/{accountVirtualType}")
	public ModelAndView create(@PathVariable int accountVirtualType,
							   @RequestParam(value = "screenname", required = false) String screenname,
							   @RequestParam(value = "picurl1", required = false) String picurl1) {
		Map<String, Object> mapMessage = new HashMap<String, Object>();
		try {
			AccountVirtual accountVirtual = new AccountVirtual();
			accountVirtual.setScreenname(screenname);

			//头像处理
			AccountVirtualHeadIcon headIcon = new AccountVirtualHeadIcon();
			headIcon.setPic(picurl1);
			String picHead = picurl1.substring(0, picurl1.lastIndexOf("."));
			String suffix = picurl1.substring(picurl1.lastIndexOf("."), picurl1.length());
			headIcon.setPic_M(picHead + "_M" + suffix);
			headIcon.setPic_S(picHead + "_S" + suffix);
			headIcon.setPic_SS(picHead + "_SS" + suffix);
			accountVirtual.setHeadicon(headIcon);

			accountVirtual.setUno(getUuid());
			Date now = new Date();
			accountVirtual.setCreate_time(now);
			accountVirtual.setAccountVirtualType(AccountVirtualType.getByCode(accountVirtualType));
			accountVirtual.setCreate_user(this.getCurrentUser().getUserid());


//			JoymeAppServiceSngl.get().createAccountVirtual(accountVirtual);
        } catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("redirect:/joymeapp/mobilegame/virtual/list/" + accountVirtualType);
	}

	@RequestMapping(value = "/modifypage/{accountVirtualType}")
	public ModelAndView modifyPage(@PathVariable int accountVirtualType
			, @RequestParam(value = "virtualId", required = false) long virtualId,
								   @RequestParam(value = "pager.offset", required = false) int pageStartIndex) {
		Map<String, Object> mapMessage = new HashMap<String, Object>();
		QueryExpress queryExpress = new QueryExpress();
		try {
			queryExpress.add(QueryCriterions.eq(AccountVirtualField.VIRTUAL_ID, virtualId));

//			AccountVirtual accountVirtual = JoymeAppServiceSngl.get().getAccountVirtual(queryExpress);

			mapMessage.put("accountVirtualType", accountVirtualType);
			mapMessage.put("pageStartIndex", pageStartIndex);
//			mapMessage.put("accountVirtual", accountVirtual);
        } catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("/joymeapp/mobilegame/user/modifypage", mapMessage);
	}

	@RequestMapping(value = "/modify/{accountVirtualType}")
	public ModelAndView modify(@PathVariable int accountVirtualType,
							   @RequestParam(value = "screenname", required = false) String screenname,
							   @RequestParam(value = "oldpicurl1", required = false) String oldpicurl1,
							   @RequestParam(value = "picurl1", required = false) String picurl1,
							   @RequestParam(value = "pager.offset", required = false) int pageStartIndex,
							   @RequestParam(value = "virtualId", required = false) Long virtualId,
							   @RequestParam(value = "del", required = false) String del) {
		Map<String, Object> mapMessage = new HashMap<String, Object>();
		QueryExpress queryExpress = new QueryExpress();
		UpdateExpress updateExpress = new UpdateExpress();
		try {

			//修改
			if (StringUtil.isEmpty(del)) {
				if (!oldpicurl1.equals(picurl1)) {
					AccountVirtualHeadIcon headIcon = new AccountVirtualHeadIcon();
					headIcon.setPic(picurl1);
					String picHead = picurl1.substring(0, picurl1.lastIndexOf("."));
					String suffix = picurl1.substring(picurl1.lastIndexOf("."), picurl1.length());
					headIcon.setPic_M(picHead + "_M" + suffix);
					headIcon.setPic_S(picHead + "_S" + suffix);
					headIcon.setPic_SS(picHead + "_SS" + suffix);
					updateExpress.set(AccountVirtualField.HEADICON, headIcon.toJson());
				}
				updateExpress.set(AccountVirtualField.SCREENNAME, screenname);
			} else {
				//删除或者恢复
				updateExpress.set(AccountVirtualField.REMOVE_STATUS, del);
			}
			queryExpress.add(QueryCriterions.eq(AccountVirtualField.VIRTUAL_ID, virtualId));
//			JoymeAppServiceSngl.get().modifyAccountVirtual(updateExpress, queryExpress);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("redirect:/joymeapp/mobilegame/virtual/list/" + accountVirtualType + "?pager.offset=" + pageStartIndex);
	}

	private String getUuid() {
		return UUID.randomUUID().toString();
	}
}
