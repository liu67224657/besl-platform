package com.enjoyf.webapps.joyme.webpage.controller.app;


import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
@Controller
@RequestMapping("/app/taoyuan/simulation")
public class TaoyuanController extends BaseRestSpringController {


    @RequestMapping
    public ModelAndView talentcaluatorv() {


        return new ModelAndView("/views/jsp/app/ty-simulation");
    }
}
