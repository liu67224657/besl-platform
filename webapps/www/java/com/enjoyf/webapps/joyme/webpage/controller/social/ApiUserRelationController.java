package com.enjoyf.webapps.joyme.webpage.controller.social;

import com.enjoyf.platform.service.social.ObjectRelationType;
import com.enjoyf.platform.service.social.SocialServiceSngl;
import com.enjoyf.platform.service.social.UserRelation;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-9-25
 * Time: 上午9:43
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/api/userrelation")
public class ApiUserRelationController extends BaseRestSpringController {
    private static final Logger logger = LoggerFactory.getLogger(ApiUserRelationController.class);

    private JsonBinder binder = JsonBinder.buildNormalBinder();

    /**
     * 用户隐私查询接口
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/home")
    @ResponseBody
    public String home(@RequestParam(value = "srcProfileId") String srcProfileId,
                       @RequestParam(value = "destProfileId") String destProfileId, HttpServletRequest request) {
        try {
           if (!StringUtil.isEmpty(srcProfileId) && !StringUtil.isEmpty(destProfileId)){
               UserRelation userRelation = SocialServiceSngl.get().getRelation(srcProfileId,destProfileId, ObjectRelationType.WIKI_PROFILE);
               if (userRelation != null){
                   JSONObject jsonObject=  ResultCodeConstants.SUCCESS.getJsonObject();
                   jsonObject.put("result",userRelation);
                   return jsonObject.toString();
               }
           }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultCodeConstants.ERROR.getJsonString();
    }


}
