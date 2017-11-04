package com.enjoyf.platform.db.sync;

import com.enjoyf.platform.db.AbstractSequenceBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.sync.ShareBody;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-6-4
 * Time: 下午4:06
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractShareBodyAccessor extends AbstractSequenceBaseTableAccessor<ShareBody> implements ShareBodyAccessor {
    Logger logger = LoggerFactory.getLogger(AbstractShareBodyAccessor.class);

    private static final String TABLE_NAME = "share_body";
    private static final String KEY_SEQUNECE_NAME = "SEQ_SHARE_BODY_ID";

    @Override
    public ShareBody insert(ShareBody shareBody, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        //share_body_id share_id,share_subject,share_body,share_url,removestatus,createuserid,createdate createuserip, modifyuserid, modifydate, modifyip
        try {
            shareBody.setShareBodyId(getSeqNo(KEY_SEQUNECE_NAME, conn));
            pstmt = conn.prepareStatement(getInsertSql());

            pstmt.setLong(1, shareBody.getShareBodyId());
            pstmt.setLong(2, shareBody.getShareId());
            pstmt.setString(3, shareBody.getShareSubject());
            pstmt.setString(4, shareBody.getShareBody());
            pstmt.setString(5, shareBody.getPicUrl());
            pstmt.setString(6, shareBody.getRemoveStatus().getCode());
            pstmt.setString(7, shareBody.getCreateUserId());
            pstmt.setTimestamp(8, new Timestamp(shareBody.getCreateDate() == null ? System.currentTimeMillis() : shareBody.getCreateDate().getTime()));
            pstmt.setString(9, shareBody.getCreateUserIp());
            pstmt.setString(10, shareBody.getLastModifyUserId());
            pstmt.setTimestamp(11, new Timestamp(shareBody.getLastModifyDate().getTime()));
            pstmt.setString(12, shareBody.getLastModifyUserIp());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On insert ShareBody, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return shareBody;
    }

    @Override
    public ShareBody get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<ShareBody> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<ShareBody> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress,QueryExpress queryExpress,Connection conn) throws DbException {
         return super.update(TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    protected ShareBody rsToObject(ResultSet rs) throws SQLException {
        ShareBody shareBody = new ShareBody();
        shareBody.setShareBodyId(rs.getLong("share_body_id"));
        shareBody.setShareId(rs.getLong("share_id"));
        shareBody.setShareSubject(rs.getString("share_subject"));
        shareBody.setShareBody(rs.getString("share_body"));
        shareBody.setPicUrl(rs.getString("pic_url"));
        shareBody.setRemoveStatus(ActStatus.getByCode(rs.getString("removestatus")));

        shareBody.setCreateUserId(rs.getString("createuserid"));
        shareBody.setCreateDate(new Date(rs.getTimestamp("createdate").getTime()));
        shareBody.setCreateUserIp(rs.getString("modifyip"));

        shareBody.setLastModifyUserId(rs.getString("modifyuserid"));
        shareBody.setLastModifyDate(new Date(rs.getTimestamp("modifydate").getTime()));
        shareBody.setLastModifyUserIp(rs.getString("modifyip"));

        return shareBody;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO "+ TABLE_NAME+" (share_body_id,share_id,share_subject,share_body,pic_url,removestatus,createuserid,createdate,createuserip, modifyuserid, modifydate, modifyip) VALUES(?, ?, ?, ?, ?, ? ,? ,?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("insert sql: " + sql);
        }
        return sql;
    }
}
