package com.enjoyf.webapps.joyme.webpage.controller.joymeapp.gameclient.api;

import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.platform.webapps.common.wordfilter.ContextFilterUtils;
import com.enjoyf.util.StringUtil;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Result;
import java.util.Set;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/6/30
 * Description:
 */
@Controller
@RequestMapping("/joymeapp/gameclient/api/verify")
public class GameClientApiVerifyController {

    private static final String POST_CONTENT = "1";   //发文章
    private static final String NICKNAME = "2";   //昵称
    private static final String SIGNATURE = "3";    //签名
    private static final String PLAY_GAMES = "4";       //正在玩的游戏

    @RequestMapping
    @ResponseBody
    public String comment(HttpServletRequest request, HttpServletResponse response) {
        String text = request.getParameter("text");
        String appkey = request.getParameter("appkey");
        String type = request.getParameter("type");

        if (StringUtil.isEmpty(text)) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        if (type.equals(POST_CONTENT)) {
            //文章 敏感词
            Set<String> postKeyword = ContextFilterUtils.getPostContainBlackList(text);
            if (!CollectionUtil.isEmpty(postKeyword)) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("rs", String.valueOf(ResultCodeConstants.CONTEXT_CONTENT_ILLEGAL.getCode()));
                jsonObject.put("msg", ResultCodeConstants.CONTEXT_CONTENT_ILLEGAL.getMsg());
                jsonObject.put("result", postKeyword);
                return jsonObject.toString();
            } else {
                return ResultCodeConstants.SUCCESS.getJsonString();
            }
        }


        return ResultCodeConstants.PARAM_EMPTY.getJsonString();
    }
}
