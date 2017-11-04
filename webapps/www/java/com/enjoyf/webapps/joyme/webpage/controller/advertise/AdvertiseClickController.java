package com.enjoyf.webapps.joyme.webpage.controller.advertise;


import com.enjoyf.webapps.joyme.weblogic.advertise.AdvertiseWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 */
@Controller
@RequestMapping("/click")
public class AdvertiseClickController extends BaseRestSpringController {
    @Resource(name = "advertiseWebLogic")
    private AdvertiseWebLogic advertiseWebLogic;

    @RequestMapping(value = "/{publishId}")
    public ModelAndView click(HttpServletRequest request, HttpServletResponse response,
                              @PathVariable(value = "publishId") String publishId) {
        return new ModelAndView("redirect:" + advertiseWebLogic.getAdvertisePublishRedirectUrl(request, response, publishId, null));
    }

    @RequestMapping(value = "/{publishId}/{locationCode}")
    public ModelAndView clickWithLocation(HttpServletRequest request, HttpServletResponse response,
                                          @PathVariable(value = "publishId") String publishId,
                                          @PathVariable(value = "locationCode") String locationCode) {
        return new ModelAndView("redirect:" + advertiseWebLogic.getAdvertisePublishRedirectUrl(request, response, publishId, locationCode));
    }
}
