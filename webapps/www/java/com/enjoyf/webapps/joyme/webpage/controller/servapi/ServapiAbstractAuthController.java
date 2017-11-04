package com.enjoyf.webapps.joyme.webpage.controller.servapi;

import com.enjoyf.platform.service.usercenter.AuthProfile;
import com.enjoyf.platform.service.usercenter.LoginDomain;
import com.enjoyf.platform.webapps.common.html.tag.ImageURLTag;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import net.sf.json.JSONObject;

/**
 * Created by ericliu on 14/10/22.
 */
public abstract class ServapiAbstractAuthController extends BaseRestSpringController {
    public static final String LOTTERY_TYPE = "lotteryType_";


    protected JSONObject getResultByAuthProfile(AuthProfile profile, String loginId, LoginDomain loginDomain) {
        JSONObject jsonObject = new JSONObject();

        JSONObject tokenJson = new JSONObject();
        tokenJson.put("token", profile.getToken().getToken());
        tokenJson.put("expires", String.valueOf(profile.getToken().getTokenExpires()));

        JSONObject loginJson = new JSONObject();
        loginJson.put("loginid", loginId);
        loginJson.put("logindomain", loginDomain.getCode());

        JSONObject profileJson = new JSONObject();
        profileJson.put("uno", profile.getProfile().getUno());
        profileJson.put("profileid", profile.getProfile().getProfileId());
        profileJson.put("icon", ImageURLTag.parseUserCenterHeadIcon(profile.getProfile().getIcon(), profile.getProfile().getSex(), "m", true));
        profileJson.put("nick", profile.getProfile().getNick() == null ? "" : profile.getProfile().getNick());
        profileJson.put("uid", String.valueOf(profile.getProfile().getUid()));
        profileJson.put("desc", profile.getProfile().getDescription() == null ? "" : profile.getProfile().getDescription());
        profileJson.put("flag", profile.getProfile().getFlag().getValue());
        profileJson.put("mobile", profile.getProfile().getMobile() == null ? "" : profile.getProfile().getMobile());
        profileJson.put("sex", profile.getProfile().getSex() == null ? "" : profile.getProfile().getSex());
        profileJson.put("province", profile.getProfile().getProvinceId() == null ? "" : profile.getProfile().getProvinceId());
        profileJson.put("city", profile.getProfile().getCityId()== null ? "" : profile.getProfile().getCityId());

        jsonObject.put("rs", 1);
        jsonObject.put("token", tokenJson);
        jsonObject.put("profile", profileJson);
        jsonObject.put("login",loginJson);

        return jsonObject;
    }


}
