package com.enjoyf.webapps.joyme.webpage.controller.joymeapp.gameclient.json;

import com.enjoyf.platform.service.IntValidStatus;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.social.ObjectRelationType;
import com.enjoyf.platform.service.social.ProfileRelation;
import com.enjoyf.platform.service.social.SocialServiceSngl;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.util.StringUtil;
import com.enjoyf.webapps.joyme.webpage.controller.joymeapp.gameclient.AbstractGameClientBaseController;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/12/16
 * Description:
 */
@Controller
@RequestMapping("/joymeapp/gameclient/json/profile")
public class GameClientJsonProfileController extends AbstractGameClientBaseController {

    //喜欢动作
    @ResponseBody
    @RequestMapping(value = "/like")
    public String like(HttpServletRequest request) {
        String callback = request.getParameter("callback");

        String uidParam = request.getParameter("uid");
        if (StringUtil.isEmpty(uidParam)) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        long uid = -1;
        try {
            uid = Long.parseLong(uidParam);
        } catch (NumberFormatException e) {
        }
        if (uid == -1) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        String destUidParam = request.getParameter("desuid");
        if (StringUtil.isEmpty(uidParam)) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        long destUid = -1;
        try {
            destUid = Long.parseLong(destUidParam);
        } catch (NumberFormatException e) {
        }
        if (destUid == -1) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }
        if (uid == destUid) {
            if (callback == null || StringUtil.isEmpty(callback)) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("rs", ResultCodeConstants.SUCCESS.getCode());
                jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
                jsonObject.put("result", new JSONObject());
                return jsonObject.toString();
            } else {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("rs", ResultCodeConstants.SUCCESS.getCode());
                jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
                jsonObject.put("result", new JSONObject());
                return callback + "([" + jsonObject.toString() + "])";
            }
        }
        try {
            Profile profile = UserCenterServiceSngl.get().getProfileByUid(uid);
            if (profile == null) {
                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
            }

            Profile destProfile = UserCenterServiceSngl.get().getProfileByUid(destUid);
            if (destProfile == null) {
                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
            }
            AuthApp app = OAuthServiceSngl.get().getApp(request.getParameter("appkey"));

            ProfileRelation profileRelation = new ProfileRelation();
            profileRelation.setType(ObjectRelationType.PROFILE);
            profileRelation.setSrcProfileId(profile.getProfileId());
            profileRelation.setDestProfileId(destProfile.getProfileId());
            profileRelation.setSrcStatus(IntValidStatus.VALID);
            profileRelation.setDestStatus(IntValidStatus.UNVALID);
            profileRelation.setProfileKey(app.getProfileKey());
            profileRelation.setModifyIp(getIp(request));
            profileRelation.setModifyTime(new Date());

            SocialServiceSngl.get().saveProfileRelation(profileRelation);

            if (callback == null || StringUtil.isEmpty(callback)) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("rs", ResultCodeConstants.SUCCESS.getCode());
                jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
                jsonObject.put("result", new JSONObject());
                return jsonObject.toString();
            } else {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("rs", ResultCodeConstants.SUCCESS.getCode());
                jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
                jsonObject.put("result", new JSONObject());
                return callback + "([" + jsonObject.toString() + "])";
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            if (callback == null || StringUtil.isEmpty(callback)) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("rs", ResultCodeConstants.ERROR.getCode());
                jsonObject.put("msg", ResultCodeConstants.ERROR.getMsg());
                jsonObject.put("result", new JSONObject());
                return jsonObject.toString();
            } else {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("rs", ResultCodeConstants.ERROR.getCode());
                jsonObject.put("msg", ResultCodeConstants.ERROR.getMsg());
                jsonObject.put("result", new JSONObject());
                return callback + "([" + jsonObject.toString() + "])";
            }
        }
    }

    //不喜欢动作
    @ResponseBody
    @RequestMapping(value = "/unlike")
    public String unlike(HttpServletRequest request) {
        String callback = request.getParameter("callback");

        String uidParam = request.getParameter("uid");
        if (StringUtil.isEmpty(uidParam)) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        long uid = -1;
        try {
            uid = Long.parseLong(uidParam);
        } catch (NumberFormatException e) {
        }
        if (uid == -1) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        String destUidParam = request.getParameter("desuid");
        if (StringUtil.isEmpty(uidParam)) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        long destUid = -1;
        try {
            destUid = Long.parseLong(destUidParam);
        } catch (NumberFormatException e) {
        }
        if (destUid == -1) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        if (uid == destUid) {
            if (callback == null || StringUtil.isEmpty(callback)) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("rs", ResultCodeConstants.SUCCESS.getCode());
                jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
                jsonObject.put("result", new JSONObject());
                return jsonObject.toString();
            } else {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("rs", ResultCodeConstants.SUCCESS.getCode());
                jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
                jsonObject.put("result", new JSONObject());
                return callback + "([" + jsonObject.toString() + "])";
            }
        }
        try {
            Profile profile = UserCenterServiceSngl.get().getProfileByUid(uid);
            if (profile == null) {
                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
            }

            Profile destProfile = UserCenterServiceSngl.get().getProfileByUid(destUid);
            if (destProfile == null) {
                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
            }
            AuthApp app = OAuthServiceSngl.get().getApp(request.getParameter("appkey"));

            SocialServiceSngl.get().removeProfileRelation(profile.getProfileId(), destProfile.getProfileId(), ObjectRelationType.PROFILE, app.getProfileKey());

            if (callback == null || StringUtil.isEmpty(callback)) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("rs", ResultCodeConstants.SUCCESS.getCode());
                jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
                jsonObject.put("result", new JSONObject());
                return jsonObject.toString();
            } else {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("rs", ResultCodeConstants.SUCCESS.getCode());
                jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
                jsonObject.put("result", new JSONObject());
                return callback + "([" + jsonObject.toString() + "])";
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            if (callback == null || StringUtil.isEmpty(callback)) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("rs", ResultCodeConstants.ERROR.getCode());
                jsonObject.put("msg", ResultCodeConstants.ERROR.getMsg());
                jsonObject.put("result", new JSONObject());
                return jsonObject.toString();
            } else {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("rs", ResultCodeConstants.ERROR.getCode());
                jsonObject.put("msg", ResultCodeConstants.ERROR.getMsg());
                jsonObject.put("result", new JSONObject());
                return callback + "([" + jsonObject.toString() + "])";
            }
        }
    }

}
