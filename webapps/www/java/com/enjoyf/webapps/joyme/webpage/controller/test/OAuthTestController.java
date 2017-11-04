package com.enjoyf.webapps.joyme.webpage.controller.test;

import com.enjoyf.platform.util.http.HttpClientManager;
import com.enjoyf.platform.util.http.HttpParameter;
import com.enjoyf.platform.util.http.HttpResult;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 13-9-3
 * Time: 下午10:10
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/oautht")
public class OAuthTestController {

    @RequestMapping(value = "/360/authorize")
    public ModelAndView redirect() {

        String s = "https://openapi.360.cn/oauth2/authorize?" +
                "client_id=7ba3c645c14e9e2012a14cb1b3e3138b" +
                "&response_type=code&" +
                "redirect_uri=http://www.joyme.test/oautht/360/gettoken&scope=basic&display=default";
        return new ModelAndView("redirect:" + s);
    }

    @RequestMapping(value = "/360/gettoken")
    public ModelAndView getAccessToken(@RequestParam(value = "code", required = true) String code) {

        HttpParameter[] params = {
                new HttpParameter("grant_type", "authorization_code"),
                new HttpParameter("code", code),
                new HttpParameter("client_id", "7ba3c645c14e9e2012a14cb1b3e3138b"),
                new HttpParameter("client_secret", "833f9dca2459d377844e777d2adf431a"),
                new HttpParameter("redirect_uri", "http://www.joyme.test/oautht/360/gettoken"),
        };

        HttpResult result = new HttpClientManager().post("https://openapi.360.cn/oauth2/access_token", params, null);

        System.out.println(result);

        Map<String, Object> mapMessage = new HashMap<String, Object>();

        JSONObject object = (JSONObject)JSONValue.parse(result.getResult());
        String at = object.get("access_token").toString();
        String rt = object.get("refresh_token").toString();

        mapMessage.put("at", at);
        mapMessage.put("rt", rt);
        mapMessage.put("result", result.getResult());
        return new ModelAndView("/views/jsp/test/360ac", mapMessage);
    }

    @RequestMapping(value = "/360/refreshtoken")
    public ModelAndView refreshToken(@RequestParam(value = "rt", required = true) String refreshToken) {

        HttpParameter[] params = {
                new HttpParameter("grant_type", "refresh_token"),
                new HttpParameter("refresh_token", refreshToken),
                new HttpParameter("client_id", "7ba3c645c14e9e2012a14cb1b3e3138b"),
                new HttpParameter("client_secret", "833f9dca2459d377844e777d2adf431a"),
                new HttpParameter("scope", "basic"),
                new HttpParameter("redirect_uri", "http://www.joyme.test/oauth/360/gettoken"),
        };

        HttpResult result = new HttpClientManager().post("https://openapi.360.cn/oauth2/access_token", params, null);

        System.out.println(result);

        Map<String, Object> mapMessage = new HashMap<String, Object>();

        JSONObject object = (JSONObject)JSONValue.parse(result.getResult());
        String at = object.get("access_token").toString();
        String rt = object.get("refresh_token").toString();

        mapMessage.put("at", at);
        mapMessage.put("rt", rt);
        mapMessage.put("result", result.getResult());
        return new ModelAndView("/views/jsp/test/360rt", mapMessage);
    }

}
