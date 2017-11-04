package com.enjoyf.webapps.joyme.webpage.controller.giftmarket.giftmarketwap;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.content.ActivityType;
import com.enjoyf.platform.service.content.MobileExclusive;
import com.enjoyf.platform.service.point.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.LoginDomain;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.QuerySort;
import com.enjoyf.platform.util.sql.QuerySortOrder;
import com.enjoyf.platform.webapps.common.*;
import com.enjoyf.webapps.joyme.dto.activity.ActivityDTO;
import com.enjoyf.webapps.joyme.dto.activity.ActivityMygiftDTO;
import com.enjoyf.webapps.joyme.weblogic.giftmarket.GiftMarketWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterSession;
import com.enjoyf.webapps.joyme.webpage.controller.giftmarket.AbstractGiftMarketBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 14-6-21
 * Time: 下午5:57
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/json/giftmarket/wap")
public class JsonGiftmarketListController extends AbstractGiftMarketBaseController {

    @Resource(name = "giftMarketWebLogic")
    private GiftMarketWebLogic giftMarketWebLogic;

    private JsonBinder jsonBinder = JsonBinder.buildNormalBinder();

    @ResponseBody
    @RequestMapping(value = "/checkbindphone")
    public String checkBindPhone(HttpServletRequest request, HttpServletResponse response) {
        ResultObjectMsg resultMsg = new ResultObjectMsg(ResultListMsg.CODE_S);
        UserCenterSession userSession = getUserCenterSeesion(request);
        if (userSession == null) {
            resultMsg.setMsg(JoymeResultMsg.CODE_E);
            return jsonBinder.toJson(resultMsg);
        }
        if (StringUtil.isEmpty(userSession.getMobile())) {
            resultMsg.setMsg(JoymeResultMsg.USER_IS_NOT_BIND_PHONE);
            return jsonBinder.toJson(resultMsg);
        }

        return jsonBinder.toJson(resultMsg);
    }

    @ResponseBody
    @RequestMapping(value = "/checkphoneisbind")
    public String checkBindIsBind(@RequestParam(value = "phone", required = false) String phone,
                                  @RequestParam(value = "profilekey", required = false, defaultValue = "wxdyh") String profileKey) {
        ResultObjectMsg resultMsg = new ResultObjectMsg(ResultListMsg.CODE_S);

        try {
//             account = UserCenterServiceSngl.get().getUserAccountByPhone(phone);
            if (UserCenterServiceSngl.get().checkMobileIsBinded(phone, profileKey)) {
                resultMsg.setMsg(JoymeResultMsg.PHONE_IS_BIND);
            }

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            resultMsg.setRs(ResultListMsg.CODE_E);
            resultMsg.setMsg("system.error");
        }

        return jsonBinder.toJson(resultMsg);

    }

    @ResponseBody
    @RequestMapping(value = "/creategiftreserve")
    public String createGiftReserve(HttpServletRequest request, @RequestParam(value = "reserve", required = false) String reserve,
                                    @RequestParam(value = "aid", required = false) String aid
    ) {
        ResultObjectMsg resultMsg = new ResultObjectMsg(ResultListMsg.CODE_S);
        try {
            UserCenterSession userSession = getUserCenterSeesion(request);
            if (userSession == null) {
                resultMsg.setRs(ResultListMsg.CODE_E);
                resultMsg.setMsg(JoymeResultMsg.USERSSION_IS_NULL);
                return jsonBinder.toJson(resultMsg);
            }
            GiftReserve giftReserve = new GiftReserve();
            if (!StringUtil.isEmpty(aid)) {
                giftReserve = PointServiceSngl.get().getGiftReserve(new QueryExpress()
                        .add(QueryCriterions.eq(GiftReserveField.UNO, userSession.getUno()))
                        .add(QueryCriterions.eq(GiftReserveField.AID, Long.parseLong(aid))));
                if (giftReserve != null) {
                    resultMsg.setRs(ResultListMsg.CODE_E);
                    resultMsg.setMsg(JoymeResultMsg.CODE_E);
                    return jsonBinder.toJson(resultMsg);
                }
                giftReserve = new GiftReserve();
                giftReserve.setAid(Long.parseLong(aid));
            }

            giftReserve.setGiftName(reserve);
            giftReserve.setUno(userSession.getUno());
            giftReserve.setCreateIp(getIp(request));
            giftReserve.setCreateTime(new Date());
            giftReserve.setValidStatus(ValidStatus.INVALID);
            giftReserve.setLoginDomain(LoginDomain.WEIXIN);
            PointServiceSngl.get().createGiftReserve(giftReserve);

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            resultMsg.setRs(ResultListMsg.CODE_E);
            resultMsg.setMsg("system.error");
        }

        return jsonBinder.toJson(resultMsg);

    }

    @RequestMapping(value = "/mygift")
    @ResponseBody
    public String myGift(HttpServletRequest request, HttpServletResponse response,
                         @RequestParam(value = "p", required = false) Integer pageNo) {
        ResultPageMsg resultMsg = new ResultPageMsg(ResultPageMsg.CODE_S);
        try {
            pageNo = pageNo == null ? 1 : pageNo;
            Pagination pagination = new Pagination(pageNo * 20, pageNo, 20);
            UserCenterSession userSession = getUserCenterSeesion(request);
            if (userSession == null) {
                resultMsg.setMsg(JoymeResultMsg.USERSSION_IS_NULL);
                jsonBinder.toJson(resultMsg);
            }
            long now = System.currentTimeMillis();
            long lastWeek = 60l * 60l * 24l * 1000 * 7;
            long selectDate = now - lastWeek;

//            pointWebLogic.queryUserExchangeLogByUno(userSession.getBlogwebsite().getUno(), pagination);
            PageRows<ActivityMygiftDTO> pageRows = giftMarketWebLogic.queryActivityMygiftDTO(pagination, new Date(selectDate), new Date(now), userSession.getProfileId(),userSession.getAppkey());

            if (pageRows == null || CollectionUtil.isEmpty(pageRows.getRows())) {
                resultMsg.setMsg("gift.is.null");
                resultMsg.setPage(new JsonPagination(pageRows.getPage()));
                return jsonBinder.toJson(resultMsg);
            }
            resultMsg.setResult(pageRows.getRows());
            resultMsg.setPage(new JsonPagination(pageRows.getPage()));

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            resultMsg.setRs(ResultListMsg.CODE_E);
            resultMsg.setMsg("system.error");
        }
        return jsonBinder.toJson(resultMsg);
    }

    @RequestMapping(value = "/hotactivity")
    @ResponseBody
    public String hotActivity(@RequestParam(value = "pnum", required = false, defaultValue = "1") Integer page,
                              @RequestParam(value = "count", required = false, defaultValue = "10") Integer count,
                              @RequestParam(value = "ordertype", required = false, defaultValue = "1") Integer type) {
        ResultPageMsg resultMsg = new ResultPageMsg(ResultPageMsg.CODE_S);
        Pagination pagination = new Pagination(count * page, page, count);
        QueryExpress queryExpress = new QueryExpress().add(QueryCriterions.in(ActivityGoodsField.DISPLAY_TYPE, new Integer[]{2,3}))
                .add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_TYPE, ActivityType.EXCHANGE_GOODS.getCode()))
                .add(QueryCriterions.eq(ActivityGoodsField.REMOVE_STATUS, ActStatus.ACTED.getCode()))
                .add(QueryCriterions.ne(ActivityGoodsField.CHANNEL_TYPE, MobileExclusive.NEWSCLIENT.getCode()));

        if (type == 1) {
            queryExpress.add(QuerySort.add(ActivityGoodsField.DISPLAY_ORDER, QuerySortOrder.ASC));
        } else {
            queryExpress.add(QuerySort.add(ActivityGoodsField.CREATEDATE, QuerySortOrder.DESC));
        }
        try {
            PageRows<ActivityDTO> pageRows = giftMarketWebLogic.queryActivityDTOs(queryExpress, pagination);
            if (pageRows == null) {
                resultMsg.setRs(ResultListMsg.CODE_E);
                resultMsg.setMsg("gift.is.null");
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }
            resultMsg.setResult(pageRows.getRows());
            resultMsg.setPage(new JsonPagination(pageRows.getPage()));
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            resultMsg.setRs(ResultListMsg.CODE_E);
            resultMsg.setMsg("system.error");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        return JsonBinder.buildNormalBinder().toJson(resultMsg);

    }


}
