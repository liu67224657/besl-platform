package com.enjoyf.webapps.tools.webpage.controller.audit;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: yujiaxiu
 * Date: 11-11-29
 * Time: 下午6:56
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/audit/profile")
public class AuditProfileController extends ToolsBaseController {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    public static final int NUM_OF_INCREASE_DAY = -5;
    public static final long MILLISECOND_OF_DAY = 1000 * 60 * 60 * 24;


    @Resource(name = "i18nSource")
    private ResourceBundleMessageSource i18nSource;


    //博客列表
    @RequestMapping(value = "/profilelist")
    public ModelAndView queryProfileListForAudit(
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "createstartdate", required = false) String createStartDate,
            @RequestParam(value = "createenddate", required = false) String createEndDate,
            @RequestParam(value = "screenName", required = false) String screenName,
            @RequestParam(value = "userid", required = false) String userid,
            @RequestParam(value = "audit", required = false) String audited,
            @RequestParam(value = "sorttype", required = false) String sorttype,
            @RequestParam(value = "items", required = false, defaultValue = "0") int items,
            @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pagerOffset,
            @RequestParam(value = "maxPageItems", required = false, defaultValue = "1") int maxPageItems
    ) {

        Pagination page = new Pagination(items, (pagerOffset / maxPageItems) + 1, WebappConfig.get().getHomePageSize());



        return new ModelAndView("/profile/auditprofilelist");
    }


    //全选通过操作
    @RequestMapping(value = "/allpass")
    public ModelAndView auditMultipleProfiles(
            @RequestParam(value = "applyid") String[] unos,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "createstartdate", required = false) String createStartDate,
            @RequestParam(value = "createenddate", required = false) String createEndDate,
            @RequestParam(value = "screenName", required = false) String screenName,
            @RequestParam(value = "userid", required = false) String userid,
            @RequestParam(value = "audit", required = false) String audited,
            @RequestParam(value = "sorttype", required = false) String sorttype,
            @RequestParam(value = "items", required = false, defaultValue = "0") int items,
            @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pagerOffset,
            @RequestParam(value = "maxPageItems", required = false, defaultValue = "1") int maxPageItems
    ) {

        //页面Map
        Map<String, Object> mapMsg = new HashMap<String, Object>();
        return new ModelAndView("forward:/audit/profile/profilelist", mapMsg);

    }


    //进入禁止页面
    @RequestMapping(value = "/banpage")
    public ModelAndView banPage(
            @RequestParam(value = "uno", required = false) String uno,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "createstartdate", required = false) String createStartDate,
            @RequestParam(value = "createenddate", required = false) String createEndDate,
            @RequestParam(value = "screenName", required = false) String screenName,
            @RequestParam(value = "userid", required = false) String userid,
            @RequestParam(value = "audit", required = false) String audited,
            @RequestParam(value = "sorttype", required = false) String sorttype,
            @RequestParam(value = "items", required = false, defaultValue = "0") int items,
            @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pagerOffset,
            @RequestParam(value = "maxPageItems", required = false, defaultValue = "1") int maxPageItems) {
        if (logger.isDebugEnabled()) {
            logger.debug("uno :" + uno);
        }

        //页面Map
        Map<String, Object> mapMsg = new HashMap<String, Object>();


        return new ModelAndView("/profile/banprofile", mapMsg);
    }


    // 单个博客详细内容
    @RequestMapping(value = "/details")
    public ModelAndView detailsPage(
            @RequestParam(value = "uno", required = false) String uno
    ) {
        if (logger.isDebugEnabled()) {
            logger.debug("uno :" + uno);
        }

        Map<String, Object> mapMsg = new HashMap<String, Object>();
        return new ModelAndView("/profile/profiledetails", mapMsg);
    }


    // 单个博客对应的账号列表
    @RequestMapping(value = "/accountlist")
    public ModelAndView accountListPage(
            @RequestParam(value = "uno", required = false) String uno
    ) {
        if (logger.isDebugEnabled()) {
            logger.debug("uno :" + uno);
        }

        Map<String, Object> mapMsg = new HashMap<String, Object>();

        return new ModelAndView("/profile/accountlist", mapMsg);
    }


    // 禁止操作
    @RequestMapping(value = "/ban")
    public ModelAndView ban(
            @RequestParam(value = "uno", required = false) String uno,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "during", required = false) String during,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "createstartdate", required = false) String createStartDate,
            @RequestParam(value = "createenddate", required = false) String createEndDate,
            @RequestParam(value = "screenName", required = false) String screenName,
            @RequestParam(value = "userid", required = false) String userid,
            @RequestParam(value = "audit", required = false) String audited,
            @RequestParam(value = "sorttype", required = false) String sorttype,
            @RequestParam(value = "items", required = false, defaultValue = "0") int items,
            @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pagerOffset,
            @RequestParam(value = "maxPageItems", required = false, defaultValue = "1") int maxPageItems
    ) {


        if (true) {
            return new ModelAndView("profile/success");
        } else {
            return new ModelAndView("profile/failed");
        }
    }


    @RequestMapping(value = "/preupdatedomain")
    public ModelAndView preModifyProfileDomain(@RequestParam(value = "uno", required = true)String uno){
        if (logger.isDebugEnabled()) {
            logger.debug("uno :" + uno);
        }


        return new ModelAndView("/profile/modify_domain");
    }


    @RequestMapping(value = "/updatedomain")
    public ModelAndView modifyProfileDomain(@RequestParam(value = "uno", required = true)String uno,
                                            @RequestParam(value = "domainType", required = false)String domainType){

        return new ModelAndView("/profile/modify_domain");
    }

}
