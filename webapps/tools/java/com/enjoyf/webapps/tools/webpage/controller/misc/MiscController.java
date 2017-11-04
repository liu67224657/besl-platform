package com.enjoyf.webapps.tools.webpage.controller.misc;

import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-8-15 下午2:23
 * Description:
 */
@Controller
@RequestMapping(value = "/tool/")
public class MiscController extends ToolsBaseController {

    @RequestMapping("/360/genjsobj")
    public ModelAndView page() {
        return new ModelAndView();
    }

    @RequestMapping("/360/page")
    public ModelAndView generatorPage() {
        return new ModelAndView();
    }


}
