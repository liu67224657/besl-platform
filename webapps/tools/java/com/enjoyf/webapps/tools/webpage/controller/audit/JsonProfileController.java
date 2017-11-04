package com.enjoyf.webapps.tools.webpage.controller.audit;

import com.enjoyf.platform.webapps.common.JoymeResultMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Created by IntelliJ IDEA.
 * User: yujiaxiu
 * Date: 11-12-28
 * Time: 下午2:07
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/json/profile")
public class JsonProfileController extends ToolsBaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    //@ResponseBody表示该方法返回的结果直接输入到ResponseBody中去
    @ResponseBody
    @RequestMapping("/delicon")
    public String jsonDeleteHeadIcon(@RequestParam(value = "uno") String uno,
                                     @RequestParam(value = "moreheadicon") String[] moreheadicons) {
        if (logger.isDebugEnabled()) {
            logger.debug("parameter from jsp : uno=" + uno);
        }

        JoymeResultMsg resultMsg = new JoymeResultMsg(JoymeResultMsg.CODE_E);


        return JsonBinder.buildNormalBinder().toJson(resultMsg);
    }


    //
    @ResponseBody
    @RequestMapping("/restore")
    public String jsonRestoreHeadIcon(
            @RequestParam(value = "uno") String uno,
            @RequestParam(value = "moreheadicon") String moreheadicon) {
        if (logger.isDebugEnabled()) {
            logger.debug("parameter from jsp : uno=" + uno);
        }

        JoymeResultMsg resultMsg = new JoymeResultMsg(JoymeResultMsg.CODE_E);

        return JsonBinder.buildNormalBinder().toJson(resultMsg);

    }

    @ResponseBody
    @RequestMapping("/delplayinggames")
    public String delplayinggames(@RequestParam(value = "uno") String uno,
                                  @RequestParam(value = "playinggames") String playinggames) {
        if (logger.isDebugEnabled()) {
            logger.debug("parameter from jsp: uno=" + uno);
        }

        JoymeResultMsg resultMsg = new JoymeResultMsg(JoymeResultMsg.CODE_E);
        return JsonBinder.buildNormalBinder().toJson(resultMsg);
    }


    //恢复描述
    @ResponseBody
    @RequestMapping("/restoredesc")
    public String jsonRestoreDesc(@RequestParam(value = "uno", required = false) String uno) {
        if (logger.isDebugEnabled()) {
            logger.debug("parameter from jsp: uno=" + uno);
        }

        JoymeResultMsg resultMsg = new JoymeResultMsg(JoymeResultMsg.CODE_E);
        return JsonBinder.buildNormalBinder().toJson(resultMsg);
    }
}
