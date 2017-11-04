package com.enjoyf.webapps.joyme.webpage.controller.joymeapp.wikiapp.api;

import com.enjoyf.platform.service.IntValidStatus;
import com.enjoyf.platform.service.joymeapp.ClientLineItem;
import com.enjoyf.platform.service.joymeapp.JoymeAppServiceSngl;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.social.ObjectRelation;
import com.enjoyf.platform.service.social.ObjectRelationType;
import com.enjoyf.platform.service.social.SocialServiceSngl;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.http.AppUtil;
import com.enjoyf.platform.util.log.LoggerImplStream;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.webapps.joyme.dto.joymeapp.wikiapp.WikiAppTopDTO;
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
@RequestMapping(value = "/joymeapp/wikiapp/api/wikitop/")
public class WikiAppTopController extends AbstractWikiAppController {
    private static Logger logger = LoggerFactory.getLogger(WikiAppTopController.class);


    @ResponseBody
    @RequestMapping(value = "/list")
    public String report(HttpServletRequest request, HttpServletResponse response) {
        String appKey = HTTPUtil.getParam(request, "appkey");
        String uno = HTTPUtil.getParam(request, "uno");
        String platform = HTTPUtil.getParam(request, "platform");


        try {
            if (StringUtil.isEmpty(appKey) || StringUtil.isEmpty(uno) || StringUtil.isEmpty(platform)) {
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
            Profile profile = UserCenterServiceSngl.get().getProfileByUno(uno, authApp.getProfileKey());
            if (profile == null) {
                return ResultCodeConstants.LOGIN_JSON_PROFILE_IS_NULL.getJsonString();
            }

            List<ClientLineItem> clientLineItems = JoymeAppServiceSngl.get().queryClientLineItemList("wikitop_0");
            if (!CollectionUtil.isEmpty(clientLineItems)) {
                Collections.sort(clientLineItems, new Comparator() {
                    public int compare(Object o1, Object o2) {
                        if (Double.parseDouble(((ClientLineItem) o1).getRate()) < Double.parseDouble(((ClientLineItem) o2).getRate())) {
                            return 1;
                        }
                        return 0;
                    }
                });
                Set<String> wikiKeySet = new HashSet();
                List<WikiAppTopDTO> returnList = new ArrayList<WikiAppTopDTO>();
                for (ClientLineItem clientLineItem : clientLineItems) {
                    wikiKeySet.add(clientLineItem.getDirectId());
                    WikiAppTopDTO wikiAppTopDTO = new WikiAppTopDTO();
                    wikiAppTopDTO.setHotsum(clientLineItem.getRate());
                    wikiAppTopDTO.setJi(clientLineItem.getUrl());
                    wikiAppTopDTO.setPicurl(clientLineItem.getPicUrl());
                    wikiAppTopDTO.setJt("-2");
                    wikiAppTopDTO.setName(clientLineItem.getTitle());
                    wikiAppTopDTO.setCategory(clientLineItem.getCategory());
                    wikiAppTopDTO.setWikikey(clientLineItem.getDirectId());
                    wikiAppTopDTO.setFollow("0");
                    returnList.add(wikiAppTopDTO);
                }
                Map<String, ObjectRelation> map = SocialServiceSngl.get().checkObjectRelation(profile.getProfileId(), ObjectRelationType.WIKI, wikiKeySet);
                if (map != null) {
                    for (WikiAppTopDTO wikiAppTopDTO : returnList) {
                        ObjectRelation objectRelation = map.get(wikiAppTopDTO.getWikikey());
                        if (objectRelation != null && objectRelation.getStatus().equals(IntValidStatus.VALID)) {
                            wikiAppTopDTO.setFollow("1");
                        }
                    }
                }

                Map<String, Object> returnMap = new HashMap<String, Object>();
                returnMap.put("rows", returnList);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("rs", String.valueOf(ResultCodeConstants.SUCCESS.getCode()));
                jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
                jsonObject.put("result", returnMap);
                return jsonObject.toString();
            }
        } catch (ServiceException e) {
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }


        return "";
    }

    @ResponseBody
    @RequestMapping(value = "/getinfo")
    public String getInfo(HttpServletRequest request, HttpServletResponse response) {
        String wikikey = HTTPUtil.getParam(request, "wikikey");
        if (!StringUtil.isEmpty(wikikey)) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }
        //获取排行榜排名
//        JoymeAppServiceSngl.get().


        return null;
    }
}
