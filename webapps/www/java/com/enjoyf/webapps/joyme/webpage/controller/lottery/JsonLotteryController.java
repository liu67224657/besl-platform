package com.enjoyf.webapps.joyme.webpage.controller.lottery;

import com.enjoyf.platform.util.log.GAlerter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-7-18
 * Time: 下午5:16
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/json/lottery")
public class JsonLotteryController extends AbstractLotteryBaseController {

    @RequestMapping("/lotteryawardlist")
    public ModelAndView list(HttpServletRequest request) {
        return null;
    }

    @RequestMapping("/extaction1")
    public ModelAndView extAction1(HttpServletRequest request) {
        return null;
    }

    @RequestMapping("/extaction2")
    public ModelAndView extAction2(HttpServletRequest request) {
        return null;
    }

    @RequestMapping("/extaction3")
    public ModelAndView extAction3(HttpServletRequest request) {
        return null;
    }


    @RequestMapping("/extaction4")
    public ModelAndView extAction4(HttpServletRequest request) {
        return null;
    }

    @RequestMapping("/extaction5")
    public ModelAndView extAction5(HttpServletRequest request) {
        return null;
    }


}
