package com.enjoyf.webapps.joyme.webpage.controller.usercenter;

import com.enjoyf.platform.service.misc.Region;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.ProfileField;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterSession;
import com.enjoyf.webapps.joyme.webpage.cache.SysCommCache;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-9-25
 * Time: 上午9:43
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/modifyheadimg")
public class ModifyHeadimgController extends BaseRestSpringController {
    private static final Logger logger = LoggerFactory.getLogger(ModifyHeadimgController.class);


    @RequestMapping(value = "/upload")
    public ModelAndView upload(HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            UserCenterSession userSession = getUserCenterSeesion(request);
            if (userSession == null || StringUtil.isEmpty(userSession.getProfileId())) {
                mapMessage.put("isnotlogin", "true");
                return new ModelAndView("/views/jsp/usercenter/person-center",mapMessage);
            }
            String profileid = userSession.getProfileId();
            //String profileid = "91da4e84629fba26dd4b9bc66d25ba50";
            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileid);

            mapMessage.put("type","portrait");
            mapMessage.put("profile",profile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView("/views/jsp/usercenter/modify-headimg", mapMessage);
    }

}
