package com.enjoyf.webapps.joyme.webpage.controller.share;

import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.TemplateHotdeployConfig;
import com.enjoyf.platform.serv.sync.SyncContent;
import com.enjoyf.platform.service.account.TokenInfo;
import com.enjoyf.platform.service.point.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.sync.ShareBaseInfo;
import com.enjoyf.platform.service.sync.ShareInfo;
import com.enjoyf.platform.service.sync.SyncServiceSngl;
import com.enjoyf.platform.service.usercenter.LoginDomain;
import com.enjoyf.platform.service.usercenter.ProfileFlag;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.service.usercenter.UserLogin;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.NamedTemplate;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.webapps.joyme.weblogic.point.PointResultMsg;
import com.enjoyf.webapps.joyme.weblogic.point.PointWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-6-21
 * Time: 下午2:04
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/share")
public class ShareController extends AbstractShareBaseController {

    @Resource(name = "pointWebLogic")
    private PointWebLogic pointWebLogic;

    private TemplateHotdeployConfig templateHotdeployConfig = HotdeployConfigFactory.get().getConfig(TemplateHotdeployConfig.class);

    @RequestMapping(value = "/content/{thirdcode}/bind")
    public ModelAndView shareContentBind(@RequestParam(value = "sid", required = true) long shareId,
                                         @PathVariable(value = "thirdcode") String thirdCode,
                                         HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> messageMap = new HashMap<String, Object>();
        LoginDomain loginDomain = LoginDomain.getByCode(thirdCode);
        if (loginDomain == null) {
            return new ModelAndView("/views/jsp/common/custompage", putErrorMessage("sync.auth.error.notsupport"));
        }

        UserCenterSession userSession = getUserCenterSeesion(request);
        if (userSession == null) {
            return new ModelAndView("redirect:/profile/sync/" + thirdCode + "/bind?rurl=/share/content/" + thirdCode + "/page?sid=" + shareId + "&icn=true");
        }
        try {
            int flag = ProfileFlag.getFlagByLoginDomain(loginDomain);
            if (!userSession.getFlag().hasFlag(flag)) {
                return new ModelAndView("redirect:/profile/sync/" + thirdCode + "/bind?rurl=/share/content/" + thirdCode + "/page?sid=" + shareId + "&icn=true");
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return new ModelAndView("/views/jsp/common/custompage", putErrorMessage(ResultCodeConstants.USERCENTER_WRONG_DOMAIN.getMsg()));
        }

        //uno get bind third
        return new ModelAndView("redirect:/share/content/" + thirdCode + "/page?sid=" + shareId, messageMap);
    }

    @RequestMapping(value = "/content/{thirdcode}/page")
    public ModelAndView shareContentPageByThird(@RequestParam(value = "sid", required = true) long shareId,
                                                @PathVariable(value = "thirdcode") String thirdCode,
                                                HttpServletRequest request, HttpServletResponse response) {
        LoginDomain loginDomain = LoginDomain.getByCode(thirdCode);
        if (loginDomain == null) {
            return new ModelAndView("/views/jsp/common/custompage", putErrorMessage("sync.auth.error.notsupport"));
        }


        UserCenterSession userSession = getUserCenterSeesion(request);
        if (userSession == null) {
            return new ModelAndView("redirect:/profile/sync/" + thirdCode + "/bind?rurl=/share/contentpage?sid=" + shareId );
        }

        try {
            int flag = ProfileFlag.getFlagByLoginDomain(loginDomain);
            if (!userSession.getFlag().hasFlag(flag)) {
                return new ModelAndView("redirect:/profile/sync/" + thirdCode + "/bind?rurl=/share/content/" + thirdCode + "/page?sid=" + shareId + "&icn=true");
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return new ModelAndView("/views/jsp/common/custompage", putErrorMessage(ResultCodeConstants.USERCENTER_WRONG_DOMAIN.getMsg()));
        }


        Map<String, Object> messageMap = new HashMap<String, Object>();
        //choose shareInfo
        try {

            ShareInfo shareInfo = SyncServiceSngl.get().choiceShareInfo(shareId);
            if (shareInfo == null) {
                return new ModelAndView("/views/jsp/common/custompage", putErrorMessage("share.base.info.not.exists"));
            }
            messageMap.put("bindDomain", thirdCode);
            messageMap.put("shareInfo", shareInfo);

            String rurl = URLEncoder.encode("/share/content/" + thirdCode + "/bind?sid=" + shareId, "UTF-8");
            messageMap.put("reurl", rurl);

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException,e:", e);
        } catch (UnsupportedEncodingException e) {
        }

        //uno get bind third
        return new ModelAndView("/views/jsp/share/share-content", messageMap);
    }


    /**
     * 通过ID选择  一条分享信息
     *
     * @param shareId
     * @param request
     * @param response
     * @return
     */
    @Deprecated
    @RequestMapping(value = "/contentpage")
    public ModelAndView shareContentPage(@RequestParam(value = "sid", required = true) long shareId,
                                         HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> messageMap = new HashMap<String, Object>();
        //choose shareInfo
        try {
            ShareInfo shareInfo = SyncServiceSngl.get().choiceShareInfo(shareId);
            if (shareInfo == null) {
                return new ModelAndView("/views/jsp/common/custompage", putErrorMessage("share.base.info.not.exists"));
            }

            Set<String> bindCodeSet = new HashSet<String>();

            UserCenterSession userSession = getUserCenterSeesion(request);
            if (userSession != null) {

                Set<LoginDomain> loginDomains = userSession.getFlag().getLoginDomain();
                for (LoginDomain loginDomain : loginDomains) {
                    bindCodeSet.add(loginDomain.getCode());
                }
            }

            messageMap.put("bindCodeSet", bindCodeSet);
            messageMap.put("shareInfo", shareInfo);

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException,e:", e);
        }

        //uno get bind third
        return new ModelAndView("/views/jsp/share/sharepage-content", messageMap);
    }


    /**
     * 分享动作
     *
     * @return
     */
    @RequestMapping(value = "/content/{thirdCode}")
    public ModelAndView shareContent(@PathVariable(value = "thirdCode") String thirdCode,
                                     @RequestParam(value = "shareid", required = false) Long shareId,
                                     @RequestParam(value = "topic", required = true) String topic,
                                     @RequestParam(value = "title", required = false) String title,
                                     @RequestParam(value = "body", required = true) String body,
                                     @RequestParam(value = "pic", required = true) String pic,
                                     @RequestParam(value = "url", required = true) String url,
                                     HttpServletRequest request, HttpServletResponse response) {
        UserCenterSession userSession = getUserCenterSeesion(request);
        ShareBaseInfo shareBaseInfo = null;
        try {
            if (shareId != null) {
                shareBaseInfo = SyncServiceSngl.get().getShareInfoById(shareId);
            }

            LoginDomain loginDomain = LoginDomain.getByCode(thirdCode);
            if (loginDomain == null) {
                return new ModelAndView("/views/jsp/common/custompage", putErrorMessage("sync.auth.error.notsupport"));
            }

            TokenInfo tokenInfo = null;
            if (userSession != null) {
                Set<LoginDomain> loginDomains=new HashSet<LoginDomain>();
                loginDomains.add(loginDomain);
                List<UserLogin> userLogin = UserCenterServiceSngl.get().queryUserLoginUno(userSession.getUno(), loginDomains);
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

            SyncServiceSngl.get().syncShareInfo(syncContent, tokenInfo, loginDomain, userSession.getUno(), shareId);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " share occure ServiceException.e: ", e);
            return new ModelAndView("/views/jsp/common/custompage", putErrorMessage("system.error"));
        }

        Map<String, Object> mapMessage = new HashMap<String, Object>();
        String returnUrl = "/views/jsp/share/success";
        PointResultMsg pointResultMsg = PointResultMsg.SUCCESS;
        if (userSession != null && shareBaseInfo != null) {
            Date now = new Date();

            if (shareBaseInfo.getShareRewardType().hasPoint()) {
                PointActionHistory actionHistory = new PointActionHistory();
                actionHistory.setActionDate(now);

                Map<String, String> paramMap = new HashMap<String, String>();
                String descriptionTemplate = templateHotdeployConfig.getSharePointActionHistoryTemplate();

                paramMap.put("wikiname", shareBaseInfo.getShareKey());
                String descriptionBody = NamedTemplate.parse(descriptionTemplate).format(paramMap);

                actionHistory.setActionDescription(descriptionBody);
                actionHistory.setActionType(PointActionType.SHARE);
                actionHistory.setProfileId(userSession.getProfileId());
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
                    try {
                        UserPoint userPoint = PointServiceSngl.get().getUserPoint(new QueryExpress().add(QueryCriterions.eq(UserPointField.PROFILEID, userSession.getProfileId())));
                        if (userPoint != null) {
                            userSession.setPointAmount(userPoint.getUserPoint());
                        } else {
                            userSession.setPointAmount(0);
                        }

                        userSession.setPointAmount(userSession.getPointAmount());
                        returnUrl = "/views/jsp/share/success-point";
                    } catch (ServiceException e) {
                        GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e: ", e);
                    }
                }
            }

            if (shareBaseInfo.getShareRewardType().hasLottery()) {
                //todo
            }
        }

        return new ModelAndView(returnUrl, mapMessage);

    }

    @RequestMapping(value = "/newrelease/{thirdcode}/bind")
    public ModelAndView shareReleaseBind(@RequestParam(value = "sid", required = true) long shareId,
                                         @PathVariable(value = "thirdcode") String thirdCode,
                                         HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> messageMap = new HashMap<String, Object>();
        LoginDomain loginDomain = LoginDomain.getByCode(thirdCode);
        if (loginDomain == null) {
            return new ModelAndView("/views/jsp/common/custompage", putErrorMessage("sync.auth.error.notsupport"));
        }

        UserCenterSession userSession = getUserCenterSeesion(request);
        if (userSession == null) {
            return new ModelAndView("redirect:/profile/sync/" + thirdCode + "/bind?rurl=/share/newrelease/" + thirdCode + "/page?sid=" + shareId + "&icn=true");
        }

        if (!userSession.getFlag().getLoginDomain().contains(loginDomain)) {
            return new ModelAndView("redirect:/profile/sync/" + thirdCode + "/bind?rurl=/share/newrelease/" + thirdCode + "/page?sid=" + shareId + "&icn=true");
        }

        //uno get bind third
        return new ModelAndView("redirect:/share/newrelease/" + thirdCode + "/page?sid=" + shareId, messageMap);
    }

    @RequestMapping(value = "/newrelease/{thirdcode}/page")
    public ModelAndView shareReleasePageByThird(@RequestParam(value = "sid", required = true) long shareId,
                                                @PathVariable(value = "thirdcode") String thirdCode,
                                                HttpServletRequest request, HttpServletResponse response) {
        LoginDomain loginDomain = LoginDomain.getByCode(thirdCode);
        if (loginDomain == null) {
            return new ModelAndView("/views/jsp/common/custompage", putErrorMessage("sync.auth.error.notsupport"));
        }


        Map<String, Object> messageMap = new HashMap<String, Object>();
        //choose shareInfo
        try {

            ShareInfo shareInfo = SyncServiceSngl.get().choiceShareInfo(shareId);
            if (shareInfo == null) {
                return new ModelAndView("/views/jsp/common/custompage", putErrorMessage("share.base.info.not.exists"));
            }
            messageMap.put("bindDomain", thirdCode);
            messageMap.put("shareInfo", shareInfo);

            String rurl = URLEncoder.encode("/share/newrelease/" + thirdCode + "/bind?sid=" + shareId, "UTF-8");
            messageMap.put("reurl", rurl);

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException,e:", e);
        } catch (UnsupportedEncodingException e) {
        }

        //uno get bind third
        return new ModelAndView("views/jsp/share/share-newrelease", messageMap);
    }

    @RequestMapping(value = "/newrelease/{thirdCode}")
    public ModelAndView shareNewReleaseContent(@PathVariable(value = "thirdCode") String thirdCode,
                                               @RequestParam(value = "shareid", required = false) Long shareId,
                                               @RequestParam(value = "topic", required = true) String topic,
                                               @RequestParam(value = "title", required = false) String title,
                                               @RequestParam(value = "body", required = true) String body,
                                               @RequestParam(value = "pic", required = true) String pic,
                                               @RequestParam(value = "url", required = true) String url,
                                               HttpServletRequest request, HttpServletResponse response) {
        UserCenterSession userSession = getUserCenterSeesion(request);
        ShareBaseInfo shareBaseInfo = null;
        try {
            if (shareId != null) {
                shareBaseInfo = SyncServiceSngl.get().getShareInfoById(shareId);
            }

            LoginDomain loginDomain = LoginDomain.getByCode(thirdCode);
            if (loginDomain == null) {
                return new ModelAndView("/views/jsp/common/custompage", putErrorMessage("sync.auth.error.notsupport"));
            }

            TokenInfo tokenInfo = null;
            if (userSession != null) {
                Set<LoginDomain> loginDomains=new HashSet<LoginDomain>();
                loginDomains.add(loginDomain);
                List<UserLogin> userLogin = UserCenterServiceSngl.get().queryUserLoginUno(userSession.getUno(), loginDomains);
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

            SyncServiceSngl.get().syncShareInfo(syncContent, tokenInfo, loginDomain, userSession.getUno(), shareId);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " share occure ServiceException.e: ", e);
            return new ModelAndView("/views/jsp/common/custompage", putErrorMessage("system.error"));
        }

        Map<String, Object> mapMessage = new HashMap<String, Object>();
        String returnUrl = "/views/jsp/share/success";
        PointResultMsg pointResultMsg = PointResultMsg.SUCCESS;
        if (userSession != null && shareBaseInfo != null) {
            Date now = new Date();

            if (shareBaseInfo.getShareRewardType().hasPoint()) {
                PointActionHistory actionHistory = new PointActionHistory();
                actionHistory.setActionDate(now);

                Map<String, String> paramMap = new HashMap<String, String>();
                String descriptionTemplate = templateHotdeployConfig.getSharePointActionHistoryTemplate();

                paramMap.put("wikiname", shareBaseInfo.getShareKey());
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
                    returnUrl = "/views/jsp/share/success-point";
                }
            }

            if (shareBaseInfo.getShareRewardType().hasLottery()) {
                //todo
            }
        }

        return new ModelAndView(returnUrl, mapMessage);

    }


}
