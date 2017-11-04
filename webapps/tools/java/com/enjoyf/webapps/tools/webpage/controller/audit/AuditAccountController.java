package com.enjoyf.webapps.tools.webpage.controller.audit;

import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by IntelliJ IDEA.
 * User: yujiaxiu
 * Date: 12-2-20
 * Time: 下午9:58
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/audit/account")
public class AuditAccountController extends ToolsBaseController {
    Logger logger = LoggerFactory.getLogger(this.getClass());



    @RequestMapping(value = "/accountlist")
    public ModelAndView accountList(
            @RequestParam(value = "audit", required = false, defaultValue = "0")int audited,
            @RequestParam(value = "startdate", required = false)String startDate,
            @RequestParam(value = "enddate",required = false)String endDate,
            @RequestParam(value = "createstartdate", required = false)String createStartDate,
            @RequestParam(value = "createenddate", required = false)String createEndDate,
            @RequestParam(value = "accountuno", required = false)String accountUno,
            @RequestParam(value = "authtoken", required = false)String authToken,
            @RequestParam(value = "sorttype", required = false)String sortType,
            @RequestParam(value = "items", required = false, defaultValue = "0")int items,
            @RequestParam(value = "pager.offset", required = false, defaultValue = "0")int pagerOffset,
            @RequestParam(value = "maxPageItems", required = false, defaultValue = "1")int maxPageItems
    ){


        return new ModelAndView("/account/accountlist");
    }
}
