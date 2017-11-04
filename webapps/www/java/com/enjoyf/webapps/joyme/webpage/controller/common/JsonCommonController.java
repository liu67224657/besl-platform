package com.enjoyf.webapps.joyme.webpage.controller.common;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.webapps.joyme.webpage.cache.SysCommCache;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
@Controller
@RequestMapping(value = "/json/common")
public class JsonCommonController {

    @RequestMapping(value = "/getregion")
    @ResponseBody
    public String jsonGetProvience() {
       return JsonBinder.buildNormalBinder().toJson(SysCommCache.get().getRegionMap());
    }
}
