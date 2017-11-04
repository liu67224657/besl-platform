package com.enjoyf.webapps.joyme.webpage.controller.youku.api;

import com.enjoyf.platform.service.IntRemoveStatus;
import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.content.GoodsActionType;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.point.*;
import com.enjoyf.platform.service.point.pointwall.PointwallWallApp;
import com.enjoyf.platform.service.point.pointwall.PointwallWallAppField;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.*;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.platform.webapps.common.JoymeResultMsg;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.platform.webapps.common.ResultPageMsg;
import com.enjoyf.util.StringUtil;
import com.enjoyf.webapps.joyme.dto.goods.GoodsSimpleDTO;
import com.enjoyf.webapps.joyme.dto.youku.YoukuAppDTO;
import com.enjoyf.webapps.joyme.weblogic.giftmarket.AllowExchangeStatus;
import com.enjoyf.webapps.joyme.weblogic.giftmarket.GiftMarketWebLogic;
import com.enjoyf.webapps.joyme.weblogic.youku.YoukuGameWebLogic;
import com.enjoyf.webapps.joyme.webpage.controller.youku.AbstractYoukuController;
import net.sf.json.JSONObject;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/6/18
 * Description:
 */
@Controller
@RequestMapping("/youku/api/giftmarket")
public class YoukuApiGiftController extends AbstractYoukuController {

    @Resource(name = "i18nSource")
    private ResourceBundleMessageSource i18nSource;

    @Resource(name = "giftMarketWebLogic")
    private GiftMarketWebLogic giftMarketWebLogic;

    /**
     * 兑换商品
     *
     * @param request
     * @param response
     * @param gid
     * @param aid
     * @param appkey
     * @param profileId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/exchange")
    public String exchange(HttpServletRequest request, HttpServletResponse response,
                           @RequestParam(value = "gid", required = true) String gid,
                           @RequestParam(value = "aid", required = true) String aid,
                           @RequestParam(value = "appkey", required = false) String appkey,
                           @RequestParam(value = "profileid", required = false) String profileId,
                           @RequestParam(value = "secid", required = false) String secId,
                           @RequestParam(value = "type", required = false) String type) {
        JoymeResultMsg resultMsg = new JoymeResultMsg(JoymeResultMsg.CODE_S);
        try {
            if (StringUtil.isEmpty(gid)) {
                resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
                resultMsg.setMsg(i18nSource.getMessage("param.empty", null, Locale.CHINA));
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }
            Long goodsId = Long.valueOf(gid);
            ActivityGoods activityGoods = PointServiceSngl.get().getActivityGoods(goodsId);
            if (activityGoods == null) {
                resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
                resultMsg.setMsg(i18nSource.getMessage("activity.is.null", null, Locale.CHINA));
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }
            Date now = new Date();
            long startTime;
            long endTime;

            long seckillId = 0l;
            if (ChooseType.YES.equals(activityGoods.getSeckilltype()) && StringUtil.isEmpty(secId)) {
                resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
                resultMsg.setMsg(i18nSource.getMessage("goods.seckill.is.null", null, Locale.CHINA));
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }
            if (ChooseType.YES.equals(activityGoods.getSeckilltype()) && !StringUtil.isEmpty(secId)) {
                //秒杀
                try {
                    seckillId = Long.valueOf(secId);
                } catch (NumberFormatException e) {
                }
                GoodsSeckill goodsSeckill = PointServiceSngl.get().getGoodsSeckillById(seckillId);
                if (goodsSeckill == null || !goodsSeckill.getGoodsId().equals(gid) || !goodsSeckill.getRemoveStatus().equals(IntRemoveStatus.USED)) {
                    resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
                    resultMsg.setMsg(i18nSource.getMessage("goods.seckill.is.null", null, Locale.CHINA));
                    return JsonBinder.buildNormalBinder().toJson(resultMsg);
                }
                seckillId = goodsSeckill.getSeckillId();
                startTime = goodsSeckill.getStartTime().getTime();
                endTime = goodsSeckill.getEndTime().getTime();
                if (now.getTime() < startTime || now.getTime() > endTime) {
                    resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
                    resultMsg.setMsg(i18nSource.getMessage("goods.seckill.is.null", null, Locale.CHINA));
                    return JsonBinder.buildNormalBinder().toJson(resultMsg);
                }
            } else {
                //兑换
                startTime = activityGoods.getStartTime().getTime();
                endTime = activityGoods.getEndTime().getTime();
                if (now.getTime() < startTime || now.getTime() > endTime) {
                    resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
                    resultMsg.setMsg(i18nSource.getMessage("activity.has.expire", null, Locale.CHINA));
                    return JsonBinder.buildNormalBinder().toJson(resultMsg);
                }
            }

            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileId);
            UserPoint userPoint = giftMarketWebLogic.getUserPoint(appkey, profile);

            int userHasPoint = userPoint == null ? 0 : userPoint.getUserPoint();
            if (userHasPoint < activityGoods.getGoodsPoint()) {
                resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
                resultMsg.setMsg(i18nSource.getMessage("point.not.enough", null, Locale.CHINA));
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }

            AllowExchangeStatus allowExchangeStatus = giftMarketWebLogic.allowExchangeGoods(activityGoods, profile.getProfileId());

            if (!AllowExchangeStatus.ALLOW.equals(allowExchangeStatus)) {
                if (AllowExchangeStatus.HAS_EXCHANGED_BY_DAY.equals(allowExchangeStatus)) {
                    resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
                    resultMsg.setMsg(i18nSource.getMessage("goods.has.exchanged.day", null, Locale.CHINA));
                } else {
                    resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
                    resultMsg.setMsg(i18nSource.getMessage("goods.has.exchanged", null, Locale.CHINA));
                }
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }
            GoodsActionType goodsActionType = null;
            String address = "";
            if ("1".equals(type)) {
                goodsActionType = GoodsActionType.GIFT;
            } else {
                goodsActionType = GoodsActionType.getByCode(Integer.parseInt(type));
                UserAccount userAccount = UserCenterServiceSngl.get().getUserAccount(profile.getUno());
                if (userAccount != null) {
                    if (userAccount.getAddress() != null) {
                        address = userAccount.getAddress().toJsonStr();
                    }
                }
            }

            if (ChooseType.YES.equals(activityGoods.getSeckilltype())) {
                //秒杀
                ActivityGoodsDTO activityGoodsDTO = PointServiceSngl.get().seckillGoods(seckillId, goodsId, profile.getProfileId(), appkey, profile.getUno(), now, getIp(request), goodsActionType, address);
                resultMsg.getResult().add(genSeckillDTO(activityGoodsDTO));
            } else {
                //兑换
                UserConsumeLog userConsumeLog = PointServiceSngl.get().consumeGoods(profile.getUno(), profile.getProfileId(), appkey, goodsId, now, getIp(request), goodsActionType, address);
                GoodsItem goodsItem = null;
                if (userConsumeLog.getGoodsItemId() > 0) {
                    goodsItem = PointServiceSngl.get().getGoodsItemById(userConsumeLog.getGoodsItemId());
                }
                resultMsg.getResult().add(genDTO(activityGoods, goodsItem, userConsumeLog));
            }
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
            } else if (e.equals(PointServiceException.GOODS_SECKILL_IS_NULL)) {
                resultMsg.setMsg(i18nSource.getMessage("goods.seckill.is.null", null, Locale.CHINA));
            } else if (e.equals(PointServiceException.GOODS_SECKILL_TOTAL_BEYOND)) {
                resultMsg.setMsg(i18nSource.getMessage("goods.seckill.total.beyond", null, Locale.CHINA));
            } else if (e.equals(PointServiceException.GOODS_SECKILL_END)) {
                resultMsg.setMsg(i18nSource.getMessage("goods.seckill.end", null, Locale.CHINA));
            } else if (e.equals(PointServiceException.GOODS_SECKILL_NO_START)) {
                resultMsg.setMsg(i18nSource.getMessage("goods.seckill.no.start", null, Locale.CHINA));
            } else {
                resultMsg.setMsg(i18nSource.getMessage("system.error", null, Locale.CHINA));
                GAlerter.lab(this.getClass().getName() + " occured ServicdeExcpetion.e", e);
            }
        }
        return JsonBinder.buildNormalBinder().toJson(resultMsg);
    }

    @RequestMapping(value = "/getaddress")
    @ResponseBody
    public String getAddress(@RequestParam(value = "profileid", required = false) String profileId) {
        ResultObjectMsg msg = new ResultObjectMsg(ResultPageMsg.CODE_S);
        try {
            if (StringUtil.isEmpty(profileId)) {
                msg.setRs(ResultPageMsg.CODE_E);
                msg.setMsg("profileId.is.null");
                return JsonBinder.buildNormalBinder().toJson(msg);
            }

            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileId);
            UserAccount userAccount = UserCenterServiceSngl.get().getUserAccount(profile.getUno());
            if (userAccount == null) {
                msg.setMsg("address.is.null");
            } else {
                if (userAccount.getAddress() == null) {
                    msg.setMsg("address.is.null");
                } else {
                    msg.setResult(userAccount);
                }
            }
        } catch (ServiceException e) {
            msg.setRs(ResultObjectMsg.CODE_E);
            msg.setMsg("system.error");
            return JsonBinder.buildNormalBinder().toJson(msg);
        }
        return JsonBinder.buildNormalBinder().toJson(msg);
    }

    @RequestMapping(value = "/modifyaddress")
    @ResponseBody
    public String insertAddress(@RequestParam(value = "profileid", required = false) String profileId,
                                @RequestParam(value = "name", required = false) String name,
                                @RequestParam(value = "phone", required = false) String phone,
                                @RequestParam(value = "zipcode", required = false) String zipcode,
                                @RequestParam(value = "address", required = false) String address,
                                @RequestParam(value = "province", required = false) String province,
                                @RequestParam(value = "city", required = false) String city,
                                @RequestParam(value = "county", required = false) String county) {
        ResultObjectMsg msg = new ResultObjectMsg(ResultPageMsg.CODE_S);
        try {
            if (StringUtil.isEmpty(profileId)) {
                msg.setRs(ResultPageMsg.CODE_E);
                msg.setMsg("profileId.is.null");
                return JsonBinder.buildNormalBinder().toJson(msg);
            }

            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileId);
            Address userAddress = new Address();
            userAddress.setAddress(address);
            userAddress.setContact(name);
            userAddress.setPhone(phone);
            userAddress.setPostcode(zipcode);
            userAddress.setCity(city);
            userAddress.setProvince(province);
            userAddress.setCounty(county);

            boolean bool = UserCenterServiceSngl.get().modifyUserAccount(new UpdateExpress().set(UserAccountField.ADDRESS, userAddress.toJsonStr()), profile.getUno());
            if (bool) {
                msg.setMsg("success");
            }
        } catch (ServiceException e) {
            msg.setRs(ResultObjectMsg.CODE_E);
            msg.setMsg("system.error");
            return JsonBinder.buildNormalBinder().toJson(msg);
        }
        return JsonBinder.buildNormalBinder().toJson(msg);
    }

    private GoodsSimpleDTO genDTO(ActivityGoods goods, GoodsItem goodsItem, UserConsumeLog userConsumeLog) {
        GoodsSimpleDTO dto = new GoodsSimpleDTO();
        dto.setGoodsId(goods.getActivityGoodsId());
        dto.setGoodsName(goods.getActivitySubject());
        dto.setGoodsType(goods.getActivitygoodsType().getCode());
        if (userConsumeLog != null) {
            dto.setConsumeOrder(userConsumeLog.getConsumeOrder());
        }
        if (goodsItem != null) {
            dto.setGoodsItemId(goodsItem.getGoodsItemId());
            dto.setItemName1(goodsItem.getSnName1());
            dto.setItemValue1(goodsItem.getSnValue1());
            dto.setItemName2(goodsItem.getSnName2());
            dto.setItemValue2(goodsItem.getSnValue2());
        }

        return dto;
    }

    private GoodsSimpleDTO genSeckillDTO(ActivityGoodsDTO activityGoodsDTO) {
        if (activityGoodsDTO == null) {
            return null;
        }
        GoodsSimpleDTO dto = new GoodsSimpleDTO();
        dto.setGoodsId(activityGoodsDTO.getGoodsId());
        dto.setGoodsName(activityGoodsDTO.getGoodsName());
        dto.setGoodsType(activityGoodsDTO.getGoodsType());
        dto.setConsumeOrder(activityGoodsDTO.getConsumeOrder());
        dto.setGoodsItemId(activityGoodsDTO.getGoodsItemId());
        dto.setItemName1(activityGoodsDTO.getItemName1());
        dto.setItemValue1(activityGoodsDTO.getItemValue1());
        dto.setItemName2(activityGoodsDTO.getItemName2());
        dto.setItemValue2(activityGoodsDTO.getItemValue2());
        return dto;
    }
}
