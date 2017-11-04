package com.enjoyf.webapps.tools.webpage.controller.lottery;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.lottery.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 13-7-16
 * Time: 下午4:25
 * To change this template use File | Settings | File Templates.
 */

@Controller
@RequestMapping(value = "/ticket/award")
public class TicketAwardController extends ToolsBaseController {
    @RequestMapping(value = "/list")
    public ModelAndView queryTicketAwardPageRows(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,      //数据库记录索引
                                                 @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                                                 @RequestParam(value = "ticketId", required = false) Long ticketId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
//        try {
//
//            //获取存在且状态为激活的彩票信息
//            List<Ticket> ticketList = LotteryServiceSngl.get().queryTicketList(new QueryExpress().add(QueryCriterions.eq(TicketField.VALIDSTATUS, ValidStatus.VALID.getCode())));
//            if (CollectionUtil.isEmpty(ticketList)) {
//                return new ModelAndView("/lottery/ticketawardlist");
//            }
//            mapMessage.put("ticket", ticketList);
//
//            if (ticketId == null) {
//                return new ModelAndView("/lottery/ticketawardlist", mapMessage);
//            }
//            mapMessage.put("ticketId", ticketId);
//            int curPage = (pageStartIndex / pageSize) + 1;
//            Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
//            PageRows<TicketAward> pageRows = LotteryServiceSngl.get().queryTicketAwardPageRows(new QueryExpress().add(QueryCriterions.eq(TicketAwardField.TICKET_ID, ticketId))
//                    .add(QuerySort.add(TicketAwardField.AWARD_LEVEL, QuerySortOrder.ASC)), pagination);
//            mapMessage.put("list", pageRows.getRows());
//            mapMessage.put("page", pageRows.getPage());
//
//        } catch (ServiceException e) {
//            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
//            mapMessage = putErrorMessage(mapMessage, "system.error");
//        }
        return new ModelAndView("/lottery/ticketawardlist", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createPage(@RequestParam(value = "ticketId", required = false) Long ticketId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
//        try {
//            if (ticketId == null) {
//                return new ModelAndView("/ticket/award/list?ticketId=" + ticketId);
//            }
//            //获取彩票信息里填写的最大奖品等级
//            Ticket ticket = LotteryServiceSngl.get().getTicketById(new QueryExpress().add(QueryCriterions.eq(TicketField.TICKET_ID, ticketId)));
//            List<Integer> listAward = new ArrayList<Integer>();
//            for (int i = 1; i <= ticket.getAwardLevelCount(); i++) {
//                listAward.add(i);
//            }
//            mapMessage.put("awardLevel", listAward);
//            mapMessage.put("ticketId", ticketId);
//        } catch (ServiceException e) {
//            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
//            mapMessage = putErrorMessage(mapMessage, "system.error");
//        }

        return new ModelAndView("/lottery/createticketward", mapMessage);
    }

    @RequestMapping(value = "/createticketaward")
    public ModelAndView createTicketAward(@RequestParam(value = "ticketId", required = false) Long ticketId,
                                          @RequestParam(value = "awardDesc", required = true) String awardDesc,
                                          @RequestParam(value = "awardPic", required = true) String awardPic,
                                          @RequestParam(value = "awardLevel", required = true) int awardLevel,
                                          @RequestParam(value = "awardCount", required = true) int awardCount,
                                          @RequestParam(value = "currentCount", required = true) int currentCount,
                                          @RequestParam(value = "awardName", required = true) String awardName) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();
//        try {
//            if (ticketId == null) {
//                return new ModelAndView("redirect:/ticket/award/list");
//            }
//            //根据ID和 奖品等级查找是否有数据 如果有则调回添加页面 不能重复添加同样奖品等级
//            TicketAward ticketAward = LotteryServiceSngl.get().getTicketAwardById(new QueryExpress().add(QueryCriterions.eq(TicketAwardField.TICKET_ID, ticketId))
//                    .add(QueryCriterions.eq(TicketAwardField.AWARD_LEVEL, awardLevel)));
//            if (ticketAward != null) {
//                mapMessage.put("levelExist", "lottery.award.level.has.exist");
//                mapMessage.put("ticketId", ticketId);
//                return new ModelAndView("forward:/ticket/award/createpage", mapMessage);
//            }
//
//            ticketAward = new TicketAward();
//            ticketAward.setTicketId(ticketId);
//            ticketAward.setAwardName(awardName);
//            ticketAward.setAwardDesc(awardDesc);
//            ticketAward.setAwardPic(awardPic);
//            ticketAward.setAwardLevel(awardLevel);
//            ticketAward.setAwardCount(awardCount);
//            ticketAward.setCurrentCount(currentCount);
//            ticketAward.setCreateUserId(getCurrentUser().getUserid());
//            ticketAward.setCreateDate(new Date());
//            ticketAward.setCreateIp(getIp());
//            LotteryServiceSngl.get().createTicketAward(ticketAward);
//        } catch (ServiceException e) {
//            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
//            mapMessage = putErrorMessage(mapMessage, "system.error");
//        }

        return new ModelAndView("redirect:/ticket/award/list?ticketId=" + ticketId);
    }

    @RequestMapping(value = "/delete")
    public ModelAndView deleteTicketAward(@RequestParam(value = "ticketAwardId", required = false) Long ticketAwardId,
                                          @RequestParam(value = "ticketId", required = false) Long ticketId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
//        try {
//            UpdateExpress updateExpress = new UpdateExpress();
//            updateExpress.set(TicketAwardField.VALIDSTATUS, ValidStatus.REMOVED.getCode());
//            updateExpress.set(TicketAwardField.LASTMODIFYUSERID, getCurrentUser().getUserid());
//            updateExpress.set(TicketAwardField.LASTMODIFYDATE, new Date());
//            updateExpress.set(TicketAwardField.LASTMODIFYIP, getIp());
//            LotteryServiceSngl.get().modifyTicketAward(updateExpress, new QueryExpress().add(QueryCriterions.eq(TicketAwardField.TICKET_AWARD_ID, ticketAwardId)));
//        } catch (ServiceException e) {
//            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
//            mapMessage = putErrorMessage(mapMessage, "system.error");
//        }


        return new ModelAndView("redirect:/ticket/award/list?ticketId=" + ticketId);
    }

    @RequestMapping(value = "recover")
    public ModelAndView recoverTicketAward(@RequestParam(value = "ticketAwardId", required = false) Long ticketAwardId,
                                           @RequestParam(value = "ticketId", required = false) Long ticketId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
//        try {
//            UpdateExpress updateExpress = new UpdateExpress();
//            updateExpress.set(TicketAwardField.VALIDSTATUS, ValidStatus.VALID.getCode());
//            updateExpress.set(TicketAwardField.LASTMODIFYUSERID, getCurrentUser().getUserid());
//            updateExpress.set(TicketAwardField.LASTMODIFYDATE, new Date());
//            updateExpress.set(TicketAwardField.LASTMODIFYIP, getIp());
//            LotteryServiceSngl.get().modifyTicketAward(updateExpress, new QueryExpress().add(QueryCriterions.eq(TicketAwardField.TICKET_AWARD_ID, ticketAwardId)));
//        } catch (ServiceException e) {
//            GAlerter.lab(this.getClass().getName() + "occured ServiceExcpetion e", e);
//            mapMessage = putErrorMessage(mapMessage, "system.error");
//        }
        return new ModelAndView("redirect:/ticket/award/list?ticketId=" + ticketId);
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView toModifyPage(@RequestParam(value = "ticketAwardId", required = false) Long ticketAwardId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

//        try {
//            TicketAward ticketAward = LotteryServiceSngl.get().getTicketAwardById(new QueryExpress().add(QueryCriterions.eq(TicketAwardField.TICKET_AWARD_ID, ticketAwardId)));
//            mapMessage.put("ticketAward", ticketAward);
//
//            Ticket ticket = LotteryServiceSngl.get().getTicketById(new QueryExpress().add(QueryCriterions.eq(TicketField.TICKET_ID, ticketAward.getTicketId())));
//            List<Integer> listAward = new ArrayList<Integer>();
//            for (int i = 1; i <= ticket.getAwardLevelCount(); i++) {
//                listAward.add(i);
//            }
//
//            mapMessage.put("awardLevel", listAward);
//
//
//        } catch (ServiceException e) {
//            GAlerter.lab(this.getClass().getName() + "orrcured ServiceExcpetion e", e);
//            mapMessage = putErrorMessage(mapMessage, "system.error");
//        }

        return new ModelAndView("/lottery/modifyticketaward", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modifyTicketAward(@RequestParam(value = "ticketId", required = false) Long ticketId,
                                          @RequestParam(value = "awardDesc", required = true) String awardDesc,
                                          @RequestParam(value = "awardPic", required = true) String awardPic,
                                          @RequestParam(value = "awardLevel", required = true) int awardLevel,
                                          @RequestParam(value = "awardCount", required = true) int awardCount,
                                          @RequestParam(value = "currentCount", required = true) int currentCount,
                                          @RequestParam(value = "awardName", required = true) String awardName,
                                          @RequestParam(value = "ticketAwardId", required = false) Long ticketAwardId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
//        try {
//            TicketAward ticketAward = LotteryServiceSngl.get().getTicketAwardById(new QueryExpress()
//                    .add(QueryCriterions.eq(TicketAwardField.TICKET_ID, ticketId))
//                    .add(QueryCriterions.eq(TicketAwardField.AWARD_LEVEL, awardLevel))
//                    .add(QueryCriterions.ne(TicketAwardField.TICKET_AWARD_ID, ticketAwardId)));
//            //根据ID和 奖品等级查找是否有数据 如果有则调回添加页面 不能重复添加同样奖品等级
//            if (ticketAward != null) {
//                mapMessage.put("levelExist", "lottery.award.level.has.exist");
//                mapMessage.put("ticketId", ticketId);
//                mapMessage.put("ticketAwardId", ticketAwardId);
//                return new ModelAndView("forward:/ticket/award/modifypage", mapMessage);
//            }
//
//            UpdateExpress updateExpress = new UpdateExpress();
//
//            updateExpress.set(TicketAwardField.AWRD_DESC, awardDesc);
//            updateExpress.set(TicketAwardField.AWARD_PIC, awardPic);
//            updateExpress.set(TicketAwardField.AWARD_LEVEL, awardLevel);
//            updateExpress.set(TicketAwardField.AWARD_COUNT, awardCount);
//            updateExpress.set(TicketAwardField.CURRENT_COUNT, currentCount);
//            updateExpress.set(TicketAwardField.AWARD_NAME, awardName);
//            updateExpress.set(TicketAwardField.LASTMODIFYUSERID, getCurrentUser().getUserid());
//            updateExpress.set(TicketAwardField.LASTMODIFYDATE, new Date());
//            updateExpress.set(TicketAwardField.LASTMODIFYIP, getIp());
//            LotteryServiceSngl.get().modifyTicketAward(updateExpress, new QueryExpress().add(QueryCriterions.eq(TicketAwardField.TICKET_AWARD_ID, ticketAwardId)));
//
//
//        } catch (ServiceException e) {
//            GAlerter.lab(this.getClass().getName() + "crrcured ServiceException e", e);
//            mapMessage = putErrorMessage(mapMessage, "system.error");
//        }

        return new ModelAndView("redirect:/ticket/award/list?ticketId=" + ticketId);
    }
}
