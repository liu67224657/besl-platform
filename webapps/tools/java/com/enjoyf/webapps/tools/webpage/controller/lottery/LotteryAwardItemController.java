package com.enjoyf.webapps.tools.webpage.controller.lottery;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.lottery.*;
import com.enjoyf.platform.service.profile.Profile;
import com.enjoyf.platform.service.profile.ProfileBlog;
import com.enjoyf.platform.service.profile.ProfileBlogField;
import com.enjoyf.platform.service.profile.ProfileServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
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
 * User: ericliu
 * Date: 13-5-31
 * Time: 上午9:43
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/lottery/awarditem")
public class LotteryAwardItemController extends ToolsBaseController {

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,      //数据库记录索引
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                             @RequestParam(value = "lotteryid", required = false) Long lotteryId,
                             @RequestParam(value = "lotteryawardid", required = false) Long lotteryAwardId) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            List<Lottery> lotteryList = LotteryServiceSngl.get().queryLottery(new QueryExpress().add(QueryCriterions.ne(LotteryField.VALID_STATUS, ValidStatus.REMOVED.getCode())));
            mapMessage.put("lotteryList", lotteryList);

            if (lotteryId == null) {
                return new ModelAndView("/lottery/lotteryawarditemlist", mapMessage);
            }
            mapMessage.put("lotteryId", lotteryId);

            List<LotteryAward> awardList = LotteryServiceSngl.get().queryLotteryAward(new QueryExpress()
                    .add(QueryCriterions.eq(LotteryAwardField.LOTTERY_AWARD_TYPE, LotteryAwardType.VIRTUAL.getCode()))
                    .add(QueryCriterions.ne(LotteryAwardField.VALID_STATUS, ValidStatus.REMOVED.getCode()))
                    .add(QueryCriterions.eq(LotteryAwardField.LOTTERY_ID, lotteryId)));
            mapMessage.put("awardList", awardList);

            if (lotteryAwardId == null) {
                return new ModelAndView("/lottery/lotteryawarditemlist", mapMessage);
            }
            mapMessage.put("lotteryAwardId", lotteryAwardId);

            int curPage = (pageStartIndex / pageSize) + 1;
            Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);

            PageRows<LotteryAwardItem> pageRows = LotteryServiceSngl.get().queryByLotteryAwardIdPage(lotteryAwardId, pagination);
            Set<String> unoSet = new HashSet<String>();
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                for (LotteryAwardItem item : pageRows.getRows()) {
                    if (!StringUtil.isEmpty(item.getOwnUserNo())) {
                        unoSet.add(item.getOwnUserNo());
                    }
                }
            }
            List<Profile> profileList = new ArrayList<Profile>();
            if (!CollectionUtil.isEmpty(unoSet)) {
                profileList = ProfileServiceSngl.get().queryProfilesByUnos(unoSet);
                if (!CollectionUtil.isEmpty(profileList)) {
                    mapMessage.put("profileList", profileList);
                }
            }

            mapMessage.put("profileList", profileList);
            mapMessage.put("list", pageRows.getRows());
            mapMessage.put("page", pageRows.getPage());

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/lottery/lotteryawarditemlist", mapMessage);
        }

        return new ModelAndView("/lottery/lotteryawarditemlist", mapMessage);
    }


    //
    @RequestMapping(value = "/createpage")
    public ModelAndView createPage(@RequestParam(value = "lotteryid", required = false) Long lotteryId,
                                   @RequestParam(value = "lotteryawardid", required = false) Long lotteryAwardId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        if (lotteryId == null) {
            mapMessage = putErrorMessage(mapMessage, "lottery.lotteryId.empty");
            return new ModelAndView("redirect:/lottery/awarditem/list?lotteryawardid=" + lotteryAwardId + "&lotteryid=" + lotteryId);
        }

        mapMessage.put("lotteryId", lotteryId);

        if (lotteryAwardId == null) {
            mapMessage = putErrorMessage(mapMessage, "lottery.lotteryAwardId.empty");
            return new ModelAndView("redirect:/lottery/awarditem/list?lotteryawardid=" + lotteryAwardId + "&lotteryid=" + lotteryId);
        }
        mapMessage.put("lotteryAwardId", lotteryAwardId);
        try {
            LotteryAward lotteryAward = LotteryServiceSngl.get().getLotteryAwardById(lotteryAwardId);
            if (lotteryAward == null) {
                mapMessage = putErrorMessage(mapMessage, "lottery.lotteryAwardId.empty");
                return new ModelAndView("redirect:/lottery/awarditem/list?lotteryawardid=" + lotteryAwardId + "&lotteryid=" + lotteryId);
            }
            mapMessage.put("lotteryAward", lotteryAward);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("redirect:/lottery/awarditem/list?lotteryawardid=" + lotteryAwardId + "&lotteryid=" + lotteryId, mapMessage);
        }

        return new ModelAndView("/lottery/batchcreatelotteryawarditem", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "lotteryid", required = false) Long lotteryId,
                               @RequestParam(value = "lotteryawardid", required = true) Long lotteryAwardId,
                               @RequestParam(value = "name1", required = true) String name1,
                               @RequestParam(value = "value1", required = true) String value1,
                               @RequestParam(value = "name2", required = false) String name2,
                               @RequestParam(value = "value2", required = false) String value2) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        if (lotteryAwardId == null) {
            mapMessage = putErrorMessage(mapMessage, "lottery.lotteryAwardId.empty");
            return new ModelAndView("redirect:/lottery/awarditem/list");
        }

        try {
            LotteryAward lotteryAward = LotteryServiceSngl.get().getLotteryAwardById(lotteryAwardId);

            if (lotteryAward == null) {
                mapMessage.put("lotteryAwardName", lotteryAward.getLotteryAwardName());
                mapMessage.put("lotteryAwardId", lotteryAward.getLotteryAwardId());
                return new ModelAndView("redirect:/lottery/awarditem/list?lotteryawardid=" + lotteryAwardId, mapMessage);
            }

            int allowCreateNum = lotteryAward.getLotteryAwardAmount() - lotteryAward.getLotteryAwardRestAmount();

            String[] value1Array = value1.split("\n");
            if (value1Array.length > allowCreateNum) {
                return new ModelAndView("redirect:/lottery/log/createpage");
            }
            String[] value2Array = null;
            if (!StringUtil.isEmpty(value2)) {
                value2Array = value2.split("\n");
                if (value1Array.length != value2Array.length) {
                    mapMessage.put("errorMsg", "value1.length.noteq.value2");
                    return new ModelAndView("/lottery/batchcreatelotteryawarditem", mapMessage);
                }

                if (StringUtil.isEmpty(name2)) {
                    mapMessage.put("errorMsg", "value2.name.empty");
                    return new ModelAndView("/lottery/batchcreatelotteryawarditem", mapMessage);
                }
            }

            List<LotteryAwardItem> lotteryAwardItemList = new ArrayList<LotteryAwardItem>();
            for (int i = 0; i < value1Array.length; i++) {
                LotteryAwardItem lotteryAwardItem = new LotteryAwardItem();
                lotteryAwardItem.setLotteryAwardId(lotteryAwardId);
                lotteryAwardItem.setName1(name1);
                lotteryAwardItem.setValue1(value1Array[i].replace("\r", "").trim());

                if (!CollectionUtil.isEmpty(value2Array)) {
                    lotteryAwardItem.setName2(name2);
                    lotteryAwardItem.setValue2(value2Array[i].replace("\r", "").trim());
                }

                lotteryAwardItem.setLotteryStatus(ValidStatus.VALID);
                lotteryAwardItem.setCreateDate(new Date());
                lotteryAwardItemList.add(lotteryAwardItem);
            }

            boolean bval = LotteryServiceSngl.get().createLotteryAwardItem(lotteryAwardId,lotteryAwardItemList);


        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("forward:/lottery/awarditem/createpage", mapMessage);

        }

        return new ModelAndView("redirect:/lottery/awarditem/list?lotteryawardid=" + lotteryAwardId + "&lotteryid=" + lotteryId);
    }

    @RequestMapping(value = "received")
    public ModelAndView received(@RequestParam(value = "awarditemid", required = true) Long lotteryAwardItemId,
                                 @RequestParam(value = "lid", required = false) Long lotteryId,
                                 @RequestParam(value = "awardid", required = false) Long lotteryAwardId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        if (lotteryAwardItemId == 0) {
            return new ModelAndView("redirect:/lottery/awarditem/list?lotteryawardid=" + lotteryAwardId + "&lotteryid=" + lotteryId);
        }
        try {
            LotteryAwardItem lotteryAwardItem = LotteryServiceSngl.get().getLotteryAwardItemById(lotteryAwardItemId);
            if (lotteryAwardItem == null) {
                return new ModelAndView("redirect:/lottery/awarditem/list?lotteryawardid=" + lotteryAwardId + "&lotteryid=" + lotteryId);
            }

            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(LotteryAwardItemField.LOTTERY_STATUS, ValidStatus.REMOVED.getCode());
            LotteryServiceSngl.get().modifyLotteryAwardItemById(updateExpress, lotteryAwardItemId);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("redirect:/lottery/awarditem/list?lotteryawardid=" + lotteryAwardId + "&lotteryid=" + lotteryId);
        }
        return new ModelAndView("redirect:/lottery/awarditem/list?lotteryawardid=" + lotteryAwardId + "&lotteryid=" + lotteryId);
    }
}
