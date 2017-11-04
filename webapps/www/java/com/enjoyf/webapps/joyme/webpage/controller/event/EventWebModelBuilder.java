package com.enjoyf.webapps.joyme.webpage.controller.event;

import org.springframework.web.servlet.ModelAndView;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-11-4 上午10:53
 * Description:
 */
interface EventWebModelBuilder {


    public ModelAndView buildModelAndView() throws Exception;
}
