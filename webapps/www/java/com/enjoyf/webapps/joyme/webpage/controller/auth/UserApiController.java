package com.enjoyf.webapps.joyme.webpage.controller.auth;

import com.enjoyf.platform.service.point.UserPoint;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.*;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.webapps.joyme.weblogic.giftmarket.GiftMarketWebLogic;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/11/4
 * Description:
 */
@Controller
@RequestMapping("/api/user")
public class UserApiController {


    @Resource(name = "giftMarketWebLogic")
    private GiftMarketWebLogic giftMarketWebLogic;

    /**
     * 自动注册
     * clientid required
     * appkey  required
     * logindomain required
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/getbyuno")
    public String getByUno(HttpServletRequest request, HttpServletResponse response,
                           @RequestParam(value = "uno", required = false) String uno) {

        JSONObject jsonObject = new JSONObject();

        String appKey = HTTPUtil.getParam(request, "appkey");
        try {
            if (StringUtil.isEmpty(uno) || StringUtil.isEmpty(appKey)) {
                jsonObject.put("rs", ResultCodeConstants.PARAM_EMPTY.getCode());
                jsonObject.put("msg", ResultCodeConstants.PARAM_EMPTY.getMsg());
                return jsonObject.toString();
            }

            Profile profile = UserCenterServiceSngl.get().getProfileByUno(uno, appKey);
            if (profile == null) {
                jsonObject.put("rs", ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getCode());
                jsonObject.put("msg", ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getMsg());
                return jsonObject.toString();
            }

            UserPoint userPoint = giftMarketWebLogic.getUserPoint(appKey, profile);

            JSONObject profileJson = new JSONObject();
            profileJson.put("uno", profile.getUno());
            profileJson.put("icon", profile.getIcon() == null ? "" : profile.getIcon());
            profileJson.put("nick", profile.getNick() == null ? "" : profile.getNick());
            profileJson.put("uid", profile.getUid());
            profileJson.put("point", String.valueOf(userPoint == null ? 0 : userPoint.getUserPoint()));
            profileJson.put("description", profile.getDescription() == null ? "" : profile.getDescription());

            jsonObject.put("rs", 1);
            jsonObject.put("profile", profileJson);

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);

            jsonObject.put("rs", ResultCodeConstants.SYSTEM_ERROR.getCode());
            jsonObject.put("msg", ResultCodeConstants.SYSTEM_ERROR.getMsg());

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            jsonObject.put("rs", ResultCodeConstants.SYSTEM_ERROR.getCode());
            jsonObject.put("msg", ResultCodeConstants.SYSTEM_ERROR.getMsg());
        }
        return jsonObject.toString();
    }


    /**
     * 自动注册
     * clientid required
     * appkey  required
     * logindomain required
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/getbyuid")
    public String getByUid(HttpServletRequest request, HttpServletResponse response,
                           @RequestParam(value = "uid", required = false) String uidParam) {

        JSONObject jsonObject = new JSONObject();
        try {
            String appkey = HTTPUtil.getParam(request, "appkey");
            if (StringUtil.isEmpty(uidParam) || StringUtil.isEmpty(appkey)) {
                jsonObject.put("rs", ResultCodeConstants.PARAM_EMPTY.getCode());
                jsonObject.put("msg", ResultCodeConstants.PARAM_EMPTY.getMsg());
                return jsonObject.toString();
            }

            long uid = -1;
            if (!StringUtil.isEmpty(uidParam)) {
                uid = Long.parseLong(uidParam);
            }

            if (uid < 0l) {
                jsonObject.put("rs", ResultCodeConstants.PARAM_EMPTY.getCode());
                jsonObject.put("msg", ResultCodeConstants.PARAM_EMPTY.getMsg());
                return jsonObject.toString();
            }

            Profile profile = UserCenterServiceSngl.get().getProfileByUid(uid);
            if (profile == null) {
                jsonObject.put("rs", ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getCode());
                jsonObject.put("msg", ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getMsg());
                return jsonObject.toString();
            }


            UserPoint userPoint = giftMarketWebLogic.getUserPoint(appkey, profile);

//
            JSONObject profileJson = new JSONObject();
            profileJson.put("uno", profile.getUno());
            profileJson.put("icon", profile.getIcon() == null ? "" : profile.getIcon());
            profileJson.put("nick", profile.getNick() == null ? "" : profile.getNick());
            profileJson.put("uid", profile.getUid());
            profileJson.put("point", String.valueOf(userPoint == null ? 0 : userPoint.getUserPoint()));
            profileJson.put("description", profile.getDescription() == null ? "" : profile.getDescription());


            jsonObject.put("rs", 1);
            jsonObject.put("profile", profileJson);

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            jsonObject.put("rs", ResultCodeConstants.SYSTEM_ERROR.getCode());
            jsonObject.put("msg", ResultCodeConstants.SYSTEM_ERROR.getMsg());
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            jsonObject.put("rs", ResultCodeConstants.SYSTEM_ERROR.getCode());
            jsonObject.put("msg", ResultCodeConstants.SYSTEM_ERROR.getMsg());
        }
        return jsonObject.toString();
    }
}
