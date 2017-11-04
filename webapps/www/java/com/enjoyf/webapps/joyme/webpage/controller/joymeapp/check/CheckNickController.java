package com.enjoyf.webapps.joyme.webpage.controller.joymeapp.check;

import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.wordfilter.ContextFilterUtils;
import com.enjoyf.util.StringUtil;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/12/16
 * Description:
 */
@Controller
@RequestMapping(value = "/joymeapp/check")
public class CheckNickController extends BaseRestSpringController {


    //检查昵称的接口
    @ResponseBody
    @RequestMapping(value = "/nick")
    public String nick(@RequestParam(value = "nick", required = false, defaultValue = "") String nick,
                       HttpServletRequest request) {

        JSONObject jsonObject = new JSONObject();
        nick = nick.trim();
        String uid = HTTPUtil.getParam(request, "uid");
        if (StringUtil.isEmpty(nick.trim()) || StringUtil.isEmpty(uid)) {
            jsonObject.put("rs", String.valueOf(ResultCodeConstants.PARAM_EMPTY.getCode()));
            jsonObject.put("msg", String.valueOf(ResultCodeConstants.PARAM_EMPTY.getMsg()));
            return jsonObject.toString();
        }
        if (ContextFilterUtils.postContainBlackList(nick)) {
            jsonObject.put("rs", ResultCodeConstants.CONTEXT_CONTENT_ILLEGAL.getCode());
            jsonObject.put("msg", ResultCodeConstants.CONTEXT_CONTENT_ILLEGAL.getMsg());
            return jsonObject.toString();
        }

        try {

            //todo
            Profile profile = UserCenterServiceSngl.get().getProfileByNick(nick);

            if (profile != null && !uid.equals(profile.getUid() + "")) {
                jsonObject.put("rs", String.valueOf(ResultCodeConstants.USERCENTER_PROFILE_HAS_EXISTS.getCode()));
                jsonObject.put("msg", String.valueOf(ResultCodeConstants.USERCENTER_PROFILE_HAS_EXISTS.getMsg()));
                return jsonObject.toString();
            }

            jsonObject.put("rs", String.valueOf(ResultCodeConstants.SUCCESS.getCode()));
            jsonObject.put("msg", String.valueOf(ResultCodeConstants.SUCCESS.getMsg()));
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            jsonObject.put("rs", String.valueOf(ResultCodeConstants.SYSTEM_ERROR.getCode()));
            jsonObject.put("msg", String.valueOf(ResultCodeConstants.SYSTEM_ERROR.getMsg()));
        }
        return jsonObject.toString();
    }
}
