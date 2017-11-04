package com.enjoyf.platform.tools.comment;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.comment.CommentHandler;
import com.enjoyf.platform.serv.comment.CommentCache;
import com.enjoyf.platform.serv.comment.CommentConfig;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.comment.*;
import com.enjoyf.platform.util.*;
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
public class ImportWikiDiscussCommentController {

    private static final Logger logger = LoggerFactory.getLogger(ImportWikiDiscussCommentController.class);

    private static final Set<String> uniKeySet = new HashSet<String>();

    static {
//        uniKeySet.add("gundam|哈哈哈");


//        uniKeySet.add("gundam|沙发我来抢一个哈");
//        uniKeySet.add("gundam|高达WIKI讨论区规则");
//        uniKeySet.add("gundam|关于GK件的一些科普”");
//        uniKeySet.add("gundam|高达WIKI编辑教程");
//        uniKeySet.add("gundam|高达WIKI模魂真悟专区上线！");
//        uniKeySet.add("gundam|有人喜欢BB战士（SD高达）吗，yooo");
//        uniKeySet.add("gundam|新手想入坑高达！");
//        uniKeySet.add("gundam|《SD敢达OL2》客户端开放下载");
//        uniKeySet.add("gundam|123123");
//        uniKeySet.add("gundam|【解惑】高达跟敢达的区别，你造吗？");
//        uniKeySet.add("gundam|转帖：浅谈吉恩系MS独眼系统");
//        uniKeySet.add("gundam|MG百式ver．２．０店家偷跑图！买不买");
//        uniKeySet.add("gundam|沙发已被抢，这年头真竞争压力可真大啊");
//        uniKeySet.add("gundam|喜欢高达的人跟喜欢EXO的人有什么区别？");
//        uniKeySet.add("gundam|SEED党和UC党的战争是时候该停止了！");
//        uniKeySet.add("gundam|祖国版和万代的模型区别大么？");
//        uniKeySet.add("gundam|高达这种人形兵器的泛用性");
//        uniKeySet.add("gundam|我们");
//        uniKeySet.add("gundam|MC大白鹅评测（官方供图）");
//        uniKeySet.add("gundam|2014年最有价值的高达模型");
//        uniKeySet.add("gundam|高达精选演讲稿-基连•扎比篇");
//        uniKeySet.add("gundam|高达精选演讲稿-雷比尔将军篇");
//        uniKeySet.add("gundam|嗯，据说百式二点零会缩胶");
//        uniKeySet.add("gundam|机动战士高达0080：锅灶旁的战争");
//        uniKeySet.add("gundam|教你如何一个小时成为别人眼中的高达迷！");
//        uniKeySet.add("gundam|当有人问你：高达这么多部都讲的啥？怎么办");
//        uniKeySet.add("gundam|我一定要誓死保护我的钢普拉！求祝福");
//        uniKeySet.add("gundam|我男朋友很喜欢高达，但是我有一个问题");
//        uniKeySet.add("gundam|EVA和高达处在同一战场会怎么样");
//        uniKeySet.add("gundam|突然翻出了以前做的模型");
//        uniKeySet.add("gundam|RMG-79秘密档案，附下载");
//        uniKeySet.add("gundam|是男人就开扎古，这句话出自哪里？");
//        uniKeySet.add("gundam|高达动画顺序");
//        uniKeySet.add("gundam|来晒一下办公桌把！");
//        uniKeySet.add("gundam|高达动画的创作背景你了了解多少？");
//        uniKeySet.add("gundam|高达里有哪些好音乐啊，平时跑步的时候听的");
//        uniKeySet.add("gundam|高达沉迷度测试");
//        uniKeySet.add("gundam|给大家介绍一些装X神器");
//        uniKeySet.add("gundam|【资源下载】08MS小队重制版资源");
//        uniKeySet.add("gundam|【资源下载】高达MS大全集2013资源");
//        uniKeySet.add("gundam|【剧情】关于高达origin剧情的漏洞");
//        uniKeySet.add("gundam|【BT下载】高达OO两季DVD高清打包");

    }

    private static CommentConfig config;
    private static CommentHandler commentHandler;
    private static CommentCache commentCache;

    public static void main(String[] args) {
        FiveProps servProps = Props.instance().getServProps();
        try {
            commentHandler = new CommentHandler("comment", servProps);
            config = new CommentConfig(servProps);
            commentCache = new CommentCache(config.getMemCachedConfig());
        } catch (DbException e) {
            System.exit(0);
            logger.error("update pointHandler error.");
        }

        updateComment();
//        importCommentReply();
    }

    private static void updateComment() {
        try {
            CommentBean commentBean = commentHandler.getCommentBean(new QueryExpress().add(QueryCriterions.eq(CommentBeanField.GROUPID, 134777l)).add(QueryCriterions.like(CommentBeanField.DESCRIPTION, "%《三剑豪2》、《兽血沸腾2》%")));
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(CommentBeanField.DESCRIPTION, commentBean.getDescription().replace("《三剑豪2》、《兽血沸腾2》", "《兽血沸腾》"));
            commentHandler.modifyCommentBean(updateExpress, new QueryExpress().add(QueryCriterions.eq(CommentBeanField.COMMENT_ID, commentBean.getCommentId())));
            commentCache.removeCommentBean(commentBean.getCommentId());
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private static void importCommentReply() {
        try {
            for (String uniKey : uniKeySet) {
                System.out.println("----------------------------------begin---------------------------------");
                CommentBean oldBean = commentHandler.getCommentBean(new QueryExpress().add(QueryCriterions.eq(CommentBeanField.UNI_KEY, uniKey)));
                if (oldBean != null) {
                    CommentDomain domain = oldBean.getDomain();
                    String oldCommentId = oldBean.getCommentId();

                    String subKey = oldBean.getUniqueKey().split("\\|")[0];
                    String oldTitle = oldBean.getUniqueKey().split("\\|")[1];
                    String newTitle = "讨论区:" + oldTitle;
                    String newKey = subKey + "|" + newTitle;
                    System.out.println("old:" + oldCommentId + "," + oldBean.getUniqueKey() + "," + oldTitle);
                    System.out.println("new:" + newKey + "," + newTitle);
                    CommentBean newBean = commentHandler.getCommentBean(new QueryExpress().add(QueryCriterions.eq(CommentBeanField.UNI_KEY, newKey)));
                    if (newBean == null) {
                        System.out.println("update");
                        String newCommentId = com.enjoyf.util.MD5Util.Md5(newKey + domain.getCode());

                        UpdateExpress updateExpress = new UpdateExpress();
                        updateExpress.set(CommentBeanField.COMMENT_ID, newCommentId);
                        updateExpress.set(CommentBeanField.UNI_KEY, newKey);
                        updateExpress.set(CommentBeanField.TITLE, newTitle);
                        commentHandler.modifyCommentBean(updateExpress, new QueryExpress().add(QueryCriterions.eq(CommentBeanField.COMMENT_ID, oldCommentId)));
                    } else {
                        System.out.println("query");
                        List<CommentReply> oldList = commentHandler.queryCommentReply(new QueryExpress()
                                .add(QueryCriterions.eq(CommentReplyField.COMMENT_ID, oldCommentId))
                                .add(QuerySort.add(CommentReplyField.REPLY_ID, QuerySortOrder.ASC)));
                        int totals = 0;
                        int floorNum = 0;
                        int commentSum = 0;
                        for (CommentReply oldReply : oldList) {
                            if (oldReply.getRootId() == 0l) {
                                floorNum += 1;
                                totals += 1;
                            }
                            if (!oldReply.getRemoveStatus().equals(ActStatus.ACTED)) {
                                commentSum += 1;
                            }
                            System.out.println("old list:" + oldReply.getReplyId() + "," + oldReply.getRootId() + "," + oldReply.getRemoveStatus().getCode());
                        }
                        System.out.println("totals:" + totals + ",commentSum:" + commentSum + ",floorNum:" + floorNum);

                        List<CommentReply> newList = commentHandler.queryCommentReply(new QueryExpress()
                                .add(QueryCriterions.eq(CommentReplyField.COMMENT_ID, newBean.getCommentId()))
                                .add(QuerySort.add(CommentReplyField.REPLY_ID, QuerySortOrder.ASC)));
                        for (CommentReply newReply : newList) {
                            if (newReply.getRootId() == 0l) {
                                totals += 1;
                            }
                            if (!newReply.getRemoveStatus().equals(ActStatus.ACTED)) {
                                commentSum += 1;
                            }
                            System.out.println("new list:" + newReply.getReplyId() + "," + newReply.getRootId() + "," + newReply.getRemoveStatus().getCode());
                        }

                        commentHandler.modifyCommentBean(new UpdateExpress()
                                .set(CommentBeanField.TOTAL_ROWS, totals)
                                .set(CommentBeanField.COMMENT_SUM, commentSum), new QueryExpress()
                                .add(QueryCriterions.eq(CommentBeanField.COMMENT_ID, newBean.getCommentId())));

                        for (CommentReply oldReply : oldList) {
                            commentHandler.modifyCommentReply(new QueryExpress()
                                    .add(QueryCriterions.eq(CommentReplyField.REPLY_ID, oldReply.getReplyId())), new UpdateExpress().set(CommentReplyField.COMMENT_ID, newBean.getCommentId()));
                        }
                        for (CommentReply newReply : newList) {
                            if (newReply.getRootId() == 0l) {
                                floorNum = floorNum + 1;
                                commentHandler.modifyCommentReply(new QueryExpress()
                                        .add(QueryCriterions.eq(CommentReplyField.REPLY_ID, newReply.getReplyId())), new UpdateExpress().set(CommentReplyField.FLOOR_NUM, floorNum));
                                commentCache.removeCommentReply(newBean.getCommentId(), newReply.getReplyId());
                            }
                        }

                        commentCache.removeLastCommentReply(newBean.getCommentId());
                        int maxPage = totals / 5 + 1;
                        for (int i = 1; i <= maxPage; i++) {
                            commentCache.removeCommentReplyIdList(newBean.getCommentId(), 0l, i);
                        }
                        commentCache.removeCommentBean(newBean.getCommentId());
                    }
                } else {
                    System.out.println("no:" + uniKey);
                }
            }
            System.out.println("----------------------------------end---------------------------------");
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


}
