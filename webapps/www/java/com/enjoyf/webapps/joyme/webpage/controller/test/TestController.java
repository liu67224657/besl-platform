package com.enjoyf.webapps.joyme.webpage.controller.test;

import com.enjoyf.platform.db.gameres.GameDBHandler;
import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.gameres.gamedb.GameDB;
import com.enjoyf.platform.service.gameres.gamedb.GameDBChannelInfo;
import com.enjoyf.platform.service.gameres.gamedb.GamePlatformCode;
import com.enjoyf.platform.service.gameres.gamedb.GamePlatformInfo;
import com.enjoyf.platform.service.profile.ProfileServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;

import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.QRcodeGenerator;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.util.CookieUtil;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;

import com.google.common.base.Strings;
import com.mongodb.BasicDBObject;
import jofc2.model.Chart;
import jofc2.model.axis.Label;
import jofc2.model.axis.XAxis;
import jofc2.model.axis.YAxis;
import jofc2.model.elements.LineChart;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
@Controller
@RequestMapping(value = "/test")
public class TestController extends BaseRestSpringController {

    private static Logger logger = LoggerFactory.getLogger(TestController.class);

    @RequestMapping("/cookie/getbyclient")
    public ModelAndView getByClient(HttpServletRequest request,HttpServletResponse response) throws ServiceException {
        CookieUtil.setCookie(request, response, "cookie_key_wxdyhflag", "true", Integer.MAX_VALUE);
        CookieUtil.setCookie(request, response, "cookie_key_user_wxdyhflag", "ericliu", Integer.MAX_VALUE);
        ProfileServiceSngl.get().saveMobileCode("ericliu", "ericliu");

        return new ModelAndView("/views/jsp/cookie-getbyclient");
    }

    @RequestMapping("/cookie/createbyserv")
    public ModelAndView createByServ(HttpServletRequest request,HttpServletResponse response) throws ServiceException {
        String cookieValue= CookieUtil.getCookieValue(request, "test");

        Map map=new HashMap();
        map.put("cookievalue",cookieValue);

        String createbyserver= CookieUtil.getCookieValue(request, "createbyserver");
        map.put("createbyserver",createbyserver);


        return new ModelAndView("/views/jsp/cookie-createbyserv",map);
    }



    @ResponseBody
    @RequestMapping("/genQRcode")
    public String genQRcode(HttpServletResponse response) throws IOException {
        try {
            return String.valueOf(UserCenterServiceSngl.get().getUidPoolLength());
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName());
            return String.valueOf(-1l);
        }
    }

    @RequestMapping("/cookie/passport")
    public ModelAndView testPassport(HttpServletRequest request,
                                     HttpServletResponse response) throws ServiceException {

        GAlerter.lab(CookieUtil.getCookieValue(request, "JParam"));

        try {
            GAlerter.lab(URLDecoder.decode(CookieUtil.getCookieValue(request, "JParam"), "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView("/views/jsp/cookie-testbypassport");
    }

    @RequestMapping("/cookie/passportset")
    public ModelAndView setCookie(HttpServletRequest request,
                                  HttpServletResponse response) throws ServiceException {

        CookieUtil.setCookie(request, response, "JParam", "111111");
        return new ModelAndView("/views/jsp/cookie-testbypassport");
    }
}
