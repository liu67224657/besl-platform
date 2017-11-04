package com.enjoyf.webapps.joyme.webpage.controller.joymeapp.client.api;

import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.joymeappconfig.JoymeAppConfigServiceSngl;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.webapps.joyme.dto.joymeapp.client.*;
import com.enjoyf.webapps.joyme.weblogic.joymeapp.client.ClientWebLogic;
import com.enjoyf.webapps.joyme.webpage.controller.joymeapp.client.AbstractClientBaseController;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 15-3-2
 * Time: 下午15:17
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/joymeapp/client/api/menu")
public class ClientApiMenuController extends AbstractClientBaseController {

    @Resource(name = "clientWebLogic")
    private ClientWebLogic clientWebLogic;

    /**
     * 二级菜单列表
     *
     * @param request
     * @param response
     * @param pid
     * @param flag
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/sublist")
    public String subList(HttpServletRequest request, HttpServletResponse response,
                          @RequestParam(value = "menuid", required = false, defaultValue = "O") String pid,
                          @RequestParam(value = "flag", required = false) String flag) {
        String appKey = HTTPUtil.getParam(request, "appkey");
        if (StringUtil.isEmpty(appKey)) {
            return ResultCodeConstants.MINI_CLIENT_PARAM_APPKEY_NULL.getJsonString();
        }
        appKey = getAppKey(appKey);
        String platformStr = HTTPUtil.getParam(request, "platform");
        if (StringUtil.isEmpty(platformStr)) {
            return ResultCodeConstants.MINI_CLIENT_PARAM_PLATFORM_NULL.getJsonString();
        }
        int platform = Integer.valueOf(platformStr);
        try {
            Long parentId = Long.valueOf(pid);
            List<JoymeAppMenu> list = JoymeAppConfigServiceSngl.get().queryJoymeAppMenuByCache(appKey, parentId, null);
            if (!CollectionUtil.isEmpty(list)) {
                List<ClientModuleDataDTO> result = new ArrayList<ClientModuleDataDTO>();
                for (JoymeAppMenu menu : list) {
                    ClientModuleDataDTO moduleDataDTO = clientWebLogic.buildClientModuleDataDTO(menu, platform);
                    if (moduleDataDTO != null) {
                        result.add(moduleDataDTO);
                    }
                }
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("rs", ResultCodeConstants.SUCCESS.getCode());
                jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
                jsonObject.put("result", result);
                return jsonObject.toString();
            } else {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("rs", ResultCodeConstants.SUCCESS.getCode());
                jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
                jsonObject.put("result", new JSONObject());
                return jsonObject.toString();
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e:", e);
            return ResultCodeConstants.ERROR.getJsonString();
        }
    }


    @ResponseBody
    @RequestMapping(value = "/get")
    public String get(HttpServletRequest request, HttpServletResponse response,
                      @RequestParam(value = "mid", required = false, defaultValue = "O") String mid,
                      @RequestParam(value = "flag", required = false) String flag) {
        String appKey = HTTPUtil.getParam(request, "appkey");
        if (StringUtil.isEmpty(appKey)) {
            return ResultCodeConstants.MINI_CLIENT_PARAM_APPKEY_NULL.getJsonString();
        }
        appKey = getAppKey(appKey);
        String platform = HTTPUtil.getParam(request, "platform");
        if (StringUtil.isEmpty(platform)) {
            return ResultCodeConstants.MINI_CLIENT_PARAM_PLATFORM_NULL.getJsonString();
        }
        return ResultCodeConstants.SUCCESS.getJsonString();
    }

    @ResponseBody
    @RequestMapping(value = "/getbytag")
    public String getByTag(HttpServletRequest request, HttpServletResponse response,
                           @RequestParam(value = "tid", required = false, defaultValue = "O") String tid,
                           @RequestParam(value = "flag", required = false) String flag) {
        String appKey = HTTPUtil.getParam(request, "appkey");
        if (StringUtil.isEmpty(appKey)) {
            return ResultCodeConstants.MINI_CLIENT_PARAM_APPKEY_NULL.getJsonString();
        }
        appKey = getAppKey(appKey);
        String platform = HTTPUtil.getParam(request, "platform");
        if (StringUtil.isEmpty(platform)) {
            return ResultCodeConstants.MINI_CLIENT_PARAM_PLATFORM_NULL.getJsonString();
        }
        return ResultCodeConstants.SUCCESS.getJsonString();
    }
}
