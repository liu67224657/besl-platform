package com.enjoyf.webapps.tools.webpage.controller.gameclient.advertise;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.advertise.AdvertiseServiceSngl;
import com.enjoyf.platform.service.advertise.app.AppAdvertise;
import com.enjoyf.platform.service.advertise.app.AppAdvertiseField;
import com.enjoyf.platform.service.advertise.app.AppAdvertiseType;
import com.enjoyf.platform.service.advertise.app.GameExistJson;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-6-10
 * Time: 上午10:30
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/gameclient/advertise")
public class GameClientAdvertiseMaterialController extends ToolsBaseController {

    private static final int PAGE_SIZE = 40;

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                             @RequestParam(value = "qname", required = false) String qname,
                             @RequestParam(value = "removestatus", required = false) String removestatus,
                             @RequestParam(value = "redirecttype", required = false) String redirecttype) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            int curPage = (pageStartIndex / pageSize) + 1;
            Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(AppAdvertiseField.ADVERTISE_TYPE, AppAdvertiseType.GAMECLIENT.getCode()));

            //名称
            if (!StringUtil.isEmpty(qname)) {
                queryExpress.add(QueryCriterions.like(AppAdvertiseField.ADVERTISE_NAME, "%" + qname + "%"));
            }

            //状态
            if (!StringUtil.isEmpty(removestatus) && removestatus.equals("y")) {
                queryExpress.add(QueryCriterions.eq(AppAdvertiseField.REMOVE_STATUS, ActStatus.ACTED.getCode()));
            } else if (!StringUtil.isEmpty(removestatus) && removestatus.equals("n")) {
                queryExpress.add(QueryCriterions.eq(AppAdvertiseField.REMOVE_STATUS, ActStatus.UNACT.getCode()));
            }

            //类型
            if (!StringUtil.isEmpty(redirecttype)) {
                queryExpress.add(QueryCriterions.eq(AppAdvertiseField.REDIRECT_TYPE, Integer.valueOf(redirecttype)));
            }

            queryExpress.add(QuerySort.add(AppAdvertiseField.ADVERTISE_ID, QuerySortOrder.DESC));

            PageRows<AppAdvertise> pageRows = AdvertiseServiceSngl.get().queryAppAdvertise(queryExpress, pagination);
            mapMessage.put("list", pageRows.getRows());
            mapMessage.put("page", pageRows.getPage());
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/gameclient/advertise/list", mapMessage);
        }
        return new ModelAndView("/gameclient/advertise/list", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createpage() {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        return new ModelAndView("/gameclient/advertise/createpage", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(
            @RequestParam(value = "advertise_name", required = false) String advertise_name,
            @RequestParam(value = "advertise_desc", required = false) String advertise_desc,
            @RequestParam(value = "appPlatform", required = false, defaultValue = "0") String appPlatform,
            @RequestParam(value = "adname1", required = false) String advertiseName1,
            @RequestParam(value = "addesc1", required = false) String advertiseDesc1,
            @RequestParam(value = "url1", required = false) String url1,
            @RequestParam(value = "picurl1", required = false) String picUrl1,
            @RequestParam(value = "redirecttype1", required = false) String redirecttype1,

            @RequestParam(value = "adname2", required = false) String advertiseName2,
            @RequestParam(value = "addesc2", required = false) String advertiseDesc2,
            @RequestParam(value = "url2", required = false) String url2,
            @RequestParam(value = "picurl2", required = false) String picUrl12,
            @RequestParam(value = "redirecttype2", required = false) String redirecttype2,

            @RequestParam(value = "adname3", required = false) String advertiseName3,
            @RequestParam(value = "addesc3", required = false) String advertiseDesc3,
            @RequestParam(value = "url3", required = false) String url3,
            @RequestParam(value = "picurl3", required = false) String picUrl3,
            @RequestParam(value = "redirecttype3", required = false) String redirecttype3,

            @RequestParam(value = "adname4", required = false) String advertiseName4,
            @RequestParam(value = "addesc4", required = false) String advertiseDesc4,
            @RequestParam(value = "url4", required = false) String url4,
            @RequestParam(value = "picurl4", required = false) String picUrl4,
            @RequestParam(value = "redirecttype4", required = false) String redirecttype4) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            AppAdvertise appAdvertise = new AppAdvertise();
            appAdvertise.setCreateTime(new Date());
            appAdvertise.setCreatIp(this.getIp());
            appAdvertise.setCreateUser(this.getCurrentUser().getUserid());

            appAdvertise.setAdvertiseName(advertise_name);
            appAdvertise.setAdvertiseDesc(advertise_desc);
            appAdvertise.setAppPlatform(AppPlatform.getByCode(Integer.valueOf(appPlatform)));

            GameExistJson json = new GameExistJson();
            json.setTitle1(advertiseName1);
            json.setDesc1(advertiseDesc1);
            json.setUrl1(url1);
            json.setPicurl1(picUrl1);
            json.setType1(redirecttype1);

            json.setTitle2(advertiseName2);
            json.setDesc2(advertiseDesc2);
            json.setUrl2(url2);
            json.setPicurl2(picUrl12);
            json.setType2(redirecttype2);

            json.setTitle3(advertiseName3);
            json.setDesc3(advertiseDesc3);
            json.setUrl3(url3);
            json.setPicurl3(picUrl3);
            json.setType3(redirecttype3);

            json.setTitle4(advertiseName4);
            json.setDesc4(advertiseDesc4);
            json.setUrl4(url4);
            json.setPicurl4(picUrl4);
            json.setType4(redirecttype4);

            appAdvertise.setExtstring(json.toJson());
            appAdvertise.setAppAdvertiseType(AppAdvertiseType.GAMECLIENT);

            appAdvertise = AdvertiseServiceSngl.get().createAppAdvertise(appAdvertise);

            writeToolsLog(LogOperType.ADVERTISE_MATERIAL_ADD, "广告素材,advertiseId" + appAdvertise.getAdvertiseId());
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/gameclient/advertise/list", mapMessage);
        }

        return new ModelAndView("redirect:/gameclient/advertise/list");
    }


    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyPage(@RequestParam(value = "advertiseId", required = false) Long advertiseId,
                                   @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            AppAdvertise appAdvertise = AdvertiseServiceSngl.get().getAppAdvertise(advertiseId);
            mapMessage.put("appAdvertise", appAdvertise);
            mapMessage.put("gameExistJson", GameExistJson.parse(appAdvertise.getExtstring()));
            mapMessage.put("pageStartIndex", pageStartIndex);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/advertise/app/list", mapMessage);
        }
        return new ModelAndView("/gameclient/advertise/modifypage", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(
            @RequestParam(value = "advertise_name", required = false) String advertise_name,
            @RequestParam(value = "advertise_desc", required = false) String advertise_desc,
            @RequestParam(value = "appPlatform", required = false, defaultValue = "0") String appPlatform,
            @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
            @RequestParam(value = "advertiseId", required = false) Long advertiseId,
            @RequestParam(value = "removestatus", required = false) String removestatus,

            @RequestParam(value = "adname1", required = false) String advertiseName1,
            @RequestParam(value = "addesc1", required = false) String advertiseDesc1,
            @RequestParam(value = "url1", required = false) String url1,
            @RequestParam(value = "picurl1", required = false) String picUrl1,
            @RequestParam(value = "redirecttype1", required = false) String redirecttype1,

            @RequestParam(value = "adname2", required = false) String advertiseName2,
            @RequestParam(value = "addesc2", required = false) String advertiseDesc2,
            @RequestParam(value = "url2", required = false) String url2,
            @RequestParam(value = "picurl2", required = false) String picUrl12,
            @RequestParam(value = "redirecttype2", required = false) String redirecttype2,

            @RequestParam(value = "adname3", required = false) String advertiseName3,
            @RequestParam(value = "addesc3", required = false) String advertiseDesc3,
            @RequestParam(value = "url3", required = false) String url3,
            @RequestParam(value = "picurl3", required = false) String picUrl3,
            @RequestParam(value = "redirecttype3", required = false) String redirecttype3,

            @RequestParam(value = "adname4", required = false) String advertiseName4,
            @RequestParam(value = "addesc4", required = false) String advertiseDesc4,
            @RequestParam(value = "url4", required = false) String url4,
            @RequestParam(value = "picurl4", required = false) String picUrl4,
            @RequestParam(value = "redirecttype4", required = false) String redirecttype4) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {

            UpdateExpress updateExpress = new UpdateExpress();


            //list 页面修改状态
            if (!StringUtil.isEmpty(removestatus)) {
                if (!StringUtil.isEmpty(removestatus) && removestatus.equals("n")) {
                    updateExpress.set(AppAdvertiseField.REMOVE_STATUS, ActStatus.UNACT.getCode());
                }
                if (!StringUtil.isEmpty(removestatus) && removestatus.equals("y")) {
                    updateExpress.set(AppAdvertiseField.REMOVE_STATUS, ActStatus.ACTED.getCode());
                }
            }

            //modifypage
            if (StringUtil.isEmpty(removestatus)) {
                GameExistJson json = new GameExistJson();
                json.setTitle1(advertiseName1);
                json.setDesc1(advertiseDesc1);
                json.setUrl1(url1);
                json.setPicurl1(picUrl1);
                json.setType1(redirecttype1);

                json.setTitle2(advertiseName2);
                json.setDesc2(advertiseDesc2);
                json.setUrl2(url2);
                json.setPicurl2(picUrl12);
                json.setType2(redirecttype2);

                json.setTitle3(advertiseName3);
                json.setDesc3(advertiseDesc3);
                json.setUrl3(url3);
                json.setPicurl3(picUrl3);
                json.setType3(redirecttype3);

                json.setTitle4(advertiseName4);
                json.setDesc4(advertiseDesc4);
                json.setUrl4(url4);
                json.setPicurl4(picUrl4);
                json.setType4(redirecttype4);
                updateExpress.set(AppAdvertiseField.EXTSTRING, json.toJson());
                updateExpress.set(AppAdvertiseField.ADVERTISE_NAME, advertise_name);
                updateExpress.set(AppAdvertiseField.ADVERTISE_DESC, advertise_desc);
                updateExpress.set(AppAdvertiseField.ADVERTISE_PLATFORM, AppPlatform.getByCode(Integer.valueOf(appPlatform)).getCode());

            }


            boolean bval = AdvertiseServiceSngl.get().modifyAppAdvertise(updateExpress, advertiseId);

            if (bval) {
                writeToolsLog(LogOperType.ADVERTISE_MATERIAL_UPDATE, "广告素材,advertiseId" + advertiseId);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/gameclient/advertise/list", mapMessage);
        }
        return new ModelAndView("redirect:/gameclient/advertise/list?maxPageItems=" + PAGE_SIZE + "&pager.offset=" + pageStartIndex, mapMessage);
    }


}
