package com.enjoyf.webapps.joyme.webpage.controller.giftmarket.giftmarketwap;

import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.TemplateHotdeployConfig;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.content.ActivityType;
import com.enjoyf.platform.service.content.MobileExclusive;
import com.enjoyf.platform.service.joymeapp.ClientLineItem;
import com.enjoyf.platform.service.joymeapp.JoymeAppServiceSngl;
import com.enjoyf.platform.service.point.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.UserCenterServiceException;
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
import com.enjoyf.platform.webapps.common.JoymeResultMsg;
import com.enjoyf.platform.webapps.common.ResultListMsg;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.webapps.joyme.dto.activity.ActivityDTO;
import com.enjoyf.webapps.joyme.dto.activity.ActivityDetailDTO;
import com.enjoyf.webapps.joyme.dto.activity.ActivityMygiftDTO;
import com.enjoyf.webapps.joyme.dto.profile.MobileCodeDTO;
import com.enjoyf.webapps.joyme.weblogic.giftmarket.AllowExchangeStatus;
import com.enjoyf.webapps.joyme.weblogic.giftmarket.GiftMarketWebLogic;
import com.enjoyf.webapps.joyme.weblogic.profile.MobileVerifyWebLogic;
import com.enjoyf.webapps.joyme.weblogic.profile.PhoneVerifyError;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterSession;
import com.enjoyf.webapps.joyme.webpage.util.Constant;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 14-6-19
 * Time: 下午2:15
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/giftmarket/wap")
public class WapGiftmarketController extends AbstractWapGiftMarketController {
    TemplateHotdeployConfig templateConfig = HotdeployConfigFactory.get().getConfig(TemplateHotdeployConfig.class);

    @Resource(name = "giftMarketWebLogic")
    private GiftMarketWebLogic giftMarketWebLogic;

    @Resource(name = "mobileVerifyWebLogic")
    private MobileVerifyWebLogic mobileVerifyWebLogic;

    @Resource(name = "i18nSource")
    private ResourceBundleMessageSource i18nSource;

    private static final int pageSize = 15;

    private JsonBinder jsonBinder = JsonBinder.buildNormalBinder();


    //更多礼包列表  最多查询20行
    @RequestMapping
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "p", required = false, defaultValue = "1") Integer pageNo,
                             @RequestParam(value = "openid", required = false) String openId,
                             @RequestParam(value = "token", required = false) String token) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            if (!StringUtil.isEmpty(openId) || !StringUtil.isEmpty(token)) {
                mapMessage.put("openid", openId);
                mapMessage.put("token", token);
            }
            /**
             * 服务号通过code调取微信接口获取openid  订阅号没有高级接口 所以无法通过微信接口获得openid 采取前台拼接传参方式获得openId
             */
            if (!StringUtil.isEmpty(openId)) {
                UserCenterSession userSession = getWxUserSession(openId, request, response);
                mapMessage.put("uno", userSession.getUno());
                mapMessage.put("openid", openId);
            }

            //独家礼包
            PageRows<ClientLineItem> exclusivePageRows = JoymeAppServiceSngl.get().queryItemsByLineCode(PC_EXCLUSIVE, "", new Pagination(hotActivityPageSize, pageNo, hotActivityPageSize));
            if (exclusivePageRows != null && !CollectionUtil.isEmpty(exclusivePageRows.getRows())) {
                Set<Long> exclusiveLong = new LinkedHashSet<Long>();
                for (ClientLineItem clientLineItem : exclusivePageRows.getRows()) {
                    exclusiveLong.add(Long.parseLong(clientLineItem.getDirectId()));
                }
                QueryExpress exclusiveQueryExpress = new QueryExpress()
                        .add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_TYPE, ActivityType.EXCHANGE_GOODS.getCode()))
                        .add(QueryCriterions.eq(ActivityGoodsField.REMOVE_STATUS, ActStatus.ACTED.getCode()))
                        .add(QueryCriterions.in(ActivityGoodsField.ACTIVITY_GOODS_ID, exclusiveLong.toArray()))
                        .add(QuerySort.add(ActivityGoodsField.CREATEDATE, QuerySortOrder.DESC));
                List<ActivityGoods> exclusiveActivityList = PointServiceSngl.get().listActivityGoods(exclusiveQueryExpress);
                if (!CollectionUtil.isEmpty(exclusiveActivityList)) {
                    List<ActivityDTO> returnExclusiveList = new ArrayList<ActivityDTO>();
                    for (ActivityGoods activityGoods : exclusiveActivityList) {
                        returnExclusiveList.add(giftMarketWebLogic.buildExchangeActivityDTO(activityGoods));
                    }
                    mapMessage.put("exclusiveList", returnExclusiveList);
                }
            }

            Pagination pagination = new Pagination(pageNo * pageSize, pageNo, pageSize);
            QueryExpress queryExpress = new QueryExpress()
                    .add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_TYPE, ActivityType.EXCHANGE_GOODS.getCode()))
                    .add(QueryCriterions.eq(ActivityGoodsField.REMOVE_STATUS, ActStatus.ACTED.getCode()))
//                    .add(QueryCriterions.ne(ActivityGoodsField.CHANNEL_TYPE, MobileExclusive.NEWSCLIENT.getCode()))
                    .add(QuerySort.add(ActivityGoodsField.DISPLAY_ORDER, QuerySortOrder.ASC));
//            if (!CollectionUtil.isEmpty(tempIds)) {
//                queryExpress.add(QueryCriterions.notIn(ActivityGoodsField.ACTIVITY_GOODS_ID, tempIds.toArray()));
//            }
            PageRows<ActivityDTO> pageRows = giftMarketWebLogic.queryActivityDTOs(queryExpress, pagination);
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                mapMessage.put("newList", pageRows.getRows());
                mapMessage.put("page", pageRows.getPage());
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/common/custompage", mapMessage);
        }

        return new ModelAndView("/views/jsp/giftmarket/giftmarketwap/giftmarketwap", mapMessage);
    }

    @RequestMapping(value = "/hotactivity")
    public ModelAndView hotActivity(HttpServletRequest request, HttpServletResponse response,
                                    @RequestParam(value = "p", required = false, defaultValue = "1") Integer pageNo,
                                    @RequestParam(value = "openid", required = false) String openId,
                                    @RequestParam(value = "token", required = false) String token
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            if (!StringUtil.isEmpty(openId) || !StringUtil.isEmpty(token)) {
                mapMessage.put("openid", openId);
                mapMessage.put("token", token);
            }

            UserCenterSession userSession = getWxUserSession(openId, request, response);
            if (userSession == null) {
                if (!StringUtil.isEmpty(openId)) {
                    userSession = getWxUserSession(openId, request, response);
                    mapMessage.put("uno", userSession.getUno());
                    mapMessage.put("openid", openId);
                }
            } else {
                mapMessage.put("uno", userSession.getUno());
            }

            Pagination pagination = new Pagination(pageNo * pageSize, pageNo, pageSize);
            QueryExpress queryExpress = new QueryExpress().add(QueryCriterions.eq(ActivityGoodsField.HOT_ACTIVITY, 1))
                    .add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_TYPE, ActivityType.EXCHANGE_GOODS.getCode()))
                    .add(QueryCriterions.eq(ActivityGoodsField.REMOVE_STATUS, ActStatus.ACTED.getCode()))
                    .add(QueryCriterions.ne(ActivityGoodsField.CHANNEL_TYPE, MobileExclusive.NEWSCLIENT.getCode()))
                    .add(QuerySort.add(ActivityGoodsField.DISPLAY_ORDER, QuerySortOrder.ASC));
            PageRows<ActivityDTO> pageRows = giftMarketWebLogic.queryActivityDTOs(queryExpress, pagination);


            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                mapMessage.put("rows", pageRows.getRows());
                mapMessage.put("page", pageRows.getPage());
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/common/custompage", mapMessage);
        }
        return new ModelAndView("/views/jsp/giftmarket/giftmarketwap/hotactivity", mapMessage);
    }

    /**
     * 详情页面
     */
    @RequestMapping(value = "/giftdetail")
    public ModelAndView giftDetail(HttpServletRequest request, HttpServletResponse response,
                                   @RequestParam(value = "aid", required = false) String aid,
                                   @RequestParam(value = "openid", required = false) String openId,
                                   @RequestParam(value = "token", required = false) String token) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            System.out.println("openId="+openId);
            UserCenterSession userSession = getWxUserSession(openId, request, response);
            if (!StringUtil.isEmpty(openId) || !StringUtil.isEmpty(token)) {
                mapMessage.put("openid", openId);
                mapMessage.put("token", token);
            }

            if (userSession == null) {
                if (!StringUtil.isEmpty(openId)) {
                    userSession = getWxUserSession(openId, request, response);
                    mapMessage.put("uno", userSession.getUno());
                    mapMessage.put("openid", openId);
                }
            }

            ActivityDetailDTO activityDetailDTO = giftMarketWebLogic.getActivityGiftDetailDTO(Long.parseLong(aid));
            if (activityDetailDTO == null) {
                return new ModelAndView("/views/jsp/giftmarket/giftmarketwap/giftisnull", mapMessage);
            }
            mapMessage.put("detail", activityDetailDTO);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/common/custompage", mapMessage);
        }
        return new ModelAndView("/views/jsp/giftmarket/giftmarketwap/gift_detail_wap", mapMessage);
    }

    @RequestMapping(value = "/giftdetailresult")
    public ModelAndView giftDetailResult(HttpServletRequest request, @RequestParam(value = "uid", required = false) String uid,
                                         @RequestParam(value = "aid", required = false) String aid,
                                         @RequestParam(value = "uno", required = false) String uno) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            UserCenterSession userSession = getUserCenterSeesion(request);
            if (userSession == null) {
                return new ModelAndView("redirect:/giftmarket/wap");
            }

            ActivityDetailDTO activityDetailDTO = giftMarketWebLogic.getActivityGiftDetailDTO(Long.parseLong(aid));
            if (activityDetailDTO == null) {
                return new ModelAndView("/views/jsp/giftmarket/giftmarketwap/giftisnull", mapMessage);
            }
            List<UserExchangeLog> userExchangeLog = PointServiceSngl.get().queryUserExchangeByQueryExpress(new QueryExpress().add(QueryCriterions.eq(UserExchangeLogField.USER_EXCHANGE_LOG_ID, Long.parseLong(uid)))
                    .add(QueryCriterions.eq(UserExchangeLogField.USER_NO, uno)));

            mapMessage.put("userExchangeLog", userExchangeLog);
            mapMessage.put("activityDetailDTO", activityDetailDTO);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/common/custompage", mapMessage);
        }
        return new ModelAndView("/views/jsp/giftmarket/giftmarketwap/giftdetailresult", mapMessage);
    }

    /**
     * 输入手机号的页面
     */
    @RequestMapping(value = "/bindophonepage")
    public ModelAndView bindPhonePage(@RequestParam(value = "aid", required = false) String aid,
                                      @RequestParam(value = "openid", required = false) String openId,
                                      @RequestParam(value = "token", required = false) String token) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("aid", aid);
        mapMessage.put("token", token);
        mapMessage.put("openid", openId);
        return new ModelAndView("/views/jsp/giftmarket/giftmarketwap/bind_phone_page", mapMessage);
    }

    /**
     * 给手机发送验证码
     */
    @RequestMapping(value = "/bindphone")
    public ModelAndView bindPhone(HttpServletRequest request,
                                  HttpServletResponse response, @RequestParam(value = "phone", required = false) String phone,
                                  @RequestParam(value = "openid", required = false) String openId,
                                  @RequestParam(value = "token", required = false) String token,
                                  @RequestParam(value = "aid", required = false) String aid) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            mapMessage.put("phone", phone);
            mapMessage.put("aid", aid);
            mapMessage.put("token", token);
            mapMessage.put("openid", openId);
            UserCenterSession userSession = getUserCenterSeesion(request);

            if (userSession == null) {
                return new ModelAndView("redirect:/giftmarket/wap");
            }

            String lastObj = getTimeInCookie(request);
            String vTemplate = templateConfig.getVerifyMobileSmsTemplate();
            if (!StringUtil.isEmpty(lastObj)) {
                long now = System.currentTimeMillis();
                long last = Long.parseLong(lastObj);

                long intravel = 30 - ((now - last) / 1000);
                if (intravel >= 0) {
                    mapMessage.put("intravel", intravel);
                } else {
                    deleteTimeInCookie(request, response);
                    //MobileCodeDTO dto = mobileVerifyWebLogic.generatorCode(userSession.getUno(), phone, vTemplate, MobileVerifyWebLogic.VERIFYSMS_TIMES);
                    boolean result = UserCenterServiceSngl.get().sendMobileNo(phone);
                    //if (dto.getRs() == MobileCodeDTO.RS_SUCCESS) {
                    if (result){
                        saveTimeInCookie(request, response);
                        mapMessage.put("intravel", "30");
                        //把生成code存入session
//                        request.getSession().setAttribute(Constant.SESSION_MOBILE_CODE, dto.getCode());
//                        UserCenterServiceSngl.get().saveMobileCode(userSession.getUno(), dto.getCode());
                    }
                }
                return new ModelAndView("/views/jsp/giftmarket/giftmarketwap/bind_phone_verify", mapMessage);
            }
            //MobileCodeDTO dto = mobileVerifyWebLogic.generatorCode(userSession.getUno(), phone, vTemplate, MobileVerifyWebLogic.VERIFYSMS_TIMES);
            //if (dto.getRs() == MobileCodeDTO.RS_SUCCESS) {
            boolean result = UserCenterServiceSngl.get().sendMobileNo(phone);
            if(result){
                saveTimeInCookie(request, response);
                mapMessage.put("intravel", "30");
                //把生成code存入session
//                request.getSession().setAttribute(Constant.SESSION_MOBILE_CODE, dto.getCode());
//                UserCenterServiceSngl.get().saveMobileCode(userSession.getUno(), dto.getCode());
            }

        } catch (UserCenterServiceException ue){
            GAlerter.lab(this.getClass().getName() + " occured UserCenterServiceException.e:", ue);
        }
        catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/common/custompage", mapMessage);
        }
        return new ModelAndView("/views/jsp/giftmarket/giftmarketwap/bind_phone_verify", mapMessage);
    }

    /**
     * JS调用的重新发送验证码
     */
    @RequestMapping(value = "/jsonbindphone")
    @ResponseBody
    public String bindPhoneJson(HttpServletRequest request,
                                HttpServletResponse response, @RequestParam(value = "phone", required = false) String phone) {
        ResultObjectMsg resultMsg = new ResultObjectMsg(ResultListMsg.CODE_S);
        try {
            UserCenterSession userSession = getUserCenterSeesion(request);

            if (userSession == null) {
                resultMsg.setRs(ResultListMsg.CODE_E);
                return jsonBinder.toJson(resultMsg);
            }
            //String vTemplate = templateConfig.getVerifyMobileSmsTemplate();
            //MobileCodeDTO dto = mobileVerifyWebLogic.generatorCode(userSession.getUno(), phone, vTemplate, MobileVerifyWebLogic.VERIFYSMS_TIMES);
            //resultMsg.setResult(dto);
            //if (dto.getRs() == MobileCodeDTO.RS_SUCCESS) {
            boolean result = UserCenterServiceSngl.get().sendMobileNo(phone);
            if(result){
                saveTimeInCookie(request, response);
                //把生成code存入session
//                request.getSession().setAttribute(Constant.SESSION_MOBILE_CODE, dto.getCode());
//                UserCenterServiceSngl.get().saveMobileCode(userSession.getUno(), dto.getCode());
            }

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            resultMsg.setRs(ResultListMsg.CODE_E);
            resultMsg.setMsg("system.error");
        }
        return jsonBinder.toJson(resultMsg);
    }

    /**
     * 绑定手机
     */
    @RequestMapping(value = "/verify")
    @ResponseBody
    public String verify(HttpServletRequest request, @RequestParam(value = "phone", required = false) String phone,
                         @RequestParam(value = "verifycode", required = false) String verifyCode) {
        String callback = request.getParameter("callback");
        ResultObjectMsg resultMsg = new ResultObjectMsg(ResultListMsg.CODE_S);
        try {
            if (StringUtil.isEmpty(verifyCode)) {
                resultMsg.setMsg(PhoneVerifyError.ERROR_VERIFY_NULL);
                if (StringUtil.isEmpty(callback)) {
                    return jsonBinder.toJson(resultMsg);
                } else {
                    return callback + "(" + jsonBinder.toJson(resultMsg) + ")";
                }

            }
            if (StringUtil.isEmpty(phone)) {
                resultMsg.setMsg(PhoneVerifyError.ERROR_PHONE_NULL);
                if (StringUtil.isEmpty(callback)) {
                    return jsonBinder.toJson(resultMsg);
                } else {
                    return callback + "(" + jsonBinder.toJson(resultMsg) + ")";
                }

            }
            //todo
            UserCenterSession userSession = getUserCenterSeesion(request);
//            String sessionCode = UserCenterServiceSngl.get().getMobileCode(userSession.getUno());
//
//            if (StringUtil.isEmpty(sessionCode)) {
//                resultMsg.setMsg(PhoneVerifyError.ERROR_VERIFY_NOT_SEND);
//                if (StringUtil.isEmpty(callback)) {
//                    return jsonBinder.toJson(resultMsg);
//                } else {
//                    return callback + "(" + jsonBinder.toJson(resultMsg) + ")";
//                }
//            }
//            if (!sessionCode.equals(verifyCode)) {
//                resultMsg.setMsg(PhoneVerifyError.ERROR_VERIFY);
//                if (StringUtil.isEmpty(callback)) {
//                    return jsonBinder.toJson(resultMsg);
//                } else {
//                    return callback + "(" + jsonBinder.toJson(resultMsg) + ")";
//                }
//            }
            if(!UserCenterServiceSngl.get().checkMobileVerifyCode(phone,verifyCode)){
                resultMsg.setMsg(PhoneVerifyError.ERROR_VERIFY);
                if (StringUtil.isEmpty(callback)) {
                    return jsonBinder.toJson(resultMsg);
                } else {
                    return callback + "(" + jsonBinder.toJson(resultMsg) + ")";
                }
            }

            UserCenterServiceSngl.get().bindMobile(phone, userSession.getProfileId(), getIp(request));

            userSession.setMobile(phone);
            resultMsg.setMsg(PhoneVerifyError.SUCCESS);
        } catch (ServiceException e) {
            if (e.equals(UserCenterServiceException.PROFILE_HAS_EXISTS)) {
                resultMsg.setRs(ResultListMsg.CODE_E);
                resultMsg.setMsg(PhoneVerifyError.ERROR_PHONE_HAS_BIND);
            } else {
                GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
                resultMsg.setRs(ResultListMsg.CODE_E);
                resultMsg.setMsg("system.error");
            }
        }
        if (StringUtil.isEmpty(callback)) {
            return jsonBinder.toJson(resultMsg);
        } else {
            return callback + "(" + jsonBinder.toJson(resultMsg) + ")";
        }
    }

    /**
     * 领号操作
     */
    @RequestMapping(value = "/getcode")
    @ResponseBody
    public String getCode(HttpServletRequest request,
                          HttpServletResponse response,
                          @RequestParam(value = "gid", required = true) long goodsId,
                          @RequestParam(value = "openid", required = false) String openId,
                          @RequestParam(value = "token", required = false) String token) {
        JoymeResultMsg resultMsg = new JoymeResultMsg(JoymeResultMsg.CODE_S);
        try {
            String url = request.getRequestURL().toString();
            if (url.contains("joyme.com")) {
                if (!validateOpenIdAndToken(request, response, openId, token)) {
                    resultMsg.setStatus_code(JoymeResultMsg.USERSSION_IS_NULL);
                    resultMsg.setMsg(JoymeResultMsg.USERSSION_IS_NULL);
                    return jsonBinder.toJson(resultMsg);
                }
            }
            UserCenterSession userSession = getWxUserSession(openId, request, response);
            if (userSession == null) {
                resultMsg.setStatus_code(JoymeResultMsg.USERSSION_IS_NULL);
                resultMsg.setMsg(JoymeResultMsg.CODE_E);
                return jsonBinder.toJson(resultMsg);
            }

            ActivityGoods activityGoods = PointServiceSngl.get().getActivityGoods(goodsId);
            if (activityGoods.getChannelType().getCode() != MobileExclusive.DEFAULT.getCode() && activityGoods.getChannelType().getCode() != MobileExclusive.WEIXIN.getCode()) {
                resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
                resultMsg.setMsg(i18nSource.getMessage("is.not.weixin.exclusive", null, Locale.CHINA));
                return jsonBinder.toJson(resultMsg);
            }
            //通过 商品领取类型 和 用户领取记录 判断 是否允许用户 继续 领取
            AllowExchangeStatus allowExchangeStatus = giftMarketWebLogic.allowGetCode(activityGoods, userSession.getProfileId());
            if (allowExchangeStatus.equals(AllowExchangeStatus.NO_ALLOW)) {
                resultMsg.setMsg(i18nSource.getMessage("message.user.syserror", null, Locale.CHINA));
                resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
                return jsonBinder.toJson(resultMsg);
            }
            if (allowExchangeStatus.equals(AllowExchangeStatus.HAS_EXCHANGED)) {
                resultMsg.setMsg(i18nSource.getMessage("user.gift.getcode.one", null, Locale.CHINA));
                resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
                return jsonBinder.toJson(resultMsg);
            }
            if (allowExchangeStatus.equals(AllowExchangeStatus.HAS_EXCHANGED_BY_DAY)) {
                resultMsg.setMsg(i18nSource.getMessage("user.gift.getcode.by.day", null, Locale.CHINA));
                resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
                return jsonBinder.toJson(resultMsg);
            }
            if (allowExchangeStatus.equals(AllowExchangeStatus.HAS_EXCHANGED_BY_INTRVAL)) {
                resultMsg.setMsg(i18nSource.getMessage("user.gift.getcode.by.intaval", null, Locale.CHINA));
                resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
                return jsonBinder.toJson(resultMsg);
            }
            Long newDate = System.currentTimeMillis();
            Long lastDate = userSession.getGetCodeTime();
            if (lastDate != null) {
                long interval = (newDate - lastDate) / 1000;
                if (interval <= 15) {
                    resultMsg.setMsg(i18nSource.getMessage("user.gift.get.code.interval", null, Locale.CHINA));
                    resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
                    return jsonBinder.toJson(resultMsg);
                }
            }

            long tomorrow = 60l * 60l * 24l * 1000l;
            long endTime = activityGoods.getEndTime().getTime();
            long now = System.currentTimeMillis();
            if (now > (endTime + tomorrow)) {
                resultMsg.setMsg(i18nSource.getMessage("time.is.out", null, Locale.CHINA));
                resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
                return jsonBinder.toJson(resultMsg);
            }
            UserExchangeLog userExchangeLog = PointServiceSngl.get().exchangeGoodsItem(userSession.getUno(), userSession.getProfileId(), userSession.getAppkey(), goodsId, new Date(), getIp(request), "weixin", true);
            if (userExchangeLog == null) {
                resultMsg.setMsg(i18nSource.getMessage("user.gift.getcode.null", null, Locale.CHINA));
                resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
                return jsonBinder.toJson(resultMsg);
            }
            userSession.setGetCodeTime(userExchangeLog.getExhangeTime().getTime());
            List<UserExchangeLog> userList = new ArrayList<UserExchangeLog>();
            userList.add(userExchangeLog);
            resultMsg.setResult(userList);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
            resultMsg.setMsg("system.error");
        }

        return jsonBinder.toJson(resultMsg);
    }

    /**
     * 领号结果页
     *
     * @param code 领的号码
     */
    @RequestMapping(value = "/getcoderesult")
    public ModelAndView getCodeResult(@RequestParam(value = "code", required = false) String code) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("code", code);
        return new ModelAndView("/views/jsp/giftmarket/giftmarketwap/getcode_result", mapMessage);
    }

    /**
     * 淘号操作
     */
    @ResponseBody
    @RequestMapping(value = "/taocode")
    public String taoCode(HttpServletRequest request, HttpServletResponse response,
                          @RequestParam(value = "gid", required = true) long goodsId,
                          @RequestParam(value = "aid", required = true) long activityId,
                          @RequestParam(value = "openid", required = false) String openId,
                          @RequestParam(value = "token", required = false) String token) {
        JoymeResultMsg resultMsg = new JoymeResultMsg(JoymeResultMsg.CODE_S);

        try {
            String url = request.getRequestURL().toString();
            if (url.contains("joyme.com")) {
                if (!validateOpenIdAndToken(request, response, openId, token)) {
                    resultMsg.setStatus_code(JoymeResultMsg.USERSSION_IS_NULL);
                    resultMsg.setMsg(JoymeResultMsg.USERSSION_IS_NULL);
                    return jsonBinder.toJson(resultMsg);
                }
            }

            UserCenterSession userSession = getUserCenterSeesion(request);
            if (userSession == null) {
                resultMsg.setStatus_code(JoymeResultMsg.USERSSION_IS_NULL);
                return jsonBinder.toJson(resultMsg);
            }

            Long newDate = System.currentTimeMillis();
            Long lastDate = userSession.getTaoCodeTime();
            if (lastDate != null) {
                long interval = (newDate - lastDate) / 1000;
                if (interval <= 15) {
                    resultMsg.setMsg(i18nSource.getMessage("user.gift.get.code.interval", null, Locale.CHINA));
                    resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
                    return jsonBinder.toJson(resultMsg);
                }
            }

            ActivityGoods activityGoods = PointServiceSngl.get().getActivityGoods(activityId);
            long tomorrow = 60l * 60l * 24l * 1000l;
            long endTime = activityGoods.getEndTime().getTime();
            long now = System.currentTimeMillis();
            if (now > (endTime + tomorrow)) {
                resultMsg.setMsg(i18nSource.getMessage("time.is.out", null, Locale.CHINA));
                resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
                return jsonBinder.toJson(resultMsg);
            }
            List<ExchangeGoodsItem> listExchangeGoodsItem = PointServiceSngl.get().taoExchangeGoodsItemByGoodsId(userSession.getUno(), userSession.getProfileId(), userSession.getAppkey(), goodsId, new Date(), getIp(request));

            if (listExchangeGoodsItem.size() < 10) {
                resultMsg.setMsg(i18nSource.getMessage("user.gift.tao.num.limit.10", null, Locale.CHINA));
                resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
                return jsonBinder.toJson(resultMsg);
            }
            userSession.setTaoCodeTime(System.currentTimeMillis());
            resultMsg.setResult(listExchangeGoodsItem);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            resultMsg.setMsg(i18nSource.getMessage("system.error", null, Locale.CHINA));
            return jsonBinder.toJson(resultMsg);
        }
        return jsonBinder.toJson(resultMsg);
    }

    /**
     * 淘号结果页面
     */
    @RequestMapping(value = "/taocoderesult")
    public ModelAndView taoCodeResult(@RequestParam(value = "list", required = false) String array,
                                      @RequestParam(value = "aid", required = false) String aid) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        String[] value = array.split(",");
        List<String> list = Arrays.asList(value);
        try {
            ActivityGoods activityGoods = PointServiceSngl.get().getActivityGoods(Long.parseLong(aid));
            mapMessage.put("activity", activityGoods);
            mapMessage.put("valuelist", list);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/common/custompage", mapMessage);
        }
        return new ModelAndView("/views/jsp/giftmarket/giftmarketwap/taocode_result", mapMessage);
    }

    /**
     * 预定页面
     */
    @RequestMapping(value = "/reserve")
    public ModelAndView reserve(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "openid", required = false) String openId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            UserCenterSession userSession = getUserCenterSeesion(request);
            if (userSession == null) {
                if (!StringUtil.isEmpty(openId)) {
                    userSession = getWxUserSession(openId, request, response);
                    mapMessage.put("uno", userSession.getUno());
                    mapMessage.put("openId", openId);
                }
            } else {
                mapMessage.put("uno", userSession.getUno());
                mapMessage.put("openId", openId);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/common/custompage", mapMessage);
        }
        return new ModelAndView("/views/jsp/giftmarket/giftmarketwap/gift_reserve", mapMessage);
    }

    @RequestMapping(value = "/mygift")
    public ModelAndView myGift(HttpServletRequest request,
                               @RequestParam(value = "p", required = false) Integer pageNo) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            pageNo = pageNo == null ? 1 : pageNo;
            Pagination pagination = new Pagination(pageNo * myGiftPageSize, pageNo, myGiftPageSize);
            UserCenterSession userSession = getUserCenterSeesion(request);
            if (userSession == null) {
                mapMessage.put("list", null);
                return new ModelAndView("/views/jsp/giftmarket/giftmarketwap/mygiftwap", mapMessage);
            }

            mapMessage.put("uno", userSession.getUno());

            long now = System.currentTimeMillis();
            long lastWeek = 60l * 60l * 24l * 1000 * 7;
            long selectDate = now - lastWeek;

            PageRows<ActivityMygiftDTO> pageRows = giftMarketWebLogic.queryActivityMygiftDTO(pagination, new Date(selectDate), null, userSession.getProfileId(), userSession.getAppkey());
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                mapMessage.put("list", pageRows.getRows());
                mapMessage.put("page", pageRows.getPage());
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/common/custompage", mapMessage);
        }
        return new ModelAndView("/views/jsp/giftmarket/giftmarketwap/mygiftwap", mapMessage);
    }

    @RequestMapping(value = "/returnview")
    public ModelAndView returnView() {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("date", new Date());
        return new ModelAndView("/views/jsp/giftmarket/giftmarketwap/gift_session_is_null", mapMessage);
    }
}
