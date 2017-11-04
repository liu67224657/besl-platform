package com.enjoyf.webapps.joyme.webpage.controller.dynamic;

import com.enjoyf.util.StringUtil;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: zhaoxin
 * Date: 11-10-11
 * Time: 上午9:01
 * Desc: 动态调用的action
 */
@Controller
public class DynamicURLController extends BaseRestSpringController {

    private static final String ROOTPATH_EVENT = "/views/jsp/event";
    private static final String ROOTPATH_HELP = "/views/jsp/help";
    private static final String ROOTPATH_ABOUT = "/views/jsp/about";
    private static final String ROOTPATH_SUBJECT = "/views/jsp/special";
    private static final String ROOTPATH_MOBILE = "/views/jsp/mobile";

    /**
     * 根动态调用
     *
     * @return
     */
    @RequestMapping(value = "/about")
    public String aboutRoot() {
        return "/views/jsp/about";
    }

    /**
     * 一层动态调用
     *
     * @param sPath
     * @return
     */
    @RequestMapping(value = "/about/{path}")
    public String aboutOne(@PathVariable(value = "path") String sPath) {
        return ROOTPATH_ABOUT + "/" + sPath;
    }

    /**
     * 二层动态调用
     *
     * @param sPath
     * @return
     */
    @RequestMapping(value = "/about/{path}/{path2}")
    public String aboutTwo(@PathVariable(value = "path") String sPath, @PathVariable(value = "path2") String sPath2) {
        return ROOTPATH_ABOUT + "/" + sPath + "/" + sPath2 + "";
    }

    /**
     * 三层动态调用
     *
     * @param sPath
     * @return
     */
    @RequestMapping(value = "/about/{path}/{path2}/{path3}")
    public String aboutThree(@PathVariable(value = "path") String sPath,
                             @PathVariable(value = "path2") String sPath2,
                             @PathVariable(value = "path3") String sPath3) {
        return ROOTPATH_ABOUT + "/" + sPath + "/" + sPath2 + "/" + sPath3;
    }


    // ---- help ---

    /**
     * 根动态调用
     *
     * @return
     */
    @RequestMapping(value = "/help")
    public String helpRoot() {
        return "/views/jsp/help/help";
    }

    /**
     * 一层动态调用
     *
     * @param sPath
     * @return
     */
    @RequestMapping(value = "/help/{path}")
    public String helpOne(@PathVariable(value = "path") String sPath) {
        return ROOTPATH_HELP + "/" + sPath;
    }

    /**
     * 二层动态调用
     *
     * @param sPath
     * @return
     */
    @RequestMapping(value = "/help/{path}/{path2}")
    public String helpTwo(@PathVariable(value = "path") String sPath, @PathVariable(value = "path2") String sPath2) {
        return ROOTPATH_HELP + "/" + sPath + "/" + sPath2 + "";
    }

    /**
     * 三层动态调用
     *
     * @param sPath
     * @return
     */
    @RequestMapping(value = "/help/{path}/{path2}/{path3}")
    public String helpThree(@PathVariable(value = "path") String sPath,
                            @PathVariable(value = "path2") String sPath2,
                            @PathVariable(value = "path3") String sPath3) {
        return ROOTPATH_HELP + "/" + sPath + "/" + sPath2 + "/" + sPath3;
    }

    /**
     * 一层动态调用
     *
     * @param sPath
     * @return
     */
//    @RequestMapping(value = "/event")
//    public String eventRoot(@PathVariable(value = "path") String sPath) {
//        return "/views/jsp/event/event";
//    }

    /**
     * 一层动态调用
     *
     * @param sPath
     * @return
     */
    @RequestMapping(value = "/event/{path}")
    public String eventOne(@PathVariable(value = "path") String sPath) {
        return ROOTPATH_EVENT + "/" + sPath;
    }

    /**
     * 二层动态调用
     *
     * @param sPath
     * @return
     */
    @RequestMapping(value = "/event/{path}/{path2}")
    public String eventTwo(@PathVariable(value = "path") String sPath, @PathVariable(value = "path2") String sPath2) {
        return ROOTPATH_EVENT + "/" + sPath + "/" + sPath2 + "";
    }

    /**
     * 三层动态调用
     *
     * @param sPath
     * @return
     */
    @RequestMapping(value = "/event/{path}/{path2}/{path3}")
    public String eventThree(@PathVariable(value = "path") String sPath,
                             @PathVariable(value = "path2") String sPath2,
                             @PathVariable(value = "path3") String sPath3) {
        return ROOTPATH_EVENT + "/" + sPath + "/" + sPath2 + "/" + sPath3;
    }


    /**
     * 根动态调用
     *
     * @return
     */
    @RequestMapping(value = "/about/job")
    public String jobRoot() {
        return "/views/jsp/job";
    }

    /**
     * 专题：一层动态调用
     *
     * @param sPath
     * @return
     */
    @RequestMapping(value = "/special/{path}")
    public String subjectOne(@PathVariable(value = "path") String sPath) {
        return ROOTPATH_SUBJECT + "/" + sPath;
    }

    /**
     * 专题：二层动态调用
     *
     * @param sPath
     * @return
     */
    @RequestMapping(value = "/special/{path}/{path2}")
    public String subjectTwo(@PathVariable(value = "path") String sPath, @PathVariable(value = "path2") String sPath2) {
        return ROOTPATH_SUBJECT + "/" + sPath + "/" + sPath2 + "";
    }

    /**
     * 专题：三层动态调用
     *
     * @param sPath
     * @return
     */
    @RequestMapping(value = "/special/{path}/{path2}/{path3}")
    public String subjectThree(@PathVariable(value = "path") String sPath,
                               @PathVariable(value = "path2") String sPath2,
                               @PathVariable(value = "path3") String sPath3) {
        return ROOTPATH_SUBJECT + "/" + sPath + "/" + sPath2 + "/" + sPath3;
    }


    /**
     * 下载动态地址
     *
     * @return
     */
    @RequestMapping(value = "/mobile")
    public String mobileDown() {
        return ROOTPATH_MOBILE + "/mobile";
    }


    /**
     * 下载动态地址一级
     *  http://www.joyme.com/mobile/iphone
     *  http://www.joyme.com/mobile/android
     * @return
     */
    @RequestMapping(value = "/mobile/{path}")
    public ModelAndView mobileOne(@PathVariable(value = "path") String sPath, @RequestParam(value = "code", required = false) String code) {
        Map map = new HashMap();
        if (!StringUtil.isEmpty("code")) {
            map.put("code", code);
        }
        return new ModelAndView(ROOTPATH_MOBILE + "/" + sPath, map);
    }


    /**
     * 下载动态地址二级
     *
     * @return
     */
    @RequestMapping(value = "/mobile/{path}/{path2}")
    public String mobileTwo(@PathVariable(value = "path") String sPath,
                          @PathVariable(value = "path2") String sPath2) {
        return ROOTPATH_MOBILE + "/" + sPath + "/" + sPath2;
    }


    /**
     * 下载动态地址二级
     *
     * @return
     */
    @RequestMapping(value = "/mobile/{path}/{path2}/{path3}")
    public String mobileThree(@PathVariable(value = "path") String sPath,
                            @PathVariable(value = "path2") String sPath2,
                            @PathVariable(value = "path3") String sPath3) {
        return ROOTPATH_MOBILE + "/" + sPath + "/" + sPath2 + "/" + sPath3;
    }


}
