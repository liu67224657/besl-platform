package com.enjoyf.webapps.joyme.webpage.controller.joymeapp;

import com.enjoyf.platform.service.joymeapp.PushListType;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.http.AppUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.JsonPagination;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.platform.webapps.common.ResultObjectPageMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.util.StringUtil;
import com.enjoyf.webapps.joyme.dto.joymeapp.NotificationDTO;
import com.enjoyf.webapps.joyme.weblogic.joymeapp.JoymeAppWebLogic;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-6-6
 * Time: 下午4:57
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/joymeapp/notification")
public class JoymeAppNotificationController {
    private static final int SOCIAL_MESSAGE_PAGE_SIZE = 20;


    @Resource(name = "joymeAppWebLogic")
    private JoymeAppWebLogic joymeAppWebLogic;

    @ResponseBody
    @RequestMapping(value = "/list")
    public String list(HttpServletRequest request, HttpServletResponse response,
                       @RequestParam(value = "flagtime", required = false, defaultValue = "0") String timestampStr,
                       @RequestParam(value = "platform", required = false, defaultValue = "0") String platformStr,
                       @RequestParam(value = "appkey", required = false) String appKey,
                       @RequestParam(value = "pnum", required = false, defaultValue = "1") String page) {
        ResultObjectPageMsg msg = new ResultObjectPageMsg();

        try {
            long timestamp = 0l;
            if (!StringUtil.isEmpty(timestampStr)) {
                timestamp = Long.valueOf(timestampStr);
            }

            int currentPage = StringUtil.isEmpty(page) ? 1 : Integer.valueOf(page);
            Pagination pagination = new Pagination(SOCIAL_MESSAGE_PAGE_SIZE * currentPage, currentPage, SOCIAL_MESSAGE_PAGE_SIZE);
            int platform = StringUtil.isEmpty(platformStr) ? 0 : Integer.parseInt(platformStr);

            String appId = getAppKey(appKey);
            //

            PushListType pushListType = PushListType.SYSTEM_MESSAGE;
            if ("17yfn24TFexGybOF0PqjdY".equals(appId)) {
                String version = HTTPUtil.getParam(request, "version");
                if (!StringUtil.isEmpty(version) && AppUtil.getVersionInt(version) >= 300) {
                    pushListType = PushListType.WANB_ASK_SYSTEM_MESSAGE;
                }

            }


            if ("17yfn24TFexGybOF0PqjdY".equals(appId)) {
                appId = "25AQWaK997Po2x300CQeP0";   //画报app指向新画报app
            }


            PageRows<NotificationDTO> pageRows = joymeAppWebLogic.queryJoymeAppPushMessage(appId, platform, pagination, timestamp, pushListType);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("rows", pageRows.getRows());
            msg.setResult(map);
            msg.setPage(new JsonPagination(pageRows.getPage()));
            msg.setRs(ResultObjectMsg.CODE_S);
            msg.setMsg("success");
            return JsonBinder.buildNormalBinder().toJson(msg);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            msg.setRs(ResultObjectMsg.CODE_E);
            msg.setMsg("system.error");
            //TODO先解决android海贼迷1.0.15奔溃问题
            Map<String, Object> map = new HashMap<String, Object>();
            PageRows<NotificationDTO> pageRows = new PageRows<NotificationDTO>();
            map.put("rows", pageRows.getRows());
            msg.setResult(map);

            return JsonBinder.buildNormalBinder().toJson(msg);
        }

    }

    private String getAppKey(String appKey) {
        if (com.enjoyf.platform.util.StringUtil.isEmpty(appKey)) {
            return "";
        }
        if (appKey.length() < 23) {
            return appKey;
        }
        return appKey.substring(0, appKey.length() - 1);
    }
}
