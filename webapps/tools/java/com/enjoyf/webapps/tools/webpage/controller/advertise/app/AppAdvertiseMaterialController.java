package com.enjoyf.webapps.tools.webpage.controller.advertise.app;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.advertise.AdvertiseServiceSngl;
import com.enjoyf.platform.service.advertise.app.AppAdvertise;
import com.enjoyf.platform.service.advertise.app.AppAdvertiseField;
import com.enjoyf.platform.service.advertise.app.AppAdvertiseRedirectType;
import com.enjoyf.platform.service.advertise.app.AppAdvertiseType;
import com.enjoyf.platform.service.ask.AskUtil;
import com.enjoyf.platform.service.ask.JoymewikiJt;
import com.enjoyf.platform.service.ask.WanbaJt;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.ShortUrlUtils;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.webapps.tools.webpage.controller.advertise.AdvertiseBaseController;
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
@RequestMapping(value = "/advertise/app")
public class AppAdvertiseMaterialController extends AdvertiseBaseController {

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
            queryExpress.add(QueryCriterions.eq(AppAdvertiseField.ADVERTISE_TYPE, AppAdvertiseType.DEFAULT.getCode()));

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
            mapMessage.put("WanbaJt", WanbaJt.getToolsAll());
            PageRows<AppAdvertise> pageRows = AdvertiseServiceSngl.get().queryAppAdvertise(queryExpress, pagination);
            mapMessage.put("list", pageRows.getRows());
            mapMessage.put("page", pageRows.getPage());
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/advertise/app/list", mapMessage);
        }
        return new ModelAndView("/advertise/app/list", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createpage() {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("WanbaJt", WanbaJt.getToolsAll());
        return new ModelAndView("/advertise/app/createpage", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "adname", required = false) String advertiseName,
                               @RequestParam(value = "addesc", required = false) String advertiseDesc,
                               @RequestParam(value = "url", required = false) String url,
                               @RequestParam(value = "picurl1", required = false) String picUrl1,
                               @RequestParam(value = "picurl2", required = false) String picUrl2,
                               @RequestParam(value = "redirecttype", required = false) String redirecttype,
                               @RequestParam(value = "appPlatform", required = false) String appPlatform,
                               @RequestParam(value = "shortlinkurl", required = false, defaultValue = "0") Integer shortlinkurl) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            if (url.contains("http") && WanbaJt.WAP_WIKI.getCode() == Integer.valueOf(redirecttype)) {
                url = AskUtil.handleUrl(url);
            }
            //采用短链接
            if (shortlinkurl == 1) {
                url = ShortUrlUtils.getSinaURL(url);
            }


            AppAdvertise appAdvertise = new AppAdvertise();
            appAdvertise.setAdvertiseName(advertiseName);
            appAdvertise.setAdvertiseDesc(advertiseDesc);
            appAdvertise.setUrl(url);
            appAdvertise.setPicUrl1(picUrl1 == null ? "" : picUrl1);
            appAdvertise.setPicUrl2(picUrl2 == null ? "" : picUrl2);

            appAdvertise.setCreateTime(new Date());
            appAdvertise.setCreatIp(this.getIp());
            appAdvertise.setCreateUser(this.getCurrentUser().getUserid());

            if (redirecttype.equals("1")) {
                appAdvertise.setAppAdvertiseRedirectType(AppAdvertiseRedirectType.APPSTORE.getCode());
            } else {
                appAdvertise.setAppAdvertiseRedirectType(Integer.valueOf(redirecttype));
            }

            if (!StringUtil.isEmpty(appPlatform) && appPlatform.equals("1")) {
                appAdvertise.setAppPlatform(AppPlatform.ANDROID);
            } else if (!StringUtil.isEmpty(appPlatform) && appPlatform.equals("2")) {
                appAdvertise.setAppPlatform(AppPlatform.WEB);
            } else if (!StringUtil.isEmpty(appPlatform) && appPlatform.equals("3")) {
                appAdvertise.setAppPlatform(AppPlatform.CLIENT);
            }

            appAdvertise = AdvertiseServiceSngl.get().createAppAdvertise(appAdvertise);

            writeToolsLog(LogOperType.ADVERTISE_MATERIAL_ADD, "广告素材,advertiseId" + appAdvertise.getAdvertiseId());
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/advertise/app/list", mapMessage);
        }

        return new ModelAndView("redirect:/advertise/app/list");
    }

    @RequestMapping(value = "/appinfo")
    public ModelAndView appinfo(@RequestParam(value = "advertiseid", required = false) Integer advertiseId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            AppAdvertise appAdvertise = AdvertiseServiceSngl.get().getAppAdvertise(advertiseId);
            mapMessage.put("appAdvertise", appAdvertise);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/advertise/app/list", mapMessage);
        }

        return new ModelAndView("/advertise/app/appinfo", mapMessage);
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyPage(@RequestParam(value = "advertiseId", required = false) Long advertiseId,
                                   @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            AppAdvertise appAdvertise = AdvertiseServiceSngl.get().getAppAdvertise(advertiseId);
            mapMessage.put("appAdvertise", appAdvertise);
            mapMessage.put("pageStartIndex", pageStartIndex);
            mapMessage.put("WanbaJt", WanbaJt.getToolsAll());
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/advertise/app/list", mapMessage);
        }
        return new ModelAndView("/advertise/app/modifypage", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                               @RequestParam(value = "advertiseId", required = false) Long advertiseId,
                               @RequestParam(value = "removestatus", required = false) String removestatus,
                               @RequestParam(value = "adname", required = false) String advertiseName,
                               @RequestParam(value = "addesc", required = false) String advertiseDesc,
                               @RequestParam(value = "url", required = false) String url,
                               @RequestParam(value = "picurl1", required = false) String picUrl1,
                               @RequestParam(value = "picurl2", required = false) String picUrl2,
                               @RequestParam(value = "redirecttype", required = false) String redirecttype,
                               @RequestParam(value = "appPlatform", required = false) String appPlatform,
                               @RequestParam(value = "shortlinkurl", required = false, defaultValue = "0") Integer shortlinkurl) {
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
                if (url.contains("http") && WanbaJt.WAP_WIKI.getCode() == Integer.valueOf(redirecttype)) {
                    url = AskUtil.handleUrl(url);
                }
                //采用短链接
                if (shortlinkurl == 1) {
                    url = ShortUrlUtils.getSinaURL(url);
                }

                updateExpress.set(AppAdvertiseField.ADVERTISE_NAME, advertiseName);
                updateExpress.set(AppAdvertiseField.ADVERTISE_DESC, advertiseDesc);
                updateExpress.set(AppAdvertiseField.ADVERTISE_URL, url);
                updateExpress.set(AppAdvertiseField.ADVERTISE_PICURL1, picUrl1 == null ? "" : picUrl1);
                updateExpress.set(AppAdvertiseField.ADVERTISE_PICURL2, picUrl2 == null ? "" : picUrl2);
                if (redirecttype.equals("1")) {
                    updateExpress.set(AppAdvertiseField.REDIRECT_TYPE, AppAdvertiseRedirectType.APPSTORE.getCode());
                } else {
                    updateExpress.set(AppAdvertiseField.REDIRECT_TYPE, Integer.valueOf(redirecttype));
                }

                if (!StringUtil.isEmpty(appPlatform) && appPlatform.equals("0")) {
                    updateExpress.set(AppAdvertiseField.ADVERTISE_PLATFORM, AppPlatform.IOS.getCode());
                } else if (!StringUtil.isEmpty(appPlatform) && appPlatform.equals("1")) {
                    updateExpress.set(AppAdvertiseField.ADVERTISE_PLATFORM, AppPlatform.ANDROID.getCode());
                } else if (!StringUtil.isEmpty(appPlatform) && appPlatform.equals("2")) {
                    updateExpress.set(AppAdvertiseField.ADVERTISE_PLATFORM, AppPlatform.WEB.getCode());
                } else if (!StringUtil.isEmpty(appPlatform) && appPlatform.equals("3")) {
                    updateExpress.set(AppAdvertiseField.ADVERTISE_PLATFORM, AppPlatform.CLIENT.getCode());
                }

            }


            boolean bval = AdvertiseServiceSngl.get().modifyAppAdvertise(updateExpress, advertiseId);
            if (bval) {
                writeToolsLog(LogOperType.ADVERTISE_MATERIAL_UPDATE, "广告素材,advertiseId" + advertiseId);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/advertise/app/list", mapMessage);
        }
        return new ModelAndView("redirect:/advertise/app/list?maxPageItems=" + PAGE_SIZE + "&pager.offset=" + pageStartIndex, mapMessage);
    }


}
