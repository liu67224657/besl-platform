package com.enjoyf.webapps.tools.webpage.controller.advertise;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.advertise.AdvertiseAgent;
import com.enjoyf.platform.service.advertise.AdvertiseAgentField;
import com.enjoyf.platform.service.advertise.AdvertiseServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
@Controller
@RequestMapping(value = "/advertise/agent")
public class AdvertiseAgentController extends AdvertiseBaseController {

    @RequestMapping(value = "/list")
    public ModelAndView agentList(@RequestParam(value = "agentname", required = false) String agentName,
                                  @RequestParam(value = "validstatus", required = false) String validStatus,
                                  @RequestParam(value = "items", required = false, defaultValue = "0") Integer items,
                                  @RequestParam(value = "pager.offset", required = false, defaultValue = "0") Integer pagerOffset,
                                  @RequestParam(value = "maxPageItems", required = false, defaultValue = "1") Integer maxPageItems) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        QueryExpress queryExpress = new QueryExpress()
                .add(QuerySort.add(AdvertiseAgentField.CREATEDATE, QuerySortOrder.DESC));
        if (!StringUtil.isEmpty(agentName)) {
            queryExpress.add(QueryCriterions.like(AdvertiseAgentField.AGENTNAME, "%" + agentName.trim() + "%"));
        }
        if (!StringUtil.isEmpty(validStatus)) {
            queryExpress.add(QueryCriterions.eq(AdvertiseAgentField.VALIDSTATUS, validStatus.trim()));
        }

        Pagination page = new Pagination(items, (pagerOffset / maxPageItems) + 1, WebappConfig.get().getHomePageSize());


        PageRows<AdvertiseAgent> pageRows = new PageRows<AdvertiseAgent>();
        try {
            pageRows = AdvertiseServiceSngl.get().queryPageAgents(queryExpress, page);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured Service Exception.e:", e);
            mapMessage.put("errorMsgMap", putErrorMap("error", "error.exception"));
            return new ModelAndView("/advertise/agentlist", mapMessage);
        }
        mapMessage.put("rows", pageRows.getRows());
        mapMessage.put("page", pageRows.getPage());
        mapMessage.put("agentList", pageRows.getRows());
        mapMessage.put("agentName", agentName);
        mapMessage.put("validStatus", validStatus);

        mapMessage.put("items", page.getTotalRows());
        mapMessage.put("maxPageItems", page.getMaxPage());
        mapMessage.put("pagerOffset", page.getCurPage());

        return new ModelAndView("/advertise/agentlist", mapMessage);
    }

    @RequestMapping(value = "/addpage")
    public ModelAndView addAgentPage() {
        return new ModelAndView("/advertise/agentaddpage");
    }

    @RequestMapping(value = "/add")
    public ModelAndView addAgent(@RequestParam(value = "agentname") String agentName,
                                 @RequestParam(value = "agentdesc", required = false) String agentDesc) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        AdvertiseAgent advertiseAgent = new AdvertiseAgent();
        advertiseAgent.setAgentName(agentName);
        advertiseAgent.setAgentDesc(agentDesc);
        advertiseAgent.setCreateDate(new Date());
        advertiseAgent.setCreateIp(this.getIp());
        advertiseAgent.setCreateUserid(this.getCurrentUser().getUsername());

        try {
            AdvertiseServiceSngl.get().createAgent(advertiseAgent);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured Service Exception.e:", e);

            mapMessage.put("errorMsgMap", putErrorMap("agentName", "error.exception"));
            return new ModelAndView("/advertise/agentaddpage", mapMessage);
        }

        return new ModelAndView("redirect:/advertise/agent/list");
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyAgentPage(@RequestParam(value = "agentid") String agentId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        AdvertiseAgent advertiseAgent = null;
        try {
            advertiseAgent = AdvertiseServiceSngl.get().getAgent(agentId);

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " modifyAgentPage occured Service Exception.e:", e);

            mapMessage.put("errorMsgMap", putErrorMap("error", "error.exception"));
            return new ModelAndView("redirect:/advertise/agent/list", mapMessage);
        }

        if (advertiseAgent == null || advertiseAgent.getValidStatus().equals(ValidStatus.REMOVED)) {
            mapMessage.put("errorMsgMap", putErrorMap("error", "error.advertise.agent.notexists"));
            return new ModelAndView("redirect:/advertise/agent/list", mapMessage);
        } else {
            mapMessage.put("agent", advertiseAgent);
        }

        return new ModelAndView("/advertise/agentmodifypage", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modifyAgent(@RequestParam(value = "agentid") String agentId,
                                    @RequestParam(value = "agentdesc", required = false) String agentDesc,
                                    @RequestParam(value = "agentname", required = false) String agentName,
                                    @RequestParam(value = "validstatus", required = false) String validstatus) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        UpdateExpress updateExpress = new UpdateExpress().set(AdvertiseAgentField.AGENTNAME, agentName)
                .set(AdvertiseAgentField.AGENTDESC, agentDesc)
                .set(AdvertiseAgentField.VALIDSTATUS, validstatus)
                .set(AdvertiseAgentField.UPDATEDATE, new Date())
                .set(AdvertiseAgentField.UPDATEIP, getIp())
                .set(AdvertiseAgentField.UPDATEUSERID, getCurrentUser().getUsername());

        try {
            AdvertiseServiceSngl.get().modifyAgent(updateExpress, agentId);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " modifyAgentPage occured Service Exception.e:", e);

            mapMessage.put("errorMsgMap", putErrorMap("error", "error.exception"));
            return new ModelAndView("redirect:/advertise/agent/list", mapMessage);
        }

        return new ModelAndView("redirect:/advertise/agent/list");
    }


    @RequestMapping(value = "/remove")
    public ModelAndView removeAgent() {
        return null;
    }
}
