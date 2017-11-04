package com.enjoyf.webapps.tools.webpage.controller.apiwiki;

import com.enjoyf.platform.cloud.OkHttpUtil;
import com.enjoyf.platform.cloud.PageRowsUtil;
import com.enjoyf.platform.cloud.messageservice.AppPushMessage;
import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.webapps.tools.util.MicroAuthUtil;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import com.google.gson.JsonObject;
import com.squareup.okhttp.Response;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.*;

/**
 * Created by zhimingli on 2017-3-27 0027.
 */

@Controller
@RequestMapping(value = "/apiwiki/pushmessage")
public class ApiwikiPushMessageController extends ToolsBaseController {

    private static String APPKEY = "2ojbX21Pd7WqJJRWmIniM0";

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "20") int pageSize,
                             @RequestParam(value = "sendStatus", required = false) String sendStatus,
                             @RequestParam(value = "sendType", required = false) String sendType,
                             @RequestParam(value = "pushType", required = false, defaultValue = "") String pushType,
                             HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            int curPage = (pageStartIndex / pageSize) + 1;
            String authorization = MicroAuthUtil.getToken();
            String urlget = WebappConfig.get().getMessageServiceUrl() + "/api/app-push-messages?" +
                    "page=" + (curPage - 1) + "&size=" + pageSize;

            if (!StringUtil.isEmpty(sendStatus)) {
                mapMessage.put("sendStatus", sendStatus);
                urlget += "&sendStatus=" + sendStatus;
            }
            if (!StringUtil.isEmpty(sendType)) {
                mapMessage.put("sendType", sendType);
                urlget += "&sendType=" + sendType;
            }
            if (!StringUtil.isEmpty(pushType)) {
                mapMessage.put("pushType", pushType);
                urlget += "&pushType=" + pushType;
            }
            urlget += "&removeStatus=UNVALID&sort=id,desc";

            Response response = OkHttpUtil.doGet(urlget, authorization, null);
            PageRows<AppPushMessage> pageRows = PageRowsUtil.getPage(response, pageStartIndex, pageSize, AppPushMessage.class);
            mapMessage.put("list", pageRows.getRows());
            mapMessage.put("page", pageRows.getPage());

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured error.", e);
            mapMessage.put("errorMsg", "system.error");
            return new ModelAndView("/apiwiki/pushmessage/list", mapMessage);
        }

        return new ModelAndView("/apiwiki/pushmessage/list", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createPage(HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        putDaySelect(mapMessage);

        return new ModelAndView("/apiwiki/pushmessage/createpage", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(
            @RequestParam(value = "profiletag", required = false) String profiletag,
            @RequestParam(value = "platformtag", required = false) String platformtag,
            @RequestParam(value = "channeltag", required = false) String channeltag,
            @RequestParam(value = "verisontag", required = false) String verisontag,
            @RequestParam(value = "minverisontag", required = false) String minverisontag,
            @RequestParam(value = "jt") Integer jt,
            @RequestParam(value = "ji", required = false, defaultValue = "") String ji,
            @RequestParam(value = "body") String body,
            @RequestParam(value = "sendtype") String sendtype,
            @RequestParam(value = "year", required = false) String year,
            @RequestParam(value = "month", required = false) String month,
            @RequestParam(value = "day", required = false) String day,
            @RequestParam(value = "hour", required = false) String hour,
            @RequestParam(value = "min", required = false) String min
    ) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("profiletag", profiletag);
        mapMessage.put("platformtag", platformtag);
        mapMessage.put("channeltag", channeltag);
        mapMessage.put("verisontag", verisontag);
        mapMessage.put("minverisontag", minverisontag);
        mapMessage.put("jt", jt);
        mapMessage.put("ji", ji);
        mapMessage.put("body", body);
        mapMessage.put("sendtype", sendtype);
        mapMessage.put("year", year);
        mapMessage.put("month", month);
        mapMessage.put("day", day);
        mapMessage.put("hour", hour);
        mapMessage.put("min", min);
        putDaySelect(mapMessage);

        if (sendtype.equals("delayed")) {
            if (StringUtil.isEmpty(year) ||
                    StringUtil.isEmpty(month) ||
                    StringUtil.isEmpty(day) ||
                    StringUtil.isEmpty(hour) ||
                    StringUtil.isEmpty(min)) {
                mapMessage.put("errorMsg", "delayed.sendmessage.date.illegal");
                return new ModelAndView("/apiwiki/pushmessage/createpage", mapMessage);
            }
        }

        try {
            String authorization = MicroAuthUtil.getToken();
            String urlpost = WebappConfig.get().getMessageServiceUrl() + "/api/app-push-messages";

            StringBuffer tagSb = new StringBuffer("");
            if (!StringUtil.isEmpty(profiletag)) {
                tagSb.append(profiletag).append(",");
            }
            if (platformtag != null) {
                tagSb.append(platformtag).append(",");
            }
            if (!StringUtil.isEmpty(channeltag)) {
                tagSb.append(channeltag).append(",");
            }
            if (!StringUtil.isEmpty(verisontag)) {
                tagSb.append(verisontag);
                if (!StringUtil.isEmpty(minverisontag)) {
                    tagSb.append(".").append(minverisontag);
                }
                tagSb.append(",");
            }
            String tags = "";
            if (!StringUtil.isEmpty(tagSb.toString())) {
                tags = tagSb.substring(0, tagSb.length() - 1);
            }

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("appkey", APPKEY);
            jsonObject.addProperty("jt", jt);
            jsonObject.addProperty("ji", ji);
            jsonObject.addProperty("title", body);
            jsonObject.addProperty("sendType", sendtype);
            if (sendtype.equals("delayed")) {
                Date date = DateUtil.formatStringToDate(year + "-" + month + "-" + day + " " + hour + ":" + min, "yyyy-MM-dd HH:mm");
                jsonObject.addProperty("sendTime", date.toString());
            }
            if (!StringUtil.isEmpty(tags)) {
                jsonObject.addProperty("tags", tags);
            }

            Response response = OkHttpUtil.doPost(urlpost, jsonObject, authorization, null);
            if (response.code() > 206) {
                mapMessage.put("errorMsg", "pushmessage.failed");
                return new ModelAndView("/apiwiki/pushmessage/createpage", mapMessage);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured error.", e);
            mapMessage.put("errorMsg", "system.error");
            return new ModelAndView("/apiwiki/pushmessage/createpage", mapMessage);
        }

        return new ModelAndView("redirect:/apiwiki/pushmessage/list");
    }


    private void putDaySelect(Map map) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        List<String> yearsList = new ArrayList<String>();
        int year = calendar.get(Calendar.YEAR);
        yearsList.add(String.valueOf(year));
        yearsList.add(String.valueOf(year + 1));
        map.put("years", yearsList);

        List<String> monthList = new ArrayList<String>();
        for (int i = 1; i <= 12; i++) {
            monthList.add(StringUtil.appendZore(i, 2));
        }
        map.put("months", monthList);

        List<String> dayList = new ArrayList<String>();
        for (int i = 1; i <= 31; i++) {
            dayList.add(StringUtil.appendZore(i, 2));
        }
        map.put("days", dayList);

        List<String> hourList = new ArrayList<String>();
        for (int i = 0; i <= 23; i++) {
            hourList.add(StringUtil.appendZore(i, 2));
        }
        map.put("hours", hourList);

        List<String> minList = new ArrayList<String>();
        minList.add("00");
        minList.add("30");
        map.put("mins", minList);
    }

}
