package com.enjoyf.platform.db.sync;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.sync.ShareTopic;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

import static com.enjoyf.platform.tools.gameresource.GameDataProcessor.getSeqNo;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-6-4
 * Time: 下午4:06
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractShareTopicAccessor extends AbstractBaseTableAccessor<ShareTopic> implements ShareTopicAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractShareTopicAccessor.class);

    private static final String TABLE_NAME = "share_topic";
    private static final String KEY_SEQUNECE_NAME = "SEQ_SHARE_TOPIC_ID";

    @Override
    public ShareTopic insert(ShareTopic shareTopic, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        try {
            shareTopic.setShareTopicId(getSeqNo(KEY_SEQUNECE_NAME, conn));

            pstmt = conn.prepareStatement(getInsertSql());
            pstmt.setLong(1, shareTopic.getShareTopicId());
            pstmt.setLong(2, shareTopic.getShareId());
            pstmt.setString(3, shareTopic.getShareTopic());
            pstmt.setString(4, shareTopic.getRemoveStatus().getCode());
            pstmt.setString(5, shareTopic.getCreateUserId());
            pstmt.setTimestamp(6, new Timestamp(shareTopic.getCreateDate() == null ? System.currentTimeMillis() : shareTopic.getCreateDate().getTime()));
            pstmt.setString(7, shareTopic.getCreateUserIp());
            pstmt.setString(8, shareTopic.getLastModifyUserid());
            pstmt.setTimestamp(9, new Timestamp(shareTopic.getLastModifyDate() == null ? System.currentTimeMillis() : shareTopic.getLastModifyDate().getTime()));
            pstmt.setString(10, shareTopic.getLastModifyUserIp());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On insert ShareTopic, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return shareTopic;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + TABLE_NAME + " (share_topic_id,share_id, share_topic, removestatus, createuserid, createdate, createuserip, modifyuserid, modifydate, modifyip) VALUES(?,?,?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("AppErrorInfo INSERT Script:" + sql);
        }
        return sql;
    }

    @Override
    public ShareTopic get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<ShareTopic> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<ShareTopic> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    protected ShareTopic rsToObject(ResultSet rs) throws SQLException {
        ShareTopic shareTopic = new ShareTopic();
        shareTopic.setShareTopicId(rs.getLong("share_topic_id"));
        shareTopic.setShareId(rs.getLong("share_id"));
        shareTopic.setShareTopic(rs.getString("share_topic"));
        shareTopic.setRemoveStatus(ActStatus.getByCode(rs.getString("removestatus")));
        shareTopic.setCreateUserId(rs.getString("createuserid"));
        if (rs.getTimestamp("createdate") != null) {
            shareTopic.setCreateDate(new Date(rs.getTimestamp("createdate").getTime()));
        }
        shareTopic.setCreateUserIp(rs.getString("createuserip"));
        shareTopic.setLastModifyUserid(rs.getString("modifyuserid"));
        if(rs.getTimestamp("modifydate")!=null){
            shareTopic.setLastModifyDate(new Date(rs.getTimestamp("modifydate").getTime()));
        }
        shareTopic.setLastModifyUserIp("modifyip");

        return shareTopic;
    }
}
