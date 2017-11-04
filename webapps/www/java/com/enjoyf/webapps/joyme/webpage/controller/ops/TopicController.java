package com.enjoyf.webapps.joyme.webpage.controller.ops;

import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 11-9-16
 * Time: 下午4:56
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/ops/topic")
public class TopicController extends BaseRestSpringController {

    @RequestMapping
    public ModelAndView help(HttpServletRequest request) {
        return new ModelAndView("/views/jsp/topic/topic");
    }
}
