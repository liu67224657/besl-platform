package com.enjoyf.webapps.joyme.webpage.controller.joymeapp;

import com.enjoyf.platform.service.joymeapp.JoymeAppMenu;
import com.enjoyf.platform.service.joymeapp.JoymeAppMenuModuleType;
import com.enjoyf.platform.service.joymeapp.JoymeAppServiceSngl;
import com.enjoyf.platform.service.joymeapp.JoymeAppTopNews;
import com.enjoyf.platform.service.joymeappconfig.JoymeAppConfigServiceSngl;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.ResultListMsg;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.webapps.joyme.dto.joymeapp.*;
import com.enjoyf.webapps.joyme.weblogic.joymeapp.JoymeAppWebLogic;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-20
 * Time: 下午1:45
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/joymeapp/menu")
public class JoymeAppMenuController extends JoymeAppBaseController {

    @Resource(name = "joymeAppWebLogic")
    private JoymeAppWebLogic joymeAppWebLogic;

    @ResponseBody
    @RequestMapping(value = "/getbmenu")
    public String getButtomMenu(HttpServletRequest request,
                                @RequestParam(value = "appkey", required = false) String appkey,
                                @RequestParam(value = "pid", required = false, defaultValue = "0") Long pid) {
        ResultObjectMsg resultMsg = null;
        try {
            resultMsg = new ResultObjectMsg(ResultListMsg.CODE_S);

            if (StringUtil.isEmpty(appkey)) {
                resultMsg.setRs(ResultObjectMsg.CODE_E);
                resultMsg.setMsg("param.is.empty");
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }

            List<JoymeAppBottomMenuDTO> bottomMenuDTOList = joymeAppWebLogic.queryBottomMenuListByAppKey(appkey, pid);
            List<JoymeAppMenuTagDTO> menuTags = joymeAppWebLogic.queryMenuTags(pid);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("rs", ResultObjectMsg.CODE_S);
            jsonObject.put("result", bottomMenuDTOList);
            jsonObject.put("tags", menuTags);
            jsonObject.put("msg", "");

            return jsonObject.toString();
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            resultMsg.setMsg("system.error");
        }
        return JsonBinder.buildNormalBinder().toJson(resultMsg);
    }

    @ResponseBody
    @RequestMapping(value = "/getmenubytag")
    public String getMenuByTagid(HttpServletRequest request,
                                 @RequestParam(value = "appkey", required = false) String appkey,
                                 @RequestParam(value = "tid", required = false, defaultValue = "0") Long tid) {
        ResultObjectMsg resultMsg = null;
        try {
            resultMsg = new ResultObjectMsg(ResultListMsg.CODE_S);

            if (StringUtil.isEmpty(appkey)) {
                resultMsg.setRs(ResultObjectMsg.CODE_E);
                resultMsg.setMsg("param.is.empty");
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }

            List<JoymeAppBottomMenuDTO> bottomMenuDTOList = joymeAppWebLogic.queryBottomMenuListByTagid(tid);

            resultMsg.setResult(bottomMenuDTOList);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            resultMsg.setMsg("system.error");
        }
        return JsonBinder.buildNormalBinder().toJson(resultMsg);
    }

    @ResponseBody
    @RequestMapping(value = "/getbypid")
    public String getMenuByPid(HttpServletRequest request,
                               @RequestParam(value = "appkey", required = false) String appkey,
                               @RequestParam(value = "pid", required = false, defaultValue = "0") Long pid) {
        ResultObjectMsg resultMsg = null;
        return JsonBinder.buildNormalBinder().toJson(resultMsg);
    }


    @ResponseBody
    @RequestMapping(value = "/gettopmenu")
    public String getTopMenu(HttpServletRequest request,
                             @RequestParam(value = "appkey", required = false) String appkey,
                             @RequestParam(value = "channelid", required = false) String channelid
    ) {
        ResultObjectMsg resultMsg = null;
        try {
            resultMsg = new ResultObjectMsg(ResultListMsg.CODE_S);

            if (StringUtil.isEmpty(appkey)) {
                resultMsg.setRs(ResultObjectMsg.CODE_E);
                resultMsg.setMsg("param.is.empty");
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }


            List<JoymeAppTopMenuDTO> topMenuList = joymeAppWebLogic.queryTopMenuListByAppKey(appkey, channelid);

            appkey = getAppKey(appkey);
            List<JoymeAppTopNews> returnNews = JoymeAppConfigServiceSngl.get().queryJoymeAppTopNewsByAppKey(appkey);

            List<JoymeAppTopNewsDTO> rollingnews = buildJoymeAppTopNewsDTO(returnNews);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("rs", ResultObjectMsg.CODE_S);
            jsonObject.put("result", topMenuList);
            jsonObject.put("rollingnews", rollingnews);
            jsonObject.put("msg", "");

            return jsonObject.toString();
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            resultMsg.setMsg("system.error");
        }
        return JsonBinder.buildNormalBinder().toJson(resultMsg);
    }

    @ResponseBody
    @RequestMapping(value = "/getcategorymenu")
    public String getCategoryMenu(HttpServletRequest request,
                                  @RequestParam(value = "appkey", required = false) String appkey) {
        ResultObjectMsg resultMsg = null;
        try {
            resultMsg = new ResultObjectMsg(ResultListMsg.CODE_S);

            if (StringUtil.isEmpty(appkey)) {
                resultMsg.setRs(ResultObjectMsg.CODE_E);
                resultMsg.setMsg("param.is.empty");
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }
            appkey = getAppKey(appkey);
            List<JoymeAppMenu> menuList = JoymeAppConfigServiceSngl.get().queryJoymeAppMenuByCache(appkey, 0l, null);
            AppMenuDTO returnDTO = new AppMenuDTO();
            if (!CollectionUtil.isEmpty(menuList)) {
                for (JoymeAppMenu menu : menuList) {
                    if (menu != null) {
                        if (JoymeAppMenuModuleType.TOP_ROLLING.equals(menu.getModuleType())) {
                            AppCategoryMenuDTO topMenu = new AppCategoryMenuDTO();
                            List<JoymeAppBottomMenuDTO> topList = joymeAppWebLogic.queryBottomMenuListByAppKey(appkey, menu.getMenuId());
                            List<JoymeAppMenuTagDTO> topTags = joymeAppWebLogic.queryMenuTags(menu.getMenuId());
                            topMenu.setMenuList(topList);
                            topMenu.setTagList(topTags);
                            returnDTO.setTop(topMenu);
                        } else if (JoymeAppMenuModuleType.TOP_BELOW_ROLLING.equals(menu.getModuleType())) {
                            AppCategoryMenuDTO topBelowMenu = new AppCategoryMenuDTO();
                            List<JoymeAppBottomMenuDTO> topBelowList = joymeAppWebLogic.queryBottomMenuListByAppKey(appkey, menu.getMenuId());
                            List<JoymeAppMenuTagDTO> topBelowTags = joymeAppWebLogic.queryMenuTags(menu.getMenuId());
                            topBelowMenu.setMenuList(topBelowList);
                            topBelowMenu.setTagList(topBelowTags);
                            returnDTO.setTopBelow(topBelowMenu);
                        } else if (JoymeAppMenuModuleType.MIDDLE_ROLLING.equals(menu.getModuleType())) {
                            AppCategoryMenuDTO middleMenu = new AppCategoryMenuDTO();
                            List<JoymeAppBottomMenuDTO> middleList = joymeAppWebLogic.queryBottomMenuListByAppKey(appkey, menu.getMenuId());
                            List<JoymeAppMenuTagDTO> middleTags = joymeAppWebLogic.queryMenuTags(menu.getMenuId());
                            middleMenu.setMenuList(middleList);
                            middleMenu.setTagList(middleTags);
                            returnDTO.setMiddle(middleMenu);
                        } else if (JoymeAppMenuModuleType.HOT_RECOMMENDED.equals(menu.getModuleType())) {
                            AppCategoryMenuDTO hotrecommendedMenu = new AppCategoryMenuDTO();
                            List<JoymeAppBottomMenuDTO> hotList = joymeAppWebLogic.queryBottomMenuListByAppKey(appkey, menu.getMenuId());
                            List<JoymeAppMenuTagDTO> hotTags = joymeAppWebLogic.queryMenuTags(menu.getMenuId());
                            hotrecommendedMenu.setMenuList(hotList);
                            hotrecommendedMenu.setTagList(hotTags);
                            returnDTO.setHotrecommend(hotrecommendedMenu);
                        }
                    }
                }
            }
            resultMsg.setResult(returnDTO);
            resultMsg.setMsg("success");
            resultMsg.setRs(ResultListMsg.CODE_S);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            resultMsg.setMsg("system.error");
        }
        return JsonBinder.buildNormalBinder().toJson(resultMsg);
    }

    @ResponseBody
    @RequestMapping(value = "/getgeneral")
    public String getGeneralMenu(HttpServletRequest request,
                                 @RequestParam(value = "appkey", required = false) String appkey) {
        ResultObjectMsg resultMsg = null;
        try {
            resultMsg = new ResultObjectMsg(ResultListMsg.CODE_S);

            if (StringUtil.isEmpty(appkey)) {
                resultMsg.setRs(ResultObjectMsg.CODE_E);
                resultMsg.setMsg("param.appkey.empty");
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }

            AppGeneralMenuDTO leftDTO = new AppGeneralMenuDTO();
            List<JoymeAppBottomMenuDTO> leftList = joymeAppWebLogic.queryBottomMenuByModule(appkey, JoymeAppMenuModuleType.LEFT_ONE);
            if (!CollectionUtil.isEmpty(leftList)) {
                leftDTO.setMenuList(leftList);
            }

            AppGeneralMenuDTO rightDTO = new AppGeneralMenuDTO();
            List<JoymeAppBottomMenuDTO> rightList = joymeAppWebLogic.queryBottomMenuByModule(appkey, JoymeAppMenuModuleType.RIGHT_FOUR);
            if (!CollectionUtil.isEmpty(rightList)) {
                rightDTO.setMenuList(rightList);
            }

            AppGeneralMenuDTO bottomDTO = new AppGeneralMenuDTO();
            List<JoymeAppBottomMenuDTO> bottomList = joymeAppWebLogic.queryBottomMenuByModule(appkey, JoymeAppMenuModuleType.BOTTOM_LIST);
            if (!CollectionUtil.isEmpty(bottomList)) {
                bottomDTO.setMenuList(bottomList);
            }

            Map<String, AppGeneralMenuDTO> map = new HashMap<String, AppGeneralMenuDTO>();
            map.put("left", leftDTO);
            map.put("right", rightDTO);
            map.put("bottom", bottomDTO);

            resultMsg.setResult(map);
            resultMsg.setMsg("success");
            resultMsg.setRs(ResultListMsg.CODE_S);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            resultMsg.setRs(ResultObjectMsg.CODE_E);
            resultMsg.setMsg("system.error");
        }
        return JsonBinder.buildNormalBinder().toJson(resultMsg);
    }

    private List<JoymeAppTopNewsDTO> buildJoymeAppTopNewsDTO(List<JoymeAppTopNews> rollingnews) {
        List<JoymeAppTopNewsDTO> returnNews = new ArrayList<JoymeAppTopNewsDTO>();
        for (JoymeAppTopNews joymeAppTopNews : rollingnews) {
            JoymeAppTopNewsDTO dto = new JoymeAppTopNewsDTO();
            dto.setTitle(joymeAppTopNews.getTitle());
            dto.setUrl(joymeAppTopNews.getUrl());
            returnNews.add(dto);
        }
        return returnNews;
    }
}
