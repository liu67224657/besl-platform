package com.enjoyf.webapps.joyme.webpage.controller.activity;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.serv.sync.SyncContent;
import com.enjoyf.platform.service.account.TokenInfo;
import com.enjoyf.platform.service.lottery.LotteryAward;
import com.enjoyf.platform.service.lottery.LotteryServiceException;
import com.enjoyf.platform.service.lottery.LotteryServiceSngl;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.sync.ShareInfo;
import com.enjoyf.platform.service.sync.SyncServiceSngl;
import com.enjoyf.platform.service.usercenter.*;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.wordfilter.ContextFilterUtils;
import com.enjoyf.util.CookieUtil;
import com.enjoyf.util.StringUtil;
import com.enjoyf.webapps.joyme.dto.activity.Shuang12DTO;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterCookieUtil;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Created by zhimingli on 2015/6/18.
 * 动态调用的controller
 */
@Controller
public class DynamicActivityController extends BaseRestSpringController {

    private static final String ROOTPATH_ACTIVITY = "/views/jsp/activity";
    private static final String ZQ_ACTIVITY_NAME = "zqactivity";  //2015中秋活动

    private static final String S12_ACTIVITY_NAME = "s12activity";

    private static final String PAY = "pay";  //个人信息里面的充值页面

    private static final String POST_WORDS = "postwords";
    private static final String WORDS_LIST = "wordslist";
    private static final String LOTTERY_AWARD = "lotteryaward";
    private static final String LOTTERY_SHARE = "share";
    private static final int PAGE_SIZE = 8;

    private static final String thirdCode = "sinaweibo";

    private static HashMap<String, Long> lotteryIdMap = new HashMap<String, Long>();

    private static HashMap<String, Long> shareIdMap = new HashMap<String, Long>();

    private static List<String> firstDayTotteryLog = new ArrayList<String>();

    static {
        //测试专用
        shareIdMap.put("joyme.test", 10640l);
        shareIdMap.put("joyme.alpha", 11100l);
        shareIdMap.put("joyme.beta", 11970l);
        shareIdMap.put("joyme.com", 12101l);

        lotteryIdMap.put("2015-12-10", 10050L);
        lotteryIdMap.put("2015-12-11", 10060L);
        lotteryIdMap.put("2015-12-12", 10051L);
        lotteryIdMap.put("2015-12-13", 10061L);
        lotteryIdMap.put("2015-12-14", 10052L);
        lotteryIdMap.put("2015-12-15", 10062L);
        lotteryIdMap.put("2015-12-16", 10053L);
        lotteryIdMap.put("2015-12-17", 10063L);
        lotteryIdMap.put("2015-12-18", 10054L);
        lotteryIdMap.put("2015-12-19", 10064L);
        lotteryIdMap.put("2015-12-20", 10055L);

        firstDayTotteryLog.add("恭喜李墨客获得限量手办");
        firstDayTotteryLog.add("恭喜刘凯获得着迷精美U盘");
        firstDayTotteryLog.add("恭喜李墨客获得游戏抱枕");
        firstDayTotteryLog.add("恭喜张然获得AppleWatch");
        firstDayTotteryLog.add("恭喜任远获得着迷高档毛毡文件袋");
        firstDayTotteryLog.add("恭喜张子楠获得精美卡通抱枕");
        firstDayTotteryLog.add("恭喜李丽获得着迷精美U盘");
        firstDayTotteryLog.add("恭喜徐萌萌获得炫光游戏鼠标");
        firstDayTotteryLog.add("恭喜许可获得大容量移动电源");
    }

    /**
     * 一层目录
     */
    @RequestMapping(value = "/activity/{path}")
    public ModelAndView aboutOne(@PathVariable(value = "path") String path,HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        //2015 双 12
        if (S12_ACTIVITY_NAME.equals(path)) {
            Pagination pagination = new Pagination(800, 1, 800);
            try {
                PageRows<String> shuang12DTOList = LotteryServiceSngl.get().getShuang12Cache(pagination);
                if (shuang12DTOList != null && !CollectionUtil.isEmpty(shuang12DTOList.getRows())) {
                    List<Shuang12DTO> list = new ArrayList<Shuang12DTO>();
                    for (String value : shuang12DTOList.getRows()) {
                        if (value.indexOf("|") > 0) {
                            String uid = value.split("\\|")[0];
                            String words = value.split("\\|")[1];
                            if (!StringUtil.isEmpty(uid)) {
                                Profile profile = UserCenterServiceSngl.get().getProfileByUid(Long.valueOf(uid));
                                if (profile != null) {
                                    Shuang12DTO shuang12DTO = new Shuang12DTO();
                                    shuang12DTO.setUid(profile.getUid());
                                    shuang12DTO.setNick(profile.getNick());
                                    shuang12DTO.setWords(words);
                                    list.add(shuang12DTO);
                                }
                            }
                        }
                    }
                    mapMessage.put("list", list);
                    mapMessage.put("page", shuang12DTOList.getPage());
                }
            } catch (Exception e) {
                GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
                mapMessage.put("errorMessage", "system.error");
                return new ModelAndView("/views/jsp/common/custompage", mapMessage);
            }
            return new ModelAndView(ROOTPATH_ACTIVITY + "/2015s12/" + path, mapMessage);
        } else if (POST_WORDS.equals(path)) {
            String callback = request.getParameter("callback");

            String appKey = HTTPUtil.getParam(request, "appkey");
            if (StringUtil.isEmpty(appKey)) {
                appKey = CookieUtil.getCookieValue(request, UserCenterCookieUtil.COOKIEKEY_APPKEY);
            }
            String tokenString = HTTPUtil.getParam(request, "token");
            if (StringUtil.isEmpty(tokenString)) {
                tokenString = CookieUtil.getCookieValue(request, UserCenterCookieUtil.COOKIEKEY_TOKEN);
            }
            String uno = HTTPUtil.getParam(request, "uno");
            if (StringUtil.isEmpty(uno)) {
                uno = CookieUtil.getCookieValue(request, UserCenterCookieUtil.COOKIEKEY_UNO);
            }
            long uid = -1l;
            String uidStr = HTTPUtil.getParam(request, "uid");
            if (StringUtil.isEmpty(uidStr)) {
                uidStr = CookieUtil.getCookieValue(request, UserCenterCookieUtil.COOKIEKEY_UID);
            }
            if (!StringUtil.isEmpty(uidStr)) {
                try {
                    uid = Long.parseLong(uidStr);
                } catch (NumberFormatException e) {
                }
            }

            LoginDomain loginDomain = null;
            String loginDomainStr = HTTPUtil.getParam(request, "logindomain");
            if (StringUtil.isEmpty(loginDomainStr)) {
                loginDomainStr = CookieUtil.getCookieValue(request, UserCenterCookieUtil.COOKIEKEY_LOGINDOMAIN);
            }
            if (!StringUtil.isEmpty(loginDomainStr)) {
                loginDomain = LoginDomain.getByCode(loginDomainStr);
            }

            try {
                if (StringUtil.isEmpty(tokenString) || StringUtil.isEmpty(appKey) || (StringUtil.isEmpty(uno) && uid < 0l) || loginDomain == null) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("rs", String.valueOf(ResultCodeConstants.USER_NOT_LOGIN.getCode()));
                    jsonObject.put("msg", ResultCodeConstants.USER_NOT_LOGIN.getMsg());
                    if (StringUtil.isEmpty(callback)) {
                        HTTPUtil.writeJson(response, jsonObject.toString());
                        return null;
                    } else {
                        HTTPUtil.writeJson(response, callback + "([" + jsonObject.toString() + "])");
                        return null;
                    }
                }

                AuthApp app = OAuthServiceSngl.get().getApp(appKey);
                if (app == null || StringUtil.isEmpty(app.getProfileKey())) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("rs", String.valueOf(ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED.getCode()));
                    jsonObject.put("msg", ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED.getMsg());
                    if (StringUtil.isEmpty(callback)) {
                        HTTPUtil.writeJson(response, jsonObject.toString());
                        return null;
                    } else {
                        HTTPUtil.writeJson(response, callback + "([" + jsonObject.toString() + "])");
                        return null;
                    }
                }

                Token token = UserCenterServiceSngl.get().getToken(tokenString);
                //token不存在
                if (token == null) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("rs", String.valueOf(ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED.getCode()));
                    jsonObject.put("msg", ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED.getMsg());
                    if (StringUtil.isEmpty(callback)) {
                        HTTPUtil.writeJson(response, jsonObject.toString());
                        return null;
                    } else {
                        HTTPUtil.writeJson(response, callback + "([" + jsonObject.toString() + "])");
                        return null;
                    }
                }

                //token对应的profilekey和app的profilekey不一致
                if (!token.getProfileKey().equals(app.getProfileKey())) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("rs", String.valueOf(ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED.getCode()));
                    jsonObject.put("msg", ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED.getMsg());
                    if (StringUtil.isEmpty(callback)) {
                        HTTPUtil.writeJson(response, jsonObject.toString());
                        return null;
                    } else {
                        HTTPUtil.writeJson(response, callback + "([" + jsonObject.toString() + "])");
                        return null;
                    }
                }

                //uno和token对应的uno不一致
                if (!StringUtil.isEmpty(uno) && !uno.equals(token.getUno())) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("rs", String.valueOf(ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED.getCode()));
                    jsonObject.put("msg", ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED.getMsg());
                    if (StringUtil.isEmpty(callback)) {
                        HTTPUtil.writeJson(response, jsonObject.toString());
                        return null;
                    } else {
                        HTTPUtil.writeJson(response, callback + "([" + jsonObject.toString() + "])");
                        return null;
                    }
                }

                //uid 和 token对应的uid不一致
                if (uid > 0l && uid != token.getUid()) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("rs", String.valueOf(ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED.getCode()));
                    jsonObject.put("msg", ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED.getMsg());
                    if (StringUtil.isEmpty(callback)) {
                        HTTPUtil.writeJson(response, jsonObject.toString());
                        return null;
                    } else {
                        HTTPUtil.writeJson(response, callback + "([" + jsonObject.toString() + "])");
                        return null;
                    }
                }

                Profile profile = UserCenterServiceSngl.get().getProfileByUid(uid);
                if (profile == null) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("rs", String.valueOf(ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED.getCode()));
                    jsonObject.put("msg", ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED.getMsg());
                    if (StringUtil.isEmpty(callback)) {
                        HTTPUtil.writeJson(response, jsonObject.toString());
                        return null;
                    } else {
                        HTTPUtil.writeJson(response, callback + "([" + jsonObject.toString() + "])");
                        return null;
                    }
                }

                String words = request.getParameter("words");
                if (StringUtil.isEmpty(words)) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("rs", String.valueOf(ResultCodeConstants.COMMENT_PARAM_BODY_NULL.getCode()));
                    jsonObject.put("msg", ResultCodeConstants.COMMENT_PARAM_BODY_NULL.getMsg());
                    if (StringUtil.isEmpty(callback)) {
                        HTTPUtil.writeJson(response, jsonObject.toString());
                        return null;
                    } else {
                        HTTPUtil.writeJson(response, callback + "([" + jsonObject.toString() + "])");
                        return null;
                    }
                }

                Set<String> simpleKeyword = ContextFilterUtils.getSimpleEditorBlackList(words);
                Set<String> postKeyword = ContextFilterUtils.getPostContainBlackList(words);
                if (!CollectionUtil.isEmpty(simpleKeyword) || !CollectionUtil.isEmpty(postKeyword)) {
                    Set<String> keyword = new HashSet<String>();
                    keyword.addAll(simpleKeyword);
                    keyword.addAll(postKeyword);

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("rs", String.valueOf(ResultCodeConstants.COMMENT_REPLY_BODY_TEXT_ILLEGE.getCode()));
                    jsonObject.put("msg", ResultCodeConstants.COMMENT_REPLY_BODY_TEXT_ILLEGE.getMsg());
                    jsonObject.put("result", keyword);
                    if (StringUtil.isEmpty(callback)) {
                        HTTPUtil.writeJson(response, jsonObject.toString());
                        return null;
                    } else {
                        HTTPUtil.writeJson(response, callback + "([" + jsonObject.toString() + "])");
                        return null;
                    }
                }

                Shuang12DTO shuang12DTO = new Shuang12DTO();
                shuang12DTO.setUid(profile.getUid());
                shuang12DTO.setWords(words);
                shuang12DTO.setNick(profile.getNick());
                LotteryServiceSngl.get().putShuang12Cache(profile, words);

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("rs", String.valueOf(ResultCodeConstants.SUCCESS.getCode()));
                jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
                jsonObject.put("result", shuang12DTO);
                if (StringUtil.isEmpty(callback)) {
                    HTTPUtil.writeJson(response, jsonObject.toString());
                    return null;
                } else {
                    HTTPUtil.writeJson(response, callback + "([" + jsonObject.toString() + "])");
                    return null;
                }
            } catch (Exception e) {
                GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("rs", String.valueOf(ResultCodeConstants.SYSTEM_ERROR.getCode()));
                jsonObject.put("msg", ResultCodeConstants.SYSTEM_ERROR.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    try {
                        HTTPUtil.writeJson(response, jsonObject.toString());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    return null;
                } else {
                    try {
                        HTTPUtil.writeJson(response, callback + "([" + jsonObject.toString() + "])");
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    return null;
                }
            }
        } else if (WORDS_LIST.equals(path)) {
            String callback = request.getParameter("callback");
            int cp = 1;
            if (!StringUtil.isEmpty(request.getParameter("pnum"))) {
                cp = Integer.valueOf(request.getParameter("pnum"));
            }
            Pagination pagination = new Pagination(PAGE_SIZE * cp, cp, PAGE_SIZE);
            try {
                PageRows<String> shuang12DTOList = LotteryServiceSngl.get().getShuang12Cache(pagination);
                if (shuang12DTOList != null && !CollectionUtil.isEmpty(shuang12DTOList.getRows())) {
                    List<Shuang12DTO> list = new ArrayList<Shuang12DTO>();
                    for (String value : shuang12DTOList.getRows()) {
                        if (value.indexOf("|") > 0) {
                            String uid = value.split("|")[0];
                            String words = value.split("|")[1];
                            if (!StringUtil.isEmpty(uid)) {
                                Profile profile = UserCenterServiceSngl.get().getProfileByUid(Long.valueOf(uid));
                                if (profile != null) {
                                    Shuang12DTO shuang12DTO = new Shuang12DTO();
                                    shuang12DTO.setUid(profile.getUid());
                                    shuang12DTO.setNick(profile.getNick());
                                    shuang12DTO.setWords(words);
                                    list.add(shuang12DTO);
                                }
                            }
                        }
                    }
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("rs", String.valueOf(ResultCodeConstants.SUCCESS.getCode()));
                    jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
                    jsonObject.put("result", list);
                    if (StringUtil.isEmpty(callback)) {
                        HTTPUtil.writeJson(response, jsonObject.toString());
                        return null;
                    } else {
                        HTTPUtil.writeJson(response, callback + "([" + jsonObject.toString() + "])");
                        return null;
                    }
                }
            } catch (Exception e) {
                GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("rs", String.valueOf(ResultCodeConstants.SYSTEM_ERROR.getCode()));
                jsonObject.put("msg", ResultCodeConstants.SYSTEM_ERROR.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    try {
                        HTTPUtil.writeJson(response, jsonObject.toString());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    return null;
                } else {
                    try {
                        HTTPUtil.writeJson(response, callback + "([" + jsonObject.toString() + "])");
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    return null;
                }
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("rs", String.valueOf(ResultCodeConstants.SUCCESS.getCode()));
            jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
            if (StringUtil.isEmpty(callback)) {
                try {
                    HTTPUtil.writeJson(response, jsonObject.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            } else {
                try {
                    HTTPUtil.writeJson(response, callback + "([" + jsonObject.toString() + "])");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        } else if (LOTTERY_AWARD.equals(path)) {
            String callback = request.getParameter("callback");

            String appKey = HTTPUtil.getParam(request, "appkey");
            if (StringUtil.isEmpty(appKey)) {
                appKey = CookieUtil.getCookieValue(request, UserCenterCookieUtil.COOKIEKEY_APPKEY);
            }
            String tokenString = HTTPUtil.getParam(request, "token");
            if (StringUtil.isEmpty(tokenString)) {
                tokenString = CookieUtil.getCookieValue(request, UserCenterCookieUtil.COOKIEKEY_TOKEN);
            }
            String uno = HTTPUtil.getParam(request, "uno");
            if (StringUtil.isEmpty(uno)) {
                uno = CookieUtil.getCookieValue(request, UserCenterCookieUtil.COOKIEKEY_UNO);
            }
            long uid = -1l;
            String uidStr = HTTPUtil.getParam(request, "uid");
            if (StringUtil.isEmpty(uidStr)) {
                uidStr = CookieUtil.getCookieValue(request, UserCenterCookieUtil.COOKIEKEY_UID);
            }
            if (!StringUtil.isEmpty(uidStr)) {
                try {
                    uid = Long.parseLong(uidStr);
                } catch (NumberFormatException e) {
                }
            }

            LoginDomain loginDomain = null;
            String loginDomainStr = HTTPUtil.getParam(request, "logindomain");
            if (StringUtil.isEmpty(loginDomainStr)) {
                loginDomainStr = CookieUtil.getCookieValue(request, UserCenterCookieUtil.COOKIEKEY_LOGINDOMAIN);
            }
            if (!StringUtil.isEmpty(loginDomainStr)) {
                loginDomain = LoginDomain.getByCode(loginDomainStr);
            }
            try {
                if (StringUtil.isEmpty(tokenString) || StringUtil.isEmpty(appKey) || (StringUtil.isEmpty(uno) && uid < 0l) || loginDomain == null) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("rs", String.valueOf(ResultCodeConstants.USER_NOT_LOGIN.getCode()));
                    jsonObject.put("msg", ResultCodeConstants.USER_NOT_LOGIN.getMsg());
                    if (StringUtil.isEmpty(callback)) {
                        HTTPUtil.writeJson(response, jsonObject.toString());
                        return null;
                    } else {
                        HTTPUtil.writeJson(response, callback + "([" + jsonObject.toString() + "])");
                        return null;
                    }
                }

                if (!loginDomain.equals(LoginDomain.SINAWEIBO)) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("rs", String.valueOf(ResultCodeConstants.USERCENTER_LOGINKEY_ILLEGAL.getCode()));
                    jsonObject.put("msg", ResultCodeConstants.USERCENTER_LOGINKEY_ILLEGAL.getMsg());
                    if (StringUtil.isEmpty(callback)) {
                        HTTPUtil.writeJson(response, jsonObject.toString());
                        return null;
                    } else {
                        HTTPUtil.writeJson(response, callback + "([" + jsonObject.toString() + "])");
                        return null;
                    }
                }

                AuthApp app = OAuthServiceSngl.get().getApp(appKey);
                if (app == null || StringUtil.isEmpty(app.getProfileKey())) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("rs", String.valueOf(ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED.getCode()));
                    jsonObject.put("msg", ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED.getMsg());
                    if (StringUtil.isEmpty(callback)) {
                        HTTPUtil.writeJson(response, jsonObject.toString());
                        return null;
                    } else {
                        HTTPUtil.writeJson(response, callback + "([" + jsonObject.toString() + "])");
                        return null;
                    }
                }

                Token token = UserCenterServiceSngl.get().getToken(tokenString);
                //token不存在
                if (token == null) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("rs", String.valueOf(ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED.getCode()));
                    jsonObject.put("msg", ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED.getMsg());
                    if (StringUtil.isEmpty(callback)) {
                        HTTPUtil.writeJson(response, jsonObject.toString());
                        return null;
                    } else {
                        HTTPUtil.writeJson(response, callback + "([" + jsonObject.toString() + "])");
                        return null;
                    }
                }

                //token对应的profilekey和app的profilekey不一致
                if (!token.getProfileKey().equals(app.getProfileKey())) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("rs", String.valueOf(ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED.getCode()));
                    jsonObject.put("msg", ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED.getMsg());
                    if (StringUtil.isEmpty(callback)) {
                        HTTPUtil.writeJson(response, jsonObject.toString());
                        return null;
                    } else {
                        HTTPUtil.writeJson(response, callback + "([" + jsonObject.toString() + "])");
                        return null;
                    }
                }

                //uno和token对应的uno不一致
                if (!StringUtil.isEmpty(uno) && !uno.equals(token.getUno())) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("rs", String.valueOf(ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED.getCode()));
                    jsonObject.put("msg", ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED.getMsg());
                    if (StringUtil.isEmpty(callback)) {
                        HTTPUtil.writeJson(response, jsonObject.toString());
                        return null;
                    } else {
                        HTTPUtil.writeJson(response, callback + "([" + jsonObject.toString() + "])");
                        return null;
                    }
                }

                //uid 和 token对应的uid不一致
                if (uid > 0l && uid != token.getUid()) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("rs", String.valueOf(ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED.getCode()));
                    jsonObject.put("msg", ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED.getMsg());
                    if (StringUtil.isEmpty(callback)) {
                        HTTPUtil.writeJson(response, jsonObject.toString());
                        return null;
                    } else {
                        HTTPUtil.writeJson(response, callback + "([" + jsonObject.toString() + "])");
                        return null;
                    }
                }

                Profile profile = UserCenterServiceSngl.get().getProfileByUid(uid);
                if (profile == null) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("rs", String.valueOf(ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED.getCode()));
                    jsonObject.put("msg", ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED.getMsg());
                    if (StringUtil.isEmpty(callback)) {
                        HTTPUtil.writeJson(response, jsonObject.toString());
                        return null;
                    } else {
                        HTTPUtil.writeJson(response, callback + "([" + jsonObject.toString() + "])");
                        return null;
                    }
                }

                Long lotteryId = 10050L;//这是dev环境的
                if (WebappConfig.get().getDomain().contains("joyme.com")) {
                    lotteryId = lotteryIdMap.get(DateUtil.formatDateToString(new Date(), DateUtil.PATTERN_DATE));
                }

                LotteryAward lotteryAward = LotteryServiceSngl.get().userLottery(lotteryId, "", getIp(request), profile, "");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
                jsonObject.put("rs", String.valueOf(ResultCodeConstants.SUCCESS.getCode()));
                if (lotteryAward != null) {
                    JSONObject lotteryObj = new JSONObject();
                    lotteryObj.put("lotteryAwardLevel", lotteryAward.getLotteryAwardLevel());
                    lotteryObj.put("lotteryAwardName", lotteryAward.getLotteryAwardName());
                    lotteryObj.put("lotteryAwardDesc", lotteryAward.getLotteryAwardDesc());
                    lotteryObj.put("lotteryAwardId", lotteryAward.getLotteryAwardId());
                    lotteryObj.put("lotteryAwardPic", lotteryAward.getLotteryAwardPic());
                    lotteryObj.put("lotteryTimes", lotteryAward.getCreateIp());
                    jsonObject.put("result", lotteryObj);
                }
                if (StringUtil.isEmpty(callback)) {
                    HTTPUtil.writeJson(response, jsonObject.toString());
                    return null;
                } else {
                    HTTPUtil.writeJson(response, callback + "([" + jsonObject.toString() + "])");
                    return null;
                }
            } catch (ServiceException e) {
                if (e.equals(LotteryServiceException.USER_HAD_LOTTERY_AWARD_TODAY)) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("msg", ResultCodeConstants.AWARD_USER_HAS_AWARD.getMsg());
                    jsonObject.put("rs", String.valueOf(ResultCodeConstants.AWARD_USER_HAS_AWARD.getCode()));
                    if (StringUtil.isEmpty(callback)) {
                        try {
                            HTTPUtil.writeJson(response, jsonObject.toString());
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        return null;
                    } else {
                        try {
                            HTTPUtil.writeJson(response, callback + "([" + jsonObject.toString() + "])");
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        return null;
                    }
                } else if (e.equals(LotteryServiceException.USER_HAS_NO_TIME)) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("msg", ResultCodeConstants.AWARD_NOT_ENOUGH.getMsg());
                    jsonObject.put("rs", String.valueOf(ResultCodeConstants.AWARD_NOT_ENOUGH.getCode()));
                    if (StringUtil.isEmpty(callback)) {
                        try {
                            HTTPUtil.writeJson(response, jsonObject.toString());
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        return null;
                    } else {
                        try {
                            HTTPUtil.writeJson(response, callback + "([" + jsonObject.toString() + "])");
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        return null;
                    }
                } else {
                    GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("msg", ResultCodeConstants.SYSTEM_ERROR.getMsg());
                    jsonObject.put("rs", String.valueOf(ResultCodeConstants.SYSTEM_ERROR.getCode()));
                    if (StringUtil.isEmpty(callback)) {
                        try {
                            HTTPUtil.writeJson(response, jsonObject.toString());
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        return null;
                    } else {
                        try {
                            HTTPUtil.writeJson(response, callback + "([" + jsonObject.toString() + "])");
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        return null;
                    }
                }
            } catch (Exception e) {
                GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("msg", ResultCodeConstants.SYSTEM_ERROR.getMsg());
                jsonObject.put("rs", String.valueOf(ResultCodeConstants.SYSTEM_ERROR.getCode()));
                if (StringUtil.isEmpty(callback)) {
                    try {
                        HTTPUtil.writeJson(response, jsonObject.toString());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    return null;
                } else {
                    try {
                        HTTPUtil.writeJson(response, callback + "([" + jsonObject.toString() + "])");
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    return null;
                }
            }
        } else if (LOTTERY_SHARE.equals(path)) {
            String callback = request.getParameter("callback");

            String appKey = HTTPUtil.getParam(request, "appkey");
            if (StringUtil.isEmpty(appKey)) {
                appKey = CookieUtil.getCookieValue(request, UserCenterCookieUtil.COOKIEKEY_APPKEY);
            }
            String tokenString = HTTPUtil.getParam(request, "token");
            if (StringUtil.isEmpty(tokenString)) {
                tokenString = CookieUtil.getCookieValue(request, UserCenterCookieUtil.COOKIEKEY_TOKEN);
            }
            String uno = HTTPUtil.getParam(request, "uno");
            if (StringUtil.isEmpty(uno)) {
                uno = CookieUtil.getCookieValue(request, UserCenterCookieUtil.COOKIEKEY_UNO);
            }
            long uid = -1l;
            String uidStr = HTTPUtil.getParam(request, "uid");
            if (StringUtil.isEmpty(uidStr)) {
                uidStr = CookieUtil.getCookieValue(request, UserCenterCookieUtil.COOKIEKEY_UID);
            }
            if (!StringUtil.isEmpty(uidStr)) {
                try {
                    uid = Long.parseLong(uidStr);
                } catch (NumberFormatException e) {
                }
            }

            LoginDomain loginDomain = null;
            String loginDomainStr = HTTPUtil.getParam(request, "logindomain");
            if (StringUtil.isEmpty(loginDomainStr)) {
                loginDomainStr = CookieUtil.getCookieValue(request, UserCenterCookieUtil.COOKIEKEY_LOGINDOMAIN);
            }
            if (!StringUtil.isEmpty(loginDomainStr)) {
                loginDomain = LoginDomain.getByCode(loginDomainStr);
            }

            if (StringUtil.isEmpty(tokenString) || StringUtil.isEmpty(appKey) || (StringUtil.isEmpty(uno) && uid < 0l) || loginDomain == null) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("rs", String.valueOf(ResultCodeConstants.USER_NOT_LOGIN.getCode()));
                jsonObject.put("msg", ResultCodeConstants.USER_NOT_LOGIN.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    try {
                        HTTPUtil.writeJson(response, jsonObject.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                } else {
                    try {
                        HTTPUtil.writeJson(response, callback + "([" + jsonObject.toString() + "])");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }

            try {
                if (!loginDomain.equals(LoginDomain.SINAWEIBO)) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("rs", String.valueOf(ResultCodeConstants.USERCENTER_LOGINKEY_ILLEGAL.getCode()));
                    jsonObject.put("msg", ResultCodeConstants.USERCENTER_LOGINKEY_ILLEGAL.getMsg());
                    if (StringUtil.isEmpty(callback)) {
                        HTTPUtil.writeJson(response, jsonObject.toString());
                        return null;
                    } else {
                        HTTPUtil.writeJson(response, callback + "([" + jsonObject.toString() + "])");
                        return null;
                    }
                }

                AuthApp app = OAuthServiceSngl.get().getApp(appKey);
                if (app == null || StringUtil.isEmpty(app.getProfileKey())) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("rs", String.valueOf(ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED.getCode()));
                    jsonObject.put("msg", ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED.getMsg());
                    if (StringUtil.isEmpty(callback)) {
                        HTTPUtil.writeJson(response, jsonObject.toString());
                        return null;
                    } else {
                        HTTPUtil.writeJson(response, callback + "([" + jsonObject.toString() + "])");
                        return null;
                    }
                }

                Token token = UserCenterServiceSngl.get().getToken(tokenString);
                //token不存在
                if (token == null) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("rs", String.valueOf(ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED.getCode()));
                    jsonObject.put("msg", ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED.getMsg());
                    if (StringUtil.isEmpty(callback)) {
                        HTTPUtil.writeJson(response, jsonObject.toString());
                        return null;
                    } else {
                        HTTPUtil.writeJson(response, callback + "([" + jsonObject.toString() + "])");
                        return null;
                    }
                }

                //token对应的profilekey和app的profilekey不一致
                if (!token.getProfileKey().equals(app.getProfileKey())) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("rs", String.valueOf(ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED.getCode()));
                    jsonObject.put("msg", ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED.getMsg());
                    if (StringUtil.isEmpty(callback)) {
                        HTTPUtil.writeJson(response, jsonObject.toString());
                        return null;
                    } else {
                        HTTPUtil.writeJson(response, callback + "([" + jsonObject.toString() + "])");
                        return null;
                    }
                }

                //uno和token对应的uno不一致
                if (!StringUtil.isEmpty(uno) && !uno.equals(token.getUno())) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("rs", String.valueOf(ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED.getCode()));
                    jsonObject.put("msg", ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED.getMsg());
                    if (StringUtil.isEmpty(callback)) {
                        HTTPUtil.writeJson(response, jsonObject.toString());
                        return null;
                    } else {
                        HTTPUtil.writeJson(response, callback + "([" + jsonObject.toString() + "])");
                        return null;
                    }
                }

                //uid 和 token对应的uid不一致
                if (uid > 0l && uid != token.getUid()) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("rs", String.valueOf(ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED.getCode()));
                    jsonObject.put("msg", ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED.getMsg());
                    if (StringUtil.isEmpty(callback)) {
                        HTTPUtil.writeJson(response, jsonObject.toString());
                        return null;
                    } else {
                        HTTPUtil.writeJson(response, callback + "([" + jsonObject.toString() + "])");
                        return null;
                    }
                }

                Profile profile = UserCenterServiceSngl.get().getProfileByUid(uid);
                if (profile == null) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("rs", String.valueOf(ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED.getCode()));
                    jsonObject.put("msg", ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED.getMsg());
                    if (StringUtil.isEmpty(callback)) {
                        HTTPUtil.writeJson(response, jsonObject.toString());
                        return null;
                    } else {
                        HTTPUtil.writeJson(response, callback + "([" + jsonObject.toString() + "])");
                        return null;
                    }
                }

                TokenInfo tokenInfo = null;
                if (profile != null) {
                    Set<LoginDomain> loginDomains = new HashSet<LoginDomain>();
                    loginDomains.add(loginDomain);
                    List<UserLogin> userLogin = UserCenterServiceSngl.get().queryUserLoginUno(profile.getUno(), loginDomains);
                    if (!CollectionUtil.isEmpty(userLogin)) {
                        tokenInfo = userLogin.get(0).getTokenInfo();
                    }
                }
                if (tokenInfo == null) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("rs", String.valueOf(ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED.getCode()));
                    jsonObject.put("msg", ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED.getMsg());
                    if (StringUtil.isEmpty(callback)) {
                        HTTPUtil.writeJson(response, jsonObject.toString());
                        return null;
                    } else {
                        HTTPUtil.writeJson(response, callback + "([" + jsonObject.toString() + "])");
                        return null;
                    }
                }

                long shareId = shareIdMap.get(WebappConfig.get().getDomain());

                ShareInfo shareInfo = SyncServiceSngl.get().choiceShareInfo(shareId);

                SyncContent syncContent = new SyncContent();
                syncContent.setSyncTopic(shareInfo.getShareTopic().getShareTopic());
                syncContent.setSyncText(shareInfo.getShareBody().getShareBody());
                syncContent.setSyncImg(shareInfo.getShareBody().getPicUrl());
                syncContent.setSyncContentUrl(shareInfo.getBaseInfo().getShareSource());
                syncContent.setSyncTitle(shareInfo.getShareBody().getShareSubject());

                SyncServiceSngl.get().syncShareInfo(syncContent, tokenInfo, loginDomain, profile.getUno(), shareId);

                LotteryServiceSngl.get().incrShuang12ShareTime(profile);

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
                jsonObject.put("rs", String.valueOf(ResultCodeConstants.SUCCESS.getCode()));
                if (StringUtil.isEmpty(callback)) {
                    HTTPUtil.writeJson(response, jsonObject.toString());
                    return null;
                } else {
                    HTTPUtil.writeJson(response, callback + "([" + jsonObject.toString() + "])");
                    return null;
                }
            } catch (ServiceException e) {
                GAlerter.lab(this.getClass().getName() + " share occure ServiceException.e: ", e);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("msg", ResultCodeConstants.SYSTEM_ERROR.getMsg());
                jsonObject.put("rs", String.valueOf(ResultCodeConstants.SYSTEM_ERROR.getCode()));
                if (StringUtil.isEmpty(callback)) {
                    try {
                        HTTPUtil.writeJson(response, jsonObject.toString());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    return null;
                } else {
                    try {
                        HTTPUtil.writeJson(response, callback + "([" + jsonObject.toString() + "])");
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    return null;
                }
            } catch (Exception e) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("msg", ResultCodeConstants.SYSTEM_ERROR.getMsg());
                jsonObject.put("rs", String.valueOf(ResultCodeConstants.SYSTEM_ERROR.getCode()));
                if (StringUtil.isEmpty(callback)) {
                    try {
                        HTTPUtil.writeJson(response, jsonObject.toString());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    return null;
                } else {
                    try {
                        HTTPUtil.writeJson(response, callback + "([" + jsonObject.toString() + "])");
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    return null;
                }
            }
        } else if (ZQ_ACTIVITY_NAME.equals(path)) {
            Collections.shuffle(firstDayTotteryLog);
            mapMessage.put("logList", firstDayTotteryLog);
            return new ModelAndView(ROOTPATH_ACTIVITY + "/2015zq/" + path, mapMessage);
        } else if (PAY.equals(path)) {
            mapMessage.put("nav", "pay");
            return new ModelAndView(ROOTPATH_ACTIVITY + "/" + path, mapMessage);
        } else {
            return new ModelAndView("/views/jsp/my/404", mapMessage);
        }
//        return new ModelAndView(ROOTPATH_ACTIVITY + "/" + path, mapMessage);
    }

    /**
     * 二层目录
     */
    @RequestMapping(value = "/activity/{path}/{path2}")
    public ModelAndView aboutTwo(@PathVariable(value = "path") String sPath, @PathVariable(value = "path2") String sPath2) {
        Map map = new HashMap();

        return new ModelAndView(ROOTPATH_ACTIVITY + "/" + sPath + "/" + sPath2, map);
    }
}
