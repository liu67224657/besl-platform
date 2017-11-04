package com.enjoyf.webapps.joyme.webpage.controller.joymeapp;

import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.ResultListMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-20
 * Time: 下午1:45
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/joymeapp/api/report/")
public class JoymeAppReportController extends BaseRestSpringController {


    @ResponseBody
    @RequestMapping(value = "/install")
    public String reportInstall(HttpServletRequest request,
                                @RequestParam(value = "cid", required = false) String clientId,
                                @RequestParam(value = "appkey", required = false) String appkey,
                                @RequestParam(value = "platform", required = false) Integer platform,
                                @RequestParam(value = "version", required = false) String version,
                                @RequestParam(value = "channelid", required = false) String channelId,
                                @RequestParam(value = "device", required = false) String device,
                                @RequestParam(value = "itype", required = false) Integer installType,
                                @RequestParam(value = "screen", required = false) String screen,
                                @RequestParam(value = "osv", required = false) String osVersion,
                                @RequestParam(value = "date", required = false) Long installTime,
                                @RequestParam(value = "access_token", required = false) String access_token,
                                @RequestParam(value = "token_secr", required = false) String token_secr) {
        ResultListMsg resultMsg = new ResultListMsg(ResultListMsg.CODE_S);

        if (StringUtil.isEmpty(clientId) || StringUtil.isEmpty(appkey) || platform == null || installTime == null) {
            resultMsg.setRs(ResultListMsg.CODE_E);
            resultMsg.setMsg("param.is.empty");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }

        AppInstallInfo installInfo = new AppInstallInfo();
        installInfo.setClientId(clientId);
        installInfo.setAppKey(appkey);
        installInfo.setPlatform(platform);
        installInfo.setAppVersion(version);
        installInfo.setChannelid(channelId);
        installInfo.setDevice(device);
        installInfo.setInstallType(installType);
        installInfo.setScreen(screen);
        installInfo.setOsVersion(osVersion);
        installInfo.setInstallDate(new Date(installTime));
        installInfo.setAccess_token(access_token);
        installInfo.setToken_scr(token_secr);
        installInfo.setIp(getIp(request));
        try {
            JoymeAppServiceSngl.get().reportInstallInfo(installInfo);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }

        return JsonBinder.buildNormalBinder().toJson(resultMsg);
    }

    @ResponseBody
    @RequestMapping(value = "/pageview")
    public String reportPageView(HttpServletRequest request,
                                 @RequestParam(value = "cid", required = false) String clientId,
                                 @RequestParam(value = "appkey", required = false) String appkey,
                                 @RequestParam(value = "platform", required = false) Integer platform,
                                 @RequestParam(value = "channelid", required = false) String channelid,
                                 @RequestParam(value = "param", required = false) String param,//json数组（ltype，location，rtime，refer，refertype,createtime）,
                                 @RequestParam(value = "device", required = false) String device,
                                 @RequestParam(value = "screen", required = false) String screen,
                                 @RequestParam(value = "osv", required = false) String osVersion,
                                 @RequestParam(value = "access_token", required = false) String access_token,
                                 @RequestParam(value = "token_secr", required = false) String token_secr) {
        ResultListMsg resultMsg = new ResultListMsg(ResultListMsg.CODE_S);

        if (StringUtil.isEmpty(clientId) || StringUtil.isEmpty(appkey) || platform == null) {
            resultMsg.setRs(ResultListMsg.CODE_E);
            resultMsg.setMsg("param.is.empty");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }

        List<AppPageViewEntry> pageViewList = parseByJsonObj(param);
        if (CollectionUtil.isEmpty(pageViewList)) {
            resultMsg.setRs(ResultListMsg.CODE_E);
            resultMsg.setMsg("pageview.is.empty");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }

        AppPageViewInfo pageViewInfo = new AppPageViewInfo();
        pageViewInfo.setAppKey(appkey);
        pageViewInfo.setPlatform(platform);
        pageViewInfo.setChannelid(channelid);
        pageViewInfo.setPageViewList(pageViewList);
        pageViewInfo.setClientId(clientId);
        pageViewInfo.setDevice(device);
        pageViewInfo.setIp(getIp(request));
        pageViewInfo.setOsVersion(osVersion);
        pageViewInfo.setScreen(screen);
        pageViewInfo.setAccess_token(access_token);
        pageViewInfo.setToken_secr(token_secr);
        try {
            JoymeAppServiceSngl.get().reportPageViewInfo(pageViewInfo);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e;", e);
        }

        return JsonBinder.buildNormalBinder().toJson(resultMsg);
    }

    @ResponseBody
    @RequestMapping(value = "/errorlog")
    public String reportError(HttpServletRequest request,
                              @RequestParam(value = "cid", required = false) String clientId,
                              @RequestParam(value = "appkey", required = false) String appkey,
                              @RequestParam(value = "platform", required = false) Integer plaform,
                              @RequestParam(value = "version", required = false) String version,
                              @RequestParam(value = "errorinfo", required = false) String errorinfo,
                              @RequestParam(value = "errordate", required = false) Long errorDate,
                              @RequestParam(value = "device", required = false) String device,
                              @RequestParam(value = "screen", required = false) String screen,
                              @RequestParam(value = "osv", required = false) String osVersion,
                              @RequestParam(value = "access_token", required = false) String access_token,
                              @RequestParam(value = "token_secr", required = false) String token_secr) {
        ResultListMsg resultMsg = new ResultListMsg(ResultListMsg.CODE_S);

        if (StringUtil.isEmpty(clientId) || StringUtil.isEmpty(appkey) || plaform == null || errorDate == null) {
            resultMsg.setRs(ResultListMsg.CODE_E);
            resultMsg.setMsg("param.is.empty");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }

        AppErrorInfo errorInfo = new AppErrorInfo();
        errorInfo.setClientId(clientId);
        errorInfo.setAppKey(appkey);
        errorInfo.setPlatform(plaform);
        errorInfo.setAppVersion(version);
        errorInfo.setErrorInfo(errorinfo);
        errorInfo.setErrorDate(new Date(errorDate));
        errorInfo.setDevice(device);
        errorInfo.setIp(getIp(request));
        errorInfo.setOsVersion(osVersion);
        errorInfo.setScreen(screen);
        errorInfo.setOsVersion(osVersion);
        errorInfo.setAccess_token(access_token);
        errorInfo.setAccess_token(token_secr);
        try {
            JoymeAppServiceSngl.get().reportAppErrorInfo(errorInfo);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e;", e);
        }

        return JsonBinder.buildNormalBinder().toJson(resultMsg);
    }

    //ltype，url，rtime，refer，refertype,createtime
    private List<AppPageViewEntry> parseByJsonObj(String jsonObj) {
        List<AppPageViewEntry> returnObj = new ArrayList<AppPageViewEntry>();

        JSONArray jsonArray = (JSONArray) JSONValue.parse(jsonObj);
        if (jsonArray == null) {
            return null;
        }

        for (int i = 0; i < jsonArray.size(); i++) {
            AppPageViewEntry clientPageView = new AppPageViewEntry();

            JSONObject paramObj = (JSONObject) jsonArray.get(i);

            try {
                long createtime = Long.parseLong(String.valueOf(paramObj.get("createtime")));
                clientPageView.setCreateTime(new Date(createtime));
            } catch (NumberFormatException e) {
                continue;
            }

            String location = String.valueOf(paramObj.get("location"));
            String refer = String.valueOf(paramObj.get("refer"));


            try {
                int rtime = Integer.parseInt(String.valueOf(paramObj.get("rtime")));
                clientPageView.setRtime(rtime);
            } catch (NumberFormatException e) {
            }
            try {
                int urlType = Integer.parseInt(String.valueOf(paramObj.get("ltype")));
                clientPageView.setLocationtype(urlType);
            } catch (NumberFormatException e) {
            }
            try {
                int referType = Integer.parseInt(String.valueOf(paramObj.get("refertype")));
                clientPageView.setRefertype(referType);
            } catch (NumberFormatException e) {
            }
            clientPageView.setLocation(location);
            clientPageView.setRefer(refer);

            returnObj.add(clientPageView);
        }

        return returnObj;
    }

    public static void main(String[] args) {
        String s = "[{\"location\":\"%E9%A6%96%E9%A1%B5\",\"ltype\":1,\"rtime\":40,\"refer\":\"%E9%A6%96%E9%A1%B5\",\"refertype\":1,\"createtime\":1},{\"url\":\"%E9%A6%96%E9%A1%B5\",\"ltype\":2,\"rtime\":40,\"refer\":\"%E9%A6%96%E9%A1%B5\",\"refertype\":2,\"createtime\":1}]";

        System.out.println(new JoymeAppReportController().parseByJsonObj(s));
    }
}
