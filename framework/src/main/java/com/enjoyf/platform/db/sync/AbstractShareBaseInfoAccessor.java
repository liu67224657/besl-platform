package com.enjoyf.platform.db.sync;

import com.enjoyf.platform.db.AbstractSequenceBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.sync.ShareBaseInfo;
import com.enjoyf.platform.service.sync.ShareRewardType;
import com.enjoyf.platform.service.sync.ShareType;
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
public abstract class AbstractShareBaseInfoAccessor extends AbstractSequenceBaseTableAccessor<ShareBaseInfo> implements ShareBaseInfoAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractShareBaseInfoAccessor.class);
    private String TABLE_NAME = "share_base_info";
    private String KEY_SEQUNECE_NAME = "SEQ_SHARE_INFO_ID";

    @Override
    public ShareBaseInfo insert(ShareBaseInfo shareInfo, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            shareInfo.setShareId(getSeqNo(KEY_SEQUNECE_NAME, conn));

            pstmt = conn.prepareStatement(getInsertSql());

            //share_id,share_key share_source_url, expire_date, share_type, share_reward_type, createuserid,createdate, createuserip,
            // removestatus, share_revward_point, share_reward_id,modifyuserid,modifydate,modifyip share_display_style
            pstmt.setLong(1, shareInfo.getShareId());
            pstmt.setString(2, shareInfo.getShareKey());
            pstmt.setString(3, shareInfo.getShareSource());
            pstmt.setTimestamp(4, shareInfo.getExpireDate() == null ? null : new Timestamp(shareInfo.getExpireDate().getTime()));
            pstmt.setInt(5, shareInfo.getShareType().getCode());
            pstmt.setInt(6, shareInfo.getShareRewardType().getValue());
            pstmt.setString(7, shareInfo.getCreateUserId());
            pstmt.setTimestamp(8, new Timestamp(shareInfo.getCreateDate() == null ? System.currentTimeMillis() : shareInfo.getCreateDate().getTime()));
            pstmt.setString(9, shareInfo.getCreateUserIp());
            pstmt.setString(10, shareInfo.getRemoveStatus().getCode());
            pstmt.setInt(11, shareInfo.getShareRewardPoint());
            pstmt.setLong(12, shareInfo.getShareRewardId());
            pstmt.setString(13, shareInfo.getLastModifyUserid());
            pstmt.setTimestamp(14, new Timestamp(shareInfo.getLastModifyDate() == null ? System.currentTimeMillis() : shareInfo.getLastModifyDate().getTime()));
            pstmt.setString(15, shareInfo.getLastModifyUserIp());

            pstmt.setString(16, shareInfo.getDisplayStyle());
            pstmt.setString(17, shareInfo.getDirectId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On insert ShareBaseInfo, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
        return shareInfo;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + TABLE_NAME + " (share_id,share_key, share_source_url, expire_date, share_type, share_reward_type, createuserid, createdate, createuserip, removestatus, share_revward_point, share_reward_id, modifyuserid, modifydate, modifyip,share_display_style,share_direct_id) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("ShareBaseInfo INSERT Script:" + sql);
        }
        return sql;
    }

    @Override
    public ShareBaseInfo get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<ShareBaseInfo> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public List<ShareBaseInfo> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(TABLE_NAME, updateExpress, queryExpress, conn);
    }

    //share_id, share_key,share_source_url, expire_date, share_type, share_reward, createuserid, createuserip, removestatus, share_revward_point, share_reward_id ,modifyuserid, modifydate, modifyip,share_direct_id
    @Override
    protected ShareBaseInfo rsToObject(ResultSet rs) throws SQLException {
        ShareBaseInfo shareInfo = new ShareBaseInfo();
        shareInfo.setShareId(rs.getLong("share_id"));
        shareInfo.setShareKey(rs.getString("share_key"));
        shareInfo.setDisplayStyle(rs.getString("share_display_style"));
        shareInfo.setDirectId(rs.getString("share_direct_id"));


        shareInfo.setShareSource(rs.getString("share_source_url"));
        if (rs.getTimestamp("expire_date") != null) {
            shareInfo.setExpireDate(new Date(rs.getTimestamp("expire_date").getTime()));
        }
        shareInfo.setShareType(ShareType.getByCode(rs.getInt("share_type")));
        shareInfo.setShareRewardType(ShareRewardType.getByValue(rs.getInt("share_reward_type")));

        shareInfo.setCreateUserId(rs.getString("createuserid"));
        shareInfo.setCreateDate(rs.getTimestamp("createdate"));
        shareInfo.setCreateUserIp(rs.getString("createuserip"));

        shareInfo.setRemoveStatus(ActStatus.getByCode(rs.getString("removestatus")));
        shareInfo.setShareRewardPoint(rs.getInt("share_revward_point"));
        shareInfo.setShareRewardId(rs.getLong("share_reward_id"));

        shareInfo.setLastModifyUserid(rs.getString("modifyuserid"));
        shareInfo.setLastModifyDate(rs.getTimestamp("modifydate"));
        shareInfo.setLastModifyUserIp(rs.getString("modifyip"));

        return shareInfo;
    }
}
