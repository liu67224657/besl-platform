package com.enjoyf.webapps.tools.webpage.controller.point;

import com.enjoyf.platform.service.point.*;
//import com.enjoyf.platform.service.profile.Profile;
//import com.enjoyf.platform.service.profile.ProfileBlog;
//import com.enjoyf.platform.service.profile.ProfileServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.service.tools.ToolsLog;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-8
 * Time: 上午10:06
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/point/pointactionhistory")
public class PointActionHistoryController extends ToolsBaseController {

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,      //数据库记录索引
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                             @RequestParam(value = "profilename", required = false) String profilename,
                             @RequestParam(value = "profileid", required = false) String profileId,
                             @RequestParam(value = "actionhistoryid", required = false) Long actionHistoryId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        if (StringUtil.isEmpty(profilename) && StringUtil.isEmpty(profileId)) {
            return new ModelAndView("/point/pointactionhistorylist", mapMessage);
        }

        try {
            Profile profile = null;
            if (!StringUtil.isEmpty(profileId)) {
                profile = UserCenterServiceSngl.get().getProfileByProfileId(profileId);
            } else if (!StringUtil.isEmpty(profilename)) {
                profile = UserCenterServiceSngl.get().getProfileByNick(profilename);
            }

            if (profile == null) {
                mapMessage = putErrorMessage(mapMessage, "profile.has.notexists");
                return new ModelAndView("/point/pointactionhistorylist", mapMessage);
            }

            //pageRows setting
            int curPage = (pageStartIndex / pageSize) + 1;
            Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
            //sql condition setting
            QueryExpress queryExpress = new QueryExpress();
            if (actionHistoryId != null) {
                queryExpress.add(QueryCriterions.eq(PointActionHistoryField.ACTIONHISTORYID, actionHistoryId));
            }
            queryExpress.add(QueryCriterions.eq(PointActionHistoryField.PROFILEID, profile.getProfileId()));
            queryExpress.add(QuerySort.add(PointActionHistoryField.CREATEDATE, QuerySortOrder.DESC));


            PageRows<PointActionHistory> pageRows = PointServiceSngl.get().queryPointActionHistoryByPage(queryExpress, pagination);

            mapMessage.put("list", pageRows.getRows());
            mapMessage.put("page", pageRows.getPage());
            mapMessage.put("profileid", profile.getProfileId());
            mapMessage.put("userno", profile.getUno());
            mapMessage.put("profilename", profile.getNick());
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }


        return new ModelAndView("/point/pointactionhistorylist", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createPage(@RequestParam(value = "userno", required = true) String userno,
                                   @RequestParam(value = "profilid", required = true) String profileid) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();

        PointActionType type = new PointActionType(0,HistoryActionType.POINT.getCode(),"");
        mapMessage.put("type", type);

        //get all PointActionType's quantity
        Collection<PointActionType> collection = PointActionType.getAll();
        mapMessage.put("actiontypecollection", collection);
        Collection<PointKeyType> pointKeyTypes = PointKeyType.getAll();
        mapMessage.put("pointKeyTypes", pointKeyTypes);

        try {
            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileid);
            mapMessage.put("profilename", profile.getNick());
            mapMessage.put("userno", profile.getUno());
        } catch (ServiceException e) {
        }

        mapMessage.put("profileid", profileid);

        return new ModelAndView("point/createpointactionhistory", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "userno", required = false) String userNo,
                               @RequestParam(value = "profileid", required = false) String profileId,
                               @RequestParam(value = "actiontype", required = false) Integer actionType,
                               @RequestParam(value = "actiondescription", required = false) String actionDescription,
                               @RequestParam(value = "pointvalue", required = false) Integer pointValue,
                               @RequestParam(value = "pointkey", required = false) String pointKey) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        Date now = new Date();

        PointActionHistory actionHistory = new PointActionHistory();
        actionHistory.setUserNo(userNo);
        actionHistory.setProfileId(profileId);
        actionHistory.setActionType(PointActionType.getByCode(actionType));
        actionHistory.setActionDescription(actionDescription);
        actionHistory.setPointValue(pointValue);
        actionHistory.setCreateDate(now);
        actionHistory.setActionDate(now);


        try {
            if (PointServiceSngl.get().increasePointActionHistory(actionHistory, PointKeyType.getByCode(pointKey))) {
                //
                ToolsLog log = new ToolsLog();

                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.ADMIN_MODIFY_POINT);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("profileid:" + profileId + " 积分：" + pointValue);

                addLog(log);


            }
            mapMessage.put("profileid", profileId);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }


        return new ModelAndView("redirect:/point/pointactionhistory/list?profileid=" + profileId);
    }

}
