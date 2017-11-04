package com.enjoyf.webapps.joyme.webpage.controller.lottery;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.lottery.*;
import com.enjoyf.platform.service.misc.MiscServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.weixin.WeixinUtil;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.platform.webapps.common.ResultPageMsg;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

/**
 * Created by zhimingli on 2016/6/17 0017.
 */
@Controller
@RequestMapping(value = "/lottery/xingji")
public class XingjihuoxianController extends AbstractLotteryBaseController {
    private static String APPID_TEST = "wx84aede059d0ee638";
    private static String SECRET_TEST = "07ff620c4ac93ce660e6fd57dc6eb1ce";

    private static String APPID = "wx3bd793db50573eb5";
    private static String SECRET = "418f6940c15eafe7e284f2802d2c115c";

    private static final long XINGJI_LOTTERY = 10075l;//todo 及时更换

    //抽奖页面
    @RequestMapping("/testpage")
    public ModelAndView testpage(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        return new ModelAndView("/views/jsp/lottery/xingjihuoxian/xingji", mapMessage);
    }

    //抽奖分享页面
    @RequestMapping("/sharepage")
    public ModelAndView sharepage(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        return new ModelAndView("/views/jsp/lottery/xingjihuoxian/sharexingji", mapMessage);
    }


    //抽奖页面
    @RequestMapping("/page")
    public ModelAndView page(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        //微信网页授权返回code，根据code获取access_token
        String code = request.getParameter("code");
        String access_token = "";
        String openid = "";
        if (!StringUtil.isEmpty(code)) {
            GAlerter.lan("XinjihuoxianController----code>" + code);
            String fullUrl = WeixinUtil.getFullUrl(request);

            //测试环境用测试号
            if (fullUrl.contains("testbeta")) {
                APPID = APPID_TEST;
                SECRET = SECRET_TEST;
            }

            String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + APPID + "&secret=" + SECRET + "&grant_type=authorization_code&code=" + code;
            JSONObject jsonObject = WeixinUtil.httpRequest(url, "GET", null);

            if (jsonObject != null) {
                access_token = jsonObject.getString("access_token");
                openid = jsonObject.getString("openid");
            } else {
                GAlerter.lan("XinjihuoxianController----jsonObject is null");
            }

        }

        //以下信息分享的时候需要 TODO
        String openid_timestamp = System.currentTimeMillis() / 1000 + ""; //时间以秒为单位
        String openid_nonce = "4b4cde46a659";//随机串
        String openid_signature = "";////加密结果
        try {
            String fullUrl = WeixinUtil.getFullUrl(request);
            //测试环境用测试号
            if (fullUrl.contains("testbeta")) {
                APPID = APPID_TEST;
                SECRET = SECRET_TEST;
            }

            String ticket = MiscServiceSngl.get().getTicketCache(APPID, SECRET);
            if (!StringUtil.isEmpty(ticket)) {
                openid_signature = WeixinUtil.getSignature(ticket, openid_timestamp, openid_nonce, fullUrl);
            }
            mapMessage.put("appId", APPID);
            mapMessage.put("timestamp", openid_timestamp);
            mapMessage.put("nonceStr", openid_nonce);
            mapMessage.put("signature", openid_signature);
            mapMessage.put("openid", openid);
        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ModelAndView("/views/jsp/lottery/xingjihuoxian/xingji", mapMessage);
    }


    //抽奖动作
    @RequestMapping("/action")
    @ResponseBody
    public String action(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        String profileId = request.getParameter("openid");
        if (StringUtil.isEmpty(profileId)) {
            return ResultCodeConstants.USER_NOT_LOGIN.getJsonObject().toString();
        }

        JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
        Date curDate = new Date();

        try {
            Date endTime = DateUtil.formatStringToDate("2016-06-28 18:00:00", DateUtil.DEFAULT_DATE_FORMAT2);
            //活动已结束
            if (curDate.getTime() >= endTime.getTime()) {
                Map map = new HashMap();
                map.put("level", -2000);
                jsonObject.put("reuslt", map);
                return jsonObject.toString();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            LotteryAwardItem lotteryAwardItem = LotteryServiceSngl.get().lottery(XINGJI_LOTTERY, profileId, getIp(request), curDate, profileId);


            if (lotteryAwardItem != null) {
                Map map = new HashMap();

                //TODO 先写死，页面没机会了需要提示
                if (lotteryAwardItem.getLotteryAwardItemId() == -1000) {
                    map.put("level", -1000);
                    jsonObject.put("reuslt", map);
                    return jsonObject.toString();
                }


                LotteryAward lotteryAward = LotteryServiceSngl.get().getLotteryAwardById(lotteryAwardItem.getLotteryAwardId());

                if (lotteryAward != null) {
                    map.put("awardid", lotteryAward.getLotteryAwardId());
                    map.put("awardname", lotteryAward.getLotteryAwardName());
                    map.put("awarddesc", lotteryAward.getLotteryAwardDesc());
                    map.put("itemid", lotteryAwardItem.getLotteryAwardItemId());
                    map.put("name1", lotteryAwardItem.getName1());
                    map.put("value1", lotteryAwardItem.getValue1());
                    map.put("name2", lotteryAwardItem.getName2());
                    map.put("value2", lotteryAwardItem.getValue2());
                    map.put("level", lotteryAward.getLotteryAwardLevel());
                    jsonObject.put("reuslt", map);
                    //无抽奖机会了
                }


            }
            return jsonObject.toString();


        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e: ", e);
            return ResultCodeConstants.SYSTEM_ERROR.toString();
        }
    }

    //获取用户的中奖信息
    @RequestMapping("/getlottery")
    @ResponseBody
    public String getlottery(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        String profileId = request.getParameter("openid");
        if (StringUtil.isEmpty(profileId)) {
            return ResultCodeConstants.USER_NOT_LOGIN.getJsonString();
        }

        JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
        try {
            List<UserLotteryLog> userLotteryLogList = LotteryServiceSngl.get().queryUserLotteryLog(XINGJI_LOTTERY, profileId);
            jsonObject.put("result", userLotteryLogList);
            return jsonObject.toString();
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e: ", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
    }


    //分享动作
    @RequestMapping("/share")
    @ResponseBody
    public String share(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
        String profileId = request.getParameter("openid");
        if (StringUtil.isEmpty(profileId)) {
            return ResultCodeConstants.USER_NOT_LOGIN.getJsonString();
        }
        try {
            LotteryServiceSngl.get().addChance(XINGJI_LOTTERY, profileId, 2, new Date());
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }


    //填写用户信息
    @RequestMapping("/address")
    @ResponseBody
    public String address(HttpServletRequest request, HttpServletResponse response,
                          @RequestParam(value = "uid", required = false) String uid,
                          @RequestParam(value = "phone", required = false) String phone,
                          @RequestParam(value = "address", required = false) String addressStr,
                          @RequestParam(value = "qq", required = false) String qq) {
        ResultObjectMsg msg = new ResultObjectMsg(ResultPageMsg.CODE_S);
        if (StringUtil.isEmpty(phone) || StringUtil.isEmpty(addressStr) || StringUtil.isEmpty(qq)) {
            msg.setRs(ResultPageMsg.CODE_E);
            msg.setMsg("parameter.is.null");
            return JsonBinder.buildNormalBinder().toJson(msg);
        }
        LotteryAddress lotteryAddress = new LotteryAddress();
        if (StringUtil.isEmpty(uid)) {
            uid = "uid";
        }

        lotteryAddress.setProfileid(uid);
        Address address = new Address();
        address.setPhone(phone);
        address.setAddress(addressStr);
        address.setQq(qq);
        lotteryAddress.setAddress(address);
        lotteryAddress.setCreateTime(new Date());
        lotteryAddress.setCreateIp(getIp(request));
        boolean bool = false;
        try {
            bool = LotteryServiceSngl.get().modifyLotteryAddressByUid(lotteryAddress, uid);
            if (bool) {
                msg.setMsg("success");
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return JsonBinder.buildNormalBinder().toJson(msg);
    }

}
