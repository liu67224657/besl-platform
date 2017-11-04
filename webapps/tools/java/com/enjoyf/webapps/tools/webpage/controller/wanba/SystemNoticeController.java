package com.enjoyf.webapps.tools.webpage.controller.wanba;

import com.enjoyf.platform.service.ask.WanbaJt;
import com.enjoyf.platform.service.joymeapp.AppChannelType;
import com.enjoyf.platform.service.joymeapp.PushListType;
import com.enjoyf.platform.service.joymeapp.config.AppConfig;
import com.enjoyf.platform.service.joymeapp.config.AppConfigField;
import com.enjoyf.platform.service.joymeappconfig.JoymeAppConfigServiceSngl;
import com.enjoyf.platform.service.notice.NoticeServiceSngl;
import com.enjoyf.platform.service.notice.SystemNotice;
import com.enjoyf.platform.service.notice.SystemNoticeField;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by zhimingli on 2016/9/25 0025.
 */
@Controller
@RequestMapping(value = "/wanba/systemnotice")
public class SystemNoticeController extends ToolsBaseController {

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                             @RequestParam(value = "appkey", required = false, defaultValue = "3iiv7VWfx84pmHgCUqRwun") String appkey,
                             @RequestParam(value = "platform", required = false) String platform,
                             @RequestParam(value = "version", required = false, defaultValue = "3.0.0") String version,
                             HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
        QueryExpress queryExpress = new QueryExpress();
        try {
            queryExpress.add(QuerySort.add(SystemNoticeField.CREATETIME, QuerySortOrder.DESC));
            if (!StringUtil.isEmpty(appkey)) {
                queryExpress.add(QueryCriterions.eq(SystemNoticeField.APPKEY, appkey));
            }
            if (!StringUtil.isEmpty(platform)) {
                queryExpress.add(QueryCriterions.eq(SystemNoticeField.PLATFORM, Integer.valueOf(platform)));
            }
            PageRows<SystemNotice> systemNoitceList = NoticeServiceSngl.get().querySystemNoitceByPage(queryExpress, pagination);
            mapMessage.put("list", systemNoitceList.getRows());
            mapMessage.put("page", systemNoitceList.getPage());
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/wanba/systemnotice/systemnoticelist", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createPage(HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("wanbaJt", WanbaJt.getToolsAll());
        return new ModelAndView("/wanba/systemnotice/createpage", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "text", required = false) String text,
                               @RequestParam(value = "appkey", required = false, defaultValue = "3iiv7VWfx84pmHgCUqRwun") String appkey,
                               @RequestParam(value = "jt", required = false) String jt,
                               @RequestParam(value = "ji", required = false) String ji,
                               @RequestParam(value = "title", required = false) String title,
                               @RequestParam(value = "platform", required = false) String platform,
                               @RequestParam(value = "version", required = false) String version,
                               HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        SystemNotice systemNotice = new SystemNotice();
        try {
            systemNotice.setText(text);
            systemNotice.setAppkey(appkey);
            systemNotice.setJi(ji);
            systemNotice.setJt(jt);
            systemNotice.setCreateTime(new Date());
            systemNotice.setTitle(title);
            systemNotice.setPlatform(Integer.valueOf(platform));
            String arr[] = version.split(",");
            for (String ver : arr) {
                if (!StringUtil.isEmpty(ver)) {
                    systemNotice.setVersion(ver);
                    NoticeServiceSngl.get().createSystemNotice(systemNotice);
                }
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }
        return new ModelAndView("redirect:/wanba/systemnotice/list");
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyPage(@RequestParam(value = "systemNoticeId") Long systemNoticeId,
                                   HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            mapMessage.put("wanbaJt", WanbaJt.getToolsAll());
            SystemNotice systemNotice = NoticeServiceSngl.get().getSystemNoitce(systemNoticeId);
            mapMessage.put("systemNotice", systemNotice);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/wanba/systemnotice/modifypage", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "systemNoticeId", required = false) Long systemNoticeId,
                               @RequestParam(value = "text", required = false) String text,
                               @RequestParam(value = "jt", required = false) String jt,
                               @RequestParam(value = "ji", required = false) String ji,
                               @RequestParam(value = "title", required = false) String title,
                               HttpServletRequest request) {

        UpdateExpress updateExpress = new UpdateExpress();
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            updateExpress.set(SystemNoticeField.TEXT, text);
            updateExpress.set(SystemNoticeField.JT, jt);
            updateExpress.set(SystemNoticeField.JI, ji);
            updateExpress.set(SystemNoticeField.TITLE, title);
            NoticeServiceSngl.get().modifySystemNotice(systemNoticeId, updateExpress);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }
        return new ModelAndView("redirect:/wanba/systemnotice/list", map);
    }


    @RequestMapping(value = "/remove")
    public ModelAndView remove(@RequestParam(value = "systemNoticeId") Long systemNoticeId,
                               @RequestParam(value = "appkey", required = false, defaultValue = "3iiv7VWfx84pmHgCUqRwun") String appkey,
                               @RequestParam(value = "platform", required = false, defaultValue = "0") String platform,
                               @RequestParam(value = "version", required = false, defaultValue = "3.0.0") String version) {

        try {
            NoticeServiceSngl.get().deleteSystemNotice(appkey, version, platform, systemNoticeId);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }
        return new ModelAndView("redirect:/wanba/systemnotice/list");
    }


    @ResponseBody
    @RequestMapping(value = "/getversionv2")
    public String getversionv2(HttpServletRequest request, HttpServletResponse response,
                               @RequestParam(value = "appkey", required = true) String appkey,
                               @RequestParam(value = "pushlisttype", required = false) Integer pushListType,
                               @RequestParam(value = "platform", required = false) Integer platform,
                               @RequestParam(value = "enterprise", required = false) Integer enterprise
    ) {
        if (StringUtil.isEmpty(appkey) || platform == null || pushListType == null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("rs", ResultCodeConstants.ERROR.getCode());
            jsonObject.put("msg", ResultCodeConstants.ERROR.getMsg());
            jsonObject.put("result", new JSONObject());
            return jsonObject.toString();
        }

        try {
            Set<String> versionSet = new HashSet<String>();
            Set<AppChannelType> channelSet = new HashSet<AppChannelType>();

            PushListType pushType = PushListType.getByCode(pushListType);
            if (pushType.equals(PushListType.PUSH_MESSAGE)) {
                int i = 0;
                Pagination pagination = null;
                do {
                    i = i + 1;
                    pagination = new Pagination(200 * i, i, 200);
                    PageRows<AppConfig> appConfigList = JoymeAppConfigServiceSngl.get().queryAppConfigByPage(new QueryExpress()
                            .add(QueryCriterions.eq(AppConfigField.APPKEY, appkey))
                            .add(QueryCriterions.eq(AppConfigField.PLATFORM, platform))
                            .add(QueryCriterions.eq(AppConfigField.ENTERPRISE, enterprise)), pagination);
                    if (appConfigList != null && !CollectionUtil.isEmpty(appConfigList.getRows())) {
                        pagination = appConfigList.getPage();
                        for (AppConfig appConfig : appConfigList.getRows()) {
                            if (appConfig != null) {
                                if (!StringUtil.isEmpty(appConfig.getVersion())) {
                                    versionSet.add(appConfig.getVersion());
                                }
                                if (!StringUtil.isEmpty(appConfig.getChannel())) {
                                    if (AppChannelType.getByCode(appConfig.getChannel()) != null) {
                                        channelSet.add(AppChannelType.getByCode(appConfig.getChannel()));
                                    }
                                }
                            }
                        }
                    }
                } while (!pagination.isLastPage());
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("rs", ResultCodeConstants.SUCCESS.getCode());
                jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
                JSONObject result = new JSONObject();
                result.put("versionList", versionSet);
                result.put("channelList", channelSet);
                jsonObject.put("result", result);
                return jsonObject.toString();
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rs", ResultCodeConstants.SUCCESS.getCode());
        jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
        jsonObject.put("result", new JSONObject());
        return jsonObject.toString();
    }
}
