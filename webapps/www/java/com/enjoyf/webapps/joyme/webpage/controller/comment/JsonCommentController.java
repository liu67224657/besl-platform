package com.enjoyf.webapps.joyme.webpage.controller.comment;

import com.enjoyf.platform.crypto.MD5Util;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.comment.*;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.service.usercenter.UserCenterUtil;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.platform.webapps.common.wordfilter.ContextFilterUtils;
import com.enjoyf.util.CookieUtil;
import com.enjoyf.webapps.joyme.dto.comment.*;
import com.enjoyf.webapps.joyme.dto.vote.JSONWikiVote;
import com.enjoyf.webapps.joyme.dto.vote.JSONWikiVoteSet;
import com.enjoyf.webapps.joyme.weblogic.comment.AllowCommentStatus;
import com.enjoyf.webapps.joyme.weblogic.comment.CommentFactory;
import com.enjoyf.webapps.joyme.weblogic.comment.CommentWebLogic;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-11-10
 * Time: 下午5:20
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/jsoncomment/bean")
public class JsonCommentController extends AbstractCommentController {
    /**
     * 主楼列表
     *
     * @param request
     * @param response
     * @param uniKey
     * @return
     */
    @ResponseBody
    @RequestMapping("/get")
    public String get(HttpServletRequest request, HttpServletResponse response,
                      @RequestParam(value = "unikey", required = false) String uniKey
    ) {
        String callback = HTTPUtil.getParam(request, "callback");
        try {
            CommentBean commentBean = CommentServiceSngl.get().getCommentBeanByUniKey(uniKey);
            if (commentBean != null) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("rs", "1");
                jsonObject.put("msg", "success");
                jsonObject.put("result", commentBean);
                if (StringUtil.isEmpty(callback)) {
                    return jsonObject.toString();
                } else {
                    return callback + "([" + jsonObject.toString() + "])";
                }
            } else {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("rs", "1");
                jsonObject.put("msg", "success");
                if (StringUtil.isEmpty(callback)) {
                    return jsonObject.toString();
                } else {
                    return callback + "([" + jsonObject.toString() + "])";
                }
            }
        } catch (Exception e) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("rs", "0");
            jsonObject.put("msg", "system.error");
            if (StringUtil.isEmpty(callback)) {
                return jsonObject.toString();
            } else {
                return callback + "([" + jsonObject.toString() + "])";
            }
        }
    }
}
