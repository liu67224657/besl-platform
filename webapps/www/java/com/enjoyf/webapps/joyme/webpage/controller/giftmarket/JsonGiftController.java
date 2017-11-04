package com.enjoyf.webapps.joyme.webpage.controller.giftmarket;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.TemplateHotdeployConfig;
import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.content.ActivityType;
import com.enjoyf.platform.service.content.MobileExclusive;
import com.enjoyf.platform.service.message.Message;
import com.enjoyf.platform.service.message.MessageServiceSngl;
import com.enjoyf.platform.service.message.MessageType;
import com.enjoyf.platform.service.misc.MiscServiceSngl;
import com.enjoyf.platform.service.misc.SMSLogType;
import com.enjoyf.platform.service.point.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.ProfileFlag;
import com.enjoyf.platform.service.usercenter.UserCenterServiceException;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sms.SMSSenderSngl;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.platform.webapps.common.JoymeResultMsg;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.webapps.joyme.dto.activity.ActivityDTO;
import com.enjoyf.webapps.joyme.dto.activity.ActivityMygiftDTO;
import com.enjoyf.webapps.joyme.weblogic.giftmarket.AllowExchangeStatus;
import com.enjoyf.webapps.joyme.weblogic.giftmarket.GiftMarketWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterSession;
import net.sf.json.JSONObject;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

//import com.enjoyf.webapps.joyme.webpage.base.mvc.UserSession;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-25
 * Time: 上午10:26
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/json/gift")
public class JsonGiftController extends AbstractGiftMarketBaseController {

    private JsonBinder jsonBinder = JsonBinder.buildNormalBinder();

    private static final long CODE_INTERVAL_TIME = 15;

    @Resource(name = "i18nSource")
    private ResourceBundleMessageSource i18nSource;


    @Resource(name = "giftMarketWebLogic")
    private GiftMarketWebLogic giftMarketWebLogic;

    @ResponseBody
    @RequestMapping(value = "/getcode")
    public String exchange(HttpServletRequest request, HttpServletResponse response,
                           @RequestParam(value = "gsid", required = true) long goodsId,
                           @RequestParam(value = "aid", required = true) long activityId) {
        JoymeResultMsg resultMsg = new JoymeResultMsg(JoymeResultMsg.CODE_S);

        try {
            UserCenterSession userSession = getUserCenterSeesion(request);

            if (userSession == null) {
                resultMsg.setMsg(i18nSource.getMessage("user.gift.uno.null", null, Locale.CHINA));
                resultMsg.setStatus_code(JoymeResultMsg.CODE_NOT_LOGIN);
                return jsonBinder.toJson(resultMsg);
            }

            if (StringUtil.isEmpty(userSession.getMobile())) {
                resultMsg.setStatus_code(JoymeResultMsg.USER_IS_NOT_BIND_PHONE);
                return jsonBinder.toJson(resultMsg);
            }
            //todo 没用？？？
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:ss:mm");
            long tim = 30l * 60 * 6l * 1000l;
            Date nowTime = new Date();
            long thirtyMLong = nowTime.getTime() - tim;
            Date thirtyM = new Date(thirtyMLong);

            //绑定 继续 以下 操作
            ActivityGoods activityGoods = PointServiceSngl.get().getActivityGoods(goodsId);
            //通过 商品领取类型 和 用户领取记录 判断 是否允许用户 继续 领取

            AllowExchangeStatus allowExchangeStatus = giftMarketWebLogic.allowGetCode(activityGoods, userSession.getProfileId());
            if (allowExchangeStatus.equals(AllowExchangeStatus.NO_ALLOW)) {
                resultMsg.setMsg(i18nSource.getMessage("message.user.syserror", null, Locale.CHINA));
                resultMsg.setStatus_code(JoymeResultMsg.CODE_PROFILE_BAN);
                return jsonBinder.toJson(resultMsg);
            }
            if (allowExchangeStatus.equals(AllowExchangeStatus.HAS_EXCHANGED)) {
                resultMsg.setMsg(i18nSource.getMessage("user.gift.getcode.one", null, Locale.CHINA));
                resultMsg.setStatus_code(JoymeResultMsg.CODE_FORBID_POST);
                return jsonBinder.toJson(resultMsg);
            }
            if (allowExchangeStatus.equals(AllowExchangeStatus.HAS_EXCHANGED_BY_DAY)) {
                resultMsg.setMsg(i18nSource.getMessage("user.gift.getcode.by.day", null, Locale.CHINA));
                resultMsg.setStatus_code(JoymeResultMsg.CODE_FORBIP);
                return jsonBinder.toJson(resultMsg);
            }
            if (allowExchangeStatus.equals(AllowExchangeStatus.HAS_EXCHANGED_BY_INTRVAL)) {
                resultMsg.setMsg(i18nSource.getMessage("user.gift.getcode.by.intaval", null, Locale.CHINA));
                resultMsg.setStatus_code(JoymeResultMsg.EXCHANG_POINT_NOT_ENOUGH);
                return jsonBinder.toJson(resultMsg);
            }
            Long newDate = System.currentTimeMillis();
            //todo
            Long lastDate = userSession.getGetCodeTime();
            if (lastDate != null) {
                long interval = (newDate - lastDate) / 1000;
                if (interval <= CODE_INTERVAL_TIME) {
                    resultMsg.setMsg(i18nSource.getMessage("user.gift.get.code.interval", null, Locale.CHINA));
                    resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
                    return jsonBinder.toJson(resultMsg);
                }
            }

            //todo 建议用equals
            int exclusive = activityGoods.getChannelType().getCode();
            if (exclusive == MobileExclusive.WEIXIN.getCode()) {
                resultMsg.setMsg(i18nSource.getMessage("is.weixin.exclusive", null, Locale.CHINA));
                resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
                return jsonBinder.toJson(resultMsg);
            } else if (exclusive == MobileExclusive.NEWSCLIENT.getCode()) {
                resultMsg.setMsg(i18nSource.getMessage("is.newsclient.exclusive", null, Locale.CHINA));
                resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
                return jsonBinder.toJson(resultMsg);
            }
            long tomorrow = 60l * 60l * 24l * 1000l;
            long endTime = activityGoods.getEndTime().getTime();
            long now = System.currentTimeMillis();
            if (now > (endTime + tomorrow)) {
                resultMsg.setMsg(i18nSource.getMessage("time.is.out", null, Locale.CHINA));
                resultMsg.setStatus_code("-7");
                return jsonBinder.toJson(resultMsg);
            }


            UserExchangeLog userExchangeLog = PointServiceSngl.get().exchangeGoodsItem(userSession.getUno(), userSession.getProfileId(), userSession.getAppkey(), goodsId, new Date(), getIp(request), "pc", true);
            if (userExchangeLog == null) {
                resultMsg.setMsg(i18nSource.getMessage("user.gift.getcode.null", null, Locale.CHINA));
                resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
                return jsonBinder.toJson(resultMsg);
            }
            userSession.setGetCodeTime(userExchangeLog.getExhangeTime().getTime());
            //站内 通知
//            pushNotice(userExchangeLog.getUserNo(), userExchangeLog.getGoodsName(), userExchangeLog.getGoodsId());
            //修改 本次事件 对应的activity 的 event_time 字段 为 new Date（）
            sendOutEvent(activityId);
//            if (exchangeGoods.getShareId() > 0) {
//                sendOutShareInfo(userSession, exchangeGoods.getShareId());
//            }
            List<UserExchangeLog> userList = new ArrayList<UserExchangeLog>();
            userList.add(userExchangeLog);
            resultMsg.setResult(userList);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            resultMsg.setMsg(i18nSource.getMessage("system.error", null, Locale.CHINA));
            return jsonBinder.toJson(resultMsg);
        }
        return jsonBinder.toJson(resultMsg);
    }

    @ResponseBody
    @RequestMapping(value = "/taoCode")
    public String taoCode(HttpServletRequest request, HttpServletResponse response,
                          @RequestParam(value = "gsid", required = true) long goodsId,
                          @RequestParam(value = "aid", required = true) long activityId) {
        JoymeResultMsg resultMsg = new JoymeResultMsg(JoymeResultMsg.CODE_S);

        try {
            UserCenterSession userSession = getUserCenterSeesion(request);
            if (userSession == null) {
                resultMsg.setMsg(i18nSource.getMessage("user.gift.profile.bind", null, Locale.CHINA));
                resultMsg.setStatus_code(JoymeResultMsg.CODE_NOT_LOGIN);
                return jsonBinder.toJson(resultMsg);
            }
            //判断 用户 账号 是否绑定 第三方账号

            Long newDate = System.currentTimeMillis();
            Long lastDate = userSession.getTaoCodeTime();
            if (lastDate != null) {
                long interval = (newDate - lastDate) / 1000;
                if (interval <= CODE_INTERVAL_TIME) {
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
                resultMsg.setStatus_code("-7");
                return jsonBinder.toJson(resultMsg);
            }
            List<ExchangeGoodsItem> listExchangeGoodsItem = PointServiceSngl.get().taoExchangeGoodsItemByGoodsId(userSession.getUno(), userSession.getProfileId(), userSession.getAppkey(), goodsId, new Date(), getIp(request));

            if (listExchangeGoodsItem.size() < 10) {
                resultMsg.setMsg(i18nSource.getMessage("user.gift.tao.num.limit.10", null, Locale.CHINA));
                resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
                return jsonBinder.toJson(resultMsg);
            }
            userSession.setTaoCodeTime(System.currentTimeMillis());
            //修改 本次事件 对应的activity 的 event_time 字段 为 new Date（）
            sendOutEvent(activityId);

            resultMsg.setResult(listExchangeGoodsItem);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            resultMsg.setMsg(i18nSource.getMessage("system.error", null, Locale.CHINA));
            return jsonBinder.toJson(resultMsg);
        }
        return jsonBinder.toJson(resultMsg);
    }

    /**
     * 手机短信发码
     *
     * @param request
     * @param response
     * @param code
     * @param goodsId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/sendmobile")
    public String sendCodeToMobile(HttpServletRequest request, HttpServletResponse response,
                                   @RequestParam(value = "code", required = false) String code,
                                   @RequestParam(value = "gid", required = false) Long goodsId,
                                   @RequestParam(value = "aid", required = false) Long aid,
                                   @RequestParam(value = "lid", required = false) Long lid) {
        JoymeResultMsg resultMsg = new JoymeResultMsg(JoymeResultMsg.CODE_S);

        try {
            UserCenterSession userSession = getUserCenterSeesion(request);
            //是否登陆
            if (userSession == null) {
                resultMsg.setMsg(i18nSource.getMessage("user.gift.uno.null", null, Locale.CHINA));
                resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
                return jsonBinder.toJson(resultMsg);
            }

            //判断 用户 账号 是否绑定 第三方账号
//            Set<AccountDomain> setDomain = userSession.getSyncDomainSet();
            Boolean bool = false;
            if (userSession.getFlag().hasFlag(ProfileFlag.FLAG_QQ) || userSession.getFlag().hasFlag(ProfileFlag.FLAG_SINA)) {
                bool = true;
            }
            if (!bool) {
                resultMsg.setMsg(i18nSource.getMessage("user.gift.profile.bind", null, Locale.CHINA));
                resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
                return jsonBinder.toJson(resultMsg);
            }
            //码 是否 空
            if (StringUtil.isEmpty(code)) {
                resultMsg.setMsg(i18nSource.getMessage("user.gift.code.null", null, Locale.CHINA));
                resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
                return jsonBinder.toJson(resultMsg);
            }
            //是否 绑定手机
            if (StringUtil.isEmpty(userSession.getMobile())) {
//            if (!ActStatus.ACTED.equals(userSession.getBlogwebsite().getPhoneVerifyStatus())) {
                resultMsg.setMsg(i18nSource.getMessage("profile.phone.no.bind", null, Locale.CHINA));
                resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
                return jsonBinder.toJson(resultMsg);
            }
            //DB是否 有用户的手机
            String phone = userSession.getMobile();
            if (StringUtil.isEmpty(phone)) {
                resultMsg.setMsg(i18nSource.getMessage("profile.phone.no.bind", null, Locale.CHINA));
                resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
                return jsonBinder.toJson(resultMsg);
            }
            //只能发送两次
            List<UserExchangeLog> logList = PointServiceSngl.get().queryUserExchangeByQueryExpress(new QueryExpress()
                    .add(QueryCriterions.eq(UserExchangeLogField.USER_EXCHANGE_LOG_ID, lid))
                    .add(QueryCriterions.eq(UserExchangeLogField.USER_NO, userSession.getUno()))
                    .add(QueryCriterions.eq(UserExchangeLogField.EXCHANGE_GOODS_ID, goodsId)));

            if (CollectionUtil.isEmpty(logList)) {
                resultMsg.setMsg(i18nSource.getMessage("user.gift.log.null", null, Locale.CHINA));
                resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
                return jsonBinder.toJson(resultMsg);
            }
            UserExchangeLog log = logList.get(0);
            if (log.getSmsCount() >= 2) {
                resultMsg.setMsg(i18nSource.getMessage("user.gift.sms.count.two", null, Locale.CHINA));
                resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
                return jsonBinder.toJson(resultMsg);
            }
            //DB的码 和 code 是否相同
            if (!log.getSnValue1().equals(code)) {
                resultMsg.setMsg(i18nSource.getMessage("user.gift.code.not.eq", null, Locale.CHINA));
                resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
                return jsonBinder.toJson(resultMsg);
            }
            //
            ActivityGoods activityGoods = PointServiceSngl.get().getActivityGoods(goodsId);
            if (activityGoods == null) {
                resultMsg.setMsg(i18nSource.getMessage("content.activity.null", null, Locale.CHINA));
                resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
                return jsonBinder.toJson(resultMsg);
            }

            //开始发送
            // 1.先执行 db的 写入操作，
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.increase(UserExchangeLogField.SMS_COUNT, 1);
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(UserExchangeLogField.USER_EXCHANGE_LOG_ID, log.getUserExchangeLogId()));
            boolean isIncrease = PointServiceSngl.get().increaseSmsCountExchangLog(updateExpress, queryExpress);
            if (!isIncrease) {
                resultMsg.setMsg(i18nSource.getMessage("message.user.syserror", null, Locale.CHINA));
                resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
                return jsonBinder.toJson(resultMsg);
            }
            //2. 发送短信
            TemplateHotdeployConfig templateConfig = HotdeployConfigFactory.get().getConfig(TemplateHotdeployConfig.class);
            String vTemplate = templateConfig.getGiftCodeMobileSmsTemplate();
            Map<String, String> paramMap = new HashMap<String, String>();

            paramMap.put("goodsname", activityGoods.getActivitySubject());
            paramMap.put("code", code);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = df.format(activityGoods.getEndTime());
            paramMap.put("date", date);

            try {
                boolean boolSms = MiscServiceSngl.get().sendSMS(phone, NamedTemplate.parse(vTemplate).format(paramMap), SMSLogType.GIFT_CODE.getCode(), SMSSenderSngl.CODE_DEFAULT);
                if (boolSms) {
                    resultMsg.setMsg(i18nSource.getMessage("user.gift.sms.code.mobile.success", null, Locale.CHINA));
                    resultMsg.setStatus_code(JoymeResultMsg.CODE_S);
                    return jsonBinder.toJson(resultMsg);
                } else {
                    GAlerter.lab(this.getClass().getName() + " sendSMS error");
                    resultMsg.setMsg(i18nSource.getMessage("send.url.error", null, Locale.CHINA));
                    resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
                    return jsonBinder.toJson(resultMsg);
                }
            } catch (ServiceException e) {
                GAlerter.lab(this.getClass().getName() + " sendSMS error.e:", e);
                resultMsg.setMsg(i18nSource.getMessage("system.error", null, Locale.CHINA));
                return jsonBinder.toJson(resultMsg);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            resultMsg.setMsg(i18nSource.getMessage("system.error", null, Locale.CHINA));
            return jsonBinder.toJson(resultMsg);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/mygift")
    public String myGift(HttpServletRequest request, HttpServletResponse response,
                         @RequestParam(value = "p", required = false) Integer pageNo) {

        String callback = request.getParameter("callback");
        pageNo = pageNo == null ? 1 : pageNo;
        Pagination pagination = new Pagination(pageNo * myMGiftPageSize, pageNo, myMGiftPageSize);
        UserCenterSession userCenterSession = getUserCenterSeesion(request);
        if (userCenterSession == null) {
            if (StringUtil.isEmpty(callback)) {
                return ResultCodeConstants.USER_NOT_LOGIN.getJsonString();
            } else {
                return callback + "([" + ResultCodeConstants.USER_NOT_LOGIN.getJsonString() + "])";
            }
        }
        PageRows<ActivityMygiftDTO> pageRows = null;
        try {
            pageRows = giftMarketWebLogic.queryActivityMygiftDTO(pagination, null, null, userCenterSession.getProfileId(), userCenterSession.getAppkey());
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("rs", String.valueOf(ResultCodeConstants.SUCCESS.getCode()));
                jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
                JSONObject result = new JSONObject();
                result.put("rows", pageRows.getRows());
                result.put("page", pageRows.getPage());
                jsonObject.put("result", result);
                if (StringUtil.isEmpty(callback)) {
                    return jsonObject.toString();
                } else {
                    return callback + "([" + jsonObject.toString() + "])";
                }
            }
        } catch (ServiceException e) {
            if (StringUtil.isEmpty(callback)) {
                return ResultCodeConstants.ERROR.getJsonString();
            } else {
                return callback + "([" + ResultCodeConstants.ERROR.getJsonString() + "])";
            }
        }
        if (StringUtil.isEmpty(callback)) {
            return ResultCodeConstants.SUCCESS.getJsonString();
        } else {
            return callback + "([" + ResultCodeConstants.SUCCESS.getJsonString() + "])";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/more")
    public String more(HttpServletRequest request, HttpServletResponse response,
                       @RequestParam(value = "firstletter", required = false) String firstLetter,
                       @RequestParam(value = "platform", required = false) String platform,
                       @RequestParam(value = "pattern", required = false) String pattern,
                       @RequestParam(value = "exclusive", required = false) String exclusive,
                       @RequestParam(value = "p", required = false, defaultValue = "1") Integer pageNo) {

        String callback = request.getParameter("callback");
        pageNo = pageNo == null ? 1 : pageNo;
        Pagination pagination = new Pagination(pageNo * WAP_MORE_PAGE_SIZE, pageNo, WAP_MORE_PAGE_SIZE);

        PageRows<ActivityGoods> pageRows = null;
        try {
            pageRows = PointServiceSngl.get().queryActivityGoodsByLetter(ActivityType.EXCHANGE_GOODS, firstLetter.toLowerCase(), null);
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("rs", String.valueOf(ResultCodeConstants.SUCCESS.getCode()));
                jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
                JSONObject result = new JSONObject();
                result.put("rows", pageRows.getRows());
                result.put("page", pageRows.getPage());
                jsonObject.put("result", result);
                if (StringUtil.isEmpty(callback)) {
                    return jsonObject.toString();
                } else {
                    return callback + "([" + jsonObject.toString() + "])";
                }
            }
        } catch (ServiceException e) {
            if (StringUtil.isEmpty(callback)) {
                return ResultCodeConstants.ERROR.getJsonString();
            } else {
                return callback + "([" + ResultCodeConstants.ERROR.getJsonString() + "])";
            }
        }
        if (StringUtil.isEmpty(callback)) {
            return ResultCodeConstants.SUCCESS.getJsonString();
        } else {
            return callback + "([" + ResultCodeConstants.SUCCESS.getJsonString() + "])";
        }
    }

    /**
     * 绑定手机
     *
     * @param request
     * @param phone
     * @param verifyCode
     * @return
     */
    @RequestMapping(value = "/verifyphone")
    @ResponseBody
    public String verify(HttpServletRequest request, @RequestParam(value = "phone", required = false) String phone,
                         @RequestParam(value = "verifycode", required = false) String verifyCode) {
        String callback = request.getParameter("callback");
        try {
            if (StringUtil.isEmpty(verifyCode)) {
                if (StringUtil.isEmpty(callback)) {
                    return ResultCodeConstants.MOBILE_VERIFY_CODE_NULL.getJsonString();
                } else {
                    return callback + "([" + ResultCodeConstants.MOBILE_VERIFY_CODE_NULL.getJsonString() + "])";
                }

            }
            if (StringUtil.isEmpty(phone)) {
                if (StringUtil.isEmpty(callback)) {
                    return ResultCodeConstants.MOBILE_PHONE_NULL.getJsonString();
                } else {
                    return callback + "([" + ResultCodeConstants.MOBILE_PHONE_NULL.getJsonString() + "])";
                }
            }

            UserCenterSession userSession = getUserCenterSeesion(request);
//            String sessionCode = UserCenterServiceSngl.get().getMobileCode(userSession.getUno());
//
//            if (StringUtil.isEmpty(sessionCode)) {
//                if (StringUtil.isEmpty(callback)) {
//                    return ResultCodeConstants.MOBILE_VERIFY_CODE_ERROR.getJsonString();
//                } else {
//                    return callback + "([" + ResultCodeConstants.MOBILE_VERIFY_CODE_ERROR.getJsonString() + "])";
//                }
//            }
//            if (!sessionCode.equals(verifyCode)) {
//                if (StringUtil.isEmpty(callback)) {
//                    return ResultCodeConstants.MOBILE_VERIFY_CODE_ERROR.getJsonString();
//                } else {
//                    return callback + "([" + ResultCodeConstants.MOBILE_VERIFY_CODE_ERROR.getJsonString() + "])";
//                }
//            }
            if(!UserCenterServiceSngl.get().checkMobileVerifyCode(phone,verifyCode)){
                if (StringUtil.isEmpty(callback)) {
                    return ResultCodeConstants.MOBILE_VERIFY_CODE_ERROR.getJsonString();
                } else {
                    return callback + "([" + ResultCodeConstants.MOBILE_VERIFY_CODE_ERROR.getJsonString() + "])";
                }
            }
            boolean bval = UserCenterServiceSngl.get().bindMobile(phone, userSession.getProfileId(), getIp(request));
            if (bval) {
                userSession.setMobile(phone);
            }
        } catch (ServiceException e) {
            //手机号或者账号已经绑定过
            if (e.equals(UserCenterServiceException.BIND_PHONE_ERROR_PRFOILE_HAS_BINDED) ||
                    e.equals(UserCenterServiceException.BIND_PHONE_ERROR_PHONE_HAS_BINDED)) {
                if (StringUtil.isEmpty(callback)) {
                    return ResultCodeConstants.MOBILE_HAS_BINDED.getJsonString();
                } else {
                    return callback + "([" + ResultCodeConstants.MOBILE_HAS_BINDED.getJsonString() + "])";
                }
            } else {
                GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
                if (StringUtil.isEmpty(callback)) {
                    return ResultCodeConstants.ERROR.getJsonString();
                } else {
                    return callback + "([" + ResultCodeConstants.ERROR.getJsonString() + "])";
                }
            }
        }
        if (StringUtil.isEmpty(callback)) {
            return ResultCodeConstants.SUCCESS.getJsonString();
        } else {
            return callback + "([" + ResultCodeConstants.SUCCESS.getJsonString() + "])";
        }
    }

    @RequestMapping(value = "/searchpage")
    @ResponseBody
    public String searchPage(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "searchtext", required = false) String searchText,
                             @RequestParam(value = "p", required = false, defaultValue = "1") Integer pageNo,
                             @RequestParam(value = "psize", required = false, defaultValue = "10") Integer psize) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        String callback = request.getParameter("callback");
        try {
            Pagination paginatin = new Pagination(pageNo * psize, pageNo, psize);
            PageRows<ActivityDTO> activityDTOPageRows = giftMarketWebLogic.searchActivity(searchText, paginatin);
            mapMessage.put("searchtext", searchText);
            if (activityDTOPageRows != null) {
                mapMessage.put("list", activityDTOPageRows.getRows());
                mapMessage.put("page", activityDTOPageRows.getPage());
            } else {
                mapMessage.put("list", "");
                mapMessage.put("page", "");


            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("rs", "1");
            jsonObject.put("result", mapMessage);
            return callback + "([" + jsonObject.toString() + "])";
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return callback + "([" + ResultCodeConstants.ERROR.getJsonString() + "])";
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return callback + "([" + ResultCodeConstants.ERROR.getJsonString() + "])";
        }

    }

    private void pushNotice(String uno, String goodsName, Long id) {
        try {
            Map<String, String> paramMap = new HashMap<String, String>();
            ExchangeGoods goods = PointServiceSngl.get().getExchangeGoodS(id);
            String noticeTemplate = goods.getNoticeBody();

            paramMap.put("gamesname", goodsName);
            paramMap.put("URL_WWW", WebappConfig.get().getUrlWww());

            String messageBody = "";
            messageBody = NamedTemplate.parse(noticeTemplate).format(paramMap);

            Message message = new Message();
            message.setBody(messageBody);
            message.setMsgType(MessageType.OPERATION);
            message.setOwnUno(uno);
            message.setRecieverUno(uno);
            message.setSendDate(new Date());

            MessageServiceSngl.get().postMessage(uno, message);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }
    }
}