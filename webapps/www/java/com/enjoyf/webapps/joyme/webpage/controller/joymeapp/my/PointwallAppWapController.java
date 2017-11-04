package com.enjoyf.webapps.joyme.webpage.controller.joymeapp.my;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.point.*;
import com.enjoyf.platform.service.point.pointwall.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.oauth.renren.api.client.utils.Md5Utils;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.QuerySort;
import com.enjoyf.platform.util.sql.QuerySortOrder;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.webapps.joyme.weblogic.giftmarket.GiftMarketWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tonydiao on 2014/12/5.
 */
@Controller
@RequestMapping(value = "/my/hotapp")
public class PointwallAppWapController extends BaseRestSpringController {
    @Resource(name = "giftMarketWebLogic")
    private GiftMarketWebLogic giftMarketWebLogic;

    @RequestMapping
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {

            String appkey = HTTPUtil.getParam(request, "appkey");
            String platform = HTTPUtil.getParam(request, "platform");
            String uno = HTTPUtil.getParam(request, "uno");
            String clientId = HTTPUtil.getParam(request, "clientid");

            if (StringUtil.isEmpty(uno)) {
                mapMessage.put("errorMessage", "uno.is.null");
                return new ModelAndView("/views/jsp/my/hotappwap", mapMessage);
            }
            if (StringUtil.isEmpty(appkey)) {
                mapMessage.put("errorMessage", "appkey.is.null");
                return new ModelAndView("/views/jsp/my/hotappwap", mapMessage);
            }
            if (StringUtil.isEmpty(clientId)) {
                mapMessage.put("errorMessage", "clientid.is.null");
                return new ModelAndView("/views/jsp/my/hotappwap", mapMessage);
            }
            if (StringUtil.isEmpty(platform)) {
                mapMessage.put("errorMessage", "platform.is.null");
                return new ModelAndView("/views/jsp/my/hotappwap", mapMessage);
            }
            mapMessage.put("platform", platform);
            mapMessage.put("clientid", clientId);
            AuthApp authApp = OAuthServiceSngl.get().getApp(appkey);
            if (authApp == null) {
                mapMessage.put("errorMessage", "authapp.is.null");
                return new ModelAndView("/views/jsp/my/hotappwap", mapMessage);
            }
            Profile profile = UserCenterServiceSngl.get().getProfileByUno(uno, authApp.getProfileKey());

            if (profile == null) {

                mapMessage.put("errorMessage", "profile.is.null");
                return new ModelAndView("/views/jsp/my/hotappwap", mapMessage);
            }

            PointwallWall wall = PointServiceSngl.get().getPointwallWall(new QueryExpress().add(QueryCriterions.eq(PointwallWallAppField.APPKEY, appkey)));

            if (wall == null) {

                mapMessage.put("errorMessage", "wall is.null");
                return new ModelAndView("/views/jsp/my/hotappwap", mapMessage);
            }


            String wallMoneyName = "迷豆";
            if ( wall.getWallMoneyName() != null) {

                wallMoneyName = wall.getWallMoneyName();
            }

            //加入平台货币名称
            mapMessage.put("wall", wall);
            mapMessage.put("wallMoneyName", wallMoneyName);

            if (!StringUtil.isEmpty(wall.getTemplate()) && wall.getTemplate().equals("3")) {
                mapMessage.put("template", wall.getTemplate());
                UserPoint userPoint = giftMarketWebLogic.getUserPoint(appkey, profile);
                int userHasPoint = userPoint == null ? 0 : userPoint.getUserPoint();
                mapMessage.put("userHasPoint", userHasPoint);

                String linkUrl = "/my/giftlist?profileid=" + profile.getProfileId() + "&appkey=" + appkey + "&type="+wall.getShopKey()+"&clientid=" + clientId + "&platform=" + platform;
                mapMessage.put("linkUrl", linkUrl);
            } else {

                mapMessage.put("template", "");
            }
            mapMessage.put("appkey", appkey);
            mapMessage.put("uno", uno);
            mapMessage.put("profileid", profile.getProfileId());

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/my/hotappwap", mapMessage);
        }

        return new ModelAndView("/views/jsp/my/hotappwap", mapMessage);
    }


}
