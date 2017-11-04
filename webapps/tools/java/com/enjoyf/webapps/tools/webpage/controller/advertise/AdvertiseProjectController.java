package com.enjoyf.webapps.tools.webpage.controller.advertise;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.advertise.AdvertiseProject;
import com.enjoyf.platform.service.advertise.AdvertiseProjectField;
import com.enjoyf.platform.service.advertise.AdvertiseServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.google.gdata.util.common.base.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Calendar;
import java.util.Date;
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
@RequestMapping(value = "/advertise/project")
public class AdvertiseProjectController extends AdvertiseBaseController {
    public static final int NUM_OF_INCREASE_DAY = -5;

    @RequestMapping(value = "/list")
    public ModelAndView projectList(@RequestParam(value = "projectname", required = false) String projectName,
                                    @RequestParam(value = "startdate", required = false) String startDate,
                                    @RequestParam(value = "enddate", required = false) String endDate,
                                    @RequestParam(value = "statenddate", required = false) String statEndDate,
                                    @RequestParam(value = "validstatus", required = false) String validStatus,
                                    @RequestParam(value = "items", required = false, defaultValue = "0") Integer items,
                                    @RequestParam(value = "pager.offset", required = false, defaultValue = "0") Integer pagerOffset,
                                    @RequestParam(value = "maxPageItems", required = false, defaultValue = "1") Integer maxPageItems) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        QueryExpress queryExpress = new QueryExpress()
                .add(QuerySort.add(AdvertiseProjectField.CREATEDATE, QuerySortOrder.DESC));
        if (!StringUtil.isEmpty(projectName)) {
            queryExpress.add(QueryCriterions.like(AdvertiseProjectField.PROJECTNAME, "%" + projectName.trim() + "%"));
        }
        if (!StringUtil.isEmpty(validStatus)) {
            queryExpress.add(QueryCriterions.eq(AdvertiseProjectField.VALIDSTATUS, validStatus.trim()));
        }
        if (!StringUtil.isEmpty(startDate)) {
            queryExpress.add(QueryCriterions.geq(AdvertiseProjectField.STARTDATE, DateUtil.StringTodate(startDate, DateUtil.DATE_FORMAT)));
        }
        if (!StringUtil.isEmpty(endDate)) {
            queryExpress.add(QueryCriterions.leq(AdvertiseProjectField.ENDDATE, DateUtil.StringTodate(endDate, DateUtil.DATE_FORMAT)));
        }
        if (!StringUtil.isEmpty(statEndDate)) {
            queryExpress.add(QueryCriterions.leq(AdvertiseProjectField.STATENDDATE, DateUtil.StringTodate(statEndDate, DateUtil.DATE_FORMAT)));
        }

        Pagination page = new Pagination(items, (pagerOffset / maxPageItems) + 1, WebappConfig.get().getHomePageSize());

        PageRows<AdvertiseProject> advertiseProjectList = new PageRows<AdvertiseProject>();
        try {
            advertiseProjectList = AdvertiseServiceSngl.get().queryPageProjects(queryExpress, page);
            int str = advertiseProjectList.getRows().size();
            System.out.println("长度:" + str);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured Service Exception.e:", e);
            mapMessage.put("errorMsgMap", putErrorMap("error", "error.exception"));
            return new ModelAndView("/advertise/projectlist", mapMessage);
        }

        mapMessage.put("projectList", advertiseProjectList.getRows());
        mapMessage.put("page", advertiseProjectList.getPage());
        mapMessage.put("projectName", projectName);
        mapMessage.put("startDate", startDate);
        mapMessage.put("endDate", endDate);
        mapMessage.put("statEndDate", statEndDate);
        mapMessage.put("validStatus", validStatus);

        mapMessage.put("items", page.getTotalRows());
        mapMessage.put("maxPageItems", page.getMaxPage());
        mapMessage.put("pagerOffset", page.getCurPage());
        return new ModelAndView("/advertise/projectlist", mapMessage);
    }

    @RequestMapping(value = "/addpage")
    public ModelAndView addProjectPage() {
        return new ModelAndView("/advertise/projectaddpage");
    }

    @RequestMapping(value = "/add")
    public ModelAndView addProject(@RequestParam(value = "projectname") String projectName,
                                   @RequestParam(value = "projectdesc", required = false) String projectDesc,
                                   @RequestParam(value = "startdate", required = false) Date startDate,
                                   @RequestParam(value = "enddate", required = false) Date endDate,
                                   @RequestParam(value = "statstartdate", required = false) Date statStartDate,
                                   @RequestParam(value = "statenddate", required = false) Date statEndDate) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        Map<String, String> errorMsgMap = new HashMap<String, String>();
        if (StringUtil.isEmpty(projectName)) {
            errorMsgMap.putAll(putErrorMap("projectName", "error.advertise.project.name.notnull"));
        }

        if (startDate != null && endDate != null) {
            if (startDate.after(endDate)) {
                errorMsgMap.putAll(putErrorMap("projectDate", "error.advertise.project.startdate.after.enddate"));
            } else {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(endDate);
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                calendar.add(Calendar.MILLISECOND, -1);

                endDate = calendar.getTime();
            }
        }

        if (statStartDate != null && statEndDate != null) {
            if (statStartDate.after(statEndDate)) {
                errorMsgMap.putAll(putErrorMap("statDate", "error.advertise.project.statstartdate.after.statenddate"));
            } else {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(statEndDate);
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                calendar.add(Calendar.MILLISECOND, -1);

                statEndDate = calendar.getTime();
            }
        }

        if (!CollectionUtil.isEmpty(errorMsgMap)) {
            mapMessage.put("errorMsgMap", putErrorMap("projectName", "error.exception"));
            return new ModelAndView("/advertise/projectaddpage", mapMessage);
        }


        AdvertiseProject advertiseProject = new AdvertiseProject();
        advertiseProject.setProjectName(projectName);
        advertiseProject.setProjectDesc(projectDesc);
        advertiseProject.setStartDate(startDate);
        advertiseProject.setEndDate(endDate);
        advertiseProject.setStatStartDate(statStartDate);
        advertiseProject.setStatEndDate(statEndDate);
        advertiseProject.setCreateDate(new Date());
        advertiseProject.setCreateIp(this.getIp());
        advertiseProject.setCreateUserid(this.getCurrentUser().getUsername());

        try {
            AdvertiseServiceSngl.get().createProject(advertiseProject);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " add occured Service Exception.e:", e);

            mapMessage.put("errorMsgMap", putErrorMap("projectName", "error.exception"));
            return new ModelAndView("/advertise/projectaddpage", mapMessage);
        }

        return new ModelAndView("redirect:/advertise/project/list");
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyProjectPage(@RequestParam(value = "projectid") String projectid) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        AdvertiseProject advertiseProject = null;
        try {
            advertiseProject = AdvertiseServiceSngl.get().getProject(projectid);

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " modifyProjectPage occured Service Exception.e:", e);

            mapMessage.put("errorMsgMap", putErrorMap("error", "error.exception"));
            return new ModelAndView("redirect:/advertise/project/list", mapMessage);
        }

        if (advertiseProject == null || advertiseProject.getValidStatus().equals(ValidStatus.REMOVED)) {
            mapMessage.put("errorMsgMap", putErrorMap("error", "error.advertise.project.notexists"));
            return new ModelAndView("redirect:/advertise/project/list", mapMessage);
        } else {
            mapMessage.put("project", advertiseProject);
        }

        return new ModelAndView("/advertise/projectmodifypage", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modifyProject(@RequestParam(value = "projectid") String projectid,
                                      @RequestParam(value = "projectname") String projectName,
                                      @RequestParam(value = "projectdesc", required = false) String projectDesc,
                                      @RequestParam(value = "startdate", required = false) Date startDate,
                                      @RequestParam(value = "enddate", required = false) Date endDate,
                                      @RequestParam(value = "statstartdate", required = false) Date statStartDate,
                                      @RequestParam(value = "statenddate", required = false) Date statEndDate,
                                      @RequestParam(value = "validstatus", required = false) String validstatus) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        Map<String, String> errorMsgMap = new HashMap<String, String>();
        if (StringUtil.isEmpty(projectName)) {
            errorMsgMap.putAll(putErrorMap("projectName", "error.advertise.project.name.notnull"));
        }

        if (!CollectionUtil.isEmpty(errorMsgMap)) {
            mapMessage.put("errorMsgMap", putErrorMap("projectName", "error.exception"));
            return new ModelAndView("/advertise/projectmodifypage", mapMessage);
        }

        UpdateExpress updateExpress = new UpdateExpress().set(AdvertiseProjectField.PROJECTNAME, projectName)
                .set(AdvertiseProjectField.PROJECTDESC, projectDesc)
                .set(AdvertiseProjectField.VALIDSTATUS, validstatus)
                .set(AdvertiseProjectField.UPDATEDATE, new Date())
                .set(AdvertiseProjectField.UPDATEIP, getIp())
                .set(AdvertiseProjectField.UPDATEUSERID, getCurrentUser().getUsername())
                .set(AdvertiseProjectField.STARTDATE,startDate)
                .set(AdvertiseProjectField.ENDDATE,endDate)
                .set(AdvertiseProjectField.STATENDDATE,statEndDate);

        try {
            AdvertiseServiceSngl.get().modifyProject(updateExpress, projectid);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " modifyProject occured Service Exception.e:", e);

            mapMessage.put("errorMsgMap", putErrorMap("error", "error.exception"));
            return new ModelAndView("redirect:/advertise/project/list", mapMessage);
        }

        return new ModelAndView("redirect:/advertise/project/list");
    }


    @RequestMapping(value = "/remove")
    public ModelAndView removeAgent() {
        return null;
    }
}
