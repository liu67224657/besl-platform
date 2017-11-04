package com.enjoyf.platform.db.comment;

import com.enjoyf.platform.db.*;
import com.enjoyf.platform.db.conn.DataSourceManager;
import com.enjoyf.platform.db.conn.DataSourceProps;
import com.enjoyf.platform.db.conn.DbConnFactory;
import com.enjoyf.platform.service.comment.*;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * @Auther: <a mailto:yinpengyi@enjoyf.com>Yin Pengyi</a>
 */
public class CommentHandler {
    private DataBaseType dataBaseType;
    private String dataSourceName;

    private CommentBeanAccessor commentBeanAccessor;
    private CommentReplyAccessor commentReplyAccessor;
    private CommentHistoryAccessor commentHistoryAccessor;
    private CommentForbidAccessor commentForbidAccessor;
    private CommentVoteOptionAccessor commentVoteOptionAccessor;
    private CommentVideoAccessor commentVideoAccessor;
    private CommentPermissionAccessor commentPermissionAccessor;

    public CommentHandler(String dsn, FiveProps props) throws DbException {
        dataSourceName = dsn.toLowerCase();

        //create the catasource
        DataSourceManager.get().append(dataSourceName, props);

        dataBaseType = DataSourceProps.getDataSourceProps(dataSourceName).getDataBaseType();

        commentBeanAccessor = TableAccessorFactory.get().factoryAccessor(CommentBeanAccessor.class, dataBaseType);
        commentReplyAccessor = TableAccessorFactory.get().factoryAccessor(CommentReplyAccessor.class, dataBaseType);
        commentHistoryAccessor = TableAccessorFactory.get().factoryAccessor(CommentHistoryAccessor.class, dataBaseType);
        commentForbidAccessor = TableAccessorFactory.get().factoryAccessor(CommentForbidAccessor.class, dataBaseType);
        commentVoteOptionAccessor = TableAccessorFactory.get().factoryAccessor(CommentVoteOptionAccessor.class, dataBaseType);
        commentVideoAccessor=TableAccessorFactory.get().factoryAccessor(CommentVideoAccessor.class,dataBaseType);
        commentPermissionAccessor=TableAccessorFactory.get().factoryAccessor(CommentPermissionAccessor.class, dataBaseType);
    }


    public CommentBean createCommentBean(CommentBean commentBean) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return commentBeanAccessor.insert(commentBean, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public CommentBean getCommentBean(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return commentBeanAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<CommentBean> queryCommentBean(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return commentBeanAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<CommentBean> queryCommentBean(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<CommentBean> list = commentBeanAccessor.query(queryExpress, pagination, conn);

            PageRows<CommentBean> pageRows = new PageRows<CommentBean>();
            pageRows.setPage(pagination);
            pageRows.setRows(list);

            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<CommentBean> queryCommentBeanByPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<CommentBean> pageRows;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            pageRows = new PageRows<CommentBean>();
            List<CommentBean> list = commentBeanAccessor.query(queryExpress, pagination, conn);
            pageRows.setRows(list);
            pageRows.setPage(pagination);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifyCommentBean(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return commentBeanAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<CommentReply> queryCommentReplyByPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<CommentReply> pageRows ;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<CommentReply> list = commentReplyAccessor.query(queryExpress, pagination, conn);
            pageRows = new PageRows<CommentReply>();
            pageRows.setPage(pagination);
            pageRows.setRows(list);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<CommentReply> queryCommentReply(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return commentReplyAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public int removeCommentReply(String commentId) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return commentReplyAccessor.remove(commentId, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public CommentReply createCommentReply(CommentReply reply) throws DbException {
        Connection conn = null;
        CommentReply returnReply = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            conn.setAutoCommit(false);


            long minDisplayOrder=Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000);
//            long minDisplayOrder = commentReplyAccessor.getMinDisplayOrder(conn);
//            if (minDisplayOrder == 0l) {
//                minDisplayOrder = (long) Integer.MAX_VALUE;
//            } else {
//                minDisplayOrder = minDisplayOrder - 1l;
//            }
            reply.setDisplayOrder(minDisplayOrder);

            returnReply = commentReplyAccessor.insert(reply, conn);

            conn.commit();
        } catch (SQLException e) {
            GAlerter.lab("CommentHandler createCommentReply occur Exception.e:", e);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
        return returnReply;
    }

    public CommentReply getCommentReply(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return commentReplyAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifyCommentReply(QueryExpress queryExpress, UpdateExpress updateExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return commentReplyAccessor.update(queryExpress, updateExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public CommentHistory getCommentHistory(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return commentHistoryAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public CommentHistory createCommentHistory(CommentHistory commentHistory) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return commentHistoryAccessor.insert(commentHistory, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifyCommentHistory(QueryExpress queryExpress, UpdateExpress updateExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return commentHistoryAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public List<CommentHistory> queryCommentHistory(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return commentHistoryAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<CommentBean> queryByAvgScore(String keyWords) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return commentBeanAccessor.queryByAvgScore(keyWords, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

     //CommentForbid表 start
    public CommentForbid getCommentForbid(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return commentForbidAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public CommentForbid createCommentForbid(CommentForbid commentForbid) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return commentForbidAccessor.insert(commentForbid, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifyCommentForbid( UpdateExpress updateExpress,QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return commentForbidAccessor.update(updateExpress, queryExpress, conn)>0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public List<CommentForbid> queryCommentForbid(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return commentForbidAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<CommentForbid> queryCommentForbidByPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<CommentForbid> pageRows;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<CommentForbid> list = commentForbidAccessor.query(queryExpress, pagination, conn);
            pageRows = new PageRows<CommentForbid>();
            pageRows.setPage(pagination);
            pageRows.setRows(list);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean deleteCommentForbid(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return commentForbidAccessor.delete(queryExpress, conn)>0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


     //CommentVoteOption表 start



    public int countCommentVoteOption(QueryExpress queryExpress) throws DbException {
            Connection conn = null;
            try {
                conn = DbConnFactory.factory(dataSourceName);
                return commentVoteOptionAccessor.countCommentVoteOption(queryExpress, conn);
            } finally {
                DataBaseUtil.closeConnection(conn);
            }
        }


    public CommentVoteOption getCommentVoteOption(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return commentVoteOptionAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public CommentVoteOption createCommentVoteOption(CommentVoteOption commentVoteOption) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return commentVoteOptionAccessor.insert(commentVoteOption, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifyCommentVoteOption( UpdateExpress updateExpress,QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return commentVoteOptionAccessor.update(updateExpress, queryExpress, conn)>0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public List<CommentVoteOption> queryCommentVoteOption(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return commentVoteOptionAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<CommentVoteOption> queryCommentVoteOptionByPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<CommentVoteOption> pageRows;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<CommentVoteOption> list = commentVoteOptionAccessor.query(queryExpress, pagination, conn);
            pageRows = new PageRows<CommentVoteOption>();
            pageRows.setPage(pagination);
            pageRows.setRows(list);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean deleteCommentVoteOption(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return commentVoteOptionAccessor.delete(queryExpress, conn)>0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public boolean deleteCommentBean(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return commentBeanAccessor.delete(queryExpress, conn)>0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    /////////commentvideo
   public CommentVideo  getCommentVideo(QueryExpress queryExpress)throws DbException{
       Connection conn = null;
       try {
           conn = DbConnFactory.factory(dataSourceName);
           return commentVideoAccessor.get(queryExpress, conn);
       } finally {
           DataBaseUtil.closeConnection(conn);
       }
   }

    public CommentVideo  createCommentVideo(CommentVideo commentVideo) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return commentVideoAccessor.insert(commentVideo, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifyCommentVideo( UpdateExpress updateExpress,QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return commentVideoAccessor.update(updateExpress, queryExpress, conn)>0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public List<CommentVideo> queryCommentVideo(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return commentVideoAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<CommentVideo> queryCommentVideo(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<CommentVideo> pageRows;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<CommentVideo> list = commentVideoAccessor.query(queryExpress, pagination, conn);
            pageRows = new PageRows<CommentVideo>();
            pageRows.setPage(pagination);
            pageRows.setRows(list);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public int countCommentReply(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return commentReplyAccessor.count(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }
    
    public CommentPermission insertPermission(CommentPermission permission)throws DbException{
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return commentPermissionAccessor.insert(permission, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }
    public CommentPermission getPermission(QueryExpress queryExpress)throws DbException{
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return commentPermissionAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }
    
    public List<CommentPermission> queryPermissionList(QueryExpress queryExpress)throws DbException{
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return commentPermissionAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }
    
    public int updatePermission(UpdateExpress updateExpress,QueryExpress queryExpress)throws DbException{
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return commentPermissionAccessor.update(updateExpress, queryExpress,conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }
}
