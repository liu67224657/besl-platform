package com.enjoyf.webapps.joyme.webpage.controller.joymeapp.gameclient.api;

import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.webapps.joyme.webpage.controller.joymeapp.gameclient.AbstractGameClientBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/12/29
 * Description:
 */
@Controller
@RequestMapping("/joymeapp/gameclient/api/feedback")
public class GameClientApiFeedbackController extends AbstractGameClientBaseController {


    //获取收藏列表
    @ResponseBody
    @RequestMapping
    public String favoriteList() {
        return ResultCodeConstants.SUCCESS.getJsonString();
    }

}
