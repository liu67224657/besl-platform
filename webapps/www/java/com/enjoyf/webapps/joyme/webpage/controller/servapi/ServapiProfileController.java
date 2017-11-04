package com.enjoyf.webapps.joyme.webpage.controller.servapi;

import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.*;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.wordfilter.ContextFilterUtils;
import com.enjoyf.webapps.joyme.weblogic.user.DiscuzModifyField;
import com.enjoyf.webapps.joyme.weblogic.user.DiscuzUtil;
import com.enjoyf.webapps.joyme.webpage.controller.auth.AbstractAuthController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;


@Controller
@RequestMapping("/servapi/profile")
public class ServapiProfileController extends AbstractAuthController {

    private Logger logger = LoggerFactory.getLogger(ServapiProfileController.class);


    @ResponseBody
    @RequestMapping("/modify/nick")
    public String modifyNick(HttpServletRequest request,
                             @RequestParam(value = "nick", required = false) String nick) {

        String pofileId = HTTPUtil.getParam(request, "profileid");
        try {
            nick = nick.trim();
            if (StringUtil.isEmpty(nick)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            }

            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(pofileId);
            if (profile == null) {
                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
            }

            if (profile.getFlag().hasFlag(ProfileFlag.FLAG_NICK_HASCOMPLETE)) {
                return ResultCodeConstants.USERCENTER_NICK_HAS_MODIFY.getJsonString();
            }

            Profile nickProfile = UserCenterServiceSngl.get().getProfileByNick(nick);
            if (nickProfile != null && nickProfile.getProfileId().equalsIgnoreCase(pofileId)) {
                return ResultCodeConstants.USERCENTER_NICK_HAS_EXISTS.getJsonString();
            }
            if (ContextFilterUtils.postContainBlackList(nick)) {
                return ResultCodeConstants.CONTEXT_CONTENT_ILLEGAL.getJsonString();
            }

            boolean bval=UserCenterServiceSngl.get().modifyProfile(new UpdateExpress()
                    .set(ProfileField.NICK, nick)
                    .set(ProfileField.CHECKNICK, nick.toLowerCase())
                    .set(ProfileField.FLAG, profile.getFlag().has(ProfileFlag.FLAG_NICK_HASCOMPLETE).getValue())
                    , profile.getProfileId());

            if (bval) {
                try {
                    DiscuzUtil.modifyProfile(DiscuzModifyField.FIELD_SCREENNAME, nick, profile.getUid());
                } catch (Exception e) {
                }
            }

            return ResultCodeConstants.SUCCESS.getJsonString();
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
    }

    @RequestMapping(value = "/modify/icon")
    @ResponseBody
    public String modifyIcon(HttpServletRequest request) {
        String pofileId = HTTPUtil.getParam(request, "profileid");
        String headIcon = HTTPUtil.getParam(request, "icon");

        try {
            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(pofileId);

            Icon icon = new Icon();
            icon.setIcon(headIcon);
            if (profile.getIcons() == null) {
                profile.setIcons(new Icons());
            }

            if (profile.getIcons().getIconList() == null) {
                profile.getIcons().setIconList(new ArrayList<Icon>());
            }

            profile.getIcons().getIconList().add(0, icon);
            if (profile.getIcons().getIconList().size() > 8) {
                profile.getIcons().getIconList().remove(profile.getIcons().getIconList().get(8));
            }

            UserCenterServiceSngl.get().modifyProfile(new UpdateExpress()
                            .set(ProfileField.ICON, headIcon)
                            .set(ProfileField.ICONS, profile.getIcons().toJsonStr()), pofileId
            );

            try {
                DiscuzUtil.modifyProfile(DiscuzModifyField.FIELD_HEADICON, headIcon, profile.getUid());
            } catch (Exception e) {
            }

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }

        return ResultCodeConstants.SUCCESS.getJsonString();
    }

    @RequestMapping(value = "/modify/info")
    @ResponseBody
    public String modifyInfo(HttpServletRequest request) {
        String profileId = HTTPUtil.getParam(request, "profileid");
        String sex = HTTPUtil.getParam(request, "sex");
        String proviceIdParam = HTTPUtil.getParam(request, "proviceid");
        String cityIdParam = HTTPUtil.getParam(request, "cityid");

        try {
            if (StringUtil.isEmpty(profileId)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            }

            if (StringUtil.isEmpty(sex) && StringUtil.isEmpty(proviceIdParam) && StringUtil.isEmpty(cityIdParam)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            }

            int provinceid = -2;
            if (!StringUtil.isEmpty(proviceIdParam)) {
                try {
                    provinceid = Integer.parseInt(proviceIdParam);
                } catch (NumberFormatException e) {
                    logger.error("provinceid NumberFormatException" + e.getMessage());
                }
            }

            int cityId = -2;
            if (!StringUtil.isEmpty(cityIdParam)) {
                try {
                    cityId = Integer.parseInt(cityIdParam);
                } catch (NumberFormatException e) {
                    logger.error("cityId NumberFormatException" + e.getMessage());
                }
            }

            UpdateExpress updateExpress = new UpdateExpress();
            if (!StringUtil.isEmpty(sex)) {
                updateExpress.set(ProfileField.SEX, sex);
            }
            if (provinceid > -2) {
                updateExpress.set(ProfileField.PROVINCEID, provinceid);
            }
            if (cityId > -2) {
                updateExpress.set(ProfileField.CITYID, cityId);
            }

            if(StringUtil.isEmpty(sex) && provinceid>-2 && cityId>-2){
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            }

            UserCenterServiceSngl.get().modifyProfile(updateExpress, profileId);

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }

        return ResultCodeConstants.SUCCESS.getJsonString();
    }


}
