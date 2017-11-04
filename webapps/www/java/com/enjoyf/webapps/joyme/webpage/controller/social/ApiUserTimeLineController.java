package com.enjoyf.webapps.joyme.webpage.controller.social;

import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.timeline.TimeLineActionType;
import com.enjoyf.platform.service.timeline.UserTimeline;
import com.enjoyf.platform.service.timeline.UserTimelineDomain;
import com.enjoyf.platform.service.timeline.UserTimelineType;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.webapps.joyme.dto.usercenter.UserinfoDTO;
import com.enjoyf.webapps.joyme.weblogic.usercenter.UserCenterWebLogic;
import com.enjoyf.webapps.joyme.weblogic.usercenter.UserTimelineWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterSession;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-9-25
 * Time: 上午9:43
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/api/usertimeline/")
public class ApiUserTimeLineController extends BaseRestSpringController {
    private static final Logger logger = LoggerFactory.getLogger(ApiUserTimeLineController.class);

    private JsonBinder binder = JsonBinder.buildNormalBinder();

    @Resource(name = "userTimelineWebLogic")
    private UserTimelineWebLogic userTimelineWebLogic;

    @Resource(name = "userCenterWebLogic")
    private UserCenterWebLogic userCenterWebLogic;

    /**
     * 动态分页查询接口
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public String home(@RequestParam(value = "page", defaultValue = "1") Integer page,
                       @RequestParam(value = "count", defaultValue = "10") Integer count,
                       @RequestParam(value = "pid", required = false) String pid,
                       HttpServletRequest request) {
        Pagination pagination = new Pagination(page * count, page, count);
        JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
        try {
            if (StringUtil.isEmpty(pid)) {
                UserCenterSession userCenterSession = getUserCenterSeesion(request);
                pid = userCenterSession.getProfileId();
            }
            PageRows<UserTimeline> pageRows = userTimelineWebLogic.queryUserTimeline(pid, UserTimelineDomain.MY.getCode(), UserTimelineType.WIKI.getCode(), pagination);
            List<String> profileids = new ArrayList<String>();

            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                for (UserTimeline userTimeline : pageRows.getRows()) {
                    if (userTimeline.getActionType().equals(TimeLineActionType.FOCUS_USER)) {
                        if (userTimeline.getExtendBody().contains("关注了")) {
                            continue;
                        }
                        profileids.add(userTimeline.getExtendBody());
                    }
                }
            }

            jsonObject.put("result", pageRows);
            jsonObject.put("userinfoMap", userCenterWebLogic.buildUserinfoMap(profileids, false));
            return jsonObject.toString();
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return ResultCodeConstants.SYSTEM_ERROR.toString();
        }
    }


    @RequestMapping(value = "/friendlist")
    @ResponseBody
    public String friendList(@RequestParam(value = "page", defaultValue = "1") Integer page,
                             @RequestParam(value = "count", defaultValue = "10") Integer count,
                             @RequestParam(value = "pid", required = false) String pid, HttpServletRequest request) {
        Pagination pagination = new Pagination(page * count, page, count);
        JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
        try {
            if (StringUtil.isEmpty(pid)) {
                UserCenterSession userCenterSession = getUserCenterSeesion(request);
                pid = userCenterSession.getProfileId();
            }
            PageRows<UserTimeline> pageRows = userTimelineWebLogic.queryUserTimeline(pid, UserTimelineDomain.FRIEND.getCode(), UserTimelineType.WIKI.getCode(), pagination);
            Map<String, UserinfoDTO> map = new HashMap<String, UserinfoDTO>();
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                List<String> profileids = new ArrayList<String>();
                for (UserTimeline userTimeline : pageRows.getRows()) {
                    profileids.add(userTimeline.getDestProfileid());
                    if (userTimeline.getActionType().equals(TimeLineActionType.FOCUS_USER)) {
                        if (userTimeline.getExtendBody().contains("关注了")) {
                            continue;
                        }
                        profileids.add(userTimeline.getExtendBody());
                    }
                }
                map = userCenterWebLogic.buildUserinfoMap(profileids, false);
            }

            jsonObject.put("result", pageRows);
            jsonObject.put("userinfoMap", map);
            return jsonObject.toString();
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return ResultCodeConstants.SYSTEM_ERROR.toString();
        }
    }


}
