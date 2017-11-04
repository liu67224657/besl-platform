package com.enjoyf.webapps.joyme.webpage.controller.usercenter;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.RelationStatus;
import com.enjoyf.platform.service.point.PointActionType;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.social.ObjectRelationType;
import com.enjoyf.platform.service.social.SocialServiceSngl;
import com.enjoyf.platform.service.social.UserRelation;
import com.enjoyf.platform.service.timeline.TimeLineActionType;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.service.usercenter.UserPrivacy;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.webapps.joyme.dto.usercenter.UserinfoDTO;
import com.enjoyf.webapps.joyme.weblogic.point.PointWebLogic;
import com.enjoyf.webapps.joyme.weblogic.profile.CustomizeWebLogic;
import com.enjoyf.webapps.joyme.weblogic.usercenter.UserRelationWebLogic;
import com.enjoyf.webapps.joyme.weblogic.usercenter.UserTimelineWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterSession;
import com.enjoyf.webapps.joyme.webpage.util.Constant;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * <p/>
 * Description:关注action
 * </p>
 * <pre>
 * /focus	关注
 * /unfocus	取消关注
 * </pre>
 *
 * @author: <a href=mailto:wengangsai@enjoyfound.com>saiwengang</a>
 */
@Controller
@RequestMapping("/api/usercenter/relation")
public class ApiRelationController extends BaseRestSpringController {
    Logger logger = LoggerFactory.getLogger(ApiRelationController.class);

    @Resource(name = "userRelationWebLogic")
    private UserRelationWebLogic userRelationWebLogic;

    @Resource(name = "userTimelineWebLogic")
    private UserTimelineWebLogic userTimelineWebLogic;

    @Autowired
    private CustomizeWebLogic customizeWebLogic;

    @Resource(name = "pointWebLogic")
    private PointWebLogic pointWebLogic;

    @Resource(name = "i18nSource")
    private ResourceBundleMessageSource i18nSource;

    private JsonBinder binder = JsonBinder.buildNormalBinder();

    /**
     * 关注 //todo jsonp格式.用callback
     * <p/>
     * * @param srcprofileid 关注人的srcprofileid
     *
     * @param destprofileid 被关注人的destprofileid
     * @param request
     * @return
     */
    @RequestMapping(value = "/follow")
    @ResponseBody
    public String follow(@RequestParam(value = "destprofileid") String destprofileid, HttpServletRequest request) {
        String callback = HTTPUtil.getParam(request, "callback");
        String userIp = getIp(request);

        UserCenterSession userCenterSession = getUserCenterSeesion(request);
        try {
            if (userCenterSession == null) {
                JSONObject jsonObject = ResultCodeConstants.USER_NOT_LOGIN.getJsonObject();
                return callback + "([" + jsonObject.toString() + "])";
            }

            if (StringUtil.isEmpty(destprofileid)) {
                JSONObject jsonObject = ResultCodeConstants.PARAM_EMPTY.getJsonObject();
                return callback + "([" + jsonObject.toString() + "])";
            }

            if (userCenterSession.getProfileId().equals(destprofileid)) {
                JSONObject jsonObject = ResultCodeConstants.USERCENTER_FOLLOWSAME_USER.getJsonObject();
                return callback + "([" + jsonObject.toString() + "])";
            }
            //查看被关注人是否允许关注
            UserPrivacy userPrivacy = UserCenterServiceSngl.get().getUserPrivacy(destprofileid);
            if (userPrivacy != null && userPrivacy.getFunctionSetting().getAcceptFollow().equals("0")) {
                return ResultCodeConstants.USERCENTER_FOLLOW_USER_NO_AGREE.getJsonString(callback);
            }
            UserRelation relation = userRelationWebLogic.follow(userCenterSession.getProfileId(), destprofileid, "", userIp);
            if(relation==null){
                return ResultCodeConstants.USERCENTER_FOLLOW_ALREADY_EXISTS.getJsonString(callback);
            }
            //查找被关注人和用户之间的关系
            JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
            if (relation.getSrcStatus().getCode() == RelationStatus.FOCUS.getCode() && relation.getDestStatus().getCode() == RelationStatus.FOCUS.getCode()) {
                jsonObject.put("result", Constant.FLAG_FOCUSTYPE_EACHOTHER);
            } else {
                jsonObject.put("result", Constant.FLAG_FOCUSTYPE_FOCUS);
            }
            userTimelineWebLogic.buildUserTimeline(userCenterSession.getProfileId(), TimeLineActionType.FOCUS_USER.getCode(), "wiki", destprofileid, new Date());

            //声望上报
            pointWebLogic.wikiReport(PointActionType.FANS, destprofileid);
            //关注用户得积分
            pointWebLogic.wikiReport(PointActionType.FOLLOW_USER, userCenterSession.getProfileId());
            //私信上报
            customizeWebLogic.privacysave(userCenterSession.getProfileId(), "", 3, destprofileid);
            return callback + "([" + jsonObject.toString() + "])";
        } catch (ServiceException e) {
            logger.error(this.getClass().getName() + " focus occured ServiceException:", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString(callback);
        }
    }

    /**
     * 取消关注 //todo 返回的是jsonp
     *
     * @param destProfileId 关注人的focusprofileid
     * @param request
     * @return
     */
    @RequestMapping(value = "/unfollow")
    @ResponseBody
    public String unfollow(@RequestParam(value = "destprofileid") String destProfileId, HttpServletRequest request) {
        String callback = HTTPUtil.getParam(request, "callback");
        JSONObject jsonObject = ResultCodeConstants.FAILED.getJsonObject();

        String requestIp = request.getRemoteAddr();
        try {
            UserCenterSession userCenterSeesion = getUserCenterSeesion(request);
            if (userCenterSeesion == null) {
                return ResultCodeConstants.USER_NOT_LOGIN.getJsonObject().toString();
            }
            String srcprofileid = userCenterSeesion.getProfileId();

            if (StringUtil.isEmpty(destProfileId)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonObject().toString();
            } else {
                boolean bVal = userRelationWebLogic.unfollow(srcprofileid, destProfileId, requestIp);
                if (bVal) {
                    UserRelation relation = SocialServiceSngl.get().getRelation(srcprofileid, destProfileId, ObjectRelationType.WIKI_PROFILE);
                    jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
                    if (relation.getSrcStatus().getCode() == RelationStatus.UNFOCUS.getCode() && relation.getDestStatus().getCode() == RelationStatus.FOCUS.getCode()) {
                        jsonObject.put("result", Constant.FLAG_FOCUSTYPE_FANS);
                    } else {
                        jsonObject.put("result", Constant.FLAG_FOCUSTYPE_NONE_FOCUS);
                    }

                    pointWebLogic.wikiReport(PointActionType.CANCEL_FOLLOW, destProfileId);
                    //私信上报
                    customizeWebLogic.privacysave(srcprofileid, "", 4, destProfileId);

                }
            }
        } catch (ServiceException e) {
            logger.error(this.getClass().getName() + " unfocus occured ServiceException:", e);
        }
        return callback + "([" + jsonObject.toString() + "])";
    }


    @RequestMapping(value = "/removefans")
    @ResponseBody
    public String removeFans(@RequestParam(value = "destprofileid") String destProfileId, HttpServletRequest request) {
        String callback = HTTPUtil.getParam(request, "callback");
        JSONObject jsonObject = ResultCodeConstants.FAILED.getJsonObject();

        String requestIp = request.getRemoteAddr();
        try {
            UserCenterSession userCenterSeesion = getUserCenterSeesion(request);
            if (userCenterSeesion == null) {
                jsonObject = ResultCodeConstants.USER_NOT_LOGIN.getJsonObject();
                return callback + "([" + binder.toJson(jsonObject.toString()) + "])";
            }
            String srcprofileid = userCenterSeesion.getProfileId();

            if (StringUtil.isEmpty(destProfileId)) {
                jsonObject = ResultCodeConstants.PARAM_EMPTY.getJsonObject();
            } else {
                boolean bVal = userRelationWebLogic.unfollow(destProfileId, srcprofileid, requestIp);
                if (bVal) {
                    UserRelation relation = SocialServiceSngl.get().getRelation(destProfileId, srcprofileid, ObjectRelationType.WIKI_PROFILE);
                    jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
                    if (relation.getSrcStatus().getCode() == RelationStatus.UNFOCUS.getCode() && relation.getDestStatus().getCode() == RelationStatus.FOCUS.getCode()) {
                        jsonObject.put("result", Constant.FLAG_FOCUSTYPE_FANS);
                    } else {
                        jsonObject.put("result", Constant.FLAG_FOCUSTYPE_NONE_FOCUS);
                    }
                    //移除粉丝减少声望值
                    pointWebLogic.wikiReport(PointActionType.CANCEL_FOLLOW, srcprofileid);
                    //私信上报
                    customizeWebLogic.privacysave(srcprofileid, "", 4, destProfileId);
                }
            }
        } catch (ServiceException e) {
            logger.error(this.getClass().getName() + " unfocus occured ServiceException:", e);
        }
        return callback + "([" + binder.toJson(jsonObject.toString()) + "])";
    }

    /**
     * 根据profileid获取用户粉丝
     *
     * @param profileid 关注人的profileid
     * @param request
     * @return
     */
    @RequestMapping(value = "/fans/list")
    @ResponseBody
    public String userfans(@RequestParam(value = "profileid") String profileid,
                           @RequestParam(value = "pno", defaultValue = "0") String pno,
                           @RequestParam(value = "psize", defaultValue = "10") String psize, HttpServletRequest request) {

        UserCenterSession userCenterSession = this.getUserCenterSeesion(request);
        String sessionPid = userCenterSession != null ? userCenterSession.getProfileId() : "";
        try {
            int pnum = Integer.parseInt(pno);
            int size = Integer.parseInt(psize);
            Pagination page = new Pagination(size * pnum, pnum, size);
            JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();

            PageRows<UserinfoDTO> pageRows = userRelationWebLogic.queryFansUserinfoProfileid(profileid, ObjectRelationType.WIKI_PROFILE, page, sessionPid,true);
            jsonObject.put("result", pageRows.getRows());
            jsonObject.put("page", pageRows.getPage());
            return jsonObject.toString();
        } catch (ServiceException e) {
            logger.error(this.getClass().getName() + " unfocus occured ServiceException:", e);
            return ResultCodeConstants.SUCCESS.toString();
        }
    }

    /**
     * 根据profileid获取用户关注人
     *
     * @param profileid 关注人的profileid
     * @param request
     * @return
     */
    @RequestMapping(value = "/follow/list")
    @ResponseBody
    public String userfollows(@RequestParam(value = "profileid") String profileid,
                              @RequestParam(value = "pno", defaultValue = "0") String pno,
                              @RequestParam(value = "psize", defaultValue = "10") String psize, HttpServletRequest request) {


        UserCenterSession userCenterSession = this.getUserCenterSeesion(request);
        String sessionPid = userCenterSession != null ? userCenterSession.getProfileId() : "";
        try {
            int pnum = Integer.parseInt(pno);
            int size = Integer.parseInt(psize);
            Pagination page = new Pagination(size * pnum, pnum, size);
            JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();

            PageRows<UserinfoDTO> pageRows = userRelationWebLogic.queryFollowUserinfoProfileid(profileid, ObjectRelationType.WIKI_PROFILE, page, sessionPid,true);
            jsonObject.put("result", pageRows.getRows());
            jsonObject.put("page", pageRows.getPage());
            return jsonObject.toString();
        } catch (ServiceException e) {
            logger.error(this.getClass().getName() + " unfocus occured ServiceException:", e);
            return ResultCodeConstants.SUCCESS.toString();
        }
    }

    private String getRelationMsg(UserRelation relation) {
        String returnMsg;
        if (relation == null || (relation.getSrcStatus().equals(ActStatus.UNACT) && relation.getDestStatus().equals(ActStatus.UNACT))) {
            returnMsg = Constant.FLAG_FOCUSTYPE_NONE_FOCUS;
        } else if (relation.getSrcStatus().equals(ActStatus.UNACT) && relation.getDestStatus().equals(ActStatus.ACTED)) {
            returnMsg = Constant.FLAG_FOCUSTYPE_FANS;
        } else if (relation.getSrcStatus().equals(ActStatus.ACTED) && relation.getDestStatus().equals(ActStatus.UNACT)) {
            returnMsg = Constant.FLAG_FOCUSTYPE_FOCUS;
        } else {
            returnMsg = Constant.FLAG_FOCUSTYPE_EACHOTHER;
        }
        return returnMsg;
    }

    @RequestMapping(value = "/import")
    @ResponseBody
    public String importHistoryData(@RequestParam(value = "to") String destprofileid, @RequestParam(value = "from") String srcProfileId) {
        String result = "";
        if (StringUtil.isEmpty(destprofileid) || StringUtil.isEmpty(srcProfileId))
            return result;
        try {
            userRelationWebLogic.follow(srcProfileId, destprofileid, "import history data", "127.0.0.1");
            result = "ok";
        } catch (ServiceException e) {
            logger.error(this.getClass().getName() + " focus occured ServiceException:", e);
            result = "error";

        }
        return result;
    }
}
