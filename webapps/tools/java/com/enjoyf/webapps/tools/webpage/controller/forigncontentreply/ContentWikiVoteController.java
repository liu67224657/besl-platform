package com.enjoyf.webapps.tools.webpage.controller.forigncontentreply;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.comment.*;
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
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by tonydiao on 2014/12/15.
 */

@Controller
@RequestMapping(value = "/comment/vote/wiki")
public class ContentWikiVoteController extends ToolsBaseController {


    @Resource(name = "i18nSource")
    private ResourceBundleMessageSource i18nSource;

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,      //数据库记录索引
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "20") int pageSize,
                             @RequestParam(value = "searchTitle", required = false) String searchTitle,
                             @RequestParam(value = "searchNick", required = false) String searchNick,
                             @RequestParam(value = "errorMsg", required = false) String errorMsg) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            if (!StringUtil.isEmpty(errorMsg)) {
                mapMessage.put("errorMsg", errorMsg);
            }

            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(CommentBeanField.DOMAIN, CommentDomain.WIKI_VOTE.getCode()));
            if (!StringUtil.isEmpty(searchTitle)) {
                mapMessage.put("searchTitle", searchTitle);
                queryExpress.add(QueryCriterions.like(CommentBeanField.TITLE, "%" + searchTitle + "%"));
            }

            if (!StringUtil.isEmpty(searchNick)) {
                mapMessage.put("searchNick", searchNick);
                Profile profile = UserCenterServiceSngl.get().getProfileByNick(searchNick);
                if (profile != null) {
                    queryExpress.add(QueryCriterions.eq(CommentBeanField.URI, profile.getProfileId()));
                } else {
                    return new ModelAndView("/forigncontent/vote/votelist", mapMessage);
                }
            }

            queryExpress.add(new QuerySort(CommentBeanField.REMOVE_STATUS, QuerySortOrder.ASC));  //按ing--已审核,n--未审核，y---已删除
            queryExpress.add(new QuerySort(CommentBeanField.CREATE_TIME, QuerySortOrder.DESC));     //按添加时间倒序

            //    queryExpress.add(QueryCriterions.ne(CommentReplyField.REMOVE_STATUS, ActStatus.ACTED.getCode()));    //只要不是删除状态的就返回
            int curPage = (pageStartIndex / pageSize) + 1;
            Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);

            PageRows<CommentBean> pageRows = CommentServiceSngl.get().queryCommentBeanByPage(queryExpress, pagination);
            List<CommentBean> list = pageRows.getRows();
            if (pageRows != null && list != null && list.size() > 0) {

                for (int i = 0; i < list.size(); i++) {
                    QueryExpress queryExpressVote = new QueryExpress();
                    queryExpressVote.add(QueryCriterions.eq(CommentVoteOptionField.COMMENT_ID, list.get(i).getCommentId()));
                    queryExpressVote.add(QueryCriterions.eq(CommentVoteOptionField.REMOVE_STATUS, ValidStatus.VALID.getCode()));
                    List<CommentVoteOption> voteList = CommentServiceSngl.get().queryCommentVoteOption(queryExpressVote);
                    if (voteList != null && voteList.size() > 0) {
                        list.get(i).setVoteList(voteList);
                    }
                    if (!StringUtil.isEmpty(list.get(i).getUri())) {
                        Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(list.get(i).getUri());
                        if (profile != null) {
                            list.get(i).setProfile(profile);
                        }
                    }
                }
                mapMessage.put("list", list);
                mapMessage.put("page", pageRows.getPage());
            }

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }


        return new ModelAndView("/forigncontent/vote/votelist", mapMessage);
    }


    @RequestMapping(value = "/createpage")
    public ModelAndView createPage() {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        return new ModelAndView("/forigncontent/vote/voteadd", mapMessage);
    }


    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "uri", required = false) String uri,
                               @RequestParam(value = "title", required = false) String title,
                               @RequestParam(value = "description", required = false) String description,
                               @RequestParam(value = "removeStatus", required = false) String removeStatus,
                               HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            CommentBean commentBean = new CommentBean();
            if (!StringUtil.isEmpty(uri)) {
                Profile profile = UserCenterServiceSngl.get().getProfileByNick(uri);
                if (profile != null) {
                    commentBean.setUri(profile.getProfileId());
                }
            }

            if (StringUtil.isEmpty(uri) || StringUtil.isEmpty(commentBean.getUri())) {
                mapMessage.put("title", title);
                mapMessage.put("uri", uri);
                mapMessage.put("description", description);
                mapMessage.put("removeStatus", removeStatus);
                mapMessage = putErrorMessage(mapMessage, "创建者呢称在数据库中不存在");
                return new ModelAndView("/forigncontent/vote/voteadd", mapMessage);
            }

            commentBean.setUniqueKey(UUID.randomUUID().toString());   //UUID 结果
            commentBean.setDomain(CommentDomain.WIKI_VOTE);
            commentBean.setTitle(title);
            commentBean.setDescription(description);
            commentBean.setRemoveStatus(ActStatus.getByCode(removeStatus));
            CommentServiceSngl.get().createCommentBean(commentBean);


            ToolsLog log = new ToolsLog();
            log.setOpUserId(getCurrentUser().getUserid());//用户的ID
            log.setOperType(LogOperType.WIKI_VOTE_CREATE);  //操作的类型
            log.setOpTime(new Date());//操作时间
            log.setOpIp(getIp());//用户IP
            Map<String, String[]> params = request.getParameterMap();
            String queryString = "  ";
            for (String key : params.keySet()) {
                String[] values = params.get(key);
                for (int i = 0; i < values.length; i++) {
                    queryString += key + "=" + values[i] + "&";
                }
            }
            // 去掉最后一个空格
            queryString = queryString.substring(0, queryString.length() - 1);

            if (queryString.length() > 1950) {
                queryString = queryString.substring(0, 1950);
            }
            log.setOpAfter("wiki投票create方法,queryString" + queryString); //描述 推荐用中文
            addLog(log);

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("forward:/comment/vote/wiki/list", mapMessage);
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyPage(@RequestParam(value = "commentId", required = true) String commentId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            mapMessage.put("commentId", commentId);
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(CommentBeanField.COMMENT_ID, commentId));
            queryExpress.add(QueryCriterions.eq(CommentBeanField.DOMAIN, CommentDomain.WIKI_VOTE.getCode()));

            CommentBean commentBean = CommentServiceSngl.get().getCommentBean(queryExpress);
            if (!StringUtil.isEmpty(commentBean.getUri())) {
                Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(commentBean.getUri());
                if (profile != null) {
                    commentBean.setUri(profile.getNick());
                }
            }
            mapMessage.put("item", commentBean);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/forigncontent/vote/voteedit", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "commentId", required = false) String commentId,
                               @RequestParam(value = "unikey", required = false) String unikey,
                               @RequestParam(value = "uri", required = false) String uri,
                               @RequestParam(value = "title", required = false) String title,
                               @RequestParam(value = "description", required = false) String description,
                               @RequestParam(value = "removeStatus", required = false) String removeStatus,
                               HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            String profileid = "";
            if (!StringUtil.isEmpty(uri)) {
                Profile profile = UserCenterServiceSngl.get().getProfileByNick(uri);
                if (profile != null) {
                    profileid = profile.getProfileId();
                }
            }

            if (StringUtil.isEmpty(uri) || StringUtil.isEmpty(profileid)) {
                mapMessage.put("commentId", commentId);
                CommentBean commentBean = new CommentBean();
                commentBean.setCommentId(commentId);
                commentBean.setTitle(title);
                commentBean.setUri(uri);
                commentBean.setUniqueKey(unikey);
                commentBean.setDescription(description);
                commentBean.setRemoveStatus(ActStatus.getByCode(removeStatus));

                mapMessage.put("item", commentBean);
                mapMessage = putErrorMessage(mapMessage, "创建者呢称在数据库中不存在");
                return new ModelAndView("/forigncontent/vote/voteedit", mapMessage);
            }

            UpdateExpress updateExpress = new UpdateExpress();

            updateExpress.set(CommentBeanField.URI, profileid);
            updateExpress.set(CommentBeanField.TITLE, title);
            updateExpress.set(CommentBeanField.DESCRIPTION, description);
            updateExpress.set(CommentBeanField.REMOVE_STATUS, removeStatus);
            CommentServiceSngl.get().modifyCommentBeanById(commentId, updateExpress);

            ToolsLog log = new ToolsLog();
            log.setOpUserId(getCurrentUser().getUserid());//用户的ID
            log.setOperType(LogOperType.WIKI_VOTE_MODIFY);  //操作的类型
            log.setOpTime(new Date());//操作时间
            log.setOpIp(getIp());//用户IP
            Map<String, String[]> params = request.getParameterMap();
            String queryString = "  ";
            for (String key : params.keySet()) {
                String[] values = params.get(key);
                for (int i = 0; i < values.length; i++) {
                    queryString += key + "=" + values[i] + "&";
                }
            }
            // 去掉最后一个空格
            queryString = queryString.substring(0, queryString.length() - 1);

            if (queryString.length() > 1950) {
                queryString = queryString.substring(0, 1950);
            }
            log.setOpAfter("wiki投票modify方法,queryString" + queryString); //描述 推荐用中文
            addLog(log);

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("forward:/comment/vote/wiki/list", mapMessage);
    }


    @RequestMapping(value = "/delete")
    public ModelAndView delete(@RequestParam(value = "commentId", required = true) String commentId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(CommentBeanField.REMOVE_STATUS, ActStatus.ACTED.getCode());
            boolean result = CommentServiceSngl.get().modifyCommentBeanById(commentId, updateExpress);
            if (!result) {
                mapMessage.put("errorMsg", "删除失败!");
            }

            ToolsLog log = new ToolsLog();
            log.setOpUserId(getCurrentUser().getUserid());//用户的ID
            log.setOperType(LogOperType.WIKI_VOTE_DELETE);  //操作的类型
            log.setOpTime(new Date());//操作时间
            log.setOpIp(getIp());//用户IP
            log.setOpAfter("wiki投票delete方法,commentId" + commentId); //描述 推荐用中文
            addLog(log);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("forward:/comment/vote/wiki/list", mapMessage);
    }


    @RequestMapping(value = "/option/createpage")
    public ModelAndView optionCreatePage(@RequestParam(value = "commentId", required = true) String commentId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("commentId", commentId);
        return new ModelAndView("/forigncontent/vote/voteoptionadd", mapMessage);
    }

    @RequestMapping(value = "/option/create")
    public ModelAndView optionCreate(@RequestParam(value = "commentId", required = true) String commentId,
                                     @RequestParam(value = "title", required = false) String title,
                                     @RequestParam(value = "pic", required = false) String pic,
                                     @RequestParam(value = "displayOrder", required = false) String displayOrder,
                                     @RequestParam(value = "removeStatus", required = false) String removeStatus,
                                     HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("commentId", commentId);

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Long result = new Date().getTime() - sdf.parse("2010-01-01 00:00:00").getTime();
            result = result / 1000;
            int autoDisplayOrder = Integer.MAX_VALUE - result.intValue();     //0到1989158807 之间的一个数

            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(CommentVoteOptionField.COMMENT_ID, commentId));
            queryExpress.add(QueryCriterions.eq(CommentVoteOptionField.TITLE, title));
            List<CommentVoteOption> list = CommentServiceSngl.get().queryCommentVoteOption(queryExpress);
            if (list.size() > 0) {
                mapMessage = putErrorMessage(mapMessage, "以该标题命名的投票选项已经存在！请更改名称后重试");
                mapMessage.put("title", title);
                mapMessage.put("pic", pic);
                mapMessage.put("displayOrder", displayOrder);
                //   mapMessage.put("removeStatus", removeStatus);
                return new ModelAndView("/forigncontent/vote/voteoptionadd", mapMessage);
            }

            CommentVoteOption commentVoteOption = new CommentVoteOption();
            commentVoteOption.setVoteOptionId(UUID.randomUUID().toString());
            commentVoteOption.setCommentId(commentId);
            commentVoteOption.setDisplayOrder(Long.valueOf(autoDisplayOrder));
            commentVoteOption.setCreateTime(new Date());
            //新增投票选项的status都是valid
            commentVoteOption.setRemoveStatus(ValidStatus.VALID.getCode());
            commentVoteOption.setTitle(title);
            commentVoteOption.setCreateUser(getCurrentUser().getUserid());
            if (!StringUtil.isEmpty(pic)) {
                commentVoteOption.setPic(pic);
            }
            CommentServiceSngl.get().createCommentVoteOption(commentVoteOption);

            ToolsLog log = new ToolsLog();
            log.setOpUserId(getCurrentUser().getUserid());//用户的ID
            log.setOperType(LogOperType.WIKI_VOTE_OPTION_CREATE);  //操作的类型
            log.setOpTime(new Date());//操作时间
            log.setOpIp(getIp());//用户IP
            Map<String, String[]> params = request.getParameterMap();
            String queryString = "  ";
            for (String key : params.keySet()) {
                String[] values = params.get(key);
                for (int i = 0; i < values.length; i++) {
                    queryString += key + "=" + values[i] + "&";
                }
            }
            // 去掉最后一个空格
            queryString = queryString.substring(0, queryString.length() - 1);

            if (queryString.length() > 1950) {
                queryString = queryString.substring(0, 1950);
            }
            log.setOpAfter("wiki投票option create方法,queryString" + queryString); //描述 推荐用中文
            addLog(log);

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/comment/vote/wiki/modifypage", mapMessage);
    }


    //交换在两个投票选项在投票中的排序
    @RequestMapping(value = "/option/swap")
    @ResponseBody
    public String swap(@RequestParam(value = "voteOptionIdFirst", required = true) String voteOptionIdFirst,
                       @RequestParam(value = "displayOrderFirst", required = true) Integer displayOrderFirst,
                       @RequestParam(value = "voteOptionIdSecond", required = true) String voteOptionIdSecond,
                       @RequestParam(value = "displayOrderSecond", required = true) Integer displayOrderSecond) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        JsonBinder binder = JsonBinder.buildNormalBinder();
        try {
            UpdateExpress updateExpressA = new UpdateExpress();
            updateExpressA.set(CommentVoteOptionField.DISPLAY_ORDER, Long.valueOf(displayOrderSecond));
            QueryExpress queryExpressA = new QueryExpress();
            queryExpressA.add(QueryCriterions.eq(CommentVoteOptionField.VOTE_OPTION_ID, voteOptionIdFirst));

            boolean boolA = CommentServiceSngl.get().modifyCommentVoteOption(updateExpressA, queryExpressA);

            UpdateExpress updateExpressB = new UpdateExpress();
            updateExpressB.set(CommentVoteOptionField.DISPLAY_ORDER, Long.valueOf(displayOrderFirst));
            QueryExpress queryExpressB = new QueryExpress();
            queryExpressB.add(QueryCriterions.eq(CommentVoteOptionField.VOTE_OPTION_ID, voteOptionIdSecond));

            boolean boolB = CommentServiceSngl.get().modifyCommentVoteOption(updateExpressB, queryExpressB);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return binder.toJson(mapMessage);
    }


    @RequestMapping(value = "/option/modifypage")
    public ModelAndView optionModifyPage(@RequestParam(value = "voteOptionId", required = true) String voteOptionId,
                                         @RequestParam(value = "commentId", required = true) String commentId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            mapMessage.put("voteOptionId", voteOptionId);
            mapMessage.put("commentId", commentId);
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(CommentVoteOptionField.VOTE_OPTION_ID, voteOptionId));
            CommentVoteOption commentVoteOption = CommentServiceSngl.get().getCommentVoteOption(queryExpress);
            mapMessage.put("item", commentVoteOption);

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("/forigncontent/vote/voteoptionedit", mapMessage);
    }

    @RequestMapping(value = "/option/modify")
    public ModelAndView optionModify(@RequestParam(value = "voteOptionId", required = true) String voteOptionId,
                                     @RequestParam(value = "commentId", required = true) String commentId,
                                     @RequestParam(value = "title", required = false) String title,
                                     @RequestParam(value = "pic", required = false) String pic,
                                     @RequestParam(value = "displayOrder", required = false) String displayOrder,
                                     @RequestParam(value = "removeStatus", required = false) String removeStatus,
                                     HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            mapMessage.put("commentId", commentId);

            QueryExpress queryExpressRepeat = new QueryExpress();
            queryExpressRepeat.add(QueryCriterions.eq(CommentVoteOptionField.COMMENT_ID, commentId));
            queryExpressRepeat.add(QueryCriterions.eq(CommentVoteOptionField.TITLE, title));
            queryExpressRepeat.add(QueryCriterions.ne(CommentVoteOptionField.VOTE_OPTION_ID, voteOptionId));

            List<CommentVoteOption> list = CommentServiceSngl.get().queryCommentVoteOption(queryExpressRepeat);
            if (list.size() > 0) {

                mapMessage = putErrorMessage(mapMessage, "以该标题命名的投票选项已经存在！请更改名称后重试");
                mapMessage.put("voteOptionId", voteOptionId);
                CommentVoteOption commentVoteOption = new CommentVoteOption();
                commentVoteOption.setPic(pic);
                commentVoteOption.setTitle(title);
                commentVoteOption.setDisplayOrder(Long.valueOf(displayOrder));
                commentVoteOption.setRemoveStatus(removeStatus);
                commentVoteOption.setCommentId(commentId);
                commentVoteOption.setVoteOptionId(voteOptionId);


                mapMessage.put("item", commentVoteOption);
                return new ModelAndView("/forigncontent/vote/voteoptionedit", mapMessage);

            }

            QueryExpress queryExpressUpdate = new QueryExpress();
            queryExpressUpdate.add(QueryCriterions.eq(CommentVoteOptionField.COMMENT_ID, commentId));
            queryExpressUpdate.add(QueryCriterions.eq(CommentVoteOptionField.VOTE_OPTION_ID, voteOptionId));
            CommentVoteOption commentVoteOptionPre = CommentServiceSngl.get().getCommentVoteOption(queryExpressUpdate);

            if (!commentVoteOptionPre.getRemoveStatus().equals(removeStatus)) {
                //如果更新前后的remove_status发生了变化

                //如果删除前是valid并且optionTotal的值大于0，那么不允许更改状态
                //如果删除前是valid并且optionTotal的值等于0，那么允许更改状态，这时不会影响commentbean的totalRows的值
                if (commentVoteOptionPre.getRemoveStatus().equals("valid")) {

                    if (commentVoteOptionPre.getOptionTotal() > 0) {

                        mapMessage = putErrorMessage(mapMessage, "该投票选项已经有人投票，无法把审核状态从'已审核'改成其他状态！");
                        mapMessage.put("voteOptionId", voteOptionId);
                        CommentVoteOption commentVoteOption = new CommentVoteOption();
                        commentVoteOption.setPic(pic);
                        commentVoteOption.setTitle(title);
                        commentVoteOption.setDisplayOrder(Long.valueOf(displayOrder));
                        commentVoteOption.setRemoveStatus(removeStatus);
                        commentVoteOption.setCommentId(commentId);
                        commentVoteOption.setVoteOptionId(voteOptionId);

                        mapMessage.put("item", commentVoteOption);
                        return new ModelAndView("/forigncontent/vote/voteoptionedit", mapMessage);
                    }
                }
                //如果改变后的状态是valid 那么要检查optionTotal的值，如果大于0，不允许这样的操作    ，除非手动更改数据库，要不不会出现这样的情况
                if (removeStatus.equals("valid")) {

                    if (commentVoteOptionPre.getOptionTotal() > 0) {

                        mapMessage = putErrorMessage(mapMessage, "该投票选项已经有人投票，无法更改审核状态为'已审核'！");
                        mapMessage.put("voteOptionId", voteOptionId);

                        CommentVoteOption commentVoteOption = new CommentVoteOption();
                        commentVoteOption.setPic(pic);
                        commentVoteOption.setTitle(title);
                        commentVoteOption.setDisplayOrder(Long.valueOf(displayOrder));
                        commentVoteOption.setRemoveStatus(removeStatus);
                        commentVoteOption.setCommentId(commentId);
                        commentVoteOption.setVoteOptionId(voteOptionId);

                        mapMessage.put("item", commentVoteOption);
                        return new ModelAndView("/forigncontent/vote/voteoptionedit", mapMessage);
                    }
                }
            }
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(CommentVoteOptionField.REMOVE_STATUS, removeStatus);
            updateExpress.set(CommentVoteOptionField.DISPLAY_ORDER, Long.valueOf(displayOrder));
            updateExpress.set(CommentVoteOptionField.TITLE, title);
            updateExpress.set(CommentVoteOptionField.PIC, pic);
            updateExpress.set(CommentVoteOptionField.CREATE_USER, getCurrentUser().getUserid());
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(CommentVoteOptionField.VOTE_OPTION_ID, voteOptionId));

            boolean result = CommentServiceSngl.get().modifyCommentVoteOption(updateExpress, queryExpress);
            if (!result) {
                mapMessage.put("errorMsg", "更新失败!");
            }


            ToolsLog log = new ToolsLog();
            log.setOpUserId(getCurrentUser().getUserid());//用户的ID
            log.setOperType(LogOperType.WIKI_VOTE_OPTION_MODIFY);  //操作的类型
            log.setOpTime(new Date());//操作时间
            log.setOpIp(getIp());//用户IP
            Map<String, String[]> params = request.getParameterMap();
            String queryString = "  ";
            for (String key : params.keySet()) {
                String[] values = params.get(key);
                for (int i = 0; i < values.length; i++) {
                    queryString += key + "=" + values[i] + "&";
                }
            }
            // 去掉最后一个空格
            queryString = queryString.substring(0, queryString.length() - 1);

            if (queryString.length() > 1950) {
                queryString = queryString.substring(0, 1950);
            }
            log.setOpAfter("wiki投票option modify方法,queryString" + queryString); //描述 推荐用中文
            addLog(log);

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/comment/vote/wiki/modifypage", mapMessage);
    }

    @RequestMapping(value = "/option/delete")
    public ModelAndView optionDelete(@RequestParam(value = "voteOptionId", required = true) String voteOptionId,
                                     @RequestParam(value = "commentId", required = true) String commentId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("commentId", commentId);
        try {

            //如果待删除前其removeStatus 是valid,并且optionTotal字段的值大于0，那么不允许删除
            QueryExpress queryExpressDelete = new QueryExpress();
            queryExpressDelete.add(QueryCriterions.eq(CommentVoteOptionField.COMMENT_ID, commentId));
            queryExpressDelete.add(QueryCriterions.eq(CommentVoteOptionField.VOTE_OPTION_ID, voteOptionId));
            queryExpressDelete.add(QueryCriterions.eq(CommentVoteOptionField.REMOVE_STATUS, ValidStatus.VALID.getCode()));
            CommentVoteOption commentVoteOptionTemp = CommentServiceSngl.get().getCommentVoteOption(queryExpressDelete);
            if (commentVoteOptionTemp != null && commentVoteOptionTemp.getOptionTotal() > 0) {
                mapMessage.put("errorMsg", "该选项已经有人投票，无法删除");
                return new ModelAndView("forward:/comment/vote/wiki/modifypage", mapMessage);

/*

                //获得原来的total_rows的值
                QueryExpress queryExpress = new QueryExpress();
                queryExpress.add(QueryCriterions.eq(CommentBeanField.COMMENT_ID, commentId));
                queryExpress.add(QueryCriterions.eq(CommentBeanField.DOMAIN, CommentDomain.WIKI_VOTE.getCode()));

                CommentBean commentBean = CommentServiceSngl.get().getCommentBean(queryExpress);



                if(commentBean.getTotalRows()-new Long(commentVoteOptionTemp.getOptionTotal()).intValue()<0) {
                    mapMessage.put("errorMsg", "总得票数小于分项得票数，程序内部数据不一致，无法删除");
                    return new ModelAndView("forward:/comment/vote/wiki/modifypage", mapMessage);
                }
*/



/*
                //执行减法操作
                UpdateExpress updateExpressTemp = new UpdateExpress();

                updateExpressTemp.set(CommentBeanField.TOTAL_ROWS,commentBean.getTotalRows()- new Long(commentVoteOptionTemp.getOptionTotal()).intValue());


                CommentServiceSngl.get().modifyCommentBeanById(commentId, updateExpressTemp);
*/

            }

            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(CommentVoteOptionField.REMOVE_STATUS, ValidStatus.REMOVED.getCode());

            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(CommentVoteOptionField.VOTE_OPTION_ID, voteOptionId));

            boolean result = CommentServiceSngl.get().modifyCommentVoteOption(updateExpress, queryExpress);
            if (!result) {
                mapMessage.put("errorMsg", "删除失败!");
            }

            ToolsLog log = new ToolsLog();
            log.setOpUserId(getCurrentUser().getUserid());//用户的ID
            log.setOperType(LogOperType.WIKI_VOTE_OPTION_DELETE);  //操作的类型
            log.setOpTime(new Date());//操作时间
            log.setOpIp(getIp());//用户IP

            log.setOpAfter("wiki投票option delete方法,voteOptionId:" + voteOptionId+";commentId"+commentId); //描述 推荐用中文
            addLog(log);


        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/comment/vote/wiki/modifypage", mapMessage);
    }
}
