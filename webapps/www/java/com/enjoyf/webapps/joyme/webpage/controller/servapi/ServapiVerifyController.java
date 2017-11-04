package com.enjoyf.webapps.joyme.webpage.controller.servapi;

import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.TemplateHotdeployConfig;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.*;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.wordfilter.ContextFilterUtils;
import com.enjoyf.webapps.joyme.dto.profile.MobileCodeDTO;
import com.enjoyf.webapps.joyme.weblogic.profile.MobileVerifyWebLogic;
import com.enjoyf.webapps.joyme.webpage.util.Constant;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


@Controller
@RequestMapping("/servapi/verify/word")
public class ServapiVerifyController extends ServapiAbstractAuthController {

    private static final String POST_CONTENT = "1";   //发文章

    @ResponseBody
    @RequestMapping
    public String auth(HttpServletRequest request,
                       @RequestParam(value = "word", required = false) String word) {
        String type = request.getParameter("type");

        if (StringUtil.isEmpty(word)) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        if (StringUtil.isEmpty(type) || type.equals(POST_CONTENT)) {
            //文章 敏感词
            Set<String> postKeyword = ContextFilterUtils.getPostContainBlackList(word);
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
