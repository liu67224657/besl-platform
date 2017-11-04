package com.enjoyf.webapps.joyme.webpage.controller.joymeapp.wikiapp.api;

import com.enjoyf.platform.service.event.EventDispatchServiceSngl;
import com.enjoyf.platform.service.event.system.ActivityUserEvent;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.service.usercenter.UserCenterUtil;
import com.enjoyf.platform.service.usercenter.activityuser.ActivityActionType;
import com.enjoyf.platform.service.usercenter.activityuser.ActivityObjectType;
import com.enjoyf.platform.service.usercenter.activityuser.ActivityProfile;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.http.AppUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.JsonPagination;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.webapps.joyme.dto.joymeapp.wikiapp.ActivityUserDTO;
import com.enjoyf.webapps.joyme.webpage.controller.joymeapp.wikiapp.AbstractWikiAppController;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by zhitaoshi on 2015/4/7.
 */
@Controller
@RequestMapping(value = "/joymeapp/wikiapp/api/activityuser")
public class WikiAppApiActivityUserController extends AbstractWikiAppController {
    private static Logger logger = LoggerFactory.getLogger(WikiAppApiActivityUserController.class);

    /**
     * 活跃用户上报接口
     *
     * @param request  必传参数 appkey-公共参数 wikikey-WIKI的key(mt lt) atype-动作类型int值 uno-公共参数 otype-对象类型int值 destid-对象的ID
     * @param response
     * @return PARAM_EMPTY SUCCESS
     */
    @ResponseBody
    @RequestMapping(value = "/report")
    public String report(HttpServletRequest request, HttpServletResponse response) {
        String appKey = HTTPUtil.getParam(request, "appkey");
        String subKey = HTTPUtil.getParam(request, "wikikey");
        String atype = HTTPUtil.getParam(request, "atype");
        String uno = HTTPUtil.getParam(request, "uno");
        String otype = HTTPUtil.getParam(request, "otype");
        String destid = HTTPUtil.getParam(request, "destid");

        if (StringUtil.isEmpty(appKey) || StringUtil.isEmpty(subKey)
                || StringUtil.isEmpty(atype) || StringUtil.isEmpty(uno) || StringUtil.isEmpty(otype) | StringUtil.isEmpty(destid)) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        appKey = AppUtil.getAppKey(appKey);

        AuthApp authApp = null;
        try {
            authApp = OAuthServiceSngl.get().getApp(appKey);
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        if (authApp == null) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        ActivityActionType actionType = null;
        try {
            actionType = ActivityActionType.getByCode(Integer.parseInt(atype));
        } catch (Exception e) {
            logger.error("actionType error.e:" + e.getMessage() + " atype:" + atype);
        }

        ActivityObjectType objectType = null;
        try {
            objectType = ActivityObjectType.getByCode(Integer.parseInt(otype));
        } catch (Exception e) {
            logger.error("actionType error.e:" + e.getMessage() + " otype:" + otype);
        }

        if (actionType == null || objectType == null) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }


        try {
            ActivityUserEvent event = new ActivityUserEvent();
            event.setActionTime(new Date());
            event.setActionType(actionType);
            event.setSubkey(subKey);
            event.setAppkey(appKey);
            event.setProfileId(UserCenterUtil.getProfileId(uno, authApp.getProfileKey()));
            event.setUno(uno);
            event.setObjectType(objectType);
            event.setObjectId(destid);

            EventDispatchServiceSngl.get().dispatch(event);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }

        return ResultCodeConstants.SUCCESS.getJsonString();
    }


    @ResponseBody
    @RequestMapping(value = "/list")
    public String list(HttpServletRequest request, HttpServletResponse response) {
        String appKey = HTTPUtil.getParam(request, "appkey");
        String subKey = HTTPUtil.getParam(request, "wikikey");

        if (StringUtil.isEmpty(appKey) || StringUtil.isEmpty(subKey)) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        Pagination page = getPaginationbyRequest(request);

        appKey = AppUtil.getAppKey(appKey);


        try {
            PageRows<ActivityProfile> pageRows = UserCenterServiceSngl.get().queryActivityProfile(appKey, subKey, page);

            List<ActivityUserDTO> list = new ArrayList<ActivityUserDTO>();
            for (ActivityProfile aProfile : pageRows.getRows()) {
                ActivityUserDTO dto = new ActivityUserDTO();
                dto.setIconurl(aProfile.getIconurl());
                dto.setDesc(aProfile.getDesc());
                dto.setNick(aProfile.getNick());
                dto.setActiontime(aProfile.getActivityUser().getActionTime().getTime());
                dto.setActiontype(aProfile.getActivityUser().getActionType().getCode());
                dto.setObjectid(aProfile.getActivityUser().getObjectId());
                dto.setObjecttype(aProfile.getActivityUser().getObjectType().getCode());
                dto.setProfileid(aProfile.getActivityUser().getProfileId());
                dto.setUno(aProfile.getActivityUser().getUno());
                list.add(dto);
            }
            JSONObject jsonObject = new JSONObject();
            Map resultMap = new HashMap();
            resultMap.put("rows", list);
            resultMap.put("page", new JsonPagination(pageRows.getPage()));
            jsonObject.put("rs", String.valueOf(ResultCodeConstants.SUCCESS.getCode()));
            jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
            jsonObject.put("result", resultMap);
            return jsonObject.toString();

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
    }


    @ResponseBody
    @RequestMapping(value = "/sum")
    public String getActivityUserSum(HttpServletRequest request, HttpServletResponse response) {
        String appKey = HTTPUtil.getParam(request, "appkey");
        String subKey = HTTPUtil.getParam(request, "wikikey");

        if (StringUtil.isEmpty(appKey) || StringUtil.isEmpty(subKey)) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        Pagination page = getPaginationbyRequest(request);

        appKey = AppUtil.getAppKey(appKey);


        try {
            int actvitiyUserSum = UserCenterServiceSngl.get().getActvitiyUserSum(appKey, subKey);

            JSONObject jsonObject = new JSONObject();
            Map map = new HashMap();
            map.put("usersum", actvitiyUserSum);
            jsonObject.put("rs", "1");
            jsonObject.put("result", map);
            return jsonObject.toString();
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
    }
}
