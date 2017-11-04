package com.enjoyf.platform.tools.comment;/**
 * Created by ericliu on 16/7/29.
 */

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.comment.CommentHandler;
import com.enjoyf.platform.db.usercenter.UserCenterHandler;
import com.enjoyf.platform.serv.comment.CommentRedis;
import com.enjoyf.platform.service.comment.CommentBean;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.redis.RedisManager;
import com.enjoyf.platform.util.sql.QueryExpress;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/7/29
 */
public class UpdateCommentFolorNo {

    private static CommentHandler commentHandler;
    private static RedisManager redisManager;

    public static void main(String[] args) throws DbException {
        FiveProps servProps = Props.instance().getServProps();

        commentHandler = new CommentHandler("writeable", servProps);
        redisManager = new RedisManager(servProps);
        setFolorNum();
    }


    private static void setFolorNum() throws DbException {
//        commentHandler.queryCommentBean(new QueryExpress(), Pagination(1,1,1));

        long now = System.currentTimeMillis();
        Pagination page = new Pagination(1000, 1, 1000);


        PageRows<CommentBean> pageRows;

        do {
            try {
                pageRows = commentHandler.queryCommentBean(new QueryExpress(), page);

                //
                for (CommentBean bean : pageRows.getRows()) {
                    System.out.println("====set commentbean " + bean.getCommentId());
                    redisManager.set("commentservice_comment_reply_folornum_" + bean.getCommentId(), String.valueOf(bean.getTotalRows()));
                }
            } catch (DbException e) {
                e.printStackTrace();
            }

            if (page.isLastPage()) {
                break;
            } else {
                page.setCurPage(page.getCurPage() + 1);
            }
        } while (true);

        System.out.println("====finish import: time:" + ((System.currentTimeMillis() - now) / 1000));

    }


}
