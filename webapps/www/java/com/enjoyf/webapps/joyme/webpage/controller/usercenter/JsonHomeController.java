package com.enjoyf.webapps.joyme.webpage.controller.usercenter;

import com.enjoyf.platform.service.RelationStatus;
import com.enjoyf.platform.service.point.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.social.ObjectRelationType;
import com.enjoyf.platform.service.social.SocialServiceSngl;
import com.enjoyf.platform.service.social.UserRelation;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.ProfileSum;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.service.usercenter.VerifyProfile;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.platform.webapps.common.html.tag.ImageURLTag;
import com.enjoyf.webapps.joyme.dto.usercenter.UserinfoDTO;
import com.enjoyf.webapps.joyme.weblogic.point.PointWebLogic;
import com.enjoyf.webapps.joyme.weblogic.usercenter.UserCenterWebLogic;
import com.enjoyf.webapps.joyme.weblogic.usercenter.UserCenterWebUtil;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterSession;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by pengxu on 2016/12/29.
 */
@Controller
@RequestMapping(value = "/joyme/json/usercenter")
public class JsonHomeController extends BaseRestSpringController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Resource(name = "userCenterWebLogic")
    private UserCenterWebLogic usercenterWebLogic;

    @Resource(name = "pointWebLogic")
    private PointWebLogic pointWebLogic;

    private JsonBinder binder = JsonBinder.buildNormalBinder();

    @RequestMapping(value = "/userinfo")
    @ResponseBody
    public String userinfo(@RequestParam(value = "profileId") String profileId, HttpServletRequest request) {
        String callback = HTTPUtil.getParam(request, "callback");
        JSONObject jsonObject = ResultCodeConstants.FAILED.getJsonObject();
        try {
            if (!StringUtil.isEmpty(profileId)) {
                UserCenterSession userCenterSession = getUserCenterSeesion(request);

                Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileId);
                UserPoint userPoint = pointWebLogic.getUserPoint(DEFAULT_APPKEY, profileId);
                if (profile != null) {
                    UserinfoDTO userinfoDTO = new UserinfoDTO();
                    userinfoDTO.setUno(profile.getProfileId());
                    userinfoDTO.setNick(profile.getNick());
                    userinfoDTO.setUid(profile.getUid());
                    userinfoDTO.setProfileId(profile.getProfileId());
                    userinfoDTO.setDesc(profile.getDescription());
                    UserRelation userRelation = SocialServiceSngl.get().getRelation(userCenterSession == null ? "" : userCenterSession.getProfileId(), profileId, ObjectRelationType.WIKI_PROFILE);
                    if (userRelation != null) {
                        userinfoDTO.setFollowStatus(String.valueOf(userRelation.getSrcStatus().getCode()));
                    } else {
                        userinfoDTO.setFollowStatus(String.valueOf(RelationStatus.UNFOCUS.getCode()));
                    }

                    ProfileSum profileSum = UserCenterServiceSngl.get().getProfileSum(profileId);
                    if (profileSum != null) { //粉丝/关注数
                        userinfoDTO.setFans(profileSum.getFansSum());
                        userinfoDTO.setFollows(profileSum.getFollowSum());
                    }

                    userinfoDTO.setIcon(ImageURLTag.parseUserCenterHeadIcon(profile.getIcon(), profile.getSex(), "m", true));
                    userinfoDTO.setSex(profile.getSex());
                    userinfoDTO.setWorship(String.valueOf(userPoint.getWorshipNum()));
                    userinfoDTO.setPrestige(userPoint.getPrestige());
                    userinfoDTO.setEdits(UserCenterWebUtil.getEditNum(profile.getUid()));

                    VerifyProfile verifyProfile = UserCenterServiceSngl.get().getVerifyProfileById(profileId);

                    //该用户获得的道具
                    Map<String, String> skinChooseMap = PointServiceSngl.get().getChooseLottery(profile.getProfileId());
                    if (skinChooseMap != null && !skinChooseMap.isEmpty()) {
                        String lotteryType = usercenterWebLogic.getLotteryType();
                        if (!StringUtil.isEmpty(skinChooseMap.get(lotteryType + LotteryType.HEAD.getCode()))) {
                            userinfoDTO.setHeadskin(skinChooseMap.get(lotteryType + LotteryType.HEAD.getCode()));
                        }
                        if (!StringUtil.isEmpty(skinChooseMap.get(lotteryType + LotteryType.BG.getCode()))) {
                            userinfoDTO.setCardskin(skinChooseMap.get(lotteryType + LotteryType.BG.getCode()));
                        }
                        if (!StringUtil.isEmpty(skinChooseMap.get(lotteryType + LotteryType.CHAT.getCode()))) {
                            userinfoDTO.setBubbleskin(skinChooseMap.get(lotteryType + LotteryType.CHAT.getCode()));
                        }
                        if (!StringUtil.isEmpty(skinChooseMap.get(lotteryType + LotteryType.COMMENT.getCode()))) {
                            userinfoDTO.setReplyskin(skinChooseMap.get(lotteryType + LotteryType.COMMENT.getCode()));
                        }
                    }

                    if (verifyProfile != null) {    //认证
                        userinfoDTO.setVtype(verifyProfile.getVerifyType());
                        userinfoDTO.setVdesc(verifyProfile.getDescription());
                        userinfoDTO.setVtitle(StringUtil.isEmpty(verifyProfile.getVerifyTitle()) ? "" : verifyProfile.getVerifyTitle());
                    }
                    userinfoDTO.setUserMWikiDTO(usercenterWebLogic.getUserMWikiDTO(profile.getUid()));
                    jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
                    jsonObject.put("result", userinfoDTO);
                }
            } else {
                jsonObject = ResultCodeConstants.PARAM_EMPTY.getJsonObject();
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " /joyme/api/usercenter/userinfo?profileId=" + profileId + "  ServiceException.e:" + e);
        }
        return callback + "([" + jsonObject.toString() + "]);";
    }


    @RequestMapping(value = "/sign")
    @ResponseBody
    public String sign(HttpServletRequest request, HttpServletResponse reponse) {
        JSONObject jsonObject = ResultCodeConstants.ERROR.getJsonObject();
        try {
            UserCenterSession userCenterSession = getUserCenterSeesion(request);
            if (userCenterSession == null) {
                return jsonObject.toString();
            }
            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(userCenterSession.getProfileId());
            if (profile == null) {
                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
            }
            int value = pointWebLogic.modifyUserPoint(PointActionType.WANBA_SIGN, profile.getProfileId(), DEFAULT_APPKEY, WanbaPointType.SIGN, null);
            jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
            jsonObject.put("result", value);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " Exception.e:" + e);
        }
        return jsonObject.toString();
    }

}
