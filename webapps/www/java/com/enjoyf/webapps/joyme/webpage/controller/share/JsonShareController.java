package com.enjoyf.webapps.joyme.webpage.controller.share;

import com.enjoyf.platform.serv.sync.SyncContent;
import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.account.TokenInfo;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.sync.*;
import com.enjoyf.platform.service.usercenter.LoginDomain;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.service.usercenter.UserLogin;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.security.DESUtil;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.platform.webapps.common.wordfilter.ContextFilterUtils;
import com.enjoyf.webapps.joyme.dto.share.*;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-6-24
 * Time: 下午4:08
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/json/share")
public class JsonShareController extends AbstractShareBaseController {

    private JsonBinder jsonBinder = JsonBinder.buildNormalBinder();
    private Logger logger = Logger.getLogger(JsonShareController.class);

    @ResponseBody
    @RequestMapping(value = "/getbyid")
    public String sharePage(@RequestParam(value = "sid", required = true) long shareId,
                            HttpServletRequest request, HttpServletResponse response) {
        ResultObjectMsg resultMsg = null;

        //choose shareInfo
        try {
            ShareBaseInfo baseInfo = SyncServiceSngl.get().getShareInfo(new QueryExpress().add(QueryCriterions.eq(ShareBaseInfoField.SHAREID, shareId)));

            if (baseInfo == null) {
                resultMsg = new ResultObjectMsg(ResultObjectMsg.CODE_E);
                resultMsg.setMsg("baseinfo.not.exists");
                return jsonBinder.toJson(resultMsg);
            }

            ShareBaseInfoSimpleDTO dto = new ShareBaseInfoSimpleDTO();
            dto.setShare_id(shareId);
            dto.setDisplay_style(baseInfo.getDisplayStyle());

            resultMsg = new ResultObjectMsg(ResultObjectMsg.CODE_S);
            resultMsg.setResult(dto);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException,e:", e);
        }

        //uno get bind third
        return jsonBinder.toJson(resultMsg);
    }

    //根据uno和shareId分享到第三方
    @ResponseBody
    @RequestMapping(value = "/{third}/sharebyid")
    public String shareById(@RequestParam(value = "sid", required = true) String shareId,
                            @RequestParam(required = true) String uno,
                            @PathVariable(value = "third") String third) {
        if (logger.isDebugEnabled()) {
            logger.debug("shareById,shareId is:" + shareId + ",uno is: " + uno);
        }
        try {
            //根据sid查找分享内容
            if (shareId == null || shareId.length() == 0) {
                ResultObjectMsg msg = new ResultObjectMsg();
                msg.setRs(ResultObjectMsg.CODE_E);
                msg.setMsg("param.is.empty");
                msg.setResult("");//msg.setResult(uno);
                return jsonBinder.toJson(msg);
            }
            ShareInfo shareInfo = SyncServiceSngl.get().choiceShareInfo(Long.parseLong(shareId));
            if (shareInfo == null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("shareInfo is null");
                }
                ResultObjectMsg msg = new ResultObjectMsg();
                msg.setRs(ResultObjectMsg.CODE_E);
                msg.setMsg("sid.err");
                msg.setResult("");//msg.setResult(uno);
                return jsonBinder.toJson(msg);
            }
            return share(uno, shareInfo, third);
        } catch (ServiceException e) {
            GAlerter.lab("system.err", e);
            ResultObjectMsg msg = new ResultObjectMsg();
            msg.setRs(ResultObjectMsg.CODE_E);
            msg.setResult("");//msg.setResult(uno);
            return jsonBinder.toJson(msg);
        }
    }

    //根据内容直接分享到第三方
    @ResponseBody
    @RequestMapping(value = "/{third}/sharebycustom")
    public String shareByInfo(@RequestParam(required = true) String uno,
                              @RequestParam(value = "topic", required = true) String topic,
                              @RequestParam(value = "title", required = false) String title,
                              @RequestParam(value = "body", required = true) String body,
                              @RequestParam(value = "pic", required = true) String pic,
                              @RequestParam(value = "url", required = true) String url,
                              @RequestParam(value = "sharetype", required = false) Integer sharetype,
                              @PathVariable(value = "third") String third) {
        if (logger.isDebugEnabled()) {
            logger.debug("share info only by uno:" + uno);
        }
        //过滤关键词，如果不通过直接返回
        if (ContextFilterUtils.postContainBlackList(topic)
                || ContextFilterUtils.postContainBlackList(title)
                || ContextFilterUtils.postContainBlackList(body)) {
            if (logger.isDebugEnabled()) {
                logger.debug("not pass,topic:" + topic + ", title:" + title + ", body" + body);
            }
            ResultObjectMsg msg = new ResultObjectMsg();
            msg.setRs(ResultObjectMsg.CODE_E);
            msg.setMsg("content.err");
            msg.setResult("");//msg.setResult(uno);
            return jsonBinder.toJson(msg);
        }
        //封装分享内容
        ShareInfo shareInfo = new ShareInfo();
        ShareBaseInfo shareBaseInfo = new ShareBaseInfo();
        shareBaseInfo.setShareSource(url);
        if (null == sharetype) {
            shareBaseInfo.setShareId(-1);
        } else {
            shareBaseInfo.setShareId(sharetype);
        }

        ShareBody shareBody = new ShareBody();
        shareBody.setShareBody(body);
        shareBody.setShareSubject(title);
        shareBody.setPicUrl(pic);
        ShareTopic shareTopic = new ShareTopic();
        shareTopic.setShareTopic(topic);
        shareInfo.setBaseInfo(shareBaseInfo);
        shareInfo.setShareBody(shareBody);
        shareInfo.setShareTopic(shareTopic);
        return share(uno, shareInfo, third);
    }

    @RequestMapping(value = "/getsign")
    public ModelAndView getsign(@RequestParam(value = "uno", required = true) String uno,
                                @RequestParam(value = "appkey", required = true) String appkey) throws Exception {
        Map<String, Object> messageMap = new HashMap<String, Object>();
        try {
            AuthApp app = OAuthServiceSngl.get().getApp(getAppKey(appkey));
            String param = "&" + appkey + "&" + uno + "&" + new Date().getTime();
            String encryptParam = DESUtil.desEncrypt(app.getAppKey(), param);
            messageMap.put("sign", encryptParam);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return new ModelAndView("/views/jsp/test/sharetest", messageMap);
    }

    private static String getAppKey(String appKey) {
        if (StringUtil.isEmpty(appKey)) {
            return "";
        }
        if (appKey.length() < 23) {
            return appKey;
        }
        return appKey.substring(0, appKey.length() - 1);
    }

    //分享公用方法。
    private String share(String profileUno, ShareInfo shareInfo, String third) {
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("share function,uno:" + profileUno + ", shareInfo:" + shareInfo);
            }
            LoginDomain accountDomain = LoginDomain.getByCode(third);
            if (accountDomain == null) {
                ResultObjectMsg msg = new ResultObjectMsg();
                msg.setRs(ResultObjectMsg.CODE_E);
                msg.setMsg("third.err");
                msg.setResult("");// msg.setResult(profileUno)
                return jsonBinder.toJson(msg);
            }
            //设置分享内容
            SyncContent syncContent = new SyncContent();
            syncContent.setSyncTitle(shareInfo.getShareBody().getShareSubject());
            syncContent.setSyncTopic(shareInfo.getShareTopic().getShareTopic());
            syncContent.setSyncText(shareInfo.getShareBody().getShareBody());
            syncContent.setSyncImg(shareInfo.getShareBody().getPicUrl());
            syncContent.setSyncContentUrl(shareInfo.getBaseInfo().getShareSource());
            //根据uno查找账号第三方信息

            TokenInfo tokenInfo = null;
            Set<LoginDomain> loginDomains = new HashSet<LoginDomain>();
            loginDomains.add(accountDomain);
            List<UserLogin> userLogin = UserCenterServiceSngl.get().queryUserLoginUno(profileUno, loginDomains);
            if (!CollectionUtil.isEmpty(userLogin)) {
                tokenInfo = userLogin.get(0).getTokenInfo();
            }

//            AccountThird thirdAccount = AccountServiceSngl.get().getAccountThirdByProfileUno(profileUno, accountDomain);
//            if (logger.isDebugEnabled()) {
//                logger.debug("get AccountThird:" + thirdAccount);
//            }
//            TokenInfo tokenInfo = null;
//            if (thirdAccount != null) {
//                tokenInfo = thirdAccount.getThirdToken();
//            }
            if (tokenInfo == null) {
                ResultObjectMsg msg = new ResultObjectMsg();
                msg.setRs(ResultObjectMsg.CODE_E);
                msg.setMsg("uno.err");
                msg.setResult("");// msg.setResult(profileUno);
                return jsonBinder.toJson(msg);
            }
            //分享到第三方的方法
            SyncServiceSngl.get().syncShareInfo(syncContent, tokenInfo, accountDomain, profileUno, shareInfo.getBaseInfo().getShareId());
        } catch (ServiceException e) {
            GAlerter.lab("system.err", e);
            ResultObjectMsg msg = new ResultObjectMsg();
            msg.setRs(ResultObjectMsg.CODE_E);
            msg.setMsg("system.err");
            msg.setResult("");// msg.setResult(profileUno);
            return jsonBinder.toJson(msg);
        }
        ResultObjectMsg msg = new ResultObjectMsg();
        msg.setRs(ResultObjectMsg.CODE_S);
        msg.setMsg("success");
        msg.setResult("");// msg.setResult(profileUno);
        return jsonBinder.toJson(msg);
    }
}
