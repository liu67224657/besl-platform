package com.enjoyf.webapps.image.webpage.controller.res;/**
 * Created by ericliu on 16/8/8.
 */

import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.http.HttpByteData;
import com.enjoyf.platform.util.http.HttpURLUtils;
import com.enjoyf.platform.util.log.GAlerter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 获取用户头像
 *
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/8/8
 */
@Controller
@RequestMapping(value = "/res/headicon/")
public class HeadIconController {
    private static final String DEFAULT_HEAD_MALE = "";
    private static final String DEFAULT_HEAD_FEMALE = "";

    @RequestMapping(value = "/{pid}")
    public ModelAndView getUserHeadIcon(@PathVariable String pid, HttpServletResponse response) {

        try {
            String icon = getIconByProfileAndSize(pid, "M");

            HttpByteData imageData = HttpURLUtils.getByte(icon, false);
            response.setHeader("Content-Type", imageData.getContentType());
            response.getOutputStream().write(imageData.getData());
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured e: ", e);
        }finally {
            try {
                response.getWriter().close();
                response.flushBuffer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @RequestMapping(value = "/{pid}_{size}")
    public ModelAndView getUserHeadIcon(@PathVariable String pid, @PathVariable String size, HttpServletResponse response) {

        try {
            String icon = getIconByProfileAndSize(pid, size);

            HttpByteData imageData = HttpURLUtils.getByte(icon, false);
            response.setHeader("Content-Type", imageData.getContentType());
            response.getOutputStream().write(imageData.getData());
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured e: ", e);
        }finally {
            try {
                response.getWriter().close();
                response.flushBuffer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private String getIconByProfileAndSize(String pid, String size) throws Exception {
        String head = DEFAULT_HEAD_MALE;
        try {
            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(pid);

            if (profile != null) {
                if (!StringUtil.isEmpty(profile.getIcon())) {
                    head = profile.getIcon();
                } else {
                    head = "0".equals(profile.getSex()) ? DEFAULT_HEAD_FEMALE : DEFAULT_HEAD_MALE;
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return head;
    }


}
