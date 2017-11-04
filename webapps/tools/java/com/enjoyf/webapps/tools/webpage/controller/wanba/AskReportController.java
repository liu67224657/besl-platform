package com.enjoyf.webapps.tools.webpage.controller.wanba;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.ask.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by zhimingli on 2016/12/7 0007.
 * 举报后台
 */

@Controller
@RequestMapping("/wanba/report")
public class AskReportController {

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                             @RequestParam(value = "validStatus", required = false) String validStatus,
                             @RequestParam(value = "title", required = false) String title,
                             HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
        QueryExpress queryExpress = new QueryExpress();
        try {
            queryExpress.add(QuerySort.add(AskReportField.CREATETIME, QuerySortOrder.DESC));
            if (!StringUtil.isEmpty(title)) {
                queryExpress.add(QueryCriterions.like(AskReportField.EXTSTR, "%" + title + "%"));
            }
            if (!StringUtil.isEmpty(validStatus)) {
                queryExpress.add(QueryCriterions.eq(AskReportField.VALIDSTATUS, ValidStatus.getByCode(validStatus).getCode()));
            }
            PageRows<AskReport> askReport = AskServiceSngl.get().queryAskReport(queryExpress, pagination);

            if (askReport != null && !CollectionUtil.isEmpty(askReport.getRows())) {
                List<AskReport> askReportList = askReport.getRows();

                Set<Long> qidSet = new HashSet<Long>();
                Set<Long> aidSet = new HashSet<Long>();
                for (AskReport report : askReportList) {
                    if (report.getItemType().equals(ItemType.QUESTION)) {
                        qidSet.add(Long.valueOf(report.getDestId()));
                    } else if (report.getItemType().equals(ItemType.ANSWER)) {
                        aidSet.add(Long.valueOf(report.getDestId()));
                    }
                }

                mapMessage.put("qMap", AskServiceSngl.get().queryQuestionByIds(qidSet));
                mapMessage.put("aMap", AskServiceSngl.get().queryAnswerByAnswerIds(aidSet));
            }
            mapMessage.put("validStatus", validStatus);
            mapMessage.put("title", title);
            mapMessage.put("list", askReport.getRows());
            mapMessage.put("page", askReport.getPage());
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }
        return new ModelAndView("/wanba/report/reportlist", mapMessage);
    }


    @ResponseBody
    @RequestMapping(value = "/modify")
    public String modify(@RequestParam(value = "reportId", required = false) String reportId,
                         @RequestParam(value = "validStatus", required = false) String validStatus,
                         @RequestParam(value = "type", required = false) String type,
                         @RequestParam(value = "destId", required = false) String destId,
                         HttpServletRequest request) {

        UpdateExpress updateExpress = new UpdateExpress();
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            updateExpress.set(AskReportField.VALIDSTATUS, ValidStatus.getByCode(validStatus).getCode());
            boolean val = AskServiceSngl.get().modifyAskReport(updateExpress, reportId);

            //删除操作，要同时问题或者答案
            if (val && validStatus.equals(ValidStatus.REMOVED.getCode())) {
                if (type.equals("1")) {
                    AskServiceSngl.get().deleteQuestion(Long.valueOf(destId));
                } else {
                    Answer answer = AskServiceSngl.get().getAnswer(Long.valueOf(destId));
                    if (answer != null) {
                        AskServiceSngl.get().deleteAnswer(answer.getAnswerId());
                    }
                }
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }
        return ResultCodeConstants.SUCCESS.getJsonString();
    }
}
