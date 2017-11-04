package com.enjoyf.webapps.joyme.webpage.base.mvc;

import com.enjoyf.platform.service.comment.CommentForbid;
import com.enjoyf.platform.service.comment.CommentServiceSngl;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.util.StringUtil;
import net.sf.json.JSONObject;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 禁言验证
 */
public class WanbaAskApiForbidInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String pid = HTTPUtil.getParam(request, "pid");
        JSONObject jsonObject = new JSONObject();
        if (!StringUtil.isEmpty(pid)) {
            //禁言
            CommentForbid forbid = CommentServiceSngl.get().getCommentForbidByCache(pid);
            if (forbid != null) {
                jsonObject.put("rs", ResultCodeConstants.COMMENT_PROFILE_FORBID.getCode());
                jsonObject.put("msg", ResultCodeConstants.COMMENT_PROFILE_FORBID.getMsg());
                HTTPUtil.writeJson(response, jsonObject.toString());
                return false;
            }
        }
        return true;
    }
}
