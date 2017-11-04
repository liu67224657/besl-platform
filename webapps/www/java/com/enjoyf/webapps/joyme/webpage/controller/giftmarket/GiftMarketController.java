package com.enjoyf.webapps.joyme.webpage.controller.giftmarket;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.content.*;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.point.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.webapps.joyme.dto.activity.ActivityDTO;
import com.enjoyf.webapps.joyme.dto.giftmarket.ConsumeRankDTO;
import com.enjoyf.webapps.joyme.dto.giftmarket.CreditDetailDTO;
import com.enjoyf.webapps.joyme.weblogic.giftmarket.GiftMarketWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-25
 * Time: 上午10:26
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/giftmarket")
public class GiftMarketController extends AbstractGiftMarketBaseController {

    @Resource(name = "giftMarketWebLogic")
    private GiftMarketWebLogic giftMarketWebLogic;

    private final String APPKEY = "default";

    @RequestMapping
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response,
                              @RequestParam(value = "p", required = false) String platform) {

        return new ModelAndView("redirect:/gift");
    }

    /**
     * 积分明细记录 todo 是否还要放在giftmarket下面？
     *
     * @param request
     * @param response
     * @param page
     * @return
     */
    @RequestMapping(value = "/creditdetail")
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "p", required = false) Integer page) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();
        UserCenterSession userSession = getUserCenterSeesion(request);
        if (userSession == null) {
            return new ModelAndView("redirect:/giftmarket");
        }
        try {
            //右侧 今日已赚积分
            int dayPoint = getUserDayPoint(userSession);
            mapMessage.put("dayPoint", dayPoint);
            //用户积分
            int pointAmount = getUserPointAmount(userSession);
            mapMessage.put("pointAmount", pointAmount);
            //右侧 七天排行榜
            List<ActivityHotRanks> ranksList = PointServiceSngl.get().queryActivityHot(new QueryExpress().add(QueryCriterions.eq(ActivityHotRanksField.REMOVE_STATUS, ActStatus.UNACT.getCode()))
                    .add(QuerySort.add(ActivityHotRanksField.EXCHANGE_NUM, QuerySortOrder.DESC)));
            mapMessage.put("ranksList", ranksList);
            //右侧 大家正在领
            List<UserRecentLogEntry> recentLogList = queryUserRecentLog();
            mapMessage.put("recentLogList", recentLogList);

            PageRows<CreditDetailDTO> pageRows = queryCreditDetail(userSession, page);
            if (pageRows == null || CollectionUtil.isEmpty(pageRows.getRows())) {
                return new ModelAndView("/views/jsp/giftmarket/creditdetailnone", mapMessage);
            }
            mapMessage.put("creditDetailList", pageRows.getRows());
            mapMessage.put("page", pageRows.getPage());
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/common/custompage", mapMessage);
        }
        return new ModelAndView("/views/jsp/giftmarket/creditdetail", mapMessage);
    }

}