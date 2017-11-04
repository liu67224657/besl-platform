package com.enjoyf.webapps.tools.webpage.controller.lottery;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.lottery.LotteryServiceSngl;
import com.enjoyf.platform.service.lottery.Ticket;
import com.enjoyf.platform.service.lottery.TicketField;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.bouncycastle.ocsp.OCSPReqGenerator;
import org.jfree.chart.axis.Tick;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import javax.management.monitor.StringMonitor;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 13-7-12
 * Time: 下午4:53
 * To change this template use File | Settings | File Templates.
 */

@Controller()
@RequestMapping(value = "/ticket/menu")
public class TicketController extends ToolsBaseController {
    @RequestMapping(value = "/list")
    public ModelAndView queryTicketList(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,      //数据库记录索引
                                        @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();
//        try {
//            int curPage = (pageStartIndex / pageSize) + 1;
//            Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
//            PageRows<Ticket> pageRows = LotteryServiceSngl.get().queryTicketPageRows(new QueryExpress().add(QuerySort.add(TicketField.CREATEDATE, QuerySortOrder.DESC)), pagination);
//
//            mapMessage.put("list", pageRows.getRows());
//            mapMessage.put("page", pageRows.getPage());
//        } catch (ServiceException e) {
//            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
//            mapMessage = putErrorMessage(mapMessage, "system.error");
//        }

        return new ModelAndView("/lottery/ticketlist", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createPage() {
        Map<String, Object> mapMessage = new HashMap<String, Object>();


        return new ModelAndView("/lottery/createticket");
    }

    @RequestMapping(value = "/createticket")
    public ModelAndView createTicket(@RequestParam(value = "ticketname", required = true) String ticketName,
                                     @RequestParam(value = "ticketdesc", required = true) String ticketDesc,
                                     @RequestParam(value = "baserate", required = true) int baserate,
                                     @RequestParam(value = "awardlevelcount", required = true) int level,
                                     @RequestParam(value = "wintype", required = true) int winType,
                                     @RequestParam(value = "wintime", required = false) String winTime,
                                     @RequestParam(value = "starttime", required = true) String starttime,
                                     @RequestParam(value = "endtime", required = true) String endtime) throws ParseException {
//        try {
//            SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            Ticket ticket = new Ticket();
//
//            ticket.setTicketName(ticketName);
//            ticket.setTicketDesc(ticketDesc);
//            ticket.setBase_rate(baserate);
//            ticket.setCurr_num(0);
//            ticket.setAwardLevelCount(level);
//            ticket.setValidStatus(ValidStatus.INVALID);
//            ticket.setWin_type(winType);
//            if (winTime != "" && !"".equals(winTime)) {
//                ticket.setWinCronexp(winTime);
//            }
//            ticket.setStart_time(sim.parse(starttime));
//            ticket.setEnd_time(sim.parse(endtime));
//            ticket.setCreateUserid(getCurrentUser().getUserid());
//            ticket.setCreateIp(getIp());
//            ticket.setCreateDate(new Date());
//            LotteryServiceSngl.get().createTicket(ticket);
//        } catch (ServiceException e) {
//            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
//        }
        return new ModelAndView("redirect:/ticket/menu/list");
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyPage(@RequestParam(value = "ticketId", required = true) long ticketId) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();
//        try {
//            Ticket ticket = LotteryServiceSngl.get().getTicketById(new QueryExpress().add(QueryCriterions.eq(TicketField.TICKET_ID, ticketId)));
//            mapMessage.put("ticket", ticket);
//
//        } catch (ServiceException e) {
//            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
//        }

        return new ModelAndView("/lottery/ticketmodifypage", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "ticketid", required = true) long ticketId,
                               @RequestParam(value = "ticketname", required = true) String ticketName,
                               @RequestParam(value = "ticketdesc", required = true) String ticketDesc,
                               @RequestParam(value = "baserate", required = true) int baserate,
                               @RequestParam(value = "awardlevelcount", required = true) int level,
                               @RequestParam(value = "wintype", required = true) int winType,
                               @RequestParam(value = "wintime", required = false) String winTime,
                               @RequestParam(value = "starttime", required = true) String starttime,
                               @RequestParam(value = "endtime", required = true) String endtime) throws ParseException {
//        try {
//            SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            UpdateExpress updateExpress = new UpdateExpress();
//            updateExpress.set(TicketField.TICKET_NAME, ticketName);
//            updateExpress.set(TicketField.TICKET_DESC, ticketDesc);
//            updateExpress.set(TicketField.BASE_RATE, baserate);
//            updateExpress.set(TicketField.AWARD_LEVEL_COUNT, level);
//            updateExpress.set(TicketField.WIN_TYPE, winType);
//            updateExpress.set(TicketField.WIN_CRONEXP, winTime);
//            updateExpress.set(TicketField.START_TIME, sim.parse(starttime));
//            updateExpress.set(TicketField.END_TIME, sim.parse(endtime));
//
//            updateExpress.set(TicketField.LASTMODIFYUSERID,getCurrentUser().getUserid());
//            updateExpress.set(TicketField.LASTMODIFYIP,getIp());
//            updateExpress.set(TicketField.LASTMODIFYDATE,new Date());
//            if (winTime != "" && !"".equals(winTime)) {
//                updateExpress.set(TicketField.WIN_CRONEXP, winTime);
//            }
//
//            LotteryServiceSngl.get().modifyTicket(updateExpress, new QueryExpress().add(QueryCriterions.eq(TicketField.TICKET_ID, ticketId)));
//        } catch (ServiceException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }


        return new ModelAndView("redirect:/ticket/menu/list");

    }

    @RequestMapping(value = "/activate")
    public ModelAndView activateTicket(@RequestParam(value = "ticketid", required = true) long ticketId,
                                       @RequestParam(value = "validstatus", required = false) String validStatus) {

//        try {
//            if (validStatus.equals("valid")) {
//                return new ModelAndView("redirect:/ticket/menu/list");
//            } else {
//                UpdateExpress updateExpress = new UpdateExpress();
//                updateExpress.set(TicketField.VALIDSTATUS, ValidStatus.VALID.getCode());
//                updateExpress.set(TicketField.LASTMODIFYUSERID, getCurrentUser().getUserid());
//                updateExpress.set(TicketField.LASTMODIFYIP, getIp());
//                updateExpress.set(TicketField.LASTMODIFYDATE, new Date());
//                LotteryServiceSngl.get().modifyTicket(updateExpress, new QueryExpress().add(QueryCriterions.eq(TicketField.TICKET_ID, ticketId)));
//            }
//
//
//        } catch (ServiceException e) {
//            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
//        }

        return new ModelAndView("redirect:/ticket/menu/list");
    }

    @RequestMapping(value = "/delete")
    public ModelAndView deleteTicket(@RequestParam(value = "ticketid", required = true) long ticketId,
                                     @RequestParam(value = "validstatus", required = false) String validStatus) {
//        try {
//            if (validStatus.equals("removed")) {
//                return new ModelAndView("redirect:/ticket/menu/list");
//            } else {
//                UpdateExpress updateExpress = new UpdateExpress();
//                updateExpress.set(TicketField.VALIDSTATUS, ValidStatus.REMOVED.getCode());
//                updateExpress.set(TicketField.LASTMODIFYUSERID, getCurrentUser().getUserid());
//                updateExpress.set(TicketField.LASTMODIFYIP, getIp());
//                updateExpress.set(TicketField.LASTMODIFYDATE, new Date());
//                LotteryServiceSngl.get().modifyTicket(updateExpress, new QueryExpress().add(QueryCriterions.eq(TicketField.TICKET_ID, ticketId)));
//            }
//        } catch (ServiceException e) {
//            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
//        }

        return new ModelAndView("redirect:/ticket/menu/list");
    }

}
