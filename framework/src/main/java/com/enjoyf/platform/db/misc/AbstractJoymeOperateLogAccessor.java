package com.enjoyf.platform.db.misc;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.misc.JoymeOperateLog;
import com.enjoyf.platform.service.misc.JoymeOperateType;
import com.enjoyf.platform.util.log.GAlerter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-9-3 下午5:32
 * Description:
 */
public abstract class AbstractJoymeOperateLogAccessor extends AbstractBaseTableAccessor<JoymeOperateLog> implements JoymeOperateLogAccessor {

    private static Logger logger = LoggerFactory.getLogger(AbstractJoymeOperateLogAccessor.class);

    private static final String KEY_TABLE_NAME = "joyme_operate_log";

    @Override
    public JoymeOperateLog insert(JoymeOperateLog log, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);

            //operate_type, operate_server_id, operate_id, operate_time
            pstmt.setInt(1, log.getOperateType().getCode());
            pstmt.setString(2, log.getOperateServerId());
            pstmt.setLong(3, log.getOperateId());
            pstmt.setTimestamp(4, new Timestamp(log.getOperateTime() == null ? System.currentTimeMillis() : log.getOperateTime().getTime()));

            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                log.setOperateLogId(rs.getLong(1));
            }

            return log;
        } catch (SQLException e) {
            GAlerter.lab(this.getClass().getName() + "On insert , a SQLException occurred.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
            DataBaseUtil.closeResultSet(rs);
        }
    }

    private String getInsertSql() {
        String sqlScript = "INSERT INTO " + KEY_TABLE_NAME + "(operate_type, operate_server_id, operate_id, operate_time) VALUES (? ,?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("insert script: " + sqlScript);
        }

        return sqlScript;
    }


    @Override
    public long getMaxJoymeOperateId(String serviceId, JoymeOperateType operateType, Connection conn) throws DbException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        long returnLong = 0;
        String sql = "SELECT MAX(operate_id) FROM " + KEY_TABLE_NAME + " WHERE operate_server_id=? AND operate_type=?";
        if (logger.isDebugEnabled()) {
            logger.debug("The get sql:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, serviceId);
            pstmt.setInt(2, operateType.getCode());

            rs = pstmt.executeQuery();

            if (rs.next()) {
                returnLong = rs.getLong(1);
            }
        } catch (SQLException e) {
            GAlerter.lab("On getMaxJoymeOperateId, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
        return returnLong;
    }


    @Override
    protected JoymeOperateLog rsToObject(ResultSet rs) throws SQLException {
        return null;
    }

}
