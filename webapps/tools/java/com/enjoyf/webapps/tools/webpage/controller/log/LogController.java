package com.enjoyf.webapps.tools.webpage.controller.log;

import com.enjoyf.platform.crypto.MD5Util;
import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.*;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.webapps.tools.weblogic.log.LogWebLogic;
import com.enjoyf.webapps.tools.weblogic.privilege.CacheUtil;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import com.enjoyf.webapps.tools.webpage.controller.home.LoginController;
import com.google.common.base.Strings;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * User: taijunli
 * Date: 11-12-7
 * Time: 上午9:37
 */
@Controller
@RequestMapping(value = "/log")
public class LogController extends ToolsBaseController {

    @Resource(name = "logWebLogic")
    private LogWebLogic logWebLogic;

    private int pageNum = 100;


    @RequestMapping(value = "/preloglist")
    public ModelAndView preQueryLogList() {
        Map<String, Object> mapMsg = new HashMap<String, Object>();
        ToolsLog entity = new ToolsLog();

        entity.setOpTime(new Date());
        Pagination pagination = new Pagination(pageNum, 1, WebappConfig.get().getHomePageSize());

        try {
            PageRows<ToolsLog> pageRows = logWebLogic.queryLogs(entity, pagination);

            mapMsg.put("rows", pageRows.getRows());
            mapMsg.put("page", pageRows.getPage());
        } catch (ServiceException e) {
            GAlerter.lab("preQueryLogList a Controller ServiceException :", e);
        }

        return new ModelAndView("/log/loglist", mapMsg);
    }

    @RequestMapping(value = "/preloginloglist")
    public ModelAndView preQueryLoginLogList() {
        Map<String, Object> mapMsg = new HashMap<String, Object>();
        ToolsLoginLog entity = new ToolsLoginLog();

        Pagination pagination = new Pagination(pageNum, 1, WebappConfig.get().getHomePageSize());

        try {
            PageRows<ToolsLoginLog> pageRows = logWebLogic.queryLoginLogs(entity, pagination);

            mapMsg.put("rows", pageRows.getRows());
            mapMsg.put("page", pageRows.getPage());
        } catch (ServiceException e) {
            GAlerter.lab("preQueryLoginLogList a Controller ServiceException :", e);
        }

        return new ModelAndView("/log/loginloglist", mapMsg);
    }

    @RequestMapping(value = "/loglist")
    public ModelAndView queryLogList(
            @RequestParam(value = "opUserId", required = false) String opUserId,
            @RequestParam(value = "startTime", required = false) String startTime,
            @RequestParam(value = "endTime", required = false) String endTime,
            @RequestParam(value = "opafter", required = false) String opAfter,
            @RequestParam(value = "items", required = false, defaultValue = "0") int items,                   //总记录数
            @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pagerOffset,      //数据库记录索引
            @RequestParam(value = "maxPageItems", required = false, defaultValue = "1") int maxPageItems) {   //每页记录数

        Pagination pagination = new Pagination(items, (pagerOffset / maxPageItems) + 1, WebappConfig.get().getHomePageSize());
        Map<String, Object> mapMsg = new HashMap<String, Object>();

        ToolsLog entity = new ToolsLog();
        entity.setOpTime(new Date());

        if (!Strings.isNullOrEmpty(opUserId)) {
            entity.setOpUserId(opUserId);
        }
        if (!Strings.isNullOrEmpty(startTime)) {
            entity.setStartTime(DateUtil.StringTodate(startTime, DateUtil.PATTERN_DATE));
        }
        if (!Strings.isNullOrEmpty(endTime)) {
            entity.setEndTime(DateUtil.StringTodate((endTime + ToolsLog.END_TIME_PRIFIX), DateUtil.PATTERN_DATE_TIME));
        }
        if (!StringUtil.isEmpty(opAfter)) {
            entity.setOpAfter(opAfter);
        }

        try {
            PageRows<ToolsLog> pageRows = logWebLogic.queryLogs(entity, pagination);

            mapMsg.put("rows", pageRows.getRows());
            mapMsg.put("page", pageRows.getPage());
            mapMsg.put("entity", entity);
        } catch (ServiceException e) {
            GAlerter.lab("queryLogs a Controller ServiceException :", e);
        }

        return new ModelAndView("/log/loglist", mapMsg);
    }

    @RequestMapping(value = "/loginloglist")
    public ModelAndView queryLoginLogList(
            @RequestParam(value = "userId", required = false) String userId,
            @RequestParam(value = "startTime", required = false) String startTime,
            @RequestParam(value = "endTime", required = false) String endTime,
            @RequestParam(value = "items", required = false, defaultValue = "0") int items,                   //总记录数
            @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pagerOffset,      //数据库记录索引
            @RequestParam(value = "maxPageItems", required = false, defaultValue = "1") int maxPageItems) {   //每页记录数

        Pagination pagination = new Pagination(items, (pagerOffset / maxPageItems) + 1, WebappConfig.get().getHomePageSize());
        Map<String, Object> mapMsg = new HashMap<String, Object>();

        ToolsLoginLog entity = new ToolsLoginLog();

        if (!Strings.isNullOrEmpty(userId)) {
            entity.setUserId(userId);
        }

        if (!Strings.isNullOrEmpty(startTime)) {
            entity.setStartTime(DateUtil.StringTodate(startTime, DateUtil.PATTERN_DATE));
        }
        if (!Strings.isNullOrEmpty(endTime)) {
            entity.setEndTime(DateUtil.StringTodate((endTime + ToolsLog.END_TIME_PRIFIX), DateUtil.PATTERN_DATE_TIME));
        }


        try {
            PageRows<ToolsLoginLog> pageRows = logWebLogic.queryLoginLogs(entity, pagination);

            mapMsg.put("rows", pageRows.getRows());
            mapMsg.put("page", pageRows.getPage());
            mapMsg.put("entity", entity);
        } catch (ServiceException e) {
            GAlerter.lab("queryLoginLogList a Controller ServiceException :", e);
        }

        return new ModelAndView("/log/loginloglist", mapMsg);
    }

    @RequestMapping(value = "/querylog")
    public ModelAndView queryLog(
            @RequestParam(value = "lid", required = true) long lid,
            @RequestParam(value = "opTime", required = true) String opTime) {
        Map<String, Object> mapMsg = new HashMap<String, Object>();

        ToolsLog entity = null;
        try {

            entity = logWebLogic.queryLog(lid, DateUtil.StringTodate(opTime, "yyyy-MM-dd"));

            mapMsg.put("entity", entity);

        } catch (ServiceException e) {
            GAlerter.lab("getLog a Controller ServiceException :", e);
        }

        return new ModelAndView("/log/querylog", mapMsg);
    }

    @RequestMapping(value = "/queryloginlog")
    public ModelAndView queryloginLog(
            @RequestParam(value = "lid", required = true) long lid) {
        ToolsLoginLog entity = null;
        Map<String, Object> mapMsg = new HashMap<String, Object>();

        try {
            entity = logWebLogic.queryLoginLog(lid);
        } catch (ServiceException e) {
            GAlerter.lab("queryloginlog a Controller ServiceException :", e);
        }

        mapMsg.put("entity", entity);

        return new ModelAndView("/log/queryloginlog", mapMsg);
    }

    @ResponseBody
    @RequestMapping(value = "/addlog")
    public String addlog(@RequestParam(value = "ip", required = false) String ip,
                         @RequestParam(value = "userid", required = false) String userid,
                         @RequestParam(value = "btype", required = false) String btype,
                         @RequestParam(value = "stype", required = false) String stype,
                         @RequestParam(value = "opafter", required = false) String opafter,
                         @RequestParam(value = "encrypt", required = false) String encrypt) {
        JSONObject returnObj = new JSONObject();
        if (StringUtil.isEmpty(userid)) {
            returnObj.put("msg", "userid.is.null");
            returnObj.put("rs", 0);
            return returnObj.toString();
        }
        try {
            PrivilegeUser privilegeUser = ToolsServiceSngl.get().getUserByLoginName(userid);
            if (privilegeUser == null) {
                returnObj.put("msg", "userid.not.exist");
                returnObj.put("rs", 0);
                return returnObj.toString();
            }

            String en = MD5Util.Md5(CacheUtil.getToolsCookeySecretKey() + userid);
            if (StringUtil.isEmpty(encrypt) || !encrypt.equals(en)) {
                returnObj.put("msg", "encrypt.error");
                returnObj.put("rs", 0);
                return returnObj.toString();
            }

            ToolsLog log = new ToolsLog();

            log.setOpUserId(userid);
            LogOperType operType = LogOperType.getByCode(btype + "." + stype);
            if (operType == null) {
                returnObj.put("msg", "opertype.not.exist");
                returnObj.put("rs", 0);
                return returnObj.toString();
            }
            log.setOperType(operType);
            log.setOpTime(new Date());
            log.setOpIp(ip);
            log.setOpAfter(opafter);
            addLog(log);

            returnObj.put("msg", "success");
            returnObj.put("rs", 1);
        } catch (Exception e) {
            returnObj.put("msg", "system.error");
            returnObj.put("rs", 0);
        }
        return returnObj.toString();
    }
}
