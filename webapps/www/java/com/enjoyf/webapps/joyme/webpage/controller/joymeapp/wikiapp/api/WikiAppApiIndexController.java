package com.enjoyf.webapps.joyme.webpage.controller.joymeapp.wikiapp.api;

import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.social.ObjectRelationType;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QuerySort;
import com.enjoyf.platform.util.sql.QuerySortOrder;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.webapps.joyme.dto.joymeapp.wikiapp.WikiAppIndexDTO;
import com.enjoyf.webapps.joyme.dto.joymeapp.wikiapp.WikiAppIndexItemDTO;
import com.enjoyf.webapps.joyme.dto.joymeapp.wikiapp.WikiAppIndexModuleDTO;
import com.enjoyf.webapps.joyme.weblogic.joymeapp.wikiapp.WikiAppWebLogic;
import com.enjoyf.webapps.joyme.webpage.controller.joymeapp.wikiapp.AbstractWikiAppController;
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
 * Created by zhitaoshi on 2015/4/7.
 */
@Controller
@RequestMapping(value = "/joymeapp/wikiapp/api/index")
public class WikiAppApiIndexController extends AbstractWikiAppController {

    @Resource(name = "wikiAppWebLogic")
    private WikiAppWebLogic wikiAppWebLogic;

    private static final String HOT_WIKI_LINE_CODE = "wikiapp_hot_wiki_";

    @ResponseBody
    @RequestMapping
    public String index(HttpServletRequest request, HttpServletResponse response) {
        String appKey = HTTPUtil.getParam(request, "appkey");
        String uidParam = HTTPUtil.getParam(request, "uid");
        String platformParam = HTTPUtil.getParam(request, "platform");
        if (StringUtil.isEmpty(appKey) || StringUtil.isEmpty(uidParam) || StringUtil.isEmpty(platformParam)) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        long uid = -1l;
        try {
            uid = Long.parseLong(uidParam);
        } catch (NumberFormatException e) {
        }
        if (uid == -1l) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }
        int platform = -1;
        try {
            platform = Integer.parseInt(platformParam);
        } catch (NumberFormatException e) {
        }
        if (platform == -1) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        int pSize = 10;
        Pagination pagination = new Pagination(pSize * 1, 1, pSize);

        try {
            WikiAppIndexDTO indexDTO = new WikiAppIndexDTO();
            List<WikiAppIndexItemDTO> headInfo = new ArrayList<WikiAppIndexItemDTO>();
            List<WikiAppIndexModuleDTO> rows = new ArrayList<WikiAppIndexModuleDTO>();
            WikiAppIndexModuleDTO subscribe = new WikiAppIndexModuleDTO();

            //我的订阅
            Profile profile = UserCenterServiceSngl.get().getProfileByUid(uid);
            if (profile == null) {
                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
            }
            subscribe.setTitle("我的订阅");
            subscribe.setType(String.valueOf(ClientItemDomain.JOYMEWIKI.getCode()));
            subscribe.setJt(String.valueOf(AppRedirectType.NOTHING.getCode()));
            subscribe.setJi("");
            subscribe.setRows(wikiAppWebLogic.queryWikiAppSubscribe(profile.getProfileId(), ObjectRelationType.WIKI, pagination));
            indexDTO.setSubscribe(subscribe);

            List<ClientLine> lineList = JoymeAppServiceSngl.get().queryClientLine(ClientLineType.WIKIAPP_INDEX, platform);
            if (!CollectionUtil.isEmpty(lineList)) {
                for (ClientLine line : lineList) {
                    if (line == null) {
                        continue;
                    }
                    //轮播图
                    if (ClientItemType.WIKIAPP_HEADINFO.equals(line.getItemType())) {
                        List<ClientLineItem> itemList = JoymeAppServiceSngl.get().queryClientLineItem(line.getCode(), QuerySort.add(ClientLineItemField.DISPLAY_ORDER, QuerySortOrder.ASC), pagination);
                        if (!CollectionUtil.isEmpty(itemList)) {
                            for (ClientLineItem item : itemList) {
                                WikiAppIndexItemDTO headItemDTO = wikiAppWebLogic.buildWikiAppIndexItemDTO(item);
                                if (headItemDTO != null) {
                                    headInfo.add(headItemDTO);
                                }
                            }
                        }
                    } else {
                        //自定义模块
                        WikiAppIndexModuleDTO moduleDTO = new WikiAppIndexModuleDTO();
                        moduleDTO.setTitle(line.getLineName());
                        moduleDTO.setType(String.valueOf(line.getItemType().getCode()));
                        moduleDTO.setJt("");
                        moduleDTO.setJi(line.getCode());
                        List<WikiAppIndexItemDTO> itemRows = new ArrayList<WikiAppIndexItemDTO>();
                        List<ClientLineItem> itemList = JoymeAppServiceSngl.get().queryClientLineItem(line.getCode(), QuerySort.add(ClientLineItemField.DISPLAY_ORDER, QuerySortOrder.ASC), pagination);
                        if (!CollectionUtil.isEmpty(itemList)) {
                            for (ClientLineItem item : itemList) {
                                WikiAppIndexItemDTO itemDTO = wikiAppWebLogic.buildWikiAppIndexItemDTO(item);
                                if (itemDTO != null) {
                                    itemRows.add(itemDTO);
                                }
                            }
                        }
                        moduleDTO.setRows(itemRows);
                        rows.add(moduleDTO);
                    }
                }
            }
            indexDTO.setHeadinfo(headInfo);
            indexDTO.setRows(rows);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("rs", String.valueOf(ResultCodeConstants.SUCCESS.getCode()));
            jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
            jsonObject.put("result", indexDTO);
            return jsonObject.toString();
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
            return ResultCodeConstants.ERROR.getJsonString();
        }
    }

    @ResponseBody
    @RequestMapping(value = "/sublist")
    public String subList(HttpServletRequest request, HttpServletResponse response,
                          @RequestParam(value = "linecode", required = false) String lineCode,
                          @RequestParam(value = "pnum", required = false) String pNum,
                          @RequestParam(value = "pcount", required = false) String pCount) {
        if(StringUtil.isEmpty(lineCode)){
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }
        String appKey = HTTPUtil.getParam(request, "appkey");
        String uidParam = HTTPUtil.getParam(request, "uid");
        String platformParam = HTTPUtil.getParam(request, "platform");
        if (StringUtil.isEmpty(appKey) || StringUtil.isEmpty(uidParam) || StringUtil.isEmpty(platformParam)) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        long uid = -1l;
        try {
            uid = Long.parseLong(uidParam);
        } catch (NumberFormatException e) {
        }
        if (uid == -1l) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }
        int platform = -1;
        try {
            platform = Integer.parseInt(platformParam);
        } catch (NumberFormatException e) {
        }
        if (platform == -1) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        int cp = 1;
        try {
            cp = Integer.parseInt(pNum);
        } catch (NumberFormatException e) {
        }
        int pSize = 10;
        try {
            pSize = Integer.parseInt(pCount);
        } catch (NumberFormatException e) {
        }
        Pagination pagination = new Pagination(pSize * cp, cp, pSize);

        try {
            List<WikiAppIndexItemDTO> itemDTOs = new ArrayList<WikiAppIndexItemDTO>();

            List<ClientLineItem> itemList = JoymeAppServiceSngl.get().queryClientLineItem(lineCode, QuerySort.add(ClientLineItemField.DISPLAY_ORDER, QuerySortOrder.ASC), pagination);
            if (!CollectionUtil.isEmpty(itemList)) {
                for (ClientLineItem item : itemList) {
                    WikiAppIndexItemDTO itemDTO = wikiAppWebLogic.buildWikiAppIndexItemDTO(item);
                    if (itemDTO != null) {
                        itemDTOs.add(itemDTO);
                    }
                }
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("rs", String.valueOf(ResultCodeConstants.SUCCESS.getCode()));
            jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
            JSONObject rows = new JSONObject();
            rows.put("rows", itemDTOs);
            jsonObject.put("result", rows);
            return jsonObject.toString();
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
            return ResultCodeConstants.ERROR.getJsonString();
        }
    }

}
