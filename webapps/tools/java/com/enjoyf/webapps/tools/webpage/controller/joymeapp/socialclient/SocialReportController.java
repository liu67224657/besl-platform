package com.enjoyf.webapps.tools.webpage.controller.joymeapp.socialclient;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.content.ContentServiceSngl;
import com.enjoyf.platform.service.content.social.SocialContent;
import com.enjoyf.platform.service.content.social.SocialReport;
import com.enjoyf.platform.service.content.social.SocialReportField;
import com.enjoyf.platform.service.content.social.SocialReportType;
import com.enjoyf.platform.service.profile.ProfileServiceSngl;
import com.enjoyf.platform.service.profile.socialclient.SocialProfile;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 14-4-19
 * Time: 下午8:33
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/joymeapp/socialclient/report")
public class SocialReportController extends ToolsBaseController {
	@RequestMapping(value = "/list")
	public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
							 @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
							 @RequestParam(value = "status", required = false) String status) {
		Map<String, Object> mapMessage = new HashMap<String, Object>();

		int curPage = (pageStartIndex / pageSize) + 1;
		Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
		QueryExpress queryExpress = new QueryExpress();
		if (!StringUtil.isEmpty(status)) {
			mapMessage.put("status", status);
			queryExpress.add(QueryCriterions.eq(SocialReportField.VALID_STATUS, status));
		} else {
			mapMessage.put("status", ValidStatus.INVALID.getCode());
			queryExpress.add(QueryCriterions.eq(SocialReportField.VALID_STATUS, ValidStatus.INVALID.getCode()));
		}
		try {
			PageRows<SocialReport> pageRows = ContentServiceSngl.get().querySocialReportByPage(queryExpress, pagination);
			if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
				mapMessage.put("list", pageRows.getRows());
				mapMessage.put("page", pageRows.getPage());

				List<SocialReport> list = pageRows.getRows();
				Set<String> unos = new HashSet<String>();
				Set<Long> contentIds = new HashSet<Long>();
				for(SocialReport report:list){
					unos.add(report.getUno());
					unos.add(report.getPostUno());
					contentIds.add(report.getContentId());
				}

				List<SocialContent> contentList = ContentServiceSngl.get().querySocialContentByIdSet(contentIds);
				mapMessage.put("contentList",contentList);

				Map<String, SocialProfile> mapProfile = ProfileServiceSngl.get().querySocialProfilesByUnosMap(unos);
				mapMessage.put("mapprofile", mapProfile);
			}


		} catch (ServiceException e) {
			GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
			mapMessage = putErrorMessage(mapMessage, "system.error");
			return new ModelAndView("/joymeapp/socialclient/report/reportlist", mapMessage);
		}
		return new ModelAndView("/joymeapp/socialclient/report/reportlist", mapMessage);
	}

	@RequestMapping(value = "/modify")
	public ModelAndView modify(@RequestParam(value = "reportid", required = false) String reportId,
							   @RequestParam(value = "validstatus", required = false) String validstatus) {
		Map<String, Object> mapMessage = new HashMap<String, Object>();
		try {
			if (ValidStatus.VALID.getCode().equals(validstatus)) {
				ContentServiceSngl.get().modifySocialReport(new UpdateExpress().set(SocialReportField.VALID_STATUS, ValidStatus.VALID.getCode()), new QueryExpress().add(QueryCriterions.eq(SocialReportField.REPORT_ID, Long.parseLong(reportId))));
			} else {
				SocialReport socialReport = ContentServiceSngl.get().getSocialReport(new QueryExpress().add(QueryCriterions.eq(SocialReportField.REPORT_ID, Long.parseLong(reportId))));
				if (socialReport == null) {
					return new ModelAndView("redirect:/joymeapp/socialclient/report/list");
				}
				boolean bool = false;
				if (SocialReportType.CONTENT.equals(socialReport.getReportType())) {
					bool = ContentServiceSngl.get().removeSocialContent(socialReport.getUno(), socialReport.getContentId());
				} else {
					bool = ContentServiceSngl.get().removeSocialContentReply(socialReport.getContentId(), socialReport.getReplyId());
				}
				if (bool) {
					ContentServiceSngl.get().modifySocialReport(new UpdateExpress().set(SocialReportField.VALID_STATUS, ValidStatus.REMOVED.getCode()), new QueryExpress().add(QueryCriterions.eq(SocialReportField.REPORT_ID, Long.parseLong(reportId))));
				}
			}
		} catch (ServiceException e) {
			GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
			mapMessage = putErrorMessage(mapMessage, "system.error");
			return new ModelAndView("/joymeapp/socialclient/report/reportlist", mapMessage);
		}


		return new ModelAndView("redirect:/joymeapp/socialclient/report/list");
	}
}
