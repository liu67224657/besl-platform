package com.enjoyf.webapps.tools.webpage.controller.customerservice;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.misc.IpForbidden;
import com.enjoyf.platform.service.misc.IpForbiddenField;
import com.enjoyf.platform.service.misc.MiscServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.service.tools.ToolsConstants;
import com.enjoyf.platform.service.tools.ToolsLog;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.IpUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.webapps.tools.weblogic.customerservice.IpManagementWebLogic;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: yujiaxiu
 * Date: 12-4-10
 * Time: 下午1:12
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/cs/ipmanage")
public class IpManagementController extends ToolsBaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource(name = "ipManagementWebLogic")
    private IpManagementWebLogic ipManagementWebLogic;

    @RequestMapping(value = "/iplist")
    public ModelAndView queryIpList(@RequestParam(value = "ip", required = false)String ip,
                                    @RequestParam(value = "createdate", required = false)String createDate,
                                    @RequestParam(value = "validstatus", required = false)String validStatus,
                                    @RequestParam(value = "items", required = false, defaultValue = "0") int items,
                                    @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pagerOffset,
                                    @RequestParam(value = "maxPageItems", required = false, defaultValue = "1") int maxPageItems){

        Pagination pagination = new Pagination(items, (pagerOffset / maxPageItems) + 1, WebappConfig.get().getHomePageSize());
        Map<String, Object> msgMap = new HashMap<String, Object>();

        IpForbidden ipForbidden = new IpForbidden();

        if(!StringUtil.isEmpty(ip)){

            try {
                ipForbidden.setStartIP(IpUtil.cvtToLong(ip));
            } catch (UnknownHostException e) {
                GAlerter.lab("convert ip from String to long occurred an Exception: " + e);
            }

        }
        if(!StringUtil.isEmpty(createDate)){

            ipForbidden.setCreateDate(DateUtil.StringTodate(createDate, DateUtil.DATE_FORMAT));

        }
        if(!StringUtil.isEmpty(validStatus)){
            ipForbidden.setStatus(ValidStatus.getByCode(validStatus));
        }


        PageRows<IpForbidden> pageRows = ipManagementWebLogic.queryIpList(ipForbidden, pagination);

        msgMap.put("rows", pageRows.getRows());
        msgMap.put("page", pageRows.getPage());
        msgMap.put("ip", ip);
        msgMap.put("createdate", createDate);
        msgMap.put("validstatus", validStatus);

        return new ModelAndView("/ipmanage/iplist", msgMap);

    }


    @RequestMapping(value = "/preincreaseip")
    public ModelAndView preIncreaseIpForbidden() {
        return new ModelAndView("/ipmanage/createippage");
    }

    @RequestMapping(value = "/increaseip")
    public ModelAndView increaseIpForbidden(@RequestParam(value = "startip", required = false)String startIp,
                                            @RequestParam(value = "endip", required = false)String endIp,
                                            @RequestParam(value = "desc", required = false)String desc,
                                            @RequestParam(value = "num", required = false, defaultValue = "0")int num,
                                            @RequestParam(value = "datetype", required = false)String dateType){

        Map<String, Object> msgMap = new HashMap<String, Object>();
        Map<String, String> errorMsgMap = new HashMap<String, String>();
        IpForbidden ipForbidden = new IpForbidden();

        //fill the entry
        if(StringUtil.isEmpty(startIp)){
            errorMsgMap.put("startip", "error.ipmanagement.startip.null");
        }else {
            try {
                ipForbidden.setStartIP(IpUtil.cvtToLong(startIp));
            } catch (UnknownHostException e) {
                errorMsgMap.put("startip", "error.ipmanagement.startip.converterror");
                GAlerter.lab("convert ip from String to long occurred an Exception: " + e);
            }
        }

        if(StringUtil.isEmpty(endIp)){
            errorMsgMap.put("endip", "error.ipmanagement.endip.null");
        }else {
            try {
                ipForbidden.setEndIp(IpUtil.cvtToLong(endIp));
            } catch (UnknownHostException e) {
                errorMsgMap.put("endip", "error.ipmanagement.endip.converterror");
                GAlerter.lab("convert ip from String to long occurred an Exception: " + e);
            }
        }

        if(num <= 0){
            errorMsgMap.put("num", "error.ipmanagement.num.null");
        }else {
            if("day".equals(dateType)){
                ipForbidden.setUtillDate(DateUtil.adjustDate(new Date(), Calendar.DAY_OF_MONTH, num));
            }else if("week".equals(dateType)){
                ipForbidden.setUtillDate(DateUtil.adjustDate(new Date(), Calendar.WEEK_OF_MONTH, num));
            }else if("month".equals(dateType)){
                ipForbidden.setUtillDate(DateUtil.adjustDate(new Date(), Calendar.MONTH, num));
            }else {
                ipForbidden.setUtillDate(DateUtil.adjustDate(new Date(), Calendar.YEAR, num));
            }
        }
        if(!StringUtil.isEmpty(desc)){
            ipForbidden.setDescription(desc);
        }
        ipForbidden.setCreateUserid(getCurrentUser().getUserid());


        //
        if(!errorMsgMap.isEmpty()){
            msgMap.put("startip", startIp);
            msgMap.put("endip", endIp);
            msgMap.put("desc", desc);
            msgMap.put("num", num);
            msgMap.put("datetype", dateType);
            msgMap.put("errorMsgMap", errorMsgMap);
            return new ModelAndView("/ipmanage/createippage", msgMap);
        }

        try {
            MiscServiceSngl.get().createIpForbidden(ipForbidden);
        } catch (ServiceException e) {
            GAlerter.lab("An Exception occurred when create a IpForbidden : " + e);

            errorMsgMap.put("system", "error.exception");
            msgMap.put("errorMsgMap", errorMsgMap);
            return new ModelAndView("/ipmanage/createippage", msgMap);
        }

        // log
        ToolsLog log = new ToolsLog();

        log.setOpUserId(getCurrentUser().getUserid());
        log.setOperType(LogOperType.IPMANAGEMENT_INCREASEIPFORBIDDEN);
        log.setOpTime(new Date());
        log.setOpIp(getIp());
        log.setOpAfter(StringUtil.truncate(ipForbidden.toString(), ToolsConstants.SPLIT_SIZE));

        addLog(log);

        return new ModelAndView("redirect:/cs/ipmanage/iplist");

    }

    @RequestMapping(value = "/batchupdate")
    public ModelAndView batchUpdate(@RequestParam(value = "ipids", required = false)String ipids,
                                    @RequestParam(value = "updateRemoveStatusCode", required = false)String updateRemoveStatusCode,
                                    @RequestParam(value = "ip", required = false)String ip,
                                    @RequestParam(value = "createdate", required = false)String createDate,
                                    @RequestParam(value = "validstatus", required = false)String validStatus,
                                    @RequestParam(value = "items", required = false, defaultValue = "0") int items,
                                    @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pagerOffset,
                                    @RequestParam(value = "maxPageItems", required = false, defaultValue = "1") int maxPageItems) {

        Pagination pagination = new Pagination(items, (pagerOffset / maxPageItems) + 1, WebappConfig.get().getHomePageSize());

        Map<String, Object> msgMap = new HashMap<String, Object>();
        msgMap.put("ip", ip);
        msgMap.put("createdate", createDate);
        msgMap.put("validstatus", validStatus);
        msgMap.put("page", pagination);

        UpdateExpress updateExpress = new UpdateExpress();

        if (ValidStatus.getByCode(updateRemoveStatusCode) != null) {
            updateExpress.set(IpForbiddenField.VALIDSTATUS, ValidStatus.getByCode(updateRemoveStatusCode).getCode());
        }

        //
        try {
            if (!Strings.isNullOrEmpty(ipids) && ValidStatus.getByCode(updateRemoveStatusCode) != null) {
                //
                String[] ipIdSplits = ipids.split(",");
                for (String ipId : ipIdSplits) {
                    //
                    QueryExpress queryExpress = new QueryExpress();
                    queryExpress.add(QueryCriterions.eq(IpForbiddenField.IPID, Long.valueOf(ipId)));

                    MiscServiceSngl.get().modifyIpForbidden(queryExpress, updateExpress);

                    // log
                    ToolsLog log = new ToolsLog();

                    log.setOpUserId(getCurrentUser().getUserid());
                    log.setOperType(LogOperType.IPMANAGEMENT_BATCHUPDATE);
                    log.setOpTime(new Date());
                    log.setOpIp(getIp());
                    log.setOpAfter(StringUtil.truncate("VALIDSTATUS:" + ValidStatus.getByCode(updateRemoveStatusCode).getCode(),
                            ToolsConstants.SPLIT_SIZE));

                    addLog(log);
                }
            }
        } catch (ServiceException e) {
            GAlerter.lab("batchValidStatus, ServiceException :", e);
        }

        return new ModelAndView("forward:/cs/ipmanage/iplist", msgMap);
    }

}
