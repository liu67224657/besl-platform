package com.enjoyf.webapps.tools.webpage.controller.lottery;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.lottery.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.service.tools.ToolsLog;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-31
 * Time: 上午9:43
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/lottery/award")
public class LotteryAwardController extends ToolsBaseController {

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "lotteryid", required = false) Long lotteryId,
                             @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,      //数据库记录索引
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            List<Lottery> lotteryList = LotteryServiceSngl.get().queryLottery(new QueryExpress().add(QueryCriterions.ne(LotteryField.VALID_STATUS, ValidStatus.REMOVED.getCode())));
            if (CollectionUtil.isEmpty(lotteryList)) {
                return new ModelAndView("/lottery/lotteryawardlist");
            }
            mapMessage.put("lotteryList", lotteryList);

            if (lotteryId == null) {
                return new ModelAndView("/lottery/lotteryawardlist", mapMessage);
            }
            mapMessage.put("lotteryId", lotteryId);

            Lottery lottery = LotteryServiceSngl.get().getLotteryById(lotteryId);
            mapMessage.put("lottery", lottery);

            int curPage = (pageStartIndex / pageSize) + 1;
            Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);

            PageRows<LotteryAward> pageRows = LotteryServiceSngl.get().queryLotteryAwardByPage(new QueryExpress().add(QueryCriterions.eq(LotteryAwardField.LOTTERY_ID, lotteryId))
                    .add(QuerySort.add(LotteryAwardField.LOTTERY_AWARD_LEVEL, QuerySortOrder.ASC)), pagination);

            mapMessage.put("list", pageRows.getRows());
            mapMessage.put("page", pageRows.getPage());
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/lottery/lotteryawardlist", mapMessage);
        }

        return new ModelAndView("/lottery/lotteryawardlist", mapMessage);
    }


    //
    @RequestMapping(value = "/createpage")
    public ModelAndView createPage(@RequestParam(value = "lotteryid", required = false) Long lotteryId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        if (lotteryId == null) {
            mapMessage = putErrorMessage(mapMessage, "lottery.lotteryAwardId.empty");
            return new ModelAndView("redirect:/lottery/award/list?lotteryid=" + lotteryId);
        }

        try {
            Lottery lottery = LotteryServiceSngl.get().getLotteryById(lotteryId);
            if (lottery == null) {
                mapMessage = putErrorMessage(mapMessage, "lottery.lotteryAwardId.empty");
                return new ModelAndView("redirect:/lottery/award/list?lotteryid=" + lotteryId);
            }

            mapMessage.put("lottery", lottery);

            //根据lottery的award_level_count去取awardLevel
            int count = lottery.getAwardLevelCount();
            List<AwardLevel> levelList = new ArrayList<AwardLevel>();
            Collection<AwardLevel> coll = AwardLevel.getAll();
            for (AwardLevel level : coll) {
                levelList.add(level);
                if (levelList.size() >= count)
                    break;
            }
            mapMessage.put("levelList", levelList);

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("redirect:/lottery/award/list?lotteryid=" + lotteryId);
        }

        mapMessage.put("awardtypecollection", LotteryAwardType.getAll());
        mapMessage.put("lotteryAwardRuleCountType", LotteryAwardRuleCountType.getAll());

        return new ModelAndView("/lottery/createlotteryaward", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "lotteryid", required = false) Long lotteryId,
                               @RequestParam(value = "awardname", required = false) String awardName,
                               @RequestParam(value = "awarddesc", required = false) String awardDesc,
                               @RequestParam(value = "awardlevel", required = false) Integer awardLevel,
                               @RequestParam(value = "awardtype", required = false) Integer awardType,
                               @RequestParam(value = "awardamount", required = false) Integer awardAmount,
                               @RequestParam(value = "createdate", required = false) String createdatestr,
                               @RequestParam(value = "lastmodifydate", required = false) String lastmodifydatestr,
                               @RequestParam(value = "lottery_lotteryruleactiontype", required = false) Integer lottery_lotteryruleactiontype,
                               @RequestParam(value = "minrate", required = false) String minrate,
                               @RequestParam(value = "maxrate", required = false) String maxrate,
                               @RequestParam(value = "lotteryAwardRuleCountType", required = false) Integer lotteryAwardRuleCountType,
                               @RequestParam(value = "lottery_award_count_number", required = false) String lottery_award_count_number,
                               @RequestParam(value = "add_lottery_number", required = false) Integer add_lottery_number,
                               @RequestParam(value = "lotteryAwardId", required = false) Integer lotteryAwardId,

                               HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        Date createdate = null;
        Date lastmodifydate = null;
        try {
            createdate = DateUtil.formatStringToDate(createdatestr, DateUtil.PATTERN_DATE_TIME);
            lastmodifydate = DateUtil.formatStringToDate(lastmodifydatestr, DateUtil.PATTERN_DATE_TIME);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            Date date = new Date();
            String ip = getIp();

            LotteryAward award = new LotteryAward();
            award.setLotteryId(lotteryId);
            award.setLotteryAwardName(awardName);
            award.setLotteryAwardDesc(awardDesc);
            award.setLotteryAwardLevel(awardLevel);

            LotteryAwardRule rule = new LotteryAwardRule();


            LotteryRuleActionType lotteryRuleActionType = LotteryRuleActionType.getByCode(lottery_lotteryruleactiontype);

            //放入中奖号码
            if (awardAmount > 0) {
                List<Integer> rangeList = new ArrayList<Integer>();
                Random random = new Random();


                if (lotteryRuleActionType.equals(LotteryRuleActionType.RANDOM)) {   //按随机数
                    int min = Integer.valueOf(minrate);
                    int max = Integer.valueOf(maxrate);
                    for (int i = 0; i < awardAmount; i++) {

                        rangeList.add(random.nextInt(max - min + 1) + min);
                    }
                    rule.setMin(minrate);
                    rule.setMax(maxrate);
                    rule.setText(lottery_award_count_number);
                } else if (lotteryRuleActionType.equals(LotteryRuleActionType.TIMESTAMP)) {//按时间戳

                    Date minrateDate = DateUtil.formatStringToDate(minrate, DateUtil.PATTERN_DATE_TIME);
                    Date maxrateDate = DateUtil.formatStringToDate(maxrate, DateUtil.PATTERN_DATE_TIME);
                    int min_rate = Long.valueOf(minrateDate.getTime() / 1000).intValue();
                    int max_rate = Long.valueOf(maxrateDate.getTime() / 1000).intValue();
                    for (int i = 0; i < awardAmount; i++) {
                        rangeList.add((int) (Math.random() * 1000000 + 100000));
                    }
                    rule.setMin(minrate);
                    rule.setMax(maxrate);
                    rule.setText(lottery_award_count_number);

                } else if (lotteryRuleActionType.equals(LotteryRuleActionType.TIMES)) {//按次数
                    String arrLotteryCode[] = minrate.split(",");
                    for (String str : arrLotteryCode) {
                        rangeList.add((int) (Math.random() * 1000000 + 100000));
                    }
                    rule.setMin(minrate);
                    rule.setMax("");
                    rule.setText(lottery_award_count_number);
                }
                rule.setRangeList(rangeList);
            }
            //奖品次数类型:
            rule.setLotteryAwardRuleCountType(LotteryAwardRuleCountType.getByCode(lotteryAwardRuleCountType));
            if (rule.getLotteryAwardRuleCountType().equals(LotteryAwardRuleCountType.CUSTOM)) {//自定义时间段

                List<LotteryAwardRuleCustom> lotteryAwardRuleCustomList = new ArrayList<LotteryAwardRuleCustom>();
                for (int i = 1; i <= add_lottery_number; i++) {
                    String startTime = request.getParameter("startTime" + i);
                    String endTime = request.getParameter("endTime" + i);
                    String times = request.getParameter("times" + i);
                    if (!StringUtil.isEmpty(startTime) && !StringUtil.isEmpty(endTime) && !StringUtil.isEmpty(times)) {
                        LotteryAwardRuleCustom custom = new LotteryAwardRuleCustom();
                        custom.setStartTime(DateUtil.formatStringToDate(startTime, DateUtil.PATTERN_DATE_TIME));
                        custom.setEndTime(DateUtil.formatStringToDate(endTime, DateUtil.PATTERN_DATE_TIME));
                        custom.setTimes(Integer.valueOf(times));
                        lotteryAwardRuleCustomList.add(custom);
                    }
                }

                if (!CollectionUtil.isEmpty(lotteryAwardRuleCustomList)) {
                    rule.setCountRule(lotteryAwardRuleCustomList);
                }
            }


            award.setAwardRule(rule);

            award.setLotteryAwardType(LotteryAwardType.getByCode(awardType));
            award.setLotteryAwardAmount(awardAmount);
            award.setCreateDate(createdate);
            award.setLastModifyDate(lastmodifydate);
            award.setCreateIp(ip);
            if (LotteryAwardType.GOODS.getCode() == awardType) {
                award.setValidStatus(ValidStatus.VALID);
                award.setLotteryAwardRestAmount(awardAmount);
            } else if (LotteryAwardType.VIRTUAL.getCode() == awardType) {
                award.setValidStatus(ValidStatus.INVALID);
            } else {
                award.setValidStatus(ValidStatus.VALID);
                award.setLotteryAwardRestAmount(awardAmount);
            }


            LotteryServiceSngl.get().createLotteryAward(award);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("forward:/lottery/award/createpage", mapMessage);
        }

        return new ModelAndView("redirect:/lottery/award/list?lotteryid=" + lotteryId, mapMessage);
    }

    //
    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyPage(@RequestParam(value = "lotteryid", required = true) Long lotteryId,
                                   @RequestParam(value = "lotteryawardid", required = true) Long lotteryAwardId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        if (lotteryAwardId == null) {
            mapMessage = putErrorMessage(mapMessage, "lottery.lotteryAwardId.empty");
            return new ModelAndView("forward:/lottery/award/list", mapMessage);
        }

        try {
            List<Lottery> lotteryList = LotteryServiceSngl.get().queryLottery(new QueryExpress().add(QueryCriterions.ne(LotteryField.VALID_STATUS, ValidStatus.REMOVED.getCode())));
            if (CollectionUtil.isEmpty(lotteryList)) {
                mapMessage = putErrorMessage(mapMessage, "lottery.lotteryId.empty");
                return new ModelAndView("forward:/lottery/award/list?lotteryid=" + lotteryId, mapMessage);
            }

            mapMessage.put("lotteryList", lotteryList);

            Lottery lottery = LotteryServiceSngl.get().getLotteryById(lotteryId);
            if (lottery == null) {
                return new ModelAndView("redirect:/lottery/award/list?lotteryid=" + lotteryId);
            }

            mapMessage.put("lottery", lottery);

            //根据lottery的award_level_count去取awardLevel
            int count = lottery.getAwardLevelCount();
            List<AwardLevel> levelList = new ArrayList<AwardLevel>();
            Collection<AwardLevel> coll = AwardLevel.getAll();
            for (AwardLevel level : coll) {
                levelList.add(level);
                if (levelList.size() >= count)
                    break;
            }
            mapMessage.put("levelList", levelList);

            mapMessage.put("awardtypecollection", LotteryAwardType.getAll());

            mapMessage.put("statuscollection", ValidStatus.getAll());

            LotteryAward lotteryAward = LotteryServiceSngl.get().getLotteryAwardById(lotteryAwardId);
            if (lotteryAward == null) {
                mapMessage = putErrorMessage(mapMessage, "lottery.lotteryAwardId.empty");
                return new ModelAndView("forward:/lottery/award/list?lotteryid=" + lotteryId, mapMessage);
            }

            LotteryAwardRule rule = lotteryAward.getAwardRule();
            if (rule != null) {
                List<LotteryAwardRuleCustom> customList = rule.getCountRule();

                if (!CollectionUtil.isEmpty(customList)) {
                    List<LotteryAwardRuleCustom> newCustomList = new ArrayList<LotteryAwardRuleCustom>();
                    for (LotteryAwardRuleCustom custom : customList) {
                        custom.setEndTime(new Timestamp(custom.getEndTime().getTime()));
                        custom.setStartTime(new Timestamp(custom.getStartTime().getTime()));
                        newCustomList.add(custom);
                    }
                    lotteryAward.getAwardRule().setCountRule(newCustomList);
                }
            }

            mapMessage.put("lotteryAward", lotteryAward);
            mapMessage.put("lotteryAwardRuleCountType", LotteryAwardRuleCountType.getAll());
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("forward:/lottery/award/list?lotteryid=" + lotteryId, mapMessage);
        }
        return new ModelAndView("/lottery/modifylotteryaward", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "lotteryid", required = false) Long lotteryId,
                               @RequestParam(value = "awardname", required = false) String awardName,
                               @RequestParam(value = "awarddesc", required = false) String awardDesc,
                               @RequestParam(value = "awardlevel", required = false) Integer awardLevel,
                               @RequestParam(value = "awardtype", required = false) Integer awardType,
                               @RequestParam(value = "awardamount", required = false) Integer awardAmount,
                               @RequestParam(value = "createdate", required = false) String createdatestr,
                               @RequestParam(value = "lastmodifydate", required = false) String lastmodifydatestr,
                               @RequestParam(value = "lottery_lotteryruleactiontype", required = false) Integer lottery_lotteryruleactiontype,
                               @RequestParam(value = "minrate", required = false) String minrate,
                               @RequestParam(value = "maxrate", required = false) String maxrate,
                               @RequestParam(value = "lotteryAwardRuleCountType", required = false) Integer lotteryAwardRuleCountType,
                               @RequestParam(value = "lottery_award_count_number", required = false) String lottery_award_count_number,
                               @RequestParam(value = "add_lottery_number", required = false) Integer add_lottery_number,
                               @RequestParam(value = "lotteryAwardId", required = false) Integer lotteryAwardId,
                               @RequestParam(value = "validstatus", required = false) String validstatus,

                               HttpServletRequest request) throws ParseException {

        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            Date createdate = DateUtil.formatStringToDate(createdatestr, DateUtil.PATTERN_DATE_TIME);
            Date lastmodifydate = DateUtil.formatStringToDate(lastmodifydatestr, DateUtil.PATTERN_DATE_TIME);

            UpdateExpress updateExpress = new UpdateExpress();

            Date date = new Date();
            String ip = getIp();

            updateExpress.set(LotteryAwardField.LOTTERY_ID, lotteryId);
            updateExpress.set(LotteryAwardField.LOTTERY_AWARD_NAME, awardName);
            updateExpress.set(LotteryAwardField.LOTTERY_AWARD_DESC, awardDesc);
            updateExpress.set(LotteryAwardField.LOTTERY_AWARD_LEVEL, awardLevel);
            updateExpress.set(LotteryAwardField.LOTTERY_AWARD_TYPE, awardType);
            updateExpress.set(LotteryAwardField.VALID_STATUS, validstatus);
            updateExpress.set(LotteryAwardField.LOTTERY_AWARD_AMOUNT, awardAmount);
            updateExpress.set(LotteryAwardField.CREATEDATE, createdate);
            updateExpress.set(LotteryAwardField.LASTMODIFYDATE, lastmodifydate);
            updateExpress.set(LotteryAwardField.LASTMODIFYIP, ip);

            LotteryAwardRule rule = new LotteryAwardRule();
            LotteryRuleActionType lotteryRuleActionType = LotteryRuleActionType.getByCode(lottery_lotteryruleactiontype);
            //放入中奖号码
            if (awardAmount > 0) {
                List<Integer> rangeList = new ArrayList<Integer>();
                Random random = new Random();


                if (lotteryRuleActionType.equals(LotteryRuleActionType.RANDOM)) {   //按随机数
                    int min = Integer.valueOf(minrate);
                    int max = Integer.valueOf(maxrate);
                    for (int i = 0; i < awardAmount; i++) {
                        rangeList.add(random.nextInt(max - min + 1) + min);
                    }
                    rule.setMin(minrate);
                    rule.setMax(maxrate);
                    rule.setText(lottery_award_count_number);
                } else if (lotteryRuleActionType.equals(LotteryRuleActionType.TIMESTAMP)) {//按时间戳

                    Date minrateDate = DateUtil.formatStringToDate(minrate, DateUtil.PATTERN_DATE_TIME);
                    Date maxrateDate = DateUtil.formatStringToDate(maxrate, DateUtil.PATTERN_DATE_TIME);
                    int min_rate = Long.valueOf(minrateDate.getTime() / 1000).intValue();
                    int max_rate = Long.valueOf(maxrateDate.getTime() / 1000).intValue();
                    for (int i = 0; i < awardAmount; i++) {
                        rangeList.add(random.nextInt(max_rate - min_rate + 1) + min_rate);
                    }
                    rule.setMin(minrate);
                    rule.setMax(maxrate);
                    rule.setText(lottery_award_count_number);

                } else if (lotteryRuleActionType.equals(LotteryRuleActionType.TIMES)) {//按次数
                    String arrLotteryCode[] = minrate.split(",");
                    for (String str : arrLotteryCode) {
                        rangeList.add(Integer.valueOf(str));
                    }
                    rule.setMin(minrate);
                    rule.setMax("");
                    rule.setText(lottery_award_count_number);
                }
                rule.setRangeList(rangeList);
            }
            //奖品次数类型:
            rule.setLotteryAwardRuleCountType(LotteryAwardRuleCountType.getByCode(lotteryAwardRuleCountType));
            if (rule.getLotteryAwardRuleCountType().equals(LotteryAwardRuleCountType.CUSTOM)) {//自定义时间段

                List<LotteryAwardRuleCustom> lotteryAwardRuleCustomList = new ArrayList<LotteryAwardRuleCustom>();
                for (int i = 1; i <= add_lottery_number; i++) {
                    String startTime = request.getParameter("startTime" + i);
                    String endTime = request.getParameter("endTime" + i);
                    String times = request.getParameter("times" + i);
                    if (!StringUtil.isEmpty(startTime) && !StringUtil.isEmpty(endTime) && !StringUtil.isEmpty(times)) {
                        LotteryAwardRuleCustom custom = new LotteryAwardRuleCustom();
                        custom.setStartTime(DateUtil.formatStringToDate(startTime, DateUtil.PATTERN_DATE_TIME));
                        custom.setEndTime(DateUtil.formatStringToDate(endTime, DateUtil.PATTERN_DATE_TIME));
                        custom.setTimes(Integer.valueOf(times));
                        rule.setText("");
                        lotteryAwardRuleCustomList.add(custom);
                    }
                }

                if (!CollectionUtil.isEmpty(lotteryAwardRuleCustomList)) {
                    rule.setCountRule(lotteryAwardRuleCustomList);
                }
            }
            updateExpress.set(LotteryAwardField.LOTTERY_AWARD_RULE, rule == null ? "" : rule.toJson());


            LotteryServiceSngl.get().modifyLotteryAwardById(updateExpress, lotteryAwardId);


        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("forward:/lottery/award/modifypage", mapMessage);
        }
        return new ModelAndView("redirect:/lottery/award/list?lotteryid=" + lotteryId);
    }

    //
    @RequestMapping(value = "/delete")
    public ModelAndView delete(@RequestParam(value = "lotteryid", required = true) Long lotteryId,
                               @RequestParam(value = "lotteryawardid", required = true) Long lotteryAwardId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(LotteryAwardField.VALID_STATUS, ValidStatus.REMOVED.getCode());
        try {
            LotteryServiceSngl.get().modifyLotteryAwardById(updateExpress, lotteryAwardId);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("redirect:/lottery/award/list?lotteryid=" + lotteryId);
        }
        return new ModelAndView("redirect:/lottery/award/list?lotteryid=" + lotteryId);
    }

    @RequestMapping(value = "/recover")
    public ModelAndView recover(@RequestParam(value = "lotteryid", required = true) Long lotteryId,
                                @RequestParam(value = "lotteryawardid", required = true) Long lotteryAwardId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(LotteryAwardField.VALID_STATUS, ValidStatus.VALID.getCode());
        try {
            LotteryServiceSngl.get().modifyLotteryAwardById(updateExpress, lotteryAwardId);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("redirect:/lottery/award/list?lotteryid=" + lotteryId);
        }
        return new ModelAndView("redirect:/lottery/award/list?lotteryid=" + lotteryId);
    }

}
