package com.enjoyf.webapps.joyme.webpage.controller.joymeapp.gameclient.api;

import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.webapps.joyme.webpage.controller.joymeapp.gameclient.AbstractGameClientBaseController;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/12/29
 * Description:
 */
@Controller
@RequestMapping("/joymeapp/gameclient/api/article")
public class GameClientApiArticleController extends AbstractGameClientBaseController {

    //获取收藏列表
    @ResponseBody
    @RequestMapping(value = "/favoritelist")
    public String favoriteList() {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rs", ResultCodeConstants.SUCCESS.getCode());
        jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());

        Map map = new HashMap();
        map.put("rows", Collections.EMPTY_LIST);
        map.put("page", new Pagination(30, 1, 30));
        jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
        jsonObject.put("result", map);
        return jsonObject.toString();
    }

    //喜欢收藏列表
    @ResponseBody
    @RequestMapping(value = "/favorite")
    public String favorite() {
        return ResultCodeConstants.SUCCESS.getJsonString();
    }

    //取消喜欢收藏列表
    @ResponseBody
    @RequestMapping(value = "/unfavorite")
    public String unFavorite() {
        return ResultCodeConstants.SUCCESS.getJsonString();
    }
}
