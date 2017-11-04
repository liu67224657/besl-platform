package com.enjoyf.webapps.tools.webpage.controller.customerservice;

import com.enjoyf.platform.service.profile.ProfileBlog;
import com.enjoyf.platform.service.profile.ProfileDetailField;
import com.enjoyf.platform.service.profile.ProfileServiceSngl;
import com.enjoyf.platform.service.profile.VerifyType;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: yujiaxiu
 * Date: 12-9-13
 * Time: 下午4:48
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/cs")
public class VerifyController extends ToolsBaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final Pattern PATTERN_BLOG_DOMAIN = Pattern.compile("people/([^/?#&]*)");

    @RequestMapping(value = "/verifypg")
    public ModelAndView getVerifyPage() {
        return new ModelAndView("verify/verifypage");
    }

    @RequestMapping(value = "/getp")
    public ModelAndView getProfile(@RequestParam(value = "screenname") String screenname, @RequestParam(value = "purl") String purl) {

        ProfileBlog profileBlog = null;
        Map<String, Object> msgMap = new HashMap<String, Object>();
        Map<String, String> errorMsgMap = new HashMap<String, String>();

        msgMap.put("errorMsgMap", errorMsgMap);
        msgMap.put("screenname", screenname);
        msgMap.put("purl", purl);

        try {
            if (!StringUtil.isEmpty(screenname)) {
                profileBlog = ProfileServiceSngl.get().getProfileBlogByScreenName(screenname);
            } else if (!StringUtil.isEmpty(purl)) {
                //todo webcommon 抽取抽象方法
//                if (purl.contains("http://")) {
//                    purl = purl.substring("http://".length(), purl.indexOf("."));
//                } else {
//                    int index = purl.indexOf(".");
//                    if (index > 0) {
//                        purl = purl.substring(0, index);
//                    }
//                }
                String domain = getDomainByUrl(purl);
                if (StringUtil.isEmpty(domain)) {
                    errorMsgMap.put("parameter", "error.parameter.null");
                    return new ModelAndView("verify/verifypage", msgMap);
                }

                profileBlog = ProfileServiceSngl.get().getProfileBlogByDomain(domain);
            } else {
                errorMsgMap.put("parameter", "error.parameter.null");
            }

            if (profileBlog != null) {
                msgMap.put("profileDetail", ProfileServiceSngl.get().getProfileByUno(profileBlog.getUno()).getDetail());
                msgMap.put("profileBlog", profileBlog);
            } else {
                errorMsgMap.put("parameter", "error.parameter.notexsit");
            }

        } catch (ServiceException e) {
            GAlerter.lab("There is an exception when get a Profile:", e);
        }

        return new ModelAndView("verify/verifypage", msgMap);

    }

    @RequestMapping(value = "/verify")
    public ModelAndView updateProfileDetail(@RequestParam(value = "vdesc") String vdesc, @RequestParam(value = "uno") String uno,
                                            @RequestParam(value = "vtype") String vtype) {
        ProfileBlog profileBlog = null;
        Map<String, Object> msgMap = new HashMap<String, Object>();
        Map<String, String> errorMsgMap = new HashMap<String, String>();

        msgMap.put("errorMsgMap", errorMsgMap);
        msgMap.put("vdesc", vdesc);
        msgMap.put("vtype", vtype);
        msgMap.put("uno", uno);

//        if(!StringUtil.isEmpty(uno) && !StringUtil.isEmpty(vtype)){
        Map<ObjectField, Object> map = new HashMap<ObjectField, Object>();

        map.put(ProfileDetailField.VERIFYSTATUS, VerifyType.getByCode(vtype).getCode());
        map.put(ProfileDetailField.VERIFYDESC, vdesc);
        try {
            ProfileServiceSngl.get().updateProfileDetail(uno, map);
            msgMap.put("result", "s");
        } catch (ServiceException e) {
            msgMap.put("result", "f");
        }
//        }

        return new ModelAndView("verify/verifypage", msgMap);
    }

    private String getDomainByUrl(String url) {
        Matcher matcher = PATTERN_BLOG_DOMAIN.matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }
}
