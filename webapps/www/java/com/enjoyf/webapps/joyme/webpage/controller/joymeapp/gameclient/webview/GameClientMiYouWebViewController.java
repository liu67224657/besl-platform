package com.enjoyf.webapps.joyme.webpage.controller.joymeapp.gameclient.webview;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.comment.*;
import com.enjoyf.platform.service.joymeapp.JoymeAppServiceSngl;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTagField;
import com.enjoyf.platform.service.joymeapp.gameclient.ImgDTO;
import com.enjoyf.platform.service.joymeapp.gameclient.TagDedearchives;
import com.enjoyf.platform.service.joymeapp.gameclient.TagDedearchivesFiled;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.timeline.SocialTimeLineDomain;
import com.enjoyf.platform.service.timeline.SocialTimeLineItem;
import com.enjoyf.platform.service.timeline.TimeLineServiceSngl;
import com.enjoyf.platform.service.usercenter.LoginDomain;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.ProfileSum;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.http.URLUtils;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.util.StringUtil;
import com.enjoyf.webapps.joyme.dto.comment.MainReplyDTO;
import com.enjoyf.webapps.joyme.weblogic.comment.CommentWebLogic;
import com.enjoyf.webapps.joyme.webpage.controller.joymeapp.gameclient.AbstractGameClientBaseController;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by tonydiao on 2014/12/23.
 */

@Controller
@RequestMapping("/joymeapp/gameclient/webview/miyou/")
public class GameClientMiYouWebViewController extends AbstractGameClientBaseController {


    private static final int PAGE_SIZE = 10;

    @Resource(name = "commentWebLogic")
    private CommentWebLogic commentWebLogic;

    //迷友圈list
    @RequestMapping(value = "/list")
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            String uid = HTTPUtil.getParam(request, "uid");
            //    if (StringUtil.isEmpty(uid)) {
            //  return new ModelAndView("/views/jsp/gameclient/404", mapMessage);
            //  }
            String logindomain = HTTPUtil.getParam(request, "logindomain");
            String retype = HTTPUtil.getParam(request, "retype");
            String posttype = HTTPUtil.getParam(request, "posttype");
            mapMessage.put("uid", uid);
            mapMessage.put("retype", retype);
            mapMessage.put("logindomain", logindomain);
            mapMessage.put("posttype", posttype);

            //存放点赞数最多的评论ID
            Set<String> commentIdSet = new HashSet<String>();
            //存放我回复过的帖子ID
            Set<String> historySet = new HashSet<String>();
            //取用户头像信息ID
            Set<String> profileSet = new HashSet<String>();
            Profile profile = null;
            if (!StringUtil.isEmpty(uid)) {
                profile = UserCenterServiceSngl.get().getProfileByUid(Long.parseLong(uid));
                ProfileSum profileSum = UserCenterServiceSngl.get().getProfileSum(profile.getProfileId());
                mapMessage.put("profilesum", profileSum);
            }
            if (StringUtil.isEmpty(retype) || "miyou".equals(retype)) {
                QueryExpress queryExpress = new QueryExpress()
                        .add(QueryCriterions.eq(TagDedearchivesFiled.TAGID, TAGID))
                        .add(QueryCriterions.eq(TagDedearchivesFiled.REMOVE_STATUS, ValidStatus.VALID.getCode()))
                        .add(QuerySort.add(TagDedearchivesFiled.DISPLAY_ORDER, QuerySortOrder.ASC));

                //查询迷友圈
                PageRows<TagDedearchives> tagPageRows = JoymeAppServiceSngl.get().queryTagDedearchivesByPage(true, TAGID, 0, queryExpress, new Pagination(PAGE_SIZE, 1, PAGE_SIZE));
                if (tagPageRows != null && !CollectionUtil.isEmpty(tagPageRows.getRows())) {
                    Set<String> tagSet = new HashSet<String>();
                    for (TagDedearchives tag : tagPageRows.getRows()) {
                        tagSet.add(tag.getDede_archives_id());
                    }
                    Map<String, CommentBean> commentBeanMap = CommentServiceSngl.get().queryCommentBeanByIds(tagSet);
                    if (commentBeanMap != null) {
                        List<CommentBean> miyouList = new ArrayList<CommentBean>(commentBeanMap.values());

                        for (CommentBean commentBean : miyouList) {
                            commentBean.setPic(URLUtils.getJoymeDnUrl(commentBean.getPic()));
                            profileSet.add(commentBean.getUri());
                            historySet.add(commentBean.getCommentId());
                            if (commentBean.getCommentSum() > 0) {
                                commentIdSet.add(commentBean.getCommentId());
                            }
                        }
                        List<CommentBean> returnList = new ArrayList<CommentBean>();
                        String commentId = "";
                        //如果posttype不为空 查询用户发布的最新消息 放在迷友圈第一位显示
                        if (!StringUtil.isEmpty(posttype)) {
                            PageRows<CommentBean> newCommentPageRows = CommentServiceSngl.get().queryCommentBeanByPage(new QueryExpress().add(QueryCriterions.eq(CommentBeanField.URI, profile.getProfileId()))
                                    .add(QueryCriterions.eq(CommentBeanField.DOMAIN, CommentDomain.GAMECLIENT_MIYOU.getCode()))
                                    .add(QueryCriterions.eq(CommentBeanField.REMOVE_STATUS, ActStatus.UNACT.getCode()))
                                    .add(QuerySort.add(CommentBeanField.CREATE_TIME, QuerySortOrder.DESC)), new Pagination(1, 1, 1));
                            if (newCommentPageRows != null && !CollectionUtil.isEmpty(newCommentPageRows.getRows())) {
                                newCommentPageRows.getRows().get(0).setPic(URLUtils.getJoymeDnUrl(newCommentPageRows.getRows().get(0).getPic()));
                                newCommentPageRows.getRows().get(0).setDescription(replaceHtmlText(newCommentPageRows.getRows().get(0).getDescription()));
                                returnList.add(newCommentPageRows.getRows().get(0));
                                commentId = newCommentPageRows.getRows().get(0).getCommentId();
                            }
                        }
                        mapMessage.put("commentId", commentId);
                        for (TagDedearchives tagDedearchives : tagPageRows.getRows()) {
                            for (CommentBean commentBean : miyouList) {
                                commentBean.setDescription(replaceHtmlText(commentBean.getDescription()));

                                commentBean.setPic(URLUtils.getJoymeDnUrl(commentBean.getPic()));
                                if (commentBean.getCommentId().equals(commentId)) {
                                    continue;
                                }
                                if (tagDedearchives.getDede_archives_id().equals(commentBean.getCommentId())) {
                                    returnList.add(commentBean);
                                }
                            }
                        }


                        mapMessage.put("miyoulist", returnList);
                        mapMessage.put("miyoupage", tagPageRows.getPage());
                    }
                }
            } else if ("mymiyou".equals(retype)) {
                //查询我的迷友圈
                if (!StringUtil.isEmpty(logindomain) && !LoginDomain.CLIENT.getCode().equals(logindomain)) {
                    if (profile == null) {
                        return new ModelAndView("/views/jsp/gameclient/404", mapMessage);
                    }
                    PageRows<SocialTimeLineItem> timeLinePageRows = TimeLineServiceSngl.get().querySocialTimeLineItemList(SocialTimeLineDomain.MY_MIYOU, profile.getProfileId(), new Pagination(PAGE_SIZE, 1, PAGE_SIZE));

                    if (timeLinePageRows != null && !CollectionUtil.isEmpty(timeLinePageRows.getRows())) {
                        Set<String> lineSet = new HashSet<String>();
                        for (SocialTimeLineItem timeline : timeLinePageRows.getRows()) {
                            lineSet.add(timeline.getDirectId());
                        }
                        Map<String, CommentBean> commentBeanMap = CommentServiceSngl.get().queryCommentBeanByIds(lineSet);
                        if (commentBeanMap != null) {
                            List<CommentBean> mymiyouList = new ArrayList<CommentBean>(commentBeanMap.values());
                            for (CommentBean commentBean : mymiyouList) {
                                commentBean.setPic(URLUtils.getJoymeDnUrl(commentBean.getPic()));
                                profileSet.add(commentBean.getUri());
                                historySet.add(commentBean.getCommentId());
                                if (commentBean.getCommentSum() > 0) {
                                    commentIdSet.add(commentBean.getCommentId());
                                }
                            }
                            //重新排序
                            Collections.sort(mymiyouList, new Comparator() {
                                public int compare(Object o1, Object o2) {
                                    if (((CommentBean) o1).getCreateTime().getTime() < ((CommentBean) o2).getCreateTime().getTime()) {
                                        return 1;
                                    }
                                    return -1;
                                }
                            });
                            //五天没发贴
                            long lastPost = mymiyouList.get(0).getCreateTime().getTime();    //最后一次发帖时间
                            long fivetimes = 60 * 60 * 24 * 5 * 1000; //5天
                            if ((new Date().getTime() - lastPost) > fivetimes) {
                                mapMessage.put("postguide", "true");
                            }

                            //今天昨天三天前更早前分类
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            Calendar today = Calendar.getInstance();
                            today.add(Calendar.DATE, 0);
                            Calendar today2 = Calendar.getInstance();
                            today2.add(Calendar.DATE, -1);
                            Calendar today3 = Calendar.getInstance();
                            today3.add(Calendar.DATE, -2);

                            String todayString = sdf.format(today.getTime());
                            String today2String = sdf.format(today2.getTime());
                            String today3String = sdf.format(today3.getTime());
                            List<CommentBean> todayList = new ArrayList<CommentBean>();
                            List<CommentBean> yesterdayList = new ArrayList<CommentBean>();
                            List<CommentBean> dayList = new ArrayList<CommentBean>();
                            List<CommentBean> moredayList = new ArrayList<CommentBean>();
                            String postTime = "";
                            for (CommentBean commentBean : mymiyouList) {
                                commentBean.setDescription(replaceHtmlText(commentBean.getDescription()));
                                commentBean.setPic(URLUtils.getJoymeDnUrl(commentBean.getPic()));
                                postTime = sdf.format(commentBean.getCreateTime());
                                if (postTime.contains(todayString)) {
                                    todayList.add(commentBean);
                                } else if (postTime.contains(today2String)) {
                                    yesterdayList.add(commentBean);
                                } else if (postTime.contains(today3String)) {
                                    dayList.add(commentBean);
                                } else {
                                    moredayList.add(commentBean);
                                }
                            }

                            mapMessage.put("todayList", todayList);
                            mapMessage.put("yesterdayList", yesterdayList);
                            mapMessage.put("dayList", dayList);
                            mapMessage.put("moredayList", moredayList);
                            mapMessage.put("mymiyouList", mymiyouList);
                            mapMessage.put("mymiyoupage", timeLinePageRows.getPage());
                        }
                    }
                } else {
                    QueryExpress queryExpress = new QueryExpress()
                            .add(QueryCriterions.eq(TagDedearchivesFiled.TAGID, TAGID))
                            .add(QueryCriterions.eq(TagDedearchivesFiled.REMOVE_STATUS, ValidStatus.VALID.getCode()))
                            .add(QuerySort.add(TagDedearchivesFiled.DISPLAY_ORDER, QuerySortOrder.ASC));

                    //查询迷友圈
                    PageRows<TagDedearchives> tagPageRows = JoymeAppServiceSngl.get().queryTagDedearchivesByPage(true, TAGID, 0, queryExpress, new Pagination(PAGE_SIZE, 1, PAGE_SIZE));
                    if (tagPageRows != null && !CollectionUtil.isEmpty(tagPageRows.getRows())) {
                        Set<String> tagSet = new HashSet<String>();
                        for (TagDedearchives tag : tagPageRows.getRows()) {
                            tagSet.add(tag.getDede_archives_id());
                        }
                        Map<String, CommentBean> commentBeanMap = CommentServiceSngl.get().queryCommentBeanByIds(tagSet);
                        if (commentBeanMap != null) {
                            List<CommentBean> miyouList = new ArrayList<CommentBean>(commentBeanMap.values());

                            for (CommentBean commentBean : miyouList) {
                                commentBean.setPic(URLUtils.getJoymeDnUrl(commentBean.getPic()));
                                profileSet.add(commentBean.getUri());
                                historySet.add(commentBean.getCommentId());
                                if (commentBean.getCommentSum() > 0) {
                                    commentIdSet.add(commentBean.getCommentId());
                                }
                            }
                            List<CommentBean> returnList = new ArrayList<CommentBean>();
                            for (TagDedearchives tagDedearchives : tagPageRows.getRows()) {
                                for (CommentBean commentBean : miyouList) {
                                    commentBean.setDescription(replaceHtmlText(commentBean.getDescription()));
                                    commentBean.setPic(URLUtils.getJoymeDnUrl(commentBean.getPic()));
                                    if (tagDedearchives.getDede_archives_id().equals(commentBean.getCommentId())) {
                                        returnList.add(commentBean);
                                    }
                                }
                            }
                            mapMessage.put("miyoulist", returnList);
                            mapMessage.put("miyoupage", tagPageRows.getPage());
                        }
                    }
                }
            }


            //查询帖子点赞数最多的评论
            Map<String, CommentReply> map = CommentServiceSngl.get().queryHotReplyCacheByAgreeSum(commentIdSet);
            if (map != null) {
                for (CommentReply commentReply : map.values()) {
                    profileSet.add(commentReply.getReplyProfileId());
                }
            }
            List<CommentHistory> commentHistories = null;
            //查询用户点过赞的
            if (!CollectionUtil.isEmpty(historySet)) {
                commentHistories = CommentServiceSngl.get().queryCommentHistory(new QueryExpress().add(QueryCriterions.eq(CommentHistoryField.PROFILE_ID, uid))
                        .add(QueryCriterions.eq(CommentHistoryField.DOMAIN, CommentDomain.GAMECLIENT_MIYOU.getCode()))
                        .add(QueryCriterions.in(CommentHistoryField.OBJECT_ID, historySet.toArray())));
            }
            Map<String, Profile> profilesMap = UserCenterServiceSngl.get().queryProfiles(profileSet);

            mapMessage.put("hotReply", map == null ? "" : map.values());
            mapMessage.put("profiles", profilesMap == null ? "" : profilesMap.values());
            mapMessage.put("commentHistories", commentHistories);
//            mapMessage.put("share_url", ShortUrlUtils.getSinaURL(WebappConfig.get().URL_WWW + "/joymeapp/gameclient/webview/miyou/share?commentid=" + commentId);

            if (StringUtil.isEmpty(retype) || "miyou".equals(retype)) {
                return new ModelAndView("/views/jsp/gameclient/webview/miyou", mapMessage);
            } else {
                return new ModelAndView("/views/jsp/gameclient/webview/mymiyou", mapMessage);
            }

        } catch (ServiceException
                e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/gameclient/404", mapMessage);
        }


//        return new ModelAndView("/views/jsp/gameclient/webview/miyou", mapMessage);
    }

    @RequestMapping(value = "/miyoudetail")
    public ModelAndView miyouDetail(HttpServletRequest request, HttpServletResponse response,
                                    @RequestParam(value = "commentid", required = false) String commentId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            String uid = HTTPUtil.getParam(request, "uid");
            String logindomain = HTTPUtil.getParam(request, "logindomain");
            String appkey = HTTPUtil.getParam(request, "appkey");
            mapMessage.put("uid", uid);
            mapMessage.put("logindomain", logindomain);
            mapMessage.put("appkey", appkey);

            CommentBean commentBean = CommentServiceSngl.get().getCommentBeanById(commentId);
            if (commentBean == null) {
                mapMessage.put("message", "cid.is.empty");
                return new ModelAndView("/views/jsp/common/custompage-wap", mapMessage);
            }
            if (commentBean.getRemoveStatus().equals(ActStatus.ACTED)) {
                mapMessage.put("message", "cid.is.empty");
                return new ModelAndView("/views/jsp/common/custompage-wap", mapMessage);
            }
            commentBean.setPic(URLUtils.getJoymeDnUrl(commentBean.getPic()));
            CommentServiceSngl.get().modifyCommentBeanById(commentId, new UpdateExpress().increase(CommentBeanField.LONG_COMMENT_SUM, 1).increase(CommentBeanField.FIVE_USER_SUM, 1));
            //存放我回复过的帖子ID
            Set<String> historySet = new HashSet<String>();
            //取用户头像信息ID
            Set<String> profileSet = new HashSet<String>();


//            int hotSize = 1;
//            if (commentBean.getCommentSum() <= 10) {
//                hotSize = 1;
//            } else if (commentBean.getCommentSum() > 10 && commentBean.getCommentSum() <= 20) {
//                hotSize = 2;
//            } else if (commentBean.getCommentSum() > 20 && commentBean.getCommentSum() <= 50) {
//                hotSize = 3;
//            } else if (commentBean.getCommentSum() > 50 && commentBean.getCommentSum() <= 100) {
//                hotSize = 4;
//            } else if (commentBean.getCommentSum() > 100) {
//                hotSize = 5;
//            }
//            if (commentBean.getCommentSum() > 10) {
//                List<MainReplyDTO> hotList = commentWebLogic.queryHotMainReplyDTO(commentBean, hotSize);
//                mapMessage.put("hotList", hotList);
//                if (!CollectionUtil.isEmpty(hotList)) {
//                    Map<Long, Long> hotMap = new HashMap<Long, Long>();
//                    for (MainReplyDTO mainReplyDTO : hotList) {
//                        hotMap.put(mainReplyDTO.getReply().getReply().getRid(), mainReplyDTO.getReply().getReply().getRid());
//                        historySet.add(String.valueOf(mainReplyDTO.getReply().getReply().getRid()));
//                    }
//
//                }
//            }
            mapMessage.put("hotMap", new HashMap<Long, Long>());
            int pageNum = 1;
            int pageSize = 15;
            PageRows<MainReplyDTO> mainReplyRows = commentWebLogic.queryMainReplyDTO(commentBean, pageNum, pageSize, true);
            if (mainReplyRows != null && !CollectionUtil.isEmpty(mainReplyRows.getRows())) {
                for (MainReplyDTO mainReplyDTO : mainReplyRows.getRows()) {
                    mainReplyDTO.getReply().getReply().getBody().setText(mainReplyDTO.getReply().getReply().getBody().getText().replaceAll("\n", "<br/>"));
                    historySet.add(String.valueOf(mainReplyDTO.getReply().getReply().getRid()));
                }
            }

            mapMessage.put("mainReplyRows", mainReplyRows);
            historySet.add(commentBean.getCommentId());
            profileSet.add(commentBean.getUri());
            //查询用户点过赞的
            List<CommentHistory> commentHistories = null;
            //查询用户点过赞的
            if (!CollectionUtil.isEmpty(historySet)) {
                commentHistories = CommentServiceSngl.get().queryCommentHistory(new QueryExpress().add(QueryCriterions.eq(CommentHistoryField.PROFILE_ID, uid))
                        .add(QueryCriterions.eq(CommentHistoryField.DOMAIN, CommentDomain.GAMECLIENT_MIYOU.getCode()))
                        .add(QueryCriterions.in(CommentHistoryField.OBJECT_ID, historySet.toArray())));
            }
            Map<String, Profile> profilesMap = UserCenterServiceSngl.get().queryProfiles(profileSet);

            long createTimeLong = commentBean.getCreateTime().getTime();
            mapMessage.put("profiles", profilesMap == null ? "" : profilesMap.values());
            mapMessage.put("commentHistories", commentHistories);
            commentBean.setDescription(replaceHtmlText(commentBean.getDescription()));
            if (!StringUtil.isEmpty(commentBean.getExpandstr())) {
                List<String> pics = CommentBean.fromJson(commentBean.getExpandstr());
                if (!CollectionUtils.isEmpty(pics)) {
                    List<ImgDTO> returnPics = JoymeAppServiceSngl.get().picInfoListCache(commentId, pics);
                    mapMessage.put("pics", returnPics);
                }
            }
            mapMessage.put("sharedesc", commentBean.getDescription().replaceAll("\n", " "));
            if (!com.enjoyf.platform.util.StringUtil.isEmpty(commentBean.getDescription())) {
                commentBean.setDescription(commentBean.getDescription().replaceAll("\n", "<br/>").replaceAll(" ", "&nbsp;"));
            }
            mapMessage.put("shareimg", commentBean.getPic() + "?imageView2/1/w/212/h/212" + URLEncoder.encode("|", "utf-8") + "imageMogr2/thumbnail/212x212" + URLEncoder.encode("!", "utf-8"));
            mapMessage.put("commentBean", commentBean);
            mapMessage.put("createTimeLong", createTimeLong);
            mapMessage.put("share_url", ShortUrlUtils.getSinaURL("http://api." + WebappConfig.get().DOMAIN + "/joymeapp/gameclient/webview/miyou/share?commentid=" + commentId));

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/gameclient/404", mapMessage);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return new ModelAndView("/views/jsp/gameclient/webview/miyoudetail", mapMessage);
    }

    @RequestMapping(value = "/share")
    public ModelAndView share
            (HttpServletRequest
                     request, HttpServletResponse
                    response,
             @RequestParam(value = "commentid", required = false) String
                     commentId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            String uid = HTTPUtil.getParam(request, "uid");
            mapMessage.put("uid", uid);

            CommentBean commentBean = CommentServiceSngl.get().getCommentBeanById(commentId);
            if (commentBean == null) {
                mapMessage.put("message", "cid.is.empty");
                return new ModelAndView("/views/jsp/common/custompage-wap", mapMessage);
            }
            commentBean.setPic(URLUtils.getJoymeDnUrl(commentBean.getPic()));
            if (!StringUtil.isEmpty(commentBean.getExpandstr())) {
                List<String> pics = CommentBean.fromJson(commentBean.getExpandstr());
                if (!CollectionUtils.isEmpty(pics)) {
                    mapMessage.put("pics", pics);
                }
            }
            CommentServiceSngl.get().modifyCommentBeanById(commentId, new UpdateExpress().increase(CommentBeanField.LONG_COMMENT_SUM, 1));
            //存放我回复过的帖子ID
            Set<String> historySet = new HashSet<String>();
            //取用户头像信息ID
            Set<String> profileSet = new HashSet<String>();


            int hotSize = 1;
            if (commentBean.getCommentSum() <= 10) {
                hotSize = 1;
            } else if (commentBean.getCommentSum() > 10 && commentBean.getCommentSum() <= 20) {
                hotSize = 2;
            } else if (commentBean.getCommentSum() > 20 && commentBean.getCommentSum() <= 50) {
                hotSize = 3;
            } else if (commentBean.getCommentSum() > 50 && commentBean.getCommentSum() <= 100) {
                hotSize = 4;
            } else if (commentBean.getCommentSum() > 100) {
                hotSize = 5;
            }
            if (commentBean.getCommentSum() > 10) {
                List<MainReplyDTO> hotList = commentWebLogic.queryHotMainReplyDTO(commentBean, hotSize,true);
                mapMessage.put("hotList", hotList);
                if (!CollectionUtil.isEmpty(hotList)) {
                    Map<Long, Long> hotMap = new HashMap<Long, Long>();
                    for (MainReplyDTO mainReplyDTO : hotList) {
                        hotMap.put(mainReplyDTO.getReply().getReply().getRid(), mainReplyDTO.getReply().getReply().getRid());
                        historySet.add(String.valueOf(mainReplyDTO.getReply().getReply().getRid()));
                    }
                    mapMessage.put("hotMap", hotMap);
                }
            }

            int pageNum = 1;
            int pageSize = 15;
            PageRows<MainReplyDTO> mainReplyRows = commentWebLogic.queryMainReplyDTO(commentBean, pageNum, pageSize, true);
            if (mainReplyRows != null && !CollectionUtil.isEmpty(mainReplyRows.getRows())) {
                for (MainReplyDTO mainReplyDTO : mainReplyRows.getRows()) {
                    historySet.add(String.valueOf(mainReplyDTO.getReply().getReply().getRid()));
                }
            }

            mapMessage.put("mainReplyRows", mainReplyRows);
            historySet.add(commentBean.getCommentId());
            profileSet.add(commentBean.getUri());
            //查询用户点过赞的
//            List<CommentHistory> commentHistories = CommentServiceSngl.get().queryCommentHistory(new QueryExpress().add(QueryCriterions.eq(CommentHistoryField.PROFILE_ID, uid))
//                    .add(QueryCriterions.eq(CommentHistoryField.DOMAIN, CommentDomain.GAMECLIENT_MIYOU.getCode()))
//                    .add(QueryCriterions.in(CommentHistoryField.OBJECT_ID, historySet.toArray())));

            Map<String, Profile> profilesMap = UserCenterServiceSngl.get().queryProfiles(profileSet);

            mapMessage.put("profiles", profilesMap == null ? "" : profilesMap.values());
//            mapMessage.put("commentHistories", commentHistories);
            mapMessage.put("title", commentBean.getDescription().replaceAll("\n", " "));
            if (!com.enjoyf.platform.util.StringUtil.isEmpty(commentBean.getDescription())) {
                commentBean.setDescription(commentBean.getDescription().replaceAll("\n", "<br/>"));
            }
            mapMessage.put("commentBean", commentBean);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/gameclient/404", mapMessage);
        }

        return new ModelAndView("/views/jsp/gameclient/webview/miyoushare", mapMessage);
    }

    @RequestMapping(value = "/deletereply")
    public ModelAndView deleteReply
            (HttpServletRequest
                     request, HttpServletResponse
                    response,
             @RequestParam(value = "rid", required = false) String
                     rid,
             @RequestParam(value = "uid", required = false) String
                     uid,
             @RequestParam(value = "commentid", required = false) String
                     commentId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            Map<String, String> jParam = getJParam(request.getHeader("JParam"));
            if (!CollectionUtil.isEmpty(jParam)) {
                uid = jParam.get("uid");
            }
            mapMessage.put("uid", uid);
            if (StringUtil.isEmpty(uid) || StringUtil.isEmpty(rid) || StringUtil.isEmpty(commentId)) {
                mapMessage.put("message", "oauth.param.is.null");
                return new ModelAndView("/views/jsp/common/custompage-wap", mapMessage);
            }

            CommentBean commentBean = CommentServiceSngl.get().getCommentBeanById(commentId);
            if (commentBean == null) {
                mapMessage.put("message", "cid.is.empty");
                return new ModelAndView("/views/jsp/common/custompage-wap", mapMessage);
            }
            Profile profile = UserCenterServiceSngl.get().getProfileByUid(Long.parseLong(uid));
            if (profile == null) {
                mapMessage.put("message", "profile.default.is.null");
                return new ModelAndView("/views/jsp/common/custompage-wap", mapMessage);
            }
            CommentReply commentReply = CommentServiceSngl.get().getCommentReply(new QueryExpress().add(QueryCriterions.eq(CommentReplyField.REPLY_ID, Long.parseLong(rid)))
                    .add(QueryCriterions.eq(CommentReplyField.REPLY_UNO, profile.getUno())));
            if (commentReply == null) {
                mapMessage.put("message", "comment.bean.null");
                return new ModelAndView("/views/jsp/common/custompage-wap", mapMessage);
            }
            CommentServiceSngl.get().removeCommentReply(commentId, Long.parseLong(rid), 0l, null);
            JoymeAppServiceSngl.get().modifyAnimeTag(commentBean.getGroupId(), new QueryExpress().add(QueryCriterions.eq(AnimeTagField.TAG_ID, commentBean.getGroupId())),
                    new UpdateExpress().increase(AnimeTagField.FAVORITE_NUM, -1l));
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/gameclient/404", mapMessage);
        }

        return new ModelAndView("redirect:/joymeapp/gameclient/webview/miyou/miyoudetail?commentid=" + commentId, mapMessage);
    }
}
