package com.enjoyf.webapps.tools.webpage.controller.advertise;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.advertise.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.google.gdata.util.common.base.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
@Controller
@RequestMapping(value = "/advertise/publish")
public class AdvertisePublishController extends AdvertiseBaseController {

    @RequestMapping(value = "/list")
    public ModelAndView publishList(@RequestParam(value = "projectid", required = false) String projectId,
                                    @RequestParam(value = "agentid", required = false) String agentId,
                                    @RequestParam(value = "publishname", required = false) String publishName,
                                    @RequestParam(value = "validstatus", required = false) String validStatus,
                                    @RequestParam(value = "items", required = false, defaultValue = "0") Integer items,
                                    @RequestParam(value = "pager.offset", required = false, defaultValue = "0") Integer pagerOffset,
                                    @RequestParam(value = "maxPageItems", required = false, defaultValue = "1") Integer maxPageItems) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        QueryExpress queryExpress = new QueryExpress()
                .add(QuerySort.add(AdvertisePublishField.CREATEDATE, QuerySortOrder.DESC));
        if (!StringUtil.isEmpty(validStatus)) {
            queryExpress.add(QueryCriterions.eq(AdvertisePublishField.VALIDSTATUS, validStatus));
        }
        if (!StringUtil.isEmpty(projectId)) {
            queryExpress.add(QueryCriterions.eq(AdvertisePublishField.PROJECTID, projectId.trim()));
        }
        if (!StringUtil.isEmpty(agentId)) {
            queryExpress.add(QueryCriterions.eq(AdvertisePublishField.AGENTID, agentId.trim()));
        }
        if (!StringUtil.isEmpty(publishName)) {
            queryExpress.add(QueryCriterions.like(AdvertisePublishField.PUBLISHNAME, "%" + publishName.trim() + "%"));
        }


        PageRows<AdvertisePublish> advertisePublishList = new PageRows<AdvertisePublish>();
        Pagination page = new Pagination(items, (pagerOffset / maxPageItems) + 1, WebappConfig.get().getHomePageSize());
        List<AdvertiseAgent> agentList = new ArrayList<AdvertiseAgent>();
        List<AdvertiseProject> projectList = new ArrayList<AdvertiseProject>();
        try {
            advertisePublishList = AdvertiseServiceSngl.get().queryPagePublishsByState(queryExpress, page, true);
            agentList = AdvertiseServiceSngl.get().queryAgents(new QueryExpress());
            projectList = AdvertiseServiceSngl.get().queryProjects(new QueryExpress());
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured Service Exception.e:", e);

            mapMessage.put("errorMsgMap", putErrorMap("error", "error.exception"));
            return new ModelAndView("/advertise/publishlist", mapMessage);
        }

        mapMessage.put("publishList", advertisePublishList.getRows());
        mapMessage.put("agentId", agentId);
        mapMessage.put("projectId", projectId);
        mapMessage.put("publishName", publishName);

        mapMessage.put("agentList", agentList);
        mapMessage.put("projectList", projectList);

        mapMessage.put("validStatus", validStatus);
        mapMessage.put("source", "publish");
        mapMessage.put("page", advertisePublishList.getPage());
        mapMessage.put("rows", advertisePublishList.getRows());
        mapMessage.put("items", advertisePublishList.getPage().getTotalRows());
        mapMessage.put("maxPageItems", advertisePublishList.getPage().getMaxPage());
        mapMessage.put("pagerOffset", advertisePublishList.getPage().getCurPage());

        return new ModelAndView("/advertise/publishlist", mapMessage);
    }

    @RequestMapping(value = "/listbyproject")
    public ModelAndView publishListByProject(@RequestParam(value = "projectid") String projectId,
                                             @RequestParam(value = "agentid", required = false) String agentId,
                                             @RequestParam(value = "publishname", required = false) String publishName,
                                             @RequestParam(value = "validstatus", required = false) String validStatus,
                                             @RequestParam(value = "items", required = false, defaultValue = "0") Integer items,
                                             @RequestParam(value = "pager.offset", required = false, defaultValue = "0") Integer pagerOffset,
                                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "1") Integer maxPageItems) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        AdvertiseProject project = new AdvertiseProject();
        try {
            project = AdvertiseServiceSngl.get().getProject(projectId);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured Service Exception.e:", e);
            mapMessage.put("errorMsgMap", putErrorMap("error", "error.exception"));
            return new ModelAndView("/advertise/agentlist", mapMessage);
        }

        mapMessage.put("source", "project");

        List<AdvertiseAgent> agentList = new ArrayList<AdvertiseAgent>();
        try {
            agentList = AdvertiseServiceSngl.get().queryAgents(new QueryExpress());
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured Service Exception.e:", e);
            mapMessage.put("errorMsgMap", putErrorMap("error", "error.exception"));
            return new ModelAndView("/advertise/agentlist", mapMessage);
        }

        QueryExpress queryExpress = new QueryExpress()
                .add(QueryCriterions.eq(AdvertisePublishField.PROJECTID, projectId))
                .add(QuerySort.add(AdvertisePublishField.CREATEDATE, QuerySortOrder.DESC));
        if (!StringUtil.isEmpty(validStatus)) {
            queryExpress.add(QueryCriterions.eq(AdvertisePublishField.VALIDSTATUS, validStatus));
        }
        if (!StringUtil.isEmpty(publishName)) {
            queryExpress.add(QueryCriterions.like(AdvertisePublishField.PUBLISHNAME, "%" + publishName.trim() + "%"));
        }
        if (!StringUtil.isEmpty(agentId)) {
            queryExpress.add(QueryCriterions.eq(AdvertisePublishField.AGENTID, agentId.trim()));
        }

        PageRows<AdvertisePublish> advertisePublishList = new PageRows<AdvertisePublish>();
        Pagination page = new Pagination(items, (pagerOffset / maxPageItems) + 1, WebappConfig.get().getHomePageSize());

        try {

            AdvertiseProject advertiseProjectjet = AdvertiseServiceSngl.get().getProject(projectId);
            mapMessage.put("project", advertiseProjectjet);

            advertisePublishList = AdvertiseServiceSngl.get().queryPagePublishsByState(queryExpress, page, true);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured Service Exception.e:", e);

            mapMessage.put("errorMsgMap", putErrorMap("error", "error.exception"));
            return new ModelAndView("/advertise/publishlist", mapMessage);
        }

        mapMessage.put("publishList", advertisePublishList.getRows());
        mapMessage.put("project", project);
        mapMessage.put("agentList", agentList);

        mapMessage.put("validStatus", validStatus);
        mapMessage.put("publishName", publishName);
        mapMessage.put("projectId", projectId);
        mapMessage.put("agentId", agentId);

        mapMessage.put("page", advertisePublishList.getPage());

        mapMessage.put("items", page.getTotalRows());
        mapMessage.put("maxPageItems", page.getMaxPage());
        mapMessage.put("pagerOffset", page.getCurPage());

        return new ModelAndView("/advertise/publishlistbyproject", mapMessage);
    }
    @RequestMapping(value = "/publishlistbyproject")
    public ModelAndView publishlistbyproject(@RequestParam(value = "projectid") String projectId,
                                             @RequestParam(value = "agentid", required = false) String agentId,
                                             @RequestParam(value = "publishname", required = false) String publishName,
                                             @RequestParam(value = "validstatus", required = false) String validStatus,
                                             @RequestParam(value = "items", required = false, defaultValue = "0") Integer items,
                                             @RequestParam(value = "pager.offset", required = false, defaultValue = "0") Integer pagerOffset,
                                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "1") Integer maxPageItems) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        AdvertiseProject project = new AdvertiseProject();
        try {
            project = AdvertiseServiceSngl.get().getProject(projectId);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured Service Exception.e:", e);
            mapMessage.put("errorMsgMap", putErrorMap("error", "error.exception"));
            return new ModelAndView("/advertise/agentlist", mapMessage);
        }

        mapMessage.put("source", "project");

        List<AdvertiseAgent> agentList = new ArrayList<AdvertiseAgent>();
        try {
            agentList = AdvertiseServiceSngl.get().queryAgents(new QueryExpress());
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured Service Exception.e:", e);
            mapMessage.put("errorMsgMap", putErrorMap("error", "error.exception"));
            return new ModelAndView("/advertise/agentlist", mapMessage);
        }

        QueryExpress queryExpress = new QueryExpress()
                .add(QueryCriterions.eq(AdvertisePublishField.PROJECTID, projectId))
                .add(QuerySort.add(AdvertisePublishField.CREATEDATE, QuerySortOrder.DESC));
        if (!StringUtil.isEmpty(validStatus)) {
            queryExpress.add(QueryCriterions.eq(AdvertisePublishField.VALIDSTATUS, validStatus));
        }
        if (!StringUtil.isEmpty(publishName)) {
            queryExpress.add(QueryCriterions.like(AdvertisePublishField.PUBLISHNAME, "%" + publishName.trim() + "%"));
        }
        if (!StringUtil.isEmpty(agentId)) {
            queryExpress.add(QueryCriterions.eq(AdvertisePublishField.AGENTID, agentId.trim()));
        }

        PageRows<AdvertisePublish> advertisePublishList = new PageRows<AdvertisePublish>();
        Pagination page = new Pagination(items, (pagerOffset / maxPageItems) + 1, WebappConfig.get().getHomePageSize());

        try {

            AdvertiseProject advertiseProjectjet = AdvertiseServiceSngl.get().getProject(projectId);
            mapMessage.put("project", advertiseProjectjet);

            advertisePublishList = AdvertiseServiceSngl.get().queryPagePublishsByState(queryExpress, page, true);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured Service Exception.e:", e);

            mapMessage.put("errorMsgMap", putErrorMap("error", "error.exception"));
            return new ModelAndView("/advertise/publishlist", mapMessage);
        }

        mapMessage.put("publishList", advertisePublishList.getRows());
        mapMessage.put("project", project);
        mapMessage.put("agentList", agentList);

        mapMessage.put("validStatus", validStatus);
        mapMessage.put("publishName", publishName);
        mapMessage.put("projectId", projectId);
        mapMessage.put("agentId", agentId);

        mapMessage.put("page", advertisePublishList.getPage());

        mapMessage.put("items", page.getTotalRows());
        mapMessage.put("maxPageItems", page.getMaxPage());
        mapMessage.put("pagerOffset", page.getCurPage());

        return new ModelAndView("/advertise/publishprojectlist", mapMessage);
    }

    @RequestMapping(value = "/listbyagent")
    public ModelAndView listbyagent(@RequestParam(value = "agentid") String agentId,
                                           @RequestParam(value = "publishname", required = false) String publishName,
                                           @RequestParam(value = "projectid", required = false) String projectId,
                                           @RequestParam(value = "validstatus", required = false) String validStatus,
                                           @RequestParam(value = "items", required = false, defaultValue = "0") Integer items,
                                           @RequestParam(value = "pager.offset", required = false, defaultValue = "0") Integer pagerOffset,
                                           @RequestParam(value = "maxPageItems", required = false, defaultValue = "1") Integer maxPageItems) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("source", "agent");
        AdvertiseAgent agent = new AdvertiseAgent();
        try {
            agent = AdvertiseServiceSngl.get().getAgent(agentId);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured Service Exception.e:", e);

            mapMessage.put("errorMsgMap", putErrorMap("error", "error.exception"));
            return new ModelAndView("/advertise/publishlist", mapMessage);
        }
        mapMessage.put("agent", agent);
        mapMessage.put("agentId", agentId);

        return new ModelAndView("/advertise/publishlistbyagent", mapMessage);
    }
    @RequestMapping(value = "/publishlistbyagent")
    public ModelAndView publishlistbyagent(@RequestParam(value = "agentid") String agentId,
                                           @RequestParam(value = "publishname", required = false) String publishName,
                                           @RequestParam(value = "projectid", required = false) String projectId,
                                           @RequestParam(value = "validstatus", required = false) String validStatus,
                                           @RequestParam(value = "items", required = false, defaultValue = "0") Integer items,
                                           @RequestParam(value = "pager.offset", required = false, defaultValue = "0") Integer pagerOffset,
                                           @RequestParam(value = "maxPageItems", required = false, defaultValue = "1") Integer maxPageItems) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("source", "agent");

        QueryExpress queryExpress = new QueryExpress()
                .add(QueryCriterions.eq(AdvertisePublishField.AGENTID, agentId))
                .add(QuerySort.add(AdvertisePublishField.CREATEDATE, QuerySortOrder.DESC));
        if (!StringUtil.isEmpty(publishName)) {
            queryExpress.add(QueryCriterions.like(AdvertisePublishField.PUBLISHNAME, "%" + publishName.trim() + "%"));
        }
        if (!StringUtil.isEmpty(validStatus)) {
            queryExpress.add(QueryCriterions.eq(AdvertisePublishField.VALIDSTATUS, validStatus.trim()));
        }
        if (!StringUtil.isEmpty(projectId)) {
            queryExpress.add(QueryCriterions.eq(AdvertisePublishField.PROJECTID, projectId.trim()));
        }

        PageRows<AdvertisePublish> advertisePublishList = new PageRows<AdvertisePublish>();
        AdvertiseAgent agent = new AdvertiseAgent();
        List<AdvertiseProject> projectList = new ArrayList<AdvertiseProject>();
        Pagination page = new Pagination(items, (pagerOffset / maxPageItems) + 1, WebappConfig.get().getHomePageSize());
        try {
            agent = AdvertiseServiceSngl.get().getAgent(agentId);
            projectList = AdvertiseServiceSngl.get().queryProjects(new QueryExpress());
            advertisePublishList = AdvertiseServiceSngl.get().queryPagePublishsByState(queryExpress, page, true);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured Service Exception.e:", e);

            mapMessage.put("errorMsgMap", putErrorMap("error", "error.exception"));
            return new ModelAndView("/advertise/publishlist", mapMessage);
        }

        mapMessage.put("publishList", advertisePublishList.getRows());
        mapMessage.put("agent", agent);

        mapMessage.put("publishName", publishName);
        mapMessage.put("projectId", projectId);
        mapMessage.put("validStatus", validStatus);
        mapMessage.put("projectList", projectList);

        mapMessage.put("agentId", agentId);
        mapMessage.put("page", advertisePublishList.getPage());
        mapMessage.put("rows", advertisePublishList.getRows());
        mapMessage.put("items", page.getTotalRows());
        mapMessage.put("maxPageItems", page.getMaxPage());
        mapMessage.put("pagerOffset", page.getCurPage());
        return new ModelAndView("/advertise/publishagentlist", mapMessage);
    }


    @RequestMapping(value = "/addpage")
    public ModelAndView addProjectPage(@RequestParam(value = "agentid", required = false) String agentId,
                                       @RequestParam(value = "projectid", required = false) String projectId,
                                       @RequestParam(value = "source", required = false, defaultValue = "publish") String source) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            if (!StringUtil.isEmpty(agentId)) {
                AdvertiseAgent advertiseAgent = AdvertiseServiceSngl.get().getAgent(agentId);
                mapMessage.put("agent", advertiseAgent);
            } else {
                QueryExpress queryExpress = new QueryExpress()
                        .add(QueryCriterions.eq(AdvertiseAgentField.VALIDSTATUS, ValidStatus.VALID.getCode()))
                        .add(QuerySort.add(AdvertiseAgentField.CREATEDATE, QuerySortOrder.DESC));
                List<AdvertiseAgent> advertiseAgentList = AdvertiseServiceSngl.get().queryAgents(queryExpress);
                mapMessage.put("agentList", advertiseAgentList);
            }

            if (!StringUtil.isEmpty(projectId)) {
                AdvertiseProject advertiseProjectjet = AdvertiseServiceSngl.get().getProject(projectId);
                mapMessage.put("project", advertiseProjectjet);
                source = "project";
            } else {
                QueryExpress queryExpress = new QueryExpress()
                        .add(QueryCriterions.eq(AdvertisePublishField.VALIDSTATUS, ValidStatus.VALID.getCode()))
                        .add(QuerySort.add(AdvertisePublishField.CREATEDATE, QuerySortOrder.DESC));
                List<AdvertiseProject> advertiseProjectList = AdvertiseServiceSngl.get().queryProjects(queryExpress);
                mapMessage.put("projectList", advertiseProjectList);
            }

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured Service Exception.e:", e);

            mapMessage.put("errorMsgMap", putErrorMap("error", "error.exception"));
            return new ModelAndView("/advertise/publishlist", mapMessage);
        }

        mapMessage.put("source", source);
        return new ModelAndView("/advertise/publishaddpage", mapMessage);
    }

    @RequestMapping(value = "/add")
    public ModelAndView addPublish(@RequestParam(value = "publishname") String publishName,
                                   @RequestParam(value = "publishdesc", required = false) String publishDesc,
                                   @RequestParam(value = "agentid") String agentId,
                                   @RequestParam(value = "projectid") String projectId,
                                   @RequestParam(value = "redirecturl") String redirectUrl,
                                   @RequestParam(value = "statenddate") Date statEndDate,
                                   @RequestParam(value = "source") String source) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        Map<String, String> errorMsgMap = new HashMap<String, String>();
        if (redirectUrl.startsWith("http://") || redirectUrl.startsWith("https://")) {
            mapMessage.put("errorMsgMap", putErrorMap("error", "error.advertise.publish.redriecturl.illegl"));
        }


        if (!CollectionUtil.isEmpty(errorMsgMap)) {
            return new ModelAndView("/advertise/projectaddpage", mapMessage);
        }


        AdvertisePublish advertisePublish = new AdvertisePublish();
        advertisePublish.setAgentId(agentId);
        advertisePublish.setProjectId(projectId);
        advertisePublish.setCreateDate(new Date());
        advertisePublish.setCreateIp(getIp());
        advertisePublish.setCreateUserid(getCurrentUser().getUsername());
        advertisePublish.setPublishDesc(publishDesc);
        advertisePublish.setPublishName(publishName);
        advertisePublish.setRedirectUrl(redirectUrl);
        advertisePublish.setStatEndDate(statEndDate);

        try {
            AdvertiseServiceSngl.get().createPublish(advertisePublish);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " add occured Service Exception.e:", e);

            mapMessage.put("errorMsgMap", putErrorMap("publishName", "error.exception"));
            return new ModelAndView("/advertise/publishaddpage", mapMessage);
        }
        return new ModelAndView(getPublishListBySource(source, agentId, projectId), mapMessage);
    }


    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyProjectPage(@RequestParam(value = "publishid", required = false) String publishId,
                                          @RequestParam(value = "modifyprojectid", required = false) String modifyProjectid,
                                          @RequestParam(value = "modifyagentid", required = false) String modifyAgentid,
                                          @RequestParam(value = "source", required = false) String source) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("source", source);

        AdvertisePublish advertisePublish = null;
        String agentId = "";
        String projectId = "";
        try {
            advertisePublish = AdvertiseServiceSngl.get().getPublish(publishId);

            if (advertisePublish == null || advertisePublish.getValidStatus().equals(ValidStatus.REMOVED)) {
                mapMessage.put("errorMsgMap", putErrorMap("error", "error.advertise.publish.notexists"));
                if (source.equals("publish")) {
                    return new ModelAndView("redirect:/advertise/publish/list", mapMessage);
                }
                if (source.equals("agent")) {
                    return new ModelAndView("redirect:/advertise/publish/publishlistbyagent?agentid=" + modifyAgentid, mapMessage);
                }
                if (source.equals("project")) {
                    return new ModelAndView("redirect:/advertise/publish/publishlistbyproject?projectid=" + modifyProjectid, mapMessage);
                }
            }


            AdvertiseAgent advertiseAgent = AdvertiseServiceSngl.get().getAgent(advertisePublish.getAgentId());
            AdvertiseProject advertiseProject = AdvertiseServiceSngl.get().getProject(advertisePublish.getProjectId());

            agentId = advertisePublish.getAgentId();
            projectId = advertisePublish.getPublishId();

            mapMessage.put("publish", advertisePublish);
            mapMessage.put("agent", advertiseAgent);
            mapMessage.put("project", advertiseProject);

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " modifyProjectPage occured Service Exception.e:", e);

            mapMessage.put("errorMsgMap", putErrorMap("error", "error.exception"));
            return new ModelAndView(getPublishListBySource(source, agentId, projectId), mapMessage);
        }


        return new ModelAndView("/advertise/publishmodifypage", mapMessage);
//          return new ModelAndView("redirect:/advertise/publish/listbyagent");
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modifyProject(@RequestParam(value = "publishid") String publishId,
                                      @RequestParam(value = "publishname") String publishName,
                                      @RequestParam(value = "publishdesc", required = false) String publishDesc,
                                      @RequestParam(value = "redirecturl") String redirectUrl,
                                      @RequestParam(value = "source") String source,
                                      @RequestParam(value = "agentid") String agentId,
                                      @RequestParam(value = "projectid") String projectId,
                                      @RequestParam(value = "validstatus", required = false) String validstatus) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("source", source);
        Map<String, String> errorMsgMap = new HashMap<String, String>();

        if (!CollectionUtil.isEmpty(errorMsgMap)) {
            mapMessage.put("errorMsgMap", putErrorMap("projectName", "error.exception"));
            return new ModelAndView("/advertise/projectmodifypage", mapMessage);
        }

        UpdateExpress updateExpress = new UpdateExpress().set(AdvertisePublishField.PUBLISHNAME, publishName)
                .set(AdvertisePublishField.PUBLISHDESC, publishDesc)
                .set(AdvertisePublishField.VALIDSTATUS, validstatus)
                .set(AdvertisePublishField.UPDATEDATE, new Date())
                .set(AdvertisePublishField.REDIRECTURL, redirectUrl)
                .set(AdvertisePublishField.UPDATEIP, getIp())
                .set(AdvertisePublishField.UPDATEUSERID, getCurrentUser().getUsername());

        try {
            AdvertiseServiceSngl.get().modifyPublish(updateExpress, publishId);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " modifyProject occured Service Exception.e:", e);

            mapMessage.put("errorMsgMap", putErrorMap("error", "error.exception"));
            return new ModelAndView(getPublishListBySource(source, agentId, projectId), mapMessage);
        }

        return new ModelAndView(getPublishListBySource(source, agentId, projectId), mapMessage);
    }

    @RequestMapping(value = "/detail")
    public ModelAndView detail(@RequestParam(value = "publishid") String publishId,
                               @RequestParam(value = "source", required = false) String source,
                               @RequestParam(value = "projectid", required = false) String projectId,
                               @RequestParam(value = "agentid", required = false) String agentId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        AdvertisePublish advertisePublish = null;
        try {
            advertisePublish = AdvertiseServiceSngl.get().getPublish(publishId);

            if (advertisePublish == null || advertisePublish.getValidStatus().equals(ValidStatus.REMOVED)) {
                mapMessage.put("errorMsgMap", putErrorMap("error", "error.advertise.publish.notexists"));
                if (source.equals("publish")) {
//                    return new ModelAndView("/advertise/publishdetail", mapMessage);
                    return new ModelAndView("redirect:/advertise/publish/list", mapMessage);
                }
                if (source.equals("project")) {
                    return new ModelAndView("redirect:/advertise/publish/publishlistbyproject?projectid=" + projectId, mapMessage);
                }
                if (source.equals("agent")) {
                    return new ModelAndView("redirect:/advertise/publish/publishlistbyagent?agentid=" + agentId, mapMessage);
                }
            }

            AdvertiseAgent advertiseAgent = AdvertiseServiceSngl.get().getAgent(advertisePublish.getAgentId());
            AdvertiseProject advertiseProject = AdvertiseServiceSngl.get().getProject(advertisePublish.getProjectId());
            mapMessage.put("publish", advertisePublish);
            mapMessage.put("agent", advertiseAgent);
            mapMessage.put("project", advertiseProject);
            mapMessage.put("source", source);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " modifyProjectPage occured Service Exception.e:", e);

            mapMessage.put("errorMsgMap", putErrorMap("error", "error.exception"));
        }

        return new ModelAndView("/advertise/publishdetail", mapMessage);
    }


    private String getPublishListBySource(String source, String agentId, String projectId) {
        if (source.equals("agent")) {
            return "redirect:/advertise/publish/publishlistbyagent?agentid=" + agentId;
        } else if (source.equals("project")) {
            return "redirect:/advertise/publish/publishlistbyproject?projectid=" + projectId;
        } else {
            return "redirect:/advertise/publish/list";
        }
    }
}
