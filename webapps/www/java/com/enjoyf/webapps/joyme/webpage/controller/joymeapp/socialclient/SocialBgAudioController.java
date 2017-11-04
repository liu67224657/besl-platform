package com.enjoyf.webapps.joyme.webpage.controller.joymeapp.socialclient;

import com.enjoyf.platform.util.NextPageRows;
import com.enjoyf.platform.util.NextPagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.webapps.joyme.dto.joymeapp.socialclient.BgAudioDTO;
import com.enjoyf.webapps.joyme.weblogic.joymeapp.socialclient.SocialClientWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-4-14
 * Time: 下午12:27
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/socialapp/bgaudio")
public class SocialBgAudioController extends AbstractSocialAppBaseController {
    private Logger logger = Logger.getLogger(SocialBgAudioController.class);
    private static final int SOCIAL_BGAUDIO_PAGE_SIZE = 30;

    @Resource(name = "socialClientWebLogic")
    private SocialClientWebLogic socialClientWebLogic;

    @ResponseBody
    @RequestMapping(value = "/list")
    public String list(HttpServletRequest request, HttpServletResponse response,
                       @RequestParam(value = "platform", required = false) String platformStr,
                       @RequestParam(value = "startid", required = false, defaultValue = "0") String startNumStr,
                       @RequestParam(value = "isnext", required = false, defaultValue = "true") String isNext
    ) {
        ResultObjectMsg resultMsg = new ResultObjectMsg();

        if (StringUtil.isEmpty(platformStr)) {
            resultMsg.setRs(ResultCodeConstants.SOCIAL_PLATFORM_IS_NULL.getCode());
            resultMsg.setMsg(ResultCodeConstants.SOCIAL_PLATFORM_IS_NULL.getMsg());
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        int platform = Integer.valueOf(platformStr);

        int startNum = 0;
        try {
            startNum = Integer.parseInt(startNumStr);
        } catch (NumberFormatException e) {
        }

        Boolean next = Boolean.valueOf(isNext);
        NextPagination nextPagination = new NextPagination(startNum, SOCIAL_BGAUDIO_PAGE_SIZE, next);
        try {
            NextPageRows<BgAudioDTO> result = socialClientWebLogic.querySocialBgAudio(platform, nextPagination);
            if (result == null) {
                resultMsg.setRs(ResultCodeConstants.SUCCESS.getCode());
                resultMsg.setMsg("result.is.empty");
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }
            resultMsg.setResult(result);
            resultMsg.setRs(ResultCodeConstants.SUCCESS.getCode());
            resultMsg.setMsg("success");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            resultMsg.setRs(ResultObjectMsg.CODE_E);
            resultMsg.setMsg("system.error");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
    }

}
