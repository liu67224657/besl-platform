package com.enjoyf.webapps.joyme.webpage.controller.usercenter;

import com.enjoyf.platform.service.RelationStatus;
import com.enjoyf.platform.service.point.PointActionType;
import com.enjoyf.platform.service.point.PointServiceSngl;
import com.enjoyf.platform.service.point.UserPoint;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.social.ObjectRelationType;
import com.enjoyf.platform.service.social.SocialServiceSngl;
import com.enjoyf.platform.service.social.UserRelation;
import com.enjoyf.platform.service.timeline.*;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.ProfileSum;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.service.usercenter.VerifyProfile;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.webapps.joyme.dto.usercenter.UserinfoDTO;
import com.enjoyf.webapps.joyme.weblogic.point.PointWebLogic;
import com.enjoyf.webapps.joyme.weblogic.usercenter.UserCenterWebLogic;
import com.enjoyf.webapps.joyme.weblogic.usercenter.UserRelationWebLogic;
import com.enjoyf.webapps.joyme.weblogic.usercenter.UserTimelineWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-9-25
 * Time: 上午9:43
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/usercenter")
public class HomeController extends BaseRestSpringController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Resource(name = "userTimelineWebLogic")
    private UserTimelineWebLogic userTimelineWebLogic;

    @Resource(name = "userCenterWebLogic")
    private UserCenterWebLogic usercenterWebLogic;

    @Resource(name = "userRelationWebLogic")
    private UserRelationWebLogic userRelationWebLogic;

    @Resource(name = "pointWebLogic")
    private PointWebLogic pointWebLogic;

    private JsonBinder binder = JsonBinder.buildNormalBinder();

    @RequestMapping(value = "/home")
    public ModelAndView index(HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            UserCenterSession userSession = getUserCenterSeesion(request);

            if (userSession == null || StringUtil.isEmpty(userSession.getProfileId())) {
                mapMessage.put("isnotlogin", "true");
                return new ModelAndView("/views/jsp/usercenter/person-center", mapMessage);
            }
            String profileId = userSession.getProfileId();
            Date profileDate = new Date();
            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileId);
            GAlerter.lan(this.getClass().getName() + " profileEnd=" + (new Date().getTime() - profileDate.getTime()));
            Date verifyDate = new Date();
            VerifyProfile verifyProfile = UserCenterServiceSngl.get().getVerifyProfileById(profile.getProfileId());
            GAlerter.lan(this.getClass().getName() + " verifyDateend=" + (new Date().getTime() - verifyDate.getTime()));

            //该用户获得的道具 todo 删除用户激励体系
//            Date chooseMapDate = new Date();
//            Map<String, String> chooseMap = PointServiceSngl.get().getChooseLottery(profileId);
            ProfileSum profileSum = UserCenterServiceSngl.get().getProfileSum(profile.getProfileId());

            UserinfoDTO userinfoDTO = usercenterWebLogic.buildUserInfoDTO(profile, verifyProfile, profileSum, true);
//            GAlerter.lan(this.getClass().getName() + " chooseMapDateend=" + (new Date().getTime() - chooseMapDate.getTime()));

            mapMessage.put("userinfoDTO", userinfoDTO);

            //用户关系，包含粉丝和关注
            Pagination UserRelationPage = new Pagination(8, 1, 8);
            Date relationDate = new Date();
            createUserRelation(profileId, mapMessage, UserRelationPage);
            GAlerter.lan(this.getClass().getName() + " relationDateend=" + (new Date().getTime() - relationDate.getTime()));

            Pagination timeLinePage = new Pagination(10, 1, 10);
            //我的动态 好友动态 start
            Date timelineDate = new Date();
            PageRows<UserTimeline> myTimeLinePage = userTimelineWebLogic.queryUserTimeline(profileId, UserTimelineDomain.MY.getCode(), UserTimelineType.WIKI.getCode(), timeLinePage);
            mapMessage.put("myTimeLinePage", myTimeLinePage.getPage());
            mapMessage.put("myTimeLineRows", myTimeLinePage.getRows());
            PageRows<UserTimeline> friendTimeLinePage = userTimelineWebLogic.queryUserTimeline(profileId, UserTimelineDomain.FRIEND.getCode(), UserTimelineType.WIKI.getCode(), timeLinePage);
            mapMessage.put("friendTimeLinePage", friendTimeLinePage.getPage());
            mapMessage.put("friendTimeLineRows", friendTimeLinePage.getRows());
            GAlerter.lan(this.getClass().getName() + " timelineDate=" + (new Date().getTime() - timelineDate.getTime()));
            List<String> allProfileList = new ArrayList<String>();
            List<String> profileids = new ArrayList<String>();
            List<String> worshipProfileIdList = new ArrayList<String>();

            if (myTimeLinePage != null && !CollectionUtil.isEmpty(myTimeLinePage.getRows())) {
                for (UserTimeline userTimeline : myTimeLinePage.getRows()) {
                    if (userTimeline.getActionType().equals(TimeLineActionType.FOCUS_USER)) {
                        if (userTimeline.getExtendBody().contains("关注了")) {
                            continue;
                        }
                        profileids.add(userTimeline.getExtendBody());
                    }
                }
            }

            if (friendTimeLinePage != null && !CollectionUtil.isEmpty(friendTimeLinePage.getRows())) {
                for (UserTimeline userTimeline : friendTimeLinePage.getRows()) {
                    profileids.add(userTimeline.getDestProfileid());
                    if (userTimeline.getActionType().equals(TimeLineActionType.FOCUS_USER)) {
                        if (userTimeline.getExtendBody().contains("关注了")) {
                            continue;
                        }
                        profileids.add(userTimeline.getExtendBody());
                    }
                }
            }
            allProfileList.addAll(profileids);
            //膜拜
//            Date mobaiDate = new Date();
//            PageRows<String> pageRows = PointServiceSngl.get().queryWorship(profileId, new Pagination(100, 1, 100));
//            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
//                worshipProfileIdList.addAll(pageRows.getRows());
//                allProfileList.addAll(pageRows.getRows());
//                mapMessage.put("totalWorshipNum", pageRows.getPage().getTotalRows());
//            }
            Map<String, UserinfoDTO> map = usercenterWebLogic.buildUserinfoMap(allProfileList, false);

            mapMessage.put("userinfMap", buildUserinfoMapByProfileList(map, profileids));
            //我的动态 好友动态 end
            if (!CollectionUtil.isEmpty(worshipProfileIdList)) {
                Map<String, UserinfoDTO> userinfoDTOMap = buildUserinfoMapByProfileList(map, worshipProfileIdList);
                mapMessage.put("worshipProfileList", (userinfoDTOMap != null && !userinfoDTOMap.isEmpty()) ? userinfoDTOMap.values() : "");
            }
//            GAlerter.lan(this.getClass().getName() + " mobai and userinfoMap=" + (new Date().getTime() - mobaiDate.getTime()));

            mapMessage.put("userTimelineActionType", UserTimelineActionType.getAll());
            //初始化用户积分
//            timelineDate = new Date();
//            UserPoint userPoint = pointWebLogic.getUserPoint(DEFAULT_APPKEY, profileId);
//            mapMessage.put("userPoint", userPoint);
//            GAlerter.lan(this.getClass().getName() + " userPoint=" + (new Date().getTime() - timelineDate.getTime()));

            //是否签到
//            timelineDate = new Date();
//            int value = PointServiceSngl.get().getUserPointByDay(profileId, PointActionType.WANBA_SIGN.getCode() + "_" + profileId + "_" + DateUtil.formatDateToString(new Date(), "yyyyMMdd"));
//            GAlerter.lan(this.getClass().getName() + " value=" + (new Date().getTime() - timelineDate.getTime()));
//
//            String result = value > 0 ? "success" : "signed";
//            mapMessage.put("signFlag", result);
            //今日积分
//            timelineDate = new Date();
//            Integer todayPorint = PointServiceSngl.get().getUserPointByDay(profileId, "");
//            mapMessage.put("todayPorint", todayPorint);
//            GAlerter.lan(this.getClass().getName() + " todayPorint=" + (new Date().getTime() - timelineDate.getTime()));

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
        }
        return new ModelAndView("/views/jsp/usercenter/person-center", mapMessage);
    }

    private Map<String, UserinfoDTO> buildUserinfoMapByProfileList(Map<String, UserinfoDTO> map, List<String> profileList) {
        Map<String, UserinfoDTO> returnMap = new LinkedHashMap<String, UserinfoDTO>();
        if (map != null && !map.isEmpty()) {
            for (String pid : profileList) {
                if (map.get(pid) != null) {
                    returnMap.put(pid, map.get(pid));
                }
            }
        }
        return returnMap;
    }

    /**
     * 个人中心 他人视角
     *
     * @param request
     * @param pid
     * @return
     */
    @RequestMapping(value = "/page")
    public ModelAndView u(HttpServletRequest request, @RequestParam(value = "pid", required = false) String pid) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        if (StringUtil.isEmpty(pid)) {
            return new ModelAndView("redirect:/usercenter/home");
        }
        try {
            UserCenterSession userCenterSession = getUserCenterSeesion(request);
            if (userCenterSession != null) {
                if (userCenterSession.getProfileId().equals(pid)) {//如果传入的ID和当前登录ID相同 跳转到本人视角
                    return new ModelAndView("redirect:/usercenter/home");
                }
            }
            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(pid);
            //用户认证
            VerifyProfile verifyProfile = UserCenterServiceSngl.get().getVerifyProfileById(profile.getProfileId());
            //该用户获得的道具
//            Map<String, String> chooseMap = PointServiceSngl.get().getChooseLottery(pid);
            ProfileSum profileSum = UserCenterServiceSngl.get().getProfileSum(profile.getProfileId());
            UserinfoDTO userinfoDTO = usercenterWebLogic.buildUserInfoDTO(profile, verifyProfile, profileSum, true);
            UserRelation userRelation = SocialServiceSngl.get().getRelation(userCenterSession == null ? "" : userCenterSession.getProfileId(), pid, ObjectRelationType.WIKI_PROFILE);
            if (userRelation != null) {
                userinfoDTO.setFollowStatus(String.valueOf(userRelation.getSrcStatus().getCode()));
            } else {
                userinfoDTO.setFollowStatus(String.valueOf(RelationStatus.UNFOCUS.getCode()));
            }
            mapMessage.put("userinfoDTO", userinfoDTO);

            //用户关系，包含粉丝和关注
            Pagination UserRelationPage = new Pagination(8, 1, 8);
            createUserRelation(pid, mapMessage, UserRelationPage);

            Pagination timeLinePage = new Pagination(10, 1, 10);
            //我的动态
            PageRows<UserTimeline> myTimeLinePage = userTimelineWebLogic.queryUserTimeline(pid, UserTimelineDomain.MY.getCode(), UserTimelineType.WIKI.getCode(), timeLinePage);
            mapMessage.put("myTimeLinePage", myTimeLinePage.getPage());
            mapMessage.put("myTimeLineRows", myTimeLinePage.getRows());
            List<String> allProfileList = new ArrayList<String>();
            List<String> profileids = new ArrayList<String>();
            List<String> worshipProfileIdList = new ArrayList<String>();

            if (myTimeLinePage != null && !CollectionUtil.isEmpty(myTimeLinePage.getRows())) {
                for (UserTimeline userTimeline : myTimeLinePage.getRows()) {
                    if (userTimeline.getActionType().equals(TimeLineActionType.FOCUS_USER)) {
                        if (userTimeline.getExtendBody().contains("关注了")) {
                            continue;
                        }
                        profileids.add(userTimeline.getExtendBody());
                    }
                }
            }
            allProfileList.addAll(profileids);
//            PageRows<String> pageRows = PointServiceSngl.get().queryWorship(pid, new Pagination(100, 1, 100));
//            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
//                worshipProfileIdList.addAll(pageRows.getRows());
//                allProfileList.addAll(pageRows.getRows());
//                mapMessage.put("totalWorshipNum", pageRows.getPage().getTotalRows());
//            }
            Map<String, UserinfoDTO> map = usercenterWebLogic.buildUserinfoMap(allProfileList, false);

            mapMessage.put("userinfMap", buildUserinfoMapByProfileList(map, profileids));
            //我的动态 好友动态 end
            if (!CollectionUtil.isEmpty(worshipProfileIdList)) {
                Map<String, UserinfoDTO> userinfoDTOMap = buildUserinfoMapByProfileList(map, worshipProfileIdList);
//                mapMessage.put("worshipProfileList", (userinfoDTOMap != null && !userinfoDTOMap.isEmpty()) ? userinfoDTOMap.values() : "");
            }

            //初始化用户积分
//            UserPoint userPoint = pointWebLogic.getUserPoint(DEFAULT_APPKEY, pid);
//            mapMessage.put("userPoint", userPoint);

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
        }
        return new ModelAndView("/views/jsp/usercenter/other-person", mapMessage);
    }


    private void createUserRelation(String profileid, Map<String, Object> mapMessage, Pagination page) throws ServiceException {
        PageRows<UserinfoDTO> followRows = userRelationWebLogic.queryFollowUserinfoProfileid(profileid, ObjectRelationType.WIKI_PROFILE, page, profileid, false);
        mapMessage.put("followList", followRows.getRows());

        PageRows<UserinfoDTO> fansRows = userRelationWebLogic.queryFansUserinfoProfileid(profileid, ObjectRelationType.WIKI_PROFILE, page, profileid, false);
        mapMessage.put("fansList", fansRows.getRows());

    }
}
