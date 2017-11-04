package com.enjoyf.webapps.joyme.webpage.controller.task;

import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-12-31
 * Time: 下午6:55
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/joymetask")
public class JoymeTaskController {

    /**
     * 签到
     * @param uno
     * @param appKey
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/sign")
    public String sign(@RequestParam(value = "uno", required = false) String uno,
                       @RequestParam(value = "appkey", required = false) String appKey) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rs", ResultCodeConstants.SUCCESS.getCode());
        jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
        Map map = new HashMap();
        jsonObject.put("result", map);
        return jsonObject.toString();
    }


}
