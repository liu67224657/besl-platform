package com.enjoyf.webapps.tools.webpage.controller.usercenter;

import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.log.GAlerter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/11/20
 * Description:
 */
@Controller
@RequestMapping("/usercenter")
public class UserCenterController {

    @RequestMapping("/initpage")
    public ModelAndView initUidPage(
            @RequestParam(value = "start", required = false) String start,
            @RequestParam(value = "end", required = false) String end){
        Map map=new HashMap();

        try {
            map.put("uidLength", UserCenterServiceSngl.get().getUidPoolLength());
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName()+" occured ServiceException.e:",e);
        }

        map.put("start", start);
        map.put("end", end);

        return new ModelAndView("/usercenter/uidpage",map);
    }

    @RequestMapping("/init")
    public ModelAndView init(
            @RequestParam(value = "start", required = false) Long start,
            @RequestParam(value = "end", required = false) Long end){
        Map map=new HashMap();

        try {
            UserCenterServiceSngl.get().initUidPool(start, end);
            map.put("start", start);
            map.put("end", end);

            return new ModelAndView("/common/success",map);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName()+" occured init uid pool e:",e );
        }

        return new ModelAndView("/usercenter/uidpage",map);
    }

}
