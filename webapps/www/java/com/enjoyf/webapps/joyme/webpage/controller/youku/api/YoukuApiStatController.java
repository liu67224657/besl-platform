package com.enjoyf.webapps.joyme.webpage.controller.youku.api;

import com.enjoyf.platform.util.log.GAlerter;
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
@RequestMapping("/youku/api/stat")
public class YoukuApiStatController {


    /**
     * gameid:      1373 gamedb的id
     * guid:        7066707c5bdc38af1621eaf94a6fe779  iOS7.0下可用，作为idfa的备用参数
     * idfa:        5F3DC82D-9967-40BE-84E1-6AD25D9031F1
     * locationid:  2
     * network:     WIFI
     * operator:    中国移动_46002
     * pid:         69b81504767483cf
     * source:      8
     * tabid:       8
     * vdid:        51BB4968-6B29-4BF7-BC3B-27E9EFE5FE0D
     * ver:         4.5.1
     * ouid:        9fad37b0a535be43cbc1a5bc990cd677b843bf27 作为取不到idfa时的备用
     *
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping("/idfa")
    public String statsIDFA(HttpServletRequest request, HttpServletResponse response) {
//        http://statis.api.3g.youku.com/openapi-wireless/statis/games/app_game_detail
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 1);
        jsonObject.put("status", "success");
        return jsonObject.toString();
    }


}
