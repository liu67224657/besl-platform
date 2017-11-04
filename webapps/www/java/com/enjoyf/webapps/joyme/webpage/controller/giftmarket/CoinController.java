package com.enjoyf.webapps.joyme.webpage.controller.giftmarket;

import com.enjoyf.webapps.joyme.weblogic.giftmarket.GiftMarketWebLogic;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-25
 * Time: 上午10:26
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/coin")
public class CoinController extends AbstractGiftMarketBaseController {

    @Resource(name = "giftMarketWebLogic")
    private GiftMarketWebLogic giftMarketWebLogic;

    @RequestMapping
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "p", required = false) Integer pageNo) {
        return new ModelAndView("redirect:/giftmarket");
    }

    @RequestMapping(value = "/{gid}")
    public ModelAndView detail(HttpServletRequest request, HttpServletResponse response,
                               @PathVariable(value = "gid") Long gid) {
        return new ModelAndView("redirect:/giftmarket");

    }

}