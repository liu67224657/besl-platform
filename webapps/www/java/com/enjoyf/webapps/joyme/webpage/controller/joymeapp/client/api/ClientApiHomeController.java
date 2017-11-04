package com.enjoyf.webapps.joyme.webpage.controller.joymeapp.client.api;

import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.joymeappconfig.JoymeAppConfigServiceSngl;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.*;
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
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 15-3-2
 * Time: 下午15:17
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/joymeapp/client/api/home")
public class ClientApiHomeController extends AbstractClientBaseController {

    @Resource(name = "clientWebLogic")
    private ClientWebLogic clientWebLogic;
    /**
     * 首页
     */
    @ResponseBody
    @RequestMapping
    public String home(HttpServletRequest request, HttpServletResponse response,
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
            List<JoymeAppMenu> menuList = JoymeAppConfigServiceSngl.get().queryJoymeAppMenuByCache(appKey, 0l, null);
            if(!CollectionUtil.isEmpty(menuList)){
                ClientHomeDTO homeDTO = new ClientHomeDTO();
                for(JoymeAppMenu menu:menuList){
                    if(menu != null){
                        if(JoymeAppMenuModuleType.HEADLIST.equals(menu.getModuleType())){
                            List<JoymeAppMenu> headList = JoymeAppConfigServiceSngl.get().queryJoymeAppMenuByCache(appKey, menu.getMenuId(), JoymeAppMenuModuleType.HEADLIST);
                            if(!CollectionUtil.isEmpty(headList)){
                                ClientModuleDTO headListDTO = clientWebLogic.buildClientModuleDTO(menu, headList, platform);
                                if(headListDTO != null){
                                    homeDTO.setHeadlist(headListDTO);
                                }
                            }
                        }else if(JoymeAppMenuModuleType.NEWSLIST.equals(menu.getModuleType())){
                            List<JoymeAppMenu> newsList = JoymeAppConfigServiceSngl.get().queryJoymeAppMenuByCache(appKey, menu.getMenuId(), JoymeAppMenuModuleType.NEWSLIST);
                            if(!CollectionUtil.isEmpty(newsList)){
                                NewsModuleDTO newsListDTO = clientWebLogic.buildNewsModuleDTO(menu, newsList);
                                if(newsListDTO != null){
                                    homeDTO.setNewslist(newsListDTO);
                                }
                            }
                        }else if(JoymeAppMenuModuleType.OTHERINFO.equals(menu.getModuleType())){
                            List<JoymeAppMenu> otherList = JoymeAppConfigServiceSngl.get().queryJoymeAppMenuByCache(appKey, menu.getMenuId(), JoymeAppMenuModuleType.OTHERINFO);
                            if(!CollectionUtil.isEmpty(otherList)){
                                List<Map<String, OtherInfoModuleDateDTO>> otherInfo = new ArrayList<Map<String, OtherInfoModuleDateDTO>>();
                                for(JoymeAppMenu menuData:otherList){
                                    Map<String, OtherInfoModuleDateDTO> map = clientWebLogic.buildOtherModuleDataDTOMap(menuData);
                                    if(map != null){
                                        otherInfo.add(map);
                                    }
                                }
                                if(!CollectionUtil.isEmpty(otherInfo)){
                                    homeDTO.setOtherinfo(otherInfo);
                                }
                            }
                        }else if(JoymeAppMenuModuleType.MODULE1.equals(menu.getModuleType())){
                            List<JoymeAppMenu> module1List = JoymeAppConfigServiceSngl.get().queryJoymeAppMenuByCache(appKey, menu.getMenuId(), JoymeAppMenuModuleType.MODULE1);
                            if(!CollectionUtil.isEmpty(module1List)){
                                ClientModuleDTO module1ListDTO = clientWebLogic.buildClientModuleDTO(menu, module1List, platform);
                                if(module1ListDTO != null){
                                    homeDTO.setModule1(module1ListDTO);
                                }
                            }
                        }else if(JoymeAppMenuModuleType.MODULE2.equals(menu.getModuleType())){
                            List<JoymeAppMenu> module2List = JoymeAppConfigServiceSngl.get().queryJoymeAppMenuByCache(appKey, menu.getMenuId(), JoymeAppMenuModuleType.MODULE2);
                            if(!CollectionUtil.isEmpty(module2List)){
                                ClientModuleDTO module2ListDTO = clientWebLogic.buildClientModuleDTO(menu, module2List, platform);
                                if(module2ListDTO != null){
                                    homeDTO.setModule2(module2ListDTO);
                                }
                            }
                        }else if(JoymeAppMenuModuleType.MODULE3.equals(menu.getModuleType())){
                            List<JoymeAppMenu> module3List = JoymeAppConfigServiceSngl.get().queryJoymeAppMenuByCache(appKey, menu.getMenuId(), JoymeAppMenuModuleType.MODULE3);
                            if(!CollectionUtil.isEmpty(module3List)){
                                ClientModuleDTO module3ListDTO = clientWebLogic.buildClientModuleDTO(menu, module3List, platform);
                                if(module3ListDTO != null){
                                    homeDTO.setModule3(module3ListDTO);
                                }
                            }
                        }
                    }
                }
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("rs", ResultCodeConstants.SUCCESS.getCode());
                jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
                jsonObject.put("result", homeDTO);
                return jsonObject.toString();
            }else {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("rs", ResultCodeConstants.SUCCESS.getCode());
                jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
                jsonObject.put("result", new JSONObject());
                return jsonObject.toString();
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
            return ResultCodeConstants.ERROR.getJsonString();
        }
    }

}
