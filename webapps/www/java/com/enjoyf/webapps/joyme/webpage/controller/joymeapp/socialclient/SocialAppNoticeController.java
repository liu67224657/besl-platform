package com.enjoyf.webapps.joyme.webpage.controller.joymeapp.socialclient;

import com.enjoyf.platform.service.message.MessageServiceSngl;
import com.enjoyf.platform.service.message.Notice;
import com.enjoyf.platform.service.message.NoticeType;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-12-11 下午1:03
 * Description:
 */
@Controller
@RequestMapping(value = "/joymeapp/notice")
public class SocialAppNoticeController extends AbstractSocialAppBaseController {

    /**
     * 读 消息
     */
    @ResponseBody
    @RequestMapping(value = "/social/read")
    public String readNotice(HttpServletRequest request, HttpServletResponse response,
                           @RequestParam(value = "uno", required = false) String uno,
                           @RequestParam(value = "client_id", required = false) String clientId,
                           @RequestParam(value = "client_token", required = false) String clientToken,
                           @RequestParam(value = "appkey", required = false) String appKey,
                           @RequestParam(value = "platform", required = false) String platform,
                           @RequestParam(value = "channelid", required = false) String channelCode,
                           @RequestParam(value = "access_token", required = false) String access_token,
                           @RequestParam(value = "token_secr", required = false) String token_secr
    ) {
        ResultObjectMsg resultMsg = new ResultObjectMsg(ResultObjectMsg.CODE_S);
        try {
            if (StringUtil.isEmpty(uno)) {
                resultMsg.setRs(ResultCodeConstants.SOCIAL_UNO_IS_NULL.getCode());
                resultMsg.setMsg(ResultCodeConstants.SOCIAL_UNO_IS_NULL.getMsg());
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }
            MessageServiceSngl.get().readNoticeByType(uno, NoticeType.SOCIAL_CLIENT);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            resultMsg.setRs(ResultObjectMsg.CODE_E);
            resultMsg.setMsg("system.error");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        resultMsg.setMsg("system.success");
        return JsonBinder.buildNormalBinder().toJson(resultMsg);
    }

    /**
     * 获取 未读 消息 数目，0--没有新消息
     */
    @ResponseBody
    @RequestMapping(value = "/social/get")
    public String getNotice(HttpServletRequest request, HttpServletResponse response,
                           @RequestParam(value = "uno", required = false) String uno,
                           @RequestParam(value = "client_id", required = false) String clientId,
                           @RequestParam(value = "client_token", required = false) String clientToken,
                           @RequestParam(value = "appkey", required = false) String appKey,
                           @RequestParam(value = "platform", required = false) String platform,
                           @RequestParam(value = "channelid", required = false) String channelCode,
                           @RequestParam(value = "access_token", required = false) String access_token,
                           @RequestParam(value = "token_secr", required = false) String token_secr
    ) {
        ResultObjectMsg resultMsg = new ResultObjectMsg(ResultObjectMsg.CODE_S);
        try {
            if (StringUtil.isEmpty(uno)) {
                resultMsg.setRs(ResultCodeConstants.SOCIAL_UNO_IS_NULL.getCode());
                resultMsg.setMsg(ResultCodeConstants.SOCIAL_UNO_IS_NULL.getMsg());
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }
            int sum = 0;
            Notice notice = MessageServiceSngl.get().getNoticeByCache(uno, NoticeType.SOCIAL_CLIENT);
            if(notice != null){
                sum = notice.getCount();
            }
            resultMsg.setResult(sum);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            resultMsg.setRs(ResultObjectMsg.CODE_E);
            resultMsg.setMsg("system.error");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }

        resultMsg.setMsg("system.success");
        return JsonBinder.buildNormalBinder().toJson(resultMsg);
    }

}
