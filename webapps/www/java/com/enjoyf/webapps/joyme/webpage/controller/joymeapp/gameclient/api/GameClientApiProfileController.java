package com.enjoyf.webapps.joyme.webpage.controller.joymeapp.gameclient.api;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.IntValidStatus;
import com.enjoyf.platform.service.comment.CommentBean;
import com.enjoyf.platform.service.comment.CommentBeanField;
import com.enjoyf.platform.service.comment.CommentDomain;
import com.enjoyf.platform.service.comment.CommentServiceSngl;
import com.enjoyf.platform.service.event.EventDispatchServiceSngl;
import com.enjoyf.platform.service.event.system.TaskAwardEvent;
import com.enjoyf.platform.service.event.task.TaskAction;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.social.ObjectRelation;
import com.enjoyf.platform.service.social.ObjectRelationType;
import com.enjoyf.platform.service.social.ProfileRelation;
import com.enjoyf.platform.service.social.SocialServiceSngl;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.http.AppUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.oauth.renren.api.client.utils.Md5Utils;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.util.StringUtil;
import com.enjoyf.webapps.joyme.dto.comment.ProfilePicDTO;
import com.enjoyf.webapps.joyme.dto.gamedb.GameDBSimpleDTO;
import com.enjoyf.webapps.joyme.dto.usercenter.GameClientProfileDTO;
import com.enjoyf.webapps.joyme.dto.usercenter.ProfileDTO;
import com.enjoyf.webapps.joyme.weblogic.comment.ProfilePicWebLogic;
import com.enjoyf.webapps.joyme.weblogic.gamedb.GameDbWebLogic;
import com.enjoyf.webapps.joyme.weblogic.joymeapp.gameclient.GameClientWebLogic;
import com.enjoyf.webapps.joyme.weblogic.usercenter.UserCenterWebLogic;
import com.enjoyf.webapps.joyme.webpage.controller.joymeapp.gameclient.AbstractGameClientBaseController;
import net.sf.json.JSONObject;
import net.sourceforge.pinyin4j.PinyinHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/12/16
 * Description:
 */
@Controller
@RequestMapping("/joymeapp/gameclient/api/profile")
public class GameClientApiProfileController extends AbstractGameClientBaseController {

    @Resource(name = "userCenterWebLogic")
    private UserCenterWebLogic userCenterWebLogic;

    @Resource(name = "gameDbWebLogic")
    private GameDbWebLogic gameDbWebLogic;

    @Resource(name = "profilePicWebLogic")
    private ProfilePicWebLogic profilePicWebLogic;

    @Resource(name = "gameClientWebLogic")
    private GameClientWebLogic gameClientWebLogic;

    //获取人
    @ResponseBody
    @RequestMapping(value = "/getbyuid")
    public String getByUid(HttpServletRequest request) {

        String uidParam = HTTPUtil.getParam(request, "uid");
        String platform = HTTPUtil.getParam(request, "platform");
        if (StringUtil.isEmpty(uidParam) || StringUtil.isEmpty(platform)) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        long uid = -1;
        try {
            uid = Long.parseLong(uidParam);
        } catch (NumberFormatException e) {
        }
        if (uid == -1) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        try {
            Profile profile = UserCenterServiceSngl.get().getProfileByUid(uid);
            if (profile == null) {
                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
            }

            GameClientProfileDTO profileDTO = userCenterWebLogic.builGameClientDTO(profile, platform);
            List<GameDBSimpleDTO> gameDBSimpleDTOList = new ArrayList<GameDBSimpleDTO>();
            List<ProfileDTO> likeProfileList = new ArrayList<ProfileDTO>();
            List<ProfileDTO> likedProfileList = new ArrayList<ProfileDTO>();
            List<ProfilePicDTO> picList = new ArrayList<ProfilePicDTO>();

            //组装dto
            Set<Long> longSet = new HashSet<Long>();
            PageRows<ObjectRelation> gameRelationList = SocialServiceSngl.get().queryObjectRelation(profile.getProfileId(), ObjectRelationType.GAME, new Pagination(1 * 6, 1, 6));
            for (ObjectRelation objectRelation : gameRelationList.getRows()) {
                longSet.add(Long.valueOf(objectRelation.getObjectId()));
            }
            Map<String, GameDBSimpleDTO> gameSimpleDTOMap = gameDbWebLogic.queryGameDTOByGameDBIds(longSet, null, "");
            for (ObjectRelation relation : gameRelationList.getRows()) {
                if (gameSimpleDTOMap.containsKey(relation.getObjectId())) {
                    gameSimpleDTOMap.get(relation.getObjectId()).setJt(AppRedirectType.REDIRECT_GAMEDETAIL.getCode() + "");
                    gameDBSimpleDTOList.add(gameSimpleDTOMap.get(relation.getObjectId()));
                }
            }

            //喜欢的人
            PageRows<ProfileRelation> LikeRelationList = SocialServiceSngl.get().querySrcRelation(profile.getProfileId(), ObjectRelationType.PROFILE, new Pagination(1 * 6, 1, 6));
            PageRows<ProfileRelation> likedRelationList = SocialServiceSngl.get().queryDestRelation(profile.getProfileId(), ObjectRelationType.PROFILE, new Pagination(1 * 6, 1, 6));
            Set<String> prifileIdSet = new HashSet<String>();
            for (ProfileRelation relation : LikeRelationList.getRows()) {
                prifileIdSet.add(relation.getDestProfileId());
            }
            for (ProfileRelation relation : likedRelationList.getRows()) {
                prifileIdSet.add(relation.getSrcProfileId());
            }

            //组装dto
            if (!CollectionUtil.isEmpty(prifileIdSet)) {
                Map<String, ProfileDTO> profileDTOMap = userCenterWebLogic.buildProfileDTOByProfileIDs(prifileIdSet);
                //
                for (ProfileRelation relation : LikeRelationList.getRows()) {
                    if (profileDTOMap.containsKey(relation.getDestProfileId())) {
                        likeProfileList.add(profileDTOMap.get(relation.getDestProfileId()));
                    }
                }

                //
                for (ProfileRelation relation : likedRelationList.getRows()) {
                    if (profileDTOMap.containsKey(relation.getSrcProfileId())) {
                        likedProfileList.add(profileDTOMap.get(relation.getSrcProfileId()));
                    }
                }
            }

            PageRows<CommentBean> picBeanRows = CommentServiceSngl.get().queryCommentBeanByPage(new QueryExpress()
                    .add(QueryCriterions.eq(CommentBeanField.URI, profile.getProfileId()))
                    .add(QueryCriterions.eq(CommentBeanField.DOMAIN, CommentDomain.GAMECLIENT_MIYOU.getCode()))
                    .add(QueryCriterions.ne(CommentBeanField.REMOVE_STATUS, ActStatus.ACTED.getCode())), new Pagination(1 * 8, 1, 8));
            for (CommentBean bean : picBeanRows.getRows()) {
                ProfilePicDTO dto = profilePicWebLogic.buildProfilePicDTO(bean);
                picList.add(dto);
            }

            //按喜欢的游戏名称的汉语拼音排序
            //     Collections.sort(gameDBSimpleDTOList, new ComparatorPinYin());

            JSONObject jsonObject = new JSONObject();
            Map map = new HashMap();
            map.put("profile", profileDTO);
            map.put("likegame", gameDBSimpleDTOList);
            map.put("likeprofile", likeProfileList);
            map.put("likedprofile", likedProfileList);
            map.put("likepic", picList);
            jsonObject.put("rs", "1");
            jsonObject.put("result", map);

            return jsonObject.toString();
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
    }

    //喜欢动作
    @ResponseBody
    @RequestMapping(value = "/like")
    public String like(HttpServletRequest request) {
        String uidParam = request.getParameter("uid");
        String clientId = StringUtil.isEmpty(HTTPUtil.getParam(request, "clientid")) ? request.getHeader("User-Agent") : HTTPUtil.getParam(request, "clientid"); //用于任务系统

        if (StringUtil.isEmpty(uidParam)) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        long uid = -1;
        try {
            uid = Long.parseLong(uidParam);
        } catch (NumberFormatException e) {
        }
        if (uid == -1) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        String destUidParam = request.getParameter("desuid");
        if (StringUtil.isEmpty(uidParam)) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        long destUid = -1;
        try {
            destUid = Long.parseLong(destUidParam);
        } catch (NumberFormatException e) {
        }
        if (destUid == -1) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }
        if (uid == destUid) {
            return ResultCodeConstants.SUCCESS.getJsonString();
        }
        try {
            Profile profile = UserCenterServiceSngl.get().getProfileByUid(uid);
            if (profile == null) {
                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
            }

            Profile destProfile = UserCenterServiceSngl.get().getProfileByUid(destUid);
            if (destProfile == null) {
                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
            }
            AuthApp app = OAuthServiceSngl.get().getApp(request.getParameter("appkey"));

            Date date = new Date();
            ProfileRelation profileRelation = new ProfileRelation();
            profileRelation.setType(ObjectRelationType.PROFILE);
            profileRelation.setSrcProfileId(profile.getProfileId());
            profileRelation.setDestProfileId(destProfile.getProfileId());
            profileRelation.setSrcStatus(IntValidStatus.VALID);
            profileRelation.setDestStatus(IntValidStatus.UNVALID);
            profileRelation.setProfileKey(app.getProfileKey());
            profileRelation.setModifyIp(getIp(request));
            profileRelation.setModifyTime(new Date());

            ProfileRelation relation = SocialServiceSngl.get().saveProfileRelation(profileRelation);
            if (relation != null) {
                String platFormString = HTTPUtil.getParam(request, "platform");
                String appKey = HTTPUtil.getParam(request, "appkey");
                AppPlatform appPlatform = null;
                try {
                    appPlatform = AppPlatform.getByCode(Integer.parseInt(platFormString));
                } catch (Exception e) {
                }

                if (appPlatform != null && !StringUtil.isEmpty(appKey)) {
                    sendOutDayilLikeTask(profile, getIp(request), appPlatform, appKey, date, uidParam, clientId);
                }
            }
            return ResultCodeConstants.SUCCESS.getJsonString();
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
    }

    private void sendOutDayilLikeTask(Profile profile, String ip, AppPlatform appPlatform, String appKey, Date date, String directId, String clientId) {
        try {
            TaskAwardEvent taskEvent = new TaskAwardEvent();
            taskEvent.setDoTaskDate(date);
            taskEvent.setTaskAction(TaskAction.LIKE_PROFILE);
            taskEvent.setProfileId(profile.getProfileId());
            taskEvent.setTaskIp(ip);
//            taskEvent.setTaskId(getDayilLikeFans(appPlatform));
            taskEvent.setClientId(clientId);
            taskEvent.setUno(profile.getUno());
            taskEvent.setAppkey(AppUtil.getAppKey(appKey));
            taskEvent.setProfileKey(profile.getProfileKey());
            taskEvent.setAppPlatform(appPlatform);
            taskEvent.setDirectId(directId);
            EventDispatchServiceSngl.get().dispatch(taskEvent);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
        }
    }

    //不喜欢动作
    @ResponseBody
    @RequestMapping(value = "/unlike")
    public String unlike(HttpServletRequest request) {
        String uidParam = request.getParameter("uid");
        if (StringUtil.isEmpty(uidParam)) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        long uid = -1;
        try {
            uid = Long.parseLong(uidParam);
        } catch (NumberFormatException e) {
        }
        if (uid == -1) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        String destUidParam = request.getParameter("desuid");
        if (StringUtil.isEmpty(uidParam)) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        long destUid = -1;
        try {
            destUid = Long.parseLong(destUidParam);
        } catch (NumberFormatException e) {
        }
        if (destUid == -1) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        if (uid == destUid) {
            return ResultCodeConstants.SUCCESS.getJsonString();
        }
        try {
            Profile profile = UserCenterServiceSngl.get().getProfileByUid(uid);
            if (profile == null) {
                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
            }

            Profile destProfile = UserCenterServiceSngl.get().getProfileByUid(destUid);
            if (destProfile == null) {
                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
            }
            AuthApp app = OAuthServiceSngl.get().getApp(request.getParameter("appkey"));

            SocialServiceSngl.get().removeProfileRelation(profile.getProfileId(), destProfile.getProfileId(), ObjectRelationType.PROFILE, app.getProfileKey());

            return ResultCodeConstants.SUCCESS.getJsonString();
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
    }


    //保存图片接口
    @ResponseBody
    @RequestMapping(value = "/savepic")
    public String savePic(HttpServletRequest request) {
        String uidParam = HTTPUtil.getParam(request, "uid");
        String appkey = HTTPUtil.getParam(request, "appkey");
        String pic = request.getParameter("pic");
        String clientId = StringUtil.isEmpty(HTTPUtil.getParam(request, "clientid")) ? request.getHeader("User-Agent") : HTTPUtil.getParam(request, "clientid"); //用于任务系统

        if (StringUtil.isEmpty(uidParam) || StringUtil.isEmpty(pic) || StringUtil.isEmpty(appkey)) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        String platFormString = HTTPUtil.getParam(request, "platform");
        AppPlatform appPlatform = null;
        try {
            appPlatform = AppPlatform.getByCode(Integer.parseInt(platFormString));
        } catch (Exception e) {
        }
        if (appPlatform == null) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        long uid = -1;
        try {
            uid = Long.parseLong(uidParam);
        } catch (NumberFormatException e) {
        }
        if (uid == -1) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        try {
            Profile profile = UserCenterServiceSngl.get().getProfileByUid(uid);
            if (profile == null) {
                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
            }

            CommentBean commentBean = new CommentBean();
            commentBean.setUniqueKey(UUID.randomUUID().toString());
            commentBean.setUri(profile.getProfileId());
            commentBean.setDomain(CommentDomain.GAMECLIENT_MIYOU);
            commentBean.setCommentId(Md5Utils.md5(commentBean.getUniqueKey() + CommentDomain.GAMECLIENT_MIYOU.getCode()));
            commentBean.setPic(pic);
            commentBean.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
            commentBean = CommentServiceSngl.get().createCommentBean(commentBean);

//			ClientLine clientLine = JoymeAppServiceSngl.get().getClientLineByCode(GameClientConstants.REMOCCEND_PIC);
//			Map<String, String> errorMsgMap = new HashMap<String, String>();
//			Map<String, String> lineItemKeyValues = new HashMap<String, String>();
//			Map<String, Object> mapMsg = new HashMap<String, Object>();
//			lineItemKeyValues.put("itemname", profile.getNick());
//			lineItemKeyValues.put("author", profile.getProfileId());
//			lineItemKeyValues.put("picurl", commentBean.getPic());
//			lineItemKeyValues.put("itemdomain", String.valueOf(ClientItemDomain.COMMENTBEAN.getCode()));
//			lineItemKeyValues.put("lineid", String.valueOf(clientLine.getLineId()));
//			lineItemKeyValues.put("itemtype", String.valueOf(ClientItemType.COMMENTBEAN.getCode()));
//			lineItemKeyValues.put("directid", commentBean.getCommentId());
//			ClientLineItem clientLineItem = ClientLineWebDataProcessorFactory.get().factory(ClientItemDomain.COMMENTBEAN).generateAddLineItem(lineItemKeyValues, errorMsgMap, mapMsg);
//			JoymeAppServiceSngl.get().createClientLineItem(clientLineItem);

            Map map = new HashMap();
            map.put("picid", commentBean.getCommentId());
            String flag = request.getParameter("flag");
            if (!StringUtil.isEmpty(flag)) {
                map.put("flag", flag);
            }
            map.put("picurl", commentBean.getPic());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("rs", ResultCodeConstants.SUCCESS.getCode());
            jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
            jsonObject.put("result", map);

            sendOutSaveProfilePicTask(profile, getIp(request), appPlatform, appkey, new Date(), clientId);

            return jsonObject.toString();
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
    }

    private void sendOutSaveProfilePicTask(Profile profile, String ip, AppPlatform appPlatform, String appKey, Date date, String clientId) {
        try {
            TaskAwardEvent taskEvent = new TaskAwardEvent();
            taskEvent.setDoTaskDate(date);
            taskEvent.setProfileId(profile.getProfileId());
            taskEvent.setTaskIp(ip);
            //  taskEvent.setTaskId(getSaveProfilePicTaskId(appPlatform));
            taskEvent.setTaskAction(TaskAction.UPLOAD_IMAGE);
            taskEvent.setUno(profile.getUno());
            taskEvent.setAppkey(AppUtil.getAppKey(appKey));
            taskEvent.setProfileKey(profile.getProfileKey());
            taskEvent.setAppPlatform(appPlatform);
            taskEvent.setClientId(clientId);
            EventDispatchServiceSngl.get().dispatch(taskEvent);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
        }
    }

    //删除图片接口
    @ResponseBody
    @RequestMapping(value = "/delpic")
    public String deletePic(HttpServletRequest request) {
        String uidParam = request.getParameter("uid");
        String picid = request.getParameter("picid");
        if (StringUtil.isEmpty(uidParam) || StringUtil.isEmpty(picid)) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        long uid = -1;
        try {
            uid = Long.parseLong(uidParam);
        } catch (NumberFormatException e) {
        }
        if (uid == -1) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        try {
            Profile profile = UserCenterServiceSngl.get().getProfileByUid(uid);
            if (profile == null) {
                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
            }

            CommentBean commentBean = CommentServiceSngl.get().getCommentBeanById(picid);
            if (commentBean == null) {
                return ResultCodeConstants.PIC_NOT_EXISTS.getJsonString();
            }

            if (!commentBean.getUri().equals(profile.getProfileId())) {
                return ResultCodeConstants.PIC_REMOVED_FAILED.getJsonString();
            }

            if (commentBean.getRemoveStatus().equals(ActStatus.ACTED)) {
                return ResultCodeConstants.PIC_REMOVED_FAILED.getJsonString();
            }

            boolean result = CommentServiceSngl.get().modifyCommentBeanById(picid, new UpdateExpress().set(CommentBeanField.REMOVE_STATUS, ActStatus.ACTED.getCode()));

            return ResultCodeConstants.SUCCESS.getJsonString();
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
    }

    /**
     * TA喜欢的人
     */
    @ResponseBody
    @RequestMapping("/likelist")
    public String likeList(HttpServletRequest request,
                           @RequestParam(value = "uid", required = false) String uidStr,
                           @RequestParam(value = "pnum", required = false) String pnum,
                           @RequestParam(value = "pcount", required = false) String pcount) {
        String uidParam = StringUtil.isEmpty(uidStr) ? request.getParameter("uid") : uidStr;

        if (StringUtil.isEmpty(uidParam)) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        long uid = -1;
        try {
            uid = Long.parseLong(uidParam);
        } catch (NumberFormatException e) {
        }
        if (uid == -1) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }
        String pageNo = StringUtil.isEmpty(pnum) ? request.getParameter("pnum") : pnum;
        String pageCount = StringUtil.isEmpty(pcount) ? request.getParameter("pcount") : pcount;
        int pagNum = 1;
        try {
            pagNum = Integer.parseInt(pageNo);
        } catch (NumberFormatException e) {
        }
        int pageSize = 24;
        try {
            pageSize = Integer.parseInt(pageCount);
        } catch (NumberFormatException e) {
        }

        try {
            Pagination pagination = new Pagination(pagNum * pageSize, pagNum, pageSize);

            Profile profile = UserCenterServiceSngl.get().getProfileByUid(uid);
            if (profile == null) {
                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
            }
            Map map = new HashMap();
            map.put("uid", profile.getUid());

            List<ProfileDTO> likeProfileList = new ArrayList<ProfileDTO>();
            //喜欢的人
            PageRows<ProfileRelation> likeRelationList = SocialServiceSngl.get().querySrcRelation(profile.getProfileId(), ObjectRelationType.PROFILE, pagination);
            if (likeRelationList == null) {
                JSONObject jsonObject = new JSONObject();
                map.put("priflelist", likeProfileList);
                map.put("page", pagination);
                jsonObject.put("rs", "1");
                jsonObject.put("msg", "success");
                jsonObject.put("result", map);
                return jsonObject.toString();
            }
            Set<String> prifileIdSet = new HashSet<String>();
            for (ProfileRelation relation : likeRelationList.getRows()) {
                prifileIdSet.add(relation.getDestProfileId());
            }

            //组装dto
            Map<String, ProfileDTO> profileDTOMap = userCenterWebLogic.buildProfileDTOByProfileIDs(prifileIdSet);
            for (ProfileRelation relation : likeRelationList.getRows()) {
                if (profileDTOMap.containsKey(relation.getDestProfileId())) {
                    likeProfileList.add(profileDTOMap.get(relation.getDestProfileId()));
                }
            }

            JSONObject jsonObject = new JSONObject();
            map.put("priflelist", likeProfileList);
            map.put("page", likeRelationList.getPage());
            jsonObject.put("rs", "1");
            jsonObject.put("msg", "success");
            jsonObject.put("result", map);
            return jsonObject.toString();
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }

    }

    /**
     * 喜欢TA的人
     */
    @ResponseBody
    @RequestMapping("/likedlist")
    public String likedList(HttpServletRequest request,
                            @RequestParam(value = "uid", required = false) String uidStr,
                            @RequestParam(value = "pnum", required = false) String pnum,
                            @RequestParam(value = "pcount", required = false) String pcount) {
        String uidParam = StringUtil.isEmpty(uidStr) ? request.getParameter("uid") : uidStr;
        if (StringUtil.isEmpty(uidParam)) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        long uid = -1;
        try {
            uid = Long.parseLong(uidParam);
        } catch (NumberFormatException e) {
        }
        if (uid == -1) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        String pageNo = StringUtil.isEmpty(pnum) ? request.getParameter("pnum") : pnum;
        String pageCount = StringUtil.isEmpty(pcount) ? request.getParameter("pcount") : pcount;
        int pagNum = 1;
        try {
            pagNum = Integer.parseInt(pageNo);
        } catch (NumberFormatException e) {
        }
        int pageSize = 24;
        try {
            pageSize = Integer.parseInt(pageCount);
        } catch (NumberFormatException e) {
        }

        try {
            Pagination pagination = new Pagination(pagNum * pageSize, pagNum, pageSize);

            Profile profile = UserCenterServiceSngl.get().getProfileByUid(uid);
            if (profile == null) {
                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
            }
            Map map = new HashMap();
            map.put("uid", profile.getUid());

            List<ProfileDTO> likedProfileList = new ArrayList<ProfileDTO>();
            //喜欢的人
            PageRows<ProfileRelation> likedRelationList = SocialServiceSngl.get().queryDestRelation(profile.getProfileId(), ObjectRelationType.PROFILE, pagination);
            if (likedRelationList == null) {
                JSONObject jsonObject = new JSONObject();
                map.put("priflelist", likedProfileList);
                map.put("page", pagination);
                jsonObject.put("rs", "1");
                jsonObject.put("msg", "success");
                jsonObject.put("result", map);
                return jsonObject.toString();
            }

            Set<String> prifileIdSet = new HashSet<String>();
            for (ProfileRelation relation : likedRelationList.getRows()) {
                prifileIdSet.add(relation.getSrcProfileId());
            }

            //组装dto
            Map<String, ProfileDTO> profileDTOMap = userCenterWebLogic.buildProfileDTOByProfileIDs(prifileIdSet);
            for (ProfileRelation relation : likedRelationList.getRows()) {
                if (profileDTOMap.containsKey(relation.getSrcProfileId())) {
                    likedProfileList.add(profileDTOMap.get(relation.getSrcProfileId()));
                }
            }

            JSONObject jsonObject = new JSONObject();
            map.put("priflelist", likedProfileList);
            map.put("page", likedRelationList.getPage());
            jsonObject.put("rs", "1");
            jsonObject.put("msg", "success");
            jsonObject.put("result", map);
            return jsonObject.toString();
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
    }


    //汉语拼音首字母排序，用于getbyuid接口中的游戏名称排序
    static class ComparatorPinYin implements Comparator<GameDBSimpleDTO> {
        @Override
        public int compare(GameDBSimpleDTO o1, GameDBSimpleDTO o2) {
            return ToPinYinString(o1.getName()).compareTo(ToPinYinString(o2.getName()));
        }

        private String ToPinYinString(String str) {
            StringBuilder sb = new StringBuilder();
            String[] arr = null;

            for (int i = 0; i < str.length(); i++) {
                arr = PinyinHelper.toHanyuPinyinStringArray(str.charAt(i));
                if (arr != null && arr.length > 0) {
                    for (String string : arr) {
                        sb.append(string);
                    }
                }
            }
            return sb.toString();
        }

    }


//    @ResponseBody
//    @RequestMapping("/likepageloading")
//    public String likePicPage(HttpServletRequest request,
//                              @RequestParam(value = "uid", required = false) String uidStr,
//                              @RequestParam(value = "appkey", required = false) String appkey,
//                              @RequestParam(value = "platform", required = false) String platform) {
//        String uidParam = StringUtil.isEmpty(uidStr) ? request.getParameter("uid") : uidStr;
//        if (StringUtil.isEmpty(uidParam)) {
//            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
//        }
//
//        long uid = -1;
//        try {
//            uid = Long.parseLong(uidParam);
//        } catch (NumberFormatException e) {
//        }
//        if (uid == -1) {
//            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
//        }
//        if (StringUtil.isEmpty(appkey)) {
//            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
//        }
//        if (StringUtil.isEmpty(platform)) {
//            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
//        }
//        AppPlatform appPlatform = null;
//        try {
//            appPlatform = AppPlatform.getByCode(Integer.parseInt(platform));
//        } catch (Exception e) {
//        }
//        try {
//            Profile profile = null;
//            if (uid > 0) {
//                profile = UserCenterServiceSngl.get().getProfileByUid(uid);
//            }
//            List<GameClientPicDTO> list = gameClientWebLogic.queryGameClientPicDTO(profile != null ? profile.getProfileId() : "");
//            if (profile != null) {
//                sendOutMiyouTask(profile, getIp(request), appPlatform, appkey, new Date());
//            }
//            Map map = new HashMap();
//            JSONObject jsonObject = new JSONObject();
//            map.put("list", list);
//            jsonObject.put("rs", "1");
//            jsonObject.put("msg", "success");
//            jsonObject.put("result", map);
//            return jsonObject.toString();
//        } catch (ServiceException e) {
//            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
//            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
//        } catch (Exception e) {
//            GAlerter.lab(this.getClass().getName() + " occured Exception.e: ", e);
//            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
//        }
//    }
//
//    private void sendOutMiyouTask(Profile profile, String ip, AppPlatform appPlatform, String appKey, Date date) {
//        try {
//            TaskAwardEvent taskEvent = new TaskAwardEvent();
//            taskEvent.setDoTaskDate(date);
//            taskEvent.setProfileId(profile.getProfileId());
//            taskEvent.setTaskIp(ip);
//            taskEvent.setTaskAction(TaskAction.VIEW_MIYOU);
////            taskEvent.setTaskId(getMiyouPageTaskId(appPlatform));
//            taskEvent.setUno(profile.getUno());
//            taskEvent.setAppkey(AppUtil.getAppKey(appKey));
//            taskEvent.setProfileKey(profile.getProfileKey());
//            taskEvent.setAppPlatform(appPlatform);
//            EventDispatchServiceSngl.get().dispatch(taskEvent);
//        } catch (ServiceException e) {
//            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
//        }
//    }
}
