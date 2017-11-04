package com.enjoyf.webapps.tools.webpage.controller.lottery;

import com.enjoyf.platform.service.lottery.Lottery;
import com.enjoyf.platform.service.lottery.LotteryField;
import com.enjoyf.platform.service.lottery.LotteryServiceSngl;
import com.enjoyf.platform.service.lottery.UserLotteryLog;
import com.enjoyf.platform.service.profile.ProfileBlog;
import com.enjoyf.platform.service.profile.ProfileServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.QuerySort;
import com.enjoyf.platform.util.sql.QuerySortOrder;
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
@RequestMapping(value = "/lottery/log")
public class UserLotteryLogController extends ToolsBaseController {

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,      //数据库记录索引
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                             @RequestParam(value = "profilename", required = false) String profileName,
                             @RequestParam(value = "startdate", required = false) Date from,
                             @RequestParam(value = "lid", required = false, defaultValue = "0") Long lotteryId,
                             @RequestParam(value = "enddate", required = false) Date to) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();

        if(!StringUtil.isEmpty(profileName)){
            mapMessage.put("profilename", profileName);
        }
        if(from!=null){
            mapMessage.put("from", from);
        }
        mapMessage.put("lotteryId", lotteryId);
        if(to!=null){
            mapMessage.put("to", to);
        }
        try {
            List<Lottery> lotteryList = LotteryServiceSngl.get().queryLottery(new QueryExpress().add(QuerySort.add(LotteryField.CREATE_DATE, QuerySortOrder.DESC)));
            if(!CollectionUtil.isEmpty(lotteryList)){
                mapMessage.put("lotteryList", lotteryList);
            }

            String userNo = "";
            if(!StringUtil.isEmpty(profileName)){
                Profile profile = UserCenterServiceSngl.get().getProfileByNick(profileName);
                if (profile == null) {
                    mapMessage = putErrorMessage(mapMessage, "profile.has.notexists");
                    return new ModelAndView("/lottery/userlotteryloglist", mapMessage);
                }
                userNo = profile.getUno();
            }

            int curPage = (pageStartIndex / pageSize) + 1;
            Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);

            PageRows<UserLotteryLog> pageRows = LotteryServiceSngl.get().queryUserLotteryLogByPage(lotteryId, userNo, from, to, pagination);
            List<UserLotteryLog> list = new ArrayList<UserLotteryLog>();
            if(pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())){
                for(UserLotteryLog log:pageRows.getRows()){
//                    Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(log.getUno());
//                    if(profile != null){
//                        log.setScreenName(profile.getNick());
//                        list.add(log);
//                    }
                    list.add(log);
                }
            }
            mapMessage.put("list", list);
            mapMessage.put("page", pageRows.getPage());
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/lottery/userlotteryloglist", mapMessage);
        }
        return new ModelAndView("/lottery/userlotteryloglist", mapMessage);
    }
}