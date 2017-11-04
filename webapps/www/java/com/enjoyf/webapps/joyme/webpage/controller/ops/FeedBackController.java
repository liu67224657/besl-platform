package com.enjoyf.webapps.joyme.webpage.controller.ops;

import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * <p/>
 * Description:意见相关的ACTION
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
@Controller
@RequestMapping(value = "/ops")
public class FeedBackController extends BaseRestSpringController {
    private Logger logger = LoggerFactory.getLogger(FeedBackController.class);
    @RequestMapping(value = "/feedback")
    public ModelAndView reply(HttpServletRequest request) {
        return new ModelAndView("/views/jsp/feedback/feedback");
    }
}
