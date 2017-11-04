package com.enjoyf.webapps.joyme.webpage.controller.joymeapp.advertise;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.advertise.app.AppAdvertisePublishType;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.ResultListMsg;
import com.enjoyf.util.StringUtil;
import com.enjoyf.webapps.joyme.dto.joymeapp.advertise.AdvertiseDTO;
import com.enjoyf.webapps.joyme.weblogic.advertise.AppAdvertiseWebLogic;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  2014/6/10 11:37
 * Description:
 */
@Controller
@RequestMapping(value = "/joymeapp/app/advertise")
public class AppAdvertiseController {

    @Resource(name = "appAdvertiseWebLogic")
    private AppAdvertiseWebLogic appAdvertiseWebLogic;

    private JsonBinder jsonBinder = JsonBinder.buildNormalBinder();

    @ResponseBody
    @RequestMapping("/query")
    public String query(@RequestParam(value = "appkey", required = false) String appkey,
                        @RequestParam(value = "platform", required = false) String platform,
                        @RequestParam(value = "adtype", required = false) String adType,
                        @RequestParam(value = "channelid", required = false) String channelId,
                        @RequestParam(value = "access_token", required = false) String access_token,
                        @RequestParam(value = "token_secr", required = false) String token_secr) {
        ResultListMsg listMsg = new ResultListMsg(ResultListMsg.CODE_E);
        if (StringUtil.isEmpty(appkey)) {
            listMsg.setRs(ResultCodeConstants.PARAM_ISEMPTY.getCode());
            listMsg.setMsg(ResultCodeConstants.PARAM_ISEMPTY.getMsg());
            return jsonBinder.toJson(listMsg);
        }

        AppAdvertisePublishType publishType = null;
        if (StringUtil.isEmpty(adType)) {
            try {
                int type = Integer.parseInt(adType);
                publishType = AppAdvertisePublishType.getByCode(type);
            } catch (NumberFormatException e) {
            }
        }

        if (publishType == null) {
            listMsg.setRs(ResultCodeConstants.APP_WRONG_ADTYPE.getCode());
            listMsg.setMsg(ResultCodeConstants.APP_WRONG_ADTYPE.getMsg());
            return jsonBinder.toJson(listMsg);
        }

        try {
            List<AdvertiseDTO> dtoList = appAdvertiseWebLogic.queryAdvertise(appkey, publishType, new Date());
            listMsg.setResult(dtoList);
            listMsg.setRs(ResultListMsg.CODE_S);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            listMsg.setMsg("system.error");
        }

        return jsonBinder.toJson(listMsg);
    }


}
