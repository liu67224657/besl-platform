package com.enjoyf.webapps.joyme.webpage.controller.joymeapp.gameclient.webview;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.event.EventServiceSngl;
import com.enjoyf.platform.service.event.task.*;
import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.gameres.gamedb.GameDB;
import com.enjoyf.platform.service.gameres.gamedb.GameDBChannel;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.lottery.LotteryAward;
import com.enjoyf.platform.service.lottery.LotteryServiceSngl;
import com.enjoyf.platform.service.message.MessageServiceSngl;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.point.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.Token;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.http.AppUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.webapps.joyme.dto.task.TaskInfoDTO;
import com.enjoyf.webapps.joyme.weblogic.event.TaskWebLogic;
import com.enjoyf.webapps.joyme.weblogic.gamedb.GameDbWebLogic;
import com.enjoyf.webapps.joyme.weblogic.giftmarket.GiftMarketWebLogic;
import com.enjoyf.webapps.joyme.webpage.controller.joymeapp.gameclient.AbstractGameClientBaseController;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by zhimingli
 * Date: 2015/01/15
 * Time: 9:38
 */
@Controller
@RequestMapping("/joymeapp/gameclient/webview/task")
public class GameClientTaskWebViewController extends AbstractGameClientBaseController {

    @Resource(name = "giftMarketWebLogic")
    private GiftMarketWebLogic giftMarketWebLogic;

    @Resource(name = "gameDbWebLogic")
    private GameDbWebLogic gameDbWebLogic;

    @Resource(name = "taskWebLogic")
    private TaskWebLogic taskWebLogic;

    private static HashMap<String, Long> lotteryIdMap = new HashMap<String, Long>();

    private static List<String> firstDayTotteryLog = new ArrayList<String>();
    private static List<String> otherTotteryLog = new ArrayList<String>();

    private static String ACTIVITY_DW_2015_TIAN = "activity_2015_dw_tian";
    private static String ACTIVITY_DW_2015_XIAN = "activity_2015_dw_xian";

    static {
        //测试专用
        lotteryIdMap.put("2015-05-11", 10043L);

        lotteryIdMap.put("2015-05-12", 10030L);
        lotteryIdMap.put("2015-05-13", 10040L);
        lotteryIdMap.put("2015-05-14", 10031L);
        lotteryIdMap.put("2015-05-15", 10041L);
        lotteryIdMap.put("2015-05-16", 10032L);
        lotteryIdMap.put("2015-05-17", 10042L);
        lotteryIdMap.put("2015-05-18", 10033L);

//        firstDayTotteryLog.add("恭喜李墨客获得限量手办");
//        firstDayTotteryLog.add("恭喜刘凯获得着迷精美U盘");
//        firstDayTotteryLog.add("恭喜李墨客获得游戏抱枕");
//        firstDayTotteryLog.add("恭喜张然获得AppleWatch");
//        firstDayTotteryLog.add("恭喜任远获得着迷高档毛毡文件袋");
//        firstDayTotteryLog.add("恭喜张子楠获得精美卡通抱枕");
//        firstDayTotteryLog.add("恭喜李丽获得着迷精美U盘");
//        firstDayTotteryLog.add("恭喜徐萌萌获得炫光游戏鼠标");
//        firstDayTotteryLog.add("恭喜许可获得大容量移动电源");
        firstDayTotteryLog.add("恭喜 故巷笑别获得 大闸蟹礼券一张");
        firstDayTotteryLog.add("恭喜 居然真中2Q币获得 大闸蟹礼券一张");
        firstDayTotteryLog.add("恭喜 旧人难拥°获得 大闸蟹礼券一张");
        firstDayTotteryLog.add("恭喜 配角碍人获得 大闸蟹礼券一张");
        firstDayTotteryLog.add("恭喜 巷尾-temper获得 大闸蟹礼券一张");
        firstDayTotteryLog.add("恭喜 匹诺曹获得 大闸蟹礼券一张");
        firstDayTotteryLog.add("恭喜 Jason获得 着迷公仔一个");
        firstDayTotteryLog.add("恭喜 T行者获得 着迷公仔一个");
        firstDayTotteryLog.add("恭喜 Se Hun获得 着迷公仔一个");
        firstDayTotteryLog.add("恭喜 嗨、达令！获得 着迷公仔一个");
        firstDayTotteryLog.add("恭喜 devil获得 着迷公仔一个");
        firstDayTotteryLog.add("恭喜 女王さま获得 着迷公仔一个");
        firstDayTotteryLog.add("恭喜 Laity获得 着迷LOGO帽一个");
        firstDayTotteryLog.add("恭喜 独眼竜获得 着迷LOGO帽一个");
        firstDayTotteryLog.add("恭喜 非Reality获得 着迷LOGO帽一个");
        firstDayTotteryLog.add("恭喜 红色三倍速获得 着迷LOGO帽一个");
        firstDayTotteryLog.add(" 恭喜 Justify获得 着迷LOGO帽一个");
        firstDayTotteryLog.add("恭喜 てんかふぶ获得 着迷LOGO帽一个");
        firstDayTotteryLog.add("恭喜 孤痞获得 迷豆30个");
        firstDayTotteryLog.add("恭喜 しにがみ获得 迷豆30个");
        firstDayTotteryLog.add("恭喜 Yang-tong获得 迷豆30个");
        firstDayTotteryLog.add("恭喜 凛妙喵获得 迷豆30个");
        firstDayTotteryLog.add("恭喜 DCboy获得 迷豆30个");
        firstDayTotteryLog.add("恭喜 看毛啊？获得 迷豆30个");
        firstDayTotteryLog.add("恭喜 Batman获得 迷豆30个");
        firstDayTotteryLog.add("恭喜 这村我说的算获得 迷豆30个");
        firstDayTotteryLog.add("恭喜 big诺获得 迷豆50个");
        firstDayTotteryLog.add("恭喜 车干衣申获得 迷豆50个");
        firstDayTotteryLog.add("恭喜 TAfox2003获得 迷豆50个");
        firstDayTotteryLog.add("恭喜 撒西比获得 迷豆50个");
        firstDayTotteryLog.add("恭喜 CocoBizi获得 迷豆100个");
        firstDayTotteryLog.add("恭喜 夜露死苦获得 迷豆100个");
        firstDayTotteryLog.add("恭喜 OpWang获得 迷豆100个");
        firstDayTotteryLog.add("恭喜 人生就是要开心获得 迷豆100个");
        firstDayTotteryLog.add("恭喜 Starkindustries获得 迷豆100个");
        firstDayTotteryLog.add("恭喜 纯音获得 迷豆100个");
        firstDayTotteryLog.add("恭喜 土岐ユナ大获得 迷豆100个");
        firstDayTotteryLog.add("恭喜 石田桃の助获得 迷豆100个");
        firstDayTotteryLog.add("恭喜 力天使14徒获得 迷豆100个");
        firstDayTotteryLog.add("恭喜 Tiny获得 迷豆100个");
        firstDayTotteryLog.add("恭喜 大世界战略获得 迷豆200个");
        firstDayTotteryLog.add("恭喜 Magnus获得 迷豆200个");
        firstDayTotteryLog.add("恭喜 腐国贵工子获得 迷豆200个");
        firstDayTotteryLog.add("恭喜 Crixalis获得 迷豆200个");
        firstDayTotteryLog.add("恭喜 神阿拉丁丁获得 迷豆200个");
        firstDayTotteryLog.add("恭喜 MogulKahn获得 迷豆200个");
        firstDayTotteryLog.add("恭喜 还有大腰子么获得 迷豆200个");
        firstDayTotteryLog.add("恭喜 Abaddon获得 迷豆200个");
        firstDayTotteryLog.add("恭喜 少来朱砂获得 迷豆200个");
        firstDayTotteryLog.add("恭喜 Azgalor获得 迷豆200个");




        otherTotteryLog.add("恭喜刘天怡获得iPhone6");
        otherTotteryLog.add("恭喜李墨客获得限量手办");
        otherTotteryLog.add("恭喜刘凯获得着迷精美U盘");
        otherTotteryLog.add("恭喜常素素获得Apple Watch");
        otherTotteryLog.add("恭喜李墨客获得游戏抱枕");
        otherTotteryLog.add("恭喜张然获得AppleWatch");
        otherTotteryLog.add("恭喜任远获得着迷高档毛毡文件袋");
        otherTotteryLog.add("恭喜张子楠获得精美卡通抱枕");
        otherTotteryLog.add("恭喜李丽获得着迷精美U盘");
        otherTotteryLog.add("恭喜郭佳获得iPhone6");
        otherTotteryLog.add("恭喜徐萌萌获得炫光游戏鼠标");
        otherTotteryLog.add("恭喜许可获得大容量移动电源");
        otherTotteryLog.add("恭喜张超获得Apple Watch一块");
        otherTotteryLog.add("恭喜王小楠获得着迷精美卡通抱枕");
        otherTotteryLog.add("恭喜李思思获得着迷限量收藏手办");
        otherTotteryLog.add("恭喜王容嘉获得着迷纪念豪华大礼包");
        otherTotteryLog.add("恭喜张天爱获得着迷精美U盘");
        otherTotteryLog.add("恭喜赵凡获得着迷毛毡文件夹");
        otherTotteryLog.add("恭喜赵敏获得着迷限量收藏手办");
        otherTotteryLog.add("恭喜刘可欣获得着迷精美卡通抱枕");
        otherTotteryLog.add("恭喜李柯获得着迷限量收藏手办");
        otherTotteryLog.add("恭喜郭靖获得着迷精美U盘");
        otherTotteryLog.add("恭喜李思琪获得着迷毛毡文件夹");
        otherTotteryLog.add("恭喜赵子强获得Apple Watch一块");
        otherTotteryLog.add("恭喜张勇获得着迷限量收藏手办");
        otherTotteryLog.add("恭喜李牧彦获得着迷纪念豪华大礼包");
        otherTotteryLog.add("恭喜刘语熙获得着迷精美U盘");
        otherTotteryLog.add("恭喜张媛媛获得着迷毛毡文件夹");
        otherTotteryLog.add("恭喜郭志明获得着迷限量收藏手办");
        otherTotteryLog.add("恭喜欧阳乐获得着迷精美卡通抱枕");
        otherTotteryLog.add("恭喜施乐佳获得着迷限量收藏手办");
        otherTotteryLog.add("恭喜陈曦羡获得着迷纪念豪华大礼包");
        otherTotteryLog.add("恭喜刘心悠获得着迷精美U盘");
        otherTotteryLog.add("恭喜赵昕得着迷毛毡文件夹");

        otherTotteryLog.add("恭喜郑天宇获得iPhone6");
        otherTotteryLog.add("恭喜曾镭淼获得Apple Watch一块");
        otherTotteryLog.add("恭喜刘然获得Apple Watch一块");
        otherTotteryLog.add("恭喜武文晶获得裂魂传游戏抱枕");
        otherTotteryLog.add("恭喜姜巍巍获得裂魂传游戏抱枕");
        otherTotteryLog.add("恭喜吴楠获得100元话费充值卡");
        otherTotteryLog.add("恭喜王玉获得着迷U盘");
        otherTotteryLog.add("恭喜任红获得着迷鸭舌帽");
        otherTotteryLog.add("恭喜杨兆燕获得着迷鸭舌帽");
        otherTotteryLog.add("恭喜刘煜获得着迷鸭舌帽");
    }

    //查看任务列表
    @RequestMapping(value = "/list")
    public ModelAndView taskList(HttpServletRequest request, HttpServletResponse response) throws ServiceException {

        String appkey = HTTPUtil.getParam(request, "appkey");
        String platform = HTTPUtil.getParam(request, "platform");
        String uidParam = HTTPUtil.getParam(request, "uid");

        //不使用session进入   /joymeapp/gameclient/webview/giftmarket/list ，使用parameter retype来进入
//        HttpSession session = request.getSession();
//        session.setAttribute(KEY_GIFT_TAB_FLAG, VALUE_GIFT_TAB_FLAG);

        AppPlatform appPlatform = null;
        try {
            appPlatform = AppPlatform.getByCode(Integer.parseInt(platform));
        } catch (Exception e) {
        }
        long uid = -1;
        try {
            uid = Long.parseLong(uidParam);
        } catch (NumberFormatException e) {
        }

        if (uid < 0l || appPlatform == null || StringUtil.isEmpty(appkey)) {
            return null;
        }

        Map map = new HashMap();
        Date doTaskDate = new Date();
        try {
            Profile profile = UserCenterServiceSngl.get().getProfileByUid(uid);
            if (profile == null) {
                return null;
            }

            //get userpoint
            UserPoint userPoint = null;
            try {
                userPoint = giftMarketWebLogic.getUserPoint(appkey, profile);
            } catch (ServiceException e) {
                GAlerter.lab(this.getClass().getName() + "occured Exception.e:", e);
            }

            //获取这一appkey,在这一平台的所有任务
            Map<String, List<Task>> taskMap = EventServiceSngl.get().queryTaskByGroupIds(AppUtil.getAppKey(appkey), appPlatform, TaskGroupType.COMMON);
            //获取所有当前已经完成的任务
            Map<String, TaskLog> cTaskMap = EventServiceSngl.get().checkCompleteTask(profile.getProfileId(), AppUtil.getAppKey(appkey), appPlatform, doTaskDate);

            TaskInfoDTO taskInfoDTO = taskWebLogic.getTaskInfo(taskMap, cTaskMap, profile);

            map.put("uncomplete", taskInfoDTO.getUnComplete());
            map.put("allcount", taskInfoDTO.getTaskCount());

            map.put("point", String.valueOf(userPoint == null ? 0 : userPoint.getUserPoint()));
            map.put("profile", profile);
            map.put("platform", platform);

            map.put("list", taskInfoDTO.getList());
            map.put("completeMap", cTaskMap);
            map.put("appkey", AppUtil.getAppKey(appkey));

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
        }

        return new ModelAndView("/views/jsp/gameclient/webview/task-list", map);
    }

    private static final String GAMETASK_IP_KEY = "gametask_ip_key";

//    @RequestMapping(value = "/taskdownload")
//    public ModelAndView taskDownload(HttpServletRequest request) {
//        Map<String, Object> mapMessage = new HashMap<String, Object>();
//        String clientId = HTTPUtil.getParam(request, "clientid");
//        String loginDomain = HTTPUtil.getParam(request, "logindomain");
//        String platform = HTTPUtil.getParam(request, "platform");
//        String appkey = HTTPUtil.getParam(request, "appkey");
//        String uno = HTTPUtil.getParam(request, "uno");
//        String uid = HTTPUtil.getParam(request, "uid");
//        String token = HTTPUtil.getParam(request, "token");
//        mapMessage.put("clientId", clientId);
//        mapMessage.put("logindomain", loginDomain);
//        mapMessage.put("platform", platform);
//        mapMessage.put("uno", uno);
//        mapMessage.put("appkey", appkey);
//        mapMessage.put("taskId", "qbshare");
//        mapMessage.put("token", token);
//        mapMessage.put("uid", uid);
//        return new ModelAndView("/views/jsp/gameclient/webview/task-downloadindex", mapMessage);
//    }

    /**
     * 抽奖动作
     */
    @ResponseBody
    @RequestMapping(value = "/taskdownload")
    public String taskDownload(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        //2015 端午活动
        String type = request.getParameter("type");
        if ("2015dw".equals(type)) {
            String support = request.getParameter("support");
            String num = request.getParameter("num");
            try {
                long number = 1L;
                if (!StringUtil.isEmpty(num)) {
                    number = Long.valueOf(num);
                }
                if ("tian".equals(support)) {
                    MessageServiceSngl.get().setTORedisIncr(ACTIVITY_DW_2015_TIAN, number, -1);
                } else if ("xian".equals(support)) {
                    MessageServiceSngl.get().setTORedisIncr(ACTIVITY_DW_2015_XIAN, number, -1);
                }
            } catch (ServiceException e) {
                e.printStackTrace();
            }
            jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
            jsonObject.put("rs", String.valueOf(ResultCodeConstants.SUCCESS.getCode()));
            return "taskdownload([" + jsonObject.toString() + "])";
        }


        Map<String, String> JParam = HTTPUtil.getJParam(request.getParameter("JParam"));
        String appKey = StringUtil.isEmpty(HTTPUtil.getParam(request, "appkey")) ? JParam.get("appkey") : HTTPUtil.getParam(request, "appkey");
        String uid = StringUtil.isEmpty(HTTPUtil.getParam(request, "uid")) ? JParam.get("uid") : HTTPUtil.getParam(request, "uid");
        String platform = StringUtil.isEmpty(HTTPUtil.getParam(request, "platform")) ? JParam.get("platform") : HTTPUtil.getParam(request, "platform");
        String clientId = StringUtil.isEmpty(HTTPUtil.getParam(request, "clientid")) ? JParam.get("clientid") : HTTPUtil.getParam(request, "clientid");
        String channel = StringUtil.isEmpty(HTTPUtil.getParam(request, "channelid")) ? JParam.get("channelid") : HTTPUtil.getParam(request, "channelid");
        String loginDomain = StringUtil.isEmpty(HTTPUtil.getParam(request, "logindomain")) ? JParam.get("logindomain") : HTTPUtil.getParam(request, "logindomain");
        String tokenStr = StringUtil.isEmpty(HTTPUtil.getParam(request, "token")) ? JParam.get("token") : HTTPUtil.getParam(request, "token");
        String code = request.getParameter("code");
        try {
            if (StringUtil.isEmpty(uid) || StringUtil.isEmpty(clientId) || StringUtil.isEmpty(appKey) || StringUtil.isEmpty(platform) || StringUtil.isEmpty(tokenStr) || loginDomain.equals("client")) {
                return ResultCodeConstants.USER_NOT_LOGIN.getJsonString();
            }

//            if (!appKey.endsWith("I") || !AppUtil.checkIsIOS(request) || !AppPlatform.IOS.equals(AppPlatform.getByCode(Integer.valueOf(platform))) || !channel.contains("appstore")) {
//                return ResultCodeConstants.FAILED.getJsonString();
//            }
            Token token = UserCenterServiceSngl.get().getToken(tokenStr);
            if (token == null) {
                return ResultCodeConstants.USER_NOT_LOGIN.getJsonString();
            } else {
                if (!uid.equals(token.getUid() + "")) {
                    return ResultCodeConstants.USER_NOT_LOGIN.getJsonString();
                }
            }

            Profile profile = UserCenterServiceSngl.get().getProfileByUid(Long.valueOf(uid));
            if (profile == null) {
                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
            }
            appKey = AppUtil.getAppKey(appKey);
            AuthApp app = OAuthServiceSngl.get().getApp(appKey);
            if (app == null) {
                return ResultCodeConstants.APP_NOT_EXISTS.getJsonString();
            }


//            Long lotteryId = 10050L;//这是dev环境的
//            if (WebappConfig.get().getDomain().contains("joyme.com")) {
//                lotteryId = lotteryIdMap.get(DateUtil.formatDateToString(new Date(), DateUtil.PATTERN_DATE));
//            }

            LotteryAward lotteryAward = LotteryServiceSngl.get().userLottery(-1l, profile.getProfileId(), getIp(request), profile, StringUtil.isEmpty(code) ? "" : code);

            jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
            jsonObject.put("rs", String.valueOf(ResultCodeConstants.SUCCESS.getCode()));
            if (lotteryAward == null) {
                jsonObject.put("result", null);
            } else {
                if (lotteryAward.getLotteryAwardLevel() == 0) {
                    PointActionHistory pointActionHistory = new PointActionHistory();
                    pointActionHistory.setActionDate(new Date());
                    pointActionHistory.setActionDescription("玩霸中秋抽奖");
                    pointActionHistory.setActionType(PointActionType.LOTTERY_AWARD);
                    pointActionHistory.setCreateDate(new Date());
                    pointActionHistory.setPointValue(Integer.parseInt(StringUtil.isEmpty(lotteryAward.getLotteryAwardDesc()) ? "0" : lotteryAward.getLotteryAwardDesc()));
                    pointActionHistory.setProfileId(profile.getProfileId());
                    pointActionHistory.setUserNo(profile.getUno());
                    PointServiceSngl.get().increasePointActionHistory(pointActionHistory, PointKeyType.SYHB);
                }

                JSONObject lotteryObj = new JSONObject();
                lotteryObj.put("lotteryAwardLevel", lotteryAward.getLotteryAwardLevel());
                lotteryObj.put("lotteryAwardName", lotteryAward.getLotteryAwardName());
                lotteryObj.put("lotteryAwardDesc", lotteryAward.getLotteryAwardDesc());
                lotteryObj.put("lotteryAwardId", lotteryAward.getLotteryAwardId());
                lotteryObj.put("lotteryTimes", lotteryAward.getCreateIp());
                jsonObject.put("result", lotteryObj);
            }
            return jsonObject.toString();
        } catch (Exception e) {
            if (e.getMessage().contains("uniqe_code_id")) {
                Map map = new HashMap();
                map.put("msg", "success");
                jsonObject.put("rs", "1");
                jsonObject.put("result", null);
                return jsonObject.toString();
            } else {
                GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
                return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
            }
        }

    }

    /**
     * 抽奖页面
     */
    @RequestMapping(value = "/taskgetcode")
    public ModelAndView taskGetCode(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        String type = request.getParameter("type");
        mapMessage.put("JParam", HTTPUtil.getParam(request, "JParam"));
        Map<String, String> JParam = HTTPUtil.getJParam(request.getParameter("JParam"));
        String appKey = StringUtil.isEmpty(HTTPUtil.getParam(request, "appkey")) ? JParam.get("appkey") : HTTPUtil.getParam(request, "appkey");
        String uid = StringUtil.isEmpty(HTTPUtil.getParam(request, "uid")) ? JParam.get("uid") : HTTPUtil.getParam(request, "uid");
        String platform = StringUtil.isEmpty(HTTPUtil.getParam(request, "platform")) ? JParam.get("platform") : HTTPUtil.getParam(request, "platform");
        String clientId = StringUtil.isEmpty(HTTPUtil.getParam(request, "clientid")) ? JParam.get("clientid") : HTTPUtil.getParam(request, "clientid");
        String channel = StringUtil.isEmpty(HTTPUtil.getParam(request, "channelid")) ? JParam.get("channelid") : HTTPUtil.getParam(request, "channelid");
        String loginDomain = StringUtil.isEmpty(HTTPUtil.getParam(request, "logindomain")) ? JParam.get("logindomain") : HTTPUtil.getParam(request, "logindomain");
        String tokenString = StringUtil.isEmpty(HTTPUtil.getParam(request, "token")) ? JParam.get("token") : HTTPUtil.getParam(request, "token");

//        Cookie cookie[] = request.getCookies();
//        GAlerter.lan("cookielength========================" + cookie.length);
//        for (int i = 0; i < cookie.length; i++) {
//            GAlerter.lan("Cookie+=============" + cookie[i].getName() + "========" + cookie[i].getValue());
//        }


        try {
            if ("2015dw".equals(type)) {
                try {
                    String tian = MessageServiceSngl.get().getRedis(ACTIVITY_DW_2015_TIAN);
                    String xian = MessageServiceSngl.get().getRedis(ACTIVITY_DW_2015_XIAN);
                    Integer tianIn = StringUtil.isEmpty(tian) ? 0 : Integer.valueOf(tian);
                    Integer xianIn = StringUtil.isEmpty(xian) ? 0 : Integer.valueOf(xian);
                    Integer total = tianIn + xianIn;
                    String tianPer = "50";
                    String xianPer = "50";
                    if (total > 0) {
                        DecimalFormat df = new DecimalFormat("0");
                        Double tianDou = (tianIn * 1.0 / total) * 100;
                        Double xianDou = (xianIn * 1.0 / total) * 100;
                        if (tianDou > 70) {
                            tianPer = "70";
                            xianPer = "30";
                        } else if (xianDou > 70) {
                            tianPer = "30";
                            xianPer = "70";
                        } else {
                            tianPer = df.format(tianDou);
                            xianPer = df.format(xianDou);
                        }
                    }
                    mapMessage.put("tianIn", tianIn);
                    mapMessage.put("xianIn", xianIn);
                    mapMessage.put("tianPer", tianPer);
                    mapMessage.put("xianPer", xianPer);
                } catch (ServiceException e) {
                    e.printStackTrace();
                }
                if ("share".equals(request.getParameter("share"))) {
                    return new ModelAndView("/views/jsp/activity/2015dw/dw_share", mapMessage);
                }
                mapMessage.put("short_url", ShortUrlUtils.getSinaURL(WebappConfig.get().URL_WWW + "/joymeapp/gameclient/webview/task/taskgetcode?type=2015dw&share=share"));
                return new ModelAndView("/views/jsp/activity/2015dw/dw", mapMessage);
            } else if ("2015zqindex".equals(type)) {      //中秋2015活动首页
                Collections.shuffle(firstDayTotteryLog);
                mapMessage.put("logList", firstDayTotteryLog);
                return new ModelAndView("/views/jsp/activity/2015zq/zqactivity_index", mapMessage);
            } else if ("2015zqpage".equals(type)) {
                mapMessage.put("loginDomain", loginDomain);
                mapMessage.put("uid", uid);
                mapMessage.put("platform", platform);
                mapMessage.put("clientid", clientId);
                mapMessage.put("channelid", channel);
                mapMessage.put("token", tokenString);
                mapMessage.put("appkey", appKey);
                Profile profile = UserCenterServiceSngl.get().getProfileByUid(Long.parseLong(uid));
                mapMessage.put("profileId", profile.getProfileId());
                mapMessage.put("short_url", ShortUrlUtils.getSinaURL(WebappConfig.get().URL_WWW + "/joymeapp/gameclient/webview/task/share?type=zq2015"));
                return new ModelAndView("/views/jsp/activity/2015zq/zqactivity", mapMessage);
            }


            //打开页面次数+1
            LotteryServiceSngl.get().addOrIncrTime("_lottery_openactivety_count_");

            if (!StringUtil.isEmpty(type) && type.equals("share")) {

                return new ModelAndView("/views/jsp/gameclient/webview/lottery/share", mapMessage);
            }
            if (StringUtil.isEmpty(uid) || StringUtil.isEmpty(clientId) || StringUtil.isEmpty(appKey)
                    || StringUtil.isEmpty(platform)
                    || StringUtil.isEmpty(tokenString)) {
                return new ModelAndView("/views/jsp/my/404", mapMessage);
            }
            Token token = UserCenterServiceSngl.get().getToken(tokenString);
            if (token == null) {
                return new ModelAndView("/views/jsp/my/404", mapMessage);
            } else {
                if (!uid.equals(token.getUid() + "")) {
                    return new ModelAndView("/views/jsp/my/404", mapMessage);
                }
            }
            appKey = AppUtil.getAppKey(appKey);
            AuthApp app = OAuthServiceSngl.get().getApp(appKey);
            if (app == null) {
                return new ModelAndView("/views/jsp/my/404", mapMessage);
            }
            Profile profile = UserCenterServiceSngl.get().getProfileByUid(Long.parseLong(uid));
            if (profile == null) {
                return new ModelAndView("/views/jsp/my/404", mapMessage);
            }

            mapMessage.put("short_url", ShortUrlUtils.getSinaURL(WebappConfig.get().URL_WWW + "/joymeapp/gameclient/webview/task/taskgetcode?type=share"));

            String date_short = DateUtil.formatDateToString(new Date(), DateUtil.PATTERN_DATE_SHORT);
            if (date_short.equals("20150512")) {
                Collections.shuffle(firstDayTotteryLog);
                mapMessage.put("logList", firstDayTotteryLog);
            } else {
                mapMessage.put("logList", RandomUtil.getRandomByList(otherTotteryLog, 15));
            }

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e:", e);
            return new ModelAndView("/views/jsp/my/404", mapMessage);
        }
        return new ModelAndView("/views/jsp/gameclient/webview/lottery/activity", mapMessage);
    }

    @RequestMapping(value = "/share")
    public ModelAndView share(HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("JParam", HTTPUtil.getParam(request, "JParam"));
        Map<String, String> JParam = HTTPUtil.getJParam(request.getParameter("JParam"));
        String appKey = StringUtil.isEmpty(HTTPUtil.getParam(request, "appkey")) ? JParam.get("appkey") : HTTPUtil.getParam(request, "appkey");
        String uid = StringUtil.isEmpty(HTTPUtil.getParam(request, "uid")) ? JParam.get("uid") : HTTPUtil.getParam(request, "uid");
        String platform = StringUtil.isEmpty(HTTPUtil.getParam(request, "platform")) ? JParam.get("platform") : HTTPUtil.getParam(request, "platform");
        String clientId = StringUtil.isEmpty(HTTPUtil.getParam(request, "clientid")) ? JParam.get("clientid") : HTTPUtil.getParam(request, "clientid");
        String channel = StringUtil.isEmpty(HTTPUtil.getParam(request, "channelid")) ? JParam.get("channelid") : HTTPUtil.getParam(request, "channelid");
        String loginDomain = StringUtil.isEmpty(HTTPUtil.getParam(request, "logindomain")) ? JParam.get("logindomain") : HTTPUtil.getParam(request, "logindomain");
        String tokenString = StringUtil.isEmpty(HTTPUtil.getParam(request, "token")) ? JParam.get("token") : HTTPUtil.getParam(request, "token");
        try {
            String type = request.getParameter("type");
            if ("zq2015".equals(type)) {
                return new ModelAndView("/views/jsp/activity/2015zq/zqactivity_share", mapMessage);
            }
            if (StringUtil.isEmpty(uid) || StringUtil.isEmpty(clientId) || StringUtil.isEmpty(appKey)
                    || StringUtil.isEmpty(platform)
                    || StringUtil.isEmpty(tokenString)) {
                return new ModelAndView("/views/jsp/my/404", mapMessage);
            }
            Token token = UserCenterServiceSngl.get().getToken(tokenString);
            if (token == null) {
                return new ModelAndView("/views/jsp/my/404", mapMessage);
            } else {
                if (!uid.equals(token.getUid() + "")) {
                    return new ModelAndView("/views/jsp/my/404", mapMessage);
                }
            }
            appKey = AppUtil.getAppKey(appKey);
            AuthApp app = OAuthServiceSngl.get().getApp(appKey);
            if (app == null) {
                return new ModelAndView("/views/jsp/my/404", mapMessage);
            }
            Profile profile = UserCenterServiceSngl.get().getProfileByUid(Long.parseLong(uid));
            if (profile == null) {
                return new ModelAndView("/views/jsp/my/404", mapMessage);
            }

            String lotteryAwardId = request.getParameter("lotteryAwardId");
            if (StringUtil.isEmpty(lotteryAwardId)) {
                return new ModelAndView("/views/jsp/my/404", mapMessage);
            }
            LotteryAward award = LotteryServiceSngl.get().getLotteryAwardById(Long.valueOf(lotteryAwardId));
            if (award == null) {
                return new ModelAndView("/views/jsp/my/404", mapMessage);
            }
            mapMessage.put("award", award);
            mapMessage.put("short_url", ShortUrlUtils.getSinaURL(WebappConfig.get().URL_WWW + "/joymeapp/gameclient/webview/task/taskgetcode?type=share"));
            //中奖页面
            Set<Long> setLong = new HashSet<Long>();
            if (WebappConfig.get().getDomain().contains("joyme.dev")) {
                setLong = new HashSet<Long>();
                setLong.add(894565L);
                setLong.add(894562l);
            } else if (WebappConfig.get().getDomain().contains("joyme.com")) {
                setLong = new HashSet<Long>();
                setLong.add(70790L);
                setLong.add(70746L);
                setLong.add(71025L);
            }
            GameDB gameDB = null;
            if (!CollectionUtil.isEmpty(setLong)) {
                Map<Long, GameDB> map = GameResourceServiceSngl.get().queryGameDBSet(setLong);
                List<Long> longList = new ArrayList<Long>();
                longList.addAll(setLong);
                gameDB = map.get(longList.get(new Random().nextInt(longList.size())));
                mapMessage.put("gamedb", gameDB);
            }

            mapMessage.put("gamedb", gameDB);
            mapMessage.put("profileId", profile.getProfileId());
            //渠道是固定的
            GameDBChannel gameDBChannel = GameResourceServiceSngl.get().getGameDbChannel("def");
            String downLoadUrl = gameDbWebLogic.getDownLink(platform, gameDB, gameDBChannel);
            mapMessage.put("downLoadUrl", downLoadUrl);

            return new ModelAndView("/views/jsp/gameclient/webview/lottery/win", mapMessage);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e:", e);
        }
        return new ModelAndView("/views/jsp/gameclient/webview/lottery/win", mapMessage);
    }

    private List<String> blacklist() {
        List<String> list = new ArrayList<String>();
        list.add("106.113.54.81");
        list.add("106.117.122.60");
        list.add("106.38.238.59");
        list.add("111.122.99.65");
        list.add("111.161.31.142");
        list.add("111.172.193.173");
        list.add("111.193.137.194");
        list.add("111.204.253.10");
        list.add("112.251.198.99");
        list.add("112.64.141.82");
        list.add("113.119.204.22");
        list.add("113.121.81.0");
        list.add("113.240.238.102");
        list.add("113.247.220.3");
        list.add("113.247.220.42");
        list.add("113.73.179.114");
        list.add("113.8.243.108");
        list.add("114.111.166.243");
        list.add("114.219.34.26");
        list.add("115.103.252.21");
        list.add("115.46.113.136");
        list.add("115.46.69.174");
        list.add("116.1.57.242");
        list.add("116.213.171.174");
        list.add("117.136.13.150");
        list.add("117.136.19.101");
        list.add("117.136.36.43");
        list.add("117.136.38.132");
        list.add("117.136.45.189");
        list.add("117.136.66.2");
        list.add("117.158.132.224");
        list.add("117.169.1.166");
        list.add("117.24.34.67");
        list.add("117.39.35.187");
        list.add("118.212.205.238");
        list.add("119.129.209.249");
        list.add("119.130.184.246");
        list.add("119.176.236.112");
        list.add("119.85.35.162");
        list.add("120.32.55.183");
        list.add("121.228.148.62");
        list.add("121.31.193.251");
        list.add("121.33.121.233");
        list.add("121.33.51.189");
        list.add("123.128.32.81");
        list.add("123.138.215.86");
        list.add("123.180.80.75");
        list.add("124.166.231.206");
        list.add("125.105.83.199");
        list.add("125.77.66.79");
        list.add("163.179.53.158");
        list.add("171.43.236.224");
        list.add("175.1.20.21");
        list.add("180.112.218.20");
        list.add("180.118.4.221");
        list.add("180.142.208.91");
        list.add("182.110.112.137");
        list.add("182.89.201.28");
        list.add("182.89.204.10");
        list.add("183.10.163.129");
        list.add("183.232.39.76");
        list.add("183.56.216.88");
        list.add("210.82.53.192");
        list.add("211.157.164.1");
        list.add("211.97.108.32");
        list.add("218.22.69.140");
        list.add("218.72.60.46");
        list.add("220.191.239.154");
        list.add("221.194.176.1");
        list.add("221.218.157.7");
        list.add("222.209.111.23");
        list.add("223.104.10.150");
        list.add("223.104.10.152");
        list.add("223.104.10.217");
        list.add("223.104.10.225");
        list.add("223.104.23.28");
        list.add("223.104.5.207");
        list.add("27.128.43.23");
        list.add("27.151.62.229");
        list.add("36.46.146.213");
        list.add("36.48.99.249");
        list.add("36.97.66.137");
        list.add("39.162.144.193");
        list.add("58.20.85.110");
        list.add("58.48.100.181");
        list.add("58.63.78.118");
        list.add("59.41.246.109");
        list.add("59.41.246.217");
        list.add("59.41.246.96");
        list.add("60.169.24.71");
        list.add("60.184.225.37");
        list.add("61.172.35.138");
        list.add("61.188.187.22");
        return list;
    }

    public static void main(String[] args) {
        Collections.shuffle(firstDayTotteryLog);
        System.out.println(firstDayTotteryLog);

        System.out.println((1 * 1.0 / 100) * 100);
    }

}