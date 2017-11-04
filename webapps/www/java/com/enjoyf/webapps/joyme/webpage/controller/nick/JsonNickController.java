package com.enjoyf.webapps.joyme.webpage.controller.nick;

import com.enjoyf.platform.service.profile.ProfileBlog;
import com.enjoyf.platform.service.profile.ProfileServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.webapps.common.JoymeResultMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 11-10-14
 * Time: 上午9:26
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/json/nick")
public class JsonNickController {
    private static final Logger logger = LoggerFactory.getLogger(JsonNickController.class);

    /**
     * 根据昵称查询用户
     *
     * @param nick
     * @return
     */
    @RequestMapping("/clickat")
    @ResponseBody
    public String josnQueryBlogByNick(HttpServletRequest request, @RequestParam(value = "nick") String nick) {
        if (logger.isDebugEnabled()) {
            logger.debug("get at link by nick ====== " + nick);
        }

        List<ProfileBlog> rtlist = new ArrayList<ProfileBlog>();
        try {
            if (StringUtil.isEmpty(nick)) {
                return JsonBinder.buildNonNullBinder().toJson(new JoymeResultMsg(JoymeResultMsg.CODE_E, ""));
            }

            ProfileBlog blog = ProfileServiceSngl.get().getProfileBlogByScreenName(nick);
            if (blog != null) {
                rtlist.add(blog);
                return JsonBinder.buildNonNullBinder().toJson(new JoymeResultMsg(JoymeResultMsg.CODE_S, "", rtlist));
            }

            return JsonBinder.buildNonNullBinder().toJson(new JoymeResultMsg(JoymeResultMsg.CODE_E, ""));
        } catch (ServiceException e) {
            logger.error(this.getClass().getName() + " occured ServiceException: ", e);
            return JsonBinder.buildNonNullBinder().toJson(new JoymeResultMsg(JoymeResultMsg.CODE_E, ""));

        }
    }
}
