package com.enjoyf.platform.tools.comment;

import com.enjoyf.platform.crypto.MD5Util;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.comment.CommentHandler;
import com.enjoyf.platform.db.content.ContentHandler;
import com.enjoyf.platform.db.point.PointHandler;
import com.enjoyf.platform.db.stats.StatHandler;
import com.enjoyf.platform.db.usercenter.UserCenterHandler;
import com.enjoyf.platform.db.wikiurl.WikiUrlHandler;
import com.enjoyf.platform.serv.comment.CommentCache;
import com.enjoyf.platform.serv.comment.CommentConfig;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.comment.*;
import com.enjoyf.platform.service.content.*;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterUtil;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.redis.RedisManager;
import com.enjoyf.platform.util.sql.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-7-2
 * Time: 下午5:12
 * To change this template use File | Settings | File Templates.
 */
public class ImportCommentController {

    private static final Logger logger = LoggerFactory.getLogger(ImportCommentController.class);

    private static long CONTENT_ID = 61000l;

    private static CommentConfig config;
    private static ContentHandler contentHandler;
    private static CommentHandler commentHandler;
    private static UserCenterHandler userCenterHandler;
    private static RedisManager manager;
    private static CommentCache commentCache;
    private static WikiUrlHandler wikiUrlHandler;
    private static PointHandler pointHandler;

    private static StatHandler statHandler;

    public static void main(String[] args) {


    }

    private static void updateWikiPageCommentUri() {
        Set<String> wikiSet = new HashSet<String>();
        wikiSet.add("dtcq");
        try {
            int cp = 0;
            Pagination page = null;
            do {
                cp += 1;
                page = new Pagination(1000 * cp, cp, 1000);
                PageRows<CommentBean> pageRows = commentHandler.queryCommentBeanByPage(new QueryExpress().add(QueryCriterions.eq(CommentBeanField.UNI_KEY, "qjnn|368234.shtml")), page);
//                PageRows<CommentBean> pageRows = commentHandler.queryCommentBeanByPage(new QueryExpress().add(QueryCriterions.eq(CommentBeanField.DOMAIN, 1)).add(QuerySort.add(CommentBeanField.CREATE_TIME, QuerySortOrder.DESC)), page);
                if (pageRows == null || CollectionUtil.isEmpty(pageRows.getRows())) {
                    return;
                }
                page = pageRows.getPage();
                for (CommentBean bean : pageRows.getRows()) {
                    String uri = bean.getUri();
                    if (!StringUtil.isEmpty(uri) && uri.startsWith("http://www.joyme.com/")) {
                        String src = uri.replace("http://www.joyme.com/", "");
                        if (!src.startsWith("wiki/") && !src.startsWith("mwiki/") && !src.startsWith("wxwiki/")) {
                            System.out.println(uri);
                            String wikiKey = bean.getUniqueKey().substring(0, bean.getUniqueKey().indexOf("|"));
                            System.out.println(wikiKey);
                            String upUri = "";
                            if (wikiSet.contains(wikiKey)) {
                                upUri = uri.replace("http://www.joyme.com/", "http://www.joyme.com/wiki/");
                            } else {
                                upUri = "http://" + wikiKey + ".joyme.com/" + uri.substring(uri.lastIndexOf("/") + 1);
                            }
                            System.out.println(upUri);
                            System.out.println("---------------------------------------------");
                        }
                    }
                }
            } while (!page.isLastPage());
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private static void addSubKeyCommentReply() {
        try {
            QueryExpress queryExpress = new QueryExpress();
            Pagination page = null;
            int cp = 0;
            do {
                cp += 1;
                page = new Pagination(500 * cp, cp, 500);
                PageRows<CommentReply> pageRows = commentHandler.queryCommentReplyByPage(queryExpress, page);
                if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                    Map<String, CommentBean> map = new HashMap<String, CommentBean>();
                    for (CommentReply reply : pageRows.getRows()) {
                        CommentBean commentBean = map.get(reply.getCommentId());
                        if (commentBean == null) {
                            commentBean = commentHandler.getCommentBean(new QueryExpress().add(QueryCriterions.eq(CommentBeanField.COMMENT_ID, reply.getCommentId())));
                        }
                        if (commentBean != null) {
                            map.put(commentBean.getCommentId(), commentBean);
                            if (commentBean.getUniqueKey().contains("|")) {
                                String subKey = commentBean.getUniqueKey().substring(0, commentBean.getUniqueKey().indexOf("|"));
                                System.out.println("-------------------------" + reply.getReplyId() + ":" + subKey + "------------------------");
                                UpdateExpress updateExpress = new UpdateExpress();
                                updateExpress.set(CommentReplyField.SUBKEY, subKey);
                                commentHandler.modifyCommentReply(new QueryExpress().add(QueryCriterions.eq(CommentReplyField.REPLY_ID, reply.getReplyId())), updateExpress);
                            }
                        }
                    }
                }
            } while (!page.isLastPage());
            System.out.println("-------------------end-----------------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void updateArticleToWWW() {
        try {
            QueryExpress queryExpress = new QueryExpress();
//            queryExpress.add(QueryCriterions.eq(CommentBeanField.COMMENT_ID, "fffa55de3ca85b48efbd8e8036ac7317"));
            queryExpress.add(QueryCriterions.eq(CommentBeanField.DOMAIN, CommentDomain.CMS_COMMENT.getCode()));
            Pagination page = null;
            int cp = 0;
            do {
                cp += 1;
                page = new Pagination(200 * cp, cp, 200);
                PageRows<CommentBean> commentBeanPageRows = commentHandler.queryCommentBeanByPage(queryExpress, page);
                if (commentBeanPageRows != null && !CollectionUtil.isEmpty(commentBeanPageRows.getRows())) {
                    for (CommentBean commentBean : commentBeanPageRows.getRows()) {
                        String uri = commentBean.getUri();
                        System.out.println(uri);
                        if (uri.startsWith("http://article.joyme.com/article/")) {
                            uri = uri.replace("http://article.joyme.com/article/", "http://www.joyme.com/");
                            System.out.println(uri);
                            UpdateExpress updateExpress = new UpdateExpress();
                            updateExpress.set(CommentBeanField.URI, uri);
                            commentHandler.modifyCommentBean(updateExpress, new QueryExpress()
                                    .add(QueryCriterions.eq(CommentBeanField.COMMENT_ID, commentBean.getCommentId())));
                        }
                    }
                }
            } while (!page.isLastPage());
            System.out.println("-------------------end-----------------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void updateCommentBeanSum() {
        try {
//            String commentId = MD5Util.Md5("ms|343426.shtml" + "1");
//            System.out.println(commentId);
//            List<CommentReply> list = commentHandler.queryCommentReply(new QueryExpress()
//                    .add(QueryCriterions.eq(CommentReplyField.COMMENT_ID, commentId))
//                    .add(QueryCriterions.ne(CommentReplyField.REMOVE_STATUS, ActStatus.ACTED.getCode())));
//            UpdateExpress updateExpress = new UpdateExpress().set(CommentBeanField.COMMENT_SUM, list.size());
//            commentHandler.modifyCommentBean(updateExpress, new QueryExpress().add(QueryCriterions.eq(CommentBeanField.COMMENT_ID, commentId)));

            String commentId = MD5Util.Md5("op|【杂谈】是你们，抑制了我们搞基的冲动" + CommentDomain.UGCWIKI_COMMENT.getCode());
            System.out.println(commentId);
            List<CommentReply> list = commentHandler.queryCommentReply(new QueryExpress()
                    .add(QueryCriterions.eq(CommentReplyField.COMMENT_ID, commentId))
                    .add(QueryCriterions.ne(CommentReplyField.REMOVE_STATUS, ActStatus.ACTED.getCode())));
            UpdateExpress updateExpress = new UpdateExpress().set(CommentBeanField.COMMENT_SUM, list.size());
            commentHandler.modifyCommentBean(updateExpress, new QueryExpress().add(QueryCriterions.eq(CommentBeanField.COMMENT_ID, commentId)));

        } catch (DbException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private static void updateCommentBeanURL() {
        int a = 0;
        int b = 0;
        int c = 0;
        int d = 0;
        try {
            int cp = 0;
            Pagination page = null;
            do {
                cp += 1;
                page = new Pagination(100 * cp, cp, 100);
                PageRows<CommentBean> pageRows = commentHandler.queryCommentBeanByPage(new QueryExpress().add(QueryCriterions.eq(CommentBeanField.DOMAIN, 1)), page);
                if (pageRows == null || CollectionUtil.isEmpty(pageRows.getRows())) {
                    return;
                }
                page = pageRows.getPage();
                for (CommentBean bean : pageRows.getRows()) {
                    String uri = bean.getUri();

                    if (uri.contains("/wiki/wiki")) {
                        uri = uri.replaceFirst("/wiki", "");
                        System.out.println(uri);
                        a++;
                        commentHandler.modifyCommentBean(new UpdateExpress().set(CommentBeanField.URI, uri),
                                new QueryExpress().add(QueryCriterions.eq(CommentBeanField.COMMENT_ID, bean.getCommentId())));
                    } else if (uri.contains("/wiki/mwiki")) {
                        // System.out.println(uri);
                        b++;
                    } else if (uri.contains("/mwiki/mwiki")) {
                        // System.out.println(uri);
                        c++;
                    } else if (uri.contains("/mwiki/wiki")) {
                        //  System.out.println(uri);
                        d++;
                    }
//                    if (!StringUtil.isEmpty(uri)) {
//                        if (uri.endsWith("/")) {
//                            System.out.println(uri);
//                            uri = uri.substring(0, uri.lastIndexOf("/"));
//                            System.out.println(uri);
//                            commentHandler.modifyCommentBean(new UpdateExpress().set(CommentBeanField.URI, uri),
//                                    new QueryExpress().add(QueryCriterions.eq(CommentBeanField.COMMENT_ID, bean.getCommentId())));
//                            System.out.println(bean.getCommentId());
//                        }
//                    }
                }
            } while (!page.isLastPage());
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        System.out.println("a=" + a + ",b=" + b + ",c=" + c + ",d=" + d);
    }

    private static void updateCommentByById() {
        try {
            UpdateExpress updateExpress = new UpdateExpress();
            //d37b867df7a090b045273689cbc55408
            updateExpress.set(CommentBeanField.URI, "http://op.joyme.com/wiki/路飞那不为人知的桃色绯闻");
            CommentServiceSngl.get().modifyCommentBeanById("d37b867df7a090b045273689cbc55408", updateExpress);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void updateCommentReplyProfileId() {
        try {
            List<CommentReply> list = commentHandler.queryCommentReply(new QueryExpress().add(QueryCriterions.eq(CommentReplyField.REPLY_PROFILEID, "")));
            for (CommentReply reply : list) {
                String profileId = UserCenterUtil.getProfileId(reply.getReplyUno(), "www");
                System.out.println(reply.getReplyId() + ":" + reply.getReplyUno() + ":" + profileId);
                CommentServiceSngl.get().modifyCommentReplyById(reply.getCommentId(), reply.getReplyId(), new UpdateExpress().set(CommentReplyField.REPLY_PROFILEID, profileId));
            }
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private static void updateCommentSum() {
        try {
            int cp = 0;
            Pagination page = null;
            int count = 1;
            do {
                cp += 1;
                page = new Pagination(500 * cp, cp, 500);
                PageRows<CommentBean> pageRows = commentHandler.queryCommentBeanByPage(new QueryExpress()
//                        .add(QueryCriterions.ne(CommentBeanField.DOMAIN, CommentDomain.CMS_COMMENT.getCode()))
//                        .add(QueryCriterions.eq(CommentBeanField.UNI_KEY, "hlddz|345064.shtml"))
                        .add(QueryCriterions.eq(CommentBeanField.REMOVE_STATUS, ActStatus.UNACT.getCode())), page);
                if (pageRows == null || CollectionUtil.isEmpty(pageRows.getRows())) {
                    continue;
                }
                page = pageRows.getPage();
                for (CommentBean bean : pageRows.getRows()) {
                    System.out.println("---------------" + count + "----------------");
                    System.out.println(bean.getCommentId());
                    System.out.println(bean.getUri());
                    System.out.println(bean.getCommentSum());
                    List<CommentReply> replyList = commentHandler.queryCommentReply(new QueryExpress()
                            .add(QueryCriterions.eq(CommentReplyField.COMMENT_ID, bean.getCommentId()))
                            .add(QueryCriterions.ne(CommentReplyField.REMOVE_STATUS, ActStatus.ACTED.getCode())));
                    int size = 0;
                    if (replyList != null && !CollectionUtil.isEmpty(replyList)) {
                        size = replyList.size();
                    }
                    System.out.println(size);

                    if (size != bean.getCommentSum()) {
                        boolean bool = commentHandler.modifyCommentBean(new UpdateExpress().set(CommentBeanField.COMMENT_SUM, size),
                                new QueryExpress().add(QueryCriterions.eq(CommentBeanField.COMMENT_ID, bean.getCommentId())));
                        System.out.println("------------------" + bool + "------------------");
                        if (bool) {
                            commentCache.removeCommentBean(bean.getCommentId());
                        }
                    }
                    count = count + 1;
                }
            } while (!page.isLastPage());
            System.out.println("-------------------end--------------------");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void updateCommentBean() {
        try {
            int cp = 0;
            Pagination page = null;
            do {
                cp += 1;
                page = new Pagination(100 * cp, cp, 100);
                PageRows<CommentBean> pageRows = commentHandler.queryCommentBeanByPage(new QueryExpress().add(QueryCriterions.eq(CommentBeanField.DOMAIN, 1)), page);
                if (pageRows == null || CollectionUtil.isEmpty(pageRows.getRows())) {
                    return;
                }
                page = pageRows.getPage();
                for (CommentBean bean : pageRows.getRows()) {
                    String uri = bean.getUri();
                    if (!StringUtil.isEmpty(uri)) {
                        if (uri.endsWith("/")) {
                            System.out.println(uri);
                            uri = uri.substring(0, uri.lastIndexOf("/"));
                            System.out.println(uri);
                            commentHandler.modifyCommentBean(new UpdateExpress().set(CommentBeanField.URI, uri),
                                    new QueryExpress().add(QueryCriterions.eq(CommentBeanField.COMMENT_ID, bean.getCommentId())));
                            System.out.println(bean.getCommentId());
                        }
                    }
                }
            } while (!page.isLastPage());
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private static void updateCommentByReply() {
        try {
            List<CommentBean> commentBeans = commentHandler.queryCommentBean(new QueryExpress().add(QueryCriterions.eq(CommentBeanField.DOMAIN, CommentDomain.UGCWIKI_COMMENT.getCode())));
            if (!CollectionUtil.isEmpty(commentBeans)) {
                for (CommentBean bean : commentBeans) {
                    System.out.println(bean.getCommentId());
                    List<CommentReply> replyList = commentHandler.queryCommentReply(new QueryExpress()
                            .add(QueryCriterions.eq(CommentReplyField.COMMENT_ID, bean.getCommentId()))
                            .add(QueryCriterions.eq(CommentReplyField.ROOT_ID, 0l))
                            .add(QueryCriterions.eq(CommentReplyField.REMOVE_STATUS, ActStatus.UNACT.getCode())));
                    int commentSum = replyList.size();
                    System.out.println(commentSum);
                    List<CommentReply> replyList2 = commentHandler.queryCommentReply(new QueryExpress()
                            .add(QueryCriterions.eq(CommentReplyField.COMMENT_ID, bean.getCommentId()))
                            .add(QueryCriterions.eq(CommentReplyField.ROOT_ID, 0l)));
                    int totalRows = replyList2.size();
                    System.out.println(totalRows);
                    UpdateExpress updateExpress = new UpdateExpress();
                    updateExpress.set(CommentBeanField.TOTAL_ROWS, totalRows);
                    updateExpress.set(CommentBeanField.COMMENT_SUM, commentSum);
                    commentHandler.modifyCommentBean(updateExpress, new QueryExpress().add(QueryCriterions.eq(CommentBeanField.COMMENT_ID, bean.getCommentId())));

                }
            }
        } catch (DbException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private static void updateComment() {
        try {
            commentHandler.modifyCommentBean(new UpdateExpress()
                    .set(CommentBeanField.TOTAL_ROWS, 3)
                    .set(CommentBeanField.COMMENT_SUM, 3), new QueryExpress()
                    .add(QueryCriterions.eq(CommentBeanField.COMMENT_ID, "67ca784438fe236862667fe649cb1813")));
        } catch (DbException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private static void importCommentFromContent() {
        int sum = 0;
        try {
            int cpage = 0;
            Pagination page = new Pagination(100, cpage, 100);
            do {
                cpage = cpage + 1;

                QueryExpress queryExpress = new QueryExpress();
                queryExpress.add(QueryCriterions.eq(ForignContentField.CONTENT_DOMAIN, ForignContentDomain.WIKI_CONTENT.getCode()));
                PageRows<ForignContent> contentList = contentHandler.queryForignContent(queryExpress, new Pagination(page.getPageSize() * cpage, cpage, page.getPageSize()));
                page = contentList == null ? page : contentList.getPage();
                if (contentList != null && !CollectionUtil.isEmpty(contentList.getRows())) {
                    for (ForignContent content : contentList.getRows()) {
                        System.out.println("old comment:" + content.getContentId() + "," + content.getContentUrl());
                        String title = content.getContentUrl().substring(content.getContentUrl().lastIndexOf("/") + 1, content.getContentUrl().length());
                        System.out.println(title);
                        String uniKey = content.getKeyWords() + "|" + title;
                        System.out.println(uniKey);
                        String newCommentId = MD5Util.Md5(uniKey + CommentDomain.UGCWIKI_COMMENT.getCode());
                        System.out.println(newCommentId);

                        CommentBean commentBean = commentHandler.getCommentBean(new QueryExpress().add(QueryCriterions.eq(CommentReplyField.COMMENT_ID, newCommentId)));
                        if (commentBean == null) {
                            int floorNum = 0;
                            List<ForignContentReply> replyList = contentHandler.queryForignContentReply(new QueryExpress()
                                    .add(QueryCriterions.eq(ForignContentReplyField.CONTENT_ID, content.getContentId()))
                                    .add(QueryCriterions.eq(ForignContentReplyField.ROOT_ID, 0l))
                                    .add(QueryCriterions.eq(ForignContentReplyField.REMOVE_STATUS, ActStatus.UNACT.getCode())));
                            if (!CollectionUtil.isEmpty(replyList)) {
                                for (ForignContentReply reply : replyList) {
                                    Profile profile = userCenterHandler.getProfile(reply.getReplyUno(), "www");
                                    if (profile != null) {
                                        floorNum += 1;
                                        CommentReply commentReply = new CommentReply();
                                        commentReply.setCommentId(newCommentId);
                                        commentReply.setReplyUno(reply.getReplyUno());

                                        commentReply.setReplyProfileId(profile.getProfileId());
                                        commentReply.setReplyProfileKey(profile.getProfileKey());
                                        commentReply.setParentId(0l);
                                        commentReply.setParentUno("");
                                        commentReply.setParentProfileId("");
                                        commentReply.setParentProfileKey("");
                                        commentReply.setRootId(0l);
                                        commentReply.setRootUno("");
                                        commentReply.setRootProfileId("");
                                        commentReply.setRootProfileKey("");
                                        commentReply.setSubReplySum(0);
                                        commentReply.setAgreeSum(0);
                                        commentReply.setDisagreeSum(0);

                                        ReplyBody body = new ReplyBody();
                                        body.setText(reply.getBody());
                                        body.setPic("");

                                        commentReply.setBody(body);
                                        commentReply.setCreateTime(reply.getCreateTime());
                                        commentReply.setCreateIp(reply.getCreateIp());
                                        commentReply.setFloorNum(floorNum);
                                        commentReply.setTotalRows(0);
                                        commentReply.setScore(0);
                                        commentReply.setDomain(CommentDomain.UGCWIKI_COMMENT);
                                        commentHandler.createCommentReply(commentReply);
                                    }
                                }
                            }
                            commentBean = new CommentBean();
                            commentBean.setCommentId(newCommentId);
                            commentBean.setUniqueKey(uniKey);
                            commentBean.setUri(content.getContentUrl());
                            commentBean.setDomain(CommentDomain.UGCWIKI_COMMENT);
                            commentBean.setTitle(content.getContentTitle());
                            commentBean.setPic("");
                            commentBean.setDescription(content.getContentDesc());
                            commentBean.setCreateTime(new Date());
                            commentBean.setRemoveStatus(ActStatus.UNACT);
                            commentBean.setTotalRows(floorNum);
                            commentBean.setCommentSum(floorNum);
                            commentBean.setLongCommentSum(content.getLong_comment_num());
                            commentBean.setAverageScore(content.getAverage_score());
                            commentBean.setDisplayOrder(content.getDisplay_order());
                            commentBean.setScoreCommentSum(content.getScorereply_num());
                            commentBean.setScoreSum(0);
                            commentBean.setScoreTimes(0);
                            commentBean.setFiveUserSum(0);
                            commentBean.setFourUserSum(0);
                            commentBean.setThreeUserSum(0);
                            commentBean.setTwoUserSum(0);
                            commentBean.setOneUserSum(0);
                            commentHandler.createCommentBean(commentBean);

                        } else {
                            int floorNum = 0;

                            commentHandler.removeCommentReply(newCommentId);

                            List<ForignContentReply> replyList = contentHandler.queryForignContentReply(new QueryExpress()
                                    .add(QueryCriterions.eq(ForignContentReplyField.CONTENT_ID, content.getContentId()))
                                    .add(QueryCriterions.eq(ForignContentReplyField.ROOT_ID, 0l))
                                    .add(QueryCriterions.eq(ForignContentReplyField.REMOVE_STATUS, ActStatus.UNACT.getCode())));
                            if (!CollectionUtil.isEmpty(replyList)) {
                                for (ForignContentReply reply : replyList) {
                                    Profile profile = userCenterHandler.getProfile(reply.getReplyUno(), "www");
                                    if (profile != null) {
                                        floorNum += 1;
                                        CommentReply commentReply = new CommentReply();
                                        commentReply.setCommentId(newCommentId);
                                        commentReply.setReplyUno(reply.getReplyUno());

                                        commentReply.setReplyProfileId(profile.getProfileId());
                                        commentReply.setReplyProfileKey(profile.getProfileKey());
                                        commentReply.setParentId(0l);
                                        commentReply.setParentUno("");
                                        commentReply.setParentProfileId("");
                                        commentReply.setParentProfileKey("");
                                        commentReply.setRootId(0l);
                                        commentReply.setRootUno("");
                                        commentReply.setRootProfileId("");
                                        commentReply.setRootProfileKey("");
                                        commentReply.setSubReplySum(0);
                                        commentReply.setAgreeSum(0);
                                        commentReply.setDisagreeSum(0);

                                        ReplyBody body = new ReplyBody();
                                        body.setText(reply.getBody());
                                        body.setPic("");

                                        commentReply.setBody(body);
                                        commentReply.setCreateTime(reply.getCreateTime());
                                        commentReply.setCreateIp(reply.getCreateIp());
                                        commentReply.setFloorNum(floorNum);
                                        commentReply.setTotalRows(0);
                                        commentReply.setScore(0);
                                        commentReply.setDomain(CommentDomain.UGCWIKI_COMMENT);
                                        commentHandler.createCommentReply(commentReply);
                                    }
                                }
                            }
                            UpdateExpress updateExpress = new UpdateExpress();
                            updateExpress.set(CommentBeanField.TOTAL_ROWS, floorNum);
                            updateExpress.set(CommentBeanField.COMMENT_SUM, floorNum);
                            commentHandler.modifyCommentBean(updateExpress, new QueryExpress().add(QueryCriterions.eq(CommentBeanField.COMMENT_ID, newCommentId)));
                        }
                    }
                }
                sum += contentList.getRows().size();
                System.out.println(sum);
            } while (!page.isLastPage());
            System.out.println(sum);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void importCommentReply() {
        int sum = 0;
        try {
            int cpage = 0;
            Pagination page = new Pagination(100, cpage, 100);
            do {
                cpage = cpage + 1;

                QueryExpress queryExpress = new QueryExpress();
                queryExpress.add(QueryCriterions.eq(ForignContentField.CONTENT_DOMAIN, ForignContentDomain.WIKI_CONTENT.getCode()));
                PageRows<ForignContent> contentList = contentHandler.queryForignContent(queryExpress, new Pagination(page.getPageSize() * cpage, cpage, page.getPageSize()));
                page = contentList == null ? page : contentList.getPage();
                if (contentList != null && !CollectionUtil.isEmpty(contentList.getRows())) {
                    for (ForignContent content : contentList.getRows()) {
//                        System.out.println("content:" + content.getContentId());
//                        String uniKey = content.getKeyWords() + "|" + content.getForignId();
//                        String commentId = MD5Util.Md5(uniKey + CommentDomain.UGCWIKI_COMMENT.getCode());
//                        System.out.println(commentId);
//                        CommentBean commentBean = commentHandler.getCommentBean(new QueryExpress()
//                                .add(QueryCriterions.eq(CommentBeanField.COMMENT_ID, commentId)));
//                        if (commentBean == null) {
//                            Pagination page2 = new Pagination(100, 1, 100);
//                            int floorNum = 1;
//                            int size = 0;
//                            do {
//                                PageRows<ForignContentReply> pageRows2 = contentHandler.queryForignContentReplyByPage(new QueryExpress()
//                                        .add(QueryCriterions.eq(ForignContentReplyField.CONTENT_ID, content.getContentId()))
//                                        .add(QueryCriterions.eq(ForignContentReplyField.ROOT_ID, 0l))
//                                        .add(QueryCriterions.eq(ForignContentReplyField.REMOVE_STATUS, ActStatus.UNACT.getCode())), page2);
//                                page2 = pageRows2 == null ? page2 : pageRows2.getPage();
//                                if (pageRows2 != null && !CollectionUtil.isEmpty(pageRows2.getRows())) {
//                                    for (ForignContentReply reply : pageRows2.getRows()) {
//                                        CommentReply commentReply = new CommentReply();
//                                        commentReply.setObjectId(commentId);
//                                        commentReply.setReplyUno(reply.getReplyUno());
//
//                                        Profile profile = userCenterHandler.getProfile(reply.getReplyUno(), "www");
//                                        if (profile != null) {
//                                            commentReply.setReplyProfileId(profile.getProfileId());
//                                            commentReply.setReplyProfileKey(profile.getProfileKey());
//                                            commentReply.setParentId(0l);
//                                            commentReply.setParentUno("");
//                                            commentReply.setParentProfileId("");
//                                            commentReply.setParentProfileKey("");
//                                            commentReply.setRootId(0l);
//                                            commentReply.setRootUno("");
//                                            commentReply.setRootProfileId("");
//                                            commentReply.setRootProfileKey("");
//                                            commentReply.setSubReplySum(0);
//                                            commentReply.setAgreeSum(0);
//                                            commentReply.setDisagreeSum(0);
//
//                                            ReplyBody body = new ReplyBody();
//                                            body.setText(reply.getBody());
//                                            body.setPic("");
//                                            commentReply.setBody(body);
//                                            commentReply.setCreateTime(reply.getCreateTime());
//                                            commentReply.setCreateIp(reply.getCreateIp());
//                                            commentReply.setFloorNum(floorNum);
//                                            commentReply.setTotalRows(0);
//                                            commentReply.setScore(0);
//                                            commentReply.setDomain(CommentDomain.UGCWIKI_COMMENT);
//                                            floorNum += 1;
//                                        }
//                                    }
//                                }
//                                size += pageRows2.getRows().size();
//                            } while (!page2.isLastPage());
//                            commentBean = new CommentBean();
//                            commentBean.setObjectId(commentId);
//                            commentBean.setUniqueKey(uniKey);
//                            commentBean.setUri(content.getContentUrl());
//                            commentBean.setDomain(CommentDomain.UGCWIKI_COMMENT);
//                            commentBean.setTitle(content.getContentTitle());
//                            commentBean.setPic("");
//                            commentBean.setDescription(content.getContentDesc());
//                            commentBean.setCreateTime(new Date());
//                            commentBean.setRemoveStatus(ActStatus.UNACT);
//                            commentBean.setTotalRows(size);
//                            commentBean.setCommentSum(size);
//                            commentBean.setLongCommentSum(content.getLong_comment_num());
//                            commentBean.setAverageScore(content.getAverage_score());
//                            commentBean.setDisplayOrder(content.getDisplay_order());
//                            commentBean.setScoreCommentSum(content.getScorereply_num());
//                            commentBean.setScoreSum(0);
//                            commentBean.setScoreTimes(0);
//                            commentBean.setFiveUserSum(0);
//                            commentBean.setFourUserSum(0);
//                            commentBean.setThreeUserSum(0);
//                            commentBean.setTwoUserSum(0);
//                            commentBean.setOneUserSum(0);
//                            commentHandler.createCommentBean(commentBean);
//                            System.out.println("insert " + commentId);
//                        } else {

                        System.out.println("old comment:" + content.getContentId() + "," + content.getContentUrl());
                        String title = content.getContentUrl().substring(content.getContentUrl().lastIndexOf("/") + 1, content.getContentUrl().length());
                        System.out.println(title);
                        String uniKey = content.getKeyWords() + "|" + title;
                        System.out.println(uniKey);
                        String newCommentId = MD5Util.Md5(uniKey + CommentDomain.UGCWIKI_COMMENT.getCode());
                        System.out.println(newCommentId);

                        CommentBean commentBean = commentHandler.getCommentBean(new QueryExpress().add(QueryCriterions.eq(CommentReplyField.COMMENT_ID, newCommentId)));
                        if (commentBean == null) {
                            int floorNum = 0;
                            List<ForignContentReply> replyList = contentHandler.queryForignContentReply(new QueryExpress()
                                    .add(QueryCriterions.eq(ForignContentReplyField.CONTENT_ID, content.getContentId()))
                                    .add(QueryCriterions.eq(ForignContentReplyField.ROOT_ID, 0l))
                                    .add(QueryCriterions.eq(ForignContentReplyField.REMOVE_STATUS, ActStatus.UNACT.getCode())));
                            if (!CollectionUtil.isEmpty(replyList)) {
                                for (ForignContentReply reply : replyList) {
                                    Profile profile = userCenterHandler.getProfile(reply.getReplyUno(), "www");
                                    if (profile != null) {
                                        floorNum += 1;
                                        CommentReply commentReply = new CommentReply();
                                        commentReply.setCommentId(newCommentId);
                                        commentReply.setReplyUno(reply.getReplyUno());

                                        commentReply.setReplyProfileId(profile.getProfileId());
                                        commentReply.setReplyProfileKey(profile.getProfileKey());
                                        commentReply.setParentId(0l);
                                        commentReply.setParentUno("");
                                        commentReply.setParentProfileId("");
                                        commentReply.setParentProfileKey("");
                                        commentReply.setRootId(0l);
                                        commentReply.setRootUno("");
                                        commentReply.setRootProfileId("");
                                        commentReply.setRootProfileKey("");
                                        commentReply.setSubReplySum(0);
                                        commentReply.setAgreeSum(0);
                                        commentReply.setDisagreeSum(0);

                                        ReplyBody body = new ReplyBody();
                                        body.setText(reply.getBody());
                                        body.setPic("");

                                        commentReply.setBody(body);
                                        commentReply.setCreateTime(reply.getCreateTime());
                                        commentReply.setCreateIp(reply.getCreateIp());
                                        commentReply.setFloorNum(floorNum);
                                        commentReply.setTotalRows(0);
                                        commentReply.setScore(0);
                                        commentReply.setDomain(CommentDomain.UGCWIKI_COMMENT);
                                        commentHandler.createCommentReply(commentReply);
                                    }
                                }
                            }
                            commentBean = new CommentBean();
                            commentBean.setCommentId(newCommentId);
                            commentBean.setUniqueKey(uniKey);
                            commentBean.setUri(content.getContentUrl());
                            commentBean.setDomain(CommentDomain.UGCWIKI_COMMENT);
                            commentBean.setTitle(content.getContentTitle());
                            commentBean.setPic("");
                            commentBean.setDescription(content.getContentDesc());
                            commentBean.setCreateTime(new Date());
                            commentBean.setRemoveStatus(ActStatus.UNACT);
                            commentBean.setTotalRows(floorNum);
                            commentBean.setCommentSum(floorNum);
                            commentBean.setLongCommentSum(content.getLong_comment_num());
                            commentBean.setAverageScore(content.getAverage_score());
                            commentBean.setDisplayOrder(content.getDisplay_order());
                            commentBean.setScoreCommentSum(content.getScorereply_num());
                            commentBean.setScoreSum(0);
                            commentBean.setScoreTimes(0);
                            commentBean.setFiveUserSum(0);
                            commentBean.setFourUserSum(0);
                            commentBean.setThreeUserSum(0);
                            commentBean.setTwoUserSum(0);
                            commentBean.setOneUserSum(0);
                            commentHandler.createCommentBean(commentBean);

                        } else {
                            int floorNum = 0;

                            commentHandler.removeCommentReply(newCommentId);

//                            List<CommentReply> commentReplyList = commentHandler.queryCommentReply(new QueryExpress().add(QueryCriterions.eq(CommentReplyField.COMMENT_ID, newCommentId))
//                                    .add(QueryCriterions.eq(CommentReplyField.DOMAIN, CommentDomain.UGCWIKI_COMMENT.getCode())));
//                            floorNum = commentReplyList.size();

                            List<ForignContentReply> replyList = contentHandler.queryForignContentReply(new QueryExpress()
                                    .add(QueryCriterions.eq(ForignContentReplyField.CONTENT_ID, content.getContentId()))
                                    .add(QueryCriterions.eq(ForignContentReplyField.ROOT_ID, 0l))
                                    .add(QueryCriterions.eq(ForignContentReplyField.REMOVE_STATUS, ActStatus.UNACT.getCode())));
                            if (!CollectionUtil.isEmpty(replyList)) {
                                for (ForignContentReply reply : replyList) {
                                    Profile profile = userCenterHandler.getProfile(reply.getReplyUno(), "www");
                                    if (profile != null) {
                                        floorNum += 1;
                                        CommentReply commentReply = new CommentReply();
                                        commentReply.setCommentId(newCommentId);
                                        commentReply.setReplyUno(reply.getReplyUno());

                                        commentReply.setReplyProfileId(profile.getProfileId());
                                        commentReply.setReplyProfileKey(profile.getProfileKey());
                                        commentReply.setParentId(0l);
                                        commentReply.setParentUno("");
                                        commentReply.setParentProfileId("");
                                        commentReply.setParentProfileKey("");
                                        commentReply.setRootId(0l);
                                        commentReply.setRootUno("");
                                        commentReply.setRootProfileId("");
                                        commentReply.setRootProfileKey("");
                                        commentReply.setSubReplySum(0);
                                        commentReply.setAgreeSum(0);
                                        commentReply.setDisagreeSum(0);

                                        ReplyBody body = new ReplyBody();
                                        body.setText(reply.getBody());
                                        body.setPic("");

                                        commentReply.setBody(body);
                                        commentReply.setCreateTime(reply.getCreateTime());
                                        commentReply.setCreateIp(reply.getCreateIp());
                                        commentReply.setFloorNum(floorNum);
                                        commentReply.setTotalRows(0);
                                        commentReply.setScore(0);
                                        commentReply.setDomain(CommentDomain.UGCWIKI_COMMENT);
                                        commentHandler.createCommentReply(commentReply);
                                    }
                                }
                            }
                            UpdateExpress updateExpress = new UpdateExpress();
                            updateExpress.set(CommentBeanField.TOTAL_ROWS, floorNum - 1);
                            updateExpress.set(CommentBeanField.COMMENT_SUM, floorNum - 1);
                            commentHandler.modifyCommentBean(updateExpress, new QueryExpress().add(QueryCriterions.eq(CommentBeanField.COMMENT_ID, newCommentId)));
                        }

//                        List<CommentReply> commentReplyList = commentHandler.queryCommentReply(new QueryExpress().add(QueryCriterions.eq(CommentReplyField.COMMENT_ID, newCommentId)));
//
//                        int floorNum = 1;
//                        if (!CollectionUtil.isEmpty(commentReplyList)) {
//                            floorNum = commentReplyList.size() + 1;
//                        }
//
//                        Pagination page2 = new Pagination(100, 1, 100);
//                        do {
//                            PageRows<ForignContentReply> pageRows2 = contentHandler.queryForignContentReplyByPage(new QueryExpress()
//                                    .add(QueryCriterions.eq(ForignContentReplyField.CONTENT_ID, content.getContentId()))
//                                    .add(QueryCriterions.eq(ForignContentReplyField.ROOT_ID, 0l))
//                                    .add(QueryCriterions.eq(ForignContentReplyField.REMOVE_STATUS, ActStatus.UNACT.getCode())), page2);
//                            page2 = pageRows2 == null ? page2 : pageRows2.getPage();
//                            if (pageRows2 != null && !CollectionUtil.isEmpty(pageRows2.getRows())) {
//                                for (ForignContentReply reply : pageRows2.getRows()) {
//                                    Profile profile = userCenterHandler.getProfile(reply.getReplyUno(), "www");
//                                    if (profile != null) {
//                                        CommentReply commentReply = new CommentReply();
//                                        commentReply.setObjectId(newCommentId);
//                                        commentReply.setReplyUno(reply.getReplyUno());
//
//                                        commentReply.setReplyProfileId(profile.getProfileId());
//                                        commentReply.setReplyProfileKey(profile.getProfileKey());
//                                        commentReply.setParentId(0l);
//                                        commentReply.setParentUno("");
//                                        commentReply.setParentProfileId("");
//                                        commentReply.setParentProfileKey("");
//                                        commentReply.setRootId(0l);
//                                        commentReply.setRootUno("");
//                                        commentReply.setRootProfileId("");
//                                        commentReply.setRootProfileKey("");
//                                        commentReply.setSubReplySum(0);
//                                        commentReply.setAgreeSum(0);
//                                        commentReply.setDisagreeSum(0);
//
//                                        ReplyBody body = new ReplyBody();
//                                        body.setText(reply.getBody());
//                                        body.setPic("");
//
//                                        commentReply.setBody(body);
//                                        commentReply.setCreateTime(reply.getCreateTime());
//                                        commentReply.setCreateIp(reply.getCreateIp());
//                                        commentReply.setFloorNum(floorNum);
//                                        commentReply.setTotalRows(0);
//                                        commentReply.setScore(0);
//                                        commentReply.setDomain(CommentDomain.UGCWIKI_COMMENT);
//                                        commentHandler.createCommentReply(commentReply);
//                                    }
//                                    floorNum += 1;
//                                }
//                            }
//                        } while (!page2.isLastPage());
////                        UpdateExpress updateExpress = new UpdateExpress();
////                        updateExpress.set(CommentBeanField.TOTAL_ROWS, floorNum - 1);
////                        updateExpress.set(CommentBeanField.COMMENT_SUM, commentSum);
////                        commentHandler.modifyCommentBean(updateExpress, new QueryExpress().add(QueryCriterions.eq(CommentBeanField.COMMENT_ID, commentId)));
////                        System.out.println("update " + commentId);
                    }
//                    }
                }
                sum += contentList.getRows().size();
                System.out.println(sum);
            } while (!page.isLastPage());
            System.out.println(sum);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void importComment() {
        try {
            do {
                QueryExpress queryExpress = new QueryExpress();
//                queryExpress.add(QueryCriterions.leq(ForignContentField.CONTENT_ID, CONTENT_ID));
//                queryExpress.add(QueryCriterions.geq(ForignContentField.CONTENT_ID, CONTENT_ID - 1000));
                queryExpress.add(QueryCriterions.eq(ForignContentField.CONTENT_DOMAIN, ForignContentDomain.WIKI_CONTENT.getCode()));
                queryExpress.add(QuerySort.add(ForignContentField.CONTENT_ID, QuerySortOrder.DESC));
                List<ForignContent> contentList = contentHandler.queryForignContent(queryExpress);
                if (!CollectionUtil.isEmpty(contentList)) {
                    System.out.println("---------------------------------------" + CONTENT_ID + "-----------------------------------------");
                    for (ForignContent content : contentList) {
                        String url = content.getContentUrl();
                        if (url.indexOf("http://www.joyme.com/wiki/") >= 0) {
                            if (url.indexOf("http://www.joyme.com/wiki/wiki/") >= 0) {
                                String keyWords = url.replaceAll("http://www.joyme.com/wiki/wiki/", "").replaceAll("/" + content.getForignId(), "").trim();
                                UpdateExpress updateExpress = new UpdateExpress();
                                updateExpress.set(ForignContentField.CONTENT_URL, "http://www.joyme.com/wiki/" + url.replaceAll("http://www.joyme.com/wiki/wiki/", "").trim());
                                updateExpress.set(ForignContentField.KEY_WORDS, keyWords);
                                contentHandler.updateForignContent(updateExpress, new QueryExpress().add(QueryCriterions.eq(ForignContentField.CONTENT_ID, content.getContentId())));
                            } else if (url.indexOf("http://www.joyme.com/wiki/mwiki/") >= 0) {
                                String keyWords = url.replaceAll("http://www.joyme.com/wiki/mwiki/", "").replaceAll("/" + content.getForignId(), "").trim();
                                UpdateExpress updateExpress = new UpdateExpress();
                                updateExpress.set(ForignContentField.CONTENT_URL, "http://www.joyme.com/mwiki/" + url.replaceAll("http://www.joyme.com/wiki/mwiki/", "").trim());
                                updateExpress.set(ForignContentField.KEY_WORDS, keyWords);
                                contentHandler.updateForignContent(updateExpress, new QueryExpress().add(QueryCriterions.eq(ForignContentField.CONTENT_ID, content.getContentId())));
                            }
                        } else if (url.indexOf("http://www.joyme.com/mwiki") >= 0) {
                            String keyWords = url.replaceAll("http://www.joyme.com/mwiki/", "").replaceAll("/" + content.getForignId(), "").trim();
                            UpdateExpress updateExpress = new UpdateExpress();
                            updateExpress.set(ForignContentField.KEY_WORDS, keyWords);
                            contentHandler.updateForignContent(updateExpress, new QueryExpress().add(QueryCriterions.eq(ForignContentField.CONTENT_ID, content.getContentId())));
                        } else if (url.endsWith(".shtml") && url.indexOf("http://www.joyme.com/wiki/") < 0 && url.indexOf("http://www.joyme.com/mwiki") < 0) {
                            String upUrl = "";
                            if (url.indexOf("http://www.joyme.com/") >= 0) {
                                upUrl = "http://www.joyme.com/wiki/" + url.replaceAll("http://www.joyme.com/", "").trim();
                                contentHandler.updateForignContent(new UpdateExpress()
                                                .set(ForignContentField.CONTENT_URL, upUrl)
                                                .set(ForignContentField.KEY_WORDS, url.replaceAll("http://www.joyme.com/", "").replaceAll("/" + content.getForignId(), "").trim()),
                                        new QueryExpress().add(QueryCriterions.eq(ForignContentField.CONTENT_ID, content.getContentId())));
                            } else {

                            }
                        }
                        CONTENT_ID = content.getContentId();
                    }
                    System.out.println("---------------------------------------" + CONTENT_ID + "-----------------------------------------");
                }

            } while (CONTENT_ID > 10167l);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

}
