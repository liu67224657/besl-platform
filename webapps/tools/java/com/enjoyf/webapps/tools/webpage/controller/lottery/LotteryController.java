package com.enjoyf.webapps.tools.webpage.controller.lottery;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.lottery.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.service.tools.ToolsLog;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-31
 * Time: 上午9:43
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/lottery")
public class LotteryController extends ToolsBaseController {

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,      //数据库记录索引
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();
        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);

        try {
            PageRows<Lottery> pageRows = LotteryServiceSngl.get().queryLotteryByPage(new QueryExpress().add(QuerySort.add(LotteryField.CREATE_DATE, QuerySortOrder.ASC)), pagination);

            mapMessage.put("list", pageRows.getRows());
            mapMessage.put("page", pageRows.getPage());

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("/lottery/lotterylist", mapMessage);
    }


    //
    @RequestMapping(value = "/createpage")
    public ModelAndView createPage() {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        mapMessage.put("lotteryRuleActionTypeCollection", LotteryRuleActionType.getAll());
        mapMessage.put("lotteryRuleChanceTypeCollection", LotteryRuleChanceType.getAll());
        return new ModelAndView("/lottery/createlottery", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "lotteryname", required = false) String lotteryName,
                               @RequestParam(value = "lotterydesc", required = false) String lotteryDesc,
                               @RequestParam(value = "baserate", required = false) Integer baseRate,
                               @RequestParam(value = "awardlevelcount", required = false) Integer awardLevelCount,
                               @RequestParam(value = "start_date", required = false) String start_date,
                               @RequestParam(value = "end_date", required = false) String end_date,
                               @RequestParam(value = "initChance", required = false) Integer initChance,
                               @RequestParam(value = "lotteryRuleActionType", required = false) Integer lotteryRuleActionType,
                               @RequestParam(value = "lotteryRuleChanceType", required = false) Integer lotteryRuleChanceType,
                               @RequestParam(value = "maxchance", required = false) Integer maxchance
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        Date date = new Date();
        String ip = getIp();

        Lottery lottery = new Lottery();
        lottery.setLotteryName(lotteryName);
        lottery.setLotteryDesc(lotteryDesc);
        lottery.setBaseRate(baseRate);
        lottery.setAwardLevelCount(awardLevelCount);
        lottery.setValidStatus(ValidStatus.VALID);
        lottery.setCreateDate(date);
        lottery.setCreateIp(ip);
        lottery.setLastModifyDate(date);
        lottery.setLastModifyIp(ip);


        LotteryRule rule = new LotteryRule();
        rule.setRuleChanceType(LotteryRuleChanceType.getByCode(lotteryRuleChanceType));
        rule.setMaxChance(maxchance);
        rule.setLotteryRuleActionType(LotteryRuleActionType.getByCode(lotteryRuleActionType));
        rule.setInitChance(initChance);
        try {
            lottery.setLotteryRule(rule);
            lottery.setStartDate(DateUtil.formatStringToDate(start_date, DateUtil.PATTERN_DATE_TIME));
            lottery.setEndDate(DateUtil.formatStringToDate(end_date, DateUtil.PATTERN_DATE_TIME));

            LotteryServiceSngl.get().createLottery(lottery);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("forward:/lottery/createpage", mapMessage);
        }

        return new ModelAndView("forward:/lottery/list", mapMessage);
    }

    //
    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyPage(@RequestParam(value = "lotteryid", required = true) Long lotteryId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        mapMessage.put("lotteryRuleActionTypeCollection", LotteryRuleActionType.getAll());
        mapMessage.put("lotteryRuleChanceTypeCollection", LotteryRuleChanceType.getAll());


        try {
            Lottery lottery = LotteryServiceSngl.get().getLotteryById(lotteryId);
            if (lottery == null) {
                mapMessage = putErrorMessage(mapMessage, "lottery.lotteryId.empty");
                return new ModelAndView("forward:/lottery/list", mapMessage);
            }
            mapMessage.put("lottery", lottery);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("forward:/lottery/list", mapMessage);
        }
        return new ModelAndView("/lottery/modifylottery", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "lotteryid", required = true) Long lotteryId,
                               @RequestParam(value = "lotteryname", required = false) String lotteryName,
                               @RequestParam(value = "lotterydesc", required = false) String lotteryDesc,
                               @RequestParam(value = "baserate", required = false) Integer baseRate,
                               @RequestParam(value = "awardlevelcount", required = false) Integer awardLevelCount,
                               @RequestParam(value = "start_date", required = false) String start_date,
                               @RequestParam(value = "end_date", required = false) String end_date,
                               @RequestParam(value = "initChance", required = false) Integer initChance,
                               @RequestParam(value = "lotteryRuleActionType", required = false) Integer lotteryRuleActionType,
                               @RequestParam(value = "lotteryRuleChanceType", required = false) Integer lotteryRuleChanceType,
                               @RequestParam(value = "maxchance", required = false) Integer maxchance) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            List<Lottery> list = LotteryServiceSngl.get().queryLottery(new QueryExpress().add(QueryCriterions.eq(LotteryField.LOTTERY_NAME, lotteryName)));
            if (!CollectionUtil.isEmpty(list) && list.get(0).getLotteryId() != lotteryId) {
                mapMessage.put("nameExist", "lottery.name.has.exist");
                return new ModelAndView("forward:/lottery/modifypage", mapMessage);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("forward:/lottery/modifypage", mapMessage);
        }

        try {
            UpdateExpress updateExpress = new UpdateExpress();

            Date date = new Date();
            String ip = getIp();

            updateExpress.set(LotteryField.LOTTERY_NAME, lotteryName);
            updateExpress.set(LotteryField.LOTTERY_DESC, lotteryDesc);
            updateExpress.set(LotteryField.BASE_RATE, baseRate);
            updateExpress.set(LotteryField.AWARD_LEVEL_COUNT, awardLevelCount);
            updateExpress.set(LotteryField.LAST_MODIFY_DATE, date);
            updateExpress.set(LotteryField.LAST_MODIFY_IP, ip);


            LotteryRule rule = new LotteryRule();
            rule.setRuleChanceType(LotteryRuleChanceType.getByCode(lotteryRuleChanceType));
            rule.setMaxChance(maxchance);
            rule.setLotteryRuleActionType(LotteryRuleActionType.getByCode(lotteryRuleActionType));
            rule.setInitChance(initChance);
            updateExpress.set(LotteryField.LOTTERY_RULE, rule.toJson());


            updateExpress.set(LotteryField.START_DATE, DateUtil.formatStringToDate(start_date, DateUtil.PATTERN_DATE_TIME));
            updateExpress.set(LotteryField.END_DATE, DateUtil.formatStringToDate(end_date, DateUtil.PATTERN_DATE_TIME));

            LotteryServiceSngl.get().modifyLotteryById(updateExpress, lotteryId);


        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("forward:/lottery/modifypage", mapMessage);
        }
        return new ModelAndView("redirect:/lottery/list");
    }

    //
    @RequestMapping(value = "/delete")
    public ModelAndView delete(@RequestParam(value = "lotteryid", required = true) Long lotteryId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(LotteryField.VALID_STATUS, ValidStatus.REMOVED.getCode());
        try {
            LotteryServiceSngl.get().modifyLotteryById(updateExpress, lotteryId);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("forward:/lottery/list", mapMessage);
        }
        return new ModelAndView("redirect:/lottery/list");
    }

    @RequestMapping(value = "/recover")
    public ModelAndView recover(@RequestParam(value = "lotteryid", required = true) Long lotteryId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(LotteryField.VALID_STATUS, ValidStatus.VALID.getCode());
        try {
            LotteryServiceSngl.get().modifyLotteryById(updateExpress, lotteryId);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("forward:/lottery/list", mapMessage);
        }
        return new ModelAndView("redirect:/lottery/list");
    }


}
