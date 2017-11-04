package com.enjoyf.webapps.joyme.webpage.controller.usercenter;

import com.enjoyf.platform.service.usercenter.*;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-9-25
 * Time: 上午9:43
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/api/privacy")
public class ApiPrivacyController extends BaseRestSpringController {
    private static final Logger logger = LoggerFactory.getLogger(ApiPrivacyController.class);

    private JsonBinder binder = JsonBinder.buildNormalBinder();

    /**
     * 用户隐私查询接口
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/home")
    @ResponseBody
    public String home(@RequestParam(value = "profileId") String profileId, HttpServletRequest request) {
        try {
            if (!StringUtil.isEmpty(profileId)) {
                UserPrivacy userPrivacy = UserCenterServiceSngl.get().getUserPrivacy(profileId);
                if (userPrivacy != null) {
                    JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
                    jsonObject.put("result", userPrivacy);
                    return jsonObject.toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultCodeConstants.ERROR.getJsonString();
    }


    @RequestMapping(value = "/updateuserprivacy")
    @ResponseBody
    public String updateuserprivacy(@RequestParam(value = "uid") String uid, HttpServletRequest request,
                                    @RequestParam(value = "userat", required = false) String userat,
                                    @RequestParam(value = "comment", required = false) String comment,
                                    @RequestParam(value = "agreement", required = false) String agreement,
                                    @RequestParam(value = "follow", required = false) String follow,
                                    @RequestParam(value = "systeminfo", required = false) String systeminfo) {
        try {
            if (StringUtil.isEmpty(uid)) {
                GAlerter.lan("updateuserprivacy---uid is null");
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            }

            //
            if (StringUtil.isEmpty(userat) && StringUtil.isEmpty(comment) && StringUtil.isEmpty(agreement) &&
                    StringUtil.isEmpty(userat) && StringUtil.isEmpty(systeminfo)) {
                GAlerter.lan("userat---uid is null");
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            }

            Profile profile = UserCenterServiceSngl.get().getProfileByUid(Long.valueOf(uid));
            if (profile == null) {
                GAlerter.lan("updateuserprivacy---profile is null");
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            }

            UserPrivacy userPrivacy = UserCenterServiceSngl.get().getUserPrivacy(profile.getProfileId());
            if (userPrivacy != null) {
                UserPrivacyPrivacyAlarm privacyAlarm = userPrivacy.getAlarmSetting();

                if (!StringUtil.isEmpty(agreement)) {
                    privacyAlarm.setAgreement(agreement);
                }
                if (!StringUtil.isEmpty(comment)) {
                    privacyAlarm.setComment(comment);
                }
                if (!StringUtil.isEmpty(follow)) {
                    privacyAlarm.setFollow(follow);
                }
                if (!StringUtil.isEmpty(systeminfo)) {
                    privacyAlarm.setSysteminfo(systeminfo);
                }
                if (!StringUtil.isEmpty(userat)) {
                    privacyAlarm.setUserat(userat);
                }
                UpdateExpress updateExpress = new UpdateExpress().set(UserPrivacyField.ALARMSETTING, privacyAlarm.toJson())
                        .set(UserPrivacyField.UPDATEIP, getIp(request))
                        .set(UserPrivacyField.UPDATETIME, new Timestamp((new Date()).getTime()));
                UserCenterServiceSngl.get().modifyUserPrivacy(profile.getProfileId(), updateExpress);
            }
            return ResultCodeConstants.SUCCESS.getJsonString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultCodeConstants.ERROR.getJsonString();
    }


}
