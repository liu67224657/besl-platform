package com.enjoyf.webapps.joyme.webpage.controller.lottery;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.account.AccountDomain;
import com.enjoyf.platform.service.lottery.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.webapps.joyme.weblogic.lottery.AllowLotteryStatus;
import com.enjoyf.webapps.joyme.weblogic.lottery.LotteryWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-7-18
 * Time: 下午5:16
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/lottery/360")
public class Lottery360Controller extends AbstractLotteryBaseController {

    Logger logger = LoggerFactory.getLogger(Lottery360Controller.class);

    private static final long WANGMENG_LOTTERY_ID = 10010l;

    @Resource(name = "lotteryWebLogic")
    private LotteryWebLogic lotteryWebLogic;

    @RequestMapping("/page")
    public ModelAndView page(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        return null;
    }

    @RequestMapping(value = "/{thirdCode}/bind")
    public ModelAndView bind(@PathVariable(value = "thirdCode") String thirdCode,
                             HttpServletRequest request,
                             HttpServletResponse response) {


        return null;
    }

    @RequestMapping
    public ModelAndView action(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        return null;
    }

    //跑马灯iframe
    @RequestMapping("/rewardlist")
    public ModelAndView rewardlist(HttpServletRequest request) {
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
