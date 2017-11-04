package com.enjoyf.webapps.joyme.webpage.controller.youku.api;

import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.point.PointServiceSngl;
import com.enjoyf.platform.service.point.pointwall.AppInstallType;
import com.enjoyf.platform.service.point.pointwall.PointwallApp;
import com.enjoyf.platform.service.point.pointwall.PointwallAppField;
import com.enjoyf.platform.service.point.pointwall.PointwallTasklog;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.oauth.renren.api.client.utils.Md5Utils;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.util.CookieUtil;
import com.enjoyf.util.StringUtil;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterCookieUtil;
import com.enjoyf.webapps.joyme.webpage.controller.youku.AbstractYoukuController;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/6/18
 * Description:
 */
@Controller
@RequestMapping("/youku/api/report/idfa")
public class YoukuApiReportController extends AbstractYoukuController {

    @RequestMapping
    @ResponseBody
    public String reportIdfa(HttpServletRequest request, HttpServletResponse response) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rs", "1");
        jsonObject.put("msg", "success");

        try {

            String idfa = UserCenterCookieUtil.getCookieKeyValue(request, "idfa");
            String appid = request.getParameter("aid");
            if (StringUtil.isEmpty(idfa) || StringUtil.isEmpty(appid)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            }

            long aid = -1l;
            try {
                aid = Long.parseLong(appid);
            } catch (NumberFormatException e) {
            }

            if (aid < 0l) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            }


            QueryExpress getAppQueryExpress = new QueryExpress();
            getAppQueryExpress.add(QueryCriterions.eq(PointwallAppField.APP_ID, aid));
            PointwallApp pointwallApp = PointServiceSngl.get().getPointwallApp(getAppQueryExpress);

            if (pointwallApp == null) {
                return ResultCodeConstants.APP_NOT_EXISTS.getJsonString();
            }

            PointwallTasklog pointwallTasklog = new PointwallTasklog();
            pointwallTasklog.setTaskId(Md5Utils.md5(idfa + appid + AppInstallType.DOWNLOADED.getCode()));
            pointwallTasklog.setClientId(idfa);
            pointwallTasklog.setProfileid(idfa);
            pointwallTasklog.setAppId(aid);
            pointwallTasklog.setPackageName(pointwallApp.getPackageName());
            pointwallTasklog.setAppkey(APPKEY);
            pointwallTasklog.setStatus(AppInstallType.DOWNLOADED.getCode());
            pointwallTasklog.setCreateIp(getIp(request));
            pointwallTasklog.setPointAmount(0);
            //todo 写死先
            pointwallTasklog.setPlatform(AppPlatform.IOS.getCode());

            PointServiceSngl.get().insertPointwallTasklog(pointwallTasklog);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:,", e);
            return "callback([" + jsonObject.toString() + "])";
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:,", e);
            return "callback([" + ResultCodeConstants.SYSTEM_ERROR.getJsonString() + "])";
        }

        return "callback([" + jsonObject.toString() + "])";
    }

    @RequestMapping("/activity")
    @ResponseBody
    public String activity(HttpServletRequest request, HttpServletResponse response) {

        JSONObject jsonObject = new JSONObject();

        try {
            return jsonObject.toString();

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:,", e);
            jsonObject.put("status", "error");
        }


        return jsonObject.toString();
    }

    //http://test.ios.gamex.mobile.youku.com/app/ios/recommend/
    @RequestMapping("/pay")
    @ResponseBody
    public String pay(HttpServletRequest request, HttpServletResponse response) {


        JSONObject jsonObject = new JSONObject();
        try {
            return jsonObject.toString();
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:,", e);
            jsonObject.put("status", "error");
        }

        return jsonObject.toString();
    }
}
