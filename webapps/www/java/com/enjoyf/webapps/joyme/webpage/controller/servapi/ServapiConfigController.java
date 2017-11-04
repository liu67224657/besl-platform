package com.enjoyf.webapps.joyme.webpage.controller.servapi;

import com.enjoyf.webapps.joyme.webpage.cache.SysCommCache;
import com.enjoyf.webapps.joyme.webpage.controller.auth.AbstractAuthController;
import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/servapi/config")
public class ServapiConfigController extends AbstractAuthController {

    @RequestMapping(value = "/cityinfo")
    @ResponseBody
    public String getCityConfig() {
        return new Gson().toJson(SysCommCache.get().getRegionMap());
    }


}
