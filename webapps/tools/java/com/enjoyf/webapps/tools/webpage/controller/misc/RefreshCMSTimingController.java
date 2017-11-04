package com.enjoyf.webapps.tools.webpage.controller.misc;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.misc.MiscServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.service.misc.RefreshCMSTiming;
import com.enjoyf.platform.service.misc.RefreshCMSTimingField;
import com.enjoyf.platform.service.misc.RefreshReleaseType;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.http.HttpClientManager;
import com.enjoyf.platform.util.http.HttpResult;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhimingli on 2015/7/28.
 */
@Controller
@RequestMapping(value = "/misc/refreshcms")
public class RefreshCMSTimingController extends ToolsBaseController {

    private String get_cms_name = "http://article." + WebappConfig.get().getDomain() + "/plus/api.php?a=getTypeName&ids=";

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "20") int pageSize,
                             @RequestParam(value = "remove_status", required = false) String remove_status) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        QueryExpress queryExpress = new QueryExpress();
        int curPage = pageStartIndex / pageSize + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
        try {
            if (StringUtil.isEmpty(remove_status)) {
                queryExpress.add(QueryCriterions.eq(RefreshCMSTimingField.REMOVE_STATUS, ActStatus.UNACT.getCode()));
            } else {
                queryExpress.add(QueryCriterions.eq(RefreshCMSTimingField.REMOVE_STATUS, ActStatus.getByCode(remove_status).getCode()));
            }
            queryExpress.add(QuerySort.add(RefreshCMSTimingField.RELEASE_TIME, QuerySortOrder.ASC));
            PageRows<RefreshCMSTiming> pageRows = MiscServiceSngl.get().queryRefreshCMSTimingByPage(queryExpress, pagination);
            mapMessage.put("list", pageRows.getRows());
            mapMessage.put("page", pageRows.getPage());
            mapMessage.put("remove_status", remove_status);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/misc/refreshcms/refreshcmslist", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createpage(@RequestParam(value = "errorMsg", required = false) String errorMsg,
                                   @RequestParam(value = "cms_id", required = false) String cms_id) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("release_type", RefreshReleaseType.ONE_TIME.getCode());
        mapMessage.put("cms_id", cms_id);
        if (!StringUtil.isEmpty(errorMsg)) {
            mapMessage.put("errorMsg", "cms栏目不存在，请仔细检查");
        }
        return new ModelAndView("/misc/refreshcms/createpage", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "cms_id", required = false) String cms_id,
                               @RequestParam(value = "release_type", required = false) int release_type,
                               @RequestParam(value = "release_time", required = false) String release_time) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            RefreshCMSTiming refreshCMSTiming = new RefreshCMSTiming();
            refreshCMSTiming.setCms_id(cms_id);

            String cmsname = getGet_cms_name(cms_id);
            if (StringUtil.isEmpty(cmsname)) {
                mapMessage.put("cms_id", cms_id);
                mapMessage.put("release_type", release_type);
                mapMessage.put("release_time", release_time);

                return new ModelAndView("redirect:/misc/refreshcms/createpage?errorMsg=errorMsg&cms_id=" + cms_id);
            } else {
                refreshCMSTiming.setCms_name(cmsname);
                refreshCMSTiming.setModify_time(new Date());
                refreshCMSTiming.setModify_user(getCurrentUser().getUserid());
                refreshCMSTiming.setRefreshReleaseType(RefreshReleaseType.getByCode(release_type));
                refreshCMSTiming.setRelease_time(DateUtil.formatStringToDate(release_time, DateUtil.PATTERN_DATE_TIME).getTime());
                refreshCMSTiming.setRemove_status(ActStatus.UNACT);
                MiscServiceSngl.get().createRefreshCMSTiming(refreshCMSTiming);
            }

            writeToolsLog(LogOperType.CMS_REFRESH_ADD, "cms_id:" + cms_id);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/misc/refreshcms/list", mapMessage);
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifypage(@RequestParam(value = "time_id", required = false) long time_id,
                                   @RequestParam(value = "cms_id", required = false) String cms_id,
                                   @RequestParam(value = "errorMsg", required = false) String errorMsg) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(RefreshCMSTimingField.TIME_ID, time_id));
        try {
            RefreshCMSTiming refreshCMSTiming = MiscServiceSngl.get().getRefreshCMSTiming(queryExpress);
            mapMessage.put("refreshCMSTiming", refreshCMSTiming);
            if (!StringUtil.isEmpty(errorMsg)) {
                refreshCMSTiming.setCms_id(cms_id);
                mapMessage.put("errorMsg", "cms栏目不存在，请仔细检查");
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return new ModelAndView("/misc/refreshcms/modifypage", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "time_id", required = false) long time_id,
                               @RequestParam(value = "cms_id", required = false) String cms_id,
                               @RequestParam(value = "release_type", required = false) int release_type,
                               @RequestParam(value = "release_time", required = false) String release_time,
                               @RequestParam(value = "remove_status", required = false) String remove_status) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(RefreshCMSTimingField.TIME_ID, time_id));
        try {
            String cmsname = getGet_cms_name(cms_id);
            if (!StringUtil.isEmpty(cmsname)) {
                UpdateExpress updateExpress = new UpdateExpress();
                updateExpress.set(RefreshCMSTimingField.CMS_ID, cms_id);
                updateExpress.set(RefreshCMSTimingField.CMS_NAME, cmsname);
                updateExpress.set(RefreshCMSTimingField.RELEASE_TYPE, release_type);
                updateExpress.set(RefreshCMSTimingField.REMOVE_STATUS, remove_status);
                updateExpress.set(RefreshCMSTimingField.RELEASE_TIME, DateUtil.formatStringToDate(release_time, DateUtil.PATTERN_DATE_TIME).getTime());
                MiscServiceSngl.get().modifyRefreshCMSTiming(updateExpress, queryExpress);
                writeToolsLog(LogOperType.CMS_REFRESH_MODIFY, "time_id:" + time_id);
            } else {
                return new ModelAndView("redirect:/misc/refreshcms/modifypage?time_id=" + time_id + "&errorMsg=errorMsg&cms_id=" + cms_id);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView("redirect:/misc/refreshcms/list?remove_status=" + remove_status);
    }


    private String getGet_cms_name(String cmsid) {
        String cms_name = "";
        try {
            HttpClientManager httpClient = new HttpClientManager();
            HttpResult result = httpClient.get(get_cms_name + cmsid, null);
            if (result.getReponseCode() == 200) {
                JSONArray jsonArray = JSONArray.fromObject(result.getResult());
                if (jsonArray.size() > 0) {
                    JSONObject jsonObje = jsonArray.getJSONObject(0);
                    cms_name = jsonObje.getString("typename");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cms_name;
    }


}
