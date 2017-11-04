package com.enjoyf.webapps.joyme.webpage.controller.wanba.api;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.notice.NoticeServiceSngl;
import com.enjoyf.platform.service.notice.NoticeType;
import com.enjoyf.platform.service.notice.UserNotice;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.http.AppUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.wordfilter.WanbaResultCodeConstants;
import com.enjoyf.webapps.joyme.dto.Wanba.WanbaNoticeDTO;
import com.enjoyf.webapps.joyme.dto.Wanba.WanbaNoticeSumDTO;
import com.enjoyf.webapps.joyme.weblogic.wanba.WanbaNoticeWebLogic;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/9/14
 */
@RequestMapping("/wanba/api/notice")
@Controller
public class WanbaNoticeController {

    @Resource
    private WanbaNoticeWebLogic wanbaNoticeWebLogic;

    @RequestMapping("/query/sum")
    @ResponseBody
    public String querySum(HttpServletRequest request) {
        String profileId = HTTPUtil.getParam(request, "pid");
        String appkey = HTTPUtil.getParam(request, "appkey");


        String version = HTTPUtil.getParam(request, "version");
        String platform = HTTPUtil.getParam(request, "platform");

        //check param
        if (StringUtil.isEmpty(profileId) || StringUtil.isEmpty(appkey) || StringUtil.isEmpty(version)
                || StringUtil.isEmpty(platform)) {
            return WanbaResultCodeConstants.PARAM_EMPTY.getJsonString();
        }
        try {
            appkey = AppUtil.getAppKey(appkey);
            List<WanbaNoticeSumDTO> list = wanbaNoticeWebLogic.queryAppSumByProfile(profileId, version, platform, appkey);

            JSONObject jsonObject = WanbaResultCodeConstants.SUCCESS.getJsonObject();
            Map resultMap = new HashMap();
            resultMap.put("rows", list);
            jsonObject.put("result", JsonBinder.buildNonNullBinder(resultMap));
            return jsonObject.toString();

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
            return WanbaResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
    }


    //问答通知
    @RequestMapping("/question/list")
    @ResponseBody
    public String questionList(HttpServletRequest request,
                               @RequestParam(value = "pnum", required = false, defaultValue = "1") String pnum,
                               @RequestParam(value = "pcount", required = false, defaultValue = "20") String pcount) {
        String profileId = HTTPUtil.getParam(request, "pid");
        int pageNo = 1;
        int pageCount = 20;
        try {
            pageNo = Integer.parseInt(pnum);
            pageCount = Integer.valueOf(pcount);
        } catch (NumberFormatException e) {
        }


        //check param
        if (StringUtil.isEmpty(profileId)) {
            return WanbaResultCodeConstants.PARAM_EMPTY.getJsonString();
        }
        try {
            PageRows<WanbaNoticeDTO> pageRows = wanbaNoticeWebLogic.queryQuestionNotice(profileId, "", NoticeType.ANSWER, new Pagination(pageNo * pageCount, pageNo, pageCount));

            JSONObject jsonObject = WanbaResultCodeConstants.SUCCESS.getJsonObject();
            Map resultMap = new HashMap();
            resultMap.put("rows", pageRows.getRows());
            resultMap.put("page", pageRows.getPage());
            jsonObject.put("result", JsonBinder.buildNonNullBinder(resultMap));

            if (!StringUtil.isEmpty(profileId)) {
                try {
                    NoticeServiceSngl.get().readNotice(profileId, "", NoticeType.ANSWER);
                } catch (Exception e) {
                    GAlerter.lab(this.getClass().getName() + " read notice error.e:", e);
                }
            }

            return jsonObject.toString();

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
            return WanbaResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
    }


    //系统消息
    @RequestMapping("/system/list")
    @ResponseBody
    public String systemList(HttpServletRequest request,
                             @RequestParam(value = "pnum", required = false, defaultValue = "1") String pnum,
                             @RequestParam(value = "pcount", required = false, defaultValue = "20") String pcount) {
        String profileId = HTTPUtil.getParam(request, "pid");
        int pageNo = 1;
        int pageCount = 20;
        try {
            pageNo = Integer.parseInt(pnum);
            pageCount = Integer.valueOf(pcount);
        } catch (NumberFormatException e) {
        }
        String appkey = HTTPUtil.getParam(request, "appkey");
        appkey = AppUtil.getAppKey(appkey);

        String version = HTTPUtil.getParam(request, "version");
        String platform = HTTPUtil.getParam(request, "platform");

        //check param
        if (StringUtil.isEmpty(profileId) || StringUtil.isEmpty(version) || StringUtil.isEmpty(platform)) {
            return WanbaResultCodeConstants.PARAM_EMPTY.getJsonString();
        }
        try {
            PageRows<WanbaNoticeDTO> pageRows = wanbaNoticeWebLogic.querySystemNotice(appkey, version, platform, new Pagination(pageNo * pageCount, pageNo, pageCount));

            JSONObject jsonObject = WanbaResultCodeConstants.SUCCESS.getJsonObject();
            Map resultMap = new HashMap();
            resultMap.put("rows", pageRows.getRows());
            resultMap.put("page", pageRows.getPage());
            jsonObject.put("result", resultMap);

            if (!StringUtil.isEmpty(profileId)) {
                NoticeServiceSngl.get().readSystemNotice(profileId, appkey, version, platform);
            }
            return jsonObject.toString();

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
            return WanbaResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
    }

    @RequestMapping("/user/delete")
    @ResponseBody
    public String systemDelete(HttpServletRequest request) {
        String profileId = HTTPUtil.getParam(request, "pid");
        String nidParam = HTTPUtil.getParam(request, "nid");

        //check param
        if (StringUtil.isEmpty(profileId) || StringUtil.isEmpty(nidParam)) {
            return WanbaResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        long nid = -1l;
        try {
            nid = Long.parseLong(nidParam);
        } catch (NumberFormatException e) {
        }
        if (nid < 0l) {
            return WanbaResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        try {
            UserNotice userNotice = NoticeServiceSngl.get().getUserNotice(nid);
            if (userNotice == null) {
                return WanbaResultCodeConstants.USERNOTICE_NOTEXISTS.getJsonString();
            }

            if (!userNotice.getProfileId().equals(profileId)) {
                return WanbaResultCodeConstants.USERNOTICE_PROFILENOTEQ.getJsonString();
            }

            boolean bval = NoticeServiceSngl.get().deleteUserNotice(profileId, "", userNotice.getNoticeType(), nid);
            return bval ? WanbaResultCodeConstants.SUCCESS.getJsonString() : WanbaResultCodeConstants.FAILED.getJsonString();
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
            return WanbaResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
    }

}
