package com.enjoyf.webapps.tools.webpage.controller.forigncontentreply;

import com.enjoyf.platform.crypto.MD5Util;
import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.comment.*;
import com.enjoyf.platform.service.joymeapp.ClientLineItemField;
import com.enjoyf.platform.service.joymeapp.JoymeAppServiceSngl;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTag;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTagAppType;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTagField;
import com.enjoyf.platform.service.joymeapp.gameclient.*;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.timeline.SocialTimeLineDomain;
import com.enjoyf.platform.service.timeline.SocialTimeLineItem;
import com.enjoyf.platform.service.timeline.SocialTimeLineItemField;
import com.enjoyf.platform.service.timeline.TimeLineServiceSngl;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.text.ResolveContent;
import com.enjoyf.platform.text.TextProcessorFatctory;
import com.enjoyf.platform.text.WordProcessorKey;
import com.enjoyf.platform.text.processor.TextProcessor;
import com.enjoyf.platform.util.CollectionUtil;
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

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zhimingli on 2015/7/3.
 */
@Controller
@RequestMapping(value = "/comment/bean")
public class CommentBeanController extends ToolsBaseController {

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,      //数据库记录索引
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "20") int pageSize,
                             @RequestParam(value = "commentdomain", required = false, defaultValue = "7") String commentdomain,
                             @RequestParam(value = "searchtitle", required = false) String searchtitle,
                             @RequestParam(value = "tagid", required = false, defaultValue = "-2") Long tagid,
                             @RequestParam(value = "remove_status", required = false) String remove_status,
                             @RequestParam(value = "profilenick", required = false) String nick,
                             @RequestParam(value = "startdate", required = false) String startDate,
                             @RequestParam(value = "enddate", required = false) String endDate,
                             @RequestParam(value = "quanzi", required = false) String quanZi,
                             @RequestParam(value = "virtual_user", required = false) String virtualUser) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            List<AnimeTag> animeTags = JoymeAppServiceSngl.get().queryAnimeTag(new QueryExpress()
                    .add(QueryCriterions.eq(AnimeTagField.APP_TYPE, AnimeTagAppType.SYHB_QUANZI.getCode()))
                    .add(QueryCriterions.eq(AnimeTagField.REMOVE_STATUS, ValidStatus.VALID.getCode()))
                    .add(QuerySort.add(AnimeTagField.DISPLAY_ORDER, QuerySortOrder.ASC)));

            mapMessage.put("animetags", animeTags);

            QueryExpress queryExpress = new QueryExpress();
            QueryExpress queryExpress2 = new QueryExpress();
            int curPage = (pageStartIndex / pageSize) + 1;
            Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            queryExpress.add(QueryCriterions.eq(TagDedearchivesFiled.TAGID, tagid));
            queryExpress2.add(QueryCriterions.eq(TagDedearchivesFiled.TAGID, tagid));
            if (!StringUtil.isEmpty(quanZi)) {
                queryExpress.add(QueryCriterions.eq(TagDedearchivesFiled.DISPLAY_TAG, Long.parseLong(quanZi)));
                queryExpress2.add(QueryCriterions.eq(TagDedearchivesFiled.DISPLAY_TAG, Long.parseLong(quanZi)));
                mapMessage.put("quanzi", quanZi);
            }
            if (!StringUtil.isEmpty(nick)) {
                mapMessage.put("nick", nick);
                Profile profile = UserCenterServiceSngl.get().getProfileByNick(nick);
                if (profile == null) {
                    return new ModelAndView("/forigncontent/commentbean/commentbeanlist", mapMessage);
                }
                queryExpress.add(QueryCriterions.eq(TagDedearchivesFiled.PROFILE_ID, profile.getProfileId()));
                queryExpress2.add(QueryCriterions.eq(TagDedearchivesFiled.PROFILE_ID, profile.getProfileId()));
            }
            if (!StringUtil.isEmpty(virtualUser)) {
                queryExpress.add(QueryCriterions.eq(TagDedearchivesFiled.ARCHIVE_CONTENT_TYPE, Integer.parseInt(virtualUser)));
                queryExpress2.add(QueryCriterions.eq(TagDedearchivesFiled.ARCHIVE_CONTENT_TYPE, Integer.parseInt(virtualUser)));
                mapMessage.put("virtualUser", virtualUser);
            }

            if (!StringUtil.isEmpty(startDate)) {
                Date date = sdf.parse(startDate);
                queryExpress.add(QueryCriterions.geq(TagDedearchivesFiled.DEDE_ARCHIVES_PUBDATE, date.getTime()));
                queryExpress2.add(QueryCriterions.geq(TagDedearchivesFiled.DEDE_ARCHIVES_PUBDATE, date.getTime()));
                mapMessage.put("startdate", date);
            }
            if (!StringUtil.isEmpty(endDate)) {
                Date date = sdf.parse(endDate);
                queryExpress.add(QueryCriterions.leq(TagDedearchivesFiled.DEDE_ARCHIVES_PUBDATE, date.getTime()));
                queryExpress2.add(QueryCriterions.leq(TagDedearchivesFiled.DEDE_ARCHIVES_PUBDATE, date.getTime()));
                mapMessage.put("enddate", date);
            }

            queryExpress.add(QuerySort.add(TagDedearchivesFiled.DISPLAY_ORDER, QuerySortOrder.ASC));
            if (!StringUtil.isEmpty(remove_status)) {
                queryExpress.add(QueryCriterions.eq(TagDedearchivesFiled.REMOVE_STATUS, ValidStatus.getByCode(remove_status).getCode()));
                queryExpress2.add(QueryCriterions.eq(TagDedearchivesFiled.REMOVE_STATUS, ValidStatus.getByCode(remove_status).getCode()));
            }

            PageRows<TagDedearchives> pageRows = JoymeAppServiceSngl.get().queryTagDedearchivesByPage(true, tagid, -1, queryExpress, pagination);

            if (!CollectionUtil.isEmpty(pageRows.getRows())) {
                Set<String> commentIds = new HashSet<String>();
                Set<String> profileIds = new HashSet<String>();
                for (TagDedearchives dedearchives : pageRows.getRows()) {
                    commentIds.add(dedearchives.getDede_archives_id());
                    profileIds.add(dedearchives.getProfileId());

                }
                Map<String, CommentBean> commentBeanMap = CommentServiceSngl.get().queryCommentBeanByIds(commentIds);
                Map<String, Profile> profileMap = UserCenterServiceSngl.get().queryProfiles(profileIds);
                mapMessage.put("profileMap", profileMap);
                mapMessage.put("commentBeanMap", commentBeanMap);
            }
            List<Integer> usernum = JoymeAppServiceSngl.get().queryPostNum(queryExpress2);

            mapMessage.put("list", pageRows.getRows());
            mapMessage.put("usernum", CollectionUtil.isEmpty(usernum) ? 0 : usernum.size());
            mapMessage.put("page", pageRows.getPage());
            mapMessage.put("remove_status", remove_status);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return new ModelAndView("/forigncontent/commentbean/commentbeanlist", mapMessage);
    }

    @RequestMapping(value = "/delete")
    public ModelAndView delete(@RequestParam(value = "id", required = false) String id,
                               @RequestParam(value = "remove_status", required = false) String remove_status,
                               @RequestParam(value = "tag_id", required = false) Long tag_id,
                               @RequestParam(value = "archiveId", required = false) String archiveId,
                               @RequestParam(value = "pager.offset", required = false) String pageroffset,
                               HttpServletRequest request
    ) {
        try {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(TagDedearchivesFiled.ID, id));
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(TagDedearchivesFiled.REMOVE_STATUS, ValidStatus.getByCode(remove_status).getCode());
            boolean bval = JoymeAppServiceSngl.get().modifyTagDedearchives(tag_id, archiveId, queryExpress, updateExpress, ArchiveRelationType.TAG_RELATION);
            if (ValidStatus.REMOVED.getCode().equals(remove_status)) {
                CommentBean commentBean = CommentServiceSngl.get().getCommentBean(new QueryExpress().add(QueryCriterions.eq(CommentBeanField.COMMENT_ID, archiveId)));
                if (commentBean != null) {
                    UpdateExpress timeLineUpdateExpress = new UpdateExpress();
                    timeLineUpdateExpress.set(SocialTimeLineItemField.REMOVE_STATUS, ActStatus.ACTED.getCode());
                    QueryExpress timeLineQueryExpress = new QueryExpress();
                    timeLineQueryExpress.add(QueryCriterions.eq(SocialTimeLineItemField.OWN_UNO, commentBean.getUri()))
                            .add(QueryCriterions.eq(SocialTimeLineItemField.CONTENT_ID, commentBean.getCommentId()));

                    TimeLineServiceSngl.get().modifySocialTimeLineItem(SocialTimeLineDomain.MY_MIYOU, commentBean.getUri(), timeLineUpdateExpress, timeLineQueryExpress);
                }
                if (bval) {
                    UpdateExpress updateCommentBean = new UpdateExpress();
                    updateCommentBean.set(CommentBeanField.REMOVE_STATUS, ActStatus.ACTED.getCode());
                    CommentServiceSngl.get().modifyCommentBeanById(archiveId, updateCommentBean);
                    JoymeAppServiceSngl.get().modifyAnimeTag(commentBean.getGroupId(),
                            new QueryExpress().add(QueryCriterions.eq(AnimeTagField.TAG_ID, commentBean.getGroupId())),
                            new UpdateExpress().increase(AnimeTagField.PLAY_NUM, -1l));
                }
            } else if (ValidStatus.VALID.getCode().equals(remove_status)) {
                CommentBean commentBean = CommentServiceSngl.get().getCommentBean(new QueryExpress().add(QueryCriterions.eq(CommentBeanField.COMMENT_ID, archiveId)));
                if (commentBean != null) {
                    UpdateExpress timeLineUpdateExpress = new UpdateExpress();
                    timeLineUpdateExpress.set(SocialTimeLineItemField.REMOVE_STATUS, ActStatus.UNACT.getCode());
                    QueryExpress timeLineQueryExpress = new QueryExpress();
                    timeLineQueryExpress.add(QueryCriterions.eq(SocialTimeLineItemField.OWN_UNO, commentBean.getUri()))
                            .add(QueryCriterions.eq(SocialTimeLineItemField.CONTENT_ID, commentBean.getCommentId()));

                    TimeLineServiceSngl.get().modifySocialTimeLineItem(SocialTimeLineDomain.MY_MIYOU, commentBean.getUri(), timeLineUpdateExpress, timeLineQueryExpress);

                }
                if (bval) {
                    UpdateExpress updateCommentBean = new UpdateExpress();
                    updateCommentBean.set(CommentBeanField.REMOVE_STATUS, ActStatus.UNACT.getCode());
                    CommentServiceSngl.get().modifyCommentBeanById(archiveId, updateCommentBean);
                    JoymeAppServiceSngl.get().modifyAnimeTag(commentBean.getGroupId(),
                            new QueryExpress().add(QueryCriterions.eq(AnimeTagField.TAG_ID, commentBean.getGroupId())),
                            new UpdateExpress().increase(AnimeTagField.PLAY_NUM, 1l));
                }
            }

            writeToolsLog(LogOperType.WANBA_MIYOU_DELETE, "玩霸迷友圈:,tagid:" + tag_id + ",archiveId:" + archiveId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView("redirect:/comment/bean/list?pager.offset=" + pageroffset);
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyPage(@RequestParam(value = "id", required = false) String id,
                                   @RequestParam(value = "pageofferset", required = false) String pageofferset) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(TagDedearchivesFiled.ID, id));
        try {
            TagDedearchives tagDedearchives = JoymeAppServiceSngl.get().getTagDedearchives(queryExpress);
            if (tagDedearchives != null) {
                CommentBean commentBean = CommentServiceSngl.get().getCommentBeanById(tagDedearchives.getDede_archives_id());
                mapMessage.put("commentBean", commentBean);
            }
            mapMessage.put("tagDedearchives", tagDedearchives);


            QueryExpress tagQuery = new QueryExpress();
            tagQuery.add(QuerySort.add(AnimeTagField.DISPLAY_ORDER, QuerySortOrder.DESC));
            //“审核中”不露出 <> -100
            tagQuery.add(QueryCriterions.ne(AnimeTagField.TAG_ID, -100l));
            tagQuery.add(QueryCriterions.eq(AnimeTagField.REMOVE_STATUS, ValidStatus.VALID.getCode()));
            tagQuery.add(QueryCriterions.eq(AnimeTagField.APP_TYPE, AnimeTagAppType.SYHB.getCode()));
            //-1表示出现玩霸2.1.0
            tagQuery.add(QueryCriterions.eq(AnimeTagField.VOLUME, "-1"));
            //全部标签
            List<AnimeTag> animeTagList = JoymeAppServiceSngl.get().queryAnimeTag(tagQuery);
            mapMessage.put("animeTagList", animeTagList);

            QueryExpress commentQuery = new QueryExpress();
            commentQuery.add(QueryCriterions.eq(TagDedearchivesFiled.DEDE_ARCHIVES_ID, tagDedearchives.getDede_archives_id()));
            commentQuery.add(QueryCriterions.ne(TagDedearchivesFiled.TAGID, -2L));
            commentQuery.add(QueryCriterions.ne(TagDedearchivesFiled.REMOVE_STATUS, ActStatus.ACTED.getCode()));
            List<TagDedearchives> tagDedearchiveseList = JoymeAppServiceSngl.get().queryTagDedearchives(commentQuery);
            mapMessage.put("tagDedearchiveses", tagDedearchiveseList);

            if (!CollectionUtil.isEmpty(tagDedearchiveseList)) {
                StringBuffer checkTagId = new StringBuffer();
                for (TagDedearchives dedearchives : tagDedearchiveseList) {
                    if (tagDedearchives.getDede_archives_id().equals(tagDedearchives.getDede_archives_id())) {
                        checkTagId.append(dedearchives.getTagid() + ",");
                    }
                }
                mapMessage.put("checkTagId", checkTagId);
            }

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/comment/bean/list", mapMessage);
        }
        mapMessage.put("pageofferset", pageofferset);
        return new ModelAndView("/forigncontent/commentbean/modifypage", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "id", required = false) String id,
                               @RequestParam(value = "agree_num", required = false) Integer agree_num,
                               @RequestParam(value = "comment_num", required = false) Integer comment_num,
                               @RequestParam(value = "red_num", required = false) Integer red_num,
                               @RequestParam(value = "oneUserSum", required = false) Integer oneUserSum,//-1置顶
                               @RequestParam(value = "desc", required = false) String desc,
                               @RequestParam(value = "commentid", required = false) String dedearchiveid,
                               @RequestParam(value = "checkTagId", required = false, defaultValue = "") String checkTagId,
                               @RequestParam(value = "pageofferset", required = false) String pageroffset,
                               HttpServletRequest request) {
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(TagDedearchivesFiled.ID, id));
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(CommentBeanField.DESCRIPTION, desc);
            updateExpress.set(CommentBeanField.COMMENT_SUM, comment_num);
            updateExpress.set(CommentBeanField.LONG_COMMENT_SUM, red_num);
            updateExpress.set(CommentBeanField.SCORE_COMMENT_SUM, agree_num);
            updateExpress.set(CommentBeanField.ONE_USER_SUM, oneUserSum);
            CommentServiceSngl.get().modifyCommentBeanById(dedearchiveid, updateExpress);


            //更新这个帖子对应的标签
            QueryExpress commentQuery = new QueryExpress();
            commentQuery.add(QueryCriterions.eq(TagDedearchivesFiled.DEDE_ARCHIVES_ID, dedearchiveid));
            commentQuery.add(QueryCriterions.ne(TagDedearchivesFiled.TAGID, -2L));
            List<TagDedearchives> tagDedearchiveseList = JoymeAppServiceSngl.get().queryTagDedearchives(commentQuery);


            //帖子所在标签map
            Map<Long, TagDedearchives> tagDedearchivesMap = new HashMap<Long, TagDedearchives>();
            for (TagDedearchives dedearchives : tagDedearchiveseList) {
                tagDedearchivesMap.put(dedearchives.getTagid(), dedearchives);
            }


            //之前选中的标签
            Map<String, String> checkTagIdMap = new HashMap<String, String>();
            String checkTagIdArr[] = checkTagId.split(",");
            for (String tagid : checkTagIdArr) {
                if (!StringUtil.isEmpty(tagid)) {
                    checkTagIdMap.put(tagid, tagid);
                }
            }


            //
            String tagidArr[] = request.getParameterValues("tag_id");
            if (tagidArr != null && tagidArr.length > 0) {
                for (String tagid : tagidArr) {
                    TagDedearchives tagDedearchives = tagDedearchivesMap.get(Long.valueOf(tagid));
                    if (!checkTagIdMap.containsKey(tagid) && tagDedearchives == null) {
                        long currTimeLong = System.currentTimeMillis();
                        TagDedearchives insertTagDedeArchive = new TagDedearchives();
                        insertTagDedeArchive.setId(MD5Util.Md5(tagid + dedearchiveid));
                        insertTagDedeArchive.setDisplay_order(currTimeLong);
                        insertTagDedeArchive.setDede_archives_id(dedearchiveid);
                        insertTagDedeArchive.setTagid(Long.valueOf(tagid));
                        insertTagDedeArchive.setDede_archives_pubdate(currTimeLong);
                        insertTagDedeArchive.setRemove_status(ValidStatus.INVALID);
                        insertTagDedeArchive.setArchiveContentType(ArchiveContentType.MIYOU_COMMENT);
                        insertTagDedeArchive.setTagDisplyType(TagDisplyType.MIYOU);
                        JoymeAppServiceSngl.get().createTagDedearchives(insertTagDedeArchive);
                    } else {

                        QueryExpress archiveQuey = new QueryExpress();
                        archiveQuey.add(QueryCriterions.eq(TagDedearchivesFiled.ID, tagDedearchives.getId()));
                        UpdateExpress archiveUpdate = new UpdateExpress();
                        archiveUpdate.set(TagDedearchivesFiled.REMOVE_STATUS, ValidStatus.INVALID.getCode());
                        JoymeAppServiceSngl.get().modifyTagDedearchives(tagDedearchives.getTagid(), tagDedearchives.getDede_archives_id(), archiveQuey, archiveUpdate, ArchiveRelationType.TAG_RELATION);
                    }
                    checkTagIdMap.remove(tagid);
                }
            }

            //删除
            for (Map.Entry<String, String> entry : checkTagIdMap.entrySet()) {
                String tagid = entry.getKey();
                TagDedearchives tagDedearchives = tagDedearchivesMap.get(Long.valueOf(tagid));
                QueryExpress archiveQuey = new QueryExpress();
                archiveQuey.add(QueryCriterions.eq(TagDedearchivesFiled.ID, tagDedearchives.getId()));
                UpdateExpress archiveUpdate = new UpdateExpress();
                archiveUpdate.set(TagDedearchivesFiled.REMOVE_STATUS, ValidStatus.REMOVED.getCode());
                JoymeAppServiceSngl.get().modifyTagDedearchives(tagDedearchives.getTagid(), tagDedearchives.getDede_archives_id(), archiveQuey, archiveUpdate, ArchiveRelationType.TAG_RELATION);
            }
//            } else {
//                //删除
//                for (Map.Entry<Long, TagDedearchives> entry : tagDedearchivesMap.entrySet()) {
//                    TagDedearchives tagDedearchives = tagDedearchivesMap.get(entry.getKey());
//                    QueryExpress archiveQuey = new QueryExpress();
//                    archiveQuey.add(QueryCriterions.eq(TagDedearchivesFiled.ID, tagDedearchives.getId()));
//                    UpdateExpress archiveUpdate = new UpdateExpress();
//                    archiveUpdate.set(TagDedearchivesFiled.REMOVE_STATUS, ValidStatus.REMOVED.getCode());
//                    JoymeAppServiceSngl.get().modifyTagDedearchives(tagDedearchives.getTagid(), tagDedearchives.getDede_archives_id(), archiveQuey, archiveUpdate, ArchiveRelationType.TAG_RELATION);
//                }
//            }


            writeToolsLog(LogOperType.WANBA_MIYOU_UPDATE, "玩霸迷友圈:,id:" + id + ",agree_num:" + agree_num + ",comment_num:" + comment_num + ",red_num:" + red_num);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return new ModelAndView("redirect:/comment/bean/list", map);
        }
        return new ModelAndView("redirect:/comment/bean/list?pager.offset=" + pageroffset);
    }


    @RequestMapping(value = "/sort")
    public ModelAndView sort(@RequestParam(value = "id", required = false) String id,
                             @RequestParam(value = "tagid", required = false) String tagid,
                             @RequestParam(value = "dede_archives_id", required = false) String dede_archives_id,
                             @RequestParam(value = "display_order", required = false) String display_order,
                             @RequestParam(value = "remove_status", required = false) String remove_status,
                             @RequestParam(value = "quanzi", required = false) String quanzi
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(TagDedearchivesFiled.ID, id));
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(ClientLineItemField.DISPLAY_ORDER, Integer.valueOf(display_order));
            JoymeAppServiceSngl.get().modifyTagDedearchives(Long.valueOf(tagid), dede_archives_id, queryExpress, updateExpress, ArchiveRelationType.TAG_RELATION);
            writeToolsLog(LogOperType.WANBA_MIYOU_SORT, "玩霸迷友圈:,id:" + id + ",tagid:" + tagid + ",dede_archives_id:" + dede_archives_id + ",display_order:" + display_order);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("redirect:/comment/bean/list", mapMessage);
        }
        return new ModelAndView("redirect:/comment/bean/list?remove_status=" + remove_status + "&quanzi=" + quanzi);
    }

    @RequestMapping(value = "createpage")
    public ModelAndView createPage(@RequestParam(value = "nick", required = false) String nick,
                                   @RequestParam(value = "pic", required = false) String pic,
                                   @RequestParam(value = "text", required = false) String text) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            List<AnimeTag> animeTags = JoymeAppServiceSngl.get().queryAnimeTag(new QueryExpress()
                    .add(QueryCriterions.eq(AnimeTagField.APP_TYPE, AnimeTagAppType.SYHB_QUANZI.getCode()))
                    .add(QueryCriterions.eq(AnimeTagField.REMOVE_STATUS, ValidStatus.VALID.getCode()))
                    .add(QuerySort.add(AnimeTagField.DISPLAY_ORDER, QuerySortOrder.ASC)));

            mapMessage.put("animetags", animeTags);
            String userName = WebappConfig.get().getVirtualUserName();
            if (!StringUtil.isEmpty(userName)) {
                String[] names = userName.split("\\|");
                List<String> nameList = Arrays.asList(names);
                mapMessage.put("names", nameList);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("/forigncontent/commentbean/createpage", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "nick", required = false) String nick,
                               @RequestParam(value = "pic", required = false) String pic,
                               @RequestParam(value = "text", required = false) String text,
                               @RequestParam(value = "groupid", required = false) String groupid) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            Profile profile = UserCenterServiceSngl.get().getProfileByNick(nick);
            if (profile == null) {
                mapMessage.put("errorMsg", "profile.has.notexists");
                mapMessage.put("nick", nick);
                mapMessage.put("pic", pic);
                mapMessage.put("text", text);
                return new ModelAndView("/forigncontent/commentbean/createpage", mapMessage);
            }


            String uniqueKey = CommentUtil.genUniqueByUUID();
            String commentId = CommentUtil.genCommentId(uniqueKey, CommentDomain.GAMECLIENT_MIYOU);
            CommentBean commentBean = new CommentBean();
            commentBean.setDescription(StringUtil.isEmpty(text) ? "" : text);
            commentBean.setUri(profile.getProfileId());
            commentBean.setDomain(CommentDomain.GAMECLIENT_MIYOU);
            commentBean.setUniqueKey(uniqueKey);
            commentBean.setCommentId(commentId);
            commentBean.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
            commentBean.setCreateTime(new Date());
            commentBean.setGroupId(Long.parseLong(groupid));
            if (!StringUtil.isEmpty(pic)) {
                String[] picArray = pic.split("\\@");
                List<String> picList = Arrays.asList(picArray);
                commentBean.setPic(picArray[0]);
                commentBean.setExpandstr(JsonBinder.buildNonDefaultBinder().toJson(picList));
            } else {
                commentBean.setPic("");
                commentBean.setExpandstr("");
            }
            CommentServiceSngl.get().createCommentBean(commentBean);
            commentBean = CommentServiceSngl.get().getCommentBeanById(commentId);
            if (commentBean != null) {
                Long groupIdLong = Long.parseLong(groupid);
                TagDedearchives archive = new TagDedearchives();
                long tagId = AnimeUtil.TAG_ID_MIYOU_ARTICLE;
                archive.setTagid(tagId);
                archive.setArchiveContentType(ArchiveContentType.VIRTUAL_MIYOU_COMMENT);
                archive.setArchiveRelationType(ArchiveRelationType.TAG_RELATION);
                archive.setDede_archives_id(commentBean.getCommentId());
                archive.setId(AnimeUtil.genTagArchiveId(tagId, commentBean.getCommentId()));
                archive.setDede_archives_pubdate(commentBean.getCreateTime().getTime());
                archive.setDisplay_order(Long.valueOf(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000)));
                archive.setProfileId(profile.getProfileId());
                archive.setDisplay_tag(groupIdLong);
                JoymeAppServiceSngl.get().createTagDedearchives(archive);

                JoymeAppServiceSngl.get().modifyAnimeTag(groupIdLong,
                        new QueryExpress().add(QueryCriterions.eq(AnimeTagField.TAG_ID, groupIdLong)),
                        new UpdateExpress().increase(AnimeTagField.PLAY_NUM, 1l));

                SocialTimeLineItem item = new SocialTimeLineItem();
                item.setProfileId(profile.getProfileId());
                item.setDomain(SocialTimeLineDomain.MY_MIYOU);
                item.setDirectId(commentBean.getCommentId());
                item.setDirectProfileId(profile.getProfileId());
                item.setRemoveStatus(ActStatus.UNACT);
                TimeLineServiceSngl.get().insertSocialTimeLineItem(item.getDomain(), item.getProfileId(), item);
            }

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("redirect:/comment/bean/list", mapMessage);
        }
        return new ModelAndView("redirect:/comment/bean/list");

    }


    @RequestMapping(value = "/createreplypage")
    public ModelAndView createReplyPage(@RequestParam(value = "commentid", required = false) String commentId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            CommentBean commentBean = CommentServiceSngl.get().getCommentBeanById(commentId);

            if (commentBean != null) {
                Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(commentBean.getUri());
                mapMessage.put("profile", profile);
            }
            mapMessage.put("comentBean", commentBean);
            if (!StringUtil.isEmpty(commentBean.getExpandstr())) {
                List<String> picList = CommentBean.fromJson(commentBean.getExpandstr());
                mapMessage.put("pics", picList);
            }
            String userName = WebappConfig.get().getVirtualUserName();
            if (!StringUtil.isEmpty(userName)) {
                String[] names = userName.split("\\|");
                List<String> nameList = Arrays.asList(names);
                mapMessage.put("names", nameList);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("redirect:/comment/bean/list", mapMessage);
        }

        return new ModelAndView("/forigncontent/commentbean/createreplypage", mapMessage);
    }

    @RequestMapping(value = "/createreply")
    public ModelAndView createReply(HttpServletRequest request,
                                    @RequestParam(value = "commentid", required = false) String commentId,
                                    @RequestParam(value = "nick", required = false) String nick,
                                    @RequestParam(value = "text", required = false) String text,
                                    @RequestParam(value = "textare", required = false) String textare,
                                    @RequestParam(value = "appkey", defaultValue = "17yfn24TFexGybOF0PqjdY") String appkey) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            Profile profile = UserCenterServiceSngl.get().getProfileByNick(nick);
            AuthApp authApp = OAuthServiceSngl.get().getApp(appkey);

            if (profile == null) {
                mapMessage.put("errorMsg", "profile.has.notexists");
                mapMessage.put("nick", nick);
                mapMessage.put("textare", textare);

                return new ModelAndView("/forigncontent/commentbean/createreplypage", mapMessage);
            }
            ReplyBody replyBody = ReplyBody.parse(postreply(text));
            if (replyBody == null || StringUtil.isEmpty(replyBody.getText())) {
                mapMessage.put("errorMsg", "error.viewline.item.input.wrong.data");
                mapMessage.put("nick", nick);
                mapMessage.put("textare", textare);
                return new ModelAndView("/forigncontent/commentbean/createreplypage", mapMessage);
            }
            CommentBean commentBean = CommentServiceSngl.get().getCommentBeanById(commentId);
            CommentReply reply = new CommentReply();
            reply.setCommentId(commentBean.getCommentId());
            reply.setSubKey(commentBean.getUniqueKey());
            reply.setReplyUno(profile.getUno());
            reply.setReplyProfileId(profile.getProfileId());
            reply.setReplyProfileKey(authApp.getProfileKey());

            reply.setAgreeSum(0);
            reply.setDisagreeSum(0);
            reply.setSubReplySum(0);
            reply.setBody(replyBody);
            reply.setCreateTime(new Date());
            reply.setCreateIp(getIp());
            reply.setRemoveStatus(ActStatus.UNACT);
            reply.setTotalRows(0);

            reply.setDomain(CommentDomain.GAMECLIENT_MIYOU);
            reply = CommentServiceSngl.get().createCommentReply(reply, commentBean.getTotalRows());
            JoymeAppServiceSngl.get().modifyAnimeTag(commentBean.getGroupId(), new QueryExpress().add(QueryCriterions.eq(AnimeTagField.TAG_ID, commentBean.getGroupId())),
                    new UpdateExpress().increase(AnimeTagField.FAVORITE_NUM, 1l));
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("redirect:/comment/bean/list?", mapMessage);
        }


        return new ModelAndView("redirect:/comment/bean/list");
    }

    @RequestMapping(value = "/modifygroup")
    public ModelAndView modifyGroup(@RequestParam(value = "commentid", required = false) String commentId,
                                    @RequestParam(value = "groupid", required = false) String groupId) {
        try {
            CommentServiceSngl.get().modifyCommentBeanById(commentId, new UpdateExpress().set(CommentBeanField.GROUPID, Long.parseLong(groupId)));
            JoymeAppServiceSngl.get().modifyTagDedearchives(-2l, commentId, new QueryExpress().add(QueryCriterions.eq(TagDedearchivesFiled.DEDE_ARCHIVES_ID, commentId)), new UpdateExpress().set(TagDedearchivesFiled.DISPLAY_TAG, Long.parseLong(groupId)), ArchiveRelationType.TAG_RELATION);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return new ModelAndView("redirect:/comment/bean/list");
        }

        return new ModelAndView("redirect:/comment/bean/list");
    }

    private String postreply(String text) {
        ResolveContent resolveContent = new ResolveContent();
        resolveContent.setContent(text);
        TextProcessor textProcessor = TextProcessorFatctory.get().getProcessorByKey(WordProcessorKey.KEY_POST_REPLY);
        if (textProcessor != null) {
            resolveContent = textProcessor.process(resolveContent);
        }
        return resolveContent.getContent();
    }

}
