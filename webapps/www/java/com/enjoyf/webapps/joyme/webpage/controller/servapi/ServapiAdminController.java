package com.enjoyf.webapps.joyme.webpage.controller.servapi;

import com.enjoyf.platform.service.event.EventDispatchServiceSngl;
import com.enjoyf.platform.service.event.system.UserPointEvent;
import com.enjoyf.platform.service.point.PointActionType;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.util.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/12/30
 * Description:
 */
@Controller
@RequestMapping(value = "/servapi/admin")
public class ServapiAdminController extends AbstractServApiController {

    //http://servapi.joyme.test/servapi/point/adjust?uid=20001&appkey=default&token=04a70b35-e9c7-431d-859a-2a7fd96c85f8&uno=0e65b12d-0a81-46d3-90e0-674b5d21d65b&reason=6&desc=xiaozujiajifen&point=10&destid=101
    private static final String FLAG = "31a5c6d924c176b96d372998eda003c4";

    /**
     * @param uidString    uid
     * @param reasonString 6
     * @param desc         小组获取
     * @param destid       帖子ID
     * @param pointString  分数
     * @param appkey       appkey
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/adjustpoint")
    public String adminAdjust(@RequestParam(value = "uid", required = false) String uidString,
                              @RequestParam(value = "reason", required = false) String reasonString,
                              @RequestParam(value = "desc", required = false) String desc,
                              @RequestParam(value = "destid", required = false) String destid,
                              @RequestParam(value = "point", required = false) String pointString,
                              @RequestParam(value = "appkey", required = false) String appkey,
                              @RequestParam(value = "flag", required = false) String flag,
                              HttpServletRequest request, HttpServletResponse response) {
        if (StringUtil.isEmpty(flag) || !flag.equals(FLAG)) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }


        long uid = -1;
        try {
            uid = Long.parseLong(uidString);
        } catch (NumberFormatException e) {
        }

        PointActionType type = null;
        try {
            int reason = -1;
            reason = Integer.parseInt(reasonString);
            if (reason > 0) {
                type = PointActionType.getByCode(reason);
            }

        } catch (NumberFormatException e) {
        }

        //point 是0 抛错，不用调用接口
        int point = 0;
        try {
            point = Integer.parseInt(pointString);
        } catch (NumberFormatException e) {
        }

        if (uid == -1 || type == null || point == 0) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        try {
            Profile profile = UserCenterServiceSngl.get().getProfileByUid(uid);
            if (profile == null) {
                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
            }

            UserPointEvent event = new UserPointEvent();
            event.setProfileId(profile.getProfileId());
            event.setUno(profile.getUno());
            event.setObjectId(destid);
            event.setPoint(point);
            event.setPointActionType(type);
            event.setDescription(desc);
          //  event.setProfileKey(profile.getProfileKey());
          //  event.setPointKey(PointKeyType.getByCode(appkey).getValue());
            event.setAppkey(appkey);         //modified by tony 2015-05-14，不再使用pointKey和profileKey,使用appkey,在pointLogic中处理事件时再用appkey去查询pointkey
            event.setActionDate(new Date());
            EventDispatchServiceSngl.get().dispatch(event);

            return ResultCodeConstants.SUCCESS.getJsonString();
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + "occured Exception.e: ", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
    }

}
