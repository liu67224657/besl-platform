package com.enjoyf.webapps.joyme.webpage.controller.joymeapp.gameclient.api;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.comment.*;
import com.enjoyf.platform.service.event.EventDispatchServiceSngl;
import com.enjoyf.platform.service.event.system.GameClientMiyouPostEvent;
import com.enjoyf.platform.service.joymeapp.JoymeAppServiceSngl;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTag;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTagAppType;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTagField;
import com.enjoyf.platform.service.joymeapp.gameclient.ArchiveRelationType;
import com.enjoyf.platform.service.joymeapp.gameclient.TagDedearchives;
import com.enjoyf.platform.service.joymeapp.gameclient.TagDedearchivesFiled;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.timeline.SocialTimeLineDomain;
import com.enjoyf.platform.service.timeline.SocialTimeLineItem;
import com.enjoyf.platform.service.timeline.SocialTimeLineItemField;
import com.enjoyf.platform.service.timeline.TimeLineServiceSngl;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.util.*;
import com.enjoyf.webapps.joyme.dto.comment.GroupDTO;
import com.enjoyf.webapps.joyme.dto.comment.MiyouDTO;
import com.enjoyf.webapps.joyme.weblogic.comment.CommentWebLogic;
import com.enjoyf.webapps.joyme.webpage.controller.joymeapp.gameclient.AbstractGameClientBaseController;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/6/29
 * Description:
 */
@Controller
@RequestMapping("/joymeapp/gameclient/api/miyou")
public class GameClientApiMiyouController extends AbstractGameClientBaseController {


    @Resource(name = "commentWebLogic")
    private CommentWebLogic commentWebLogic;


    /**
     * 迷友发布接口
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/post")
    @ResponseBody
    public String post(HttpServletRequest request, HttpServletResponse response) {
        String uidParam = HTTPUtil.getParam(request, "uid");
        String appkey = HTTPUtil.getParam(request, "appkey");
        String text = request.getParameter("text");
        String pic = request.getParameter("pic");
        String tag = request.getParameter("tag") == null ? "" : request.getParameter("tag");


        long uid = -1l;
        try {
            uid = Long.parseLong(uidParam);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        if (uid < 0l || StringUtil.isEmpty(appkey) || StringUtil.isEmpty(text)
                || StringUtil.isEmpty(pic)) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        try {
            Profile profile = UserCenterServiceSngl.get().getProfileByUid(uid);
            if (profile == null) {
                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
            }

            //禁言
            CommentForbid forbid = CommentServiceSngl.get().getCommentForbidByCache(profile.getProfileId());
            if (forbid != null) {
                return ResultCodeConstants.COMMENT_PROFILE_FORBID.getJsonString();
            }

            String uniqueKey = CommentUtil.genUniqueByUUID();
            String commentId = CommentUtil.genCommentId(uniqueKey, CommentDomain.GAMECLIENT_MIYOU);
            CommentBean commentBean = new CommentBean();
            commentBean.setPic(pic);
            List<String> jsonPics = new ArrayList<String>();
            jsonPics.add(pic);
            commentBean.setExpandstr(JsonBinder.buildNonDefaultBinder().toJson(jsonPics));
            commentBean.setDescription(commentWebLogic.postreply(text));
            commentBean.setUri(profile.getProfileId());
            commentBean.setDomain(CommentDomain.GAMECLIENT_MIYOU);
            commentBean.setUniqueKey(uniqueKey);
            commentBean.setCommentId(commentId);
            commentBean.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
            commentBean.setCreateTime(new Date());
            commentBean.setGroupId(-1000l);
            CommentServiceSngl.get().createCommentBean(commentBean);


            commentBean = CommentServiceSngl.get().getCommentBeanById(commentId);
            if (commentBean != null) {
                //sendout insert anime_tag  insert timeline
                GameClientMiyouPostEvent postEvent = new GameClientMiyouPostEvent();
                postEvent.setCreateTime(commentBean.getCreateTime());
                postEvent.setProfileId(profile.getProfileId());
                postEvent.setDirectId(commentBean.getCommentId());
                postEvent.setTag(tag);
                postEvent.setGroupId("-1000");
                EventDispatchServiceSngl.get().dispatch(postEvent);
            }

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);

            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();

        }


        return ResultCodeConstants.SUCCESS.getJsonString();
    }

    @RequestMapping("/newpost")
    @ResponseBody
    public String newPost(HttpServletRequest request, HttpServletResponse response) {
        String uidParam = HTTPUtil.getParam(request, "uid");
        String appkey = HTTPUtil.getParam(request, "appkey");
        String text = request.getParameter("text");
        String pic = request.getParameter("pics");
        String groupid = request.getParameter("groupid");
        String tag = request.getParameter("tag") == null ? "" : request.getParameter("tag");


        long uid = -1l;
        try {
            uid = Long.parseLong(uidParam);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        if (uid < 0l || StringUtil.isEmpty(appkey) || StringUtil.isEmpty(groupid) || (StringUtil.isEmpty(text) && StringUtil.isEmpty(pic))) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }
        Long groupIdLong = Long.parseLong(groupid);
        try {
            AnimeTag animeTag = JoymeAppServiceSngl.get().getAnimeTag(groupIdLong, new QueryExpress()
                    .add(QueryCriterions.eq(AnimeTagField.APP_TYPE, AnimeTagAppType.SYHB_QUANZI.getCode()))
                    .add(QueryCriterions.eq(AnimeTagField.REMOVE_STATUS, ValidStatus.VALID.getCode())));

            if (animeTag == null) {
                return ResultCodeConstants.TAGID_NOT_EXISTS.getJsonString();
            }
            Profile profile = UserCenterServiceSngl.get().getProfileByUid(uid);
            if (profile == null) {
                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
            }

            //禁言
            CommentForbid forbid = CommentServiceSngl.get().getCommentForbidByCache(profile.getProfileId());
            if (forbid != null) {
                return ResultCodeConstants.COMMENT_PROFILE_FORBID.getJsonString();
            }

            String uniqueKey = CommentUtil.genUniqueByUUID();
            String commentId = CommentUtil.genCommentId(uniqueKey, CommentDomain.GAMECLIENT_MIYOU);
            CommentBean commentBean = new CommentBean();
            //commentBean.setPic(pic);
            commentBean.setExpandstr(StringUtil.isEmpty(pic) ? "" : pic);
            commentBean.setDescription(StringUtil.isEmpty(text) ? "" : commentWebLogic.postreply(text));
            commentBean.setUri(profile.getProfileId());
            commentBean.setDomain(CommentDomain.GAMECLIENT_MIYOU);
            commentBean.setUniqueKey(uniqueKey);
            commentBean.setCommentId(commentId);
            commentBean.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
            commentBean.setCreateTime(new Date());
            commentBean.setGroupId(groupIdLong);
            if (!StringUtil.isEmpty(pic)) {
                List<String> pics = fromJson(pic);
                if (!CollectionUtil.isEmpty(pics)) {
                    commentBean.setPic(pics.get(0));
                }
            }
            CommentServiceSngl.get().createCommentBean(commentBean);


            commentBean = CommentServiceSngl.get().getCommentBeanById(commentId);
            if (commentBean != null) {
                //sendout insert anime_tag  insert timeline
                JoymeAppServiceSngl.get().modifyAnimeTag(groupIdLong,
                        new QueryExpress().add(QueryCriterions.eq(AnimeTagField.TAG_ID, groupIdLong)),
                        new UpdateExpress().increase(AnimeTagField.PLAY_NUM, 1l));

                GameClientMiyouPostEvent postEvent = new GameClientMiyouPostEvent();
                postEvent.setCreateTime(commentBean.getCreateTime());
                postEvent.setProfileId(profile.getProfileId());
                postEvent.setDirectId(commentBean.getCommentId());
                postEvent.setTag(tag);
                postEvent.setGroupId(groupid);
                EventDispatchServiceSngl.get().dispatch(postEvent);
            }

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
        return ResultCodeConstants.SUCCESS.getJsonString();
    }

    @RequestMapping("/delpost")
    @ResponseBody
    public String delPost(HttpServletRequest request, HttpServletResponse response) {
        String uidParam = HTTPUtil.getParam(request, "uid");
        String commentid = HTTPUtil.getParam(request, "commentid");
        try {

            if (StringUtil.isEmpty(uidParam) || StringUtil.isEmpty(commentid)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            }
            CommentBean commentBean = CommentServiceSngl.get().getCommentBean(new QueryExpress().add(QueryCriterions.eq(CommentBeanField.COMMENT_ID, commentid)));
            if (commentBean == null) {
                return ResultCodeConstants.COMMENT_BEAN_NULL.getJsonString();
            }
            Profile profile = UserCenterServiceSngl.get().getProfileByUid(Long.parseLong(uidParam));
            if (profile == null) {
                return ResultCodeConstants.LOGIN_JSON_PROFILE_IS_NULL.getJsonString();
            }
            if (!profile.getProfileId().equals(commentBean.getUri())) {
                return ResultCodeConstants.COMMENT_PROFILE_FORBID.getJsonString();
            }

            boolean bool = CommentServiceSngl.get().modifyCommentBeanById(commentid, new UpdateExpress().set(CommentBeanField.REMOVE_STATUS, ActStatus.ACTED.getCode()));
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(TagDedearchivesFiled.DEDE_ARCHIVES_ID, commentid));
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(TagDedearchivesFiled.REMOVE_STATUS, ValidStatus.REMOVED.getCode());
            boolean bval = JoymeAppServiceSngl.get().modifyTagDedearchives(-2l, commentid, queryExpress, updateExpress, ArchiveRelationType.TAG_RELATION);

            UpdateExpress timeLineUpdateExpress = new UpdateExpress();
            timeLineUpdateExpress.set(SocialTimeLineItemField.REMOVE_STATUS, ActStatus.ACTED.getCode());
            QueryExpress timeLineQueryExpress = new QueryExpress();
            timeLineQueryExpress.add(QueryCriterions.eq(SocialTimeLineItemField.OWN_UNO, commentBean.getUri()))
                    .add(QueryCriterions.eq(SocialTimeLineItemField.CONTENT_ID, commentBean.getCommentId()));

            TimeLineServiceSngl.get().modifySocialTimeLineItem(SocialTimeLineDomain.MY_MIYOU, commentBean.getUri(), timeLineUpdateExpress, timeLineQueryExpress);
            if (bool && !commentBean.getRemoveStatus().equals(ActStatus.ACTED)) {
                long groupId = commentBean.getGroupId();
                JoymeAppServiceSngl.get().modifyAnimeTag(groupId,
                        new QueryExpress().add(QueryCriterions.eq(AnimeTagField.TAG_ID, groupId)),
                        new UpdateExpress().increase(AnimeTagField.PLAY_NUM, -1l));
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
        return ResultCodeConstants.SUCCESS.getJsonString();
    }

    @RequestMapping("/list")
    @ResponseBody
    public String miyouList(HttpServletRequest request) {
        String uidParam = HTTPUtil.getParam(request, "uid");
        if (StringUtil.isEmpty(uidParam)) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }
        int curPageNo = 1;
        try {
            curPageNo = Integer.parseInt(request.getParameter("pnum"));
        } catch (NumberFormatException e) {
        }
        int pageSize = 20;
        try {
            pageSize = Integer.parseInt(request.getParameter("pcount"));
        } catch (NumberFormatException e) {
        }

        try {
            List<AnimeTag> animeTags = JoymeAppServiceSngl.get().queryAnimeTag(new QueryExpress()
                    .add(QueryCriterions.eq(AnimeTagField.APP_TYPE, AnimeTagAppType.SYHB_QUANZI.getCode()))
                    .add(QueryCriterions.eq(AnimeTagField.REMOVE_STATUS, ValidStatus.VALID.getCode()))
                    .add(QuerySort.add(AnimeTagField.DISPLAY_ORDER, QuerySortOrder.ASC)));

            if (CollectionUtil.isEmpty(animeTags)) {
                return ResultCodeConstants.ANIME_TAG_IS_NULL.getJsonString();
            }
            List<GroupDTO> returnGroups = new ArrayList<GroupDTO>();
            Map<Long, String> groupMap = new HashMap<Long, String>();
            for (AnimeTag animeTag : animeTags) {
                groupMap.put(animeTag.getTag_id(), animeTag.getTag_name());
                GroupDTO groupDTO = new GroupDTO();
                groupDTO.setJt("40");
                groupDTO.setJi(String.valueOf(animeTag.getTag_id()));
                groupDTO.setName(animeTag.getTag_name());
                groupDTO.setType("1");
                groupDTO.setPicurl(animeTag.getPicjson().getIos());
                groupDTO.setPepolenum(String.valueOf(animeTag.getTotal_sum()));
                groupDTO.setDesc(animeTag.getTag_desc());
                returnGroups.add(groupDTO);
            }

            Pagination pagination = new Pagination(curPageNo * pageSize, curPageNo, pageSize);
            QueryExpress queryExpress = new QueryExpress()
                    .add(QueryCriterions.eq(TagDedearchivesFiled.TAGID, TAGID))
                    .add(QueryCriterions.in(TagDedearchivesFiled.DISPLAY_TAG, groupMap.keySet().toArray()))
                    .add(QueryCriterions.eq(TagDedearchivesFiled.REMOVE_STATUS, ValidStatus.VALID.getCode()))
                    .add(QuerySort.add(TagDedearchivesFiled.DISPLAY_ORDER, QuerySortOrder.ASC));

            //查询迷友圈

            PageRows<TagDedearchives> tagPageRows = JoymeAppServiceSngl.get().queryTagDedearchivesByPage(true, TAGID, 0, queryExpress, pagination);
            Map resultMap = new HashMap();
            resultMap.put("rows", new ArrayList<MiyouDTO>());
            if (tagPageRows != null && !CollectionUtil.isEmpty(tagPageRows.getRows())) {
                Set<String> tagSet = new HashSet<String>();
                for (TagDedearchives tag : tagPageRows.getRows()) {
                    tagSet.add(tag.getDede_archives_id());
                }
                Map<String, CommentBean> commentBeanMap = CommentServiceSngl.get().queryCommentBeanByIds(tagSet);
                List<MiyouDTO> miyouDTOList = commentWebLogic.buildMiyou(commentBeanMap, groupMap, uidParam);
                if (!CollectionUtil.isEmpty(miyouDTOList)) {
                    List<MiyouDTO> returnMiyouDTOs = new ArrayList<MiyouDTO>();
                    for (TagDedearchives tagDedearchives : tagPageRows.getRows()) {
                        for (MiyouDTO miyouDTO : miyouDTOList) {
                            if (tagDedearchives.getDede_archives_id().equals(miyouDTO.getCommentid())) {
                                returnMiyouDTOs.add(miyouDTO);
                            }
                        }
                    }
                    resultMap.put("rows", returnMiyouDTOs);
                }
            }
            resultMap.put("groups", returnGroups);
            resultMap.put("page", tagPageRows.getPage());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("rs", String.valueOf(ResultCodeConstants.SUCCESS.getCode()));
            jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
            jsonObject.put("result", resultMap);

            return jsonObject.toString();
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
    }

    @RequestMapping("/grouplist")
    @ResponseBody
    public String groupList(HttpServletRequest request) {
        String uidParam = HTTPUtil.getParam(request, "uid");
        String groupId = HTTPUtil.getParam(request, "groupid");
        if (StringUtil.isEmpty(uidParam) || StringUtil.isEmpty(groupId)) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }
        int curPageNo = 1;
        try {
            curPageNo = Integer.parseInt(request.getParameter("pnum"));
        } catch (NumberFormatException e) {
        }
        int pageSize = 20;
        try {
            pageSize = Integer.parseInt(request.getParameter("pcount"));
        } catch (NumberFormatException e) {
        }
        Map resultMap = new HashMap();
        JSONObject jsonObject = null;
        try {
            Long groupLongId = Long.parseLong(groupId);
            AnimeTag animeTag = JoymeAppServiceSngl.get().getAnimeTag(groupLongId, new QueryExpress()
                    .add(QueryCriterions.eq(AnimeTagField.TAG_ID, groupLongId))
                    .add(QueryCriterions.eq(AnimeTagField.APP_TYPE, AnimeTagAppType.SYHB_QUANZI.getCode()))
                    .add(QueryCriterions.eq(AnimeTagField.REMOVE_STATUS, ValidStatus.VALID.getCode())));
            if (animeTag == null) {
                return ResultCodeConstants.ANIME_TAG_IS_NULL.getJsonString();
            }
            //圈子信息
            GroupDTO groupDTO = new GroupDTO();
            groupDTO.setName(animeTag.getTag_name());
            groupDTO.setPicurl(animeTag.getPicjson().getIos());
            groupDTO.setDesc(animeTag.getTag_desc());
            groupDTO.setPepolenum(String.valueOf(animeTag.getTotal_sum()));
            resultMap.put("headinfo", groupDTO);

            Pagination pagination = new Pagination(curPageNo * pageSize, curPageNo, pageSize);

            Map<Long, String> groupMap = new HashMap<Long, String>();
            groupMap.put(animeTag.getTag_id(), animeTag.getTag_name());

            QueryExpress queryExpress = new QueryExpress()
                    .add(QueryCriterions.eq(TagDedearchivesFiled.TAGID, TAGID))
                    .add(QueryCriterions.eq(TagDedearchivesFiled.DISPLAY_TAG, groupLongId))
                    .add(QueryCriterions.eq(TagDedearchivesFiled.REMOVE_STATUS, ValidStatus.VALID.getCode()))
                    .add(QuerySort.add(TagDedearchivesFiled.DISPLAY_ORDER, QuerySortOrder.ASC));

            //查询迷友圈
            PageRows<TagDedearchives> tagPageRows = JoymeAppServiceSngl.get().queryTagDedearchivesByPage(true, TAGID, 0, queryExpress, pagination);

            resultMap.put("rows", new ArrayList<MiyouDTO>());
            if (tagPageRows != null && !CollectionUtil.isEmpty(tagPageRows.getRows())) {
                Set<String> tagSet = new HashSet<String>();
                for (TagDedearchives tag : tagPageRows.getRows()) {
                    tagSet.add(tag.getDede_archives_id());
                }
                //查询帖子的信息
                Map<String, CommentBean> commentBeanMap = CommentServiceSngl.get().queryCommentBeanByIds(tagSet);

                //拼装实体
                List<MiyouDTO> miyouDTOList = commentWebLogic.buildMiyou(commentBeanMap, groupMap, uidParam);
                if (!CollectionUtil.isEmpty(miyouDTOList)) {
                    List<MiyouDTO> returnMiyouDTOs = new ArrayList<MiyouDTO>();
                    //按顺序排列
                    for (TagDedearchives tagDedearchives : tagPageRows.getRows()) {
                        for (MiyouDTO miyouDTO : miyouDTOList) {
                            if (tagDedearchives.getDede_archives_id().equals(miyouDTO.getCommentid())) {
                                returnMiyouDTOs.add(miyouDTO);
                            }
                        }
                    }
                    resultMap.put("rows", returnMiyouDTOs);
                }
            }
            jsonObject = new JSONObject();

            resultMap.put("page", tagPageRows.getPage());
            jsonObject.put("rs", String.valueOf(ResultCodeConstants.SUCCESS.getCode()));
            jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
            jsonObject.put("result", resultMap);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
        return jsonObject.toString();
    }


    @RequestMapping("/u")
    @ResponseBody
    public String home(HttpServletRequest request) {
        String uidParam = HTTPUtil.getParam(request, "uid");

        if (StringUtil.isEmpty(uidParam)) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }
        int curPageNo = 1;
        try {
            curPageNo = Integer.parseInt(request.getParameter("pnum"));
        } catch (NumberFormatException e) {
        }
        int pageSize = 20;
        try {
            pageSize = Integer.parseInt(request.getParameter("pcount"));
        } catch (NumberFormatException e) {
        }
        JSONObject jsonObject = null;
        try {
            Map resultMap = new HashMap();
            Pagination pagination = new Pagination(curPageNo * pageSize, curPageNo, pageSize);

            Profile profile = UserCenterServiceSngl.get().getProfileByUid(Long.parseLong(uidParam));
            if (profile == null) {
                return ResultCodeConstants.LOGIN_JSON_PROFILE_IS_NULL.getJsonString();
            }
            //查询用户发布的帖子
            PageRows<SocialTimeLineItem> timeLinePageRows = TimeLineServiceSngl.get().querySocialTimeLineItemList(SocialTimeLineDomain.MY_MIYOU, profile.getProfileId(), pagination);

            if (timeLinePageRows != null && !CollectionUtil.isEmpty(timeLinePageRows.getRows())) {
                Set<String> lineSet = new HashSet<String>();
                for (SocialTimeLineItem timeline : timeLinePageRows.getRows()) {
                    lineSet.add(timeline.getDirectId());
                }
                //获得帖子的信息
                Map<String, CommentBean> commentBeanMap = CommentServiceSngl.get().queryCommentBeanByIds(lineSet);
                //查询圈子的信息
                List<AnimeTag> animeTags = JoymeAppServiceSngl.get().queryAnimeTag(new QueryExpress()
                        .add(QueryCriterions.eq(AnimeTagField.APP_TYPE, AnimeTagAppType.SYHB_QUANZI.getCode()))
                        .add(QueryCriterions.eq(AnimeTagField.REMOVE_STATUS, ValidStatus.VALID.getCode()))
                        .add(QuerySort.add(AnimeTagField.DISPLAY_ORDER, QuerySortOrder.ASC)));

                Map<Long, String> groupMap = new HashMap<Long, String>();
                for (AnimeTag animeTag : animeTags) {
                    groupMap.put(animeTag.getTag_id(), animeTag.getTag_name());
                }
                //拼装实体
                List<MiyouDTO> miyouDTOList = commentWebLogic.buildMiyou(commentBeanMap, groupMap, uidParam);
                if (!CollectionUtil.isEmpty(miyouDTOList)) {
                    List<MiyouDTO> returnMiyouDTOs = new ArrayList<MiyouDTO>();
                    //按用户发布的顺序排列
                    for (SocialTimeLineItem socialTimeLineItem : timeLinePageRows.getRows()) {
                        for (MiyouDTO miyouDTO : miyouDTOList) {
                            if (socialTimeLineItem.getDirectId().equals(miyouDTO.getCommentid())) {
                                returnMiyouDTOs.add(miyouDTO);
                            }
                        }
                    }
                    resultMap.put("rows", returnMiyouDTOs);
                }
                resultMap.put("page", timeLinePageRows.getPage());
                jsonObject = new JSONObject();
                jsonObject.put("rs", String.valueOf(ResultCodeConstants.SUCCESS.getCode()));
                jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
                jsonObject.put("result", resultMap);
            } else {
                return ResultCodeConstants.COMMENT_BEAN_NULL.getJsonString();
            }

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }

        return jsonObject.toString();
    }


}
