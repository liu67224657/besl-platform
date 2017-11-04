package com.enjoyf.webapps.tools.webpage.controller.feint;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.serv.viewline.FeintCache;
import com.enjoyf.platform.service.profile.ProfileBlog;
import com.enjoyf.platform.service.profile.ProfileService;
import com.enjoyf.platform.service.profile.ProfileServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.viewline.ViewLineServiceSngl;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-7-4
 * Time: 下午5:53
 * To change this template use File | Settings | File Templates.
 */

@Controller
@RequestMapping(value = "/feint")
public class FeintController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping
    public ModelAndView feint(HttpServletRequest request) {
        logger.debug("进入 /feintcontent");
        return new ModelAndView("/feint/feintcontent") ;
    }

    @RequestMapping(value = "/addcontent")
    public ModelAndView addContent(HttpServletRequest request,
                                   @RequestParam(value = "url") String url,
                                   @RequestParam(value = "startDate") String startDate,
                                   @RequestParam(value = "count") Integer count){
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        FeintCache feintCache = new FeintCache();

        String contentId = "";
        String domain = "";
        if(!StringUtil.isEmpty(url)){
            String[] splits = url.split("/home/");
            String domainAndCid = splits[1];
            
            String[] strs = domainAndCid.split("/");
            domain = strs[0];
            contentId = strs[1];
              
        } else {
            mapMessage.put("message", "feint error");
            return new ModelAndView("/feint/feintcontent",mapMessage) ;
        }
        if(!StringUtil.isEmpty(domain)){
            try {
                ProfileBlog blog = ProfileServiceSngl.get().getProfileBlogByDomain(domain);
                feintCache.setContentUNO(blog.getUno());
                feintCache.setContentID(contentId);
            } catch (ServiceException e) {
                GAlerter.lab(this.getClass().getName() + " occurred Service Exception.e:", e);
                mapMessage.put("message", "feint error");
                return new ModelAndView("/feint/feintcontent",mapMessage) ;
            }
        } else {
            mapMessage.put("message", "feint error");
            return new ModelAndView("/feint/feintcontent",mapMessage) ;
        }

        feintCache.setCount(count!=null ? count : 0);

        if(StringUtil.isEmpty(startDate)){
            feintCache.setStartDate(new Date());
        }else {
            try {
                feintCache.setStartDate(DateUtil.formatStringToDate(startDate,DateUtil.DEFAULT_DATE_FORMAT2));
            } catch (ParseException e) {
                GAlerter.lab(this.getClass().getName() + " occured ParseException Exception.e:", e);
                feintCache.setStartDate(new Date());
            }
        }


        try {
            ViewLineServiceSngl.get().feintContentFavorite(contentId,feintCache);
            mapMessage.put("message", "feint success");
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured Service Exception.e:", e);
            mapMessage.put("message", "feint error");
        }

        logger.debug("进入 /feint/feintcontent");
        return new ModelAndView("/feint/feintcontent",mapMessage) ;
    }
}
