package com.enjoyf.webapps.joyme.webpage.controller.giftmarket;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.point.ActivityHotRanks;
import com.enjoyf.platform.service.point.ActivityHotRanksField;
import com.enjoyf.platform.service.point.PointServiceSngl;
import com.enjoyf.platform.service.point.UserRecentLogEntry;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.QuerySort;
import com.enjoyf.platform.util.sql.QuerySortOrder;
import com.enjoyf.webapps.joyme.dto.activity.ActivityMygiftDTO;
import com.enjoyf.webapps.joyme.weblogic.giftmarket.GiftMarketWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.enjoyf.webapps.joyme.webpage.base.mvc.UserSession;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-25
 * Time: 上午10:26
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/mygift")
public class MyGiftController extends AbstractGiftMarketBaseController {

    @Resource(name = "giftMarketWebLogic")
    private GiftMarketWebLogic giftMarketWebLogic;

    @RequestMapping
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "p", required = false) Integer pageNo) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();

        pageNo = pageNo == null ? 1 : pageNo;
        Pagination pagination = new Pagination(pageNo * myGiftPageSize, pageNo, myGiftPageSize);
        UserCenterSession userCenterSession = getUserCenterSeesion(request);
        if (userCenterSession == null) {
            return new ModelAndView("redirect:/gift");
        }

        try {
            PageRows<ActivityMygiftDTO> pageRows = giftMarketWebLogic.queryActivityMygiftDTO(pagination, null, null, userCenterSession.getProfileId(), userCenterSession.getAppkey());
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                mapMessage.put("list", pageRows.getRows());
                mapMessage.put("page", pageRows.getPage());
            }

            //右侧 今日已赚积分
            int dayPoint = getUserDayPoint(userCenterSession);
            mapMessage.put("dayPoint", dayPoint);
            //用户积分
            int pointAmount = getUserPointAmount(userCenterSession);
            mapMessage.put("pointAmount", pointAmount);
            //右侧 七天排行榜
            List<ActivityHotRanks> ranksList = PointServiceSngl.get().queryActivityHot(new QueryExpress().add(QueryCriterions.eq(ActivityHotRanksField.REMOVE_STATUS, ActStatus.UNACT.getCode()))
                    .add(QuerySort.add(ActivityHotRanksField.EXCHANGE_NUM, QuerySortOrder.DESC)));
            mapMessage.put("ranksList", ranksList);
            //右侧 大家正在领
            List<UserRecentLogEntry> recentLogList = queryUserRecentLog();
            mapMessage.put("recentLogList", recentLogList);
            //上架日志
//            buildCommon(mapMessage);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/common/custompage", mapMessage);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Excpetion.e:", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/common/custompage", mapMessage);
        }
        return new ModelAndView("views/jsp/giftmarket/mygift", mapMessage);
    }

    @RequestMapping(value = "/m")
    public ModelAndView mlist(HttpServletRequest request, HttpServletResponse response,
                              @RequestParam(value = "p", required = false) Integer pageNo) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();

        pageNo = pageNo == null ? 1 : pageNo;
        Pagination pagination = new Pagination(pageNo * myMGiftPageSize, pageNo, myMGiftPageSize);
        UserCenterSession userCenterSession = getUserCenterSeesion(request);
        if (userCenterSession == null) {
            mapMessage.put("userSession", userCenterSession);
            return new ModelAndView("/views/jsp/giftmarket/m/mygift-m", mapMessage);
        }

        try {
            PageRows<ActivityMygiftDTO> pageRows = giftMarketWebLogic.queryActivityMygiftDTO(pagination, null, null, userCenterSession.getProfileId(), userCenterSession.getAppkey());
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                mapMessage.put("list", pageRows.getRows());
                mapMessage.put("page", pageRows.getPage());
            }

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/common/custompage", mapMessage);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Excpetion.e:", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/common/custompage", mapMessage);
        }
        return new ModelAndView("/views/jsp/giftmarket/m/mygift-m", mapMessage);
    }

}