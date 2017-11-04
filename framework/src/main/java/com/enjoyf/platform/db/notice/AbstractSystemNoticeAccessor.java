package com.enjoyf.platform.db.notice;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.notice.SystemNotice;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Date;
import java.util.List;

public abstract class AbstractSystemNoticeAccessor extends AbstractBaseTableAccessor<SystemNotice> implements SystemNoticeAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractSystemNoticeAccessor.class);

    private static final String KEY_TABLE_NAME = "system_notice";

    @Override
    public SystemNotice insert(SystemNotice systemNotice, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, systemNotice.getText());
            pstmt.setTimestamp(2, new Timestamp(systemNotice.getCreateTime().getTime()));
            pstmt.setString(3, systemNotice.getAppkey());
            pstmt.setString(4, systemNotice.getJt());
            pstmt.setString(5, systemNotice.getJi());
            pstmt.setString(6, systemNotice.getTitle());
            pstmt.setInt(7, systemNotice.getPlatform());
            pstmt.setString(8, systemNotice.getVersion());
            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                systemNotice.setSystemNoticeId(rs.getLong(1));
            }

        } catch (SQLException e) {
            logger.error("On insert profile, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return systemNotice;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(text,create_time,appkey,jt,ji,title,platform,version) VALUES (?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("SystemNotice insert sql" + sql);
        }
        return sql;
    }

    @Override
    public SystemNotice get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public List<SystemNotice> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<SystemNotice> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int delete(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.delete(KEY_TABLE_NAME, queryExpress, conn);
    }


    @Override
    protected SystemNotice rsToObject(ResultSet rs) throws SQLException {

        SystemNotice returnObject = new SystemNotice();

        returnObject.setSystemNoticeId(rs.getLong("system_notice_id"));
        returnObject.setText(rs.getString("text"));
        returnObject.setCreateTime(new Date(rs.getTimestamp("create_time").getTime()));
        returnObject.setAppkey(rs.getString("appkey"));
        returnObject.setJt(rs.getString("jt"));
        returnObject.setJi(rs.getString("ji"));
        returnObject.setTitle(rs.getString("title"));
        returnObject.setPlatform(rs.getInt("platform"));
        returnObject.setVersion(rs.getString("version"));
//
        return returnObject;
    }
}