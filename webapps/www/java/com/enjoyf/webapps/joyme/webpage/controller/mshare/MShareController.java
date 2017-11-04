package com.enjoyf.webapps.joyme.webpage.controller.mshare;

import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.TemplateHotdeployConfig;
import com.enjoyf.platform.serv.sync.SyncContent;
import com.enjoyf.platform.service.account.AccountDomain;
import com.enjoyf.platform.service.account.TokenInfo;
import com.enjoyf.platform.service.point.PointActionHistory;
import com.enjoyf.platform.service.point.PointActionType;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.sync.ShareBaseInfo;
import com.enjoyf.platform.service.sync.ShareInfo;
import com.enjoyf.platform.service.sync.SyncServiceSngl;
import com.enjoyf.platform.service.usercenter.LoginDomain;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.service.usercenter.UserLogin;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.NamedTemplate;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.webapps.joyme.weblogic.point.PointResultMsg;
import com.enjoyf.webapps.joyme.weblogic.point.PointWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterSession;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserSession;
import com.enjoyf.webapps.joyme.webpage.controller.share.AbstractShareBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 13-7-16
 * Time: 下午7:36
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/mshare")
public class MShareController extends AbstractShareBaseController {

    @Resource(name = "pointWebLogic")
    private PointWebLogic pointWebLogic;

    private TemplateHotdeployConfig templateHotdeployConfig = HotdeployConfigFactory.get().getConfig(TemplateHotdeployConfig.class);

    @RequestMapping(value = "/content/{thirdcode}/bind")
    public ModelAndView shareContentBind(@RequestParam(value = "sid", required = true) long shareId,
                                         @PathVariable(value = "thirdcode") String thirdCode,
                                         HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> messageMap = new HashMap<String, Object>();
        AccountDomain accountDomain = AccountDomain.getByCode(thirdCode);
        if (accountDomain == null) {
            return new ModelAndView("/views/jsp/common/custompage", putErrorMessage("sync.auth.error.notsupport"));
        }

        String refer = request.getHeader("refer");

        UserSession userSession = getUserBySession(request);
        if (userSession == null) {
            return new ModelAndView("redirect:/profile/sync/" + thirdCode + "/bind?rurl=/mshare/content/" + thirdCode + "/page?sid=" + shareId + "&icn=true");
        }

        if (!userSession.getSyncDomainSet().contains(accountDomain)) {
            return new ModelAndView("redirect:/profile/sync/" + thirdCode + "/bind?rurl=/mshare/content/" + thirdCode + "/page?sid=" + shareId + "&icn=true");
        }

        //uno get bind third
        return new ModelAndView("redirect:/mshare/content/" + thirdCode + "/page?sid=" + shareId, messageMap);
    }

    @RequestMapping(value = "/content/{thirdcode}/page")
    public ModelAndView shareContentPageByThird(@RequestParam(value = "sid", required = true) long shareId,
                                                @PathVariable(value = "thirdcode") String thirdCode,
                                                HttpServletRequest request, HttpServletResponse response) {
        AccountDomain accountDomain = AccountDomain.getByCode(thirdCode);
        if (accountDomain == null) {
            return new ModelAndView("/views/jsp/common/custompage", putErrorMessage("sync.auth.error.notsupport"));
        }


        Map<String, Object> messageMap = new HashMap<String, Object>();
        //choose shareInfo
        try {

            ShareInfo shareInfo = SyncServiceSngl.get().choiceShareInfo(shareId);
            if (shareInfo == null) {
                return new ModelAndView("/views/jsp/common/custompage", putErrorMessage("share.base.info.not.exists"));
            }
            String reurl = "/mshare/content/" + thirdCode + "/page?sid=" + shareId;
            messageMap.put("bindDomain", thirdCode);
            messageMap.put("shareInfo", shareInfo);
            messageMap.put("reurl", reurl);

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException,e:", e);
        }

        //uno get bind third
        return new ModelAndView("views/jsp/mobilepage/share-content", messageMap);
    }


    @RequestMapping(value = "/content/{thirdCode}")
    public ModelAndView shareContent(@PathVariable(value = "thirdCode") String thirdCode,
                                     @RequestParam(value = "shareid", required = false) Long shareId,
                                     @RequestParam(value = "topic", required = true) String topic,
                                     @RequestParam(value = "title", required = false) String title,
                                     @RequestParam(value = "body", required = true) String body,
                                     @RequestParam(value = "pic", required = true) String pic,
                                     @RequestParam(value = "url", required = true) String url,
                                     HttpServletRequest request, HttpServletResponse response) {
        //get session
        UserCenterSession userSession = getUserCenterSeesion(request);
        ShareBaseInfo shareBaseInfo = null;
        try {
            //form param
            if (shareId != null) {
                shareBaseInfo = SyncServiceSngl.get().getShareInfoById(shareId);
            }

            //get thirdcode
            LoginDomain userLoginDomain = LoginDomain.getByCode(thirdCode);
            if (userLoginDomain == null) {
                return new ModelAndView("/views/jsp/common/custompage", putErrorMessage("sync.auth.error.notsupport"));
            }

            //get accesstoken by uno
            TokenInfo tokenInfo = null;
            if (userSession != null) {
                Set<LoginDomain> userLoginSet=new HashSet<LoginDomain>();
                userLoginSet.add(userLoginDomain);
                List<UserLogin> userLogin = UserCenterServiceSngl.get().queryUserLoginUno(userSession.getUno(), userLoginSet);
                if (!CollectionUtil.isEmpty(userLogin)) {
                    tokenInfo = userLogin.get(0).getTokenInfo();
                }
            }
            if (tokenInfo == null) {
                return new ModelAndView("/views/jsp/common/custompage", putErrorMessage("sync.auth.error"));
            }

            SyncContent syncContent = new SyncContent();
            syncContent.setSyncTopic(topic);
            syncContent.setSyncText(body);
            syncContent.setSyncImg(pic);
            syncContent.setSyncContentUrl(url);
            syncContent.setSyncTitle(title);

            SyncServiceSngl.get().syncShareInfo(syncContent, tokenInfo, userLoginDomain, userSession.getUno(), shareId);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " share occure ServiceException.e: ", e);
            return new ModelAndView("/views/jsp/common/custompage", putErrorMessage("system.error"));
        }

        Map<String, Object> mapMessage = new HashMap<String, Object>();

        url = (url.startsWith("http://") || url.startsWith("https://")) ? url : "http://" + url;

        mapMessage.put("returnUrl", url);
        String returnUrl = "/views/jsp/mobilepage/share/success";
        PointResultMsg pointResultMsg = PointResultMsg.SUCCESS;
        if (userSession != null && shareBaseInfo != null) {
            Date now = new Date();

            if (shareBaseInfo.getShareRewardType().hasPoint()) {
                PointActionHistory actionHistory = new PointActionHistory();
                actionHistory.setActionDate(now);

                Map<String, String> paramMap = new HashMap<String, String>();
                String descriptionTemplate = templateHotdeployConfig.getExchangePointActionHistoryTemplate();

                paramMap.put("goodsname", shareBaseInfo.getShareKey());
                String descriptionBody = NamedTemplate.parse(descriptionTemplate).format(paramMap);

                actionHistory.setActionDescription(descriptionBody);
                actionHistory.setActionType(PointActionType.SHARE);
                actionHistory.setCreateDate(now);
                actionHistory.setDestId(String.valueOf(shareBaseInfo.getShareId()));
                actionHistory.setPointValue(shareBaseInfo.getShareRewardPoint());
                actionHistory.setUserNo(userSession.getUno());

                try {
                    pointResultMsg = pointWebLogic.awardPoint(actionHistory);
                } catch (ServiceException e) {
                    GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e: ", e);
                }


                //加积分不加积分
                if (pointResultMsg.equals(PointResultMsg.SUCCESS)) {
                    mapMessage.put("awardPoint", actionHistory.getPointValue());
                    //更新session
                    userSession.setPointAmount(userSession.getPointAmount() + actionHistory.getPointValue());
                    returnUrl = "/views/jsp/mobilepage/share/success-point";
                }
            }

            if (shareBaseInfo.getShareRewardType().hasLottery()) {
                //todo
            }
        }

        return new ModelAndView(returnUrl, mapMessage);

    }


}
