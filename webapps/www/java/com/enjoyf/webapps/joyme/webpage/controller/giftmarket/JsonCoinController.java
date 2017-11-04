package com.enjoyf.webapps.joyme.webpage.controller.giftmarket;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.content.*;
import com.enjoyf.platform.service.message.Message;
import com.enjoyf.platform.service.message.MessageServiceSngl;
import com.enjoyf.platform.service.message.MessageType;
import com.enjoyf.platform.service.point.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.platform.webapps.common.JoymeResultMsg;
import com.enjoyf.webapps.joyme.dto.activity.ActivityDTO;
import com.enjoyf.webapps.joyme.dto.goods.GoodsSimpleDTO;
import com.enjoyf.webapps.joyme.weblogic.giftmarket.AllowExchangeStatus;
import com.enjoyf.webapps.joyme.weblogic.giftmarket.GiftMarketWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterSession;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
@RequestMapping(value = "/json/coin")
public class JsonCoinController extends AbstractGiftMarketBaseController {

    private JsonBinder jsonBinder = JsonBinder.buildNormalBinder();

    @Resource(name = "i18nSource")
    private ResourceBundleMessageSource i18nSource;


    @Resource(name = "giftMarketWebLogic")
    private GiftMarketWebLogic giftMarketWebLogic;

    @ResponseBody
    @RequestMapping
    public String query(HttpServletRequest request, HttpServletResponse response,
                        @RequestParam(value = "category", required = false) Integer goodsCategory) {
        JoymeResultMsg resultMsg = new JoymeResultMsg(JoymeResultMsg.CODE_S);
        try {
            PageRows<ActivityDTO> goodsActivityPageRows = giftMarketWebLogic.queryActivityDTOs(new QueryExpress()
                    .add(QueryCriterions.eq(ActivityField.ACTIVITY_TYPE, ActivityType.GOODS.getCode()))
                    .add(QueryCriterions.eq(ActivityField.REMOVE_STATUS, ActStatus.ACTED.getCode()))
                    .add(QueryCriterions.bitwiseAnd(ActivityField.GOODS_CATEGORY, QueryCriterionRelation.GT, goodsCategory, 0))
                    .add(QuerySort.add(ActivityField.DISPLAY_ORDER, QuerySortOrder.ASC))
                    , new Pagination(giftMarketGoodsSize, pageNo, giftMarketGoodsSize));
            if (goodsActivityPageRows == null || CollectionUtil.isEmpty(goodsActivityPageRows.getRows())) {
                resultMsg.setResult(new ArrayList());
            } else {
                resultMsg.setResult(goodsActivityPageRows.getRows());
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
            resultMsg.setMsg(i18nSource.getMessage("system.error", null, Locale.CHINA));
        }
        return jsonBinder.toJson(resultMsg);
    }


    @ResponseBody
    @RequestMapping(value = "/exchange")
    public String exchange(HttpServletRequest request, HttpServletResponse response,
                           @RequestParam(value = "gsid", required = true) long goodsId,
                           @RequestParam(value = "aid", required = true) long activityId) {
        JoymeResultMsg resultMsg = new JoymeResultMsg(JoymeResultMsg.CODE_S);

        UserCenterSession userCenterSession = getUserCenterSeesion(request);
        if (userCenterSession == null) {
            resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
            resultMsg.setMsg(i18nSource.getMessage("user.not.login", null, Locale.CHINA));
            return jsonBinder.toJson(resultMsg);
        }
        ActivityGoods activityGoods=null;
        try {
             activityGoods= PointServiceSngl.get().getActivityGoods(goodsId);
            if (activityGoods == null) {
                resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
                resultMsg.setMsg(i18nSource.getMessage("activity.is.null", null, Locale.CHINA));
                return jsonBinder.toJson(resultMsg);
            }
            if ((activityGoods.getEndTime().getTime() - System.currentTimeMillis()) < 0) {
                resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
                resultMsg.setMsg(i18nSource.getMessage("activity.has.expire", null, Locale.CHINA));
                return jsonBinder.toJson(resultMsg);
            }

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e", e);
            resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
            resultMsg.setMsg(i18nSource.getMessage("system.error", null, Locale.CHINA));
        }

        UserConsumeLog userConsumeLog = null;

        try {

            UserPoint userPoint = PointServiceSngl.get().getUserPoint(new QueryExpress().add(QueryCriterions.eq(UserPointField.USERNO, userCenterSession.getUno())));
            //todo 放到logic
            if(userPoint == null){
                userPoint = new UserPoint();
                userPoint.setUserNo(userCenterSession.getUno());
                userPoint.setProfileId(userCenterSession.getProfileId());
                userPoint.setConsumeAmount(0);
                userPoint.setConsumeExchange(0);
                userPoint.setUserPoint(0);
                userPoint.setPointKey(PointKeyType.WWW.getValue());
                PointServiceSngl.get().addUserPoint(userPoint);
            }
            int userHasPoint = userPoint == null ? 0 : userPoint.getUserPoint();
            if (userHasPoint < activityGoods.getGoodsPoint()) {
                resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
                resultMsg.setMsg(i18nSource.getMessage("point.not.enough", null, Locale.CHINA));
                return jsonBinder.toJson(resultMsg);
            }

            AllowExchangeStatus allowExchangeStatus = giftMarketWebLogic.allowExchangeGoods(activityGoods, userCenterSession.getUno());

            if (!AllowExchangeStatus.ALLOW.equals(allowExchangeStatus)) {
                if (AllowExchangeStatus.HAS_EXCHANGED_BY_DAY.equals(allowExchangeStatus)) {
                    resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
                    resultMsg.setMsg(i18nSource.getMessage("goods.has.exchanged.day", null, Locale.CHINA));
                } else {
                    resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
                    resultMsg.setMsg(i18nSource.getMessage("goods.has.exchanged", null, Locale.CHINA));
                }
                return jsonBinder.toJson(resultMsg);
            }
            //兑换的事务
            userConsumeLog = PointServiceSngl.get().consumeGoods(userCenterSession.getUno(), userCenterSession.getProfileId(),userCenterSession.getAppkey(),goodsId, new Date(), getIp(request),GoodsActionType.WWW,"");

            //事件 modify activity enevt_time to new date
            sendOutEvent(activityId);

            //todo
            userCenterSession.setPointAmount(userHasPoint - activityGoods.getGoodsPoint());


            //分享
//            if (goods.getShareId() > 0) {
//                sendOutShareInfo(userCenterSession, goods.getShareId());
//            }

            GoodsItem goodsItem = null;
            if (userConsumeLog.getGoodsItemId() > 0) {
                goodsItem = PointServiceSngl.get().getGoodsItemById(userConsumeLog.getGoodsItemId());
            }
            //站内通知
//            pushNotice(userCenterSession.getUno(), goods, goodsItem);

            resultMsg.setResult(new ArrayList<GoodsSimpleDTO>());
            resultMsg.getResult().add(genDTO(activityGoods, goodsItem));
        } catch (ServiceException e) {
            resultMsg.setStatus_code(JoymeResultMsg.CODE_E);

            if (e.equals(PointServiceException.GOODS_NOT_EXISTS)) {
                resultMsg.setMsg(i18nSource.getMessage("goods.not.exists", null, Locale.CHINA));
            } else if (e.equals(PointServiceException.GOODS_GET_FAILED)) {
                resultMsg.setMsg(i18nSource.getMessage("goods.get.failed", null, Locale.CHINA));
            } else if (e.equals(PointServiceException.GOODS_OUTOF_RESTAMMOUNT)) {
                resultMsg.setMsg(i18nSource.getMessage("goods.out.of.rest.amount", null, Locale.CHINA));
            } else if (e.equals(PointServiceException.GOODS_ITEM_NOT_EXISTS)) {
                resultMsg.setMsg(i18nSource.getMessage("goods.item.not.exists", null, Locale.CHINA));
            } else if (e.equals(PointServiceException.GOODS_ITEM_GET_FAILED)) {
                resultMsg.setMsg(i18nSource.getMessage("goods.item.not.exists", null, Locale.CHINA));
            } else if (e.equals(PointServiceException.USER_POINT_NOT_ENOUGH)) {
                resultMsg.setMsg(i18nSource.getMessage("point.not.enough", null, Locale.CHINA));
            } else {
                resultMsg.setMsg(i18nSource.getMessage("system.error", null, Locale.CHINA));
                GAlerter.lab(this.getClass().getName() + " occured ServicdeExcpetion.e", e);
            }
        }
        return jsonBinder.toJson(resultMsg);
    }

    private GoodsSimpleDTO genDTO(ActivityGoods goods, GoodsItem goodsItem) {
        GoodsSimpleDTO dto = new GoodsSimpleDTO();
        dto.setGoodsId(goods.getActivityGoodsId());
        dto.setGoodsName(goods.getActivitySubject());
        dto.setGoodsType(goods.getActivitygoodsType().getCode());

        if (goodsItem != null) {
            dto.setGoodsItemId(goodsItem.getGoodsItemId());
            dto.setItemName1(goodsItem.getSnName1());
            dto.setItemValue1(goodsItem.getSnValue1());
            dto.setItemName2(goodsItem.getSnName2());
            dto.setItemValue2(goodsItem.getSnValue2());
        }

        return dto;
    }

    private void pushNotice(String uno, Goods goods, GoodsItem goodsItem) {
        try {
            Map<String, String> paramMap = new HashMap<String, String>();

            String noticeTemplate = goods.getNoticeBody();

            paramMap.put("goodsname", goods.getGoodsName());
            String messageBody = "";
            if (goods.getGoodsType().equals(GoodsType.GOODS)) {
                messageBody = NamedTemplate.parse(noticeTemplate).format(paramMap);
            } else if (goodsItem != null) {
                StringBuilder sb = new StringBuilder();
                sb.append(goodsItem.getSnName1()).append("：").append(goodsItem.getSnValue1());
                if (!StringUtil.isEmpty(goodsItem.getSnName2()) && !StringUtil.isEmpty(goodsItem.getSnName2())) {
                    sb.append(" ").append(goodsItem.getSnName2()).append("：").append(goodsItem.getSnValue2());
                }
                paramMap.put("key", sb.toString());
                messageBody = NamedTemplate.parse(noticeTemplate).format(paramMap);
            }

            Message message = new Message();
            message.setBody(messageBody);
            message.setMsgType(MessageType.OPERATION);
            message.setOwnUno(uno);
            message.setRecieverUno(uno);
            message.setSendDate(new Date());

            MessageServiceSngl.get().postMessage(uno, message);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Excpetion.e", e);
        }
    }
}