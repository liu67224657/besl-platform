package com.enjoyf.webapps.joyme.webpage.controller.profile;

import com.enjoyf.platform.service.profile.ProfileBlog;
import com.enjoyf.platform.service.profile.ProfileBlogHeadIcon;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Icon;
import com.enjoyf.platform.service.usercenter.Icons;
import com.enjoyf.platform.service.usercenter.ProfileField;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.platform.webapps.common.JoymeResultMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.webapps.joyme.weblogic.user.DiscuzModifyField;
import com.enjoyf.webapps.joyme.weblogic.user.DiscuzUtil;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 11-12-12
 * Time: 下午8:38
 * To change this template use File | Settings | File Templates.
 */

@Controller
@RequestMapping(value = "/json/profile/headicon")
public class JsonHeadIconController extends BaseRestSpringController {

    Logger logger = LoggerFactory.getLogger(JsonHeadIconController.class);

    private JsonBinder binder = JsonBinder.buildNormalBinder();

    /**
     * 保存用户多头像设置
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/save")
    @ResponseBody
    public String ajaxSaveHeadIcons(HttpServletRequest request, HttpServletResponse response,
                                    @RequestParam(value = "headIcon", required = false) String[] headIcons) {
        JoymeResultMsg resultMsg = new JoymeResultMsg(JoymeResultMsg.CODE_E);
        UserCenterSession userSession = this.getUserCenterSeesion(request);

        Icons icons = new Icons();
        if (headIcons != null && headIcons.length > 0) {
            for (int i = 0; i < headIcons.length; i++) {
                icons.add(new Icon(i, headIcons[i]));
            }
        }


        String currentIcon = CollectionUtil.isEmpty(icons.getIconList()) ? "" : icons.getIconList().get(0).getIcon();

        try {
            UserCenterServiceSngl.get().modifyProfile(new UpdateExpress()
                            .set(ProfileField.ICON, currentIcon).set(ProfileField.ICONS, icons.toJsonStr()), userSession.getProfileId()
            );
            userSession.setIcons(icons);
            userSession.setIcon(currentIcon);

            DiscuzUtil.modifyProfile(DiscuzModifyField.FIELD_HEADICON, currentIcon, userSession.getUid());
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        resultMsg.setStatus_code(JoymeResultMsg.CODE_S);
        return binder.toJson(resultMsg);
    }

    private boolean isNewHeadIconAdd(ProfileBlog profileBlog, String[] headIcons) {
        if (profileBlog.getHeadIconSet() == null) {
            return true;
        }

        Set<ProfileBlogHeadIcon> set = profileBlog.getHeadIconSet().getIconSet();

        for (String pageHeadIcon : headIcons) {
            if (!set.contains(pageHeadIcon)) {
                return true;
            }
        }
        return false;
    }

}
