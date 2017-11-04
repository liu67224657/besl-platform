package com.enjoyf.webapps.tools.webpage.controller.point;

import com.enjoyf.platform.service.point.PointServiceSngl;
import com.enjoyf.platform.service.point.UserPoint;
import com.enjoyf.platform.service.point.UserPointField;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-8
 * Time: 上午10:06
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/point/userpoint")
public class UserPointController extends ToolsBaseController {

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "profilename", required = false) String profileName) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        if (StringUtil.isEmpty(profileName)) {
            return new ModelAndView("/point/userpointlist", mapMessage);
        }

        try {
            Profile profile = UserCenterServiceSngl.get().getProfileByNick(profileName);
            if (profile == null) {
                mapMessage = putErrorMessage(mapMessage, "profile.has.notexists");
                return new ModelAndView("/point/userpointlist", mapMessage);
            }

            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(UserPointField.PROFILEID, profile.getProfileId()));

            UserPoint userPointObj = PointServiceSngl.get().getUserPoint(queryExpress);

            mapMessage.put("userPointObj", userPointObj);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        mapMessage.put("profilename", profileName);
        return new ModelAndView("/point/userpointlist", mapMessage);
    }


}
