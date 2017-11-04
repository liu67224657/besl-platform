package com.enjoyf.platform.db.content;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.content.social.SocialContentReply;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  14-4-14 下午8:51
 * Description:
 */
public abstract class AbstractSocialContentReplyAccessor extends AbstractBaseTableAccessor<SocialContentReply> implements SocialContentReplyAccessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractSocialContentReplyAccessor.class);

    private static final String TABLE_NAME = "social_content_reply";

    @Override
    public SocialContentReply insert(SocialContentReply reply, Connection conn) throws DbException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            //reply_uno,content_id,content_uno,parent_id,parent_uno,root_id,root_uno,body,create_time,create_ip,remove_status
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, reply.getReplyUno());
            pstmt.setLong(2, reply.getContentId());
            pstmt.setString(3, reply.getContentUno());
            pstmt.setLong(4, reply.getParentId());
            pstmt.setString(5, reply.getParentUno());
            pstmt.setLong(6, reply.getRootId());
            pstmt.setString(7, reply.getRootUno());

            pstmt.setString(8, reply.getBody());

            pstmt.setTimestamp(9, new Timestamp(reply.getCreateTime() == null ? System.currentTimeMillis() : reply.getCreateTime().getTime()));
            pstmt.setString(10, reply.getCreateIp());

            pstmt.setString(11, reply.getRemoveStatus().getCode());
            pstmt.setFloat(12, reply.getLon());
            pstmt.setFloat(13, reply.getLat());

            pstmt.execute();

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                reply.setReplyId(rs.getLong(1));
            }
        } catch (SQLException e) {
            GAlerter.lab("On insert Content, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return reply;
    }

    @Override
    public List<SocialContentReply> queryByRootId(long contentId, Long rootId, Pagination page, Connection conn) throws DbException {

        //
        List<SocialContentReply> returnValue = new ArrayList<SocialContentReply>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "";
        if (rootId == null) {
            sql = "SELECT * FROM " + TABLE_NAME + " WHERE content_id=? AND remove_status=? ORDER BY reply_id ASC LIMIT ?, ?";
        } else {
            sql = "SELECT * FROM " + TABLE_NAME + " WHERE content_id=? AND root_id= ? AND remove_status=? ORDER BY reply_id ASC LIMIT ?, ?";
        }

        if (logger.isDebugEnabled()) {
            logger.debug("The query sql:" + sql);
        }

        try {
            //
            pstmt = conn.prepareStatement(sql);

            pstmt.setLong(1, contentId);
            if (rootId == null) {
                pstmt.setString(2, ActStatus.UNACT.getCode());
                pstmt.setInt(3, page.getStartRowIdx());
                pstmt.setInt(4, page.getPageSize());
            } else {
                pstmt.setLong(2, rootId);
                pstmt.setString(3, ActStatus.UNACT.getCode());
                pstmt.setInt(4, page.getStartRowIdx());
                pstmt.setInt(5, page.getPageSize());
            }
            rs = pstmt.executeQuery();

            while (rs.next()) {
                returnValue.add(rsToObject(rs));
            }
        } catch (SQLException
                e) {
            GAlerter.lab("On query, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
        return returnValue;
    }

    @Override
    public List<SocialContentReply> query
            (QueryExpress
                     queryExpress, Connection
                    conn) throws DbException {
        return super.query(TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<SocialContentReply> queryByPage
            (QueryExpress
                     queryExpress, Pagination
                    page, Connection
                    conn) throws DbException {
        return super.query(TABLE_NAME, queryExpress, page, conn);
    }

    @Override
    public int update
            (UpdateExpress
                     updateExpress, QueryExpress
                    queryExpress, Connection
                    conn) throws DbException {
        return super.update(TABLE_NAME, updateExpress, queryExpress, conn);
    }

    protected String getInsertSql() {
        String sql = "INSERT INTO " + TABLE_NAME + " (reply_uno,content_id,content_uno,parent_id,parent_uno,root_id,root_uno,body,create_time,create_ip,remove_status,lon,lat) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";

        if (logger.isDebugEnabled()) {
            logger.debug(" the insert sql:" + sql);
        }

        return sql;
    }

    @Override
    protected SocialContentReply rsToObject(ResultSet rs) throws SQLException {
        SocialContentReply reply = new SocialContentReply();

        reply.setReplyId(rs.getLong("reply_id"));
        reply.setReplyUno(rs.getString("reply_uno"));
        reply.setContentId(rs.getLong("content_id"));
        reply.setContentUno(rs.getString("content_uno"));
        reply.setParentId(rs.getLong("parent_id"));
        reply.setParentUno(rs.getString("parent_uno"));
        reply.setRootId(rs.getLong("root_id"));
        reply.setRootUno(rs.getString("root_uno"));
        reply.setBody(rs.getString("body"));
        reply.setCreateTime(rs.getTimestamp("create_time"));
        reply.setCreateIp(rs.getString("create_ip"));
        reply.setRemoveStatus(ActStatus.getByCode(rs.getString("remove_status")));

        reply.setLon(rs.getFloat("lon"));
        reply.setLat(rs.getFloat("lat"));

        return reply;
    }
}
