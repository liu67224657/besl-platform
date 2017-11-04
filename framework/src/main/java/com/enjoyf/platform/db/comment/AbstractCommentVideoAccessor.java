package com.enjoyf.platform.db.comment;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.comment.CommentVideo;
import com.enjoyf.platform.service.comment.CommentVideoType;
import com.enjoyf.platform.service.comment.CommentVideoUrl;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Date;
import java.util.List;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/4/10
 * Description:
 */
public abstract class AbstractCommentVideoAccessor extends AbstractBaseTableAccessor<CommentVideo> implements CommentVideoAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractCommentVideoAccessor.class);

    private static final String KEY_TABLE_NAME = "comment_video";

    @Override
    public CommentVideo insert(CommentVideo commentVideo, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql());
            pstmt.setString(1, commentVideo.getVideoTitle());
            pstmt.setString(2, commentVideo.getVideoDesc());
            pstmt.setString(3, commentVideo.getProfileid());
            pstmt.setString(4, commentVideo.getAppkey());
            pstmt.setString(5, commentVideo.getSdk_key());
            pstmt.setString(6, commentVideo.getJsonUrl().toJson());
            pstmt.setInt(7, commentVideo.getCommentVideoType().getCode());
            pstmt.setString(8, commentVideo.getActStatus().getCode());
            pstmt.setTimestamp(9, new Timestamp(commentVideo.getCreateTime().getTime()));
            pstmt.setString(10, commentVideo.getCreateIp());
            pstmt.setString(11,commentVideo.getVideoPic());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("On insert profile, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return commentVideo;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(video_title,video_desc,profileid,appkey,sdk_key,json_url,comment_video_type,act_status,create_time,create_ip,video_pic) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("CommentVideo insert sql" + sql);
        }
        return sql;
    }

    @Override
    public CommentVideo get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public List<CommentVideo> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<CommentVideo> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int delete(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.delete(KEY_TABLE_NAME, queryExpress, conn);
    }


    @Override
    protected CommentVideo rsToObject(ResultSet rs) throws SQLException {

        CommentVideo returnObject = new CommentVideo();

        returnObject.setCommentVideoId(rs.getLong("comment_video_id"));
        returnObject.setVideoTitle(rs.getString("video_title"));
        returnObject.setVideoDesc(rs.getString("video_desc"));
        returnObject.setProfileid(rs.getString("profileid"));
        returnObject.setAppkey(rs.getString("appkey"));
        returnObject.setSdk_key(rs.getString("sdk_key"));
        returnObject.setJsonUrl(CommentVideoUrl.parse(rs.getString("json_url")));
        returnObject.setCommentVideoType(CommentVideoType.getByCode(rs.getInt("comment_video_type")));
        returnObject.setActStatus(ActStatus.getByCode(rs.getString("act_status")));
        returnObject.setCreateTime(new Date(rs.getTimestamp("create_time").getTime()));
        returnObject.setCreateIp(rs.getString("create_ip"));
        returnObject.setVideoPic(rs.getString("video_pic"));


        return returnObject;
    }
}