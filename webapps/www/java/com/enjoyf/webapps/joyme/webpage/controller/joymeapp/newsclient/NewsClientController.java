package com.enjoyf.webapps.joyme.webpage.controller.joymeapp.newsclient;

import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.JoymeAppHotdeployConfig;
import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.gameres.gamedb.GameDB;
import com.enjoyf.platform.service.gameres.gamedb.GameDBChannelInfo;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.joymeapp.JoymeAppClientConstant;
import com.enjoyf.platform.util.joymeapp.JoymeAppCommonParameterUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.JsonPagination;
import com.enjoyf.platform.webapps.common.ResultListMsg;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.platform.webapps.common.ResultPageMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.webapps.joyme.dto.joymeapp.ShakeGameDTO;
import com.enjoyf.webapps.joyme.dto.joymeapp.client.ClientTopMenuDTO;
import com.enjoyf.webapps.joyme.dto.joymeclient.*;
import com.enjoyf.webapps.joyme.weblogic.joymeapp.client.ClientWebLogic;
import com.enjoyf.webapps.joyme.webpage.controller.joymeapp.JoymeAppBaseController;
import com.mongodb.BasicDBObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-12-11 下午1:03
 * Description:
 */
@Controller
@RequestMapping(value = "/joymeapp/newsclient/")
public class NewsClientController extends JoymeAppBaseController {

    private static final String NEWS_CLIENT_INDEX = "newsclientidex_";
    private static final String NEWS_SHAKE_GAME = "shakegame_";
    private JoymeAppHotdeployConfig config = HotdeployConfigFactory.get().getConfig(JoymeAppHotdeployConfig.class);

    @Resource(name = "clientWebLogic")
    private ClientWebLogic clientWebLogic;

    @ResponseBody
    @RequestMapping(value = "/appinfo")
    public String appinfo(HttpServletRequest request, @RequestParam(value = "appkey", required = false) String appkey,
                          @RequestParam(value = "platform", required = false) String platform) {
        ResultObjectMsg resultMsg = new ResultObjectMsg(ResultPageMsg.CODE_S);
        try {
            String isOpen = config.getJoymeAppConfig().getShakeIsOpen();  //获得开关是否打开按钮

            Map<String, Object> returnMap = new HashMap<String, Object>();
            String version = request.getParameter("version");
            if (!StringUtil.isEmpty(version) && version.equals("1.4.3")) {
                returnMap.put("dfad_open", "true");
                returnMap.put("dfad_url", "http://ai.m.taobao.com");
                //海贼迷审核广告
            } else if (appkey.equals("0G30ZtEkZ4vFBhAfN7Bx4vI") && platform.equals("0") && "1.1.2".equals(version)) {
                returnMap.put("defad_open", "false");
                returnMap.put("defad_url", "");
            } else if (appkey.equals("0G30ZtEkZ4vFBhAfN7Bx4vI") && platform.equals("0") && "1.1.3".equals(version)) {
                returnMap.put("defad_open", "true");
                returnMap.put("defad_url", "http://ai.m.taobao.com");
            } else {
                returnMap.put("dfad_open", "false");
                returnMap.put("dfad_url", "");
            }

            returnMap.put("shake_open", isOpen);
            resultMsg.setResult(returnMap);
            resultMsg.setMsg("success");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            resultMsg.setRs(ResultListMsg.CODE_E);
            resultMsg.setMsg("system.error");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
    }

    @ResponseBody
    @RequestMapping(value = "shakewait")
    public String shakeWait(@RequestParam(value = "appkey", required = false) String appkey,
                            @RequestParam(value = "platform", required = false) String platform) {
        ResultObjectMsg resultMsg = new ResultObjectMsg(ResultPageMsg.CODE_S);
        Map<String, Object> returnMap = new HashMap<String, Object>();
        try {

            String gameText = config.getJoymeAppConfig().getGameText();

            List<GameDB> lists = GameResourceServiceSngl.get().queryGameDBByCache();
            if (!CollectionUtil.isEmpty(lists)) {
                List<String> list = new ArrayList<String>();
                for (GameDB gameDB : lists) {
                    list.add(gameDB.getGameName());
                }
                List returnList = RandomUtil.getRandomByList(list, 7);
                returnMap.put("game_list", returnList);
            }
            if (!StringUtil.isEmpty(gameText)) {
                String[] gameTexts = gameText.split("\\|");
                List<String> gameTextList = Arrays.asList(gameTexts);
                List returnList = RandomUtil.getRandomByList(gameTextList, 5);
                returnMap.put("info_text", returnList);
            }
            resultMsg.setResult(returnMap);
            resultMsg.setMsg("success");
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            resultMsg.setRs(ResultListMsg.CODE_E);
            resultMsg.setMsg("system.error");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        return JsonBinder.buildNormalBinder().toJson(resultMsg);
    }

    @ResponseBody
    @RequestMapping(value = "/shakegame")
    public String shakeGame(@RequestParam(value = "appkey", required = false) String appkey,
                            @RequestParam(value = "platform", required = false) String platform) {
        ResultObjectMsg resultMsg = new ResultObjectMsg(ResultPageMsg.CODE_S);
        try {
//            //查询出items (有缓存)
//            List<ClientLineItem> clientLineItems = JoymeAppServiceSngl.get().queryClientLineItemList(NEWS_SHAKE_GAME + platform);
//            if (CollectionUtil.isEmpty(clientLineItems)) {
//                resultMsg.setRs(ResultListMsg.CODE_E);
//                resultMsg.setMsg("clientlineitem.is.null");
//                return JsonBinder.buildNormalBinder().toJson(resultMsg);
//            }
//            //前5条
//            Set<Long> longs = new LinkedHashSet<Long>();
//            List<ClientLineItem> tempList = null;
//            if ("0".equals(platform)) {
//                //李旭的需求 取前五
//                tempList = new ArrayList<ClientLineItem>();
//                int i = 0;
//                for (ClientLineItem clientLineItem : clientLineItems) {
//                    if (i > 4) {
//                        break;
//                    }
//                    tempList.add(clientLineItem);
//                    i++;
//                }
//                Collections.shuffle(tempList);
//
//            } else {
//                tempList = RandomUtil.getRandomByList(clientLineItems, 5);
//            }
//            for (ClientLineItem clientLineItem : tempList) {
//                longs.add(Long.parseLong(clientLineItem.getDirectId()));
//            }
//            //随机取出5条
////            List<ClientLineItem> list = RandomUtil.getRandomByList(clientLineItems, 5);
////            Set<Long> longs = new HashSet<Long>();
////            for (ClientLineItem clientLineItem : list) {
////                longs.add(Long.parseLong(clientLineItem.getDirectId()));
////            }
//            Map<Long, GameDB> map = GameResourceServiceSngl.get().queryGameDBSet(longs);
////            List<GameDB> gameDbs = (List);
//            Collection<GameDB> gameDbs = map.values();
            List<ShakeGameDTO> shakeGameDTOs = new ArrayList<ShakeGameDTO>();
            Set<String> idSet = JoymeAppServiceSngl.get().getGameIdByWeight(NEWS_SHAKE_GAME + platform);
            if (CollectionUtil.isEmpty(idSet)) {
                resultMsg.setRs(ResultPageMsg.CODE_S);
                resultMsg.setResult(shakeGameDTOs);
                resultMsg.setMsg("success");
            }
            Set<Long> gameIdSet = new HashSet<Long>();
            for (String id : idSet) {
                gameIdSet.add(Long.valueOf(id));
            }
            if (CollectionUtil.isEmpty(gameIdSet)) {
                resultMsg.setRs(ResultPageMsg.CODE_S);
                resultMsg.setResult(shakeGameDTOs);
                resultMsg.setMsg("success");
            }
            Map<Long, GameDB> map = GameResourceServiceSngl.get().queryGameDBSet(gameIdSet);
            Collection<GameDB> gameDbs = map.values();
            for (GameDB gameDB : gameDbs) {
                if (gameDB == null) {
                    resultMsg.setRs(ResultPageMsg.CODE_S);
                    resultMsg.setResult(shakeGameDTOs);
                    resultMsg.setMsg("success");
                }
                ShakeGameDTO shakeGameDTO = new ShakeGameDTO();
                shakeGameDTO.setGame_name(gameDB.getGameName());
                shakeGameDTO.setIcon(gameDB.getGameIcon());
                shakeGameDTO.setRec_reason(gameDB.getRecommendReason());
                shakeGameDTO.setRec_reason2(gameDB.getRecommendReason2());
                shakeGameDTO.setReta(String.valueOf(gameDB.getGameRate()));
                shakeGameDTO.setGameid(gameDB.getGameDbId() + "");
                shakeGameDTO.setIos_url(gameDB.getIosDownload());
                shakeGameDTO.setAndroid_url(gameDB.getAndroidDownload());
                shakeGameDTOs.add(shakeGameDTO);
            }
            resultMsg.setResult(shakeGameDTOs);
            resultMsg.setMsg("success");
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            resultMsg.setRs(ResultListMsg.CODE_E);
            resultMsg.setMsg("system.error");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            resultMsg.setRs(ResultListMsg.CODE_E);
            resultMsg.setMsg("system.error");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        return JsonBinder.buildNormalBinder().toJson(resultMsg);
    }

    @ResponseBody
    @RequestMapping(value = "/home")
    public String item(@RequestParam(value = "pnum", required = false, defaultValue = "1") Integer page,
                       @RequestParam(value = "count", required = false, defaultValue = "10") Integer count,
                       @RequestParam(value = "appkey", required = false) String appkey,
                       @RequestParam(value = "platform", required = false) String platform,
                       @RequestParam(value = "channelid", required = false) String channelId,
                       @RequestParam(value = "flag", required = false) String flag,
                       HttpServletRequest request) {
        ResultPageMsg resultMsg = new ResultPageMsg(ResultPageMsg.CODE_S);
        String clientId = request.getParameter("client_id") == null ? request.getParameter("clientid") : request.getParameter("client_id");
        if (StringUtil.isEmpty(clientId)) {
            resultMsg.setRs(ResultListMsg.CODE_E);
            resultMsg.setMsg("param.is.null");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }

        String clientToken = request.getParameter("client_token") == null ? request.getParameter("clienttoken") : request.getParameter("client_token");
        if (StringUtil.isEmpty(clientToken)) {
            resultMsg.setRs(ResultListMsg.CODE_E);
            resultMsg.setMsg("param.is.null");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }


        if (StringUtil.isEmpty(appkey)) {
            resultMsg.setRs(ResultListMsg.CODE_E);
            resultMsg.setMsg("param.is.null");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        if (StringUtil.isEmpty(platform)) {
            resultMsg.setRs(ResultListMsg.CODE_E);
            resultMsg.setMsg("param.is.null");

        }

        if (StringUtil.isEmpty(channelId)) {
            resultMsg.setRs(ResultListMsg.CODE_E);
            resultMsg.setMsg("param.is.null");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }

        //joymapp公共参数
        JoymeAppClientConstant joymeAppClientConstant = JoymeAppCommonParameterUtil.geAppCommonParameter(request);

        try {
            Pagination pagination = new Pagination(count * page, page, count);
            PageRows<NewsItemsDTO> pageRows = clientWebLogic.queryNewsItems(NEWS_CLIENT_INDEX + platform, flag, pagination, joymeAppClientConstant);
            if (pageRows == null) {
                resultMsg.setMsg("client.line.item.is.null");
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }

            resultMsg.setResult(pageRows.getRows());
            resultMsg.setPage(new JsonPagination(pageRows.getPage()));
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            resultMsg.setRs(ResultListMsg.CODE_E);
            resultMsg.setMsg("system.error");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            resultMsg.setRs(ResultListMsg.CODE_E);
            resultMsg.setMsg("system.error");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }


        return JsonBinder.buildNormalBinder().toJson(resultMsg);
    }

    @ResponseBody
    @RequestMapping(value = "/headmenu")
    public String headMenu(HttpServletRequest request, HttpServletResponse response,
                           @RequestParam(value = "appkey", required = false) String appKey,
                           @RequestParam(value = "platform", required = false) String platform,
                           @RequestParam(value = "channelid", required = false) String channelCode,
                           @RequestParam(value = "querychannel", required = false, defaultValue = "false") Boolean queryChannel,
                           @RequestParam(value = "flag", required = false) String flag) {
        ResultObjectMsg resultMsg = new ResultObjectMsg(ResultObjectMsg.CODE_S);
        try {
            if (platform == null) {
                resultMsg.setRs(ResultObjectMsg.CODE_E);
                resultMsg.setMsg("param.is.null");
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }

            List<ClientTopMenuDTO> topMenuList = clientWebLogic.queryClientTopMenuList(appKey, AppTopMenuCategory.NEWS_CLIENT_LINE.getCode(), channelCode, Integer.parseInt(platform), flag);
            if (CollectionUtil.isEmpty(topMenuList)) {
                resultMsg.setMsg("headmenu.is.null");
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }
            resultMsg.setResult(topMenuList);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            resultMsg.setRs(ResultObjectMsg.CODE_E);
            resultMsg.setMsg("system.error");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        return JsonBinder.buildNormalBinder().toJson(resultMsg);
    }
}
