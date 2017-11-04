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
//todo rename
@Controller
@RequestMapping(value = "/person")
public class PersonInfoController extends BaseRestSpringController {
    private static final Logger logger = LoggerFactory.getLogger(PersonInfoController.class);


    @RequestMapping(value = "/info")
    public ModelAndView index(HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            UserCenterSession userSession = getUserCenterSeesion(request);
            if (userSession == null || StringUtil.isEmpty(userSession.getProfileId())) {
                mapMessage.put("isnotlogin", "true");
                return new ModelAndView("/views/jsp/usercenter/person-center", mapMessage);
            }
            String profiileId = userSession.getProfileId();
            //String profiileId = "91da4e84629fba26dd4b9bc66d25ba50";
            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profiileId);
            Map<Integer, Region> regionMap = SysCommCache.get().getRegionMap();
            List<Region> regionList = Lists.newArrayList();
            for (Region region : regionMap.values()) {
                if (region.getRegionLevel() == 1) {
                    regionList.add(region);
                }
            }
            mapMessage.put("regionList", regionList);
            mapMessage.put("profile", profile);
            mapMessage.put("type", "message");
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        return new ModelAndView("/views/jsp/usercenter/person-info", mapMessage);
    }

    @RequestMapping(value = "/save")
    public ModelAndView save(HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {

            String profileId = request.getParameter("profileId");
            String sex = request.getParameter("sex");
            String nick = request.getParameter("nick");
            String description = request.getParameter("description");
            String birthday = request.getParameter("birthday");
            String hobby = request.getParameter("hobby");
            String provinceId = request.getParameter("provinceId");
            String msn = request.getParameter("msn");
            String realName = request.getParameter("realName");
            String mobile = request.getParameter("mobile");


            UpdateExpress updateExpress = new UpdateExpress()
                    .set(ProfileField.BIRTHDAY, birthday)
                    .set(ProfileField.SEX, sex)
                    .set(ProfileField.DESCRIPTION, description)
                    .set(ProfileField.HOBBY, hobby)
                    .set(ProfileField.PROVINCEID, Integer.parseInt(provinceId))
                    .set(ProfileField.MOBILE, mobile)
                    .set(ProfileField.REALNAME, realName);
            if (StringUtil.isEmpty(nick)) {
                updateExpress.set(ProfileField.NICK, nick);
            }
            UserCenterServiceSngl.get().modifyProfile(updateExpress, profileId);
            Map<Integer, Region> regionMap = SysCommCache.get().getRegionMap();
            List<Region> regionList = Lists.newArrayList();
            for (Region region : regionMap.values()) {
                if (region.getRegionLevel() == 1) {
                    regionList.add(region);
                }
            }
            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileId);
            mapMessage.put("regionList", regionList);
            mapMessage.put("profile", profile);
            mapMessage.put("type", "message");
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        return new ModelAndView("/views/jsp/usercenter/person-info", mapMessage);
    }
}
